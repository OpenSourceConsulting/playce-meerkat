package com.athena.meerkat.controller.web.tomcat.services;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;

@Service
public class ApplicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);
	
	@Autowired
	private ApplicationRepository appRepo;
	
	@Autowired
	private TomcatConfigFileService tcfService;

	public TomcatApplication getApplication(int id) {
		return appRepo.findOne(id);
	}
/*
	public boolean start(TomcatApplication app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(MeerkatConstants.APP_STATE_STARTED);
		// app.setLastStartedDate(new Date());
		this.update(app);
		success = true;
		return success;
	}

	public boolean stop(TomcatApplication app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(MeerkatConstants.APP_STATE_STOPPED);
		// app.setLastStoppedDate(new Date());
		this.update(app);
		success = true;
		return success;
	}
	*/
	
	/**
	 * <pre>
	 * save to filesystem and db
	 * </pre>
	 * @param app
	 * @return saved war file path.
	 */
	public TomcatApplication saveFileAndData(TomcatApplication app) {
		
		MultipartFile warFile = app.getWarFile();
		String filePath = tcfService.getDomainFilePath(app.getTomcatDomain().getId(), null) + File.separator + warFile.getOriginalFilename();
		
		File savePath = new File(tcfService.getFileFullPath(filePath));
		
		try{
			/*
			 * save to filesystem.
			 */
			warFile.transferTo(savePath);
			LOGGER.debug("saved {}", savePath.getAbsolutePath());
			
		}catch(IOException e){
			LOGGER.error(e.toString(), e);
			throw new RuntimeException(e);
		}
		
		/*
		 * save to db
		 */
		app.setWarPath(filePath);
		appRepo.save(app);
		
		return app;
	}
	
	public void update(TomcatApplication app) {
		appRepo.save(app);
	}

	public boolean delete(TomcatApplication app) {
		appRepo.delete(app);
		return true;

	}
}
