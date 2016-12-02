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

class DimensionLibrary implements GameDefinitions
{
  // Monster Type for each monster_index.
     public byte monster_type[];

  // Images WIDTH. array order is [monster_index][image_index]
     public short width[][];

  // Images Height.
     public short height[][];

  // last monster index in these array.
     private short last_index;

 /************************** CONSTRUCTOR *******************************/

     public DimensionLibrary()
     {
         monster_type = new byte[2*MONSTER_NUMBER];
         width = new short[2*MONSTER_NUMBER][];
         height = new short[2*MONSTER_NUMBER][];

         last_index = -1;
     }

 /********************* LOAD MONSTER DIMENSION **************************/

   // param type : monster type
   // returns the index in the width & height array.

     public short loadMonsterDimensions( byte type )
     {
       // Dimensions already loaded ?
           for(short i=0; i<=last_index; i++ )
              if( monster_type[i] == type )
                 // ok, no need to reload these dimensions
                    return i;

        // we load the dimensions for each image
           AnimLibrary alib = ObjectLibrary.getMonsterSpriteByNumber(type);
           String basename = new String("graphics/monsters/monstr"+type+"/monst-");
           short tab_height[] = new short[alib.nb_images];
           short tab_width[] = new short[alib.nb_images];

           Label l = new Label();
           MediaTracker tracker = new MediaTracker(l);
           Image im;

           for( int i=0; i<alib.nb_images; i++)
           {
            // we load the image
               im = Toolkit.getDefaultToolkit().getImage(basename+i+".gif");
               tracker.addImage(im,0);
         
               try{
                   tracker.waitForID(0);
               }
               catch(InterruptedException e) { e.printStackTrace(); }

            // we get its dimensions
               tab_width[i] = (short) im.getWidth(l);
               tab_height[i] = (short) im.getHeight(l);
           }
          
       // we write the results
          last_index++;
          monster_type[last_index] = type;
          width[last_index] = tab_width;
          height[last_index] = tab_height;

        return last_index;
    }

 /********************************************************************************************/

    public short getWidth( int monster_index, int image_index) {
         return width[monster_index][image_index];
    }

 /*******************************************************************************************/

    public short getHeight( int monster_index, int image_index) {
         return height[monster_index][image_index];
    }

 /*******************************************************************************************/


    public static short[][] getCloudDims( String level_name, int nb_cloud_images, int cloud_image_index[] )
    {
       if(nb_cloud_images==0)
          return null;

       short dims[][] = new short[CLOUD_NUMBER][2];  // width, height
       Image im[] = new Image[nb_cloud_images];
       String basename = "graphics/levels/"+level_name+"/cloud";


        // 1 - We load the cloud images

           Label l = new Label();
           MediaTracker tracker = new MediaTracker(l);

           for( int i=0; i<nb_cloud_images; i++)
           {
            // we load the image
               im[i] = Toolkit.getDefaultToolkit().getImage(basename+i+".gif");
               tracker.addImage(im[i],i);
           }
         
           try{
                   tracker.waitForAll();
           }
           catch(InterruptedException e) {
           	 e.printStackTrace();
           	 return null;
           }


        // 2 - We get the clouds dimensions
           for(byte i=0; i<CLOUD_NUMBER; i++ )          
              if(cloud_image_index[i]>=0){
                 dims[i][0] = (short) im[cloud_image_index[i]].getWidth(l);
                 dims[i][1] = (short) im[cloud_image_index[i]].getHeight(l);
              }

        return dims;
    }

}

