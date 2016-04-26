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
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatConfigFileRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

@Service
public class TomcatConfigFileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatDomainService.class);
	
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
	 * version 을 증가시키지는 않는다.
	 * </pre>
	 * @param conf
	 * @return
	 */
	@Transactional
	public TomcatConfigFile saveConfigFile(TomcatConfigFile conf) {
		
		/*
		 * save to filesystem.
		 */
		String filePath = getDomainFilePath(conf.getTomcatDomain().getId(), conf.getTomcatInstance());
		filePath = filePath + File.separator + getFileTypeName(conf.getFileTypeCdId(), conf.getVersion());
		
		File newFile = new File(getFileFullPath(filePath));
		
		try{
			FileUtils.writeStringToFile(newFile, conf.getContent(), MeerkatConstants.UTF8);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		conf.setFilePath(filePath);
		
		/*
		 * save to db
		 */
		return tomcatConfigFileRepo.save(conf);
	}

	public List<TomcatConfigFile> getConfigFileVersions(TomcatInstance tomcat, int fileTypeCdId) {
		List<TomcatConfigFile> configs = tomcatConfigFileRepo
				.getConfiFileOrderByVersionDesc(tomcat.getDomainId(), tomcat.getId(), fileTypeCdId);

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
	
	public TomcatConfigFile getLatestContextConfigFile(int domainId) {
		
		return tomcatConfigFileRepo.getLatestConfigFile(domainId, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD);
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param configFileId
	 * @return
	 */
	public String getConfigFileContents(Integer configFileId) {
		TomcatConfigFile file = getTomcatConfigFileById(configFileId);
		
		return getConfigFileContents(file.getFilePath());
	}
	
	public String getConfigFileContents(String filePath) {
		String contents = null;
		try{
			contents = FileUtils.readFileToString(new File(getFileFullPath(filePath)), MeerkatConstants.UTF8);
		}catch(IOException e){
			//throw new RuntimeException(e);
			contents = e.toString(); //TODO this is temporary code.
		}
		
		return contents;
	}
	
	/**
	 * <pre>
	 * save initial server.xml & context.xml
	 * </pre>
	 * @param domainId
	 * @param tomcatVersion
	 */
	public void saveNewTomcatConfigFiles(int domainId, String tomcatVersion) {
		TomcatConfigFile serverXml = createNewTomcatConfigFile(tomcatVersion, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD);
		serverXml.setDominaId(domainId);
		
		TomcatConfigFile contextXml = createNewTomcatConfigFile(tomcatVersion, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD);
		contextXml.setDominaId(domainId);
		
		saveConfigFile(serverXml);
		saveConfigFile(contextXml);
	}
	
	private TomcatConfigFile createNewTomcatConfigFile(String tomcatVersion, int fileTypeCdId) {
		TomcatConfigFile tcFile = new TomcatConfigFile();
		
		try{
			tcFile.setFileTypeCdId(fileTypeCdId);
			tcFile.setVersion(1);
			tcFile.setContent(readNewConfigFile(tomcatVersion, fileTypeCdId));
			
		}catch(IOException e){
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
	
	public String getFileTypeName(int fileTypeCdId, int version) {
		String fileName = codeHandler.getFileTypeName(fileTypeCdId);
		
		if(version == 0) {
			return fileName;
		}
		
		int pos = fileName.lastIndexOf(".");
		
		if(pos > -1) {
			return fileName.substring(0, pos) + "_" + version + fileName.substring(pos);
		}
		
		return fileName + "_" + version;
	}
	
	public String getDomainFilePath(int domainId, TomcatInstance ti){
		String filePath = configFileDir + File.separator + domainId + "_" + getTiPath(ti);
		
		return filePath;
	}
	
	private String getTiPath(TomcatInstance ti) {
		return ti == null? null: String.valueOf(ti.getId());
	}
	
	public String getFileFullPath(TomcatConfigFile confFile){
		return getFileFullPath(confFile.getFilePath());
	}
	
	public String getFileFullPath(String filePath){
		return cmdHome + File.separator + filePath;
	}
	
}
