/*
 * Add a system to calculate health, health regen, and resistances.
 */
public class Armor extends Item
{
    public Armor (){
        super ();
    }
    
    public void explicate(TextIO t){
        t.clear();
        t.say(name);
        t.blank(1);
        t.say(desc);
        t.blank(1);
        t.say("Health: " + health);
        t.say("Armor: " + defense);
    }
}
