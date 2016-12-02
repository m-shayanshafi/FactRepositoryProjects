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
import java.awt.geom.*;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.io.*;


class BrokenStone extends BasicSprite
{
	
  // Number of cycles during when we display the broken stones
     static final private byte BROKEN_STONE_CYCLES = 8;
     
  // When a stone is broken we use the following array to move the stone image.
  // [][] = { dx, dy }
     static final private int STONE_OFFSET[][] = {  {  3,  5, 5, 10, 10,  8,  6, 4,  2,  0  },
                                                    { -5, -2, 5, 10, 20, 30, 30, 30, 30, 40 } };

  // Corrections to game sprite refresh. [side][]
  // Order: xcor_left, ycor_bottom, xcor_right, ycor_top
     static final private int ROTATE_OFFSET[][] = { {    0, -20, 5,  0 },
     	                                            {  -15,   0, 0, 10 }  };


  /************************* SPRITE IMAGE ***************************/

  // We refer to this stone image to calculate the rotated image
     private BufferedImage im;
   
  // Broken stone step 0 -> BROKEN_STONE_CYCLES
     private int b_stone_step;
     
  // Original stone index : stage, stone number, line
     private byte origin_stone[] = new byte[3];
     
  // Falling on the rightside (side==1) or on the leftside (side==0)?
     private byte side;
     private byte signed_side;
     
  // Not initialized ? should end ?
     private boolean not_initialized;
     private boolean should_end;


  /**************************** CONSTRUCTOR *****************************/

    BrokenStone( byte levl, byte stone, byte line, boolean rightside )
    {
       // init sprite first position and rectangle
          b_stone_step = 0;

          origin_stone[0] = levl;
          origin_stone[1] = stone;
          origin_stone[2] = line;

             if(  rightside == true ) {
                side = RIGHT;
                signed_side = 1;
             }
             else {
                side = LEFT;
                signed_side = -1;
             }
          
          not_initialized = true;
          should_end = false;

          setRectangle( stone*STONE_WIDTH,
                        STAGE_HEIGHT[levl]+line*STONE_HEIGHT,
                        STONE_WIDTH, STONE_HEIGHT );

          updateOldRectangle();
     }


     public void setImage( BufferedImage im_stone )
     {
       not_initialized = false;
       im = im_stone;
     }


  /************************* NETWORK INFORMATION ***************************/

      public void sendData( DataOutputStream ds_snd ) throws IOException
      {
          ds_snd.writeByte( MSG_BROKENSTONE );
          ds_snd.writeByte( (byte) origin_stone[0] );
          ds_snd.writeByte( (byte) origin_stone[1] );
          ds_snd.writeByte( (byte) origin_stone[2] );
          
          if(side==RIGHT)
              ds_snd.writeBoolean(true);
          else
              ds_snd.writeBoolean(false);
      }


 /**************************** POSITION UPGRADE ****************************/

    public void updateSpritePosition()
    {
       if( b_stone_step == BROKEN_STONE_CYCLES ){
       	  should_end = true;
          return;
       }

     // we upgrade x, y
        setRectangle( getX()+signed_side*STONE_OFFSET[0][b_stone_step],
                           getY()+STONE_OFFSET[1][b_stone_step],
                           STONE_WIDTH, STONE_HEIGHT );
     
      // position restrictions
         if( ( getX()+r.width+ROTATE_OFFSET[side][2] >= 640 ) || ( getX()+ROTATE_OFFSET[side][0] < 0 ) )
         {
           should_end = true;

            if(getX()<0) setX(0);
         
           b_stone_step = BROKEN_STONE_CYCLES;	
         }
         else
           b_stone_step++;
    }


 /**************************** STONE ROTATION *******************************/

    public AffineTransform getTransform( int y )
    {
       AffineTransform tx = new AffineTransform();

       tx.translate( getX(),y );
       tx.rotate( signed_side*0.15*(b_stone_step+1) );
      
       return tx;
    }


 /************************ DATA & POSITION ACCESS ***************************/

    public BufferedImage getImage() {
        return im;
    }
    
    public boolean firstDisplay() {
       return  not_initialized;
    }
    
    public boolean shouldEnd() {
       return  should_end;
    }
    
    public int getLevel() {
       return  origin_stone[0];
    }
    
    public int getStone() {
       return  origin_stone[1];
    }
    
    public int getLine() {
       return  origin_stone[2];
    }
    
    public int getCorX1(){
    	return ROTATE_OFFSET[side][0];
    }
    
    public int getCorY1(){
    	return ROTATE_OFFSET[side][1];
    }
    
    public int getCorX2(){
    	return ROTATE_OFFSET[side][2];
    }
    
    public int getCorY2(){
    	return ROTATE_OFFSET[side][3];
    }
    
 /*************************************************************************/
}