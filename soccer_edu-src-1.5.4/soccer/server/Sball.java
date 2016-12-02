/* Sball.java

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

package soccer.server;

import soccer.common.Ball;

public class Sball extends MovingObj
{  
	// player will have chance to get the ball when ball-player distance is under control range
	public static double CONTROLRANGE 	   = 1.5; 
	
	// Once the player has kicked the ball, he has to wait at least NOBALL sec to have  
	// the ball under his control again; this parameter is critical for dribbling 
	public static double NOBALL = 0.50;

	// maximum ball speed in m/s
	public static double MAXSPEED   = 30;
	
	// K1 is the kick force factor 
	public static double K1     = MAXSPEED * HeartOfWorld.SIM_STEP_SECONDS / Splayer.MAXKICK; 
	
	// friction factor, such as a1 = -FRICTIONFACTOR * v0;
	public static double FRICTIONFACTOR     = 0.05;
	
	// max random factor for ball movement
	public static double RANDOM     = 0.02; 
	
	// the player who has the ball
	public char controllerType;
	public int controllerId;
	public int side;
	public boolean free;

  /**
   * true is grabbed with the mouse or by the goalie
   */
  public boolean isGrabbed;
 
	public Sball()
	{
		super();
		controllerType = 'f';
		controllerId = 0;
		free = true;
		isGrabbed = false;
	}    
	
	// set the ball at position(x,y)
	public void set(double x, double y)
	{
		position.setXY(x,y);
		velocity.setXY(0,0);
		acceleration.setXY(0,0);
		controllerType = 'f';
		controllerId = 0;
		free = true;
	}    
	
	// set the ball at position(x,y)
	public void set(Splayer player, boolean grabbed )
	{
		position.setXY( player.position );
		velocity.setXY( player.velocity );
		acceleration.setXY(0,0);
		controllerType = player.side;
		controllerId = player.id;	
		isGrabbed = grabbed;
		free = false;
	}   

	// reset Sball data using the input Ball oject
	public void assign( Ball ball )
	{
		set( ball.position.getX(), ball.position.getY() );
		controllerType = ball.controllerType; 
		controllerId = ball.controllerId; 
		isGrabbed = ball.isGrabbed; 
	}
	
	// copy Sball data using the input Sball oject
	public void copy( Sball sball )
	{
		set( sball.position.getX(), sball.position.getY() );
		controllerType = sball.controllerType; 
		controllerId = sball.controllerId; 
		isGrabbed = sball.isGrabbed; 
	}
		
}
