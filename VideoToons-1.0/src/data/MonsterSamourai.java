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


class MonsterSamourai extends AnimMonsterLibrary
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterSamourai()
    {
         anim.name = new String("samourai");
         anim.nb_images = 16;

         anim.behaviour = GameDefinitions.MB_HITTING;

      // Images offset & indexes

         int array1[] = { 0, 1, 1, 0, 0, 2, 2, 0 };
         anim.move_right = new Anim( array1 );
        
         int array2[] = { 7, 8, 8, 7, 7, 9, 9, 7 };
         anim.move_left = new Anim( array2 );

         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;

         int array4[] = { 7 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 0 };
         anim.rightside = new Anim( array6 );
         
         anim.jump_left = null;
         anim.jump_right = null;
         anim.hit_up_right = null;
         anim.hit_up_left = null;

         int array11[] = { 13, 13, 14, 14 };
         anim.fall_left = new Anim( array11 );
         
         int array12[] = { 6, 6, 15, 15 };
         anim.fall_right = new Anim( array12 );

         anim.dead_offset = null;
         anim.nb_dead_steps = 0;
         anim.dead = null;

         int array15[] = { 13, 13, 14, 14 };
         anim.hurt_left = new Anim( array15 );
         
         int array16[] = { 6, 6, 15, 15};
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -10, -15, -17, -10, 0, 0, };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=6;

         int array18[] = { 10, 10, 10, 12, 12, 11, 11, 7  };
         anim.hit_left = new Anim( array18 );
         
         int array19[] = {  3, 3, 3, 5, 5, 4, 4, 0 };
         anim.hit_right = new Anim( array19 );

         int array20[] = { -6, 0, 0, -19, 0, 15, 0, 10 };
         anim.hit_left_offset = array20;
         
         int array21[] = { -6, 0, 0, 0, 0, 0, 0, 6 };
         anim.hit_right_offset = array21;
         anim.nb_hit_steps = 8;
         
         anim.sprite_basicspeed = 2;
         anim.overlap_sprite = 10;
         anim.std_width = 28;
         anim.std_height = 41;

         anim.hit_interval = 2000;
         anim.invisible_interval = 3000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.LIGHT_HIT_SOUND;
         anim.hurt_sound = SoundLibrary.LOW_HURT_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}


