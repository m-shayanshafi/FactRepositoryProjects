package org.mysoft.td.engine.util;

public class RealPoint {

	public double x;
	public double y;
	
	public RealPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		RealPoint p2 = (RealPoint)obj;
		return p2.x == x && p2.y == y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}	
	
	public RealPoint clone() {
		return new RealPoint(x, y);
	}
	
	public static double distance(RealPoint p1, RealPoint p2) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static boolean equals(RealPoint position, RealPoint target, double tolerance) {
		return Math.abs(position.x - target.x) < tolerance && Math.abs(position.y - target.y) < tolerance; 
	}
}
