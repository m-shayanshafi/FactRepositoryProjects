package org.mysoft.td.engine.world;

import java.util.ArrayList;
import java.util.List;

import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.engine.objects.GenericTurrent;


public abstract class IndexedWorld extends GenericWorld {

	protected WorldIndex index;
	
	public WorldIndex getIndex() {
		return index;
	}
	
	@Override
	protected void init() {
		super.init();
		index = new WorldIndex(this);
	}
	
	@Override
	public GenericTurrent getTurrentAt(int x, int y) {
		if(inside(x, y))
			return (GenericTurrent)index.getObjectAt(x, y);
		else
			return null;
	}
	
	@Override
	public void removeTurrent(GenericTurrent turrent) {
		index.removeObjectAt(turrent.getPosition());
	}

	@Override
	public void addTurrent(GenericTurrent turrent) {
		index.putObject(turrent, turrent.getPosition());
	}
	
	public List<GenericTurrent> getTurrentsInRangeOf(GenericEnemy enemy) {
		if(inside(enemy.getPosition()))
			return getTurrentsInRange(enemy.getPosition());
		else
			return new ArrayList<GenericTurrent>(0);
	}	
	
	public void updateIndex() {
		
	}
	
	@Override
	public List<GenericTurrent> getTurrentsInRange(int x, int y) {
		return index.getTurrentsInRange(x, y);
	}
		
}
