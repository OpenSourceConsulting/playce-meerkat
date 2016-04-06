package com.athena.meerkat.controller.web.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/MonData")
public class MonDataController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MonDataController.class);
	
	@Autowired
	private MonDataService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MonDataController() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse list(ExtjsGridParam gridParam){
	
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(service.getMonDataListTotalCount(gridParam));
		jsonRes.setList(service.getMonDataList(gridParam));
		
		return jsonRes;
	}
	*/
	
	@MessageMapping("/monitor/create")
    @SendTo("/topic/agents")
	public SimpleJsonResponse create(SimpleJsonResponse jsonRes, MonData monData){
		
		service.insertMonData(monData);
		jsonRes.setMsg("ok.");
		
		LOGGER.debug("saved. ----------------");
		
		return jsonRes;
	}
	
	@RequestMapping(value="/update")
	@ResponseBody
	public SimpleJsonResponse update(SimpleJsonResponse jsonRes, MonData monData){
		
		//service.updateMonData(monData);
		jsonRes.setMsg("사용자 정보가 정상적으로 수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, MonData monData){
		
		//service.deleteMonData(monData);
		jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping(value="/getMonData", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getMonData(SimpleJsonResponse jsonRes, MonData monData){
	
		//jsonRes.setData(service.getMonData(monData));
		
		return jsonRes;
	}

}
//end of MonDataController.java