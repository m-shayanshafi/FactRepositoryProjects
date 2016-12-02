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
import java.awt.image.BufferedImage;


class Tunnel extends BasicSprite
{
  /****************************** TUNNEL DATA ******************************/

  // Does this tunnel has to implement collisions ? ( with players )
     boolean implements_collisions;


  /****************************** CONSTRUCTOR ******************************/

   public Tunnel( int x0, int y0, boolean does_col )
   {
   	super();

     // Collisions with players ?
   	implements_collisions = does_col;
       
     // We init the tunnel position. Its dimensions are set later.
        r.x = x0;
        r.y = y0;

     // Some info on us
        spr_TYPE = SP_TUNNEL;
        spr_ID = (byte)( getStage()*2 + getSide() );

   }

   public void initDimension( int w, int h ){
       r.width = w;
       r.height = h;
       updateOldRectangle();
   }


  /******************************** COLLISIONS *********************************/

  // Collision behaviour with a player
     public void collisionBehaviour( BasicSprite bs )
     {
          if(implements_collisions==false)
             return;

          Rectangle r_bs =  bs.getRectangle();

          if( r.intersects(r_bs) == false )
                return;

          if(getSide()==LEFT)
          {        	
            if( r.contains( r_bs.x, r_bs.y+r_bs.height ) == true )
               bs.setX(r.x+r.width);
          }
          else
            if( r.contains( r_bs.x+r_bs.width, r_bs.y+r_bs.height ) == true )
               bs.setX(r.x-r_bs.width);
     }

  // Collision behaviour with Monsters/Clouds.
  // They are suppose to go in the tunnel. There are no real collision. 
  // We return a Rectangle of the tunnel zone to redraw.
     public Rectangle getTunnelRedrawZone( BasicSprite spr )
     {
        Rectangle r2 = spr.getRectangle();

       // Intersection ?
          if( r.intersects(r2) == false ) return null;
 
          Rectangle r_inter = r.intersection( r2 );

       // this rectangle should fit the screen
          Tools.fitOnScreen( r_inter );
          if( r_inter.width == 0)  return null;

       // We return the zone to redraw
          return r_inter;
     }

}