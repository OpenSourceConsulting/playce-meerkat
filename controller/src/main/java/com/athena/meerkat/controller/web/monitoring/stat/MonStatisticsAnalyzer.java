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
 * BongJin Kwon		2016. 6. 16.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.athena.meerkat.controller.web.monitoring.MonitoringData;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmx;
import com.athena.meerkat.controller.web.monitoring.server.MonData;
import com.athena.meerkat.controller.web.monitoring.server.MonFs;


/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 * @see com.athena.meerkat.controller.ThreadPoolConfig
 */
@Component
public class MonStatisticsAnalyzer implements InitializingBean{
	
	private static final String KEY_SEPERATOR = "_";
	private static final String[] SKIP_MON_FACTORS = new String[]{"mem.used", "net.in", "net.out"};
	private static final String[] SKIP_JMX_MON_FACTORS = new String[]{"ti.run", "jmx.tomcatThreads", "mem.used"};
	
	@Autowired
	private MonUtilStatService statService;
	
	private HashMap<String, Integer> statKeysMap;
	
	private class AnalyzingTask implements Runnable {

        private List<? extends MonitoringData> datas;

        public AnalyzingTask(List<? extends MonitoringData> datas) {
            this.datas = datas;
        }

        public void run() {
            
        	List<MonUtilStat> stats = new ArrayList<MonUtilStat>(this.datas.size());
        	for (MonitoringData data : datas) {
        		MonUtilStat stat = null;
            	if (data instanceof MonData) {
            		
            		if(isSkip(((MonData) data))){
    					continue;
    				}
            		stat = new MonUtilStat((MonData)data);
            		
    			} else if (data instanceof MonJmx) {
    				
    				if(isSkip(((MonJmx) data))){
    					continue;
    				}
    				
            		stat = new MonUtilStat((MonJmx)data);
            		
    			} else if (data instanceof MonFs) {
            		stat = new MonUtilStat((MonFs)data);
    			}
            	
            	Integer id = statKeysMap.get(getKey(stat));
            	if (id != null) {
            		stat.setId(id);
    			}
            	
            	stats.add(stat);
			}
        	
        	statService.save(stats);
        	saveKeys(stats);
        }

    }
	
	@Autowired
	private TaskExecutor taskExecutor;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonStatisticsAnalyzer() {
	}
	
	private void initStatKeys(){
		statKeysMap = new HashMap<String, Integer>();
		
		List<MonUtilStat> stats = statService.getAll();
		saveKeys(stats);
	}
	

	/**
	 * <pre>
	 * 서버별, 톰캣 인스턴스별, monFactorId 별 최신 모니터링 사용률만 따로 저장한다. (to mon_util_stat table).
	 * </pre>
	 * @param datas
	 */
	public void analyze(List<? extends MonitoringData> datas) {
		taskExecutor.execute(new AnalyzingTask(datas));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initStatKeys();
	}

	
	private void saveKeys(List<MonUtilStat> stats) {
		
		for (MonUtilStat stat : stats) {
			statKeysMap.put(getKey(stat), stat.getId());
		}
	}
	
	private String getKey(MonUtilStat stat) {
		return stat.getServerId() + KEY_SEPERATOR + stat.getTomcatInstanceId() + KEY_SEPERATOR + stat.getMonFactorId();
	}


	private boolean isSkip(MonJmx mon) {
		return isSkip(SKIP_JMX_MON_FACTORS, mon.getMonFactorId());
	}
	
	private boolean isSkip(MonData mon) {
		return isSkip(SKIP_MON_FACTORS, mon.getMonFactorId());
	}
	
	private boolean isSkip(String[] skipFactors, String monFactorId) {

		boolean skip = false;
		for (int i = 0; i < skipFactors.length; i++) {
			if(skipFactors[i].equals(monFactorId)){
				skip = true;
				break;
			}
		}
		return skip;
	}
}
//end of MonStatisticsAnalyzer.java