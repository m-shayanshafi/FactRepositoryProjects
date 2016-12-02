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
import java.awt.geom.*;
import java.awt.image.BufferedImage;


class GameScreen extends Window implements GameDefinitions
{

 /************************** DISPLAY DEFINITIONS *******************************/

  // Possible values when asking for setRefreshMode(byte)

     static final public byte NONE = -1;
     static final public byte BLANK_SCREEN  = 0;
     static final public byte WHOLE_SCREEN  = 1;
     static final public byte BUILD_SCREEN  = 2;
     static final public byte SPRITES_ONLY  = 3;
     static final public byte MOVE_LEVEL_UP = 4;
     static final public byte VICTORY       = 5;
     static final public byte DEFEAT        = 6;

  // Window Status
     static final public byte LOADING = 0;
     static final public byte READY   = 1;
     static final public byte ACTIVE  = 2;
     static final public byte PAUSED  = 3;
     static final public byte STOPPED = 4;

  // Total to load a level
     static final private int TOTAL_LOADING = 2000;

 /************************** DISPLAY VARIABLES *******************************/

  // Image Library
     private ImageLibrary imlib;

  // handle on the current level
     private ClientLevelProcess level;

  // Loading level current value
     private short loading_level;

  // Screen refresh mode: whole, sprites, ...
     private byte refresh_mode;

  // game screen status
     private byte gamescreen_status;

  // first screen display ?
     private boolean first_display;

  // update the quit menu zone ?
     private boolean update_quit_menu;
     
  // display pause image ?
     private boolean display_pause_image;

  // OffScreen images. offScreenLevel is the level reference, offScreenWindow
  // is the window double-buffer.
     private Image offScreenLevel;
     private Image offScreenWindow;

     private Font fscore;


 /****************************** CONSTRUCTOR  ********************************/

     public GameScreen( Frame f_scr )
     {
      // Window creation.
         super( f_scr );

         setLayout(null);
         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setBackground(C_BLACK);

      // Double-buffering image for level and Window
         offScreenLevel = createImage(LEVEL_WIDTH,LEVEL_HEIGHT);
         offScreenWindow = createImage(640,480);

      // The game screen is being loaded...
         setStatus(LOADING);
         refresh_mode = BLANK_SCREEN;    // We first need to draw a blank screen
         loading_level = 0;              // 0% of the level image has been loaded ...
         first_display=true;
         update_quit_menu=false;
         display_pause_image=false;

         fscore = new Font("Serif", Font.BOLD,30);

      // Key Listeners
           addKeyListener(new KeyAdapter()
           {
              public void keyReleased(KeyEvent e)
              {
                level.getDefaultPlayer().keyReleased( e.getKeyCode() );
              }
			
              public void keyPressed(KeyEvent e) 
              {
                level.getDefaultPlayer().keyPressed( e.getKeyCode() );
	      }
	   });

      // Image Library
         imlib = new ImageLibrary();
    }

 /******************************** DRAW SCREEN *********************************/

  // Very important : we assume that the level has been created !

    private void drawGameScreen( Graphics g )
    {
    	g.drawImage( offScreenWindow, XO, YO, this );
    	
     // The game screen is now active
        setStatus(ACTIVE);
    	refresh_mode = SPRITES_ONLY; 
    }

 /******************************** BUILD SCREEN ********************************/
     
