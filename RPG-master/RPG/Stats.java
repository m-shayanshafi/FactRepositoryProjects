public class Stats
{
    int health; //current health
    int base_health; //max health
    int bloodlust; //current bloodlust
    int base_bloodlust; //max bloodlust
    int defense; //defense strength
    int base_energy; //max energy
    int energy; //current energy
    int attack; //attack strength
    String name; //name of entity
    int base = 100; //base level for stats, used to generate all numbers
    public Stats(String x) //take in name
    {
        health = base; //set all stats to the base
        bloodlust = base;
        defense = base;
        energy = base;
        attack = base;
        name = x; //set name
    }
    public Stats(String x, int xbase) //overload method with a variable to edit the base stat (used to adjust enemy difficulty)
    {
        base = xbase; // set base to explicit param
        health = base; //set stats
        bloodlust = base;
        defense = base;
        energy = base;
        attack = base;
        name = x; //set name
    }
    public String getName() //accessors for instance fields
    {
        return name;
    }

    public int getHealth()
    {
        return health;    
    }

    public int getAttack()
    {
        return attack;
    }

    public int getBloodlust()
    {
        return bloodlust;    
    }

    public int getDefense()
    {
        return defense;    
    }

    public int getEnergy()
    {
        return energy;    
    }

    public void setHealth(int n) //mutators for instance fields
    {
         if (n < base_health) health = n; //make sure health doesn't go over max
        else health = base_health;
    }

    public void setBloodlust(int n)
    {
       if (n < base_bloodlust) bloodlust = n;
        else bloodlust = bloodlust;
    }  

    public void setDefense(int n)
    {
        defense = n;
    }  

    public void setEnergy(int n)
    {
        if (n < base_energy) energy = n;
        else energy = base_energy;
    }

    public void setAttack(int n)
    {
        attack = n;
    }

    public void takeHealthDamage(int amount) //take amount damage to health
    {
        if (health - amount > 0) health = health - amount;
        else health = 0;

    }

    public void updateStats(Entity a) //update stats based on current loadout's properties
    {
        attack = base + a.getWeapon().getAttack() + a.getHat().getAttack() + a.getRing().getAttack();
        defense = base + a.getArmor().getDefense() + a.getHat().getDefense() + a.getRing().getDefense();
        base_health = base + a.getRing().getHealth() + a.getHat().getHealth() + a.getArmor().getHealth();
        bloodlust = base + a.getRing().getBloodlust() + a.getHat().getBloodlust();
        base_energy = base + a.getRing().getEnergy() + a.getHat().getEnergy();
    }
}
