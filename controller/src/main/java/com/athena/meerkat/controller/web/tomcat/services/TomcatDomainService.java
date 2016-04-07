package com.athena.meerkat.controller.web.tomcat.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationReposiroty;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationVersionRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainTomcatConfigurationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatDomainDatasourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;
import com.athena.meerkat.controller.web.tomcat.viewmodels.ClusteringConfComparisionViewModel;

@Service
public class TomcatDomainService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TomcatDomainService.class);
	@Autowired
	private DomainRepository domainRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;
	@Autowired
	private CommonCodeRepository commonRepo;

	@Autowired
	private DomainTomcatConfigurationRepository domainTomcatConfRepo;
	@Autowired
	private ClusteringConfigurationReposiroty clusteringConfRepo;
	@Autowired
	private ApplicationRepository appRepo;
	@Autowired
	private TomcatDomainDatasourceRepository tdDatasoureRepo;
	@Autowired
	private DataSourceRepository dsRepo;

	@Autowired
	private ClusteringConfigurationVersionRepository clusteringConfigVerRepo;

	@Transactional
	public TomcatDomain save(TomcatDomain domain) {
		return domainRepo.save(domain);
	}

	@Transactional
	public DomainTomcatConfiguration saveWithConfig(TomcatDomain domain,
			DomainTomcatConfiguration config) {
		domainRepo.save(domain);

		config.setTomcatDomain(domain);

		return saveDomainTomcatConfig(config);
	}

	@Transactional
	public void saveDatasources(List<TomcatDomainDatasource> datasources) {
		tdDatasoureRepo.save(datasources);
	}

	// public ServiceResult edit(int domainId, String name, boolean
	// is_clustering) {
	// TomcatDomain domain = domainRepo.findOne(domainId);
	// if (domain == null) {
	// return new ServiceResult(Status.FAILED, "Domain does not exist");
	// }
	// domain.setName(name);
	// // domain.setIsClustering(is_clustering);
	// domainRepo.save(domain);
	// return new ServiceResult(Status.DONE, "Done", true);
	// }

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

	public List<ClusteringConfiguration> getClusteringConfigurationList(
			int domainId, int revision) {
		return null;
		// return clusteringConfigRepo.findByDomain_IdAndRevision(domainId,
		// revision);
	}

	public List<ClusteringConfiguration> getClusteringConfigurationByName(
			String name) {
		return clusteringConfRepo.findByName(name);

	}

	// public ClusteringConfiguration saveConfig(ClusteringConfiguration config)
	// {
	// return clusteringConfigRepo.save(config);
	// }
	//

	//
	// public void deleteClusteringConfig(ClusteringConfiguration config) {
	// clusteringConfigRepo.delete(config);
	// }

	public DomainTomcatConfiguration getTomcatConfig(int domainId) {
		return domainTomcatConfRepo.findByTomcatDomain_Id(domainId);
	}

	public List<ClusteringConfigurationVersion> getClusteringConfVersions(
			TomcatDomain td) {
		return clusteringConfRepo.getVersions(td.getId());
	}

	public List<ClusteringConfiguration> getClusteringConf(TomcatDomain td,
			Integer version) {
		List<ClusteringConfiguration> result = clusteringConfRepo
				.findByTomcatDomainAndClusteringConfigurationVersion_Id(td,
						version);
		return result;
	}

	public DomainTomcatConfiguration saveDomainTomcatConfig(
			DomainTomcatConfiguration conf) {
		return domainTomcatConfRepo.save(conf);

	}

	public ClusteringConfigurationVersion getClusteringConfigVersion(int id) {
		return clusteringConfigVerRepo.findOne(id);
	}

	public void saveClusteringConfigs(List<ClusteringConfiguration> confs) {
		clusteringConfRepo.save(confs);
	}

	public void saveClusteringConfig(ClusteringConfiguration config) {
		clusteringConfRepo.save(config);
	}

	public ClusteringConfigurationVersion saveCluteringConfVersion(
			ClusteringConfigurationVersion version) {
		return clusteringConfigVerRepo.save(version);
	}

	public ClusteringConfigurationVersion getLatestClusteringConfVersion(
			int domainId) {
		List<ClusteringConfigurationVersion> latestVersion = clusteringConfigVerRepo
				.findFirstClusteringVersionByDomainId(domainId);
		if (latestVersion.size() == 0) {
			return null;
		}

		return latestVersion.get(0);
	}

	public ClusteringConfiguration getClusteringConfig(int id) {
		return clusteringConfRepo.findOne(id);
	}

	public void deleteClusteringConfig(ClusteringConfiguration config) {
		clusteringConfRepo.delete(config);
	}

	public List<ClusteringConfComparisionViewModel> getClusteringConfComparison(
			Integer domainId, Integer firstVersion, Integer secondVersion) {
		List<ClusteringConfComparisionViewModel> list = new ArrayList<ClusteringConfComparisionViewModel>();
		TomcatDomain td = domainRepo.findOne(domainId);
		if (td != null) {
			List<ClusteringConfiguration> firstList = clusteringConfRepo
					.findByTomcatDomainAndClusteringConfigurationVersion_Id(td,
							firstVersion);
			List<ClusteringConfiguration> secondList = clusteringConfRepo
					.findByTomcatDomainAndClusteringConfigurationVersion_Id(td,
							secondVersion);
			// order by name
			firstList.addAll(secondList);
			Collections.sort(firstList);

			for (int i = 0; i < firstList.size() - 1;) {
				ClusteringConfiguration first = firstList.get(i);
				ClusteringConfiguration second = firstList.get(i + 1);
				if (first.getName().equals(second.getName())) {
					list.add(new ClusteringConfComparisionViewModel(first
							.getId(), first.getName(), first.getValue(), second
							.getId(), second.getValue()));
					i += 2;
				} else {
					i++;
					if (first.getClusteringConfigurationVersion().getId() == firstVersion) {
						list.add(new ClusteringConfComparisionViewModel(first
								.getId(), first.getName(), first.getValue(), 0,
								""));
					} else if (first.getClusteringConfigurationVersion()
							.getId() == secondVersion) {
						list.add(new ClusteringConfComparisionViewModel(0,
								first.getName(), "", first.getId(), first
										.getValue()));
					}
				}
			}

		}
		return list;
	}

	public List<ClusteringConfiguration> searchClusteringConfByDomainAndVersionAndName(
			int domainId, int versionId, String keyword) {
		return clusteringConfRepo
				.findByTomcatDomain_IdAndClusteringConfigurationVersion_IdAndNameContaining(
						domainId, versionId, keyword);
	}

	public List<DataSource> getDatasourceByDomainId(Integer domainId) {
		return dsRepo.getDatasourcesByDomainId(domainId);
	}
}
