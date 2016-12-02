/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: PrintWindow.java
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
package net.sf.jdivelog.gui.printing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.printing.FileOutputDevice;
import net.sf.jdivelog.printing.OutputDevice;
import net.sf.jdivelog.printing.PrintJob;
import net.sf.jdivelog.printing.PrinterOutputDevice;
import net.sf.jdivelog.printing.Report;
import net.sf.jdivelog.printing.report.ImageUtil;
import net.sf.jdivelog.printing.report.ReportManager;

public class PrintWindow extends JDialog implements ActionListener {

    private static final long serialVersionUID = 7462774605061573389L;
    private StatusInterface status;
    private JDiveLog logbook;
    private ReportManager reportManager;
    private JPanel jContentPanel;
    private JPanel reportBox;
    private JComboBox reportField;
    private JPanel outputBox;
    private JPanel buttonPanel;
    private JButton runButton;
    private JRadioButton printerRadioButton;
    private ButtonGroup radioButtonGroup;
    private JRadioButton fileRadioButton;
    private JComboBox printerCombobox;
    private JTextField fileField;
    private JButton chooseFileButton;
    private JComboBox mimetypeCombobox;
    
    public PrintWindow(Window parent, StatusInterface status, JDiveLog logbook) {
    	super(parent, ModalityType.APPLICATION_MODAL);
        this.status = status;
        this.logbook = logbook;
        reportManager = new ReportManager();
        initialize();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButton) {
            run();
        } else if (e.getSource() == chooseFileButton) {
            chooseFile();
        }
    }
    
    //
    // private methods
    //
    
    private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("printing"));
        setSize(600, 250);
        getContentPane().add(getJContentPanel());
    }
    
    private JPanel getJContentPanel() {
        if (jContentPanel == null) {
            jContentPanel = new JPanel();
            jContentPanel.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(5,5,5,5);
            gc.gridy = 0;
            gc.gridx = 0;
            jContentPanel.add(getReportBox(), gc);
            gc.gridy = 1;
            jContentPanel.add(getOutputBox(), gc);
            gc.gridy = 0;
            gc.gridx = 1;
            gc.gridheight = 2;
            jContentPanel.add(getButtonPanel(), gc);
        }
        return jContentPanel;
    }
    
    private JPanel getReportBox() {
        if (reportBox == null) {
            reportBox = new JPanel();
            reportBox.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(5,5,5,5);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            reportBox.add(new JLabel(Messages.getString("printing.report.report")));
            gc.gridx = 1;
            reportBox.add(getReportField(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("printing.report"));
            reportBox.setBorder(border);
        }
        return reportBox;
    }
    
    private JComboBox getReportField() {
        if (reportField == null) {
            reportField = new JComboBox(reportManager.getReportNames());
        }
        return reportField;
    }
    
    private JPanel getOutputBox() {
        if (outputBox == null) {
            outputBox = new JPanel();
            outputBox.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(5,5,5,5);
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            outputBox.add(getPrinterRadioButton(), gc);
            gc.gridx = 1;
            gc.gridwidth = 2;
            outputBox.add(getPrinterCombobox() ,gc);
            gc.gridy = 1;
            gc.gridx = 0;
            gc.gridwidth = 1;
            outputBox.add(getFileRadioButton(), gc);
            gc.gridwidth = 2;
            gc.gridx = 1;
            outputBox.add(getMimetypeCombobox() ,gc);
            gc.gridy = 2;
            gc.gridx = 1;
            gc.gridwidth = 1;
            outputBox.add(getFileField(), gc);
            gc.gridx = 2;
            outputBox.add(getChooseFileButton(), gc);
            Border border = BorderFactory.createTitledBorder(Messages.getString("printing.output"));
            outputBox.setBorder(border);
        }
        return outputBox;
    }
    
    private ButtonGroup getRadioButtonGroup() {
        if (radioButtonGroup == null) {
            radioButtonGroup = new ButtonGroup();
        }
        return radioButtonGroup;
    }
    
    private JRadioButton getPrinterRadioButton() {
        if (printerRadioButton == null) {
            printerRadioButton = new JRadioButton(Messages.getString("printing.printer"));
            getRadioButtonGroup().add(printerRadioButton);
            printerRadioButton.setSelected(true);
        }
        return printerRadioButton;
    }
    
    private JRadioButton getFileRadioButton() {
        if (fileRadioButton == null) {
            fileRadioButton = new JRadioButton(Messages.getString("printing.file"));
            getRadioButtonGroup().add(fileRadioButton);
        }
        return fileRadioButton;
    }
    
    private JComboBox getPrinterCombobox() {
        if (printerCombobox == null) {
            printerCombobox = new JComboBox(PrinterOutputDevice.getAvailablePrinters());
        }
        return printerCombobox;
    }
    
    private JTextField getFileField() {
        if (fileField == null) {
            fileField = new JTextField(100);
            fileField.setMinimumSize(new Dimension(100, 20));
        }
        return fileField;
    }
    
    private JButton getChooseFileButton() {
        if (chooseFileButton == null) {
            chooseFileButton = new JButton(Messages.getString("printing.choosefile"));
            chooseFileButton.addActionListener(this);
        }
        return chooseFileButton;
    }
    
    private JComboBox getMimetypeCombobox() {
        if (mimetypeCombobox == null) {
            mimetypeCombobox = new JComboBox(getLocalized(FileOutputDevice.getMimeTypes()));
        }
        return mimetypeCombobox;
    }
    
    private String[] getLocalized(String[] keys) {
        String[] texts = new String[keys.length];
        for (int i=0; i<keys.length; i++) {
            texts[i] = Messages.getString(keys[i]);
        }
        return texts;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1,1));
            buttonPanel.add(getRunButton());
        }
        return buttonPanel;
    }
    
    private JButton getRunButton() {
        if (runButton == null) {
            runButton = new JButton(Messages.getString("printing.run"));
            runButton.setDefaultCapable(true);
            runButton.addActionListener(this);
        }
        return runButton;
    }
    private Report getSelectedReport() {
        return reportManager.getReportByName((String) getReportField().getSelectedItem());
    }
    
    private OutputDevice getSelectedOutputDevice() {
        if (printerRadioButton.isSelected()) {
            String printername = (String)printerCombobox.getSelectedItem();
            PrinterOutputDevice dev = new PrinterOutputDevice(printername);
            return dev;
        }
        File f = new File(getFileField().getText());
        if (f.exists()) {
            int result = JOptionPane.showConfirmDialog(this, Messages.getString("printing.overwrite"), Messages.getString("printing.file_exists"), JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                return new FileOutputDevice(f.getPath(), FileOutputDevice.getMimeTypes()[getMimetypeCombobox().getSelectedIndex()]);
            }
        } else {
            return new FileOutputDevice(f.getPath(), FileOutputDevice.getMimeTypes()[getMimetypeCombobox().getSelectedIndex()]);
        }
        return null;
    }
    
    private void run() {
        Report r = getSelectedReport();
        OutputDevice od = getSelectedOutputDevice();
        if (od != null) {
            ImageUtil.setLogbook(logbook);
            PrintJob pj = new PrintJob(logbook.toString(), r, od);
            PrintRunner pr = new PrintRunner(status, pj);
            pr.start();
            dispose();
        }
    }
    
    private void chooseFile() {
        JFileChooser fc = new JFileChooser();
        int ret = fc.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            getFileField().setText(f.getPath());
        }
    }
    
    private static class PrintRunner extends Thread {
        private StatusInterface status;
        private PrintJob job;
        public PrintRunner(StatusInterface status, PrintJob job) {
            this.status = status;
            this.job = job;
        }
        public void run() {
            status.messageInfo(Messages.getString("printing"));
            status.infiniteProgressbarStart();
            try {
                job.execute();
            } finally  {
                status.infiniteProgressbarEnd();
                status.messageClear();
            }
        }
    }
    
}
