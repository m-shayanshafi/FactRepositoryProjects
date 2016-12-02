/* Player.java
   This class describes a player.
   
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
 * This class describes a player. 
 * @author Yu Zhang
 */
public class Player
{

  /**
   * The player's side.
   */
  public char side;
  /**
   * The player's number.
   */
  public int id;

  /**
   * true is grabbed with the mouse
   */
  public boolean isGrabbed;
  
  /**
   * The current position of the player.
   */
  public Vector2d position;
  
  /**
   * The current moving(velocity) direction, also facing direction player. (in degree)
   */
  public double direction; 

  /**
   * Construct an empty player. Type is set to 'n', id is set to 0, position is
   * set to (0,0) and direction is set to 0.
   */
  public Player()
  {
    side = 'n';
    id = 0;
    position = new Vector2d();
    direction = 0;
    isGrabbed = false;
  }
  
  /**
   * Construct a player.
   *
   * @param side the player's side.
   * @param id   the player's number.
   * @param position the location of the player.
   * @param dir the  facing direction of the player.
   */
  public Player(char side, int id, Vector2d position, double dir)
  {
    this.side = side;
    this.id = id;
    this.position = new Vector2d(position);
    direction = dir;
    isGrabbed = false;
  }  
    
}
