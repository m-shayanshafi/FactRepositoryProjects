package via.aventurica.view.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.LineBorder;

public class RoundedBorder extends LineBorder {
	private final static long serialVersionUID = 1L;
	
	private final static int ONLY_TOP = 0, ONLY_BOTTOM = 1, BOTH = 2, NONE = 3; 
	private final int paintMode;
	private final boolean close; 
	private final int radius; 
	
	public RoundedBorder(Color color, int radius, boolean roundedOnTop, boolean roundedOnBottom, boolean close) { 
		super(color, 1); 
		this.radius = radius;
		this.close = close; 
		paintMode = roundedOnTop && roundedOnBottom ? BOTH : roundedOnTop ? ONLY_TOP : roundedOnBottom ? ONLY_BOTTOM : NONE;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.setColor(lineColor); 
		switch(paintMode) { 
		case NONE: 
			g.drawRect(x, y, width-1, height-1); 
			break; 
		case BOTH: 
			g.drawRoundRect(x, y, width-1, height-1, radius, radius);
			
			break;  
		case ONLY_BOTTOM: 
			g.drawRoundRect(x, y-radius, width-1, height+radius-1, radius, radius); 
			if(close) 
				g.drawRect(x, y, width, 1); 
			break; 
		case ONLY_TOP: 
			g.drawRoundRect(x, y, width-1, height+radius, radius, radius);
			if(close) 
				g.drawRect(x, y+height-1, width, 1); 
			break; 
		
		}
	}
}
