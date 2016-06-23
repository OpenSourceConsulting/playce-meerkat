/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Bong-Jin Kwon	2015. 1. 13.		First Draft.
 */
package com.athena.meerkat.controller;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 2.0
 */
public abstract class MeerkatConstants {

	public static final String UTF8 = "UTF-8";

	/**
	 * env.sh 로딩완료 - 11 은 에러발생
	 */
	public static final int INSTANCE_STATE_PEND1 = 10;

	/**
	 * server.xml 로딩완료 - 21 은 에러발생
	 */
	public static final int INSTANCE_STATE_PEND2 = 20;

	/**
	 * context.xml 로딩완료 - 31 은 에러발생
	 */
	public static final int INSTANCE_STATE_PEND3 = 30;

	/**
	 * 설치가 유효한 상태
	 */
	public static final int INSTANCE_STATE_VALID = 100;

	/**
	 * Default value of SSH port
	 */
	public static final int DEFAULT_SSH_PORT = 22;

	/**
	 * Default value of hostname
	 */
	public static String DEFAULT_HOSTNAME = "localhost";

	/**
	 * Default value of ip address version 4
	 */
	public static String DEFAULT_IPv4 = "127.0.0.1";

	/**
	 * Default value of cpu clock speed measurement unit
	 */
	public static String DEFAULT_CPU_MEASUREMENT_UNIT = "MHz";

	/**
	 * Class driver for MySQL
	 */
	public static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	/**
	 * Class driver for Oracle
	 */
	public static String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

	/**
	 * Tomcat status - 'TS_STATE' common code
	 */
	public static final int TOMCAT_STATUS_NOTINSTALLED = 5;
	public static final int TOMCAT_STATUS_INSTALLED = 6;
	public static final int TOMCAT_STATUS_SHUTDOWN = 7;
	public static final int TOMCAT_STATUS_RUNNING = 8;
	public static final int TOMCAT_STATUS_STARTING = 14;
	public static final int TOMCAT_STATUS_STOPPING = 15;

	/* State of application */
	public static int APP_STATE_STARTED = 1;
	public static int APP_STATE_STOPPED = 2;

	/**
	 * meerkat repo address
	 */
	public static final String MEERKAT_REPO = "/home/meerkat/repo/tomcat/";// input
																			// later;
	public static final String TOMCAT_ARCHIVE_FILE = "jboss-ews-as-2.1.0.zip";
	public static final String TOMCAT_ARCHIVE_TEMPLATE_FILE = "jboss-ews-as-template.zip";

	/**
	 * Machine type
	 */
	public static final int MACHINE_TOMCAT_SERVER_TYPE = 1;
	public static final int MACHINE_DATAGRID_SERVER_TYPE = 2;

	/**
	 * Revision type
	 */
	public static final int REVISION_CLUSTERING_CONFIG_TYPE = 1;
	public static final int REVISION_ENV_CONFIG_TYPE = 2;

	/**
	 * Common code
	 */
	public static final String CODE_GROP_TE_VERSION = "tever";// tomcat version
	public static final String CODE_GROP_TS_STATE = "TS_STATE";
	public static final String CODE_GROP_DB_TYPE = "dbType";
	public static final String CODE_GROP_CONFIG_FILE_TYPE = "configFileType";
	public static final String CODE_GROP_ALERT_ITEM = "ALERT_ITEM";
	public static final String CODE_GROP_ALERT_THRESHOLD_OPERATOR = "ALERT_THRESHOLD_OPERATOR";
	public static final String CODE_GROP_DATAGRID_SEVER_TYPE = "SESSION_GROUP_TYPE";
	public static final String CODE_GROP_TASK = "TASK";

	public static final Integer ALERT_ITEM_CPU_USED = 18;
	public static final Integer ALERT_ITEM_MEM_USED = 19;
	public static final Integer ALERT_ITEM_DISK_USED = 20;
	public static final Integer ALERT_ITEM_AGENT = 21;

	public static final Integer ALERT_ITEM_OPPERATOR_GREATER_THAN_ID = 16;
	public static final Integer ALERT_ITEM_OPPERATOR_LESS_THAN_ID = 17;

