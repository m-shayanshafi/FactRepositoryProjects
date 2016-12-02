-- =============================================================================
-- Database Name: stigma
-- =============================================================================
USE stigma;

SET FOREIGN_KEY_CHECKS=0;

-- Drop table Accounts
DROP TABLE IF EXISTS `Accounts`;

CREATE TABLE `Accounts` (
  `Account_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT,
  `E_mail` varchar(255) character set utf8 collate utf8_unicode_ci NOT NULL,
  `Password` char(41) character set utf8 collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY(`Account_id`),
  UNIQUE INDEX `E_mail`(`E_mail`)
)
ENGINE=INNODB;

-- Drop table Avatars
DROP TABLE IF EXISTS `Avatars`;

CREATE TABLE `Avatars` (
  `Avatar_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Account_id` int(8) UNSIGNED NOT NULL,
  `Name` varchar(50) character set utf8 collate utf8_unicode_ci NOT NULL,
  `Level` int(3) UNSIGNED NOT NULL,
  `Experience` int(6) UNSIGNED NOT NULL,
  `Safe_Map_Id` int(4) UNSIGNED NOT NULL,
  `Gender` int(1) UNSIGNED NOT NULL,
  `Strength` int(3) UNSIGNED NOT NULL,
  `Willpower` int(3) UNSIGNED NOT NULL,
  `Agility` int(3) UNSIGNED NOT NULL,
  `Finesse` int(3) UNSIGNED NOT NULL,
  `Money` int(8) UNSIGNED NOT NULL,
  PRIMARY KEY(`Avatar_id`),
  UNIQUE INDEX `Name`(`Name`),
  INDEX `Accounts_Avatars`(`Account_id`)
)
ENGINE=INNODB;

ALTER TABLE `Avatars` ADD
  CONSTRAINT `Accounts_Avatars` FOREIGN KEY (`Account_id`)
    REFERENCES `Accounts`(`Account_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

DROP TABLE IF EXISTS `Archetypes`;
    
CREATE TABLE `Archetypes` (
  `Avatar_id` int(8) UNSIGNED NOT NULL,
  `Archetype_id` int(6) UNSIGNED NOT NULL,
  PRIMARY KEY(`Avatar_id`,`Archetype_id`),
)
ENGINE=INNODB; 

ALTER TABLE `Archetypes` ADD
  CONSTRAINT `Archetypes_Avatars` FOREIGN KEY (`Avatar_id`)
    REFERENCES `Avatars`(`Avatar_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

DROP TABLE IF EXISTS `Inventory`;
    
CREATE TABLE `Inventory` (
  `Avatar_id` int(8) UNSIGNED NOT NULL,
  `Item_id` int(6) UNSIGNED NOT NULL,
  `Item_position` int(6) UNSIGNED NOT NULL,
  `Component_id` int(6) UNSIGNED NOT NULL,
  PRIMARY KEY(`Avatar_id`,`Item_id`,`Component_id`),
)
ENGINE=INNODB; 

ALTER TABLE `Inventory` ADD
  CONSTRAINT `Inventory_Avatars` FOREIGN KEY (`Avatar_id`)
    REFERENCES `Avatars`(`Avatar_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
       
SET FOREIGN_KEY_CHECKS=1;