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
import java.net.*;


public class MultiplayerWindow extends JWindow implements GameDefinitions
{

  private Image back;

  private JButton b_host,b_quit,b_connect;
  private JTextField t_name;
  private JTextArea t_status;
  private UserData u_dat;

  private static String server_name;

    public MultiplayerWindow(Frame frame, UserData u_data)
    {
       super(frame);
       u_dat = u_data;
       u_dat.is_server_host = false;

         try
         {
           if(server_name==null)
               server_name = InetAddress.getLocalHost().getHostName();
         }
         catch(UnknownHostException e ) {
             e.printStackTrace();
             server_name = new String("");
         }


      // We load the images
         Image im[] = new Image[6];
         ImageIcon imi[] = new ImageIcon[6];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back2.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<6;i++){
           im[i] = getToolkit().getImage("Graphics/mulw"+i+".gif");
           mediaTracker.addImage(im[i],i+1);
         }

          try{
             mediaTracker.waitForAll();
          }
          catch(InterruptedException e){
               e.printStackTrace();
          }

         for(int i=0;i<6;i++)
          imi[i] = new ImageIcon(im[i]);


         Font f = new Font("Serif", Font.BOLD, 16);
         Font f2 = new Font("Dialog",Font.PLAIN,12);
         Color c_text = new Color(50,0,0);

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

      // Host Server button
         b_host = new JButton(imi[0]);
         b_host.setBounds(200,420,140,30);
         b_host.setRolloverIcon(imi[3]);
         b_host.setPressedIcon(imi[3]);
         b_host.setBorderPainted(false);
         b_host.setContentAreaFilled(false);

         jp.add(b_host);

          b_host.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                 VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                 VideoToons.setWindowNumber(VideoToons.LEVEL_SETUP_WINDOW);
              }
            });

      // Quit button
         b_quit = new JButton(imi[1]);
         b_quit.setBounds(360,420,80,30);
         b_quit.setRolloverIcon(imi[4]);
         b_quit.setPressedIcon(imi[4]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                  VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                  VideoToons.setWindowNumber(VideoToons.PLAYER_SETUP_WINDOW);
              }
            });

      // Connect button
         b_connect = new JButton(imi[2]);
         b_connect.setBounds(405,242,90,30);
         b_connect.setRolloverIcon(imi[5]);
         b_connect.setPressedIcon(imi[5]);
         b_connect.setBorderPainted(false);
         b_connect.setContentAreaFilled(false);

         jp.add(b_connect);

          b_connect.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                 String s = t_name.getText();

                 VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );
                 
                 if( s==null || s.length()==0 ) {
                    t_status.setText("Please enter a server name !");
                    return;
                 }

              // We try to connect
                 server_name = s;
                 t_status.setText( "Trying to connect to "+s+"..." );

                 u_dat.server_name = s;
                 GameClient gc = GameClient.connectToServer( u_dat );

                   if(gc==null) {
                      t_status.setText( u_dat.getErrorString());
                      return;
                   }
              
               // Success...
                  VideoToons.setGameClient(gc);
                  VideoToons.setWindowNumber(VideoToons.CLIENT_WAIT_WINDOW);
              }
            });

      // Text field
           t_name = new JTextField(8 )
           {
              public void requestFocus()
              {
                 try{
                     super.requestFocus();
                 }
                 catch(NullPointerException ne) {}
                
                 getCaret().setVisible(true);
              }

              public void grabFocus()
              {
                try{
                    super.grabFocus();
                 }
                 catch(NullPointerException ne) {}
 
                 getCaret().setVisible(true);
              }
           };
                  
         t_name.setFont(f);
         t_name.setBounds(252,244,140,30);
         t_name.setForeground(c_text);
         t_name.setOpaque(false);
         t_name.setBorder(new EmptyBorder(6, 4, 6, 4) );
         t_name.setText(server_name);

         jp.add(t_name);

      // Text Field for errors
         t_status = new JTextArea("Enter the name of the computer which acts as the Game\nServer.");
         t_status.setFont(f2);
         t_status.setBounds(144,293,353,60);
         t_status.setForeground(c_text);
         t_status.setOpaque(false);
         t_status.setBorder(new EmptyBorder(4, 6, 6, 4) );
         t_status.setEditable(false);

         jp.add(t_status);

       // Score reset
         VideoToons.resetScore();

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
                dispatchEvent(new WindowEvent(MultiplayerWindow.this, WindowEvent.WINDOW_ACTIVATED));
             }
         });

      // step 1
         t_name.requestFocus();

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);

      // step 2... if you remove one of these steps it doesn't work...
         t_name.requestFocus();
  }


}


