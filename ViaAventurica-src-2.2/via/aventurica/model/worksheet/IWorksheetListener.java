package via.aventurica.model.worksheet;

/**
 * Notifier Interface für alle Objekte, die darauf reagieren müssen, 
 * dass ein neues {@link Worksheet} verwendet wird.  
 */
public interface IWorksheetListener {
	/**
	 * Benachrichtigt, dass ein neues {@link Worksheet} verwendet wird. 
	 * @param newWorksheet das neue {@link Worksheet}, dass nun aktuell ist.
	 */
	public void worksheetChanged(Worksheet newWorksheet); 
}