    public void buildGameScreen()
    {
      int top_tun,state;

      Graphics offscr, offscrwin, offstone;
      Image backPic, im[];
      LevelData ldat;
      UserData udat;
      BasicSprite bspr;
      Player p;
      Tunnel tn;
      Cloud c;
      
   // *** I - SOME INITS...
   
        addLevelLoadingValue(0);
        level = VideoToons.getClientLevelProcess();
        ldat = level.getLevelConstructionData();
        udat = VideoToons.getUserData();

         if ( offScreenLevel == null )
                offScreenLevel = createImage(LEVEL_WIDTH,LEVEL_HEIGHT);

        offscr = offScreenLevel.getGraphics();
        top_tun = level.getCurrentTopTunnel();


   // *** II - PLAYER IMAGES
   
        // 2.1 - Players
           imlib.player_images = new Image[level.getNetPlayerNumber()][];

           for(byte i=0; i<level.getNetPlayerNumber(); i++ )
           {
             // we check if the image array has already been loaded
                boolean array_found=false;

           	for(byte j=0; j<i; j++)
           	   if( Tools.equal(udat.sprite_name[i],udat.sprite_name[j]) )
           	   {
           	     imlib.player_images[i] = imlib.player_images[j];
           	     array_found=true; // images found !
                     break;
           	   }
           	   
               if(!array_found)
               {
                  AnimLibrary alib = ObjectLibrary.getPlayerSpriteByName(udat.sprite_name[i]);

                  imlib.player_images[i] = ImageLibrary.loadGIFImages("graphics/players/"+udat.sprite_name[i]
                                                 +"/"+udat.sprite_name[i]+"-", alib.nb_images );
               }
           }
           
           addLevelLoadingValue(300);

        // 2.2 - Player Live
           p = level.getDefaultPlayer();
           imlib.live_image = ImageLibrary.loadImage("graphics/players/"+p.getSpriteName()+"/live.gif");
           addLevelLoadingValue(50);

        // 2.3 - Player Game Over, Victory & Defeat images...
           imlib.gameover_image = ImageLibrary.loadImage("graphics/gameover.gif");
           imlib.victory_image = ImageLibrary.loadImage("graphics/levels/"+ldat.path_name+"/victory.gif");
           imlib.defeat_image = ImageLibrary.loadImage("graphics/levels/"+ldat.path_name+"/defeat.gif");
           imlib.waiting_image = ImageLibrary.loadImage("graphics/waiting.gif");
           imlib.quit_image = ImageLibrary.loadImage("graphics/quit.jpg");
           imlib.continue_image = ImageLibrary.loadImage("graphics/continue.jpg");
           imlib.pause_image = ImageLibrary.loadImage("graphics/pause.gif");


   // *** II BIS - MONSTER IMAGES
   
        // 2bis.1 - Monsters
           for(byte i=0; i<MONSTER_NUMBER; i++ )
             for(byte k=LEFT; k<=RIGHT; k++)
             {
                if( ldat.monsters_id[i][k] == MT_NONE ) continue;

               // we check if the image array has already been loaded
                  boolean array_found=false;

                  for(byte j=0; j<=i; j++)
                  {
                    if(array_found) break;
                  
                    for(byte s=LEFT; s<=RIGHT; s++)
                    {
                      if(array_found || (j==i && k==s) ) break;
                    	
           	      if( ldat.monsters_id[j][s] == ldat.monsters_id[i][k] )
           	      {
           	         imlib.monster_images[i][k] = imlib.monster_images[j][s];
           	         array_found=true; // images found !
                         break;
           	      }
           	    }
           	  }
           	   
               if(!array_found)
               {
                  AnimLibrary alib = ObjectLibrary.getMonsterSpriteByNumber(ldat.monsters_id[i][k]);

                  imlib.monster_images[i][k] = ImageLibrary.loadGIFImages("graphics/monsters/monstr"
                                                    +ldat.monsters_id[i][k]+"/monst-", alib.nb_images );
               }
           }
           
           addLevelLoadingValue(200);

   // *** III - BACKGROUND IMAGE ( THE big image to load ... )
   
          backPic = ImageLibrary.loadImage("graphics/levels/"+ldat.path_name+"/background.jpg");
          offscr.drawImage(backPic,0,0,LEVEL_WIDTH,LEVEL_HEIGHT,this);
          addLevelLoadingValue(300);


   // *** IV - TUNNELS ...
   
      // 4.1 - We load the tunnel images
         im = ImageLibrary.loadGIFImages("graphics/levels/"+ldat.path_name
                                                  +"/tunnel", ldat.nb_tunnel_images );

      // 4.2 - We create buffered images
         BufferedImage buf_im[] = new BufferedImage[ldat.nb_tunnel_images];
      
          for(byte i=0; i<ldat.nb_tunnel_images; i++ )
          {
    	      buf_im[i] = new BufferedImage( im[i].getWidth(this),
    	                               im[i].getHeight(this),BufferedImage.TYPE_INT_ARGB );

              Graphics2D off_bf = buf_im[i].createGraphics();
              off_bf.drawImage( im[i],0,0,this );
          }

         addLevelLoadingValue(100);

      // 4.3 - we inits tunnels images and draw them
         for(byte i=0; i<TUNNEL_NUMBER; i++ )
           for(byte k=LEFT; k<=RIGHT; k++ )
           {
              if( ldat.tunnel_image_index[i][k]>=0 )
              {
                tn = level.getTunnel( i, k );
              	imlib.tunnel_images[i][k] = buf_im[ldat.tunnel_image_index[i][k]];
              	tn.initDimension(imlib.tunnel_images[i][k].getWidth(),
              	                 imlib.tunnel_images[i][k].getHeight() );

                offscr.drawImage( imlib.tunnel_images[i][k], tn.getX(), tn.getY(), this );
              }

             addLevelLoadingValue( 10 );
           }

         
   // *** V - STAGES & BLOCKS
   
      // 5.1 - First the stones backup...
         for(byte i=0; i<STAGE_NUMBER; i++ )
         {
            for(byte j=0; j<32; j++ )
              for(byte k=0; k<2; k++ )
              {
                if(k==0)
              	    state = level.getInitialStoneState(i,j);
              	else
              	    state = level.getStoneState(i,j,(byte)1);

                 if( state!=STONE_BROKEN && state!=STONE_HARD && state!=STONE_HLEFT && state!=STONE_HRIGHT)
                 {
                   // we create a backup image
                      imlib.stone_backup_image[i][j][k] = createImage( STONE_WIDTH, STONE_HEIGHT );
                      offstone = imlib.stone_backup_image[i][j][k].getGraphics();

                      offstone.drawImage(offScreenLevel, 0,0,STONE_WIDTH,STONE_HEIGHT,
                                    j*STONE_WIDTH, STAGE_HEIGHT[i]+k*STONE_HEIGHT,
                                    (j+1)*STONE_WIDTH, STAGE_HEIGHT[i]+(k+1)*STONE_HEIGHT,this);
                 }
              }
              
            addLevelLoadingValue(10);
         }

      // 5.2 - We load the stage images
         im = ImageLibrary.loadGIFImages("graphics/levels/"+ldat.path_name
                                                          +"/stage", ldat.nb_stage_images );

      // 5.3 - We draw the stages
         for(byte i=0; i<STAGE_NUMBER; i++)
         {
           if(ldat.stage_image_index[i]>=0){
             imlib.stage_images[i] = im[ldat.stage_image_index[i]];
             offscr.drawImage( imlib.stage_images[i],0,STAGE_HEIGHT[i],this);
           }

           // we erase all the pre_destroyed stones as specified
              if(ldat.pre_destroyed_stones[i])
                 for(byte j=0; j<STONE_NUMBER; j++ )
                    if( level.getInitialStoneState(i,j)!=level.getStoneState(i,j,(byte)0) )
                         offscr.drawImage( imlib.stone_backup_image[i][j][0],
                                           j*STONE_WIDTH, STAGE_HEIGHT[i], this );

           addLevelLoadingValue( 50 );
         }

      // 5.4 - We load and draw the blocks
         im = ImageLibrary.loadGIFImages("graphics/levels/"+ldat.path_name
                                                          +"/block", ldat.nb_block_images );
         for(byte i=0; i<BLOCK_NUMBER; i++)
           if(ldat.block_image_index[i]>=0){
             Block bl = level.getStageBlock(i);

             offscr.drawImage( im[ldat.block_image_index[i]],bl.getX(),bl.getY(),this);
           }
         
         addLevelLoadingValue( 50 );


   // *** VI - FIRST GAME SCREEN CREATION
   
       // 6.1 - Screen Image
           if ( offScreenWindow == null )
                offScreenWindow = createImage(640,480);
        
           offscrwin = offScreenWindow.getGraphics();

           offscrwin.drawImage( offScreenLevel,0,0,640,480, 0, STAGE_HEIGHT[top_tun],
                                640, STAGE_HEIGHT[top_tun-4]+20, this );

        // 6.2 - We load the cloud images
           imlib.transparent_cloud = ldat.transparent_cloud;
           buf_im = new BufferedImage[ldat.nb_cloud_images];
           im = ImageLibrary.loadGIFImages("graphics/levels/"+ldat.path_name
                                           +"/cloud", ldat.nb_cloud_images );

        // 6.3 - We create buffered images
           for(byte i=0; i<ldat.nb_cloud_images; i++ )
           {
    	      buf_im[i] = new BufferedImage( im[i].getWidth(this),
    	                      im[i].getHeight(this),BufferedImage.TYPE_INT_ARGB );

              Graphics2D off_bf = buf_im[i].createGraphics();
              off_bf.drawImage( im[i],0,0,this );
           }

           addLevelLoadingValue(80);

        // 6.4 - We init the clouds dimensions and image array
           for(byte i=0; i<CLOUD_NUMBER; i++ )          
              if(ldat.cloud_image_index[i]>=0){
              	    imlib.cloud_images[i] = buf_im[ldat.cloud_image_index[i]];
                    c = level.getStageCloud(i);
              	    c.initDimension(imlib.cloud_images[i].getWidth(),imlib.cloud_images[i].getHeight() );
              }

        // 6.5 - We draw the clouds
           for(byte i=0; i<=top_tun; i++ )
           {
              c = level.getStageCloud(i);
           	
              if(c==null)
                 continue;

              if(imlib.transparent_cloud[i]==true)
              {
                  Graphics2D g2 = (Graphics2D) offscrwin;

                  g2.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5) );
                  g2.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-STAGE_HEIGHT[top_tun],this);

