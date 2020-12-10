-- 用户表
create table users
(
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    enabled  int(1) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;;
-- 权限表
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;;

CREATE TABLE `persistent_logins`
(
    `username`  varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    `series`    varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    `token`     varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    `last_used` timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`series`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
create
unique index ix_auth_username on authorities (username,authority);

CREATE TABLE `executor`
(
    `EXECUTOR_ID`   int(10) NOT NULL AUTO_INCREMENT COMMENT '主键字段',
    `EXECUTOR_NAME` varchar(255) NOT NULL COMMENT '执行器名称',
    `DESC`          varchar(2048) NULL COMMENT '对该执行器的一些描述，选填字段',
    `REGISTER_TYPE` int(2) NOT NULL DEFAULT 0 COMMENT '执行器注册方式，0-自动注册，1-手动注册，默认为自动注册',
    PRIMARY KEY (`EXECUTOR_ID`)
);

create table ``
(

)