package org.mysoft.td.engine.path;

import org.mysoft.gameutils.helper.MovementGraphBuilder;
import org.mysoft.td.engine.world.EWorldObjectType;
import org.mysoft.td.engine.world.GenericWorld;

public class WorldGraphBuilder extends MovementGraphBuilder {

	protected final GenericWorld world;
	
	public class WorldCostResolver implements ICostResolver {

		private GenericWorld world;
		
		public WorldCostResolver(GenericWorld world) {
			this.world = world;
		}
		
		@Override
		public double resolve(int x, int y) {
			return cost(x, y);
		}

		@Override
		public boolean walkable(int x, int y) {
			return world.getObjectTypeAt(x, y) == EWorldObjectType.EMPTY;
		}
		
	}
	
	protected double cost(int x, int y) {
		switch (world.getObjectTypeAt(x, y)) {
		case EMPTY:
			return 1;
		default:
			return 99999;
		}
	}
	
	public WorldGraphBuilder(GenericWorld world) {
		super(world.getWidth(), world.getHeight());
		this.world = world;
		setResolver(new WorldCostResolver(world));
	}
	
}
