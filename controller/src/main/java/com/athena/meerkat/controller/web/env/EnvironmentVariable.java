package com.athena.meerkat.controller.web.env;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.domain.ClusteringConfigurationValue;
import com.athena.meerkat.controller.web.machine.Machine;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "environment_variable")
public class EnvironmentVariable {
	@Id
	private int Id;
	@Column(name = "name")
	private String name;
	@OneToMany//(mappedBy = "environment_variable")
	@JsonManagedReference
	private List<EnvironmentVariableValue> envValues;

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

	public List<EnvironmentVariableValue> getEnvValues() {
		return envValues;
	}

	public void setEnvValues(List<EnvironmentVariableValue> envValues) {
		this.envValues = envValues;
	}

}
