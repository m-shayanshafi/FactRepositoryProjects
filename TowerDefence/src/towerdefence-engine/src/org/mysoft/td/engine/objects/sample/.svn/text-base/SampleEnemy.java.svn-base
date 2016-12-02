package org.mysoft.td.engine.objects.sample;

import org.mysoft.td.engine.ai.sample.SampleEnemyAI;
import org.mysoft.td.engine.objects.MassiveEnemy;
import org.mysoft.td.engine.world.GenericWorld;

public class SampleEnemy extends MassiveEnemy {

	public final static float SPEED = 0.3f;
	public final static float MASS = 5f;
	public final static int HP = 100;
	public final static double RANGE = 10;

	public SampleEnemy(GenericWorld world) {
		super(world);
		init();
	}

	public SampleEnemy(GenericWorld world,int x, int y) {
		super(world);
		name = "Sample enemy";
		this.setPosition(x, y);
		this.setSize(1, 1);
		maxSpeed = SPEED;
		init();
	}
	
	public SampleEnemy(GenericWorld world, String name, int x, int y) {
		super(world);
		this.name = name;
		this.setPosition(x, y);
		this.setSize(1, 1);
		maxSpeed = SPEED;
		init();
	}

	public void init() {
		mass = MASS;
		maxHitPoints = HP;
		hitPoints = HP;
		setAI(new SampleEnemyAI());
	}
	
}
