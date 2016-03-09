package com.athena.meerkat.monitoring.obj;

import java.io.Serializable;

public class SimpleMonitoringObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int time;
	private int value;
	private int value2;

	public int getTime() {
		return time;
	}

	public void setTime(int val1) {
		this.time = val1;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int val2) {
		this.value = val2;
	}

	public SimpleMonitoringObject(int l, int _val, int _val2) {
		time = l;
		value = _val;
		value2 = _val2;
	}

	public int getValue2() {
		return value2;
	}

	public void setValue2(int value2) {
		this.value2 = value2;
	}
}
