-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: personal_cloud
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `file_derivative`
--

DROP TABLE IF EXISTS `file_derivative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_derivative` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `origin_file_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `size` int DEFAULT NULL,
  `s3_path` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,
  `format` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_derivative`
--

LOCK TABLES `file_derivative` WRITE;
/*!40000 ALTER TABLE `file_derivative` DISABLE KEYS */;
INSERT INTO `file_derivative` VALUES (1,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',200,'thumbnails/200.jpg','jpg','2025-07-04 15:54:04'),(2,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',400,'thumbnails/400.jpg','jpg','2025-07-04 15:54:05'),(3,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',800,'thumbnails/800.jpg','jpg','2025-07-04 15:54:06'),(4,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',200,'thumbnails/200.jpg','jpg','2025-07-04 15:57:26'),(5,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',400,'thumbnails/400.jpg','jpg','2025-07-04 15:57:42'),(6,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',800,'thumbnails/800.jpg','jpg','2025-07-04 15:58:25'),(7,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',200,'thumbnails/200.jpg','jpg','2025-07-04 16:01:05'),(8,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Sjpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083125Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=dd53296438ff38bc9e08b141b85cfffed5811c0114a8d63e2ee6ffc7795c90fa','jpg','2025-07-04 16:31:26'),(9,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Ljpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083125Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=5ea14f8a15034156ffaa2aa342621037a4a420cf2a9290c75189457afab0d7e2','jpg','2025-07-04 16:31:27'),(10,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Mjpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083125Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=9c98e54d308b5d8dd84b8678c8c0fd405f8a08f881c495d561068b659fffa2ed','jpg','2025-07-04 16:31:27'),(11,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Sjpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083224Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=a8548ce1913eac1cd1b57e58f8d6ca6d32d91a53b140d93d12f89621436b25a5','jpg','2025-07-04 16:32:41'),(12,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Ljpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083224Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=61bd27ed5affb859d297b350f5ae55a8b84ea20329fc3e72aa924b96afe8759f','jpg','2025-07-04 16:32:45'),(13,'7cbe5244-6730-4efa-9d0a-25220af43156.png','thumbnail',NULL,'http://127.0.0.1:9000/personal/7cbe5244-6730-4efa-9d0a-25220af43156-568b34240e26b9a8391350a5a81aaf50-thumb-Mjpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250704T083224Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=minio%2F20250704%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=2d9676489c34f5ed438c7615bbe35d22c4635c70236caaff21bd016583fd4603','jpg','2025-07-04 16:32:45');
/*!40000 ALTER TABLE `file_derivative` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `file_hash` varchar(32) NOT NULL COMMENT '文件hash',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-初始化，2完成，3失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `system_status` int DEFAULT '0' COMMENT '0不是系统目录，1是系统目录',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否为隐藏目录',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_file_hash` (`file_hash`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_info`
--

LOCK TABLES `file_info` WRITE;
/*!40000 ALTER TABLE `file_info` DISABLE KEYS */;
INSERT INTO `file_info` VALUES (13,'我的应用收藏','/','directory',0,'',10,1,'2025-06-24 16:45:21','2025-06-24 16:45:21',1,0),(14,'我的应用收藏','/','directory',0,'',11,1,'2025-06-24 16:51:45','2025-06-24 16:51:45',1,0),(15,'jdk17u-jdk-17.0.16-6.tar.gz','/','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,1,'2025-06-26 11:58:50','2025-06-26 11:58:50',NULL,0),(16,'VSCodium-x64-1.101.03933.msi','/','',131317760,'1956cda5754b30eea9c7454fb48ad6a6',11,1,'2025-06-26 14:20:46','2025-06-26 14:20:46',NULL,0),(17,'1x09 - The One Where Underdog Gets Away (1).mkv','/','video/x-matroska',240545506,'674c84f7e99064b5de5183795712c281',11,1,'2025-06-26 14:21:08','2025-06-26 14:21:08',NULL,0),(18,'jdk17u-jdk-17.0.16-6.tar.gz','/','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,2,'2025-06-26 14:27:43','2025-06-26 14:27:47',NULL,0),(19,'the-game-of-life-and-how-to-play-it-csl-asheville.pdf','/','application/pdf',277710,'998ed4c966f7d0e4cfaa12b3b68dfc2f',11,2,'2025-06-26 16:59:26','2025-06-26 16:59:26',NULL,0),(20,'Untitled-2.ini','/','',1567,'7fc7ddb3571bf668e6f718e65c3bdeae',11,2,'2025-06-26 17:08:25','2025-06-26 17:08:25',NULL,0),(21,'the-game-of-life-and-how-to-play-it-csl-asheville.pdf998ed4c966f7d0e4cfaa12b3b68dfc2f','/','application/pdf',277710,'998ed4c966f7d0e4cfaa12b3b68dfc2f',11,2,'2025-06-26 17:23:46','2025-06-26 17:23:46',NULL,0),(22,'丁总.txt','/','text/plain',426555,'99a36a9601955462e66dc69c78e78d60',11,2,'2025-06-26 17:28:51','2025-06-26 17:28:51',NULL,0),(23,'视频合集V1.0.5版本测试报告.doc','/','application/msword',121054,'e1a8d54f48d0b1154d536735eb1519cb',11,2,'2025-06-26 17:31:09','2025-06-26 17:31:10',NULL,0),(24,'视频合集V1.0.2版本测试报告.doc','/','application/msword',122510,'defe93666883818ab885e59225bc6bdd',11,0,'2025-06-26 17:32:12','2025-06-27 15:30:30',NULL,0),(25,'视频合集V1.0.2版本测试报告.doc','/','application/msword',122510,'defe93666883818ab885e59225bc6bdd',11,0,'2025-06-26 17:32:56','2025-06-27 15:30:24',NULL,0),(26,'Untitled-3.txt','/','text/plain',95,'f022ba8fcb18f6503934bf77e3d4a692',11,0,'2025-06-26 17:37:48','2025-06-27 15:26:56',NULL,0),(27,'231','/','folder',0,'',11,0,'2025-06-27 14:25:10','2025-06-27 15:30:36',NULL,0),(28,'版权资源问题.txt','/231/','text/plain',4610,'5e780f87151ed9ec03e227969a31e680',11,2,'2025-06-27 14:27:03','2025-06-27 14:27:03',NULL,0),(29,'123','27','folder',0,'',11,2,'2025-06-27 14:37:56','2025-06-27 14:37:56',NULL,0),(30,'Untitled-2.ini','29','',1567,'7fc7ddb3571bf668e6f718e65c3bdeae',11,2,'2025-06-27 14:38:33','2025-06-27 14:38:33',NULL,0),(31,'123','/231/','directory',0,'',11,2,'2025-06-27 15:31:17','2025-06-27 15:31:17',NULL,0),(32,'123','/231/','directory',0,'',11,2,'2025-06-27 15:31:38','2025-06-27 15:31:38',NULL,0),(33,'123','/','directory',0,'',11,0,'2025-06-27 15:35:50','2025-06-27 15:37:34',NULL,0),(34,'12344','/','directory',0,'',11,0,'2025-06-27 15:37:39','2025-06-27 15:57:22',NULL,0),(35,'12314','34','directory',0,'',11,0,'2025-06-27 15:37:47','2025-06-27 15:57:20',NULL,0),(36,'目录判断','34','directory',0,'',11,0,'2025-06-27 15:37:59','2025-06-27 15:57:22',NULL,0),(37,'视频合集V1.0.2版本测试报告.doc','36','application/msword',122510,'defe93666883818ab885e59225bc6bdd',11,0,'2025-06-27 15:38:06','2025-06-27 15:57:21',NULL,0),(38,'12344','/','directory',0,'',11,0,'2025-06-27 18:12:15','2025-07-02 10:17:32',NULL,0),(39,'12344','/','directory',0,'',11,0,'2025-06-27 18:12:16','2025-06-27 18:15:20',NULL,0),(40,'the-game-of-life-and-how-to-play-it-csl-asheville.pdf','39','application/pdf',277710,'998ed4c966f7d0e4cfaa12b3b68dfc2f',11,0,'2025-06-27 18:12:25','2025-06-27 18:15:20',NULL,0),(41,'1x20 - The One With The Evil Orthodontist.mkv','/','video/x-matroska',227578934,'3a86dcf1f8ec6af15f9643f24635294b',11,1,'2025-06-30 10:22:47','2025-06-30 10:22:47',NULL,0),(42,'22','/','directory',0,'',11,2,'2025-06-30 10:23:12','2025-06-30 10:23:12',NULL,0),(43,'jdk17u-jdk-17.0.16-6.tar.gz','42','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,1,'2025-06-30 10:23:20','2025-06-30 10:23:20',NULL,0),(44,'jdk17u-jdk-17.0.16-6.tar.gz','42','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,2,'2025-06-30 10:24:18','2025-06-30 10:24:22',NULL,0),(45,'我的应用收藏','/','directory',0,'',12,2,'2025-06-30 17:54:45','2025-06-30 17:54:45',1,0),(46,'我的应用收藏','/','directory',0,'',13,2,'2025-07-01 17:50:56','2025-07-01 17:50:56',1,0),(47,'我的应用收藏','/','directory',0,'',14,2,'2025-07-01 17:51:09','2025-07-01 17:51:09',1,0),(48,'canal.deployer-1.1.8.tar.gz','38','application/x-gzip',116717548,'c4c331f1d8ca9eb3707ea32fd62e51b4',13,0,'2025-07-02 08:52:13','2025-07-02 08:56:05',NULL,0),(49,'123','38','directory',0,'',11,0,'2025-07-02 08:58:14','2025-07-02 10:17:32',NULL,0),(50,'2222','/','directory',0,'',11,2,'2025-07-02 09:31:50','2025-07-02 09:31:50',NULL,0),(51,'1x24 - The One Where Rachel Finds Out.mkv','/','video/x-matroska',243644371,'434b452656ab50036e4bb687ba4c2a26',11,2,'2025-07-02 11:30:03','2025-07-02 11:30:12',NULL,0),(52,'jdk17u-jdk-17.0.16-6.tar.gz','/','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,2,'2025-07-02 14:12:30','2025-07-02 14:12:34',NULL,0),(53,'jdk17u-jdk-17.0.16-6.tar.gz','/','application/x-gzip',107859755,'8d6a598ee4f03c1291058a9b9b0cadd9',11,2,'2025-07-02 14:26:23','2025-07-02 14:26:27',NULL,0),(54,'12345','/','directory',0,'',11,0,'2025-07-02 17:53:06','2025-07-02 17:59:05',NULL,0),(55,'canal.deployer-1.1.8.tar.gz','54','application/x-gzip',116717548,'c4c331f1d8ca9eb3707ea32fd62e51b4',11,0,'2025-07-02 17:53:13','2025-07-02 17:59:05',NULL,0),(56,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:26:06','2025-07-03 16:26:06',NULL,0),(57,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,1,'2025-07-03 16:30:13','2025-07-03 16:30:13',NULL,0),(58,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:31:08','2025-07-03 16:31:08',NULL,0),(59,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:33:49','2025-07-03 16:33:49',NULL,0),(60,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:34:22','2025-07-03 16:34:22',NULL,0),(61,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:40:54','2025-07-03 16:40:54',NULL,0),(62,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 16:45:00','2025-07-03 16:45:00',NULL,0),(63,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,2,'2025-07-03 16:49:29','2025-07-03 16:49:29',NULL,0),(64,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-03 17:40:36','2025-07-03 17:50:28',NULL,0),(65,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,2,'2025-07-03 17:41:02','2025-07-03 17:41:02',NULL,0),(66,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-03 17:50:33','2025-07-03 17:50:34',NULL,0),(67,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-03 17:54:57','2025-07-03 18:10:44',NULL,0),(68,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,2,'2025-07-03 17:55:46','2025-07-03 17:55:46',NULL,0),(69,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,2,'2025-07-03 17:57:00','2025-07-03 17:57:00',NULL,0),(70,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-03 17:59:24','2025-07-03 18:10:42',NULL,0),(71,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-03 18:10:49','2025-07-04 11:21:08',NULL,0),(72,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-03 18:14:12','2025-07-04 11:21:06',NULL,0),(73,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,0,'2025-07-04 10:30:25','2025-07-04 11:21:05',NULL,0),(74,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-04 10:56:02','2025-07-04 11:21:03',NULL,0),(75,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,0,'2025-07-04 10:58:05','2025-07-04 11:21:02',NULL,0),(76,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,0,'2025-07-04 11:00:15','2025-07-04 11:21:00',NULL,0),(77,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-04 11:06:52','2025-07-04 11:20:59',NULL,0),(78,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,0,'2025-07-04 11:07:21','2025-07-04 11:20:56',NULL,0),(79,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,0,'2025-07-04 11:16:01','2025-07-04 11:20:55',NULL,0),(80,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,1,'2025-07-04 11:21:56','2025-07-04 11:21:56',NULL,0),(81,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,1,'2025-07-04 12:00:54','2025-07-04 12:00:54',NULL,0),(82,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,1,'2025-07-04 14:57:07','2025-07-04 14:57:07',NULL,0),(83,'部署架构图.png','/','image/png',354497,'7258ea7e09f312a3ad6b3092cccfaf94',11,1,'2025-07-04 15:23:11','2025-07-04 15:23:11',NULL,0),(84,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:34:33','2025-07-04 15:34:33',NULL,0),(85,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:36:45','2025-07-04 15:36:46',NULL,0),(86,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:46:51','2025-07-04 15:46:52',NULL,0),(87,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:50:58','2025-07-04 15:50:58',NULL,0),(88,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:51:22','2025-07-04 15:51:22',NULL,0),(89,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:52:21','2025-07-04 15:52:22',NULL,0),(90,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:53:42','2025-07-04 15:53:43',NULL,0),(91,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:56:49','2025-07-04 15:56:50',NULL,0),(92,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 15:59:17','2025-07-04 15:59:17',NULL,0),(93,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 16:30:43','2025-07-04 16:30:43',NULL,0),(94,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 16:31:24','2025-07-04 16:31:25',NULL,0),(95,'7cbe5244-6730-4efa-9d0a-25220af43156.png','/','image/png',173973,'568b34240e26b9a8391350a5a81aaf50',11,2,'2025-07-04 16:32:24','2025-07-04 16:32:24',NULL,0);
/*!40000 ALTER TABLE `file_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share`
--

DROP TABLE IF EXISTS `share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `share_url` varchar(255) NOT NULL COMMENT '分享链接',
  `extract_code` varchar(10) DEFAULT NULL COMMENT '提取码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `user_id` bigint NOT NULL COMMENT '分享人ID',
  `encrypt` tinyint NOT NULL DEFAULT '0' COMMENT '是否加密 0-否 1-是',
  `pub_type` tinyint NOT NULL DEFAULT '1' COMMENT '公开类型 1-所有人 2-好友',
  `period` int DEFAULT NULL COMMENT '有效期长度',
  `period_unit` tinyint NOT NULL DEFAULT '1' COMMENT '有效期单位 1-天',
  `passwd` varchar(20) DEFAULT NULL COMMENT '分享密码（明文）',
  `link_id` varchar(64) NOT NULL COMMENT '外链ID',
  `link_url` varchar(255) NOT NULL COMMENT '外链URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-失效，1-有效',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_link_id` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件/目录/标签分享主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share`
--

LOCK TABLES `share` WRITE;
/*!40000 ALTER TABLE `share` DISABLE KEYS */;
INSERT INTO `share` VALUES (5,'https://share.example.com/5958ac15acdf436c8c630d4c2b9551b4',NULL,'2052-11-14 03:19:39',11,0,1,9999,1,NULL,'5958ac15acdf436c8c630d4c2b9551b4','https://share.example.com/5958ac15acdf436c8c630d4c2b9551b4','2025-06-30 11:19:38',1),(6,'https://share.example.com/1116b63a480a4b419b3939fdb68326a4',NULL,'2052-11-14 03:24:38',11,0,1,9999,1,NULL,'1116b63a480a4b419b3939fdb68326a4','https://share.example.com/1116b63a480a4b419b3939fdb68326a4','2025-06-30 11:24:38',1),(7,'https://share.example.com/c4c08f341c684f2991a86a9fbcb64ae3',NULL,'2052-11-14 03:26:16',11,0,1,9999,1,NULL,'c4c08f341c684f2991a86a9fbcb64ae3','https://share.example.com/c4c08f341c684f2991a86a9fbcb64ae3','2025-06-30 11:26:15',1),(8,'https://share.example.com/9776529c96134b5b93ab60da2273d1f2',NULL,'2052-11-14 03:28:10',11,0,1,9999,1,NULL,'9776529c96134b5b93ab60da2273d1f2','https://share.example.com/9776529c96134b5b93ab60da2273d1f2','2025-06-30 11:28:09',1),(9,'https://share.example.com/dce7926808be4c28bf1fbac2b495f7ef','7119','2052-11-14 03:28:17',11,1,1,9999,1,'7119','dce7926808be4c28bf1fbac2b495f7ef','https://share.example.com/dce7926808be4c28bf1fbac2b495f7ef','2025-06-30 11:28:16',1),(10,'https://share.example.com/981134fd73294651b34f7560ac87c15c','2790','2052-11-14 03:30:44',11,1,1,9999,1,'2790','981134fd73294651b34f7560ac87c15c','https://share.example.com/981134fd73294651b34f7560ac87c15c','2025-06-30 11:30:43',1),(11,'https://share.example.com/ae34cca0668b4af6ba4f7cc98ebef9d6','1995','2025-07-07 03:31:06',11,1,1,7,1,'1995','ae34cca0668b4af6ba4f7cc98ebef9d6','https://share.example.com/ae34cca0668b4af6ba4f7cc98ebef9d6','2025-06-30 11:31:05',1),(12,'https://share.example.com/5c6b8f0dce5e49b8b7618173bad80ccf','7211','2052-11-14 03:42:39',11,1,1,9999,1,'7211','5c6b8f0dce5e49b8b7618173bad80ccf','https://share.example.com/5c6b8f0dce5e49b8b7618173bad80ccf','2025-06-30 11:42:38',1),(13,'http://localhost:5173/shared/08727466e39d4221a69e07a85bae80ce','3934','2052-11-14 03:47:06',11,1,1,9999,1,'3934','08727466e39d4221a69e07a85bae80ce','http://localhost:5173/shared/08727466e39d4221a69e07a85bae80ce','2025-06-30 11:47:05',1),(14,'http://localhost:5173/shared/e90c7932408e45f1b303731270be5e5a','2314','2052-11-14 06:18:56',11,1,1,9999,1,'2314','e90c7932408e45f1b303731270be5e5a','http://localhost:5173/shared/e90c7932408e45f1b303731270be5e5a','2025-06-30 14:18:55',1),(15,'http://localhost:5173/shared/6829babb756446c8b92dff5afc91b3ff','3620','2052-11-14 07:00:55',11,1,1,9999,1,'3620','6829babb756446c8b92dff5afc91b3ff','http://localhost:5173/shared/6829babb756446c8b92dff5afc91b3ff','2025-06-30 15:00:55',1),(16,'http://localhost:5173/shared/36c17c9c844b40ee87bb2e7fb1e28617','8169','2052-11-14 08:37:20',11,1,1,9999,1,'8169','36c17c9c844b40ee87bb2e7fb1e28617','http://localhost:5173/shared/36c17c9c844b40ee87bb2e7fb1e28617','2025-06-30 16:37:19',1),(17,'http://localhost:5173/shared/ec17db4206c347f1b195786d12ede545','2817','2052-11-14 08:43:25',11,1,1,9999,1,'2817','ec17db4206c347f1b195786d12ede545','http://localhost:5173/shared/ec17db4206c347f1b195786d12ede545','2025-06-30 16:43:25',1),(18,'http://localhost:5173/shared/e7eb7c9602c3483d930fdfb17ca940fe','7338','2052-11-14 09:14:49',11,1,1,9999,1,'7338','e7eb7c9602c3483d930fdfb17ca940fe','http://localhost:5173/shared/e7eb7c9602c3483d930fdfb17ca940fe','2025-06-30 17:14:49',1),(19,'http://localhost:5173/shared/17abe6cb248d434696ae1de5102ad38f','2595','2025-07-07 09:16:11',11,1,1,7,1,'2595','17abe6cb248d434696ae1de5102ad38f','http://localhost:5173/shared/17abe6cb248d434696ae1de5102ad38f','2025-06-30 17:16:10',1),(20,'http://localhost:5173/shared/228c5be6934b4435b9e882a1e18e5060','9774','2052-11-14 10:14:42',11,1,1,9999,1,'9774','228c5be6934b4435b9e882a1e18e5060','http://localhost:5173/shared/228c5be6934b4435b9e882a1e18e5060','2025-06-30 18:14:42',1),(21,'http://localhost:5173/shared/246447b9e3204c1d86bf9c1e0195dca3','6425','2052-11-15 01:06:32',11,1,1,9999,1,'6425','246447b9e3204c1d86bf9c1e0195dca3','http://localhost:5173/shared/246447b9e3204c1d86bf9c1e0195dca3','2025-07-01 09:06:32',1),(22,'http://localhost:5173/shared/a408b27bc5b74c33900d6f47e3fe9a8c','3612','2052-11-15 01:37:41',11,1,1,9999,1,'3612','a408b27bc5b74c33900d6f47e3fe9a8c','http://localhost:5173/shared/a408b27bc5b74c33900d6f47e3fe9a8c','2025-07-01 09:37:40',1),(23,'http://localhost:5173/shared/809348f3f3f34f62941a06b520634106','9295','2052-11-15 03:48:46',11,1,1,9999,1,'9295','809348f3f3f34f62941a06b520634106','http://localhost:5173/shared/809348f3f3f34f62941a06b520634106','2025-07-01 11:48:45',0),(24,'http://localhost:5173/shared/e3e44c6b1b3c41e7b7d73f37de0fdf9d','4612','2052-11-15 08:16:33',11,1,1,9999,1,'4612','e3e44c6b1b3c41e7b7d73f37de0fdf9d','http://localhost:5173/shared/e3e44c6b1b3c41e7b7d73f37de0fdf9d','2025-07-01 16:16:32',0),(25,'http://localhost:5173/shared/526c111a575649538c6e4e28375b6eac','9564','2025-07-08 08:58:28',11,1,1,7,1,'9564','526c111a575649538c6e4e28375b6eac','http://localhost:5173/shared/526c111a575649538c6e4e28375b6eac','2025-07-01 16:58:28',0),(26,'http://localhost:5173/shared/ac1377ba5ebe44239898f354533851f6','7846','2052-11-15 09:02:11',11,1,1,9999,1,'7846','ac1377ba5ebe44239898f354533851f6','http://localhost:5173/shared/ac1377ba5ebe44239898f354533851f6','2025-07-01 17:02:11',0),(27,'http://localhost:5173/shared/f603937aa6144cb88c68dfa3296670a2','6239','2052-11-15 09:10:07',11,1,1,9999,1,'6239','f603937aa6144cb88c68dfa3296670a2','http://localhost:5173/shared/f603937aa6144cb88c68dfa3296670a2','2025-07-01 17:10:07',1),(28,'http://localhost:5173/shared/dc0f2a7ddf8d4b5a93d05d65d9135a19','5306','2052-11-15 09:20:57',11,1,1,9999,1,'5306','dc0f2a7ddf8d4b5a93d05d65d9135a19','http://localhost:5173/shared/dc0f2a7ddf8d4b5a93d05d65d9135a19','2025-07-01 17:20:56',0),(29,'http://localhost:5173/shared/50552f6163a04d038ab1cc63f30567f8','2607','2052-11-15 09:21:47',11,1,1,9999,1,'2607','50552f6163a04d038ab1cc63f30567f8','http://localhost:5173/shared/50552f6163a04d038ab1cc63f30567f8','2025-07-01 17:21:47',0),(30,'http://localhost:5173/shared/2507e0a0588849c8abaded68928c9200','7589','2052-11-15 10:09:50',11,1,1,9999,1,'7589','2507e0a0588849c8abaded68928c9200','http://localhost:5173/shared/2507e0a0588849c8abaded68928c9200','2025-07-01 18:09:49',0),(31,'http://localhost:5173/shared/34539c2225444c04a075c89e344b359c','6150','2052-11-15 10:14:37',11,1,1,9999,1,'6150','34539c2225444c04a075c89e344b359c','http://localhost:5173/shared/34539c2225444c04a075c89e344b359c','2025-07-01 18:14:36',1),(32,'http://localhost:5173/shared/647cf83308ce4e87aa1ce8216ade3261','6024','2025-07-03 00:47:50',11,1,1,1,1,'6024','647cf83308ce4e87aa1ce8216ade3261','http://localhost:5173/shared/647cf83308ce4e87aa1ce8216ade3261','2025-07-02 08:47:49',1),(33,'http://localhost:5173/shared/7d6bb3944d44470f939f15c6ddd5eaa4','0763','2052-11-16 01:14:57',11,1,1,9999,1,'0763','7d6bb3944d44470f939f15c6ddd5eaa4','http://localhost:5173/shared/7d6bb3944d44470f939f15c6ddd5eaa4','2025-07-02 09:14:57',1),(34,'http://localhost:5173/shared/05c4f8efebd4463594c0adb8cf4a3e4e','2306','2052-11-16 01:15:10',11,1,1,9999,1,'2306','05c4f8efebd4463594c0adb8cf4a3e4e','http://localhost:5173/shared/05c4f8efebd4463594c0adb8cf4a3e4e','2025-07-02 09:15:09',1),(35,'http://localhost:5173/shared/68538f2e0fbe45e9a98be8e74ce33e03','3321','2025-07-09 03:39:01',11,1,1,7,1,'3321','68538f2e0fbe45e9a98be8e74ce33e03','http://localhost:5173/shared/68538f2e0fbe45e9a98be8e74ce33e03','2025-07-02 11:39:00',1),(36,'http://localhost:5173/shared/851dc8d939874095a3c0ff53413464f6','6642','2052-11-16 05:02:51',11,1,1,9999,1,'6642','851dc8d939874095a3c0ff53413464f6','http://localhost:5173/shared/851dc8d939874095a3c0ff53413464f6','2025-07-02 13:02:51',1),(37,'http://localhost:5173/shared/4f1f3f64399248769d1b277a19e3841d','2532','2052-11-16 05:03:07',11,1,1,9999,1,'2532','4f1f3f64399248769d1b277a19e3841d','http://localhost:5173/shared/4f1f3f64399248769d1b277a19e3841d','2025-07-02 13:03:06',1),(38,'http://localhost:5173/shared/6e4f5796b5764a00b0a3fb460bc5589b','9931','2025-07-09 09:53:27',11,1,1,7,1,'9931','6e4f5796b5764a00b0a3fb460bc5589b','http://localhost:5173/shared/6e4f5796b5764a00b0a3fb460bc5589b','2025-07-02 17:53:26',1),(39,'http://localhost:5173/shared/3d0b48bf177b492d8a30d8eb151b4b46','1657','2025-07-09 09:53:39',11,1,1,7,1,'1657','3d0b48bf177b492d8a30d8eb151b4b46','http://localhost:5173/shared/3d0b48bf177b492d8a30d8eb151b4b46','2025-07-02 17:53:38',1),(40,'http://localhost:5173/shared/d0c38d8bd5784b19acaba0c611a58617','7468','2052-11-16 09:59:55',11,1,1,9999,1,'7468','d0c38d8bd5784b19acaba0c611a58617','http://localhost:5173/shared/d0c38d8bd5784b19acaba0c611a58617','2025-07-02 17:59:55',0);
/*!40000 ALTER TABLE `share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share_content`
--

DROP TABLE IF EXISTS `share_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share_content` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `share_id` bigint NOT NULL COMMENT '分享ID',
  `obj_id` bigint NOT NULL COMMENT '内容/目录',
  `obj_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型 file/dir',
  PRIMARY KEY (`id`),
  KEY `idx_share_id` (`share_id`),
  KEY `idx_obj_id` (`obj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分享内容关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_content`
--

LOCK TABLES `share_content` WRITE;
/*!40000 ALTER TABLE `share_content` DISABLE KEYS */;
INSERT INTO `share_content` VALUES (1,5,42,'dir'),(2,6,42,'dir'),(3,7,42,'dir'),(4,8,42,'dir'),(5,9,42,'dir'),(6,10,42,'dir'),(7,11,42,'dir'),(8,12,38,'dir'),(9,13,42,'dir'),(10,14,23,'file'),(11,15,42,'dir'),(12,16,42,'dir'),(13,17,42,'dir'),(14,18,23,'file'),(15,18,38,'dir'),(16,19,23,'file'),(17,19,38,'dir'),(18,20,42,'dir'),(19,21,23,'file'),(20,21,22,'file'),(21,22,23,'file'),(22,23,23,'file'),(23,24,23,'file'),(24,25,23,'file'),(25,26,23,'file'),(26,27,23,'file'),(27,28,22,'file'),(28,29,22,'file'),(29,30,23,'file'),(30,31,22,'file'),(31,32,22,'file'),(32,33,23,'file'),(33,34,23,'file'),(34,35,50,'dir'),(35,36,51,'file'),(36,36,23,'file'),(37,37,51,'file'),(38,38,53,'file'),(39,39,52,'file'),(40,40,22,'file');
/*!40000 ALTER TABLE `share_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `space`
--

DROP TABLE IF EXISTS `space`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `space` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total_space` bigint NOT NULL,
  `used_space` bigint NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `space`
--

LOCK TABLES `space` WRITE;
/*!40000 ALTER TABLE `space` DISABLE KEYS */;
INSERT INTO `space` VALUES (7,10,21474836480,0,'2025-06-24 16:45:22','2025-06-24 16:45:22'),(8,11,21474836480,0,'2025-06-24 16:51:46','2025-06-24 16:51:46'),(9,12,21474836480,0,'2025-06-30 17:54:45','2025-06-30 17:54:45'),(10,13,21474836480,0,'2025-07-01 17:50:56','2025-07-01 17:50:56'),(11,14,21474836480,0,'2025-07-01 17:51:09','2025-07-01 17:51:09');
/*!40000 ALTER TABLE `space` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10,'jeffchan','$2a$10$IOwpUsS2s1eAKClWC9uYyuY5QB/uCgCkUn7wEFnsf49vjSRvdnQza',NULL,NULL,1,'2025-06-24 16:45:16','2025-06-24 16:45:16'),(11,'2623768443@qq.com','$2a$10$70.i31GuXkcs8KUasFY8Je0wasIkOAGIza0Vfo1YQxIA7HzN1gkOa',NULL,NULL,1,'2025-06-24 16:51:45','2025-06-24 16:51:45'),(12,'123','$2a$10$UnshSu0Vb6/KfqPfAP8mguMAYoYZkJ3Atv8Wmg7qoo47j5I.4.67C',NULL,NULL,1,'2025-06-30 17:54:45','2025-06-30 17:54:45'),(13,'123456','$2a$10$3Fg9QKKSfH.xRUVxBv6Cn.mem8J0JUfMnq37t6p/EVF1Z0J66EFFO',NULL,NULL,1,'2025-07-01 17:50:56','2025-07-01 17:50:56'),(14,'1234567','$2a$10$tnCdW68iH3USEOYWJOO3.eWqKRoxpbL4cHZB4wmqD.PzIwAWMhqy.',NULL,NULL,1,'2025-07-01 17:51:09','2025-07-01 17:51:09');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'personal_cloud'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-04 17:34:51
