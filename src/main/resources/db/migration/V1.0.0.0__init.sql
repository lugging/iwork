create table ytx_user_info
(
    user_id       bigint auto_increment comment '主键，自增'
        primary key,
    phone_num     varchar(32)      not null comment '手机号',
    user_status   tinyint(1) default 0 null comment '用户状态0:正常 1：异常',
    country_code  varchar(32)      null comment '国家码',
    app_version   varchar(32)      null comment '客户端当前版本',
    complete_code varchar(128)     null comment 'APP完整校验码',
    device_type   varchar(21)      null comment '客户端类型 0：andorid 1:ios 2:exe',
    device_agent  varchar(128)     null comment '终端类型',
    app_id         varchar(64)      null,
    create_time   datetime         null comment '建立时间',
    update_time   datetime         null comment '更新时间'
);
