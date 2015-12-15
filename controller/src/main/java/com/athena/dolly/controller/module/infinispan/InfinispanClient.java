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
 * Sang-cheon Park	2014. 3. 31.		First Draft.
 */
package com.athena.dolly.controller.module.infinispan;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.dolly.controller.module.DollyClient;
import com.athena.dolly.controller.module.vo.MemoryVo;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class InfinispanClient extends DollyClient {
	
    private static final Logger logger = LoggerFactory.getLogger(InfinispanClient.class);
	
	private static final int RETRY_CNT = 3;
	private static final String URL_PREFIX = "service:jmx:remoting-jmx://";
	private static final String EMBEDDED_URL_PREFIX = "service:jmx:rmi:///jndi/rmi://";

	private String connectionUrl;
	private String user;
	private String passwd;
	private boolean embedded;
	private JMXConnector jmxConnector;
	
	public InfinispanClient(String connectionUrl, String user, String passwd, boolean embedded) {
		this.connectionUrl = connectionUrl;
		this.user = user;
		this.passwd = passwd;
		this.embedded = embedded;
		
		init();
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public void init() {
        Map<String, Object> environment = new HashMap<String, Object>();
        
        if (user != null && passwd != null) {
        	environment.put(JMXConnector.CREDENTIALS, new String[] {user, passwd});
        }
        environment.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
        environment.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_STARTTLS", false);
        
		try {
			JMXServiceURL serviceURL = null;
			if (embedded) {
				serviceURL = new JMXServiceURL(EMBEDDED_URL_PREFIX + connectionUrl + "/jmxrmi");
			} else {
				serviceURL = new JMXServiceURL(URL_PREFIX + connectionUrl);
			}

	        int cnt = 0;
	        while(cnt++ < RETRY_CNT) {
		        try {
		        	this.jmxConnector = JMXConnectorFactory.connect(serviceURL, environment);
		        	break;
		        } catch(Exception e) {
		        	logger.error("Unhandled exception has occurred. : ", e);
		        }
	        }
		} catch (MalformedURLException e) {
        	logger.error("MalformedURLException has occurred. : ", e);
		}
	}//end of init()

	@Override
	public MemoryVo getMemoryUsage() {
		MemoryVo memory = null;
		
		try {
			ObjectName objectName=new ObjectName("java.lang:type=Memory");			
			
			MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
			CompositeDataSupport heapMemoryUsage = (CompositeDataSupport)connection.getAttribute(objectName, "HeapMemoryUsage");
			
			memory = new MemoryVo();
	        memory.setCommitted((Long)heapMemoryUsage.get("committed"));
	        memory.setInit((Long)heapMemoryUsage.get("init"));
	        memory.setMax((Long)heapMemoryUsage.get("max"));
	        memory.setUsed((Long)heapMemoryUsage.get("used"));
	        
        	logger.debug("nodeName: [{}], memoryUsage: [committed: {}, init:{}, max:{}, used:{}]", new Object[]{nodeName, memory.getCommitted(), memory.getInit(), memory.getMax(), memory.getUsed()});
		} catch (Exception e) {
			logger.error("unhandled exception has errored : ", e);
		}
		
		return memory;
	}

	@Override
	public String getCpuUsage() {
		String cpuUsageStr = null;
		
		try {
			MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
	    	
			ObjectName osObjectName = new ObjectName("java.lang:type=OperatingSystem");			
			ObjectName runTimeObjectName = new ObjectName("java.lang:type=Runtime");			

			//before Cpu
			int availableProcessors = (Integer)connection.getAttribute(osObjectName, "AvailableProcessors");
		    long prevUpTime = (Long) connection.getAttribute(runTimeObjectName, "Uptime");
		    long prevProcessCpuTime = (Long) connection.getAttribute(osObjectName, "ProcessCpuTime");
		    
		    try  {
		        Thread.sleep(1000);
		    } catch (Exception ignored) { 
		    	// ignore
		    }
		    
			//after Cpu
		    long upTime = (Long) connection.getAttribute(runTimeObjectName, "Uptime");
		    long processCpuTime = (Long) connection.getAttribute(osObjectName, "ProcessCpuTime");

		    long elapsedCpu = processCpuTime - prevProcessCpuTime;
		    long elapsedTime = upTime - prevUpTime;		    
		    
		    double cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
		    cpuUsageStr = String.format("%.2f", cpuUsage);
        	logger.debug("nodeName: [{}], cpuUsage: [{}]", nodeName, cpuUsageStr);
		} catch (Exception e) {
			logger.error("unhandled exception has errored : ", e);
		}
		
		return cpuUsageStr;
	}
}
//end of JmxClient.java