package com.athena.meerkat.controller.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "mon_alert_config")
public class MonAlertConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "alert_item_cd_id")
	private int alertItemCdId;

	@Column(name = "threshold_op_cd_id")
	private Integer thresholdOpCdId;

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

	public Integer getThresholdOpCdId() {
		return thresholdOpCdId;
	}

	public void setThresholdOpCdId(Integer thresholdOpCdId) {
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

	public Integer getDomainId() {
		if (getTomcatDomain() != null) {
			return getTomcatDomain().getId();
		}
		return 0;
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

	public Integer getServerId() {
		if (getServer() != null) {
			return getServer().getId();
		}
		return 0;
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
