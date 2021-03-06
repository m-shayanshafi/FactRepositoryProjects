/*
 * ChallengeWindow.java
 *
 * Created on December 21, 2006, 8:58 PM
 */

package shoddybattleclient;
import javax.swing.*;
import shoddybattle.*;
import java.io.*;
import netbattle.*;
import netbattle.messages.WithdrawChallengeMessage;
import mechanics.clauses.Clause.ClauseChoice;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.prefs.*;

/**
 *
 * @author  Colin
 */
public class ChallengeWindow extends JFrame {
    
    protected HumanClient m_server;
    protected String m_target;
    protected Pokemon[] m_team;
    protected JCheckBox[] m_clauses;
    protected boolean[] m_selectionEnabled;
    protected JCheckBoxList m_clauseList;
    
    static {
        ToolTipManager manager = ToolTipManager.sharedInstance();
        manager.setInitialDelay(0);
        manager.setReshowDelay(0);
    }
    
    protected void setStatusText(String text) {
        lblStatus.setText(text);
    }
    
    private String getClauseKey(String clause) {
        return m_server.getLobbyWindow().getModData().getName() +
                ".clauses."
                + clause;
    }
    
    public boolean getDefaultState(ClauseChoice c) {
        return Preferences.userRoot().getBoolean(getClauseKey(c.getName()),
                c.isEnabledByDefault());
    }
    
