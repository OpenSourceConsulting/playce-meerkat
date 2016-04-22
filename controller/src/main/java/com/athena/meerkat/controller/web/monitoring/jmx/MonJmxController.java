package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.athena.meerkat.common.constant.MeerkatConstant;
import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.monitoring.server.MonData;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/monitor/jmx")
public class MonJmxController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MonJmxController.class);
	
	@Autowired
	private MonJmxService service;
	

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonJmxController() {
	}
	
	@MessageMapping("/monitor/jmx/create")
	@SendToUser("/queue/jmx/agents")
	public SimpleJsonResponse create(List<Map> datas) {
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		
		List<MonJmx> monJmxs = copyProperties(datas);
		
		service.insertMonJmxs(monJmxs);
		service.saveInstanceState(monJmxs);
		
		LOGGER.debug("saved. ----------------");

		return jsonRes;
	}
	
	
	/*
	@RequestMapping(value="/update")
	@ResponseBody
	public SimpleJsonResponse update(SimpleJsonResponse jsonRes, MonJmx monJmx){
		
		service.updateMonJmx(monJmx);
		jsonRes.setMsg("사용자 정보가 정상적으로 수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, MonJmx monJmx){
		
		service.deleteMonJmx(monJmx);
		jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping(value="/getMonJmx", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getMonJmx(SimpleJsonResponse jsonRes, MonJmx monJmx){
	
		jsonRes.setData(service.getMonJmx(monJmx));
		
		return jsonRes;
	}
*/
	private List<MonJmx> copyProperties(List<Map> maps) {
		
		List<MonJmx> messages = new ArrayList<MonJmx>();
    	
		try{
	    	for (Map map : maps) {
	    		
	    		MonJmx msg = new MonJmx();
				
	    		PropertyUtils.copyProperties(msg, map);
	    		
				messages.add(msg);
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
    	
    	return messages;
	}
}
//end of MonJmxController.java