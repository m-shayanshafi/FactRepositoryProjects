/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasBlendingPanel.java
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
package net.sf.jdivelog.gui.gasblending;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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
import net.sf.jdivelog.gui.MixField;
import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.commands.UndoableCommand;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.GasFractions;
import net.sf.jdivelog.model.Mix;
import net.sf.jdivelog.model.gasblending.GasBlendingSettings;
import net.sf.jdivelog.model.gasblending.GasSource;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Panel for Gas Blending Calculations
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class GasBlendingPanel extends JPanel implements LogbookChangeListener {

    private static final long serialVersionUID = -1840683824491566224L;

    private MainWindow mainWindow;

    private JTextField fieldTankSize;

    private JTextField fieldCurrentMixPressure;

    private JTextField fieldPlannedMixPressure;

    private JButton buttonCalculate;
    
    private JButton buttonAddGasSource;
    
    private JButton buttonEditGasSource;
    
    private JButton buttonRemoveGasSource;
    
    private JButton buttonRefreshSources;

    private GasSourceTableModel gasSourceTableModel;

    private JTable gasPoolTable;

    private JTextArea outputArea;

    private JButton buttonSave;

    private JButton buttonRestore;

    private List<FillStep> currentProcedure;

    private MixField fieldCurrentGas;

    private MixField fieldPlannedGas;

    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public GasBlendingPanel() {
        super();
        initialize();
        getFieldTankSize().grabFocus();
    }
    
    public GasBlendingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.getLogbookChangeNotifier().addLogbookChangeListener(this);
        initialize();
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            load(mainWindow.getLogBook().getGasBlendingSettings());
        }
    }

    //
    // private methods
    //
    
    private void load(GasBlendingSettings settings) {
        DecimalFormat format = new DecimalFormat("######");
        if (settings != null) {
            getFieldTankSize().setText(settings.getTankVolume() == null ? null : format.format(settings.getTankVolume()));
            getFieldCurrentMixPressure().setText(settings.getCurrentPressure() == null ? null : format.format(settings.getCurrentPressure()));
            getFieldCurrentGas().setMix(settings.getCurrentMix());
            getFieldPlannedMixPressure().setText(settings.getPlannedPressure() == null ? null : format.format(settings.getPlannedPressure()));
            getFieldPlannedGas().setMix(settings.getPlannedMix());
            getGasSourceTableModel().setSources(settings.getGasSources());
        } else {
            getFieldTankSize().setText(null);
            getFieldCurrentMixPressure().setText(null);
            getFieldCurrentGas().setMix(Mix.AIR);
            getFieldPlannedMixPressure().setText(null);
            getFieldPlannedGas().setMix(Mix.AIR);
            getGasSourceTableModel().setSources(new ArrayList<GasSource>());
        }
        getGasSourceTableModel().fireTableDataChanged();
    }

    private void calculate() {
        setCurrentProcedure(null);
        if (validateInput()) {
            LinkedList<FillStep> procedure = new LinkedList<FillStep>();
            double plannedN2 = 100-getPlannedMixO2()-getPlannedMixHe();
            double currentN2 = 100-getCurrentMixO2()-getCurrentMixHe();
            double deltaO2 = getPlannedMixO2()/100 * getTextFieldAsDouble(getFieldPlannedMixPressure())
                    - getCurrentMixO2()/100 * getTextFieldAsDouble(getFieldCurrentMixPressure());
            double deltaN2 = plannedN2/100 * getTextFieldAsDouble(getFieldPlannedMixPressure())
                    - currentN2/100 * getTextFieldAsDouble(getFieldCurrentMixPressure());
            double deltaHe = getPlannedMixHe()/100 * getTextFieldAsDouble(getFieldPlannedMixPressure())
                    - getCurrentMixHe()/100 * getTextFieldAsDouble(getFieldCurrentMixPressure());

            // TODO check if all deltas are zero! (division by zero...)

            double tankSize = getTextFieldAsDouble(getFieldTankSize());
            double startPressure = getTextFieldAsDouble(getFieldCurrentMixPressure());
            double endPressure = getTextFieldAsDouble(getFieldPlannedMixPressure());

            Mix startMix = getFieldCurrentGas().getMix();
            GasTank startTank = new GasTank(tankSize, startMix, startPressure);

            // TODO check if tank must be emptied...

            GasTank currentTank = startTank.clone();
            
            GasFractions mixToFill = createMix(deltaO2, deltaHe, deltaN2);
            if (mixToFill.getOxygenFraction() < 0) {
                new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.to_much_oxygen_in_current_mix"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                return;
            }
            if (mixToFill.getNitrogenFraction() < 0) {
                new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.to_much_nitrogen_in_current_mix"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                return;
            }
            if (mixToFill.getHeliumFraction() < 0) {
                new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.to_much_helium_in_current_mix"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                return;
            }

            // check if the needed mix exists
            List<GasSource> sources = getGasSourceTableModel().findSources(mixToFill);
            Collections.sort(sources, new EmptiestGasSourceComparator(true));
            if (sources.size() > 0) {
                Iterator<GasSource> it = sources.iterator();
                while (it.hasNext()) {
                    GasSource source = it.next();
                    double diff = source.getMaxDifference(tankSize, currentTank.getPressure());
                    if (currentTank.getPressure() < endPressure && diff > 0) {
                        double fillDiff = currentTank.getPressure() + diff <= endPressure ? diff : endPressure - currentTank.getPressure();
                        addFillStep(procedure, source, currentTank.getPressure() + fillDiff, fillDiff*currentTank.getTankSize());
                        currentTank.fill(source.getMix(), fillDiff);
                    }
                }
                if (currentTank.getPressure() < endPressure) {
                    // did not succeed... reset procedure
                    procedure.clear();
                } else {
                    setCurrentProcedure(procedure);
                    String output = createProcedureOutput(procedure, currentTank);
                    getOutputArea().setText(output);
                    return;
                }
            }
            
            currentTank = startTank.clone();

            if (deltaHe > 0) {
                sources = getGasSourceTableModel().findHelium();
                if (sources.size() == 0) {
                    new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.no_helium_available"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                    return;
                }
                double maxFill = currentTank.getPressure() + deltaHe;
                
                Collections.sort(sources, new EmptiestGasSourceComparator(false));
                Iterator<GasSource> it = sources.iterator();
                while (it.hasNext()) {
                    GasSource source = it.next();
                    double maxDiff = source.getMaxDifference(currentTank.getTankSize(), startPressure);
                    if (maxDiff > 0) {
                        double maxPressure = currentTank.getPressure() + maxDiff;
                        double fillPressure = maxPressure <= maxFill ? maxPressure : maxFill;
                        if (currentTank.getPressure() < maxFill) {
                            addFillStep(procedure, source, fillPressure, (fillPressure-currentTank.getPressure())*currentTank.getTankSize());
                            currentTank.fill(source.getMix(), fillPressure-currentTank.getPressure());
                        }
                    }
                }
                if (currentTank.getPressure() < maxFill) {
                    new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_helium_available"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                    return;
                }
            }
            
            GasFractions nitroxMixToFill = createMix(deltaO2, 0, deltaN2); // since we already added the helium part
            List<GasSource> compressors = getGasSourceTableModel().findCompressors();
            GasSource compressorMinO2 = null;
            GasSource compressorMaxO2 = null;
            for (GasSource compressor : compressors) {
                if (compressorMinO2 == null || compressorMinO2.getMix().getOxygen() > compressor.getMix().getOxygen()) {
                    compressorMinO2 = compressor;
                }
                if (compressorMaxO2 == null || compressorMaxO2.getMix().getOxygen() < compressor.getMix().getOxygen()) {
                    compressorMaxO2 = compressor;
                }
            }
            if (compressorMinO2 == null || compressorMaxO2 == null) {
                new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.no_matching_compressor_found"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                return;
            }

            // check if the rest (o2/n2) is fillable with the available compressors
            if (nitroxMixToFill.getOxygenFraction() >= compressorMinO2.getMix().getOxygenFraction() && nitroxMixToFill.getOxygenFraction() <= compressorMaxO2.getMix().getOxygenFraction()) {
                // yes, it should be possible... now calculate...
                double nitroxPressureToFill = endPressure - currentTank.getPressure();
                double compMinO2PressureToFill = nitroxPressureToFill*(nitroxMixToFill.getOxygenFraction() - compressorMaxO2.getMix().getOxygenFraction()) / (compressorMinO2.getMix().getOxygenFraction() - compressorMaxO2.getMix().getOxygenFraction());
                double compMaxO2PressureToFill = nitroxPressureToFill - compMinO2PressureToFill;
                if (compressorMinO2.getPressure() <= compressorMaxO2.getPressure()) {
                    double maxPressure = currentTank.getPressure() + compressorMinO2.getMaxDifference(currentTank.getTankSize(), currentTank.getPressure());
                    if (maxPressure < currentTank.getPressure()+compMinO2PressureToFill) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_compressor_pressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    addFillStep(procedure, compressorMinO2, currentTank.getPressure() + compMinO2PressureToFill, compMinO2PressureToFill*currentTank.getTankSize());
                    currentTank.fill(compressorMinO2.getMix(), compMinO2PressureToFill);

                    maxPressure = currentTank.getPressure() + compressorMaxO2.getMaxDifference(currentTank.getTankSize(), currentTank.getPressure());
                    if (maxPressure < endPressure) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_compressor_pressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    addFillStep(procedure, compressorMaxO2, currentTank.getPressure() + compMaxO2PressureToFill, compMaxO2PressureToFill*currentTank.getTankSize());
                    currentTank.fill(compressorMaxO2.getMix(), compMaxO2PressureToFill);
                } else {
                    double maxPressure = currentTank.getPressure() + compressorMaxO2.getMaxDifference(currentTank.getTankSize(), currentTank.getPressure());
                    if (maxPressure < currentTank.getPressure()+compMaxO2PressureToFill) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_compressor_pressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    addFillStep(procedure, compressorMaxO2, currentTank.getPressure() + compMaxO2PressureToFill, compMaxO2PressureToFill*currentTank.getTankSize());
                    currentTank.fill(compressorMaxO2.getMix(), compMaxO2PressureToFill);

                    maxPressure = currentTank.getPressure() + compressorMinO2.getMaxDifference(currentTank.getTankSize(), currentTank.getPressure());
                    if (maxPressure < endPressure) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_compressor_pressure"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    addFillStep(procedure, compressorMinO2, currentTank.getPressure() + compMinO2PressureToFill, compMinO2PressureToFill*currentTank.getTankSize());
                    currentTank.fill(compressorMinO2.getMix(), compMinO2PressureToFill);
                }
            } else {
                // no, so fill up with o2 and a compressor
                GasSource compressor = null;
                Collections.sort(compressors, new MaxO2GasSourceComparator());
                for (GasSource source : compressors) {
                    if (source.getMix().getOxygenFraction() < nitroxMixToFill.getOxygenFraction() && endPressure <= source.getPressure()) {
                        compressor = source;
                        break;
                    }
                }
                if (compressor == null) {
                    new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.no_matching_compressor_found"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                    return;
                }
                double deltaFillCompressor = deltaN2 / compressor.getMix().getNitrogenFraction();
                double deltaFillO2 = endPressure - currentTank.getPressure() - deltaFillCompressor;
                
                if (deltaFillO2 > 0) {
                    sources = getGasSourceTableModel().findOxygen();
                    if (sources.size() == 0) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.no_oxygen_available"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    double maxFill = currentTank.getPressure() + deltaFillO2;
                    
                    Collections.sort(sources, new EmptiestGasSourceComparator(false));
                    Iterator<GasSource> it = sources.iterator();
                    while (it.hasNext()) {
                        GasSource source = it.next();
                        double maxDiff = source.getMaxDifference(currentTank.getTankSize(), currentTank.getPressure());
                        if (maxDiff > 0) {
                            double maxPressure = currentTank.getPressure() + maxDiff;
                            double fillPressure = maxPressure <= maxFill ? maxPressure : maxFill;
                            if (currentTank.getPressure() < maxFill) {
                                addFillStep(procedure, source, fillPressure, (fillPressure-currentTank.getPressure())*currentTank.getTankSize());
                                currentTank.fill(source.getMix(), fillPressure - currentTank.getPressure());
                            }
                        }
                    }
                    if (currentTank.getPressure() < maxFill) {
                        new MessageDialog(mainWindow, Messages.getString("mixing.error"), Messages.getString("mixing.not_enough_oxygen_available"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
                        return;
                    }
                    addFillStep(procedure, compressor, endPressure, (endPressure-currentTank.getPressure())*currentTank.getTankSize());
                    currentTank.fill(compressor.getMix(), deltaFillCompressor);
                }
            }
            setCurrentProcedure(procedure);
            String output = createProcedureOutput(procedure, currentTank);
            getOutputArea().setText(output);
        }
    }
    
    private static GasFractions createMix(double o2fraction, double heliumfraction, double nitrogenfraction) {
        double total = o2fraction + heliumfraction + nitrogenfraction;
        final double o2 = o2fraction / total;
        final double he = heliumfraction / total;
        final double n2 = nitrogenfraction / total;
        return new GasFractions() {

            public double getHeliumFraction() {
                return he;
            }

            public double getNitrogenFraction() {
                return n2;
            }

            public double getOxygenFraction() {
                return o2;
            }
            
        };
    }

    private double getCurrentMixO2() {
        return getFieldCurrentGas().getMix().getOxygenFraction() * 100;
    }

    private double getPlannedMixO2() {
        return getFieldPlannedGas().getMix().getOxygenFraction() * 100;
    }

    private double getCurrentMixHe() {
        return getFieldCurrentGas().getMix().getHeliumFraction() * 100;
    }

    private double getPlannedMixHe() {
        return getFieldPlannedGas().getMix().getHeliumFraction() * 100;
    }
    
    private void refreshSources() {
        if (currentProcedure != null) {
            for (FillStep step : currentProcedure) {
                if (!step.getSource().isCompressor()) {
                    step.getSource().addVolume(-step.getTransferVolume());
                }
            }
            currentProcedure = null;
        }
        getGasSourceTableModel().fireTableDataChanged();
    }
    
    private void setCurrentProcedure(List<FillStep> procedure) {
        currentProcedure = procedure;
        if (procedure == null) {
            getButtonRefreshSources().setEnabled(false);
        } else {
            getButtonRefreshSources().setEnabled(true);
        }
    }
    
    private void addFillStep(List<FillStep> steps, GasSource source, double endPressure, double transferVolume) {
        steps.add(new FillStep(source, endPressure, transferVolume));
    }
    
    private String createProcedureOutput(List<FillStep> steps, GasTank tank) {
        StringBuffer procedure = new StringBuffer();
        double price1 = 0.0;
        double price2 = 0.0;
        for (FillStep step : steps) {
            appendFillStatement(procedure, step.getSource(), step.getEndPressure());
            price1 += step.getSource().getPrice1()*step.getTransferVolume();
            price2 += step.getSource().getPrice2()*step.getTransferVolume();
        }
        appendFillSummary(procedure, tank, price1, price2);
        return procedure.toString();
    }

    private void appendFillStatement(StringBuffer procedure, GasSource source, double endPressure) {
        DecimalFormat format = new DecimalFormat("######"); //$NON-NLS-1$
        procedure.append(Messages.getString("mixing.fill_up_with"));
        procedure.append(" ");
        procedure.append(source.getDescription());
        procedure.append(" ");
        procedure.append(Messages.getString("mixing.up_to"));
        procedure.append(" ");
        procedure.append(format.format(endPressure));
        procedure.append(" ");
        procedure.append(UnitConverter.getDisplayPressureUnit());
        procedure.append("\n");
    }
    
    private void appendFillSummary(StringBuffer procedure, GasTank tank, double price1, double price2) {
        DecimalFormat fm = new DecimalFormat("######"); //$NON-NLS-1$
        procedure.append("\n\n");
        procedure.append(Messages.getString("mixing.your_new_mix"));
        procedure.append(":\n");
        procedure.append(Messages.getString("mixing.tank_size"));
        procedure.append(": ");
        procedure.append(fm.format(tank.getTankSize()));
        procedure.append(" ");
        procedure.append(UnitConverter.getDisplayVolumeUnit());
        procedure.append("\n");
        procedure.append(Messages.getString("mixing.tank_pressure"));
        procedure.append(": ");
        procedure.append(fm.format(tank.getPressure()));
        procedure.append(" ");
        procedure.append(UnitConverter.getDisplayPressureUnit());
        procedure.append("\n");
        procedure.append("O2: ");
        procedure.append(tank.getMix().getOxygen());
        procedure.append("% \n");
        procedure.append("He: ");
        procedure.append(tank.getMix().getHelium());
        procedure.append("% \n");
        procedure.append("N2: ");
        procedure.append(tank.getMix().getNitrogen());
        procedure.append("% \n\n");
        
        appendModLine(procedure, tank, 1.2);
        appendModLine(procedure, tank, 1.3);
        appendModLine(procedure, tank, 1.4);
        appendModLine(procedure, tank, 1.5);
        appendModLine(procedure, tank, 1.6);

        NumberFormat priceformat = NumberFormat.getNumberInstance();
        priceformat.setMaximumFractionDigits(2);
        procedure.append(Messages.getString("mixing.price1"));
        procedure.append(": ");
        procedure.append(priceformat.format(price1));
        procedure.append("\n");
        procedure.append(Messages.getString("mixing.price2"));
        procedure.append(": ");
        procedure.append(priceformat.format(price2));
        procedure.append("\n");
    }

    /**
     * @param procedure
     * @param tank
     * @param ppO2Max
     */
    private void appendModLine(StringBuffer procedure, GasTank tank, double ppO2Max) {
        DecimalFormat format = new DecimalFormat("######"); //$NON-NLS-1$
        UnitConverter uc = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.getDisplaySystem());
        DecimalFormat f = new DecimalFormat("0.0");
        procedure.append("MOD ");
        procedure.append(f.format(ppO2Max));
        procedure.append(": ");
        double modm = calcMOD(ppO2Max, tank.getMix().getOxygenFraction());
        double mod = uc.convertAltitude(modm);
        procedure.append(format.format(mod));
        procedure.append(UnitConverter.getDisplayAltitudeUnit());
        if (tank.getMix().getHelium() > 0) {
            procedure.append("   ");
            procedure.append("END: ");
            double endm = calcEND(modm, tank.getMix().getNitrogenFraction());
            double end = uc.convertAltitude(endm);
            procedure.append(format.format(end));
            procedure.append(UnitConverter.getDisplayAltitudeUnit());
        }
        procedure.append("\n");
    }
    
    /**
     * @param depth in meters
     * @param fN2 nitrogen percentage (0-1)
     * @return end depth in meters
     */
    private static double calcEND(double depth, double fN2) {
        double p = (depth/10.0)+1;
        double end = ((fN2*p/0.79)-1.0)*10;
        return end;
    }
    
    /**
     * @param ppO2Max maximum ppO2
     * @param fO2 oxygen percentage (0-1)
     * @return mod in meters
     */
    private static double calcMOD(double ppO2Max, double fO2) {
        double mod = ((ppO2Max/fO2)-1)*10;
        return mod;
    }

    private boolean validateInput() {
        if (!getFieldCurrentGas().isGasValid() ) {
            // TODO correct error message
            new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.gas_percentages_over_100_percent"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return false;
        }
        if (!getFieldPlannedGas().isGasValid()) {
            // TODO correct error message
            new MessageDialog(mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.gas_percentages_over_100_percent"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return false;
        }
        return true;
    }

    private double getTextFieldAsDouble(JTextField textfield) {
        String s = textfield.getText();
        if (s == null || "".equals(s)) {
            return 0;
        }
        return Double.parseDouble(s);
    }
    
    private void addGasSource() {
        GasSourceEditWindow gsew = new GasSourceEditWindow(this.mainWindow, this.mainWindow, getGasSourceTableModel());
        gsew.setVisible(true);
    }
    
    private void editGasSource() {
        int selected = getGasPoolTable().getSelectedRow();
        if (selected >= 0) {
            GasSource source = getGasSourceTableModel().getGasSource(selected);
            GasSourceEditWindow gsew = new GasSourceEditWindow(this.mainWindow, this.mainWindow, getGasSourceTableModel(), source);
            gsew.setVisible(true);
        } else {
            new MessageDialog(mainWindow, Messages.getString("mixing.no_gas_source_selected"), null, null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$
        }
        
    }
    
    private void removeGasSource() {
        int selected = getGasPoolTable().getSelectedRow();
        if (selected >= 0) {
            getGasSourceTableModel().removeRow(selected);
        } else {
            new MessageDialog(mainWindow, Messages.getString("mixing.no_gas_source_selected"), null, null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$
        }
    }
    
    private void storeToLogbook() {
        UndoableCommand cmd = new CommandStoreToLogbook();
        CommandManager.getInstance().execute(cmd);
    }
    
    private void restoreFromLogbook() {
        UndoableCommand cmd = new CommandRestoreFromLogbook();
        CommandManager.getInstance().execute(cmd);        
    }

    private void initialize() {
        JPanel contentPane = this;
        contentPane.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel persistencePanel = new JPanel();
        persistencePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        persistencePanel.add(getButtonSave());
        persistencePanel.add(getButtonRestore());
        contentPane.add(persistencePanel);
        
        JPanel tanksizepanel = new JPanel();
        tanksizepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tanksizepanel.add(new JLabel(Messages.getString("volume") + " [" + UnitConverter.getDisplayVolumeUnit() + "]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        tanksizepanel.add(getFieldTankSize());
        tanksizepanel.add(getButtonCalculate());
        contentPane.add(tanksizepanel);

        contentPane.add(getMixturesPanel());

        JPanel poolPane = new JPanel();
        poolPane.setLayout(new BorderLayout());
        JPanel poolButtonPane = new JPanel();
        poolButtonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        poolButtonPane.add(getButtonAddGasSource());
        poolButtonPane.add(getButtonEditGasSource());
        poolButtonPane.add(getButtonRemoveGasSource());
        poolButtonPane.add(getButtonRefreshSources());
        poolPane.add(poolButtonPane, BorderLayout.NORTH);
        JScrollPane poolTablePane = new JScrollPane(getGasPoolTable());
        poolPane.add(poolTablePane, BorderLayout.CENTER);
        Border border = BorderFactory.createTitledBorder(Messages.getString("mixing.gaspool"));
        poolPane.setBorder(border);
        contentPane.add(poolPane);
        
        JScrollPane outputPane = new JScrollPane(getOutputArea());
        outputPane.setMinimumSize(new Dimension(300,200));
        border = BorderFactory.createTitledBorder(Messages.getString("mixing.procedure"));
        outputPane.setBorder(border);
        contentPane.add(outputPane);
    }
    
    private JPanel getMixturesPanel() {
        JPanel mixturesPanel = new JPanel();
        mixturesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mixturesPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;

        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc1 = new GridBagConstraints();
        gc1.weightx = 0.5;
        gc1.weighty = 0.5;
        gc1.anchor = GridBagConstraints.EAST;
        gc1.insets = new java.awt.Insets(1, 1, 1, 1);
        gc1.gridy = 0;
        gc1.gridx = 0;
        gc1.fill = GridBagConstraints.NONE;
        currentPanel.add(new JLabel(Messages.getString("pressure") + " [" + UnitConverter.getDisplayPressureUnit() + "]"), gc1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        gc1.gridx = 1;
        gc1.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(getFieldCurrentMixPressure(), gc1);
        gc1.gridy = 1;
        gc1.gridx = 0;
        gc1.fill = GridBagConstraints.NONE;
        currentPanel.add(new JLabel(Messages.getString("gas")), gc1); //$NON-NLS-1$
        gc1.gridx = 1;
        gc1.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(getFieldCurrentGas(), gc1);
        Border border = BorderFactory.createTitledBorder(Messages.getString("mixing.current_content"));
        currentPanel.setBorder(border);
        gc.gridy = 0;
        gc.gridx = 0;
        mixturesPanel.add(currentPanel, gc);

        JPanel plannedPanel = new JPanel();
        plannedPanel.setLayout(new GridBagLayout());
        gc1.gridy = 0;
        gc1.gridx = 0;
        gc1.fill = GridBagConstraints.NONE;
        plannedPanel.add(new JLabel(Messages.getString("pressure") + " [" + UnitConverter.getDisplayPressureUnit() + "]"), gc1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        gc1.gridx = 1;
        gc1.fill = GridBagConstraints.HORIZONTAL;
        plannedPanel.add(getFieldPlannedMixPressure(), gc1);
        gc1.gridy = 1;
        gc1.gridx = 0;
        gc1.fill = GridBagConstraints.NONE;
        plannedPanel.add(new JLabel(Messages.getString("gas")), gc1); //$NON-NLS-1$
        gc1.gridx = 1;
        gc1.fill = GridBagConstraints.HORIZONTAL;
        plannedPanel.add(getFieldPlannedGas(), gc1);
        border = BorderFactory.createTitledBorder(Messages.getString("mixing.planned_content"));
        plannedPanel.setBorder(border);
        gc.gridy = 0;
        gc.gridx = 1;
        mixturesPanel.add(plannedPanel, gc);

        return mixturesPanel;
    }
    
    private MixField getFieldCurrentGas() {
        if (fieldCurrentGas == null) {
            fieldCurrentGas = new MixField(mainWindow, mainWindow.getGasDatabase());
        }
        return fieldCurrentGas;
    }
    
    private MixField getFieldPlannedGas() {
        if (fieldPlannedGas == null) {
            fieldPlannedGas = new MixField(mainWindow, mainWindow.getGasDatabase());
        }
        return fieldPlannedGas;
    }

    private JTextField getFieldTankSize() {
        if (fieldTankSize == null) {
            fieldTankSize = new JTextField();
            fieldTankSize.setColumns(5);
            fieldTankSize.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    fieldTankSize.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldTankSize;
    }

    private JTextField getFieldCurrentMixPressure() {
        if (fieldCurrentMixPressure == null) {
            fieldCurrentMixPressure = new JTextField();
            fieldCurrentMixPressure.setColumns(5);
            fieldCurrentMixPressure.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    fieldCurrentMixPressure.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldCurrentMixPressure;
    }

    private JTextField getFieldPlannedMixPressure() {
        if (fieldPlannedMixPressure == null) {
            fieldPlannedMixPressure = new JTextField();
            fieldPlannedMixPressure.setColumns(5);
            fieldPlannedMixPressure.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    fieldPlannedMixPressure.selectAll();
                }

                public void focusLost(FocusEvent e) {
                }
                
            });
        }
        return fieldPlannedMixPressure;
    }

    private GasSourceTableModel getGasSourceTableModel() {
        if (gasSourceTableModel == null) {
            gasSourceTableModel = new GasSourceTableModel();
        }
        return gasSourceTableModel;
    }

    private JTable getGasPoolTable() {
        if (gasPoolTable == null) {
            gasPoolTable = new JTable(getGasSourceTableModel(), getGasSourceTableModel().getColumnModel());
            gasPoolTable.addKeyListener(new KeyListener() {

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
            gasPoolTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        editGasSource();
                    }
                }
            });
            gasPoolTable.setRowHeight(30);
        }
        getGasSourceTableModel().getColumnModel().getColumn(0).setWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(0).setMinWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(0).setMaxWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(1).setWidth(200);
        getGasSourceTableModel().getColumnModel().getColumn(1).setMinWidth(150);
        getGasSourceTableModel().getColumnModel().getColumn(1).setMaxWidth(400);
        getGasSourceTableModel().getColumnModel().getColumn(2).setMinWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(2).setMaxWidth(50);
        getGasSourceTableModel().getColumnModel().getColumn(3).setMinWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(3).setMaxWidth(50);
        getGasSourceTableModel().getColumnModel().getColumn(4).setMinWidth(30);
        getGasSourceTableModel().getColumnModel().getColumn(4).setMaxWidth(50);
        getGasSourceTableModel().getColumnModel().getColumn(5).setWidth(200);
        getGasSourceTableModel().getColumnModel().getColumn(5).setMinWidth(100);
        return gasPoolTable;
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
    
    private JButton getButtonRefreshSources() {
        if (buttonRefreshSources == null) {
            buttonRefreshSources = new JButton(Messages.getString("mixing.refresh_sources")); //$NON-NLS-1$
            buttonRefreshSources.setEnabled(false);
            buttonRefreshSources.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    refreshSources();
                }
                
            });
        }
        return buttonRefreshSources;
    }

    private JTextArea getOutputArea() {
        if (outputArea == null) {
            // outputArea = new JTextArea(250,30);
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
        return outputArea;
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

    //
    // inner classes
    //

    private class EmptiestGasSourceComparator implements Comparator<GasSource> {

        private boolean compressorsFirst;

        private EmptiestGasSourceComparator(boolean compressorsFirst) {
            this.compressorsFirst = compressorsFirst;
        }

        public int compare(GasSource source1, GasSource source2) {
            if (compressorsFirst) {
                if (source1.isCompressor()) {
                    return -1;
                }
                if (source2.isCompressor()) {
                    return 1;
                }
            }
            return (int) (source1.getPressure() - source2.getPressure());
        }

    }
    
    private class MaxO2GasSourceComparator implements Comparator<GasSource> {
        
        public int compare(GasSource source1, GasSource source2) {
            if (source1.getMix().getOxygen() == source2.getMix().getOxygen()) {
                return 0;
            }
            if (source1.getMix().getOxygen() > source2.getMix().getOxygen()) {
                return -1;
            }
            return 1;
        }
    }
    
    private class CommandStoreToLogbook implements UndoableCommand {
        
        private GasBlendingSettings oldSettings;
        private GasBlendingSettings newSettings;
        private boolean oldChanged;

        public void undo() {
            mainWindow.getLogBook().setGasBlendingSettings(oldSettings);
            mainWindow.setChanged(oldChanged);
        }

        public void redo() {
            mainWindow.getLogBook().setGasBlendingSettings(newSettings);
            mainWindow.setChanged(true);
        }

        public void execute() {
            this.oldChanged = mainWindow.isChanged();
            oldSettings = mainWindow.getLogBook().getGasBlendingSettings();
            newSettings = new GasBlendingSettings();
            newSettings.setTankVolume(getValidDouble(getFieldTankSize()));
            newSettings.setCurrentMix(getFieldCurrentGas().getMix());
            newSettings.setCurrentPressure(getValidDouble(getFieldCurrentMixPressure()));
            newSettings.setPlannedMix(getFieldPlannedGas().getMix());
            newSettings.setPlannedPressure(getValidDouble(getFieldPlannedMixPressure()));
            newSettings.setGasSources(getGasSourceTableModel().getSources());
            mainWindow.getLogBook().setGasBlendingSettings(newSettings);
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
    
    private class CommandRestoreFromLogbook implements UndoableCommand {

        private GasBlendingSettings oldSettings;
        private GasBlendingSettings newSettings;

        public void undo() {
            load(oldSettings);
        }

        public void redo() {
            load(newSettings);
        }

        public void execute() {
            oldSettings = new GasBlendingSettings();
            oldSettings.setTankVolume(getValidDouble(getFieldTankSize()));
            oldSettings.setCurrentMix(getFieldCurrentGas().getMix());
            oldSettings.setCurrentPressure(getValidDouble(getFieldCurrentMixPressure()));
            oldSettings.setPlannedMix(getFieldPlannedGas().getMix());
            oldSettings.setPlannedPressure(getValidDouble(getFieldPlannedMixPressure()));
            oldSettings.setGasSources(getGasSourceTableModel().getSources());
            newSettings = mainWindow.getLogBook().getGasBlendingSettings();
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

}
