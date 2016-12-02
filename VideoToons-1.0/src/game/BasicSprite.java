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
import java.io.*;


class BasicSprite implements GameDefinitions
{

  /************************ SPRITE IDENTITY ******************************/

   // Sprite Type. See GameDefinitions for a complete list of types.
      protected byte spr_TYPE;

   // Sprite ID
      protected byte spr_ID;

   // is the sprite visible on the GameScreen ?
      protected boolean spr_visible;

  /************************* SPRITE STATUS *******************************/
  
  // sprite status (see gamedefinitions - basicsprite states )
     protected byte spr_status;

  /*********************** SPRITE DEFINITION *****************************/

   // Sprite position & size
      protected Rectangle r;

   // Sprite's old rectangle (previous cycle)
      protected Rectangle old_r;

   // The current displayed image index. After all a sprite is an image...
      protected short current_im;


  /*************************** CONSTRUCTOR *******************************/

      public BasicSprite(){
        r = new Rectangle();
        old_r = new Rectangle();
        spr_visible = true;
        spr_status = BS_ACTIVE;
      }

      public BasicSprite(int x, int y, int w, int h){
        r = new Rectangle(x,y,w,h);
        old_r = new Rectangle();

        updateOldRectangle();
        spr_visible = true;
        spr_status = BS_ACTIVE;
      }

  /**************************** POSITION *********************************/

     public short getX(){
        return (short) r.x;
     }
     
     public short getY(){
        return (short) r.y;
     }

     public void setY( int y ){
        r.y = y;
     }
     
     public void setX( int x ){
        r.x = x;
     }

     public short getWidth(){
        return (short) r.width;
     }
     
     public short getHeight(){
        return (short) r.height;
     }

     public void setRectangle(int x, int y, int w, int h){
        r = new Rectangle(x,y,w,h);
     }

     public Rectangle getRectangle(){
        return new Rectangle(r);	
     }
     
     public void updateOldRectangle(){
        old_r.x = r.x;
        old_r.y = r.y;
        old_r.width = r.width;
        old_r.height = r.height;
     }
     
     public void setOldRectangle( Rectangle rnew ){
        old_r = new Rectangle(rnew);
     }

     public Rectangle getOldRectangle(){
        return new Rectangle(old_r);
     }

  /****************************** MOVEMENTS ********************************/

     public void move(int x, int y){
        r.setLocation(x,y);
     }
     
     public void translateX(int dx){
        r.x = r.x + dx;
     }

     public void translateY(int dy){
        r.y = r.y + dy;
     }

     public void resize( int v, int h ){
     	r.setSize( v, h );
     }

 /****************************** IMAGE INDEX ********************************/

     public int getCurrentImageIndex(){
        return current_im;
     }

 /************************** SPRITE IDENTITY ACCESS *************************/

     public boolean isVisible() {
        return spr_visible;
     }

     public void setVisibility( boolean val ) {
        spr_visible = val;
     }

     public byte getSpriteID() {
        return spr_ID;
     }

     public void setSpriteID( byte val ) {
        spr_ID = val;
     }
     
     public byte getSpriteType() {
        return spr_TYPE;
     }

     public byte getStatus() {
        return spr_status;
     }

     public void setStatus( byte val ){
        spr_status = val;
     }

  /************************* NETWORK INFORMATION ***************************/

      public void sendData( byte header, DataOutputStream ds_snd ) throws IOException
      {
          ds_snd.writeByte(header);
          ds_snd.writeByte(spr_ID);

          ds_snd.writeInt(r.x);
          ds_snd.writeInt(r.y);
          ds_snd.writeInt(r.width);
          ds_snd.writeInt(r.height);
          ds_snd.writeShort(current_im);
          ds_snd.writeByte(spr_status);
          ds_snd.writeBoolean(spr_visible);
      }

      public void receiveData( DataInputStream ds_rcv ) throws IOException
      {
         r.x = ds_rcv.readInt();
         r.y = ds_rcv.readInt();
         r.width = ds_rcv.readInt();
         r.height = ds_rcv.readInt();
         current_im = ds_rcv.readShort();
         spr_status = ds_rcv.readByte();
         spr_visible = ds_rcv.readBoolean();
      }

      static public void flushReceivedData( DataInputStream ds_rcv ) throws IOException
      {
         ds_rcv.readInt();
         ds_rcv.readInt();
         ds_rcv.readInt();
         ds_rcv.readInt();
         ds_rcv.readShort();
         ds_rcv.readByte();
         ds_rcv.readBoolean();
      }


  /******************************* UTILITY *********************************/  

      public byte getStage(){
         return (byte)( 11-(r.y/115) );
      }

      public byte getSide(){
      	if(r.x<320)
            return LEFT;
        return RIGHT;	
      }
}
