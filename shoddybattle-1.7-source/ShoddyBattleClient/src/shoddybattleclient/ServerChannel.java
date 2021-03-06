/*
 * ServerChannel.java
 *
 * Created on August 24, 2007, 10:39 PM
 */

package shoddybattleclient;
import shoddybattle.util.TimeInterval;
import netbattle.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import netbattle.messages.*;
import java.awt.event.*;
import netbattle.database.registry.AccountRegistry;
import netbattle.messages.BanMessage;
import shoddybattle.ModData;
import java.util.prefs.Preferences;
import javax.jnlp.*;
import java.net.URL;

/**
 *
 * @author  Colin
 */
public class ServerChannel extends javax.swing.JPanel implements ChatWindow {
    
    private ChatColouriser m_colouriser;
    private HumanClient m_server;
    private int m_id;
    
    /** Creates new form ServerChannel */
    public ServerChannel(HumanClient server, int id) {
        m_server = server;
        m_id = id;
        initComponents();
        m_colouriser = new ChatColouriser(txtChat, server.getUserName(), null) {
            public void openPage(URL url) {
                LobbyWindow.viewWebPage(url);
            }
        };
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();
        txtChatMessage = new javax.swing.JTextField();
        cmdSubmit = new javax.swing.JButton();

        jScrollPane1.setViewportView(txtChat);

        txtChatMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtChatMessageKeyPressed(evt);
            }
        });

        cmdSubmit.setText("Submit");
        cmdSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSubmitActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 486, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(txtChatMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cmdSubmit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdSubmit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .add(txtChatMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .add(13, 13, 13))
        );
    }// </editor-fold>//GEN-END:initComponents

private void cmdSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSubmitActionPerformed
    submitMessage();
}

    public void sendMessage(String text, boolean important) {
        m_colouriser.addMessage(text, important);
    }

    public int getChannelId() {
        return m_id;
    }
    
    public void updateStatus(boolean online, String user, int level, int status) {
        
    }
    
private void submitMessage() {
    String message = txtChatMessage.getText().trim();
    if (message.length() == 0)
        return;
    txtChatMessage.setText(null);
    
    if (message.charAt(0) != '/') {
        m_server.sendChatMessage(m_id, message);
        return;
    }
    
    message = message.substring(1);
    int idx = message.indexOf(' ');
    String command = message, parameter = null;
    if (idx != -1) {
        command = message.substring(0, idx).toLowerCase();
        parameter = message.substring(idx + 1);
    }
    
    if (command.equals("clear")) {
        m_colouriser.clear();
    } else if (command.equals("wall")) {
        if (parameter != null) {
            m_server.sendChatMessage(-2, parameter);
        }
    } else {
        m_colouriser.addMessage("Invalid command: /" + command, false);
    }
}//GEN-LAST:event_cmdSubmitActionPerformed

private void txtChatMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChatMessageKeyPressed
    if (evt.getKeyChar() == '\n') {
        submitMessage();
    }
}//GEN-LAST:event_txtChatMessageKeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdSubmit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtChat;
    private javax.swing.JTextField txtChatMessage;
    // End of variables declaration//GEN-END:variables
    
}
