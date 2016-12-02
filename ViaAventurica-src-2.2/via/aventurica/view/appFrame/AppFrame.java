package via.aventurica.view.appFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import via.aventurica.ConfigManager;
import via.aventurica.ViaAventurica;
import via.aventurica.model.worksheet.IWorksheetListener;
import via.aventurica.model.worksheet.Worksheet;
import via.aventurica.view.appFrame.contextPane.ContextPane;
import via.aventurica.view.appFrame.mapView.scrollpane.MapViewScrollpane;
import via.aventurica.view.utils.AppIcons;
import via.aventurica.view.utils.RoundedBorder;

public class AppFrame extends JFrame implements IWorksheetListener{
	private final static long serialVersionUID = 1L;
	
	private Worksheet currentWorksheet; 

	private MapViewScrollpane mapView; 
	private final ApplicationToolbar appToolbar= new ApplicationToolbar();
	private final ContextPane contextPane = new ContextPane(); 

	
	public AppFrame() { 
		super("ViaAventurica (Version "+ViaAventurica.VERSION+")"); 
		
		ViaAventurica.addWorksheetListener(this); 
		setIconImage(AppIcons.APPLICATION_ICON.ICON.getImage());
		mapView = new MapViewScrollpane(); 
		mapView.setBorder(new RoundedBorder(Color.DARK_GRAY, 5, true, true, false)); 
		
		setBounds(ConfigManager.getInstance().getWindowBounds());
		setLayout(new BorderLayout(4,4)); 
		add(appToolbar, BorderLayout.PAGE_START); 
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapView, contextPane);
		splitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    private static final long serialVersionUID = 1L;

					public void setBorder(Border b) {
                    }
                };
            }
        });

		splitPane.setBorder(null); 
		splitPane.setDividerLocation(getWidth()-140);
		splitPane.setResizeWeight(1); 
		add(splitPane, BorderLayout.CENTER); 
		 
		addWindowListener(new WindowAdapter() { 
			@Override
			public void windowClosing(WindowEvent e) {
				ConfigManager configs = ConfigManager.getInstance(); 
				
				configs.setLastMap(currentWorksheet==null ? null : currentWorksheet.map); 
				if(getExtendedState() != MAXIMIZED_BOTH)
					configs.setWindowBounds(getBounds());
				
				configs.save(); 
				
				System.exit(0); 
			}
		});  
		
		setVisible(true); 
	}

	
	public MapViewScrollpane getMapView() {
		return mapView;
	}

	private void unloadWorksheet() {
		if(currentWorksheet!=null) {
			currentWorksheet.unload(); 
			currentWorksheet = null; 
			System.gc();
		}
	}
	
	public void worksheetChanged(Worksheet newWorksheet) {
		unloadWorksheet(); 
		currentWorksheet = newWorksheet; 
	}
	
	public BufferedImage createScreenshot() { 
		return mapView.createScreenshot(); 
	}

	public JComponent getMapComponent() {
		return mapView.getRenderer(); 
	}

}
