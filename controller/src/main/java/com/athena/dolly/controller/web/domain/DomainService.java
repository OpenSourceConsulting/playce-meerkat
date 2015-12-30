package com.athena.dolly.controller.web.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.dolly.controller.ServiceResult;
import com.athena.dolly.controller.ServiceResult.Status;
import com.athena.dolly.controller.web.datagridserver.DatagridServerGroup;
import com.athena.dolly.controller.web.tomcat.instance.TomcatInstance;
import com.athena.dolly.controller.web.tomcat.instance.TomcatInstanceRepository;

@Service
public class DomainService {
	@Autowired
	private DomainRepository domainRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;

	public Domain save(Domain domain) {
		return domainRepo.save(domain);
	}

	public ServiceResult edit(int domainId, String name, boolean is_clustering) {
		Domain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			return new ServiceResult(Status.FAILED, "Domain does not exist");
		}
		domain.setName(name);
		domain.setClustering(is_clustering);
		domainRepo.save(domain);
		return new ServiceResult(Status.DONE, "Done", true);
	}

	public boolean delete(int domainId) {
		Domain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			//return new ServiceResult(Status.FAILED, "Domain does not exist");
			return false;
		}
		//delete all associated tomcats
		tomcatRepo.delete(domain.getTomcats());
		//delete relation between domain and datagridgroup
		domain.getServerGroup().setDomain(null);
		domainRepo.delete(domain);
		//return new ServiceResult(Status.DONE, "Deleted", true);
		return true;
	}

	public ServiceResult configure(int domainId) {
		return new ServiceResult(Status.DONE, "Not implemented yet");
	}

	public ServiceResult getAll() {
		List<Domain> list = domainRepo.findAll();
		return new ServiceResult(Status.DONE, "Done", list);
	}

	public ServiceResult getApplicationListByDomain(int domainId) {
		Domain domain = domainRepo.findOne(domainId);
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

	public Domain getDomain(int id) {
		Domain domain = domainRepo.findOne(id);
		return domain;
	}
	public Domain getDomainByName(String name)
	{
		return domainRepo.findByName(name);
	}
}
