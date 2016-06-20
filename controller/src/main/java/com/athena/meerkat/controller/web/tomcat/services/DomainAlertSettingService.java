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
package com.athena.meerkat.controller.web.tomcat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.DomainAlertSetting;
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
public class DomainAlertSettingService {

	@Autowired
	private DomainAlertSettingRepository domainAlertRepo;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public DomainAlertSettingService() {

	}

	public DomainAlertSetting getDomainAlert(Integer alertId) {
		return domainAlertRepo.findOne(alertId);
	}

	public DomainAlertSetting saveAlertSetting(DomainAlertSetting alert) {
		Integer alertItem = alert.getAlertItemCdId();
		if (alertItem == MeerkatConstants.ALERT_ITEM_CPU_USED) {
			alert.setMonFactorId(MeerkatConstants.MON_FACTOR_CPU_USED);
		} else if (alertItem == MeerkatConstants.ALERT_ITEM_MEM_USED) {
			alert.setMonFactorId(MeerkatConstants.MON_FACTOR_MEM_USED);
		} else if (alertItem == MeerkatConstants.ALERT_ITEM_DISK_USED) {
			alert.setMonFactorId(MeerkatConstants.MON_FACTOR_DISK_USED);
		}
		return domainAlertRepo.save(alert);
	}

	public void saveAllAlertSettings(List<DomainAlertSetting> alertSettings) {
		domainAlertRepo.save(alertSettings);

	}

}
//end of DomainAlertSettingService.java