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
package com.xenoage.bp2k6.gui.loader.parser;

import java.util.List;

import org.dom4j.Element;

import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.gui.loader.GUILoader;
import com.xenoage.bp2k6.util.*;


/**
 * @author   Andreas Wenger
 */
public class GroupParser
{
  
  public static Group parseGroup(Element e, Screen parentScreen)
  {
    String id = XMLReader.readAttributeValue(e, "id");
    Group ret = new Group(id, parentScreen);
    //flyin duration
    ret.setDurationFlyIn(XMLReader.readAttributeValueFloat(e, "flyinduration"));
    //flyout duration
    ret.setDurationFlyOut(XMLReader.readAttributeValueFloat(e, "flyoutduration"));
    //next group wait duration
    ret.setDurationNextGroupWait(
      XMLReader.readAttributeValueFloat(e, "nextgroupwaitduration"));
    //parse controls
    GUILoader.parseControls(e, ret, parentScreen, ret);
    //actions
    List listActions = e.elements("action");
    for (int i = 0; i < listActions.size(); i++)
    {
      Element el = (Element) listActions.get(i);
      Action a = ActionParser.parseAction(el);
      if (el.attributeValue("event").equals("groupopen"))
        ret.addActionGroupOpen(a);
      else if (el.attributeValue("event").equals("grouptime"))
        ret.addActionGroupTime(a);
      else if (el.attributeValue("event").equals("keypress"))
        ret.addActionKeypress(a);
      
    }
    //action sets
    List listActionSets = e.elements("actionset");
    for (int i = 0; i < listActionSets.size(); i++)
    {
      Element el = (Element) listActionSets.get(i);
      ActionSet a = ActionSetParser.parseActionSet(el);
      ret.addActionSet(a);
    }
    return ret;
  }
   
}
