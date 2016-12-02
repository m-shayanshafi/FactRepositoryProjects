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
package com.xenoage.bp2k6.gui.loader;

import net.sf.jirr.SColor;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.gui.loader.parser.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author   Sonix
 * @author   Andi
 */
public class GUILoader
{


  //id: name of screen, without ".xml"
  public static Screen loadScreen(String id)
  {
    String lang = Language.getCurrentLanguageID();
    Logging.log(Logging.LEVEL_MESSAGES, new GUILoader(),
      "Loading screen \"" + lang + "/"+ id + "\"");
    Screen screen = new Screen(GameEngine.getUIManager());
    try
    {
      //open xml file with screen definition
      SAXReader reader = new SAXReader();
      reader.setEncoding(XMLReader.encoding);
      Document doc = reader.read("data/gui/" + lang + "/" + id + ".xml");
      Element root = doc.getRootElement();
      //id
      screen.setID(XMLReader.readAttributeValue(root, "id"));
      //program
      screen.setProgram(XMLReader.readTextTrim(root, "program"));
      //background image
      String backgroundImage = XMLReader.readTextTrim(root, "background");
      if (backgroundImage.length() > 0)
        screen.setBackgroundImage(
          TextureFactory.loadTexture("data/" + backgroundImage, false));
      //gui scene
      Element eScene = root.element("scene");
      if (eScene != null)
      {
        screen.setGUIScene(XMLReader.readAttributeValue(eScene, "systemid"));
      }
      //fading
      Element fading = root.element("fading");
      if (fading != null)
      {
        screen.setFadeInColor(new SColor(XMLReader.readAttributeValueIntHex(fading, "incolor")));
        screen.setFadeOutColor(new SColor(XMLReader.readAttributeValueIntHex(fading, "outcolor")));
        screen.setFadeInTime(XMLReader.readAttributeValueFloat(fading, "intime"));
        screen.setFadeOutTime(XMLReader.readAttributeValueFloat(fading, "outtime"));
        if (XMLReader.readAttributeValue(fading, "usefadeout") == "false")
          screen.setUseFadeOut(false);
      }
      //music
      Element music = root.element("music");
      if (music != null)
      {
        screen.setMusicFadeInTime(XMLReader.readAttributeValueFloat(music, "fadein"));
        screen.setMusicFadeOutTime(XMLReader.readAttributeValueFloat(music, "fadeout"));
        screen.setMusicContinue(XMLReader.readAttributeValueBoolean(music, "continue"));
        screen.setMusic(Variables.replaceVariables(music.getTextTrim()));
      }
      //following screen
      Element follow = root.element("follow");
      if (follow != null)
      {
        screen.setNextScreenClose(XMLReader.readAttributeValue(follow, "nextid"));
        screen.setAutomaticCloseTime(XMLReader.readAttributeValueFloat(follow, "time"));
      }
      //groups
      Element eGroups = root.element("groups");
      //start group
      screen.setStartGroupID(
        XMLReader.readAttributeValue(eGroups, "start"));
      //list of groups
      if (eGroups != null)
      {
        List listGroups = eGroups.elements("group");
        for (int i = 0; i < listGroups.size(); i++)
        {
          Element el = (Element) listGroups.get(i);
          Group group = GroupParser.parseGroup(el, screen);
          screen.addGroup(group);
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new GUILoader(),
        "Screen \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(new GUILoader(), ex);
      return null;
    }
    return screen;
  }

  
  /**
   * Parse a list of controls and add it to
   * the given target container (Group or Panel)
   */
  public static void parseControls(Element controls, Container target,
    Screen parentScreen, Group parentGroup)
  {
    if (controls != null)
    {
      List listControls = controls.elements();
      for (int i = 0; i < listControls.size(); i++)
      {
        Control c = null;
        Element elemControl = (Element) listControls.get(i);
        if (elemControl.getName().equals("button"))
        {
          c = ButtonParser.parseButton(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("energybar"))
        {
          c = EnergyBarParser.parseEnergyBar(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("label"))
        {
          c = LabelParser.parseLabel(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("image"))
        {
          c = ImageParser.parseImage(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("optionbutton"))
        {
          c = OptionButtonParser.parseOptionButton(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("imageswitch"))
        {
          c = ImageSwitchParser.parseImageSwitch(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("panel"))
        {
          c = PanelParser.parsePanel(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getName().equals("keychangebutton"))
        {
          c = KeyChangeButtonParser.parseKeyChangeButton(
            elemControl, parentScreen, parentGroup);
        }
        if (c != null)
          target.addControl(c, parentGroup, parentScreen);
        //ignore actions and actionsets here
        else if (!elemControl.getName().equals("action")
          && !elemControl.getName().equals("actionset")) 
          Logging.log(Logging.LEVEL_WARNINGS, new GUILoader(), "Unknown control type \"" +
            elemControl.getName() + "\" in screen \"" + parentScreen.getID() + "\"!");
      }
    }
  }


}
