package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TomcatDomain;

@Repository
public interface DomainRepository extends JpaRepository<TomcatDomain, Integer> {
	List<TomcatDomain> findByName(String name);
}
