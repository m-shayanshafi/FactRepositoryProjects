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

public class SoundsPanel {

	public static JPanel getSoundsPanel() {
		JPanel pnlSounds = new JPanel();
		pnlSounds.setLayout(new BoxLayout(pnlSounds, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(PlayerSoundsPanel.getPlayerSoundsPanel(), "Player Sounds");
		tabbedPane.add(EnemySoundsPanel.getEnemySoundsPanel(), "Enemy Sounds");
		tabbedPane.add(OtherSoundsPanel.getOtherSoundsPanel(), "Other Sounds");
		pnlSounds.add(tabbedPane);
		
		return pnlSounds;
	}
}
