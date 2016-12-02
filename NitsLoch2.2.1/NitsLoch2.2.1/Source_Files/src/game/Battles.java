/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.game;

import src.Constants;
import src.enums.Armor;
import src.enums.DamageType;
import src.enums.Direction;
import src.enums.EnemyBehavior;
import src.enums.ExplosionType;
import src.enums.Facing;
import src.enums.GroundItems;
import src.enums.Weapon;
import src.exceptions.EnemyDiedException;
import src.land.Land;
import src.scenario.MiscScenarioData;

/**
 * This class will control fighting between 2 people in the world.
 * There are a number of fight methods set up.  Each fight method
 * takes 2 parameters, the player/enemy that is attacking, and the
 * player/enemy that is being attacked.  The aggressor player/enemy is
 * always the first parameter.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Battles {

	private static final int PLR_MIN_FIGHTING		= 8;
	private static final int PLR_MIN_MARKSMAN		= 20;
	private static final int PLR_MIN_MARTIAL_ARTS	= 8;

	private static final int ENEMY_MIN_FIGHTING		= 15;
	private static final int ENEMY_MIN_MARKSMAN		= 40;
	private static final int ENEMY_MIN_MARTIAL_ARTS	= 10;

	/**
	 * The fight method for a player attacking an enemy.
	 * @param plr Player : The player that is attacking
	 * @param enemy Enemy : The enemy that is being attacked.
	 */
	public static void fight(Player plr, Enemy enemy) throws EnemyDiedException {
		if(checkForAmmo(plr)){
			enemy.setIsRunning(false);
			enemy.setHasBeenAttacked(true);
			
			if(enemy.getType().getBehavior() == EnemyBehavior.COWARDLY)
				enemy.setIsRunning(true);

			// Rocket launcher treated differently
			if(plr.getReadiedWeapon().usesRockets()){
				GameWorld.getInstance().worldExplode(enemy.getRow(), enemy.getCol(),
						ExplosionType.MAJOR);
				GameWorld.getInstance().triggerWorldUpdate();
				return;
			}

			if(hitTarget(plr, enemy)){
				
				try {
					if(plr.getReadiedWeapon().getDamageType().isFighting())
						src.enums.Sounds.PLAYER_MELEE_ATTACK.playSound();
					else if(plr.getReadiedWeapon().getDamageType().isMarksman())
						src.enums.Sounds.PLAYER_MARKSMAN_ATTACK.playSound();
					else if(plr.getReadiedWeapon().getDamageType().isMartialArts())
						src.enums.Sounds.PLAYER_MART_ARTS_ATTACK.playSound();
					else if(plr.getReadiedWeapon().getDamageType().isFlame())
						src.enums.Sounds.PLAYER_FLAME_ATTACK.playSound();
					else if(plr.getReadiedWeapon().getDamageType().isOther())
						src.enums.Sounds.PLAYER_OTHER_ATTACK.playSound();
				} catch (Exception ex) { }
				
				Messages msg = Messages.getInstance();
				int damageAmount = getDamage(plr, enemy);
				msg.addMessage("You " + plr.getReadiedWeapon().getVerb() + 
						((enemy.getType().getIsProperNoun()) ? " " : " the ") + 
								enemy.getName() + " with " + plr.getReadiedWeapon().getSecondaryName() +
						" for " + damageAmount + " hit points.");

				if(enemy.drainLife(damageAmount)){ // Enemy dies
					Land land = GameWorld.getInstance().getLand()
					[enemy.getRow()][enemy.getCol()];
					msg.addMessage("You have killed " +
							((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + ".");

					// If killed a shopkeeper, set caught stealing to false.
					if(enemy.getType().getIsShopkeeper())
						plr.setCaughtStealing(false);

					// Increase hit points and heal (if enemy's ability isn't too low compared to the player's)
					if(enemy.getAbility() >= (.5 * plr.getCurrentAbility())) {
						plr.setMaxHP(plr.getMaxHitPoints() + Constants.HP_BONUS);
						plr.heal(src.Constants.HP_BONUS);
						msg.addMessage("You gain " + Constants.HP_BONUS + " hit points");
					}

					// Add ability points (if enemy's ability isn't too low compared to the player's)
					if(enemy.getAbility() >= (.2 * plr.getCurrentAbility())) {
						int increase = 1;
						if(Math.random() < .5) increase = 2;
						plr.addAbilityPoints(increase);
					}

					if(plr.getReadiedWeapon().getDamageType().destroysMoney()){
						msg.addMessage("You burned up " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") 
								+ enemy.getName() + "'s money");
					}
					else {
						plr.addMoney(enemy.getMoney());
						msg.addMessage("You found " + enemy.getMoney() + " dollars on " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") +
								enemy.getName() + ".");
					}

					// Add armor and weapon
					if(enemy.getArmor() == Armor.A_00 &&
							enemy.getWeapon() == Weapon.W_00){
						msg.addMessage("You found no weapon or armor on " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + ".");
					}
					else { // Enemy has at least some armor or weapon
						if(enemy.getArmor() != Armor.A_00){
							msg.addMessage("You found " + 
									enemy.getArmor().getSecondaryName() + " on  " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + ".");
							plr.addArmor(enemy.getArmor());
						}
						else{
							msg.addMessage("You found no armor on  " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + ".");
						}

						// Add weapon
						int weaponLevel = 1;
						if(enemy.getAdvanced() > 1 && Math.random() < Constants.CHANCE_FIND_ADV_WEAPON)
							weaponLevel = enemy.getAdvanced();
						if(enemy.getWeapon() != Weapon.W_00){
							plr.addWeapon(enemy.getWeapon(), weaponLevel);
							msg.addMessage("You found " + enemy.getWeapon().getSecondaryName() + " on  " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") +
									enemy.getName() + ".");
						}
						else {
							msg.addMessage("You found no weapon on  " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + ".");
						}
					}
					
					// Random chance to drop random item.
					double rand = Math.random();
					
					if(rand < MiscScenarioData.SPAWN_CHANCE_ITEM){ // Spawn an item.
						double rand2 = Math.random();
						
						if(rand2 < .33){
							land.addItem(new Item(GroundItems.BANDAIDS, 0));
						}
						else if(rand2 < .66){
							land.addItem(new Item(GroundItems.BULLETS, 0));
						}
						else{
							int moneyAmt = (int) (Math.random() * 30.0) + 10;
							land.addItem(new Item(GroundItems.MONEY, moneyAmt));
						}
					}

					// Clear enemy from the world and create a grave
					land.setEnemy(null);
					if(land.getItem() == null)
						land.setItem(new Item(GroundItems.GRAVE, 0));
					
					throw new EnemyDiedException();
				} // end enemy dies
				else {
					if(enemy.getHitPoints() < MiscScenarioData.RUN_HP_AMOUNT && 
							Math.random() < MiscScenarioData.RUN_CHANCE){
						enemy.setIsRunning(true);
					}
				}
			}
			else{
				Messages.getInstance().addMessage("You missed " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName());
			}
		}

		//GameWorld.getInstance().triggerWorldUpdate();
	}
	
	/**
	 * Checks to see if the player deals a critical hit or stuns the enemy.
	 * If a critical hit is dealt, it will return the modified damage amount.
	 * If the enemy is stunned, it will set the enemy's stunned variable and
	 * return the normal damage amount.  If nothing happens, it will just
	 * return the regular damage amount given.
	 * @param baseDamage int : base damage
	 * @param plr Player : the player dealing damage
	 * @param enemy Enemy : the enemy taking damage
	 * @return int : damage amount
	 */
	private static int modifyBaseDamage(int baseDamage, Player plr, Enemy enemy){
		int newDamage = baseDamage;
		int chance = plr.getCurrentAbility()/2 - enemy.getAbility()/2 + 10;
		if(chance > 33) chance = 33;
		if(chance > Math.random() * 100){
			if(plr.getReadiedWeapon().getDamageType().isFighting()){
				newDamage += baseDamage * .2;
				Messages.getInstance().addMessage("Critical hit!");
				return newDamage;
			}
			else if(plr.getReadiedWeapon().getDamageType().isMartialArts()){
				enemy.setStunned(true);
				Messages.getInstance().addMessage("You stunned  " +
								((enemy.getType().getIsProperNoun()) ? "" : " the ") + enemy.getName() + "!");
				return baseDamage;
			}
			else if(plr.getReadiedWeapon().getDamageType().isMarksman()) {
				newDamage += baseDamage * .15;
				Messages.getInstance().addMessage("Critical hit!");
				return newDamage;
			}
		}
		return baseDamage;
	}

	/**
	 * Calculates the damage based on the player's attacking damage type
	 * and the enemy's armor.
	 * @param plr Player : the player that is attacking
	 * @param enemy Enemy : the enemy that is being attacked
	 * @return int : damage amount
	 */
	private static int getDamage(Player plr, Enemy enemy){
		int baseDamage = plr.getReadiedWeapon().getDamage() * 
			plr.getWeaponLevel(plr.getReadiedWeapon());
		
		baseDamage = modifyBaseDamage(baseDamage, plr, enemy);
		
		double randomDamageBase = baseDamage / 3.0;
		double randomDamage = randomDamageBase * Math.random();
		if(Math.random() > .5) randomDamage *= -1;
		int rawDamageAmount = (int)(baseDamage + randomDamage);
		int armorAmount = enemy.getArmor().getAbsorb(
				plr.getReadiedWeapon().getDamageType());
		int damageAmount = (int)(rawDamageAmount - (rawDamageAmount * 
				(armorAmount / 100.0)));
		return damageAmount;
	}

	/**
	 * Calculates the damage based on the enemy's attacking damage type
	 * and the player's armor.
	 * @param enemy Enemy : the enemy that is attacking
	 * @param plr Player : the player that is being attacked
	 * @return int : damage amount
	 */
	private static int getDamage(Enemy enemy, Player plr){
		int baseDamage = enemy.getWeapon().getDamage();
		double randomDamageBase = baseDamage / 3.0;
		double randomDamage = randomDamageBase * Math.random();
		if(Math.random() > .5) randomDamage *= -1;
		int rawDamageAmount = (int)(baseDamage + randomDamage);
		int armorAmount = plr.getReadiedArmor().getAbsorb(
				enemy.getWeapon().getDamageType());
		int damageAmount = (int)(rawDamageAmount - (rawDamageAmount * 
				(armorAmount / 100.0)));
		return damageAmount;
	}

	/**
	 * The fight method for an enemy attacking a player.
	 * @param enemy Enemy : The enemy that is attacking.
	 * @param plr Player : The player that is being attacked.
	 */
	public static void fight(Enemy enemy, Player plr){
		if(plr.getHitPoints() <= 0) return;
		/*if(enemy.getType().getBehavior() == EnemyBehavior.INNOCENT){
			if(!enemy.getHasBeenAttacked()) return;
		}*/
		
		if(enemy.getStunned()){
			enemy.setStunned(false);
			return;
		}

		if(hitTarget(enemy, plr)){
			
			try {
				if(enemy.getWeapon().getDamageType().isFighting())
					src.enums.Sounds.ENEMY_MELEE_ATTACK.playSound();
				else if(enemy.getWeapon().getDamageType().isMarksman())
					src.enums.Sounds.ENEMY_MARKSMAN_ATTACK.playSound();
				else if(enemy.getWeapon().getDamageType().isMartialArts())
					src.enums.Sounds.ENEMY_MART_ARTS_ATTACK.playSound();
				else if(enemy.getWeapon().getDamageType().isFlame())
					src.enums.Sounds.ENEMY_FLAME_ATTACK.playSound();
				else if(enemy.getWeapon().getDamageType().isOther())
					src.enums.Sounds.ENEMY_OTHER_ATTACK.playSound();
			} catch (Exception ex) { }
			
			int damageAmount = getDamage(enemy, plr);
			damageAmount = damageAmount + (int)(damageAmount * 
					(MiscScenarioData.DAMAGE_SCALE * (enemy.getAdvanced()-1)));
			Messages.getInstance().addMessage(((enemy.getType().getIsProperNoun()) ? "" : "The ") + enemy.getName() +
					" " + enemy.getWeapon().getVerb()+ " you with " + 
					enemy.getWeapon().getSecondaryName() + " for " + 
					damageAmount + " hit points.");
			plr.drainLife(damageAmount);

			// Break weapon
			if(enemy.getWeapon().getDamageType().breaksWeapons() && 
					plr.getReadiedWeapon().canBeBroken()){
				if(enemy.getAbility()/2 - getPlrAbil(enemy, plr)/2 + 10 > Math.random() * 100){ // Enemy breaks weapon
					Messages.getInstance().addMessage(
					"He hit your weapon and broke it.");
					plr.removeWeapon(plr.getReadiedWeapon());
					plr.setReadiedWeapon(Weapon.W_00);
				}
			}

			// Break armor
			if(enemy.getWeapon().getDamageType().destroysArmor() && 
					plr.getReadiedArmor().canBeDestroyed()){
				if(enemy.getAbility() - getPlrAbil(enemy, plr) + 10 > Math.random() * 100){ // Enemy breaks weapon
					Messages.getInstance().addMessage(
					"Your armor has been destroyed.");
					plr.removeArmor(plr.getReadiedArmor());
					plr.setReadiedArmor(Armor.A_00);
				}
			}

			// Thief stuff
			if(enemy.isThief()){
				if(Math.random() < src.Constants.STEAL_PERCENT){
					Messages.getInstance().addMessage(((enemy.getType().getIsProperNoun()) ? "" : "The ") + enemy.getName() + 
							" just stole your money!");
					enemy.setMoney(enemy.getMoney() + plr.getMoney());
					plr.removeMoney();
					enemy.setIsRunning(true);
				}
			}
		}
		else {
			Messages.getInstance().addMessage(((enemy.getType().getIsProperNoun()) ? "" : "The ") + enemy.getName() +
			" missed you.");
		}
	}

	/**
	 * Checks to see whether or not the player successfully hits the
	 * enemy.
	 * @param plr Player : the player that is attacking
	 * @param enemy Enemy : the enemy that is being attacked
	 * @return boolean : hit target
	 */
	private static boolean hitTarget(Player plr, Enemy enemy){
		int diffAbil;
		boolean hitTarget = false;

		diffAbil = plr.getCurrentAbility()/2 - enemy.getAbility()/2;
		diffAbil += 40;

		// Make sure there's some chance of hitting the enemy.
		if(plr.getReadiedWeapon().getDamageType().isFighting() && 
				diffAbil < PLR_MIN_FIGHTING) 
			diffAbil = PLR_MIN_FIGHTING;
		else if(plr.getReadiedWeapon().getDamageType().isMarksman() && 
				diffAbil < PLR_MIN_MARKSMAN) 
			diffAbil = PLR_MIN_MARKSMAN;
		else if(plr.getReadiedWeapon().getDamageType().isMartialArts() && 
				diffAbil < PLR_MIN_MARTIAL_ARTS) 
			diffAbil = PLR_MIN_MARTIAL_ARTS;
		else if(plr.getReadiedWeapon().getDamageType().isFlame() ||
				plr.getReadiedWeapon().getDamageType().isOther())
			diffAbil = src.Constants.FLAME_HIT_PERCENT;
		if((int)(Math.random() * 100.0) < diffAbil) hitTarget = true;

		return hitTarget;
	}
	
	/**
	 * Gets the player's ability based on the type of weapon the
	 * enemy is using.  So, if the enemy is using a melee weapon,
	 * it will return the fighting ability of the player.  If the
	 * enemy's weapon is martial arts, it will return the martial
	 * arts ability of the player.
	 * @param enemy Enemy : the attacking enemy
	 * @param plr Player : the player
	 * @return int : player's ability
	 */
	private static int getPlrAbil(Enemy enemy, Player plr){
		int playerAbil = 0;
		DamageType damType = enemy.getWeapon().getDamageType();
		if(damType.isFighting() || damType.isOther()) playerAbil = plr.getFightingAbility();
		else if(damType.isMarksman()) playerAbil = plr.getMarksmanAbility();
		else if(damType.isMartialArts()) playerAbil = plr.getMartialArtsAbility();
		return playerAbil;
	}

	/**
	 * Checks to see whether or not the enemy successfully hits the
	 * player.
	 * @param enemy Enemy : the enemy that is attacking
	 * @param plr Player : the player that is being attacked
	 * @return boolean : hit target
	 */
	private static boolean hitTarget(Enemy enemy, Player plr){
		boolean hitTarget = false;

		//int diffAbil = enemy.getAbility()/2 - plr.getCurrentAbility()/2;
		int playerAbil = getPlrAbil(enemy, plr);
		
		int diffAbil = enemy.getAbility()/2 - playerAbil/2;
		diffAbil += 40;

		// Make sure there's some chance of hitting the player.
		if(enemy.getWeapon().getDamageType().isFighting() && 
				diffAbil < ENEMY_MIN_FIGHTING) 
			diffAbil = ENEMY_MIN_FIGHTING;
		if(enemy.getWeapon().getDamageType().isMarksman() && 
				diffAbil < ENEMY_MIN_MARKSMAN) 
			diffAbil = ENEMY_MIN_MARKSMAN;
		if(enemy.getWeapon().getDamageType().isMartialArts() && 
				diffAbil < ENEMY_MIN_MARTIAL_ARTS) 
			diffAbil = ENEMY_MIN_MARTIAL_ARTS;
		if(enemy.getWeapon().getDamageType().isFlame())
			diffAbil = src.Constants.FLAME_HIT_PERCENT;
		if((int)(Math.random() * 100.0) < diffAbil) hitTarget = true;

		return hitTarget;
	}


	/**
	 * Checks to see which type of weapon the player is using, and
	 * makes sure the palyer has enough ammo to use it.  If the
	 * player is out of ammo, it will print a message to the messages
	 * window and will return false.
	 * @return boolean : has ammo
	 */
	private static boolean checkForAmmo(Player plr){
		DamageType currentDamType = plr.getReadiedWeapon().getDamageType();
		if(currentDamType.isMarksman()){
			if(plr.getNumBullets() > 0){
				plr.removeBullets(1);
				return true;
			}
			else {
				Messages.getInstance().addMessage("You are out of bullets.");
				return false;
			}
		}
		if(plr.getReadiedWeapon().usesFlamePacks()){
			if(plr.getNumFlamePacks() > 0){
				plr.removeFlamePacks(1);
				return true;
			}
			else {
				Messages.getInstance().addMessage("You are out of flame packs.");
				return false;
			}
		}
		else if(plr.getReadiedWeapon().usesRockets()){
			if(plr.getNumRockets() > 0){
				plr.removeRockets(1);
				return true;
			}
			else {
				Messages.getInstance().addMessage("You are out of rockets.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Player shoots in specified direction.
	 * @param plr Player : the player that is shooting
	 * @param direction Direction : direction to shoot in
	 */
	public static void shoot(Player plr, Direction direction) throws EnemyDiedException {
		setFacing(plr, direction);
		
		int row = plr.getRow();
		int col = plr.getCol();

		int worldRow, worldCol;
		Land worldCell;

		plr.setIsShooting(false);

		if(!checkForAmmo(plr)) return;

		for(int i = 0; i <= 2; i++){
			try {
				worldRow = GameWorld.getInstance().getRowByDirection(direction, row);
				worldCol = GameWorld.getInstance().getColByDirection(direction, col);
				worldCell = GameWorld.getInstance().getLandAt(worldRow, worldCol);

				if(worldCell.stopsProjectiles()){
					Messages.getInstance().addMessage("You shoot at nothing");
					return;
				}
				else if(worldCell.getEnemy() != null){
					plr.addBullets(1); // Temporarily add the bullet back
					try {
						fight(plr, worldCell.getEnemy());
					} catch(EnemyDiedException ex) {
						throw new EnemyDiedException();
					}
					return;
				}

				row = worldRow;
				col = worldCol;
			} catch(NullPointerException ex){
				return;
			}
		}
	}
	
	/**
	 * Sets the facing based on the direction he is shooting in.
	 * @param plr Player : the player
	 * @param direction Direction : the direction of the shot.
	 */
	private static void setFacing(Player plr, Direction direction) {
		if(direction == Direction.EAST || direction == Direction.NORTHEAST ||
				direction == Direction.SOUTHEAST)
			plr.setFacing(Facing.RIGHT);
		else if(direction == Direction.WEST || direction == Direction.NORTHWEST ||
				direction == Direction.SOUTHEAST)
			plr.setFacing(Facing.LEFT);
	}

	/**
	 * Player shoots a rocket in the specified direction.
	 * @param plr Player : shooting player
	 * @param direction Direction : direction the player is shooting in.
	 */
	public static void shootRocket(Player plr, Direction direction){
		setFacing(plr, direction);
		
		int row = plr.getRow();
		int col = plr.getCol();

		int worldRow, worldCol;
		Land worldCell;

		plr.setIsShooting(false);

		if(!checkForAmmo(plr)) return;

		for(int i = 0; i <= 3; i++){
			try {
				worldRow = GameWorld.getInstance().getRowByDirection(direction, row);
				worldCol = GameWorld.getInstance().getColByDirection(direction, col);
				worldCell = GameWorld.getInstance().getLandAt(worldRow, worldCol);

				if(worldCell.stopsProjectiles() || worldCell.getEnemy() != null){
					GameWorld.getInstance().worldExplode(worldRow, worldCol, ExplosionType.MAJOR);
					return;
				}

				row = worldRow;
				col = worldCol;
			} catch(NullPointerException ex){
				return;
			}
		}
	}

}
