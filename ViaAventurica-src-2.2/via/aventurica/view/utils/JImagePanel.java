package via.aventurica.view.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JViewport;

public class JImagePanel extends JPanel {
	private final static long serialVersionUID = 1L; 
	
	private BufferedImage image;
	private final static Color BACKGROUND_COLOR = Color.DARK_GRAY; 
	private final static Color FOREGROUND_COLOR = Color.WHITE; 
	private boolean hasError = false; 
	
	public static class ImageLoadingException extends Exception { 
		private static final long serialVersionUID = 1L;

		public ImageLoadingException(String image, Throwable cause){ 
			super("Das Bild: "+image+" konnte nicht geladen werden...", cause); 
		}
	}
	
	public JImagePanel() { 
		super(false); 
		setOpaque(true); 
	}
	
	public JImagePanel(final String imageFilename, final boolean isLocal) throws  ImageLoadingException{ 
		this(); 
		loadImage(imageFilename, isLocal); 
	
	}
	
	public void loadImage(final String imageFilename, final boolean isLocal){
		new Thread(new Runnable() {
			public void run() {
				try { 
					unloadImage(); 
					repaint(); 
					image = ImageIO.read(new BufferedInputStream(isLocal ? getClass().getResourceAsStream(imageFilename) : new FileInputStream(new File(imageFilename))));
					Dimension size = new Dimension(image.getWidth(), image.getHeight());
					setMinimumSize(size);
					setPreferredSize(size);
				} catch(Exception e) { 
					hasError = true;
					e.printStackTrace();
				} finally { 
					if(getParent() instanceof JViewport)
						((JViewport)getParent()).updateUI();
					repaint(); 
				}
			}
		}).start(); 
			
			
		
	}
	
	public void unloadImage() { 
		this.image = null; 
		hasError = false; 
		System.gc(); 
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Rectangle clip = g.getClipBounds();
		if(image!=null) {
			g.drawImage(image, 
					clip.x, clip.y, clip.x+clip.width, clip.y+clip.height, 
					clip.x, clip.y, clip.x+clip.width, clip.y+clip.height, this);
		} else {
			g.setColor(BACKGROUND_COLOR); 
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			g.setColor(hasError ? Color.RED : FOREGROUND_COLOR); 
			g.drawString(hasError ? "X" : "Lade...", 4, 10); 
		}
	}
	

	public BufferedImage getImage() {
		return image;
	}
}
