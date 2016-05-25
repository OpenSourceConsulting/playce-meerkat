

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
