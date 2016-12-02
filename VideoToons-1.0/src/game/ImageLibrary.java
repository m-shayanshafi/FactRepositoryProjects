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
import java.awt.image.BufferedImage;

class ImageLibrary implements GameDefinitions
{

 // ***************************** TUNNELS ************************************ //

       public BufferedImage tunnel_images[][];


 // *****************************  CLOUDS ************************************ //

       public BufferedImage cloud_images[];

    // transparent cloud ?
       public boolean transparent_cloud[];


 // ***************************** PLAYERS ************************************ //

    // array order is [player_id][image_index]
       public Image player_images[][];

    // Small image 25x25 representing the host player
       public Image live_image;

    // Victory & defeat images
       public Image victory_image;
       public Image defeat_image;
       
    // Game Over image
       public Image gameover_image;

    // Waiting other players image
       public Image waiting_image;

    // Quit Menu :Player quits or resumes
       public Image quit_image;
       public Image continue_image;
       
    // player pause
       public Image pause_image;

 // ***************************** MONSTERS *********************************** //

    // array order is [stage][side][image_index]
       public Image monster_images[][][];


 // ****************************** STONES ************************************ //

       public Image stage_images[];
       
    // Stone backup images. You must first check that the stone still exists.
       public Image stone_backup_image[][][];


 // **************************** CONSTRUCTOR ********************************* //

    public ImageLibrary()
    {
       tunnel_images = new BufferedImage[TUNNEL_NUMBER][2];
       cloud_images  = new BufferedImage[CLOUD_NUMBER];
       stage_images  = new Image[STAGE_NUMBER];
       monster_images = new Image[MONSTER_NUMBER][2][];

       stone_backup_image = new Image[STAGE_NUMBER][STONE_NUMBER][LINE_NUMBER];
    }


 // **************************** TO LOAD IMAGES ****************************** //

    public static Image[] loadGIFImages( String basename, int nb_img )
    {
       Image tab[] = new Image[nb_img];
       GameScreen gs = VideoToons.getGameScreen();
       MediaTracker tracker = new MediaTracker(gs);

         for( int i=0; i<nb_img; i++)
         {
            tab[i] = gs.getToolkit().getImage(basename+i+".gif");
            tracker.addImage(tab[i],i);
         }
         
          try{
               tracker.waitForAll();
          }
          catch(InterruptedException e) { e.printStackTrace(); }
          
       return tab;
    }


    public static Image loadImage( String name )
    {
       Image im;
       GameScreen gs = VideoToons.getGameScreen();
       MediaTracker tracker = new MediaTracker(gs);

         im = gs.getToolkit().getImage(name);
         tracker.addImage(im,0);

         try{
               tracker.waitForID(0);
          }
          catch(InterruptedException e) { e.printStackTrace(); }

       return im;
    }

}