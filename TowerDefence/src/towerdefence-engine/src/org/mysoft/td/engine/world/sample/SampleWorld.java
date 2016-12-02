package org.mysoft.td.engine.world.sample;

import org.mysoft.gameutils.data.base.IntDimension;
import org.mysoft.td.engine.world.EWorldObjectType;
import org.mysoft.td.engine.world.IndexedWorld;

public class SampleWorld extends IndexedWorld {

	@Override
	protected void init() {
		size = new IntDimension(64, 48);
		super.init();
	}

	@Override
	public EWorldObjectType getObjectTypeAt(int x, int y) {
		if(!inside(x, y))
			return EWorldObjectType.EMPTY;
		else if((x==0 || x==getWidth()-1) && (y>getHeight()/2 - 2 && y<getHeight()/2 + 2))
			return EWorldObjectType.EMPTY;
		else if(x==0 || x==getWidth()-1 || y==0 || y==getHeight()-1)
			return EWorldObjectType.WALL;
		else {
			if(getTurrentAt(x, y) != null)
				return EWorldObjectType.TURRENT;
		}
		
		return EWorldObjectType.EMPTY;
	}






}
