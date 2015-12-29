package com.athena.dolly.controller.web.datagridserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.dolly.controller.web.machine.Machine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "datagrid_server")
public class DatagridServer {

	@Id
	@Column(name = "Id")
	private int Id;
	@Column(name = "type")
	private String type;
	@OneToOne
	private Machine machine;

	@ManyToOne
	@JoinColumn(name="server_group_id")
	@JsonManagedReference	
	private DatagridServerGroup datagridServerGroup;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

}
