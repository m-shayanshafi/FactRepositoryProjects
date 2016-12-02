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


abstract class AnimatedSprite extends BasicSprite
{

  /************************ SPRITE ANIMATIONS *****************************/

  // All the sprite anims and own data
     protected AnimLibrary anm;

  // The current sprite animation
     protected Anim current_anim;
     
     
  /************************ ANIMATIONS CYCLES *****************************/

  // Current position in the jump_offset array
     protected int current_jump_cycle;

  // Timer to measure the nb of cycles when we are being injured
     protected int current_hurt_cycle;

  // Timer to measure the nb of cycles when we hit
     protected int current_hit_cycle;

  // Timer to measure the nb of cycles when we are dead
     protected int current_dead_cycle;


  /*********************** OTHER PROPERTIES *******************************/

  // Extra speed : when the sprite is moved by something
  // The total speed of the sprite is : sprite_speed + extra_speed
     protected byte extra_speed;

  // First stone. When the sprite dies, it reappears on that stone
  // [stage][stone][line] the line is supposed to be the first (0).
     protected byte first_stone[] = new byte[3];

  // first time coord when waiting between two hits !
     private long hit_interval_t0;

  /******************** SPRITE MOVEMENTS ATTRIBUTS ************************/

   // Is the player jumping ? falling ?
      protected boolean jumping;
      protected boolean falling;

   // Does the sprite moves (x) ?
      protected boolean movingright;
      protected boolean movingleft;

   // What is the sprite orientation ? right, left or middle ?
      protected boolean rightside;
      protected boolean leftside;

   // Are we trying to hit something ?
      protected boolean hitting;
      protected boolean hitting_stone;
      
   // On which side are we hurt ? on the right side ?
      protected boolean hurt_right_side;



  /**************************** CONSTRUCTOR *******************************/

    public AnimatedSprite( AnimLibrary anm, byte stone, byte stage )
    {
      int w, h, i;

     // Sprite animations
        this.anm = anm;

     // init sprite first position and rectangle
        current_im = anm.rightside.getIndex(0);
  
        setRectangle( stone*STONE_WIDTH, STAGE_HEIGHT[stage]-anm.std_height,
                                              anm.std_width, anm.std_height );
        updateOldRectangle();

        first_stone[0] = stage;
        first_stone[1] = stone;
        first_stone[2] = 0;

      // First hit
         hit_interval_t0 = System.currentTimeMillis();

     // Default properties
        defaultState();
    }

  /**************************** SPRITE DEFAULTS **************************/

   protected void defaultState()
   {
        current_anim = anm.rightside;
        rightside    = true;
        leftside     = false;
        movingright  = false;
        movingleft   = false;
        
        jumping      = false;
        falling      = false;        

        hitting       = false;
        hitting_stone = false;
        
        spr_visible       = true;
                
        extra_speed       = 0;
        current_hit_cycle = 0;
        current_hurt_cycle= 0;
        current_dead_cycle= 0;
        
        spr_status = BS_ACTIVE;
   }


  /************************* LIVES & NAME *********************************/ 
    
    public String getSpriteName(){
        return anm.name;
    }

    public int getImageNumber(){
        return anm.nb_images;
    }

 /************************* ABSTRACT METHODS ******************************/
 
  // To tell what to do after updating positions, images, sizes ...
  // This method is called before we redraw the sprite.
    abstract public void updateSpritePosition();

  // tells how this sprite reacts to a collision with another sprite...
    abstract public void collisionBehaviour( BasicSprite bs );

  // Changes the current image to be displayed
    abstract public void updateCurrentImage();

  // To Limit The Sprite X Range On the right side ...
    abstract protected void limitXRangeRight();

  // ... and on the left side ...
    abstract protected void limitXRangeLeft();

 // returns true (still hurt), false(not hurt anymore)
    abstract protected boolean areWeHurt();

