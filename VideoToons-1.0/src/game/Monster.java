/*
 * VideoToons. A Tribute to old Video Games.
 * Copyright (C) 2001 - Bertrand Le Nistour
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package videotoons.game;

import java.awt.*;
import java.awt.event.*;


abstract class Monster extends AnimatedSprite
{
	
 /************************** USEFUL LINKS **********************************/

  // The Dimension Library we are linked to ...
     private DimensionLibrary dimlib;

  // our index in the dimension library
     private short dimlib_index;

  // The server level process...
     protected ServerLevelProcess level;


 /**************************** MONSTER CONSTRUCTOR *****************************/

    public Monster( AnimLibrary anm, ServerLevelProcess level, byte stone, byte stage, byte speed)
    {
      super(anm,stone,stage);

      // Our Identity
         spr_TYPE = SP_MONSTER;
         setStatus( BS_DANGEROUS );

      // Useful Links
         this.level = level;
         dimlib = level.getDimensionLibrary();

      // speed default ?
         if(speed>=0 && speed<10)
            anm.sprite_basicspeed = speed;
    }

 /**************************************************************************/
 /************************** MONSTER METHODS *******************************/
 /**************************************************************************/

   // called by the ServerLevelProcess to init the dimension index for this
   // monster
      public void setDimensionLibraryIndex( short index ) {
          dimlib_index = index;
      }

 /**************************************************************************/

   // is the stone on my right broken ?
      public boolean isStoneOnMyRightBroken()
      {
      	  byte stone = (byte) ( (r.x+r.width)/STONE_WIDTH );
      	  
      	  if(stone<0) stone=31;
      	  if(stone>31) stone=0;

          byte stone_state = level.getStoneState( getStage(), stone, 0);
          
          if(stone_state==STONE_BROKEN || stone_state==STONE_ACID)
             return true;
          else
             return false;
      }

 /**************************************************************************/

   // is the stone on my left broken ?
      public boolean isStoneOnMyLeftBroken()
      {
      	  byte stone = (byte) ( r.x/STONE_WIDTH );
      	  
      	  if(stone<0) stone=31;
      	  if(stone>31) stone=0;

          byte stone_state = level.getStoneState( getStage(), stone, 0);
          
          if(stone_state==STONE_BROKEN || stone_state==STONE_ACID)
             return true;
          else
             return false;
      }

 /**************************************************************************/
 /********************* REDEFINITION OF ABSTRACT METHODS *******************/
 /**************************************************************************/
 
  /*************************** UPDATE CURRENT IMAGE ************************/

  // Changes the current image to be displayed

    public void updateCurrentImage()
    {
    	current_im = current_anim.nextImageIndex();

    	resize( dimlib.getWidth(dimlib_index,current_im),
                dimlib.getHeight(dimlib_index,current_im) );
    }

 /********************* TO LIMIT THE SPRITE'S X RANGE ***********************/

  // On the right side ...
    protected void limitXRangeRight()
    {
      if( r.x>=640 )
         r.x += - 640 - dimlib.getWidth(dimlib_index,current_im);
    }

  // On the left side ...
    protected void limitXRangeLeft()
    {
      int w = dimlib.getWidth(dimlib_index,current_im);

      if( r.x <= -w )
            r.x += 639+w;
    }

  /**************************************************************************/

   // behaviour when a collision with a block occur...

    abstract public void collisionWithBlock( Block bl );

  /**************************************************************************/
 }