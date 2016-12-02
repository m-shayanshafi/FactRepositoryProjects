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


import java.awt.Rectangle;

class Block extends BasicSprite
{

  /************************* CLOUD PROPERTIES ************************/

   // Block stage
      private int stage;

   // Block stone
      private int stone;

  /***************************** CONSTRUCTOR *************************/

     public Block( int stage, int stone )
     {
     	super();

     	this.stage = stage;
     	this.stone = stone;

     // Some info on us
        spr_TYPE = SP_BLOCK;
        spr_ID = (byte) stage;

      /* we initialize the position. The cloud's dimensions will be set later.*/
     	 r.x = stone*STONE_WIDTH;
     	 r.y = STAGE_HEIGHT[stage]-100;
     	 
     	 r.width = 20;
     	 r.height = 100;
     }

  /******************************** COLLISIONS *********************************/

  // Collision behaviour with a player
     public void collisionBehaviour( AnimatedSprite bs )
     {
          Rectangle r_bs =  bs.getRectangle();

          if( r.intersects(r_bs) == false )
                return;

          Rectangle r_old = bs.getOldRectangle();

        // First thing to do... was the previous pos better ?
          bs.setX(r_old.x);

       // if it's the case we quit
          if( r.intersects(r_bs) == false )
                return;

       // Otherwise we must be falling inside the block...

          if(r_old.x <= r.x)
              bs.setX(r.x-r_bs.width-1);
          else
              bs.setX(r.x+r.width+1);
     }



   /************************ DATA ACCESS *************************/

     public int getStone(){
        return stone;
     }

     public byte getStage(){
     	return (byte) stage;
     }

   /****************************** END ***************************/
}