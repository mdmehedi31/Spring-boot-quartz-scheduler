

CREATE SCHEMA `qrtz_practices` ;

CREATE TABLE `qrtz_practices`.`campaign_name` (
                                                  `id` INT NOT NULL AUTO_INCREMENT,
                                                  `name` VARCHAR(45) NULL,
                                                  PRIMARY KEY (`id`));

CREATE TABLE `qrtz_practices`.`cmp_emails` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `camp_id` INT NULL,
                                               `name` VARCHAR(200) NULL,
                                               `email` VARCHAR(250) NULL,
                                               PRIMARY KEY (`id`));
