import java.awt.image.BufferedImage;

public abstract class Item
{
    protected String name;
    protected String desc;
    protected int attack;
    protected int defense;
    protected int health;
    protected int energy;
    protected int bloodlust;
    protected BufferedImage picture;

    public Item (){ 
    }

    public String getName(){
        return name;
    }

    public String getDescription(){ // get description
        return desc;
    }

    public abstract void explicate(TextIO t); //overloaded in each item to let it describe itself

    public int getAttack(){
        return attack;
    }

    public int getDefense(){
        return defense;
    }

    public int getHealth(){
        return health;
    }

    public int getEnergy(){
        return energy;
    }

    public int getBloodlust(){
        return bloodlust;
    }
    
    public BufferedImage getPicture()   {
        return picture;
    }

    public String toString(){
        return name + "\n" + desc;
    }
}
