/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb3 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET SQL_SAFE_UPDATES = 0;
DROP DATABASE IF EXISTS `t101`;
CREATE DATABASE IF NOT EXISTS `t101` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `t101`;

-- ----------------------------
-- 班级类型表
-- ----------------------------
DROP TABLE IF EXISTS `banjileixing`;
CREATE TABLE IF NOT EXISTS `banjileixing` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `leixing` varchar(200) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb3 COMMENT='班级类型';

DELETE FROM `banjileixing`;
INSERT INTO `banjileixing` (`id`, `addtime`, `leixing`) VALUES
  (61, '2021-05-03 03:24:25', '计算机科学与技术'),
  (62, '2021-05-03 03:24:25', '软件工程'),
  (63, '2021-05-03 03:24:25', '人工智能'),
  (64, '2021-05-03 03:24:25', '大数据'),
  (65, '2021-05-03 03:24:25', '网络工程'),
  (66, '2021-05-03 03:24:25', '信息安全');

-- ----------------------------
-- 配置文件
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE IF NOT EXISTS `config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='配置文件';

DELETE FROM `config`;
INSERT INTO `config` (`id`, `name`, `value`) VALUES
  (1, 'picture1', 'http://localhost:8080/springbootrd362/upload/picture1.jpg'),
  (2, 'picture2', 'http://localhost:8080/springbootrd362/upload/picture2.jpg'),
  (3, 'picture3', 'http://localhost:8080/springbootrd362/upload/picture3.jpg'),
  (6, 'homepage', 'https://asoa-1305425069.cos.ap-shanghai.myqcloud.com/1669635627773202432.png');

-- ----------------------------
-- 教师表
-- ----------------------------
DROP TABLE IF EXISTS `jiaoshi`;
CREATE TABLE IF NOT EXISTS `jiaoshi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gonghao` varchar(200) NOT NULL COMMENT '工号',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `jiaoshixingming` varchar(200) NOT NULL COMMENT '教师姓名',
  `xingbie` varchar(200) DEFAULT NULL COMMENT '性别',
  `xueyuanmingcheng` varchar(200) DEFAULT NULL COMMENT '学院名称',
  `zhicheng` varchar(200) DEFAULT NULL COMMENT '职称',
  `shouji` varchar(200) DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `zhaopian` varchar(200) DEFAULT NULL COMMENT '照片',
  PRIMARY KEY (`id`),
  UNIQUE KEY `gonghao` (`gonghao`)
) ENGINE=InnoDB AUTO_INCREMENT=1620012387555 DEFAULT CHARSET=utf8mb3 COMMENT='教师';

DELETE FROM `jiaoshi`;
INSERT INTO `jiaoshi` (`id`, `addtime`, `gonghao`, `mima`, `jiaoshixingming`, `xingbie`, `xueyuanmingcheng`, `zhicheng`, `shouji`, `youxiang`, `zhaopian`) VALUES
  (21, '2021-05-03 03:24:25', '教师1', '123456', '张伟', '男', '计算机学院', '副教授', '13823888881', '773890001@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian1.jpg'),
  (22, '2021-05-03 03:24:25', '教师2', '123456', '李华', '女', '软件学院', '教授', '13823888882', '773890002@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian2.jpg'),
  (23, '2021-05-03 03:24:25', '教师3', '123456', '王强', '男', '信息学院', '讲师', '13823888883', '773890003@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian3.jpg'),
  (24, '2021-05-03 03:24:25', '教师4', '123456', '刘敏', '女', '计算机学院', '副教授', '13823888884', '773890004@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian4.jpg'),
  (25, '2021-05-03 03:24:25', '教师5', '123456', '陈静', '女', '软件学院', '助教', '13823888885', '773890005@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian5.jpg'),
  (26, '2021-05-03 03:24:25', '教师6', '123456', '赵磊', '男', '信息学院', '教授', '13823888886', '773890006@qq.com', 'http://localhost:8080/springbootrd362/upload/jiaoshi_zhaopian6.jpg');

-- ----------------------------
-- 竞赛信息表（扩展字段）
-- ----------------------------
DROP TABLE IF EXISTS `jingsaixinxi`;
CREATE TABLE IF NOT EXISTS `jingsaixinxi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaileixing` varchar(200) DEFAULT NULL COMMENT '竞赛类型',
  `jingsaididian` varchar(200) DEFAULT NULL COMMENT '竞赛地点',
  `jingsaiguize` longtext COMMENT '竞赛规则',
  `jingsaijiangli` longtext COMMENT '竞赛奖励',
  `jingsaishijian` datetime DEFAULT NULL COMMENT '竞赛时间',
  `moshi` varchar(200) DEFAULT NULL COMMENT '模式（付费/免费）',
  `fengmiantupian` varchar(200) DEFAULT NULL COMMENT '封面图片',
  `gonghao` varchar(200) DEFAULT NULL COMMENT '工号',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '教师姓名',
  -- 新增字段 --
  `jingsaijib` varchar(50) DEFAULT '校级' COMMENT '竞赛级别（校级/省级/国家级/国际级）',
  `baomingkaishi` datetime DEFAULT NULL COMMENT '报名开始时间',
  `baomingjiezhi` datetime DEFAULT NULL COMMENT '报名截止时间',
  `tijiaojiezhi` datetime DEFAULT NULL COMMENT '作品提交截止时间',
  `shoufeibiaozhun` varchar(200) DEFAULT '免费' COMMENT '收费标准',
  `cansaiyaoqiu` longtext COMMENT '参赛要求',
  `jiangxiangshezhi` longtext COMMENT '奖项设置',
  `zhuangtai` varchar(50) DEFAULT '报名中' COMMENT '竞赛状态（报名中/进行中/已结束）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1620012453487 DEFAULT CHARSET=utf8mb3 COMMENT='竞赛信息';

DELETE FROM `jingsaixinxi`;
INSERT INTO `jingsaixinxi` (`id`, `addtime`, `jingsaimingcheng`, `jingsaileixing`, `jingsaididian`, `jingsaiguize`, `jingsaijiangli`, `jingsaishijian`, `moshi`, `fengmiantupian`, `gonghao`, `jiaoshixingming`, `jingsaijib`, `baomingkaishi`, `baomingjiezhi`, `tijiaojiezhi`, `shoufeibiaozhun`, `cansaiyaoqiu`, `jiangxiangshezhi`, `zhuangtai`) VALUES
  (31, '2024-01-01 00:00:00', '第十六届全国大学生数学建模竞赛', '数学建模', '全国', '参赛队伍由3名大学生组成，在3天内完成建模和论文撰写。', '全国一等奖：奖金5000元+证书；全国二等奖：奖金3000元+证书；全国三等奖：证书', '2024-09-06 08:00:00', '免费', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian1.jpg', '教师1', '张伟', '国家级', '2024-07-01 00:00:00', '2024-08-31 23:59:59', '2024-09-05 23:59:59', '免费', '全国大学生（不含研究生）均可参加，每队3人', '一等奖、二等奖、三等奖及优秀奖', '报名中'),
  (32, '2024-01-01 00:00:00', '全国大学生电子设计竞赛', '电子设计', '全国', '围绕电子电路设计，参赛队伍在4天3夜内完成设计制作和调试。', '全国一等奖：证书+奖杯；全国二等奖：证书；省级奖项若干', '2024-08-01 08:00:00', '免费', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian2.jpg', '教师2', '李华', '国家级', '2024-05-01 00:00:00', '2024-07-15 23:59:59', '2024-07-31 23:59:59', '免费', '全国在校大学生（含专科、本科），每队2-3人', '全国一、二、三等奖，省级奖项', '进行中'),
  (33, '2024-01-01 00:00:00', '省级大学生程序设计竞赛', '程序设计', '省内高校', '个人或团队参赛，在线完成ACM类算法题目。', '一等奖：奖金2000元；二等奖：奖金1000元；三等奖：奖金500元', '2024-06-15 09:00:00', '免费', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian3.jpg', '教师3', '王强', '省级', '2024-04-01 00:00:00', '2024-05-31 23:59:59', '2024-06-10 23:59:59', '免费', '省内各高校在校本科生和专科生', '一、二、三等奖及优秀奖', '进行中'),
  (34, '2024-01-01 00:00:00', '校级创新创业大赛', '创新创业', '本校', '参赛项目需具有创新性，提交商业计划书并进行答辩。', '金奖：奖金3000元；银奖：奖金2000元；铜奖：奖金1000元', '2024-05-20 09:00:00', '免费', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian4.jpg', '教师4', '刘敏', '校级', '2024-03-01 00:00:00', '2024-04-30 23:59:59', '2024-05-15 23:59:59', '免费', '本校全体在校学生，每队2-5人', '金奖1名、银奖3名、铜奖5名', '已结束'),
  (35, '2024-01-01 00:00:00', '全国软件和信息技术专业人才大赛', '软件设计', '全国', '参赛者需提交软件作品，包含源代码和文档说明。', '全国特等奖：奖金10000元；全国一等奖：5000元；全国二等奖：3000元', '2024-10-15 09:00:00', '100元/队', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian5.jpg', '教师5', '陈静', '国家级', '2024-07-15 00:00:00', '2024-09-30 23:59:59', '2024-10-10 23:59:59', '100元/队', '全国在校大学生，个人或2-4人团队参赛', '特等奖、一、二、三等奖及优秀奖', '报名中'),
  (36, '2024-01-01 00:00:00', '校级数据挖掘竞赛', '数据分析', '本校', '围绕真实数据集，完成分析报告和建模预测。', '一等奖：奖金500元；二等奖：奖金300元；三等奖：奖金200元', '2024-11-10 09:00:00', '免费', 'http://localhost:8080/springbootrd362/upload/jingsaixinxi_fengmiantupian6.jpg', '教师6', '赵磊', '校级', '2024-09-01 00:00:00', '2024-10-31 23:59:59', '2024-11-05 23:59:59', '免费', '本校在校本科生，1-3人组队', '一等奖1名、二等奖2名、三等奖3名', '报名中');

-- ----------------------------
-- 竞赛报名表（扩展字段）
-- ----------------------------
DROP TABLE IF EXISTS `jingsaibaoming`;
CREATE TABLE IF NOT EXISTS `jingsaibaoming` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gonghao` varchar(200) DEFAULT NULL COMMENT '工号（指导教师）',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '教师姓名',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaileixing` varchar(200) DEFAULT NULL COMMENT '竞赛类型',
  `cansaileixing` varchar(200) DEFAULT NULL COMMENT '参赛类型（个人/团队）',
  `cansairenyuan` varchar(200) DEFAULT NULL COMMENT '参赛人员',
  `cansaizuopin` varchar(200) DEFAULT NULL COMMENT '参赛作品（附件路径）',
  `cansaixuanyan` longtext COMMENT '参赛宣言',
  `shenqingriqi` date DEFAULT NULL COMMENT '申请日期',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号（队长/个人）',
  `xueshengxingming` varchar(200) DEFAULT NULL COMMENT '学生姓名',
  `sfsh` varchar(200) DEFAULT '待审核' COMMENT '是否审核（待审核/已通过/已驳回）',
  `shhf` longtext COMMENT '审核回复',
  `ispay` varchar(200) DEFAULT '未支付' COMMENT '是否支付',
  -- 新增字段 --
  `xueyuanmingcheng` varchar(200) DEFAULT NULL COMMENT '学院名称',
  `nianji` varchar(50) DEFAULT NULL COMMENT '年级',
  `shouji` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `tuanduimingcheng` varchar(200) DEFAULT NULL COMMENT '团队名称',
  `tuanduichengyuan` longtext COMMENT '团队成员详情（JSON）',
  `jingsaixinxiid` bigint DEFAULT NULL COMMENT '关联竞赛信息ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1620012496393 DEFAULT CHARSET=utf8mb3 COMMENT='竞赛报名';

DELETE FROM `jingsaibaoming`;
INSERT INTO `jingsaibaoming` (`id`, `addtime`, `gonghao`, `jiaoshixingming`, `jingsaimingcheng`, `jingsaileixing`, `cansaileixing`, `cansairenyuan`, `cansaizuopin`, `cansaixuanyan`, `shenqingriqi`, `xuehao`, `xueshengxingming`, `sfsh`, `shhf`, `ispay`, `xueyuanmingcheng`, `nianji`, `shouji`, `tuanduimingcheng`, `tuanduichengyuan`, `jingsaixinxiid`) VALUES
  (41, '2024-01-10 00:00:00', '教师1', '张伟', '第十六届全国大学生数学建模竞赛', '数学建模', '团队', '学生姓名1,学生姓名2,学生姓名3', '', '我们团队热爱数学建模，期待在此次比赛中取得优异成绩！', '2024-07-05', '学生1', '李明', '已通过', '材料齐全，通过审核', '已支付', '计算机学院', '大三', '13823888881', '建模先锋队', '[{"xuehao":"学生1","xingming":"李明","xueyuan":"计算机学院"},{"xuehao":"学生2","xingming":"王芳","xueyuan":"计算机学院"},{"xuehao":"学生3","xingming":"张强","xueyuan":"软件学院"}]', 31),
  (42, '2024-01-10 00:00:00', '教师2', '李华', '全国大学生电子设计竞赛', '电子设计', '团队', '学生姓名4,学生姓名5', '', '我们热爱电子技术，期待展现实力！', '2024-05-20', '学生4', '刘洋', '已通过', '审核通过', '未支付', '信息学院', '大二', '13823888884', '电子创客队', '[{"xuehao":"学生4","xingming":"刘洋","xueyuan":"信息学院"},{"xuehao":"学生5","xingming":"陈晓","xueyuan":"信息学院"}]', 32),
  (43, '2024-01-10 00:00:00', '教师3', '王强', '省级大学生程序设计竞赛', '程序设计', '个人', '学生姓名6', '', '编程是我的热情所在！', '2024-04-15', '学生6', '赵磊', '已通过', '个人参赛，通过', '未支付', '软件学院', '大三', '13823888886', '', '', 33),
  (44, '2024-02-01 00:00:00', '教师4', '刘敏', '校级创新创业大赛', '创新创业', '团队', '学生姓名1,学生姓名2', '', '创新改变未来！', '2024-03-10', '学生1', '李明', '已驳回', '材料不完整，请补充商业计划书', '未支付', '计算机学院', '大三', '13823888881', '创新先锋', '[{"xuehao":"学生1","xingming":"李明","xueyuan":"计算机学院"},{"xuehao":"学生2","xingming":"王芳","xueyuan":"计算机学院"}]', 34),
  (45, '2024-07-20 00:00:00', '教师5', '陈静', '全国软件和信息技术专业人才大赛', '软件设计', '团队', '学生姓名4,学生姓名5,学生姓名6', '', '用代码诠释创意！', '2024-07-20', '学生4', '刘洋', '待审核', '', '未支付', '信息学院', '大二', '13823888884', '码力十足', '[{"xuehao":"学生4","xingming":"刘洋","xueyuan":"信息学院"},{"xuehao":"学生5","xingming":"陈晓","xueyuan":"信息学院"},{"xuehao":"学生6","xingming":"赵磊","xueyuan":"软件学院"}]', 35),
  (46, '2024-09-05 00:00:00', '教师6', '赵磊', '校级数据挖掘竞赛', '数据分析', '个人', '学生姓名6', '', '数据中蕴含无限可能！', '2024-09-05', '学生6', '赵磊', '待审核', '', '未支付', '软件学院', '大三', '13823888886', '', '', 36);

-- ----------------------------
-- 作品打分表（扩展字段）
-- ----------------------------
DROP TABLE IF EXISTS `zuopindafen`;
CREATE TABLE IF NOT EXISTS `zuopindafen` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `xueshengxingming` varchar(200) DEFAULT NULL COMMENT '学生姓名',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaileixing` varchar(200) DEFAULT NULL COMMENT '竞赛类型',
  `zuopinpingfen` int DEFAULT NULL COMMENT '作品评分（0-100）',
  `pingjianeirong` longtext COMMENT '评价内容',
  `pingjiashijian` date DEFAULT NULL COMMENT '评价时间',
  `gonghao` varchar(200) DEFAULT NULL COMMENT '工号',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '教师姓名',
  `chuangshen` varchar(20) DEFAULT '否' COMMENT '是否晋级（是/否）',
  `jingsaixinxiid` bigint DEFAULT NULL COMMENT '关联竞赛ID',
  `baomingid` bigint DEFAULT NULL COMMENT '关联报名ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1620012537728 DEFAULT CHARSET=utf8mb3 COMMENT='作品打分';

