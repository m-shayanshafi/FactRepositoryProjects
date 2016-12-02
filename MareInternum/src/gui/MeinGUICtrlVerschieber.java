/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package gui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import entities.Konstanten;

public class MeinGUICtrlVerschieber implements MouseListener, MouseMotionListener{
	
	/**
	 * component which is moved
	 */
	private Component meineComponent;
	
	/**
	 * absolute x values of moving component
	 */
	private int absX;
	
	/**
	 * absolute y values of moving component
	 */
	private int absY;	
	
	/**
	 * id of moving component
	 */
	private int id;
	
	/**
	 * flag for switching moving on and of
	 */
	private boolean moving = false;
	
	/**
	 * flag which says if right of left button of mouse should be used
	 * default is right mouse button = false
	 */
	private boolean useLeftMouseButton = false;
	
	public MeinGUICtrlVerschieber(Component component, int id, boolean useLeftButton){
		this.absY = 0;
		this.absX = 0;
		this.meineComponent = component;
		this.id = id;
		this.useLeftMouseButton = useLeftButton;
	}
	
	public void mouseExited(MouseEvent e){}
	
	/**
	 * handles mouse pressed event and starts mode for moving the component
	 */
	public void mousePressed(MouseEvent e){
		
		if((e.getButton() == MouseEvent.BUTTON3 && !useLeftMouseButton)
				||(e.getButton() == MouseEvent.BUTTON1 && useLeftMouseButton)){
			absX = e.getX();
			absY = e.getY();
			moving = true;
		}
		else{
			moving = false;
		}
		
	}
	
	/**
	 * empty stub to satisfy interface
	 */
	public void mouseEntered(MouseEvent e){}
	
	/**
	 * empty stub to satisfy interface
	 */
	public void mouseReleased(MouseEvent e){}
	
	/**
	 * empty stub to satisfy interface
	 */
	public void mouseClicked(MouseEvent e){}
	
	/**
	 * empty stub to satisfy interface
	 */
	public void mouseOver(MouseEvent e){}
	
	/**
	 * empty stub to satisfy interface
	 */
	public void mouseMoved(MouseEvent e){}
	
	/**
	 * handles event if mouse is dragged
	 */
	public synchronized void mouseDragged(MouseEvent e){
		int valueY = e.getY() - absY;
		int valueX = e.getX() - absX;
		if(id == Konstanten.PNL_KARTE){
			if(meineComponent.getX() + meineComponent.getWidth() + valueX < 150 ||
					meineComponent.getX() + valueX > Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 150){
				valueX = 0;
			}
			if(meineComponent.getY() + meineComponent.getHeight() + valueY < 150 ||
					meineComponent.getY() + valueY > Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 250){
				valueY = 0;
			}
		}else{
			if(meineComponent.getX() + valueX < 0 || meineComponent.getX() + meineComponent.getWidth() + valueX > Toolkit.getDefaultToolkit().getScreenSize().getWidth()){
				valueX = 0;
			}
			if(meineComponent.getY() + valueY < 0 || meineComponent.getY() + meineComponent.getHeight() + valueY > Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 110){
				valueY = 0;
			}
		}
			
		
		if(moving){
			this.verschieben(valueX, valueY);
		}
		
	}
	
	/**
	 * register new coordinates of moved component
	 * @param valueX
	 * @param valueY
	 */
	public void verschieben(int valueX,int valueY){
		meineComponent.setLocation(meineComponent.getX() + valueX, meineComponent.getY() + valueY);
	} 
}
