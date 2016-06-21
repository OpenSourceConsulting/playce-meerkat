package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.MonAlertConfig;

@Repository
public interface DomainAlertSettingRepository extends JpaRepository<MonAlertConfig, Integer> {

	List<MonAlertConfig> findByServer_Id(Integer serverId);

	List<MonAlertConfig> findByTomcatDomain_Id(Integer domainId);

}
