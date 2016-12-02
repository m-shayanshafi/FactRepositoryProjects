DROP TABLE IF EXISTS Accounts;

CREATE TABLE Accounts (
  Account_id INTEGER PRIMARY KEY AUTOINCREMENT,
  E_mail varchar(255) UNIQUE NOT NULL,
  Password char(41) NOT NULL
);

DROP TABLE IF EXISTS Avatars;

CREATE TABLE Avatars (
  Avatar_id INTEGER PRIMARY KEY AUTOINCREMENT,
  Account_id int(8) NOT NULL,
  Name varchar(50) UNIQUE NOT NULL,
  Level int(3) NOT NULL,
  Experience int(6) NOT NULL,
  Safe_Map_Id int(4) NOT NULL,
  Gender int(1) NOT NULL,
  Strength int(3) NOT NULL,
  Willpower int(3) NOT NULL,
  Agility int(3) NOT NULL,
  Finesse int(3) NOT NULL,
  Money int(8) NOT NULL,
  FOREIGN KEY (Account_id) REFERENCES Accounts (Account_id) 
  							ON DELETE CASCADE ON UPDATE CASCADE  
);

CREATE INDEX IF NOT EXISTS Avatars_Account_id ON Avatars(Account_id);

DROP TABLE IF EXISTS Archetypes;

CREATE TABLE Archetypes (
  Avatar_id INTEGER NOT NULL,
  Archetype_id int(6) NOT NULL,
  PRIMARY KEY(Avatar_id,Archetype_id),
  FOREIGN KEY (Avatar_id) REFERENCES Avatars(Avatar_id) 
  							ON DELETE CASCADE ON UPDATE CASCADE  
);

DROP TABLE IF EXISTS Inventory;

CREATE TABLE Inventory
(
    Avatar_id INTEGER NOT NULL,
    Item_id  INTEGER NOT NULL,
    Item_position INTEGER NOT NULL,
    Item_type  int(6) NOT NULL,
    Item_kind int(6) NOT NULL,
    PRIMARY KEY (Avatar_id , Item_id, Item_type,Item_kind),
    FOREIGN KEY (Avatar_id) REFERENCES Avatars(Avatar_id) 
  							ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS Item_Modifiers;

CREATE TABLE Item_Modifiers
(
    Avatar_id INTEGER NOT NULL,
    Item_id  INTEGER NOT NULL,
    Modifier_id INTEGER NOT NULL,
    PRIMARY KEY (Avatar_id , Item_id, Modifier_id),
    FOREIGN KEY (Avatar_id,Item_id) REFERENCES Avatars(Avatar_id,Item_id) 
  							ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS Item_Effects;

CREATE TABLE Item_Effects
(
    Avatar_id INTEGER NOT NULL,
    Item_id  INTEGER NOT NULL,
    Effect_id INTEGER NOT NULL,
    PRIMARY KEY (Avatar_id , Item_id, Effect_id),
    FOREIGN KEY (Avatar_id,Item_id) REFERENCES Avatars(Avatar_id,Item_id) 
  							ON DELETE CASCADE ON UPDATE CASCADE
);