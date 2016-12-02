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


public class AboutWindow extends JWindow implements GameDefinitions, ActionListener
{

  private Image back, back2;

  private JPanel j_drawzone;

  private Font f_text, f_title;
  private Color c_text, c_title, c_shadow;

  private Image offScreenImage;

 // a line which contains a title must begin with a space
  private String text[] =
    {
  	" VideoToons",
  	"",
  	"Version 1.0",
  	"Sept. 2000 - Feb. 2001",
  	"",
  	"",

  	" Game Design",
  	"Bertrand Le Nistour",
  	"Frédéric Ponsavady ",
  	"Mael Naguat",
  	"",
  	"",

  	" Java Program",
  	"Bertrand Le Nistour",
  	"",
  	"",

  	" 2D Art",
  	"Bertrand Le Nistour",
  	"Guillaume Le Nistour",
  	"",
  	"",

  	"   Graphics From",
  	"Nintendo Games",
  	"Sega Games",
  	"Konami Games",
  	"NEC Games",
  	"Sunlight Motion Pictures",
  	"",
  	"",

  	" Musics From",
  	"Mystical Ninja - Konami",
  	"Kid Icarus - Nintendo",
  	"Zelda - Nintendo",
  	"Adventure Island - Konami",
  	"Castelvania - Konami",
  	"Metroid - Nintendo",
        "Dragon Spirit - Irem",
  	"",

  	" Game Tests",
  	"Guillaume Le Nistour",
  	"Thierry Pasquier",
  	"John-Guy Park",
  	"Anza Decouz",
        "Patrice Bruyere",
  	"Vladimir Tardy",
        "Jean-Michel Peron",
        "Stéphane Muracciole",
        "Jean-Stéphane Lebrun",
        "Pierre N'guyen",
        "",

        " Thanks to",
        "all friends & parents",
        "",
        "SNES9x Emulator",
        "ZSNES Emulator",
        "vgmusic.com",
        "",
        "",
        "",
        "No toons were hurt during",
        "the game development...",
        "",
        "",
        "Hope you enjoyed the game, ",
        "and yes... ",
        "",
        "",
        "...this game is 100% Java !",
    };

 private Timer timer;

 private int y0;
 private int first_line;
 private static int DRAWZONE_WIDTH = 200;
 private static int DRAWZONE_HEIGHT = 180;


    public AboutWindow( Frame frame)
    {
       super(frame);

         f_text = new Font("Dialog", Font.BOLD, 15);
         f_title = new Font("Dialog", Font.BOLD, 20);
         c_text = new Color(30,30,90);
         c_title = new Color(40,60,170);
         c_shadow = new Color(0,0,50);

         y0 = DRAWZONE_HEIGHT;

      // We load the images
         Image im[] = new Image[2];
         ImageIcon imi[] = new ImageIcon[2];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back9.jpg");
         back2 = getToolkit().getImage("Graphics/about.jpg");
         mediaTracker.addImage(back,0);
         mediaTracker.addImage(back2,1);


         for(int i=0;i<2;i++){
           im[i] = getToolkit().getImage("Graphics/about"+i+".gif");
           mediaTracker.addImage(im[i],i+2);
         }

         try{
             mediaTracker.waitForAll();
         }
         catch(InterruptedException e){
               e.printStackTrace();
         }
         
         for(int i=0;i<2;i++)
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
         JButton b_ok = new JButton(imi[0]);
         b_ok.setBounds(250,400,140,30);
         b_ok.setRolloverIcon(imi[1]);
         b_ok.setPressedIcon(imi[1]);

         b_ok.setBorderPainted(false);
         b_ok.setContentAreaFilled(false);

         jp.add(b_ok);

          b_ok.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e) {
              	timer.stop();
                VideoToons.setWindowNumber( VideoToons.GAME_MODE_WINDOW );
              }
            });

      // Score Panel
          j_drawzone = new JPanel()
          {
             public void paintComponent(Graphics g)
             {
               try
               {
                    if(offScreenImage==null)
                           offScreenImage = createImage(DRAWZONE_WIDTH,DRAWZONE_HEIGHT);
                   
                    Graphics offScreen = offScreenImage.getGraphics();
    
                    offScreen.drawImage(back2,0,0,this);

                    if(first_line==text.length) {
                    	first_line=0;
                    	y0=DRAWZONE_WIDTH+50;
                    }

                     for(int i=first_line; i<text.length;i++)
                     {
                        int base_y = i*25+y0;
                        
                        if(base_y<0) {
                           first_line++;
                           continue;
                        }
                           
                        if( base_y>DRAWZONE_HEIGHT+15)
                           break;

                        if( text[i].length()==0)
                           continue;

                        if(text[i].charAt(0)==' ')
                        {
                           offScreen.setFont(f_title);
                           offScreen.setColor(c_shadow);
                           offScreen.drawString( text[i],
                                 (DRAWZONE_WIDTH -text[i].length()*10 )/2 -4,
                                 base_y +1);

                           offScreen.setColor(c_title);
                           offScreen.drawString( text[i],
                                 (DRAWZONE_WIDTH -text[i].length()*10 )/2 -5,
                                 base_y );
                        }
                        else
                        {             
                           offScreen.setFont(f_text);
                           offScreen.setColor(c_text);    
                           offScreen.drawString( text[i],
                                 (DRAWZONE_WIDTH -text[i].length()*7 )/2,
                                 base_y );                         
                        }
                     }

      	            g.drawImage(offScreenImage,0,0,this);
      	       }
      	       catch (Exception e) {
               }
             }

	     public void repaint() {
		paint(getGraphics());
	     }
          };

         j_drawzone.setOpaque(true);
         j_drawzone.setBounds(220,180,DRAWZONE_WIDTH,DRAWZONE_HEIGHT);
         jp.add(j_drawzone);

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);

         timer = new Timer(15,this);
         timer.start();
  }



  public void actionPerformed( ActionEvent e)
  {
    if(e.getSource()!=timer)
       return;

    y0--;
    
    j_drawzone.repaint();
  }

}

