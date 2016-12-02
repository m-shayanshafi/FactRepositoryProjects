package via.aventurica.view.utils;


import java.awt.Dimension;

import javax.swing.JButton;

/**
 * Button Implementierung f�r einen Button in der Toolbar. 
 * Auf diesem Button erscheint nur das Icon, wenn der Button eines bekommen hat, w�hrend der Text von der {@link ExtendedWorksheetAction} kommt. 
 */
@SuppressWarnings("serial")
public class ToolbarButton extends JButton {

	/**
	 * Initialisiert den Button
	 * @param action die Action, die ausgef�hrt werden soll. 
	 */
	public ToolbarButton(ExtendedWorksheetAction action)
	{
		super(action);
		setPreferredSize(new Dimension(24, 24)); 
		if(action.hasIcon())
			setText("");

		setToolTipText(action.getToolTip()); 
		
	}
	
}
