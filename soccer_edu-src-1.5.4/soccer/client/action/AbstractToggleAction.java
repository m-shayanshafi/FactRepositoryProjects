/* AbstractToggleAction.java
   This class is the super class of all the client actions

   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package soccer.client.action;
import java.awt.event.ActionEvent;

public abstract class AbstractToggleAction extends AbstractClientAction {
	public static final String TOGGLE_STATE = "togglestate";
	public AbstractToggleAction() {
		super();
		setToggledOn(getInitialToggleState());
	}
	/**
	 * You can override this to return a different inital toggle
	 * state. The default is false.
	 */
	protected boolean getInitialToggleState() {
		return false;
	}
	/**
	 * Subclasses can use this to determine if the toggle action is currently
	 * toggled on.
	 */
	public boolean isToggledOn() {
		try {
			return ((Boolean) getValue(TOGGLE_STATE)).booleanValue();
		} catch (NullPointerException npe) {
			throw new IllegalStateException("Toggle is neither on or off!");
		}
	}
	/**
	 * Programmatically changes the toggle state. You must call
	 * toggleStateChanged yourself, as it is only called if the user
	 * changes the toggle state of the action.
	 */
	protected void setToggledOn(boolean b) {
		putValue(TOGGLE_STATE, b ? Boolean.TRUE : Boolean.FALSE);
	}
	/**
	 * Subclasses should not override this; it just changes the toggle
	 * state of the action. You should override toggleStateChanged()
	 * if you actually want to do something when the toggle state
	 * changes
	 */
	final public void actionPerformed(ActionEvent e) {
		setToggledOn(!isToggledOn());
		toggleStateChanged();
	}
	/**
	 * Subclasses can override this to do whatever they want whenever
	 * the toggle state changes. By default, does nothing.
	 */
	protected void toggleStateChanged() {

	}
}
