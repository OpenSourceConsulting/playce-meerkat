package com.athena.meerkat.controller.web.tomcat.viewmodels;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatInstance;

public class TomcatInstanceViewModel {
	private Integer id;
	private String name;
	private String hostName;
	private String stateNm;
	private int state;
	private String ipAddress;
	private String tomcatVersion;
	private String osName;
	private int httpPort;
	private int ajpPort;
	private int redirectPort;
	private String jvmVersion;
	private String domainName;
	private int domainId;
	private int latestServerXmlConfigFileId = 0;
	private int latestContextXmlConfigFileId = 0;

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getJvmVersion() {
		return jvmVersion;
	}

	public void setJvmVersion(String jvmVersion) {
		this.jvmVersion = jvmVersion;
	}

	public int getRedirectPort() {
		return redirectPort;
	}

	public void setRedirectPort(int redirectPort) {
		this.redirectPort = redirectPort;
	}

	public int getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(int ajpPort) {
		this.ajpPort = ajpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getTomcatVersion() {
		return tomcatVersion;
	}

	public void setTomcatVersion(String tomcatVersion) {
		this.tomcatVersion = tomcatVersion;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TomcatInstanceViewModel(TomcatInstance tc) {
		id = tc.getId();
		name = tc.getName();
		if (tc.getServer() != null) {
			hostName = tc.getServer().getName();
			ipAddress = tc.getServer().getSshIPAddr();
			osName = tc.getServer().getName();

		}
		stateNm = tc.getStateNm();
		state = tc.getState();
		if (tc.getTomcatDomain() != null) {
			domainName = tc.getTomcatDomain().getName();
			domainId = tc.getTomcatDomain().getId();

			DomainTomcatConfiguration conf = tc.getTomcatDomain()
					.getDomainTomcatConfig();
			if (conf != null) {
				httpPort = conf.getHttpPort();
				ajpPort = conf.getAjpPort();
				redirectPort = conf.getRedirectPort();
				jvmVersion = "";
				tomcatVersion = conf.getTomcatVersion();
			}
		}

	}

	public TomcatInstanceViewModel() {

	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateNm() {
		return stateNm;
	}

	public void setStateNm(String stateNm) {
		this.stateNm = stateNm;
	}

	public int getLatestServerXmlConfigFileId() {
		return latestServerXmlConfigFileId;
	}

	public void setLatestServerXmlConfigFileId(int latestServerXmlConfigFileId) {
		this.latestServerXmlConfigFileId = latestServerXmlConfigFileId;
	}

	public int getLatestContextXmlConfigFileId() {
		return latestContextXmlConfigFileId;
	}

	public void setLatestContextXmlConfigFileId(int latestContextXmlConfigFileId) {
		this.latestContextXmlConfigFileId = latestContextXmlConfigFileId;
	}
}
