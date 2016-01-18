package com.athena.dolly.controller.web.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component
public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findByUserNameOrEmail(String userName, String email);
	List<User> findByUserNameContaining(String userName);
}
