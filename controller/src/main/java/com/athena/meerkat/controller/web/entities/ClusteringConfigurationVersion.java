package com.athena.meerkat.controller.web.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.common.MeerkatUtils;

@Entity
@Table(name = "clustering_conf_version")
public class ClusteringConfigurationVersion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;

	@Column(name = "version")
	private int version;

	@Column(name = "create_time")
	private Date createdTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedTimeString() {
		return MeerkatUtils.dateTimeToString(createdTime,
				MeerkatConstants.DATE_TIME_FORMATTER);
	}

	public String getVersionAndCreatedTime() {
		return String.valueOf(version) + " - " + getCreatedTimeString();
	}

}
