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

-- f20cc080-c89f-11ec-989e-8b76480d43cf
insert into `user` values
('1ecc89ff20cc080989e8b76480d43cf','1ecc89ff20cc080989e8b76480d43cf','ruyuan_00','13800000000','ruyuan_00@gmail.com','TENANT_ADMIN');

-- 940f0e60-c8a0-11ec-989e-8b76480d43cf
insert into `user` values
('1ecc8a0940f0e60989e8b76480d43cf','1ecc8a0940f0e60989e8b76480d43cf','ruyuan_01','13811111111','ruyuan_01@gmail.com','USER');