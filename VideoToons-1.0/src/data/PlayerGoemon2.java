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

package videotoons.data;

import videotoons.game.*;


class PlayerGoemon2
{
   public AnimLibrary anim;

    public PlayerGoemon2()
    {
    	anim = new AnimLibrary();

         anim.name = new String("goemon2");
         anim.nb_images = 29;

      // Images offset & indexes

         int array1[] = { 1, 3,  4, 4, 1,  5, 6, 6 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 2,  7, 8, 8, 2,  9, 10, 10 };
         anim.move_left = new Anim( array2 );
         
         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;
         
         int array4[] = { 2 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 1 };
         anim.rightside = new Anim( array6 );
         
         int array7[] = { 15 };
         anim.jump_left = new Anim( array7 );
         
         int array8[] = { 11 };
         anim.jump_right = new Anim( array8 );

         int array9[] = { 13, 13, 13, 14, 14, 14, 14, 14, 14, 14 };
         anim.hit_up_right = new Anim( array9 );         

         int array10[] = { 17, 17, 17, 18, 18, 18, 18, 18, 18, 18 };
         anim.hit_up_left = new Anim( array10 );

         int array11[] = { 16 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 12 };
         anim.fall_right = new Anim( array12 );

         int array13[] = { -20, -20, -15, -10, -6, -2, 0, 2, 6, 10, 15, 20, 25 };
         anim.dead_offset = array13;
         anim.nb_dead_steps = 13;

         int array14[] = { 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20 };
         anim.dead = new Anim( array14 );
         
         int array15[] = { 24 };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 28 };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -10, -5, -2, 0 };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=4;

         int array18[] = { 17, 25, 26, 27, 27, 27, 2  };
         anim.hit_left = new Anim( array18 );
         
         int array19[] = { 13, 21, 22, 23, 23, 23, 1  };
         anim.hit_right = new Anim( array19 );

         int array20[] = { 0,  0, -17,  0,  0,  0, 17 };
         anim.hit_left_offset = array20;
         
         int array21[] = { -23, 9, 14,  0,  0,  0,  0 };
         anim.hit_right_offset = array21;
         anim.nb_hit_steps = 7;

         anim.sprite_basicspeed = 5;
         anim.overlap_sprite = 10;
         anim.std_width = 26;
         anim.std_height = 41;

         anim.hit_interval = 200;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.LIGHT_HIT_SOUND;
         anim.hurt_sound = SoundLibrary.LONG_HURT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}


