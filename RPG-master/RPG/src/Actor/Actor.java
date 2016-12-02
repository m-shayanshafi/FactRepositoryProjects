package Actor;

import FightClasses.RandomGenerator;
import Game.Illness;
import Game.Status;
import Game.TextHandler;
import Items.Item;
import Items.Weapon;
import Skills.Skill;

import java.util.Vector;

/**
 * Created by piano_000 on 7/12/2015.
 */
public abstract class Actor {
    protected String name;
    protected double health;
    protected double maxHealth;
    protected int damageMod;
    protected Vector<Item> items = new Vector<Item>();
    protected Weapon weapon;
    protected Vector<Skill> skills = new Vector<Skill>();
    protected int techniquePoints;
    protected int maxTechniquePoints;
    protected Illness illness = Illness.NONE;
    protected Status status = new Status(Status.State.NORMAL,0);
    protected String description;
    protected TextHandler out = TextHandler.getInstance();

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public int addHealth(double h) {
        double overflow = (health + h) - maxHealth; //Excess health that exceeds the max health
        health = health + h;
        int actualHeal;
        if(overflow > 0) {
            actualHeal = (int) (h - overflow);
        } else {
            actualHeal = (int) h;
        }
        if(health > maxHealth) {
            health = health - overflow;
        }
        return actualHeal;
    }

    public void subHealth(double h) {
        health = health - h;
        if(health < 0) {
            health = 0;
        }
    }

    public void setHealth(double h) {
        health = h;
    }

    public void setMaxHealth(double mh) {
        maxHealth = mh;
    }

    public int getDamageMod() {
        return damageMod;
    }

    public Vector<Item> getItems() {
        return items;
    }

    public Vector<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill s) { skills.add(s); }

    public void addItem(Item i) { items.add(i); }

    public void printInventory() {
        for(int i = 0; i < items.size(); i++) {
            out.printToConsole(items.get(i).getName());
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getTechniquePoints() { return techniquePoints; }

    public void setTechniquePoints(int tp) {
        techniquePoints = tp;
    }

    public int getMaxTechniquePoints() { return maxTechniquePoints; }

    public void setMaxTechniquePoints(int mtp) {
        maxTechniquePoints = mtp;
    }

    public void addTechniquePoints(int t) {
        techniquePoints = techniquePoints + t;
    }

    public void subTechniquePoints(int t) {
        techniquePoints = techniquePoints - t;
    }

    public String getDescription() {
        return description;
    }

    public void printStatus() {
        out.printToConsole(this.name + "'s Health: " + health + ", TP: " + techniquePoints);
    }

    public double getCombatDmg() {
        return getDamageMod() + weapon.getDamage();
    }

    public Illness getIllness() {
        return illness;
    }

    public void setIllness(Illness i) {
        illness = i;
    }

    public Status.State getStatus() {
        return status.getState();
    }

    public void setStatus(Status.State s) {
        status.setState(s, -1); //Default to everlasting state
    }

    public void setStatus(Status.State s, int duration) {
        status.setState(s, duration);
    }
    public Status getFullStatus() {
        return status;
    }

    public Boolean hitCalculator() {
        RandomGenerator generator = new RandomGenerator();
        int hit = generator.getNumberBetween(0, 9);
        if(hit < 8) {
            return true;
        } else {
            return false;
        }
    }

    public double attack(Actor opponent) {
        Boolean hit = hitCalculator();
        double threatBuilt = 0;
        if(hit) {
            //Attack lands
            if(opponent.getStatus() == Status.State.GUARDING) { //Guarding halves the amount of damage delivered
                opponent.subHealth(getCombatDmg() / 2);
                threatBuilt = (getCombatDmg() / 2);
                out.printToConsole(opponent.getName() + " guarded against the attack!");
                out.printToConsole(name + " dealt " + (getCombatDmg() / 2) + " damage to " + opponent.getName());
            } else { // Normal Atatck
                opponent.subHealth(getCombatDmg());
                threatBuilt = getCombatDmg();
                out.printToConsole(name + " dealt " + getCombatDmg() + " damage to " + opponent.getName());
            }

            if(opponent.getHealth() <= 0) {
                out.printToConsole(opponent.getName() + " has been felled!");
                opponent.setStatus(Status.State.FAINTED, -1);
            }
        } else {
            //Attack missed
            out.printToConsole(name + " missed their attack!");
        }
        return threatBuilt;
    }
}
