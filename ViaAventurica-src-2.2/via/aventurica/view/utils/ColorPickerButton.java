package via.aventurica.view.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

public class ColorPickerButton extends javax.swing.JButton implements ActionListener{
	private final static long serialVersionUID = 1L;
	
	private Color selectedColor; 
	
	public ColorPickerButton() {
		super(); 
		setPreferredSize(new Dimension(32, 16)); 
		setText(""); 
		addActionListener(this);
	}
	
	public ColorPickerButton(Color c) { 
		this(); 
		setSelectedColor(c); 
	}
	
	
	public Color getSelectedColor() {
		return selectedColor;
	}
	
	
	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}
	
	public void actionPerformed(ActionEvent e) {
		Color newColor = JColorChooser.showDialog(this, "Farbe wählen", selectedColor);
		if(newColor!=null)
			setSelectedColor(newColor); 
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor); 
		g.fillRect(0, 0, getWidth(), getHeight()); 
	}

}
