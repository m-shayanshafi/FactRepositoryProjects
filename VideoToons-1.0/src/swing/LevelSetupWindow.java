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


public class LevelSetupWindow extends JWindow implements GameDefinitions
{

  private Image back,card;

  private JLabel l_wname;
  private JLabel l_name;
  private JLabel l_difficulty;
  private JPanel j_drawzone;

  private JTextArea t_description;

  private UserData user_dat;
  private LevelInfo l_info;

    public LevelSetupWindow(Frame frame, UserData user_data)
    {
       super(frame);

       this.user_dat = user_data;

         Font f = new Font("Serif", Font.BOLD, 16);
         Font f_small = new Font("Dialog", Font.PLAIN, 15);
         Color c_text = new Color(30,80,0);

      // We load the images
         Image im[] = new Image[8];
         ImageIcon imi[] = new ImageIcon[8];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back5.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<8;i++){
           im[i] = getToolkit().getImage("Graphics/levsel"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }

      // Card
         card = getToolkit().getImage("Graphics/levels/"+user_dat.level_name+"/card.jpg");
         mediaTracker.addImage(card,9);

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
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

      // Prev Image button
         JButton b_prev = new JButton(imi[0]);
         b_prev.setBounds(230,342,30,30);
         b_prev.setRolloverIcon(imi[1]);
         b_prev.setPressedIcon(imi[1]);
         b_prev.setBorderPainted(false);
         b_prev.setContentAreaFilled(false);

         jp.add(b_prev);

          b_prev.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                changeToPrevLevelImage();
                j_drawzone.repaint();
              }
            });

      // Next Image button
         JButton b_next = new JButton(imi[2]);
         b_next.setBounds(380,342,30,30);
         b_next.setRolloverIcon(imi[3]);
         b_next.setPressedIcon(imi[3]);
         b_next.setBorderPainted(false);
         b_next.setContentAreaFilled(false);

         jp.add(b_next);

          b_next.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                changeToNextLevelImage();
                j_drawzone.repaint();
              }
            });

      // Ok button
         JButton b_ok = new JButton(imi[4]);
         b_ok.setBounds(182,420,90,30);
         b_ok.setRolloverIcon(imi[5]);
         b_ok.setPressedIcon(imi[5]);
         b_ok.setBorderPainted(false);
         b_ok.setContentAreaFilled(false);

         jp.add(b_ok);

          b_ok.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );

              	 if(user_dat.game_mode == ONEPLAYER) {
              	    VideoToons.setWindowNumber(VideoToons.GAME_WINDOW);
                    VideoToons.getGameServer().changeLevelName( user_dat.level_name );
                    VideoToons.getGameServer().startGame();
                 }
                 else
                    VideoToons.setWindowNumber(VideoToons.SERVER_WAIT_WINDOW);
              }
            });

      // Cancel button
         JButton b_quit = new JButton(imi[6]);
         b_quit.setBounds(368,420,90,30);
         b_quit.setRolloverIcon(imi[7]);
         b_quit.setPressedIcon(imi[7]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
               VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );

              	 if(user_dat.game_mode == ONEPLAYER) {
              	    VideoToons.setWindowNumber(VideoToons.PLAYER_SETUP_WINDOW);
                    VideoToons.getGameServer().cancelGameServer();
                 }
              	 else
                    VideoToons.setWindowNumber(VideoToons.MULTIPLAYER_WINDOW);
              }
            });

      // Light version of the current level
         l_info = ObjectLibrary.getLevelInfo(user_dat.level_name);

      // labels
         l_wname = new JLabel(l_info.getWorldName(),SwingConstants.CENTER);
         l_wname.setFont(f);
         l_wname.setBounds(30,200,140,30);
         l_wname.setForeground(c_text);
         l_wname.setOpaque(false);
         l_wname.setBorder(new EmptyBorder(4, 2, 4, 2) );

         jp.add(l_wname);

         l_name = new JLabel(l_info.getLevelName(),SwingConstants.CENTER);
         l_name.setFont(f);
         l_name.setBounds(30,270,140,30);
         l_name.setForeground(c_text);
         l_name.setOpaque(false);
         l_name.setBorder(new EmptyBorder(4, 2, 4, 2) );

         jp.add(l_name);

         l_difficulty = new JLabel(l_info.getDifficulty(),SwingConstants.CENTER);
         l_difficulty.setFont(f);
         l_difficulty.setBounds(30,340,140,30);
         l_difficulty.setForeground(c_text);
         l_difficulty.setOpaque(false);
         l_difficulty.setBorder(new EmptyBorder(4, 2, 4, 2) );

         jp.add(l_difficulty);

         t_description = new JTextArea(l_info.getDescription());
         t_description.setFont(f_small);
         t_description.setBounds(470,200,140,170);
         t_description.setForeground(c_text);
         t_description.setOpaque(false);
         t_description.setBorder(new EmptyBorder(4, 4, 4, 4) );
         t_description.setEditable(false);
         t_description.setLineWrap(true);
         t_description.setWrapStyleWord(true);

         jp.add(t_description);

      // Pannel for images
          j_drawzone = new JPanel()
          {
             public void paintComponent(Graphics g){
                g.drawImage(card,0,0,this);
             }
          };

         j_drawzone.setBounds(185,153,270,190);
         jp.add(j_drawzone);


      // One player game ?
         if( user_dat.game_mode == ONEPLAYER )
         {
            if( !VideoToons.isServerAlive() )
            {
               VideoToons.createNewServer();
               Tools.waitTime( 1000 );

            // CLIENT CREATION
               user_dat.server_name = new String("localhost");
               GameClient gc = GameClient.connectToServer( user_dat );

               if(gc==null) {
                   System.out.println( user_dat.getErrorString() );
                   System.exit(1);
               }

            // Success...
               VideoToons.setGameClient(gc);
            }
            else
               VideoToons.getGameServer().setGameStatus( GM_READY );
         }


       // Score reset
          VideoToons.resetScore();

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }



  public void changeToNextLevelImage()
  {
         MediaTracker mediaTracker = new MediaTracker(this);

         user_dat.level_name = ObjectLibrary.getNextLevelName();

         card = getToolkit().getImage("Graphics/levels/"+user_dat.level_name+"/card.jpg");
         mediaTracker.addImage(card,0);

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }
          
       // Level Info
          l_info = ObjectLibrary.getLevelInfo(user_dat.level_name);         
          l_wname.setText(l_info.getWorldName());
          l_name.setText(l_info.getLevelName());
          l_difficulty.setText(l_info.getDifficulty());
          t_description.setText(l_info.getDescription());
  }


  public void changeToPrevLevelImage()
  {
         MediaTracker mediaTracker = new MediaTracker(this);

         user_dat.level_name = ObjectLibrary.getPrevLevelName();

         card = getToolkit().getImage("Graphics/levels/"+user_dat.level_name+"/card.jpg");
         mediaTracker.addImage(card,0);

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }

       // Level Info
          l_info = ObjectLibrary.getLevelInfo(user_dat.level_name);         
          l_wname.setText(l_info.getWorldName());
          l_name.setText(l_info.getLevelName());
          l_difficulty.setText(l_info.getDifficulty());
          t_description.setText(l_info.getDescription());
  }

}


