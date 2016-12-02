/* World.java
   The player's view of the soccer field.

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
   
   with modifications by Vadim Kyrylov:
   January 2006.
*/

	
package soccer.client;

import java.util.*;

import soccer.client.view.Field;
import soccer.common.*;
import soccer.server.SoccerWorld;	// consider using this as parent (not implemented yet) 

public class World 
{	
	// High Level Actions Symble
	public static final int SHOOT           = 1;
	public static final int MOVE            = 2;
	public static final int PASS            = 3;
	public static final int CHASE           = 4;
	
	// Low Level Action Symble
	public static final int DRIVE           = 5;
	public static final int KICK            = 6;
	
	// Once a player makes up his mind to do an action, the action will
	// be carried out for next INERTIA steps. So this means, the client
	// does not need to send a command every step, he only needs to send
	// a command every INERTIA steps.
	// This will also give a client more time to think before to actually 
	// do something.
	public static int INERTIA   =  1;
	
	// physical factors of the soccer world
	
	// Heart rate ( the time each step spends in sec)
	public static double SIM_STEP_SECONDS    = 0.05;	// this will be overriden by the server
	
	// player will have chance to get the ball when ball-player distance is under control range
	public static double CONTROLRANGE   = 1.5; 
	
	// maximum ball speed in m/s
	public static double BALLMAXSPEED   = 23;
	
	// friction factor, such as a1 = -FRICTIONFACTOR * v0;
	public static double FRICTIONFACTOR     = 0.065;
	
	// player's maximum speed (in m/s)
	public static double MAXSPEED   = 7; 
	
	// the time a player needs to reach full speed (in sec)
	// without friction
	public static double TIMETOMAX  = 1;
	
	// player's maximum dash force 
	public static double MAXDASH  = 100;
	
	// player's maximum kick force 
	public static double MAXKICK  = 100;
	
	// max dribble force factor, when player is dribbling, 
	//the max force he can use to dash is MAXDASH * DRIBBLE 
	public static  double DRIBBLEFACTOR  = 0.4;
	
	// players collides when their distance is smaller than COLLIDERANGE
	public static double COLLIDERANGE = 0.6;
	
	// K1 is the force factor, MAXSPEED speed divided by TIMETOMAX
	// MAXDASH * K1 * TIMETOMAX * (1 / SIM_STEP_SECONDS) = MAXSPEED * SIM_STEP_SECONDS
	public static double K1 = MAXSPEED * SIM_STEP_SECONDS * 
	            SIM_STEP_SECONDS / TIMETOMAX / MAXDASH;
	
	// K2 is the friction factor,
	// 0 = MAXDASH * k1 + MAXSPEED * SIM_STEP_SECONDS * K2;
	public static double K2 = MAXDASH / (MAXSPEED * SIM_STEP_SECONDS) * K1; 		
	
	// BK1 is the kick force factor 
	public static double BK1     = BALLMAXSPEED * SIM_STEP_SECONDS / MAXKICK;
	
	// latest info from server
	public InitData 	init = null;
	public InfoData 	infoData = null;
	public RefereeData 	referee = null;	
	public SeeData 		see = null;
	public ViewData 	view = null;
	public HearData 	message = null;
	
	public HearData 	leftM = null;
	public HearData 	rightM = null;
	
	// my status
	public Player 	me;
	// my drive force and direction
	public double 		force;
	public double 		direction;
	// my iAmOffside status
	public int 			status;
	// used to store my position at previous step,
	// for calculating my velocity.
	public Vector2d 	prePosition = new Vector2d();
	public Vector2d 	myVelocity = new Vector2d();
	
	// my action time
	public int actionTime = 0;
	
	// ball status
	public Ball 		ball;
	
	// used to store ball's position at previous step,
	// for calculating ball's velocity.
	public Vector2d 	ballPosition =  new Vector2d(); 
	public Vector2d 	ballVelocity = new Vector2d();  
	
	// players
	public Vector 		leftTeam;
	public Vector 		rightTeam;
	
	// high-level knowledge
	public boolean 		isBallKickable = false;
	public boolean 		letMeKick = false;
	public Vector2d 	ball2Me = null; // ball's position relative to the player
	public double 		distance2Ball;
	public double 		direction2Ball;
	
	
	
	public int 			prePeriod = -1;
	public int 			preMode = RefereeData.BEFORE_KICK_OFF;
	public char 		preController = 'f';
	public int 			leftGoal = 0;
	public int 			rightGoal = 0;
		
	
	// high level action 
	public int actionType;
	
	public Vector2d destination = new Vector2d();
	
	public World(InitData init)
	{
		this.init = init;  
	}
	
	public World()
	{
	
	}  
	
	public Vector2d getGoalPosition() 
	{
		return new Vector2d(Field.LENGTH / 2, 0);
	}  
	
	
	// create default world
	public void init() 
	{
		ball = new Ball();
		
		leftTeam = new Vector();
		rightTeam = new Vector();	
		
		for ( int i=0; i<SoccerWorld.TEAM_FULL; i++ ) {
			Player plr1 = new Player('l', i+1, new Vector2d(), 0);
			leftTeam.addElement( plr1 );
			Player plr2 = new Player('r', i+1, new Vector2d(), 0);
			rightTeam.addElement( plr2 );
		}	
	}

	// for debugging only
	public void printBall()
	{
		System.out.println();
		System.out.println("<< Ball parameters: >> position: " + ball.position );	
		System.out.print("  controllerType = " + ball.controllerType );	
		System.out.print("  controllerId = " + ball.controllerId );	
		System.out.println("  isGrabbed = " + ball.isGrabbed );	
		System.out.println();
	}

	// for debugging only
	public void printTeams()
	{
		System.out.println();
		System.out.println("<< Left team parameters: >>");	
		for ( int i=0; i<leftTeam.size(); i++ ) {
			Player plr = (Player)leftTeam.elementAt(i);
			System.out.println(plr.id + " " + plr.side + "  position: " + plr.position );	
			System.out.println("  direction = " + plr.direction + "  isGrabbed = " + plr.isGrabbed );
		}	
		System.out.println();
		System.out.println("<< Right team parameters: >>");	
		for ( int i=0; i<rightTeam.size(); i++ ) {
			Player plr = (Player)rightTeam.elementAt(i);
			System.out.println(plr.id + " " + plr.side + "  position: " + plr.position );	
			System.out.println("  direction = " + plr.direction + "  isGrabbed = " + plr.isGrabbed );
		}	
		System.out.println();
	}

}
