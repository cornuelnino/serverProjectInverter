-- MariaDB dump 10.19  Distrib 10.4.28-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	10.4.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `earnings`
--

DROP TABLE IF EXISTS `earnings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `earnings` (
  `idearnings` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `euro` double DEFAULT NULL,
  `kilowatter` double DEFAULT NULL,
  `inverter_idinverter` int(11) NOT NULL,
  PRIMARY KEY (`idearnings`),
  KEY `fk_earnings_inverter1_idx` (`inverter_idinverter`),
  CONSTRAINT `fk_earnings_inverter1` FOREIGN KEY (`inverter_idinverter`) REFERENCES `inverter` (`idinverter`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `earnings`
--

LOCK TABLES `earnings` WRITE;
/*!40000 ALTER TABLE `earnings` DISABLE KEYS */;
INSERT INTO `earnings` VALUES (1,'2024-02-06 00:00:00',1.2,24,4),(2,'2024-02-07 00:00:00',1.1,22,4),(3,'2024-02-08 00:00:00',1,20,4),(4,'2024-02-09 00:00:00',2,18,4),(5,'2024-02-10 00:00:00',1.8,19,4),(6,'2024-02-11 00:00:00',1.4,21,4),(7,'2024-02-12 00:00:00',1.1,16,4),(8,'2024-02-13 00:00:00',666,15,4),(9,'2024-02-14 00:00:00',1.67,18,4),(10,'2024-02-15 00:00:00',0.2,19,4),(11,'2024-02-16 00:00:00',0.8,24,4),(12,'2024-02-17 00:00:00',2,21,4),(13,'2024-02-18 00:00:00',2.4,17,4),(14,'2024-03-12 00:00:00',2.5,11,4),(15,'2024-03-13 00:00:00',666,11,4),(16,'2024-03-14 00:00:00',1000,28,4),(17,'2024-03-15 00:00:00',2.5,11,4),(18,'2024-03-16 00:00:00',1.5,14,4),(19,'2024-03-17 00:00:00',1.5,28,4),(20,'2024-03-06 00:00:00',0.008385709090909091,0.04300363636363636,4),(21,'2024-02-01 00:00:00',45.5,32,4),(22,'2024-02-02 12:00:00',45.5,32,4),(23,'2024-02-03 12:24:32',45.5,32,4),(24,'2024-03-12 00:00:00',0.0012919159663865546,0.006625210084033613,4),(25,'2024-03-13 00:00:00',2,0.006625210084033613,4),(26,'2024-03-14 00:00:00',1,0.006625210084033613,1),(27,'2024-03-15 00:00:00',3,0.006625210084033613,1),(28,'2024-03-16 12:24:32',5,2,1),(29,'2024-03-17 12:24:32',6,2,1),(30,'2024-03-18 12:24:32',8,2,1),(31,'2024-03-19 12:24:32',11,2,1),(32,'2024-03-20 12:24:32',21,2,1),(33,'2024-03-21 12:24:32',17,2,1),(34,'2024-03-22 12:24:32',11,2,1),(35,'2024-03-23 12:24:32',8,2,1),(36,'2024-03-24 12:24:32',5,2,1),(37,'2024-03-24 14:24:32',9,2,1),(38,'2024-03-24 17:24:32',11,2,1),(39,'2024-03-25 12:24:32',11,2,1),(40,'2024-03-25 14:24:32',1,2,1),(41,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(42,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(43,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(44,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(45,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(46,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(47,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(48,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(49,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(50,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(51,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(52,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(53,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(54,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(55,'2024-03-25 00:00:00',0.0004916443488943489,0.0025212530712530714,1),(56,'2024-03-12 00:00:00',0.0012919159663865546,0.006625210084033613,4),(57,'2024-03-12 00:00:00',0.0012919159663865546,0.006625210084033613,4),(58,'2024-05-13 12:22:33',1,0,4),(59,'2024-05-13 12:22:33',2,0,4),(60,'2024-05-13 12:22:33',0,0,4),(61,'2024-05-13 12:22:33',0,0,4),(62,'2024-05-13 12:22:33',0,0,4),(63,'2024-05-13 12:22:33',0,0,4),(64,'2024-05-13 12:27:58',0,0,4),(65,'2024-05-13 12:30:10',0.00001430000089108944,0.00007333333790302277,4),(66,'2024-05-13 12:30:00',0.00001430000089108944,0.00007333333790302277,4),(67,'2024-05-13 12:36:36',0.000020908333249390126,0.00010722222179174423,4),(80,'2024-05-14 11:49:08',0,0,22),(81,'2024-05-15 10:39:10',0.0000032499998062849048,0.00001666666567325592,24),(82,'2024-05-15 10:40:10',0,0,24),(83,'2024-05-15 10:41:10',0,0,24);
/*!40000 ALTER TABLE `earnings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inverter`
--

DROP TABLE IF EXISTS `inverter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inverter` (
  `idinverter` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `macAddress` varchar(45) DEFAULT NULL,
  `position` varchar(45) DEFAULT NULL,
  `isOnline` tinyint(4) DEFAULT NULL,
  `batteryPercentage` int(11) DEFAULT NULL,
  `outputActivePower` double NOT NULL,
  `outputVoltage` double NOT NULL,
  `warnings_idwarnings` int(11) NOT NULL,
  `settings_idsettings` int(11) NOT NULL,
  `user_iduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`idinverter`),
  KEY `fk_inverter_warnings1_idx` (`warnings_idwarnings`),
  KEY `fk_inverter_settings1_idx` (`settings_idsettings`),
  KEY `fk_inverter_user1_idx` (`user_iduser`),
  CONSTRAINT `fk_inverter_settings1` FOREIGN KEY (`settings_idsettings`) REFERENCES `settings` (`idsettings`),
  CONSTRAINT `fk_inverter_user1` FOREIGN KEY (`user_iduser`) REFERENCES `user` (`iduser`),
  CONSTRAINT `fk_inverter_warnings1` FOREIGN KEY (`warnings_idwarnings`) REFERENCES `warnings` (`idwarnings`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inverter`
--

LOCK TABLES `inverter` WRITE;
/*!40000 ALTER TABLE `inverter` DISABLE KEYS */;
INSERT INTO `inverter` VALUES (1,'onduleur1','00:00:00:00:00:00','45.995385;4.112035',0,75,0,0,1,1,9),(2,'onduleur2','11:11:11:11','48.617254;-4.484855',1,72,0,0,2,2,2),(3,'onduleur3','22:22:22:22','44.479819;4.238757',1,21,66.555,58,3,3,3),(4,'onduleur4','33:33:33:33:33:33','50.955307;1.802058',1,2,0,0,4,4,8),(9,'test111','11:11:11:11:11:11','test',0,66,0,0,9,10,NULL),(15,'onduleur-QkuQY','22:22:22:22:22:22','875;4523',1,22,0,12,15,16,11),(22,'onduleur-cw6iw','16:86:96:89:31:33','875;4523',1,22,0,12,22,23,NULL),(23,'onduleur-y2UX2','?','875;4523',1,22,0,12,23,24,NULL),(24,'onduleur-eRrfF','?','875;4523',1,100,0,12,24,25,NULL);
/*!40000 ALTER TABLE `inverter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price`
--

DROP TABLE IF EXISTS `price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `price` (
  `kwPrice` double NOT NULL,
  PRIMARY KEY (`kwPrice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price`
--

LOCK TABLES `price` WRITE;
/*!40000 ALTER TABLE `price` DISABLE KEYS */;
INSERT INTO `price` VALUES (0.65);
/*!40000 ALTER TABLE `price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `idsettings` int(11) NOT NULL AUTO_INCREMENT,
  `outputSourcePriority` int(11) DEFAULT NULL,
  PRIMARY KEY (`idsettings`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,0),(2,0),(3,0),(4,0),(10,1),(11,0),(16,0),(23,0),(24,0),(25,0);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `isAdmin` tinyint(4) DEFAULT NULL,
  `createdAt` date DEFAULT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'test2.test2@gmail.com','test2','$2a$10$E9bMX4z/zW4JfnU1AXDV6.9Fy2hJJ/fAB9nW0Hb9sULwrhE14TrHy',1,'2024-02-14'),(3,'test3.test3@gmail.com','test3','$2a$10$SpMfD6JsLxF/Hz3gJtycDu9lGMn0rn3NjMtNQbU6KGlADAM7aRMVq',0,'2024-02-14'),(8,'coucou1.coucou1@gmail.com','salut','$2a$10$ZNCwnpyIhECG3MvnFc4UxeVPl2A/uzlR2Mnrdg9rSvyv680DF44Hi',0,'2024-03-25'),(9,'moser.mika@gmail.com','Moser','$2a$10$tTdFvVjCO.uOtOlbCUJrvOOEm9x6i.oZMkWdLZi84Xu.Z7SzQCkce',1,'2024-04-09'),(11,'test1.test1@gmail.com','test1','$2a$10$UiyQ5yhNa6Y3H/4HLVY6R.Tg9aOpZi/S/R76GferkTBq1f4T7JmXu',0,'2024-05-15');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warnings`
--

DROP TABLE IF EXISTS `warnings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warnings` (
  `idwarnings` int(11) NOT NULL AUTO_INCREMENT,
  `inverterFault` tinyint(4) NOT NULL,
  `lineFail` tinyint(4) NOT NULL,
  `voltageTooLow` tinyint(4) NOT NULL,
  `voltageTooHigh` tinyint(4) NOT NULL,
  `overTemperature` tinyint(4) NOT NULL,
  `fanLocked` tinyint(4) NOT NULL,
  `batteryLowAlarm` tinyint(4) NOT NULL,
  `softFail` tinyint(4) NOT NULL,
  `batteryTooLowToCharge` tinyint(4) NOT NULL,
  PRIMARY KEY (`idwarnings`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warnings`
--

LOCK TABLES `warnings` WRITE;
/*!40000 ALTER TABLE `warnings` DISABLE KEYS */;
INSERT INTO `warnings` VALUES (1,0,0,0,0,0,0,0,0,0),(2,0,0,0,0,0,0,0,0,0),(3,0,0,0,0,0,0,0,0,0),(4,0,0,0,0,0,0,0,0,0),(9,1,1,1,1,1,1,1,1,1),(10,1,1,1,1,1,1,1,1,1),(15,1,0,0,1,0,1,1,0,0),(22,1,1,1,1,1,1,1,1,1),(23,1,1,1,1,1,1,1,1,1),(24,1,1,1,1,1,1,1,1,1);
/*!40000 ALTER TABLE `warnings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-15 11:40:37
