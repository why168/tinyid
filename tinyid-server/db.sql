CREATE TABLE `tiny_id_info`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `biz_type`    varchar(63) NOT NULL DEFAULT '' COMMENT '业务类型，唯一',
    `begin_id`    bigint      NOT NULL DEFAULT '0' COMMENT '开始id，仅记录初始值，无其他含义。初始化时begin_id和max_id应相同',
    `max_id`      bigint      NOT NULL DEFAULT '0' COMMENT '当前最大id',
    `step`        int                  DEFAULT '0' COMMENT '步长',
    `delta`       int         NOT NULL DEFAULT '1' COMMENT '每次id增量',
    `remainder`   int         NOT NULL DEFAULT '0' COMMENT '余数',
    `version`     bigint      NOT NULL DEFAULT '0' COMMENT '版本号',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_biz_type` (`biz_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb3 COMMENT ='id信息表';

CREATE TABLE `tiny_id_token`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `token`       varchar(255) NOT NULL DEFAULT '' COMMENT 'token',
    `biz_type`    varchar(63)  NOT NULL DEFAULT '' COMMENT '此token可访问的业务类型标识',
    `remark`      varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='token信息表';

INSERT INTO `tiny_id_info` (`id`, `biz_type`, `begin_id`, `max_id`, `step`, `delta`, `remainder`, `version`)
VALUES (1, 'test', 1, 1, 100000, 1, 0, 1);

INSERT INTO `tiny_id_info` (`id`, `biz_type`, `begin_id`, `max_id`, `step`, `delta`, `remainder`, `version`)
VALUES (2, 'test_odd', 1, 1, 100000, 2, 1, 3);


INSERT INTO `tiny_id_token` (`id`, `token`, `biz_type`, `remark`)
VALUES (1, '0f673adf80504e2eaa552f5d791b644c', 'test', '1');

INSERT INTO `tiny_id_token` (`id`, `token`, `biz_type`, `remark`)
VALUES (2, '0f673adf80504e2eaa552f5d791b644c', 'test_odd', '1');

