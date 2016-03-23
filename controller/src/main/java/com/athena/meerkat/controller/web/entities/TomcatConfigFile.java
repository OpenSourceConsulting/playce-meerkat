package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran Ho
 * @version 2.0
 */
@Entity
@Table(name = "tomcat_config_file")
public class TomcatConfigFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;

	@Column(name = "file_type_cd_id")
	private int fileTypeCdId;
	@Column(name = "version")
	private int version;
	@Column(name = "comment")
	private String comment;
	@Column(name = "file_path")
	private String filePath;
	@Column(name = "created_time")
	private String createdTime;
	@Column(name = "create_user_id")
	private int createUserId;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "domain_id")
	private TomcatDomain tomcatDomain;

	@ManyToOne
	@JsonBackReference
	private TomcatInstance tomcatInstance;

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

}
