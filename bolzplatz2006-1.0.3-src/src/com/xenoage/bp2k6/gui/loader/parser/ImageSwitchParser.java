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

import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.gui.imageswitch.Option;
import com.xenoage.bp2k6.util.*;
import java.util.List;
import net.sf.jirr.ITexture;
import org.dom4j.Element;



/**
 * @author   Andreas Wenger
 */
public class ImageSwitchParser
{

  public static ImageSwitch parseImageSwitch(Element e,
    Screen parentScreen, Group parentGroup)
  {
    ImageSwitch c = new ImageSwitch();
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //transparency
    String transparency = e.attributeValue("transparency");
    if (transparency != null)
    {
      //add transparency for imageswitch: not needed at the moment
    }
    //source image
    String src = e.attributeValue("src");
    ITexture tex = null;
    if (src != null)
    {
      tex = MediaLoader.loadTexture(src);
      c.setImage(tex);
    }
    //options
    Element optionsContainer = e.element("options");
    if (optionsContainer != null)
    {
      List listOptions = optionsContainer.elements("option");
      for (int i = 0; i < listOptions.size(); i++)
      {
        Element el = (Element) listOptions.get(i);
        String value = XMLReader.readAttributeValue(el, "value");
        int srcx = XMLReader.readAttributeValueInt(el, "srcx");
        int srcy = XMLReader.readAttributeValueInt(el, "srcy");
        int srcwidth = XMLReader.readAttributeValueInt(el, "srcwidth");
        int srcheight = XMLReader.readAttributeValueInt(el, "srcheight");
        Option option = new Option(value,
          new Rect2i(srcx, srcy, srcx + srcwidth, srcy + srcheight));
        c.addOption(option);
      }
      //default value?
      String defaultOption = optionsContainer.attributeValue("default");
      if (defaultOption != null)
      {
        c.setSelectedIndex(Integer.parseInt(defaultOption));
      }
    }

    //actions
    List listActions = e.elements("action");
    for (int i = 0; i < listActions.size(); i++)
    {
      Element el = (Element) listActions.get(i);
      Action a = ActionParser.parseAction(el);
      if (el.attributeValue("event").equals("switch"))
        c.addActionSwitch(a);
    }

    return c;
  }


}
