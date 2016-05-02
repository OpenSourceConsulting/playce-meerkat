package com.athena.meerkat.controller.web.tomcat.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.provisioning.TomcatProvisioningService;
import com.athena.meerkat.controller.web.provisioning.xml.ContextXmlHandler;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainTomcatConfigurationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatDomainDatasourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

@Service
public class TomcatDomainService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatDomainService.class);

	@Autowired
	private DomainRepository domainRepo;

	@Autowired
	private TomcatInstanceRepository tomcatRepo;

	@Autowired
	private CommonCodeRepository commonRepo;

	@Autowired
	private DomainTomcatConfigurationRepository domainTomcatConfRepo;

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private TomcatDomainDatasourceRepository domainDatasoureRepo;

	@Autowired
	private DataSourceRepository dsRepo;

	@Autowired
	private TomcatConfigFileService confFileService;

	@Autowired
	private CommonCodeHandler codeService;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private CommonCodeHandler codeHandler;
	

	private TomcatProvisioningService provService;

	
	public TomcatProvisioningService getProvService() {
		return provService;
	}

	public void setProvService(TomcatProvisioningService provService) {
		this.provService = provService;
	}

	@Transactional
	public TomcatDomain save(TomcatDomain domain) {
		return domainRepo.save(domain);
	}

	@Transactional
	public DomainTomcatConfiguration saveWithConfig(TomcatDomain domain, DomainTomcatConfiguration config) {
		domainRepo.save(domain);

		config.setTomcatDomain(domain);
		
		config.setCatalinaHome(config.getCatalinaHome() + "/" + codeHandler.getCodeNm(MeerkatConstants.CODE_GROP_TE_VERSION, config.getTomcatVersionCd()));

		return saveNewDomainTomcatConfig(config);
	}

	/**
	 * <pre>
	 * 추가 저장시에는 context.xml 파일 버전도 증가시킨다.
	 * </pre>
	 * @param datasources
	 */
	@Transactional
	public TomcatConfigFile addDatasources(List<TomcatDomainDatasource> datasources) {
		
		domainDatasoureRepo.save(datasources);

		int domainId = datasources.get(0).getTomcatDomainId();
		
		return updateContextXml(domainId);
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param domainId
	 * @return
	 */
	@Transactional
	public TomcatConfigFile updateContextXml(int domainId) {
		
		/*
		 * make updated context.xml contents.
		 */
		TomcatConfigFile contextFile = confFileService.getLatestContextXmlFile(domainId);
		List<DataSource> dsList = getDatasources(domainId);
		
		String contextFilePath = confFileService.getFileFullPath(contextFile);
		ContextXmlHandler contextXml = new ContextXmlHandler(contextFilePath);
		
		contextFile.setContent(contextXml.updateDatasourceContents(dsList));
		
		/*
		 * save new version TomcatConfigFile.
		 */
		entityManager.detach(contextFile);
		contextFile.setId(0);//for insert.
		contextFile.increaseVersion();
		
		return confFileService.saveConfigFile(contextFile, getTomcatConfig(domainId));
	}
	
	/**
	 * <pre>
	 * wizard ui를 통한 추가: 기존 버전의 context.xml 파일만 수정한다. (버전 증가 없음)
	 * </pre>
	 * @param datasources
	 */
	@Transactional
	public void saveFirstDatasources(List<TomcatDomainDatasource> datasources) {
		
		domainDatasoureRepo.save(datasources);
		
		
		int domainId = datasources.get(0).getTomcatDomainId();
		TomcatConfigFile contextFile = confFileService.getLatestContextXmlFile(domainId);
		List<DataSource> dsList = getDatasources(domainId);
		
		String contextFilePath = confFileService.getFileFullPath(contextFile);
		ContextXmlHandler contextXml = new ContextXmlHandler(contextFilePath);
		
		contextXml.updateDatasource(dsList);
	}

	public boolean delete(int domainId) {
		TomcatDomain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			return false;
		}
		// delete all associated tomcats
		// tomcatRepo.delete(domain.getTomcats());
		// delete relation between domain and datagridgroup
		// if (domain.getServerGroup() != null) {
		// domain.getServerGroup().setDomain(null);
		// }
		domainRepo.delete(domain);
		return true;
	}

	public List<TomcatDomain> getAll() {
		return domainRepo.findAll();
	}

	public List<TomcatApplication> getApplicationListByDomain(int domainId) {

		return appRepo.findByTomcatDomain_Id(domainId);
	}

	public TomcatDomain getDomain(int id) {
		TomcatDomain domain = domainRepo.findOne(id);
		return domain;
	}

	public List<TomcatDomain> getDomainByName(String name) {
		return domainRepo.findByName(name);
	}

	public DomainTomcatConfiguration getTomcatConfig(int domainId) {
		return domainTomcatConfRepo.findByTomcatDomain_Id(domainId);
	}

	public DomainTomcatConfiguration saveNewDomainTomcatConfig(DomainTomcatConfiguration conf) {

		/*
		 * save server.xml & context.xml
		 */
		String tomcatVersion = codeService.getCodeNm(MeerkatConstants.CODE_GROP_TE_VERSION,	conf.getTomcatVersionCd());
		confFileService.saveNewTomcatConfigFiles(conf.getTomcatDomain().getId(), tomcatVersion, conf);

		/*
		 * save tomcat config
		 */
		return saveDomainTomcatConfig(conf);

	}

	public DomainTomcatConfiguration saveDomainTomcatConfig(DomainTomcatConfiguration conf) {
		
		return domainTomcatConfRepo.save(conf);
	}

	public List<DataSource> getDatasources(Integer domainId) {
		
		TomcatDomain domain = getDomain(domainId);
		
		return domain.getDatasources();
	}
	
	@Transactional
	public void deleteDomainDatasource(int domainId, int dsId) {
		
		domainDatasoureRepo.deleteByTomcatDomainIdAndDatasourceId(domainId, dsId);
		
		/*
		 * don't delete below.
		 * 
		TomcatDomain domain = getDomain(domainId);
		List<DataSource> mappedDatasources = domain.getDatasources();
		
		for (Iterator<DataSource> iterator = mappedDatasources.iterator(); iterator.hasNext();) {
			DataSource dataSource = iterator.next();
			
			if(dsId == dataSource.getId()) {
				iterator.remove();
				break;
			}
		}
		
		save(domain);//real delete db;
		*/
		
		TomcatConfigFile configFile = updateContextXml(domainId);
		
		getProvService().updateXml(domainId, configFile.getId(), null);
		
	}
}
