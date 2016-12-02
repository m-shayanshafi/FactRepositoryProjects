import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class HealthPotion extends Item{
	
	public HealthPotion()
	{
		this.id = "HealthPotion";
		//if no health is specified, set to 25
		this.health = 25;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public HealthPotion(int health)
	{
		this.id = "HealthPotion";
		this.health = health;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public HealthPotion(int x, int y)
	{
		this.id = "HealthPotion";
		this.health = 25;
		this.x = x;
		this.y = y;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public HealthPotion(int x, int y, int health)
	{
		this.id = "HealthPotion";
		this.health = health;
		this.x = x;
		this.y = y;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	
	public void paint(Graphics2D g){
		g.setTransform(new AffineTransform());
		g.translate(this.getX(),this.getY());
	    g.drawImage(this.image,0,0,this.image.getWidth(null)*2,this.image.getHeight(null)*2,null);
	    g.setTransform(new AffineTransform());
	}
	
	
	
}
