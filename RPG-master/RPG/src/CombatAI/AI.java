package CombatAI;
import Actor.*;
import FightClasses.RandomGenerator;
import Game.TextHandler;
import Items.Intent;
import Items.Item;
import Skills.Skill;

import java.util.Vector;

/**
 * Created by piano_000 on 5/26/2015.
 *
 * TODO: Create more robust AI that can handel status effects like poison and burning
 */
public class AI {
    protected Vector<Actor> heroes;
    protected Vector<Actor> enemies;
    protected int characterTurn;
    RandomGenerator generator;
    protected TextHandler out = TextHandler.getInstance();

    double LOW = .30;

    public AI(Vector<Actor> h, Vector<Actor> e) {
        heroes = h;
        enemies = e;
        generator = new RandomGenerator();
    }

    public void takeTurn(int characterTurn, int greatestThreat) {
        this.characterTurn = characterTurn;
        Boolean heal = healWounded();
        Boolean restore = true;
        if(!heal) {
            restore = restoreTP();
        }

        if(!heal || !restore) {
            //TODO: Replace this with more logic so attacking is a little less random
            int ATTACKTYPES = 4; //This is the total number of use methods
            int randomAttack = generator.getNumberBetween(1, ATTACKTYPES);

            switch(randomAttack) {
                case 1:
                    out.printToConsole("The AI chooses to use the weakest.");
                    enemies.get(characterTurn).attack(heroes.get(seekWeakest()));
                    break;
                case 2:
                    out.printToConsole("The AI chooses to use the strongest.");
                    enemies.get(characterTurn).attack(heroes.get(seekStrongest()));
                    break;
                case 3:
                    out.printToConsole("The AI chooses to use the first living.");
                    enemies.get(characterTurn).attack(heroes.get(seekFirstHero()));
                    break;
                case 4:
                    out.printToConsole("The AI Chooses to attack the biggest threat.");
                    enemies.get(characterTurn).attack(heroes.get(greatestThreat));
            }

        }
    }

    /*
    Find an ally that has less than 30% of their maximum tp. If there is an ally and a restore item exists, restore that ally's tp.
     */
    public Boolean restoreTP() {
        Boolean canRestore = false;
        Actor currentCharacter = enemies.get(characterTurn);
        int restoreIndex = 0;

        int i = 0;
        for(Item item: currentCharacter.getItems()) {
            if(item.getIntent() == Intent.RESTORE){
                canRestore = true;
                restoreIndex = i;
            }
            i++;
        }

        int target = getLowTPAlly();
        if(canRestore) {
            if(target != -1) {
                enemies.get(characterTurn).getItems().get(restoreIndex).use(enemies.get(characterTurn), enemies, target); //Restore the enemy with the lowest tp that is below 30% of max
                return true;
            }
        }
        return false;
    }

    public Boolean healWounded() {
        Boolean useItem = false;
        Boolean useSkill = false;
        Actor currentCharacter = enemies.get(characterTurn);
        int usageIndex = 0;

        //First check if the character has a healing item
        int i = 0;
        for(Item item: currentCharacter.getItems()) {
            if(item.getIntent() == Intent.HEAL){
                useItem = true;
                usageIndex = i;
            }
            i++;
        }

        //If they don't have a healing item, check if they have a healing skill
        int j = 0;
        if(!useItem) {
            for(Skill skill: currentCharacter.getSkills()) {
                if(skill.getIntent() == Intent.HEAL) {
                    useSkill = true;
                    usageIndex = j;
                }
            j++;
            }
        }

        int target = getWeakestAlly();;

        //TODO: Change the healing algorithm to use the item that would heal the character the most, but go over their max health the least.
        if(useItem) {
            //Find the weakest ally
            if(target != -1) {
                enemies.get(characterTurn).getItems().get(usageIndex).use(enemies.get(characterTurn), enemies, target); //Heal the enemy with the lowest health that is below 30% of max
                return true;
            }
        }

        if(useSkill) {
            //Find the weakest ally
            if(target != -1) {
                enemies.get(characterTurn).getSkills().get(usageIndex).use(enemies.get(characterTurn), enemies, target); //Heal the enemy with the lowest health that is below 30% of max
                return true;
            }

        }
        return false;
    }

