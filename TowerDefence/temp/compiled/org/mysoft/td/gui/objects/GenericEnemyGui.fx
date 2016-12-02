package org.mysoft.td.gui.objects;

import org.mysoft.td.engine.objects.*;
import org.mysoft.td.gui.config.*;

/**
 * @author mkoch
 */
public abstract class GenericEnemyGui extends GenericObjectGui {
    
    override public function update(): Void {
       translateX = javafx.util.Math.round((engineObject as GenericEnemy).realPosition.x * 10) + Constants.BRICK_SIZE/2;
       translateY = javafx.util.Math.round((engineObject as GenericEnemy).realPosition.y * 10) + Constants.BRICK_SIZE/2;

	   if((engineObject as GenericEnemy).state == EEnemyState.DEAD) {
	       changeState(EEnemyState.DEAD);
	   }
    }
    
    public function changeState(state: EEnemyState): Void {
        
    }
    
    override public function toRemove(): Boolean {
        return ((engineObject as GenericEnemy).state == EEnemyState.DEAD) or
        		((engineObject as GenericEnemy).state == EEnemyState.RELEASE);
    }
}