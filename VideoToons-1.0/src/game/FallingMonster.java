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


class FallingMonster extends Monster
{
  // variable for respawn time measure
     private long invisible_interval_t0;
  
 // Time before we go to the next state (ms)
    public long visible_time;

 // time before reappearing
    public long invisible_time;

 /**************************** MONSTER CONSTRUCTOR *****************************/

    public FallingMonster( AnimLibrary anm, ServerLevelProcess level, byte stone, byte stage, byte speed)
    {
      super(anm,level,stone,stage,speed);

         first_stone[0]++;

      // initial behaviour
         defaultBehaviour();
    }

   
 /**************************************************************************/
 /********************* REDEFINITION OF ABSTRACT METHODS *******************/
 /**************************************************************************/
 
 
 /******************************* DO WE FALL ********************************/

  // returns true if the sprite falls... ALSO upgrades X pos if the sprite
  // is on moving stones ...

   protected boolean doWeFall()
   {
   	return false;
   }


 /***************************** UPDATE THE FALL ******************************/   

   protected void updateFall()
   {
     // next fall step...
        translateY( anm.jump_offset[current_jump_cycle] );

        if( current_jump_cycle!=0 )
                 current_jump_cycle--;

        updateCurrentImage();
   }


 /***************************** JUMP UPGRADE ******************************/
 
   protected void updateJump()
   {}

 /***************************** HURT UPGRADE *****************************/

   protected boolean areWeHurt()
   {
     if( spr_status == BS_HURT )
     {
      // First cycle ?
     	if( current_hurt_cycle==0 )
     	{
          current_anim = anm.hurt_right;
          current_anim.reset();
          updateCurrentImage();

          level.addSound( anm.hurt_sound );
        }
        else if( current_hurt_cycle==anm.nb_hurt_steps )
        {
     	 // End of hurt cycles
     	    current_hurt_cycle = 0;
            defaultBehaviour();
            return false;
     	}

         translateY( anm.hurt_offset[current_hurt_cycle] );

         if( hurt_right_side==true ) {
             r.x += 5;
             limitXRangeRight();
         }
     	 else {
             r.x -= 5;
             limitXRangeLeft();
     	 }

        current_hurt_cycle++;
        updateCurrentImage();
        return true;
     }
     	
    return false;
   }

 /**************************** POSITION UPGRADE ****************************/

  // To tell what to do after updating positions, images, sizes ...
  // This method is called before we redraw the sprite.
  
    public void updateSpritePosition()
    {
     /******* MONSTER BASIC IA *********/

          updateBehaviour();

     /***** SOME WORLD INTERACTION *****/

       // HURT CASE
          if( areWeHurt() )
                return;

       // FALL CASE
          if( falling ){
              updateFall();
              return;
          }
     }

 /********************************************************************************/

    private void defaultBehaviour()
    {
       defaultState();

       spr_status = BS_INVINCIBLE;
       spr_visible = false;
       invisible_interval_t0 = System.currentTimeMillis();
       invisible_time = anm.invisible_interval + (short) (Math.random()*2500);
    }

 /********************************************************************************/


     private void updateBehaviour()
     {
      // respawn time elapsed ?
        long t1 = level.getCurrentTime();
        byte currenttop = level.getCurrentTopTunnel();

         if( !spr_visible )
         {
            if( t1-invisible_interval_t0 < invisible_time )
              return;

            if(first_stone[0]<=currenttop-4  || currenttop<first_stone[0]) {
                 invisible_interval_t0 = t1;
                 return;
            }

              if(getRandomStone())
              {
                 setX( first_stone[1]*STONE_WIDTH );

                 if(level.getStoneState(first_stone[0],first_stone[1],1)==STONE_BROKEN)
                      setY( STAGE_HEIGHT[first_stone[0]]+STONE_HEIGHT-3);
                 else
                      setY( STAGE_HEIGHT[first_stone[0]]+STONE_HEIGHT);

                 current_anim = anm.middle;

                 current_anim.reset();
                 updateCurrentImage();

                 spr_visible = true;
                 spr_status = BS_ACTIVE;
                 visible_time = t1;
              }
              else
                   return;
         }

       // collision timer
          if(!falling && t1-visible_time>25)
          {
             visible_time=t1;
             updateCurrentImage();
             
             if( current_anim.isAnimEnd() && spr_status!=BS_HURT)
             {
                spr_status = BS_DANGEROUS;
                current_jump_cycle = anm.nb_jump_steps-1;

                falling = true;
                
                current_anim = anm.fall_right;
                current_anim.reset();
                updateCurrentImage();

                level.addSound( anm.hit_sound );
             }
             else
                return;
          }

       // Did we fell out of the screen ?
          if( r.y-STAGE_HEIGHT[currenttop] >= 480)
          {
             defaultBehaviour();
             return;
          }
     }


 /********************************************************************************/

   /*  returns true if the selected stone is not broken
    */

     private boolean getRandomStone()
     { 
        first_stone[1] = (byte) (Math.random()*19+6);
        byte state = level.getStoneState(first_stone[0],first_stone[1],0);

        if(state != STONE_BROKEN)
            return true;
        return false;
     }

 /********************************************************************************/


     public void collisionBehaviour( BasicSprite bs )
     {
       // if the number of cycles since last collision is not elapsed we quit...
         byte state = bs.getStatus();     	
         Rectangle r_bs =  bs.getRectangle();

          if( spr_status==BS_HURT || r.intersects(r_bs) == false )
                return;

       // YEAH Collision detected !!!
          if( !falling || ( state==BS_HITLEFT && r_bs.x+r_bs.width-r.x>15 )
                                   || (state==BS_HITRIGHT && r.x-r_bs.x>15) )
             youAreHurt(leftside);

     }

 /********************************************************************************/

   // nothing ? yep... nothing...

   public void collisionWithBlock( Block bl )
   {
   }

 /********************************************************************************/

}