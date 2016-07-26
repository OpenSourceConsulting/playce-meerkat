package com.athena.meerkat.controller.web.tomcat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
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

	@Query(value = "select cf from TomcatConfigFile cf where cf.tomcatDomain.id =:domainId and cf.tomcatInstance is null and cf.fileTypeCdId = :fileTypeCdId order by cf.version desc")
	List<TomcatConfigFile> getConfiFileOrderByVersionDesc(
			@Param("domainId") int domainId, @Param("fileTypeCdId") int fileTypeCdId);

	@Query(value = "select cf from TomcatConfigFile cf where (cf.tomcatDomain.id =:domainId or cf.tomcatInstance.id = :tomcatId) and cf.fileTypeCdId = :fileTypeCdId order by cf.version desc")
	List<TomcatConfigFile> getConfiFileOrderByVersionDesc(
			@Param("domainId") int domainId, @Param("tomcatId") int tomcatId,
			@Param("fileTypeCdId") int fileTypeCdId);

	@Query(value = "select cf from TomcatConfigFile cf where cf.tomcatInstance.id =:tomcatId  and cf.fileTypeCdId = :typeId order by cf.version desc")
	List<TomcatConfigFile> findByTomcatAndFileTypeCdId(
			@Param("tomcatId") int tomcatId, @Param("typeId") int typeId);
	
	@Query(value = "select id, domain_id, file_type_cd_id, max(version) version, file_path, tomcat_instance_id, comment, created_time, create_user_id "
				  +"from tomcat_config_file where domain_id = :domainId and file_type_cd_id = :fileTypeCdId", nativeQuery = true)
	TomcatConfigFile getLatestConfigFile(@Param("domainId") int domainId, @Param("fileTypeCdId") int fileTypeCdId);
	

}
