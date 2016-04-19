package com.athena.meerkat.controller.web.monitoring.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MonFsRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonFsRepository extends JpaRepository<MonFs, MonFsPK> {

	
}