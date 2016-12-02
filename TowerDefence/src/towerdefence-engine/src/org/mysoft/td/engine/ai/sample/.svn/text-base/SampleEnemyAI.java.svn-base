package org.mysoft.td.engine.ai.sample;

import org.mysoft.gameutils.algorithms.path.Pathfinder;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.gameutils.data.base.RealPointList;
import org.mysoft.gameutils.data.path.SimplePath;
import org.mysoft.gameutils.data.path.StatefulPath;
import org.mysoft.gameutils.helper.MovementGraphBuilder;
import org.mysoft.gameutils.helper.PathManager;
import org.mysoft.td.engine.ai.ENextMove;
import org.mysoft.td.engine.ai.GenericEnemyAI;
import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.engine.objects.sample.SampleEnemy;
import org.mysoft.td.engine.path.SafestWayGraphBuilder;
import org.mysoft.td.engine.path.WorldGraphBuilder;


public class SampleEnemyAI extends GenericEnemyAI<SampleEnemy> {

	StatefulPath path;
	
	@Override
	public ENextMove nextMove() {
		return ENextMove.MOVE;
	}

	@Override
	public RealPoint nextTarget() {
		GenericEnemy enemy = (GenericEnemy)obj;
		
		if(enemy.getPosition().equals(path.nextPoint()) && !path.isLast())
			path.checkNext();
		
		return path.nextPoint();
		
	}

	@Override
	public void init() {
		path = (StatefulPath)PathManager.getInstance().getPath("path");


		if(path == null) {
			Pathfinder pathfinder = new Pathfinder(new SafestWayGraphBuilder(getWorld()).build());
		
			RealPoint from = new RealPoint(0, getObject().getWorld().getHeight() / 2);
			RealPoint to = new RealPoint(getObject().getWorld().getWidth() + 2, getObject().getWorld().getHeight() / 2);

			path = new StatefulPath(pathfinder.findBestPath(from, to));
			path.simplify(0.9);
			
			PathManager.getInstance().setPath("path", path);
		} else {
			path = path.clone();
		}
	
	}

}
