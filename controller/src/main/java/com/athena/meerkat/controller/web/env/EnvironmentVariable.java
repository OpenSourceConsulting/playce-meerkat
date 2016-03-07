package com.athena.meerkat.controller.web.env;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;

import org.springframework.data.history.Revision;

import com.athena.meerkat.controller.web.machine.Machine;

@Entity
@Table(name = "environment_variable")
public class EnvironmentVariable implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int id;
	@Column(name = "name")
	private String name;
	@OneToMany
	private List<EnvironmentVariable> environmentVariableValues;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the environmentVariableValues
	 */
	public List<EnvironmentVariable> getEnvironmentVariableValues() {
		return environmentVariableValues;
	}

	/**
	 * @param environmentVariableValues
	 *            the environmentVariableValues to set
	 */
	public void setEnvironmentVariableValues(
			List<EnvironmentVariable> environmentVariableValues) {
		this.environmentVariableValues = environmentVariableValues;
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

}
