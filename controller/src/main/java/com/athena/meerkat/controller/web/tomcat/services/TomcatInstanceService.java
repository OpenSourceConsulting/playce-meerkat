/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 9.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.tomcat.instance.domain.ConfigFileVersionRepository;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.jmx.MonJmxService;
import com.athena.meerkat.controller.web.provisioning.ProvisionModel;
import com.athena.meerkat.controller.web.provisioning.TomcatProvisioningService;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatConfigFileRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstConfigRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TomcatInstanceAppModel;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @author Tran Ho
 * @version 2.0
 */
@Service
public class TomcatInstanceService {

	static final Logger LOGGER = LoggerFactory.getLogger(TomcatInstanceService.class);

	@Autowired
	private TomcatInstanceRepository repo;

	@Autowired
	private DataSourceRepository datasourceRepo;

	@Autowired
	private ConfigFileVersionRepository configRepo;

	@Autowired
	private TomcatInstConfigRepository tomcatInstConfigRepo;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private CommonCodeRepository commonRepo;

	@Autowired
	private TomcatConfigFileRepository tomcatConfigFileRepo;

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TaskHistoryService taskService;

	@Autowired
	private MonJmxService monJmxService;

	@Autowired
	private TomcatProvisioningService proviService;

	public TomcatInstanceService() {

	}

	public Page<TomcatInstance> getList(Pageable pageable) {
		return repo.findAll(pageable);
	}

	public List<TomcatInstance> getTomcatListByDomainId(int domainId) {
		return repo.findByTomcatDomain_Id(domainId);
	}

	public List<TomcatInstance> getTomcatListWillInstallByDomainId(int domainId) {
		return repo.findByTomcatDomain_IdAndState(domainId, MeerkatConstants.TOMCAT_STATUS_NOTINSTALLED);
	}

	/**
	 * insert or update
	 * 
	 * @param inst
	 */
	public TomcatInstance save(TomcatInstance inst) {
		return repo.save(inst);
	}

	public void saveList(List<TomcatInstance> entities) {
		repo.save(entities);

	}

	public TomcatInstance findOne(int id) {
		return repo.findOne(id);
	}

	public DomainTomcatConfiguration getTomcatConfig(int instanceId) {
		TomcatInstance tomcat = findOne(instanceId);
		return getTomcatConfig(tomcat.getDomainId(), instanceId);
	}

	public DomainTomcatConfiguration getTomcatConfig(int domainId, int instanceId) {

		LOGGER.debug("domainId : {}, instanceId: {}", domainId, instanceId);

		DomainTomcatConfiguration conf = domainService.getTomcatConfig(domainId);
		DomainTomcatConfiguration clonedConf = null;
		try {
			if (conf == null) {
				LOGGER.debug("DomainTomcatConfiguration is null of domainId({})", domainId);
				return null;
			}
			clonedConf = conf.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}

		// get configurations that are different to domain tomcat config
		List<TomcatInstConfig> changedConfigs = getTomcatInstConfigs(instanceId);
		if (changedConfigs != null) {
			for (TomcatInstConfig changedConf : changedConfigs) {
				try {
					BeanUtils.setProperty(clonedConf, changedConf.getConfigName(), changedConf.getConfigValue());
				} catch (Exception e) {
					LOGGER.error(e.toString(), e);
					throw new RuntimeException(e);
				}
			}
		}

		clonedConf.setTomcatInstanceId(instanceId);

		return clonedConf;

	}

	public List<TomcatInstance> findInstances(int serverId) {
		return repo.findByServer_Id(serverId);
	}

	public List<DomainTomcatConfiguration> findInstanceConfigs(int serverId) {

		List<DomainTomcatConfiguration> configs = new ArrayList<DomainTomcatConfiguration>();
		List<TomcatInstance> list = repo.findByServer_Id(serverId);

		for (TomcatInstance tomcatInstance : list) {

			DomainTomcatConfiguration conf = getTomcatConfig(tomcatInstance.getDomainId(), tomcatInstance.getId());

			if (conf != null) {
				configs.add(conf);
			}

		}

		return configs;
	}

	@Transactional
	public void delete(TomcatInstance tomcat) {

		taskService.updateTomcatInstanceToNull(tomcat.getId());

		repo.delete(tomcat);

		monJmxService.deleteAll(tomcat.getId());
		LOGGER.debug("deleted tomcat instance ({})", tomcat.getId());
	}

