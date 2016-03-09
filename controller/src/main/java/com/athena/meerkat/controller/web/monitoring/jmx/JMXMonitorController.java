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
 * BongJin Kwon		2016. 3. 8.		First Draft.
 */
package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/monitor/jmx")
public class JMXMonitorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JMXMonitorController.class);
	
	private Map<String, List> chartDataMap = new HashMap<String, List>();
	
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");//yyyyMMddHHmmss
	private JMXConnector jmxc = null;

	public JMXMonitorController() {
		
	}

	@RequestMapping("/{tinstId}/threadpool/{attr}")
	@ResponseBody
	public SimpleJsonResponse threadpool(SimpleJsonResponse jsonRes, @PathVariable String tinstId, @PathVariable String attr) throws Exception {

		List<Map<String, Object>> chartDatas = chartDataMap.get(tinstId);
		
		if (chartDatas == null) {
			chartDatas = new ArrayList<Map<String, Object>>();
			chartDataMap.put(tinstId, chartDatas);
		}
		
		if (chartDatas.size() > 30) {
			chartDatas.remove(0);
		}
		
		if (jmxc == null) {
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://192.168.0.87:8225/jmxrmi");
			jmxc = JMXConnectorFactory.connect(url);
			LOGGER.debug("JMX connected!!");
		}
		
		ObjectName name = new ObjectName("Catalina:type=ThreadPool,name=\"http-bio-8083\"");
		Object o = jmxc.getMBeanServerConnection().getAttribute(name, "currentThreadsBusy");// currentThreadCount or currentThreadsBusy
		//System.out.println(o.toString());
		
		LOGGER.debug("currentThreadsBusy is " + o.toString());
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("time",  sdf.format(new java.util.Date()));
		dataMap.put("value", o);
		
		chartDatas.add(dataMap);
		
		jsonRes.setData(chartDatas);

		return jsonRes;
	}
	
	@RequestMapping("/{tinstId}/clear")
	public void clear(@PathVariable String tinstId) throws Exception {
		List<Map<String, Object>> chartDatas = chartDataMap.get(tinstId);
		
		if (chartDatas != null) {
			chartDatas.clear();
		}
		
		LOGGER.debug("chartDatas cleared!!");
	}
	
}
//end of JMXMonitorController.java