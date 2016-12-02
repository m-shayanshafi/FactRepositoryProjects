//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package main;

import com.jme.input.Mouse;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.jme.input.MouseInput;


public class PlayerMouseLook extends MouseInputAction {
	
	public static float SENSITIVITY = 0.12f;
	
    //private boolean button0down;
    //private boolean button1down;
    
    
    public PlayerMouseLook(Mouse mouse, float speed) {
    	this.mouse = mouse;
        this.speed = speed;
        
        //button0down = false;
        //button1down = false;
    }
	
	public synchronized void performAction(InputActionEvent evt) {
		
		float x = mouse.getLocalTranslation().x;
		float y = mouse.getLocalTranslation().y;
		if (x != 0 || y != 0)
			Player.entity.increaseAngles(y * SENSITIVITY, x * SENSITIVITY);
		
		boolean buttonTest;
		
		buttonTest = MouseInput.get().isButtonDown(0);
		if (buttonTest /*&& !button0down*/)
			Player.entity.shoot();
		//button0down = buttonTest;
		
		buttonTest = MouseInput.get().isButtonDown(1);
		if (buttonTest /*&& !button1down*/)
			Player.entity.shoot();
		//button1down = buttonTest;
	}
	
}
