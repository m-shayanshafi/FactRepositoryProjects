package game.penguincards;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import game.penguincards.debug.*;

public class PenguinCardsPanel extends JPanel {
	
	private String activeImg;
	private CardLayout cardLayout;
	protected String imgForeground,imgBackground;
	protected PenguinCardsPanel theInstance=null;
	private  PenguinCards penguinCards; 
	public int sequence;
	
	public PenguinCardsPanel(String imgForeground, String imgBackground, int sequence, String name, Dimension dimension) {
		
		super();
		
		theInstance=this;
		penguinCards=PenguinCards.getInstance();
		this.setName(name);
		this.sequence=sequence;

		this.imgForeground=imgForeground;
		this.imgBackground=imgBackground;
		this.activeImg=imgForeground;
		
		cardLayout=new CardLayout();

		ImagePanel panelFG=new ImagePanel(getScaledImage(imgForeground));
		ImagePanel panelBG=new ImagePanel(getScaledImage(imgBackground));
		
		this.setLayout(cardLayout);
		this.add(panelFG,imgForeground);
		this.add(panelBG,imgBackground);
		
		// specify the border
		this.setBorder(BorderFactory.createRaisedBevelBorder()); 
		
		activeImg= new String(imgForeground);
		setDefaultImage(imgForeground);
	}
	
	private Image getScaledImage(String imgFile) {
		BufferedImage bufferedImage=null;
		try {
			bufferedImage = ImageIO.read(new File(imgFile));
		}catch(IOException exc){
			Debug.debug(exc.getMessage());
		}
		
		return bufferedImage;
	}
	
	public  void clickImage() {
		if (activeImg.equalsIgnoreCase(imgForeground)) {
			setActiveImage(imgBackground);
			penguinCards.getCardsNumber(theInstance.sequence,Integer.parseInt(theInstance.getName()));
		}
	}
	
	protected void setActiveImage() {
		activeImg=imgBackground;
		cardLayout.show(theInstance,activeImg);
	}
	
	protected void setActiveImage(String newImage) {
		activeImg=newImage;
		cardLayout.show(theInstance,activeImg);
	}
	
	protected void setDefaultImage(String newImage) {
		activeImg=newImage;
		cardLayout.show(theInstance,activeImg);
	}
	
	public void closeCard() {
		setActiveImage(imgForeground);
	}
	
	public void disableCard() {
		theInstance.setEnabled(false);
	}
	
	class ImagePanel extends JPanel {
		Image scaledImg;
		
		public ImagePanel(Image scaledImg) {
			super();
			this.scaledImg=scaledImg;
		}
		
		public void paint(Graphics g) {
			g.drawImage(scaledImg, 0,0, this.getWidth(), this.getHeight(),this);
		}
	}
	
}
