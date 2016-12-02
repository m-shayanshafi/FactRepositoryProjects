package org.mysoft.td.gui.objects.sample;

import org.mysoft.td.gui.objects.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.transition.*;
import javafx.util.Math;
import javafx.scene.effect.*;
import org.mysoft.td.engine.objects.*;
import org.mysoft.td.engine.objects.sample.*;
import org.mysoft.td.gui.config.*;


/**
 * @author mkoch
 **/
public class SampleTurrentGui extends GenericTurrentGui {
    
	init {
   		engineObject = new org.mysoft.td.engine.objects.sample.SampleTurrent(world.engineWorld, x, y);
    }
        
	override function create():Node {
	    return Group {
	    		 	content: [
	    		 		Rectangle {
	    		 		    width: bind(engineObject as GenericTurrent).size.width * Constants.BRICK_SIZE;
	    		 		    height: bind(engineObject as GenericTurrent).size.height * Constants.BRICK_SIZE;
	    		 		    fill: Color.YELLOW;
	    		 		    stroke: Color.BLACK;
	    		 		    strokeWidth: 2;
	    		 		}
	    		 	];  
	    		} ;
	}        
    
}