package via.aventurica.view.utils;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * Erweiterte Action, die ein Icon, eine Beschriftung und einen Tooltip Text verwalten kann. 
 */
abstract class ExtAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private String toolTip; 
	private boolean hasIcon = false; 
	
	/**
	 * Initialisiert die Action, Titel und Tooltip sind gleich dem Parameter
	 * @param title titel und tooltip. 
	 */
	public ExtAction(final String title)
	{
		this(title, title, null); 
	}
	
	/**
	 * Initialisiert die Action mit einem Icon und einem Titel
	 * @param title titel
	 * @param image icon
	 */
	public ExtAction(final String title, final AppIcons image)
	{
		this(title, title, image); 
	}
	
	/**
	 * Initialisiert die Action mit Icon, Titel und Tooltip. 
	 * @param title titel
	 * @param toolTip tooltip
	 * @param image icon
	 */
	public ExtAction(final String title, final String toolTip, final AppIcons image)
	{
		super(title, image == null ? null : image.ICON); 
		this.toolTip = title;
		hasIcon = image!=null; 
	}
	
	/**
	 * @return den ToolTip für diese Action
	 */
	public String getToolTip() {
		return toolTip;
	}
	
	/**
	 * @return true, falls die Action ein Icon hat. 
	 */
	public boolean hasIcon()
	{
		return hasIcon; 
	}
	
	
	public Icon getIcon() { 
		return (Icon)getValue(Action.SMALL_ICON); 
	}
	
	
	public abstract void actionPerformed(ActionEvent e);  
	
	

	
	

}
