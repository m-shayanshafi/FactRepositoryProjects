package org.mysoft.td.gui;

import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import org.mysoft.td.engine.action.sample.SampleActionController;
import org.mysoft.td.engine.world.sample.SampleWorld;
import org.mysoft.td.engine.objects.sample.*;
import org.mysoft.td.gui.objects.*;
import org.mysoft.td.gui.objects.sample.*;
import org.mysoft.td.gui.world.*;
import javafx.scene.Scene;


/**
 * @author mkoch
 */
public class GuiController {

    public-init var interval: Duration;
    var ac: SampleActionController = new SampleActionController();
    
    public var scene: Scene;
    
    var count: Integer = 0;
    var delay: Integer = 6;
    
    public var world: GenericWorldGui
    	on replace oldWorld = newWorld {
    		ac.setWorld(newWorld.engineWorld);
    	};
    
    public var objects: GenericObjectGui[] 
      	on replace oldObjects[idxA..idxB] = newObjects {
      	    if(newObjects == null) {
      	        for(o in oldObjects[idxA..idxB]) {
      	        	ac.removeObject(o.engineObject);
      	        	delete o from objectGroup.content;
      	        }
      	    } else
	      	    for(newObject in newObjects) { 
		  			ac.addObject(newObject.engineObject);
		  			insert newObject into objectGroup.content;
	      	    }
	  	};    
    
    
    public function calculateTurn(): Void {
        ac.calculateTurn();
 
 		count ++;
 		
 		if(count mod delay == 0) {
 		    var ne = org.mysoft.td.gui.objects.sample.SampleEnemyGui{
 		    				x: -1 y: ac.world.getSizeY()/2
 		    				world: world};
 		    insert ne into objects;
 		}
 
        for(o in objects) {
            if(o.toRemove()) {
                delete o from objects;
                //o.visible = false;
            } else
            	o.update();
        }
    }
    
    public function start(): Void {
        createTimeline().play();
    }
    
	public function createTimeline(): Timeline {
    	Timeline {
        	repeatCount: Timeline.INDEFINITE
        	keyFrames: [
          		KeyFrame {
             		time: bind interval
             		action: function() {
                		calculateTurn();
              		}
           		}
        	]
       	}
	}
	
	var objectGroup: Group = Group{}
	
	init {
		insert world into scene.content;
		insert objectGroup into scene.content;
	}
   

}