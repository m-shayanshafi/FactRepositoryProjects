package org.mysoft.td.engine.objects.sample;

import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.objects.AimingTurrent;
import org.mysoft.td.engine.objects.GenericBullet;
import org.mysoft.td.engine.world.GenericWorld;

public class MachineGunTurrent extends AimingTurrent {

	public final static double[] RANGE = new double[] {4, 5, 6};
	public final static double[] RELOAD_TIME = new double[] {6, 3, 2};
	public final static int[] POWER = new int[] {1, 2, 4};
	public final static int[] COST = new int[] {10, 20, 40};
	public final static double[] BULLET_SPPED = new double[] {5, 5, 5};
	public final static double[] BULLET_RANGE = new double[] {0.1, 0.1, 0.1};
	
	public MachineGunTurrent(GenericWorld world) {
		super(world);
	}

	@Override
	public void init() {
		super.init();
		level = 0;
		levels = 3;
		range = RANGE;
		reloadTime = RELOAD_TIME;
		power = POWER;
		cost = COST;	
		bulletSpeed = BULLET_SPPED;
		bulletAttackRange = BULLET_RANGE;
		
		name = "Machine gun";
		description = "Gives continous fire and low damage";
	}
	
	@Override
	protected GenericBullet createBullet(RealPoint position, RealPoint target) {
		return new GenericBullet(position, target, getBulletSpeed(), getPower(), getBulletAttackRange(), 0);
	}

}
