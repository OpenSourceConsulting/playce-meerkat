package com.athena.meerkat.controller.web.tomcat.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.TaskHistoryDetailRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TaskHistoryRepository;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class TaskHistoryService {

	@Autowired
	private TaskHistoryRepository repository;
	
	@Autowired
	private TaskHistoryDetailRepository detailRepo;
	
	public TaskHistoryService() {
		
	}
	
	private TaskHistory createTask(int taskCdId) {
		TaskHistory task = new TaskHistory();
		task.setTaskCdId(taskCdId);
		
		return task;
	}
	
	public List<TaskHistoryDetail> getTaskHistoryDetailList(int taskHistoryId){
		return detailRepo.findByTaskHistoryIdOrderByTomcatDomainIdAscTomcatInstanceIdAsc(taskHistoryId);
	}
	
	public TaskHistory createTomcatInstallTask() {
		
		TaskHistory task = createTask(MeerkatConstants.TASK_CD_TOMCAT_INSTALL);
		
		save(task);
		/*
		List<TaskHistoryDetail> taskDetails = new ArrayList<TaskHistoryDetail>();
		for (TomcatInstance tomcatInstance : tomcats) {
			TaskHistoryDetail taskDetail = new TaskHistoryDetail(task.getId(), tomcatInstance.getId());
			taskDetails.add(taskDetail);
		}
		
		detailRepo.save(taskDetails);*/
		
		return task;
	}
	
	public void save(TaskHistory taskHistory){
		repository.save(taskHistory);
	}
	
	public void saveDetail(TaskHistoryDetail taskHistoryDetail){
		detailRepo.save(taskHistoryDetail);
	}
	
	public void createTaskHistoryDetails(int taskHistoryId, TomcatInstance tomcatInstance, File jobDir) {
		
		TaskHistoryDetail taskDetail = null;
		if (jobDir != null) {
			taskDetail = new TaskHistoryDetail(taskHistoryId, tomcatInstance, jobDir.getAbsolutePath() + File.separator + "build.log");
		} else {
			taskDetail = new TaskHistoryDetail(taskHistoryId, tomcatInstance);
		}
		saveDetail(taskDetail);
	}
	
	/*
	public List<TaskHistory> getTaskHistoryList(ExtjsGridParam gridParam){
		return repository.getTaskHistoryList(gridParam);
	}
	
	public int getTaskHistoryListTotalCount(ExtjsGridParam gridParam){
		
		return repository.getTaskHistoryListTotalCount(gridParam);
	}
	*/
	
	public TaskHistory getTaskHistory(int taskId){
		return repository.findOne(taskId);
	}
	
	public void delete(int taskId){
		repository.delete(taskId);
	}

}
//end of TaskHistoryService.java