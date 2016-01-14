/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
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
 * Sang-cheon Park	2013. 12. 5.		First Draft.
 */
package com.athena.meerkat.common.cache;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.athena.dolly.common.exception.ConfigurationException;

/**
 * <pre>
 * BCI(Byte Code Instrumentation) 수행 대상 클래스 목록 및 Infinispan 관련 설정이 저장된 파일을 로드하고 파싱한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyConfig {

	protected static final String CONFIG_FILE 				= "dolly.properties";
	
	// Property for core
	private static final String CLIENT_TYPE		 			= "dolly.client.type";
	private static final String VERBOSE_PROPERTY 			= "dolly.verbose";
	private static final String ENABLE_SSO_PROPERTY 		= "dolly.enableSSO";
	private static final String SSO_DOMAIN_LIST_PROPERTY 	= "dolly.sso.domain.list";
	private static final String SSO_PARAMETER_KEY 			= "dolly.sso.parameter.key";
	private static final String TIMEOUT_PROPERTY 			= "dolly.session.timeout";
    //private static final String TARGET_CLASS_PROPERTY 		= "dolly.instrument.target.class";
    
    private static final String USE_EMBEDDED 				= "dolly.use.infinispan.embedded";
    private static final String HOTROD_HOST 				= "dolly.hotrod.host";
    private static final String HOTROD_PORT 				= "dolly.hotrod.port";
    private static final String JGROUPS_STACK 				= "dolly.jgroups.stack";
    private static final String JGROUPS_BIND_ADDR 			= "dolly.jgroups.tcp.bind.address";
    private static final String JGROUPS_BIND_PORT 			= "dolly.jgroups.tcp.bind.port";
    private static final String JGROUPS_INIT_HOSTS 			= "dolly.jgroups.tcp.initial.hosts";
    private static final String JGROUPS_MULTICAST_PORT		= "dolly.jgroups.udp.multicast.port";
    
    private static final String COUCHBASE_URIS 				= "couchbase.cluter.uri.list";
    private static final String COUCHBASE_BUCKET_NAME 		= "couchbase.bucket.name";
    private static final String COUCHBASE_BUCKET_PASSWD		= "couchbase.bucket.passwd";

    // Property for controller
	private static final String EMBEDDED 					= "infinispan.embedded";
	private static final String JMX_SERVER_LIST 			= "infinispan.jmx.server.list";
	private static final String JMX_USER 					= "infinispan.jmx.user.list";
    private static final String JMX_PASSWD 					= "infinispan.jmx.passwd.list";
    
    public static Properties properties;

    private boolean verbose;
    private String clientType;
    private List<String> classList = new ArrayList<String>();
    private List<String> ssoDomainList = new ArrayList<String>();
    private boolean enableSSO;
    private String ssoParamKey;
    private int timeout = 30;
    
    private boolean useEmbedded;
    private String hotrodHost;
    private int hotrodPort;
    private String jgroupsStack;
    private String jgroupsBindAddress;
    private String jgroupsBindPort;
    private String jgroupsInitialHosts;
    private String jgroupsMulticastPort;
    
    private String couchbaseUris;
    private String couchbaseBucketName;
    private String couchbaseBucketPasswd;
    
    private boolean embedded;
    private String[] jmxServers;
    private String[] users;
    private String[] passwds;
    
	/**
	 * <pre>
	 * 프로퍼티 파일을 로드하고 파싱한다.
	 * </pre>
	 * @return
	 * @throws ConfigurationException
	 */
	public DollyConfig load() throws ConfigurationException {
		properties = loadConfigFile();
        parseConfigFile(properties);
        return this;
    }//end of load()

    /**
     * <pre>
     * System Property 또는 classpath에 존재하는 프로퍼티 파일을 로드한다.
     * </pre>
     * @return
     * @throws ConfigurationException
     */
    private Properties loadConfigFile() throws ConfigurationException {
    	InputStream configResource = null;
    	
        try {
        	String configFile = System.getProperty(CONFIG_FILE);
        	
        	if (configFile != null && !"".equals(configFile)) {
        		configResource = new BufferedInputStream(new FileInputStream(configFile));
        	} else {
        		configResource = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        	}
        	
            if (configResource == null) {
                throw new FileNotFoundException("Could not locate " + CONFIG_FILE + " in the classpath or System Poroperty(-Ddolly.properties=Full Qualified File Name) path.");
            }
            
            Properties config = new Properties();
            config.load(configResource);

            configResource.close();
        	
            return config;
        } catch (IOException e) {
            throw new ConfigurationException("Could not load the configuration file (" + CONFIG_FILE + "). " +
                    "Please make sure it exists at the root of the classpath or System Poroperty(-Ddolly.properties=Full Qualified File Name) path.", e);
        }
    }//end of loadConfigFile()
    
    /**
     * <pre>
     * 프로퍼티 파일을 파싱한다.
     * </pre>
     * @param config
     * @throws ConfigurationException
     */
    private void parseConfigFile(Properties config) throws ConfigurationException {
    	extractTargetClasses(config);
        extractSsoDomainList(config);
        extractOthers(config);
    }//end of parseConfigFile()

	/**
     * <pre>
     * BCI 대상 target class 들을 확인한다.
     * </pre>
     * @param config
     */
    private void extractTargetClasses(Properties config) {
    	String[] classNames = new String[]{
    		"org.apache.catalina.session.StandardSessionFacade",
    		"org.apache.catalina.session.ManagerBase",
    		"org.apache.catalina.connector.Request",
    		"weblogic.servlet.internal.session.SessionData",
    		"weblogic.servlet.internal.ServletRequestImpl",
    	};
    	
    	//String[] classNames = config.getProperty(TARGET_CLASS_PROPERTY, "").split(",");
    	
    	for (String clazzName : classNames) {
    		if (!"".equals(clazzName)) {
    			if (verbose) {
    				System.out.println(clazzName + " will be enhanced.");
    			}
    			classList.add(clazzName.replace(".", "/"));
    		}
    	}
    }//end of extractTargetClasses()

    /**
     * <pre>
     * SSO 대상 Domain 목록을 확인한다.
     * </pre>
     * @param config
     */
    private void extractSsoDomainList(Properties config) {
    	String[] domainNames = config.getProperty(SSO_DOMAIN_LIST_PROPERTY, "").split(",");
    	
    	for (String domainName : domainNames) {
    		if (!"".equals(domainName)) {
    			ssoDomainList.add(domainName);
    		}
    	}
    }//end of extractSsoDomainList()

    /**
     * <pre>
     * 기타 항목을 확인한다.
     * </pre>
     * @param config
     */
    private void extractOthers(Properties config) {
        this.verbose = Boolean.parseBoolean(config.getProperty(VERBOSE_PROPERTY, "false"));
        this.clientType = config.getProperty(CLIENT_TYPE, "infinispan");
        this.enableSSO = Boolean.parseBoolean(config.getProperty(ENABLE_SSO_PROPERTY, "false"));
		this.ssoParamKey = config.getProperty(SSO_PARAMETER_KEY, null);
    	this.timeout = Integer.parseInt(config.getProperty(TIMEOUT_PROPERTY, "30"));
    	
    	this.useEmbedded = Boolean.parseBoolean(config.getProperty(USE_EMBEDDED, "false"));
    	this.hotrodHost = config.getProperty(HOTROD_HOST, "0.0.0.0");
    	this.hotrodPort = Integer.parseInt(config.getProperty(HOTROD_PORT, "11222"));
    	this.jgroupsStack = config.getProperty(JGROUPS_STACK, "udp");
    	this.jgroupsBindAddress = config.getProperty(JGROUPS_BIND_ADDR, "127.0.0.1");
    	this.jgroupsBindPort = config.getProperty(JGROUPS_BIND_PORT, "7800");
    	this.jgroupsInitialHosts = config.getProperty(JGROUPS_INIT_HOSTS, "localhost[7800],localhost[7801]");
    	this.jgroupsMulticastPort = config.getProperty(JGROUPS_MULTICAST_PORT, "45588");
    	
    	this.couchbaseUris = config.getProperty(COUCHBASE_URIS, "");
    	this.couchbaseBucketName = config.getProperty(COUCHBASE_BUCKET_NAME, "");
    	this.couchbaseBucketPasswd = config.getProperty(COUCHBASE_BUCKET_PASSWD, "");

    	this.embedded = Boolean.parseBoolean(config.getProperty(EMBEDDED, "false"));
    	
    	String list = config.getProperty(JMX_SERVER_LIST, "");
    	if (!"".equals(list)) {
    		this.jmxServers = list.split(";");
    	}

    	list = config.getProperty(JMX_USER, "");
    	if (!"".equals(list)) {
    		this.users = list.split(";");
    	}
    	
    	list = config.getProperty(JMX_PASSWD, "");
    	if (!"".equals(list)) {
    		this.passwds = list.split(";");
    	}
    }//end of extractOthers()

	/**
	 * @return the classList
	 */
	public List<String> getClassList() {
		return classList;
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the enableSSO
	 */
	public boolean isEnableSSO() {
		return enableSSO;
	}

	/**
	 * @return the ssoDomainList
	 */
	public List<String> getSsoDomainList() {
		return ssoDomainList;
	}

	/**
	 * @return the ssoParamKey
	 */
	public String getSsoParamKey() {
		return ssoParamKey;
	}

	/**
	 * @return the useEmbedded
	 */
	public boolean isUseEmbedded() {
		return useEmbedded;
	}

	/**
	 * @return the hotrodHost
	 */
	public String getHotrodHost() {
		return hotrodHost;
	}

	/**
	 * @return the hotrodPort
	 */
	public int getHotrodPort() {
		return hotrodPort;
	}

	/**
	 * @return the jgroupsStack
	 */
	public String getJgroupsStack() {
		return jgroupsStack;
	}

	/**
	 * @return the jgroupsBindAddress
	 */
	public String getJgroupsBindAddress() {
		return jgroupsBindAddress;
	}

	/**
	 * @return the jgroupsBindPort
	 */
	public String getJgroupsBindPort() {
		return jgroupsBindPort;
	}

	/**
	 * @return the jgroupsInitialHosts
	 */
	public String getJgroupsInitialHosts() {
		return jgroupsInitialHosts;
	}

	/**
	 * @return the jgroupsMulticastPort
	 */
	public String getJgroupsMulticastPort() {
		return jgroupsMulticastPort;
	}

	/**
	 * @return the couchbaseUris
	 */
	public String getCouchbaseUris() {
		return couchbaseUris;
	}

	/**
	 * @return the couchbaseBucketName
	 */
	public String getCouchbaseBucketName() {
		return couchbaseBucketName;
	}

	/**
	 * @return the couchbaseBucketPasswd
	 */
	public String getCouchbaseBucketPasswd() {
		return couchbaseBucketPasswd;
	}

	/**
	 * @return the embedded
	 */
	public boolean isEmbedded() {
		return embedded;
	}

	/**
	 * @param embedded the embedded to set
	 */
	public void setEmbedded(boolean embedded) {
		this.embedded = embedded;
	}

	/**
	 * @return the jmxServers
	 */
	public String[] getJmxServers() {
		return jmxServers;
	}

	/**
	 * @param jmxServers the jmxServers to set
	 */
	public void setJmxServers(String[] jmxServers) {
		this.jmxServers = jmxServers;
	}

	/**
	 * @return the user
	 */
	public String[] getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(String[] users) {
		this.users = users;
	}

	/**
	 * @return the passwds
	 */
	public String[] getPasswds() {
		return passwds;
	}

	/**
	 * @param passwds the passwds to set
	 */
	public void setPasswds(String[] passwds) {
		this.passwds = passwds;
	}
}
//end of DollyConfig.java