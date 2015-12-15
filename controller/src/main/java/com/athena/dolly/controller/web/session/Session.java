package com.athena.dolly.controller.web.session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.dolly.controller.web.application.Application;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "session")
public class Session {
	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "session_key")
	private String key;
	@Column(name = "session_value")
	private String value;
	@ManyToOne
	@JsonBackReference
	private Application application;

	public Session(String sKey, String sValue) {
		// TODO Auto-generated constructor stub
		key = sKey;
		value = sValue;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
}
