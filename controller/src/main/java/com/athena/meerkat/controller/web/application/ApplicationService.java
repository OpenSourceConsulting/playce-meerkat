package com.athena.meerkat.controller.web.application;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.common.State;
import com.athena.meerkat.controller.web.domain.Domain;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;

@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository appRepo;

	public boolean deploy(Application app, Domain domain) {
		// Deploy an application to a domain means deploy to all tomcat
		// assoicated to that domain.
		Collection<TomcatInstance> tomcats = domain.getTomcats();
		for (TomcatInstance tomcat : tomcats) {
			Application newApp = app.clone();
			newApp.setTomcat(tomcat);
			appRepo.save(newApp);
		}
		return true;

	}

	public Application getApplication(int id) {
		return appRepo.findOne(id);
	}

	public boolean start(Application app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(State.APP_STATE_STARTED);
		app.setLastStartedDate(new Date());
		this.save(app);
		success = true;
		return success;
	}

	public boolean stop(Application app) {
		boolean success = false;
		// provisioning
		// ....
		app.setState(State.APP_STATE_STOPPED);
		app.setLastStoppedDate(new Date());
		this.save(app);
		success = true;
		return success;
	}

	public void save(Application app) {
		appRepo.save(app);
	}

	public boolean undeploy(Application app, Domain domain) {
		List<Application> apps = appRepo.findByDisplayNameAndTomcat_Domain_Id(
				app.getDisplayName(), domain.getId());
		for (Application a : apps) {
			app.setTomcat(null);
			appRepo.delete(a);
		}
		return true;

	}
}
