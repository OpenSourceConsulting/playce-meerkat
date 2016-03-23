package com.athena.meerkat.controller.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.CommonCode;

@Repository
public interface CommonCodeRepository extends
		JpaRepository<CommonCode, Integer> {
	@Query("SELECT c FROM CommonCode c, CommonCode parent where c.parentCodeId = parent.id and parent.codeValue like ?1")
	List<CommonCode> findByParentCodeName(String parentName);

	CommonCode findByCodeValue(String codeValue);
}
