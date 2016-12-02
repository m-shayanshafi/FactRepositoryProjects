package org.mysoft.td.engine.world;

import java.util.List;

import org.mysoft.gameutils.data.base.IntDimension;
import org.mysoft.gameutils.data.base.IntPoint;
import org.mysoft.td.engine.objects.GenericTurrent;

public abstract class GenericWorld {
	
	protected IntDimension size = null;
	
	public GenericWorld() {
		init();
	}
	
	public final IntDimension getSize() {
		return size;
	}
	
	public final int getWidth() {
		return size.width;
	}
	
	public final int getHeight() {
		return size.height;
	}

	public EWorldObjectType getObjectTypeAt(IntPoint position) {
		return getObjectTypeAt(position.x, position.y);
	}
	
	abstract public EWorldObjectType getObjectTypeAt(int x, int y);
	

	
	protected void init() {
		
	}
	
	public boolean inside(IntPoint p) {
		return size.inside(p);
	}
	
	public boolean inside(int x, int y) {
		return inside(IntPoint.create(x, y));
	}

	public boolean canPlace(GenericTurrent turrent) {
		return getObjectTypeAt(turrent.getPosition()) == EWorldObjectType.EMPTY;
	}
	
	public GenericTurrent getTurrentAt(IntPoint p) {
		return getTurrentAt(p.x, p.y);
	}
	
	public List<GenericTurrent> getTurrentsInRange(IntPoint p) {
		return getTurrentsInRange(p.x, p.y);
	}
	
	abstract public void addTurrent(GenericTurrent turrent);
	
	abstract public void removeTurrent(GenericTurrent turrent);
	
	abstract public GenericTurrent getTurrentAt(int x, int y);
	
	abstract public List<GenericTurrent> getTurrentsInRange(int x, int y);
}
