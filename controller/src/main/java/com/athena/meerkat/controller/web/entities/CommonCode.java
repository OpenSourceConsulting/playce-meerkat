/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * BongJin Kwon		2016. 3. 29.		First Draft.
 */
package com.athena.meerkat.controller.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
@Entity
@Table(name = "common_code")
public class CommonCode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;//코드id
	
	@Column(name = "grop_id")
	private String gropId;//그룹코드
	
	@Column(name = "code_nm")
	private String codeNm;//코드명
	
	@Column(name = "prto_seq")
	private short prtoSeq;//출력순서
	
	@Column(name = "cd_desc")
	private String cdDesc;//코드설명
	
	@Column(name = "mng_yn")
	private String mngYn;//관리여부

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public CommonCode() {
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the gropId
	 */
	public String getGropId() {
		return gropId;
	}

	/**
	 * @param gropId the gropId to set
	 */
	public void setGropId(String gropId) {
		this.gropId = gropId;
	}

	/**
	 * @return the codeNm
	 */
	public String getCodeNm() {
		return codeNm;
	}

	/**
	 * @param codeNm the codeNm to set
	 */
	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}

	/**
	 * @return the prtoSeq
	 */
	public short getPrtoSeq() {
		return prtoSeq;
	}

	/**
	 * @param prtoSeq the prtoSeq to set
	 */
	public void setPrtoSeq(short prtoSeq) {
		this.prtoSeq = prtoSeq;
	}

	/**
	 * @return the cdDesc
	 */
	public String getCdDesc() {
		return cdDesc;
	}

	/**
	 * @param cdDesc the cdDesc to set
	 */
	public void setCdDesc(String cdDesc) {
		this.cdDesc = cdDesc;
	}

	/**
	 * @return the mngYn
	 */
	public String getMngYn() {
		return mngYn;
	}

	/**
	 * @param mngYn the mngYn to set
	 */
	public void setMngYn(String mngYn) {
		this.mngYn = mngYn;
	}

}
//end of CommonCode.java