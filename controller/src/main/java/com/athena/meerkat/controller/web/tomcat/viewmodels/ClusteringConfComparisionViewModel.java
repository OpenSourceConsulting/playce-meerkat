package com.athena.meerkat.controller.web.tomcat.viewmodels;

public class ClusteringConfComparisionViewModel {
	
	
	private int firstId;
	private int secondId;
	// private int firstVersion;
	// private int secondVersion;
	private String name;
	private String firstValue;
	private String secondValue;

	public int getFirstId() {
		return firstId;
	}

	public void setFirstId(int firstId) {
		this.firstId = firstId;
	}

	public int getSecondId() {
		return secondId;
	}

	public void setSecondId(int secondId) {
		this.secondId = secondId;
	}

	// public int getFirstVersion() {
	// return firstVersion;
	// }
	//
	// public void setFirstVersion(int firstVersion) {
	// this.firstVersion = firstVersion;
	// }
	//
	// public int getSecondVersion() {
	// return secondVersion;
	// }
	//
	// public void setSecondVersion(int secondVersion) {
	// this.secondVersion = secondVersion;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	public ClusteringConfComparisionViewModel(int firstId, String name,
			String firstValue, int secondId, String secondValue) {
		this.firstId = firstId;
		// this.firstVersion = firstVersion;
		this.name = name;
		this.firstValue = firstValue;
		this.secondId = secondId;
		// this.secondVersion = secondVersion;
		this.secondValue = secondValue;
	}
}
