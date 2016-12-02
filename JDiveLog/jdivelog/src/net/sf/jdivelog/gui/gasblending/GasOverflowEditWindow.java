package net.sf.jdivelog.gui.gasblending;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.MnemonicFactory;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSource;

public class GasOverflowEditWindow extends JDialog {

	/**
	 * 
	 */
	private JTextField tankdescription;
	private JTextField tanksize;
    private JTextField tankpressure;
    private JPanel buttonPanel;    
    private JButton closeButton;
    private JButton cancelButton;

	private static final long serialVersionUID = 1L;
	
	private MainWindow mainWindow;
	
	private final GasOverflowTableModel gasSourceTableModel;
    private final GasOverflowSource source;

	public GasOverflowEditWindow(Window parent, MainWindow mainWindow, GasOverflowTableModel gasSourceTableModel) {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	this.mainWindow = mainWindow;
        this.gasSourceTableModel = gasSourceTableModel;
        this.source = null;
        initialize();
        load();
        new MnemonicFactory(this);
	}
	
    public GasOverflowEditWindow(Window parent, MainWindow mainWindow, GasOverflowTableModel gasSourceTableModel, GasOverflowSource source) {
    	super(parent, ModalityType.APPLICATION_MODAL);
    	this.mainWindow = mainWindow;
        this.gasSourceTableModel = gasSourceTableModel;
        this.source = source;
        initialize();
        load();
    }
	
    private void initialize() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        setTitle(Messages.getString("mixing.edit_gas_source")); //$NON-NLS-1$
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.insets = new Insets(3,3,3,3);
        gc.gridy = 0;
        gc.gridx = 0;
        panel.add(new JLabel(Messages.getString("overfill.gasoverflowsource.tankdescription")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        panel.add(getTankDescription(), gc);
        gc.gridy = 1;
        gc.gridx = 0;
        panel.add(new JLabel(Messages.getString("mixing.gasoverflowsource.tanksize")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        panel.add(getTankSize(), gc);
        gc.gridy = 2;
        gc.gridx = 0;
        panel.add(new JLabel(Messages.getString("mixing.gasoverflowsource.tankpressure")), gc); //$NON-NLS-1$
        gc.gridx = 1;
        panel.add(getTankPressure(), gc);
        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = 2;
        panel.add(getButtonPanel(), gc);
        setContentPane(panel);
        pack();
    }
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 0.5;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel
                    .setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            gc.gridx = 0;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5, 100, 5, 5);
            buttonPanel.add(getCloseButton(), gc);
            gc.gridx = 1;
            gc.gridy = 0;
            gc.insets = new java.awt.Insets(5, 5, 5, 100);
            buttonPanel.add(getCancelButton(), gc);
        }
        return buttonPanel;
    }
    
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    close();
                }
                
            });
        }
        return closeButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    cancel();
                }
                
            });
        }
        return cancelButton;
    }

    private JTextField getTankDescription() {
        if (tankdescription == null) {
        	tankdescription = new JTextField();
        	tankdescription.setColumns(20);
        }
        return tankdescription;
    }

    private JTextField getTankSize() {
        if (tanksize == null) {
        	tanksize = new JTextField();
        	tanksize.setColumns(20);
        }
        return tanksize;
    }

    private JTextField getTankPressure() {
        if (tankpressure == null) {
        	tankpressure = new JTextField();
        	tankpressure.setColumns(20);
        }
        return tankpressure;
    }
	
    private void load() {
        if (source == null) {
            // new gas source
            getTankDescription().setText("");
            getTankSize().setText("50");
            getTankPressure().setText("200");
        } else {
            // load data from existing source
        	getTankDescription().setText(source.getTankdescription());
        	getTankSize().setText(new Double(source.getTanksize()).toString());
        	getTankPressure().setText(new Double(source.getTankpressure()).toString());
        }
    }

    public void close() {
        if (save()) {
            dispose();
        }
    }

    public void cancel() {
        dispose();
    }
    
    private boolean save() {
        try {
            GasOverflowSource s = source;
            if (s == null) {
                s = new GasOverflowSource();
            }
            s.setTankdescription(getTankDescription().getText());
            s.setTankpressure(new Double(getTankPressure().getText()));
            s.setTanksize(new Double(getTankSize().getText()));
            if (source == null) {
                gasSourceTableModel.addSource(s);
            } else {
                gasSourceTableModel.fireTableDataChanged();
            }
        } catch (Exception e) {
            new MessageDialog(this.mainWindow, Messages.getString("mixing.input_error"), Messages.getString("mixing.not_a_number"), null, MessageDialog.MessageType.ERROR); //$NON-NLS-1$  //$NON-NLS-2$
            return false;
        }
        return true;
    }

}
