package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.MeerkatUtils;
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
	private Date createdTime;
	@Column(name = "create_user_id")
	private int createUserId;

	@Transient
	private String content;

	@ManyToOne
	@JsonBackReference(value = "domain-configFile")
	@JoinColumn(name = "domain_id")
	private TomcatDomain tomcatDomain;

	@ManyToOne
	@JsonBackReference(value = "inst-configFile")
	private TomcatInstance tomcatInstance;

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getFileTypeCdId() {
		return fileTypeCdId;
	}

	public void setFileTypeCdId(int fileTypeCdId) {
		this.fileTypeCdId = fileTypeCdId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getVersionAndTimeAndTomcat() {
		String str = String.valueOf(version)
				+ " - "
				+ MeerkatUtils
						.dateTimeToString(
								this.createdTime,
								MeerkatConstants.CONFIG_FILE_VERSION_DATE_TIME_FORMATTER);
		if (getTomcatInstance() != null) {
			str = str + "(" + getTomcatInstance().getName() + ")";
		}
		return str;

	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TomcatInstance getTomcatInstance() {
		return tomcatInstance;
	}

	public void setTomcatInstance(TomcatInstance tomcatInstance) {
		this.tomcatInstance = tomcatInstance;
	}

}