  // returns true if the sprite falls... ALSO upgrades X pos if the sprite
  // is on moving stones ...
    abstract protected boolean doWeFall();

  // Fall update
    abstract protected void updateFall();

  // Jump update
    abstract protected void updateJump();


 /************************************************************************/
 /************************ AVAILABLE METHODS *****************************/
 /************************************************************************/

  // Returns the stone number on our left under us (0<= <=31)
     protected byte getLeftStoneUnderUs()
     {
       byte b= (byte) ( (r.x+anm.overlap_sprite)/20);
     
       if( b<0 )
           return 0;
       else if( b > 31 )
           return 31;

       return b;
     }

  // Returns the stone number on our right under us (0<= <=31)
     protected byte getRightStoneUnderUs()
     {
       byte b;
     	
       if(!hitting)
           b= (byte) ((r.x+anm.std_width-anm.overlap_sprite)/20);
       else
           b=(byte) ((r.x+r.width-anm.overlap_sprite)/20);
     
       if( b<0 )
           return 0;
       else if( b > 31 )
           return 31;

       return b;
     }


 /*************************** EXTRA SPEED UPDATE **************************/


   protected void updateSpeedEffect()
   {
      if( extra_speed == 0 )
         return;
      
      r.x += extra_speed/3;

      if( extra_speed < 0) {
            limitXRangeLeft();
            extra_speed++;
      }
      else {
            limitXRangeRight();
            extra_speed--;
      }
   }

 /*************************************************************************/   

   protected void updateMoveWhileJumping()
   {
      if( movingright == true )
      {
          translateX( (int)anm.sprite_basicspeed );
                
          if( leftside == true )
          {
               current_anim = anm.jump_right;
               leftside=false;
               rightside=true;
           }
                  
           limitXRangeRight();
        }
        else if( movingleft == true )
        {
           translateX( -(int)anm.sprite_basicspeed );
                   
           if( rightside == true )
           {
                 current_anim = anm.jump_left;
                 leftside=true;
                 rightside=false;
            }
                   
            limitXRangeLeft();
        }
   }


 /*************************************************************************/   

   protected void updateMoveWhileFalling()
   {
      if( movingright == true )
      {
          translateX( (int)anm.sprite_basicspeed );
                
          if( leftside == true )
          {
               current_anim = anm.fall_right;
               leftside=false;
               rightside=true;
           }
                  
           limitXRangeRight();
        }
        else if( movingleft == true )
        {
           translateX( -(int)anm.sprite_basicspeed );
                   
           if( rightside == true )
           {
                 current_anim = anm.fall_left;
                 leftside=true;
                 rightside=false;
            }
                   
            limitXRangeLeft();
        }
   } 


 /*************************************************************************/   

   protected void updateGroundSteady()
   {
      // we must be on the ground (sprite image with different height)
         r.y =  STAGE_HEIGHT[getStage()] -r.height;

      // do we fall ?
         if( doWeFall() == true )
         {
             current_jump_cycle = (anm.nb_jump_steps-1);
                  
               if( rightside == true )
                     current_anim = anm.fall_right;
               else
                     current_anim = anm.fall_left;
                     
              current_anim.reset();
         }
   }


 /*************************************************************************/   

   protected void updateRightMovement()
   {
      if( rightside == true )
      {
         translateX( (int)anm.sprite_basicspeed );
         current_anim = anm.move_right;
              
         limitXRangeRight();
      }
      else if ( leftside == true )
      {
         current_anim = anm.middle;
         leftside = false;
      }
      else
      {
         current_anim = anm.rightside;
         rightside = true;	
      }

     updateCurrentImage();
     r.y =  STAGE_HEIGHT[getStage()] -r.height;
   }


 /*************************************************************************/   

