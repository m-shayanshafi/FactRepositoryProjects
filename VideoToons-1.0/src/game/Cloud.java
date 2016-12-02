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
import java.util.Random;


class Cloud extends BasicSprite
{

  /************************* CLOUD PROPERTIES ***********************/

   // Cloud speed ( ... -2, -1, 1, 2, ... )
      private byte cloud_speed;

   // Cloud stage
      private byte stage;

  /***************************** CONSTRUCTOR ************************/

     public Cloud( byte stage, byte cloud_speed )
     {
     	super();

     	this.stage = stage;
     	this.cloud_speed = cloud_speed;

     // Some info on us
        spr_TYPE = SP_CLOUD;
        spr_ID = stage;

      /* we int the position. The cloud's dimensions will be set later.*/
//     	 r.x = 140 + new Random().nextInt(240);
         r.x = 140;
     	 r.y = STAGE_HEIGHT[stage];
     }


     public void initDimension( int w, int h ){
       r.width = w;
       r.height = h;
       updateOldRectangle();
     }


   /************************* POSITION UPDATE **********************/

     public void updateCloudPosition()
     {
        r.x += cloud_speed;

         if( cloud_speed<0 && r.x<=-r.width-5)
              r.x=639;
         else 
             if(cloud_speed>0 && r.x>=645)
                  r.x=-r.width+1;
     }


   /************************ DATA ACCESS *************************/

     public byte getSpeed(){
        return cloud_speed;
     }

     public byte getStage(){
     	return stage;
     }

   /****************************** END ***************************/
}