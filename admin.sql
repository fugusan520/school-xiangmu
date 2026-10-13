/**
核心数据表：
admin
t_cabinet
t_cabinet_box
t_express
t_express_in
t_express_out
t_express_stock
**/

-- 创建数据库
-- 2. 创建数据库（替换transportation_management为实际数据库名）
CREATE DATABASE IF NOT EXISTS transportation_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 3. 创建用户并设置密码（替换transportation和transportationp）
-- MySQL 8.0+ 创建用户并设置密码（推荐方式）
CREATE USER IF NOT EXISTS 'transportation'@'%' IDENTIFIED BY 'transportationp';

-- 4. 授予用户所有权限（包括远程访问）
GRANT ALL PRIVILEGES ON transportation_management.* TO 'transportation'@'%';

-- 5. 授予用户对所有数据库的所有权限（如果需要）
-- GRANT ALL PRIVILEGES ON *.* TO 'transportation'@'%' WITH GRANT OPTION;

-- 6. 刷新权限使更改生效
FLUSH PRIVILEGES;

-- 7. 查看用户权限
SHOW GRANTS FOR 'transportation'@'%';






-- transportation_management.admin definition

CREATE TABLE `admin` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.code definition

CREATE TABLE `code` (
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `exp` bigint(20) NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.commodity definition

CREATE TABLE `commodity` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `count` int(11) NOT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `price` double NOT NULL,
  `update_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.company definition

CREATE TABLE `company` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- transportation_management.distribution definition

CREATE TABLE `distribution` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `care` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `did` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `time` datetime(6) DEFAULT NULL,
  `urgent` bit(1) NOT NULL,
  `vid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.driver definition

CREATE TABLE `driver` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `driving` bit(1) NOT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `score` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.employee definition

CREATE TABLE `employee` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.express_feedback definition

CREATE TABLE `express_feedback` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `express_no` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '快递单号',
  `admin_id` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登记人的ID',
  `message` text COLLATE utf8mb4_unicode_ci COMMENT '评价内容',
  `rating` tinyint(3) unsigned DEFAULT '0' COMMENT '评价等级（0-5）',
  `contact_info` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评价人联系方式',
  `status` tinyint(3) unsigned DEFAULT '0' COMMENT '状态：0-待处理，1-已处理，2-已忽略',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='快递评价表';


-- transportation_management.inventory definition

CREATE TABLE `inventory` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `wid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.inventory_record definition

CREATE TABLE `inventory_record` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `wid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.login_log definition

CREATE TABLE `login_log` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.sale definition

CREATE TABLE `sale` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `commodity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `count` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `pay` bit(1) NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.system_log definition

CREATE TABLE `system_log` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `busincess_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'LTD',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.t_cabinet definition

CREATE TABLE `t_cabinet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cabinet_no` varchar(32) NOT NULL COMMENT '快递柜编号',
  `cabinet_name` varchar(50) DEFAULT NULL COMMENT '快递柜名称',
  `location` varchar(255) DEFAULT NULL COMMENT '放置位置',
  `total_boxes` int(11) DEFAULT NULL COMMENT '柜口总数',
  `status` int(11) DEFAULT '0' COMMENT '状态：0正常 1维护',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cabinet_no` (`cabinet_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递柜信息表';


-- transportation_management.t_cabinet_box definition

CREATE TABLE `t_cabinet_box` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cabinet_id` bigint(20) NOT NULL COMMENT '快递柜ID',
  `box_no` varchar(20) NOT NULL COMMENT '柜口编号',
  `box_type` int(11) DEFAULT NULL COMMENT '柜口类型：1小 2中 3大',
  `status` int(11) DEFAULT '0' COMMENT '状态：0空闲 1占用 2故障',
  `cabinet_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cabinet_box` (`cabinet_id`,`box_no`),
  UNIQUE KEY `uk_cabinet_box_2` (`cabinet_id`,`box_no`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递柜柜口表';


-- transportation_management.t_express definition

CREATE TABLE `t_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `express_no` varchar(64) NOT NULL COMMENT '快递单号',
  `company_code` varchar(32) DEFAULT NULL COMMENT '快递公司编码',
  `company_name` varchar(50) DEFAULT NULL COMMENT '快递公司名称',
  `sender_name` varchar(50) DEFAULT NULL COMMENT '寄件人姓名',
  `sender_phone` varchar(20) DEFAULT NULL COMMENT '寄件人电话',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收件人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收件人电话',
  `receiver_address` varchar(255) DEFAULT NULL COMMENT '收件地址',
  `status` int(11) DEFAULT '0' COMMENT '状态：0待入库 1已入库 2已取件 3已出库',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量(kg)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `biz_type` int(11) DEFAULT '1' COMMENT '业务类型：1收件 2寄件',
  `create_user_id` varchar(255) DEFAULT NULL COMMENT '创建用户ID（寄件人）',
  `courier_id` bigint(20) DEFAULT NULL COMMENT '快递员ID（寄件揽收）',
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_express_no` (`express_no`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递基础信息表';


-- transportation_management.t_express_in definition

CREATE TABLE `t_express_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `express_id` bigint(20) NOT NULL COMMENT '快递ID',
  `cabinet_id` bigint(20) DEFAULT NULL COMMENT '快递柜ID',
  `box_no` varchar(20) DEFAULT NULL COMMENT '柜口编号',
  `in_user_id` varchar(200) DEFAULT NULL COMMENT '入库操作人',
  `in_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `verify_code` varchar(10) DEFAULT NULL COMMENT '取件验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '验证码过期时间',
  `status` int(11) DEFAULT '0' COMMENT '状态：0未取件 1已取件',
  `in_type` int(11) DEFAULT '1' COMMENT '入库类型：1收件入库 2寄件投柜',
  `out_user_id` varchar(255) DEFAULT NULL,
  `cabinet_name` varchar(255) DEFAULT NULL,
  `express_no` varchar(255) DEFAULT NULL,
  `in_email` varchar(255) DEFAULT NULL,
  `out_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_express_id` (`express_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递入库表';


-- transportation_management.t_express_out definition

CREATE TABLE `t_express_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `express_id` bigint(20) NOT NULL COMMENT '快递ID',
  `out_type` int(11) DEFAULT NULL COMMENT '出库类型：1用户取件 2快递员发货',
  `out_user_id` varchar(255) DEFAULT NULL COMMENT '出库操作人',
  `out_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '出库备注',
  `out_role` int(11) DEFAULT NULL COMMENT '出库角色：1用户 2快递员',
  `express_no` varchar(255) DEFAULT NULL,
  `out_user_email` varchar(255) DEFAULT NULL,
  `verify_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_express_id` (`express_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递出库表';


-- transportation_management.t_express_stock definition

CREATE TABLE `t_express_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `express_id` bigint(20) NOT NULL COMMENT '快递ID',
  `cabinet_id` bigint(20) DEFAULT NULL COMMENT '快递柜ID',
  `box_no` varchar(20) DEFAULT NULL COMMENT '柜口编号',
  `status` int(11) DEFAULT '0' COMMENT '状态：0在库 1已取',
  `in_time` datetime DEFAULT NULL COMMENT '入库时间',
  `cabinet_name` varchar(255) DEFAULT NULL,
  `express_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_express_id` (`express_id`),
  KEY `idx_cabinet_box` (`cabinet_id`,`box_no`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递库存表';


-- transportation_management.vehicle definition

CREATE TABLE `vehicle` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `driving` bit(1) NOT NULL,
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


-- transportation_management.warehouse definition

CREATE TABLE `warehouse` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `principle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


INSERT INTO transportation_management.admin (id,create_at,email,password,roles) VALUES
	 ('2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-05 10:00:49','admin','admin','ROLE_SUPER_ADMIN'),
	 ('31c2ebc7-d4f7-494d-ae3e-c3f72365c248','2026-02-04 17:56:37','kdy1','111111','ROLE_COURIER'),
	 ('31c2ebc7-d4f7-494d-ae3e-c3f72365c249','2026-02-04 17:56:37','kdy2','111111','ROLE_COURIER'),
	 ('7fd4fa14-8f4f-4c4a-83f1-51e94b20f580','2026-02-04 17:48:49','taokn','111111','ROLE_USER'),
	 ('7fd4fa14-8f4f-4c4a-83f1-51e94b20f581','2026-02-04 17:48:49','linjh','222222','ROLE_USER'),
	 ('c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-07 21:46:30','kdy3','111111','ROLE_COURIER'),
	 ('cf0a8d78-6efc-4751-b1e0-0651ef4ffcd5','2026-02-07 21:48:46','chenzc','111111','ROLE_USER'),
	 ('eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-07 21:31:17','chenyx','111111','ROLE_USER');
INSERT INTO transportation_management.code (email,`exp`,value) VALUES
	 ('1402014577@qq.com',1669615938121,'276237'),
	 ('admin@163.com',1717297752863,'510427'),
	 ('admin@qq.com',1705376151055,'123456'),
	 ('cccccc@qq.com',1669725702342,'858227');
INSERT INTO transportation_management.commodity (id,count,create_at,description,name,price,update_at) VALUES
	 ('1dade9ff-ba00-4f78-bd27-55771dc65061',0,'2022-12-18 11:30:17','商品简介','抽纸',9.99,NULL),
	 ('52fdac99-f224-469e-8af8-ed49c166bb23',20,'2022-11-22 14:14:29','搭载麒麟980芯片','HUWAI MATE 30 Pro',4000.0,NULL),
	 ('5fcb392b-39aa-4381-b5ad-b1ccd8d5b74e',20081,'2022-12-02 20:36:04','Mac笔记本','Mac',7400.0,NULL),
	 ('64399a10-9b5a-4ab3-aa1a-ed46d331d18e',100,'2024-01-31 15:29:50','商品简介','笔记本',5000.0,NULL),
	 ('91eb39e3-d664-4aa8-8826-750d0a45bc67',50,'2022-12-14 17:45:04','键盘','青柚',34.0,NULL),
	 ('b3a8f5a2-2dac-4194-b806-687b7f08e82f',50,'2024-01-31 15:26:46','商品简介','电脑',4399.0,NULL),
	 ('eda4215d-82d8-4a08-a3f3-981b19274006',70,'2022-11-24 23:56:52','商品简介','Apple',40000.0,NULL);
INSERT INTO transportation_management.distribution (id,address,care,did,driver,`number`,phone,status,`time`,urgent,vid) VALUES
	 ('3036e2cf-28db-4d4c-b52d-5f590540b74b','sad','冰柜冷藏, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','sad',2,'2022-11-09 13:09:23',1,'ce33de59-9584-4161-a17e-9046399d14c6'),
	 ('77a6b6b9-682c-4aba-926b-6d7645895037','2343','注意易碎, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','234',2,'2022-12-08 12:58:27',1,'ce33de59-9584-4161-a17e-9046399d14c6'),
	 ('7c6c52fd-abbe-4505-a7c0-ea12f68ed6cb','asd345','冰柜冷藏, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','sd435345',2,'2022-12-07 13:01:39',1,'ce33de59-9584-4161-a17e-9046399d14c6'),
	 ('90188024-183b-4e8f-87d9-37670df04a69','北京','注意易碎, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','13232989876',0,'2025-01-02 20:00:00',0,'ce33de59-9584-4161-a17e-9046399d14c6'),
	 ('ae587e56-1d74-4edf-9707-ef7235eebd91','324234','冰柜冷藏, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','235352',0,'2022-12-06 12:59:04',1,'ce33de59-9584-4161-a17e-9046399d14c6'),
	 ('ff9bcb29-5e7b-4462-b789-a92576711ef7','asd','冰柜冷藏, ','ea3bba9b-cda6-438d-b196-7c81e97b5040','王五','京A0000','sd',0,'2022-12-07 13:01:39',1,'ce33de59-9584-4161-a17e-9046399d14c6');
INSERT INTO transportation_management.driver (id,address,create_at,driving,gender,id_card,license,name,phone,score,update_at) VALUES
	 ('a4a88363-a438-4847-a4bf-b25e6fa5ae6e','北京','2024-01-31 16:08:10',0,'男性','423423424','xxx','李四','13793242563','12',NULL),
	 ('ea3bba9b-cda6-438d-b196-7c81e97b5040','','2022-11-27 12:26:46',0,'男性','4412242003423345534','34','王五','231325345345','12',NULL);
INSERT INTO transportation_management.employee (id,address,create_at,department,gender,id_card,name,phone,update_at) VALUES
	 ('18d600a1-8184-43bc-b46d-11c66ff2561d','北京','2024-01-31 15:30:19','A号仓库','男性','434242341','张师傅','1373242363',NULL),
	 ('8ba9bf53-7e1d-43ff-ab71-b9d6af299c26','广州','2022-12-18 11:38:16','A号仓库','男性','441253546456234','王师傅','13729532464',NULL);
INSERT INTO transportation_management.express_feedback (express_no,admin_id,message,rating,contact_info,status,created_at,name) VALUES
	 ('YT219480127408','7fd4fa14-8f4f-4c4a-83f1-51e94b20f580','相当过硬！！！！！！！！',5,NULL,1,NULL,'taokn');
INSERT INTO transportation_management.inventory (id,cid,count,location,name,wid) VALUES
	 ('0ff7a831-0824-428d-b140-ab41de52d5a4','eda4215d-82d8-4a08-a3f3-981b19274006',70,NULL,'Apple','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('3c6f0c42-25e8-4ccb-b30a-2622d9740c87','52fdac99-f224-469e-8af8-ed49c166bb23',20,NULL,'HUWAI MATE 30 Pro','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('3fb4d8b2-673d-4fa1-8106-14b9cfa41f95','91eb39e3-d664-4aa8-8826-750d0a45bc67',50,NULL,'青柚','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('8fe5231d-d0a1-4a87-8973-e91760957294','64399a10-9b5a-4ab3-aa1a-ed46d331d18e',100,NULL,'笔记本','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('ca7f8e97-1edf-4fc8-b9a1-783a7d053131','5fcb392b-39aa-4381-b5ad-b1ccd8d5b74e',20081,NULL,'Mac','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('d9afc86f-a782-4d00-9300-78484f72c117','2283b0a2-5e0b-4c1e-b651-d2e3b51b87ee',80,NULL,'手机支架','9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('e8e63bfa-be44-4cb8-abe3-0b6baf3159fa','b3a8f5a2-2dac-4194-b806-687b7f08e82f',50,NULL,'鼠标','9f2a2784-e182-4fdf-85e8-c3bde6d539d2');
INSERT INTO transportation_management.inventory_record (id,cid,count,create_at,description,name,`type`,wid) VALUES
	 ('0327e497-6349-4a33-816e-38f680bd48f4','b3a8f5a2-2dac-4194-b806-687b7f08e82f',100,'2023-01-11 13:36:27','xxx','鼠标',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('2c141734-d0d0-4f4e-bf66-ae60667f0709','eda4215d-82d8-4a08-a3f3-981b19274006',20,'2022-11-25 00:01:09','ss','Apple',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('35b32a03-0fb4-4b1a-9a6a-689eea28c031','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,'2026-02-08T22:34:39.029730100','您的取件快递：YD7777777777777777，代取件，由kdy3投放至快递柜：快递柜2，地理位置：北门东侧，柜口：BOX02。取件码为：171131，请尽快去取。',NULL,1,NULL),
	 ('4197fbfb-78a5-4cae-b825-955099da1943','52fdac99-f224-469e-8af8-ed49c166bb23',20,'2022-11-22 14:16:57','','HUWAI MATE 30 Pro',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('51f8cee5-a238-41dc-b031-40b05cd83abf','2283b0a2-5e0b-4c1e-b651-d2e3b51b87ee',100,'2023-01-11 11:06:12','xxxxx','手机支架',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('5b3759ef-4b0f-43e4-b6b2-c7f797bb0b88','64399a10-9b5a-4ab3-aa1a-ed46d331d18e',100,'2024-01-31 15:30:47','Mac笔记本入库100台','笔记本',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('6ff4d4dc-326c-4fd3-a782-06b799f9931a','5fcb392b-39aa-4381-b5ad-b1ccd8d5b74e',21312,'2022-12-02 20:36:52','213','Mac',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('717071e3-9ea1-4005-a0d1-562ef943b9bb','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,'2026-02-08T22:32:24.257545700','您的寄件快递：YT1770560532375，状态更新啦，由kdy3出库。',NULL,1,NULL),
	 ('7907b2a0-cc76-433b-8182-9d27fd8442dd','c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,'2026-02-08T22:32:24.257545700','您的寄件快递：YT1770560532375，状态更新啦，由kdy3出库。',NULL,1,NULL),
	 ('8ec4e062-766e-4a7f-9ab0-91af644f7139','91eb39e3-d664-4aa8-8826-750d0a45bc67',50,'2024-01-31 14:58:49','','青柚',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2');
INSERT INTO transportation_management.inventory_record (id,cid,count,create_at,description,name,`type`,wid) VALUES
	 ('b1bdf63a-3c77-45b6-8841-8d149986f709','5fcb392b-39aa-4381-b5ad-b1ccd8d5b74e',1231,'2022-12-02 20:37:18','123','Mac',-1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('b49e8ba9-30c8-4794-ab1f-c807c5dbc8c2','2283b0a2-5e0b-4c1e-b651-d2e3b51b87ee',20,'2023-01-11 11:08:25','xxxx','手机支架',-1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('c319b975-fd04-444f-a2c8-b67f64609aa9','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,'2026-02-08T22:37:48.287375400','您的取件快递：YD7777777777777777，状态更新啦，由chenyx出库。',NULL,1,NULL),
	 ('c4f2f4bd-d992-44f7-848c-0da0bb3dd183','c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,'2026-02-08T22:22:47.989535200','您的寄件快递：YT1770560532375，代取件，由chenyx投放至快递柜：快递柜2，地理位置：北门东侧，柜口：BOX10。取件码为：592454，请尽快去取。',NULL,1,NULL),
	 ('e99dd646-bbd0-42d9-a315-72d081af8544','b3a8f5a2-2dac-4194-b806-687b7f08e82f',50,'2023-01-11 13:39:15','50','鼠标',-1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2'),
	 ('f46402bc-9911-4f13-90a1-a4d5ec6e66d7','c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,'2026-02-08T22:37:48.287375400','您的取件快递：YD7777777777777777，状态更新啦，由chenyx出库。',NULL,1,NULL),
	 ('fa51e39c-f210-4bcf-928f-e1036c6bdffb','eda4215d-82d8-4a08-a3f3-981b19274006',50,'2023-01-11 09:27:00','入库苹果手机','Apple',1,'9f2a2784-e182-4fdf-85e8-c3bde6d539d2');
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('05fe5bc8-b9fa-434c-b3ff-343cd165de8b','Chrome','2025-06-02 22:34:15.605000','123@qq.com','127.0.0.1',1),
	 ('0c221338-6e1d-4306-9030-2716c125c5c0','Chrome','2025-06-02 22:56:11.486000','527085608@qq.com','127.0.0.1',1),
	 ('0fc41c49-c310-43a3-9b43-c6dfa0ce2150','Chrome','2024-01-31 15:25:40.086000','full_stack_white@163.com','127.0.0.1',0),
	 ('1081de76-e56f-4b07-a79a-16bd2343e07c','Chrome','2025-06-02 22:44:22.165000','527085608@qq.com','127.0.0.1',1),
	 ('121ae1ef-be5d-443a-b1de-6578302d9934','Chrome','2022-12-06 15:05:58.830000','admin@qq.com','127.0.0.1',1),
	 ('13da3736-d1de-4d53-978f-8b8dea683d4d','Chrome','2026-02-04 17:49:16.663000','111@111.com','127.0.0.1',1),
	 ('14645ea4-3136-4c6a-8a61-d64c6257fd6f','Chrome','2025-06-02 22:37:07.218000','527085608@qq.com','127.0.0.1',1),
	 ('165f3be2-e509-422b-823a-4046deb5674e','Chrome','2025-06-02 23:08:57.257000','abc@qq.com','127.0.0.1',1),
	 ('172195ea-48da-492c-93f2-ede5ab83046f','Chrome','2024-06-02 10:43:55.004000','admin@163.com','127.0.0.1',1),
	 ('1af75d04-fca0-49a1-89d7-95aa9212a24d','Chrome','2022-12-17 23:15:17.620000','admin@qq.com','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('1cd90c0f-8b99-40b9-9778-e667c30d9bbf','Chrome','2022-12-18 11:28:31.718000','admin@qq.com','127.0.0.1',1),
	 ('1dbcf23c-dbd0-483a-a6b1-63a12a206711','Chrome','2025-06-02 22:18:17.374000','527085608@qq.com','127.0.0.1',1),
	 ('1e40c9c1-d2c1-481e-a451-a317e0df00dc','Chrome','2024-06-02 11:17:50.161000','527085608@qq.com','127.0.0.1',1),
	 ('1f0ad7fc-fe9f-4998-b1fb-8ef6bc98c208','Chrome','2025-06-02 22:55:35.642000','123@qq.com','127.0.0.1',1),
	 ('2071027e-70ef-4e35-8561-1465e8060ba2','Chrome','2026-02-06 20:50:11.705000','admin','127.0.0.1',1),
	 ('224df8bb-1620-48c7-a598-fd742d427cee','Chrome','2025-06-02 22:51:39.043000','527085608@qq.com','127.0.0.1',1),
	 ('24056e99-c20d-4426-b58e-37f8fbb1dfc8','Chrome','2026-02-09 15:53:47.819000','taokn','127.0.0.1',1),
	 ('2ca87389-a97a-446e-bf50-6778f461331b','Chrome','2024-01-31 16:06:49.825000','admin@163.com','127.0.0.1',1),
	 ('36b25d1b-ba49-4673-abd0-16a136930305','Chrome','2022-12-14 19:56:04.265000','admin@qq.com','127.0.0.1',1),
	 ('3f9b2ecf-fac0-4f52-aa7f-0a202f09a841','Chrome','2022-12-19 13:10:24.253000','admin@qq.com','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('40b51d68-749e-4859-b72f-45fc635f9d0a','Chrome','2022-12-13 12:00:23.181000','admin@qq.com','127.0.0.1',1),
	 ('43a1701b-4232-40ec-a767-cbd21fdb9b19','Chrome','2026-02-06 14:38:41.575000','admin','127.0.0.1',1),
	 ('43a8927c-9102-4b90-bf14-4484b5ca6eff','Chrome','2026-02-09 15:52:54.919000','chenyx','127.0.0.1',1),
	 ('45fc842e-a6fa-4591-986b-819b777c848a','Chrome','2025-06-02 22:20:49.371000','527085608@qq.com','127.0.0.1',1),
	 ('472c7a9b-0f05-48ab-995f-353bb17cb559','Chrome','2026-02-05 13:04:47.902000','admin','127.0.0.1',1),
	 ('488286c1-3690-4248-9c26-886483c4787b','Chrome','2026-02-06 19:40:47.750000','admin','127.0.0.1',1),
	 ('4c3fe589-07c2-4003-b463-dfd5f96d6722','Chrome','2023-02-03 10:08:02.093000','admin@qq.com','127.0.0.1',1),
	 ('4e16ef2c-1ae6-4207-952d-a45170fa3c61','Chrome','2025-06-02 22:58:31.995000','123@qq.com','127.0.0.1',1),
	 ('54f781c6-d85e-48dc-927f-650a291e5e09','Chrome','2025-06-02 22:48:17.947000','527085608@qq.com','127.0.0.1',1),
	 ('57736c53-74fc-49ba-9d50-71727d0d2e34','Chrome','2026-02-09 18:19:48.384000','chenzc','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('5a4a776e-a898-42f2-9c19-5875719e05e7','Chrome','2025-06-02 22:51:08.650000','123@qq.com','127.0.0.1',1),
	 ('5eaba42f-d29d-44e8-9019-729f7139110b','Chrome','2024-01-31 15:29:35.237000','full_stack_white@163.com','127.0.0.1',1),
	 ('67be2645-7a02-4f08-9861-6269f876e1ec','Chrome','2024-06-02 10:43:34.902000','admin@163.com','127.0.0.1',0),
	 ('68fcf025-02c5-47a7-83b6-cf984591affb','Chrome','2024-01-31 15:14:46.214000','cxs2014501@163.com','127.0.0.1',1),
	 ('6cbe1050-6f2b-4b70-b761-e7997e01a9dc','Chrome','2023-01-11 13:34:48.773000','admin@qq.com','127.0.0.1',1),
	 ('6f2950b4-8335-4bcb-87d7-165c875a60bd','Chrome','2025-06-02 22:36:32.377000','123@qq.com','127.0.0.1',1),
	 ('726c5f53-b908-4363-a1ab-3c75d08c68b7','Chrome','2026-02-05 13:04:43.462000','admin','127.0.0.1',0),
	 ('73553679-d813-4309-aced-463853bc0053','Chrome','2026-02-07 21:21:28.368000','admin','127.0.0.1',1),
	 ('774a8a9d-6742-4cd8-becc-05a5fdd4603c','Chrome','2024-01-31 15:41:45.879000','admin@163.com','127.0.0.1',1),
	 ('7a5c989b-3c7c-4361-8151-60c2f0f410da','Chrome','2025-06-02 22:33:43.956000','lop@qq.com','127.0.0.1',0);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('7dfccafe-b5e7-42f6-9d1b-e2e56f06ba67','Chrome','2024-01-31 14:58:19.968000','admin@163.com','127.0.0.1',1),
	 ('82d741b7-b584-483a-bc2e-cd65cb863b16','Chrome','2026-02-07 11:55:13.377000','admin','127.0.0.1',1),
	 ('83cbb7ae-555c-41aa-bb4a-e51f632a6803','Chrome','2026-02-07 21:20:19.700000','admin','127.0.0.1',1),
	 ('85301f24-d3e7-4349-8d2b-27c3ac3ee0d2','Chrome','2022-12-13 14:26:11.472000','abc@qq.com','127.0.0.1',1),
	 ('879f7917-8182-4b98-94d3-081a9497e89b','Chrome','2022-12-18 11:27:24.102000','admin@qq.com','127.0.0.1',1),
	 ('8915ba3f-74dc-434b-af25-cf07dac74ded','Chrome','2022-12-12 23:37:24.703000','admin@qq.com','127.0.0.1',0),
	 ('899f8c58-0d5b-46e0-a784-1c3f448719e7','Chrome','2022-12-17 23:47:52.145000','admin@qq.com','127.0.0.1',1),
	 ('8d46124b-548e-4f8c-8719-56cf3226c55b','Chrome','2022-12-18 17:09:53.910000','admin@qq.com','127.0.0.1',1),
	 ('8d8d8bab-0905-4bb3-9bc9-69ea2f0987ac','Chrome','2026-02-07 21:31:58.038000','kdy3','127.0.0.1',1),
	 ('8dbd4424-6939-4a22-b4c7-fcebce4975bf','Chrome','2025-06-02 22:53:38.798000','123@qq.com','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('917934f4-9f8e-48aa-b59e-d174248ed7b6','Chrome','2023-02-16 10:30:12.742000','admin@qq.com','127.0.0.1',1),
	 ('93467357-7699-42f2-957b-a0a1a4cdc13d','Chrome','2022-12-06 15:34:35.701000','adc@qq.com','127.0.0.1',1),
	 ('93b213c5-e2ec-4558-af3e-12b2bc8f5217','Chrome','2022-12-14 17:51:18.412000','admin@qq.com','127.0.0.1',1),
	 ('947b62e7-aeb1-46c3-9b0b-8e257cc72aac','Chrome','2023-01-11 10:15:23.854000','admin@qq.com','127.0.0.1',1),
	 ('9482f3dd-7e8a-444b-ae95-596076056592','Chrome','2022-12-12 23:37:31.175000','admin@qq.com','127.0.0.1',1),
	 ('97db4f45-e842-4822-9410-54fb04bfb042','Chrome','2022-12-17 11:47:49.852000','admin@qq.com','127.0.0.1',1),
	 ('9961cc66-63ad-47c6-9c1e-c81cb08ed949','Chrome','2023-02-15 20:57:44.695000','admin@qq.com','127.0.0.1',1),
	 ('9b143487-2852-48ff-9cf4-555847c40a7c','Chrome','2025-06-02 22:20:26.948000','lop@qq.com','127.0.0.1',0),
	 ('9d827736-01fd-4d3e-b971-4554944c9356','Chrome','2022-12-17 23:06:03.771000','admin@qq.com','127.0.0.1',1),
	 ('a149603d-ac88-4b85-a860-188036776615','Chrome','2023-01-11 13:43:06.172000','123@qq.com','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('a46d45d9-fac1-4cd5-90fa-d556f6fc1af4','Chrome','2026-02-04 15:17:29.889000','123@qq.com','127.0.0.1',1),
	 ('a51b2b6d-bb35-4aae-9999-0b6cf446dbd8','Chrome','2026-02-04 17:57:04.835000','admin','127.0.0.1',1),
	 ('a82f5725-da5d-49b3-b7b5-90b544ae1296','Chrome','2023-01-11 09:22:38.737000','admin@qq.com','127.0.0.1',1),
	 ('a9d0bca3-506a-45e6-8e2b-aab01cc90414','Chrome','2026-02-07 21:48:56.920000','admin','127.0.0.1',1),
	 ('b30b9c6f-251b-49a1-9835-c8536f5c23bb','Chrome','2022-12-12 23:36:42.313000','admin@qq.com','127.0.0.1',1),
	 ('b37dbe83-b99a-4400-834a-660f065b0a89','Chrome','2025-06-02 22:35:31.341000','527085608@qq.com','127.0.0.1',1),
	 ('b8d19318-5888-4a34-b8c0-f3052afa8114','Chrome','2026-02-07 21:31:38.600000','chenyx','127.0.0.1',1),
	 ('b965a1cb-a354-41db-8099-b217505c4dc4','Chrome','2022-12-06 15:10:23.878000','admin@qq.com','127.0.0.1',1),
	 ('bc1d25a6-db1b-41cc-aae6-66bf1526f1e0','Chrome','2024-06-02 10:56:46.342000','527085608@qq.com','127.0.0.1',1),
	 ('c0a719d8-bcea-4f74-b507-f2f9c408125b','Chrome','2024-01-31 15:25:13.590000','full_stack_white@163.com','127.0.0.1',0);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('c1f7f2e3-6355-4c91-91f2-58cacae7e463','Chrome','2023-01-11 09:10:50.767000','admin@qq.com','127.0.0.1',1),
	 ('c236d3bb-8348-4283-b204-58187fb6c9b8','Chrome','2026-02-07 21:17:11.421000','taokn','127.0.0.1',1),
	 ('c779c5a9-647d-4fc3-b117-66f29fcd11c6','Chrome','2024-01-31 15:25:03.138000','full_stack_white@163.com','127.0.0.1',1),
	 ('c7e666b4-bdac-45e8-8731-05f3274f7787','Chrome','2022-12-14 17:37:48.504000','admin@qq.com','127.0.0.1',1),
	 ('ca38e16e-ba6f-45e7-8a46-fb78abbbf9cc','Chrome','2023-02-03 09:43:42.628000','admin@qq.com','127.0.0.1',1),
	 ('cb06ed83-cc81-4865-8610-ea481f0c8f1b','Chrome','2026-02-08 20:05:12.045000','chenyx','127.0.0.1',1),
	 ('cbf4e04f-2d40-4384-87dd-7767ae103c6b','Chrome','2022-12-06 15:09:47.524000','admin@qq.com','127.0.0.1',1),
	 ('cc34263b-7082-4e24-805f-947d289c3c61','Chrome','2023-02-20 19:50:49.392000','admin@qq.com','127.0.0.1',1),
	 ('cff0950c-9ce1-4f6e-b37f-3e36faa0abb8','Chrome','2026-02-07 15:40:37.792000','taokn','127.0.0.1',1),
	 ('d0c81f25-ed0d-46bd-835b-44d0ce95e944','Chrome','2026-02-09 17:40:04.354000','admin','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('d55f21f4-01e6-45a5-abb9-a9d61de1f4da','Chrome','2023-02-20 19:50:41.017000','admin@qq.com','127.0.0.1',0),
	 ('d7971d2d-01f1-42b6-a617-d88e3af22a82','Chrome','2024-06-02 10:43:31.371000','admin@163.com','127.0.0.1',0),
	 ('d99e1ab9-c3b5-446f-a7e5-c9f94b9a10e9','Chrome','2026-02-07 11:09:26.984000','admin','127.0.0.1',1),
	 ('dc6237c6-c852-42e4-857a-8d711e5a0d10','Chrome','2024-01-31 16:23:25.940000','admin@163.com','127.0.0.1',1),
	 ('e01babb2-d9b8-4b6a-8eab-800059c9e94f','Chrome','2026-02-08 20:05:03.376000','admin','127.0.0.1',1),
	 ('e1a5b794-2f16-4bf3-8db1-924755326af0','Chrome','2022-12-17 23:33:24.419000','admin@qq.com','127.0.0.1',1),
	 ('e2bf13bc-76d4-47c9-84be-65be0c96fd18','Chrome','2024-01-31 15:13:58.115000','cxs2014501@163.com','127.0.0.1',0),
	 ('e5252049-8cec-4a8b-b97b-b217d0d5c0b4','Chrome','2024-01-16 11:20:00.975000','admin@qq.com','127.0.0.1',1),
	 ('e6678543-7f24-4c3f-ba66-96bf97f83243','Chrome','2022-12-06 15:31:35.868000','adc@qq.com','127.0.0.1',1),
	 ('e8390d6a-116c-4691-a46d-5b57fb97990c','Chrome','2026-02-08 20:05:21.843000','kdy3','127.0.0.1',1);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('e9483a37-3f80-44f4-8e7f-867c6a72aa10','Chrome','2024-01-16 11:17:58.447000','admin@qq.com','127.0.0.1',1),
	 ('ea676457-0dde-4e7b-8fec-e01837c12333','Chrome','2023-01-11 09:20:22.825000','admin@qq.com','127.0.0.1',1),
	 ('eaa7939a-833a-487e-81c9-10ad128f1d93','Chrome','2022-12-02 20:27:24.135000','admin@qq.com','127.0.0.1',1),
	 ('eb531c48-ea7b-4ef3-9517-1224c26f62e8','Chrome','2022-12-18 11:37:31.447000','abc@qq.com','127.0.0.1',1),
	 ('ecf572c3-ea2a-403c-af88-dbb68c46c7ac','Chrome','2025-06-02 22:47:36.314000','527085608@qq.com','127.0.0.1',1),
	 ('eeaa06fb-38d7-404e-86f5-d34a727ffb9a','Chrome','2025-06-02 22:54:02.571000','527085608@qq.com','127.0.0.1',1),
	 ('f09d0305-ba64-43a3-8aee-e3bdcec2f7c9','Chrome','2025-06-02 22:19:20.999000','527085608@qq.com','127.0.0.1',1),
	 ('f0b74467-4143-4c85-b536-6ea9ec5e1996','Chrome','2024-01-31 15:25:46.182000','full_stack_white@163.com','127.0.0.1',1),
	 ('f32bc9de-3d76-4db9-8d71-96d6ef145dca','Chrome','2022-12-12 13:54:11.044000','admin@qq.com','127.0.0.1',1),
	 ('f3bbce9d-13eb-47c6-8038-c6862c477642','Chrome','2024-06-02 10:43:27.279000','admin@163.com','127.0.0.1',0);
INSERT INTO transportation_management.login_log (id,browser,`date`,email,ip,status) VALUES
	 ('f6984802-1a3a-44fb-b09f-f5ae31802c0d','Chrome','2024-06-02 10:50:48.953000','cccccc@qq.com','127.0.0.1',1),
	 ('f719c3e7-1547-43ee-8416-a9939c504f6f','Chrome','2022-12-13 14:25:14.960000','jack@qq.com','127.0.0.1',0),
	 ('f7366eaf-7cb4-4a90-a94a-09c1fbabf61d','Chrome','2024-06-02 11:29:01.736000','527085608@qq.com','127.0.0.1',1),
	 ('f8384696-b37a-4f1b-9456-1f224427cf25','Chrome','2022-12-18 10:54:04.371000','admin@qq.com','127.0.0.1',1),
	 ('f97fd7af-5a59-42ad-bbfb-b5fd065c0c1f','Chrome','2022-12-18 11:28:02.712000','abc@qq.com','127.0.0.1',1),
	 ('fba4b000-121c-4099-af4c-d5b9022e351d','Chrome','2026-02-07 21:30:38.679000','kdy1','127.0.0.1',1),
	 ('fc5588a9-287c-41e7-9adc-36c925d74418','Chrome','2022-12-12 23:37:20.666000','admin@qq.com','127.0.0.1',0),
	 ('fe4e4ea1-41af-406e-b246-2e1e48eb045d','Chrome','2023-01-11 11:04:37.010000','admin@qq.com','127.0.0.1',1),
	 ('ffb1b09f-b259-42ad-9781-c0e0e95c2048','Chrome','2024-01-31 15:29:08.838000','full_stack_white@163.com','127.0.0.1',1);
INSERT INTO transportation_management.sale (id,commodity,company,count,create_at,description,`number`,pay,phone,price) VALUES
	 ('00f4bac9-93d9-44d0-a7df-f0d0cf8655c2','笔记本','xxx公司','50','2024-01-31 15:32:08','员工工作电脑','123456',1,'13278987674',250000.0),
	 ('0da2a4b7-208d-4c1d-bdd2-8966590f46c5','HUWAI MATE 30 Pro','2','500','2022-12-13 14:23:44','ccc','2342',1,'232423',2000000.0),
	 ('1307750a-78eb-4b07-8fb7-5b75bb3c6dc9','手机支架','中兴','50','2023-01-11 11:07:12','xxx','234234',1,'213123',499.5),
	 ('38214342-a785-4929-9208-94762c16a7d0','Apple','小米','20','2023-01-11 10:15:35','xxx','428354352',1,'13789253421',800000.0),
	 ('b97a82bf-6aa8-47a7-a3a6-03e0df570038','鼠标','亚马逊','50','2023-01-11 13:37:50','2341234','342342',1,'214234',499.5),
	 ('bbcb2f8d-9ab4-4b6b-b0ff-1edae383c7a5','HUWAI MATE 30 Pro','华为','50','2023-01-11 10:11:07','234','2134',1,'12332542342',200000.0);
INSERT INTO transportation_management.system_log (id,account,busincess_type,ip,`method`,module,`time`) VALUES
	 ('00cf5410-35f0-451b-bcb1-bf19998bc99a','admin@163.com','查询','127.0.0.1','com.example.api.controller.DistributionController.findAll','配送管理','2024-01-31 15:41:52.141000'),
	 ('02159153-02e9-4be4-97b5-bff9292f8635','123@qq.com','查询','127.0.0.1','com.example.api.controller.VehicleController.findAll','车辆管理','2026-02-04 15:20:50.794586'),
	 ('022ec005-f15b-4a14-81d1-375bbad1bbe2','admin@qq.com','查询','127.0.0.1','com.example.api.controller.CommodityController.findAll','商品管理','2022-12-19 14:59:33.348000'),
	 ('0251c30b-75a2-47d6-9756-13d7d11c260d','123@qq.com','查询','127.0.0.1','com.example.api.controller.DistributionController.findAll','配送管理','2025-06-02 22:15:56.044000'),
	 ('031ab444-46a8-448a-85cf-9b7033987bbc','abc@qq.com','查询','127.0.0.1','com.example.api.controller.CommodityController.findAll','商品管理','2022-12-18 17:03:01.678000'),
	 ('03996bab-505a-438d-a655-de0e0f272301','admin@163.com','查询','127.0.0.1','com.example.api.controller.DistributionController.findAll','配送管理','2024-01-31 14:59:15.881000');
INSERT INTO transportation_management.t_cabinet (cabinet_no,cabinet_name,location,total_boxes,status,create_time) VALUES
	 ('1','快递柜1','北门西侧',24,0,NULL),
	 ('A02','快递柜2','北门东侧',30,0,'2026-02-08 20:07:09');
INSERT INTO transportation_management.t_cabinet_box (cabinet_id,box_no,box_type,status,cabinet_name) VALUES
	 (1,'A01',2,1,NULL),
	 (1,'A02',1,1,NULL),
	 (1,'A03',1,0,NULL),
	 (1,'A04',1,0,NULL),
	 (1,'A05',1,1,NULL),
	 (1,'A07',1,2,NULL),
	 (1,'A08',1,1,NULL),
	 (1,'A09',1,0,NULL),
	 (1,'A10',1,0,NULL),
	 (2,'BOX01',1,0,NULL);
INSERT INTO transportation_management.t_cabinet_box (cabinet_id,box_no,box_type,status,cabinet_name) VALUES
	 (2,'BOX02',1,0,NULL),
	 (2,'BOX03',1,0,NULL),
	 (2,'BOX04',1,0,NULL),
	 (2,'BOX05',1,0,NULL),
	 (2,'BOX06',1,0,NULL),
	 (2,'BOX07',1,0,NULL),
	 (2,'BOX08',1,0,NULL),
	 (2,'BOX09',1,0,NULL),
	 (2,'BOX10',1,0,NULL),
	 (2,'B01',2,0,NULL);
INSERT INTO transportation_management.t_cabinet_box (cabinet_id,box_no,box_type,status,cabinet_name) VALUES
	 (2,'B02',2,0,NULL),
	 (2,'B03',2,0,NULL),
	 (2,'B04',2,0,NULL),
	 (2,'B05',2,0,NULL),
	 (2,'B06',2,0,NULL),
	 (2,'B07',2,0,NULL),
	 (2,'B08',2,0,NULL),
	 (2,'B09',2,0,NULL),
	 (2,'B10',2,0,NULL);
INSERT INTO transportation_management.t_express (express_no,company_code,company_name,sender_name,sender_phone,receiver_name,receiver_phone,receiver_address,status,weight,remark,create_time,update_time,biz_type,create_user_id,courier_id,user_name) VALUES
	 ('SF129812349','SF','顺丰','陶','13285790001','陈','13677770001','浙江省杭州市西湖区文三路1号',0,2.50,'','2026-02-05 14:00:00','2026-02-07 17:17:53',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127402','YT','圆通','陶','13285790002','家','13677770002','浙江省杭州市西湖区文三路2号',2,3.10,'','2026-02-05 14:01:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('SF129812350','SF','顺丰','陶','13285790003','陈','13677770003','浙江省杭州市西湖区文三路3号',1,4.20,'','2026-02-05 14:02:00','2026-02-07 17:17:53',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127403','YT','圆通','陶','13285790004','家','13677770004','浙江省杭州市西湖区文三路4号',1,1.50,'','2026-02-05 14:03:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('SF129812351','SF','顺丰','陶','13285790005','陈','13677770005','浙江省杭州市西湖区文三路5号',0,2.80,'','2026-02-05 14:04:00','2026-02-07 17:17:53',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127404','YT','圆通','陶','13285790006','家','13677770006','浙江省杭州市西湖区文三路6号',0,3.50,'','2026-02-05 14:05:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('SF129812352','SF','顺丰','陶','13285790007','陈','13677770007','浙江省杭州市西湖区文三路7号',0,4.00,'','2026-02-05 14:06:00','2026-02-07 17:17:53',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127405','YT','圆通','陶','13285790008','家','13677770008','浙江省杭州市西湖区文三路8号',0,1.80,'','2026-02-05 14:07:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('SF129812353','SF','顺丰','陶','13285790009','陈','13677770009','浙江省杭州市西湖区文三路9号',0,2.20,'','2026-02-05 14:08:00','2026-02-07 17:17:53',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127406','YT','圆通','陶','13285790010','家','13677770010','浙江省杭州市西湖区文三路10号',0,3.00,'','2026-02-05 14:09:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL);
INSERT INTO transportation_management.t_express (express_no,company_code,company_name,sender_name,sender_phone,receiver_name,receiver_phone,receiver_address,status,weight,remark,create_time,update_time,biz_type,create_user_id,courier_id,user_name) VALUES
	 ('YT219480127407','YT','圆通','陶','13285790010','家','13677770010','浙江省杭州市西湖区文三路10号',0,3.00,'','2026-02-05 14:09:00','2026-02-07 17:18:10',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT219480127408','YT','圆通','陶','13285790010','家','13677770010','浙江省杭州市西湖区文三路10号',1,3.00,'','2026-02-05 14:09:00','2026-02-07 17:18:02',1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('ST1928043701274','ST','申通','陶某','13288888888','林林林','13222222222','新疆！new',0,11.00,'大傻帽！！','2026-02-06 15:06:51','2026-02-07 17:17:56',2,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('SF2312112421478910248','SF','顺丰','陶','13288888888','林总','13227409999','浙江省金华市阿斯大厦',0,78.00,'收件测试！！！！！！！','2026-02-07 15:23:10',NULL,1,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT1770449000364','YT','圆通','林','13288889999','王','13288889999','浙江省杭州市杭州大厦B区',3,21.00,'这可是个大东西','2026-02-07 15:25:14',NULL,2,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YT1770449302376','YT','圆通','神秘人','13299999999','陶','13999990000','浙江省杭州市何暗暗剧场',3,1.50,'这是一个神秘物品','2026-02-07 15:29:29',NULL,2,'2fcfac4a-0f08-434a-bed2-4e55a44fe52b',NULL,NULL),
	 ('YD21232130129830122','YD','韵达','奥特曼','13966666666','陈奕迅','13888888888','中国香港大湾区',2,3.00,'一个神秘物种','2026-02-07 21:33:38',NULL,1,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL),
	 ('JD1770472254989','JD','京东','陈奕迅','19566666666','小林','18888888888','浙江省金华市三江街道派出所',3,12.00,'冲锋','2026-02-07 21:51:50',NULL,2,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL),
	 ('ST1770552380779','ST','申通','陶','13288888888','陈','13811111111','浙江省杭州市杭州大厦',3,3.00,'这是一个快递','2026-02-08 20:08:01',NULL,2,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL),
	 ('ST217380127091283','ST','申通','陶','13288889999','林','13966667777','浙江省金华市三江街道',2,1.00,'','2026-02-08 20:12:23',NULL,1,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL);
INSERT INTO transportation_management.t_express (express_no,company_code,company_name,sender_name,sender_phone,receiver_name,receiver_phone,receiver_address,status,weight,remark,create_time,update_time,biz_type,create_user_id,courier_id,user_name) VALUES
	 ('SF999999999999999999','SF','顺丰','陶','13988887777','林','13566667777','浙江省钱塘江内',1,1.00,'','2026-02-08 20:16:33',NULL,1,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL),
	 ('YT1770560532375','YT','圆通','陶','18888888888','陈','18888888888','浙江省金华市',3,11.00,'','2026-02-08 22:22:28',NULL,2,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL),
	 ('YD7777777777777777','YD','韵达','陈','18888888888','陶','18888888888','新疆省乌鲁木齐市',2,1000.00,'','2026-02-08 22:34:13',NULL,1,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL);
INSERT INTO transportation_management.t_express_in (express_id,cabinet_id,box_no,in_user_id,in_time,verify_code,expire_time,status,in_type,out_user_id,cabinet_name,express_no,in_email,out_email) VALUES
	 (15,1,'A01','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 13:42:21','983071',NULL,0,1,'7fd4fa14-8f4f-4c4a-83f1-51e94b20f580',NULL,NULL,NULL,NULL),
	 (5,1,'A02','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 14:47:55','453260',NULL,1,1,'7fd4fa14-8f4f-4c4a-83f1-51e94b20f581',NULL,NULL,NULL,NULL),
	 (19,1,'A03','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 18:43:53','709013',NULL,1,2,'31c2ebc7-d4f7-494d-ae3e-c3f72365c248',NULL,NULL,NULL,NULL),
	 (18,1,'A04','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 18:44:40','270056',NULL,1,2,'31c2ebc7-d4f7-494d-ae3e-c3f72365c248',NULL,NULL,NULL,NULL),
	 (6,1,'A05','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 18:47:29','810482',NULL,0,1,'7fd4fa14-8f4f-4c4a-83f1-51e94b20f581',NULL,NULL,NULL,NULL),
	 (7,1,'A08','2fcfac4a-0f08-434a-bed2-4e55a44fe52b','2026-02-07 18:51:31','369692',NULL,0,1,'7fd4fa14-8f4f-4c4a-83f1-51e94b20f580',NULL,NULL,NULL,NULL),
	 (20,1,'A02','c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-07 21:36:13','390856',NULL,1,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL,NULL,NULL),
	 (21,1,'A02','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-07 21:53:07','185714',NULL,1,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL,NULL,NULL),
	 (22,1,'A02','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-08 20:08:40','856253',NULL,1,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL,NULL,NULL),
	 (23,1,'A02','c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-08 20:12:50','836455',NULL,1,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL,NULL,NULL);
INSERT INTO transportation_management.t_express_in (express_id,cabinet_id,box_no,in_user_id,in_time,verify_code,expire_time,status,in_type,out_user_id,cabinet_name,express_no,in_email,out_email) VALUES
	 (24,1,'A02','c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-08 20:16:58','167410',NULL,0,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL,NULL,NULL),
	 (26,2,'BOX10','eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-08 22:22:48','592454',NULL,1,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2',NULL,NULL,NULL,NULL),
	 (27,2,'BOX02','c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-08 22:34:39','171131',NULL,1,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f',NULL,NULL,NULL,NULL);
INSERT INTO transportation_management.t_express_out (express_id,out_type,out_user_id,out_time,remark,out_role,express_no,out_user_email,verify_code) VALUES
	 (5,1,'7fd4fa14-8f4f-4c4a-83f1-51e94b20f580','2026-02-07 19:20:52','',1,NULL,NULL,'453260'),
	 (18,2,'31c2ebc7-d4f7-494d-ae3e-c3f72365c248','2026-02-07 19:26:39','快递员出库',2,NULL,NULL,'270056'),
	 (19,2,'31c2ebc7-d4f7-494d-ae3e-c3f72365c248','2026-02-07 19:54:24','',2,NULL,NULL,'709013'),
	 (20,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-07 21:50:42','',1,NULL,NULL,'390856'),
	 (21,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-07 22:07:02','我已经取到用户快递并发出',2,NULL,NULL,'185714'),
	 (22,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-08 20:10:28','',2,NULL,NULL,'856253'),
	 (23,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-08 20:15:07','',1,NULL,NULL,'836455'),
	 (26,2,'c88a8a33-40e9-4463-9bda-c0a89f488fd2','2026-02-08 22:32:24','',2,NULL,NULL,'592454'),
	 (27,1,'eaed5ce2-fa44-4f09-aff7-6b6a719cdc7f','2026-02-08 22:37:48','',1,NULL,NULL,'171131');
INSERT INTO transportation_management.t_express_stock (express_id,cabinet_id,box_no,status,in_time,cabinet_name,express_no) VALUES
	 (15,1,'A01',0,'2026-02-07 13:42:21',NULL,NULL),
	 (5,1,'A02',1,'2026-02-07 14:47:55',NULL,NULL),
	 (19,1,'A03',1,'2026-02-07 18:43:53',NULL,NULL),
	 (18,1,'A04',1,'2026-02-07 18:44:40',NULL,NULL),
	 (6,1,'A05',0,'2026-02-07 18:47:29',NULL,NULL),
	 (7,1,'A08',0,'2026-02-07 18:51:31',NULL,NULL),
	 (20,1,'A02',1,'2026-02-07 21:36:13',NULL,NULL),
	 (21,1,'A02',1,'2026-02-07 21:53:07',NULL,NULL),
	 (22,1,'A02',1,'2026-02-08 20:08:40',NULL,NULL),
	 (23,1,'A02',1,'2026-02-08 20:12:50',NULL,NULL);
INSERT INTO transportation_management.t_express_stock (express_id,cabinet_id,box_no,status,in_time,cabinet_name,express_no) VALUES
	 (24,1,'A02',0,'2026-02-08 20:16:58',NULL,NULL),
	 (26,2,'BOX10',1,'2026-02-08 22:22:48',NULL,NULL),
	 (27,2,'BOX02',1,'2026-02-08 22:34:39',NULL,NULL);
INSERT INTO transportation_management.vehicle (id,create_at,driving,`number`,`type`) VALUES
	 ('786fb288-4445-4784-b923-de013fa3892f','2023-01-11 13:38:19',0,'京A0001','货车'),
	 ('ce33de59-9584-4161-a17e-9046399d14c6','2022-11-27 12:26:18',0,'京A0000','货车');
INSERT INTO transportation_management.warehouse (id,create_at,name,principle) VALUES
	 ('9f2a2784-e182-4fdf-85e8-c3bde6d539d2','2022-11-22 14:16:46','A号仓库','杰克');
