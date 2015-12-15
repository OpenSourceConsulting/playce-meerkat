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
package com.athena.dolly.test.client;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.ServerStatistics;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class HotrodTest {

	private RemoteCache<String, Object> cache;
	
	public HotrodTest() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Properties properties = new Properties();

		// startServer_standalone()
		properties.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222");
		
		// startServer_cluster1(), startServer_cluster2()
		//properties.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222;127.0.0.1:11322;");
		
		//properties.put("infinispan.client.hotrod.transport_factory", "org.infinispan.client.hotrod.impl.transport.tcp.TcpTransportFactory");
		//properties.put("infinispan.client.hotrod.marshaller", "org.infinispan.commons.marshall.jboss.GenericJBossMarshaller");
		//properties.put("infinispan.client.hotrod.async_executor_factory", "org.infinispan.client.hotrod.impl.async.DefaultAsyncExecutorFactory");
		
		RemoteCacheManager manager = new RemoteCacheManager(builder.withProperties(properties).build());
		
		cache = manager.getCache();
	}
	
	public synchronized void setValue(String cacheKey, Object value) {
		cache.put(cacheKey, value);
		
		// Set Expiration & Eviction like this
		//cache.put(cacheKey, value, 60, TimeUnit.SECONDS); // do expire after 60 seconds later.
		//cache.put(cacheKey, value, -1, TimeUnit.SECONDS, 5 * 60, TimeUnit.SECONDS); // no expiration & do evict after 5 minutes later.
    }
	
	@SuppressWarnings("unchecked")
	public synchronized void setValue(String cacheKey, String dataKey, Object value) {
		if (dataKey != null) {
	    	Map<String, Object> attribute = (Map<String, Object>)cache.get(cacheKey);
			
			if (attribute == null) {
				attribute = new ConcurrentHashMap<String, Object>();
			}
			
			attribute.put(dataKey, value);
			cache.put(cacheKey, attribute);
		}
	}
	
	public synchronized void replaceValue(String cacheKey, Object value) {
		cache.replace(cacheKey, value);
    }
	
	public Object getValue(String cacheKey) {
    	return cache.get(cacheKey);
    }
	
	@SuppressWarnings("unchecked")
	public Object getValue(String cacheKey, String dataKey) {
    	Map<String, Object> attribute = (Map<String, Object>)cache.get(cacheKey);
		
		if (attribute == null) {
			return null;
		} else {
			return attribute.get(dataKey);
		}
	}
	
	public synchronized void removeValue(String cacheKey) {
		cache.remove(cacheKey);
    }
	
    @SuppressWarnings("unchecked")
	public synchronized void removeValue(String cacheKey, String dataKey) {
    	Map<String, Object> attribute = (Map<String, Object>)cache.get(cacheKey);
		
		if (attribute != null) {
			attribute.remove(dataKey);
		}
		
		cache.put(cacheKey, attribute);
    }
	
	public synchronized void clear() {
		cache.clear();
    }
	
	public void printStats() {
	    System.out.println("ProtocolVersion : " + cache.getProtocolVersion());
	    System.out.println("Version : " + cache.getVersion());
	    System.out.println("isEmpty : " + cache.isEmpty());
	    System.out.println("Size : " + cache.size());
	    
		ServerStatistics statistics = cache.stats();
		System.out.println("TimeSinceStart : " + statistics.getStatistic(ServerStatistics.TIME_SINCE_START));
		System.out.println("CurrentNumberOfEntries : " + statistics.getStatistic(ServerStatistics.CURRENT_NR_OF_ENTRIES));
		System.out.println("TotalNumberOfEntries : " + statistics.getStatistic(ServerStatistics.TOTAL_NR_OF_ENTRIES));
		System.out.println("Stores : " + statistics.getStatistic(ServerStatistics.STORES));
		System.out.println("Retrievals : " + statistics.getStatistic(ServerStatistics.RETRIEVALS));
		System.out.println("Hits : " + statistics.getStatistic(ServerStatistics.HITS));
		System.out.println("Misses : " + statistics.getStatistic(ServerStatistics.MISSES));
		System.out.println("RemoveHits : " + statistics.getStatistic(ServerStatistics.REMOVE_HITS));
		System.out.println("RemoveMisses : " + statistics.getStatistic(ServerStatistics.REMOVE_MISSES));
	}
    
	public void printAllCache() {
	    Enumeration<String> cacheKeys = Collections.enumeration(cache.getBulk().keySet());
	    String cacheKey = null;
    	Object data = null;
	    int i = 1;
	    while (cacheKeys.hasMoreElements()) {
	    	cacheKey = cacheKeys.nextElement();
    		System.out.println("================== Element index [" + i + "] ==================");
    		System.out.println("Cache Key : " + cacheKey);
	    	
	    	data = cache.get(cacheKey);
	    	
	    	if (data != null) {
    			System.out.println("Cache Data : " + data.toString());
	    	} else {
	    		System.out.println("Cache Data is NULL.");
	    	}
    		
    		System.out.println("");
    		i++;  
	    }
    }
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args) {
		HotrodTest test = new HotrodTest();
		
		String cacheKey = UUID.randomUUID().toString();
		
		test.setValue(cacheKey, "test-key1", "test-value11");
		test.setValue(cacheKey, "test-key2", "test-value22");
		test.setValue(cacheKey, "test-key3", "test-value33");

		test.printStats();
		test.printAllCache();

		System.out.println("\n========================================================\n");
		
		test.setValue(cacheKey, "test-key1", "111111111111");
		test.setValue(cacheKey, "test-key2", "222222222222");
		test.setValue(cacheKey, "test-key3", "333333333333");
		
		try {
			SampleDto1 sample1 = null;
			for (int i = 0; i < 2; i++) {
				sample1 = new SampleDto1();
				sample1.setAddress1("Address1_" + i);
				sample1.setAddress2("Address2_" + i);
				sample1.setAge(25 + (i % 20));
				sample1.setEmail("support_" + i + "@osci.kr");
				sample1.setFirstName("Firstname-" + i);
				sample1.setLastName("Lastname-" + i);
				sample1.setUserId("user_" + i);
				test.setValue(cacheKey, "sampleDto1-" + i, sample1);
			}
	
			test.printStats();
			test.printAllCache();

			sample1 = (SampleDto1) test.getValue(cacheKey, "sampleDto1-1");
			System.out.println(sample1.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\n========================================================\n");

		test.removeValue(cacheKey, "test-key1");
		test.removeValue(cacheKey, "test-key2");
		test.removeValue(cacheKey, "test-key3");

		try {
			SampleDto2 sample2 = null;
			for (int i = 1; i < 11; i++) {
				sample2 = new SampleDto2();
				sample2.setAddress1("Address1_" + i);
				sample2.setAddress2("Address2_" + i);
				sample2.setAge(25 + (i % 20));
				sample2.setEmail("support_" + i + "@osci.kr");
				sample2.setFirstName("Firstname-" + i);
				sample2.setLastName("Lastname-" + i);
				sample2.setUserId("user_" + i);
				test.setValue(cacheKey, "sampleDto2-" + i, sample2);
			}
	
			test.printStats();
			test.printAllCache();
			
			sample2 = (SampleDto2) test.getValue(cacheKey, "sampleDto2-1");
			System.out.println(sample2.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		test.clear();
	}

}
//end of HotrodTest.java