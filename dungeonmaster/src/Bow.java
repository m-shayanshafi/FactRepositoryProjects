import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bow extends Item{
	
	public Bow()
	{
		this.name = "Bow";
		this.id = "Bow";
		this.damage = 30;
		this.setImage(DungeonMaster.getImage(this.getID()));
	}
	public Bow(int x, int y)
	{
		this.name = "Bow";
		this.id = "Bow";
		this.damage = 30;
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
