/*
 * SelectPokemon.java
 *
 * Created on December 18, 2006, 9:19 PM
 */

package shoddybattleclient;
import shoddybattle.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author  Colin
 */
public class SelectPokemon extends javax.swing.JDialog {
    
    private Image m_image;
    private int m_selected = -1;
    
    /** Creates new form SelectPokemon */
    public SelectPokemon(PokemonSpeciesData data, JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        lstPokemon.setListData(data.getSpeciesNames());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstPokemon = new javax.swing.JList();
        cmdSelect = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        lstPokemon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lstPokemonKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(lstPokemon);

        cmdSelect.setText("Select");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 237, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(cmdCancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cmdSelect, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cmdSelect)
                        .add(8, 8, 8)
                        .add(cmdCancel))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void lstPokemonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lstPokemonKeyReleased
    if (evt.getKeyChar() == '\n') {
        cmdSelectActionPerformed(null);
    }
}//GEN-LAST:event_lstPokemonKeyReleased

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        m_selected = lstPokemon.getSelectedIndex();
        setVisible(false);
    }//GEN-LAST:event_cmdSelectActionPerformed

    public int getSelectedItem() {
        return m_selected;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstPokemon;
    // End of variables declaration//GEN-END:variables
    
}
