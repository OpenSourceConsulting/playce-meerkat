package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TaskHistoryDetail;

/**
 * TaskHistoryDetailRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface TaskHistoryDetailRepository extends JpaRepository<TaskHistoryDetail, Integer> {

	List<TaskHistoryDetail> findByTaskHistoryIdOrderByTomcatDomainIdAscTomcatInstanceIdAsc(int taskHistoryId);
	
	TaskHistoryDetail findByTaskHistoryIdAndTomcatInstanceId(int taskHistoryId, int tomcatInstanceId);
	
}