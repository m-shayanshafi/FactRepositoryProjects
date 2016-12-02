package org.mysoft.td.engine.objects.sample;

import org.mysoft.td.engine.objects.AimingTurrent;
import org.mysoft.td.engine.world.GenericWorld;

public class ArtilleryTurrent extends AimingTurrent {

	public final static double[] RANGE = new double[] {10, 15, 20};
	public final static double[] RELOAD_TIME = new double[] {60, 45, 30};
	public final static int[] POWER = new int[] {10, 20, 44};
	public final static int[] COST = new int[] {50, 100, 300};
	public final static double[] BULLET_SPPED = new double[] {0.5, 0.8, 1.2};
	public final static double[] BULLET_RANGE = new double[] {3, 4, 5};
	
	public ArtilleryTurrent(GenericWorld world) {
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
		
		name = "Artillery";
		description = "Hi range, hi power, slow fire";
	}

}
