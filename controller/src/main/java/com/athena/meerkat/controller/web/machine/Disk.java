package com.athena.meerkat.controller.web.machine;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Disk drive object
 * 
 * @author hptra
 * 
 */
@Entity
@Table(name = "disk")
public class Disk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "name")
	private String name;
	@Column(name = "capacity")
	private double capacity;
	@Column(name = "unit")
	private String unit; // MB, GB
	@Column(name = "file_system")
	private String fileSystem;
	@Column(name = "free_capacity")
	private double freeCapacity;
	@ManyToOne(fetch = FetchType.LAZY)
	// using this annotation to prevent Infinite recursion json mapping
	@JsonBackReference
	private Machine machine;

	public Disk() {

	}

	public double getFreeCapacity() {
		return freeCapacity;
	}

	public void setFreeCapacity(double free_capacity) {
		this.freeCapacity = free_capacity;
	}

	public String getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(String file_system) {
		this.fileSystem = file_system;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
}
