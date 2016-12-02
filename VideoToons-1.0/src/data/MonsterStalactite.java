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


class MonsterStalactite extends AnimMonsterLibrary
{
  public AnimMonsterLibrary anim = new AnimMonsterLibrary();

    public MonsterStalactite()
    {
         anim.name = new String("stalactite");
         anim.nb_images = 13;

         anim.behaviour = GameDefinitions.MB_FALLING;

      // Images offset & indexes

         int array1[] = { 0 };
         anim.move_right = new Anim( array1 );
        
         int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
         anim.jump_offset = array3;
         anim.nb_jump_steps=11;

         int array4[] = { 0 };
         anim.leftside = new Anim( array4 );

         int array5[] = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 9, 8, 9, 8, 9, 8, 9, 8, 8, 8 };
         anim.middle = new Anim( array5 );
         
         int array6[] = { 0 };
         anim.rightside = new Anim( array6 );
              
         int array12[] = { 8 };
         anim.fall_right = new Anim( array12 );


         int array16[] = { 10, 10, 11, 11, 12, 12 };
         anim.hurt_right = new Anim( array16 );

         int array17[] = { -4, -2, 0, 2, 4, 4 };
         anim.hurt_offset = array17;
         anim.nb_hurt_steps=6;
        
         anim.sprite_basicspeed = 4;
         anim.overlap_sprite = 10;
         anim.std_width = 20;
         anim.std_height = 23;

         anim.invisible_interval = 2000;

         anim.jump_sound = SoundLibrary.JUMP_SOUND;
         anim.land_sound = SoundLibrary.LAND_SOUND;
         anim.hit_sound = SoundLibrary.COLAPSING_SOUND;
         anim.hurt_sound = SoundLibrary.BROKENSTONE_SOUND;
         anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}


