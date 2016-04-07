package com.athena.meerkat.controller.web.monitoring;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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

	@Query("select md from MonData md where md.monFactorId = :type and md.serverId = :serverId and md.monDt BETWEEN :tenMinsAgo AND :now")
	List<MonData> findByMonFactorIdAndServerIdInTenMins(
			@Param("type") String type, @Param("serverId") Integer serverId,
			@Param("tenMinsAgo") Date tenMinsAgo, @Param("now") Date now);

}