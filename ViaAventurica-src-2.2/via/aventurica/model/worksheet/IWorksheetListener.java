package via.aventurica.model.worksheet;

/**
 * Notifier Interface f�r alle Objekte, die darauf reagieren m�ssen, 
 * dass ein neues {@link Worksheet} verwendet wird.  
 */
public interface IWorksheetListener {
	/**
	 * Benachrichtigt, dass ein neues {@link Worksheet} verwendet wird. 
	 * @param newWorksheet das neue {@link Worksheet}, dass nun aktuell ist.
	 */
	public void worksheetChanged(Worksheet newWorksheet); 
}
