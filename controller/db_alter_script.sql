

ALTER TABLE `domain_tomcat_configuration` 
CHANGE COLUMN `java_home` `java_home` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `catalina_home` `catalina_home` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `catalina_base` `catalina_base` VARCHAR(100) NOT NULL ;


CREATE TABLE `mon_data_tbl` (
  `mon_factor_id` varchar(20) NOT NULL,
  `server_id` INT(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  PRIMARY KEY (`mon_factor_id`,`server_id`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mon_jmx_tbl` (
  `mon_factor_id` varchar(40) NOT NULL,
  `instance_id` INT(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  PRIMARY KEY (`mon_factor_id`,`instance_id`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mon_fs_tbl` (
  `server_id` int(11) NOT NULL,
  `fs_name` varchar(45) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `total` int(11) NOT NULL,
  `used` int(11) NOT NULL,
  `use_per` double NULL,
  `avail` int(11) NULL,
  PRIMARY KEY (`server_id`,`fs_name`,`mon_dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `common_code` 
CHANGE COLUMN `cd_desc` `cd_desc` VARCHAR(100) NULL DEFAULT NULL COMMENT '코드설명' ;


ALTER TABLE `server` 
CHANGE COLUMN `disk_size` `disk_size` INT(11) NULL DEFAULT NULL ,
CHANGE COLUMN `memory_size` `memory_size` INT(11) NULL DEFAULT NULL ;

ALTER TABLE `mon_jmx_tbl` CHANGE COLUMN `mon_factor_id` `mon_factor_id` VARCHAR(40) NOT NULL ;
ALTER TABLE `mon_jmx_tbl` ADD COLUMN `mon_value2` DOUBLE NULL AFTER `mon_value`;


ALTER TABLE `domain_tomcat_configuration` ADD COLUMN `server_port` MEDIUMINT NULL DEFAULT 8005 AFTER `encoding`;
UPDATE domain_tomcat_configuration SET server_port=8005;

ALTER TABLE `datagrid_server_group` CHANGE COLUMN `name` `name` VARCHAR(60) NULL DEFAULT NULL ;


ALTER TABLE `server` CHANGE COLUMN `host_name` `host_name` VARCHAR(60) NULL DEFAULT NULL ;


CREATE TABLE IF NOT EXISTS `datagrid_servers` (
  `datagrid_server_group_Id` INT(11) NOT NULL,
  `server_Id` INT(11) NOT NULL,
  `port` MEDIUMINT NULL,
  PRIMARY KEY (`datagrid_server_group_Id`, `server_Id`),
  INDEX `fk_datagrid_servers_server1_idx` (`server_Id` ASC),
  CONSTRAINT `fk_datagrid_servers_datagrid_server_group1`
    FOREIGN KEY (`datagrid_server_group_Id`)
    REFERENCES `athena_meerkat_local`.`datagrid_server_group` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_datagrid_servers_server1`
    FOREIGN KEY (`server_Id`)
    REFERENCES `athena_meerkat_local`.`server` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


ALTER TABLE `server` DROP FOREIGN KEY `fk_server_datagrid_server_group1`;
ALTER TABLE `server` DROP COLUMN `datagrid_server_group_id`, DROP INDEX `fk_server_datagrid_server_group1_idx` ;


CREATE TABLE IF NOT EXISTS `tomcat_domain_alert` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `tomcat_domain_Id` INT(11) NOT NULL,
  `alert_item_cd_id` INT(11) NULL COMMENT 'alert item code id',
  `threshold_op_cd_id` INT(11) NULL,
  `threshold_value` INT(11) NULL,
  `status` SMALLINT NULL DEFAULT 0 COMMENT '0 : disabled, 1:enabled',
  PRIMARY KEY (`Id`),
  INDEX `fk_tomcat_domain_alert_tomcat_domain1_idx` (`tomcat_domain_Id` ASC),
  CONSTRAINT `fk_tomcat_domain_alert_tomcat_domain1`
    FOREIGN KEY (`tomcat_domain_Id`)
    REFERENCES `tomcat_domain` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `task_history` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `task_cd_id` INT(11) NOT NULL COMMENT 'task code id',
  `create_user_id` INT(11) NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`Id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `task_history_detail` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `task_history_id` INT(11) NOT NULL,
  `tomcat_domain_id` INT(11) NULL,
  `tomcat_instance_id` INT(11) NULL,
  `tomcat_domain_name` VARCHAR(30) NULL,
  `tomcat_instance_name` VARCHAR(30) NULL,
  `host_name` VARCHAR(60) NULL,
  `ip_addr` VARCHAR(20) NULL,
  `status` SMALLINT NULL DEFAULT 0 COMMENT '0:작업대기중, 1: 작업진행중, 2: 작업완료, 3: 작업실패',
  `logfile_path` VARCHAR(200) NULL,
  `finished_time` DATETIME NULL,
  PRIMARY KEY (`Id`),
  INDEX `fk_task_history_domain_task_history1_idx` (`task_history_id` ASC),
  INDEX `idx_domain_id_instance_id` (`tomcat_domain_id` ASC, `tomcat_instance_id` ASC),
  CONSTRAINT `fk_task_history_domain_task_history1`
    FOREIGN KEY (`task_history_id`)
    REFERENCES `task_history` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


ALTER TABLE `tomcat_application` 
CHARACTER SET = DEFAULT , COLLATE = DEFAULT ,
ADD COLUMN `task_history_id` INT(11) NULL AFTER `tomcat_instance_id`;


CREATE TABLE IF NOT EXISTS `mon_util_stat` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `server_id` INT NULL,
  `tomcat_instance_id` INT NULL,
  `mon_factor_id` VARCHAR(40) NOT NULL,
  `mon_value` DOUBLE NULL DEFAULT 0 COMMENT 'percentage value.',
  `update_dt` DATETIME NULL COMMENT '최근 업데이트 일시.',
  PRIMARY KEY (`Id`))
ENGINE = InnoDB;

ALTER TABLE `tomcat_instance` 
ADD COLUMN `task_history_id` INT(11) NULL COMMENT '최근 install task history id.' AFTER `create_user_id`;
update tomcat_instance set task_history_id = 0;

ALTER TABLE `server` 
ADD COLUMN `agent_installed` BIT(1) NULL DEFAULT 0 COMMENT 'agent 설치 여부.' AFTER `ssh_port`;



CREATE TABLE IF NOT EXISTS `mon_alert_config` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `tomcat_domain_id` INT(11) NULL,
  `server_Id` INT(11) NULL,
  `alert_item_cd_id` INT(11) NULL COMMENT 'alert item code id',
  `threshold_op_cd_id` INT(11) NULL,
  `threshold_value` INT(11) NULL,
  `status` SMALLINT NULL DEFAULT 0 COMMENT '0 : disabled, 1:enabled',
  `mon_factor_id` VARCHAR(40) NULL,
  PRIMARY KEY (`Id`),
  INDEX `fk_tomcat_domain_alert_tomcat_domain2_idx` (`tomcat_domain_id` ASC),
  INDEX `fk_tomcat_domain_alert_server1_idx` (`server_Id` ASC),
  CONSTRAINT `fk_tomcat_domain_alert_tomcat_domain2`
    FOREIGN KEY (`tomcat_domain_id`)
    REFERENCES `tomcat_domain` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tomcat_domain_alert_server1`
    FOREIGN KEY (`server_Id`)
    REFERENCES `server` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
