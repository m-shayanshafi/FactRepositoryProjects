package via.aventurica.view.appFrame.mapView.scrollpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import via.aventurica.ViaAventurica;
import via.aventurica.model.map.Map;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.model.zoom.IZoomListener;


/**
 * Ruler für den oberen und linken rand der Scrollpane. Diese zeigen den Maßstab der Karte an und bieten ein Koordinatensystem. 
 */
public class MapViewDecoration extends JPanel implements IZoomListener, IWorksheetListener{
	private final static long serialVersionUID = 1L;
	
	public final static Color RULER_COLOR_1 = new Color(0xf9e7d3);
	public final static Color RULER_COLOR_2 = new Color(0xd5b89a);
	public final static Color CORNER_COLOR = new Color(0x669933); 
	private final static Color BORDER_COLOR = Color.DARK_GRAY;
	
	private final static int RULER_HEIGHT = 12; 

	private int pixelDistance=0;  
	@SuppressWarnings("unused")
	private int unitDistance=0; 
	
	private double zoomLevel; 
	private Map currentMap; 
	
	
	public static enum Direction { 
		RULER_HORIZONTAL, RULER_VERTICAL, CORNER_LEFT_UPPER, CORNER_RIGHT_UPPER, CORNER_LEFT_LOWER;  
	}
	
	private Direction rulerDirection; 
	private boolean isRuler; 
	

	private void setMap(Map map) {
		this.zoomLevel = 1; 
		this.currentMap = map; 
		if(map == null) {
			this.pixelDistance = 0; 
			this.unitDistance = 0; 
			setPreferredSize(new Dimension(RULER_HEIGHT, RULER_HEIGHT));
			return; 
		}
		this.pixelDistance = (int)Math.round(map.getPixelSampleDistance()); 
		this.unitDistance = (int)Math.round(map.getRealSampleDistance());
		if(rulerDirection == Direction.RULER_HORIZONTAL)
			setPreferredSize(new Dimension(map.getMap().getWidth(), RULER_HEIGHT)); 
		else 
			setPreferredSize(new Dimension(RULER_HEIGHT, map.getMap().getHeight()));
	}
	
	
	public MapViewDecoration(Direction rulerDirection) { 
		isRuler = rulerDirection == Direction.RULER_HORIZONTAL || rulerDirection == Direction.RULER_VERTICAL; 
		if(isRuler) 
			ViaAventurica.addWorksheetListener(this); 
		this.rulerDirection = rulerDirection;
		setPreferredSize(new Dimension(RULER_HEIGHT, RULER_HEIGHT));
		
	}
	
	public void worksheetChanged(Worksheet newWorksheet) {
		if(newWorksheet!=null) { 
			newWorksheet.zoomManager.addZoomListener(this);
			setMap(newWorksheet.map); 
		} else setMap(null);  
	}
	
	@Override
	protected void paintComponent(Graphics gO) {
		Graphics2D g = (Graphics2D)gO;

		
		if(isRuler) {
			Rectangle clipRect = g.getClipBounds();
			g.setColor(RULER_COLOR_1); 
			g.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height); 
			g.setColor(RULER_COLOR_2);
	
			if(pixelDistance<=0)
				return; 
			
			int pixelDistance = (int)Math.round(this.pixelDistance * zoomLevel); 
			
			if(rulerDirection == Direction.RULER_HORIZONTAL) {	 
				
				final int startIndex = clipRect.x / pixelDistance; 
				 
				final int min = startIndex %2 == 0 ? startIndex * pixelDistance : (startIndex-1)*pixelDistance;
				final int max = clipRect.x+clipRect.width; 
				
				for(int x=min, index = startIndex; x < max; x+=pixelDistance*2, index+=2) {
					g.fillRect(x, 0, pixelDistance, RULER_HEIGHT-1);
					//TODO: BUGGY
					//g.drawString(""+unitDistance * (index), x+pixelDistance+2, RULER_HEIGHT-2);
				}
				 
				g.setColor(BORDER_COLOR);
				g.drawLine(clipRect.x, RULER_HEIGHT-1, clipRect.x+clipRect.width, RULER_HEIGHT-1); 
				
			} else { 
				int min = clipRect.y / pixelDistance; 
				min = min %2 == 0 ? min * pixelDistance : (min-1)*pixelDistance;
				int max = clipRect.y+clipRect.height; 
				
				for(int y=min; y < max; y+=pixelDistance*2) {
					g.fillRect(0, y, RULER_HEIGHT-1, pixelDistance); 
				}
				g.setColor(BORDER_COLOR);
				g.drawLine(RULER_HEIGHT-1,clipRect.y, RULER_HEIGHT-1, clipRect.y+clipRect.height); 
			}
		} else { 
			final int width = getWidth(); 
			final int height = getHeight(); 
			g.setColor(CORNER_COLOR); 
			if(rulerDirection == Direction.CORNER_LEFT_UPPER) { 
				g.fillRoundRect(0, 0, width+5, height+5, 5, 5);
				g.setColor(BORDER_COLOR); 
				g.drawRect(-1, -1, width, height); 
			} else if(rulerDirection == Direction.CORNER_RIGHT_UPPER) { 
				g.fillRoundRect(-5, 0, width+5, height+5, 5, 5);
				g.setColor(BORDER_COLOR); 
				g.drawLine(0, 0, 0, height); 
				//g.drawRect(0, 0, width+1, height+1);
			} else if(rulerDirection == Direction.CORNER_LEFT_LOWER) { 
				g.fillRoundRect(0, -5, width+5, height+5, 5, 5);
				g.setColor(BORDER_COLOR);
				g.drawLine(0, 0, width, 0); 
			}
		}
	}
	
	


	public void zoomedTo(double oldZoomLevel, double newZoomLevel) {
		this.zoomLevel = newZoomLevel;
		pixelDistance = (int)(currentMap.getPixelSampleDistance() * newZoomLevel); 
		if(currentMap==null)
			return; 
		
		if(rulerDirection == Direction.RULER_HORIZONTAL)
			setPreferredSize(new Dimension((int)(currentMap.getMap().getWidth()*newZoomLevel), RULER_HEIGHT)); 
		else 
			setPreferredSize(new Dimension(RULER_HEIGHT, (int)(currentMap.getMap().getHeight()*newZoomLevel)));
		
	}
}
