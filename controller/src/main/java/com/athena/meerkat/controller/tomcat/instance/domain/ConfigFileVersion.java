/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 20.		First Draft.
 */
package com.athena.meerkat.controller.tomcat.instance.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;

/**
 * <pre>
 *
 * </pre>
 * @author Bong-Jin Kwon
 * @version 2.0
 */
@Entity
@IdClass(ConfigFileVersionPK.class)
public class ConfigFileVersion {

	@Id
	private Long tomcatInstanceId;
	
	@Id
	private Integer fileType;
	
	@Id
	private Integer revision;
	
	@Column
	private String fileName;
	
	@Column(columnDefinition="CLOB", nullable = false)
	private String contents;
	
	@Column
	private Date regDate;
	
	public ConfigFileVersion() {
		
	}
	
	public ConfigFileVersion(Long tomcatInstanceId, Integer fileType, String fileName, String contents) {
		this.tomcatInstanceId = tomcatInstanceId;
		this.fileType = fileType;
		this.fileName = fileName;
		this.contents = contents;
	}

	public Long getTomcatInstanceId() {
		return tomcatInstanceId;
	}

	public void setTomcatInstanceId(Long tomcatInstanceId) {
		this.tomcatInstanceId = tomcatInstanceId;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	@PrePersist
	void preInsert() {
	   this.regDate = new Date();
	}

}
