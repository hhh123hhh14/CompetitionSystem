-- =============================================================
-- 动态报名字段配置功能 - 数据库补丁
-- 执行方式：在 MySQL 中 USE t101; 后执行此文件
-- =============================================================
USE `t101`;

-- ----------------------------
-- 竞赛动态字段配置表
-- 每个竞赛可以配置多个自定义报名字段
-- ----------------------------
CREATE TABLE IF NOT EXISTS `jingsai_fields` (
  `id`              bigint        NOT NULL COMMENT '主键',
  `addtime`         timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jingsaixinxiid`  bigint        NOT NULL COMMENT '关联竞赛ID',
  `field_type`      varchar(50)   NOT NULL DEFAULT 'text'
                    COMMENT '字段类型：text/number/radio/checkbox/select/date/textarea',
  `field_label`     varchar(200)  NOT NULL COMMENT '字段标签（显示名称）',
  `field_key`       varchar(100)  NOT NULL COMMENT '字段KEY（英文，存储数据时使用）',
  `required`        varchar(10)   NOT NULL DEFAULT '否' COMMENT '是否必填：是/否',
  `default_value`   varchar(500)  DEFAULT NULL COMMENT '默认值',
  `field_options`   text          DEFAULT NULL COMMENT '选项列表（JSON数组），针对radio/checkbox/select',
  `placeholder`     varchar(300)  DEFAULT NULL COMMENT '占位提示文字',
  `sort_order`      int           NOT NULL DEFAULT 0 COMMENT '排序号（越小越靠前）',
  PRIMARY KEY (`id`),
  KEY `idx_jingsaixinxiid` (`jingsaixinxiid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛报名动态字段配置';

-- ----------------------------
-- 给 jingsaibaoming 表添加 ext_fields 列（存储动态字段数据的JSON）
-- ----------------------------
ALTER TABLE `jingsaibaoming`
  ADD COLUMN IF NOT EXISTS `ext_fields` LONGTEXT DEFAULT NULL
  COMMENT '动态扩展字段数据（JSON对象），按field_key存储';
