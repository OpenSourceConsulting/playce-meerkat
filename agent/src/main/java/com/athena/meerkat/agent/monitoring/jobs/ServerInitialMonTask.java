package com.athena.meerkat.agent.monitoring.jobs;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;

@Component
public class ServerInitialMonTask extends MonitoringTask implements InitializingBean{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerInitialMonTask.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Value("${meerkat.agent.server.id:0}")
    private String serverId;
    
		
	public ServerInitialMonTask() {
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	//@Scheduled(fixedRate = 5000)
	@Override
    public void monitor() {
    	
    	try{
    		String hostName 	= SigarUtil.getNetInfo().getHostName();
    		String osName 		= System.getProperty("os.name");
    		String osVersion 	= System.getProperty("os.version");
    		String osArch 		= System.getProperty("os.arch");
    		int cpuClockSpeed 	= SigarUtil.getCpuClock();
    		long memorySize 	= SigarUtil.getMemSize();
    		int cpuCore 		= SigarUtil.getCpuNum();
    		long diskSize = 0L;
    		
    		FileSystem[] fileSysList = SigarUtil.getFileSystemList();
    		for (FileSystem fs : fileSysList) {
    			FileSystemUsage fsu = SigarUtil.getFileSystemUsage(fs.getDirName());
    			diskSize += fsu.getTotal(); //mb
			}
			
    		String json = createJsonString(serverId, hostName, osName, osVersion, osArch, cpuClockSpeed, memorySize, cpuCore, diskSize);
    		
    		webSocketClient.sendInitMessage(json);
    		
    		LOGGER.debug("sended initial server machine data. ");
    		
    	}catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	}
    }

	
	protected String createJsonString(String serverId, String hostName, String osName, String osVersion
			,String osArch, int cpuClockSpeed, float memorySize, int cpuCore, float diskSize){
		
		return "{\"id\": "+serverId+" ,\"hostName\": \""+hostName+"\" ,\"osName\": \""+osName+"\" ,\"osName\": \""+osName+"\""
				+" ,\"osArch\": \""+osArch+"\" ,\"cpuClockSpeed\": "+cpuClockSpeed+" ,\"memorySize\": "+memorySize+" ,\"cpuCore\": "+cpuCore+""
				+" ,\"diskSize\": "+diskSize+"}";
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		monitor();
	}
    
}