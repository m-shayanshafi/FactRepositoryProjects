/*Class defines the unique qualities of the Player
*/
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Player extends Character{

	private boolean bowAttack = false;
	private ArrayList <Item> acquiredItems = new ArrayList<Item>();
	private long shootTime;
	private long lastShot;

public Player(){
	this.name="Link";
	this.id="Link";
	this.x=0;
	this.y=0;
	this.distance=5;
	this.sleeper=60;
	//body=new Rectangle2D.Double(36,31,28.0,34.0);
	this.motion=new Motion(this);
	this.damage = 35;
	this.shootTime = 750;
}

public void paint(Graphics2D g){
	g.setTransform(new AffineTransform());
	g.translate(this.getX(),this.getY());
    g.drawImage(this.image,0,0,this.image.getWidth(null)*2,this.image.getHeight(null)*2,null);
    g.setColor(Color.green);
    //g.draw(this.body);
    g.setTransform(new AffineTransform());
}

public boolean pickUp(Item item) {
	if(item.getID().equals("HealthPotion"))
	{
		if(this.getHealth() + item.getHealth() > 100  &&  this.getHealth() < 100)
		{
			this.setHealth(100);
			System.out.println("Health Potion: +"+item.getHealth());
			return true;
		}
		else if(this.getHealth() == 100){
			return false;
		}
		else {
			this.setHealth(this.health+item.getHealth());
			System.out.println("Health Potion: +"+item.getHealth());
			return true;
		}
	}
	else if(item.getID().equals("StaminaPotion"))
	{
		if(this.getStamina() + item.getStamina() >= 100  &&  this.getStamina() < 100)
		{
			System.out.println("Stamina Potion: +"+item.getStamina());
			this.setStamina(100);
			return true;
		}
		else if(this.getStamina() == 100){
			return false;
		}
		else {
			System.out.println("Stamina Potion: +"+item.getStamina());
			this.setStamina(this.stamina+item.getStamina());
			return true;
		}
	}
	else if(item.getID().equals("Bow"))
	{
		if(!this.acquiredItems.contains(item)) //Link doesn't have a bow yet..
		{
			this.acquiredItems.add(item);
			this.bowAttack = true;
			return true;
		}
	}
	
	return false;
}

public int getDamage() {
	return this.damage;
}
public boolean damage(int damage){
	if(this.health - damage >0) //link lives
	{
		this.health-=damage;
		return false;
	}
	else //link dies
	{
		this.health=0;
		return true;
	}
}
public void checkHitBox(Character knight)
{
	int xDist = this.getX() - knight.getX();
	int yDist = this.getY() - knight.getY();
	

	
	if( (this.getDirection().equals("Down") && xDist <= 30 && xDist >= -30 && yDist <=0 && yDist >=-55) //player looking down
		|| (this.getDirection().equals("Up")&& xDist <= 30 && xDist >= -30 && yDist <=50 && yDist >=-10) //up
		|| (this.getDirection().equals("Right")&& xDist <= 0 && xDist >= -45 && yDist <=20 && yDist >=-40)//right
		|| (this.getDirection().equals("Left")&& xDist <= 45 && xDist >= 0 && yDist <=20 && yDist >=-40))//left
	{
		knight.setAttackable(true);
	}
	else
	{
		knight.setAttackable(false);
	}

}
	//checks an item for collision with the character and acts accordingly
	public boolean checkItem(Item item){
		int xDist = this.getX() - item.getX();
		int yDist = this.getY() - item.getY();
		
		if (xDist >= -55 && xDist <=-15 && yDist >=-65 && yDist <= -20) // item in range
		{
			boolean itemUsed = false;
			itemUsed = this.pickUp(item);
			
			if(itemUsed){
				return true;
			}
		}
		else //not in range
		{
		}
		return false;
	}
	
	public boolean attack(Character character, ArrayList<Character> characters)
	{
		for(int i=0;i<characters.size();i++)
        {
        	if(characters.get(i).getID().equals("Knight"))
        	{
        		Knight knight = (Knight) characters.get(i);
        		if(knight.getAttackable() == true)
        		{
        			boolean deadKnight = knight.damage(this.getDamage());
        			if(deadKnight)
        			{
        				characters.remove(knight);
        				return true;
        			}
        		}
        	}
        }
		return false;
	}
	
	public void shoot(Player player, ArrayList<Arrow> arrows)
	{
			Arrow arrow = new Arrow(arrows, this.direction, this.getX(), this.getY());
			arrows.add(arrow);
			player.setLastShot(System.currentTimeMillis());
	}
	
	public void setBowAttack(boolean bool)
	{
		this.bowAttack = bool;
	}
	public boolean getBowAttack()
	{
		return this.bowAttack;
	}
	public long getShootTime() {
		return this.shootTime;
	}
	public void setShootTime(long time)
	{
		this.shootTime = time;
	}
	public long getLastShot()
	{
		return this.lastShot;
	}
	public void setLastShot(long time)
	{
		this.lastShot = time;
	}
}