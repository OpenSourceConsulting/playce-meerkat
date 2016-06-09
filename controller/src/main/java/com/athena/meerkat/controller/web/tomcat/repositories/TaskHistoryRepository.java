package com.athena.meerkat.controller.web.tomcat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TaskHistory;

/**
 * TaskHistoryRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Integer> {

	
}