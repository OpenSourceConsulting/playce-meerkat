package com.athena.meerkat.controller.web.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository {
	boolean insert(User user);

	List<User> getList();
}
