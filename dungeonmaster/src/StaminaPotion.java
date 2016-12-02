import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class StaminaPotion extends Item{
	public StaminaPotion()
	{
		this.id = "StaminaPotion";
		//if no stamina is specified, set to 25
		this.stamina = 25;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public StaminaPotion(int stamina)
	{
		this.id = "StaminaPotion";
		this.stamina = stamina;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public StaminaPotion(int x, int y)
	{
		this.id = "StaminaPotion";
		this.stamina = 25;
		this.x = x;
		this.y = y;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public StaminaPotion(int x, int y, int stamina)
	{
		this.id = "StaminaPotion";
		this.stamina = stamina;
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
