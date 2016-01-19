package com.athena.meerkat.controller.web.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;

@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository appRepo;

	public ServiceResult add(String name, String context_path, String war_path,
			String version) {
		if (context_path.isEmpty() || war_path.isEmpty() || name.isEmpty()
				|| version.isEmpty()) {
			return new ServiceResult(Status.FAILED, "Invalid data");
		}
		Application app = new Application(name, context_path, war_path, version);
		app.setLastModifiedDate(new Date());
		appRepo.save(app);
		return new ServiceResult(Status.FAILED, "Done", app);
	}

	public ServiceResult edit(int appId, String context_path, String war_path,
			String name, String version) {
		Application app = appRepo.getOne(appId);
		if (app == null) {
			return new ServiceResult(Status.FAILED,
					"Application does not exist");
		}
		if (context_path.isEmpty() || war_path.isEmpty() || name.isEmpty()
				|| version.isEmpty()) {
			return new ServiceResult(Status.FAILED, "Invalid data");
		}
		app.setDisplayName(name);
		app.setContextPath(context_path);
		app.setWarPath(war_path);
		app.setVersion(version);
		app.setLastModifiedDate(new Date());
		appRepo.save(app);

		return new ServiceResult(Status.DONE, "Done", app);
	}

	public ServiceResult delete(int appId) {
		Application app = appRepo.getOne(appId);
		if (app == null) {
			return new ServiceResult(Status.FAILED,
					"Application does not exist");
		}
		appRepo.delete(app);
		return new ServiceResult(Status.DONE, "Done", true);
	}
}
