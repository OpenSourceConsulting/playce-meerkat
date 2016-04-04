package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;

@Repository
public interface TomcatConfigFileRepository extends
		JpaRepository<TomcatConfigFile, Integer> {
	List<TomcatConfigFile> findByTomcatDomain_IdAndFileTypeCdId(int domainId,
			int fileTypeCdId);

	TomcatConfigFile findByTomcatDomainAndFileTypeCdIdAndVersion(
			TomcatDomain td, int id, int version);

	// @Query(value =
	// "select NEW com.athena.meerkat.controller.web.tomcat.viewmodels.ConfigFileVersionViewModel(cf.version, cf.createdTime) from TomcatConfigFile cf, CommonCode cc where cc.id = cf.fileTypeCdId and cf.tomcatDomain.id =:domainId  and cc.codeNm = :type")
	// List<TomcatConfigFile> getVersions(@Param("domainId") int domainId,
	// @Param("type") String type);

	@Query(value = "select cf from TomcatConfigFile cf, CommonCode cc where cc.id = cf.fileTypeCdId and cf.tomcatDomain.id =:domainId  and cc.codeNm = :type order by cf.version desc")
	List<TomcatConfigFile> getConfiFileOrderByVersionDesc(
			@Param("domainId") int domainId, @Param("type") String type);

	@Query(value = "select cf from TomcatConfigFile cf, CommonCode cc where cc.id = cf.fileTypeCdId and (cf.tomcatDomain.id =:domainId or cf.tomcatInstance.id = :tomcatId) and cc.codeNm = :type order by cf.version desc")
	List<TomcatConfigFile> getConfiFileOrderByVersionDesc(
			@Param("domainId") int domainId, @Param("tomcatId") int tomcatId,
			@Param("type") String type);

	@Query(value = "select cf from TomcatConfigFile cf where cf.tomcatInstance.id =:tomcatId  and cf.fileTypeCdId = :typeId order by cf.version desc")
	List<TomcatConfigFile> findByTomcatAndFileTypeCdId(
			@Param("tomcatId") int tomcatId, @Param("typeId") int typeId);

}
