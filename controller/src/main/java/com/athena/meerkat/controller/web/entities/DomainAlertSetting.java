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
@Table(name = "tomcat_domain_alert")
public class DomainAlertSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "alert_item_cd_id")
	private int alertItemCdId;

	@Column(name = "threshold_op_cd_id")
	private int thresholdOpCdId;

	@Column(name = "threshold_value")
	private Integer thresholdValue;

	@Column(name = "status")
	private boolean status;

	@Transient
	private String alertItemCdNm;
	@Transient
	private String thresholdOpCdNm;
	@Transient
	private boolean isAgentSetting;

	@Transient
	private boolean canEdit = true;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(int thresholdValue) {
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

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isAgentSetting() {
		return isAgentSetting;
	}

	public void setAgentSetting(boolean isAgentSetting) {
		this.isAgentSetting = isAgentSetting;
	}

}
