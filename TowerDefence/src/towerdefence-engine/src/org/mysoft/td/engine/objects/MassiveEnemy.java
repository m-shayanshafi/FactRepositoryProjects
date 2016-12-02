package org.mysoft.td.engine.objects;

import org.mysoft.gameutils.data.base.EDirection;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.world.GenericWorld;

public class MassiveEnemy extends GenericEnemy {

	protected double currentSpeed;
	protected double mass = 1.0;
	
	protected double moveX;
	protected double moveY;
	
	public MassiveEnemy(GenericWorld world) {
		super(world);
	}

	public RealPoint calculateNextPositionToward(int x, int y) {
		double vx = x - realPosition.x;
		double vy = y - realPosition.y;
		
		double l = (double)Math.sqrt(vx*vx + vy*vy);
		
		if(l>0) {
			vx *= maxSpeed/l;
			vy *= maxSpeed/l;
		}

		double maxd = 1/mass;
				
		double dx = vx - moveX;
		dx = (Math.abs(dx) > maxd ? Math.signum(dx)*maxd : dx);

		double dy = vy - moveY;
		dy = (Math.abs(dy) > maxd ? Math.signum(dy)*maxd : dy);
		
		moveX += dx/mass;
		moveY += dy/mass;
		

		RealPoint p = new RealPoint(realPosition.x + moveX, realPosition.y + moveY);

		return p;
	}

	@Override
	public void handleCollision(GenericObject obj, RealPoint nextPosition, EDirection collisionDirection) {
		super.handleCollision(obj, nextPosition, collisionDirection);
		
		double r = Math.random();
		
		if(collisionDirection == EDirection.EAST || collisionDirection == EDirection.WEST)
			moveX *= -0.6 * r;
		else 
			moveY *= -0.6 * r;
		
		moveX *= 0.9 + Math.random()/10;
		moveY *= 0.9 + Math.random()/10;
	}

	@Override
	public void handleHit(int power, RealPoint point, double strength) {
		super.handleHit(power, point, strength);
		
		if(state != EEnemyState.DEAD) {
			

			double dx = - (point.x - realPosition.x);
			double dy = - (point.y - realPosition.y);
			
			moveX += strength/dx/mass;
			moveY += strength/dy/mass;

			double s = Math.sqrt(moveX * moveX + moveY * moveY);
			
			double ms = maxSpeed*1.5;
		
			if(s>ms) {
				moveX *= ms/s;
				moveY *= ms/s;
			}
		
		}
	
	}	
	
	
}
