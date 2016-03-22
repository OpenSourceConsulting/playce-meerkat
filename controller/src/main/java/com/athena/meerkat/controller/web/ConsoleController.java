/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 3. 27.		First Draft.
 */
package com.athena.meerkat.controller.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.athena.meerkat.common.cache.DollyManager;
import com.athena.meerkat.common.cache.SessionKey;
import com.athena.meerkat.common.stats.DollyStats;
import com.athena.meerkat.controller.module.ClientManager;
import com.athena.meerkat.controller.module.vo.DesignDocumentVo;
import com.athena.meerkat.controller.module.vo.MemoryVo;
import com.athena.meerkat.controller.module.vo.ViewVo;
import com.athena.meerkat.controller.web.exception.ErrorInfo;
import com.google.gson.Gson;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("")
public class ConsoleController {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleController.class);
    
   // @Value("${infinispan.jmx.server.list}")
    //private String testProperty;

    @RequestMapping(value="/getServerList", method=RequestMethod.GET)
    @ResponseBody
    public List<String> getServerList(HttpServletRequest request) {
    	return ClientManager.getServerList();
    }

    @RequestMapping(value="/getServerType", method=RequestMethod.GET)
    @ResponseBody
    public String getServerType(HttpServletRequest request) {
    	return ClientManager.getServerType();
    }

    @RequestMapping(value="/getStat", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public DollyStats getStat(HttpServletRequest request) {
    	
    	DollyStats stat = DollyManager.getClient().getStats();
    	
    	if (stat == null) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
        }
    	
    	return stat;
    }   

    @RequestMapping(value="/getSessionKeyList", method=RequestMethod.GET)
    @ResponseBody
    public List<SessionKey> getSessionKeyList(HttpServletRequest request, @RequestParam("viewName") String viewName) {
    	return DollyManager.getClient().getKeys(viewName);
    }

    @RequestMapping(value="/getSessionData", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public Object getSessionData(HttpServletRequest request, @RequestParam("key") String key) {
    	
    	if (key == null) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	return DollyManager.getClient().get(key);
    }

    @RequestMapping(value="/deleteSessionData", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String deleteSessionData(HttpServletRequest request, @RequestParam("key") String key) throws Exception {
    	
    	if (key == null) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	DollyManager.getClient().remove(key);
    	
    	return "success";
    }

    @RequestMapping(value="/memories", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public List<MemoryVo> getMemoryUsageList(HttpServletRequest request) {    	
    	return ClientManager.getMemoryUsageList();
    }
    
    @RequestMapping(value="/memory/{nodeName}", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public MemoryVo getMemoryUsage(HttpServletRequest request, @PathVariable String nodeName) {
    	
    	if (!ClientManager.isValidNodeName(nodeName)) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	return ClientManager.getMemoryUsage(nodeName);
    }
    
    @RequestMapping(value="/cpus", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public List<String> getCpuUsageList(HttpServletRequest request) {    	
    	return ClientManager.getCpuUsageList();
    }
    
    @RequestMapping(value="/cpu/{nodeName}", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String getCpuUsage(HttpServletRequest request, @PathVariable String nodeName) {
    	
    	if (!ClientManager.isValidNodeName(nodeName)) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	return ClientManager.getCpuUsage(nodeName);
    }
    
    @RequestMapping(value="/ddocs", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public List<DesignDocumentVo> getDesignDocuments(HttpServletRequest request) {
    	return ClientManager.getDesigndocs(ClientManager.getServerList().get(0));
    }

    @RequestMapping(value="/ddocs/{docName}/{viewName}", method=RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String createView(HttpServletRequest request, @PathVariable String docName, @PathVariable String viewName) {
    	return ClientManager.createView(ClientManager.getServerList().get(0), docName, viewName);
    }

    @RequestMapping(value="/ddocs/{docName}/{viewName}", method=RequestMethod.PUT)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public Boolean updateView(HttpServletRequest request, @PathVariable String docName, @PathVariable String viewName, @RequestBody String body) {
    	DesignDocumentVo designDoc = new DesignDocumentVo();
    	designDoc.setDesignDocumentName(docName);

		Gson gson = new Gson();
		
		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = gson.fromJson(body, Map.class);
    	
    	ViewVo view = new ViewVo();
    	view.setViewName(viewName);
    	view.setMap(paramMap.get("map"));
    	view.setReduce(paramMap.get("reduce"));
    	
    	designDoc.getViewList().add(view);
    	
    	return ClientManager.updateView(ClientManager.getServerList().get(0), designDoc);
    }
    
    @RequestMapping(value="/ddocs/{docName}", method=RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public Boolean deleteDesignDoc(HttpServletRequest request, @PathVariable String docName) {
    	return ClientManager.deleteDesignDoc(ClientManager.getServerList().get(0), docName);
    }
    
    @RequestMapping(value="/ddocs/{docName}/{viewName}", method=RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public Boolean deleteView(HttpServletRequest request, @PathVariable String docName, @PathVariable String viewName) {
    	return ClientManager.deleteView(ClientManager.getServerList().get(0), docName, viewName);
    }

    /*
    @RequestMapping(value="/os/{nodeName}", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public OperationgSystemVo getOperatingSystemUsage(HttpServletRequest request, @PathVariable String nodeName) {
    	
    	if (!ClientManager.isValidNodeName(nodeName)) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	return ClientManager.getOperatingSystemUsage(nodeName);
    }
    
    @RequestMapping(value="/cacheStatistics/{nodeName}", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public HashMap<String,Object> getCacheStatistics(HttpServletRequest request, @PathVariable String nodeName) {
    	
    	if (!ClientManager.isValidNodeName(nodeName)) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}
    	
    	return ClientManager.getCacheStatisticsInfo(nodeName);
    }    
    
    @RequestMapping(value="/cacheManager/{nodeName}", method={RequestMethod.GET,RequestMethod.POST})
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public HashMap<String,Object> getCacheManager(HttpServletRequest request, HttpServletResponse response, @PathVariable String nodeName) {
    	
    	if (!ClientManager.isValidNodeName(nodeName)) {
    		throw new ResourceNotFoundException("Resource Not Found at [" + request.getRequestURI() + "]");
    	}

    	return ClientManager.getCacheManagerInfo(nodeName);
    }    
    */ 
    
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody ErrorInfo handleCustomException(HttpServletRequest req, ResourceNotFoundException ex) {
    	logger.error("ResourceNotFoundException has occurred. : ", ex);
		//return Response.status(Status.NOT_FOUND).build();
    	//return ex.toString();
    	return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
}
//end of ConsoleController.java