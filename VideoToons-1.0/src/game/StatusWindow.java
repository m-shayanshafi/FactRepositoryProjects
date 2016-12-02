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
import java.util.Date;

class StatusWindow extends Window implements GameDefinitions
{
  private Image image1,image2,loading;
  private Image offScreenImage;
  private Graphics offScreen;
  private Font f,ftitle;
  
  private static final int Xbar = XO + 192; 
  private static final int Ybar = YO + 200;

  private static final int BAR_WIDTH = 256;
  private static final int BAR_HEIGHT = 25;
  
  private double value;  // 0.0 - 1.0
  private String title;

  static private boolean quit;
  static private StatusWindow sw = null;
  
 /*------------------------------------------------------------------------*/

  static public StatusWindow getDefault()
  {
     return sw;	
  }

  public void setValue( double val )
  {
     value = val;

     toFront();
     repaint();
     
     if(val >= 1.0)
     {
     	Tools.waitTime( 1000 );
        weQuit();       
     }
  }


  public StatusWindow(Frame frame)
  {
     super(frame);
     quit = false;
     sw = this;
     value = 0;
     
   // some inits
      f = new Font("Serif",Font.PLAIN,12);
      	
      // Création de la fenêtre
      setLayout(null);
      setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
      setBackground(C_BLACK);
      toFront();
           
      // chargement des données pour le positionnement de l'image
        MediaTracker mediaTracker = new MediaTracker(this);
        image1 = getToolkit().getImage("Graphics/loading.jpg");
        image2 = getToolkit().getImage("Graphics/loading2.jpg");
        loading = getToolkit().getImage("Graphics/loading.gif");
        mediaTracker.addImage(image1,0);
        mediaTracker.addImage(image2,1);
        mediaTracker.addImage(loading,2);
                                
             try{
                mediaTracker.waitForAll();
             }
             catch(InterruptedException e){
                  e.printStackTrace();
             }         

      // On affiche la fenêtre
      show();            
   }

 /*------------------------------------------------------------------------*/

      	public void paint(Graphics g)
      	{
             if(offScreenImage==null)
                  offScreenImage = createImage(260,60);

             offScreen = offScreenImage.getGraphics();

             offScreen.setColor(C_BLACK);
             offScreen.fillRect(0,0,260,55);
               
             offScreen.drawImage(image2,2,35,this);
      	     offScreen.drawImage(image1,2,35,(int) (2+256*value),60,
      	                               0,0,(int)(256*value),24,this);

             offScreen.drawImage(loading,90,0,this);

             offScreen.setColor(C_BLACK);
             offScreen.setFont(f);
      	     offScreen.drawString((int)(value*100)+" %",125,51);

             offScreen.setColor(C_WHITE);
             offScreen.setFont(f);
      	     offScreen.drawString((int)(value*100)+" %",123,52);

      	     g.drawImage(offScreenImage,Xbar,Ybar,this);

      	}


	public void repaint()
	{
		paint(getGraphics());
	}	



   /*------------------------------------------------------------------------*/
   /** Do we quit ?
     */
     synchronized static public boolean doWeQuit()
     {
        return quit;
     }
     
   /*------------------------------------------------------------------------*/
   /** Ok, we quit this status window
     */
     synchronized static public void weQuit()
     {
        sw.dispose();
        sw = null;
        quit = true;
     }     
}