

ALTER TABLE `domain_tomcat_configuration` 
CHANGE COLUMN `java_home` `java_home` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `catalina_home` `catalina_home` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `catalina_base` `catalina_base` VARCHAR(100) NOT NULL ;


CREATE TABLE `mon_data_tbl` (
  `mon_factor_id` varchar(10) NOT NULL,
  `server_id` INT(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  PRIMARY KEY (`mon_factor_id`,`server_id`,`mon_dt`)
) ENGINE=InnoDB;

CREATE TABLE `mon_jmx_tbl` (
  `mon_factor_id` varchar(10) NOT NULL,
  `instance_id` INT(11) NOT NULL,
  `mon_dt` datetime NOT NULL,
  `mon_value` double NOT NULL,
  PRIMARY KEY (`mon_factor_id`,`instance_id`,`mon_dt`)
) ENGINE=InnoDB;