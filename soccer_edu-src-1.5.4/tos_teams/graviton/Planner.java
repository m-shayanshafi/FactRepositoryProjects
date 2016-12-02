/* Planner.java
   The AI player's high level strategy thinking tank.

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

import java.util.*;
import soccer.common.*;

public class Planner
{

  private World world;

  public Planner(World world)
  {
    this.world = world;  
  }

  private void kicking()
  {
    double best_value, value;
    double pass_dir, goal_dir, door_dir;
    int my_best_time, my_mean_time;
    int my_players;
    int your_best_time, your_mean_time;
    int your_players;
    int time;
    boolean goal_kick_free, direction_jammed;
    Player player;
    Enumeration players; 
    Vector teammates;
    Vector opponents;
    double xx, yy;
    double ang;
    Vector2d ballSpeed;
  
    if(world.see.player.position.distance(world.goal) < 35) world.actionType = World.SHOOT;
    else // passing possible?
    {
      best_value = -1000;
      for(int i=0; i<12; i++)
      {
		value =0;
		// the possible passing direction
		pass_dir = i * 30;
		pass_dir = Util.normal_dir(pass_dir);
	
		my_best_time = 100;
		my_mean_time = 0;
		your_best_time = 100;
		your_mean_time = 0;
	
		goal_kick_free = true;
		direction_jammed = false;
	
		my_players =0;
		your_players =0;
		
		// don't kick it out of the pitch
		xx = world.see.player.position.getX() + 10 * Math.cos(Util.Deg2Rad(pass_dir));
		yy = world.see.player.position.getY() + 15 * Math.sin(Util.Deg2Rad(pass_dir));
	
		if ( xx > World.LENGTH/2 || xx < -World.LENGTH/2 
		     || yy > World.WIDTH/2 || yy < -World.WIDTH/2 ) value -= 126;
	        
		player = null;
	        players = null;
	
		if(world.see.player.side == 'l')
		{
		  teammates = world.see.leftTeam;
		  opponents = world.see.rightTeam;
		}
		else
		{
		  teammates = world.see.rightTeam;
		  opponents = world.see.leftTeam;
		}
	
		// Are my teammates in this direction?
	        players = teammates.elements();
		while(players.hasMoreElements())
	        {
	          player = (Player) players.nextElement();
		  ang = world.see.player.position.direction(player.position) - pass_dir;
	          ang = Util.normal_dir(ang);
		  if(ang < 30)
		  {
	            if(world.see.player.position.distance(player.position) < 9)
		    { // direction jammed
		      value -= 500;
		      break;
		    }
	            if(world.see.player.position.distance(player.position) > 40)
		    { // too far away
		      break;
		    }
		    else
		    {
	              ballSpeed = Vector2d.polar(World.BALLMAXSPEED, pass_dir);
		      time = Executor.interceptTime(world, ballSpeed, player.position, new Vector2d());
		      my_players++;
		      if(time < my_best_time)
			my_best_time = time;
		      my_mean_time += time;
		    }
		  }
	        }
	
	   	
		// Are my opponents in this direction?
	        players = opponents.elements();
		while(players.hasMoreElements())
	        {
	          player = (Player) players.nextElement();
		  ang = world.see.player.position.direction(player.position) - pass_dir;
	          ang = Util.normal_dir(ang);
		  if(ang < 30)
		  {
	            if(world.see.player.position.distance(player.position) < 9)
		    { // direction jammed
		      value -= 500;
		      break;
		    }
	            if(world.see.player.position.distance(player.position) > 40)
		    { // too far away
		      break;
		    }
		    else
		    {
	              ballSpeed = Vector2d.polar(World.BALLMAXSPEED, pass_dir);
		      time = Executor.interceptTime(world, ballSpeed, player.position, new Vector2d());
		      your_players++;
		      if(time < your_best_time)
			your_best_time = time;
		      your_mean_time += time;
		    }
		  }
	        }
	
		if(my_mean_time == 0) my_mean_time = 100;
		else my_mean_time = my_mean_time / my_players;
	
		
		if(your_mean_time == 0) your_mean_time = 100;
		else your_mean_time = your_mean_time / your_players;
	        	
		value += (your_mean_time - my_mean_time) * 2;
		value += (your_best_time - my_best_time) * 10;
	
		// move the ball to opporent side is better
		if(world.see.player.side == 'l')
		  value += 120 * (-0.5 + (180 - Math.abs(pass_dir))/180);
		else value += 120 * (-0.5 + (Math.abs(pass_dir))/180);
	
		// I'd like to kick to opporent's goal
	        goal_dir = world.see.player.position.direction(world.goal);
		value += 120 * (-0.5 + (Math.abs(Math.abs(goal_dir) - Math.abs(pass_dir)))/180);
	
		// I'd like to avoid my own goal
	        door_dir = world.see.player.position.direction(world.door);
		value += 120 * (-0.5 + (Math.abs(Math.abs(door_dir) - Math.abs(pass_dir)))/180);	
	
		if(value > best_value)
		{
		  world.actionType = World.PASS;
		  world.force = 100;
		  world.direction = pass_dir;
		  best_value = value;
		}
      }
    }
  }

  public void planning()
  {
    if(world.stuck) world.actionType = World.STUCK;
    else if(world.offside) 
    {
      world.actionType = World.OFFSIDE;
      if(world.see.player.side == 'l')
        world.destination.setXY(world.see.player.position.getX() - 15, world.see.player.position.getY());
      else
        world.destination.setXY(world.see.player.position.getX() + 15, world.see.player.position.getY());
    }
    else if(world.IHaveBall) 
    {
      kicking();
    }
    else if(world.myBall) 
    {
      if(world.role != 0) 
      {
        if(!world.offsideT) world.actionType = World.CHASE;
	else world.actionType = World.MOVE;
      }
      else // i'm the goalie
      { 
        if(world.see.player.side == 'l')
	{
          if(Math.abs(world.see.player.position.getY()) < 20 && 
	     world.see.player.position.getX() < -30) world.actionType = World.CHASE;
	  else world.actionType = World.MOVE;
	}
	else
	{
          if(Math.abs(world.see.player.position.getY()) < 20 && 
	     world.see.player.position.getX() > 30) world.actionType = World.CHASE;
  	  else world.actionType = World.MOVE;
	}
      }
    }
    else
    {
      world.actionType = World.MOVE;
      if(world.status == 1) world.destination.setXY(world.offensePos);
      else if(world.status == 2) world.destination.setXY(world.defensePos);
      else world.destination.setXY(world.rolePos);
    }
  }
}
