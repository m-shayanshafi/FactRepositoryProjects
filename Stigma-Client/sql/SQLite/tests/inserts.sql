-- those passwords are hashed 'qwe'
REPLACE INTO `Accounts` (`Account_id`, `E_mail`, `Password`) VALUES (1, 'mmorpg', 'kgbTf8.eMn5fo');
REPLACE INTO `Accounts` (`Account_id`, `E_mail`, `Password`) VALUES (2, 'rabbit', 'kgbTf8.eMn5fo');
REPLACE INTO `Accounts` (`Account_id`, `E_mail`, `Password`) VALUES (3, 'test',  'kgbTf8.eMn5fo');

REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 1, 1, 'gmok',   1,     0, 1, 0, 10, 11, 12, 13,14);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 2, 1, 'gmok2',  2,    10, 1, 1, 0, 20, 21, 22, 23,24);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 3, 1, 'gmok3',  3,   100, 1, 0, 0, 30, 31, 32, 33,34);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 4, 1, 'gmok4',  4,  1000, 1, 1, 0, 40, 41, 42, 43,44);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 5, 1, 'gmok5',  5, 10000, 1, 0, 50, 51, 52, 53,54);

REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 6, 2, 'bunny',  1,     0, 1, 0, 10, 11, 12, 13,14);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 7, 2, 'bunny2', 2,    10, 1, 1, 20, 21, 22, 23,24);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 8, 2, 'bunny3', 3,   100, 1, 0, 30, 31, 32, 33,34);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES ( 9, 2, 'bunny4', 4,  1000, 1, 1, 40, 41, 42, 43,44);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (10, 2, 'bunny5', 5, 10000, 1, 0, 50, 51, 52, 53,54);

REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (11, 3, 'test',   1,     0, 1, 0, 10, 11, 12, 13,14);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (12, 3, 'test2',  2,    10, 1, 1, 20, 21, 22, 23,24);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (13, 3, 'test3',  3,   100, 1, 0, 30, 31, 32, 33,34);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (14, 3, 'test4',  4,  1000, 1, 1, 40, 41, 42, 43,44);
REPLACE INTO `Avatars` (`Avatar_id`, `Account_id`, `Name`, `Level`, `Experience`, `Safe_Map_Id`, `Gender`, `Strength`,  `Willpower`, `Agility`, `Finesse`, `Money`) VALUES (15, 3, 'test5',  5, 10000, 1, 0, 50, 51, 52, 53,54);
  
REPLACE INTO `Archetypes` (`Avatar_id`, `Archetype_id`) VALUES (12, 1);

REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (1,0,0,1,0);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (1,1,0,1,1);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (1,2,0,1,2);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (6,0,0,1,0);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (6,1,0,1,1);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (6,2,0,1,2);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (7,0,0,1,0);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (7,1,0,1,1);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (7,2,0,1,2);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (11,0,0,1,0);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (11,2,0,1,2);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (12,0,0,1,0);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (12,1,0,1,1);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (12,2,0,1,2);
REPLACE INTO `Inventory` (`Avatar_id`,`Item_id`,`Item_position`,`Item_type`, Item_kind) VALUES (12,3,0,1,1);

REPLACE INTO `Item_Modifiers` (`Avatar_id`,`Item_id`,`Modifier_id`) VALUES (11,0,1);
REPLACE INTO `Item_Effects` (`Avatar_id`,`Item_id`,`Effect_id`) VALUES (11,1,1);