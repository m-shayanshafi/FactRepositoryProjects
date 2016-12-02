/* AbstractClientAction.java
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

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import soccer.client.SoccerMaster;

public abstract class AbstractClientAction extends AbstractAction {
	private SoccerMaster m_s;
	public static final String ACCELERATOR = "accelerator";
	public static final String MNEMONIC = "mnemonic";
	public static final String VISIBLE = "visibile";

	private static boolean s_numpadLog = true;

	public AbstractClientAction() {
		setEnabled(false);
	}

	public AbstractClientAction(SoccerMaster s) {
		m_s = s;
	}

	void setSoccerMaster(SoccerMaster s) {
		m_s = s;
	}

	protected final void setMnemonic(char c) {
		putValue(MNEMONIC, "" + c);
	}

	protected final void setAccelerator(int vcode, int mod) {
		putValue(ACCELERATOR, KeyStroke.getKeyStroke(vcode, mod));
	}

	protected final void setAccelerator(int vcode) {
		setAccelerator(vcode, 0);
	}

	protected final SoccerMaster getSoccerMaster() {
		return m_s;
	}

	public boolean isVisible() {
		return (((Boolean) getValue(VISIBLE)).booleanValue());
	}

	public void setVisible(boolean b) {
		putValue(VISIBLE, b ? Boolean.TRUE : Boolean.FALSE);
	}

}
