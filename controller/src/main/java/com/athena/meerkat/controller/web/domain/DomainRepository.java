package com.athena.meerkat.controller.web.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Integer> {
	List<Domain> findByName(String name);
}
