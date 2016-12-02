/*Class defines the unique qualities of a Knight
*/
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Knight extends Character{

	private int initialX;
	private int initialY;
	private long timer;
	private boolean reset;
	private boolean attackable = false;
	private long swingTime;
	private long lastSwing;
	private boolean shot;
	
	public Knight(){
		this.name="Knight";
		this.id="Knight";
		this.direction="Left";
		this.doing="Move";
		this.x=200;
		this.y=200;
		this.distance=3;
		this.sleeper=60;
		//body=new Rectangle2D.Double(29,11,36.0,40.0);
		this.motion=new Motion(this);
		initialX = x;
		initialY = y;
		this.reset = false;
		this.damage = 5;
		this.swingTime = 1000;
		this.shot = false;
	}
	public Knight(int x, int y){
		this.name="Knight";
		this.id="Knight";
		this.direction="Right";
		this.doing="Move";
		this.x=x;
		this.y=y;
		this.distance=3;
		this.sleeper=60;
		body=new Rectangle2D.Double(29,11,36.0,40.0);
		this.motion=new Motion(this);
		initialX = x;
		initialY = y;
		this.reset = false;
		this.damage = 5;
		this.swingTime = 1000;
		this.shot = false;
	}

	public void paint(Graphics2D g){
		g.setTransform(new AffineTransform());
		g.translate(this.getX(),this.getY());
	    g.drawImage(this.image,0,0,this.image.getWidth(null)*2,this.image.getHeight(null)*2,null);
	    g.setColor(Color.red);
	    paintKnightHealth(g);
	   // g.draw(this.body);
	    g.setTransform(new AffineTransform());
	}
	
	public void paintKnightHealth(Graphics2D g)
	{
		if(this.health < 100)
		{		
			Shape shape=new Rectangle2D.Double(30, -15, 100/3, 7);
		    g.setColor(Color.red);
		    g.fill(shape);//Fills the shape with the set color
		    Shape shape2=new Rectangle2D.Double(30, -15, this.getHealth()/3, 7);
		    g.setColor(Color.green);
		    g.fill(shape2);//Fills the shape with the set color
		}
	}
		
	
	//accessor methods
	public int getInitialX(){
		return initialX;
	}
	public int getInitialY(){
		return initialY;
	}
	public long getTimer(){
		return timer;
	}
	
	//mutator methods
	public void setInitialX(){
		this.initialX = this.x;
	}
	public void setTimer(){
		this.timer = System.currentTimeMillis();
	}
	public boolean getReset() {
		return this.reset;
	}
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	public boolean getAttackable()
	{
		return this.attackable;
	}

	public void setAttackable(boolean attackable)
	{
		this.attackable = attackable;
	}
	
	public boolean damage(int damage){
		if(this.health - damage >0) //knight lives
		{
			this.health-=damage;
			return false;
		}
		else //knight dies
		{
			this.health=0;
			return true;
		}
	}
	
	public boolean attack(Knight character, ArrayList<Character> characters, Player player)
	{

		int xDist = player.getX() - character.getX();
		int yDist = player.getY() - character.getY();
		
		if( (xDist <= 30 && xDist >= -30 && yDist <=-30 && yDist >=-50)//knight looking up
				|| (xDist <= 30 && xDist >= -30 && yDist <=20 && yDist >=0)//down
				|| (xDist <= 0 && xDist >= -30 && yDist <=20 && yDist >=-40)//right
				|| (xDist <= 30 && xDist >= 0 && yDist <=20 && yDist >=-40))//left
		{
			boolean deadLink = player.damage(character.getDamage());
	    	if(deadLink)
	    	{
	    		//audioCache.get("GameOver").play();
			characters.remove(player);
			
	    	return true;
	    	}
		}
		return false;
	}
	
	public long getSwingTime() {
		return this.swingTime;
	}
	public void setSwingTime(long time)
	{
		this.swingTime = time;
	}
	public long getLastSwing()
	{
		return this.lastSwing;
	}
	public void setLastSwing(long time)
	{
		this.lastSwing = time;
	}
	public void setShot(boolean b) {
		this.shot = b;
	}
	public boolean getShot()
	{
		return this.shot;
	}
}