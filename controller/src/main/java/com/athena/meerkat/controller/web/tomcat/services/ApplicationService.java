package com.athena.meerkat.controller.web.tomcat.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.common.State;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;

@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository appRepo;

	public boolean deploy(TomcatApplication app, TomcatDomain domain) {
		/*
		// Deploy an application to a domain means deploy to all tomcat
		// assoicated to that domain.
		Collection<TomcatInstance> tomcats = domain.getTomcats();
		for (TomcatInstance tomcat : tomcats) {
			TomcatApplication newApp = app.clone();
		//	newApp.setTomcat(tomcat);
			appRepo.save(newApp);
		}
		*/
		return true;

	}

	public TomcatApplication getApplication(int id) {
		return appRepo.findOne(id);
	}

	public boolean start(TomcatApplication app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(State.APP_STATE_STARTED);
		//app.setLastStartedDate(new Date());
		this.save(app);
		success = true;
		return success;
	}

	public boolean stop(TomcatApplication app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(State.APP_STATE_STOPPED);
		//app.setLastStoppedDate(new Date());
		this.save(app);
		success = true;
		return success;
	}

	public void save(TomcatApplication app) {
		appRepo.save(app);
	}

	public boolean undeploy(TomcatApplication app, TomcatDomain domain) {
		// List<TomcatApplication> apps =
		// appRepo.findByDisplayNameAndTomcat_Domain_Id(
		// app.getDisplayName(), domain.getId());
		// for (TomcatApplication a : apps) {
		// // app.setTomcat(null);
		// appRepo.delete(a);
		// }
		return true;

	}
}