   protected void updateLeftMovement()
   {
      if( leftside == true )
      {
        translateX( -(int)anm.sprite_basicspeed );
        current_anim = anm.move_left;
              
        limitXRangeLeft();
      }
      else if ( rightside == true )
      {
         current_anim = anm.middle;
         rightside = false;
      }
      else
      {
         current_anim = anm.leftside;
         leftside = true;	
      }
            
     updateCurrentImage();
     r.y =  STAGE_HEIGHT[getStage()] -r.height;
   }

 /*************************************************************************/ 

   protected void updateHitMovement()
   {
      if(current_hit_cycle==0)
      {
         if(leftside==true)
         {
            if(spr_status != BS_DANGEROUS) spr_status = BS_HITLEFT;
            current_anim = anm.hit_left;
         }
         else
         {
            if(spr_status != BS_DANGEROUS) spr_status = BS_HITRIGHT;
            current_anim = anm.hit_right;
         }

        current_anim.reset();
      }
      else if( current_hit_cycle == anm.nb_hit_steps )
      {
      	if(spr_status != BS_DANGEROUS) spr_status = BS_ACTIVE;
      	hitting = false;
      	return;
      }

     if( leftside==true )
     {
        translateX( anm.hit_left_offset[current_hit_cycle] );
        
        if( anm.hit_left_offset[current_hit_cycle] < 0)
            limitXRangeLeft();
        else
            limitXRangeRight();
     }
     else
     {
        translateX( anm.hit_right_offset[current_hit_cycle] );
        
        if( anm.hit_right_offset[current_hit_cycle] < 0)
            limitXRangeLeft();
        else
            limitXRangeRight();
     }

     current_hit_cycle++;
     updateCurrentImage();
     
     // we must be on the ground (sprite image with different height)
        r.y = STAGE_HEIGHT[getStage()] -r.height;
   }

 /*************************************************************************/   

   protected void updateNoMovement()
   {
      if( leftside == true)
          current_anim = anm.leftside;
      else 
      {
          rightside = true;   // in case we were with the anm.middle
          current_anim = anm.rightside;
      }

     updateCurrentImage();
   }


 /*************************************************************************/

  public boolean isMovingLeft(){
      return movingleft;
  }
  

 /*************************************************************************/

  protected void youAreHurt( boolean hurtrightside )
  {
    if(spr_status == BS_DEAD || spr_status == BS_HURT || spr_status == BS_INVINCIBLE )
        return;

     spr_status = BS_HURT;
     hurt_right_side = hurtrightside;
     current_hurt_cycle = 0;
  }


 /*************************************************************************/ 
 /*                              COMMANDS                                 */
 /*************************************************************************/   

   protected void startRight(){
       if(movingright == true) return;
       movingright = true;
       movingleft = false;
       anm.move_right.reset();
   }


 /*************************************************************************/   

   protected void startLeft(){
      if(movingleft == true) return;
      movingright = false;
      movingleft = true;
      anm.move_left.reset();
   }


 /*************************************************************************/   

   protected void startJump(){
      if((jumping == true)||(falling==true)) return;
      jumping = true;
      anm.jump_right.reset();
      anm.jump_left.reset();
      current_jump_cycle = 0;

      if(spr_status==BS_HITLEFT || spr_status==BS_HITRIGHT) {
          spr_status = BS_ACTIVE;
          hitting=false;
      }
   }

 /*************************************************************************/   

   protected void startHit(){
      if( jumping==true || falling==true || hitting==true ) return;

      long t1 = System.currentTimeMillis();

      if( t1-hit_interval_t0 < anm.hit_interval )
          return;

      hit_interval_t0 = t1;       
      hitting = true;
      current_hit_cycle = 0;
   }

 /*************************************************************************/   

   protected void stopLeft(){
      movingleft = false;
   }


 /*************************************************************************/   

   protected void stopRight(){
      movingright = false;
   }


 /*************************************************************************/   

   protected void stopJump(){
      if( falling==false )
           current_jump_cycle = anm.nb_jump_steps-1;
   }


 /************************************* END *******************************/
}