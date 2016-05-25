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

	@Query("select mj from MonJmx mj where mj.monFactorId in :types and mj.monDt BETWEEN :time AND :now order by mj.monDt")
	List<MonJmx> findByMonFactorIds(@Param("types") String[] types, @Param("time") Date time, @Param("now") Date now);

	@Query(value = "select count(mfrom MonJmx mj where mj.monFactorId like 'jmx.ds.%' and mj.monDt BETWEEN :time AND :now order by mj.monDt", nativeQuery = true)
	List<MonJmx> findByJdbcConnection(@Param("time") Date time, @Param("now") Date now);

	@Query(value = "SELECT sum(mon_value) FROM athena_meerkat.mon_jmx_tbl mj where mj.mon_factor_id like :type and mj.mon_dt BETWEEN :time AND :now", nativeQuery = true)
	Long getJdbcConnectionSum(@Param("type") String type, @Param("time") Date time, @Param("now") Date now);

}