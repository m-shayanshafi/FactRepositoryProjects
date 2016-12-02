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

import java.io.*;


class FilledHole
{

   private byte stage;
   private byte stone;

  /**************************** CONSTRUCTOR *****************************/

    public FilledHole( byte stage, byte stone)
    {
        this.stage = stage;
        this.stone = stone;
    }

  /************************* NETWORK INFORMATION ***************************/

      public void sendData( DataOutputStream ds_snd ) throws IOException
      {
          ds_snd.writeByte( GameDefinitions.MSG_FILLEDHOLE );
          ds_snd.writeByte( stage );
          ds_snd.writeByte( stone );
      }


 /**************************** POSITION ACCESS ****************************/

      public byte getStage() {
          return stage;
      }

      public byte getStone() {
          return stone;
      }

 /*************************************************************************/
}
