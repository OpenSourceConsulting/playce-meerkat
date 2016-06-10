package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.collection.mutable.History;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TaskHistoryViewModel;
import com.athena.meerkat.controller.web.user.services.UserService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/task")
public class TaskHistoryController {

	@Autowired
	private TaskHistoryService service;
	@Autowired
	private UserService userService;
	@Autowired
	private CommonCodeHandler codeHandler;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TaskHistoryController() {
	}

	@RequestMapping(value = "/list/{taskId}", method = RequestMethod.GET)
	@ResponseBody
	public List<TaskHistoryDetail> list(@PathVariable Integer taskId) {

		return service.getTaskHistoryDetailList(taskId);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, TaskHistory taskHistory) {

		service.save(taskHistory);

		return jsonRes;
	}

	@RequestMapping(value = "/save/detail", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse save(SimpleJsonResponse jsonRes, TaskHistoryDetail taskHistoryDetail) {

		service.saveDetail(taskHistoryDetail);

		return jsonRes;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse delete(SimpleJsonResponse jsonRes, Integer taskId) {

		service.delete(taskId);

		return jsonRes;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTaskHistory(SimpleJsonResponse jsonRes, Integer taskId) {

		jsonRes.setData(service.getTaskHistory(taskId));

		return jsonRes;
	}

	@RequestMapping(value = "/list/domain/{domainId}", method = RequestMethod.GET)
	@ResponseBody
	public GridJsonResponse getByDomain(GridJsonResponse jsonRes, @PathVariable Integer domainId) {
		List<TaskHistoryViewModel> viewmodels = new ArrayList<>();
		List<TaskHistoryDetail> list = service.getTaskHistoryDetailListByDomain(domainId);
		for (TaskHistoryDetail detail : list) {
			TaskHistoryViewModel viewmodel = new TaskHistoryViewModel(detail);
			viewmodel.setUsername(userService.findUser(viewmodel.getUserId()).getUsername());
			//viewmodel.setTaskName(codeHandler.getCodeNm(MeerkatConstants.CODE_GROP_TASK_HISTORY, viewmodel.getTaskCdId()) + );
			viewmodels.add(viewmodel);
		}
		jsonRes.setList(viewmodels);
		jsonRes.setTotal(viewmodels.size());
		return jsonRes;
	}
}
//end of TaskHistoryController.java