/* ToggleActionMenuItem.java
   
   A menu item that is based on an AbstractClientAction.
   
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

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ToggleActionMenuItem
	extends JCheckBoxMenuItem
	implements PropertyChangeListener {
	public ToggleActionMenuItem(AbstractToggleAction aca) {
		super((String) aca.getValue(Action.NAME));
		KeyStroke key =
			(KeyStroke) aca.getValue(AbstractToggleAction.ACCELERATOR);
		if (key != null) {
			setAccelerator(key);
		}
		String mn = (String) aca.getValue(AbstractToggleAction.MNEMONIC);
		if (mn != null && mn.length() > 0) {
			setMnemonic(mn.charAt(0));
		}
		setEnabled(aca.isEnabled());
		aca.addPropertyChangeListener(this);
		addActionListener(aca);
		setSelected(aca.isToggledOn());
	}
	public void propertyChange(PropertyChangeEvent pe) {
		if ("enabled".equals(pe.getPropertyName())) {
			setEnabled(((Boolean) pe.getNewValue()).booleanValue());
		} else {
			if (AbstractToggleAction.VISIBLE.equals(pe.getPropertyName())) {
				setVisible(((Boolean) pe.getNewValue()).booleanValue());
			} else {
				if (Action.NAME.equals(pe.getPropertyName())) {
					setText((String) pe.getNewValue());
				} else {
					if (AbstractToggleAction
						.TOGGLE_STATE
						.equals(pe.getPropertyName())) {
						setSelected(
							((Boolean) pe.getNewValue()).booleanValue());
					}
				}
			}
		}
	}
}
