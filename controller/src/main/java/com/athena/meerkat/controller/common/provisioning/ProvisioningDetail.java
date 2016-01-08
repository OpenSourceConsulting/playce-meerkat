/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 10. 21.		First Draft.
 */
package com.athena.meerkat.controller.common.provisioning;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ProvisioningDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Controller에서 각 Software의 config 파일을 읽기 위한 url prefix */
	private String urlPrefix;

	/** Apache, Tomcat, JBoss, MySQL 공통 Variables */
	private String machineId;
	private Integer softwareId;
	private Integer installSeq;
	private String softwareName;
	private String user;
	private String version;
	private String javaHome;
	private String serverHome;
	private String serverName;
	private String encoding;
	private String heapSize;
	private String permgenSize;
	private String bindAddress;
	private String hostName;
	private String httpEnable = "Y";
	private String autoStart = "Y";
	private String[] databaseType;
	private String[] jndiName;
	private String[] connectionUrl;
	private String[] userName;
	private String[] password;
	
	private String fileLocation;
	private String fileName;
	
	/** HTTPD Variables */
	private String group;
	private String apacheHome;
	private String httpPort;
	private String httpsPort;
	private String uriworkermap;
	private String workers;
	
	/** Tomcat Variables */
	private String catalinaHome;
	private String catalinaBase;
	private String portOffset;
	private String highAvailability = "Y";
	private String otherBindAddress;
	private String localIPAddress;
	private String[] maxIdle;
	private String[] maxActive;
	
	/** JBoss Variables */
	private String jbossHome;
	private String baseTemplate;
	private String serverBase;
	private String domainIp;
	private String bindPort;
	private String udpGroupAddr;
	private String multicastPort;
	private String serverPeerID;
	private String jvmRoute;
	private String[] minPoolSize;
	private String[] maxPoolSize;
	
	/** MySQL Variables */
	private String dataDir;
	private String port;
	
	private Integer userId;
	
	/** Ceph Variables */
	private String fsid;
	private String journalSize;
	private String monNetworkInterface;
	private String pgNum;
	private String pgpNum;
	private String replicaSize;
	private String publicNetwork;
	private String clusterNetwork;
	private String fileSystem;
	private String ntpServer;
	private String[] hostname;
	private String[] ip;
	private String[] type;
	private String[] devicePaths;

	/**
	 * @return the urlPrefix
	 */
	public String getUrlPrefix() {
		return urlPrefix;
	}

	/**
	 * @param urlPrefix the urlPrefix to set
	 */
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	/**
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * @return the softwareId
	 */
	public Integer getSoftwareId() {
		return softwareId;
	}

	/**
	 * @param softwareId the softwareId to set
	 */
	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * @return the installSeq
	 */
	public Integer getInstallSeq() {
		return installSeq;
	}

	/**
	 * @param installSeq the installSeq to set
	 */
	public void setInstallSeq(Integer installSeq) {
		this.installSeq = installSeq;
	}

	/**
	 * @return the softwareName
	 */
	public String getSoftwareName() {
		return softwareName;
	}

	/**
	 * @param softwareName the softwareName to set
	 */
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the javaHome
	 */
	public String getJavaHome() {
		return javaHome;
	}

	/**
	 * @param javaHome the javaHome to set
	 */
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	/**
	 * @return the serverHome
	 */
	public String getServerHome() {
		return serverHome;
	}

	/**
	 * @param serverHome the serverHome to set
	 */
	public void setServerHome(String serverHome) {
		this.serverHome = serverHome;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the heapSize
	 */
	public String getHeapSize() {
		return heapSize;
	}

	/**
	 * @param heapSize the heapSize to set
	 */
	public void setHeapSize(String heapSize) {
		this.heapSize = heapSize;
	}

	/**
	 * @return the permgenSize
	 */
	public String getPermgenSize() {
		return permgenSize;
	}

	/**
	 * @param permgenSize the permgenSize to set
	 */
	public void setPermgenSize(String permgenSize) {
		this.permgenSize = permgenSize;
	}

	/**
	 * @return the bindAddress
	 */
	public String getBindAddress() {
		return bindAddress;
	}

	/**
	 * @param bindAddress the bindAddress to set
	 */
	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the httpEnable
	 */
	public String getHttpEnable() {
		return httpEnable;
	}

	/**
	 * @param httpEnable the httpEnable to set
	 */
	public void setHttpEnable(String httpEnable) {
		this.httpEnable = httpEnable;
	}

	/**
	 * @return the databaseType
	 */
	public String[] getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(String[] databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * @return the jndiName
	 */
	public String[] getJndiName() {
		return jndiName;
	}

	/**
	 * @param jndiName the jndiName to set
	 */
	public void setJndiName(String[] jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @return the connectionUrl
	 */
	public String[] getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @param connectionUrl the connectionUrl to set
	 */
	public void setConnectionUrl(String[] connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	/**
	 * @return the userName
	 */
	public String[] getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String[] userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String[] getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String[] password) {
		this.password = password;
	}

	/**
	 * @return the autoStart
	 */
	public String getAutoStart() {
		return autoStart;
	}

	/**
	 * @param autoStart the autoStart to set
	 */
	public void setAutoStart(String autoStart) {
		this.autoStart = autoStart;
	}

	/**
	 * @return the fileLocation
	 */
	public String getFileLocation() {
		return fileLocation;
	}

	/**
	 * @param fileLocation the fileLocation to set
	 */
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the apacheHome
	 */
	public String getApacheHome() {
		return apacheHome;
	}

	/**
	 * @param apacheHome the apacheHome to set
	 */
	public void setApacheHome(String apacheHome) {
		this.apacheHome = apacheHome;
	}

	/**
	 * @return the httpPort
	 */
	public String getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the httpsPort
	 */
	public String getHttpsPort() {
		return httpsPort;
	}

	/**
	 * @param httpsPort the httpsPort to set
	 */
	public void setHttpsPort(String httpsPort) {
		this.httpsPort = httpsPort;
	}

	/**
	 * @return the uriworkermap
	 */
	public String getUriworkermap() {
		return uriworkermap;
	}

	/**
	 * @param uriworkermap the uriworkermap to set
	 */
	public void setUriworkermap(String uriworkermap) {
		this.uriworkermap = uriworkermap;
	}

	/**
	 * @return the workers
	 */
	public String getWorkers() {
		return workers;
	}

	/**
	 * @param workers the workers to set
	 */
	public void setWorkers(String workers) {
		this.workers = workers;
	}

	/**
	 * @return the catalinaHome
	 */
	public String getCatalinaHome() {
		return catalinaHome;
	}

	/**
	 * @param catalinaHome the catalinaHome to set
	 */
	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	/**
	 * @return the catalinaBase
	 */
	public String getCatalinaBase() {
		return catalinaBase;
	}

	/**
	 * @param catalinaBase the catalinaBase to set
	 */
	public void setCatalinaBase(String catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	/**
	 * @return the portOffset
	 */
	public String getPortOffset() {
		return portOffset;
	}

	/**
	 * @param portOffset the portOffset to set
	 */
	public void setPortOffset(String portOffset) {
		this.portOffset = portOffset;
	}

	/**
	 * @return the highAvailability
	 */
	public String getHighAvailability() {
		return highAvailability;
	}

	/**
	 * @param highAvailability the highAvailability to set
	 */
	public void setHighAvailability(String highAvailability) {
		this.highAvailability = highAvailability;
	}

	/**
	 * @return the otherBindAddress
	 */
	public String getOtherBindAddress() {
		return otherBindAddress;
	}

	/**
	 * @param otherBindAddress the otherBindAddress to set
	 */
	public void setOtherBindAddress(String otherBindAddress) {
		this.otherBindAddress = otherBindAddress;
	}

	/**
	 * @return the localIPAddress
	 */
	public String getLocalIPAddress() {
		return localIPAddress;
	}

	/**
	 * @param localIPAddress the localIPAddress to set
	 */
	public void setLocalIPAddress(String localIPAddress) {
		this.localIPAddress = localIPAddress;
	}

	/**
	 * @return the maxIdle
	 */
	public String[] getMaxIdle() {
		return maxIdle;
	}

	/**
	 * @param maxIdle the maxIdle to set
	 */
	public void setMaxIdle(String[] maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * @return the maxActive
	 */
	public String[] getMaxActive() {
		return maxActive;
	}

	/**
	 * @param maxActive the maxActive to set
	 */
	public void setMaxActive(String[] maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * @return the jbossHome
	 */
	public String getJbossHome() {
		return jbossHome;
	}

	/**
	 * @param jbossHome the jbossHome to set
	 */
	public void setJbossHome(String jbossHome) {
		this.jbossHome = jbossHome;
	}

	/**
	 * @return the baseTemplate
	 */
	public String getBaseTemplate() {
		return baseTemplate;
	}

	/**
	 * @param baseTemplate the baseTemplate to set
	 */
	public void setBaseTemplate(String baseTemplate) {
		this.baseTemplate = baseTemplate;
	}

	/**
	 * @return the serverBase
	 */
	public String getServerBase() {
		return serverBase;
	}

	/**
	 * @param serverBase the serverBase to set
	 */
	public void setServerBase(String serverBase) {
		this.serverBase = serverBase;
	}

	/**
	 * @return the domainIp
	 */
	public String getDomainIp() {
		return domainIp;
	}

	/**
	 * @param domainIp the domainIp to set
	 */
	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
	}

	/**
	 * @return the bindPort
	 */
	public String getBindPort() {
		return bindPort;
	}

	/**
	 * @param bindPort the bindPort to set
	 */
	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	/**
	 * @return the udpGroupAddr
	 */
	public String getUdpGroupAddr() {
		return udpGroupAddr;
	}

	/**
	 * @param udpGroupAddr the udpGroupAddr to set
	 */
	public void setUdpGroupAddr(String udpGroupAddr) {
		this.udpGroupAddr = udpGroupAddr;
	}

	/**
	 * @return the multicastPort
	 */
	public String getMulticastPort() {
		return multicastPort;
	}

	/**
	 * @param multicastPort the multicastPort to set
	 */
	public void setMulticastPort(String multicastPort) {
		this.multicastPort = multicastPort;
	}

	/**
	 * @return the serverPeerID
	 */
	public String getServerPeerID() {
		return serverPeerID;
	}

	/**
	 * @param serverPeerID the serverPeerID to set
	 */
	public void setServerPeerID(String serverPeerID) {
		this.serverPeerID = serverPeerID;
	}

	/**
	 * @return the jvmRoute
	 */
	public String getJvmRoute() {
		return jvmRoute;
	}

	/**
	 * @param jvmRoute the jvmRoute to set
	 */
	public void setJvmRoute(String jvmRoute) {
		this.jvmRoute = jvmRoute;
	}

	/**
	 * @return the minPoolSize
	 */
	public String[] getMinPoolSize() {
		return minPoolSize;
	}

	/**
	 * @param minPoolSize the minPoolSize to set
	 */
	public void setMinPoolSize(String[] minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * @return the maxPoolSize
	 */
	public String[] getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(String[] maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the dataDir
	 */
	public String getDataDir() {
		return dataDir;
	}

	/**
	 * @param dataDir the dataDir to set
	 */
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the fsid
	 */
	public String getFsid() {
		return fsid;
	}

	/**
	 * @param fsid the fsid to set
	 */
	public void setFsid(String fsid) {
		this.fsid = fsid;
	}

	/**
	 * @return the journalSize
	 */
	public String getJournalSize() {
		return journalSize;
	}

	/**
	 * @param journalSize the journalSize to set
	 */
	public void setJournalSize(String journalSize) {
		this.journalSize = journalSize;
	}

	/**
	 * @return the monNetworkInterface
	 */
	public String getMonNetworkInterface() {
		return monNetworkInterface;
	}

	/**
	 * @param monNetworkInterface the monNetworkInterface to set
	 */
	public void setMonNetworkInterface(String monNetworkInterface) {
		this.monNetworkInterface = monNetworkInterface;
	}

	/**
	 * @return the pgNum
	 */
	public String getPgNum() {
		return pgNum;
	}

	/**
	 * @param pgNum the pgNum to set
	 */
	public void setPgNum(String pgNum) {
		this.pgNum = pgNum;
	}

	/**
	 * @return the pgpNum
	 */
	public String getPgpNum() {
		return pgpNum;
	}

	/**
	 * @param pgpNum the pgpNum to set
	 */
	public void setPgpNum(String pgpNum) {
		this.pgpNum = pgpNum;
	}

	/**
	 * @return the replicaSize
	 */
	public String getReplicaSize() {
		return replicaSize;
	}

	/**
	 * @param replicaSize the replicaSize to set
	 */
	public void setReplicaSize(String replicaSize) {
		this.replicaSize = replicaSize;
	}

	/**
	 * @return the publicNetwork
	 */
	public String getPublicNetwork() {
		return publicNetwork;
	}

	/**
	 * @param publicNetwork the publicNetwork to set
	 */
	public void setPublicNetwork(String publicNetwork) {
		this.publicNetwork = publicNetwork;
	}

	/**
	 * @return the clusterNetwork
	 */
	public String getClusterNetwork() {
		return clusterNetwork;
	}

	/**
	 * @param clusterNetwork the clusterNetwork to set
	 */
	public void setClusterNetwork(String clusterNetwork) {
		this.clusterNetwork = clusterNetwork;
	}

	/**
	 * @return the fileSystem
	 */
	public String getFileSystem() {
		return fileSystem;
	}

	/**
	 * @param fileSystem the fileSystem to set
	 */
	public void setFileSystem(String fileSystem) {
		this.fileSystem = fileSystem;
	}

	/**
	 * @return the ntpServer
	 */
	public String getNtpServer() {
		return ntpServer;
	}

	/**
	 * @param ntpServer the ntpServer to set
	 */
	public void setNtpServer(String ntpServer) {
		this.ntpServer = ntpServer;
	}

	/**
	 * @return the hostname
	 */
	public String[] getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String[] hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the ip
	 */
	public String[] getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String[] ip) {
		this.ip = ip;
	}

	/**
	 * @return the type
	 */
	public String[] getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String[] type) {
		this.type = type;
	}

	/**
	 * @return the devicePaths
	 */
	public String[] getDevicePaths() {
		return devicePaths;
	}

	/**
	 * @param devicePaths the devicePaths to set
	 */
	public void setDevicePaths(String[] devicePaths) {
		this.devicePaths = devicePaths;
	}
}
//end of ProvisioningDetail.java