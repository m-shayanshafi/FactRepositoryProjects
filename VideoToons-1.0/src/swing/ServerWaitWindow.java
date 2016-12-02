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
import java.util.Vector;


public class ServerWaitWindow extends JWindow implements GameDefinitions
{

  private Image back;

  private JRadioButton b_lastman;
  private JList l_clients;

  private UserData u_dat;

  private static ServerWaitWindow current_w;

    public ServerWaitWindow(Frame frame, UserData u_data)
    {
       super(frame);
       u_dat = u_data;
       u_dat.is_server_host = true;

      // We load the images
         Image im[] = new Image[8];
         ImageIcon imi[] = new ImageIcon[8];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back6.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<6;i++){
           im[i] = getToolkit().getImage("Graphics/waitr"+i+".gif");
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
         Color c_text = new Color(0,30,100);

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

      // last man standing Checkbox 
         b_lastman = new JRadioButton(imi[4]);
         b_lastman.setBounds(215,358,20,20);
         b_lastman.setPressedIcon(imi[5]);
         b_lastman.setSelectedIcon(imi[5]);
         b_lastman.setBorderPainted(false);
         b_lastman.setContentAreaFilled(false);
         
         if(u_dat.game_mode == LASTMANSTANDING )
             b_lastman.setSelected(true);

         jp.add(b_lastman);



      // Ok button
         JButton b_ok = new JButton(imi[0]);
         b_ok.setBounds(170,420,140,30);
         b_ok.setRolloverIcon(imi[2]);
         b_ok.setPressedIcon(imi[2]);
         b_ok.setBorderPainted(false);
         b_ok.setContentAreaFilled(false);

         jp.add(b_ok);

          b_ok.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
               VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );

                  if( b_lastman.isSelected() )
                  {
                    if( VideoToons.getGameServer().getClientList().size() < 2 )
                       return;
                    u_dat.game_mode = LASTMANSTANDING;
                    VideoToons.getGameServer().changeGameMode(LASTMANSTANDING);
                  }
                  else
                    VideoToons.getGameServer().changeGameMode(MULTIPLAYER);

                  deleteCurrentServerWindow();
                  VideoToons.getGameServer().startGame();
              }
            });

      // Cancel button
         JButton b_quit = new JButton(imi[1]);
         b_quit.setBounds(330,420,140,30);
         b_quit.setRolloverIcon(imi[3]);
         b_quit.setPressedIcon(imi[3]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.getSoundLibrary().playSound( SoundLibrary.BUTTON_SOUND );

              	 deleteCurrentServerWindow();
                 VideoToons.getGameServer().cancelGameServer();
                 VideoToons.setWindowNumber(VideoToons.LEVEL_SETUP_WINDOW);
              }
            });


      // JList
         l_clients = new JList();
         l_clients.setFont(f);
         l_clients.setBounds(250,180,140,140);
         l_clients.setForeground(c_text);
         l_clients.setOpaque(false);
         
         EmptyCellRenderer cellrenderer = new EmptyCellRenderer();
         cellrenderer.setHorizontalAlignment( SwingConstants.CENTER );
         cellrenderer.setForeground(c_text);
         
         l_clients.setCellRenderer( cellrenderer );
         l_clients.setBorder(new EmptyBorder(6, 8, 6, 4) );

         jp.add(l_clients);


      // SERVER CREATION
         setCurrentServerWindow(this);

         if(VideoToons.isServerAlive()) {
            updateClientList(VideoToons.getGameServer().getClientList());
         }
         else
         {
              VideoToons.createNewServer();
              Tools.waitTime( 1000 );

           // CLIENT CREATION
              u_dat.server_name = new String("localhost");
              GameClient gc = GameClient.connectToServer( u_dat );

              if(gc==null) {
                  System.out.println( u_dat.getErrorString() );
                  System.exit(1);
              }

           // Success...
              VideoToons.setGameClient(gc);
         }

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }

 /******************************************************************/
 
   public JList getClientList() {
        return l_clients;
   }


 /******************************************************************/

   static synchronized public void updateClientList( Vector v )
   {
      if(current_w == null)
        return;

      current_w.getClientList().setListData(v);
   }

   static synchronized public void setCurrentServerWindow( ServerWaitWindow sww ) {
      current_w = sww;
   }

   static synchronized public void deleteCurrentServerWindow() {
      current_w = null;
   }

 /******************************************************************/
}



class EmptyCellRenderer extends JLabel implements ListCellRenderer
{
     public EmptyCellRenderer() {
         setOpaque(false);
     }
     
     public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         setText(value.toString());
         return this;
     }
}
 
