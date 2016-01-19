package com.athena.meerkat.controller.web.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.machine.Machine;

@Service
public class SessionService {
	@Autowired
	private SessionRepository sessionRepo;

	public ServiceResult add(String key, String value) {
		Session session = sessionRepo.save(new Session(key, value));
		return new ServiceResult(Status.DONE, "Done", session);
	}

	public ServiceResult edit(int Id, String key, String value) {
		Session s = sessionRepo.findOne(Id);
		if (s == null) {
			return new ServiceResult(Status.FAILED, "Not exist");
		}
		if (!key.equals("") && !value.equals("")) {
			s.setKey(key);
			s.setValue(value);
			sessionRepo.save(s);
			return new ServiceResult(Status.DONE, "Done", s);
		}
		return new ServiceResult(Status.FAILED, "Wrong key or value");
	}

	public ServiceResult delete(int Id) {
		Session s = sessionRepo.findOne(Id);
		if (s == null) {
			return new ServiceResult(Status.FAILED, "Not exist");
		}
		sessionRepo.delete(Id);
		return new ServiceResult(Status.DONE, "Done");
	}

	public ServiceResult retrieve(int Id) {
		Session s = sessionRepo.findOne(Id);
		return new ServiceResult(Status.DONE, "Done", s);
	}

	public ServiceResult retrieve(int page, int size) {
		Page<Session> sessions = sessionRepo
				.findAll(new PageRequest(page, size));
		return new ServiceResult(Status.DONE, "Done", sessions);
	}
}
