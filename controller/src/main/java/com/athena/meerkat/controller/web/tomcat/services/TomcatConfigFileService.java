package com.athena.meerkat.controller.web.tomcat.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatConfigFileRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

@Service
public class TomcatConfigFileService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TomcatDomainService.class);
	@Autowired
	private DomainRepository domainRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;
	@Autowired
	private CommonCodeRepository commonRepo;
	@Autowired
	private TomcatConfigFileRepository tomcatConfigFileRepo;

	public List<TomcatConfigFile> getConfigFileVersions(int domainId,
			String type) {
		CommonCode codeValue = commonRepo.findByCodeNm(type);
		List<TomcatConfigFile> configs = tomcatConfigFileRepo
				.findByTomcatDomain_IdAndFileTypeCdId(domainId,
						codeValue.getId());

		return configs;
	}

	public TomcatConfigFile getConfig(TomcatDomain td, String type, int version) {
		CommonCode codeValue = commonRepo.findByCodeNm(type);
		TomcatConfigFile conf = tomcatConfigFileRepo
				.findByTomcatDomainAndFileTypeCdIdAndVersion(td,
						codeValue.getId(), version);
		return conf;
	}

	public TomcatConfigFile getTomcatConfigFileById(Integer id) {
		return tomcatConfigFileRepo.findOne(id);
	}

	@Transactional
	public TomcatConfigFile saveConfigFile(TomcatConfigFile conf) {
		return tomcatConfigFileRepo.save(conf);
	}

	public List<TomcatConfigFile> getConfigFileVersions(TomcatInstance tomcat,
			String type) {
		List<TomcatConfigFile> configs = tomcatConfigFileRepo
				.getConfiFileOrderByVersionDesc(tomcat.getDomainId(),
						tomcat.getId(), type);

		return configs;
	}

	public TomcatConfigFile getLatestConfVersion(TomcatDomain td,
			TomcatInstance tc, String type) {
		List<TomcatConfigFile> list = null;
		if (td != null) {
			list = tomcatConfigFileRepo.getConfiFileOrderByVersionDesc(
					td.getId(), type);
		} else if (tc != null) {
			list = tomcatConfigFileRepo.getConfiFileOrderByVersionDesc(
					tc.getDomainId(), tc.getId(), type);
		}
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
