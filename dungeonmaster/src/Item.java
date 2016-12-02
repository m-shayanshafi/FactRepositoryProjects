import java.awt.*;

public abstract class Item {
	protected String name;
	protected String id;
	protected int health=100;
	protected int stamina=100;
	protected int x;
	protected int y;
	protected int damage;
	protected Image image;
	protected int distance;
	protected String direction="Down";
	protected String doing="Stand";
	protected boolean doingBool=true;
	protected Motion motion;
	protected boolean motionBool=true;
	protected long sleeper;
	
	public String getName(){return this.name;}
	public void setName(String name){this.name=name;}
	public String getID(){return this.id;}
	public void setID(String id){this.id=id;}
	public int getHealth(){return this.health;}
	public void setHealth(int v){this.health = v;}
	public int getStamina(){return this.stamina;}
	public void setStamina(int v){this.stamina = v;}
	public int getX(){return this.x;}
	public void setX(int v){this.x+=v;}
	public int getY(){return this.y;}
	public void setY(int v){this.y+=v;}
	public Image getImage(){return this.image;}
	public void setImage(Image image){this.image=image;}
	public String getDirection(){return this.direction;}
	public void setDirection(String direction){this.direction=direction;}
	public Motion getMotion(){return this.motion;}
	public void setMotion(String doing){this.doing=doing;this.doingBool=false;}
	public void setMotion(String doing,String direction){this.setMotion(doing);this.setDirection(direction);}
	public boolean getMotionBool(){return motionBool;}
	public void setMotionBool(boolean motionBool){this.motionBool=motionBool;}
	public String getDoing(){return this.doing;}
	public void setDoing(String doing){this.doing=doing;}
	public boolean getDoingBool(){return doingBool;}
	public void setDoingBool(boolean doingBool){this.doingBool=doingBool;}
	public int getDistance(){return this.distance;}
	public void setDistance(int distance){this.distance=distance;}
	public long getSleeper(){return this.sleeper;}
	public void setSleeper(long sleeper){this.sleeper=sleeper;}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	public int getDamage() {
		return this.damage;
	}
	abstract public void paint(Graphics2D g);

}
