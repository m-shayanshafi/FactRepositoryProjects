package org.mysoft.td.engine.action.sample;
import java.util.ArrayList;
import java.util.List;

import org.mysoft.gameutils.data.base.EDirection;
import org.mysoft.gameutils.data.base.RealPoint;
import org.mysoft.td.engine.action.GenericActionController;
import org.mysoft.td.engine.ai.GenericEnemyAI;
import org.mysoft.td.engine.objects.AimingTurrent;
import org.mysoft.td.engine.objects.GenericBullet;
import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.engine.world.EWorldObjectType;
import org.mysoft.td.engine.world.sample.SampleWorld;

public class SampleActionController extends GenericActionController {
	
	private List<GenericBullet> dangerBullets = new ArrayList<GenericBullet>(100);

	public SampleActionController() {

	}
	
	@Override
	public boolean calculateTurn() {
		
		SampleWorld w = (SampleWorld)world;
		
		dangerBullets.clear();

		for(GenericEnemy enemy: enemies)
			enemy.calculateTurn();

		for(GenericEnemy enemy: enemies) {
			
			//enemy.calculateTurn();
			
			if(enemy.getHitPoints() <= 0) {
				enemy.die();
				continue;
			} 
			
			if(enemy.getPosition().x == world.getWidth()) {
				enemy.release();
				continue;
			}
			
			RealPoint target = ((GenericEnemyAI)enemy.getAi()).nextTarget();
			//System.out.println("pos: " + enemy.realPosition + ", target: " + target.toString());
			
			RealPoint nextPosition = enemy.calculateNextPositionToward((int)Math.round(target.x), (int)Math.round(target.y));
			
			if(canMoveToward(enemy, nextPosition))
				moveToward(enemy, nextPosition);
			else {
				EDirection dir = null;
				
				if(enemy.getRealPosition().y < nextPosition.y) {
					//south
					if(world.getObjectTypeAt((int)Math.round(enemy.getRealPosition().x), (int)Math.round(nextPosition.y) + enemy.getHeight()*2) != EWorldObjectType.EMPTY)
						dir = EDirection.SOUTH;
				} else {
					//north
					if(world.getObjectTypeAt((int)Math.round(enemy.getRealPosition().x), (int)Math.round(nextPosition.y)) != EWorldObjectType.EMPTY)
						dir = EDirection.NORTH;
				}
				
				if(enemy.getRealPosition().x < nextPosition.x) {
					//east
					if(world.getObjectTypeAt((int)Math.round(nextPosition.x), (int)Math.round(enemy.getRealPosition().y)) != EWorldObjectType.EMPTY)
						dir = EDirection.EAST;
				} else {
					//west
					if(world.getObjectTypeAt((int)Math.round(nextPosition.x), (int)Math.round(enemy.getRealPosition().y)) != EWorldObjectType.EMPTY)
						dir = EDirection.WEST;
				}
/*				
				if(world.objectTypeAt((int)Math.round(enemy.realPosition.x), (int)Math.round(nextPosition.y)) != EWorldObjectType.EMPTY) {
					dir = (enemy.realPosition.y > nextPosition.y) ? EDirection.NORTH : EDirection.SOUTH;
				}
				else if(world.objectTypeAt((int)Math.round(nextPosition.x), (int)Math.round(enemy.realPosition.y)) != EWorldObjectType.EMPTY) {
					dir = (enemy.realPosition.x > nextPosition.x) ? EDirection.WEST : EDirection.EAST;
				}
*/
				enemy.handleCollision(world.getTurrentAt((int)Math.round(nextPosition.x), (int)Math.round(nextPosition.y)), nextPosition, dir);

				nextPosition = enemy.calculateNextPositionToward((int)Math.round(target.x), (int)Math.round(target.y));
				
				if(canMoveToward(enemy, nextPosition))
					moveToward(enemy, nextPosition);
			}

		}
		
		for(GenericTurrent turrent: turrents) {
			turrent.calculateTurn();
		}
		
		for(GenericEnemy enemy: enemies) {
			List<GenericTurrent> turrents = w.getTurrentsInRangeOf(enemy);
			for(GenericTurrent t: turrents) { 
				
				if(t.readyToFire()) {
					if(t instanceof AimingTurrent) {
						AimingTurrent at = (AimingTurrent)t;
						at.aimEnemy(enemy);
					}

					t.fire();
				}
			}
		}
		
		for(GenericTurrent turrent: turrents) {
			
			if(turrent instanceof AimingTurrent) {
				AimingTurrent aturrent = (AimingTurrent)turrent;
				for(GenericBullet b: aturrent.getBullets()) {
					if(b.isDone())
						dangerBullets.add(b);
				}
			}
		}		
		
		for(GenericEnemy enemy: enemies) {
			for(GenericBullet b: dangerBullets) {
				if(enemy.getCenter().equals(b.getPosition(), b.getRange())) {
					enemy.handleHit(b.getPower(), b.getPosition(), b.getStrength());
					//System.out.println("hit");
				}
			}
					
		}
		
		
		cleanEnemies();
		
		return true;
	}

}
