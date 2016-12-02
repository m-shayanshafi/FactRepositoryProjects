package org.mysoft.td.engine.objects;

import org.mysoft.gameutils.data.base.IntDimension;
import org.mysoft.gameutils.data.base.IntPoint;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.ai.GenericObjectAI;
import org.mysoft.td.engine.world.GenericWorld;

public abstract class GenericObject {

	protected String name;
	protected String description;
	
	protected IntPoint position = new IntPoint(0, 0);
	protected IntDimension size = new IntDimension(1,1);
	protected RealPoint center = new RealPoint(0,0);

	protected GenericObjectAI ai;
	protected GenericWorld world;
	
	public GenericObject(GenericWorld world) {
		this.world = world;
	}

	public void init() {
		
	}
	
	public void calculateTurn() {
		
	}
	
	public void setAI(GenericObjectAI ai) {
		this.ai = ai;
		ai.setObject(this);
		ai.init();
	}
	
	public final GenericObjectAI getAi() {
		return ai;
	}
	
	public final void setPosition(IntPoint p) {
		setPosition(p.x, p.y);
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
		
		recalculateCenter();
	}

	public final void setSize(IntDimension d) {
		setSize(d.width, d.height);
	}
	
	public void setSize(int width, int height) {
		size.width = width;
		size.height = height;
		
		recalculateCenter();
	}
	
	protected void recalculateCenter() {
		center.x = position.x + size.width/2.0;
		center.y = position.y + size.width/2.0;
	}
	
	public final IntPoint getPosition() {
		return position;
	}
	
	public final IntDimension getSize() {
		return size;
	}
	
	public final RealPoint getCenter() {
		return center;
	}

	public final int getWidth() {
		return size.width;
	}
	
	public final int getHeight() {
		return size.height;
	}

	public final GenericWorld getWorld() {
		return world;
	}
	
}
