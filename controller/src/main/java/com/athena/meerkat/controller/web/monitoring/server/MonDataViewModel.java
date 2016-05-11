package com.athena.meerkat.controller.web.monitoring.server;

import java.util.Date;
import java.util.Map;

import com.athena.meerkat.controller.common.MeerkatUtils;

public class MonDataViewModel {
	private Date monDt;
	Map<String, Double> value;

	public Date getMonDt() {
		return monDt;
	}

	public void setMonDt(Date monDt) {
		this.monDt = monDt;
	}

	public String getMonDtString() {
		return MeerkatUtils.dateTimeToString(this.monDt, "hh:mm:ss");
	}

	public Map<String, Double> getValue() {
		return value;
	}

	public void setValue(Map<String, Double> value) {
		this.value = value;
	}

}
