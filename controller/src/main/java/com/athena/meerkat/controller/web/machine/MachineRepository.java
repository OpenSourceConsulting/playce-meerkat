package com.athena.meerkat.controller.web.machine;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("machineRepository")
public interface MachineRepository extends JpaRepository<Machine, Integer>,
		PagingAndSortingRepository<Machine, Integer> {
	Page<Machine> findAll(Pageable pageable);

	List<Machine> findByNameOrSshIPAddr(String name, String sshIPAddr);
}
