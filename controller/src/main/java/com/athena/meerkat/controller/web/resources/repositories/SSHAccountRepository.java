package com.athena.meerkat.controller.web.resources.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athena.meerkat.controller.web.entities.SshAccount;

public interface SSHAccountRepository extends
		JpaRepository<SshAccount, Integer> {

	SshAccount findByUsername(String username);
	
}
