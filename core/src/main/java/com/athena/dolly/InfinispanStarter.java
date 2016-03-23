/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2015. 1. 28.		First Draft.
 */
package com.athena.dolly;

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
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class InfinispanStarter {
	
	static {
	}
	
	private static void enableJmx(String port) {
		System.setProperty("com.sun.management.jmxremote", "true");
		System.setProperty("com.sun.management.jmxremote.port", port);
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
		System.setProperty("com.sun.management.jmxremote.authenticate", "false");
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args) {

        DollyConfig config = null;
        try {
            config = new DollyConfig().load();
        } catch (Exception e) {
            System.err.println("[Dolly] Configuration error : " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        
        String jmxPort = "9999";
        
        if (args.length > 0) {
        	if (args[0].matches("[-+]?\\d*\\.?\\d+")) {
        		jmxPort = args[0];
        	}
        }

		enableJmx(jmxPort);
		sun.management.jmxremote.ConnectorBootstrap.initialize();

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
            	System.out.println("[Dolly] jmx remote port : " + jmxPort);
            }

    		System.setProperty("java.net.preferIPv4Stack", "true");
    		
    		String configurationFile = null;
    		
    		if (config.getJgroupsStack().toLowerCase().equals("tcp")) {
	    		System.setProperty("jgroups.tcp.address", config.getJgroupsBindAddress());
	    		System.setProperty("jgroups.tcp.port", config.getJgroupsBindPort());
	    		System.setProperty("jgroups.tcpping.initial_hosts", config.getJgroupsInitialHosts());
	    		/*
	    		configurationFile = Thread.currentThread().getContextClassLoader().getResource("jgroups-tcp.xml").getFile();
	    		/*/
	    		configurationFile = InfinispanStarter.class.getResource("").getFile();
	    		configurationFile = configurationFile.substring(5, configurationFile.indexOf("lib/core")) + "jgroups-tcp.xml";
	    		//*/
    		} else {
	    		System.setProperty("jgroups.udp.mcast_port", config.getJgroupsMulticastPort());
	    		/*/
	    		configurationFile = Thread.currentThread().getContextClassLoader().getResource("jgroups-udp.xml").getFile();
	    		/*/
	    		configurationFile = InfinispanStarter.class.getResource("").getFile();
	    		configurationFile = configurationFile.substring(5, configurationFile.indexOf("lib/core")) + "jgroups-udp.xml";
	    		//*/
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
	}
}
//end of InfinispanStarter.java