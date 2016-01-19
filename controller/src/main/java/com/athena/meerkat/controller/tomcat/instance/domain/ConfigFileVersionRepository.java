package com.athena.meerkat.controller.tomcat.instance.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ConfigFileVersionRepository extends CrudRepository<ConfigFileVersion, ConfigFileVersionPK> {
	Page<ConfigFileVersion> findAll(Pageable pageable);
}
