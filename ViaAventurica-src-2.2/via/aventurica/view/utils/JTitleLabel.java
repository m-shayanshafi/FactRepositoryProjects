package via.aventurica.view.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JLabel;

import via.aventurica.view.appFrame.mapView.scrollpane.MapViewDecoration;

public class JTitleLabel extends JLabel{
	
	private static final long serialVersionUID = 1L;
	private final Color backgroundHighlightColor = MapViewDecoration.RULER_COLOR_2;
	private final Color fontColor = Color.BLACK;
	
	private GradientPaint backgroundGradient;
	private Color backgroundFadeoutColor = MapViewDecoration.RULER_COLOR_1;  
	
	public JTitleLabel() {
		super();
		init();
	}

	public JTitleLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		init();
	}

	public JTitleLabel(Icon image) {
		super(image);
		init();
	}

	public JTitleLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		init();
	}

	public JTitleLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		init();
	}

	public JTitleLabel(String text) {
		super(text);
		init();
	}
	
	private void init() {
		 
		setOpaque(false);
		setPreferredSize(new Dimension(24, 24)); 
		backgroundGradient = new GradientPaint(0,0, backgroundHighlightColor, 0, 24, backgroundFadeoutColor); 
		setFont(getFont().deriveFont(Font.BOLD)); 
		setHorizontalAlignment(CENTER); 
		//setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		setForeground(fontColor);
		
	}
	
		
	@Override
	protected void paintComponent(Graphics g) {
		final int width = getWidth(); 
		final int height = getHeight(); 
		
		((Graphics2D)g).setPaint(backgroundGradient); 
		g.fillRoundRect(0, 0, width, height+5, 5, 5);
		
		super.paintComponent(g);
	}
	

}
