/*
 * RemovePluginDialogue.java
 *
 * Created on August 22, 2007, 1:18 PM
 */

package shoddybattleclient;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 *
 * @author  Percival "Dragontamer" Tiglao
 */
public class RemovePluginDialogue extends javax.swing.JDialog {
    private JList loadedPlugins;
    private int[] selectedPlugins;
    
    /** Creates new form RemovePluginDialogue */
    private RemovePluginDialogue(java.awt.Frame parent, JList loadedPlugins) {
        super(parent, true);
        this.loadedPlugins = loadedPlugins;
        initComponents();
        
        pluginScrollPane.setViewportView(loadedPlugins);
        this.pack();
        //loadedPlugins.setVisible(true);
    }
    
    private void initComponents() {
        pluginScrollPane = new javax.swing.JScrollPane();
        buttonRemove = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        buttonRemove.setText("Remove");
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        
        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        
        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);
        
        pluginScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        
        add(pluginScrollPane, BorderLayout.CENTER);
        
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.add(buttonRemove);
        eastPanel.add(buttonCancel);
        
        eastPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        
        add(eastPanel, BorderLayout.EAST);
        pack();
    }
    
    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        selectedPlugins = null;
        this.setVisible(false);
    }
    
    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        selectedPlugins = loadedPlugins.getSelectedIndices();
        this.setVisible(false);
    }
    
    public int[] getSelectedPlugins() {
        return selectedPlugins;
    }
    
    public static int[] load(java.awt.Frame parent, JList loadedPlugins){
        RemovePluginDialogue rpd = new RemovePluginDialogue(parent, loadedPlugins);
        rpd.setVisible(true);
        return rpd.getSelectedPlugins();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String strings[] = {"test", "test2", "test3"};
                JList jlat = new JList(strings);
                new RemovePluginDialogue(new javax.swing.JFrame(), jlat).setVisible(true);
            }
        });
    }
    
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane pluginScrollPane;
    
}