DELETE FROM `zuopindafen`;
INSERT INTO `zuopindafen` (`id`, `addtime`, `xuehao`, `xueshengxingming`, `jingsaimingcheng`, `jingsaileixing`, `zuopinpingfen`, `pingjianeirong`, `pingjiashijian`, `gonghao`, `jiaoshixingming`, `chuangshen`, `jingsaixinxiid`, `baomingid`) VALUES
  (51, '2024-09-10 00:00:00', '学生1', '李明', '第十六届全国大学生数学建模竞赛', '数学建模', 88, '建模思路清晰，论文撰写规范，结论合理，建议在数据验证方面加强。', '2024-09-10', '教师1', '张伟', '是', 31, 41),
  (52, '2024-08-05 00:00:00', '学生4', '刘洋', '全国大学生电子设计竞赛', '电子设计', 79, '电路设计合理，但调试部分有所欠缺，功能基本实现。', '2024-08-05', '教师2', '李华', '否', 32, 42),
  (53, '2024-06-20 00:00:00', '学生6', '赵磊', '省级大学生程序设计竞赛', '程序设计', 95, '算法高效，代码质量优秀，解题思路独特，表现出色！', '2024-06-20', '教师3', '王强', '是', 33, 43);

-- ----------------------------
-- 作品提交表（新增）
-- ----------------------------
DROP TABLE IF EXISTS `zuopintijiao`;
CREATE TABLE IF NOT EXISTS `zuopintijiao` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `xueshengxingming` varchar(200) DEFAULT NULL COMMENT '学生姓名',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaixinxiid` bigint DEFAULT NULL COMMENT '关联竞赛信息ID',
  `baomingid` bigint DEFAULT NULL COMMENT '关联报名ID',
  `zuopinmingcheng` varchar(500) DEFAULT NULL COMMENT '作品名称',
  `zuopinmiaoshu` longtext COMMENT '作品描述',
  `fujianwenjian` longtext COMMENT '附件文件（多个逗号分隔）',
  `tijiaoshijian` datetime DEFAULT NULL COMMENT '提交时间',
  `zhuangtai` varchar(50) DEFAULT '已提交' COMMENT '提交状态（已提交/已修改）',
  `banben` int DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb3 COMMENT='作品提交';

DELETE FROM `zuopintijiao`;
INSERT INTO `zuopintijiao` (`id`, `addtime`, `xuehao`, `xueshengxingming`, `jingsaimingcheng`, `jingsaixinxiid`, `baomingid`, `zuopinmingcheng`, `zuopinmiaoshu`, `fujianwenjian`, `tijiaoshijian`, `zhuangtai`, `banben`) VALUES
  (91, '2024-09-05 10:00:00', '学生1', '李明', '第十六届全国大学生数学建模竞赛', 31, 41, '基于优化算法的供应链管理模型研究', '本文针对复杂供应链系统，构建了多目标优化模型，并使用遗传算法求解...', 'http://localhost:8080/springbootrd362/upload/zuopin_1.pdf', '2024-09-05 09:30:00', '已提交', 2),
  (92, '2024-07-30 14:00:00', '学生4', '刘洋', '全国大学生电子设计竞赛', 32, 42, '基于STM32的智能温控系统设计', '本设计基于STM32微控制器，实现了温度的精确采集、显示和控制...', 'http://localhost:8080/springbootrd362/upload/zuopin_2.pdf', '2024-07-30 13:45:00', '已提交', 1),
  (93, '2024-06-10 09:00:00', '学生6', '赵磊', '省级大学生程序设计竞赛', 33, 43, 'ACM算法题解合集', '包含10道竞赛题目的完整解题思路和代码实现...', 'http://localhost:8080/springbootrd362/upload/zuopin_3.zip', '2024-06-10 08:55:00', '已提交', 1);

-- ----------------------------
-- 获奖管理表（新增）
-- ----------------------------
DROP TABLE IF EXISTS `huojiang`;
CREATE TABLE IF NOT EXISTS `huojiang` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jingsaixinxiid` bigint DEFAULT NULL COMMENT '关联竞赛信息ID',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaileixing` varchar(200) DEFAULT NULL COMMENT '竞赛类型',
  `jingsaijib` varchar(50) DEFAULT NULL COMMENT '竞赛级别',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '获奖学号（队长/个人）',
  `xueshengxingming` varchar(200) DEFAULT NULL COMMENT '获奖学生姓名',
  `xueyuanmingcheng` varchar(200) DEFAULT NULL COMMENT '学院名称',
  `tuanduimingcheng` varchar(200) DEFAULT NULL COMMENT '团队名称',
  `tuanduichengyuan` longtext COMMENT '团队成员',
  `jiangxiangmingcheng` varchar(200) DEFAULT NULL COMMENT '奖项名称',
  `jiangxiangdengji` varchar(100) DEFAULT NULL COMMENT '奖项等级（一等奖/二等奖/三等奖/优秀奖）',
  `gonghao` varchar(200) DEFAULT NULL COMMENT '指导教师工号',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '指导教师姓名',
  `huojiangriqi` date DEFAULT NULL COMMENT '获奖日期',
  `zhengshu` varchar(500) DEFAULT NULL COMMENT '证书图片路径',
  `beizhu` longtext COMMENT '备注',
  `sfgs` varchar(10) DEFAULT '否' COMMENT '是否公示（是/否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb3 COMMENT='获奖管理';

DELETE FROM `huojiang`;
INSERT INTO `huojiang` (`id`, `addtime`, `jingsaixinxiid`, `jingsaimingcheng`, `jingsaileixing`, `jingsaijib`, `xuehao`, `xueshengxingming`, `xueyuanmingcheng`, `tuanduimingcheng`, `tuanduichengyuan`, `jiangxiangmingcheng`, `jiangxiangdengji`, `gonghao`, `jiaoshixingming`, `huojiangriqi`, `zhengshu`, `beizhu`, `sfgs`) VALUES
  (191, '2024-09-15 00:00:00', 31, '第十六届全国大学生数学建模竞赛', '数学建模', '国家级', '学生1', '李明', '计算机学院', '建模先锋队', '李明,王芳,张强', '全国一等奖', '一等奖', '教师1', '张伟', '2024-09-15', '', '优秀团队，表现出色', '是'),
  (192, '2024-06-25 00:00:00', 33, '省级大学生程序设计竞赛', '程序设计', '省级', '学生6', '赵磊', '软件学院', '', '赵磊', '省级一等奖', '一等奖', '教师3', '王强', '2024-06-25', '', '个人参赛第一名', '是'),
  (193, '2024-08-10 00:00:00', 32, '全国大学生电子设计竞赛', '电子设计', '国家级', '学生4', '刘洋', '信息学院', '电子创客队', '刘洋,陈晓', '全国三等奖', '三等奖', '教师2', '李华', '2024-08-10', '', '', '是');

-- ----------------------------
-- 电子证书表（新增）
-- ----------------------------
DROP TABLE IF EXISTS `zhengshu`;
CREATE TABLE IF NOT EXISTS `zhengshu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huojiangid` bigint DEFAULT NULL COMMENT '关联获奖ID',
  `jingsaimingcheng` varchar(200) DEFAULT NULL COMMENT '竞赛名称',
  `jingsaijib` varchar(50) DEFAULT NULL COMMENT '竞赛级别',
  `xuehao` varchar(200) DEFAULT NULL COMMENT '学号',
  `xueshengxingming` varchar(200) DEFAULT NULL COMMENT '姓名',
  `xueyuanmingcheng` varchar(200) DEFAULT NULL COMMENT '学院',
  `jiangxiangmingcheng` varchar(200) DEFAULT NULL COMMENT '奖项名称',
  `jiangxiangdengji` varchar(100) DEFAULT NULL COMMENT '获奖等级',
  `jiaoshixingming` varchar(200) DEFAULT NULL COMMENT '指导教师',
  `huojiangriqi` date DEFAULT NULL COMMENT '获奖日期',
  `zhengshubianhao` varchar(100) DEFAULT NULL COMMENT '证书编号',
  `zhengshuluqing` varchar(500) DEFAULT NULL COMMENT '证书文件路径',
  `shenqingshijian` datetime DEFAULT NULL COMMENT '申请生成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8mb3 COMMENT='电子证书';

DELETE FROM `zhengshu`;
INSERT INTO `zhengshu` (`id`, `addtime`, `huojiangid`, `jingsaimingcheng`, `jingsaijib`, `xuehao`, `xueshengxingming`, `xueyuanmingcheng`, `jiangxiangmingcheng`, `jiangxiangdengji`, `jiaoshixingming`, `huojiangriqi`, `zhengshubianhao`, `zhengshuluqing`, `shenqingshijian`) VALUES
  (291, '2024-09-16 00:00:00', 191, '第十六届全国大学生数学建模竞赛', '国家级', '学生1', '李明', '计算机学院', '全国一等奖', '一等奖', '张伟', '2024-09-15', 'CERT-2024-001', '', '2024-09-16 10:00:00'),
  (292, '2024-06-26 00:00:00', 192, '省级大学生程序设计竞赛', '省级', '学生6', '赵磊', '软件学院', '省级一等奖', '一等奖', '王强', '2024-06-25', 'CERT-2024-002', '', '2024-06-26 09:00:00'),
  (293, '2024-08-11 00:00:00', 193, '全国大学生电子设计竞赛', '国家级', '学生4', '刘洋', '信息学院', '全国三等奖', '三等奖', '李华', '2024-08-10', 'CERT-2024-003', '', '2024-08-11 11:00:00');

-- ----------------------------
-- token表
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE IF NOT EXISTS `token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COMMENT='token表';

