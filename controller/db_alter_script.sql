

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
