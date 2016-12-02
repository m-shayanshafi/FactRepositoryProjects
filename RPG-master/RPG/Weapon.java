public class Weapon extends Item
{
    public Weapon(){
        super();
    }
    
    public void explicate(TextIO t){
        t.clear();
        t.say(name);
        t.blank(1);
        t.say(desc);
        t.blank(1);
        t.say("Attack: " + attack);
    }
}
