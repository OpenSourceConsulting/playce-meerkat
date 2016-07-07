package com.athena.meerkat.controller.web.monitoring.server;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * MonDataRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonDataRepository extends JpaRepository<MonData, MonDataPK> {

	@Query("select md from MonData md where md.monFactorId in :types and md.serverId = :serverId and md.monDt BETWEEN :time AND :now order by md.monDt")
	List<MonData> findByMonFactorIdAndServerId(@Param("types") String[] types, @Param("serverId") Integer serverId, @Param("time") Date time,
			@Param("now") Date now);

	@Query("select md from MonData md where md.monFactorId in :types AND md.monDt BETWEEN :time AND :now order by md.monDt")
	List<MonData> findByMonFactorIds(@Param("types") String[] types, @Param("time") Date time, @Param("now") Date now);
	
	
	@Modifying
	@Query("delete from MonData md where md.monDt < :time")
	int deleteOldData(@Param("time") Date time);

}