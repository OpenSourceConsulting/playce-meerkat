package com.athena.meerkat.controller.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "mon_alert_config")
public class MonAlertConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "tomcat_domain_id", insertable = false, updatable = false)
	private int domainId;
	
	@Column(name = "server_id", insertable = false, updatable = false)
	private int serverId;

	@Column(name = "alert_item_cd_id")
	private int alertItemCdId;

	@Column(name = "threshold_op_cd_id")
	private int thresholdOpCdId;

	@Column(name = "threshold_value")
	private Integer thresholdValue;

	@Column(name = "status")
	private boolean status;
	
	
	@Column(name = "mon_factor_id")
	private String monFactorId;

	@Transient
	private String alertItemCdNm;
	@Transient
	private String thresholdOpCdNm;

	@ManyToOne(fetch = FetchType.LAZY)
	private TomcatDomain tomcatDomain;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Server server;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(Integer thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public int getThresholdOpCdId() {
		return thresholdOpCdId;
	}

	public void setThresholdOpCdId(int thresholdOpCdId) {
		this.thresholdOpCdId = thresholdOpCdId;
	}

	public int getAlertItemCdId() {
		return alertItemCdId;
	}

	public void setAlertItemCdId(int alertItemCdId) {
		this.alertItemCdId = alertItemCdId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
		if (this.tomcatDomain == null) {
			this.tomcatDomain = new TomcatDomain();
		}
		this.tomcatDomain.setId(domainId);
	}

	public String getAlertItemCdNm() {
		return alertItemCdNm;
	}

	public void setAlertItemCdNm(String alertItemCdNm) {
		this.alertItemCdNm = alertItemCdNm;
	}

	public String getThresholdOpCdNm() {
		return thresholdOpCdNm;
	}

	public void setThresholdOpCdNm(String thresholdOpCdNm) {
		this.thresholdOpCdNm = thresholdOpCdNm;
	}

	public String getMonFactorId() {
		return monFactorId;
	}

	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
		
		if (this.server == null) {
			this.server = new Server();
		}
		this.server.setId(serverId);
	}

	public TomcatDomain getTomcatDomain() {
		return tomcatDomain;
	}

	public void setTomcatDomain(TomcatDomain tomcatDomain) {
		this.tomcatDomain = tomcatDomain;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
