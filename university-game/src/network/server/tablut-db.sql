/* ****************************
	TABLUT-DB

author : Si-Mohamed Lamraoui
date : 27.05.10

*/


CREATE TABLE players (
	id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	pseudo CHAR(20) NOT NULL,
	password CHAR(30) NOT NULL, /* MD5 encoding */
	level INT(3) NOT NULL,
	exp INT(7),
	admin SMALLINT UNSIGNED, /* Is administrateur if  admin=1 else 0 */
    PRIMARY KEY (id)
);


/* TEST */
INSERT INTO players (pseudo, password, level, exp) 
VALUES  ("simo", 	"pass1", 250, 	999999),
	("JojoLeBarjo", "pass2", 24, 	100),
	("beer", 	"pass3", 15, 	1);



