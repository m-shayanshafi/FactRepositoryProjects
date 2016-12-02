/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.gui.editor.scenario;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PresetsPanel {
	
	public static JPanel getPresetsPanel() {
		JPanel pnlPresets = new JPanel();
		pnlPresets.setLayout(new BoxLayout(pnlPresets, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(WeaponPresetsPanel.getWeaponPresetsPanel(), "Weapon Shops");
		tabbedPane.add(ArmorPresetsPanel.getArmorPresetsPanel(), "Armor Shops");
		tabbedPane.add(AmmoPresetsPanel.getAmmoPresetsPanel(), "Ammo Shops");
		tabbedPane.add(MagicPresetsPanel.getMagicPresetsPanel(), "Magic Shops");
		tabbedPane.add(GenericPresetsPanel.getGenericPresetsPanel(), "Generic Shops");
		pnlPresets.add(tabbedPane);
		
		return pnlPresets;
	}

}
