/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 12. 5.		First Draft.
 */
package com.athena.dolly.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.security.ProtectionDomain;
import java.util.List;

import org.infinispan.commons.equivalence.ByteArrayEquivalence;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

import com.athena.meerkat.common.cache.DollyConfig;


/**
 * <pre>
 * premain() 메소드를 갖는 javaagent 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyAgent implements ClassFileTransformer {
	
	public static final String TRANSFORMER_CLASS = "com.athena.dolly.core.DollyClassTransformer";
    private ClassFileTransformer transformer;
    
    /**
     * <pre>
     * 기본 생성자로써 DollyConfig을 이용한 프로퍼티 로드 및 DollyClassTransformer의 인스턴스를 생성한다.
     * </pre>
     */
    public DollyAgent() {
    	System.out.println("[Dolly] Dolly agent activated.");

        DollyConfig config = null;
        try {
            config = new DollyConfig().load();
        } catch (Exception e) {
            System.err.println("[Dolly] Configuration error : " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }

        boolean verbose = config.isVerbose();
        
        if (config.getClientType().equals("infinispan") && config.isUseEmbedded()) {
        	System.out.println("[Dolly] Embedded infinispan hotrod cache server will be start.");
        	
            if (verbose) {
            	System.out.println("[Dolly] hotrod host : " + config.getHotrodHost());
            	System.out.println("[Dolly] hotrod port : " + config.getHotrodPort());
            	System.out.println("[Dolly] jgroups stack : " + config.getJgroupsStack());
            	System.out.println("[Dolly] jgroups bind address : " + config.getJgroupsBindAddress());
            	System.out.println("[Dolly] jgroups bind port : " + config.getJgroupsBindPort());
            	System.out.println("[Dolly] jgroups initial hosts : " + config.getJgroupsInitialHosts());
            	System.out.println("[Dolly] jgroups multicast port : " + config.getJgroupsMulticastPort());
            }

    		System.setProperty("java.net.preferIPv4Stack", "true");
    		
    		String configurationFile = null;
    		
    		if (config.getJgroupsStack().toLowerCase().equals("tcp")) {
	    		System.setProperty("jgroups.tcp.address", config.getJgroupsBindAddress());
	    		System.setProperty("jgroups.tcp.port", config.getJgroupsBindPort());
	    		System.setProperty("jgroups.tcpping.initial_hosts", config.getJgroupsInitialHosts());
	    		//configurationFile = Thread.currentThread().getContextClassLoader().getResource("jgroups-tcp.xml").getFile();
	    		//configurationFile = this.getClass().getResource("jgroups-tcp.xml").getFile();
	    		configurationFile = this.getClass().getResource("").getFile();
	    		configurationFile = configurationFile.substring(5, configurationFile.indexOf("lib/core")) + "jgroups-tcp.xml";
    		} else {
	    		System.setProperty("jgroups.udp.mcast_port", config.getJgroupsMulticastPort());
	    		//configurationFile = Thread.currentThread().getContextClassLoader().getResource("jgroups-udp.xml").getFile();
	    		//configurationFile = this.getClass().getResource("jgroups-udp.xml").getFile();
	    		configurationFile = this.getClass().getResource("").getFile();
	    		configurationFile = configurationFile.substring(5, configurationFile.indexOf("lib/core")) + "jgroups-udp.xml";
    		}
    		
			if (verbose) {
				System.out.println("[Dolly] hotrod configurationFile : " + configurationFile);
			}

			HotRodServerConfiguration hotrodConfig = new HotRodServerConfigurationBuilder()
					.host(config.getHotrodHost())
					.port(config.getHotrodPort())
					.workerThreads(160)
					.topologyStateTransfer(true).topologyReplTimeout(5000L).topologyLockTimeout(1000L)
					.build();
    		
        	GlobalConfiguration globalConfig = new GlobalConfigurationBuilder().transport().defaultTransport()
        	        .clusterName("dolly-cluster")
        	        .addProperty("configurationFile", configurationFile)
        	        //.machineId("dolly-machine-1").rackId("dolly-rack-1").siteId("dolly-site-1")
        	        .globalJmxStatistics().enable()
        	        .build();
        	
        	Configuration conf = new ConfigurationBuilder()
    	    	  	.clustering()
    	    	  		.cacheMode(CacheMode.DIST_SYNC)
    	    	    .sync()
    		    	.l1().lifespan(60000L)
    	    	    .hash().numOwners(5)
    	    	    .jmxStatistics().enable()
    	    	    .eviction()
    	    	    	.strategy(EvictionStrategy.LRU)
    	    	    	.maxEntries(8192)
    	    	    .persistence()
    	    	    	.passivation(true)
    	    	    	.addSingleFileStore()
    	    	    		.preload(true)
    	    	    		.shared(true)
    	    	    		.fetchPersistentState(true)
    	    	    		.ignoreModifications(false)
    	    	    		.purgeOnStartup(false)
    	    	    		.location(System.getProperty("java.io.tmpdir"))
    	    	    .dataContainer().keyEquivalence(ByteArrayEquivalence.INSTANCE).valueEquivalence(ByteArrayEquivalence.INSTANCE)
    	    	    //.expiration().lifespan(60L, TimeUnit.SECONDS)
    	    	    //.compatibility().enable()
    	    	    .build();
        	
        	new HotRodServer().start(hotrodConfig, new DefaultCacheManager(globalConfig, conf));
        }
        
        if (verbose) {
            System.out.println("[Dolly] Target classes :");
            
            List<String> classList = config.getClassList();
            for (String className : classList) {
            	System.out.println("   - " + className);
            }
        }

        try {
            transformer = instanciateTransformer(TRANSFORMER_CLASS, verbose, config.getClassList(), config.isEnableSSO(), config.getSsoDomainList(), config.getSsoParamKey());
        } catch (Exception e) {
            System.err.println("[Dolly] Initialization error : " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }//end of constructor()

    /**
     * <pre>
     * -javaagent 옵션으로 저징된 jar 파일의 /META-INF/MANIFEST.MF 파일 내에 Premain-Class 항목으로 지정된 경우 수행되는 메소드로써
     * class loading이 되기 전에 수행된다.
     * </pre>
     * @param agentArguments
     * @param instrumentation
     */
    public static void premain(String agentArguments, Instrumentation instrumentation) {
        instrumentation.addTransformer(new DollyAgent());
    }//end of premain()

	/* (non-Javadoc)
	 * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
	 */
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		return transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
	}//end of transform()
	
    /**
     * <pre>
     * DollyClassTransformer 클래스의 인스턴스를 생성한다.
     * </pre>
     * @param className
     * @param verbose
     * @param classList
     * @param enableSSO
     * @param ssoDomainList
     * @param ssoParamKey
     * @return
     * @throws Exception
     */
    private ClassFileTransformer instanciateTransformer(String className, boolean verbose, List<String> classList, boolean enableSSO, List<String> ssoDomainList, String ssoParamKey) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> clazzConstructor = clazz.getConstructor(new Class[]{Boolean.TYPE, List.class, Boolean.TYPE, List.class, String.class});
        return (ClassFileTransformer) clazzConstructor.newInstance(verbose, classList, enableSSO, ssoDomainList, ssoParamKey);
    }//end of instanciateTransformer()
}
//end of DollyAgent.java