package via.aventurica.model.route;

public class RouteUpdateEvent {
	private final static long serialVersionUID = 1L;
	
	public final EventType type; 
	public final Route source; 
	
	public static enum EventType { 
		/**
		 * Zur aktuellen Route {@link RouteUpdateEvent#source} wurde <b>ein oder mehrere</b> Punkte hinzugefügt
		 */
		POINT_ADDED, 
		/**
		 * Von der aktuellen Route {@link RouteUpdateEvent#source} wurde der letzte Punkt gelöscht
		 */
		POINT_REMOVED, 
		/**
		 * Eine neue Route {@link RouteUpdateEvent#source} wurde erstellt und ist nun die aktuelle Route.
		 */
		NEW_ROUTE, 
		/**
		 * Die aktuelle Route ist nun {@link RouteUpdateEvent#source}. 
		 */
		ROUTE_FOCUSED, 
		/**
		 * Von der Aktuellen Route {@link RouteUpdateEvent#source} wurden alle Punkte entfernt.
		 */
		ROUTE_CLEARED,
		/**
		 * Die Route {@link RouteUpdateEvent#source} wurde komplett gelöscht
		 */
		ROUTE_DELETED, 
		/**
		 * Die Farbe der aktuellen Route {@link RouteUpdateEvent#source} wurde geändert. 
		 */
		ROUTE_COLOR_CHANGED, 
		/**
		 * Der Name der aktuellen Route wurde geändert. 
		 */
		NAME_CHANGED
		; 
		
		public boolean isContextual() { 
			return this != POINT_ADDED && this != POINT_REMOVED; 
		}
		
	}

	public RouteUpdateEvent(EventType type, Route source) {
		super();
		this.type = type;
		this.source = source;
	}
}
