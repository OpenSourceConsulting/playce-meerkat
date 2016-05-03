package com.athena.meerkat.agent.monitoring.jobs;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hyperic.sigar.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.athena.meerkat.agent.monitoring.utils.JSONUtil;
import com.athena.meerkat.agent.monitoring.utils.SigarUtil;
import com.athena.meerkat.agent.monitoring.websocket.StompWebSocketClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * <pre>
 * tomcat instance monitoring.
 * - instance status.
 * - jmx monitoring.
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class TInstanceMonScheduledTask extends MonitoringTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TInstanceMonScheduledTask.class);
	
	private static final String MON_FACTOR_ID_THREADS = "jmx.tomcatThreads";
	
	private static final String JMX_ATTR_HEAPMEM = "HeapMemoryUsage";
	private static final String JMX_ATTR_THREAD_POOL = "Catalina:type=ThreadPool,name=\"http-bio-";
	private static final String JMX_ATTR_THREAD_USED = "currentThreadsBusy";
	private static final String JMX_ATTR_THREAD_MAX = "maxThreads";
	
	private static final double asMB = 1024.d * 1024.d;

    
    private List<String> monDatas = new ArrayList<String>();
    
    private Map<String, JMXConnector> jmxConnMap = new HashMap<String, JMXConnector>();
    
    private ObjectName memoryObj;
    
    
    @Autowired
    private StompWebSocketClient webSocketClient;
    
		
	public TInstanceMonScheduledTask() {
	}


	@Scheduled(fixedRate = 10000)
	@Override
    public void monitor() {
    	
		monDatas.clear();
    	try{
    		ArrayNode tomcatConfigs = webSocketClient.getInstanceConfigs();//DomainTomcatConfiguration array
    		
    		if (tomcatConfigs == null || tomcatConfigs.size() == 0) {
    			LOGGER.debug("tomcat instance configs is empty. monitoring skip!");
    			return;
			}
    		
    		NetConnection[] listenPorts = SigarUtil.getListenPorts();
    		
    		for (JsonNode tomcatConfig : tomcatConfigs) {
    			
    			String tomcatInstanceId = tomcatConfig.get("tomcatInstanceId").asText();
    			long port = tomcatConfig.get("httpPort").asLong();
    			int isRun = 7; // TS_STATE common code id.
    			for (NetConnection netConnection : listenPorts) {
    				if (netConnection.getLocalPort() == port) {
    					isRun = 8;
    				}
    			}
    			
    			monDatas.add(createJmxJsonString("ti.run", tomcatInstanceId, isRun, 0));//tomcat instance running status.
    			monitorJMX(monDatas, tomcatInstanceId, tomcatConfig);
    		}
    		
    		//TODO add another jmx monitoring.
    		
    		sendInstanceMonData(monDatas);
    		
    	} catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	} finally {
    		monDatas.clear();
    	}
    }
	
	private void monitorJMX(List<String> monDatas, String tomcatInstanceId, JsonNode tomcatConfig) {
		
		String rmiRegistryPort 	= JSONUtil.getString(tomcatConfig, "rmiRegistryPort");
		String httpPort			= JSONUtil.getString(tomcatConfig, "httpPort");
		
		if (StringUtils.isEmpty(rmiRegistryPort)) {
			
			LOGGER.debug("rmi port is empty. monitoring skip.");
			return;
		}
		
		JMXConnector jmxc = jmxConnMap.get(tomcatInstanceId);
		
		try{
			if (jmxc == null) {
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:"+ rmiRegistryPort +"/jmxrmi");
				jmxc = JMXConnectorFactory.connect(url);
				LOGGER.debug("JMX connected!!");
				
				jmxConnMap.put(tomcatInstanceId, jmxc);
				
				initJmx();
			}
			
			MBeanServerConnection mbeanServerConn = jmxc.getMBeanServerConnection();

			
			monitorTomcatHeapMemory(mbeanServerConn, tomcatInstanceId);
			monitorTomcatThreads(mbeanServerConn, tomcatInstanceId, httpPort);
			
		}catch(ConnectException e){
			try{
				jmxc.close();
			}catch(IOException ex){
				//ignore.
			}
			jmxConnMap.remove(tomcatInstanceId);
			
		}catch(Exception e){
			//if (LOGGER.isDebugEnabled()) {
			//	LOGGER.error(e.toString(), e);
			//}else {
				LOGGER.error("instanceId:{} - {}", tomcatInstanceId, e.toString());
			//}
		}
		
	}
	
	private void monitorTomcatHeapMemory(MBeanServerConnection mbeanServerConn, String tomcatInstanceId) throws Exception {
		
		MemoryUsage memUsage = MemoryUsage.from( (CompositeData) mbeanServerConn.getAttribute(memoryObj, JMX_ATTR_HEAPMEM));
		
		double used = memUsage.getUsed()/asMB;//mbytes
		double max = memUsage.getMax()/asMB;//mbytes
		
		monDatas.add(createJmxJsonString("jmx." + JMX_ATTR_HEAPMEM, tomcatInstanceId, used, max));
	}
	
	private void monitorTomcatThreads(MBeanServerConnection mbeanServerConn, String tomcatInstanceId, String httpPort) throws Exception {
		
		ObjectName name = new ObjectName(JMX_ATTR_THREAD_POOL + httpPort +"\"");
		Object used = mbeanServerConn.getAttribute(name, JMX_ATTR_THREAD_USED);// currentThreadsBusy
		Object max = mbeanServerConn.getAttribute(name, JMX_ATTR_THREAD_MAX);// maxThreads
		
		monDatas.add(createJmxJsonString(MON_FACTOR_ID_THREADS, tomcatInstanceId, parseDouble(used), parseDouble(max)));
	}
	
	private void initJmx() throws Exception {
		
		if (memoryObj == null) {
			memoryObj = new ObjectName("java.lang:type=Memory");
		}
		
	}
	
	private double parseDouble(Object obj) {
		return Double.parseDouble(obj.toString());
	}
    
    
}