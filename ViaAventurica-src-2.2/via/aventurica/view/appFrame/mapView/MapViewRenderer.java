package via.aventurica.view.appFrame.mapView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.IRouteListener;
import via.aventurica.model.route.Route;
import via.aventurica.model.route.RouteUpdateEvent;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.model.zoom.IZoomListener;

public class MapViewRenderer extends JPanel implements IRouteListener, IZoomListener, IWorksheetListener{
	private final static long serialVersionUID = 1L;
	
	private Worksheet ws; 
	private BufferedImage mapImage; 
	
	private final static Stroke ROUTE_INNER_STROKE = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	private final static Stroke ROUTE_OUTER_STROKE = new BasicStroke(5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND); 
	private final static Stroke ROUTE_PREVIEW_INNER_STROKE = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f, new float[] { 5f, 2f }, 0f);  
	private final static Color ROUTE_OUTER_COLOR = Color.BLACK; 
	 
	private Point previewPoint; 	
	private MapViewPopup popupMenu; 
	private double zoomLevel = 1;  
	
	public MapViewRenderer() { 
		super(); 
		ViaAventurica.addWorksheetListener(this); 
		setOpaque(true); 
		
		popupMenu = new MapViewPopup();
		
		setLayout(null); 
	}
	
	public void worksheetChanged(Worksheet newWorksheet) {
		this.zoomLevel = 1; 
		ws = newWorksheet; 
		if(newWorksheet!=null) { 
			this.mapImage = newWorksheet.map.getMap(); 
			setPreferredSize(new Dimension(mapImage.getWidth(), mapImage.getHeight()));
			scrollRectToVisible(new Rectangle(mapImage.getWidth()/2, mapImage.getHeight()/2,1,1));
			newWorksheet.routeManager.addRouteListener(this); 
			newWorksheet.zoomManager.addZoomListener(this); 
		} else  {
			this.mapImage = null; 
			setPreferredSize(new Dimension(1,1));
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		paintRectangle((Graphics2D)g, g.getClipBounds()); 
		paintChildren(g);  
	}

	private void paintRectangle(Graphics2D g, Rectangle clipBounds) {
		
		if(mapImage == null) {
			g.fillRect(0, 0, getWidth(), getHeight()); 
			return; 
		}
		
		int screenX1 = clipBounds.x; 
		int screenX2 = screenX1 + clipBounds.width; 
		int screenY1 = clipBounds.y;
		int screenY2 = screenY1 + clipBounds.height;
		
		int sourceX1 = (int)Math.round(screenX1 / zoomLevel); 
		int sourceX2 = (int)Math.round(screenX2 / zoomLevel); 
		int sourceY1 = (int)Math.round(screenY1 / zoomLevel); 
		int sourceY2 = (int)Math.round(screenY2 / zoomLevel); 
		
		
		g.drawImage(mapImage, screenX1, screenY1, screenX2, screenY2, 
				sourceX1, sourceY1, sourceX2, sourceY2, null);
		
		
		ArrayList<Route> allRoutes = ws.routeManager.getRenderingQueue();
		final int count = allRoutes.size(); 
		
		for(int routeNumber = 0; routeNumber < count - 1 ; routeNumber++) { 
			drawRoute(g, allRoutes.get(routeNumber), false); 
		}
		drawRoute(g, allRoutes.get(count-1), true); 
		
//		if(ws.routeManager.getCurrentRoute().getWaypointCount()==0)
//			return; 
		

		
	}
	
	
	private final void drawRoute(final Graphics2D g, final Route r, final boolean isCurrent) { 
		if(r.getWaypointCount()==0 || !r.boundingBox.intersects(g.getClipBounds())) // Bounding Box ist null
			return; 
		final Color routeColor = r.getRouteColor(); 
		 
		
		final Point startPoint = r.getUnzoomedStartPoint(); 
		
	 
		
		g.setColor(ROUTE_OUTER_COLOR);
		g.setStroke(ROUTE_OUTER_STROKE);
		drawSegment(g, r); 
		g.fillOval((int)(startPoint.x*zoomLevel)-5, (int)(startPoint.y*zoomLevel)-5, 11, 11);
		g.setColor(routeColor);
		g.setStroke(ROUTE_INNER_STROKE); 
		drawSegment(g, r); 
		 
		
		
		
		if(isCurrent && previewPoint!=null && r.getWaypointCount()>0) {
			Point from = r.getZoomedEndpoint();  
			
			g.setColor(ROUTE_OUTER_COLOR); 
			g.setStroke(ROUTE_OUTER_STROKE); 
			g.drawLine((int)(from.x), (int)(from.y), previewPoint.x, previewPoint.y);
			g.setColor(routeColor); 
			g.setStroke(ROUTE_PREVIEW_INNER_STROKE);
			g.drawLine(from.x, from.y, previewPoint.x, previewPoint.y);	
		}
		 
		g.fillOval((int)(startPoint.x*zoomLevel)-4, (int)(startPoint.y*zoomLevel)-4, 9, 9);
	}
	
	private final  void drawSegment(final Graphics2D g, final Route r) { 
		g.draw(r.waypointPath);
	
	}
	

	void previewLineTo(Point pt) {
		if(previewPoint == null) 
			previewPoint = pt; 
		
		Point endPoint = ws.routeManager.getCurrentRoute().getZoomedEndpoint(); 
		
		if(endPoint != null)
		{	
			int xMin = Math.min(pt.x, Math.min(previewPoint.x, endPoint.x)); 
			int yMin = Math.min(pt.y, Math.min(previewPoint.y, endPoint.y)); 
			int width = Math.max(pt.x, Math.max(previewPoint.x, endPoint.x)); 
			int height = Math.max(pt.y, Math.max(previewPoint.y, endPoint.y));
			repaint(xMin-4, yMin-4, width+8, height+8);
		}
		this.previewPoint = pt; 
	}
	
	void dismissPreview() {
		Route currentRoute = ws.routeManager.getCurrentRoute(); 
		if(currentRoute.getWaypointCount()==0 || previewPoint == null)
			return; 
		
		Point last = ws.routeManager.getCurrentRoute().getZoomedEndpoint();
		int xMin = Math.min(last.x, previewPoint.x); 
		int yMin = Math.min(last.y, previewPoint.y); 
		repaint(xMin-4, yMin-4, Math.max(last.x, previewPoint.x) - xMin + 8, Math.max(last.y, previewPoint.y) - yMin + 8); 
		this.previewPoint = null; 
	}

	public BufferedImage createScreenshot() {
		 
		if(!ws.routeManager.hasVisibleRoutes()) 
			return null;
		ArrayList<Route> routes = ws.routeManager.getRoutes();
		int boundingBoxCount = 0; 
		Rectangle boundingBox = null; 
		for(Route r : routes) { 
			if(r.getWaypointCount()>1) { 
				if(boundingBoxCount==0)
					boundingBox = new Rectangle(r.boundingBox); 
				else
					boundingBox.add(r.boundingBox); 
				boundingBoxCount++; 
			}
		}
		
		BufferedImage screenshot = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)screenshot.getGraphics(); 
		g.translate(-boundingBox.x, -boundingBox.y);
		g.setClip(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height); 
		paintComponent(g); 
		g.dispose(); 
		return screenshot; 
		
		//g.setClip(0, 0, )
		
		
		/*Route current = ws.routeManager.getCurrentRoute(); 
		if(current.getWaypointCount()>0 && mapImage!=null) {
			Rectangle boundingBox = current.boundingBox;
			BufferedImage screenshot = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D)screenshot.getGraphics(); 
			g.translate(-boundingBox.x, -boundingBox.y); 
			paintRectangle(g, boundingBox); 
			return screenshot; 
		} else return null; */ 
		
	}
	public void showPopup(Point point) {
		popupMenu.show(this, point.x, point.y); 
	}

	public void routeChanged(RouteUpdateEvent event) {
		
		repaint(event.source.boundingBox); 
	}

	public void zoomedTo(double oldZoomFactor, double newZoomFactor) {
		zoomLevel = newZoomFactor; 
		if(mapImage!=null) { 
			Dimension newPreferredSize = new Dimension((int)Math.round(mapImage.getWidth()*newZoomFactor), (int)Math.round(mapImage.getHeight()*newZoomFactor));
			setPreferredSize(newPreferredSize); 
			setMaximumSize(newPreferredSize);
		}
	}
	
	
	
}
