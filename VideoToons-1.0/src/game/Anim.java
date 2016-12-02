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

import java.io.Serializable;

public class Anim implements Serializable
{
  private int image_index[];

  private short nb_index;

  private short current_index;


 /*********************** ANIM CONSTRUCTOR *****************************/

    public Anim( int im_index[] )
    {
       image_index = im_index;
       current_index = -1;
       nb_index = (short) im_index.length;
    }

 /************************ GET CURRENT INDEX ***************************/

    public short nextImageIndex() {
      current_index = (short) ((current_index+1)%nb_index);
      return (short) image_index[current_index];
    }
    
 /************************** RESET ANIM ********************************/

    public void reset() {
      current_index = (short) -1;
    }
    
 /**************************** GET INDEX *******************************/

    public short getIndex( int ind ) {
      return (short) image_index[ind];
    }
    
    public short getAnimIndex() {
      return current_index;
    }

 /**********************************************************************/

    public boolean isAnimEnd() {
       if(current_index==nb_index-1)
          return true;
       return false;
    } 
}