/* 
 * Athena Peacock - Auto Provisioning
 * 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Tran			Mar 7, 2016		First Draft.
 */
package com.athena.meerkat.controller.web.env;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.athena.meerkat.controller.web.machine.Machine;
import com.athena.meerkat.controller.web.revision.Revision;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Tran
 * @version 1.0
 */
@Entity
@Table(name="environment_variable_value")
public class EnvironmentVariableValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "value")
	private String value;

	@Column(name = "created_time")
	private Date createdTime;

	@ManyToOne(fetch=FetchType.LAZY)
	private EnvironmentVariable env;
	@ManyToOne(fetch=FetchType.LAZY)
	private Revision revision;
	@ManyToOne(fetch=FetchType.LAZY)
	Machine machine;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param revision
	 *            the revision to set
	 */
	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	public Revision getRevision() {
		return revision;
	}
}
// end of EnvironmentVariableValue.java