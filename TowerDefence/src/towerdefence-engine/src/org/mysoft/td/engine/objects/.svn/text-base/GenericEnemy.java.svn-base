package org.mysoft.td.engine.objects;

import org.mysoft.gameutils.data.base.EDirection;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.world.GenericWorld;

public abstract class GenericEnemy extends GenericObject {

	protected double maxSpeed;
	
	protected RealPoint realPosition = new RealPoint(1,1);
	
	protected int hitPoints;
	protected int maxHitPoints;
	
	protected EEnemyState state = EEnemyState.ACTIVE;
	
	public GenericEnemy(GenericWorld world) {
		super(world);
	}
	
	public EEnemyState getState() {
		return state;
	}
	
	protected void recalculateRealPosition() {
		realPosition.x = position.x;
		realPosition.y = position.y;
	}
	
	@Override
	protected void recalculateCenter() {
		center.x = realPosition.x + size.width/2.0;
		center.y = realPosition.y + size.height/2.0;
	}
	
	@Override
	public void setPosition(int x, int y) {
		if(state == EEnemyState.DEAD)
			return;
		
		super.setPosition(x, y);
		recalculateRealPosition();
	}
	
	public void setPosition(double x, double y) {
		if(state == EEnemyState.DEAD)
			return;
		
		realPosition.x = x;
		realPosition.y = y;
		position.x = (int)Math.round(x);
		position.y = (int)Math.round(y);
		recalculateCenter();

	}
	
	public final RealPoint getRealPosition() {
		return realPosition;
	}
	
	public final int getHitPoints() {
		return hitPoints;
	}
	
	public final double getMaxSpeed() {
		return maxSpeed;
	}
	
	public final int getMaxHitPoints() {
		return maxHitPoints;
	}
	
	public RealPoint calculateNextPositionToward(int x, int y) {
		if(state == EEnemyState.DEAD)
			return realPosition;
		
		double vx = x - realPosition.x;
		double vy = y - realPosition.y;
		
		double l = (double)Math.sqrt(vx*vx + vy*vy);
		
		if(l>maxSpeed) {
			vx *= maxSpeed/l;
			vy *= maxSpeed/l;
		}
		
		return new RealPoint(realPosition.x + vx, realPosition.y + vy);
	}	
	
	public void handleCollision(GenericObject obj, RealPoint nextPosition, EDirection collisionDirection) {
		if(state == EEnemyState.DEAD)
			return;
	}
	
	public void handleHit(int power, RealPoint point, double strength) {
		hitPoints -= power;
	}
	
	public void die() {
		state = EEnemyState.DEAD;
	}
	
	public void release() {
		state = EEnemyState.RELEASE;
	}

	public String toString() {
		return name + ": " + position.x + ", " + position.y + " (" + realPosition.x + ", " + realPosition.y + ")";
	}


}
