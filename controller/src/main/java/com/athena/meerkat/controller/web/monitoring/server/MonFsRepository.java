package com.athena.meerkat.controller.web.monitoring.server;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * MonFsRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonFsRepository extends JpaRepository<MonFs, MonFsPK> {

	@Query("select md from MonFs fs where fs.serverId = :serverId and fs.monDt BETWEEN :time AND :now")
	List<MonFs> getFsMonData(@Param("serverId") Integer serverId,
			@Param("time") Date time, @Param("now") Date now);
}