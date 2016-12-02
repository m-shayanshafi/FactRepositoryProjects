/* Ball.java
   This class describes a ball.

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
   
								
	Modifications by Vadim Kyrylov 
							January 2006

*/


package soccer.common;

/**
 * This class describes a ball. 
 * @author Yu Zhang
 */
public class Ball
{

  /**
   * The current position of the ball.
   */
  public Vector2d position;

  /**
   * This describes which team has the control of the ball.
   * 'l' means left team , 'r' means right team and 'f' means the ball is free.
   */
  public char controllerType;
  /** 
   * The ball controller's id.
   */
  public int controllerId;
  
  /**
   * true is grabbed with the mouse or by the goalie
   */
  public boolean isGrabbed;
 
   /**
   * Construct an empty ball. controllerType is set to 'f', 
   * controllerId is set to 0, position is set to (0,0).
   */
  public Ball()
  {
    position = new Vector2d();
    controllerType = 'f';
    controllerId = 0;
    isGrabbed = false;
  }
  
  /**
   * Construct a ball.
   *

   * @param position the location of the ball.
   * @param controllerType the ball player's side.
   * @param controllerId   the player's number.
   */
  public Ball(Vector2d position, char controllerType, int controllerId)
  {
    this.position = new Vector2d(position);
    this.controllerType = controllerType;
    this.controllerId = controllerId;
  }  
    
}
