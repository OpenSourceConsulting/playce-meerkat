package com.athena.meerkat.controller.web.monitoring.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MonUtilStatRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface MonUtilStatRepository extends JpaRepository<MonUtilStat, Integer> {

	
}