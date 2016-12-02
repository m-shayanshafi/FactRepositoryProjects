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
package com.xenoage.bp2k6.match;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * This class stores information
 * about a team.
 *
 * @author Andi
 */
public class TeamInfo
{
  
  //general information
  private String id;
  private String name;
  private String description;
  private String stadium;
  
  //team colors and pattern (for texture)
  private PlayerColors playerColorsHome;
  private PlayerColors playerColorsAway;
  private PlayerColors playerColorsGoalkeeper;
  
  //players
  private final int playersCount = 5;
  private PlayerInfo[] playerInfo = new PlayerInfo[playersCount];


  /**
   * First constructor possibility:
   * Load team information from XML file.
   */
  public TeamInfo(String id)
  {
    //load team from XML file
    try
    {
      SAXReader reader = new SAXReader();
      reader.setEncoding(XMLReader.encoding);
      Document doc = reader.read("data/teams/" + id + ".xml");
      Element root = doc.getRootElement();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      if (!id.equals(this.id))
      {
        if (!this.id.equals("newteam"))
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Filename and ID of team \"" +
            id + "\" is not identical! Filename: \"" +
            id + ".xml\", ID: \"" + this.id + "\"");
        }
        this.id = id; //use filename for id
      }
      //name
      //if there is a special name for the current
      //language, use it.
      String localName = Language.getWithNull("team_" + this.id);
      if (localName != null)
      {
        this.name = localName;
      }
      else
      {
        this.name = XMLReader.readAttributeValue(root, "name");
      }
      //description
      this.description = XMLReader.readAttributeValue(root, "description");
      //home stadium
      this.stadium = XMLReader.readAttributeValue(root, "stadium");
        
