package org.mysoft.td.engine.util;


public class IntPoint {
	
	public final static int BUFF_X = 100;
	public final static int BUFF_Y = 100;
	
	private static IntPoint[][] points = new IntPoint[BUFF_X][BUFF_Y];
	
	public int x;
	public int y;
	
	public static IntPoint create(int x, int y) {
		if(x >= 0 && x < BUFF_X && y >=0 && y < BUFF_Y) {
			IntPoint p = points[x][y];
			if(p == null)
				points[x][y] = new IntPoint(x, y);
			
			return points[x][y];
				
		}
		return new IntPoint(x, y);
	}
	
	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		IntPoint p2 = (IntPoint)obj;
		return p2.x == x && p2.y == y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static IntPoint create(IntPoint p) {
		return create(p.x, p.y);
	}
	
	
	
}
