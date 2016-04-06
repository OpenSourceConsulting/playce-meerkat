package com.athena.meerkat.controller.web.monitoring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MonDataRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonDataRepository extends JpaRepository<MonData, MonDataPK> {

	
}