package com.athena.meerkat.controller.web.tomcat.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.code.CommonCodeHandler;
import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.CommonCode;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatConfigFileRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

@Service
public class TomcatConfigFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatDomainService.class);

	private static final String RMI_DIS_START = "<!--am.rmi.disable.start";
	private static final String RMI_DIS_END = "am.rmi.disable.end-->";
	private static final String RMI_EN_START = "<!--am.rmi.enable.start-->";
	private static final String RMI_EN_END = "<!--am.rmi.enable.end-->";

	@Autowired
	private DomainRepository domainRepo;

	@Autowired
	private TomcatInstanceRepository tomcatRepo;

	@Autowired
	private CommonCodeRepository commonRepo;

	@Autowired
	private TomcatConfigFileRepository tomcatConfigFileRepo;

	@Autowired
	private CommonCodeHandler codeHandler;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${meerkat.commander.home}")
	private String cmdHome;

	@Value("${meerkat.config.dir:conffiles}")
	private String configFileDir;

	public List<TomcatConfigFile> getConfigFileVersions(int domainId, String type) {
		CommonCode codeValue = commonRepo.findByCodeNm(type);
		List<TomcatConfigFile> configs = tomcatConfigFileRepo.findByTomcatDomain_IdAndFileTypeCdId(domainId, codeValue.getId());

		return configs;
	}

	public TomcatConfigFile getConfig(TomcatDomain td, String type, int version) {
		CommonCode codeValue = commonRepo.findByCodeNm(type);
		TomcatConfigFile conf = tomcatConfigFileRepo.findByTomcatDomainAndFileTypeCdIdAndVersion(td, codeValue.getId(), version);
		return conf;
	}

	public TomcatConfigFile getTomcatConfigFileById(Integer id) {
		return tomcatConfigFileRepo.findOne(id);
	}

	/**
	 * <pre>
	 * server.xml or context.xml 파일을 db & filesystem 같이 저장한다.
	 * - version 을 증가시키지는 않는다.
	 * - xmlConfig 의 content field 가 not null 이어야 함.
	 * </pre>
	 * 
	 * @param xmlConfig
	 * @return
	 */
	@Transactional
	public TomcatConfigFile saveConfigFile(TomcatConfigFile xmlConfig, DomainTomcatConfiguration tomcatConf) {

		/*
		 * save to filesystem.
		 */
		String filePath = getDomainFilePath(xmlConfig.getTomcatDomain().getId(), xmlConfig.getTomcatInstance());
		filePath = filePath + File.separator + getFileTypeName(xmlConfig.getFileTypeCdId(), xmlConfig.getVersion());

		File newFile = new File(getFileFullPath(filePath));

		updateRMIConfig(tomcatConf, xmlConfig, xmlConfig.getContent());

		try {
			FileUtils.writeStringToFile(newFile, xmlConfig.getContent(), MeerkatConstants.UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		xmlConfig.setFilePath(filePath);

		/*
		 * save to db
		 */
		return tomcatConfigFileRepo.save(xmlConfig);
	}

	/**
	 * <pre>
	 * server.xml 의 rmi 설정 주석 update.
	 * </pre>
	 * 
	 * @param conf
	 * @param confContents
	 */
	private void updateRMIConfig(DomainTomcatConfiguration tomcatConfig, TomcatConfigFile xmlConfig, String confContents) {

		if (MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD != xmlConfig.getFileTypeCdId()) {
			return;
		}

		String updatedContents = null;
		if (tomcatConfig.isJmxEnable()) {
			updatedContents = confContents.replace(RMI_DIS_START, RMI_EN_START).replace(RMI_DIS_END, RMI_EN_END);
		} else {
			updatedContents = confContents.replace(RMI_EN_START, RMI_DIS_START).replace(RMI_EN_END, RMI_DIS_END);
		}
		xmlConfig.setContent(updatedContents);
	}

	public List<TomcatConfigFile> getConfigFileVersions(TomcatInstance tomcat, int fileTypeCdId) {
		List<TomcatConfigFile> configs = tomcatConfigFileRepo.getConfiFileOrderByVersionDesc(tomcat.getDomainId(), tomcat.getId(), fileTypeCdId);

		return configs;
	}

	public TomcatConfigFile getLatestConfVersion(TomcatDomain td, TomcatInstance tc, int fileTypeCdId) {
		List<TomcatConfigFile> list = null;
		if (td != null) {
			list = tomcatConfigFileRepo.getConfiFileOrderByVersionDesc(td.getId(), fileTypeCdId);
		} else if (tc != null) {
			list = tomcatConfigFileRepo.getConfiFileOrderByVersionDesc(tc.getDomainId(), tc.getId(), fileTypeCdId);
		}
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public TomcatConfigFile getLatestContextXmlFile(int domainId) {

		return tomcatConfigFileRepo.getLatestConfigFile(domainId, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD);
	}

	public TomcatConfigFile getLatestServerXmlFile(int domainId) {

		return tomcatConfigFileRepo.getLatestConfigFile(domainId, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD);
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param configFileId
	 * @return
	 */
	public String getConfigFileContents(Integer configFileId) {
		TomcatConfigFile file = getTomcatConfigFileById(configFileId);

		return getConfigFileContents(file.getFilePath());
	}

	public String getConfigFileContents(String filePath) {
		String contents = null;
		try {
			contents = FileUtils.readFileToString(new File(getFileFullPath(filePath)), MeerkatConstants.UTF8);
		} catch (IOException e) {
			//throw new RuntimeException(e);
			contents = e.toString(); //TODO this is temporary code.
		}

		return contents;
	}

	/**
	 * <pre>
	 * save initial server.xml & context.xml
	 * </pre>
	 * 
	 * @param domainId
	 * @param tomcatVersion
	 */
	public void saveNewTomcatConfigFiles(int domainId, String tomcatVersion, DomainTomcatConfiguration conf) {
		TomcatConfigFile serverXml = createNewTomcatConfigFile(tomcatVersion, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD);
		serverXml.setDominaId(domainId);

		TomcatConfigFile contextXml = createNewTomcatConfigFile(tomcatVersion, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD);
		contextXml.setDominaId(domainId);

		saveConfigFile(serverXml, conf);
		saveConfigFile(contextXml, conf);
	}

	private TomcatConfigFile createNewTomcatConfigFile(String tomcatVersion, int fileTypeCdId) {
		TomcatConfigFile tcFile = new TomcatConfigFile();

		try {
			tcFile.setFileTypeCdId(fileTypeCdId);
			tcFile.setVersion(1);
			tcFile.setContent(readNewConfigFile(tomcatVersion, fileTypeCdId));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return tcFile;
	}

	private String readNewConfigFile(String tomcatVersion, int fileTypeCdId) throws IOException {

		String fileName = getFileTypeName(fileTypeCdId, 0);

		//TODO read tomcat version's file;

		Resource file = resourceLoader.getResource("classpath:/tomcatconfigs/" + fileName);

		return FileUtils.readFileToString(file.getFile(), MeerkatConstants.UTF8);
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param fileTypeCdId
	 * @param version
	 * @return
	 */
	public String getFileTypeName(int fileTypeCdId, int version) {
		String fileName = codeHandler.getFileTypeName(fileTypeCdId);

		if (version == 0) {
			return fileName;
		}

		int pos = fileName.lastIndexOf(".");

		if (pos > -1) {
			return fileName.substring(0, pos) + "_" + version + fileName.substring(pos);
		}

		return fileName + "_" + version;
	}

	public String getDomainFilePath(int domainId, TomcatInstance ti) {
		String filePath = configFileDir + File.separator + domainId + "_" + getTiPath(ti);

		return filePath;
	}

	private String getTiPath(TomcatInstance ti) {
		return ti == null ? null : String.valueOf(ti.getId());
	}

	public String getFileFullPath(TomcatConfigFile confFile) {
		return getFileFullPath(confFile.getFilePath());
	}

	public String getFileFullPath(String filePath) {
		return cmdHome + File.separator + filePath;
	}

	public void delete(List<TomcatConfigFile> confFiles) {
		tomcatConfigFileRepo.delete(confFiles);

	}

}
