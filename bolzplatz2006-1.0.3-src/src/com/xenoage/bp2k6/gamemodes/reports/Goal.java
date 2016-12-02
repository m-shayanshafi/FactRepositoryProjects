/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.gamemodes.reports;

import com.xenoage.bp2k6.util.XMLReader;

import org.dom4j.Element;


/**
 * This class conatains information
 * about a goal: The shooter of the goal, the
 * team that scored (team 0 or 1)
 * and the time of the goal.
 * 
 * @author awr
 */
public class Goal
{
  private int team;
  private int shooter;
  private int type;
  private int timePhase;
  private int time;
  
  public static int TIMEPHASE_FIRSTHALF = 0;
  public static int TIMEPHASE_SECONDHALF = 1;
  public static int TIMEPHASE_FIRSTOVERTIMEHALF = 2;
  public static int TIMEPHASE_SECONDOVERTIMEHALF = 3;
  public static int TIMEPHASE_PENALTYSHOOTOUT = 4;
  
  
  /**
   * Constructor.
   * @param team       The index of the team that scored.
   *                   0: home team, 1: visiting team.
   * @param shooter    The index of the player that shot the goal.
   * @param type       Was this goal a special one?
   *                   0: No, it was a normal goal;
   *                   1: Penalty kick goal;
   *                   2: Direct free kick goal;
   *                   3: This was an own goal, the shooter
   *                      index belongs to the other team.
   * @param timePhase  The phase of the match when the goal was shot.
   *                   Use the <code>TIMEPHASE</code>-constants.
   * @param time       Dependent on the timephase:
   *                   timephase 0, 1, 2, 3: the second of the goal,
   *                   e.g. timephase=1, time=92 means 46:32;
   *                   timephase 4: the number of the penalty shoot,
   *                   e.g. 3 means it was the 3rd kick (the second
   *                   one of the first team).
   */
  public Goal(int team, int shooter, int type,
    int timePhase, int time)
  {
    this.team = team;
    this.shooter = shooter;
    this.type = type;
    this.timePhase = timePhase;
    this.time = time;
  }
  
  
  /**
   * Constructor.
   * Creates a goal from the given XML element.
   */
  public Goal(Element eGoal)
  {
    team = XMLReader.readAttributeValueInt(eGoal, "team");
    shooter = XMLReader.readAttributeValueInt(eGoal, "shooter");
    type = XMLReader.readAttributeValueInt(eGoal, "type");
    timePhase = XMLReader.readAttributeValueInt(eGoal, "timePhase");
    time = XMLReader.readAttributeValueInt(eGoal, "time");
  }
  
  
  /**
   * Save this goal within the given
   * XML element. A new child element "goal"
   * with all information about the goal
   * is created.
   */
  public void saveToXML(Element parentElement)
  {
    //create goal element
    Element eGoal = parentElement.addElement("goal");
    eGoal.addAttribute("team", String.valueOf(team));
    eGoal.addAttribute("shooter", String.valueOf(shooter));
    eGoal.addAttribute("type", String.valueOf(type));
    eGoal.addAttribute("timephase", String.valueOf(timePhase));
    eGoal.addAttribute("time", String.valueOf(time));
  }
  
  
  //Getters...
  
  public int getTeam()
  {
    return team;
  }
  
  public int getShooter()
  {
    return shooter;
  }
  
  public int getType()
  {
    return type;
  }
  
  public int getTimePhase()
  {
    return timePhase;
  }
  
  public int getTime()
  {
    return time;
  }
  
  
  
}
