/* Sensor.java
   This class get sensing info from the server and build the world from it

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
import java.io.*;
import java.util.*;


public class Sensor
{

	// Heart rate ( the time each beat spends in sec)
	public static double HEARTRATE = 0.05;
	// End time (in min)
	public static int END = 30;
	private int round = (int) (END * (60 / HEARTRATE));

  private World world;
  private Planner planner;
  private Executor executor;
  
  public Sensor(World world, Planner planner, Executor executor)
  {
    this.world = world;
    this.planner = planner;
    this.executor = executor;
  }

  
  public void sensing(Packet info)  throws IOException
  {
    Player player = null;
    Enumeration players = null;

    // process the info
    if(info.packetType == Packet.SEE)
    {
      world.see = (SeeData)info.data;
      
      // Do I have the ball?
      if( world.see.ball.controllerType == world.see.player.side && 
	  world.see.ball.controllerId == world.see.player.id) world.IHaveBall = true;
      else world.IHaveBall = false;
      
      // find out if I am nearer to the ball than my teammates
      world.myBall = true;
      world.distance2Ball = world.see.player.position.distance(world.see.ball.position);
      world.direction2Ball = world.see.player.position.direction(world.see.ball.position);
      world.ball2Me = Vector2d.polar(world.distance2Ball,world.direction2Ball);
      if(world.see.player.side == 'r') players = world.see.rightTeam.elements();
      else players = world.see.leftTeam.elements();
      int i = 0;
      while(players.hasMoreElements())
      {
        player = (Player) players.nextElement();
	double dis = player.position.distance(world.see.ball.position);
	if(dis < world.distance2Ball) i++;
	if(i > 1)
	{
          world.myBall = false;
	  break;
	}
      }

      // find out if it's offensive or defensive
      if(world.see.ball.controllerType == world.see.player.side) world.status = 1;
      else world.status = 2;

      world.offsideT = false;

      // find out if it's offside
      if(world.offside)
      {
        if(world.offsideTime == 0) world.offside = false;
	else world.offsideTime--;
      }
      else
      {
        if(world.see.status == SeeData.NO_OFFSIDE)
	{
	  world.offside = false;
	  world.offsideT = false;
	}
	else if(world.see.status == SeeData.OFFSIDE)
	{
	  world.offside = true;
	  world.offsideT = true;
	  world.offsideTime = 30;
	}
	else if(world.see.status == SeeData.T_OFFSIDE)
	{
	  world.offside = false;
	  world.offsideT = true;
	}	
      
      }

      // find out ball's velocity
      Vector2d.subtract(world.see.ball.position, world.ballPosition, world.ballVelocity);
      world.ballPosition.setXY(world.see.ball.position);      

      // find out my velocity
      Vector2d.subtract(world.see.player.position, world.prePosition, world.myVelocity);
      world.prePosition.setXY(world.see.player.position);
      

      // find out if I am stuck
      if(world.stuck)
      {
        if(world.stuckTime == 0) world.stuck = false;
	else world.stuckTime--;
      }
      else
      {
        players = world.see.leftTeam.elements();
        while(players.hasMoreElements())
        {
          player = (Player) players.nextElement();
          double dis = player.position.distance(world.see.player.position);
	  if(dis < 2 && world.distance2Ball > 3 ) 
	  {
            world.stuck = true;
	    world.stuckTime = 5;
	    world.stuckDir = Math.random() * 360;
	    break;
	  }
        }
        players = world.see.rightTeam.elements();
        while(players.hasMoreElements())
        {
          player = (Player) players.nextElement();
	  double dis = player.position.distance(world.see.player.position);
	  if(dis < 2 && world.distance2Ball > 3 ) 
	  {
            world.stuck = true;
	    world.stuckTime = 5;
	    world.stuckDir = Math.random() * 360;
	    break;
	  }
        }
      
      }

	  int  reactionTime = world.see.time - world.actTime;
	  if(reactionTime < 0) reactionTime += round;      
      if(reactionTime >= World.INERTIA || world.IHaveBall)
      {
        planner.planning();
        executor.executing();
	world.actTime = world.see.time;
      }
    }
	  
  }

}
