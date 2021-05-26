-- 用户表
create table users
(
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    enabled  int(1)       not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;;
-- 权限表
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;;

CREATE TABLE `persistent_logins`
(
    `username`  varchar(64) NOT NULL,
    `series`    varchar(64) NOT NULL,
    `token`     varchar(64) NOT NULL,
    `last_used` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`series`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
create
    unique index ix_auth_username on authorities (username, authority);

CREATE TABLE `EXECUTOR`
(
    `EXECUTOR_ID`   int(10)       NOT NULL AUTO_INCREMENT COMMENT '主键字段',
    `EXECUTOR_NAME` varchar(255)  NOT NULL COMMENT '执行器名称',
    `DESC`          varchar(2048) NULL COMMENT '对该执行器的一些描述，选填字段',
    `REGISTER_TYPE` int(2)        NOT NULL DEFAULT 0 COMMENT '执行器注册方式，0-自动注册，1-手动注册，默认为自动注册',
    PRIMARY KEY (`EXECUTOR_ID`),
    UNIQUE INDEX `UNI_EXECUTOR_NAME` (`EXECUTOR_NAME`) USING BTREE
);

create table `JOB`
(
    `JOB_ID`        int(10)       NOT NULL AUTO_INCREMENT COMMENT '主键字段',
    `EXECUTOR_ID`   int(10)       NOT NULL COMMENT '关联对应的执行器的id',
    `JOB_NAME`      varchar(256)   NOT NULL COMMENT '任务名称',
    `ROUTE_TYPE`    int(2)        NOT NULL COMMENT '路由策略 0-第一个，1-最后一个，2-轮询，3-随机，4-一致性hash，5-最不经常使用，6-最近最久未使用，7-故障转移，8-忙碌转移，9-分片广播',
    `CRON`          varchar(50)   NOT NULL comment 'cron表达式',
    `DESC`          varchar(1024) default NULL comment '任务描述',
    `TIMEOUT`       int(10)       default NULL COMMENT '任务超时时间，单位秒',
    `RETRY_TIME`    int(2)        default NULL COMMENT '任务失败重试次数',
    `OWNER`         varchar(1024) NOT NULL comment '任务负责人',
    `WARNING_EMAIL` varchar(1024) default NULL comment '任务报警邮箱',
    `JOB_PARAM`     varchar(1024) default NULL comment '任务默认参数',
    PRIMARY KEY (`JOB_ID`),
    INDEX `IDX_EXECUTOR_ID` (`EXECUTOR_ID`) USING BTREE
)