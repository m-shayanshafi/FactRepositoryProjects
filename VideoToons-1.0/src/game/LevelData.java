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

public class LevelData implements Serializable
{

 // music name for the level
    public String music_name;

 // Stone states stage by stage... see GamesDefinitions for the <> types
    public byte stone_state[][];

 // do we display the stones at the beginning of the level for each stage ?
 // useful only when you put a monster that will fill these holes... and so
 // reconstruct the stage. If you enter true for a level, stones to be broken are
 // STONE_SIMPLE, STONE_LEFT, STONE_RIGHT, STONE_ACID. Note that the stage MUST
 // only have one line of stones.
    public boolean pre_destroyed_stones[];

 // Level name, Stone images,  number of images and image index per stage.
    public String path_name;
    public int nb_stage_images;
    public int stage_image_index[];

 // Tunnel images,  number of images, image index per stage (rightside and leftside)
 // offset for display and collision detection.
    public int nb_tunnel_images;
    public int tunnel_image_index[][];
    public int tunnel_offset[][];
    public boolean tunnel_collision[][];
    

 // Stage clouds if a cloud exists at stage i cloud_exists[i] = cloud_speed. If = 0 = no cloud
    public byte cloud_speed[];
    public int cloud_image_index[];
    public boolean transparent_cloud[];
    public int nb_cloud_images;


 // Blocks
    public int block_image_index[];   
    public int block_stone[];
    public int nb_block_images;

 // Monsters
    public byte monsters_id[][];
    public byte monsters_speed[][];

 // extra monster parameter
    public byte monster_parameter[][];

 // Time before the LevelUpMonster comes (ms)
    public long time_before_next_stage;

}