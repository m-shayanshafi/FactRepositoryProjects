package via.aventurica.view.utils;


import javax.swing.JToggleButton;

/**
 * Button Implementierung für einen Button in der Toolbar. 
 * Auf diesem Button erscheint nur das Icon, wenn der Button eines bekommen hat, während der Text von der {@link ExtendedWorksheetAction} kommt. 
 */
@SuppressWarnings("serial")
public class ToolbarToggleButton extends JToggleButton {

	/**
	 * Initialisiert den Button
	 * @param action die Action, die ausgeführt werden soll. 
	 */
	public ToolbarToggleButton(ExtendedWorksheetAction action)
	{
		super(action);
		
		if(action.hasIcon())
			setText("");

		setToolTipText(action.getToolTip()); 
		
	}
	
}
