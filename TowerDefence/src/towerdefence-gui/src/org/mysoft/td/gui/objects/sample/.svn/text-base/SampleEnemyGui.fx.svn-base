package org.mysoft.td.gui.objects.sample;

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
import org.mysoft.td.engine.objects.sample.SampleEnemy;
import org.mysoft.td.gui.config.*;
import org.mysoft.td.gui.objects.*;
import javafx.scene.text.Text;

public class SampleEnemyGui extends GenericEnemyGui {

	var image: Circle = Circle {
		 		    centerX : 00;
		 		    centerY : 00;
		 		    radius : 5;
		 		    fill: Color.YELLOW;
		 		    stroke: Color.BLACK;
		 		};
		 		
	var text: Text = Text {
	    			content: "hi"
				};

	init {
	    if(engineObject==null)
	    	engineObject = new SampleEnemy(world.engineWorld, name, x, y);
	}
   
	override function create():Node {
	    return Group {
    		 	content: [
    		 		image
    		 	];  
    		};
	}
	
	override public function update(): Void {
	    super.update();
	    text.content = (engineObject as GenericEnemy).hitPoints.toString();
	}
	
    override public function changeState(state: EEnemyState): Void {
        if(state == EEnemyState.DEAD)
        	image.fill = Color.WHITE;
    }
}