      //team colors
      List listColors = root.elements("colors");
      for (int iColors = 0; iColors < listColors.size(); iColors++)
      {
        Element eColor = (Element) listColors.get(iColors);
        PlayerColors playerColors = new PlayerColors();
        Color col;
        try
        {
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColor, "skin"));
          playerColors.setColor(PlayerColors.COLOR_SKIN, col);
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColor, "shirt"));
          playerColors.setColor(PlayerColors.COLOR_SHIRT, col);
          String shirt2 = //optional
            XMLReader.readAttributeValue(eColor, "shirt2");
          if (shirt2.length() > 0)
          {
            col = ColorTools.getColor(shirt2);
            playerColors.setColor(PlayerColors.COLOR_SHIRT2, col);
          }
          else
          {
            playerColors.setColor(PlayerColors.COLOR_SHIRT2,
              playerColors.getColor(PlayerColors.COLOR_SHIRT));
          }
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColor, "pants"));
          playerColors.setColor(PlayerColors.COLOR_PANTS, col);
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColor, "shoes"));
          playerColors.setColor(PlayerColors.COLOR_SHOES, col);
          
          playerColors.setPattern(XMLReader.readAttributeValue(eColor, "pattern"));

          String colorID = XMLReader.readAttributeValue(eColor, "id");
          if (colorID.length() == 0 || colorID.equals("home"))
            playerColorsHome = playerColors;
          else if (colorID.equals("away"))
            playerColorsAway = playerColors;
          else if (colorID.equals("goalkeeper"))
            playerColorsGoalkeeper = playerColors;
          else
            throw new Exception("Unknown color id: \"" + colorID + "\"!");
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Player colors could not be read from \"" + id + ".xml\":");
          Logging.log(Logging.LEVEL_WARNINGS, this, ex);
        }
        if (playerColorsHome == null)
          playerColorsHome = new PlayerColors();
        if (playerColorsAway == null)
          playerColorsAway = new PlayerColors();
        if (playerColorsGoalkeeper == null)
          playerColorsGoalkeeper = new PlayerColors();
      }
      
      //players
      Element ePlayers = root.element("players");
      if (ePlayers != null)
      {
        List listPlayers = ePlayers.elements("player");
        for (int i = 0; i < listPlayers.size(); i++)
        {
          if (i > playersCount - 1)
          {
            Logging.log(Logging.LEVEL_WARNINGS, this,
              "Too many players defined in team \"" + id + "\"");
            break;
          }
          Element ePlayer = (Element) listPlayers.get(i);
          playerInfo[i] = new PlayerInfo(
            XMLReader.readAttributeValue(ePlayer, "name"),
            XMLReader.readAttributeValueInt(ePlayer, "attack"),
            XMLReader.readAttributeValueInt(ePlayer, "defense"),
            XMLReader.readAttributeValueInt(ePlayer, "speed"),
            XMLReader.readAttributeValueInt(ePlayer, "stamina"),
            XMLReader.readAttributeValue(ePlayer, "headdress"),
            XMLReader.readAttributeValue(ePlayer, "glasses"));
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "TeamInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }


  /**
   * Second constructor possibility:
   * Creates a new team.
   */
  public TeamInfo(String id, String name)
  {
    this.id = id;
    this.name = name;
  }


  /**
   * Gets the id of the team.
   */
  public String getID()
  {
    return id;
  }


  /**
   * Gets the name of the team.
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Sets the name of the team.
   */
  public void setName(String name)
  {
    this.name = name;
  }


  /**
   * Gets the description of the team.
   */
  public String getDescription()
  {
    return description;
  }
  
  
  /**
   * Sets the description of the team.
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  
  /**
   * Gets the away-colors of the team.
   */
  public PlayerColors getPlayerColorsAway()
  {
    return playerColorsAway;
  }

  
  /**
   * Sets the away-colors of the team.
   */
  public void setPlayerColorsAway(PlayerColors playerColorsAway)
  {
    this.playerColorsAway = playerColorsAway;
  }


  /**
   * Gets the goalkeeper colors of the team.
   */
  public PlayerColors getPlayerColorsGoalkeeper()
  {
    return playerColorsGoalkeeper;
  }


  /**
   * Sets the goalkeeper colors of the team.
   */
  public void setPlayerColorsGoalkeeper(PlayerColors playerColorsGoalkeeper)
  {
    this.playerColorsGoalkeeper = playerColorsGoalkeeper;
  }


  /**
   * Gets the home-colors of the team.
   */
  public PlayerColors getPlayerColorsHome()
  {
    return playerColorsHome;
  }


  /**
   * Sets the home-colors of the team.
   */
  public void setPlayerColorsHome(PlayerColors playerColorsHome)
  {
    this.playerColorsHome = playerColorsHome;
  }


  /**
   * Gets a list with information about each player.
   */
  public PlayerInfo[] getPlayerInfo()
  {
    return playerInfo;
  }


  /**
   * Changes the information about a single player.
   */
  public void setPlayerInfo(int index, PlayerInfo playerInfo)
  {
    this.playerInfo[index] = playerInfo;
  }


  /**
   * Gets the id of the home stadium of the team
   * or <code>null</code> if the team has none.
   */
  public String getStadium()
  {
    return stadium;
  }


  /**
   * Sets the id of the home stadium of the team,
   * or <code>null</code> if the team has none.
   */
  public void setStadium(String stadium)
  {
    this.stadium = stadium;
  }

  
  /**
   * Gets the path of the team logo.
   * If the logo is found, this is "data/teams/" + id + "-logo.png",
   * otherwise it is "data/images/teamunknown-logo.png"
   */
  public String getTeamLogoPath()
  {
    String path = "data/teams/" + id + "-logo.png";
    if (new java.io.File(path).exists())
      return path;
    else
      return "data/images/teamunknown-logo.png";
  }
  
  
  /**
   * Gets the path of the team ads.
   * If the logo is found, this is "data/teams/" + id + "-ads.png",
   * otherwise it is "data/images/teamunknown-ads.png"
   */
  public String getTeamAdsPath()
  {
    String path = "data/teams/" + id + "-ads.png";
    if (new java.io.File(path).exists())
      return path;
    else
      return "data/images/teamunknown-ads.png";
  }
  
  
  /**
   * Compares this TeamInfo object with another one and
   * returns <code>true</code> if their contents are
   * completely equal, otherwise <code>false</code>.
   */
  public boolean equals(TeamInfo other)
  {
    if (!(id.equals(other.getID()) &&
      name.equals(other.getName()) &&
      description.equals(other.getDescription()) &&
      stadium.equals(other.getStadium()) &&
      playerColorsHome.equals(other.getPlayerColorsHome()) &&
      playerColorsAway.equals(other.getPlayerColorsAway()) &&
      playerColorsGoalkeeper.equals(other.getPlayerColorsGoalkeeper())))
      return false;
    else
      for (int i = 0; i < playersCount; i++)
        if (!playerInfo[i].equals(other.getPlayerInfo()[i]))
          return false;
      return true;
  }
  
  
  /**
   * Saves this TeamInfo in the file
   * <code>"data/teams/" + id + ".xml"</code>.
   */
  public void saveToXMLFile()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving team info \"" + id + "\"...");
    
    try
    {
    
      Document document = DocumentHelper.createDocument();
      //root element
      Element root = document.addElement("team");
      root.addAttribute("id", id);
      root.addAttribute("name", name);
      root.addAttribute("description", description);
      root.addAttribute("stadium", stadium);
      //team colors
      playerColorsHome.saveToXML(root, "home");
      playerColorsAway.saveToXML(root, "away");
      playerColorsGoalkeeper.saveToXML(root, "goalkeeper");
      //list of players
      Element ePlayers = root.addElement("players");
      for (int i = 0; i < playersCount; i++)
      {
        playerInfo[i].saveToXML(ePlayers);
      }

      File out = new File("data/teams/" + id + ".xml");
      if (out.exists()) out.delete();
      out.createNewFile();
  
      org.dom4j.io.OutputFormat outf =
        new org.dom4j.io.OutputFormat("  ", true);
      outf.setEncoding(XMLReader.encoding);
      XMLWriter writer = new XMLWriter(new FileWriter(out), outf);
      writer.write(document);
      writer.close();
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Team info \"" + id + "\" saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while saving team \"" + id + "\":");
      Logging.log(this, ex);
    }

  }
  
  
  /**
   * Gets the average attack points of the players
   * of this team.
   */
  public float getAverageAttack()
  {
    float ret = 0;
    for (int i = 0; i < playersCount; i++)
      ret += playerInfo[i].getAttackStart();
    return ret / playersCount;
  }
  
  
  /**
   * Gets the average defense points of the players
   * of this team.
   */
  public float getAverageDefense()
  {
    float ret = 0;
    for (int i = 0; i < playersCount; i++)
      ret += playerInfo[i].getDefenseStart();
    return ret / playersCount;
  }
  
  
  /**
   * Gets the average speed points of the players
   * of this team.
   */
  public float getAverageSpeed()
  {
    float ret = 0;
    for (int i = 0; i < playersCount; i++)
      ret += playerInfo[i].getSpeedStart();
    return ret / playersCount;
  }
  
  
  /**
   * Gets the average stamina points of the players
   * of this team.
   */
  public float getAverageStamina()
  {
    float ret = 0;
    for (int i = 0; i < playersCount; i++)
      ret += playerInfo[i].getStamina();
    return ret / playersCount;
  }
  
  
  /**
   * Gets the filename of the adboards texture
   * used for this team.
   */
  public String getAdboardsFilename()
  {
    String teamAds = "data/teams/" + id + "-ads.png";
    if (new File(teamAds).exists())
      return teamAds;
    else
      return "data/images/teamunknown-ads.png";
  }
  
  
  /**
   * Gets the average value of all team values.
   */
  public float getAverageAll()
  {
    return (getAverageAttack() + getAverageDefense() +
      getAverageSpeed() + getAverageStamina()) / 4;
  }


}