    /** Creates new form ChallengeWindow */
    public ChallengeWindow(HumanClient server, String target) {
        initComponents();
        m_target = target;
        m_server = server;
        
        JCheckBoxList list = m_clauseList = new JCheckBoxList();
        
        ClauseChoice[] clauses = server.getLobbyWindow().getClauses();
        if (clauses != null) {
            m_clauses = new JCheckBox[clauses.length];
            m_selectionEnabled = new boolean[clauses.length];
            for (int i = 0; i < clauses.length; ++i) {
                final ClauseChoice clause = clauses[i];
                final String name = clause.getName();
                final JCheckBox box =  new JCheckBox(name);
                box.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        updateButtons();
                        Preferences.userRoot().putBoolean(getClauseKey(name),
                                box.isSelected());
                    }
                });
                String desc = clause.getDescription();
                String pattern = "([^ -]*[ -]){9}";
                desc = desc.replaceAll(pattern, "$0<br>");
                box.setToolTipText("<html>" + desc + "</html>");
                box.setSelected(getDefaultState(clause));
                m_selectionEnabled[i] = !clause.disablesTeamSelection();
                m_clauses[i] = box;
            }
            list.setListData((Object[])m_clauses);
        }
        
        list.setVisible(true);
        list.setLocation(0, 0);
        list.setSize(300, 300);
        container.add(list);
        
        setDescription(m_target);
        String team = TeamBuilder.getDefaultTeam();
        if ((server != null) && (team != null)) {
            loadTeam(new File(team));
        }
    }
    
    protected void setDescription(String target) {
        lblStatus.setText("You are about to challenge " + target + ".");
    }
    
    private void updateButtons() {
        boolean team = true;
        for (int i = 0; i < m_clauses.length; ++i) {
            JCheckBox box = m_clauses[i];
            
            if (box == null)
                continue;
            
            if (box.isSelected() && !m_selectionEnabled[i]) {
                team = false;
                break;
            }
        }
        if (!team) {
            cmdLoadTeam.setEnabled(false);
            cmdChallenge.setEnabled(true);
            cmdBuildTeam.setEnabled(false);
            lstTeam.setVisible(false);
        } else if (m_team != null) {
            cmdLoadTeam.setEnabled(true);
            cmdChallenge.setEnabled(true);
            cmdBuildTeam.setEnabled(true);
            lstTeam.setVisible(true);
        } else {
            cmdLoadTeam.setEnabled(true);
            cmdChallenge.setEnabled(false);
            cmdBuildTeam.setEnabled(true);
            lstTeam.setVisible(true);
        }
    }
    
    public Pokemon[] getPokemon() {
        if (m_team == null)
            return new Pokemon[6];
        return m_team;
    }
    
    public String getOpponent() {
        return m_target;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmdLoadTeam = new javax.swing.JButton();
        cmdChallenge = new javax.swing.JButton();
        cmdReject = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstTeam = new javax.swing.JList();
        cmdBuildTeam = new javax.swing.JButton();
        container = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Challenge");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        lblStatus.setText("You are about to challenge Colin.");

        cmdLoadTeam.setText("Load Team");
        cmdLoadTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadTeamActionPerformed(evt);
            }
        });

        cmdChallenge.setText("Challenge");
        cmdChallenge.setEnabled(false);
        cmdChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdChallengeActionPerformed(evt);
            }
        });

        cmdReject.setText("Cancel");
        cmdReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRejectActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(lstTeam);

        cmdBuildTeam.setText("Build Team");
        cmdBuildTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBuildTeamActionPerformed(evt);
            }
        });

        container.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout containerLayout = new org.jdesktop.layout.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 150, Short.MAX_VALUE)
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 223, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblStatus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(296, 296, 296)
                        .add(jLabel2))
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmdChallenge, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmdBuildTeam, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(cmdReject, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                    .add(cmdLoadTeam, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(container, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(15, 15, 15)
                        .add(jLabel1))
                    .add(lblStatus))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(container, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdBuildTeam)
                    .add(cmdLoadTeam))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmdChallenge)
                    .add(cmdReject))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    cancelChallenge();
}//GEN-LAST:event_formWindowClosed

    private void cmdRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRejectActionPerformed
        cancelChallenge();
    }//GEN-LAST:event_cmdRejectActionPerformed

    private void cmdChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdChallengeActionPerformed
        cmdLoadTeam.setEnabled(false);
        cmdChallenge.setEnabled(false);
        executeChallenge();
    }//GEN-LAST:event_cmdChallengeActionPerformed

    private void cmdBuildTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBuildTeamActionPerformed
        new TeamBuilder(m_server.getLobbyWindow().getModData()).setVisible(true);
    }//GEN-LAST:event_cmdBuildTeamActionPerformed

    protected void executeChallenge() {
        lblStatus.setText("You have challenged " + m_target + ".");
        boolean nul = (m_clauses == null);
        boolean[] clauses = nul ? null : new boolean[m_clauses.length];
        if (!nul) {
            for (int i = 0; i < clauses.length; ++i) {
                clauses[i] = m_clauses[i].isSelected();
            }
        }
        m_server.issueChallenge(m_target, clauses);
    }
    
    protected void cancelChallenge() {
        if (m_server != null) {
            if (cmdReject.isEnabled()) {
                m_server.sendMessage(new WithdrawChallengeMessage(m_target));
            }
            m_server.getLobbyWindow().removeChallenge(this);
        }
        dispose();
    }
    
    private void cmdLoadTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadTeamActionPerformed
        JFileChooser choose = new JFileChooser();
        if (choose.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;
        
        File f = choose.getSelectedFile();
        loadTeam(f);
    }//GEN-LAST:event_cmdLoadTeamActionPerformed
    
    private void loadTeam(File f) {
        if (!f.exists()) {
            // Cannot open a file that doesn't exist!
            return;
        }
        
        Pokemon[] pokemon = new Pokemon[6];
        if (Pokemon.loadTeam(f, pokemon) == null) {
            // Failed to open the file.
            return;
        }
        
        String name[] = new String[pokemon.length];
        
        ModData data = m_server.getLobbyWindow().getModData();
        for (int i = 0; i < pokemon.length; ++i) {
            Pokemon p = pokemon[i];
            try {
                p.validate(data);
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(this, p.getName() +
                        " is an invalid pokemon for the following reason:\n\n"
                        + e.getMessage() + "\n\n"
                        + "Please load a different team.");
                return;
            }
            name[i] = p.getName() + " (" + p.getSpeciesName() + ")";
        }
        
        m_team = pokemon;
        lstTeam.setListData(name);
        cmdChallenge.setEnabled(true);
    }
    
    protected void setButtonNames(String ok, String cancel) {
        cmdChallenge.setText(ok);
        cmdReject.setText(cancel);
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChallengeWindow(null, "bearzly").setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdBuildTeam;
    private javax.swing.JButton cmdChallenge;
    private javax.swing.JButton cmdLoadTeam;
    private javax.swing.JButton cmdReject;
    private javax.swing.JPanel container;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JList lstTeam;
    // End of variables declaration//GEN-END:variables
    
}
