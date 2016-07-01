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
 * BongJin Kwon		2016. 6. 15.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.common.util.JSONUtil;
import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.FileHandler;
import com.athena.meerkat.controller.web.entities.MonAlertConfig;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainAlertSettingRepository;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Service
public class AlertSettingService implements InitializingBean{

	@Autowired
	private DomainAlertSettingRepository domainAlertRepo;
	
	@Autowired
	private FileHandler fileHandler;
	
	private String defaultAlertJson;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlertSettingService() {

	}

	public MonAlertConfig getAlertConfig(Integer alertId) {
		return domainAlertRepo.findOne(alertId);
	}

	public MonAlertConfig saveAlertSetting(MonAlertConfig alert) {
		
		return domainAlertRepo.save(alert);
	}

	public void saveAllAlertSettings(List<MonAlertConfig> alertSettings) {
		domainAlertRepo.save(alertSettings);

	}
	
	public void saveDomainDefaultAlertSetting(TomcatDomain domain) {
		List<MonAlertConfig> alertSettings = JSONUtil.jsonToList(defaultAlertJson, List.class, MonAlertConfig.class);
		List<MonAlertConfig> newConfigs = new ArrayList<MonAlertConfig>();
		
		for (MonAlertConfig alertConfig : alertSettings) {
			
			if (alertConfig.getAlertItemCdId() == MeerkatConstants.ALERT_ITEM_CPU_USED
				|| alertConfig.getAlertItemCdId() == MeerkatConstants.ALERT_ITEM_MEM_USED) {
				
				alertConfig.setTomcatDomain(domain);
				newConfigs.add(alertConfig);
			}
			
			
		}
		saveAllAlertSettings(newConfigs);
	}
	
	public void saveServerDefaultAlertSetting(Server server) {
		List<MonAlertConfig> alertSettings = JSONUtil.jsonToList(defaultAlertJson, List.class, MonAlertConfig.class);
		
		for (MonAlertConfig alertConfig : alertSettings) {
			alertConfig.setServer(server);
		}
		saveAllAlertSettings(alertSettings);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		defaultAlertJson = fileHandler.readFileToString("classpath:/default-alerts.json");
		
	}

}
//end of DomainAlertSettingService.java