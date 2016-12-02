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
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.border.*;

import java.net.*;
import java.io.*;


public class HelpWindow extends JWindow implements GameDefinitions
{

  private Image back;
  private JEditorPane html; 


    public HelpWindow(Frame frame)
    {
       super(frame);

      // We load the images
         Image im[] = new Image[2];
         ImageIcon imi[] = new ImageIcon[2];

         MediaTracker mediaTracker = new MediaTracker(this);
         back = getToolkit().getImage("Graphics/back10.jpg");
         mediaTracker.addImage(back,0);

         for(int i=0;i<2;i++){
           im[i] = getToolkit().getImage("Graphics/helpw"+i+".gif");
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

      // Quit button
         JButton b_quit = new JButton(imi[0]);
         b_quit.setBounds(230,450,190,30);
         b_quit.setRolloverIcon(imi[1]);
         b_quit.setPressedIcon(imi[1]);
         b_quit.setBorderPainted(false);
         b_quit.setContentAreaFilled(false);

         jp.add(b_quit);

          b_quit.addActionListener(new ActionListener()
           {
              public void actionPerformed (ActionEvent e)
              {
                VideoToons.setWindowNumber( VideoToons.GAME_MODE_WINDOW );
              }
            });


      // JEDITOR PANE
         URL url = null;
      
         try{
             url = new URL( "file:///"+System.getProperty("user.dir")+File.separator+"help/index.html" );
         }
         catch(MalformedURLException mue){
             System.out.println("ERROR: Help not found at"+url);
         }

         try { 
                 html = new JEditorPane(url);

                 html.setBounds(0,0,640,450);
                 html.setEditable(false);
                 html.setBorder(new EmptyBorder(0, 0, 0, 0) );
                 html.addHyperlinkListener(createHyperLinkListener());
	 
 		JScrollPane scroller = new JScrollPane(html);
                scroller.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
                scroller.setBounds(0,0,640,450);

 		scroller.getViewport().setOpaque(false);
 		scroller.setOpaque(false);
                html.setOpaque(false);

                jp.add(scroller);

         } catch (IOException e) { 
             System.out.println("IOException: " + e); 
         } 

         setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
         setVisible(true);
  }



     public HyperlinkListener createHyperLinkListener()
     { 
 	return new HyperlinkListener()
 	           { 
 	                public void hyperlinkUpdate(HyperlinkEvent e)
 	                { 
                           if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                           { 
 		              if (e instanceof HTMLFrameHyperlinkEvent)
 		              { 
 			        ( (HTMLDocument) html.getDocument() ).processHTMLFrameHyperlinkEvent( 
                                                                      (HTMLFrameHyperlinkEvent) e ); 
                              }
                              else
                              { 
 			          try { 
 			                  html.setPage(e.getURL()); 
 			          } catch (IOException ioe) { 
 			                  System.out.println("IOE: " + ioe); 
 			          } 
 		              } 
 		           } 
 	                } 
 	            }; 
     } 


}

/*
     JEditorPane html; 
      
     public static void main(String[] args) { 
 	HtmlDemo demo = new HtmlDemo(null); 
 	demo.mainImpl(); 
     } 
      
     public HtmlDemo(SwingSet2 swingset) { 
         // Set the title for this demo, and an icon used to represent this 
         // demo inside the SwingSet2 app. 
         super(swingset, "HtmlDemo", "toolbar/JEditorPane.gif"); 
 	 
         try { 
 	    URL url = null; 
 	    // System.getProperty("user.dir") + 
 	    // System.getProperty("file.separator"); 
 	    String path = null; 
 	    try { 
 		path = "/resources/index.html"; 
 		url = getClass().getResource(path); 
             } catch (Exception e) { 
 		System.err.println("Failed to open " + path); 
 		url = null; 
             } 
 	     
             if(url != null) { 
                 html = new JEditorPane(url); 
                 html.setEditable(false); 
                 html.addHyperlinkListener(createHyperLinkListener()); 
 		 
 		JScrollPane scroller = new JScrollPane(); 
 		JViewport vp = scroller.getViewport(); 
 		vp.add(html); 
                 getDemoPanel().add(scroller, BorderLayout.CENTER); 
             } 
         } catch (MalformedURLException e) { 
             System.out.println("Malformed URL: " + e); 
         } catch (IOException e) { 
             System.out.println("IOException: " + e); 
         } 
     } 
  
     public HyperlinkListener createHyperLinkListener() { 
 	return new HyperlinkListener() { 
 	    public void hyperlinkUpdate(HyperlinkEvent e) { 
 		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
 		    if (e instanceof HTMLFrameHyperlinkEvent) { 
 			((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent( 
 			    (HTMLFrameHyperlinkEvent)e); 
 		    } else { 
 			try { 
 			    html.setPage(e.getURL()); 
 			} catch (IOException ioe) { 
 			    System.out.println("IOE: " + ioe); 
 			} 
 		    } 
 		} 
 	    } 
 	}; 
     } 
      
 } 
 */