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


public class PlayerSetupWindow extends JWindow implements GameDefinitions
{

  private Image back,card;

  private JTextField t_name;
  private JPanel j_drawzone;
  private UserData user_dat;


    public PlayerSetupWindow( Frame frame, UserData user_data )
    {
       super(frame);

       this.user_dat = user_data;

         Font f = new Font("Serif", Font.BOLD, 16);
         Color c_text = new Color(50,0,0);

      // We load the images
         Image im[] = new Image[8];
         ImageIcon imi[] = new ImageIcon[8];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back3.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<8;i++){
           im[i] = getToolkit().getImage("Graphics/selp"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }
         
      // Player Card
         card = getToolkit().getImage("Graphics/players/"+user_dat.host_sprite_name+"/card.jpg");
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
         b_prev.setBounds(215,290,30,30);
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
                changeToPrevPlayerImage();
                j_drawzone.repaint();
              }
            });

      // Next Image button
         JButton b_next = new JButton(imi[2]);
         b_next.setBounds(395,290,30,30);
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
                changeToNextPlayerImage();
                j_drawzone.repaint();
              }
            });

      // Ok button
         JButton b_ok = new JButton(imi[4]);
         b_ok.setBounds(160,430,90,30);
         b_ok.setRolloverIcon(imi[5]);
         b_ok.setPressedIcon(imi[5]);
         b_ok.setBorderPainted(false);
         b_ok.setContentAreaFilled(false);

         jp.add(b_ok);

          b_ok.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
              	if(t_name.getText().length()==0)
                   return;
              	
              	user_dat.host_player_name = t_name.getText();

                 if(user_dat.host_player_name.length()>15)
                     user_dat.host_player_name = user_dat.host_player_name.substring(0,14);

                 VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );

              	 if(user_dat.game_mode == ONEPLAYER)
              	    VideoToons.setWindowNumber(VideoToons.LEVEL_SETUP_WINDOW);
              	 else
              	 {
                    VideoToons.setWindowNumber(VideoToons.MULTIPLAYER_WINDOW);
                 }
              }
            });

      // Cancel button
         JButton b_quit = new JButton(imi[6]);
         b_quit.setBounds(390,430,90,30);
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
                VideoToons.setWindowNumber(VideoToons.GAME_MODE_WINDOW);
              }
            });


      // Text field
         t_name = new JTextField(8)
         {
             public void requestFocus()
             {
                super.requestFocus();
                getCaret().setVisible(true);
             }

             public void grabFocus()
             {
                super.grabFocus();
                getCaret().setVisible(true);
             }
         };

         t_name.setFont(f);
         t_name.setBounds(330,360,160,30);
         t_name.setForeground(c_text);
         t_name.setOpaque(false);
         t_name.setBorder(new EmptyBorder(6, 4, 6, 4) );
         t_name.setText(user_data.host_player_name);

         jp.add(t_name);


      // Pannel for images
          j_drawzone = new JPanel()
          {
             public void paintComponent(Graphics g){
                g.drawImage(card,0,0,this);
             }
          };

         j_drawzone.setBounds(250,200,140,100);
         jp.add(j_drawzone);

      // Window's damn focus !!!
         addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent e) {
             	 t_name.requestFocus();
             }
         });

         t_name.addFocusListener(new FocusListener() {
             public void focusLost(FocusEvent e) {
             }
             
             public void focusGained(FocusEvent e) {
                dispatchEvent(new WindowEvent(PlayerSetupWindow.this, WindowEvent.WINDOW_ACTIVATED));	

             }
         });


      // mandatory : first try...
         t_name.requestFocus();

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);

     // mandatory : second try... it only works this way...
         t_name.requestFocus();
  }



  public void changeToNextPlayerImage()
  {
         MediaTracker mediaTracker = new MediaTracker(this);

         user_dat.host_sprite_name = ObjectLibrary.getNextPlayerName();

         card = getToolkit().getImage("Graphics/players/"+user_dat.host_sprite_name+"/card.jpg");
         mediaTracker.addImage(card,0);

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }
  }


  public void changeToPrevPlayerImage()
  {
         MediaTracker mediaTracker = new MediaTracker(this);

         user_dat.host_sprite_name = ObjectLibrary.getPrevPlayerName();

         card = getToolkit().getImage("Graphics/players/"+user_dat.host_sprite_name+"/card.jpg");
         mediaTracker.addImage(card,0);

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }
  }

}


