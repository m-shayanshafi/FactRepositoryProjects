/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasOverflowPanel.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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
package net.sf.jdivelog.gui.gasblending;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.LogbookChangeEvent;
import net.sf.jdivelog.gui.LogbookChangeListener;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSettings;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSource;
import net.sf.jdivelog.util.UnitConverter;

public class GasOverflowPanel extends JPanel implements LogbookChangeListener {
	private static final long serialVersionUID = -5926277352295399067L;

	private MainWindow mainWindow;

    private JTextField fieldStartTankSize;

    private JTextField fieldStartTankPressure;

    private JTextField fieldplannedconsumptionpressure;

    private JTextField fieldMinStartPressure;

    private JButton buttonCalculate;
    
    private JButton buttonAddGasSource;
    
    private JButton buttonEditGasSource;
    
    private JButton buttonRemoveGasSource;
    
    private GasOverflowTableModel gasSourceTableModel;

    private JTable gasSourceTable;

    private JTextArea outputArea;

    private JButton buttonSave;

    private JButton buttonRestore;

    public GasOverflowPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.getLogbookChangeNotifier().addLogbookChangeListener(this);
        initialize();
    }
    
    private void initialize() {
    	    	
        JPanel contentPane = this;
        contentPane.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel persistencePanel = new JPanel();
        persistencePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        persistencePanel.add(getButtonSave());
        persistencePanel.add(getButtonRestore());
        contentPane.add(persistencePanel);

        
        JPanel tankstartpanel = new JPanel();
        tankstartpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3,3,3,3);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridy = 0;
        gc.gridx = 0;
        p.add(new JLabel(Messages.getString("overfill.plannedconsumptionpressure") + " [" + UnitConverter.getDisplayVolumeUnit() + "]"), gc);
        gc.gridx = 1;
        p.add(getFieldStartTankSize(), gc);
        gc.gridy = 1;
        gc.gridx = 0;
        p.add(new JLabel(Messages.getString("overfill.pressurestarttank") + " [" + UnitConverter.getDisplayPressureUnit() + "]"), gc); //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
        gc.gridx = 1;
        p.add(getFieldStartTankPressure(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        p.add(new JLabel(Messages.getString("overfill.plannedconsumptionpressure") + " [" + UnitConverter.getDisplayPressureUnit() + "]"), gc); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        gc.gridx = 1;
        p.add(getfieldplannedconsumptionpressure(), gc);
        gc.gridy = 3;
        gc.gridx = 0;
        p.add(new JLabel(Messages.getString("minstartpressure") + " [" + UnitConverter.getDisplayPressureUnit() + "]"), gc); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        gc.gridx = 1;
        p.add(getFieldMinStartPressure(), gc);
        gc.gridwidth = 2;
        gc.gridy = 4;
        p.add(getButtonCalculate(), gc);
        tankstartpanel.add(p);           
        Border border = BorderFactory.createTitledBorder(Messages.getString("overfill.filltank"));
        tankstartpanel.setBorder(border);        
        contentPane.add(tankstartpanel);
        
        JPanel poolPane = new JPanel();
        poolPane.setLayout(new BorderLayout());
        JPanel poolButtonPane = new JPanel();
        poolButtonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        poolButtonPane.add(getButtonAddGasSource());
        poolButtonPane.add(getButtonEditGasSource());
        poolButtonPane.add(getButtonRemoveGasSource());
        poolPane.add(poolButtonPane, BorderLayout.NORTH);
        JScrollPane poolTablePane = new JScrollPane(getGasSourceTable());
        poolPane.add(poolTablePane, BorderLayout.CENTER);
        border = BorderFactory.createTitledBorder(Messages.getString("mixing.gaspool"));
        poolPane.setBorder(border);
        contentPane.add(poolPane);
        
        JScrollPane outputPane = new JScrollPane(getOutputArea());
        outputPane.setMinimumSize(new Dimension(300,200));
        border = BorderFactory.createTitledBorder(Messages.getString("mixing.procedure"));
        outputPane.setBorder(border);
        contentPane.add(outputPane);

    }
    
    private JButton getButtonSave() {
        if (buttonSave == null) {
            buttonSave = new JButton(Messages.getString("mixing.store_to_logbook"));
            buttonSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                	storeToLogbook();
                }                
            });
        }
        return buttonSave;
    }
    
    private JButton getButtonRestore() {
        if (buttonRestore == null) {
            buttonRestore = new JButton(Messages.getString("mixing.restore_from_logbook"));
            buttonRestore.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	restoreFromLogbook();
                }
                
            });
        }
        return buttonRestore;
    }

    private JButton getButtonCalculate() {
        if (buttonCalculate == null) {
            buttonCalculate = new JButton(Messages.getString("calculate")); //$NON-NLS-1$
            buttonCalculate.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	calculate();
                }

            });
        }
        return buttonCalculate;
    }
    
    private JTextField getFieldStartTankPressure() {
        if (fieldStartTankPressure == null) {
        	fieldStartTankPressure = new JTextField();
        	fieldStartTankPressure.setColumns(5);
        	fieldStartTankPressure.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                	fieldStartTankPressure.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldStartTankPressure;
    }
    private JTextField getfieldplannedconsumptionpressure() {
        if (fieldplannedconsumptionpressure == null) {
        	fieldplannedconsumptionpressure = new JTextField();
        	fieldplannedconsumptionpressure.setColumns(5);
        	fieldplannedconsumptionpressure.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                	fieldplannedconsumptionpressure.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldplannedconsumptionpressure;
    }

    private JTextField getFieldMinStartPressure() {
        if (fieldMinStartPressure == null) {
        	fieldMinStartPressure = new JTextField();
        	fieldMinStartPressure.setColumns(5);
        	fieldMinStartPressure.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                	fieldMinStartPressure.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldMinStartPressure;
    }
    
    private JTextField getFieldStartTankSize() {
        if (fieldStartTankSize == null) {
        	fieldStartTankSize = new JTextField();
        	fieldStartTankSize.setColumns(5);
        	fieldStartTankSize.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                	fieldStartTankSize.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldStartTankSize;
    }

    private JTextArea getOutputArea() {
        if (outputArea == null) {
            // outputArea = new JTextArea(250,30);
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
        return outputArea;
    }
    
    private JTable getGasSourceTable() {
        if (gasSourceTable == null) {
        	gasSourceTable = new JTable(getGasOverflowTableModel(), getGasOverflowTableModel().getColumnModel());
        	gasSourceTable.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent arg0) {
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    	editGasSource();
                    }
                }

                public void keyReleased(KeyEvent arg0) {
                }
                
            });
        	gasSourceTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                    	editGasSource();
                    }
                }
            });
        	gasSourceTable.setRowHeight(30);
        }
        getGasOverflowTableModel().getColumnModel().getColumn(0).setWidth(200);
        getGasOverflowTableModel().getColumnModel().getColumn(1).setWidth(50);
        getGasOverflowTableModel().getColumnModel().getColumn(2).setWidth(50);
        return gasSourceTable;
    }
    
    private GasOverflowTableModel getGasOverflowTableModel() {
        if (gasSourceTableModel == null) {
            gasSourceTableModel = new GasOverflowTableModel();
        }
        return gasSourceTableModel;
    }
    
    private JButton getButtonAddGasSource() {
        if (buttonAddGasSource == null) {
            buttonAddGasSource = new JButton(Messages.getString("mixing.add_gas_source")); //$NON-NLS-1$
            buttonAddGasSource.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    addGasSource();
                }
                
            });
        }
        return buttonAddGasSource;
    }

    private JButton getButtonEditGasSource() {
        if (buttonEditGasSource == null) {
            buttonEditGasSource = new JButton(Messages.getString("mixing.edit_gas_source")); //$NON-NLS-1$
            buttonEditGasSource.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	editGasSource();
               }                
            });
        }
        return buttonEditGasSource;
    }
    
    private JButton getButtonRemoveGasSource() {
        if (buttonRemoveGasSource == null) {
            buttonRemoveGasSource = new JButton(Messages.getString("mixing.remove_gas_source")); //$NON-NLS-1$
            buttonRemoveGasSource.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	removeGasSource();
                }
                
            });
        }
        return buttonRemoveGasSource;
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load(mainWindow.getLogBook().getGasOverflowSettings());
        }
    }
    
    private void storeToLogbook() {
        UndoableCommand cmd = new CommandStoreToLogbook();
        CommandManager.getInstance().execute(cmd);
    }

    private class CommandStoreToLogbook implements UndoableCommand {
        
        private GasOverflowSettings oldSettings;
        private GasOverflowSettings newSettings;
        private boolean oldChanged;

        public void undo() {
            mainWindow.getLogBook().setGasOverflowSettings(oldSettings);
            mainWindow.setChanged(oldChanged);
        }

        public void redo() {
            mainWindow.getLogBook().setGasOverflowSettings(newSettings);
            mainWindow.setChanged(true);
        }

        public void execute() {
            this.oldChanged = mainWindow.isChanged();
            oldSettings = mainWindow.getLogBook().getGasOverflowSettings();
            newSettings = new GasOverflowSettings();
            newSettings.setStartTankSize(getValidDouble(getFieldStartTankSize()));
            newSettings.setStartTankPressure(getValidDouble(getFieldStartTankPressure()));
            newSettings.setPlannedConsumptionPressure(getValidDouble(getfieldplannedconsumptionpressure()));
            newSettings.setMinStartPressure(getValidDouble(getFieldMinStartPressure()));
            newSettings.setGasSources(getGasOverflowTableModel().getSources());
            mainWindow.getLogBook().setGasOverflowSettings(newSettings);
            mainWindow.setChanged(true);
        }
        
        private Double getValidDouble(JTextField field) {
            try {
                return Double.parseDouble(field.getText());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
    }

    private void load(GasOverflowSettings settings) {
        DecimalFormat format = new DecimalFormat("######");
        if (settings != null) {
        	getFieldStartTankSize().setText(settings.getStartTankSize() == null ? null : format.format(settings.getStartTankSize()));
        	getFieldStartTankPressure().setText(settings.getStartTankPressure() == null ? null : format.format(settings.getStartTankPressure()));
        	getfieldplannedconsumptionpressure().setText(settings.getPlannedConsumptionPressure()== null ? null : format.format(settings.getPlannedConsumptionPressure()));
        	getFieldMinStartPressure().setText(settings.getMinStartPressure() == null ? null : format.format(settings.getMinStartPressure()));
        	getGasOverflowTableModel().setSources(settings.getGasSources());
        } else {
        	getFieldStartTankSize().setText(null);
        	getFieldStartTankPressure().setText(null);
        	getfieldplannedconsumptionpressure().setText(null);
        	getFieldMinStartPressure().setText(null);
            getGasOverflowTableModel().setSources(new ArrayList<GasOverflowSource>());
        }
        getGasOverflowTableModel().fireTableDataChanged();
    }

    private void restoreFromLogbook() {
        UndoableCommand cmd = new CommandRestoreFromLogbook();
        CommandManager.getInstance().execute(cmd);        
    }
    
    private class CommandRestoreFromLogbook implements UndoableCommand {

        private GasOverflowSettings oldSettings;
        private GasOverflowSettings newSettings;

        public void undo() {
            load(oldSettings);
        }

        public void redo() {
            load(newSettings);
        }

        public void execute() {
            oldSettings = new GasOverflowSettings();
            oldSettings.setStartTankSize(getValidDouble(getFieldStartTankSize()));
            oldSettings.setStartTankPressure(getValidDouble(getFieldStartTankPressure()));
            oldSettings.setPlannedConsumptionPressure(getValidDouble(getfieldplannedconsumptionpressure()));
            oldSettings.setMinStartPressure(getValidDouble(getFieldMinStartPressure()));
            oldSettings.setGasSources(getGasOverflowTableModel().getSources());
            newSettings = mainWindow.getLogBook().getGasOverflowSettings();
            load(newSettings);
        }
                
        private Double getValidDouble(JTextField field) {
            try {
                return Double.parseDouble(field.getText());
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
    
    private void addGasSource() {
        GasOverflowEditWindow gsew = new GasOverflowEditWindow(this.mainWindow, this.mainWindow, getGasOverflowTableModel());
        gsew.setVisible(true);
    }

    private void editGasSource() {
        int selected = getGasSourceTable().getSelectedRow();
        if (selected >= 0) {
            GasOverflowSource source = getGasOverflowTableModel().getSource(selected);
            GasOverflowEditWindow gsew = new GasOverflowEditWindow(this.mainWindow, this.mainWindow, getGasOverflowTableModel(), source);
            gsew.setVisible(true);
        } else {
            new MessageDialog(mainWindow, Messages.getString("mixing.no_gas_source_selected"), null, null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$
        }
        
    }
    
    private void removeGasSource() {
        int selected = getGasSourceTable().getSelectedRow();
        if (selected >= 0) {
        	getGasOverflowTableModel().removeRow(selected);
        } else {
            new MessageDialog(mainWindow, Messages.getString("mixing.no_gas_source_selected"), null, null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$
        }
    }

    
    private class EmptiestGasSourceComparator implements Comparator<GasOverflowSource> {

        private EmptiestGasSourceComparator() {
        }

        public int compare(GasOverflowSource source1, GasOverflowSource source2) {
            return (int) (source1.getTankpressure() - source2.getTankpressure());
        }

    }
    
    //the main calculation routine for the overfill procedure
    private void calculate() {
    	
    	int i = 0;
    	int j = 0; 
    	DecimalFormat df = new DecimalFormat( "0.00" );
    	StringBuffer output = new StringBuffer();
    	Double[] pressureFilltank = null;
    	Double[] volumeFilltank = null;
    	String[] descriptionFilltank = null;
    	
        if (fieldMinStartPressure.getText() == null || fieldMinStartPressure.getText().equals("")) {
            new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("overfill.no_minpressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return;
        }
    	
        if (fieldStartTankPressure.getText() == null || fieldStartTankPressure.getText().equals("")) {
            new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("overfill.no_startpressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return;
        }
        
        if (fieldplannedconsumptionpressure.getText() == null || fieldplannedconsumptionpressure.getText().equals("")) {
            new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("overfill.no_consumptionpressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return;
        }
        
        if (fieldStartTankSize.getText() == null || fieldStartTankSize.getText().equals("")) {
            new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("overfill.no_starttanksize"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return;
        }
 	
    	
    	//Read the fill tanks pressures
        List<GasOverflowSource> sources = getGasOverflowTableModel().getSources();
        Collections.sort(sources, new EmptiestGasSourceComparator());
        if (sources.size() > 0) {
        	pressureFilltank = new Double[sources.size()];
        	volumeFilltank = new Double[sources.size()];
        	descriptionFilltank = new String[sources.size()];
            Iterator<GasOverflowSource> it = sources.iterator();
            while (it.hasNext()) {
            	GasOverflowSource source = it.next();
            	descriptionFilltank[i] = source.getTankdescription();
            	pressureFilltank[i] = source.getTankpressure();
            	volumeFilltank[i] = source.getTanksize();
            	i++;
            }
        }

        output.append(Messages.getString("overfill.startprocedure") + ":\n");
    	
    	//start calculation until 
        i = 1; 
    	Double pressureDivetank = getValidDouble(fieldStartTankPressure);
    	while (pressureDivetank >= getValidDouble(fieldMinStartPressure)) {
    		output.append("\n" + i + "." + Messages.getString("overfill.startpressuredivetank") + " : " + df.format(pressureDivetank) + " " + UnitConverter.getDisplayPressureUnit() + "\n");
    		output.append("################################################################\n");
    		pressureDivetank = pressureDivetank - getValidDouble(fieldplannedconsumptionpressure);
    		
    		j = 0;
    		if (pressureDivetank <= getValidDouble(fieldMinStartPressure)) {
    			while (pressureDivetank <= getValidDouble(fieldMinStartPressure) && j < sources.size()) {
    				pressureDivetank = (((pressureFilltank[j] * volumeFilltank[j]) + (pressureDivetank * getValidDouble(fieldStartTankSize))) / (volumeFilltank[j] + getValidDouble(fieldStartTankSize)));
    				pressureFilltank[j] = pressureDivetank;
    				output.append(Messages.getString("overfill.overfilltank") + " " + descriptionFilltank[j] + ": " +Messages.getString("overfill.overfilltank_endpressure") + " " + df.format(pressureFilltank[j]) + " " + UnitConverter.getDisplayPressureUnit() + "\n");
    				if (pressureDivetank <= getValidDouble(fieldMinStartPressure)) {
    					j++;
    				}
    			}
    		}
    		i++;    		
    	}
    	getOutputArea().setText(output.toString());
    }

    private Double getValidDouble(JTextField field) {
        try {
            return Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
