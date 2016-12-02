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
import org.mysoft.td.gui.config.*;

/**
 * @author mkoch
 **/
public class MachineGunTurrentGui extends GenericTurrentGui {

   
	init {
   		engineObject = new org.mysoft.td.engine.objects.sample.MachineGunTurrent(world.engineWorld);
   		engineObject.position.x = x;
   		engineObject.position.y = y;
   		engineObject.init();
    
    	var midX = Constants.BRICK_SIZE * engineObject.size.width / 2;
    	var midY = Constants.BRICK_SIZE * engineObject.size.height / 2; 
    
    	line.startX = midX;
    	line.startY = midY;
    	
    	circle.centerX = midX;
    	circle.centerY = midY; 

		var circleFill = RadialGradient {
		    					centerX: midX;
		    					centerY: midY;
					    	    proportional: false
					    	    radius: (engineObject as GenericTurrent).range[(engineObject as GenericTurrent).level] * Constants.BRICK_SIZE 
					    	    stops: [
					    	           Stop {
					    	               color: Color.WHITE
					    	               offset: 0.0
					    	           },
					    	           Stop {
   					    	               color: Color.WHITE
   					    	               offset: 0.3
   					    	           },					    	           
   					    	           Stop {
   					    	               color: Color.BLUE
   					    	               offset: 1.0
   					    	           }					    	           
					    	       ]
					    	  };
		
		circle.fill = circleFill;
    }
    
	var rect = Rectangle {
		 		    width: bind(engineObject as GenericTurrent).size.width * Constants.BRICK_SIZE;
		 		    height: bind(engineObject as GenericTurrent).size.height * Constants.BRICK_SIZE;
		 		    fill: Color.GRAY;
		 		    stroke: Color.BLACK;
		 		    strokeWidth: 1;
		 		};



    var circle = Circle{
			    	radius : 0
			    	opacity: 0.5
			    };  
			    
    var line = Line {
         	    	visible: false
         	    	stroke: Color.RED;
         	    	strokeDashArray : [5.0, 4.0]
         	    	strokeWidth: 1
         	    	opacity: 0.0;
         	    };			     
       
	override function create():Node {

			 		
	    return Group {
	    		 	content: [
	    		 		circle,
						rect,
	    		 		line
	    		 	];  
	    		} ;
	}        
	
    override public function update(): Void {
       super.update();
       
       var t = (engineObject as MachineGunTurrent);
       
       if(t.state == ETurrentState.FIRE) {
           line.endX = (engineObject as AimingTurrent).target.realPosition.x * Constants.BRICK_SIZE - translateX + Constants.BRICK_SIZE/2;
           line.endY = (engineObject as AimingTurrent).target.realPosition.y * Constants.BRICK_SIZE - translateY + Constants.BRICK_SIZE/2;  
           line.visible = true;
           line.opacity = 1;
       } else if(t.state == ETurrentState.RELOAD) { 
       		line.opacity /= 2;
       } else {
           line.visible = false;
       }
       
       var tRadius = t.range[t.level] * Constants.BRICK_SIZE;
       var dr = tRadius - circle.radius;
       
       if(dr>10) {
           dr = 10;
       }
       
       if(rect.hover) {
       		if(circle.radius < tRadius) {
       		    circle.radius += dr;
       		}    
       } else {
           if(dr==0)
           		dr = 10;
           		
           if (circle.radius > 0) {
           		circle.radius -= dr;
           }
       }
    } 
    
}