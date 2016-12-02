package via.aventurica.view.actions;

import java.awt.event.ActionEvent;

import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public class ScreenshotDefaultAction extends ExtendedWorksheetAction {
	private final static long serialVersionUID = 1L;
	
	private final static ExtendedWorksheetAction DEFAULT_ACTION = new ScreenshotToClipboardAction(); 
	
	public ScreenshotDefaultAction() {
		super("Screenshot", AppIcons.SCREENSHOT_GENERAL); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DEFAULT_ACTION.actionPerformed(e); 
	}
}
