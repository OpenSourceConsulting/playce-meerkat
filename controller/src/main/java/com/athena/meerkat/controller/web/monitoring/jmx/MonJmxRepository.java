package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * MonJmxRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonJmxRepository extends JpaRepository<MonJmx, MonJmxPK> {
	@Query("select mj from MonJmx mj where mj.monFactorId in :types and mj.instanceId = :instanceId and mj.monDt BETWEEN :time AND :now order by mj.monDt")
	List<MonJmx> findByInstanceIdAndMonFactorIds(@Param("types") String[] types, @Param("instanceId") Integer instanceId, @Param("time") Date time,
			@Param("now") Date now);

}