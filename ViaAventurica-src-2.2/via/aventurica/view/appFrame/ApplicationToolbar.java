package via.aventurica.view.appFrame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import via.aventurica.ViaAventurica;
import via.aventurica.model.route.IRouteListener;
import via.aventurica.model.route.RouteControl;
import via.aventurica.model.route.RouteUpdateEvent;
import via.aventurica.model.route.RouteUpdateEvent.EventType;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.actions.AboutInfoAction;
import via.aventurica.view.actions.ChangeMapAction;
import via.aventurica.view.actions.LoadRouteAction;
import via.aventurica.view.actions.SaveRouteAction;
import via.aventurica.view.actions.ScreenshotDefaultAction;
import via.aventurica.view.actions.ScreenshotSaveAsAction;
import via.aventurica.view.actions.ScreenshotToClipboardAction;
import via.aventurica.view.actions.ToggleClickBehaviourAction;
import via.aventurica.view.actions.ZoomInAction;
import via.aventurica.view.actions.ZoomOutAction;
import via.aventurica.view.actions.ZoomResetAction;
import via.aventurica.view.utils.ToolbarButton;
import via.aventurica.view.utils.ToolbarPopupButton;
import via.aventurica.view.utils.ToolbarToggleButton;

public class ApplicationToolbar extends JToolBar implements IRouteListener, IWorksheetListener{
	private final static long serialVersionUID = 1L;
	 
	private JTextField distanceTextfield; 
	
	
	private JLabel distanceUnitLabel = new JLabel(""); 
	public ApplicationToolbar() { 
		super(); 
		ViaAventurica.addWorksheetListener(this); 
		setFloatable(false);
		setRollover(true);
		
		
		//RouteManager.addDistanceChangeListener(this); 
		
		
		
		
		distanceTextfield = new JTextField(2);
		distanceTextfield.setHorizontalAlignment(JTextField.RIGHT);
		distanceTextfield.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); 
		distanceTextfield.setMaximumSize(new Dimension(60,20));
		distanceTextfield.setEditable(false);
		distanceTextfield.setBackground(Color.WHITE); 
		routeChanged(new RouteUpdateEvent(EventType.ROUTE_DELETED, null)); 
		
		
		add(new ToolbarButton(new ChangeMapAction())); 
		//add(new ToolbarButton(new NewRouteAction())); 
		//add(new ToolbarButton(new UndoRouteAction()));
		add(new ToolbarToggleButton(new ToggleClickBehaviourAction())); 
		addSeparator(); 

		add(new ToolbarButton(new LoadRouteAction())); 
		add(new ToolbarButton(new SaveRouteAction()));
		new ToolbarPopupButton(new ScreenshotDefaultAction(), new ScreenshotToClipboardAction(), new ScreenshotSaveAsAction()).addToToolBar(this); 
		addSeparator();
		
		add(new ToolbarButton(new ZoomInAction())); 
		add(new ToolbarButton(new ZoomOutAction())); 
		add(new ToolbarButton(new ZoomResetAction())); 
		
		addSeparator(); 
		
		add(new JLabel(" Entfernung: ")); 
		add(distanceTextfield);
		add(distanceUnitLabel);
		
		addSeparator(); 
		add(new ToolbarButton(new AboutInfoAction())); 
	}
	
	
	public void routeChanged(RouteUpdateEvent evt) {
		if(evt.type != RouteUpdateEvent.EventType.ROUTE_DELETED)
			distanceTextfield.setText(RouteControl.DISTANCE_FORMAT.format(evt.source.getDistance()));
		else 
			distanceTextfield.setText(RouteControl.DISTANCE_FORMAT.format(0)); 
		
	}
	
	public void worksheetChanged(Worksheet newWorksheet) {
		
		if(newWorksheet!=null) { 
			newWorksheet.routeManager.addRouteListener(this);
			distanceUnitLabel.setText(" "+newWorksheet.map.getDistanceUnit()); 
		}
		
	}

}
