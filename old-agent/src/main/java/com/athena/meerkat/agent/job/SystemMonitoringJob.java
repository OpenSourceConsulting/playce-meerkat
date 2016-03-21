/*
                                                                                         
 * Copyright 2015 Open Source Consulting, Inc. <support@osci.kr>                                
                                                                                              
 * This file is part of athena-peacock. https://github.com/OpenSourceConsulting/athena-peacock    
                                                                                              
 * athena-peacock is free software: you can redistribute it and/or modify it under               
 * the terms of the GNU Lesser General Public License as published by the Free                  
 * Software Foundation, either version 3 of the License, or (at your option)                    
 * any later version.                                                                           
 *                                                                                              
 * athena-peacock is distributed in the hope that it will be useful, but WITHOUT ANY             
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS                    
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more                 
 * details.                                                                                     
 *                                                                                              
 * You should have received a copy of the GNU Lesser General Public License                     
 * along with athena-peacock. If not, see <http://www.gnu.org/licenses/>.                        
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 8. 1.			First Draft.
 * Ji-Woong Choi	2015. 9. 20.		Channg license from GPLv2 to GPLv3
 */

package com.athena.meerkat.agent.job;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;

import com.athena.meerkat.agent.netty.MeerkatTransmitter;
import com.athena.meerkat.agent.util.PropertyUtil;
import com.athena.meerkat.agent.util.SigarUtil;
import com.athena.meerkat.common.constant.MeerkatConstant;
import com.athena.meerkat.common.netty.MeerkatDatagram;
import com.athena.meerkat.common.netty.message.AgentSystemStatusMessage;
import com.athena.meerkat.common.scheduler.InternalJobExecutionException;
import com.athena.meerkat.common.scheduler.quartz.BaseJob;
import com.athena.meerkat.common.scheduler.quartz.JobExecution;

/**
 * <pre>
 * SIGAR를 이용한 주기적 System Monitoring을 수행하기 위한 Quartz Job
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SystemMonitoringJob extends BaseJob {
	
	private MeerkatTransmitter meerkatTransmitter;

	/**
	 * @param meerkatTransmitter the meerkatTransmitter to set
	 */
	public void setMeerkatTransmitter(MeerkatTransmitter meerkatTransmitter) {
		this.meerkatTransmitter = meerkatTransmitter;
	}

	/* (non-Javadoc)
	 * @see com.athena.meerkat.agent.scheduler.quartz.BaseJob#executeInternal(com.athena.meerkat.agent.scheduler.quartz.JobExecution)
	 */
	@Override
	protected void executeInternal(JobExecution context) throws InternalJobExecutionException {
		
//		System.out.println("============= executeInternal() =============");
//		System.out.println(meerkatTransmitter);
//		System.out.println(meerkatTransmitter.isConnected());
		
		if (!meerkatTransmitter.isConnected()) {
			return;
		}
		
		try {
			CpuPerc cpu = SigarUtil.getCpuPerc();
			Mem mem = SigarUtil.getMem();
			FileSystem[] fileSystems = SigarUtil.getFileSystem();
			FileSystemUsage fsu = null;
			
			AgentSystemStatusMessage message = new AgentSystemStatusMessage();
			message.setAgentId(IOUtils.toString(new File(PropertyUtil.getProperty(MeerkatConstant.AGENT_ID_FILE_KEY)).toURI()));
			
			// set cpu info
			message.setIdleCpu(CpuPerc.format(cpu.getIdle()).replaceAll("%", ""));
			message.setCombinedCpu(CpuPerc.format(cpu.getCombined()).replaceAll("%", ""));
			
			// set memory info
			message.setTotalMem(Long.toString(mem.getTotal() / 1024L));
			message.setFreeMem(Long.toString(mem.getActualFree() / 1024L));
			message.setUsedMem(Long.toString(mem.getActualUsed() / 1024L));
			
			//BigDecimal.valueOf(mem.getUsedPercent()).setScale(1, RoundingMode.HALF_UP).toString();

			// set disk info
			StringBuilder usage = new StringBuilder();
			
			int cnt = 0;
			long total = 0;
			long used = 0;
			
			for (FileSystem fs : fileSystems) {
				if (fs.getType() == FileSystem.TYPE_NONE) {
					continue;
				}
				
				try {
					// 특정 Volume에 장애(NFS 에러)가 발생한 경우 전체 모니터링이 실패하므로,
					// try {} catch() {} 추가
					fsu = SigarUtil.getFileSystemUsage(fs.getDirName());
					
					if (cnt > 0) {
						usage.append(",");
						
					}
					cnt++;
					usage.append(fs.getDirName()).append(":");
					usage.append(CpuPerc.format(fsu.getUsePercent()).replaceAll("%", ""));

					total += fsu.getTotal();
					used += fsu.getUsed();
				} catch (Exception e) {
					LOGGER.warn("[{}] has an error.", fs.getDirName());
					
					if (cnt > 0) {
						usage.append(",");
						
					}
					cnt++;
					usage.append(fs.getDirName()).append(":");
					usage.append("0.0");
				}
			}
			
			message.setDiskUsage(usage.toString());
			message.setTotalDisk(Long.toString(total));
			message.setUsedDisk(Long.toString(used));
			
			meerkatTransmitter.sendMessage(new MeerkatDatagram<AgentSystemStatusMessage>(message));
		} catch (SigarException e) {
			LOGGER.error("SigarException has occurred.", e);
			throw new InternalJobExecutionException(e);
		} catch (IOException e) {
			LOGGER.error("IOException has occurred.", e);
			throw new InternalJobExecutionException(e);
		} catch (Exception e) {
			LOGGER.error("Unhandled Exception has occurred.", e);
			throw new InternalJobExecutionException(e);
		}
	}//end of executeInternal()
}
//end of SystemMonitoringJob.java