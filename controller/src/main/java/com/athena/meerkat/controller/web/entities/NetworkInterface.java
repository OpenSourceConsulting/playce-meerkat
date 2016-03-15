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
public class NetworkInterface {
	@Id
	private int id;
	private String name;
	private String ipv4;
	private String ipv6;
	private int serverId;
	private String defaultGateway;
	private String netmask;

	/**
	 * 
	 */
	public NetworkInterface() {
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
	 * @return the ipv4
	 */
	public String getIpv4() {
		return ipv4;
	}

	/**
	 * @param ipv4
	 *            the ipv4 to set
	 */
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	/**
	 * @return the ipv6
	 */
	public String getIpv6() {
		return ipv6;
	}

	/**
	 * @param ipv6
	 *            the ipv6 to set
	 */
	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the defaultGateway
	 */
	public String getDefaultGateway() {
		return defaultGateway;
	}

	/**
	 * @param defaultGateway
	 *            the defaultGateway to set
	 */
	public void setDefaultGateway(String defaultGateway) {
		this.defaultGateway = defaultGateway;
	}

	/**
	 * @return the netmask
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * @param netmask
	 *            the netmask to set
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

}
