package org.mysoft.td.engine.util;

import java.util.ArrayList;
import java.util.List;

public class Path {

	ArrayList<IntPoint> points = new ArrayList<IntPoint>();
	boolean closed = true;
	int lastChecked = -1;
	
	public Path() {
		
	}
	
	public Path(IntPoint[] points) {
		for(IntPoint p: points)
			addPoint(p);
	}
	
	public void addPoint(IntPoint point) {
		points.add(point);
	}
	
	public void check(IntPoint point) {
		if(points.contains(point)) 
			lastChecked = points.indexOf(point);
		else
			lastChecked = -1;
	}
	
	public void checkNext() {
		lastChecked = (lastChecked + 1) % points.size();
	}
	
	public IntPoint nextPoint() {
		return points.get((lastChecked + 1) % points.size());
	}
	
	public boolean isLast() {
		return lastChecked == points.size();
	}
	
	public List<IntPoint> getPoints() {
		return points;
	}
	
	public Path clone() {
		Path newPath = new Path();
		
		for(IntPoint p: this.points)
			newPath.addPoint(IntPoint.create(p));
		
		return newPath;
	}
}
