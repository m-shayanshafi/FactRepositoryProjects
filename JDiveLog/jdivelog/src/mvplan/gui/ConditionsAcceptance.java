/*
 * ConditionsAcceptance.java
 *
 *  Displays Conditions of Use dialog for MV-Plan
 *
 *  @author Guy Wittig
 *  @version 6-May-2007
 *
 *   This program is part of MV-Plan
 *   Copywrite 2006 Guy Wittig
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   The GNU General Public License can be read at http://www.gnu.org/licenses/licenses.html
 */

package mvplan.gui;

import java.awt.*;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mvplan.main.*;

/**
 *
 * @author  Guy
 */
public class ConditionsAcceptance extends javax.swing.JDialog {
	private static final long serialVersionUID = -2793675568085526603L;

	/**
     * Creates new form ConditionsAcceptance
     */
    public ConditionsAcceptance(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        // Set text strings from resources
        setTitle(Mvplan.getResource("mvplan.gui.TermsAcceptance.title.text"));
        agreeButton.setText(Mvplan.getResource("mvplan.gui.TermsAcceptance.okButton.text"));
        exitButton.setText(Mvplan.getResource("mvplan.gui.TermsAcceptance.cancelButton.text"));        
        infoLabel.setText(Mvplan.getResource("mvplan.gui.TermsAcceptance.infoLabel.text")); 
        instructionsLabel.setText(Mvplan.getResource("mvplan.gui.TermsAcceptance.instructionsLabel.text"));
        agreeButton.setToolTipText( Mvplan.getResource("mvplan.gui.TermsAcceptance.agreeButton.tip") );
        exitButton.setToolTipText( Mvplan.getResource("mvplan.gui.TermsAcceptance.exitButton.tip") );
        // Display T&Cs
        termsTextArea.setFont(new Font("MONOSPACED",Font.PLAIN,12));
        new ConditionsDisplay(termsTextArea);  
                
        // Position at start
        termsTextArea.setCaretPosition(0);         
        // Listen for terms scrolled down to bottom and enable OK button.
        // Need to monitor scroll bar model.       
        termsScrollPane.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                BoundedRangeModel model = termsScrollPane.getVerticalScrollBar().getModel();
                
                if( (model.getExtent()<model.getMaximum()) && // Check that extent is set up, else initial component drawing will trigger this.
                    ( (model.getValue()+model.getExtent()) >= model.getMaximum()*0.95 )  )
                       agreeButton.setEnabled(true);             
            }         
        });
        
        setLocationRelativeTo(parent);
        getRootPane().setDefaultButton(exitButton);
        setVisible(true);
        agreeButton.setEnabled(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        infoPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        displayPanel = new javax.swing.JPanel();
        termsScrollPane = new javax.swing.JScrollPane();
        termsTextArea = new javax.swing.JTextArea();
        buttonPanel = new javax.swing.JPanel();
        instructionsLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        agreeButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        infoPanel.setLayout(new java.awt.BorderLayout());

        infoPanel.setMaximumSize(new java.awt.Dimension(1000, 14));
        infoPanel.setMinimumSize(new java.awt.Dimension(400, 14));
        infoPanel.setPreferredSize(new java.awt.Dimension(500, 14));
        infoLabel.setText("jLabel1");
        infoPanel.add(infoLabel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(infoPanel, gridBagConstraints);

        displayPanel.setLayout(new java.awt.BorderLayout());

        displayPanel.setMaximumSize(new java.awt.Dimension(1000, 1000));
        displayPanel.setMinimumSize(new java.awt.Dimension(400, 200));
        displayPanel.setPreferredSize(new java.awt.Dimension(500, 350));
        termsTextArea.setColumns(40);
        termsTextArea.setEditable(false);
        termsTextArea.setRows(20);
        termsTextArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
        termsScrollPane.setViewportView(termsTextArea);

        displayPanel.add(termsScrollPane, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(displayPanel, gridBagConstraints);

        buttonPanel.setMaximumSize(new java.awt.Dimension(1000, 43));
        buttonPanel.setMinimumSize(new java.awt.Dimension(400, 43));
        buttonPanel.setPreferredSize(new java.awt.Dimension(500, 43));
        instructionsLabel.setText("jLabel1");
        instructionsLabel.setMaximumSize(new java.awt.Dimension(300, 16));
        instructionsLabel.setPreferredSize(new java.awt.Dimension(250, 13));
        buttonPanel.add(instructionsLabel);

        exitButton.setText("Cancel");
        exitButton.setMaximumSize(new java.awt.Dimension(150, 23));
        exitButton.setMinimumSize(new java.awt.Dimension(80, 23));
        exitButton.setPreferredSize(new java.awt.Dimension(100, 30));
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(exitButton);

        agreeButton.setText("I Agree");
        agreeButton.setEnabled(false);
        agreeButton.setMaximumSize(new java.awt.Dimension(120, 23));
        agreeButton.setMinimumSize(new java.awt.Dimension(80, 23));
        agreeButton.setPreferredSize(new java.awt.Dimension(120, 30));
        agreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agreeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(agreeButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agreeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agreeButtonActionPerformed
        Mvplan.prefs.setAgreedToTerms(true);
        dispose();
    }//GEN-LAST:event_agreeButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        Mvplan.prefs.setAgreedToTerms(false);
        dispose();
    }//GEN-LAST:event_exitButtonActionPerformed
    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agreeButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel instructionsLabel;
    private javax.swing.JScrollPane termsScrollPane;
    private javax.swing.JTextArea termsTextArea;
    // End of variables declaration//GEN-END:variables
    
}
