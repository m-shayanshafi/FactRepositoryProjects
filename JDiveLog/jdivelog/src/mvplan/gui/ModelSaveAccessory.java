/*
 * ModelSaveAccessory.java
 *
 * Accessory component used in the Save dialog to allow the additon of metadata.
 *
 * @author Guy Wittig
 * @version 04-Mar-2005
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

public class ModelSaveAccessory extends javax.swing.JPanel {
	private static final long serialVersionUID = -9168259521466652493L;

	/** Creates new form ModelSaveAccessory */
    public ModelSaveAccessory() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        modelMetaDataLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        modelMetaDataText = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 10, 1, 10)));
        setMinimumSize(new java.awt.Dimension(200, 100));
        setPreferredSize(new java.awt.Dimension(200, 100));
        modelMetaDataLabel.setText("MetaData");
        modelMetaDataLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        modelMetaDataLabel.setMaximumSize(new java.awt.Dimension(80, 30));
        modelMetaDataLabel.setMinimumSize(new java.awt.Dimension(100, 30));
        modelMetaDataLabel.setPreferredSize(new java.awt.Dimension(100, 30));
        add(modelMetaDataLabel, java.awt.BorderLayout.CENTER);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(180, 100));
        modelMetaDataText.setLineWrap(true);
        modelMetaDataText.setRows(3);
        modelMetaDataText.setWrapStyleWord(true);
        modelMetaDataText.setMaximumSize(new java.awt.Dimension(200, 100));
        modelMetaDataText.setMinimumSize(new java.awt.Dimension(150, 50));
        jScrollPane1.setViewportView(modelMetaDataText);

        add(jScrollPane1, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    // Sets the MetaData text field
    /**
     * Sets MetaData field text
     * @param s Text String
     */
    public void setMetaData(String s){
        modelMetaDataText.setText(s);
    }

    /**
     * Gets the MetaData text field
     * @return String
     */
    public String getMetaData(){
        return modelMetaDataText.getText();
    }

    /**
     * Sets Label string
     * @param s String to be displayed
     */
    public void setMetaDataLabel(String s) {
        modelMetaDataLabel.setText(s);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel modelMetaDataLabel;
    private javax.swing.JTextArea modelMetaDataText;
    // End of variables declaration//GEN-END:variables
    
}
