package com.athena.meerkat.agent.monitoring.jobs;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.agent.monitoring.utils.SigarUtil;

/**
 * <pre>
 * Server Filesystem monitoring.
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class ServerFSMonScheduledTask extends MonitoringTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerFSMonScheduledTask.class);

    
    @Value("${meerkat.agent.server.id:0}")
    private String serverId;
    
    private List<String> monDatas = new ArrayList<String>();
    
    
	public ServerFSMonScheduledTask() {
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Scheduled(fixedRate = 60000)
	@Override
    public void monitor() {
    	
		monDatas.clear();
    	try{
    		FileSystem[] fileSysList = SigarUtil.getFileSystemList();
    		for (FileSystem fs : fileSysList) {
    			FileSystemUsage fsu = SigarUtil.getFileSystemUsage(fs.getDirName());
    			//System.out.println("fs: " + fs.getDirName() + ", " + fsu.getTotal() + ", " + fsu.getUsed() + ", " + fsu.getUsePercent() + ", " + fsu.getAvail());
    			monDatas.add(createFSJsonString(serverId, fs.getDirName(), fsu.getTotal(), fsu.getUsed(), fsu.getUsePercent()*100D, fsu.getAvail()));
    		}

    		sendFSMonData(monDatas);
    		
    	} catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	} finally {
    		monDatas.clear();
    	}
    }
	
}