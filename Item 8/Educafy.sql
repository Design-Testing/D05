start transaction;

drop database if exists `Educafy`;
create database `Educafy`;

use `Educafy`;

drop user 'acme-user'@'%';
drop user 'acme-manager'@'%';

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';
create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete on `Educafy`.* to 'acme-user'@'%';
grant select, insert, update, delete, create, drop, references, index, alter, create temporary tables, lock tables, create view, create routine, alter routine, execute, trigger, show view on `Educafy`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Educafy
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actor`
--

DROP TABLE IF EXISTS `actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `spammer` bit(1) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_i7xei45auwq1f6vu25985riuh` (`user_account`),
  KEY `UK_k77r9upyn4vc6rupdcqapbko5` (`spammer`),
  CONSTRAINT `FK_i7xei45auwq1f6vu25985riuh` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor`
--

LOCK TABLES `actor` WRITE;
/*!40000 ALTER TABLE `actor` DISABLE KEYS */;
/*!40000 ALTER TABLE `actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actor_form`
--

DROP TABLE IF EXISTS `actor_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `terms_and_condicions` bit(1) DEFAULT NULL,
  `user_accountpassword` varchar(255) DEFAULT NULL,
  `user_accountuser` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor_form`
--

LOCK TABLES `actor_form` WRITE;
/*!40000 ALTER TABLE `actor_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `actor_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actor_form_surname`
--

DROP TABLE IF EXISTS `actor_form_surname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor_form_surname` (
  `actor_form` int(11) NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  KEY `FK_ktdmg95o6s8m3onefnjs6tbak` (`actor_form`),
  CONSTRAINT `FK_ktdmg95o6s8m3onefnjs6tbak` FOREIGN KEY (`actor_form`) REFERENCES `actor_form` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor_form_surname`
--

LOCK TABLES `actor_form_surname` WRITE;
/*!40000 ALTER TABLE `actor_form_surname` DISABLE KEYS */;
/*!40000 ALTER TABLE `actor_form_surname` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actor_surname`
--

DROP TABLE IF EXISTS `actor_surname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor_surname` (
  `actor` int(11) NOT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor_surname`
--

LOCK TABLES `actor_surname` WRITE;
/*!40000 ALTER TABLE `actor_surname` DISABLE KEYS */;
INSERT INTO `actor_surname` VALUES (5293,'System'),(5299,'Admin'),(5300,'Gonzalez'),(5301,'Teacher1'),(5302,'Teacher2'),(5303,'Teacher3'),(5304,'Teacher11'),(5305,'Teacher12'),(5306,'Teacher13'),(5307,'Garcia'),(5308,'Lanzas'),(5309,'Surname'),(5310,'Certifier\'s'),(5311,'Certifier\'s');
/*!40000 ALTER TABLE `actor_surname` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `spammer` bit(1) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7ohwsa2usmvu0yxb44je2lge` (`user_account`),
  KEY `administratorUK_k77r9upyn4vc6rupdcqapbko5` (`spammer`),
  CONSTRAINT `FK_7ohwsa2usmvu0yxb44je2lge` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (5293,0,'','correo@gmail.com','Educafy','+34647307406','','\0',5292),(5299,0,'Reina Mercedes','conwdasto@jmsx.es','Admin1','+34647607406','http://tinyurl.com/picture.png','\0',5279),(5300,0,'Reina Mercedes','lusi@gamil.es','Admin2','+34647307406','http://tinyurl.com/picture.png','\0',5280);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assesment`
--

DROP TABLE IF EXISTS `assesment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assesment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `lesson` int(11) NOT NULL,
  `student` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_7ncrpimqna6ds08t5v5s2g1i2` (`student`),
  KEY `UK_3swtmocbw460l08swr93a3gpw` (`lesson`),
  CONSTRAINT `FK_7ncrpimqna6ds08t5v5s2g1i2` FOREIGN KEY (`student`) REFERENCES `student` (`id`),
  CONSTRAINT `FK_3swtmocbw460l08swr93a3gpw` FOREIGN KEY (`lesson`) REFERENCES `lesson` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assesment`
--

LOCK TABLES `assesment` WRITE;
/*!40000 ALTER TABLE `assesment` DISABLE KEYS */;
INSERT INTO `assesment` VALUES (5455,0,'Bien trabajado',4,5443,5307),(5456,0,'Bien trabajado',3,5444,5307),(5457,0,'Enhorabuena !',4,5445,5308),(5458,0,'Bien hecho',4,5446,5308);
/*!40000 ALTER TABLE `assesment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assesment_form`
--

DROP TABLE IF EXISTS `assesment_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assesment_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assesment_form`
--

LOCK TABLES `assesment_form` WRITE;
/*!40000 ALTER TABLE `assesment_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `assesment_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certifier`
--

DROP TABLE IF EXISTS `certifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certifier` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `spammer` bit(1) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_peycq2crr1xa7wus0x1ahv0gj` (`user_account`),
  KEY `certifierUK_k77r9upyn4vc6rupdcqapbko5` (`spammer`),
  CONSTRAINT `FK_peycq2crr1xa7wus0x1ahv0gj` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certifier`
--

LOCK TABLES `certifier` WRITE;
/*!40000 ALTER TABLE `certifier` DISABLE KEYS */;
INSERT INTO `certifier` VALUES (5310,0,'Reina Mercedes','certifier1@gmail.es','Certifier1','+34647307480','http://tinyurl.com/picture.png','\0',5290),(5311,0,'Reina Mercedes','certifier2@gmail.es','Certifier2','+34647307484','http://tinyurl.com/picture.png','\0',5291);
/*!40000 ALTER TABLE `certifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `moment` datetime DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `assesment` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_2x7bmc1pfbwvv22pfqyealq47` (`assesment`),
  CONSTRAINT `FK_2x7bmc1pfbwvv22pfqyealq47` FOREIGN KEY (`assesment`) REFERENCES `assesment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (5459,0,'2019-05-12 20:45:00','Text of comment 1',5455),(5460,0,'2019-05-12 10:45:00','Text of comment 2',5455),(5461,0,'2019-05-12 21:45:00','Text of comment 3',5456),(5462,0,'2019-05-12 20:40:00','Text of comment 4',5456),(5463,0,'2019-05-12 22:45:00','Text of comment 5',5457),(5464,0,'2019-05-12 11:45:00','Text of comment 6',5457),(5465,0,'2019-05-12 09:45:00','Text of comment 7',5458),(5466,0,'2019-05-12 14:45:00','Text of comment 8',5458);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_form`
--

DROP TABLE IF EXISTS `comment_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_form`
--

LOCK TABLES `comment_form` WRITE;
/*!40000 ALTER TABLE `comment_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration_parameters`
--

DROP TABLE IF EXISTS `configuration_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration_parameters` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `country_phone_code` varchar(255) DEFAULT NULL,
  `finder_time` int(11) NOT NULL,
  `max_finder_results` int(11) NOT NULL,
  `rebranding` bit(1) NOT NULL,
  `sys_name` varchar(255) DEFAULT NULL,
  `vat` double NOT NULL,
  `welcome_message_en` varchar(255) DEFAULT NULL,
  `welcome_message_esp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration_parameters`
--

LOCK TABLES `configuration_parameters` WRITE;
/*!40000 ALTER TABLE `configuration_parameters` DISABLE KEYS */;
INSERT INTO `configuration_parameters` VALUES (5323,0,'https://i.ibb.co/W3c4rmh/logo.png','+34',1,10,'\0','Educafy',0.21,'Welcome to Educafy! Where student find the individual lessons they are looking for!','¡Bienvenidos a Educafy! ¡El lugar donde los estudiantes encuentran las clases particulares que estaban buscando!');
/*!40000 ALTER TABLE `configuration_parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration_parameters_credit_card_make`
--

DROP TABLE IF EXISTS `configuration_parameters_credit_card_make`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration_parameters_credit_card_make` (
  `configuration_parameters` int(11) NOT NULL,
  `credit_card_make` varchar(255) DEFAULT NULL,
  KEY `FK_msvomwet3mpkas6chhws7tv92` (`configuration_parameters`),
  CONSTRAINT `FK_msvomwet3mpkas6chhws7tv92` FOREIGN KEY (`configuration_parameters`) REFERENCES `configuration_parameters` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration_parameters_credit_card_make`
--

LOCK TABLES `configuration_parameters_credit_card_make` WRITE;
/*!40000 ALTER TABLE `configuration_parameters_credit_card_make` DISABLE KEYS */;
INSERT INTO `configuration_parameters_credit_card_make` VALUES (5323,'VISA'),(5323,'MCARD'),(5323,'AMEX'),(5323,'DINNERS'),(5323,'FLY');
/*!40000 ALTER TABLE `configuration_parameters_credit_card_make` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration_parameters_spam_words`
--

DROP TABLE IF EXISTS `configuration_parameters_spam_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration_parameters_spam_words` (
  `configuration_parameters` int(11) NOT NULL,
  `spam_words` varchar(255) DEFAULT NULL,
  KEY `FK_r9o9dd0kww7hr04phroji3ig7` (`configuration_parameters`),
  CONSTRAINT `FK_r9o9dd0kww7hr04phroji3ig7` FOREIGN KEY (`configuration_parameters`) REFERENCES `configuration_parameters` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration_parameters_spam_words`
--

LOCK TABLES `configuration_parameters_spam_words` WRITE;
/*!40000 ALTER TABLE `configuration_parameters_spam_words` DISABLE KEYS */;
INSERT INTO `configuration_parameters_spam_words` VALUES (5323,'sex'),(5323,'viagra'),(5323,'cialis'),(5323,'one million'),(5323,'you\'ve been selected'),(5323,'nigeria'),(5323,'sexo'),(5323,'un millón'),(5323,'ha sido seleccionado');
/*!40000 ALTER TABLE `configuration_parameters_spam_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration_parameters_subject_levels`
--

DROP TABLE IF EXISTS `configuration_parameters_subject_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration_parameters_subject_levels` (
  `configuration_parameters` int(11) NOT NULL,
  `subject_levels` varchar(255) DEFAULT NULL,
  KEY `FK_fuapeyr8wxn6mvdy2neit6xta` (`configuration_parameters`),
  CONSTRAINT `FK_fuapeyr8wxn6mvdy2neit6xta` FOREIGN KEY (`configuration_parameters`) REFERENCES `configuration_parameters` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration_parameters_subject_levels`
--

LOCK TABLES `configuration_parameters_subject_levels` WRITE;
/*!40000 ALTER TABLE `configuration_parameters_subject_levels` DISABLE KEYS */;
INSERT INTO `configuration_parameters_subject_levels` VALUES (5323,'1ESO'),(5323,'2ESO'),(5323,'3ESO'),(5323,'4ESO'),(5323,'1BACH'),(5323,'2BACH');
/*!40000 ALTER TABLE `configuration_parameters_subject_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_card`
--

DROP TABLE IF EXISTS `credit_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit_card` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `cvv` varchar(255) DEFAULT NULL,
  `expiration_month` int(11) DEFAULT NULL,
  `expiration_year` int(11) DEFAULT NULL,
  `holder_name` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `actor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2ncxn32sn2quehl710urqs0on` (`number`),
  KEY `UK_kj6ag2kv2vppcsvh6y24est5s` (`actor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_card`
--

LOCK TABLES `credit_card` WRITE;
/*!40000 ALTER TABLE `credit_card` DISABLE KEYS */;
INSERT INTO `credit_card` VALUES (5312,0,'163',6,19,'HolderName Student 1','VISA','4716477920082572',5307),(5313,0,'728',10,20,'HolderName Student 1','MASTER','5498128346540526',5307),(5314,0,'533',6,19,'HolderName Student 1','AMEX','375278545368168',5307),(5315,0,'266',10,19,'HolderName Student 1','VISA','4532787155338743',5307),(5316,0,'885',2,19,'HolderName Student 1','VISA','4716699361876929',5307),(5317,0,'837',11,22,'HolderName Student 2','VISA','4231348143458624',5308),(5318,0,'988',11,20,'HolderName Student 2','VISA','4294148159742547',5308),(5319,0,'475',11,20,'HolderName Student 2','MASTERCARD','5547165664775350',5308),(5320,0,'941',3,27,'HolderName Student 2','VISA','4410435734979051',5308),(5321,0,'408',1,26,'HolderName Student 2','MASTERCARD','5316710759043864',5308),(5322,0,'906',1,20,'HolderName Student 2','MASTERCARD','5384948404521051',5308);
/*!40000 ALTER TABLE `credit_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curriculum`
--

DROP TABLE IF EXISTS `curriculum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curriculum` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `personal_record` int(11) NOT NULL,
  `teacher` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_of5n83aytom6e52721o6k6ae` (`personal_record`),
  UNIQUE KEY `UK_1xu9oa10iuvhqj5v91ri1xdqi` (`teacher`),
  UNIQUE KEY `UK_3ai7h3tynp97g8r0g93r84m8w` (`ticker`),
  CONSTRAINT `FK_1xu9oa10iuvhqj5v91ri1xdqi` FOREIGN KEY (`teacher`) REFERENCES `teacher` (`id`),
  CONSTRAINT `FK_of5n83aytom6e52721o6k6ae` FOREIGN KEY (`personal_record`) REFERENCES `personal_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curriculum`
--

LOCK TABLES `curriculum` WRITE;
/*!40000 ALTER TABLE `curriculum` DISABLE KEYS */;
INSERT INTO `curriculum` VALUES (5327,0,'COMM-1234',5328,5301),(5333,0,'COMX-1231',5334,5302),(5337,0,'COMX-4444',5338,5303),(5341,0,'COMX-4441',5342,5306);
/*!40000 ALTER TABLE `curriculum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curriculum_education_records`
--

DROP TABLE IF EXISTS `curriculum_education_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curriculum_education_records` (
  `curriculum` int(11) NOT NULL,
  `education_records` int(11) NOT NULL,
  UNIQUE KEY `UK_mglbvuj28iptdf2n2wliy5dne` (`education_records`),
  KEY `FK_f6du7wj59ct8k4fkxo6p6avc` (`curriculum`),
  CONSTRAINT `FK_f6du7wj59ct8k4fkxo6p6avc` FOREIGN KEY (`curriculum`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `FK_mglbvuj28iptdf2n2wliy5dne` FOREIGN KEY (`education_records`) REFERENCES `education_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curriculum_education_records`
--

LOCK TABLES `curriculum_education_records` WRITE;
/*!40000 ALTER TABLE `curriculum_education_records` DISABLE KEYS */;
INSERT INTO `curriculum_education_records` VALUES (5327,5329),(5327,5330),(5333,5335),(5337,5339),(5341,5343);
/*!40000 ALTER TABLE `curriculum_education_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curriculum_miscellaneous_records`
--

DROP TABLE IF EXISTS `curriculum_miscellaneous_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curriculum_miscellaneous_records` (
  `curriculum` int(11) NOT NULL,
  `miscellaneous_records` int(11) NOT NULL,
  UNIQUE KEY `UK_hbex6yqhywe93w3clw8y1od2q` (`miscellaneous_records`),
  KEY `FK_fxsf5ohw20jbm0wuny6j1nnc9` (`curriculum`),
  CONSTRAINT `FK_fxsf5ohw20jbm0wuny6j1nnc9` FOREIGN KEY (`curriculum`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `FK_hbex6yqhywe93w3clw8y1od2q` FOREIGN KEY (`miscellaneous_records`) REFERENCES `miscellaneous_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curriculum_miscellaneous_records`
--

LOCK TABLES `curriculum_miscellaneous_records` WRITE;
/*!40000 ALTER TABLE `curriculum_miscellaneous_records` DISABLE KEYS */;
INSERT INTO `curriculum_miscellaneous_records` VALUES (5327,5331),(5327,5332),(5333,5336),(5337,5340),(5341,5344);
/*!40000 ALTER TABLE `curriculum_miscellaneous_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education_record`
--

DROP TABLE IF EXISTS `education_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `education_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `attachment` varchar(255) DEFAULT NULL,
  `degree` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `institution` varchar(255) DEFAULT NULL,
  `is_certified` bit(1) DEFAULT NULL,
  `is_draft` bit(1) DEFAULT NULL,
  `mark` int(11) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education_record`
--

LOCK TABLES `education_record` WRITE;
/*!40000 ALTER TABLE `education_record` DISABLE KEYS */;
INSERT INTO `education_record` VALUES (5329,0,'http://attachment1.com','Informática','2018-04-11','Universidad de Sevilla','\0','',7,'2014-04-11'),(5330,0,'http://attachment2.com','Magisterio','2019-04-11','Universidad de Sevilla','\0','\0',8,'2018-04-11'),(5335,0,'http://attachment3.com','Farmacia','2019-04-11','Universidad de Sevilla','','\0',7,'2018-04-11'),(5339,0,'http://attachment3.com','Biología','2019-04-11','Universidad de Sevilla','\0','',7,'2018-04-11'),(5343,0,'http://attachment3.com','Ingeniería Industrial','2019-04-11','Universidad de Sevilla','\0','\0',7,'2018-04-11');
/*!40000 ALTER TABLE `education_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `score` double DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `reservation` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_28nenwqwwxrmswprjri3fjhon` (`reservation`),
  CONSTRAINT `FK_28nenwqwwxrmswprjri3fjhon` FOREIGN KEY (`reservation`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
INSERT INTO `exam` VALUES (5467,0,NULL,'PENDING','Title Exam 1',5449),(5468,0,NULL,'SUBMITTED','Title Exam 2',5449),(5469,0,NULL,'PENDING','Title Exam 3',5449),(5470,0,NULL,'PENDING','Title Exam 4',5450),(5471,0,NULL,'PENDING','Title Exam 5',5449),(5472,0,NULL,'INPROGRESS','Title Exam 6',5449),(5473,0,NULL,'SUBMITTED','Title Exam 7',5449);
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_questions`
--

DROP TABLE IF EXISTS `exam_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam_questions` (
  `exam` int(11) NOT NULL,
  `questions` int(11) NOT NULL,
  UNIQUE KEY `UK_c5861mb62fr4c3g7hulnm9nxg` (`questions`),
  KEY `FK_1fnrolkx2hdxwfbtnpt0j3goc` (`exam`),
  CONSTRAINT `FK_1fnrolkx2hdxwfbtnpt0j3goc` FOREIGN KEY (`exam`) REFERENCES `exam` (`id`),
  CONSTRAINT `FK_c5861mb62fr4c3g7hulnm9nxg` FOREIGN KEY (`questions`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_questions`
--

LOCK TABLES `exam_questions` WRITE;
/*!40000 ALTER TABLE `exam_questions` DISABLE KEYS */;
INSERT INTO `exam_questions` VALUES (5468,5355),(5472,5356),(5473,5353),(5473,5354);
/*!40000 ALTER TABLE `exam_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder`
--

DROP TABLE IF EXISTS `finder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `subject_level` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `teacher_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder`
--

LOCK TABLES `finder` WRITE;
/*!40000 ALTER TABLE `finder` DISABLE KEYS */;
INSERT INTO `finder` VALUES (5324,0,'2019-05-23 09:00:00',NULL,'4ESO','IT','John'),(5325,0,'2019-05-23 09:00:00',NULL,'3ESO','MAT','Patrick'),(5326,0,'2019-05-23 09:00:00',NULL,'4ESO','LENG','Robert');
/*!40000 ALTER TABLE `finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder_lessons`
--

DROP TABLE IF EXISTS `finder_lessons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder_lessons` (
  `finder` int(11) NOT NULL,
  `lessons` int(11) NOT NULL,
  KEY `FK_ciu77kkx5eqho5ku07qyo1bo` (`lessons`),
  KEY `FK_5xbps8pow4or4eeoyy5m6vl6r` (`finder`),
  CONSTRAINT `FK_5xbps8pow4or4eeoyy5m6vl6r` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`),
  CONSTRAINT `FK_ciu77kkx5eqho5ku07qyo1bo` FOREIGN KEY (`lessons`) REFERENCES `lesson` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder_lessons`
--

LOCK TABLES `finder_lessons` WRITE;
/*!40000 ALTER TABLE `finder_lessons` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder_lessons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_system_folder` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `actor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_98saja6t4e4rrmw2ej3nbwgxq` (`actor`,`is_system_folder`),
  KEY `UK_lnclqq7vcaqpv8y5di1gg5bm3` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
INSERT INTO `folder` VALUES (5294,0,'','Out box',5293),(5295,0,'','In box',5293),(5296,0,'','Trash box',5293),(5297,0,'','Notification box',5293),(5298,0,'','Spam box',5293),(5372,0,'','Out box',5299),(5373,0,'','In box',5299),(5374,0,'','Trash box',5299),(5375,0,'','Notification box',5299),(5376,0,'','Spam box',5299),(5377,0,'','Out box',5300),(5378,0,'','In box',5300),(5379,0,'','Trash box',5300),(5380,0,'','Notification box',5300),(5381,0,'','Spam box',5300),(5382,0,'','Out box',5307),(5383,0,'','In box',5307),(5384,0,'','Trash box',5307),(5385,0,'','Notification box',5307),(5386,0,'','Spam box',5307),(5387,0,'','Out box',5308),(5388,0,'','In box',5308),(5389,0,'','Trash box',5308),(5390,0,'','Notification box',5308),(5391,0,'','Spam box',5308),(5392,0,'','Out box',5309),(5393,0,'','In box',5309),(5394,0,'','Trash box',5309),(5395,0,'','Notification box',5309),(5396,0,'','Spam box',5309),(5403,0,'','Out box',5301),(5404,0,'','In box',5301),(5405,0,'','Trash box',5301),(5406,0,'','Notification box',5301),(5407,0,'','Spam box',5301),(5408,0,'','Out box',5302),(5409,0,'','In box',5302),(5410,0,'','Trash box',5302),(5411,0,'','Notification box',5302),(5412,0,'','Spam box',5302),(5413,0,'','Out box',5303),(5414,0,'','In box',5303),(5415,0,'','Trash box',5303),(5416,0,'','Notification box',5303),(5417,0,'','Spam box',5303),(5418,0,'','Out box',5304),(5419,0,'','In box',5304),(5420,0,'','Trash box',5304),(5421,0,'','Notification box',5304),(5422,0,'','Spam box',5304),(5423,0,'','Out box',5305),(5424,0,'','In box',5305),(5425,0,'','Trash box',5305),(5426,0,'','Notification box',5305),(5427,0,'','Spam box',5305),(5428,0,'','Out box',5306),(5429,0,'','In box',5306),(5430,0,'','Trash box',5306),(5431,0,'','Notification box',5306),(5432,0,'','Spam box',5306),(5433,0,'','Out box',5310),(5434,0,'','In box',5310),(5435,0,'','Trash box',5310),(5436,0,'','Notification box',5310),(5437,0,'','Spam box',5310),(5438,0,'','Out box',5311),(5439,0,'','In box',5311),(5440,0,'','Trash box',5311),(5441,0,'','Notification box',5311),(5442,0,'','Spam box',5311);
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder_form`
--

DROP TABLE IF EXISTS `folder_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder_form`
--

LOCK TABLES `folder_form` WRITE;
/*!40000 ALTER TABLE `folder_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `folder_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder_messages`
--

DROP TABLE IF EXISTS `folder_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_messages` (
  `folder` int(11) NOT NULL,
  `messages` int(11) NOT NULL,
  KEY `FK_pd7js9rp0nie7ft4b2ltq7jx0` (`messages`),
  KEY `FK_p4c0hkadh5uwpdsjbyqfkauak` (`folder`),
  CONSTRAINT `FK_p4c0hkadh5uwpdsjbyqfkauak` FOREIGN KEY (`folder`) REFERENCES `folder` (`id`),
  CONSTRAINT `FK_pd7js9rp0nie7ft4b2ltq7jx0` FOREIGN KEY (`messages`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder_messages`
--

LOCK TABLES `folder_messages` WRITE;
/*!40000 ALTER TABLE `folder_messages` DISABLE KEYS */;
INSERT INTO `folder_messages` VALUES (5382,5397),(5383,5398),(5383,5399),(5383,5400),(5383,5401),(5383,5402),(5392,5398),(5392,5399),(5392,5400),(5392,5401),(5392,5402),(5404,5397),(5419,5397);
/*!40000 ALTER TABLE `folder_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('domain_entity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lesson` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_draft` bit(1) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `subject` int(11) NOT NULL,
  `teacher` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sdlp1n7jakeyyiqjmyrfw99jj` (`ticker`),
  KEY `UK_s6cdrqevim4qld1qh9vq2j5yd` (`teacher`),
  KEY `UK_pf7noq198h1bhca8renj35jyh` (`is_draft`),
  KEY `UK_8x2j3fj60lwhds7csm5xxs0sx` (`subject`),
  CONSTRAINT `FK_s6cdrqevim4qld1qh9vq2j5yd` FOREIGN KEY (`teacher`) REFERENCES `teacher` (`id`),
  CONSTRAINT `FK_8x2j3fj60lwhds7csm5xxs0sx` FOREIGN KEY (`subject`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
INSERT INTO `lesson` VALUES (5443,0,'Description1','\0',16.5,'EYTS-6579','Lesson1',5357,5301),(5444,0,'Description2','\0',16.5,'KYTS-6529','Lesson2',5358,5302),(5445,0,'Description3','\0',16.5,'UYTO-6379','Lesson3',5358,5301),(5446,0,'Description4','\0',16.5,'EITS-4579','Lesson4',5357,5302);
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_form`
--

DROP TABLE IF EXISTS `lesson_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lesson_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_form`
--

LOCK TABLES `lesson_form` WRITE;
/*!40000 ALTER TABLE `lesson_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `lesson_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `sender` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (5397,0,'Clases de matematicas sevilla','2018-11-04 13:00:00','HIGH','Clases particulares',5307),(5398,0,'We have to buy kill of walkers','2018-11-04 13:00:00','HIGH','you\'ve been selected',5309),(5399,0,'We have to buy kill of walkers','2018-11-04 13:00:00','HIGH','you\'ve been selected',5309),(5400,0,'We have to buy kill of walkers','2018-11-04 13:00:00','HIGH','you\'ve been selected',5309),(5401,0,'We have to buy kill of walkers','2018-11-04 13:00:00','HIGH','you\'ve been selected',5309),(5402,0,'We have to buy kill of walkers','2018-11-04 13:00:00','HIGH','you\'ve been selected',5309);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_recipients`
--

DROP TABLE IF EXISTS `message_recipients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_recipients` (
  `message` int(11) NOT NULL,
  `recipients` int(11) NOT NULL,
  KEY `FK_1odmg2n3n487tvhuxx5oyyya2` (`message`),
  CONSTRAINT `FK_1odmg2n3n487tvhuxx5oyyya2` FOREIGN KEY (`message`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_recipients`
--

LOCK TABLES `message_recipients` WRITE;
/*!40000 ALTER TABLE `message_recipients` DISABLE KEYS */;
INSERT INTO `message_recipients` VALUES (5397,5301),(5398,5307),(5399,5307),(5400,5307),(5401,5307),(5402,5307);
/*!40000 ALTER TABLE `message_recipients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_tags`
--

DROP TABLE IF EXISTS `message_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_tags` (
  `message` int(11) NOT NULL,
  `tags` varchar(255) DEFAULT NULL,
  KEY `FK_suckduhsrrtot7go5ejeev57w` (`message`),
  CONSTRAINT `FK_suckduhsrrtot7go5ejeev57w` FOREIGN KEY (`message`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_tags`
--

LOCK TABLES `message_tags` WRITE;
/*!40000 ALTER TABLE `message_tags` DISABLE KEYS */;
INSERT INTO `message_tags` VALUES (5397,'Mates'),(5397,'Sevilla'),(5398,'Zombies'),(5398,'Kill'),(5399,'Zombies'),(5399,'Kill'),(5400,'Zombies'),(5400,'Kill'),(5401,'Zombies'),(5401,'Kill'),(5402,'Zombies'),(5402,'Kill');
/*!40000 ALTER TABLE `message_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miscellaneous_record`
--

DROP TABLE IF EXISTS `miscellaneous_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miscellaneous_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `free_text` varchar(255) DEFAULT NULL,
  `is_certified` bit(1) DEFAULT NULL,
  `is_draft` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miscellaneous_record`
--

LOCK TABLES `miscellaneous_record` WRITE;
/*!40000 ALTER TABLE `miscellaneous_record` DISABLE KEYS */;
INSERT INTO `miscellaneous_record` VALUES (5331,0,'Miscellaneous record 1','\0',''),(5332,0,'Miscellaneous record 2','\0','\0'),(5336,0,'Miscellaneous record 3','','\0'),(5340,0,'Miscellaneous record 3','\0',''),(5344,0,'Miscellaneous record 5','\0','\0');
/*!40000 ALTER TABLE `miscellaneous_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miscellaneous_record_attachments`
--

DROP TABLE IF EXISTS `miscellaneous_record_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miscellaneous_record_attachments` (
  `miscellaneous_record` int(11) NOT NULL,
  `attachments` varchar(255) DEFAULT NULL,
  KEY `FK_837vxqm772a0m4x5p8hyabd3h` (`miscellaneous_record`),
  CONSTRAINT `FK_837vxqm772a0m4x5p8hyabd3h` FOREIGN KEY (`miscellaneous_record`) REFERENCES `miscellaneous_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miscellaneous_record_attachments`
--

LOCK TABLES `miscellaneous_record_attachments` WRITE;
/*!40000 ALTER TABLE `miscellaneous_record_attachments` DISABLE KEYS */;
INSERT INTO `miscellaneous_record_attachments` VALUES (5331,'http://attachment11.com'),(5331,'http://attachment12.com'),(5332,'http://attachment21.com'),(5332,'http://attachment22.com'),(5336,'http://attachment31.com'),(5336,'http://attachment32.com'),(5340,'http://attachment31.com'),(5340,'http://attachment32.com'),(5344,'http://attachment312.com'),(5344,'http://attachment342.com');
/*!40000 ALTER TABLE `miscellaneous_record_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_record`
--

DROP TABLE IF EXISTS `personal_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personal_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `github` varchar(255) DEFAULT NULL,
  `is_certified` bit(1) DEFAULT NULL,
  `is_draft` bit(1) DEFAULT NULL,
  `linkedin` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `statement` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_record`
--

LOCK TABLES `personal_record` WRITE;
/*!40000 ALTER TABLE `personal_record` DISABLE KEYS */;
INSERT INTO `personal_record` VALUES (5328,0,'Teacher 1','http://www.githubTeacher1.com','\0','','http://www.linkedInTeacher1.com','http://photoTeacher1.com','Statement Teacher 1'),(5334,0,'Teacher 2','http://www.githubTeacher2.com','\0','\0','http://www.linkedInTeacher2.com','http://photoTeacher2.com','Statement Teacher 2'),(5338,0,'Teacher 3','http://www.githubTeacher3.com','\0','','http://www.linkedInTeacher3.com','http://photoTeacher3.com','Statement Teacher 3'),(5342,0,'Teacher 13','http://www.githubTeacher13.com','\0','\0','http://www.linkedInTeacher13.com','http://photoTeacher13.com','Statement Teacher 13');
/*!40000 ALTER TABLE `personal_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (5345,0,'Answer 1','Question 1'),(5346,0,'Answer 2','Question 2'),(5347,0,'Answer 3','Question 3'),(5348,0,'Answer 4','Question 4'),(5349,0,'Answer 5','Question 5'),(5350,0,'Answer 6','Question 6'),(5351,0,'Answer 7','Question 7'),(5352,0,'Answer 8','Question 8'),(5353,0,'Answer 9','Question 9'),(5354,0,'Answer 10','Question 10'),(5355,0,'Answer 11','Question 11'),(5356,0,NULL,'Question 12');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `cost` double DEFAULT NULL,
  `explanation` varchar(255) DEFAULT NULL,
  `hours_week` int(11) DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `credit_card` int(11) NOT NULL,
  `lesson` int(11) NOT NULL,
  `student` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_579gkur9nx7ydmw19u6l9ubad` (`student`),
  KEY `FK_ejj99bixyerta7ey6repfw6xp` (`credit_card`),
  KEY `FK_94kai2s68391ykl92mmh8y9x1` (`lesson`),
  CONSTRAINT `FK_579gkur9nx7ydmw19u6l9ubad` FOREIGN KEY (`student`) REFERENCES `student` (`id`),
  CONSTRAINT `FK_94kai2s68391ykl92mmh8y9x1` FOREIGN KEY (`lesson`) REFERENCES `lesson` (`id`),
  CONSTRAINT `FK_ejj99bixyerta7ey6repfw6xp` FOREIGN KEY (`credit_card`) REFERENCES `credit_card` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (5447,0,20.5,'',1,'2019-05-10 20:30:00','PENDING',5312,5443,5307),(5448,0,15.5,'',1,'2019-05-11 20:30:00','ACCEPTED',5313,5445,5307),(5449,0,15.5,'',1,'2019-05-11 20:30:00','FINAL',5313,5444,5307),(5450,0,15.5,'',1,'2019-05-11 20:30:00','FINAL',5318,5446,5308);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `teacher` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_94hm7tr8qm443etu19s1ejav8` (`teacher`),
  CONSTRAINT `FK_94hm7tr8qm443etu19s1ejav8` FOREIGN KEY (`teacher`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (5366,0,5301),(5367,0,5302),(5368,0,5303),(5369,0,5304),(5370,0,5305),(5371,0,5306);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_friday`
--

DROP TABLE IF EXISTS `schedule_friday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_friday` (
  `schedule` int(11) NOT NULL,
  `friday` bit(1) DEFAULT NULL,
  KEY `FK_jf6gfs23160m2eaef2f2p1i8v` (`schedule`),
  CONSTRAINT `FK_jf6gfs23160m2eaef2f2p1i8v` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_friday`
--

LOCK TABLES `schedule_friday` WRITE;
/*!40000 ALTER TABLE `schedule_friday` DISABLE KEYS */;
INSERT INTO `schedule_friday` VALUES (5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0');
/*!40000 ALTER TABLE `schedule_friday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_monday`
--

DROP TABLE IF EXISTS `schedule_monday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_monday` (
  `schedule` int(11) NOT NULL,
  `monday` bit(1) DEFAULT NULL,
  KEY `FK_si2byo50iyg626na0clgq2uui` (`schedule`),
  CONSTRAINT `FK_si2byo50iyg626na0clgq2uui` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_monday`
--

LOCK TABLES `schedule_monday` WRITE;
/*!40000 ALTER TABLE `schedule_monday` DISABLE KEYS */;
INSERT INTO `schedule_monday` VALUES (5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,''),(5367,'\0'),(5367,'\0'),(5367,''),(5367,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0');
/*!40000 ALTER TABLE `schedule_monday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_thursday`
--

DROP TABLE IF EXISTS `schedule_thursday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_thursday` (
  `schedule` int(11) NOT NULL,
  `thursday` bit(1) DEFAULT NULL,
  KEY `FK_adjhnqpoo0mb4lfm9s82y0g4l` (`schedule`),
  CONSTRAINT `FK_adjhnqpoo0mb4lfm9s82y0g4l` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_thursday`
--

LOCK TABLES `schedule_thursday` WRITE;
/*!40000 ALTER TABLE `schedule_thursday` DISABLE KEYS */;
INSERT INTO `schedule_thursday` VALUES (5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,''),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0');
/*!40000 ALTER TABLE `schedule_thursday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_tuesday`
--

DROP TABLE IF EXISTS `schedule_tuesday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_tuesday` (
  `schedule` int(11) NOT NULL,
  `tuesday` bit(1) DEFAULT NULL,
  KEY `FK_47be9j3irn2gfj4s9aykigxnw` (`schedule`),
  CONSTRAINT `FK_47be9j3irn2gfj4s9aykigxnw` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_tuesday`
--

LOCK TABLES `schedule_tuesday` WRITE;
/*!40000 ALTER TABLE `schedule_tuesday` DISABLE KEYS */;
INSERT INTO `schedule_tuesday` VALUES (5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0');
/*!40000 ALTER TABLE `schedule_tuesday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_wednesday`
--

DROP TABLE IF EXISTS `schedule_wednesday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_wednesday` (
  `schedule` int(11) NOT NULL,
  `wednesday` bit(1) DEFAULT NULL,
  KEY `FK_evdncbe8o12cw1q9ipo76o7hf` (`schedule`),
  CONSTRAINT `FK_evdncbe8o12cw1q9ipo76o7hf` FOREIGN KEY (`schedule`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_wednesday`
--

LOCK TABLES `schedule_wednesday` WRITE;
/*!40000 ALTER TABLE `schedule_wednesday` DISABLE KEYS */;
INSERT INTO `schedule_wednesday` VALUES (5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5366,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5367,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5368,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5369,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5370,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0'),(5371,'\0');
/*!40000 ALTER TABLE `schedule_wednesday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_profile`
--

DROP TABLE IF EXISTS `social_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `social_profile` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `nick` varchar(255) DEFAULT NULL,
  `profile_link` varchar(255) DEFAULT NULL,
  `social_network` varchar(255) DEFAULT NULL,
  `actor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_sbgl75onamh648sm42gak0vok` (`actor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_profile`
--

LOCK TABLES `social_profile` WRITE;
/*!40000 ALTER TABLE `social_profile` DISABLE KEYS */;
INSERT INTO `social_profile` VALUES (5362,0,'nickstudent1','http://www.linkstudent1.com','socialnetworkstudent1',5307),(5363,0,'nickstudent2','http://www.linkstudent2.com','socialnetworkstudent2',5308),(5364,0,'nickteacher1','http://www.linkteacher1.com','socialnetworkteacher1',5301),(5365,0,'nickteacher2','http://www.linkteacher2.com','socialnetworkteacher2',5302);
/*!40000 ALTER TABLE `social_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `spammer` bit(1) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  `finder` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_chfh7xwu983n9gc06715xkc0i` (`user_account`),
  KEY `studentUK_k77r9upyn4vc6rupdcqapbko5` (`spammer`),
  KEY `FK_ma6lkckquup6k9py2sym3xste` (`finder`),
  CONSTRAINT `FK_chfh7xwu983n9gc06715xkc0i` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FK_ma6lkckquup6k9py2sym3xste` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (5307,1,'Reina Mercedes','garcia@gmail.es','student1','+34647307406','http://tinyurl.com/picture.png','\0',5281,5324),(5308,1,'Reina Mercedes','lanzas@gmail.es','student2','+34647307406','http://tinyurl.com/picture.png','\0',5282,5325),(5309,1,'Avd de la Paz','cejas@gmail.es','student3','+34424424424','http://tinyurl.com/picture.png','',5283,5326);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description_en` varchar(255) DEFAULT NULL,
  `description_es` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `name_en` varchar(255) DEFAULT NULL,
  `name_es` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (5357,0,'Description1','Descripcion1','1ESO','Subject1','Asignatura1'),(5358,0,'Description2','Descripcion2','2ESO','Subject2','Asignatura2'),(5359,0,'Description3','Descripcion3','1BACH','Subject3','Asignatura3'),(5360,0,'Description4','Descripcion4','2BACH','Subject4','Asignatura4'),(5361,0,'Description5','Descripcion5','2BACH','Subject5','Asignatura5');
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `spammer` bit(1) DEFAULT NULL,
  `user_account` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cx1vitere593bmwkerm1slh6h` (`user_account`),
  KEY `teacherUK_k77r9upyn4vc6rupdcqapbko5` (`spammer`),
  CONSTRAINT `FK_cx1vitere593bmwkerm1slh6h` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (5301,0,'Reina Mercedes','teacher1@gmail.es','Teacher1','+34647607400','http://tinyurl.com/picture.png','\0',5284),(5302,0,'Reina Mercedes','teacher2@gmail.es','Teacher2','+34647807400','http://tinyurl.com/picture.png','\0',5285),(5303,0,'Reina Mercedes','teacher3@gmail.es','Teacher3','+34647807400','http://tinyurl.com/picture.png','\0',5286),(5304,0,'Reina Mercedes','teacher3@gmail.es','Teacher11','+34647807400','http://tinyurl.com/picture.png','\0',5287),(5305,0,'Reina Mercedes','teacher3@gmail.es','Teacher12','+34647807400','http://tinyurl.com/picture.png','\0',5288),(5306,0,'Reina Mercedes','teacher3@gmail.es','Teacher13','+34647807400','http://tinyurl.com/picture.png','\0',5289);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_period`
--

DROP TABLE IF EXISTS `time_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_period` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `day_number` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `start_hour` int(11) DEFAULT NULL,
  `reservation` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_9e33dsn9kv95qeq483hy8422q` (`reservation`),
  CONSTRAINT `FK_9e33dsn9kv95qeq483hy8422q` FOREIGN KEY (`reservation`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_period`
--

LOCK TABLES `time_period` WRITE;
/*!40000 ALTER TABLE `time_period` DISABLE KEYS */;
INSERT INTO `time_period` VALUES (5451,0,1,20,19,5450),(5452,0,1,17,16,5448),(5453,0,1,18,17,5449),(5454,0,3,9,8,5447);
/*!40000 ALTER TABLE `time_period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_castjbvpeeus0r8lbpehiu0e4` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (5279,0,'e00cf25ad42683b3df678c61f42c6bda','admin1'),(5280,0,'c84258e9c39059a89ab77d846ddab909','admin2'),(5281,0,'5e5545d38a68148a2d5bd5ec9a89e327','student1'),(5282,0,'213ee683360d88249109c2f92789dbc3','student2'),(5283,0,'8e4947690532bc44a8e41e9fb365b76a','student3'),(5284,0,'41c8949aa55b8cb5dbec662f34b62df3','teacher1'),(5285,0,'ccffb0bb993eeb79059b31e1611ec353','teacher2'),(5286,0,'82470256ea4b80343b27afccbca1015b','teacher3'),(5287,0,'ac1fab814f4b8bad93b45df881a19c75','teacher11'),(5288,0,'7aabe66cc596b0869c7cb2737a6f7b67','teacher12'),(5289,0,'3e0b812eec121a8dfaf8bfff266b3fee','teacher13'),(5290,0,'c7acc7970ffab6dcf9422fb98465399d','certifier1'),(5291,0,'99954134cc0cccdf1951c24cd5202d9d','certifier2'),(5292,0,'54b53072540eeeb8f8e9343e71f28176','system');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account_authorities`
--

DROP TABLE IF EXISTS `user_account_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account_authorities` (
  `user_account` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_pao8cwh93fpccb0bx6ilq6gsl` (`user_account`),
  CONSTRAINT `FK_pao8cwh93fpccb0bx6ilq6gsl` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account_authorities`
--

LOCK TABLES `user_account_authorities` WRITE;
/*!40000 ALTER TABLE `user_account_authorities` DISABLE KEYS */;
INSERT INTO `user_account_authorities` VALUES (5279,'ADMIN'),(5280,'ADMIN'),(5281,'STUDENT'),(5282,'STUDENT'),(5283,'STUDENT'),(5284,'TEACHER'),(5285,'TEACHER'),(5286,'TEACHER'),(5287,'TEACHER'),(5288,'TEACHER'),(5289,'TEACHER'),(5290,'CERTIFIER'),(5291,'CERTIFIER'),(5292,'ADMIN');
/*!40000 ALTER TABLE `user_account_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-05  0:01:05
commit;
