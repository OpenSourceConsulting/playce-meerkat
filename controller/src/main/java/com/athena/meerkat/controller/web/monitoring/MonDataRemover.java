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
 * BongJin Kwon		2016. 7. 7.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmxRepository;
import com.athena.meerkat.controller.web.monitoring.server.MonDataRepository;
import com.athena.meerkat.controller.web.monitoring.server.MonFsRepository;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Component
public class MonDataRemover {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonDataRemover.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	@Value("${meerkat.mon.data.save.limit:30}")
	private int dayAgo;
	
	@Autowired
	private MonDataRepository monRepo;
	
	@Autowired
	private MonJmxRepository jmxRepo;
	
	@Autowired
	private MonFsRepository fsRepo;

	@Transactional
    @Scheduled(cron="0 0 0/1 * * *")
    public void reportCurrentTime() {

    	Date now = new Date();
    	Date time = new Date(now.getTime() - (dayAgo * MeerkatConstants.ONE_MINUTE_IN_MILLIS * 60 * 24));
    	
    	LOGGER.debug("The limit time is " + dateFormat.format(time));
    	
    	monRepo.deleteOldData(time);
    	jmxRepo.deleteOldData(time);
    	fsRepo.deleteOldData(time);
    }

}
//end of MonDataRemover.java