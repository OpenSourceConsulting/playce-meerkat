package com.athena.meerkat.monitoring.obj;

import java.io.Serializable;

public class SimpleMonitoringObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long time;
	private int value;

	public long getTime() {
		return time;
	}

	public void setTime(long val1) {
		this.time = val1;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int val2) {
		this.value = val2;
	}

	public SimpleMonitoringObject(long l, int _val2) {
		time = l;
		value = _val2;
	}
}
