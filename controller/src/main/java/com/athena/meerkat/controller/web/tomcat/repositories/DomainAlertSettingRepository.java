package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DomainAlertSetting;

@Repository
public interface DomainAlertSettingRepository extends JpaRepository<DomainAlertSetting, Integer> {
	@Query("select alert from DomainAlertSetting alert join alert.tomcatDomain td join td.tomcatInstances ti where ti.serverId=?1")
	List<DomainAlertSetting> findAlertSettingsByServerId(Integer serverId);

}
