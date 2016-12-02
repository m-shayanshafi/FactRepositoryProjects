Story
A necromancer seeks his destiny by travelling across generic desert plains. He was never able to use necromancy correctly and always needed the skull of power to use any necromancy at all. He learns that his power was sealed away by a great evil. He journeys to kill that evil and reclaim his power. 

(Anish writes code for this in main class)

Mechanics
Entity class
vitality (basic health, does not regenerate)
bloodlust (attack power multiplier, increases with consecutive hits, decreases with damage taken, resets every battle)
energy (allows usage of attacks, is, does not reset, can be replenished with potions)
            	name
            	attack()
Character extends Entity
            	Enemy extends Entity  

Weapons
            	Multiple types, have basic damage (decreases enemy’s health) and secondary attribute

Gear
            	Armor (increases defense, maybe vitality)
            	Rings (random attributes)
            	Hats (random attributes)
            	Potions (restores energy/increases bloodlust)

Equip Regions
            	-Only one armor, one hat, and two rings
            	- rest is kept in inventory
            	Weight system

Inventory
            	Can be accessed by user whenever
            	Uses codes to identify items, simple int codes will do, nothing fancy
		

Battle Sequences	                

Displays your stats and then your enemy’s stats
Displays difficulty rating

TURN BASED

Your attack -
Choose a spell to cast - if not enough energy, can use basic attacks.
//calculate multipliers w/ bloodlust
Random change of gaining bloodlust
Energy decreases with spell usage
Factor energy regen/health regen in

Damage dealt to enemy - 
Apply bloodlusted damage, and any secondary effects

Their attack - 
idk sid do this, do u want them to have bloodlust or stuff like that

Damage dealt to you -
Apply their damage (bloodlusted or nah?) and then apply their secondary effects


VISUALS - 
Maybe use font coloring to display vitality (green), bloodlust(red), and energy (yellow) yellow looks bad - but maybe use background or something like that

e.g.
////////////////      100
/////////                  48
///////////////        79


GRAPHICS - 
For final project
USE SWORD AND SWORCERY GRAPHICS. THEY LOOK AMAZING.
not just for cutscenes - for whole game. this will take hella time.
FINDING ITEMS IN GAMEPLAY

Weapons are found through defeating bosses - however, gear and potions can be found in chests along the way.

Potions -
Energy Restore
Health Potion (only 1, kek, as health regen is done through lifesteal effects, or regen from gear)
Enchant Potions (T1)
Enchant Potions (T2)
Enchant Potions (T3) //or maybe just one

ATTRIBUTE SOURCES

Health Regen - Rings, hats, armor
Energy Regen - Rings, hats
Attack - Weapons, Rings, hats
Defense - Armor, Rings, hats

HATS - or I quit

Add attributes to any attribute, but in small quantities. Hats have a 1/10 chance to be UNUSUAL, where they add a higher amount to whatever. Hats are love, hats are life.


ITEM CATEGORIES

WEAPONS
-1 at once
Standard attributes:
Damage (Health reduction on enemy)
Critical Strike (Chance to do 200% damage on a spell)
Life Steal (Regains % of damage done)

ARMOR
-1 at once
Resistance (Multiplies damage by 100/100+Resistance)
Health (Increases maximum health)
Health Regen (Regenerates health over time, in and out of battles)

HATS
-1 at once
Special attributes

RINGS
-2 at once

POTIONS
-Held in inventory
-Modify energy
-Varied effects


ITEM DATABASE

WEAPONS





