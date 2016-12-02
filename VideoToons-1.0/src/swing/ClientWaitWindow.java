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

public class ClientWaitWindow extends JWindow implements GameDefinitions
{

  private Image back;

    public ClientWaitWindow(Frame frame)
    {
       super(frame);

      // We load the images
         Image im[] = new Image[2];
         ImageIcon imi[] = new ImageIcon[2];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back4.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<2;i++){
           im[i] = getToolkit().getImage("Graphics/waitc"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }

         for(int i=0;i<2;i++)
          imi[i] = new ImageIcon(im[i]);


         Font f = new Font("Serif", Font.BOLD, 16);
         Color c_text = new Color(50,0,0);

      // Window properties
         getContentPane().setLayout(null);
         getContentPane().setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         getContentPane().setBackground(C_BLACK);
         setBackground(C_BLACK);

      // Jpanel
         JPanel jp = new JPanel();
         jp.setLayout(null);
         jp.setBounds(XO,YO,640,480);
         jp.setBackground(C_BLACK);
         getContentPane().add(jp);

      // Cancel button
         JButton b_quit = new JButton(imi[0]);
         b_quit.setBounds(250,280,140,30);
         b_quit.setRolloverIcon(imi[1]);
         b_quit.setPressedIcon(imi[1]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                VideoToons.setGameStatus(GM_KILLED);
              }
            });

      // Pannel for images
          JPanel j_drawzone = new JPanel()
          {
             public void paintComponent(Graphics g){
                g.drawImage(back,0,0,this);
             }
          };

         j_drawzone.setBounds(35,180,570,120);
         jp.add(j_drawzone);

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }

}


