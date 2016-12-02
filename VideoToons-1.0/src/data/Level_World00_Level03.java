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


class Level_World00_Level03 implements GameDefinitions
{
  public LevelData ldat;

   public Level_World00_Level03()
   {
      ldat = new LevelData();

   // path name
        ldat.path_name = new String("w00-l03");

   // music
        ldat.music_name = new String("music/level-old2.mid");

   // Stones pre destroyed ?
   
        boolean array0[]= {  false,   	// stage 0
                             false,  	// stage 1
                             false,  	// stage 2
                             false,	// stage 3
                             false, 	// stage 4
                             false,  	// stage 5          
                             false,  	// stage 6
                             false,	// stage 7
                             false,	// stage 8
                             false,	// stage 9
                             false, };	// stage 10

        ldat.pre_destroyed_stones = array0;

  // Stone states

     byte array1[][] = {
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, }, // stage 10
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, }, // stage 9
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,1,1,4,4,4,4,4,4,
     	   4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,1,1,4,4,4,4,4,4, }, // stage 8
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4,4,4,4,4,4,4,
     	   4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4,4,4,4,4,4,4, }, // stage 7
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,1,1,1,1,4,4,4,4,4,4,4,1,4,4,4,4,4,4,4,4,4,4,4,4,4,
     	   4,4,4,4,4,4,4,1,1,1,1,4,4,4,4,4,4,4,1,4,4,4,4,4,4,4,4,4,4,4,4,4, }, // stage 6
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,0,0,0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,
     	   4,4,4,4,4,0,0,0,0,0,0,0,0,4,4,4,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4, }, // stage 5
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4,
     	   4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,4,4,4,4,4,4, }, // stage 4
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,4,1,1,4,4,4,4,0,0,0,0,4,4,4,1,1,4,4,4,4,4,4,4,
     	   4,4,4,4,4,4,4,4,4,4,1,1,4,4,4,4,0,0,0,0,4,4,4,1,1,4,4,4,4,4,4,4, }, // stage 3
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,1,1,4,4,1,1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
     	   4,4,4,4,4,4,1,1,4,4,1,1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4, }, // stage 2
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4,
     	   4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,4, }, // stage 1
   //------1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2---------------
     	{  4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
     	   0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, }  // stage 0
     	};

     	ldat.stone_state = array1;

   // stage images ( -1 == no stage image)
     	ldat.nb_stage_images = 6;
        //                0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10
     	int array2[] = { -1, -1,  0,  1,  2, -1,  3,  4,  5, -1, -1 };
     	ldat.stage_image_index = array2;
     	
     	
    // Tunnels
        ldat.nb_tunnel_images = 6;
    
        int array3[][] =    { { 0, 2 },  	// stage 0
                              { 1, 3 },  	// stage 1
                              { 4, 5 },  	// stage 2
                              { 4, 5 },		// stage 3
                              { 0, 2 },		// stage 4
                              { 1, 3 },  	// stage 5          
                              { 4, 5 },  	// stage 6
                              { 4, 5 } };	// stage 7
        ldat.tunnel_image_index = array3;

        int array4[][] =    { {   0, 20 },  	// stage 0
                              {   0, 20 },  	// stage 1
                              {   0, 20 },  	// stage 2
                              {   0, 20 },	// stage 3
                              {   0, 20 }, 	// stage 4
                              {   0, 20 },  	// stage 5 
                              {   0, 20 },  	// stage 6
                              {   0, 20 } };	// stage 7
        ldat.tunnel_offset = array4;

        boolean array5[][]= { { true, true  },  	// stage 0
                              { true, true  },  	// stage 1
                              { true, true  },  	// stage 2
                              { true, true   },	// stage 3
                              { true, true  }, 	// stage 4
                              { true, true  },  	// stage 5          
                              { true, true  },  	// stage 6
                              { true, true   } };	// stage 7
        ldat.tunnel_collision = array5;
        
     // Clouds at stage   0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10 
        int array6[] = { -1,  0, -1, -1, -1,  1, -1, -1, -1,  1, -1  };
        ldat.cloud_image_index = array6;

     // Clouds speed      0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  10
        byte array7[] = { 0, -4,  0,  0,  0, -6,  0,  0,  0,  2,  0  };
        ldat.cloud_speed = array7;

     // Transparent cloud ?
        boolean array8[]= {  false,   	// stage 0
                             false,  	// stage 1
                             false,  	// stage 2
                             false,	// stage 3
                             false, 	// stage 4
                             true,  	// stage 5          
                             false,  	// stage 6
                             false,	// stage 7
                             false,	// stage 8
                             true,	// stage 9
                             false, };	// stage 10

        ldat.transparent_cloud = array8;
        ldat.nb_cloud_images=2;

     // Block at stage    0,  1,  2,  3,  4,  5,  6,  7  
        int array9[] = { -1, -1,  0,  0,  1, -1,  0, -1  };
        ldat.block_image_index = array9;
        
        int array10[] ={ -1, -1, 13, 21, 14, -1, 16, -1  };
        ldat.block_stone = array10;
        
        ldat.nb_block_images=2;

     // MONSTERS !!!
        byte array11[][]= { { MT_NONE,        MT_NONE      },  	// stage 0
                            { MT_FALLINGROCK, MT_FALLINGROCK },  	// stage 1
                            { MT_SAMOURAI,    MT_HEAVYROCK      },  	// stage 2
                            { MT_HEAVYROCK,   MT_HEAVYROCK  },	// stage 3
                            { MT_SAMOURAI,    MT_HEAVYROCK  }, 	// stage 4
                            { MT_FALLINGROCK, MT_FALLINGROCK },  	// stage 5          
                            { MT_ROBBER,      MT_ROCKSTONE },  	// stage 6
                            { MT_HEAVYROCK,   MT_FALLINGROCK },  	// stage 7
                            { MT_SAMOURAI,    MT_HAMMER  } };	// stage 8


        ldat.monsters_id = array11;

    // monster speed (-1 means default)
        byte array12[][]= { { -1,  -1 },  // stage 0
                            { -1,  -1 },  // stage 1
                            { -1,  -1 },  // stage 2
                            { -1,  -1 },	// stage 3
                            { -1,  -1 }, 	// stage 4
                            { -1,  -1 },  // stage 5          
                            { -1,  -1 },  // stage 6
                            { -1,  -1 },  // stage 7
                            { -1,  -1 } };// stage 8

        ldat.monsters_speed = array12;

    // monster extra parameter (-1 means none)
        byte array13[][]= { { -1,  -1 },  // stage 0
                            { -1,  -1 },  // stage 1
                            { -1,  21 },  // stage 2
                            { 10,  23 },  // stage 3
                            { -1,  24 },  // stage 4
                            { -1,  -1 },  // stage 5          
                            { -1,  -1 },  // stage 6
                            { 14,  -1 },  // stage 7
                            { -1,  -1 } };// stage 8

        ldat.monster_parameter = array13;

        ldat.time_before_next_stage = 40000;
   }

}