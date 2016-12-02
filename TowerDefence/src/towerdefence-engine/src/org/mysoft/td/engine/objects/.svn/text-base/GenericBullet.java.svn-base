package org.mysoft.td.engine.objects;

import org.mysoft.gameutils.data.base.RealPoint;

public class GenericBullet {

	protected RealPoint position;
	protected double moveX;
	protected double moveY;
	protected RealPoint target;
	protected double speed;
	protected int power;
	protected double range;
	protected double strength;
	
	public GenericBullet(RealPoint position, RealPoint target, double speed, int power, double range, double strength) {
		super();
		this.position = position.clone();
		this.target = target.clone();
		this.speed = speed;
		this.power = power;
		this.range = range;
		this.strength = strength;
		
		double distance = position.distanceTo(target);
		double t = speed / distance;
		
		moveX = (target.x - position.x) * t;
		moveY = (target.y - position.y) * t;
	}
	
	public void turn() {
		if(isDone())
			return;
		
		if(position.distanceTo(target) < speed) {
			position.x = target.x;
			position.y = target.y;
		} else {
			position.x += moveX;
			position.y += moveY;
		}
	}
	
	public RealPoint getPosition() {
		return position;
	}
	
	public RealPoint getTarget() {
		return target;
	}
	
	public int getPower() {
		return power;
	}
	
	public boolean isDone() {
		return position.equals(target, 0.1);
	}
	
	public double getRange() {
		return range;
	}
	
	public double getStrength() {
		return strength;
	}
}
