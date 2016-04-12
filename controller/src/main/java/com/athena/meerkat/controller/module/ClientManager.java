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
 * Sang-cheon Park	2015. 1. 6.		First Draft.
 */
package com.athena.meerkat.controller.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.cache.DollyConfig;
import com.athena.meerkat.common.exception.ConfigurationException;
import com.athena.meerkat.controller.module.couchbase.CouchbaseClient;
import com.athena.meerkat.controller.module.infinispan.InfinispanClient;
import com.athena.meerkat.controller.module.vo.DesignDocumentVo;
import com.athena.meerkat.controller.module.vo.MemoryVo;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
//@Component
public class ClientManager implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);

	private DollyConfig config;
	private static String cacheType;
	private static TreeMap<String, MeerkatClient> dollyClientMap;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (DollyConfig.properties == null || config == null) {
    		try {
                config = new DollyConfig().load();
			} catch (ConfigurationException e) {
				logger.error("[Dolly] Configuration error : ", e);
			}
    	}

		dollyClientMap = new TreeMap<String, MeerkatClient>();
		MeerkatClient client = null;
		
		cacheType = config.getClientType();
		
		if (cacheType.equals("infinispan")) {
			boolean embedded = config.isEmbedded();
			String[] jmxServers = config.getJmxServers();
			String[] users = config.getUsers();
			String[] passwds = config.getPasswds();
			
			for (int i = 0; i < jmxServers.length; i++) {
				if (users != null && users.length == jmxServers.length &&
						passwds != null && passwds.length == jmxServers.length) {
					client = new InfinispanClient(jmxServers[i], users[i], passwds[i], embedded);
				} else {
					client = new InfinispanClient(jmxServers[i], null, null, embedded);
				}
				client.setNodeName("node-"+i);
				dollyClientMap.put(i + "", client);
			}
			
			logger.debug("JMX Client Info : [{}]" + dollyClientMap);
		} else if (cacheType.equals("couchbase")) {
			String[] urls = config.getCouchbaseUris().split(";");
			String name = config.getCouchbaseBucketName();
			String passwd = config.getCouchbaseBucketPasswd();
			
			for (int i = 0; i < urls.length; i++) {
				client = new CouchbaseClient(urls[i], name, passwd);
				client.setNodeName("node-"+i);
				dollyClientMap.put(i + "", client);
			}
		}
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param nodeName
	 * @return
	 */
	public static MeerkatClient getClient(String nodeName) {
		return dollyClientMap.get(nodeName);
	}//end of getClient()

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	public static String getServerType() {
		return cacheType;
	}//end of getServerType()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	public static List<String> getServerList() {
		return new ArrayList<String>(dollyClientMap.keySet());
	}//end of getServerList()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param nodeName
	 * @return
	 */
	public static boolean isValidNodeName(String nodeName) {
		return dollyClientMap.containsKey(nodeName);
	}//end of isValidNodeName()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param nodeName
	 * @return
	 */
	public static MemoryVo getMemoryUsage(String nodeName) {
        return dollyClientMap.get(nodeName).getMemoryUsage();
	}//end of getMemoryUsage()
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param nodeName
	 * @return
	 */
	public static String getCpuUsage(String nodeName) {
		return dollyClientMap.get(nodeName).getCpuUsage();
	}//end of getCpuUsage()

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	public static List<MemoryVo> getMemoryUsageList() {
		if (cacheType.equals("couchbase")) {
			return ((CouchbaseClient)dollyClientMap.get(dollyClientMap.firstKey())).getMemoryUsageList();
		} else {
			List<MemoryVo> memoryList = new ArrayList<MemoryVo>();
			List<String> nodeList = getServerList();
			
			MemoryVo memory = null;
			for (String nodeName : nodeList) {
				memory = getMemoryUsage(nodeName);
				memoryList.add(memory);
			}
			
			return memoryList;
		}
	}//end of getMemoryUsageList()

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	public static List<String> getCpuUsageList() {
		if (cacheType.equals("couchbase")) {
			return ((CouchbaseClient)dollyClientMap.get(dollyClientMap.firstKey())).getCpuUsageList();
		} else {
			Map<String, String> cpuMap = new TreeMap<String, String>();
			List<String> cpuList = new ArrayList<String>();
			List<String> nodeList = getServerList();
			
			for (String nodeName : nodeList) {
				new CpuInfo(nodeName, cpuMap).start();
			}
	
			try {
				Thread.sleep(1500);
			} catch (Exception e) {
				//ignore
			}
			
			for (String nodeName : nodeList) {
				cpuList.add(cpuMap.get(nodeName));
			}
			
			return cpuList;
		}
	}//end of getCpuUsageList()
	
	public static List<DesignDocumentVo> getDesigndocs(String nodeName) {
		return ((CouchbaseClient)dollyClientMap.get(nodeName)).getDesigndocs();
	}

	public static String createView(String nodeName, String docName, String viewName) {
		return ((CouchbaseClient)dollyClientMap.get(nodeName)).createView(docName, viewName);
	}

	public static Boolean updateView(String nodeName, DesignDocumentVo designDoc) {
		return ((CouchbaseClient)dollyClientMap.get(nodeName)).updateView(designDoc);
	}

	public static Boolean deleteDesignDoc(String nodeName, String docName) {
		return ((CouchbaseClient)dollyClientMap.get(nodeName)).deleteDesignDoc(docName);
	}

	public static Boolean deleteView(String nodeName, String docName, String viewName) {
		return ((CouchbaseClient)dollyClientMap.get(nodeName)).deleteView(docName, viewName);
	}

	/*
	public static OperationgSystemVo getOperatingSystemUsage(String nodeName) {
		OperationgSystemVo osVo = null;
		
		InfinispanClient client = (InfinispanClient)dollyClientMap.get(nodeName);
		if (client != null) {
			try {
				ObjectName objectName=new ObjectName("java.lang:type=OperatingSystem");			
				
				MBeanServerConnection connection = client.getJmxConnector().getMBeanServerConnection();
				
				osVo = new OperationgSystemVo();
	
				osVo.setName((String) connection.getAttribute(objectName, "Name"));
				osVo.setVersion((String) connection.getAttribute(objectName, "Version"));
				osVo.setArch((String) connection.getAttribute(objectName, "Arch"));
				osVo.setSystemLoadAverage((Double) connection.getAttribute(objectName, "SystemLoadAverage"));
				osVo.setAvailableProcessors((Integer)connection.getAttribute(objectName, "AvailableProcessors"));
				
				osVo.setFreePhysicalMemory((Long) connection.getAttribute(objectName, "FreePhysicalMemorySize"));
				osVo.setFreeSwapSpaceSize((Long) connection.getAttribute(objectName, "FreeSwapSpaceSize"));
				osVo.setProcessCpuTime((Long) connection.getAttribute(objectName, "ProcessCpuTime"));
				osVo.setCommittedVirtualMemorySize((Long) connection.getAttribute(objectName, "CommittedVirtualMemorySize"));
				osVo.setTotalPhysicalMemorySize((Long) connection.getAttribute(objectName, "TotalPhysicalMemorySize"));
				osVo.setTotalSwapSpaceSize((Long) connection.getAttribute(objectName, "TotalSwapSpaceSize"));
				
			} catch (Exception e) {
				logger.error("unhandled exception has errored : ", e);
			}
		}
        
        return osVo;
	}
	
	public static HashMap<String,Object> getObjectNameInfo(ObjectName objName, String nodeName) {
		InfinispanClient client = (InfinispanClient)dollyClientMap.get(nodeName);
		if (client != null) {
			try {
				MBeanServerConnection connection = client.getJmxConnector().getMBeanServerConnection();
			    HashMap<String, Object> infoMap = new HashMap<String,Object>();
				Set<ObjectName> names = new TreeSet<ObjectName>(connection.queryNames(objName, null));
					
				for (ObjectName name : names) {
					logger.info("#######################");
					logger.info("\tObjectName = " + name);
	
					MBeanInfo info = connection.getMBeanInfo(name);
			        MBeanAttributeInfo[] attributes = info.getAttributes();
			        
			        for (MBeanAttributeInfo attr : attributes) {
			        	logger.info("==========================");
			        	logger.info("attrName = " + attr.getName());
			        	logger.info("attrType = " + attr.getType());
			        	logger.info("connection.getAttribute = " + connection.getAttribute(name, attr.getName()));
	
			        	infoMap.put(attr.getName(), connection.getAttribute(name, attr.getName()));
					}
				}		    
			    
			    return infoMap;
			} catch (Exception e) {
				logger.error("unhandled exception has errored : ", e);
			}
		}
		
		return null;
	}

	public static HashMap<String,Object> getCacheStatisticsInfo(String nodeName){
		try {
			ObjectName cacheStatisticsInfo = new ObjectName("jboss.infinispan:type=Cache,name=\"default(dist_sync)\",manager=\"clustered\",component=Statistics");	
			HashMap <String, Object> cacheMap = getObjectNameInfo(cacheStatisticsInfo , nodeName);
			return cacheMap;
		} catch (Exception e) {
			logger.error("unhandled exception has errored : ", e);
		}
		
		return null;
	}
	
	public static HashMap<String,Object> getCacheManagerInfo(String nodeName){
		try {
			ObjectName cacheManagerObjectName = new ObjectName("jboss.infinispan:type=CacheManager,name=\"clustered\",component=CacheManager");	
			HashMap <String, Object> cacheManagerMap = getObjectNameInfo(cacheManagerObjectName , nodeName);
			return cacheManagerMap;
		} catch (Exception e) {
			logger.error("unhandled exception has errored : ", e);
		}
		
		return null;
	}
	*/
}
//end of ClientManager.java

class CpuInfo extends Thread {
	
	private String nodeName;
	private Map<String, String> cpuMap;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public CpuInfo(String nodeName, Map<String, String> cpuMap) {
		this.nodeName = nodeName;
		this.cpuMap = cpuMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		cpuMap.put(nodeName, ClientManager.getCpuUsage(nodeName));
	}
	
}