	public List<DataSource> getDataSourceListByTomcat(TomcatInstance tomcat) {
		// List<DataSource> list = (List<DataSource>) tomcat.getDataSources();
		// return list;
		return null;
	}

	public void saveState(TomcatInstance instance, int state) {
		//repo.setState(instanceId, state);
		instance.setState(state);
		repo.save(instance);

		LOGGER.debug("tomcat instance({}) state({}) saved.", instance.getId(), state);
	}

	public List<TomcatInstance> getAll() {
		return repo.findAll();
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param name
	 * @param domainId
	 * @return
	 */
	public List<TomcatInstance> findByNameAndDomain(String name, int domainId) {
		return repo.findByNameContainingAndTomcatDomain_Id(name, domainId);
	}

	public List<TomcatInstConfig> getTomcatInstConfigs(int tomcatId) {
		return tomcatInstConfigRepo.findByTomcatInstance_Id(tomcatId);
	}

	public List<TomcatInstanceAppModel> getApplicationByTomcat(int instanceId) {

		TomcatInstance tomcatInstance = findOne(instanceId);
		DomainTomcatConfiguration tomcatConfig = getTomcatConfig(instanceId);
		ProvisionModel pModel = new ProvisionModel(tomcatConfig, tomcatInstance, null);

		List<TomcatInstanceAppModel> apps = new ArrayList<TomcatInstanceAppModel>();
		List<String> logs = proviService.runCommandWithLogs(pModel, "listWebapps.xml");

		boolean involve = false;
		for (String line : logs) {

			if (involve && line.startsWith("  [sshexec]")) {
				apps.add(createTomcatInstanceAppModel(line));
			}

			if (involve == false && line.endsWith("..")) {
				involve = true;
			}
		}

		return apps;
	}

	private TomcatInstanceAppModel createTomcatInstanceAppModel(String line) {
		TomcatInstanceAppModel model = new TomcatInstanceAppModel();

		int pos = line.indexOf("[sshexec]");
		String[] tokens = line.substring(pos + 9).split(" ");
		for (int i = 0; i < tokens.length; i++) {
			System.out.print(tokens[i]);
			System.out.print(",");
		}
		System.out.println("");

		model.setPerm(tokens[1]);
		model.setOwn(tokens[3] + " " + tokens[4]);
		model.setDate(tokens[tokens.length - 3] + " " + tokens[tokens.length - 2]);
		model.setAppName(tokens[tokens.length - 1]);

		return model;
	}

	public TomcatInstance getTomcatInstance(int domainId, int serverId) {
		return repo.findByTomcatDomainIdAndServerId(domainId, serverId);
	}

	public void saveTomcatConfig(TomcatInstance tomcat, DomainTomcatConfiguration conf) {
		DomainTomcatConfiguration domainConfig = domainService.getTomcatConfig(tomcat.getDomainId());
		if (domainConfig == null) {
			return;
		}
		List<TomcatInstConfig> tomcatConfs = new ArrayList<>();
		if (conf != null) {
			// get all fields in domain tomcat config
			Field[] fields = DomainTomcatConfiguration.class.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				// check whether the config property is exist in read-only
				// conf
				// list
				if (Arrays.asList(MeerkatConstants.TOMCAT_INSTANCE_CONFIGS_CUSTOM).contains(name)) {
					String value = "";
					try {
						field.setAccessible(true);
						value = field.get(conf).toString();
						//check whether the value is modified
						if (!field.get(domainConfig).toString().equals(value)) {
							TomcatInstConfig tomcatConf = tomcatInstConfigRepo.findByConfigNameAndTomcatInstance(name, tomcat);
							if (tomcatConf != null) {
								tomcatConf.setConfigValue(value);
							} else {
								tomcatConf = new TomcatInstConfig(tomcat, name, value);
							}
							tomcatConfs.add(tomcatConf);
						}
						field.setAccessible(false);
					} catch (IllegalAccessException e) {

						LOGGER.error(e.toString(), e);
						throw new RuntimeException(e);
					}
				}
			}
			tomcatInstConfigRepo.save(tomcatConfs);
		}

	}

	public List<TomcatInstance> findByDomain(int domainId) {
		return repo.findByTomcatDomain_Id(domainId);
	}

	public long getTomcatInstNo() {
		return repo.count();
	}
}
