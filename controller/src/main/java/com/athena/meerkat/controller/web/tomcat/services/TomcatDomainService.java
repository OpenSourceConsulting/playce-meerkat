package com.athena.meerkat.controller.web.tomcat.services;

import java.util.List;

import org.infinispan.configuration.cache.ClusteringConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

@Service
public class TomcatDomainService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TomcatDomainService.class);
	@Autowired
	private DomainRepository domainRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;

	// @Autowired
	// private ClusteringConfigurationRepository clusteringConfigRepo;

	@Transactional
	public TomcatDomain save(TomcatDomain domain) {
		return domainRepo.save(domain);
	}

	public ServiceResult edit(int domainId, String name, boolean is_clustering) {
		TomcatDomain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			return new ServiceResult(Status.FAILED, "Domain does not exist");
		}
		domain.setName(name);
		//domain.setIsClustering(is_clustering);
		domainRepo.save(domain);
		return new ServiceResult(Status.DONE, "Done", true);
	}

	public boolean delete(int domainId) {
		TomcatDomain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			return false;
		}
		// delete all associated tomcats
		//tomcatRepo.delete(domain.getTomcats());
		// delete relation between domain and datagridgroup
		//if (domain.getServerGroup() != null) {
			// domain.getServerGroup().setDomain(null);
		//}
		domainRepo.delete(domain);
		return true;
	}

	public ServiceResult configure(int domainId) {
		return new ServiceResult(Status.DONE, "Not implemented yet");
	}

	public List<TomcatDomain> getAll() {
		return domainRepo.findAll();
	}

	public List<TomcatApplication> getApplicationListByDomain(int domainId) {
		TomcatDomain domain = domainRepo.findOne(domainId);
		// Tomcat instances that are belonged to same domain have same
		// applications. Only retrieve these application for specified domain
		if (domain != null) {
			//List<TomcatInstance> tomcats = (List<TomcatInstance>) domain.getTomcats();
			// return (List<TomcatApplication>)
			// tomcats.get(0).getApplications();
		}
		return null;
	}

	public TomcatDomain getDomain(int id) {
		TomcatDomain domain = domainRepo.findOne(id);
		return domain;
	}

	public List<TomcatDomain> getDomainByName(String name) {
		return domainRepo.findByName(name);
	}

	public List<ClusteringConfiguration> getClusteringConfigurationList(
			int domainId, int revision) {
		return null;
		// return clusteringConfigRepo.findByDomain_IdAndRevision(domainId,
		// revision);
	}

	// public List<ClusteringConfiguration> getClusteringConfigurationByName(
	// String name) {
	// return clusteringConfigRepo.findByName(name);
	// }
	//
	// public ClusteringConfiguration saveConfig(ClusteringConfiguration config)
	// {
	// return clusteringConfigRepo.save(config);
	// }
	//
	// public ClusteringConfiguration getConfig(int id) {
	// return clusteringConfigRepo.findOne(id);
	// }
	//
	// public void deleteClusteringConfig(ClusteringConfiguration config) {
	// clusteringConfigRepo.delete(config);
	// }
}
