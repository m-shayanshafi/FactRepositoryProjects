package game.penguincards;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PenguinCardsAbout extends JDialog {
	
	static PenguinCardsAbout pcAbout=null;
	
	//	set paths of images, pictures
	private String systemImgPath=new String("images/system/");
 	
	public PenguinCardsAbout() {
		initGUI();
	}
	
	private void initGUI()  {
		
		JLabel about=new JLabel();
		JLabel image=new JLabel();
		JButton closeBtn= new JButton("Close");
		
	  if (pcAbout==null) {
		image.setIcon(new ImageIcon(systemImgPath+"about.jpg"));
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(image,BorderLayout.WEST);
		
		String about_str="<html>";
		about_str = about_str + "&nbsp;&nbsp;Penguin Cards v 1.0  (October 2003)<br>";
		about_str = about_str + "&nbsp;&nbsp;written by Emin Islam Tatli <a href=mailto:eminislam@web.de>(eminislam@web.de)</a><br><br>";
		about_str = about_str + "&nbsp;&nbsp;This program can be modified and re-distributed under GPL licence.<br>";
		about_str = about_str + "</html>";
		about.setText(about_str);
		this.getContentPane().add(about,BorderLayout.CENTER);
		
		closeBtn.addActionListener(new CloseListener());
		this.getContentPane().add(closeBtn,BorderLayout.SOUTH);
		this.setTitle("About PenguinCards v1.0");
		this.setModal(true);
		this.pack();
		
		// put dialog in the center
		Dimension frame=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(int) ((frame.getWidth()-this.getWidth())/2);
		int y=(int) ((frame.getHeight()-this.getHeight())/2);
		this.setLocation(x,y); 
		
		pcAbout=this;
	  }
	    pcAbout.setVisible(true);
	}
	
	class CloseListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			pcAbout.setVisible(false);
		}
	}

}
