package com.athena.dolly.controller.web.datagridserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataGridServerRepository extends
		JpaRepository<DatagridServer, Integer> {

}
