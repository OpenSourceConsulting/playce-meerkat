package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.Server;

@Repository("serverRepository")
public interface ServerRepository extends JpaRepository<Server, Integer>,
		PagingAndSortingRepository<Server, Integer> {
	Page<Server> findAll(Pageable pageable);

	Server findByName(String name);

	List<Server> findByDatagridServerGroup_Id(Integer id);

	// List<Server> findByMachineServerType(int type);
	// List<Server> findByNameOrSshIPAddr(String name, String sshIPAddr);
}
