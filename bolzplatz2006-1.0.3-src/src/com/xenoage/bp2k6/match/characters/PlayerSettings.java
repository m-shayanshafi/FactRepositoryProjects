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
package com.xenoage.bp2k6.match.characters;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * This class stores the settings for all players,
 * like default speed, pass speed, animation
 * speed, and so on.
 * The values are loaded from the xml file
 * "/data/config/player.xml"
 *
 * @author Andi
 */
public class PlayerSettings
{

  //player settings
  private static float playerSpeed;
  private static float playerSprintFactor;
  private static float playerRotationSpeed;

  //pass settings
  private static float passSpeedHMin;
  private static float passSpeedVMin;
  private static float passSpeedHMax;
  private static float passSpeedVMax;

  //shoot settings
  private static float shootSpeedH;
  private static float shootSpeedHAdd;
  private static float shootSpeedV;
  private static float shootSpeedLiftV;
  private static float shootSpeedLiftHRed;
  
  //goalkick settings
  private static float goalkickSpeedHMin;
  private static float goalkickSpeedVMin;
  private static float goalkickSpeedHMax;
  private static float goalkickSpeedVMax;
  
  //cornerkick settings
  private static float cornerkickSpeedHMin;
  private static float cornerkickSpeedVMin;
  private static float cornerkickSpeedHMax;
  private static float cornerkickSpeedVMax;
  
  //throwin settings
  private static float throwinSpeedHMin;
  private static float throwinSpeedVMin;
  private static float throwinSpeedHMax;
  private static float throwinSpeedVMax;
  
  //penaltykick settings
  private static float penaltykickSpeedHMin;
  private static float penaltykickSpeedVMin;
  private static float penaltykickSpeedHMax;
  private static float penaltykickSpeedVMax;
  
  //freekick settings
  private static float freekickSpeedHMin;
  private static float freekickSpeedVMin;
  private static float freekickSpeedHMax;
  private static float freekickSpeedVMax; 
  
  //bounce settings
  private static float bounceBallSpeed;
  private static float bounceGoalkeeperBallSpeed;
  private static float bounceVelFactor;
  private static float bounceVelRandom;


  /**
   * Load settings from "/data/config/player.xml"
   * Call this method before working with any
   * classes from com.xenoage.bp2k6.match.players.
   */
  public static void loadSettings()
  {
    //load settings from XML file
    try
    {
      SAXReader reader = new SAXReader();
      reader.setEncoding(XMLReader.encoding);
      
      Document doc = reader.read("data/config/player.xml");
      Element root = doc.getRootElement();
      Element e = null;

      //player settings
      e = root.element("player");
      playerSpeed = XMLReader.readElementValueFloat(e.element("speed"));
      playerSprintFactor = XMLReader.readElementValueFloat(e.element("sprintfactor"));
      playerRotationSpeed = XMLReader.readElementValueFloat(e.element("rotationspeed"));

      //pass settings
      e = root.element("pass");
      passSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      passSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      passSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      passSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));

      //shoot settings
      e = root.element("shoot");
      shootSpeedH = XMLReader.readElementValueFloat(e.element("speedh"));
      shootSpeedHAdd = XMLReader.readElementValueFloat(e.element("speedhadd"));
      shootSpeedV = XMLReader.readElementValueFloat(e.element("speedv"));
      shootSpeedLiftV = XMLReader.readElementValueFloat(e.element("speedliftv"));
      shootSpeedLiftHRed = XMLReader.readElementValueFloat(e.element("speedlifthred"));
      
