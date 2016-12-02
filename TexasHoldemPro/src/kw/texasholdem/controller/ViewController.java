package kw.texasholdem.controller;

import kw.texasholdem.ai.impl.Player;
import kw.texasholdem.config.Action;
import kw.texasholdem.view.MenuPanel;
import kw.texasholdem.view.TexasHoldemMainPanel;

/**
 * 
 * Controls the GUI 
 * 
 * @author Ken Wu
 */

public class ViewController {

	private MenuPanel m_menubar;
	private TexasHoldemMainPanel m_parent;

	public ViewController(TexasHoldemMainPanel t, MenuPanel m) {
		m_parent = t;
		m_menubar = m;
	}
	
	public void updateView(Action action, Object additionalCommand) {
		switch(action) {
			case ALLOW_SAVE:
				m_menubar.setSaveButtonEnableOrDisable(true);
				break;
			case DISALLOW_SAVE:
				m_menubar.setSaveButtonEnableOrDisable(false);
				break;
			case HIDE_PROFILE_LEFT_PANEL:
				m_parent.setLeftProfilePanelVisible((String)additionalCommand, false);
				break;
			case RESET_ALL_PROFILE_LEFT_PANEL:
				m_parent.resetAllLeftProfilePanel(true);
				break;
				
			case UPDATE_LEFT_PLAYER_PANEL:
				m_parent.updateLeftPlayerPanel((Player)additionalCommand);
				break;

			default:
				break;
			
		}
	}

	public void doView(Action action) {
		switch(action) {
		case SAVE:
			m_parent.saveItNowIfNeeded();
			break;
		case CLOSE_IF_NEEDED:
			m_parent.closeAndExitIfNeeded();
			break;
		default:
			break;
		
	}
	}

}
