package com.athena.meerkat.controller.web.env;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.machine.Machine;

@Entity
@Table(name = "environment_variable")
public class EnvironmentVariable {
	@Id
	private int Id;
	@Column(name = "name")
	private String name;
	@Column(name = "value")
	private String value;
	@Column(name = "revision")
	private int revision;
	@Column(name = "is_nested")
	private boolean isNested;
	@ManyToOne(fetch=FetchType.LAZY)
	Machine machine;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public boolean isNested() {
		return isNested;
	}

	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}
}
