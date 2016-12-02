public class Potion extends Item
{
    public Potion (){
        super ();
    }
    
    public void explicate(TextIO t){
        t.clear();
        t.say(name);
        t.blank(1);
        t.say(desc);
        t.blank(1);
        t.say("Health Granted: " + health);
        t.say("Energy Granted: " + energy);
        t.say("Bloodlust Granted: " + bloodlust);
    }
}


