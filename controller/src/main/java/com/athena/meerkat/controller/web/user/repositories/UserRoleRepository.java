package com.athena.meerkat.controller.web.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athena.meerkat.controller.web.user.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	UserRole findByName(String name);
}
