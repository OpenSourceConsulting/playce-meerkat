package com.athena.meerkat.controller.web.monitoring.server;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
	//@Query(value = "SELECT server_id, fs_name, max(mon_dt) as mon_dt, total, used, use_per, avail from mon_fs_tbl where server_id= :serverId group by server_id, fs_name", nativeQuery = true)
	@Query("SELECT new MonFs(fs.serverId, fs.fsName, max(fs.monDt), fs.total, fs.used,  fs.usePer, fs.avail) from MonFs fs where fs.serverId= :serverId group by fs.serverId, fs.fsName")
	List<MonFs> getFsMonData(@Param("serverId") Integer serverId);
	
	@Modifying
	@Query("delete from MonFs fs where fs.monDt < :time")
	int deleteOldData(@Param("time") Date time);
			
}