-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: videogamemanager_main
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
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `release_year` int NOT NULL,
  `total_achievements` int NOT NULL,
  `achievements_completed` int NOT NULL,
  `completion_percentage` decimal(5,2) GENERATED ALWAYS AS ((case when (`total_achievements` = 0) then 0 else ((`achievements_completed` * 100.0) / `total_achievements`) end)) STORED,
  `game_completed` tinyint(1) GENERATED ALWAYS AS ((case when (`total_achievements` = `achievements_completed`) then 1 else 0 end)) STORED,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` (`game_id`, `user_id`, `title`, `release_year`, `total_achievements`, `achievements_completed`) VALUES (1,123,'Call of Duty',2005,20,15),(3,911,'Super Mario Bros',1985,50,50),(4,911,'The Legend of Zelda',1986,30,15),(5,911,'Metroid',1986,40,20),(6,911,'Sonic the Hedgehog',1991,25,25),(7,911,'Street Fighter II',1991,35,20),(8,911,'Mortal Kombat',1992,30,10),(9,911,'Donkey Kong Country',1994,20,20),(16,911,'Minecraft',2011,25,20),(17,911,'Fortnite',2017,50,30),(20,911,'Red Dead Redemption',2010,45,45),(22,911,'Cyberpunk 2077',2020,55,20);
INSERT INTO `games` (`game_id`, `user_id`, `title`, `release_year`, `total_achievements`, `achievements_completed`) VALUES
(23, 911, 'Halo: Combat Evolved', 2001, 20, 15),
(24, 911, 'Half-Life 2', 2004, 25, 20),
(25, 911, 'The Witcher 3', 2015, 40, 35),
(26, 911, 'Bioshock', 2007, 30, 25),
(27, 911, 'Assassin’s Creed', 2007, 35, 30),
(28, 911, 'FIFA 21', 2020, 40, 20),
(29, 911, 'Forza Horizon 4', 2018, 25, 25),
(30, 911, 'Grand Theft Auto V', 2013, 50, 45);

/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-30 16:52:13
