package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	@Query("SELECT thd From TaskHistoryDetail thd WHERE thd.taskHistoryId = ?1 "
			+ "ORDER BY thd.tomcatDomainId asc, thd.tomcatInstanceId asc")
	List<TaskHistoryDetail> getTaskHistoryDetailList(int taskHistoryId);

	//List<TaskHistoryDetail> findByTaskHistoryIdAndTomcatInstanceIdNotNullOrderByTomcatDomainIdAscTomcatInstanceIdAsc(int taskHistoryId);

	List<TaskHistoryDetail> findByTomcatInstanceId(int tomcatInstanceId);

	TaskHistoryDetail findByTaskHistoryIdAndTomcatInstanceId(int taskHistoryId, Integer tomcatInstanceId);

	List<TaskHistoryDetail> findByTomcatDomainIdOrderByFinishedTimeDesc(Integer domainId, Pageable p);

	List<TaskHistoryDetail> findByTomcatDomainIdAndTomcatInstanceIdNotNullAndFinishedTimeBetweenOrderByFinishedTimeDesc(int domainId, Date from, Date to);

	Integer countByTomcatDomainId(Integer domainId);

	List<TaskHistoryDetail> findAllByOrderByFinishedTimeDesc(Pageable p);
}
