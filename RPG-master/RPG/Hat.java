public class Hat extends Item
{
    public Hat(){
        super();
    }
    
    public void explicate(TextIO t){
        t.clear();
        t.say(name);
        t.blank(1);
        t.say(desc);
        t.blank(1);
        t.say("Health Granted: " + health);
        t.say("Energy Granted: " + energy);
        t.say("Attack Granted: " + attack);
        t.say("Bloodlust Granted: " + bloodlust);
        t.say("Defense Granted: " + defense);
    }
}
