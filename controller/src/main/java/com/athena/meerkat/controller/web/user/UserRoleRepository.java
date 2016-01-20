package com.athena.meerkat.controller.web.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	UserRole findByName(String name);
}
