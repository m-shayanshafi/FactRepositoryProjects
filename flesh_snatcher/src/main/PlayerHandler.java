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

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

public class PlayerHandler extends InputHandler {
	
	PlayerMouseHandler mouseHandler;
	
	public PlayerHandler() {
		
		/** Assign key E to action "fw". */
        KeyBindingManager.getKeyBindingManager().set( "fw",
                KeyInput.KEY_E );
        
        /** Assign key D to action "bw". */
        KeyBindingManager.getKeyBindingManager().set( "bw",
                KeyInput.KEY_D );
        
        /** Assign key S to action "le". */
        KeyBindingManager.getKeyBindingManager().set( "le",
                KeyInput.KEY_S );
		
        /** Assign key F to action "ri". */
        KeyBindingManager.getKeyBindingManager().set( "ri",
                KeyInput.KEY_F );
        
        /** Assign key SPACE to action "jmp". */
        KeyBindingManager.getKeyBindingManager().set( "jmp",
                KeyInput.KEY_SPACE );
        
        mouseHandler = new PlayerMouseHandler(1f);
        addToAttachedHandlers( mouseHandler );
        
	}
	
	
	public void update() {
		
		Player.entity.setForwardMove(0);
		Player.entity.setSideMove(0);
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "fw", true ) ) {
            Player.entity.setForwardMove(1);
        }
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "bw", true ) ) {
			Player.entity.setForwardMove(-1);
        }
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "le", true ) ) {
			Player.entity.setSideMove(-1);
        }
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "ri", true ) ) {
			Player.entity.setSideMove(1);
        }
		
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "jmp", false ) ) {
			Player.entity.jump();
        }
	}
}
