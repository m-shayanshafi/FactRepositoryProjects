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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import src.enums.Sounds;

public class OtherSoundsPanel {

	private static JTextField txtExplosion;

	public static JPanel getOtherSoundsPanel() {
		JPanel pnlOtherSounds = new JPanel();
		pnlOtherSounds.setLayout(new GridBagLayout());
		
		GridBagConstraints lstObjectsC = new GridBagConstraints();
		lstObjectsC.gridwidth = 1;
		lstObjectsC.gridheight = 1;
		lstObjectsC.gridx = 0;
		lstObjectsC.gridy = 0;
		lstObjectsC.weightx = 1;
		lstObjectsC.weighty = 0;
		lstObjectsC.fill = GridBagConstraints.HORIZONTAL;
		lstObjectsC.insets = new Insets(0, 20, 0, 20);
		
		JLabel lblExplosion = new JLabel();
		lblExplosion.setText("Explosion:");

		txtExplosion = new JTextField();
		txtExplosion.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				try {
					Sounds.EXPLOSION.setSound(txtExplosion.getText());
				} catch (Exception ex) { }
			}
		});
		
		// Add labels
		pnlOtherSounds.add(lblExplosion, lstObjectsC);
		
		// Add text fields
		lstObjectsC.insets = new Insets(0, 20, 5, 20);
		lstObjectsC.gridy = 1;
		pnlOtherSounds.add(txtExplosion, lstObjectsC);
		
		fillInfo();
		
		return pnlOtherSounds;
	}
	
	private static void fillInfo() {
		txtExplosion.setText(Sounds.EXPLOSION.getPath());
	}
}