    public int getLowTPAlly() {
        Actor lowest = null;
        int lowestIndex = 0;


        for (int i = 0; i < enemies.size(); i++) {
            double currentAllyTP = enemies.get(i).getTechniquePoints();
            double currentAllyMaxTP = enemies.get(i).getMaxTechniquePoints();

            if ((currentAllyTP < (currentAllyMaxTP * LOW)) && (lowest == null)) {
                lowest = enemies.get(i);
                lowestIndex = i;
            } else if ((currentAllyTP  < (currentAllyMaxTP * LOW)) && (currentAllyTP < (lowest.getTechniquePoints()))) {
                lowest = enemies.get(i);
                lowestIndex = i;
            } else {
                //NOTHING
            }
        }
        if (lowest != null) {
            return lowestIndex;
        } else {
            return -1; //No living allies TODO: Add error handling
        }
    }

    /*
    Finds the weakest ally; one whose health is below 30% of their maximum health
     */
    public int getWeakestAlly() {
        Actor weakest = null;
        int weakestIndex = 0;

        for (int i = 0; i < enemies.size(); i++) {
            double currentAllyHP = enemies.get(i).getHealth();
            double currentAllyMaxHP = enemies.get(i).getMaxHealth();

            if ((currentAllyHP < (currentAllyMaxHP * LOW)) && (weakest == null)) {
                weakest = enemies.get(i);
                weakestIndex = i;
            } else if ((currentAllyHP  < (currentAllyMaxHP * LOW)) && (currentAllyHP < (weakest.getHealth()))) {
                weakest = enemies.get(i);
                weakestIndex = i;
            } else {
                //NOTHING TODO: Add error handling
            }
        }
        if (weakest != null) {
            return weakestIndex;
        } else {
            return -1; //No living allies TODO: Add error handling
        }
    }

    /*
    --Find the weakest opponent--
    The first person found alive is "the weakest".
    After that if the amount of health left is lower than "the weakest", then this character is the new "the weakest"
    return the index of the weakest
     */
    public int seekWeakest() {

        Actor weakest = null;
        int weakestIndex = 0;

        for (int i = 0; i < heroes.size(); i++) {
            double currentHeroHP = heroes.get(i).getHealth();
            double currentHeroMaxHP = heroes.get(i).getMaxHealth();

            if ((currentHeroHP > 0) && (weakest == null)) {
                weakest = heroes.get(i);
                weakestIndex = i;
            } else if ((currentHeroHP  > 0) && (currentHeroHP < (weakest.getHealth()))) {
                weakest = heroes.get(i);
                weakestIndex = i;
            } else {
                //NOTHING TODO: Add error handling
            }
        }
        if (weakest != null) {
            return weakestIndex;
        } else {
            return -1; //No living opponents TODO: Add error handling
        }
    }

    /*
    --Find the strongest opponent--
    The first person found alive is "the strongest".
    After that if the amount of health left is greater than "the strongest", then this character is the new "the strongest"
    return the index of the weakest
     */
    public int seekStrongest() {

        Actor strongest = null;
        int strongestIndex = 0;

        for (int i = 0; i < heroes.size(); i++) {
            double currentHeroHP = heroes.get(i).getHealth();
            // double currentHeroMaxHP = heroes.get(i).getMaxHealth();

            if ((currentHeroHP > 0) && (strongest == null)) {
                strongest = heroes.get(i);
                strongestIndex = i;
            } else if ((currentHeroHP > 0) && (currentHeroHP > strongest.getHealth())) {
                strongest = heroes.get(i);
                strongestIndex = i;
            } else {
                //NOTHING TODO: Add error handling
            }
        }
        if (strongest != null) {
            return strongestIndex;
        } else {
            return -1; //No living opponents TODO: Add error handling
        }
    }

    /*
    Find the first living hero and use them
     */
    public int seekFirstHero() {

        for(int i = 0; i < heroes.size(); i++) {
            if(heroes.get(i).getHealth() > 0) {
                return i;
            }
        }
        return -1; //No living opponents TODO: Add error handling
    }

    public void setCharacterTurn(int i) {
        characterTurn = i;
    }

    public int getCharacterTurn() {
        return characterTurn;
    }
}
