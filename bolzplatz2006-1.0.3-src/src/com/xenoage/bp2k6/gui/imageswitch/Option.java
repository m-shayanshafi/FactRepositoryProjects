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
package com.xenoage.bp2k6.gui.imageswitch;

import com.xenoage.bp2k6.util.Rect2i;


/**
 * This class contains a option of a
 * imageswitch-control. The needed
 * information is the position of
 * the source image and the value
 * of the option.
 *
 * @author Andi
 */
public class Option
{
  private String value;
  private Rect2i srcPos;

  public Option(String value, Rect2i srcPos)
  {
    this.value = value;
    this.srcPos = srcPos;
  }

  public String getValue()
  {
    return value;
  }

  public Rect2i getSrcPos()
  {
    return srcPos;
  }

}
