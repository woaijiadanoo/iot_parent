CREATE TABLE IF NOT EXISTS `tenant`
(
    `uuid`  VARCHAR(31) NOT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `phone` VARCHAR(50)  DEFAULT NULL,
    `name`  VARCHAR(255) DEFAULT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;


CREATE TABLE IF NOT EXISTS `user`
(
    `uuid`           VARCHAR(31) PRIMARY KEY NOT NULL,
    `tenant_id`      VARCHAR(31)  DEFAULT NULL,
    `user_name`      VARCHAR(100)            NOT NULL,
    `phone`          VARCHAR(50)  DEFAULT NULL,
    `email`          VARCHAR(255) DEFAULT NULL,
    `authority_role` VARCHAR(50)             NOT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- f20cc080-c89f-11ec-989e-8b76480d43cf
insert into `user`
values ('1ecc89ff20cc080989e8b76480d43cf', '1ecc89ff20cc080989e8b76480d43cf', 'ruyuan_00', '13800000000',
        'ruyuan_00@gmail.com', 'TENANT_ADMIN');

-- 940f0e60-c8a0-11ec-989e-8b76480d43cf
insert into `user`
values ('1ecc8a0940f0e60989e8b76480d43cf', '1ecc8a0940f0e60989e8b76480d43cf', 'ruyuan_01', '13811111111',
        'ruyuan_01@gmail.com', 'USER');


CREATE TABLE IF NOT EXISTS `product`
(
    `uuid`             VARCHAR(31) PRIMARY KEY NOT NULL,
    `user_id`          VARCHAR(31)                      DEFAULT NULL,
    `tenant_id`        VARCHAR(31)                      DEFAULT NULL,
    `product_name`     VARCHAR(100)            NOT NULL,
    `product_type`     VARCHAR(100)                     DEFAULT NULL,
    `device_type`      TINYINT                          DEFAULT NULL,
    `net_type`         TINYINT                 NOT NULL DEFAULT 0,
    `data_format`      TINYINT                 NOT NULL DEFAULT 0,
    `data_check_level` TINYINT                 NOT NULL DEFAULT 0,
    `auth_type`        VARCHAR(50)             NOT NULL DEFAULT 'secretKey',
    `product_desc`     VARCHAR(200)            NOT NULL,
    `auto_active`      BOOLEAN                 NOT NULL DEFAULT true,
    `product_status`   TINYINT                 NOT NULL DEFAULT 1,
    `product_key`      VARCHAR(20)             NOT NULL,
    `product_secret`   VARCHAR(10)             NOT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;


CREATE TABLE IF NOT EXISTS `device`
(
    `uuid`             VARCHAR(31) PRIMARY KEY NOT NULL,
    `user_id`          VARCHAR(31)                      DEFAULT NULL,
    `tenant_id`        VARCHAR(31)                      DEFAULT NULL,
    `product_id`       VARCHAR(31)                      DEFAULT NULL,
    `product_name`     VARCHAR(100)            NOT NULL,
    `device_type`      TINYINT                          DEFAULT NULL,
    `regin_name`       VARCHAR(100)            NOT NULL,
    `device_name`      VARCHAR(100)            NOT NULL,
    `cn_name`          VARCHAR(200)            NOT NULL,
    `auth_type`        VARCHAR(50)             NOT NULL DEFAULT 'secretKey',
    `ip_addr`          VARCHAR(50),
    `fw_version`       VARCHAR(50),
    `active_time`      DATETIME,
    `last_online_time` DATETIME,
    `device_status`    TINYINT                 NOT NULL DEFAULT 0,
    `sdk_type`         VARCHAR(20),
    `sdk_version`      VARCHAR(10)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;


CREATE TABLE IF NOT EXISTS `device_sercet_info`
(
    `uuid`           VARCHAR(31) PRIMARY KEY NOT NULL,
    `device_name`    VARCHAR(100)            NOT NULL,
    `product_key`    VARCHAR(20)             NOT NULL,
    `product_secret` VARCHAR(20)             NOT NULL,
    `device_secret`  VARCHAR(20)             NOT NULL,
    `device_status`  TINYINT                 NOT NULL DEFAULT 0,
    `auto_active`    BOOLEAN                 NOT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;


CREATE TABLE IF NOT EXISTS `rule_chain`
(
    `uuid`               VARCHAR(31) PRIMARY KEY NOT NULL,
    `tenant_id`          VARCHAR(31)             NOT NULL,
    `user_id`            VARCHAR(31)             NOT NULL,
    `rule_chain_name`    VARCHAR(100)            NOT NULL,
    `first_rule_node_id` VARCHAR(31)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;



CREATE TABLE IF NOT EXISTS `rule_node`
(
    `uuid`          VARCHAR(31) PRIMARY KEY NOT NULL,
    `rule_chain_id` VARCHAR(31)             NOT NULL,
    `configuration` VARCHAR(1000),
    `node_type`     VARCHAR(255),
    `node_name`     VARCHAR(255)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;



CREATE TABLE IF NOT EXISTS `rule_node_relation`
(
    `from_id`             VARCHAR(31)  NOT NULL,
    `from_type`           VARCHAR(255) NOT NULL,
    `to_id`               VARCHAR(31)  NOT NULL,
    `to_type`             VARCHAR(255) NOT NULL,
    `relation_type_group` VARCHAR(255) NOT NULL,
    `relation_type`       VARCHAR(255) NOT NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;


