package via.aventurica.view.actions.context;

import java.awt.event.ActionEvent;

import via.aventurica.view.appFrame.mapView.MapViewPopup;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.ExtendedWorksheetAction;

public abstract class ExtendedPopupAction extends ExtendedWorksheetAction {
	
	
	public ExtendedPopupAction(String title, AppIcons image) {
		super(title, image);
	}

	public ExtendedPopupAction(String title, String toolTip, AppIcons image) {
		super(title, toolTip, image);
	}

	public ExtendedPopupAction(String title) {
		super(title);
	}

	private final static long serialVersionUID = 1L;
	
	@Override
	public final void actionPerformed(ActionEvent e) {
		actionPerformed(e, MapViewPopup.getLastMapPoint()); 
	}
	
	public abstract void actionPerformed(ActionEvent e, java.awt.Point mapPosition); 
}
