package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DatagridServerGroup;

@Repository
public interface DatagridServerGroupRepository extends
		JpaRepository<DatagridServerGroup, Integer> {
	//List<DatagridServerGroup> findByDomainIdIsNull();
}
