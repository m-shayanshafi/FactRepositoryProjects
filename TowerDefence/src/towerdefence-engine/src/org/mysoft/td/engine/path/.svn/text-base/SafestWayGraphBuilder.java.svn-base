package org.mysoft.td.engine.path;

import java.util.List;

import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.engine.world.GenericWorld;

public class SafestWayGraphBuilder extends WorldGraphBuilder {

	public SafestWayGraphBuilder(GenericWorld world) {
		super(world);
	}
	
	@Override
	protected double cost(int x, int y) {
		switch(world.getObjectTypeAt(x, y)) {
			case EMPTY:
				List<GenericTurrent> turrents = world.getTurrentsInRange(x, y);
				if(turrents == null || turrents.size() == 0)
					return 1;
				else
					return turrents.size() * 10;
			default:
				return 1;
		}
	}
	
}