      //goalkick settings
      e = root.element("goalkick");
      goalkickSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      goalkickSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      goalkickSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      goalkickSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));
      
      //cornerkick settings
      e = root.element("cornerkick");
      cornerkickSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      cornerkickSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      cornerkickSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      cornerkickSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));
      
      //throwin settings
      e = root.element("throwin");
      throwinSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      throwinSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      throwinSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      throwinSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));
      
      //penaltykick settings
      e = root.element("penaltykick");
      penaltykickSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      penaltykickSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      penaltykickSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      penaltykickSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));
      
      //freekick settings
      e = root.element("freekick");
      freekickSpeedHMin = XMLReader.readElementValueFloat(e.element("speedhmin"));
      freekickSpeedVMin = XMLReader.readElementValueFloat(e.element("speedvmin"));
      freekickSpeedHMax = XMLReader.readElementValueFloat(e.element("speedhmax"));
      freekickSpeedVMax = XMLReader.readElementValueFloat(e.element("speedvmax"));
      
      //bounce settings
      e = root.element("bounce");
      bounceBallSpeed = XMLReader.readElementValueFloat(e.element("ballspeed"));
      bounceGoalkeeperBallSpeed =
        XMLReader.readElementValueFloat(e.element("goalkeeperballspeed"));
      bounceVelFactor = XMLReader.readElementValueFloat(e.element("velfactor"));
      bounceVelRandom = XMLReader.readElementValueFloat(e.element("velrandom"));
      
      
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new PlayerSettings(),
        "Player settings could not be read from \"data/config/player.xml\"" +
          " could not be loaded! Details:");
      GameEngine.fatalError(new PlayerSettings(), ex);
    }
  }


  /**
   * Gets the player-speed in m/s.
   */
  public static float getPlayerSpeed()
  {
    return playerSpeed;
  }


  /**
   * Gets the speed factor for sprinting.
   */
  public static float getPlayerSprintFactor()
  {
    return playerSprintFactor;
  }


  /**
   * Gets the player-rotation-speed in degrees/s.
   */
  public static float getPlayerRotationSpeed()
  {
    return playerRotationSpeed;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a pass in m/s.
   */
  public static float getPassSpeedHMin()
  {
    return passSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a pass in m/s.
   */
  public static float getPassSpeedVMin()
  {
    return passSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a pass in m/s.
   */
  public static float getPassSpeedHMax()
  {
    return passSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a pass in m/s.
   */
  public static float getPassSpeedVMax()
  {
    return passSpeedVMax;
  }


  /**
   * Gets the horizontal ball-speed of a goal-shoot in m/s.
   */
  public static float getShootSpeedH()
  {
    return shootSpeedH;
  }


  /**
   * Gets the additional keypress-duration dependent horizontal speed of a goal-shoot in m/s.
   */
  public static float getShootSpeedHAdd()
  {
    return shootSpeedHAdd;
  }


  /**
   * Gets the vertical ball-speed of a normal goal-shoot in m/s.
   */
  public static float getShootSpeedV()
  {
    return shootSpeedV;
  }


  /**
   * Gets the vertical ball-speed of a "lifted" goal-shoot in m/s.
   */
  public static float getShootSpeedLiftV()
  {
    return shootSpeedLiftV;
  }


  /**
   * Gets the horizontal ball-speed-reduction of a "lifted" goal shoot
   * (e.g. 0.1 = 10% of normal speed).
   */
  public static float getShootSpeedLiftHRed()
  {
    return shootSpeedLiftHRed;
  }



  /**
   * Gets the minimal horizontal ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedHMin()
  {
    return goalkickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedVMin()
  {
    return goalkickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedHMax()
  {
    return goalkickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedVMax()
  {
    return goalkickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedHMin()
  {
    return cornerkickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedVMin()
  {
    return cornerkickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedHMax()
  {
    return cornerkickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedVMax()
  {
    return cornerkickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedHMin()
  {
    return throwinSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedVMin()
  {
    return throwinSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedHMax()
  {
    return throwinSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedVMax()
  {
    return throwinSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedHMin()
  {
    return penaltykickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedVMin()
  {
    return penaltykickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedHMax()
  {
    return penaltykickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedVMax()
  {
    return penaltykickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedHMin()
  {
    return freekickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedVMin()
  {
    return freekickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedHMax()
  {
    return freekickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedVMax()
  {
    return freekickSpeedVMax;
  }


  /**
   * Gets the minimal speed in m/s where a player
   * can not get the ball, but bounces it.
   */
  public static float getBounceBallSpeed()
  {
    return bounceBallSpeed;
  }
  
  
  /**
   * Gets the minimal speed in m/s where a goalkeeper
   * can not catch the ball, but bounces it.
   */
  public static float getBounceGoalkeeperBallSpeed()
  {
    return bounceGoalkeeperBallSpeed;
  }


  /**
   * This number is multiplied with the ball velocity when bouncing.
   */
  public static float getBounceVelFactor()
  {
    return bounceVelFactor;
  }


  /**
   * After bounce
   * [randomCenter(velrandom),randomCenter(velrandom),randomCenter(velrandom)]
   * is added to the ball velocity.
   */
  public static float getBounceVelRandom()
  {
    return bounceVelRandom;
  }

}
