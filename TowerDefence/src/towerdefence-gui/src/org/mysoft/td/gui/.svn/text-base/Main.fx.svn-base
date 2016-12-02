package org.mysoft.td.gui;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.animation.transition.*;
import javafx.scene.input.MouseEvent;
import org.mysoft.td.gui.objects.*;
import org.mysoft.td.gui.objects.sample.*;
import org.mysoft.td.gui.world.*;

/**
 * @author mkoch
 */

var scene: Scene = Scene {
    			width: 640
    			height: 480
    			content: []
    		};
 
Stage {
    title : bind "Tower Defence System 0.0.1"
    scene: scene
}

var world = SampleWorldGui{};

var controller: GuiController = GuiController{
    world: world
    interval: 30ms
	scene: scene;
	
};

var random: java.util.Random = new java.util.Random((new java.util.Date()).getTime());

insert 
		[for (i in [4..58 step 2]) {
		    for (j in [4..42 step 2]) {
		        if(random.nextInt(3) == 0)
					MachineGunTurrentGui{world: world x: i y: j}
				else
					null
		    }    
		}]
		
		into controller.objects;


controller.start();