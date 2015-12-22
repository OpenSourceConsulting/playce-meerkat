package com.athena.dolly.controller.web.user;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Repository
public interface UserRepository extends JpaRepository<User2, Integer> {
	List<User2> findByUserNameOrEmail(String userName, String email);

}
