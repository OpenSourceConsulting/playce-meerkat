package com.athena.dolly.controller.web.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.web.tomcat.instance.TomcatInstance;

@Service
public class DomainService {
	@Autowired
	private DomainRepository domainRepo;

	public ServiceResult add(String name, boolean is_clusering) {
		Domain domain = new Domain(name, is_clusering);
		domainRepo.save(domain);
		return new ServiceResult(Status.DONE, "Done", true);
	}

	public ServiceResult edit(int domainId, String name, boolean is_clustering) {
		Domain domain = domainRepo.getOne(domainId);
		if (domain == null) {
			return new ServiceResult(Status.FAILED, "Domain does not exist");
		}
		domain.setName(name);
		domain.setClustering(is_clustering);
		domainRepo.save(domain);
		return new ServiceResult(Status.DONE, "Done", true);
	}

	public ServiceResult delete(int domainId) {
		Domain domain = domainRepo.getOne(domainId);
		if (domain == null) {
			return new ServiceResult(Status.FAILED, "Domain does not exist");
		}
		domainRepo.delete(domain);
		return new ServiceResult(Status.DONE, "Deleted", true);
	}

	public ServiceResult configure(int domainId) {
		return new ServiceResult(Status.DONE, "Not implemented yet");
	}

	public ServiceResult getAll() {
		List<Domain> list = domainRepo.findAll();
		return new ServiceResult(Status.DONE, "Done", list);
	}

	public ServiceResult getApplicationListByDomain(int domainId) {
		Domain domain = domainRepo.getOne(domainId);
		// Tomcat instances that are belonged to same domain have same
		// applications. Only retrieve these application for specified domain
		if (domain != null) {
			List<TomcatInstance> tomcats = (List<TomcatInstance>) domain
					.getTomcats();
			if (tomcats != null) {
				return new ServiceResult(Status.DONE, "", tomcats.get(0)
						.getApplications());
			}
		}
		return new ServiceResult(Status.FAILED, "Domain does not exist");
	}
}
