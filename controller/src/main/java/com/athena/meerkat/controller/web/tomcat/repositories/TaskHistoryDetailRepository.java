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

	List<TaskHistoryDetail> findByTaskHistoryId(int taskHistoryId);

	List<TaskHistoryDetail> findByTomcatDomainId(Integer domainId);

	List<TaskHistoryDetail> findByTomcatInstanceId(Integer tomcatInstanceId);

}