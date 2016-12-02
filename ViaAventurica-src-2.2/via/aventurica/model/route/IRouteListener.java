package via.aventurica.model.route;

/**
 * Listener, der informiert wird, wenn sich die L�nge der Route ver�ndert. 
 */
public interface IRouteListener {

	
	
	/**
	 * wird aufgerufen, falls �nderungen an einer Route aufgerufen werden. Informationen, welcher Art die 
	 * �nderungen sind, lassen sich �ber den {@link RouteUpdateEvent} erfahren 
	 * @param RouteUpdateEvent event
	 * @see RouteUpdateEvent
	 */
	public void routeChanged(RouteUpdateEvent event); 
}
