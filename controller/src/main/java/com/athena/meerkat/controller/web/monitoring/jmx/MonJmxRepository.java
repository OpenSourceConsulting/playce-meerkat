package com.athena.meerkat.controller.web.monitoring.jmx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MonJmxRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonJmxRepository extends JpaRepository<MonJmx, MonJmxPK> {

	
}