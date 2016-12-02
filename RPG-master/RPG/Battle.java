public class Battle
{
    int turnCount; //The turn number of the battle
    Entity a; //First belligerent entity
    Entity b; //Second belligerent entity
    private static TextIO t = new TextIO(); //instance of TextIO class to use for output
    public Battle (Entity x, Entity y) //contructor, takes in two entities
    {
        turnCount=1; // start off with turn one
        a = x; //assign instance fields to explicit parameters
        b = y;
    }

    public void advanceBattle() //advance one turn
    {

        if (a.getStats().getHealth() > 0 && b.getStats().getHealth() > 0) // if both entities are still alive
        {
            turnCount++; //increment turn
            a.attack(b); //attack each other
            b.attack(a);
        }

    }

    public void advanceBattle(Potion p) // overloaded method, if given a Potion
    {

        if (a.getStats().getHealth() > 0 && b.getStats().getHealth() > 0)
        {
            turnCount++; //increment turn
            a.consume(p); //have the character consume the potion
        }

    }

    public Entity promptUser() // this is run repeatedly, handles turns
    {
        a.getStats().updateStats(a); //update stats for both entities, used in damage calculations
        b.getStats().updateStats(b);
        t.clear(); //clear screen
        System.out.println("This is turn " + turnCount); //print data about situation
        System.out.println("You are fighting an evil " + b.getStats().getName()); 
        System.out.println("You have " + a.stats.health + " health.");
        System.out.println("The " + b.getStats().getName() + " has " + b.stats.health + " health." );
        if (Inventory.potionExists()) //if there exists a potion item in the inventory
        {
            if (t.getInt("Use potion or attack? (1/2)") == 1) // if the user chooses to use a potion
            {

                this.advanceBattle((Potion) Inventory.getPotion()); //cast the Item as a Potion and call advanceBattle with it as an explicit param

            }
            else advanceBattle(); // user chose to attack, do that
        }
        else //no potions
        {
            t.getInt("You have no choice but to fight. Enter 1 to continue."); // no potions 
            advanceBattle(); //attack

        }
        if (a.stats.health == 0) return b; //return victor if there is a death, otherwise return null
        else if (b.stats.health == 0) return a;
        else return null;

    }

    public void collect() //collect dropped items from 
    {
        t.clear(); //clear screen
        t.say("Victory!");
        if (t.getInt("Collect dropped items from enemy? (YES-1/NO-2) The items dropped are: \n" + b.getWeapon().getName() + "\n" + b.getArmor().getName() + "\n" + b.getHat().getName() + "\n" + b.getRing().getName() ) == 1)
        /*Ask user whether or not to pick up dropped items, and list said items*/
        {
            b.drop(); //if so, add items to inventory
            t.say("Items collected!");
        }
        else t.say("The items were lost..."); //user chose to not pick up
    }

}