DELETE FROM `token`;
INSERT INTO `token` (`id`, `userid`, `username`, `tablename`, `role`, `token`, `addtime`, `expiratedtime`) VALUES
  (3, 1, 'admin', 'users', '管理员', 'h47e92njpfsz74s0mnwrj1vqtvnobvky', '2021-05-03 03:29:39', '2099-12-31 23:59:59');

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(100) DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

DELETE FROM `users`;
INSERT INTO `users` (`id`, `username`, `password`, `role`, `addtime`) VALUES
  (1, 'admin', '123456', '管理员', '2021-05-03 03:24:25');

-- ----------------------------
-- 学生表
-- ----------------------------
DROP TABLE IF EXISTS `xuesheng`;
CREATE TABLE IF NOT EXISTS `xuesheng` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `xuehao` varchar(200) NOT NULL COMMENT '学号',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `xueshengxingming` varchar(200) NOT NULL COMMENT '学生姓名',
  `xingbie` varchar(200) DEFAULT NULL COMMENT '性别',
  `xueyuanmingcheng` varchar(200) DEFAULT NULL COMMENT '学院名称',
  `banji` varchar(200) DEFAULT NULL COMMENT '班级',
  `shouji` varchar(200) DEFAULT NULL COMMENT '手机',
  `youxiang` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `zhaopian` varchar(200) DEFAULT NULL COMMENT '照片',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xuehao` (`xuehao`)
) ENGINE=InnoDB AUTO_INCREMENT=1620012392185 DEFAULT CHARSET=utf8mb3 COMMENT='学生';

DELETE FROM `xuesheng`;
INSERT INTO `xuesheng` (`id`, `addtime`, `xuehao`, `mima`, `xueshengxingming`, `xingbie`, `xueyuanmingcheng`, `banji`, `shouji`, `youxiang`, `zhaopian`) VALUES
  (11, '2021-05-03 03:24:25', '学生1', '123456', '李明', '男', '计算机学院', '计算机2021-1班', '13823888881', '773890001@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian1.jpg'),
  (12, '2021-05-03 03:24:25', '学生2', '123456', '王芳', '女', '计算机学院', '计算机2021-1班', '13823888882', '773890002@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian2.jpg'),
  (13, '2021-05-03 03:24:25', '学生3', '123456', '张强', '男', '软件学院', '软件2022-1班', '13823888883', '773890003@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian3.jpg'),
  (14, '2021-05-03 03:24:25', '学生4', '123456', '刘洋', '男', '信息学院', '信息2022-2班', '13823888884', '773890004@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian4.jpg'),
  (15, '2021-05-03 03:24:25', '学生5', '123456', '陈晓', '女', '信息学院', '信息2022-2班', '13823888885', '773890005@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian5.jpg'),
  (16, '2021-05-03 03:24:25', '学生6', '123456', '赵磊', '男', '软件学院', '软件2021-3班', '13823888886', '773890006@qq.com', 'http://localhost:8080/springbootrd362/upload/xuesheng_zhaopian6.jpg');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
