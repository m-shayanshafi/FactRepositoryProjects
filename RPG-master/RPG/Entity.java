import java.util.*;
import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Entity extends JComponent//this class is used for every living thing in the game
{
    protected Weapon weapon; //entities possess certain items
    protected Hat hat;
    protected volatile Point position =  new Point();
    protected Potion potion;
    protected Ring ring;
    protected Armor armor;
    protected BufferedImage curr_sprite;
    protected Stats stats; // object containing the stats used for damage calculations
    Random random = new Random(); //instance of random
    protected BufferedImage sprites[] = new BufferedImage[4]; //4 sprites: 0 = motionless, 1 = walking left, 2 = walking right, 3 = low health
    public Entity() //constructor (handled in subclasses)
    {

    }

    public void setSprites(String[] sprite_locs)
    {
        try {
            int count = 0;
            for(String s: sprite_locs)
            {
                sprites[count] = util.resize_img(new Dimension(125, 125), ImageIO.read(new File(s)));
                count++;
            }
        } catch (IOException e) {
        }
    }

    public Weapon getWeapon() //return items
    {
        return this.weapon;
    }

    public Hat getHat()
    {
        return this.hat;
    }

    public Potion getPotion()
    {
        return this.potion;
    }

    public Ring getRing()
    {
        return this.ring;  
    }

    public Armor getArmor()
    {
        return this.armor;
    }

    public Stats getStats() //return stats object
    {
        return this.stats;
    }

    public void consume(Potion p) //consume a potion, add its stat boosts to current stats
    {
        this.stats.setHealth(this.stats.getHealth() + p.getHealth());
        this.stats.setEnergy(this.stats.getEnergy() + p.getEnergy());
        this.stats.setBloodlust(this.stats.getBloodlust() + p.getBloodlust());
    }

    public int attack (Entity a) //attack another entity
    {
        int damage = 10 * (this.getStats().getAttack() / a.getStats().getDefense()); //dmg calculations
        int critchance = random.nextInt(11); //random chance, causes some uncertainty in damage number
        if (critchance == 1){
            System.out.println("Critical Strike");
            return (2*damage);
        }
        else{
            damage += random.nextInt(10);
        }
        a.getStats().takeHealthDamage(damage); //get stats object of victim, decrease health
        return damage; //return amount of damage taken
    }

    public Item replace(Item x) //replace item in loadout to one from inventory
    {
        Item old = null;
        if (x instanceof Armor) //if x is an instance of Armor
        {
            old = armor; //set old to the existing item
            armor = (Armor) x; //cast x as Armor and set it to armor
        }
        else if (x instanceof Potion)
        {
            old = potion;
            potion = (Potion) x;
        }
        else if (x instanceof Weapon)
        {
            old = weapon;
            weapon = (Weapon) x;
        }
        else if (x instanceof Hat)
        {
            old = hat;
            hat = (Hat) x;
        }
        else if (x instanceof Ring)
        {
            old = ring;
            ring = (Ring) x;
        }
        return old; //return the old item
    }

    public void drop() //drop items, add to inventory (used when enemies are killed)
    {
        Inventory.addItem(weapon); //call addItem method which adds them to the array
        Inventory.addItem(ring);
        Inventory.addItem(armor);
        Inventory.addItem(hat);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setClip((int)position.getX(), (int)position.getY(), get_curr_sprite().getWidth(), get_curr_sprite().getHeight());
        g2.drawImage(get_curr_sprite(), (int)position.getX(), (int)position.getY(), this);
    }

    public BufferedImage get_curr_sprite()
    {
        return curr_sprite;
    }

    public void moveTo(Point p, int duration)
    {
        moveThreader m = new moveThreader(this, p, duration);

    }

    public void set_curr_sprite(int index)
    {
        curr_sprite = sprites[index];
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(int x, int y)
    {
        position.setLocation(x,y);
        this.repaint();
    }
}
