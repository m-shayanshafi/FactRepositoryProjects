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

package videotoons.swing;

import videotoons.game.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class ScoreWindow extends JWindow implements GameDefinitions
{

  private Image back;
  private Image players[] = new Image[NBMAX_PLAYERS];

  private JPanel j_drawzone;
  private UserData user_dat;

 // score sorted index 
   private int index[];

 // podium positions
   private int pod_x[] = { 8, 106, 204, 302 };
   private int pod_y[] = { 120, 135, 150, 170 };

   private byte PODIUM_WIDTH = 90;
   private int  SCORE_YPOS = 192;
   
   private Font f20, f25;
   private Color c_text, c_shadow;

    public ScoreWindow( Frame frame, UserData user_data )
    {
       super(frame);
       this.user_dat = user_data;

         f20 = new Font("Dialog", Font.BOLD, 16);
         f25 = new Font("Dialog", Font.BOLD, 21);
         c_text = new Color(180,110,40);
         c_shadow = new Color(50,0,0);

      // We load the images
         Image im[] = new Image[8];
         ImageIcon imi[] = new ImageIcon[8];

         MediaTracker mediaTracker = new MediaTracker(this);

         if( user_dat.game_mode == ONEPLAYER || !user_dat.is_server_host)
             back = getToolkit().getImage("Graphics/back7.jpg");
         else
             back = getToolkit().getImage("Graphics/back8.jpg");

         mediaTracker.addImage(back,0);

         for(int i=0;i<8;i++){
           im[i] = getToolkit().getImage("Graphics/scor"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }
         
         for(int i=0;i<8;i++)
          imi[i] = new ImageIcon(im[i]);

      // Window properties
         getContentPane().setLayout(null);
         getContentPane().setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         getContentPane().setBackground(C_BLACK);
         setBackground(C_BLACK);

      // Jpanel
          JPanel jp = new JPanel()
          {
             public void paintComponent(Graphics g){
                g.drawImage(back,0,0,this);
             }
          };

         jp.setLayout(null);
         jp.setBounds(XO,YO,640,480);
         jp.setBackground(C_BLACK);
         getContentPane().add(jp);

      // Ok button
         JButton b_ok = null;
         
         if( user_dat.game_mode == ONEPLAYER || !user_dat.is_server_host) {
                 b_ok = new JButton(imi[1]);
                 b_ok.setBounds(280,420,80,30);
                 b_ok.setRolloverIcon(imi[4]);
                 b_ok.setPressedIcon(imi[4]);
         }
         else {
                 b_ok = new JButton(imi[2]);
                 b_ok.setBounds(380,420,140,30);
                 b_ok.setRolloverIcon(imi[5]);
                 b_ok.setPressedIcon(imi[5]);

                 JButton b_replay = new JButton(imi[0]);
                 b_replay.setBounds(120,420,140,30);
                 b_replay.setRolloverIcon(imi[3]);
                 b_replay.setPressedIcon(imi[3]);

                 b_replay.setBorderPainted(false);
                 b_replay.setContentAreaFilled(false);

                 jp.add(b_replay);

                 b_replay.addActionListener(new ActionListener()
                 {
                    public void actionPerformed (ActionEvent e) {
                       VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                       VideoToons.getSoundLibrary().setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );
                       VideoToons.getGameServer().changeLevelName( user_dat.level_name );
                       VideoToons.setWindowNumber( VideoToons.SERVER_WAIT_WINDOW );
                    }
                 });

                 JButton b_quit = new JButton(imi[6]);
                 b_quit.setBounds(280,420,80,30);
                 b_quit.setRolloverIcon(imi[7]);
                 b_quit.setPressedIcon(imi[7]);

                 b_quit.setBorderPainted(false);
                 b_quit.setContentAreaFilled(false);

                 jp.add(b_quit);

                 b_quit.addActionListener(new ActionListener()
                 {
                    public void actionPerformed (ActionEvent e) {
                         VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                         VideoToons.getSoundLibrary().setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );
                         VideoToons.getGameClient().cancelGameClient();
                         VideoToons.deleteGameServer();
                         VideoToons.setWindowNumber(VideoToons.MULTIPLAYER_WINDOW);
                    }
                 });

         }

         b_ok.setBorderPainted(false);
         b_ok.setContentAreaFilled(false);

         jp.add(b_ok);

          b_ok.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                 VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                 VideoToons.getSoundLibrary().setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );

                   if( user_dat.game_mode == ONEPLAYER ) {
                          VideoToons.setWindowNumber( VideoToons.LEVEL_SETUP_WINDOW );
                   }
                   else
                   {
                        if( user_dat.is_server_host ) {
                            VideoToons.getGameServer().changeLevelName(
                                             ObjectLibrary.getNextLevelName() );
                             VideoToons.setWindowNumber( VideoToons.SERVER_WAIT_WINDOW );
                        }
                        else
                             VideoToons.setWindowNumber( VideoToons.CLIENT_WAIT_WINDOW );
                   }
              }
            });

      // Score Panel
          j_drawzone = new JPanel()
          {
             public void paintComponent(Graphics g){

                int limit = index.length;
                
                if(limit>4) limit = 4;

                for(int i=0; i<limit; i++)
                {
                   g.drawImage( players[index[i]],
                                pod_x[i]+(PODIUM_WIDTH-players[index[i]].getWidth(this))/2,
                                pod_y[i]-players[index[i]].getHeight(this) ,this);

                   g.setFont(f20);
                   g.setColor(c_shadow);    
                   g.drawString( user_dat.player_name[index[i]],
                                 pod_x[i]+(90 - user_dat.player_name[index[i]].length()*8 )/2,
                                 pod_y[i]-70 );
                   g.setColor(c_text);
                   g.drawString( user_dat.player_name[index[i]],
                                 pod_x[i]+(90 - user_dat.player_name[index[i]].length()*8 )/2-1,
                                 pod_y[i]-70-1 );

                   g.setFont(f25);
                   g.setColor(c_shadow);    
                   g.drawString(""+user_dat.player_score[index[i]],
                                pod_x[i]+(90 - (""+user_dat.player_score[index[i]]).length()*11 )/2,
                                SCORE_YPOS);
                   g.setColor(c_text);
                   g.drawString(""+user_dat.player_score[index[i]],
                                pod_x[i]-1+(90 - (""+user_dat.player_score[index[i]]).length()*11 )/2,
                                SCORE_YPOS-1);
                }

             }
          };

         j_drawzone.setOpaque(false);
         j_drawzone.setBounds(120,180,400,200);
         jp.add(j_drawzone);

      // Preparing results
         index = Tools.sort( user_dat.player_score, user_dat.nb_players );

         for(int i=0; i<user_dat.nb_players; i++) {
           players[i] = getToolkit().getImage("Graphics/players/"+user_dat.sprite_name[i]
                                                 +"/"+user_dat.sprite_name[i]+"-1.gif");

           mediaTracker.addImage(players[i],i+7);
         }
         
         try{
             mediaTracker.waitForAll();
         }
         catch(InterruptedException e){
               e.printStackTrace();
         }

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }


}

