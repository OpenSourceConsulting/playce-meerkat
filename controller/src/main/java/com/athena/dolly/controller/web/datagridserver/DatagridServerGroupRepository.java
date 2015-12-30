package com.athena.dolly.controller.web.datagridserver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatagridServerGroupRepository extends
		JpaRepository<DatagridServerGroup, Integer> {
	List<DatagridServerGroup> findByDomainIdIsNull();
}
