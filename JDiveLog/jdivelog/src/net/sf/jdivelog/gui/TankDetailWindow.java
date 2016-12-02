/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: TankDetailWindow.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.UnitConverter;


/**
 * Description: Window to edit the tank details
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class TankDetailWindow extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = 3977581394245530418L;

    public static final String[] TANKTYPES = {Messages.getString("tanktype_unknown"), Messages.getString("tanktype_steel"), Messages.getString("tanktype_aluminium"), Messages.getString("tanktype_carbon")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    
    private static NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("numberformat")); //$NON-NLS-1$
    
    private boolean newTank;
    private TankTableModel tankTableModel = null;
    private Tank tank;

	private javax.swing.JPanel jContentPane = null;
	
	private JPanel tankPanel = null;
	private JLabel labelVolume = null;
	private JTextField fieldVolume = null;
	private JLabel labelType = null;
	private JComboBox fieldType = null;
	private JLabel labelPstart = null;
	private JTextField fieldPstart = null;
	private JLabel labelPend = null;
	private JTextField fieldPend = null;

	private JPanel buttonPanel = null;
	private JButton closeButton = null;
	private JButton cancelButton = null;
	
	UnitConverter toDisplay = null;
	UnitConverter fromDisplay = null;

    private MixField fieldMix;

    private JLabel labelGas;

    private final MixDatabase db;
	
	/**
	 * This is the constructor for editing a tank.
	 * @param parent 
	 * @param db
	 * @param tankTableModel The TankTableModel which should be notified about changes.
	 * @param tank The tank to edit.
	 * @param diveSystem The Unit System the dive is stored
	 * @param displaySystem The Unit System of the Display
	 */
	public TankDetailWindow(Window parent, MixDatabase db,TankTableModel tankTableModel, Tank tank, int diveSystem, int displaySystem) {
		super(parent, ModalityType.APPLICATION_MODAL);
        this.db = db;
		this.tankTableModel = tankTableModel;
		this.tank = tank;
		this.newTank = false;
		this.toDisplay = new UnitConverter(diveSystem, displaySystem);
		this.fromDisplay = new UnitConverter(displaySystem, diveSystem);
		initialize();
		load();
	}
	
	/**
	 * This is the constructor for adding a new tank.
	 * @param parent 
	 * @param db
	 * @param tankTableModel The TankTableModel to which the new tank must be added.
	 * @param diveSystem The Unit System the dive is stored
	 * @param displaySystem The Unit System of the Display
	 */
	public TankDetailWindow(Window parent, MixDatabase db, TankTableModel tankTableModel, int diveSystem, int displaySystem) {
		super(parent, ModalityType.APPLICATION_MODAL);
        this.db = db;
	    this.tankTableModel = tankTableModel;
	    this.tank = new Tank();
	    this.tank.setGas(new Gas());
	    this.newTank = true;
		this.toDisplay = new UnitConverter(diveSystem, displaySystem);
		this.fromDisplay = new UnitConverter(displaySystem, diveSystem);
	    initialize();
	}
	
	private void load() {
	    if (this.tank.getGas().getTankvolume() != null) {
	        getFieldVolume().setText(String.valueOf(toDisplay.convertVolume(this.tank.getGas().getTankvolume())));
	    }
	    int type = 0;
	    for (int i=0; i<TANKTYPES.length; i++) {
	        if (TANKTYPES[i].equals(this.tank.getType())) {
	            type = i;
	        }
	    }
	    getFieldType().setSelectedIndex(type);
	    getFieldMix().setMix(tank.getGas().getMix());
	    if (this.tank.getGas().getPstart() != null) {
	        getFieldPstart().setText(String.valueOf(DECIMALFORMAT.format(toDisplay.convertPressure(this.tank.getGas().getPstart()))));
	    }
	    if (this.tank.getGas().getPend() != null) {
	        getFieldPend().setText(String.valueOf(DECIMALFORMAT.format(toDisplay.convertPressure(this.tank.getGas().getPend()))));
	    }
	}
	
	private void save() {
	    if (getFieldVolume().getText() != null && !"".equals(getFieldVolume().getText())) { //$NON-NLS-1$
	        try {
	            this.tank.getGas().setTankvolume(fromDisplay.convertVolume(new Double(getFieldVolume().getText())));
	        } catch (NumberFormatException ex) {
	            this.tank.getGas().setTankvolume((Double)null);
	        }
	    } else {
	        this.tank.getGas().setTankvolume((Double)null);
	    }
	    this.tank.setType(TANKTYPES[getFieldType().getSelectedIndex()]);
	    this.tank.getGas().setMix(getFieldMix().getMix());
	    if (getFieldPstart().getText() != null && !"".equals(getFieldPstart().getText())) { //$NON-NLS-1$
	        try {
	            this.tank.getGas().setPstart(fromDisplay.convertPressure(new Double(getFieldPstart().getText())));
	        } catch (NumberFormatException ex) {
	            this.tank.getGas().setPstart((Double)null);
	        }
	    } else {
	        this.tank.getGas().setPstart((Double)null);
	    }
	    if (getFieldPend().getText() != null && !"".equals(getFieldPend().getText())) { //$NON-NLS-1$
	        try {
	            this.tank.getGas().setPend(fromDisplay.convertPressure(new Double(getFieldPend().getText())));
	        } catch (NumberFormatException ex) {
	            this.tank.getGas().setPend((Double)null);
	        }
	    } else {
	        this.tank.getGas().setPend((Double)null);
	    }
	}
	
	private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
		this.setSize(400,230);
		this.setContentPane(getJContentPane());
		this.setTitle(Messages.getString("tankdetails")); //$NON-NLS-1$
		this.setName(Messages.getString("tankdetails")); //$NON-NLS-1$
        new MnemonicFactory(this);
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getTankPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	private JPanel getTankPanel() {
	    if (tankPanel == null) {
	        tankPanel = new JPanel();
	        tankPanel.setLayout(null);
	        labelVolume = new JLabel(Messages.getString("volume")+" ["+UnitConverter.getDisplayVolumeUnit()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	        labelType = new JLabel(Messages.getString("tanktype")); //$NON-NLS-1$
	        labelGas = new JLabel(Messages.getString("gas")); //$NON-NLS-1$
	        labelPstart = new JLabel(Messages.getString("pressure_start")); //$NON-NLS-1$
	        labelPend = new JLabel(Messages.getString("pressure_end")); //$NON-NLS-1$
	        tankPanel.setLayout(new GridBagLayout());
	        GridBagConstraints gc = new GridBagConstraints();
	        gc.insets = new Insets(5,5,5,5);
	        gc.anchor = GridBagConstraints.NORTHWEST;
	        gc.gridy = 0;
	        gc.gridx = 0;
	        tankPanel.add(labelVolume, gc);
	        gc.gridx = 1;
	        tankPanel.add(getFieldVolume(), gc);
	        gc.gridy++;
	        gc.gridx = 0;
	        tankPanel.add(labelType, gc);
	        gc.gridx = 1;
	        tankPanel.add(getFieldType(), gc);
            gc.gridy++;
            gc.gridx = 0;
	        tankPanel.add(labelGas, gc);
            gc.gridx = 1;
	        tankPanel.add(getFieldMix(), gc);
            gc.gridy++;
            gc.gridx = 0;
	        tankPanel.add(labelPstart, gc);
            gc.gridx = 1;
	        tankPanel.add(getFieldPstart(), gc);
            gc.gridy++;
            gc.gridx = 0;
	        tankPanel.add(labelPend, gc);
            gc.gridx = 1;
	        tankPanel.add(getFieldPend(), gc);
	    }
	    return tankPanel;
	}
	
	private JTextField getFieldVolume() {
	    if (fieldVolume == null) {
	        fieldVolume = new JTextField(10);
	    }
	    return fieldVolume;
	}
	
	private MixField getFieldMix() {
	    if (fieldMix == null) {
	        fieldMix = new MixField(this, db);
	    }
	    return fieldMix;
	}
	
	private JComboBox getFieldType() {
	    if (fieldType == null) {
	        fieldType = new JComboBox(TANKTYPES);
	    }
	    return fieldType;
	}
	
	private JTextField getFieldPstart() {
	    if (fieldPstart == null) {
	        fieldPstart = new JTextField(10);
	    }
	    return fieldPstart;
	}
	
	private JTextField getFieldPend() {
	    if (fieldPend == null) {
	        fieldPend = new JTextField(10);
	    }
	    return fieldPend;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx=0.5;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,30,5,5);
			buttonPanel.add(getCloseButton(), gridBagConstraints1);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,30);
			buttonPanel.add(getCancelButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}
	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
			closeButton.addActionListener(this);
		}
		return closeButton;
	}
	
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            save();
            if (this.newTank) {
                this.tankTableModel.addTank(this.tank);
            } else {
                this.tankTableModel.fireTableDataChanged();
            }
            dispose();
        } else if (e.getSource() == cancelButton){
            dispose();
        }
    }

}
