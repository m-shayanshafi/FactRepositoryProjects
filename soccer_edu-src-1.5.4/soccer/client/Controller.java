/* Controller.java

   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.client;

import java.awt.event.*;
import java.io.*;

import soccer.client.view.j3d.FieldJ3D;
import soccer.common.*;

public class Controller
	implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

	protected World world;
	protected SoccerMaster soccerMaster;

	public Controller() {
		
	}
	
	public Controller(World world, SoccerMaster soccerMaster) {
		this.world = world;
		this.soccerMaster = soccerMaster;
	}

	// Handle the key events
	public void keyTyped(KeyEvent e) {
		synchronized (world) {
			switch (e.getKeyChar()) {
				
				case '1' :
					world.force = 0;
					world.actionType = World.DRIVE;
				break;
				
				case '2' :
					world.force = 15;
					world.actionType = World.DRIVE;
				break;
				
				case '3' :
					world.force = 30;
					world.actionType = World.DRIVE;
				break;
				
				case '4' :
					world.force = 45;
					world.actionType = World.DRIVE;
				break;
				
				case '5' :
					world.force = 60;
					world.actionType = World.DRIVE;
				break;
				
				case '6' :
					world.force = 70;
					world.actionType = World.DRIVE;
				break;
				
				case '7' :
					world.force = 80;
					world.actionType = World.DRIVE;
				break;
				
				case '8' :
					world.force = 90;
					world.actionType = World.DRIVE;
				break;
				
				case '9' :
					world.force = 100;
					world.actionType = World.DRIVE;
				break;
				
				case ' ' :
					if (world.isBallKickable)
						world.actionType = World.KICK;
					else
						world.actionType = World.CHASE;
				break;
			}
		}
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		synchronized (world) {
			
			switch (e.getKeyCode()) {
				
				case KeyEvent.VK_UP :
					if (e.isShiftDown())
						world.force += 30;
					else
						world.force += 15;
					if (world.force > 100)
						world.force = 100;
					world.actionType = World.DRIVE;
				break;
				
				case KeyEvent.VK_DOWN :
					if (e.isShiftDown())
						world.force -= 30;
					else
						world.force -= 15;
					if (world.force < 0)
						world.force = 0;
					world.actionType = World.DRIVE;
				break;
				
				case KeyEvent.VK_LEFT :
					if (e.isShiftDown())
						world.direction += 40;
					else
						world.direction += 10;
					world.direction = Util.normal_dir(world.direction);
					world.actionType = World.DRIVE;
				break;
				
				case KeyEvent.VK_RIGHT :
					if (e.isShiftDown())
						world.direction -= 40;
					else
						world.direction -= 10;
					world.direction = Util.normal_dir(world.direction);
					world.actionType = World.DRIVE;
				break;
				
				case KeyEvent.VK_ENTER :
					world.actionType = World.SHOOT;
				break;

			}
		}

	}

	// Handle the mouse events
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {

		if(soccerMaster.isIn3D())	
		{
			soccerMaster.arena3D.requestFocus();
			((FieldJ3D)(soccerMaster.arena3D)).myCanvas3D.requestFocus();
		}
		else {
			soccerMaster.arena2D.requestFocus();
		} 

		synchronized (world) {
			if (e.getModifiers() == InputEvent.BUTTON1_MASK)
				// left button clicked
				{
				world.destination.setXY(e.getX(), e.getY());
				soccerMaster.arena2D.user2soccer(world.destination);
				world.actionType = World.MOVE;
			} else if (
				e.getModifiers() == InputEvent.BUTTON3_MASK)
				// right button clicked
				{
				world.destination.setXY(e.getX(), e.getY());
				soccerMaster.arena2D.user2soccer(world.destination);
				world.actionType = World.PASS;
			}
		}
	}

	// Handle the mouse events
	public void mouseMoved(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	// send message out
	public void actionPerformed(ActionEvent e) {

		TalkData talk = new TalkData(soccerMaster.input.getText());
		Packet p =
			new Packet(
				Packet.TALK,
				talk,
				soccerMaster.getAddress(),
				soccerMaster.getPort());
		try {
			soccerMaster.getTransceiver().send(p);
		} catch (IOException ex) {
		}
		soccerMaster.input.setText("");
	}

}
