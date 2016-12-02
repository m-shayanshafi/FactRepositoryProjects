package via.aventurica.view.appFrame.markerDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import via.aventurica.model.marker.Marker;
import via.aventurica.model.marker.MarkerIcon;
import via.aventurica.view.utils.ColorPickerButton;
import via.aventurica.view.utils.FormEditException;
import via.aventurica.view.utils.GenericWizard;
import via.aventurica.view.utils.UserIcons;

public class MarkerDialog extends GenericWizard<Marker> {
	private final static long serialVersionUID = 1L;

	private JTextField titleTextField = new JTextField(15); 
	private JList iconList = new JList(UserIcons.INSTANCE);
	private ColorPickerButton colorPicker = new ColorPickerButton(Color.WHITE); 
	private JCheckBox isSemiTransparent = new JCheckBox("Halbtransparent", true); 
	
	public final static int DIALOG_WIDTH = 320, DIALOG_HEIGHT=250; 
	
	public static void main(String[] args) {
		new MarkerDialog(null, null); 
	}
	
	public MarkerDialog(Marker marker, Point openPointOnScreen) {
		super(marker, marker == null ? "Marker Erstellen" : marker.getText()+" bearbeiten", null, DIALOG_WIDTH, DIALOG_HEIGHT, 1, true, true); 
		if(openPointOnScreen!=null) { 
			
			setUndecorated(true);
			setBounds(openPointOnScreen.x, openPointOnScreen.y,	DIALOG_WIDTH, DIALOG_HEIGHT); 
		}
		
		setVisible(true); 
	}

	
	
	@Override
	protected JComponent getForm(int pageNumber) {
		JPanel pnl = new JPanel(new BorderLayout(4, 4));
		Marker mrk = getUserData(); 
		if(mrk!=null) { 
			titleTextField.setText(mrk.getText());
			colorPicker.setSelectedColor(mrk.getMarkerColor()); 
			isSemiTransparent.setSelected(mrk.isSemiTransparent());
			iconList.setSelectedValue(mrk.getMarkerIcon(), true); 
		}
		JPanel headPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)), bodyPanel = new JPanel(new BorderLayout()); 
		JPanel footPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		iconList.setLayoutOrientation(JList.HORIZONTAL_WRAP); 
		iconList.setCellRenderer(new MarkerRenderer()); 
		
		
		headPanel.add(new JLabel("Titel: ")); 
		headPanel.add(titleTextField); 
		
		bodyPanel.add(new JLabel("Icon für Marker:"), BorderLayout.NORTH);
		bodyPanel.add(new JScrollPane(iconList)); 
		
		footPanel.add(new JLabel("Farbe des Markers:"));
		
		
		
		footPanel.add(colorPicker); 
		footPanel.add(isSemiTransparent); 
		
		pnl.add(headPanel, BorderLayout.NORTH); 
		pnl.add(bodyPanel, BorderLayout.CENTER); 
		pnl.add(footPanel, BorderLayout.SOUTH); 
		
		return pnl; 
	}
	
	@Override
	protected void okButtonPressed() throws FormEditException {
		if(titleTextField.getText().trim().length()==0)
			throw new FormEditException("Bitte einen Titel festlegen"); 
		
		Marker mrk = getUserData(); 
		 
		if(mrk==null) { 
			mrk = new Marker(0, 0, titleTextField.getText()); 
			setUserData(mrk); 
		} else {  
			mrk.setText(titleTextField.getText());
		}
		
		mrk.setMarkerColor(colorPicker.getSelectedColor(), isSemiTransparent.isSelected()); 
		mrk.setMarkerIcon((MarkerIcon)iconList.getSelectedValue());
		
		super.okButtonPressed();
	}
	
	
	
	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.BLACK); 
		g.drawRect(0, 0, getWidth()-1, getHeight()-1); 
	}
}
