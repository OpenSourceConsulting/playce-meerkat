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
	
	private static final String[] IGNORE_DEVICES = { "none", "udev", "sysfs", "devpts", "binfmt_misc", "systemd", "gvfsd-fuse" }; 
	 
	private static final String[] IGNORE_SYSTYPES = { "proc"};

    
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
    		String root = null;
    		FileSystem[] fileSysList = SigarUtil.getFileSystemList();
    		for (FileSystem fs : fileSysList) {
    			
    			
    			if (fs.getDevName() == null || equalsAny(fs.getDevName(), IGNORE_DEVICES)) { 
    			      continue; 
    			} 
    			 
    			if (fs.getSysTypeName() == null || equalsAny(fs.getSysTypeName(), IGNORE_SYSTYPES)) { 
    			      continue; 
    			}
    			
    			FileSystemUsage fsu = SigarUtil.getFileSystemUsage(fs.getDirName());
    			
    			if(fsu.getTotal() == 0) {
    				continue;
    			}
    			
    			if(root != null && root.equals(fs.getDirName())){
    				continue;
    			} else if("/".equals(fs.getDirName())){
    				root = fs.getDirName();
    			}
    			
    			monDatas.add(createFSJsonString(serverId, fs.getDirName(), fsu.getTotal(), fsu.getUsed(), fsu.getUsePercent()*100D, fsu.getAvail()));
    		}

    		sendFSMonData(monDatas);
    		
    	} catch (Exception e) {
    		LOGGER.error(e.toString(), e);
    	} finally {
    		monDatas.clear();
    	}
    }
	
	private static boolean equalsAny(String str, String[] strs) { 
		   
		for (String s : strs) { 
		   if (s.equals(str)) { 
			   return true; 
		   } 
		} 
		 
		return false; 
	} 
	
}