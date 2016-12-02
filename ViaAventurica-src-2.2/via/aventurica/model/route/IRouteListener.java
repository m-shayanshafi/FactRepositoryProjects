package via.aventurica.model.route;

/**
 * Listener, der informiert wird, wenn sich die Länge der Route verändert. 
 */
public interface IRouteListener {

	
	
	/**
	 * wird aufgerufen, falls änderungen an einer Route aufgerufen werden. Informationen, welcher Art die 
	 * Änderungen sind, lassen sich über den {@link RouteUpdateEvent} erfahren 
	 * @param RouteUpdateEvent event
	 * @see RouteUpdateEvent
	 */
	public void routeChanged(RouteUpdateEvent event); 
}
