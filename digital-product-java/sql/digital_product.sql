-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: digital_product
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accessory`
--

DROP TABLE IF EXISTS `accessory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` int unsigned NOT NULL COMMENT '关联店铺id',
  `type_id` int unsigned NOT NULL COMMENT '关联数码配件类型id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配件名称',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配件图片',
  `price` decimal(10,2) NOT NULL COMMENT '配件价格',
  `store` int NOT NULL COMMENT '剩余数量',
  `introduce` text COLLATE utf8mb4_unicode_ci COMMENT '配件简介',
  `sale_volume` int DEFAULT '0' COMMENT '销量',
  `sale_status` tinyint NOT NULL COMMENT '售卖状态：0-已售罄，1-售卖中，2-未上架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码配件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory`
--

LOCK TABLES `accessory` WRITE;
/*!40000 ALTER TABLE `accessory` DISABLE KEYS */;
INSERT INTO `accessory` VALUES (1,1,3,'漫步者耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/693054576cea9230b8e14706.jpg',266.00,25,'漫步者W820NB是2025年上市的头戴式蓝牙耳机，搭载混合主动降噪技术，最高降噪深度达38dB。 耳机采用低功耗蓝牙方案，普通模式下续航为49小时，降噪模式下可连续使用29小时。 其40mm镀钛复合振膜发声单元支持Hi-Res认证与ACC高清编码，音质表现收放自如但偶现电流声。 产品还具备80ms低延迟游戏模式和环境声监听功能。',0,1),(2,1,1,'超薄快充充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693140a66ceadbf16f068167.png',129.99,45,'10000mAh超薄设计，支持双向快充',0,1),(3,2,1,'大容量移动电源','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143cd6ceadbf16f06817d.webp',189.50,30,'20000mAh大容量，三口输出',0,1),(4,3,1,'迷你便携充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144876ceadbf16f068186.jpg',79.99,60,'5000mAh迷你设计，轻巧便携',0,1),(5,4,1,'无线充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145396ceadbf16f06818f.jpg',199.00,25,'支持无线充电，10000mAh容量',0,1),(6,5,1,'快充充电宝套装','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931461f6ceadbf16f06819a.webp',159.99,35,'含快充线套装，15000mAh',0,1),(7,6,2,'高速USB3.0 U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146986ceadbf16f0681a0.webp',89.99,100,'64GB高速传输，USB3.0接口',0,1),(8,7,2,'金属旋转U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146fe6ceadbf16f0681a5.webp',129.00,80,'128GB金属机身，旋转保护',0,1),(9,8,2,'迷你U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147846ceadbf16f0681ad.webp',59.99,120,'32GB超迷你设计',0,1),(10,9,2,'双接口U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693148036ceadbf16f0681b4.webp',109.00,65,'USB+Type-C双接口，64GB',0,1),(11,10,2,'加密U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693148676ceadbf16f0681ba.webp',159.99,40,'256GB硬件加密U盘',0,1),(12,1,3,'高保真有线耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693140706ceadbf16f068166.jpg',149.99,55,'HiFi音质，带线控麦克风',0,1),(13,2,3,'入耳式运动耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143bd6ceadbf16f06817c.jpg',89.00,70,'防汗设计，适合运动',0,1),(14,3,3,'游戏电竞耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931447a6ceadbf16f068185.webp',199.99,40,'7.1声道，带麦克风',0,1),(15,4,3,'复古头戴式耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145296ceadbf16f06818e.webp',299.00,25,'经典复古设计，音质出色',0,1),(16,5,3,'监听级耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146126ceadbf16f068199.webp',399.99,20,'专业监听，录音棚品质',0,1),(17,6,4,'无线游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146876ceadbf16f06819f.webp',249.99,35,'2.4G无线连接，多平台支持',0,1),(18,7,4,'电竞级游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146ee6ceadbf16f0681a4.webp',349.00,25,'专业电竞，可编程按键',0,1),(19,8,4,'便携游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147766ceadbf16f0681ac.webp',129.99,50,'手机专用，蓝牙连接',0,1),(20,9,4,'经典游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147f26ceadbf16f0681b3.webp',99.00,60,'经典设计，兼容多主机',0,1),(21,10,4,'精英版游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693148626ceadbf16f0681b9.webp',499.99,15,'精英版，可更换配件',0,1),(22,1,5,'无线静音鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693140026ceadbf16f068165.jpg',79.99,80,'2.4G无线，静音按键',0,1),(23,2,5,'游戏电竞鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143aa6ceadbf16f06817b.jpg',199.00,45,'RGB灯效，高精度传感器',0,1),(24,3,5,'蓝牙多设备鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931446b6ceadbf16f068184.webp',129.99,55,'支持三设备切换',0,1),(25,4,5,'垂直人体工学鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931450d6ceadbf16f06818d.webp',159.00,30,'垂直设计，缓解手腕疲劳',0,1),(26,5,5,'便携旅行鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146026ceadbf16f068198.webp',59.99,95,'超薄设计，便携收纳',0,1),(27,6,6,'机械键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146786ceadbf16f06819e.webp',299.99,40,'Cherry轴，RGB背光',0,1),(28,7,6,'无线蓝牙键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146de6ceadbf16f0681a3.webp',179.00,60,'蓝牙连接，超薄设计',0,1),(29,8,6,'游戏机械键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147696ceadbf16f0681ab.webp',399.99,35,'游戏专用，宏编程',0,1),(30,9,6,'静电容键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147ed6ceadbf16f0681b2.webp',599.00,18,'静电容轴，手感顶级',2,1),(31,10,6,'便携折叠键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931485a6ceadbf16f0681b8.webp',149.99,50,'可折叠设计，蓝牙连接',0,1),(32,1,7,'智能运动手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/69313fc56ceadbf16f068164.png',399.99,30,'心率监测，GPS定位',0,1),(33,2,7,'户外运动手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143916ceadbf16f06817a.jpg',499.00,25,'户外专业，长续航',0,1),(34,3,7,'健康监测手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144586ceadbf16f068183.png',299.99,40,'血氧监测，睡眠分析',0,1),(35,4,7,'游泳防水手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144fc6ceadbf16f06818c.jpg',349.00,35,'50米防水，游泳专用',0,1),(36,5,7,'基础运动手环','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145f06ceadbf16f068197.webp',129.99,69,'基础运动监测，长续航',1,1),(37,6,1,'太阳能充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146656ceadbf16f06819d.webp',229.00,20,'内置太阳能充电板',0,1),(38,7,1,'车载充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146c66ceadbf16f0681a2.webp',139.99,40,'支持车载充电，10000mAh',0,1),(39,8,2,'高速固态U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931475b6ceadbf16f0681aa.webp',199.00,35,'512GB固态级别速度',0,1),(40,9,2,'卡通造型U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147dc6ceadbf16f0681b1.webp',69.99,90,'可爱卡通造型，32GB',0,1),(41,10,3,'KTV麦克风耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931484d6ceadbf16f0681b7.webp',159.00,30,'带高质量麦克风，K歌专用',0,1),(42,1,3,'降噪有线耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6930ec696cea7d35c15cc3e7.png',249.99,24,'主动降噪技术',1,1),(43,2,4,'复古摇杆手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143826ceadbf16f068179.jpg',179.00,20,'街机风格摇杆手柄',0,1),(44,3,4,'飞行模拟手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144456ceadbf16f068182.jpg',399.00,15,'专业飞行模拟游戏',0,1),(45,4,5,'轨迹球鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144ef6ceadbf16f06818b.webp',189.99,25,'拇指操作轨迹球设计',0,1),(46,5,5,'充电式无线鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145de6ceadbf16f068196.webp',99.00,50,'内置电池，Type-C充电',0,1),(47,6,6,'数字小键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146556ceadbf16f06819c.webp',89.99,60,'独立数字键盘，USB接口',0,1),(48,7,6,'多媒体键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693146b96ceadbf16f0681a1.webp',129.00,45,'集成多媒体控制键',0,1),(49,8,7,'儿童运动手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147476ceadbf16f0681a9.webp',199.99,55,'儿童专用，定位功能',0,1),(50,9,7,'商务运动手表','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147cb6ceadbf16f0681b0.webp',599.00,20,'商务外观，运动功能',0,1),(51,10,1,'磁吸充电宝','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931483e6ceadbf16f0681b6.webp',179.00,35,'磁吸式设计，5000mAh',0,1),(52,1,2,'防水U盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6930ec2a6cea7d35c15cc3e6.png',119.99,40,'IP68防水等级，128GB。',0,1),(53,2,3,'电竞入耳式耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143676ceadbf16f068178.webp',179.00,30,'电竞专用，低延迟',0,1),(54,3,4,'体感游戏手柄','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144316ceadbf16f068181.webp',299.99,20,'支持体感操作',0,1),(55,4,5,'垂直无线鼠标','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144e16ceadbf16f06818a.webp',139.00,40,'垂直人体工学，无线',0,1);
/*!40000 ALTER TABLE `accessory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accessory_orders`
--

DROP TABLE IF EXISTS `accessory_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory_orders` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` bigint NOT NULL COMMENT '订单号',
  `order_status` tinyint DEFAULT NULL COMMENT '订单状态：0-待付款，1-待接单，2-派送中，3-已送达，4-已完成，5-已取消',
  `order_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '购买总价',
  `user_id` int unsigned NOT NULL COMMENT '关联用户id',
  `shop_id` int unsigned NOT NULL COMMENT '关联店铺id',
  `accessory_id` int unsigned NOT NULL COMMENT '关联数码配件id',
  `accessory_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配件名称，冗余',
  `accessory_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配件图片，冗余',
  `accessory_price` decimal(10,2) DEFAULT NULL COMMENT '配件价格，冗余',
  `address_id` int unsigned NOT NULL COMMENT '关联地址id',
  `consignee` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人，冗余',
  `phone_number` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货电话，冗余',
  `province_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级区划编号，冗余',
  `city_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级区划编号，冗余',
  `district_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级区划编号，冗余',
  `detail_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址，冗余',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码配件订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_orders`
--

LOCK TABLES `accessory_orders` WRITE;
/*!40000 ALTER TABLE `accessory_orders` DISABLE KEYS */;
INSERT INTO `accessory_orders` VALUES (1,125327515064467457,1,'2025-12-04 17:34:47',1,129.99,1,5,36,'基础运动手环','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145f06ceadbf16f068197.webp',129.99,1,'张三','13600000000','21','2104','210411','张三的家'),(2,125327515064467458,1,'2025-12-04 17:34:47',1,249.99,1,1,42,'降噪有线耳机','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6930ec696cea7d35c15cc3e7.png',249.99,1,'张三','13600000000','21','2104','210411','张三的家'),(3,125327515064467459,1,'2025-12-04 17:34:47',2,1198.00,1,9,30,'静电容键盘','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147ed6ceadbf16f0681b2.webp',599.00,1,'张三','13600000000','21','2104','210411','张三的家');
/*!40000 ALTER TABLE `accessory_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accessory_type`
--

DROP TABLE IF EXISTS `accessory_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory_type` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码配件类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_type`
--

LOCK TABLES `accessory_type` WRITE;
/*!40000 ALTER TABLE `accessory_type` DISABLE KEYS */;
INSERT INTO `accessory_type` VALUES (2,'U盘'),(1,'充电宝'),(3,'有线耳机'),(4,'游戏手柄'),(7,'运动手表'),(6,'键盘'),(5,'鼠标');
/*!40000 ALTER TABLE `accessory_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned NOT NULL COMMENT '关联用户id',
  `consignee` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人',
  `phone_number` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货电话',
  `province_code` char(6) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '省级区划编号',
  `city_code` char(6) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '市级区划编号',
  `district_code` char(6) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '区级区划编号',
  `detail_address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细地址',
  `tag` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签',
  `is_default` tinyint DEFAULT '0' COMMENT '是否作为默认地址：0-非默认，1-默认',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,1,'张三','13600000000','21','2104','210411','张三的家','家',1),(2,1,'张三','13600000000','44','4403','440304','张三的公司','公司',0);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` tinyint NOT NULL COMMENT '角色：0-管理员，1-店家，2-用户',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(254) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'管理员','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303bd86cea9230b8e146ff.jpg','admin','afdd0b4ad2ec172c586e2150770fbf9e',0,'13600000000','admin@digital.com','2025-12-03 19:32:37','2025-12-04 16:59:13',0);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned NOT NULL COMMENT '关联用户id',
  `quantity` int DEFAULT '1' COMMENT '商品数量',
  `accessory_id` int unsigned NOT NULL COMMENT '关联数码配件id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,1,1,31),(3,1,1,36),(4,1,1,42),(5,1,1,40),(6,1,2,30);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collect`
--

DROP TABLE IF EXISTS `collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collect` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned NOT NULL COMMENT '关联用户id',
  `product_id` int unsigned NOT NULL COMMENT '关联数码产品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collect`
--

LOCK TABLES `collect` WRITE;
/*!40000 ALTER TABLE `collect` DISABLE KEYS */;
INSERT INTO `collect` VALUES (1,1,9),(2,1,32),(4,1,13),(5,1,1);
/*!40000 ALTER TABLE `collect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `digital_shop`
--

DROP TABLE IF EXISTS `digital_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `digital_shop` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` tinyint NOT NULL COMMENT '角色：0-管理员，1-店家，2-用户',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(254) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `province_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级区划编号',
  `city_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级区划编号',
  `district_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级区划编号',
  `detail_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `introduce` text COLLATE utf8mb4_unicode_ci COMMENT '介绍',
  `practice_license` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '营业执照',
  `principal_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人姓名',
  `principal_no` char(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人身份证号',
  `audit_status` tinyint NOT NULL COMMENT '审核状态：0-待提交，1-待审核，2-审核通过，3-审核不通过',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `digital_shop`
--

LOCK TABLES `digital_shop` WRITE;
/*!40000 ALTER TABLE `digital_shop` DISABLE KEYS */;
INSERT INTO `digital_shop` VALUES (1,'店铺1','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop1','afdd0b4ad2ec172c586e2150770fbf9e',1,'13680000002','sshop1@qq.com','21','2103','210304','立山区广场','您身边的专业科技生活伙伴，我们汇聚国内外主流与新兴品牌的精选数码产品，涵盖智能手机、笔记本电脑、影音娱乐及智能穿戴等全品类。店铺不仅是卖场，更是体验空间，设有沉浸式体验区，让您亲手感受前沿科技的魅力。我们的核心优势在于专业的导购咨询，团队成员均为资深科技爱好者，致力于根据您的实际使用场景与预算，提供清晰、客观的选购建议。同时，我们承诺所有产品均为官方正品，并提供贴心的售后支持与技术服务，确保您购机无忧。欢迎前来探索，开启您的高效智能生活。','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','张三','441825199909091234',2,'2025-12-03 21:30:59','2025-12-03 21:48:59',0),(2,'店铺2','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop2','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000002','shop2@example.com','14','1404','140406','朝阳区某某街道2号','专注于高品质数码产品销售，提供手机、电脑、平板等全系列智能设备，正品保障，售后无忧，欢迎选购','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','张三','110101199001010012',2,'2025-12-03 22:57:50','2025-12-03 23:02:35',0),(3,'店铺3','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop3','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000003','shop3@example.com','14','1404','140406','黄浦区某某路3号','专业经营电脑配件和外设产品，机械键盘、电竞鼠标、显卡内存应有尽有，品质优良，价格实惠，诚信经营','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','李四','310101199002020023',2,'2025-12-03 22:57:50','2025-12-03 23:02:41',0),(4,'店铺4','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop4','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000004','shop4@example.com','44','4404','440403','天河区某某大道4号','主营苹果系列产品及配件，iPhone、iPad、MacBook等全线产品，提供专业技术支持和维修服务','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','王五','440103199003030034',2,'2025-12-03 22:57:50','2025-12-03 23:03:20',0),(5,'店铺5','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop5','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000005','shop5@example.com','35','3504','350421','西湖区某某街5号','智能穿戴设备专营店，销售智能手表、运动手环、蓝牙耳机等产品，紧跟科技潮流，引领时尚生活方式','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','赵六','330106199004040045',2,'2025-12-03 22:57:50','2025-12-03 23:03:36',0),(6,'店铺6','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop6','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000006','shop6@example.com','42','4206','420624','玄武区某某巷6号','游戏外设专卖店，专业提供电竞装备，高性能显卡、电竞显示器、游戏耳机等，助力玩家畅享游戏乐趣','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','孙七','320102199005050056',2,'2025-12-03 22:57:50','2025-12-03 23:02:46',0),(7,'店铺7','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop7','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000007','shop7@example.com','13','1302','130204','锦江区某某路7号','摄影摄像器材专业店，经营单反相机、镜头、稳定器、无人机等设备，为摄影爱好者提供全方位解决方案','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','周八','510104199006060067',2,'2025-12-03 22:57:50','2025-12-03 23:02:59',0),(8,'店铺8','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop8','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000008','shop8@example.com','45','4504','450421','江岸区某某街8号','办公数码设备供应商，提供打印机、扫描仪、投影仪等办公用品，专业服务企业客户，支持批量采购优惠','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','吴九','420102199007070078',2,'2025-12-03 22:57:50','2025-12-03 23:03:28',0),(9,'店铺9','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop9','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000009','shop9@example.com','14','1403','140321','万州区某某道9号','智能家居产品专营，销售智能音箱、智能门锁、摄像头等IOT设备，打造便捷舒适的智慧生活空间','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','郑十','500101199008080089',2,'2025-12-03 22:57:50','2025-12-03 23:02:53',0),(10,'店铺10','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303db56cea9230b8e14702.png','sshop10','afdd0b4ad2ec172c586e2150770fbf9e',1,'13800000010','shop10@example.com','13','1303','130304','新城区某某路10号','二手数码交易平台，收购和销售各类二手电子产品，严格质检，性价比高，为学生和预算有限用户首选','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303da06cea9230b8e14701.png','冯十一','610102199009090090',2,'2025-12-03 22:57:50','2025-12-03 23:02:29',0);
/*!40000 ALTER TABLE `digital_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
  `release_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'系统将于今晚升级维护，暂停服务四小时通知','尊敬的顾客，您好！为了提升您的购物体验，我们将于今晚（9月11日）23:00至次日凌晨3:00进行系统升级维护。期间网站将暂停访问，订单查询、支付等功能将暂时无法使用。请您提前安排购物时间，避免影响您的购物计划。升级后，系统将更稳定、页面加载更快，并支持更多便捷功能。感谢您的理解与支持！如有紧急问题，可联系客服。','2025-12-03 21:50:47');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` int unsigned NOT NULL COMMENT '关联店铺id',
  `type_id` int unsigned NOT NULL COMMENT '关联数码产品类型id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `used` tinyint NOT NULL COMMENT '产品品质：0-全新，1-二手',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品图片',
  `price` decimal(10,2) NOT NULL COMMENT '产品价格',
  `store` int NOT NULL COMMENT '剩余数量',
  `introduce` text COLLATE utf8mb4_unicode_ci COMMENT '产品介绍',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '产品详情',
  `sale_status` tinyint NOT NULL COMMENT '售卖状态：0-已售罄，1-售卖中，2-未上架',
  `recommend` tinyint DEFAULT '0' COMMENT '是否推荐：0-未推荐，1-推荐中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码产品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,1,'华为Mate 70',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/6930527f6cea9230b8e14705.jpg',8999.99,9,'当预期的边界被打破，真正的旗舰，自带光芒。华为Mate 70，一场从内到外的彻底进化。它，是智慧的原点。HarmonyOS NEXT原生系统，让体验纯净如初，让万物互联浑然一体。它，是影像的破局者。XMAGE镜头捕捉的不只是画面，更是情绪、故事与流动的时光。光影之间，尽在掌握。它，是坚毅的化身。玄武钢骨，寰宇屏幕，在优雅与耐用之间，取得了前所未有的平衡。它，更是未来的先知。将AI化为无声的伙伴，预见你的需求，守护你的隐私，赋能每一次创造。告别追赶，开始引领。华为Mate 70','<p style=\"text-align: start;\"><strong>华为Mate 70系列：智慧领航，视野无疆</strong></p><p style=\"text-align: start;\">当科技与美学再次相遇，华为Mate 70系列以颠覆性的创新，重塑旗舰标杆。它不仅是一部手机，更是通向未来的钥匙，将智能体验、影像艺术与无畏探索融为一体。</p><p style=\"text-align: start;\"><strong>【鸿蒙原生，智界新生】</strong><br>搭载全新HarmonyOS NEXT，华为Mate 70系列实现真正意义上的原生体验。从系统底层彻底重构，带来更极致的流畅性、更强的隐私安全保障与更智慧的AI交互。万物互联不再只是概念，而是你指尖轻触即达的现实。</p><p style=\"text-align: start;\"><strong>【光影捕手，时空艺术】</strong><br>全新升级的XMAGE影像系统，首次搭载物理可变光圈与超动态鹰眼主摄。无论昼夜明暗，皆能捕捉瞬息万变的光影细节。AI构图助手与4K全焦段人像视频，让每一次创作都宛如大师执镜。</p><p style=\"text-align: start;\"><strong>【玄武钢骨，坚毅之躯】</strong><br>采用第二代昆仑玻璃与一体化高强度机身设计，抗摔耐磨能力再跃升。全新“寰宇屏”以微曲四边等深设计，兼顾沉浸视觉与舒适握感。IP68+防水防尘，无惧风雨挑战。</p><p style=\"text-align: start;\"><strong>【芯强续航，时刻在线】</strong><br>凭借新一代麒麟芯片与能效聚合技术，性能澎湃的同时功耗大幅优化。5500mAh高密度电池配合100W有线与80W无线超级快充，让你彻底告别电量焦虑。</p><p style=\"text-align: start;\"><strong>【AI无处不在，感知未来】</strong><br>华为Mate 70系列将AI融入每一个场景：实时多语言翻译、会议摘要生成、AI隐私防护……它不仅是工具，更是懂你所需的智慧伙伴。</p><p style=\"text-align: start;\"><strong>华为Mate 70系列——不只是超越，更是定义未来。</strong></p>',1,1),(2,5,1,'华为Mate 70 Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145c86ceadbf16f068195.webp',9999.99,15,'旗舰之上，再造巅峰。华为Mate 70 Pro以更强大的影像系统、更持久的续航能力，诠释极致科技美学。','<p><strong>华为Mate 70 Pro：旗舰体验，王者归来</strong></p><p>升级版XMAGE影像系统，支持100倍数字变焦，捕捉远方精彩。第二代昆仑玻璃，抗摔能力提升300%。5800mAh超大电池，120W有线闪充，让你畅享一整天。</p>',1,0),(3,2,1,'华为Mate 80 pro max',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/6930527f6cea9230b8e14705.jpg',8999.99,20,'智慧领航，视野无疆。HarmonyOS NEXT原生系统，让体验纯净如初，万物互联浑然一体。','<h1 style=\"text-align: start;\">HUAWEI Mate 80 系列：2025年度旗舰，重新定义智能通信边界</h1><p style=\"text-align: start;\">2025年11月25日，华为正式发布备受期待的Mate 80系列智能手机，再次展现了其在高端移动科技领域的引领地位。该系列包含四款机型：Mate 80、Mate 80 Pro、Mate 80 Pro Max以及顶级奢华版Mate 80 RS非凡大师，每一款都凝聚了华为最新的技术创新与设计哲学。</p><h2 style=\"text-align: start;\">突破性通信能力：无网也能连接世界</h2><p style=\"text-align: start;\">Mate 80系列最引人瞩目的突破在于其<strong>极限环境通信能力</strong>：</p><ul><li style=\"text-align: start;\">全系支持天通卫星通信与双向北斗卫星消息</li><li style=\"text-align: start;\">业界首发 700MHz无网应急通信，即使在没有信号、基站失效的自然灾害情况下，仍可实现最远13公里的设备间连接</li><li style=\"text-align: start;\">为户外探险者量身打造的“户外探索模式”，提供超过10000条专业路线指引</li></ul><h2 style=\"text-align: start;\">卓越设计与工艺美学</h2><p style=\"text-align: start;\">Mate 80系列延续并进化了华为旗舰的设计语言：</p><ul><li style=\"text-align: start;\">经典双环镜头设计搭配全金属机身框架</li><li style=\"text-align: start;\">后盖采用创新的锦纤材质，兼具轻盈与坚韧</li><li style=\"text-align: start;\">提供曜石黑、雪域白、晨曦金、云杉绿四款雅致配色</li><li style=\"text-align: start;\">Mate 80 RS非凡大师版更带来独特的槿紫配色，彰显尊贵气质</li><li style=\"text-align: start;\">整机厚度仅7.95mm，重量控制在217-219克之间，实现了强大性能与轻薄手感的完美平衡</li></ul><h2 style=\"text-align: start;\">性能与显示全面升级</h2><h3 style=\"text-align: start;\">显示体验</h3><ul><li style=\"text-align: start;\">Mate 80/Pro采用6.75英寸OLED显示屏</li><li style=\"text-align: start;\">Mate 80 Pro Max首发的超透亮灵珑屏，带来前所未有的视觉通透感</li></ul><h3 style=\"text-align: start;\">性能突破</h3><ul><li style=\"text-align: start;\">全系首发HarmonyOS 6操作系统，具备沉浸光感、灵动粒子等全新交互体验</li><li style=\"text-align: start;\">通过软硬件深度整合优化，Mate 80 Pro Max整机性能较上一代提升42%</li><li style=\"text-align: start;\">首次在移动端支持实时光线追踪技术，游戏与图形体验达到新高度</li></ul><h2 style=\"text-align: start;\">专业影像与全天候续航</h2><h3 style=\"text-align: start;\">影像系统</h3><ul><li style=\"text-align: start;\">搭载第二代红枫原色摄像头，色彩还原更加精准自然</li><li style=\"text-align: start;\">延续华为在移动影像领域的领先优势</li></ul><h3 style=\"text-align: start;\">续航能力</h3><ul><li style=\"text-align: start;\">配备5750mAh大容量电池（额定5620mAh）</li><li style=\"text-align: start;\">Mate 80支持66W有线快充 + 50W无线快充</li><li style=\"text-align: start;\">Mate 80 Pro/Pro Max支持100W有线快充 + 80W无线快充</li><li style=\"text-align: start;\">全系支持反向无线充电功能</li><li style=\"text-align: start;\">Mate 80 Pro更提供13天极限续航模式，满足特殊场景下的超长待机需求</li></ul><h2 style=\"text-align: start;\">全方位防护与智能体验</h2><ul><li style=\"text-align: start;\">全系支持6米深度IP68/IP69级防尘防水，适应各种严苛环境</li><li style=\"text-align: start;\">新增“智感握姿”功能，智能识别握持状态并调整界面交互</li><li style=\"text-align: start;\">坚固耐用的同时保持优雅外观</li></ul><h2 style=\"text-align: start;\">价格与上市信息</h2><p style=\"text-align: start;\">华为Mate 80系列已于2025年11月28日正式开售：</p><ul><li style=\"text-align: start;\">Mate 80: 4699元起</li><li style=\"text-align: start;\">Mate 80 Pro: 5999元起</li><li style=\"text-align: start;\">Mate 80 Pro Max: 7999元起</li><li style=\"text-align: start;\">Mate 80 RS非凡大师: 20GB内存 + 512GB/1TB存储，售价11999/12999元</li></ul><hr/><p><br></p><p style=\"text-align: start;\">华为Mate 80系列不仅是2025年旗舰手机的标杆之作，更是一次对未来移动通信可能性的探索。它将尖端卫星通信技术、卓越性能、专业影像和全天候可靠性融为一体，重新定义了智能手机在极端环境下的价值与可能性，为全球用户提供了真正“无界”的智能体验。</p>',1,0),(4,5,1,'华为P60 Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145b76ceadbf16f068194.webp',7999.99,25,'影像旗舰，光影诗人。XMAGE影像系统，记录每一个动人瞬间。','<p><strong>华为P60 Pro：影像创作利器</strong></p><p>搭载超聚光主摄，支持F1.4-F4.0可变光圈。超光谱摄像头，还原真实色彩。88W超级快充，30分钟充满。</p>',1,1),(5,8,1,'华为nova 12 Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147356ceadbf16f0681a8.webp',4999.99,30,'前置双目立体视觉系统，自拍更出色。时尚外观，年轻首选。','<p><strong>华为nova 12 Pro：年轻人的潮流之选</strong></p><p>前置6000万像素超广角镜头，支持4K视频录制。66W快充，轻薄设计，仅重188g。</p>',1,0),(6,2,1,'华为畅享70',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931434c6ceadbf16f068177.jpg',1299.99,40,'高品质二手机，性价比之选。6000mAh超大电池，续航无忧。','<p><strong>华为畅享70：超长续航，品质保证</strong></p><p>9成新二手机，原装配件齐全。6000mAh大电池，22.5W快充。6.75英寸大屏，影音娱乐更畅快。</p>',1,1),(7,5,2,'华为MateBook X Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145a36ceadbf16f068193.webp',12999.99,10,'14.2英寸3.1K触控全面屏，轻薄本天花板。英特尔酷睿Ultra处理器，性能强悍。','<p><strong>华为MateBook X Pro：商务精英之选</strong></p><p>重量仅980g，厚度13.5mm。90Hz高刷屏，支持十点触控。60Wh大电池，续航可达13小时。</p>',1,0),(8,6,2,'联想ThinkPad X1 Carbon',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931463d6ceadbf16f06819b.webp',11999.99,12,'商务本标杆，军工级品质。碳纤维机身，坚固耐用。','<p><strong>ThinkPad X1 Carbon：商务移动办公首选</strong></p><p>14英寸2.8K OLED屏幕，100% DCI-P3色域。通过12项MIL-STD军标测试。支持快充，1小时充电80%。</p>',1,1),(9,3,2,'苹果MacBook Pro 14',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931440e6ceadbf16f068180.jpg',14999.99,8,'M3 Pro芯片，专业创作利器。Liquid Retina XDR显示屏，色彩精准。','<p><br></p><h3 style=\"text-align: start;\"><strong>Apple MacBook Pro 14：重塑专业级笔记本电脑的巅峰之作</strong></h3><p style=\"text-align: start;\">当极致的性能、顶级的显示效果与匠心设计融为一体，便成就了这台为专业创作者、开发者和追求极致体验的用户量身打造的移动工作站——<strong>MacBook Pro 14</strong>。它不仅是工具的升级，更是对“笔记本电脑能做什么”的一次重新定义。</p><h4 style=\"text-align: start;\"><strong>核心革新：从“芯”开始的飞跃</strong></h4><ul><li style=\"text-align: start;\">Apple Silicon 的巅峰：M3 Pro 或 M3 Max 芯片MacBook Pro 14 的强大，始于其核心。搭载 Apple 最新的 M3 Pro 或 M3 Max 芯片，它采用突破性的 3 纳米工艺，带来了前所未有的性能与能效比。CPU性能：比前代快达40%，无论是编译代码、处理海量数据还是运行多个专业应用，都游刃有余。GPU革命：全新架构支持硬件加速光线追踪和网格着色，让3D渲染、视频特效和游戏画面更加真实、流畅。动态缓存技术最大化提升了GPU效率。统一内存：高达128GB的统一内存，让CPU、GPU能瞬时访问海量数据，处理大型项目如ProRes视频流、数十亿参数的机器学习模型时，依然行云流水。</li></ul><h4 style=\"text-align: start;\"><strong>视界巅峰：令人沉浸的 Liquid Retina XDR 显示屏</strong></h4><p style=\"text-align: start;\">这块屏幕本身就是一件杰作，定义了移动显示的标杆。</p><ul><li style=\"text-align: start;\">极致动态范围：采用 mini-LED 技术，拥有惊人的 1000 尼特持续亮度和 1600 尼特峰值亮度。对比度高达 1,000,000:1，让深邃的黑与耀眼的白同时呈现。</li><li style=\"text-align: start;\">ProMotion 自适应刷新率：最高达 120Hz，滚动和操作无比顺滑，同时智能节省电量。</li><li style=\"text-align: start;\">专业色彩：覆盖 P3 广色域，出厂精准校准，是摄影、调色和设计的可靠工具。</li></ul><h4 style=\"text-align: start;\"><strong>设计回归：强大，亦高效实用</strong></h4><p style=\"text-align: start;\">苹果听取了专业用户的心声，带来了备受好评的经典设计回归。</p><ul><li style=\"text-align: start;\">丰富的端口：告别“转接头焦虑”。配备：MagSafe 3 磁吸充电口：安全分离，保护机身。三个 Thunderbolt 4 (USB-C) 端口：高速数据传输、连接多台显示器。HDMI 端口：直连显示器或投影仪。SDXC 卡槽：摄影师和视频工作者的福音，直接读取素材。高阻抗耳机插孔：支持高品质音频输出。</li><li style=\"text-align: start;\">全尺寸功能键盘：舒适的键程，独立的实体 功能键行（带有触控 ID），取代了 Touch Bar，提升工作效率与精准度。</li></ul><h4 style=\"text-align: start;\"><strong>专业级影音与续航</strong></h4><ul><li style=\"text-align: start;\">录音棚级麦克风：三麦克风阵列带来清晰的语音捕捉和低噪录音效果。</li><li style=\"text-align: start;\">高保真六扬声器系统：支持空间音频，低音浑厚，高音清澈，音场宽广。</li><li style=\"text-align: start;\">持久的电池续航：在 Apple Silicon 惊人能效的加持下，最高可达 18 小时的视频播放时间，全天候工作无需寻找电源。</li></ul><h4 style=\"text-align: start;\"><strong>为谁而生？</strong></h4><ul><li style=\"text-align: start;\">创意专业人士：视频剪辑师（Final Cut Pro）、摄影师（Lightroom）、音乐制作人（Logic Pro）、3D 艺术家。</li><li style=\"text-align: start;\">软件开发与工程师：需要运行虚拟机、编译大型代码或进行数据科学分析。</li><li style=\"text-align: start;\">科研与学术工作者：处理复杂计算、模拟和数据分析。</li><li style=\"text-align: start;\">所有追求“顶级体验”的用户：希望获得一台在性能、屏幕、续航、设计上无短板，能应对未来多年挑战的笔记本电脑。</li></ul>',1,0),(10,4,2,'戴尔XPS 15',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144cf6ceadbf16f068189.jpg',13999.99,10,'15.6英寸4K OLED触控屏，视觉震撼。英特尔第13代酷睿i7，性能卓越。','<p><strong>戴尔XPS 15：创作者的梦想本</strong></p><p>NVIDIA RTX 4060独显，32GB内存。86Wh大电池，支持130W快充。CNC铝合金机身，质感出众。</p>',1,0),(11,1,2,'华硕灵耀14',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141f76ceadbf16f068173.jpg',5999.99,15,'二手笔记本，性能依旧强劲。14英寸2.8K OLED屏，轻薄便携。','<p><strong>华硕灵耀14：高性价比轻薄本</strong></p><p>95成新，原装配件齐全。AMD锐龙7处理器，16GB内存。75Wh电池，续航可达12小时。</p>',1,1),(12,5,3,'华为MatePad Pro 13.2',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145916ceadbf16f068192.jpg',5999.99,18,'13.2英寸柔光护眼屏，办公娱乐两不误。HarmonyOS系统，多设备协同。','<p><strong>MatePad Pro 13.2：大屏生产力工具</strong></p><p>2.8K OLED显示屏，支持144Hz高刷。10050mAh大电池，88W快充。支持第三代M-Pencil手写笔。</p>',1,0),(13,3,3,'苹果iPad Pro 12.9',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143fa6ceadbf16f06817f.jpg',8999.99,15,'M2芯片，性能强大。Liquid Retina XDR显示屏，专业创作首选。','<p><strong>iPad Pro 12.9：平板中的性能怪兽</strong></p><p>M2芯片，8核CPU+10核GPU。支持ProMotion自适应刷新率。兼容Apple Pencil和妙控键盘。</p>',1,1),(14,1,3,'小米平板6 Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141d36ceadbf16f068172.jpg',3299.99,25,'12.4英寸3K分辨率屏幕，骁龙8+旗舰芯片。性价比之王。','<p><strong>小米平板6 Pro：娱乐影音神器</strong></p><p>144Hz高刷屏，支持杜比视界。8600mAh大电池，67W快充。四扬声器立体声，影音体验出色。</p>',1,0),(15,1,3,'三星Galaxy Tab S9',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141bf6ceadbf16f068171.jpg',4999.99,20,'11英寸Dynamic AMOLED 2X屏幕，120Hz刷新率。骁龙8 Gen 2处理器。','<p><strong>Galaxy Tab S9：Android平板旗舰</strong></p><p>支持S Pen手写笔，IP68防水防尘。8000mAh电池，45W快充。DeX模式，秒变生产力工具。</p>',1,0),(16,8,3,'荣耀平板V8 Pro',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147286ceadbf16f0681a7.webp',1999.99,30,'二手平板，外观完好。12.1英寸2.5K屏幕，适合影音娱乐。','<p><strong>荣耀平板V8 Pro：高性价比之选</strong></p><p>9成新，功能正常。天玑8100处理器，8GB内存。支持魔法键盘，办公更高效。</p>',1,0),(17,1,4,'华为Sound X',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141a36ceadbf16f068170.webp',1999.99,35,'帝瓦雷联合设计，双低音炮震撼音效。支持一碰传音，智能家居中枢。','<p><strong>华为Sound X：智能音响旗舰</strong></p><p>60W双低音炮，下潜深度达40Hz。支持HWA高清音质、空间音频。语音助手，智能家居控制。</p>',1,0),(18,4,4,'苹果HomePod 2',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144c06ceadbf16f068188.webp',2299.99,28,'S7芯片，空间音频。Siri语音助手，HomeKit智能家居中枢。','<p><strong>HomePod 2：苹果智能音响</strong></p><p>5个高音单元，4英寸低音单元。支持空间音频、Dolby Atmos。环境感应，自动调节音效。</p>',1,0),(19,1,4,'小米Sound Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141856ceadbf16f06816f.jpg',599.99,40,'360度环绕立体声，小爱同学智能语音。性价比智能音响。','<p><strong>小米Sound Pro：智能音响之选</strong></p><p>支持Hi-Res音质认证，蓝牙5.2。小爱同学语音控制，米家智能联动。12小时续航。</p>',1,0),(20,5,4,'SoundLink Revolve+',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145806ceadbf16f068191.jpg',1999.99,32,'360度全方位音效，IPX4防水。便携蓝牙音响，户外音乐伴侣。','<p><strong>Bose SoundLink Revolve+：便携音响专家</strong></p><p>16小时续航，支持NFC快速配对。可充当充电宝使用。音质饱满，低音强劲。</p>',1,0),(21,2,4,'JBL Flip 6',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931433c6ceadbf16f068176.jpg',399.99,45,'二手便携音响，音质出色。IP67防水防尘，户外使用无忧。','<p><strong>JBL Flip 6：实惠便携音响</strong></p><p>95成新，配件齐全。12小时续航，支持PartyBoost多台串联。音质清晰，低音有力。</p>',1,1),(22,1,5,'索尼Alpha 7R V',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141756ceadbf16f06816e.jpg',25999.99,5,'6100万像素全画幅传感器，专业摄影师首选。8K视频录制。','<p><strong>索尼Alpha 7R V：专业摄影利器</strong></p><p>AI智能对焦系统，人眼/动物眼识别。8档防抖，手持也能拍清晰。支持CFexpress Type A卡，读写速度快。</p>',1,0),(23,5,5,'佳能EOS R5',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693145536ceadbf16f068190.webp',24999.99,6,'4500万像素，8K 30fps视频录制。机身防抖，专业视频利器。','<p><br></p><h2 style=\"text-align: start;\"><strong>佳能EOS R5：定义全画幅微单新高度的全能旗舰</strong></h2><p style=\"text-align: start;\">佳能EOS R5于2020年7月发布，它不仅仅是一次常规的机型迭代，更是佳能影像技术的一次“秀肌肉”式爆发。它首次将高像素、高速连拍、8K视频与强悍的机身防抖集于一身，一举确立了全画幅无反相机市场的全能旗舰标杆，其影响力延续至今。</p><h3 style=\"text-align: start;\"><strong>核心亮点：三大颠覆性突破</strong></h3><ol><li style=\"text-align: start;\">开创性的8K视频能力： EOS R5是全球首款支持内录8K RAW视频的无反相机（最高29.97fps）。这使其在专业视频制作领域实现了“越级”挑战，即使不常用8K，其超采样的4K画质也极为出色。</li><li style=\"text-align: start;\">高像素与高速度的完美结合： 它搭载了约4500万有效像素的全画幅CMOS图像传感器，同时支持最高20张/秒（电子快门） 和12张/秒（机械快门） 的超高速连拍。这意味着它既能满足商业静物、风光摄影的高细节需求，也能胜任野生动物、体育摄影的抓拍挑战。</li><li style=\"text-align: start;\">革命性的机身防抖系统： 首次引入机身5轴防抖，并与兼容的RF镜头防抖联动，最高可实现惊人的8级防抖补偿效果，极大提升了手持拍摄照片和视频的稳定性和成功率。</li></ol><h3 style=\"text-align: start;\"><strong>详细技术规格与性能</strong></h3><p style=\"text-align: start;\"><strong>一、 成像系统</strong></p><ul><li style=\"text-align: start;\">传感器： 自主研发的约4500万像素全画幅CMOS，细节表现力惊人，为后期裁剪和二次构图提供巨大空间。</li><li style=\"text-align: start;\">图像处理器： DIGIC X图像处理器，提供强大的数据处理能力，支持高性能连拍、高规格视频和先进的自动对焦算法。</li><li style=\"text-align: start;\">常用感光度： ISO 100-51200（可扩展至ISO 50-102400），高感光度下画质纯净度优秀。</li></ul><p style=\"text-align: start;\"><strong>二、 自动对焦与连拍</strong></p><ul><li style=\"text-align: start;\">对焦系统： 采用第二代全像素双核CMOS AF II技术，对焦点覆盖100% x 100%的画面区域。支持人物/动物（鸟、猫、狗）眼部和头部检测追踪对焦，对焦快、准、稳。</li><li style=\"text-align: start;\">连拍性能： 在自动对焦/自动曝光追踪下，支持最高20张/秒（电子快门）或12张/秒（机械快门）的连拍。配合大缓存，可持续拍摄大量RAW格式照片。</li></ul><p style=\"text-align: start;\"><strong>三、 视频性能</strong></p><ul><li style=\"text-align: start;\">8K RAW： 内录最高8K 30p 12bit RAW视频（需使用CFexpress B型卡）。</li><li style=\"text-align: start;\">4K高画质： 提供无裁切的4K 120p高帧率拍摄，以及超采样的4K 30p/60p，画质极其锐利。</li><li style=\"text-align: start;\">专业格式： 支持Canon Log、HDR PQ等，为专业后期调色提供空间。</li><li style=\"text-align: start;\">过热控制： 初代固件在录制高规格视频时存在过热限制，但后续固件更新显著改善了录制时长和过热警告逻辑。</li></ul><p style=\"text-align: start;\"><strong>四、 机身设计与操控</strong></p><ul><li style=\"text-align: start;\">防抖： 机身5轴防抖，与镜头防抖协同工作，最高8级效果。</li><li style=\"text-align: start;\">取景器与屏幕： 576万点OLED电子取景器，刷新率高，无拖影。3.2英寸210万点可侧翻触摸屏，操控直观。</li><li style=\"text-align: start;\">存储与连接： 双卡槽设计（CFexpress B型 + SD UHS-II），兼顾高性能与兼容性。具备全功能Type-C、HDMI、耳机和麦克风接口，以及Wi-Fi和蓝牙。</li><li style=\"text-align: start;\">机身防护： 采用镁合金材质，具备防尘防水滴性能，可靠性高。</li></ul><h3 style=\"text-align: start;\"><strong>目标用户</strong></h3><p style=\"text-align: start;\">EOS R5是一台真正的“六边形战士”，适合对画质、速度、视频均有高要求的专业摄影师和高端摄影爱好者：</p><ul><li style=\"text-align: start;\">商业与风光摄影师： 依赖4500万高像素输出巨幅高清图像。</li><li style=\"text-align: start;\">野生动物与体育摄影师： 需要高速连拍与精准的动物眼部对焦。</li><li style=\"text-align: start;\">专业视频创作者/小型工作室： 追求8K RAW工作流或超高质量4K内容。</li><li style=\"text-align: start;\">婚礼/活动摄影师： 需要一台能同时兼顾高画质照片和高质量视频记录的主力机。</li></ul><h3 style=\"text-align: start;\"><strong>历史意义与市场地位</strong></h3><p style=\"text-align: start;\">EOS R5的发布，标志着佳能在全画幅无反市场发起了强有力的技术攻势。它成功打破了当时“高像素机速度慢，速度机像素低”的格局，并以8K内录震撼了业界。尽管发布初期有关于视频过热的讨论，但它无疑极大地推动了整个行业的技术竞赛，迫使竞争对手加快创新步伐。时至今日，EOS R5仍是市场上最均衡、最强大的全能型全画幅相机之一。</p>',1,0),(24,1,5,'尼康Z9',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141536ceadbf16f06816d.jpg',32999.99,4,'旗舰级微单，4571万像素堆栈式传感器。120fps高速连拍。','<p><strong>尼康Z9：运动摄影之王</strong></p><p>无黑屏拍摄，8K 60fps视频。支持鸟类识别对焦。双CFexpress卡槽，专业可靠。</p>',1,1),(25,4,5,'富士X-T5',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693144ac6ceadbf16f068187.webp',11999.99,8,'4020万像素X-Trans传感器，胶片模拟色彩。复古外观设计。','<p><strong>富士X-T5：文艺摄影师之选</strong></p><p>19种胶片模拟模式，直出即大片。机身防抖，最高7档补偿。6.2K 30fps视频录制。</p>',1,0),(26,1,5,'佳能EOS R6 Mark II',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141336ceadbf16f06816c.jpg',13999.99,10,'二手全画幅微单，成色良好。2420万像素，40fps高速连拍。','<p><strong>佳能EOS R6 Mark II：性价比全画幅</strong></p><p>9成新，快门次数少于5000次。机身防抖，6K超采样4K视频。双卡槽设计。</p>',1,0),(27,10,6,'戴尔U2723DE',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693148226ceadbf16f0681b5.webp',3999.99,15,'27英寸4K IPS屏幕，99% sRGB色域。专业设计师首选。','<p><strong>戴尔U2723DE：专业设计显示器</strong></p><p>出厂校色，Delta E<2。支持DP、HDMI、USB-C 90W供电。升降旋转支架，人体工学设计。</p>',1,0),(28,1,6,'LG 27GP950',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141166ceadbf16f06816b.jpg',5999.99,12,'27英寸4K 160Hz电竞屏，1ms响应时间。支持G-Sync和FreeSync。','<p><strong>LG 27GP950：4K电竞显示器</strong></p><p>Nano IPS面板，98% DCI-P3色域。支持HDR600，画面细节丰富。HDMI 2.1接口，适配PS5/Xbox。</p>',1,0),(29,9,6,'明基SW270C',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147ba6ceadbf16f0681af.webp',4599.99,10,'27英寸2K专业摄影显示器，99% Adobe RGB色域。硬件校色。','<p><strong>明基SW270C：摄影师专业显示器</strong></p><p>出厂校色报告，Delta E≤2。支持黑白模式，16bit 3D LUT。USB-C 60W供电，一线连接。</p>',1,1),(30,1,6,'华硕ROG PG27AQDM',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693141076ceadbf16f06816a.jpg',7999.99,8,'27英寸2K OLED电竞屏，240Hz刷新率。0.03ms响应时间。','<p><strong>ROG PG27AQDM：OLED电竞旗舰</strong></p><p>自发光OLED面板，99% DCI-P3色域。支持G-Sync Ultimate，画面流畅无撕裂。定制散热系统。</p>',1,0),(31,3,6,'三星S27A600U',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143e96ceadbf16f06817e.jpg',1299.99,20,'二手27英寸显示器，外观完好。2K分辨率，75Hz刷新率。','<p><strong>三星S27A600U：实用办公显示器</strong></p><p>95成新，无坏点。IPS面板，广视角。支持壁挂，升降支架。适合办公和娱乐。</p>',1,1),(32,8,7,'大疆DJI Air 3',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693147186ceadbf16f0681a6.webp',6999.99,12,'双主摄像头，4800万像素广角+4800万像素中长焦。46分钟续航。','<p><strong>DJI Air 3：航拍利器</strong></p><p>支持4K 100fps HDR视频录制。全向避障系统，飞行更安全。最远图传15公里，信号稳定。</p>',1,1),(33,1,7,'大疆DJI Mini 3 Pro',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693140e66ceadbf16f068169.jpg',4999.99,15,'轻量化设计，重量仅249g。4800万像素相机，支持竖拍。','<p><br></p><p style=\"text-align: start;\"><strong>DJI Mini 3 Pro：小身材，巨能飞，专业影像触手可及</strong></p><p style=\"text-align: start;\">当轻巧便携遇见专业性能，灵感便不再受限于地平线。DJI Mini 3 Pro，重新定义入门级航拍标杆，将顶尖科技浓缩于不足249克的机身之中，让你轻松携带广阔世界，自由捕捉每一刻高光。</p><h3 style=\"text-align: start;\">核心亮点</h3><p style=\"text-align: start;\"><strong>一、轻于249克，免注册畅飞（部分国家/地区）</strong><br>极致轻巧，无需复杂注册备案，装入随身包袋即出发。旅行、徒步、探险，它都是你毫无负担的航拍伙伴，随时准备升空。</p><p style=\"text-align: start;\"><strong>二、专业级影像系统，画质大飞跃</strong></p><ul><li style=\"text-align: start;\">1/1.3英寸大底传感器： 媲美高端机型，带来更出色的感光能力，细节丰富，夜景纯净。</li><li style=\"text-align: start;\">f/1.7超大光圈： 进光量显著提升，昼夜拍摄皆出色，时刻保证画面明亮清晰。</li><li style=\"text-align: start;\">TrueSync 双原生ISO： 智能优化画质，有效抑制噪点，动态范围宽广。</li><li style=\"text-align: start;\">4K/60fps 超清视频 &amp; 4800万像素RAW照片： 记录流畅细腻的动态影像，保留海量后期空间。</li></ul><p style=\"text-align: start;\"><strong>三、三向全景避障，安全随心飞</strong></p><ul><li style=\"text-align: start;\">前、后、下三向环境感知： 主动识别障碍物，大幅提升飞行安全性。</li><li style=\"text-align: start;\">APAS 4.0（高级飞行员辅助系统）： 复杂环境下也能智能绕行，让你更专注于构图与创作。</li></ul><p style=\"text-align: start;\"><strong>四、革新构图视角，发现新世界</strong></p><ul><li style=\"text-align: start;\">无损竖拍模式： 传感器可物理旋转，直接拍摄高质量竖屏视频与照片，完美适配短视频平台与移动端观看。</li><li style=\"text-align: start;\">大角度仰拍： 云台仰角达60°，轻松获得独特低空仰视视角，创作更具冲击力的画面。</li></ul><p style=\"text-align: start;\"><strong>五、超长续航，尽兴创作</strong></p><ul><li style=\"text-align: start;\">标准电池： 续航长达34分钟，满足日常创作。</li><li style=\"text-align: start;\">长续航电池（可选）： 续航延展至惊人的47分钟，一次飞行，收获加倍。</li></ul><p style=\"text-align: start;\"><strong>六、智能功能，轻松出大片</strong></p><ul><li style=\"text-align: start;\">大师镜头： 一键自动完成复杂运镜、剪辑与配乐，生成高质量短片。</li><li style=\"text-align: start;\">焦点跟随（聚焦2.0/智能跟随4.0）： 牢牢锁定主角，复杂场景也能精准跟踪。</li><li style=\"text-align: start;\">延时摄影： 轻松创作日转夜、车流光影等震撼延时作品。</li></ul><h3 style=\"text-align: start;\">为谁而生？</h3><ul><li style=\"text-align: start;\">旅行与生活记录者： 轻装出行，随手记录壮丽山河与温馨时刻。</li><li style=\"text-align: start;\">内容创作者与Vlogger： 竖拍直出、智能跟拍，高效产出高品质社交内容。</li><li style=\"text-align: start;\">航拍新手与爱好者： 安全易上手，性能强大，是开启天空视界的第一台理想无人机。</li></ul><hr/><p><br></p><p style=\"text-align: start;\"><strong>DJI Mini 3 Pro，不仅仅是一台“迷你”无人机。它是一次影像能力的越级，一次创作自由的解放。以轻巧之躯，承载专业之魂，让你的视野超越想象，让每一个灵感瞬间，都能被清晰看见、动人呈现。</strong></p><p style=\"text-align: start;\"><strong>即刻升空，见所未见。</strong></p>',1,0),(34,9,7,'大疆Mavic 3 Classic',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/6931479e6ceadbf16f0681ae.webp',10999.99,8,'哈苏相机，2000万像素4/3 CMOS传感器。46分钟续航。','<p><strong>Mavic 3 Classic：影像旗舰无人机</strong></p><p>支持5.1K 50fps视频录制，10bit D-Log色彩模式。全向避障，最远图传15公里。</p>',1,0),(35,1,7,'道通EVO Nano+',0,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693140d66ceadbf16f068168.jpg',3999.99,10,'249g轻量机身，5000万像素1/1.28英寸传感器。28分钟续航。','<p><strong>道通EVO Nano+：性价比航拍无人机</strong></p><p>支持4K 30fps HDR视频。三向避障系统。支持一键短片，智能拍摄。</p>',1,0),(36,2,7,'大疆DJI Mini 2',1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/693143236ceadbf16f068175.jpg',1999.99,18,'二手无人机，飞行正常。1200万像素相机，4K 30fps视频。','<p><strong>DJI Mini 2：入门航拍推荐</strong></p><p>9成新，原装配件齐全。重量249g，续航31分钟。10公里图传距离，信号稳定。</p>',1,1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_orders`
--

DROP TABLE IF EXISTS `product_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_orders` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` bigint NOT NULL COMMENT '订单号',
  `order_status` tinyint DEFAULT NULL COMMENT '订单状态：0-待付款，1-待接单，2-派送中，3-已送达，4-已完成，5-已取消',
  `order_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `user_id` int unsigned NOT NULL COMMENT '关联用户id',
  `shop_id` int unsigned NOT NULL COMMENT '关联店铺id',
  `product_id` int unsigned NOT NULL COMMENT '关联产品id',
  `product_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品名称，冗余',
  `product_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品图片，冗余',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '产品价格，冗余',
  `address_id` int unsigned NOT NULL COMMENT '关联地址id',
  `consignee` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人，冗余',
  `phone_number` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货电话，冗余',
  `province_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省级区划编号，冗余',
  `city_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市级区划编号，冗余',
  `district_code` char(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区级区划编号，冗余',
  `detail_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址，冗余',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码产品订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_orders`
--

LOCK TABLES `product_orders` WRITE;
/*!40000 ALTER TABLE `product_orders` DISABLE KEYS */;
INSERT INTO `product_orders` VALUES (1,125325393350623233,1,'2025-12-04 17:26:33',1,1,1,'华为Mate 70','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/6930527f6cea9230b8e14705.jpg',8999.99,1,'张三','13600000000','21','2104','210411','张三的家');
/*!40000 ALTER TABLE `product_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_type`
--

DROP TABLE IF EXISTS `product_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_type` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数码产品类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_type`
--

LOCK TABLES `product_type` WRITE;
/*!40000 ALTER TABLE `product_type` DISABLE KEYS */;
INSERT INTO `product_type` VALUES (3,'平板'),(1,'手机'),(7,'无人机'),(6,'显示屏'),(5,'相机'),(2,'笔记本'),(4,'音响');
/*!40000 ALTER TABLE `product_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slideshow`
--

DROP TABLE IF EXISTS `slideshow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slideshow` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int unsigned NOT NULL COMMENT '关联数码产品id',
  `shop_id` int unsigned NOT NULL COMMENT '关联店铺id',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '轮播展示图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slideshow`
--

LOCK TABLES `slideshow` WRITE;
/*!40000 ALTER TABLE `slideshow` DISABLE KEYS */;
INSERT INTO `slideshow` VALUES (2,33,1,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/69314af76ceadbf16f0681bb.png'),(3,3,2,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/69314b886ceadbf16f0681bc.jpg'),(4,23,5,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/69314be36ceadbf16f0681bd.jpg'),(5,9,3,'https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/04/69314c326ceadbf16f0681be.jpg');
/*!40000 ALTER TABLE `slideshow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `username` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` tinyint NOT NULL COMMENT '角色：0-管理员，1-店家，2-用户',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(254) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'用户1','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69303d266cea9230b8e14700.jpg','uuser1','afdd0b4ad2ec172c586e2150770fbf9e',2,'13680000001','uuser1@qq.com',1422.03,'2025-12-03 21:29:56','2025-12-04 17:34:57',0),(2,'用户2','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69304e886cea9230b8e14703.jpg','uuser2','afdd0b4ad2ec172c586e2150770fbf9e',2,'13600000003','uuser2@qq.com',0.00,'2025-12-03 22:52:06','2025-12-03 22:52:06',0),(3,'用户3','https://digital-product.oss-cn-beijing.aliyuncs.com/images/2025/12/03/69304ea76cea9230b8e14704.jpg','uuser3','afdd0b4ad2ec172c586e2150770fbf9e',2,'13680000004','uuser3@qq.com',0.00,'2025-12-03 22:52:38','2025-12-03 22:52:38',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-04 19:06:02
