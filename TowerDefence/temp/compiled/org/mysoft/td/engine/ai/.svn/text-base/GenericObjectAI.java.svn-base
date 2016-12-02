package org.mysoft.td.engine.ai;

import org.mysoft.td.engine.objects.GenericObject;
import org.mysoft.td.engine.world.GenericWorld;

public abstract class GenericObjectAI<T extends GenericObject> {

	public T obj;

	public void setObject(T obj) {
		this.obj = obj;
	}
	
	public T getObject() {
		return obj;
	}
	
	public GenericWorld getWorld() {
		return obj.getWorld();
	}
	
	public abstract ENextMove nextMove();

	public abstract void init();
}
