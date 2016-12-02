package common.model;

import java.io.Serializable;

public class Position implements Serializable{
	private static final long serialVersionUID = -1499914638440184717L;
	
	private int x;
	private int y;
	
	public Position(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public static Position parse(String chaine) {
		int x = 0;
		int y = 0;
		if(chaine.contains(":"))
		{
			if(chaine.split(":").length>1)
			{
				x=Integer.parseInt(chaine.split(":")[0]);
				y=Integer.parseInt(chaine.split(":")[1]);
				return new Position(x,y);
			}
			return null;
		}
		if(chaine.contains(" "))
		{
			if(chaine.split(" ").length>1)
			{
				x=Integer.parseInt(chaine.split(" ")[0]);
				y=Integer.parseInt(chaine.split(" ")[1]);
				return new Position(x,y);
			}
			return null;
		}
		return null;
	}

	public int getX() { return x; }
	public int getY() { return y; }

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
	
}
