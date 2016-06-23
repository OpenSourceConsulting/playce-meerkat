package com.athena.meerkat.controller.web.tomcat.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.log.LogTailerListener;
import com.athena.meerkat.controller.web.tomcat.repositories.TaskHistoryDetailRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TaskHistoryRepository;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class TaskHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskHistoryService.class);

	private static final String LOG_TAILER = "logTailer";

	@Autowired
	private TaskHistoryRepository repository;

	@Autowired
	private TaskHistoryDetailRepository detailRepo;

	@Autowired
	private TomcatInstanceService tomcatService;

	public TaskHistoryService() {

	}

	private TaskHistory createTask(int taskCdId) {
		TaskHistory task = new TaskHistory();
		task.setTaskCdId(taskCdId);

		return task;
	}

	public List<TaskHistoryDetail> getTaskHistoryDetailList(int taskHistoryId) {
		return detailRepo.getTaskHistoryDetailList(taskHistoryId);
	}

	public TaskHistory createTomcatInstallTask(List<TomcatInstance> tomcats) {

		TaskHistory task = createTaskDetails(tomcats, MeerkatConstants.TASK_CD_TOMCAT_INSTANCE_INSTALL);

		tomcatService.saveList(tomcats);// update lastTaskHistoryId.

		return task;
	}

	public TaskHistory createTomcatUninstallTask(int tomcatInstanceId) {

		TomcatInstance tomcatInstance = tomcatService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(tomcatInstance);

		TaskHistory task = createTaskDetails(singleList, MeerkatConstants.TASK_CD_TOMCAT_INSTANCE_UNINSTALL);

		tomcatService.saveList(singleList);// update lastTaskHistoryId.

		return task;
	}

	public TaskHistory createApplicationDeployTask(int domainId) {

		List<TomcatInstance> tomcats = tomcatService.findByDomain(domainId);

		return createTaskDetails(tomcats, MeerkatConstants.TASK_CD_WAR_DEPLOY);
	}

	public TaskHistory createApplicationUnDeployTask(int domainId) {

		List<TomcatInstance> tomcats = tomcatService.findByDomain(domainId);

		return createTaskDetails(tomcats, MeerkatConstants.TASK_CD_WAR_UNDEPLOY);
	}

	public TaskHistory createConfigXmlUpdateSingleTask(int tomcatInstanceId, int fileTypeCdId) {

		int taskCdId = 0;

		if (MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD == fileTypeCdId) {
			taskCdId = MeerkatConstants.TASK_CD_SERVER_XML_UPDATE;
		} else if (MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD == fileTypeCdId) {
			taskCdId = MeerkatConstants.TASK_CD_CONTEXT_XML_UPDATE;
		}

		TomcatInstance instance = tomcatService.findOne(tomcatInstanceId);

		List<TomcatInstance> singleList = new ArrayList<TomcatInstance>();
		singleList.add(instance);

		return createTaskDetails(singleList, taskCdId);
	}

	public TaskHistory createConfigXmlUpdateTask(int domainId, int fileTypeCdId) {

		int taskCdId = 0;

		if (MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD == fileTypeCdId) {
			taskCdId = MeerkatConstants.TASK_CD_SERVER_XML_UPDATE;
		} else if (MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD == fileTypeCdId) {
			taskCdId = MeerkatConstants.TASK_CD_CONTEXT_XML_UPDATE;
		}

		List<TomcatInstance> tomcats = tomcatService.findByDomain(domainId);

		return createTaskDetails(tomcats, taskCdId);
	}

	public TaskHistory createTomcatConfigUpdateTask(int domainId) {

		List<TomcatInstance> tomcats = tomcatService.findByDomain(domainId);

		return createTaskDetails(tomcats, MeerkatConstants.TASK_CD_TOMCAT_CONFIG_UPDATE);
	}

	public TaskHistory createTaskDetails(List<TomcatInstance> tomcats, int taskCdId) {

		TaskHistory task = createTask(taskCdId);

		if (tomcats.size() == 0) {
			return task;
		}

		save(task);

		List<TaskHistoryDetail> taskDetails = new ArrayList<TaskHistoryDetail>();
		for (TomcatInstance tomcatInstance : tomcats) {
			TaskHistoryDetail taskDetail = new TaskHistoryDetail(task.getId(), tomcatInstance);
			taskDetails.add(taskDetail);

			tomcatInstance.setLastTaskHistoryId(task.getId());
		}

		detailRepo.save(taskDetails);

		return task;
	}

	public TaskHistory createTasks(int domainId, int taskCdId) {

		List<TomcatInstance> tomcats = tomcatService.findByDomain(domainId);

		return createTaskDetails(tomcats, taskCdId);
	}

	public void save(TaskHistory taskHistory) {
		repository.save(taskHistory);
	}

	public void saveDetail(TaskHistoryDetail taskHistoryDetail) {
		detailRepo.save(taskHistoryDetail);
	}

	public void updateTaskLogFile(int taskHistoryId, int tomcatInstanceId, File jobDir) {

		TaskHistoryDetail taskDetail = detailRepo.findByTaskHistoryIdAndTomcatInstanceId(taskHistoryId, tomcatInstanceId);
		taskDetail.setLogFilePath(jobDir.getAbsolutePath() + File.separator + "build.log");
		taskDetail.setStatus(MeerkatConstants.TASK_STATUS_WORKING);
		taskDetail.setFinishedTime(null);

		saveDetail(taskDetail);
	}

	public void updateTaskStatus(int taskHistoryId, int tomcatInstanceId, int status) {

		TaskHistoryDetail taskDetail = detailRepo.findByTaskHistoryIdAndTomcatInstanceId(taskHistoryId, tomcatInstanceId);
		taskDetail.setStatus(status);

		if (status > 1) {
			taskDetail.setFinishedTime(new Date());
		}

		saveDetail(taskDetail);
	}

	public void updateTomcatInstanceToNull(int tomcatInstanceId) {
		List<TaskHistoryDetail> taskDetails = detailRepo.findByTomcatInstanceId(tomcatInstanceId);
		for (TaskHistoryDetail taskHistoryDetail : taskDetails) {
			taskHistoryDetail.setTomcatInstance(null);
		}

		detailRepo.save(taskDetails);
	}

	public TaskHistory getTaskHistory(int taskId) {
		return repository.findOne(taskId);
	}

	public void delete(int taskId) {
		repository.delete(taskId);
	}

	public TaskHistoryDetail getTaskHistoryDetail(int taskDetailId) {
		return detailRepo.findOne(taskDetailId);
	}

	public Integer getAllTaskHistoryDetailsByDomainCount(Integer domainId) {
		return detailRepo.countByTomcatDomainId(domainId);
	}

	public List<TaskHistoryDetail> getAllTaskHistoryDetailsByDomain(Integer domainId, Pageable p) {

		return detailRepo.findByTomcatDomainIdOrderByFinishedTimeDesc(domainId, p);
	}

	public List<String> getLog(int taskDetailId, HttpSession session) {

		LogTailerListener listener = (LogTailerListener) session.getAttribute(LOG_TAILER);
		int errCode = 0;
		String logFile = null;
		
		if (listener == null) {
			TaskHistoryDetail taskDetail = getTaskHistoryDetail(taskDetailId);

			listener = new LogTailerListener(new ArrayList<String>());
			session.setAttribute(LOG_TAILER, listener);

			logFile = taskDetail.getLogFilePath();
			LOGGER.debug("log file : {}", logFile);
			
			if (logFile != null) {
				
				long delay = 3000;
				
				File file = new File(logFile);
				
				if (file.exists()) {
					Tailer tailer = new Tailer(file, listener, delay);
					new Thread(tailer).start();
				} else {
					errCode = 2;
				}
				
			} else {
				errCode = 1;
			}
			
		}

		List<String> logs = listener.getLogs();

		LOGGER.debug("logs size is {}", logs.size());
		
		if (errCode > 0){
			logs.add("로그 파일이 없습니다. " + logFile);
		}

		if (errCode > 0 || listener.isStop()) {
			logs.add("end");
			session.removeAttribute(LOG_TAILER);
		}

		return logs;

	}

	/**
	 * <pre>
	 * 24시간 이내의 task 중 실패한 detail task가  하나라도 있는 task 반환.
	 * </pre>
	 * 
	 * @param domainId
	 * @return
	 */
	public TaskHistory getLatestFailedTaskHistory(int domainId) {

		Date now = new Date();

		Date from = DateUtils.addDays(now, -1);

		List<TaskHistoryDetail> details = detailRepo.findByTomcatDomainIdAndTomcatInstanceIdNotNullAndFinishedTimeBetweenOrderByFinishedTimeDesc(domainId,
				from, now);

		TaskHistory failedTask = null;

		for (TaskHistoryDetail taskHistoryDetail : details) {
			if (MeerkatConstants.TASK_STATUS_FAIL == taskHistoryDetail.getStatus()) {
				failedTask = taskHistoryDetail.getTaskHistory();
				break;
			}
		}

		return failedTask;
	}

}
//end of TaskHistoryService.java
