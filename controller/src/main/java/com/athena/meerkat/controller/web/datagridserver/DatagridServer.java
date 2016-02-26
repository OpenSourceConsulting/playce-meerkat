package com.athena.meerkat.controller.web.datagridserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import com.athena.meerkat.controller.web.machine.Machine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "datagrid_server")
public class DatagridServer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private int Id;
	@Column(name = "type")
	private String type;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "machine_id")
	@JsonBackReference
	private Machine machine;

	@ManyToOne
	@JoinColumn(name = "server_group_id")
	@JsonBackReference
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

	/**
	 * @return the datagridServerGroup
	 */
	public DatagridServerGroup getDatagridServerGroup() {
		return datagridServerGroup;
	}

	/**
	 * @param datagridServerGroup
	 *            the datagridServerGroup to set
	 */
	public void setDatagridServerGroup(DatagridServerGroup datagridServerGroup) {
		this.datagridServerGroup = datagridServerGroup;
	}

	public String getIpAddress() {
		if (machine != null) {
			return machine.getSSHIPAddr();
		}
		return "";
	}

	public int getStatus() {
		if (machine != null) {
			return machine.getState();
		}
		return 0;
	}

	public String getHostName() {
		if (machine != null) {
			return machine.getHostName();
		}
		return "";
	}

	public String getGroupName() {
		if (datagridServerGroup != null) {
			return datagridServerGroup.getName();
		}
		return "";
	}

	public String getServerName() {
		if (machine != null) {
			return machine.getName();
		}
		return "";
	}
}
