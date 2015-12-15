/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 12. 12.		First Draft.
 */
package com.athena.dolly.test.server;

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

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class EmbeddedServer {
	
	static {
		// Set VM Arguments to enable JMX
		// -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
		
		enableJmx("9999");
		sun.management.jmxremote.ConnectorBootstrap.initialize();
	}
	
	public EmbeddedServer() {
		//*
		startServer_standalone();
		/*/
		startServer_cluster1();
		startServer_cluster2();
		//*/
	}
	
	public void startServer_standalone() {
//		System.setProperty("java.net.preferIPv4Stack", "true");
//		System.setProperty("jboss.modules.system.pkgs", "org.jboss.byteman");
//		System.setProperty("java.awt.headless", "true");
		
		HotRodServerConfiguration hotrodConfig = new HotRodServerConfigurationBuilder()
				.host("127.0.0.1")
				.port(11222)
				.workerThreads(100)
				//.topologyStateTransfer(true).topologyReplTimeout(5000L).topologyLockTimeout(1000L)
				.build();
		
		GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
		        .nonClusteredDefault()
				.globalJmxStatistics().enable()
				//.globalJmxStatistics().jmxDomain("infinispan-" + UUID.randomUUID())
		        .build();
		
		Configuration config = new org.infinispan.configuration.cache.ConfigurationBuilder()
				//.indexing().enable()
			  	.clustering().cacheMode(CacheMode.LOCAL)
			  	.sync()
			    .jmxStatistics().enable()
	    	    //.compatibility().enable()
	    	    .dataContainer().keyEquivalence(ByteArrayEquivalence.INSTANCE).valueEquivalence(ByteArrayEquivalence.INSTANCE)
			    .build();
		
		DefaultCacheManager cacheManager = new DefaultCacheManager(globalConfig, config, true);
		
		new HotRodServer().start(hotrodConfig, cacheManager);
	}

	public void startServer_cluster1() {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("jgroups.tcp.address", "127.0.0.1");
		System.setProperty("jgroups.tcp.port", "7800");
		System.setProperty("jgroups.tcpping.initial_hosts", "127.0.0.1[7800],127.0.0.1[7801]");
		
		HotRodServerConfiguration hotrodConfig = new HotRodServerConfigurationBuilder()
				.host("127.0.0.1")
				.port(11222)
				.workerThreads(100)
				.build();

    	GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
    			.transport().defaultTransport()
    	        .clusterName("dolly-cluster")
    	        .addProperty("configurationFile", EmbeddedServer.class.getResource("jgroups-tcp.xml").getFile())
    	        //.addProperty("configurationFile", EmbeddedServer.class.getResource("jgroups-udp.xml").getFile())
    	        //.machineId("dolly-machine-1").rackId("dolly-rack-1").siteId("dolly-site-1")
    	        .globalJmxStatistics().enable()
    	        .allowDuplicateDomains(true)
    	        .build();
    	
    	Configuration config = new ConfigurationBuilder()
	    	  	.clustering()
	    	  		.cacheMode(CacheMode.DIST_SYNC)
					//.hash().numOwners(5).stateTransfer().fetchInMemoryState(false)
				.sync()
	    	    .jmxStatistics()
	    	    	.enable()
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
	    	    //.compatibility().enable()
	    	    //.dataContainer().keyEquivalence(new AnyServerEquivalence()).valueEquivalence(new AnyServerEquivalence())
	    	    //.l1().lifespan(60000L)
	    	    //.expiration().lifespan(60L, TimeUnit.SECONDS)
	    	    //.storeAsBinary().enable()
	    	    .build();
		
		new HotRodServer().start(hotrodConfig, new DefaultCacheManager(globalConfig, config));
	}

	public void startServer_cluster2() {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("jgroups.tcp.address", "127.0.0.1");
		System.setProperty("jgroups.tcp.port", "7801");
		System.setProperty("jgroups.tcpping.initial_hosts", "127.0.0.1[7800],127.0.0.1[7801]");
		
		HotRodServerConfiguration hotrodConfig = new HotRodServerConfigurationBuilder()
				.host("127.0.0.1")
				.port(11322)
				.workerThreads(100)
				.topologyStateTransfer(true).topologyReplTimeout(5000L).topologyLockTimeout(1000L)
				.build();

    	GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
    			.transport().defaultTransport()
    	        .clusterName("dolly-cluster")
    	        .addProperty("configurationFile", EmbeddedServer.class.getResource("jgroups-tcp.xml").getFile())
    	        //.addProperty("configurationFile", EmbeddedServer.class.getResource("jgroups-udp.xml").getFile())
    	        //.machineId("dolly-machine-1").rackId("dolly-rack-1").siteId("dolly-site-1")
    	        .globalJmxStatistics().enable()
    	        .allowDuplicateDomains(true)
    	        .build();
    	
    	Configuration config = new ConfigurationBuilder()
	    	  	.clustering()
	    	  		.cacheMode(CacheMode.DIST_SYNC)
					//.hash().numOwners(5).stateTransfer().fetchInMemoryState(false)
	    	  	.sync()
	    	    .jmxStatistics()
	    	    	.enable()
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
	    	    //.compatibility().enable()
	    	    //.dataContainer().keyEquivalence(new AnyServerEquivalence()).valueEquivalence(new AnyServerEquivalence())
	    	    //.l1().lifespan(60000L)
	    	    //.expiration().lifespan(60L, TimeUnit.SECONDS)
	    	    //.storeAsBinary().enable()
	    	    .build();
		
		new HotRodServer().start(hotrodConfig, new DefaultCacheManager(globalConfig, config));
	}
	
	private static void enableJmx(String port) {
		System.setProperty("com.sun.management.jmxremote", "true");
		System.setProperty("com.sun.management.jmxremote.port", port);
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
		System.setProperty("com.sun.management.jmxremote.authenticate", "false");
	}

	public static void main(String[] args) {
		new EmbeddedServer();
	}

}
//end of EmbeddedServer.java