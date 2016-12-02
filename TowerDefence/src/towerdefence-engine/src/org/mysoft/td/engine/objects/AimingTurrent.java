package org.mysoft.td.engine.objects;

import java.util.LinkedList;
import java.util.List;

import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.world.GenericWorld;

public class AimingTurrent extends GenericTurrent {

	protected double[] bulletSpeed;
	protected double[] bulletAttackRange;
	protected RealPoint target;
	protected List<GenericBullet> bullets = new LinkedList<GenericBullet>();
	protected List<GenericBullet> bulletsToRemove = new LinkedList<GenericBullet>();
	
	public AimingTurrent(GenericWorld world) {
		super(world);
		target = new RealPoint(0, world.getHeight() / 2);
	}
	
	public double getBulletSpeed() {
		return bulletSpeed[level];
	}
	
	public double getBulletAttackRange() {
		return bulletAttackRange[level];
	}
	
	public final RealPoint getTarget() {
		return target;
	}
	
	public void aimEnemy(GenericEnemy enemy) {
		RealPoint enemyCenter = enemy.getCenter();
		target = enemyCenter.clone();
	}
	
	public void setTarget(RealPoint target) {
		this.target = target;
	} 

	@Override
	public final void fire() {
		fireAt(target);
	}
	
	public void fireAt(RealPoint target) {
		super.fire();
		double dist = center.distanceTo(target);
		if(dist < getBulletSpeed())
			bullets.add(createBullet(target, target));
		else
			bullets.add(createBullet(center, target));
		
	}
	
	protected GenericBullet createBullet(RealPoint position, RealPoint target) {
		return new GenericBullet(position, target, getBulletSpeed(), getPower(), getBulletAttackRange(), 1);
	}
	
	@Override
	public void calculateTurn() {
		super.calculateTurn();
		
		//TODO: przerobić na iterator z automatycznym usuwaniem
		for(GenericBullet b: bullets) {
			if(b.isDone())
				bulletsToRemove.add(b);
			else
				b.turn();		
		}
		
		bullets.removeAll(bulletsToRemove);
		bulletsToRemove.clear();
	}
	
	public List<GenericBullet> getBullets() {
		return bullets;
	}
	
}
