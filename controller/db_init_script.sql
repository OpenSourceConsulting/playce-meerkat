-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 192.168.0.87    Database: athena_meerkat_trystack
-- ------------------------------------------------------
-- Server version	5.5.44-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clustering_conf_version`
--

DROP TABLE IF EXISTS `clustering_conf_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clustering_conf_version` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clustering_conf_version`
--

LOCK TABLES `clustering_conf_version` WRITE;
/*!40000 ALTER TABLE `clustering_conf_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `clustering_conf_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clustering_configuration`
--

DROP TABLE IF EXISTS `clustering_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clustering_configuration` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `datagrid_server_group_id` int(11) DEFAULT NULL,
  `domain_id` int(11) DEFAULT NULL,
  `clustering_conf_version_id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_clustering_configuration_value_datagrid_server_group1_idx` (`datagrid_server_group_id`),
  KEY `fk_clustering_configuration_value_domain1_idx` (`domain_id`),
  KEY `fk_clustering_configuration_clustering_conf_version1_idx` (`clustering_conf_version_id`),
  CONSTRAINT `fk_clustering_configuration_clustering_conf_version1` FOREIGN KEY (`clustering_conf_version_id`) REFERENCES `clustering_conf_version` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_clustering_configuration_value_datagrid_server_group1` FOREIGN KEY (`datagrid_server_group_id`) REFERENCES `datagrid_server_group` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_clustering_configuration_value_domain1` FOREIGN KEY (`domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clustering_configuration`
--

LOCK TABLES `clustering_configuration` WRITE;
/*!40000 ALTER TABLE `clustering_configuration` DISABLE KEYS */;
/*!40000 ALTER TABLE `clustering_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_code`
--

DROP TABLE IF EXISTS `common_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_code` (
  `id` int(11) NOT NULL COMMENT '코드id',
  `grop_id` varchar(45) NOT NULL COMMENT '그룹코드',
  `code_nm` varchar(45) NOT NULL COMMENT '코드명',
  `prto_seq` smallint(6) DEFAULT NULL COMMENT '출력순서',
  `cd_desc` varchar(100) DEFAULT NULL COMMENT '코드설명',
  `mng_yn` char(1) DEFAULT 'N' COMMENT '관리여부',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_code`
--

LOCK TABLES `common_code` WRITE;
/*!40000 ALTER TABLE `common_code` DISABLE KEYS */;
INSERT INTO `common_code` VALUES (1,'dbType','MySQL',1,'Datasource Type','N'),(2,'dbType','Oracle',2,NULL,'N'),(3,'configFileType','server.xml',1,'설정파일 Type','N'),(4,'configFileType','context.xml',2,NULL,'N'),(5,'TS_STATE','not installed',1,'톰캣instance 상태','N'),(6,'TS_STATE','installed',2,NULL,'N'),(7,'TS_STATE','stop',3,NULL,'N'),(8,'TS_STATE','running',4,NULL,'N'),(9,'tever','apache-tomcat-7.0.68',2,'Tomcat Engine Version, 반드시 download url 에 사용될 zip file name 을 사용해야함.','N'),(10,'SESSION_GROUP_TYPE','Infinispan',1,NULL,'N'),(11,'SESSION_GROUP_TYPE','Couchbase',2,NULL,'N'),(12,'tever','apache-tomcat-6.0.45',1,NULL,'N'),(13,'tever','apache-tomcat-8.0.36',3,NULL,'N'),(14,'TS_STATE','starting',5,NULL,'N'),(15,'TS_STATE','stopping',6,NULL,'N'),(16,'ALERT_THRESHOLD_OPERATOR','Greater than (>)',1,NULL,'N'),(17,'ALERT_THRESHOLD_OPERATOR','Less than (<)',2,NULL,'N'),(18,'ALERT_ITEM','Memory Usage',1,NULL,'N'),(19,'ALERT_ITEM','CPU Usage',2,NULL,'N'),(20,'ALERT_ITEM','Disk Usage',3,NULL,'N'),(21,'ALERT_ITEM','Agent Running',4,NULL,'N'),(22,'TS_STATE','start fail',NULL,NULL,'N'),(23,'TS_STATE','stop fail',NULL,NULL,'N'),(100,'TASK','Tomcat Instance 설치',1,'task code','N'),(101,'TASK','Application(war) 배포',2,NULL,'N'),(102,'TASK','server.xml 수정',3,NULL,'N'),(103,'TASK','context.xml 수정',4,NULL,'N'),(104,'TASK','Tomcat 설정 수정',5,NULL,'N'),(105,'TASK','Application(war) 삭제',6,NULL,'N'),(106,'TASK','MySQL JDBC Driver 설치',7,NULL,'N'),(107,'TASK','Tomcat Instance 삭제',8,NULL,'N'),(108,'TASK','Datasource 추가',9,NULL,'N'),(109,'TASK','Datasource 삭제',10,NULL,'N'),(112,'TASK','Agent 재설치',13,NULL,'N'),(113,'TASK','Scouter Agent 설치/설정',14,NULL,'N'),(114,'TASK','Scouter Agent 설정 제거',15,NULL,'N'),(115,'TASK','Scouter Agent 설정 변경',16,NULL,'N'),(116,'TASK','Tomcat Instance 시작',17,NULL,'N'),(117,'TASK','Tomcat Instance 정지',18,NULL,'N');
/*!40000 ALTER TABLE `common_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_source`
--

DROP TABLE IF EXISTS `data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `username` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(100) CHARACTER SET euckr DEFAULT NULL,
  `max_connection` int(11) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `max_connection_pool` int(11) DEFAULT NULL,
  `min_connection_pool` int(11) DEFAULT NULL,
  `jdbc_url` varchar(500) CHARACTER SET euckr DEFAULT NULL,
  `dbtype_cd_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=euckr COLLATE=euckr_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_source`
--

LOCK TABLES `data_source` WRITE;
/*!40000 ALTER TABLE `data_source` DISABLE KEYS */;
INSERT INTO `data_source` VALUES (1,'192.168.0.153','meerkat','meerkat',8,1500,16,16,'jdbc:mysql://192.168.0.153:3306/test?useUnicode=true&characterEncoding=UTF-8',1);
/*!40000 ALTER TABLE `data_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datagrid_server_group`
--

DROP TABLE IF EXISTS `datagrid_server_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datagrid_server_group` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) CHARACTER SET euckr DEFAULT NULL,
  `type_cd_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=euckr COLLATE=euckr_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datagrid_server_group`
--

LOCK TABLES `datagrid_server_group` WRITE;
/*!40000 ALTER TABLE `datagrid_server_group` DISABLE KEYS */;
INSERT INTO `datagrid_server_group` VALUES (5,'Frontend Session Group',10),(6,'API Session Group',10);
/*!40000 ALTER TABLE `datagrid_server_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datagrid_servers`
--

DROP TABLE IF EXISTS `datagrid_servers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datagrid_servers` (
  `datagrid_server_group_Id` int(11) NOT NULL,
  `server_Id` int(11) NOT NULL,
  `port` mediumint(9) DEFAULT NULL,
  PRIMARY KEY (`datagrid_server_group_Id`,`server_Id`),
  KEY `fk_datagrid_servers_server1_idx` (`server_Id`),
  CONSTRAINT `fk_datagrid_servers_datagrid_server_group1` FOREIGN KEY (`datagrid_server_group_Id`) REFERENCES `datagrid_server_group` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_datagrid_servers_server1` FOREIGN KEY (`server_Id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datagrid_servers`
--

LOCK TABLES `datagrid_servers` WRITE;
/*!40000 ALTER TABLE `datagrid_servers` DISABLE KEYS */;
/*!40000 ALTER TABLE `datagrid_servers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain_tomcat_configuration`
--

DROP TABLE IF EXISTS `domain_tomcat_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_tomcat_configuration` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `domain_id` int(11) NOT NULL,
  `java_home` varchar(100) NOT NULL,
  `catalina_home` varchar(100) NOT NULL,
  `catalina_base` varchar(100) NOT NULL,
  `http_port` int(11) NOT NULL DEFAULT '8080',
  `session_timeout` int(11) NOT NULL DEFAULT '30',
  `ajp_port` int(11) DEFAULT NULL,
  `redirect_port` int(11) DEFAULT NULL,
  `jmx_enable` bit(1) DEFAULT NULL,
  `rmi_registry_port` int(11) DEFAULT NULL,
  `rmi_server_port` int(11) DEFAULT NULL,
  `catalina_opts` varchar(150) DEFAULT NULL,
  `modified_user_id` int(11) NOT NULL,
  `modified_date` datetime NOT NULL,
  `tomcat_version_cd` int(11) DEFAULT NULL,
  `encoding` varchar(45) DEFAULT NULL,
  `server_port` mediumint(9) DEFAULT '8005',
  PRIMARY KEY (`Id`,`domain_id`),
  KEY `fk_domain_tomcat_configuration_tomcat_domain1_idx` (`domain_id`),
  CONSTRAINT `fk_domain_tomcat_configuration_tomcat_domain1` FOREIGN KEY (`domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain_tomcat_configuration`
--

LOCK TABLES `domain_tomcat_configuration` WRITE;
/*!40000 ALTER TABLE `domain_tomcat_configuration` DISABLE KEYS */;
INSERT INTO `domain_tomcat_configuration` VALUES (1,128,'/usr/java/jdk1.8.0_91','/home/centos/servers/apache-tomcat-8.0.36','/home/centos/apps/instance1',8080,30,8009,0,'\0',8225,8226,'export CATALINA_OPTS=\"-server -Xms512m -Xmx512m\"',23,'2016-06-29 13:21:02',13,'UTF-8',8005);
/*!40000 ALTER TABLE `domain_tomcat_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_alert_config`
--

DROP TABLE IF EXISTS `mon_alert_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mon_alert_config` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tomcat_domain_id` int(11) DEFAULT NULL,
  `server_Id` int(11) DEFAULT NULL,
  `alert_item_cd_id` int(11) DEFAULT NULL COMMENT 'alert item code id',
  `threshold_op_cd_id` int(11) DEFAULT NULL,
  `threshold_value` int(11) DEFAULT NULL,
  `status` smallint(6) DEFAULT '0' COMMENT '0 : disabled, 1:enabled',
  `mon_factor_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_tomcat_domain_alert_tomcat_domain2_idx` (`tomcat_domain_id`),
  KEY `fk_tomcat_domain_alert_server1_idx` (`server_Id`),
  CONSTRAINT `fk_tomcat_domain_alert_tomcat_domain2` FOREIGN KEY (`tomcat_domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_domain_alert_server1` FOREIGN KEY (`server_Id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_alert_config`
--

LOCK TABLES `mon_alert_config` WRITE;
/*!40000 ALTER TABLE `mon_alert_config` DISABLE KEYS */;
INSERT INTO `mon_alert_config` VALUES (1,NULL,32,18,16,80,0,'cpu.used_per'),(2,NULL,32,19,16,80,0,'mem.used_per'),(3,NULL,32,20,16,80,0,NULL),(4,NULL,32,21,0,NULL,1,NULL),(5,128,NULL,18,16,80,0,'cpu.used_per'),(6,128,NULL,19,16,80,0,'mem.used_per');
/*!40000 ALTER TABLE `mon_alert_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_data_tbl`
--

DROP TABLE IF EXISTS `mon_data_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mon_data_tbl` (
  `mon_factor_id` varchar(20) NOT NULL,
  `server_id` int(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  PRIMARY KEY (`mon_factor_id`,`server_id`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_data_tbl`
--

LOCK TABLES `mon_data_tbl` WRITE;
/*!40000 ALTER TABLE `mon_data_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `mon_data_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_fs_tbl`
--

DROP TABLE IF EXISTS `mon_fs_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mon_fs_tbl` (
  `server_id` int(11) NOT NULL,
  `fs_name` varchar(45) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `total` int(11) NOT NULL,
  `used` int(11) NOT NULL,
  `use_per` double DEFAULT NULL,
  `avail` int(11) DEFAULT NULL,
  PRIMARY KEY (`server_id`,`fs_name`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_fs_tbl`
--

LOCK TABLES `mon_fs_tbl` WRITE;
/*!40000 ALTER TABLE `mon_fs_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `mon_fs_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_jmx_tbl`
--

DROP TABLE IF EXISTS `mon_jmx_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mon_jmx_tbl` (
  `mon_factor_id` varchar(40) NOT NULL,
  `instance_id` int(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  `mon_value2` double DEFAULT NULL,
  PRIMARY KEY (`mon_factor_id`,`instance_id`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_jmx_tbl`
--

LOCK TABLES `mon_jmx_tbl` WRITE;
/*!40000 ALTER TABLE `mon_jmx_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `mon_jmx_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_util_stat`
--

DROP TABLE IF EXISTS `mon_util_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mon_util_stat` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `server_id` int(11) DEFAULT NULL,
  `tomcat_instance_id` int(11) DEFAULT NULL,
  `mon_factor_id` varchar(40) NOT NULL,
  `mon_value` double DEFAULT '0' COMMENT 'percentage value.',
  `update_dt` datetime DEFAULT NULL COMMENT '최근 업데이트 일시.',
  PRIMARY KEY (`Id`),
  KEY `fk_mon_util_stat_tomcat_instance1_idx` (`tomcat_instance_id`),
  KEY `fk_mon_util_stat_server1_idx` (`server_id`),
  CONSTRAINT `fk_mon_util_stat_tomcat_instance1` FOREIGN KEY (`tomcat_instance_id`) REFERENCES `tomcat_instance` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_mon_util_stat_server1` FOREIGN KEY (`server_id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_util_stat`
--

LOCK TABLES `mon_util_stat` WRITE;
/*!40000 ALTER TABLE `mon_util_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `mon_util_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `network_interface`
--

DROP TABLE IF EXISTS `network_interface`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `network_interface` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `ipv4` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `ipv6` varchar(45) CHARACTER SET euckr DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `default_gateway` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `netmask` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ni_machine_fk_idx` (`server_id`),
  CONSTRAINT `ni_server_fk` FOREIGN KEY (`server_id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=euckr COLLATE=euckr_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `network_interface`
--

LOCK TABLES `network_interface` WRITE;
/*!40000 ALTER TABLE `network_interface` DISABLE KEYS */;
INSERT INTO `network_interface` VALUES (22,NULL,'192.168.0.153',NULL,32,NULL,NULL);
/*!40000 ALTER TABLE `network_interface` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `host_name` varchar(60) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `os_name` varchar(20) DEFAULT NULL,
  `os_version` varchar(10) DEFAULT NULL,
  `cpu_clock_speed` float DEFAULT NULL,
  `cpu_clock_unit` varchar(10) DEFAULT NULL,
  `cpu_core` int(11) DEFAULT NULL,
  `os_arch` varchar(15) CHARACTER SET euckr DEFAULT NULL,
  `origin_date` datetime DEFAULT NULL,
  `last_shutdown` datetime DEFAULT NULL,
  `description` varchar(45) CHARACTER SET euckr DEFAULT NULL,
  `is_vm` bit(1) DEFAULT NULL,
  `jvm` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `disk_size` int(11) DEFAULT NULL,
  `disk_size_unit` varchar(15) CHARACTER SET euckr DEFAULT NULL,
  `memory_size` int(11) DEFAULT NULL,
  `memory_size_unit` varchar(15) CHARACTER SET euckr DEFAULT NULL,
  `ssh_ni_id` int(11) DEFAULT NULL,
  `ssh_port` int(11) DEFAULT NULL,
  `agent_installed` bit(1) DEFAULT b'0' COMMENT 'agent 설치 여부.',
  PRIMARY KEY (`Id`),
  KEY `fk_server_ni_idx` (`ssh_ni_id`),
  CONSTRAINT `fk_server_ni` FOREIGN KEY (`ssh_ni_id`) REFERENCES `network_interface` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server`
--

LOCK TABLES `server` WRITE;
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` VALUES (32,'Local test',NULL,0,NULL,NULL,0,NULL,0,NULL,NULL,NULL,NULL,'\0',NULL,0,NULL,0,NULL,22,22,'\0');
/*!40000 ALTER TABLE `server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ssh_account`
--

DROP TABLE IF EXISTS `ssh_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssh_account` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `is_root` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_ssh_account_server1_idx` (`server_id`),
  CONSTRAINT `fk_ssh_account_server1` FOREIGN KEY (`server_id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssh_account`
--

LOCK TABLES `ssh_account` WRITE;
/*!40000 ALTER TABLE `ssh_account` DISABLE KEYS */;
INSERT INTO `ssh_account` VALUES (1,'meerkat','meerkat',32,NULL);
/*!40000 ALTER TABLE `ssh_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_history`
--

DROP TABLE IF EXISTS `task_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_history` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `task_cd_id` int(11) NOT NULL COMMENT 'task code id',
  `create_user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_history`
--

LOCK TABLES `task_history` WRITE;
/*!40000 ALTER TABLE `task_history` DISABLE KEYS */;
INSERT INTO `task_history` VALUES (12,100,23,'2016-06-29 13:21:36');
/*!40000 ALTER TABLE `task_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_history_detail`
--

DROP TABLE IF EXISTS `task_history_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_history_detail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `task_history_id` int(11) NOT NULL,
  `tomcat_domain_id` int(11) DEFAULT NULL,
  `tomcat_instance_id` int(11) DEFAULT NULL,
  `tomcat_domain_name` varchar(30) DEFAULT NULL,
  `tomcat_instance_name` varchar(30) DEFAULT NULL,
  `host_name` varchar(60) DEFAULT NULL,
  `ip_addr` varchar(20) DEFAULT NULL,
  `status` smallint(6) DEFAULT '0' COMMENT '0:작업대기중, 1: 작업진행중, 2: 작업완료, 3: 작업실패',
  `logfile_path` varchar(200) DEFAULT NULL,
  `finished_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_task_history_domain_task_history1_idx` (`task_history_id`),
  KEY `idx_domain_id_instance_id` (`tomcat_domain_id`,`tomcat_instance_id`),
  CONSTRAINT `fk_task_history_domain_task_history1` FOREIGN KEY (`task_history_id`) REFERENCES `task_history` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_history_detail`
--

LOCK TABLES `task_history_detail` WRITE;
/*!40000 ALTER TABLE `task_history_detail` DISABLE KEYS */;
INSERT INTO `task_history_detail` VALUES (1,12,128,91,NULL,'Test Tomcat',NULL,'192.168.0.153',3,'D:\\git\\athena-meerkat\\_test\\.aMeerkat\\jobs\\192.168.0.153\\1\\build.log','2016-06-29 13:21:43');
/*!40000 ALTER TABLE `task_history_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_application`
--

DROP TABLE IF EXISTS `tomcat_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_application` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `context_path` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `deployed_time` datetime DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `war_path` varchar(100) CHARACTER SET euckr DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `domain_id` int(11) DEFAULT NULL,
  `tomcat_instance_id` int(11) DEFAULT NULL,
  `task_history_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `application_state_fk_idx` (`state`),
  KEY `fk_domain_application_tomcat_domain1_idx` (`domain_id`),
  KEY `fk_domain_application_tomcat_instance1_idx` (`tomcat_instance_id`),
  CONSTRAINT `fk_domain_application_tomcat_domain1` FOREIGN KEY (`domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_domain_application_tomcat_instance1` FOREIGN KEY (`tomcat_instance_id`) REFERENCES `tomcat_instance` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_application`
--

LOCK TABLES `tomcat_application` WRITE;
/*!40000 ALTER TABLE `tomcat_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `tomcat_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_config_file`
--

DROP TABLE IF EXISTS `tomcat_config_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_config_file` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `domain_id` int(11) DEFAULT NULL,
  `file_type_cd_id` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `comment` varchar(45) DEFAULT NULL,
  `file_path` varchar(45) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `tomcat_instance_id` int(11) DEFAULT NULL,
  `create_user_id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_server_xml_domain1_idx` (`domain_id`),
  KEY `fk_tomcat_config_file_tomcat_instance1_idx` (`tomcat_instance_id`),
  KEY `fk_tomcat_config_file_user1_idx` (`create_user_id`),
  CONSTRAINT `fk_server_xml_domain10` FOREIGN KEY (`domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_config_file_tomcat_instance1` FOREIGN KEY (`tomcat_instance_id`) REFERENCES `tomcat_instance` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_config_file_user1` FOREIGN KEY (`create_user_id`) REFERENCES `user` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_config_file`
--

LOCK TABLES `tomcat_config_file` WRITE;
/*!40000 ALTER TABLE `tomcat_config_file` DISABLE KEYS */;
INSERT INTO `tomcat_config_file` VALUES (1,128,3,1,NULL,'conffiles\\128_null\\server_1.xml','2016-06-29 13:21:02',NULL,23),(2,128,4,1,NULL,'conffiles\\128_null\\context_1.xml','2016-06-29 13:21:02',NULL,23);
/*!40000 ALTER TABLE `tomcat_config_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_domain`
--

DROP TABLE IF EXISTS `tomcat_domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_domain` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET euckr NOT NULL,
  `datagrid_server_group_id` int(11) DEFAULT NULL,
  `scouter_agent_install_path` varchar(60) COLLATE euckr_bin DEFAULT NULL,
  `create_user_id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `datagrid_server_group_idx` (`datagrid_server_group_id`),
  KEY `fk_tomcat_domain_user1_idx` (`create_user_id`),
  CONSTRAINT `datagrid_server_group` FOREIGN KEY (`datagrid_server_group_id`) REFERENCES `datagrid_server_group` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_domain_user1` FOREIGN KEY (`create_user_id`) REFERENCES `user` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=euckr COLLATE=euckr_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_domain`
--

LOCK TABLES `tomcat_domain` WRITE;
/*!40000 ALTER TABLE `tomcat_domain` DISABLE KEYS */;
INSERT INTO `tomcat_domain` VALUES (128,'Domain 1',NULL,NULL,23);
/*!40000 ALTER TABLE `tomcat_domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_domain_alert`
--

DROP TABLE IF EXISTS `tomcat_domain_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_domain_alert` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tomcat_domain_id` int(11) NOT NULL,
  `alert_item_cd_id` int(11) DEFAULT NULL COMMENT 'alert item code id',
  `threshold_op_cd_id` int(11) DEFAULT NULL,
  `threshold_value` int(11) DEFAULT NULL,
  `status` smallint(6) DEFAULT '0' COMMENT '0 : disabled, 1:enabled',
  PRIMARY KEY (`Id`),
  KEY `fk_tomcat_domain_alert_tomcat_domain1_idx` (`tomcat_domain_id`),
  CONSTRAINT `fk_tomcat_domain_alert_tomcat_domain1` FOREIGN KEY (`tomcat_domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_domain_alert`
--

LOCK TABLES `tomcat_domain_alert` WRITE;
/*!40000 ALTER TABLE `tomcat_domain_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `tomcat_domain_alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_domain_datasource`
--

DROP TABLE IF EXISTS `tomcat_domain_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_domain_datasource` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `datasource_id` int(11) DEFAULT NULL,
  `tomcat_domain_id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `td_datasource_fk_idx` (`datasource_id`),
  KEY `fk_tomcat_datasource_tomcat_domain1_idx` (`tomcat_domain_id`),
  CONSTRAINT `fk_tomcat_datasource_tomcat_domain1` FOREIGN KEY (`tomcat_domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `td_datasource_fk` FOREIGN KEY (`datasource_id`) REFERENCES `data_source` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=euckr COLLATE=euckr_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_domain_datasource`
--

LOCK TABLES `tomcat_domain_datasource` WRITE;
/*!40000 ALTER TABLE `tomcat_domain_datasource` DISABLE KEYS */;
INSERT INTO `tomcat_domain_datasource` VALUES (1,1,128);
/*!40000 ALTER TABLE `tomcat_domain_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_inst_config`
--

DROP TABLE IF EXISTS `tomcat_inst_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_inst_config` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tomcat_instance_id` int(11) NOT NULL,
  `config_name` varchar(20) DEFAULT NULL,
  `config_value` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_tomcat_inst_config_tomcat_instance1_idx` (`tomcat_instance_id`),
  CONSTRAINT `fk_tomcat_inst_config_tomcat_instance1` FOREIGN KEY (`tomcat_instance_id`) REFERENCES `tomcat_instance` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_inst_config`
--

LOCK TABLES `tomcat_inst_config` WRITE;
/*!40000 ALTER TABLE `tomcat_inst_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `tomcat_inst_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tomcat_instance`
--

DROP TABLE IF EXISTS `tomcat_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tomcat_instance` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `domain_id` int(11) NOT NULL,
  `server_id` int(11) NOT NULL,
  `state` int(11) DEFAULT '5',
  `name` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `network_interface_id` int(11) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `create_user_id` int(11) NOT NULL,
  `task_history_id` int(11) DEFAULT NULL COMMENT '최근 install task history id.',
  PRIMARY KEY (`Id`),
  KEY `domain_fk_idx` (`domain_id`),
  KEY `state_fk_idx` (`state`),
  KEY `tomcat_machine_fk_idx` (`server_id`),
  KEY `fk_tomcat_instance_network_interface1_idx` (`network_interface_id`),
  KEY `fk_tomcat_instance_user1_idx` (`create_user_id`),
  CONSTRAINT `fk_tomcat_instance_network_interface1` FOREIGN KEY (`network_interface_id`) REFERENCES `network_interface` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_instance_user1` FOREIGN KEY (`create_user_id`) REFERENCES `user` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tomcat_domain_fk` FOREIGN KEY (`domain_id`) REFERENCES `tomcat_domain` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tomcat_server_fk` FOREIGN KEY (`server_id`) REFERENCES `server` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tomcat_instance`
--

LOCK TABLES `tomcat_instance` WRITE;
/*!40000 ALTER TABLE `tomcat_instance` DISABLE KEYS */;
INSERT INTO `tomcat_instance` VALUES (91,128,32,5,'Test Tomcat',NULL,'2016-06-29 13:21:36',23,12);
/*!40000 ALTER TABLE `tomcat_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `username` varchar(20) CHARACTER SET euckr NOT NULL,
  `password` varchar(100) CHARACTER SET euckr NOT NULL,
  `email` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=euckr COLLATE=euckr_bin COMMENT='				';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (23,'admin','admin','admin','admin@gmail.com','2016-02-15 17:52:14',NULL),(24,'idkbj','idkbj','11111','idkbj@osci.kr',NULL,NULL),(25,'Tran Ho1','tran.ho','admin','tran.ho@osci.kr',NULL,NULL),(27,'resadmin','resadmin','admin','admin@gmail.com',NULL,NULL),(29,'Jerry Jeong','jerry','admin','jerry@osci.kr',NULL,NULL),(30,'Ho Tran','htran','admin','tran@osci.kr',NULL,NULL),(31,'Tran Ho 2','htran2','12345','tran@gmail.com',NULL,'2016-04-27 19:02:54');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_multi_role`
--

DROP TABLE IF EXISTS `user_multi_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_multi_role` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `user_mapping_fk_idx` (`user_id`),
  KEY `user_role_mapping_fk_idx` (`role_id`),
  CONSTRAINT `user_mapping_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_role_mapping_fk` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_multi_role`
--

LOCK TABLES `user_multi_role` WRITE;
/*!40000 ALTER TABLE `user_multi_role` DISABLE KEYS */;
INSERT INTO `user_multi_role` VALUES (1,23,1),(4,23,2),(5,23,4),(6,27,1),(9,30,1),(10,30,2),(11,31,7),(12,24,6),(13,24,7),(14,25,1),(15,25,2),(16,25,4),(17,29,1);
/*!40000 ALTER TABLE `user_multi_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET euckr DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=euckr COLLATE=euckr_bin COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER_USER'),(4,'ROLE_RES_ADMIN'),(5,'ROLE_TOMCAT_ADMIN'),(6,'ROLE_TOMCAT_USER'),(7,'ROLE_USER_ADMIN');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-06 15:03:59
