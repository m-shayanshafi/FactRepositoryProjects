package org.mysoft.td.gui.world;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import org.mysoft.td.engine.world.*;
import org.mysoft.td.gui.config.*;


/**
 * @author mkoch
 */
public abstract class GenericWorldGui extends CustomNode {
 
 	public var engineWorld: GenericWorld;

	public function createGraphics(x: Integer, y: Integer): Rectangle {
	    if(engineWorld.objectTypeAt(x,y) == EWorldObjectType.WALL) {
	        return Rectangle {
	            translateX: bind x * Constants.BRICK_SIZE;
	           	translateY: bind y * Constants.BRICK_SIZE;
				x: 0
				y: 0
				width: bind Constants.BRICK_SIZE;
				height: bind Constants.BRICK_SIZE;
				fill: Color.BLACK;
	        };
	    } else if(engineWorld.objectTypeAt(x,y) == EWorldObjectType.TURRENT) {
	        return Rectangle {
	            translateX: bind x * Constants.BRICK_SIZE;
	           	translateY: bind y * Constants.BRICK_SIZE;
				x: 0
				y: 0
				width: bind Constants.BRICK_SIZE;
				height: bind Constants.BRICK_SIZE;
				fill: Color.GREEN;
	        };
	    } else {
	    	return Rectangle{
    			x: bind x * Constants.BRICK_SIZE
    			y: bind y * Constants.BRICK_SIZE
    			width: bind Constants.BRICK_SIZE
    			height: bind Constants.BRICK_SIZE
    			fill: Color.WHITE
    			
           }
	    }
	}
	
    public function createWalls(): Group {
	        return Group {
	        	content: for(i in [0..engineWorld.getSizeX()-1]) {
	        	    for(j in [0..engineWorld.getSizeY()-1]) {
	        	        createGraphics(i,j);	
	        	    }
	        	}
	        };
	    }
	    
	    
   	
   
}