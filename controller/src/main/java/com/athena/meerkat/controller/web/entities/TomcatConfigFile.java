/**
 * 
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tran
 *
 */
@Entity
@Table
public class TomcatConfigFile {
	@Id
	private int id;
	private int domainId;
	private int fileTypeCdId;
	private int version;
	private String comment;
	private String filePath;
	private java.sql.Date createdTime;
	private int tomcatInstanceId;
	private int createUserId;

	/**
	 * 
	 */
	public TomcatConfigFile() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the domainId
	 */
	public int getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId
	 *            the domainId to set
	 */
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the fileTypeCdId
	 */
	public int getFileTypeCdId() {
		return fileTypeCdId;
	}

	/**
	 * @param fileTypeCdId
	 *            the fileTypeCdId to set
	 */
	public void setFileTypeCdId(int fileTypeCdId) {
		this.fileTypeCdId = fileTypeCdId;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the createdTime
	 */
	public java.sql.Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(java.sql.Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the tomcatInstanceId
	 */
	public int getTomcatInstanceId() {
		return tomcatInstanceId;
	}

	/**
	 * @param tomcatInstanceId
	 *            the tomcatInstanceId to set
	 */
	public void setTomcatInstanceId(int tomcatInstanceId) {
		this.tomcatInstanceId = tomcatInstanceId;
	}

	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

}
