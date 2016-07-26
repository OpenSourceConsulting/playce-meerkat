package com.athena.meerkat.controller.web.tomcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TaskDetailViewModel;
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
	private TomcatInstanceService tomcatService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TaskHistoryController() {
	}

	@RequestMapping(value = "/list/{taskHistoryId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@PathVariable Integer taskHistoryId) {

		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("text", "Root");
		rootMap.put("expanded", true);

		List<TaskDetailViewModel> domains = null;
		List<TaskHistoryDetail> taskDetails = service.getTaskHistoryDetailList(taskHistoryId);

		if (taskDetails.size() > 0) {
			domains = new ArrayList<TaskDetailViewModel>();

			TaskDetailViewModel domainModel = null;
			for (TaskHistoryDetail taskHistoryDetail : taskDetails) {

				if (domainModel == null || taskHistoryDetail.getDomainName().equals(domainModel.getName()) == false) {
					domainModel = new TaskDetailViewModel(taskHistoryDetail.getDomainName());

					domains.add(domainModel);
				}

				domainModel.addChild(new TaskDetailViewModel(taskHistoryDetail));
			}

			rootMap.put("children", domains);
		}

		return rootMap;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse create(SimpleJsonResponse jsonRes, int domainId, int taskCdId) {

		TaskHistory task = service.createTasks(domainId, taskCdId);
		jsonRes.setData(task);

		return jsonRes;
	}

	@RequestMapping(value = "/create/tomcat/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse createTomcatInstanceTask(SimpleJsonResponse jsonRes, @PathVariable Integer tomcatInstanceId, int taskCdId) {

		TaskHistory task = service.createTomcatInstanceTask(tomcatInstanceId, taskCdId);
		TaskHistoryDetail taskDetail = task.getTaskHistoryDetails().get(0);

		jsonRes.setData(taskDetail);

		return jsonRes;
	}

	@RequestMapping(value = "/create/tomcats/{taskCdId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse createTomcatInstanceTasks(SimpleJsonResponse jsonRes, @PathVariable Integer taskCdId, @RequestBody List<TomcatInstance> tomcatIds) {

		List<TomcatInstance> tomcats = new ArrayList<TomcatInstance>();
		for (TomcatInstance t : tomcatIds) {
			tomcats.add(tomcatService.findOne(t.getId()));
		}

		TaskHistory task = service.createTaskDetails(tomcats, taskCdId);

		tomcatService.saveList(tomcats);// update lastTaskHistoryId.

		jsonRes.setData(task);

		return jsonRes;
	}

	@RequestMapping(value = "/create/agent", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse createAgentTask(SimpleJsonResponse jsonRes, int serverId, int taskCdId) {

		TaskHistory task = service.createTaskByServer(serverId, taskCdId);
		jsonRes.setData(task);

		return jsonRes;
	}

	@RequestMapping(value = "/create/uninstall/{tomcatInstanceId}", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse createTomcatUninstallTask(SimpleJsonResponse jsonRes, @PathVariable Integer tomcatInstanceId) {

		TaskHistory task = service.createTomcatUninstallTask(tomcatInstanceId);
		jsonRes.setData(task);

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
	public GridJsonResponse getByDomain(GridJsonResponse jsonRes, @PathVariable Integer domainId, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit, String tomcatInstName) {
		Pageable p = new PageRequest(page - 1, limit);

		List<TaskDetailViewModel> viewmodels = new ArrayList<>();
		List<TaskHistoryDetail> list = new ArrayList<>();
		if (tomcatInstName == null || tomcatInstName.isEmpty()) {
			list = service.getAllTaskHistoryDetailsByDomain(domainId, p);
			jsonRes.setTotal(service.getAllTaskHistoryDetailsByDomainCount(domainId));
		} else {
			list = service.getAllTaskHistoryDetailsByDomainAndTomcatInstName(domainId, tomcatInstName, p);
			jsonRes.setTotal(service.getAllTaskHistoryDetailsByDomainAndTomcatInstNameCount(domainId, tomcatInstName));
		}
		for (TaskHistoryDetail detail : list) {
			TaskDetailViewModel viewmodel = new TaskDetailViewModel(detail);
			viewmodel.setUsername(userService.findUser(viewmodel.getUserId()).getUsername());
			viewmodels.add(viewmodel);
		}
		jsonRes.setList(viewmodels);

		return jsonRes;
	}

	@RequestMapping(value = "/latest/failed/{domainId}", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getLastTaskDetail(SimpleJsonResponse jsonRes, @PathVariable Integer domainId) {

		TaskHistory task = service.getLatestFailedTaskHistory(domainId);

		jsonRes.setData(task);

		return jsonRes;
	}

	@RequestMapping(value = "/viewLogs/{taskDetailId}", method = RequestMethod.GET)
	public String viewLogs(Model model, @PathVariable("taskDetailId") int taskDetailId) {
		model.addAttribute("taskDetailId", taskDetailId);
		return "viewlogs";
	}

	@RequestMapping(value = "/getLogs/{taskDetailId}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getLogs(Model model, @PathVariable("taskDetailId") int taskDetailId, HttpSession session) {

		return service.getLog(taskDetailId, session);
	}

}
//end of TaskHistoryController.java