	public static final String CONFIG_FILE_TYPE_SERVER_XML = "server.xml";
	public static final int CONFIG_FILE_TYPE_SERVER_XML_CD = 3;
	public static final String CONFIG_FILE_TYPE_CONTEXT_XML = "context.xml";
	public static final int CONFIG_FILE_TYPE_CONTEXT_XML_CD = 4;

	public static final String DATE_TIME_FORMATTER = "MM/dd/yyyy HH:mm:ss";
	public static final String CONFIG_FILE_VERSION_DATE_TIME_FORMATTER = "MM/dd/yyyy HH:mm";

	/**
	 * Configuration name
	 */
	public static final String TOMCAT_INST_CONFIG_JAVAHOME_NAME = "java_home";
	public static final String TOMCAT_INST_CONFIG_HTTPPORT_NAME = "http_port";

	public static final String TOMCAT_INST_CONFIG_SESSION_TIMEOUT_NAME = "session_timeout";

	public static final String OBJ_TYPE_DOMAIN = "tomcat_domain";
	public static final String OBJ_TYPE_SESSION_SERVER_GROUP = "server_group";
	public static final String OBJ_TYPE_TOMCAT = "tomcat_inst";

	public static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	/**
	 * Monitoring Factor id
	 */
	public static final String MON_FACTOR_CPU_USED = "cpu.used";
	public static final String MON_FACTOR_CPU_USED_PER = "cpu.used_per";
	public static final String MON_FACTOR_MEM_USED = "mem.used";
	public static final String MON_FACTOR_DISK_USED = "disk.used";
	public static final String MON_FACTOR_MEM_USED_PER = "mem.used_per";
	public static final String MON_FACTOR_TI_RUN = "ti.run"; // tomcat instance
																// running
																// status.
	public static final String MON_FACTOR_NI_OUT = "net.out";
	public static final String MON_FACTOR_NI_IN = "net.in";
	public static final String MON_JMX_FACTOR_HEAP_MEMORY = "jmx.HeapMemoryUsage";
	public static final String MON_JMX_FACTOR_CPU_USAGE = "jmx.ProcessCpuLoad";
	public static final String MON_JMX_FACTOR_ACTIVE_THREADS = "jmx.tomcatThreads";
	public static final String MON_JMX_FACTOR_JDBC_CONNECTIONS = "jmx.ds";

	public static final int DISK_MON_PERIOD_MINUTE = 10;

	/**
	 * Define configuration fields that can be edited in domain level and in tomcat instance as well
	 */
	public static final String[] TOMCAT_INSTANCE_CONFIGS_CUSTOM = { "javaHome", "httpPort", "ajpPort", "sessionTimeout", "redirectPort", "jmxEnable",
			"rmiRegistryPort", "rmiServerPort", "catalinaOpts", "tomcatVersion", "encoding" };

	/**
	 * websocket events.
	 */
	public static final String WS_EVENT_INSTALL = "install";
	public static final String WS_EVENT_DEPLOY = "deploy";
	public static final String WS_EVENT_UXMLFILE = "updateXmlFile";
	public static final String WS_EVENT_INSTALL_MYSQL_DRIVER = "installMySQLDriver";

	public static final long MONITORING_MINUTE_INTERVAL = 10;

	public static final String MON_NI_TYPE_IN = "traffic_in";
	public static final String MON_NI_TYPE_OUT = "traffic_out";

	/**
	 * Task constants
	 */
	public static final int TASK_CD_TOMCAT_INSTANCE_INSTALL = 100;//install tomcat instance.
	public static final int TASK_CD_WAR_DEPLOY = 101;
	public static final int TASK_CD_WAR_UNDEPLOY = 105;
	public static final int TASK_CD_SERVER_XML_UPDATE = 102;//update server.xml file
	public static final int TASK_CD_CONTEXT_XML_UPDATE = 103;//update context.xml file
	public static final int TASK_CD_TOMCAT_CONFIG_UPDATE = 104;
	public static final int TASK_CD_JDBC_MYSQL_INSTALL = 106;
	public static final int TASK_CD_TOMCAT_INSTANCE_UNINSTALL = 107;

	public static final int TASK_STATUS_WORKING = 1;
	public static final int TASK_STATUS_SUCCESS = 2;
	public static final int TASK_STATUS_FAIL = 3;

	public static final Integer DASHBOARD_ALERT_COUNT = 10;

}
