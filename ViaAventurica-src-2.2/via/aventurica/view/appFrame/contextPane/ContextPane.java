package via.aventurica.view.appFrame.contextPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import via.aventurica.model.route.Route;
import via.aventurica.view.actions.DeleteCurrentRouteAction;
import via.aventurica.view.actions.NewRouteAction;
import via.aventurica.view.actions.UndoRouteAction;
import via.aventurica.view.utils.JTitleLabel;
import via.aventurica.view.utils.RoundedBorder;
import via.aventurica.view.utils.ToolbarButton;

public class ContextPane extends JPanel {
	private final static long serialVersionUID = 1L;
	
	private JTable routesList; 
	
	public ContextPane() {
		setLayout(new BorderLayout(0, 0)); 
		
		JPanel contentPanel = new JPanel(new BorderLayout(3,3)); 
		contentPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1)); 
		JToolBar tb = new JToolBar();
		tb.setRollover(true); 
		tb.setFloatable(false); 
		tb.add(new ToolbarButton(new NewRouteAction()));
		tb.add(new ToolbarButton(new UndoRouteAction()));
		tb.add(new ToolbarButton(new DeleteCurrentRouteAction())); 
		tb.setOpaque(false);
		
		routesList = new JTable(new RouteTableModel());
		routesList.setTableHeader(null); 
		routesList.setDefaultRenderer(Route.class, new RouteCellRenderer());
		routesList.setDefaultEditor(Route.class, new RouteCellEditor());
		routesList.setRowHeight(26);
		routesList.getColumnModel().getColumn(0).setWidth(120); 
		JScrollPane pane = new JScrollPane(routesList); 
		pane.setBorder(new EmptyBorder(3, 3, 3, 3));  
		
		contentPanel.add(tb, BorderLayout.SOUTH); 
		contentPanel.add(pane, BorderLayout.CENTER);
		
		JTitleLabel titleLabel = new JTitleLabel("Routen");
	/*	titleLabel.setBorder(new LineBorder(Color.DARK_GRAY, 1, true){  
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				g.setColor(lineColor); 
				g.drawRoundRect(0 , 0, width-1, height+2, 5, 5);
			}
		}); */  
		titleLabel.setBorder(new RoundedBorder(Color.DARK_GRAY, 5, true, false, false));
		contentPanel.setBorder(new RoundedBorder(Color.DARK_GRAY, 5, false, true, true)); 
		add(titleLabel, BorderLayout.NORTH); 
		add(contentPanel, BorderLayout.CENTER); 
	}

	
}
