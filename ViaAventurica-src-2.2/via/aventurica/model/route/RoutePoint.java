package via.aventurica.model.route;

import java.awt.Point;
import java.io.Serializable;

public class RoutePoint extends Point implements Serializable{
	private final static long serialVersionUID = 1L;

	public RoutePoint() {
		super();
	}

	public RoutePoint(int x, int y) {
		super(x, y);
	}

	public RoutePoint(Point p) {
		super(p);
	}
	
}
