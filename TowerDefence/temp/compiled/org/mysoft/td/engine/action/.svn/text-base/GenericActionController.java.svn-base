package org.mysoft.td.engine.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.exception.TDException;
import org.mysoft.td.engine.objects.EEnemyState;
import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.engine.objects.GenericObject;
import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.engine.world.EWorldObjectType;
import org.mysoft.td.engine.world.GenericWorld;

public abstract class GenericActionController {

	protected List<GenericTurrent> turrents = new ArrayList<GenericTurrent>();
	protected List<GenericEnemy> enemies = new ArrayList<GenericEnemy>();
	
//	public List<GenericEnemy> newEnemies = new LinkedList<GenericEnemy>();
	
	public GenericWorld world;

	public GenericActionController() {
	
	}
	
	public GenericActionController(GenericWorld world) {
		setWorld(world);
	}
	
	public List<GenericTurrent> getTurrents() {
		return turrents;
	}
	
	public List<GenericEnemy> getEnemies() {
		return enemies;
	}
	
	protected void checkWorld() {
		if(world==null) 
			throw new TDException("Setup world first");
	}
	
	protected void addTurrent(GenericTurrent turrent) {
		checkWorld();
		world.addTurrent(turrent);
		turrents.add(turrent);
	}

	protected void removeTurrent(GenericTurrent turrent) {
		checkWorld();
		world.removeTurrent(turrent);
		turrents.remove(turrent);
	}
	
	protected void addEnemy(GenericEnemy enemy) {
		enemies.add(enemy);
//		newEnemies.add(enemy);
	}

	protected void removeEnemy(GenericEnemy enemy) {
		enemies.remove(enemy);
	}
	
	public void addObject(GenericObject obj) {
		checkWorld();
		if(obj instanceof GenericTurrent) 
			addTurrent((GenericTurrent)obj);
		else if(obj instanceof GenericEnemy)
			addEnemy((GenericEnemy)obj);
	}
	
	public void removeObject(GenericObject obj) {
		checkWorld();
		if(obj instanceof GenericTurrent) 
			removeTurrent((GenericTurrent)obj);
		else if(obj instanceof GenericEnemy)
			removeEnemy((GenericEnemy)obj);
	}
	
	public void setWorld(GenericWorld world) {
		this.world = world;
	}
	
	abstract public boolean calculateTurn();
	

	protected boolean canMoveToward(GenericEnemy enemy, RealPoint nextPosition) {
		return world.getObjectTypeAt((int)Math.round(nextPosition.x), (int)Math.round(nextPosition.y)) == EWorldObjectType.EMPTY;
	}
	
	protected void moveToward(GenericEnemy enemy, RealPoint nextPosition) {
		enemy.setPosition(nextPosition.x, nextPosition.y);
	}
	
	protected List<GenericEnemy> getEnemiesInRangeOf(GenericTurrent turrent) {
		List<GenericEnemy> result = new ArrayList<GenericEnemy>();
		for(GenericEnemy enemy: enemies) {
			if(enemy.getState() == EEnemyState.ACTIVE)
				if(turrent.isInRange(enemy))
					result.add(enemy);
		}
		return result;
	}

	public void cleanEnemies() {
		List<GenericEnemy> r = new LinkedList<GenericEnemy>();
		
		for(GenericEnemy e: enemies) {
			if(e.getState() == EEnemyState.DEAD || e.getState() == EEnemyState.RELEASE)
				r.add(e);
		}
		
		for(GenericEnemy e: r) 
			enemies.remove(e);
	}
	
}
