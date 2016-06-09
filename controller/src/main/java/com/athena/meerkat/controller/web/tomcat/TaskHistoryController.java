package com.athena.meerkat.controller.web.tomcat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/task")
public class TaskHistoryController {
	
	@Autowired
	private TaskHistoryService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TaskHistoryController() {
	}

	@RequestMapping(value="/list/{taskId}", method = RequestMethod.GET)
	@ResponseBody
	public List<TaskHistoryDetail> list(@PathVariable Integer taskId){
	
		
		return service.getTaskHistoryDetailList(taskId);
	}
	
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, TaskHistory taskHistory){
		
		service.save(taskHistory);
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/save/detail", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, TaskHistoryDetail taskHistoryDetail){
		
		service.saveDetail(taskHistoryDetail);
		
		
		return jsonRes;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, Integer taskId){
		
		service.delete(taskId);
		
		return jsonRes;
	}
	
	@RequestMapping(value="/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTaskHistory(SimpleJsonResponse jsonRes, Integer taskId){
	
		jsonRes.setData(service.getTaskHistory(taskId));
		
		return jsonRes;
	}

}
//end of TaskHistoryController.java