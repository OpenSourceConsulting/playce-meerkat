package com.athena.meerkat.controller.web.resources.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DatagridServerGroup;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.resources.repositories.DatagridServerGroupRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationReposiroty;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationVersionRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.viewmodels.ClusteringConfComparisionViewModel;

@Service
public class ClusteringConfigurationService {
	@Autowired
	ClusteringConfigurationVersionRepository clusteringConfigVerRepo;
	@Autowired
	ClusteringConfigurationReposiroty clusteringConfigRepo;
	@Autowired
	DomainRepository domainRepo;
	@Autowired
	DatagridServerGroupRepository groupRepo;

	public ClusteringConfigurationVersion getLatestClusteringConfVersionByServerGroup(Integer groupId) {
		List<ClusteringConfigurationVersion> latestVersion = clusteringConfigVerRepo.findFirstClusteringVersionByDatagridGroupId(groupId);
		if (latestVersion.size() == 0) {
			return null;
		}
		return latestVersion.get(0);

	}

	public ClusteringConfigurationVersion getLatestClusteringConfVersionByDomain(Integer domainId) {
		List<ClusteringConfigurationVersion> latestVersion = clusteringConfigVerRepo.findFirstClusteringVersionByDomainId(domainId);
		if (latestVersion.size() == 0) {
			return null;
		}
		return latestVersion.get(0);

	}

	public List<ClusteringConfigurationVersion> getClusteringConfVersionsByGroup(DatagridServerGroup group) {
		return clusteringConfigRepo.getVersionsByServerGroup(group.getId());

	}

	public List<ClusteringConfiguration> getClusteringConfByDomain(TomcatDomain domain, Integer version) {
		List<ClusteringConfiguration> result = clusteringConfigRepo.findByTomcatDomainAndClusteringConfigurationVersion_Id(domain, version);
		return result;
	}

	public List<ClusteringConfiguration> getClusteringConfByServerGroup(DatagridServerGroup group, Integer version) {
		List<ClusteringConfiguration> result = clusteringConfigRepo.findByDatagridServerGroupAndClusteringConfigurationVersion_Id(group, version);
		return result;
	}

	public ClusteringConfiguration getClusteringConfig(int id) {
		return clusteringConfigRepo.findOne(id);
	}

	public void deleteClusteringConfig(ClusteringConfiguration config) {
		clusteringConfigRepo.delete(config);

	}

	public void saveClusteringConfigs(List<ClusteringConfiguration> confs) {
		clusteringConfigRepo.save(confs);
	}

	public ClusteringConfigurationVersion saveCluteringConfVersion(ClusteringConfigurationVersion version) {
		return clusteringConfigVerRepo.save(version);
	}

	public List<ClusteringConfiguration> searchClusteringConfByDomainAndVersionAndName(int domainId, int versionId, String keyword) {
		return clusteringConfigRepo.findByTomcatDomain_IdAndClusteringConfigurationVersion_IdAndNameContaining(domainId, versionId, keyword);
	}

	public List<ClusteringConfiguration> searchClusteringConfByServerGroupAndVersionAndName(Integer groupId, int versionId, String keyword) {
		return clusteringConfigRepo.findByDatagridServerGroup_IdAndClusteringConfigurationVersion_IdAndNameContaining(groupId, versionId, keyword);
	}

	public List<ClusteringConfComparisionViewModel> getClusteringConfComparison(Integer objectId, Integer firstVersion, Integer secondVersion) {
		List<ClusteringConfComparisionViewModel> list = new ArrayList<ClusteringConfComparisionViewModel>();
		List<ClusteringConfiguration> firstList = new ArrayList<ClusteringConfiguration>();
		List<ClusteringConfiguration> secondList = new ArrayList<ClusteringConfiguration>();

		DatagridServerGroup group = groupRepo.findOne(objectId);
		if (group != null) {
			firstList = clusteringConfigRepo.findByDatagridServerGroupAndClusteringConfigurationVersion_Id(group, firstVersion);
			secondList = clusteringConfigRepo.findByDatagridServerGroupAndClusteringConfigurationVersion_Id(group, secondVersion);
		}

		// order by name
		firstList.addAll(secondList);
		Collections.sort(firstList);

		for (int i = 0; i < firstList.size();) {

			ClusteringConfiguration first = firstList.get(i);
			ClusteringConfiguration second = null;

			if (i + 1 < firstList.size()) {
				second = firstList.get(i + 1);
			}
			if (second != null && first.getName().equals(second.getName())) {
				list.add(new ClusteringConfComparisionViewModel(first.getId(), first.getName(), first.getValue(), second.getId(), second.getValue()));
				i += 2;
			} else {
				if (first.getClusteringConfigurationVersion().getId() == firstVersion) {
					list.add(new ClusteringConfComparisionViewModel(first.getId(), first.getName(), first.getValue(), 0, ""));
				} else if (first.getClusteringConfigurationVersion().getId() == secondVersion) {
					list.add(new ClusteringConfComparisionViewModel(0, first.getName(), "", first.getId(), first.getValue()));
				}
				i++;
			}

		}

		return list;
	}

	public List<ClusteringConfigurationVersion> getClusteringConfVersionsByDomain(TomcatDomain td) {
		return clusteringConfigRepo.getVersionsByDomain(td.getId());
	}

	public List<ClusteringConfiguration> getClusteringConfigurationByName(String name) {
		return clusteringConfigRepo.findByName(name);
	}

	public ClusteringConfigurationVersion getClusteringConfigVersion(int id) {
		return clusteringConfigVerRepo.findOne(id);
	}

	public void saveClusteringConfig(ClusteringConfiguration config) {
		clusteringConfigRepo.save(config);
	}

}
