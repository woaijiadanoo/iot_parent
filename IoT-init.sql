CREATE TABLE IF NOT EXISTS `tenant`(
    `uuid`  VARCHAR(31) NOT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `phone` VARCHAR(50)  DEFAULT NULL,
    `name`  VARCHAR(255) DEFAULT NULL
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;


CREATE TABLE IF NOT EXISTS `user`(
    `uuid`  VARCHAR(31) PRIMARY KEY NOT NULL,
    `tenant_id`  VARCHAR(31) DEFAULT NULL,
    `user_name` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(50)  DEFAULT NULL,
    `email`  VARCHAR(255) DEFAULT NULL,
    `authority_role`  VARCHAR(50) NOT NULL
) ENGINE = INNODB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;