                  g2.setComposite( AlphaComposite.SrcOver );
               }
               else
                   offscrwin.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-STAGE_HEIGHT[top_tun],this);
           }

        // 6.6 - We draw the players ...
           for(byte i=0; i<level.getNetPlayerNumber(); i++ )
           {
              bspr = level.getNetPlayer(i);

              offscrwin.drawImage( imlib.player_images[i][bspr.getCurrentImageIndex()],
                                   bspr.getX(),bspr.getY()-STAGE_HEIGHT[top_tun],this);
           }

           addLevelLoadingValue( 50 );

       // 6.7 - Monsters
           for(byte i=0; i<=top_tun; i++ )
             for(byte k=LEFT; k<=RIGHT; k++)
             {
               bspr = level.getMonster(i,k);
               if(bspr==null) continue;

               offscrwin.drawImage( imlib.monster_images[i][k][bspr.getCurrentImageIndex()],
                                   bspr.getX(),bspr.getY()-STAGE_HEIGHT[top_tun],this);

                  if(bspr.getStage()<TUNNEL_NUMBER)
                  {
                      tn = level.getTunnel( bspr.getStage(),bspr.getSide() );
                      
                      if(tn != null)
                      {
                        Rectangle r = tn.getTunnelRedrawZone( bspr );

                         if(r!=null)
                         {
                           BufferedImage bf_i = imlib.tunnel_images[bspr.getStage()][tn.getSide()].getSubimage(r.x-tn.getX(),
                                                                                     r.y-tn.getY(),r.width,r.height);
                           offscrwin.drawImage(bf_i,r.x,r.y-STAGE_HEIGHT[top_tun],this);
                         }
                      }
                  }
             }

       // 6.8 - The lives
          for(byte i=0; i<p.getLives(); i++ )
              offscrwin.drawImage(imlib.live_image,10+i*25,10,this);

       // 6.9 - The Score
          offscrwin.setFont(fscore);
          offscrwin.setColor(C_BLACK);
          offscrwin.drawString(""+level.getDefaultPlayerScore(),21,62);              
          offscrwin.setColor(C_WHITE);
          offscrwin.drawString(""+level.getDefaultPlayerScore(),20,60);

       // 6.10 - "Waiting other players" image if needed
          if( udat.game_mode != ONEPLAYER )
             offscrwin.drawImage( imlib.waiting_image, 220, 220, this );


          addLevelLoadingValue( 50 );


   // *** VII - THE GAME SCREEN IS READY TO BE DRAWN
   
      level.removeLevelConstructionData();
      setStatus(READY);      
   }


 /*********************** TO TRANSLATE THE GAME SCREEN ***********************/
 
    public void translateGameScreen( Graphics g, byte cur_top_stage )
    {
       short y, y1, y2, dy;
       Graphics offscrwin = offScreenWindow.getGraphics();
       long t;
       Player p = level.getDefaultPlayer();
       Cloud c;

       byte gm_status = VideoToons.getGameStatus();
       
       y1 = STAGE_HEIGHT[cur_top_stage];
       
         if( cur_top_stage <9 )
         {
            y2 = STAGE_HEIGHT[cur_top_stage+1];
            dy = 10;
            level.setCurrentTopTunnel( ++cur_top_stage );
         }
         else if( cur_top_stage == 9 )
         {
            y2 = 0;
            dy = 20;
            level.setCurrentTopTunnel( (byte)12 );       	    
         }
         else
            return;

 
     // Beginning of the display loop
         y=y1;
       
         while(y!=y2)
         {
              t = System.currentTimeMillis();
       	
       	   // Level position upgrade
       	      y-=dy;

       	       if( y<y2 )  y=y2;
       	
       	   // Base screen level
              offscrwin.drawImage(offScreenLevel, 0,0,640,480, 0,y,640,y+480, this );

           // We draw the clouds
              for( byte i=0; i<CLOUD_NUMBER; i++ )
              {
              	c=level.getStageCloud(i);
              	
                 if( c==null || c.getY()-y>=480 || c.getY()-y<0 )
                    continue;

                  if(imlib.transparent_cloud[i]==true)
                  {
                     Graphics2D g2 = (Graphics2D) offscrwin;
 
                     g2.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5) );
                     g2.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-y,this);

                     g2.setComposite( AlphaComposite.SrcOver );
                  }
                  else
                     offscrwin.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-y,this);
 
                  if(i<TUNNEL_NUMBER)
                  {
                      Tunnel tn = level.getTunnel( i,c.getSide() );
                      
                      if(tn != null)
                      {
                        Rectangle r = tn.getTunnelRedrawZone( c );
                   	
                         if(r!=null)
                         {
                           BufferedImage bf_i = imlib.tunnel_images[i][tn.getSide()].getSubimage(r.x-tn.getX(),
                                                                                     r.y-tn.getY(),r.width,r.height);
                           offscrwin.drawImage(bf_i,r.x,r.y-y,this);
                         }
                      }
                  }
              }

           // we redraw every object
              for(byte i=0; i<level.getNetPlayerNumber(); i++ )
              {
                 BasicSprite bspr = level.getNetPlayer(i);

                 if(bspr.isVisible())
                     offscrwin.drawImage( imlib.player_images[i][bspr.getCurrentImageIndex()],
                                          bspr.getX(),bspr.getY()-y,this);
              }

           // monsters
              for(byte i=0; i<MONSTER_NUMBER; i++ )
                for(byte k=LEFT; k<=RIGHT; k++)
                {
                   BasicSprite bspr = level.getMonster(i,k);
                   if(bspr==null || !bspr.isVisible() || bspr.getY()-y>480 || bspr.getY()-y<0)
                     continue;

                   offscrwin.drawImage( imlib.monster_images[i][k][bspr.getCurrentImageIndex()],
                                        bspr.getX(),bspr.getY()-y,this);

                  if(0<=bspr.getStage() && bspr.getStage()<TUNNEL_NUMBER)
                  {
                      Tunnel tn = level.getTunnel( bspr.getStage(),bspr.getSide() );
                      
                      if(tn != null)
                      {
                        Rectangle r = tn.getTunnelRedrawZone( bspr );

                         if(r!=null)
                         {
                           BufferedImage bf_i = imlib.tunnel_images[bspr.getStage()][tn.getSide()].getSubimage(r.x-tn.getX(),
                                                                                     r.y-tn.getY(),r.width,r.height);
                           offscrwin.drawImage(bf_i,r.x,r.y-y,this);
                         }
                      }
                  }

                }

           // Then lives & score...
              for( byte i=0; i<p.getLives(); i++ )
                  offscrwin.drawImage(imlib.live_image,10+i*25,10,this);
              
           // The Score
              offscrwin.setFont(fscore);
              offscrwin.setColor(C_BLACK);    
              offscrwin.drawString(""+level.getDefaultPlayerScore(),21,62);
              offscrwin.setColor(C_WHITE);    
              offscrwin.drawString(""+level.getDefaultPlayerScore(),20,60);

           // Game Over ?
              if(p.getStatus()==BS_GAMEOVER)
                 offscrwin.drawImage( imlib.gameover_image,255,220,this );

           // Pause Message
              if( gm_status==GM_PAUSE )
                 offscrwin.drawImage( imlib.pause_image,290,225,this );

           // QUIT MENU
              if(p.wantsQuitMenu())
              {
                Graphics2D g2 = (Graphics2D) offscrwin;
                Image im_menu;

                if(p.isQuitSelected())
                   im_menu = imlib.quit_image;
                else
                   im_menu = imlib.continue_image;
 
                 g2.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.65) );
                 g2.drawImage(im_menu,240,200,this);
                 g2.setComposite( AlphaComposite.SrcOver );
              }

           // ... and print the result to the screen
              g.drawImage( offScreenWindow, XO, YO, this );

           // we wait a little if we were too fast ...
              while( ( System.currentTimeMillis() - t ) < 30 );
         }
       
      // final redraw
         redrawSprites( g );

      // New refresh mode
         refresh_mode = SPRITES_ONLY;
    }

 
  /******************************** REDRAWING ********************************/
  /********************************  SPRITES  ********************************/
  /********************************   ONLY!   ********************************/
  
   private void redrawSprites( Graphics g )
   {
      short x,y;
      Rectangle melt, new_rect;
      Graphics offscr,offscrwin;
      Graphics2D off_b_s;
      Player p = level.getDefaultPlayer();
      BasicSprite bspr;
      BrokenStone b_s;
      Cloud c;
      Image i_tmp;
      boolean quit_menu_repainted = false;
      
      byte gm_status = VideoToons.getGameStatus();
      
      byte top_tun1, top_tun2, top = level.getCurrentTopTunnel();
      short y_top = STAGE_HEIGHT[top];
      short y_top0 = (short)(YO - y_top);

        offscrwin = offScreenWindow.getGraphics();
        offscr = offScreenLevel.getGraphics();

        if(top==12){
          top_tun1 = 8;
          top_tun2 = 10;
        }
        else{
          top_tun1 = (byte)(top-4);
          top_tun2 = top;
        }
        
   // *** * * * * * * * * * * * * * * * * * * * * * * * * * * * * *** //
   // *** I - WE ERASE ALL THE OLD SPRITES FROM THE DOUBLE-BUFFER *** //
   // *** * * * * * * * * * * * * * * * * * * * * * * * * * * * * *** //

      // * 1.1 - PLAYERS - BUFFER CLEANING -
         for(byte i=0; i<level.getNetPlayerNumber(); i++ )
         {
           melt = level.getNetPlayer(i).getOldRectangle();

           offscrwin.drawImage( offScreenLevel, melt.x, melt.y-y_top, melt.x+melt.width,
                                melt.y+melt.height-y_top, melt.x, melt.y, melt.x+melt.width,
                               melt.y+melt.height, this );
         }

      // * 1.2 - MONSTERS - BUFFER CLEANING -
           for(byte i=top_tun1; i<MONSTER_NUMBER; i++ )
             for(byte k=LEFT; k<=RIGHT; k++)
                {
                   bspr = level.getMonster(i,k);
                   if(bspr==null) continue;

                   melt = bspr.getOldRectangle();

                   // monster on screen ?
                   if(melt.y+melt.height<y_top) continue;

                   offscrwin.drawImage( offScreenLevel, melt.x, melt.y-y_top, melt.x+melt.width,
                                    melt.y+melt.height-y_top, melt.x, melt.y, melt.x+melt.width,
                                    melt.y+melt.height, this );
                }

      // * 1.3 - CLOUDS - BUFFER CLEANING -
         for(byte i=top_tun1; i<=top_tun2; i++ )
         {
            c = level.getStageCloud(i); 

             if(c!=null)
             {
               melt = c.getOldRectangle();

               offscrwin.drawImage( offScreenLevel,melt.x, melt.y-y_top,
                            melt.x+melt.width, melt.y+melt.height-y_top,
                            melt.x, melt.y, melt.x+melt.width, melt.y+melt.height, this );
              }
          }
          
      // * 1.4 - BROKEN STONES - BUFFER CLEANING -
         byte n=0;
         b_s = level.getBrokenStone((byte)0);

         while( b_s != null )
         {
             x = b_s.getX();
             y = b_s.getY();

             if( b_s.firstDisplay() == true )
             {
               // We init the destruction of the stone.
               // For that we first copy the original stone image
                  BufferedImage im_b_s = new BufferedImage(STONE_WIDTH, STONE_HEIGHT,
                                                           BufferedImage.TYPE_INT_ARGB );

                  off_b_s = im_b_s.createGraphics();

                  off_b_s.drawImage( imlib.stage_images[b_s.getLevel()],
                                     0,0, STONE_WIDTH, STONE_HEIGHT,
                                     x, b_s.getLine()*STONE_HEIGHT,
                                     x+STONE_WIDTH, (b_s.getLine()+1)*STONE_HEIGHT,
                                     this );

                  b_s.setImage( im_b_s );
                  i_tmp = imlib.stone_backup_image[b_s.getLevel()][b_s.getStone()][b_s.getLine()];

               // - we erase this stone from the offscreenLevel
                  offscr.drawImage( i_tmp, x, y, this );

               // we erase this stone on offscreenWindow
                  offscrwin.drawImage( i_tmp, x, y-y_top, this );            	
             }
             else
                 offscrwin.drawImage( offScreenLevel,
                       x+b_s.getCorX1(), y-y_top+b_s.getCorY1(),
                       x+b_s.getWidth()+b_s.getCorX2(), y-y_top+b_s.getHeight()+b_s.getCorY2(),
                       x+b_s.getCorX1(), y+b_s.getCorY1(),
                       x+b_s.getWidth()+b_s.getCorX2(), y+b_s.getHeight()+b_s.getCorY2(), this );

           // next stone ...
              n++;
              b_s = level.getBrokenStone(n);
         }


      // * 1.5 - LIVES & SCORE - BUFFER CLEANING -
         offscrwin.drawImage( offScreenLevel,10,10,85,35,10,10+y_top,85,35+y_top,this );       
         offscrwin.drawImage( offScreenLevel,15,40,86,77,15,40+y_top,86,77+y_top,this );

      // * 1.6 - GAME OVER MESSAGE ? -
           if(p.getStatus()==BS_GAMEOVER)
                 offscrwin.drawImage( offScreenLevel,255,220,385,260,255,220+y_top,385,260+y_top,this );

      // * 1.7 - WAITING OTHER PLAYERS IMAGE ? CLEANING
          if( first_display )
             offscrwin.drawImage( offScreenLevel,220,220,420,250,220,220+y_top,420,250+y_top,this );

      // * 1.8 - PAUSE IMAGE CLEANING
          if( gm_status==GM_PAUSE || display_pause_image )
             offscrwin.drawImage( offScreenLevel,290,225,345,255,290,225+y_top,345,255+y_top,this );


      // * 1.9 - QUIT MENU CLEANING
          if( p.wantsQuitMenu() || update_quit_menu )
             offscrwin.drawImage( offScreenLevel,240,200,400,280,240,200+y_top,400,280+y_top,this );

      // * 1.10 - FILLING HOLES...
          n=0;
          FilledHole fh = level.getFilledHole((byte)0);

          while( fh != null )
          {
             // we make the broken stone reappear on both offscreenlevel & offscreenwindow
                  offscr.drawImage( imlib.stage_images[fh.getStage()],
                                     fh.getStone()*STONE_WIDTH, STAGE_HEIGHT[fh.getStage()],
                                     (fh.getStone()+1)*STONE_WIDTH,
                                     STAGE_HEIGHT[fh.getStage()]+STONE_HEIGHT,
                                     fh.getStone()*STONE_WIDTH, 0,
                                     (fh.getStone()+1)*STONE_WIDTH, STONE_HEIGHT,
                                     this  );

                  offscrwin.drawImage( imlib.stage_images[fh.getStage()],
                                     fh.getStone()*STONE_WIDTH, STAGE_HEIGHT[fh.getStage()]-y_top,
                                     (fh.getStone()+1)*STONE_WIDTH,
                                     STAGE_HEIGHT[fh.getStage()]+STONE_HEIGHT-y_top,
                                     fh.getStone()*STONE_WIDTH, 0,
                                     (fh.getStone()+1)*STONE_WIDTH, STONE_HEIGHT,
                                     this  );

           // next hole ...
              n++;
              fh = level.getFilledHole(n);
          }

  // *** * * * * * * * * * * * * * * * * * * * * * * * * * * *** //
  // *** II - WE REDRAW ALL THE SPRITES ON THE DOUBLE-BUFFER *** //
  // *** * * * * * * * * * * * * * * * * * * * * * * * * * * *** //
   
      // * 2.1 - CLOUDS - BUFFER UPDATE -

           for(byte i=top_tun1; i<=top_tun2; i++ )
           {
              c = level.getStageCloud(i);
              	
              if( c==null )
                    continue;

                 if(imlib.transparent_cloud[i]==true)
                 {
                     Graphics2D g2 = (Graphics2D) offscrwin;
 
                     g2.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5) );
                     g2.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-y_top,this);

                     g2.setComposite( AlphaComposite.SrcOver );
                 }
                 else
                     offscrwin.drawImage(imlib.cloud_images[i],c.getX(),c.getY()-y_top,this);

                 if(i<TUNNEL_NUMBER)
                 {
                    Tunnel tn = level.getTunnel( i,c.getSide() );
                      
                    if(tn != null)
                    {
                        Rectangle r = tn.getTunnelRedrawZone( c );
         	
                        if(r!=null){
                          BufferedImage bf_i = imlib.tunnel_images[i][tn.getSide()].getSubimage(r.x-tn.getX(),
                                                                                    r.y-tn.getY(),r.width,r.height);
                          offscrwin.drawImage(bf_i,r.x,r.y-y_top,this);
                        }
                     }
                 }
           }


      // * 2.2 - MONSTERS - BUFFER UPDATE -
         for(byte i=top_tun1; i<MONSTER_NUMBER; i++ )
            for(byte k=LEFT; k<=RIGHT; k++)
            {
               bspr = level.getMonster(i,k);
               if(bspr==null || !bspr.isVisible() || bspr.getY()-y_top>=480 || bspr.getY()-y_top<0)
                   continue;

               offscrwin.drawImage( imlib.monster_images[i][k][bspr.getCurrentImageIndex()],
                                    bspr.getX(),bspr.getY()-y_top, this);

                  if(bspr.getStage()<TUNNEL_NUMBER && bspr.getStage()>=0)
                  {
                      Tunnel tn = level.getTunnel( bspr.getStage(),bspr.getSide() );

                      if(tn != null)
                      {
                        Rectangle r = tn.getTunnelRedrawZone( bspr );

                         if(r!=null)
                         {
                           BufferedImage bf_i = imlib.tunnel_images[bspr.getStage()][tn.getSide()].getSubimage(r.x-tn.getX(),
                                                                                     r.y-tn.getY(),r.width,r.height);
                           offscrwin.drawImage(bf_i,r.x,r.y-y_top,this);
                         }
                      }
                  }
            }


      // * 2.3 - PLAYERS - BUFFER UPDATE -
      
         for(byte i=0; i<level.getNetPlayerNumber(); i++ )
         {
            bspr = level.getNetPlayer(i);

            if(bspr.isVisible())
                offscrwin.drawImage( imlib.player_images[i][bspr.getCurrentImageIndex()],
                                     bspr.getX(),bspr.getY()-y_top,this);
         }

      // * 2.4 - BROKEN STONES - BUFFER UPDATE -
         n=0;
         b_s = level.getBrokenStone((byte)0);
         
          while( b_s != null )
          {
            // affine transformation
               BufferedImage im_b_s = b_s.getImage();
               Graphics2D g2 = (Graphics2D) offscrwin;

                b_s.updateSpritePosition();

            // rectangle stone display
               if( b_s.shouldEnd() == false )
                  g2.drawImage( im_b_s, b_s.getTransform( b_s.getY()-y_top ) ,this );

               n++;
               b_s = level.getBrokenStone(n);
          }

      // * 2.5 - LIVES & SCORE - BUFFER UPDATE -
          for( byte i=0; i<p.getLives(); i++ )
              offscrwin.drawImage(imlib.live_image,10+i*25,10,this);

          offscrwin.setFont(fscore);
          offscrwin.setColor(C_BLACK);    
          offscrwin.drawString(""+level.getDefaultPlayerScore(),21,62);
          offscrwin.setColor(C_WHITE);    
          offscrwin.drawString(""+level.getDefaultPlayerScore(),20,60);

      // * 2.6 - GAME OVER MESSAGE ? - BUFFER UPDATE -
          if(p.getStatus()==BS_GAMEOVER)
              offscrwin.drawImage( imlib.gameover_image,255,220,this );

      // * 2.7 - PAUSE IMAGE - BUFFER UPDATE
          if( gm_status==GM_PAUSE )
             offscrwin.drawImage( imlib.pause_image,290,225,this );

      // * 2.8 - QUIT MENU - BUFFER UPDATE -
          if(p.wantsQuitMenu())
          {
               Graphics2D g2 = (Graphics2D) offscrwin;
               Image im_menu;
               quit_menu_repainted=true;

               if(p.isQuitSelected())
                  im_menu = imlib.quit_image;
               else
                  im_menu = imlib.continue_image;
 
               g2.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.65) );
               g2.drawImage(im_menu,240,200,this);

               g2.setComposite( AlphaComposite.SrcOver );
           }


  // *** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *** //
  // *** III - WE CAN NOW UPGRADE THE SCREEN PARTS THAT HAVE CHANGED *** //
  // *** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *** //
  
      // * 3.1 - PLAYERS - SCREEN UPDATE -
         for(byte i=0; i<level.getNetPlayerNumber(); i++ )
         {
            bspr = level.getNetPlayer(i);
            new_rect = bspr.getRectangle();
            melt = bspr.getOldRectangle();

           // rectangles should fit the screen
              Tools.fitOnScreen( new_rect );

              if(melt.y-y_top<0) {
                   melt.height += melt.y-y_top;
                   melt.y = y_top;

                   if(melt.height<0)
                       melt.height = 0;
              }

           // do we have to update the rectangles zone separetly ?
              if( melt.intersects(new_rect) == true ) {
                  melt.add( new_rect );    // we melt the two rectangles
              }
              else
                  g.drawImage( offScreenWindow, XO+new_rect.x, y_top0+new_rect.y,
                               XO+new_rect.x+new_rect.width, y_top0+new_rect.y+new_rect.height,
                               new_rect.x, new_rect.y-y_top, new_rect.x+new_rect.width,
                               new_rect.y+new_rect.height-y_top, this );

              g.drawImage( offScreenWindow, 
                           XO+melt.x, y_top0+melt.y, XO+melt.x+melt.width,
                           y_top0+melt.y+melt.height, melt.x, melt.y-y_top,
                           melt.x+melt.width, melt.y+melt.height-y_top, this );

           // back-up rectangle for next turn
              bspr.setOldRectangle( new_rect );
         }

      // * 3.2 - MONSTERS - SCREEN UPDATE -
           for(byte i=top_tun1; i<MONSTER_NUMBER; i++ )
             for(byte k=LEFT; k<=RIGHT; k++)
             {
                   bspr = level.getMonster(i,k);

                   if(bspr==null )
                      continue;
                   
                   new_rect = bspr.getRectangle();
                   melt = bspr.getOldRectangle();

                   if( (melt.y-y_top>=480 && new_rect.y-y_top>=480) || new_rect.y-y_top<0)
                       continue;

                // rectangles should fit the screen
                   Tools.fitOnScreen( new_rect );

                    if(melt.y-y_top<0) {
                       melt.height += melt.y-y_top;
                       melt.y = y_top;

                       if(melt.height<0)
                          melt.height = 0;
                   }

                // do we have to update the rectangles zone separetly ?
                   if( melt.intersects(new_rect) == true ) {
                       melt.add( new_rect );    // we melt the two rectangles
                   }
                   else
                       g.drawImage( offScreenWindow, XO+new_rect.x, y_top0+new_rect.y,
                               XO+new_rect.x+new_rect.width, y_top0+new_rect.y+new_rect.height,
                               new_rect.x, new_rect.y-y_top, new_rect.x+new_rect.width,
                               new_rect.y+new_rect.height-y_top, this );

                   g.drawImage( offScreenWindow, 
                           XO+melt.x, y_top0+melt.y, XO+melt.x+melt.width,
                           y_top0+melt.y+melt.height, melt.x, melt.y-y_top,
                           melt.x+melt.width, melt.y+melt.height-y_top, this );

                // back-up rectangle for next turn
                   bspr.setOldRectangle( new_rect );
             }


      // * 3.3 - CLOUDS - SCREEN UPDATE -
             for(byte i=top_tun1; i<=top_tun2; i++ )
             {
              	c = level.getStageCloud(i);
              	
                 if(c!=null)
                 {
                    melt = c.getRectangle();

                      if( c.getSpeed()<0 )
                         melt.width -= c.getSpeed();
                      else {
                          melt.x -= c.getSpeed();
                          melt.width += c.getSpeed();
                      }

                   // rectangles should fit the screen
                     Tools.fitOnScreen( melt );

                   // do we have to update the rectangles zone separetly ?
                     if( melt.width != 0 )
                       g.drawImage( offScreenWindow, XO+melt.x, y_top0+melt.y,
                           XO+melt.x+melt.width, y_top0+melt.y+melt.height,
                           melt.x, melt.y-y_top, melt.x+melt.width,
                           melt.y+melt.height-y_top, this );

                  // back-up rectangle for next turn
                     c.setOldRectangle( melt );
                 }
             }

      // * 3.4 - BROKEN STONES - SCREEN UPDATE -
         n=0;
         b_s = level.getBrokenStone((byte)0);
         
           while( b_s != null )
           {
             // rectangle stone display
                melt = b_s.getOldRectangle();
                melt.add( b_s.getRectangle() );
                
                g.drawImage( offScreenWindow, XO + melt.x+b_s.getCorX1(),
                           y_top0 + melt.y+b_s.getCorY1(),
                           XO + melt.x + melt.width+b_s.getCorX2(),
                           y_top0 + melt.y + melt.height +b_s.getCorY2(),
                           melt.x+b_s.getCorX1(), melt.y+b_s.getCorY1() -y_top,
                           melt.x + melt.width + b_s.getCorX2(),
                           melt.y + melt.height +b_s.getCorY2() -y_top, this);

                 if( b_s.shouldEnd() == false) {
                      n++;                    	
                      b_s.updateOldRectangle();
                      b_s = level.getBrokenStone(n);                      
                 }
                 else
                      b_s = level.eraseBrokenStone(n);
           }


      // * 3.5 - LIVES & SCORE - SCREEN UPDATE -
         g.drawImage( offScreenWindow,XO+10,YO+10,XO+85,YO+35,10,10,85,35,this );
         g.drawImage( offScreenWindow,XO+15,YO+40,XO+86,YO+77,15,40,86,77,this );            

      // * 3.6 - GAME OVER MESSAGE ? -
           if(p.getStatus()==BS_GAMEOVER)
               g.drawImage( offScreenWindow,XO+255,YO+220,XO+385,YO+260,255,220,385,260,this );

      // * 3.7 - WAITING OTHER PLAYERS IMAGE - SCREEN UPDATE -
          if( first_display ) {
             first_display = false;
             g.drawImage( offScreenWindow,XO+220,YO+220,XO+420,YO+250,220,220,420,250,this );
          }

      // * 3.8 - PAUSE IMAGE - SCREEN UPDATE -
          if( gm_status==GM_PAUSE || display_pause_image ) {
             g.drawImage( offScreenWindow,XO+290,YO+225,XO+345,YO+255,290,225,345,255,this );

             if(gm_status==GM_PAUSE)  display_pause_image = true;
          }

      // * 3.9 - QUIT MENU - SCREEN UPDATE -
          if( p.wantsQuitMenu() || update_quit_menu ) {
             g.drawImage( offScreenWindow,XO+240,YO+200,XO+400,YO+280,240,200,400,280,this );

             if(quit_menu_repainted==true)
                update_quit_menu =true;
             else if(p.wantsQuitMenu()==false)
                update_quit_menu = false;
          }

      // * 3.10 - FILLING HOLES... - SCREEN UPDATE
          n=0;
          fh = level.getFilledHole((byte)0);

          while( fh != null )
          {
             // we make the broken stone reappear on both offscreenlevel & offscreenwindow
                if(top_tun1<=fh.getStage() && fh.getStage()<=top_tun2)
                  g.drawImage( offScreenWindow,
                               fh.getStone()*STONE_WIDTH +XO,
                               STAGE_HEIGHT[fh.getStage()] +y_top0,
                               (fh.getStone()+1)*STONE_WIDTH +XO,
                               STAGE_HEIGHT[fh.getStage()]+STONE_HEIGHT +y_top0,
                               fh.getStone()*STONE_WIDTH,
                               STAGE_HEIGHT[fh.getStage()] -y_top,
                               (fh.getStone()+1)*STONE_WIDTH,
                               STAGE_HEIGHT[fh.getStage()]+STONE_HEIGHT -y_top,
                               this  );

           // next hole ...
              n++;
              fh = level.getFilledHole(n);
          }

          level.eraseFilledHoles();

      //*** END ***//
    }

 
 /**************************** TO ZOOM AN IMAGE ******************************/
 
    private void zoomResultImage( Image im, Graphics g )
    {
      Graphics  offscrwin = offScreenWindow.getGraphics();

       short imwidth = (short) im.getWidth(this);
       short imheight = (short) im.getHeight(this);
       short y_top = (short) STAGE_HEIGHT[level.getCurrentTopTunnel()];

       for(byte i=0; i<=10; i++)       
       {
         long t = System.currentTimeMillis();

         short x1 = (short)( ( 640 - (imwidth*i)/10 )/2 );
         short y1 = (short)( ( 110 - (imheight*i)/10 )/2 );

         // cleaning
         offscrwin.drawImage( offScreenLevel, x1, y1, x1+(imwidth*i)/10, y1+(imheight*i)/10,
                                              x1, y1+y_top, x1+(imwidth*i)/10, y1+y_top+(imheight*i)/10, this );

         // update
         offscrwin.drawImage( im, x1, y1, (imwidth*i)/10, (imheight*i)/10, this );

         // drawing
         g.drawImage( offScreenWindow, XO+x1, YO+y1, XO+x1+(imwidth*i)/10, YO+y1+(imheight*i)/10,
                                x1, y1, x1+(imwidth*i)/10, y1+(imheight*i)/10, this );

         // we wait a little if we were too fast ...
            while( ( System.currentTimeMillis() - t ) < 30 );
       }

    // we wait a few sec and wake the VideoToons thread
       if( refresh_mode==DEFEAT )
           Tools.waitTime(6000);
       else
           Tools.waitTime(9000);

    // New refresh mode
       refresh_mode = BLANK_SCREEN;
    }
 
 /************************* TO CHECK LOADING STATUS **************************/
 
    public void addLevelLoadingValue( int val )
    {
       StatusWindow sw = StatusWindow.getDefault();

       loading_level += val;

       sw.setValue(  (double) loading_level/TOTAL_LOADING );
    }
    

 /******************************** REFRESH MODE ******************************/

  // To change what paint() does ...

    synchronized public void setRefreshMode( byte r_mode )
    {
       refresh_mode = r_mode;
    }


 /**************************** TO CONVERT COORDINATES ************************/
   
   public short yLevelToScreen( short y_level )
   {
      return (short)( y_level - STAGE_HEIGHT[level.getCurrentTopTunnel()] );
   }
   
   public short yScreentoLevel( short y_level )
   {
      return (short) ( y_level + STAGE_HEIGHT[level.getCurrentTopTunnel()] );
   }  
       
 /********************************* DRAW CALLS *******************************/

  // Paint function.

    synchronized public void paint(Graphics g)
    {
        switch( refresh_mode )
        {
            case SPRITES_ONLY: // we redraw only sprites
                                  redrawSprites( g );
                                  break;
                               
            case WHOLE_SCREEN: // we refresh the whole screen.
                                  drawGameScreen( g );
                                  break;

            case MOVE_LEVEL_UP: // Translate the level of one stage
                                   translateGameScreen( g, level.getCurrentTopTunnel() );
                                   break;

            case BUILD_SCREEN: // Initial mode ... we construct the whole screen.
                                  buildGameScreen();
                                  refresh_mode = NONE;
                                  break; 
                               
            case BLANK_SCREEN: // for clean transitions
                                  g.setColor( C_BLACK );
                                  g.fillRect( 0, 0, SCREEN_WIDTH,SCREEN_HEIGHT );
                                  refresh_mode = BUILD_SCREEN;
                                  break;
                                  
            case VICTORY :     // this client is victorious !!!
                                  zoomResultImage( imlib.victory_image, g );
                                  break;

            case DEFEAT :      // ahh... we have lost !!!
                                  zoomResultImage( imlib.defeat_image, g );
                                  break;

            case NONE:         /* no update */
                                  break;
        }

    }


 /***************************** NO FLICKS ! *******************************/

   // To avoid any flicks we redefine these methods ...

      public void repaint()
      {
          paint(getGraphics());
      }	
	
      public void update(Graphics g)
      {
          paint(g);
      }

 /************************** GAME SCREEN STATUS ***************************/

      synchronized byte getStatus(){
         return	gamescreen_status;
      }

      synchronized void setStatus( byte status ){
         gamescreen_status = status;
      }
      
 /************************* IMAGE LIBRARY ACCESS **************************/

      public short getPlayerImageWidth( byte player_id, short image_index ){
         return (short) imlib.player_images[player_id][image_index].getWidth(this);
      }

      public short getPlayerImageHeight( byte player_id, short image_index ){
         return (short) imlib.player_images[player_id][image_index].getHeight(this);
      }

}


