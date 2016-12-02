import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Arrow extends Item{

	public Arrow(ArrayList<Arrow> arrows, String direction, int x, int y)
	{
		this.name = "Arrow";
		this.x = x + 45;
		this.y = y + 30;
		this.id="Arrow";
		this.direction = direction;
		this.setImage(DungeonMaster.getImage(this.getID()+this.getDirection()));
		//this.setImage(DungeonMaster.getImage(this.getID()));
		this.distance = 20;
		this.sleeper = 60;
		this.damage=15;
		this.doing = "Move";
		this.motionBool = true;
		this.doingBool = true;
		this.motion = new Motion(this, arrows);
		
	}
	
	public boolean checkHitBox(Character character)
	{
		Knight knight = (Knight) character;
		int knightX = knight.getX()+45;
		int knightY = knight.getY()+20;

		double distance = Math.sqrt((this.getX()-(knightX)) * 
                (this.getX()-(knightX)) +
	            (this.getY()-(knightY)) *
	            (this.getY()-(knightY)));

		if(distance < 30)
		{
			knight.damage(this.getDamage());
			knight.setShot(true);
			return true; //arrow hit
		}
		
		return false;
	}
	
	
	public void paint(Graphics2D g){
		g.setTransform(new AffineTransform());
		g.translate(this.getX(),this.getY());
	    g.drawImage(this.image,0,0,this.image.getWidth(null)*2,this.image.getHeight(null)*2,null);
	    g.setTransform(new AffineTransform());
	}
}
