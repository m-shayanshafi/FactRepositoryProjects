/* TurnOnSoundAction.java
   
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

/**
 *
 */
public class TurnOnSoundAction extends AbstractToggleAction {
	public TurnOnSoundAction() {
		super();
		putValue(NAME, "Sound On");
		//setAccelerator( KeyEvent.VK_G, Event.CTRL_MASK );
		setEnabled(true);
		setToggledOn(true);
	}
	/**
	 * The toggle was changed
	 */
	protected void toggleStateChanged() {
		getSoccerMaster().getSoundSystem().setSoundOn(isToggledOn());

	}
}
