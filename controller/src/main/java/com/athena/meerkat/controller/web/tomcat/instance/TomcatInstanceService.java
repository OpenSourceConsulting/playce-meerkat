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
package com.athena.meerkat.controller.web.tomcat.instance;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.common.SSHManager;
import com.athena.meerkat.controller.tomcat.instance.domain.ConfigFileVersionRepository;
import com.athena.meerkat.controller.web.datasource.Datasource;
import com.athena.meerkat.controller.web.datasource.DatasourceRepository;

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

	static final Logger logger = LoggerFactory
			.getLogger(TomcatInstanceService.class);

	@Autowired
	private TomcatInstanceRepository repo;

	@Autowired
	private DatasourceRepository datasourceRepo;

	@Autowired
	private ConfigFileVersionRepository configRepo;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public TomcatInstanceService() {
		// TODO Auto-generated constructor stub
	}

	public Page<TomcatInstance> getList(Pageable pageable) {
		return repo.findAll(pageable);
	}

	public List<TomcatInstance> getTomcatListByDomainId(int domainId) {
		return repo.findByDomainId(domainId);
	}

	/**
	 * insert or update
	 * 
	 * @param inst
	 */
	public TomcatInstance save(TomcatInstance inst) {
		return repo.save(inst);
	}

	public TomcatInstance getOne(Long id) {
		return repo.findOne(id);
	}

	public void delete(Long id) {
		repo.delete(id);
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
	public void loadEnvSH(SSHManager sshMng, TomcatInstance inst, int state)
			throws UnsupportedEncodingException {

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

	public void loadServerXML(SSHManager sshMng, TomcatInstance inst, int state) {

		saveState(inst, state);
	}

	public void loadContextXML(SSHManager sshMng, TomcatInstance inst, int state) {

		saveState(inst, state);
	}

	private void saveState(TomcatInstance inst, int state) {
		// inst.setState(state);
		// repo.save(inst);
	}

	/**
	 * Start a tomcat instance
	 * 
	 * @param id
	 * @return status of process. True for success
	 */
	public boolean start(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Restart tomcat instance
	 * 
	 * @param id
	 * @return status of process. True for success
	 */
	public boolean restart(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Stop tomcat instance
	 * 
	 * @param id
	 * @return
	 */
	public boolean stop(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public ServiceResult getAssociatedTomcatList(int dataSourceId) {
		Datasource ds = datasourceRepo.findOne(dataSourceId);
		if (ds != null) {
			return new ServiceResult(Status.DONE, "", ds.getTomcatInstances());
		}
		return new ServiceResult(Status.FAILED, "");
	}

	public ServiceResult editAssoicateTomcatList(int dataSourceId,
			List<Integer> removedAssociatedTomcatId) {
		Datasource ds = datasourceRepo.findOne(dataSourceId);
		if (ds != null) {

		}
		return null;
	}

	public ServiceResult getAll() {
		return new ServiceResult(Status.DONE, "", repo.findAll());
	}

}
