/* World.java
   The AI player's view of the soccer field.

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

package tos_teams.graviton;

import soccer.common.*;

public class World 
{

  // Field data
  public static int LENGTH           = 100;
  public static int WIDTH            = 65;
  public static int SIDEWALK         = 5;
  public static int RADIUS           = 9;
  public static int GOAL_DEPTH       = 2;
  public static int GOAL_WIDTH       = 8;
  public static int GOALAREA_WIDTH   = 18;
  public static int GOALAREA_DEPTH   = 6;
  public static int PENALTY_WIDTH    = 40;
  public static int PENALTY_DEPTH    = 16;  
  public static int PENALTY_CENTER   = 12;
  public static int CORNER           = 1; 
  
  // High Level Actions Symble
  public static final int SHOOT           = 1;
  public static final int MOVE            = 2;
  public static final int PASS            = 3;
  public static final int CHASE           = 4;
  public static final int STUCK           = 5;
  public static final int OFFSIDE         = 6;

  // Once a player makes up his mind to do an action, the action will
  // be carried out for next INERTIA steps. So this means, the client
  // does not need to send a command every step, he only needs to send
  // a command every INERTIA steps.
  // This will also give a client more time to think before to actually 
  // do something.
  public static int INERTIA   =  1;  

  // physical factors of the soccer world

  // Heart rate ( the time each step spends in sec)
  public static double HEARTRATE    = 0.05;
  
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
  // MAXDASH * K1 * TIMETOMAX * (1 / HEARTRATE) = MAXSPEED * HEARTRATE
  public static double K1 = MAXSPEED * HEARTRATE * 
                            HEARTRATE / TIMETOMAX / MAXDASH;

  // K2 is the friction factor,
  // 0 = MAXDASH * k1 + MAXSPEED * HEARTRATE * K2;
  public static double K2 = MAXDASH / (MAXSPEED * HEARTRATE) * K1; 		
  
  // BK1 is the kick force factor 
  public static double BK1     = BALLMAXSPEED * HEARTRATE / MAXKICK; 
  

  // role information
  int role; // if role == 0, the player is goalie
  Vector2d rolePos;
  Vector2d offensePos;
  Vector2d defensePos;

  // latest info from server
  public InitData init = null;
  public RefereeData referee = null;
  public SeeData see = null;


  // used to store my position at previous step,
  // for calculating my velocity.
  public Vector2d prePosition = new Vector2d();
  public Vector2d myVelocity = new Vector2d();
  // high level action 
  public int actionType;
  public double force;
  public double direction;
  public Vector2d destination = new Vector2d();
  public int status = 0; // 1 means offensive, 2 means defensive, 0 means neutual

  // my action time
  public int actTime = 0;

  // used to store ball's position at previous step,
  // for calculating ball's velocity.
  public Vector2d ballPosition =  new Vector2d(); 
  public Vector2d ballVelocity = new Vector2d();
  
  // high-level knowledge
  public boolean IHaveBall = false;
  public boolean myBall = false;
  public boolean stuck = false;
  public boolean offside = false;
  public boolean offsideT = false;
  public int offsideTime = 0; // if player is offside, reverse for 'offisdeTime' steps
  public double stuckDir = 0; // when it's stuck, the direction to drive out of stuck
  public int stuckTime = 0; // if player is stuck, avoid it for 'avoidTime' steps
  public double distance2Ball;
  public double direction2Ball;  
  public Vector2d ball2Me = null; // ball's position relative to the player 
  
  public Vector2d goal;
  public Vector2d door;

  public World(InitData init, int role)
  {
    this.init = init;
    this.role = role;

    if(init.clientType == InitData.LEFT)
    {
      goal = new Vector2d(World.LENGTH/2, 0);
      door = new Vector2d(-World.LENGTH/2, 0);
    }
    else 
    {
      goal = new Vector2d(-World.LENGTH/2, 0);
      door = new Vector2d(World.LENGTH/2, 0);
    }

    
    switch(role)
    {
      case 0: // goalie
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-45, 0);
	offensePos = new Vector2d(-45, 0);
	defensePos = new Vector2d(-45, 0);
      }
      else 
      {
        rolePos = new Vector2d(45, 0);
	offensePos = new Vector2d(45, 0);
	defensePos = new Vector2d(45, 0);	
      }
      break;
      case 1: // forward
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-1, 0);
	offensePos = new Vector2d(25, 15);
	defensePos = new Vector2d(15, 15);	
      }
      else 
      {
        rolePos = new Vector2d(1, 0);
	offensePos = new Vector2d(-25, -15);
	defensePos = new Vector2d(-15, -15);	
      }
      break;
      case 2: // forward
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-2, -5);
	offensePos = new Vector2d(25, -15);
	defensePos = new Vector2d(15, -15);	
      }
      else 
      {
        rolePos = new Vector2d(2, 5);
	offensePos = new Vector2d(-25, 15);
	defensePos = new Vector2d(-15, 15);	
      }
      break;
      case 3: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-2, -28);
	offensePos = new Vector2d(15, -25);
	defensePos = new Vector2d(5, -25);	
      }
      else 
      {
        rolePos = new Vector2d(2, 28);
	offensePos = new Vector2d(-15, 25);
	defensePos = new Vector2d(-5, 25);	
      }
      break;
      case 4: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-2, 28);
	offensePos = new Vector2d(15, 25);
	defensePos = new Vector2d(5, 25);	
      }
      else 
      {
        rolePos = new Vector2d(2, -28);
	offensePos = new Vector2d(-15, -25);
	defensePos = new Vector2d(-5, -25);	
      }
      break;
      case 5: // 
      if(init.clientType == InitData.LEFT)
      {
        rolePos = new Vector2d(-10, -15);
	offensePos = new Vector2d(5, -15);
	defensePos = new Vector2d(-10, -15);	
      }
      else 
      {
        rolePos = new Vector2d(10, 15);
	offensePos = new Vector2d(-5, 15);
	defensePos = new Vector2d(10, 15);	
      }
      break;
      case 6: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-10, 15);
	offensePos = new Vector2d(5, 15);
	defensePos = new Vector2d(-10, 15);	
      }
      else 
      {
        rolePos = new Vector2d(10, -15);
	offensePos = new Vector2d(-5, -15);
	defensePos = new Vector2d(10, -15);	
      }
      break;
      case 7: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-35, -10);
	offensePos = new Vector2d(-10, -10);
	defensePos = new Vector2d(-25, -10);	
      }
      else 
      {
        rolePos = new Vector2d(35, 10);
	offensePos = new Vector2d(10, 10);
	defensePos = new Vector2d(25, 10);	
      }
      break;      
      case 8: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-35, 10);
	offensePos = new Vector2d(-10, 10);
	defensePos = new Vector2d(-25, 10);	
      }
      else 
      {
        rolePos = new Vector2d(35, -10);
	offensePos = new Vector2d(10, -10);
	defensePos = new Vector2d(25, -10);	
      }
      break;      
      case 9: // 
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-25, -20);
	offensePos = new Vector2d(0, -25);
	defensePos = new Vector2d(-15, -20);	
      }
      else 
      {
        rolePos = new Vector2d(25, 20);
	offensePos = new Vector2d(0, 25);
	defensePos = new Vector2d(15, 20);	
      }
      break;
      case 10: // right forward
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-25, 20);
	offensePos = new Vector2d(0, 25);
	defensePos = new Vector2d(-15, 20);	
      }
      else 
      {
        rolePos = new Vector2d(25, -20);
	offensePos = new Vector2d(0, -25);
	defensePos = new Vector2d(15, -20);	
      }
      break;
      default:
      if(init.clientType == InitData.LEFT) 
      {
        rolePos = new Vector2d(-45, 0);
	offensePos = new Vector2d(-45, 0);
	defensePos = new Vector2d(-45, 0);	
      }
      else 
      {
        rolePos = new Vector2d(45, 0);
	offensePos = new Vector2d(45, 0);
	defensePos = new Vector2d(45, 0);	
      }
      break;      
    }
      
    
  }
    

}
