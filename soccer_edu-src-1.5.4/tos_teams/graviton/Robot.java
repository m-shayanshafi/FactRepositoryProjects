/* Robot.java
   This class get sensing info, plan its move and execute it.

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

public class Robot extends Thread 
{
  private Transceiver transceiver;
  
  private World world;
  private Sensor sensor;
  private Planner planner;
  private Executor executor;
  
  private Packet info;
  
  public Robot(Transceiver transceiver, InitData init, int role)
  {
    this.transceiver = transceiver;	  
    	
    world = new World(init, role);
    planner = new Planner(world);
    executor = new Executor(world, transceiver);    
    sensor = new Sensor(world, planner, executor);
   
  }
 
  public void run() 
  {

    while(true)
    {

      try
      {
        // sensor collects info from server, build its world
        info = transceiver.receive();
	sensor.sensing(info);
      }
      catch(IOException e)
      {

      }

    }
  } 
   
}
