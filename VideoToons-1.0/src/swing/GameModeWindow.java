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


public class GameModeWindow extends JWindow implements GameDefinitions
{

  private Image back;


    public GameModeWindow(Frame frame)
    {
       super(frame);

      // We load the images
         Image im[] = new Image[10];
         ImageIcon imi[] = new ImageIcon[10];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<10;i++){
           im[i] = getToolkit().getImage("Graphics/gmwb"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }

         for(int i=0;i<10;i++)
          imi[i] = new ImageIcon(im[i]);


         Font f = new Font("Serif", Font.BOLD, 12);
         Color c_text = new Color(200,175,175);

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

      // One player button
         JButton b_one = new JButton(imi[0]);
         b_one.setFont(f);
         b_one.setBounds(40,260,140,30);
         b_one.setForeground(c_text);
         b_one.setRolloverIcon(imi[1]);
         b_one.setPressedIcon(imi[1]);
         b_one.setBorderPainted(false);
         b_one.setContentAreaFilled(false);


         jp.add(b_one);

          b_one.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
              	VideoToons.getUserData().game_mode = ONEPLAYER;
              	VideoToons.setWindowNumber(VideoToons.PLAYER_SETUP_WINDOW);
                VideoToons.getSoundLibrary().setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );
              }
            });

      // Multiplayer button
         JButton b_multi = new JButton(imi[2]);
         b_multi.setFont(f);
         b_multi.setBounds(90,330,140,30);
         b_multi.setForeground(c_text);
         b_multi.setRolloverIcon(imi[3]);
         b_multi.setPressedIcon(imi[3]);
         b_multi.setBorderPainted(false);
         b_multi.setContentAreaFilled(false);

         jp.add(b_multi);

          b_multi.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
              	VideoToons.getUserData().game_mode = MULTIPLAYER;
              	VideoToons.setWindowNumber(VideoToons.PLAYER_SETUP_WINDOW);
                VideoToons.getSoundLibrary().setMusic( "music/setup.mid", SoundLibrary.MAX_VOLUME );
              }
            });

      // About button
         JButton b_about = new JButton(imi[6]);
         b_about.setFont(f);
         b_about.setBounds(408,328,140,30);
         b_about.setForeground(c_text);
         b_about.setRolloverIcon(imi[7]);
         b_about.setPressedIcon(imi[7]);
         b_about.setBorderPainted(false);
         b_about.setContentAreaFilled(false);

         jp.add(b_about);

          b_about.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
              	VideoToons.setWindowNumber(VideoToons.ABOUT_WINDOW);
              }
            });

      // Help button
         JButton b_help = new JButton(imi[8]);
         b_help.setFont(f);
         b_help.setBounds(458,258,140,30);
         b_help.setForeground(c_text);
         b_help.setRolloverIcon(imi[9]);
         b_help.setPressedIcon(imi[9]);
         b_help.setBorderPainted(false);
         b_help.setContentAreaFilled(false);

         jp.add(b_help);

          b_help.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
              	VideoToons.setWindowNumber(VideoToons.HELP_WINDOW);
              }
            });


      // Quit button
         JButton b_quit = new JButton(imi[4]);
         b_quit.setFont(f);
         b_quit.setBounds(250,390,140,30);
         b_quit.setForeground(c_text);
         b_quit.setRolloverIcon(imi[5]);
         b_quit.setPressedIcon(imi[5]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                 System.exit(1);
              }
            });

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }


}


