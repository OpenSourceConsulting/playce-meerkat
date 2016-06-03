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

import java.io.UnsupportedEncodingException;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.SSHManager;
import com.athena.meerkat.controller.tomcat.instance.domain.ConfigFileVersionRepository;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatConfigFileRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstConfigRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

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

	public TomcatInstanceService() {
		// TODO Auto-generated constructor stub
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

		if (conf == null) {
			LOGGER.debug("DomainTomcatConfiguration is null of domainId({})", domainId);
			return null;
		}

		// get configurations that are different to domain tomcat config
		List<TomcatInstConfig> changedConfigs = getTomcatInstConfigs(instanceId);
		if (changedConfigs != null) {
			for (TomcatInstConfig changedConf : changedConfigs) {
				try {
					BeanUtils.setProperty(conf, changedConf.getConfigName(), changedConf.getConfigValue());
				} catch (Exception e) {
					LOGGER.error(e.toString(), e);
					throw new RuntimeException(e);
				}

				/*
				Method[] methods = DomainTomcatConfiguration.class.getMethods();
				for (Method m : methods) {
					// check the setter method
					// Get the param type
					Class<?>[] types = m.getParameterTypes();
					if (m.getName()
							.toLowerCase()
							.contains(
									"set"
											+ changedConf.getConfigName()
													.toLowerCase())) {
						try {
							// run setter method to update value
							// because the setter has only one param, just get
							// the 1st param
							if (types[0].toString().toLowerCase()
									.contains("string")) {
								m.invoke(conf,
										(String) changedConf.getConfigValue());
							} else if (types[0].toString().toLowerCase()
									.contains("int")) {
								m.invoke(conf, Integer.parseInt(changedConf
										.getConfigValue()));
							} else if (types[0].toString().toLowerCase()
									.contains("boolean")) {
								m.invoke(conf, Boolean.parseBoolean(changedConf
										.getConfigValue()));
							}

						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				*/
			}
		}

		conf.setTomcatInstanceId(instanceId);

		return conf;

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

	public void delete(TomcatInstance tomcat) {
		repo.delete(tomcat);
	}

	public List<DataSource> getDataSourceListByTomcat(TomcatInstance tomcat) {
		// List<DataSource> list = (List<DataSource>) tomcat.getDataSources();
		// return list;
		return null;
	}

	@Async
	public void loadTomcatConfig(TomcatInstance inst) {

		int state = 0;

		// SSHManager sshMng = new SSHManager(inst.getSshUsername(),
		// inst.getSshPassword(), inst.getIpAddr(), "", inst.getSshPort());
		//
		// String errorMsg = sshMng.connect();
		//
		// try {
		// if (errorMsg != null) {
		// inst.setErrMsg(errorMsg);
		// saveState(inst, 1);
		// } else {
		//
		// state = DollyConstants.INSTANCE_STATE_PEND1;
		// loadEnvSH(sshMng, inst, state);
		//
		// state = DollyConstants.INSTANCE_STATE_PEND2;
		// loadServerXML(sshMng, inst, state);
		//
		// state = DollyConstants.INSTANCE_STATE_PEND3;
		// loadContextXML(sshMng, inst, state);
		//
		// state = DollyConstants.INSTANCE_STATE_VALID;
		// saveState(inst, state);
		// }
		//
		// } catch (Exception e) {
		//
		// logger.error("", e);
		// state++;
		// inst.setErrMsg(e.toString());
		// saveState(inst, state);
		//
		// } finally {
		// sshMng.close();
		// }

	}

	/**
	 * env.sh 파일을 로딩 & 없으면 생성??
	 * 
	 * @param inst
	 */
	public void loadEnvSH(SSHManager sshMng, TomcatInstance inst, int state) throws UnsupportedEncodingException {

		// String remoteFile = inst.getCatalinaBase()
		// + inst.getEnvScriptFile().replaceFirst("$CATALINA_BASE", "");
		//
		// // sshMng.scpDown(remoteFile, "D:/env.sh");
		//
		// byte[] fileByte = sshMng.scpDown(remoteFile);
		//
		// String envStr = new String(fileByte, "UTF-8");
		//
		// QConfigFileVersion confVer = QConfigFileVersion.configFileVersion;
		// EntityManager entityManager = this.entityManagerFactory
		// .createEntityManager();
		// JPAQuery query = new JPAQuery(entityManager);
		//
		// Integer maxRevision = query
		// .from(confVer)
		// .where(confVer.tomcatInstanceId.eq(inst.getId()),
		// confVer.fileType.eq(1))
		// .uniqueResult(confVer.revision.max());
		//
		// int revision = 0;
		// if (maxRevision != null) {
		// revision = maxRevision.intValue();
		// }
		//
		// ConfigFileVersion fileVer = new ConfigFileVersion(inst.getId(), 1,
		// remoteFile, envStr);
		// fileVer.setRevision(revision + 1);
		//
		// configRepo.save(fileVer);
		//
		// saveState(inst, state);
	}

	public void saveState(int instanceId, int state) {
		repo.setState(instanceId, state);

		LOGGER.debug("tomcat instance({}) state({}) saved.", instanceId, state);
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

	public List<TomcatApplication> getApplicationByTomcat(int id) {
		return appRepo.findByTomcatInstance_Id(id);
	}

	public void saveTomcatConfig(TomcatInstance tomcat, DomainTomcatConfiguration conf) {
		List<TomcatInstConfig> tomcatConfs = new ArrayList<>();
		if (conf != null) {
			// get all fields in domain tomcat config
			Field[] fields = DomainTomcatConfiguration.class.getDeclaredFields();
			for (Field field : fields) {
				// check whether the config property is exist in read-only
				// conf
				// list
				String name = field.getName();
				if (Arrays.asList(MeerkatConstants.TOMCAT_INSTANCE_CONFIGS_CUSTOM).contains(name)) {
					String value = "";
					try {
						field.setAccessible(true);
						value = field.get(conf).toString();
						field.setAccessible(false);
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					TomcatInstConfig tomcatConf = tomcatInstConfigRepo.findByConfigNameAndTomcatInstance(name, tomcat);
					if (tomcatConf != null) {
						tomcatConf.setConfigValue(value);
					} else {
						tomcatConf = new TomcatInstConfig(tomcat, name, value);
					}
					tomcatConfs.add(tomcatConf);
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
