package org.mysoft.td.gui.objects;

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import org.mysoft.td.engine.objects.*;
import org.mysoft.td.engine.world.*;
import org.mysoft.td.gui.world.*;

/**
 * @author mkoch
 */
public abstract class GenericObjectGui extends CustomNode {
    
    public var engineObject: GenericObject;
   
    public var world: GenericWorldGui;
	public var name: String;
   	public var x: Integer;
   	public var y: Integer;   
    
    public abstract function update(): Void;
    
    public function toRemove(): Boolean {
        return false;
    }

}