package org.mysoft.td.engine.objects;

import org.mysoft.gameutils.data.base.IntDimension;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.world.GenericWorld;

public abstract class GenericTurrent extends GenericObject {

	protected double[] range;
	protected double[] reloadTime;
	protected double timeToWait;
	protected int[] power;

	protected int[] cost;
	protected int level = 0;
	protected int levels = 1;
	
	public ETurrentState state = ETurrentState.WAIT;
	
	public GenericTurrent(GenericWorld world) {
		super(world);
		init();
	}
	
	public double getRange() {
		return range[level];
	}
	
	public int getPower() {
		return power[level];
	}
	
	public ETurrentState getState() {
		return state;
	}

	public double getReloadTime() {
		return reloadTime[level];
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean readyToFire() {
		return state == ETurrentState.WAIT;
	}
	
	public void fire() {
		state = ETurrentState.FIRE;
		timeToWait = reloadTime[level];
	}
	
	public boolean isInRange(GenericEnemy enemy) {
		return isInRange(enemy.center);
	}
	
	public boolean isInRange(RealPoint target, double tolerance) {
		double dist = center.distanceTo(target);
		return dist <= range[level] + tolerance;
	}

	public boolean isInRange(RealPoint target) {
		return isInRange(target, 0);
	}
	
	public boolean isInRange(double x, double y) {
		return isInRange(new RealPoint(x,y), 0);
	}

	public boolean isInRange(double x, double y, double tolerance) {
		return isInRange(new RealPoint(x,y), tolerance);
	}

	
	public void calculateTurn() {
		timeToWait--;
		if(timeToWait>0)
			state = ETurrentState.RELOAD;
		else
			state = ETurrentState.WAIT;
	}
	
	public void upgrade() {
		if(level < levels - 1)
			level++;
	}
	
	public void init() {
		size = new IntDimension(2,2);
		center = new RealPoint(position.x + size.width/2.0, position.y + size.height/2.0);
	}
}
