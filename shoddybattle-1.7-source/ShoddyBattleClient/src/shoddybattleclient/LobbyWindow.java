/*
 * LobbyWindow.java
 *
 * Created on December 21, 2006, 6:16 PM
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
import mechanics.clauses.Clause.ClauseChoice;

/**
 *
 * @author  Colin
 */
public class LobbyWindow extends JFrame implements ChatWindow {
    
    private HumanClient m_server;
    private String m_userName;
    private List m_challenge = Collections.synchronizedList(new ArrayList());
    private ChatColouriser m_colouriser;
    private ModData m_modData = ModData.getDefaultData();
    private WelcomeTextEditor m_welcomeTextEditor;
    private ClauseChoice[] m_clauses;
    
    private static class User implements Comparable {
        private String m_name;
        private int m_level;
        private int m_status;
        private static final String m_colors[] = {
            "black",        // Regular user
            "#000099",      // Mod
            "rgb(200,0,0)"  // Admin
        };
        public User(String str, int level, int status) {
            m_name = str;
            m_level = level;
            m_status = status;
        }
        public String getName() {
            return m_name;
        }
        public int compareTo(Object o2) {
            User u2 = ((User)o2);
            if (m_level > u2.m_level)
                return -1;
            if (m_level < u2.m_level)
                return 1;
            if (m_status < u2.m_status)
                return -1;
            if (m_status > u2.m_status)
                return 1;
            String s2 = u2.getName();
            return m_name.compareToIgnoreCase(s2);
        }
        public boolean equals(Object o2) {
            return m_name.equals(((User)o2).m_name);
        }
        public int getStatus() {
            return m_status;
        }
        public String toString() {
            String colour = (m_status == StatusChangeMessage.STATUS_AWAY) ?
                "rgb(130,130,130)" : m_colors[m_level];
            String suffix = "";
            if (m_level == AccountRegistry.LEVEL_MOD) {
                suffix = "*";
            } else if (m_level == AccountRegistry.LEVEL_ADMIN) {
                suffix = "**";
            }
            return "<html><font style='color: "
                    + colour + "'>" + m_name + suffix + "</font></html>";
        }
    }
    
    public ClauseChoice[] getClauses() {
        return m_clauses;
    }
    
    public ModData getModData() {
        return m_modData;
    }
    
    /** Creates new form LobbyWindow */
    public LobbyWindow(HumanClient server, String userName) {
        initComponents();
        
        m_server = server;
        
        // Download mod data here.
        File f = new File(ModData.getStorageLocation()
                + m_server.getServerName());
        try {
            m_server.receiveFile(f);
            m_modData = new ModData(f);
        } catch (IOException e) {

        }
        
        m_colouriser = new ChatColouriser(txtChat, userName, null) {
            public void openPage(URL url) {
                viewWebPage(url);
            }
        };
        m_userName = userName;
        m_server.setLobbyWindow(this);
        
        String[] users = null, battles = null;
        User[] userObjects = null;
        try {
            UserListMessage msg =
                    (UserListMessage)m_server.getNextMessage();
            users = msg.getUserList();
            int[] levels = msg.getLevels();
            int[] statuses = msg.getStatuses();
            userObjects = new User[users.length];
            for (int i = 0; i < users.length; ++i) {
                int level = (levels == null) ? 0 : levels[i];
                int status = (statuses == null) ? 0 : statuses[i];
                userObjects[i] = new User(users[i], level, status);
            }
            battles = msg.getBattleList();
            if (battles != null) {
                int[] fids = msg.getBattleFids();
                for (int i = 0; i < battles.length; ++i) {
                    server.addBattleMapEntry(battles[i], fids[i]);
                }
            }
            m_clauses = msg.getClauses();
        } catch (Throwable e) {
            // Panic.
        }
        
        initListModel(lstUsers, userObjects, true);
        initListModel(lstBattles, battles, false);
        
        m_server.start();
        
        mnuBattles.setSelected(Preferences.userRoot().getBoolean("messages.battles", false));
    }
    
    public void setAdminVisibility(int level) {
        mnuAdmin.setVisible(level > AccountRegistry.LEVEL_USER);
    }
    
    private void initListModel(JList list, Object[] entry, boolean sort) {
        ArrayList arr = new ArrayList();
        if (entry != null) {
            arr.addAll(Arrays.asList(entry));
        }
        if (sort) {
            Collections.sort(arr);
        }
        list.setModel(new UserListModel(arr));
    }
    
    public boolean showBattleMessages() {
        return mnuBattles.isSelected();
    }
    
    public void removeChallenge(ChallengeWindow wnd) {
        synchronized (m_challenge) {
            Iterator i = m_challenge.iterator();
            while (i.hasNext()) {
                if (i.next() == wnd) {
                    i.remove();
                    return;
                }
            }
        }
    }
    
    public ChallengeWindow getChallengeByOpponent(String opponent) {
        synchronized (m_challenge) {
            Iterator i = m_challenge.iterator();
            while (i.hasNext()) {
                ChallengeWindow wnd = (ChallengeWindow)i.next();
                if (opponent.equals(wnd.getOpponent())) {
                    return wnd;
                }
            }
            return null;
        }
    }
    
    public void updateUserListModel(
            JList list, boolean online, Object user, boolean sort) {
        UserListModel model = (UserListModel)list.getModel();
        if (online) {
            model.remove(user);
            model.add(user);
        } else {
            model.remove(user);
        }
        ArrayList modelList = model.getList();
        if (sort) {
            Collections.sort(modelList);
        }
        list.setModel(new UserListModel(modelList));
    }
    
    public WelcomeTextEditor getWelcomeTextEditor() {
        return m_welcomeTextEditor;
    }
    
    public void updateStatus(boolean online, String user, int level, int status) {
        if (user == null)
            return;
        updateUserListModel(lstUsers, online, new User(user, level, status), true);
    }
    
    public void updateBattle(boolean online, String description) {
        updateUserListModel(lstBattles, online, description, false);
    }
    
    public void sendMessage(String message, boolean important) {
        m_colouriser.addMessage(message, important);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtChatMessage = new javax.swing.JTextField();
        cmdSubmit = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList();
        cmdChallenge = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstBattles = new javax.swing.JList();
        cmdWatch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuNewTeam = new javax.swing.JMenuItem();
        mnuOpenTeam = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        mnuBanSelf = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuLeaveServer = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        mnuExit = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        mnuAway = new javax.swing.JCheckBoxMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuBattles = new javax.swing.JCheckBoxMenuItem();
        mnuAdmin = new javax.swing.JMenu();
        mnuUsersList = new javax.swing.JMenuItem();
        mnuWelcomeText = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuWebSite = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        mnuBugs = new javax.swing.JMenuItem();
        mnuFeatures = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jPanel1.setOpaque(false);

        lstUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstUsersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstUsers);

        cmdChallenge.setText("Challenge");
        cmdChallenge.setOpaque(false);
        cmdChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdChallengeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmdChallenge, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmdChallenge)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Users", jPanel1);

        jPanel2.setOpaque(false);

        jScrollPane3.setViewportView(lstBattles);

        cmdWatch.setText("Watch");
        cmdWatch.setOpaque(false);
        cmdWatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdWatchActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmdWatch, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmdWatch)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Battles", jPanel2);

        jScrollPane1.setViewportView(txtChat);

        jMenu1.setText("File");

        mnuNewTeam.setText("New Team");
        mnuNewTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewTeamActionPerformed(evt);
            }
        });
        jMenu1.add(mnuNewTeam);

        mnuOpenTeam.setText("Open Team");
        mnuOpenTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenTeamActionPerformed(evt);
            }
        });
        jMenu1.add(mnuOpenTeam);
        jMenu1.add(jSeparator4);

        mnuBanSelf.setText("Ban Self");
        mnuBanSelf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBanSelfActionPerformed(evt);
            }
        });
        jMenu1.add(mnuBanSelf);
        jMenu1.add(jSeparator1);

        mnuLeaveServer.setText("Leave Server");
        mnuLeaveServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLeaveServerActionPerformed(evt);
            }
        });
        jMenu1.add(mnuLeaveServer);
        jMenu1.add(jSeparator2);

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Status");

        mnuAway.setText("Away");
        mnuAway.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mnuAwayStateChanged(evt);
            }
        });
        jMenu4.add(mnuAway);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("View");

        mnuBattles.setText("View Battle Notices");
        mnuBattles.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mnuBattlesStateChanged(evt);
            }
        });
        jMenu2.add(mnuBattles);

        jMenuBar1.add(jMenu2);

        mnuAdmin.setText("Admin");

        mnuUsersList.setText("Users List");
        mnuUsersList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUsersListActionPerformed(evt);
            }
        });
        mnuAdmin.add(mnuUsersList);

        mnuWelcomeText.setText("Welcome Text");
        mnuWelcomeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuWelcomeTextActionPerformed(evt);
            }
        });
        mnuAdmin.add(mnuWelcomeText);

        jMenuBar1.add(mnuAdmin);

        jMenu3.setText("Help");

        mnuWebSite.setText("Shoddy Battle Web Site");
        mnuWebSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuWebSiteActionPerformed(evt);
            }
        });
        jMenu3.add(mnuWebSite);
        jMenu3.add(jSeparator3);

        mnuBugs.setText("Bug Tracker");
        mnuBugs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBugsActionPerformed(evt);
            }
        });
        jMenu3.add(mnuBugs);

        mnuFeatures.setText("Feature Suggestions");
        mnuFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFeaturesActionPerformed(evt);
            }
        });
        jMenu3.add(mnuFeatures);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(txtChatMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdSubmit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                        .add(6, 6, 6))
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmdSubmit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .add(txtChatMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                        .add(13, 13, 13))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void mnuAwayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mnuAwayStateChanged
    m_server.sendMessage(new StatusChangeMessage(true,
            m_server.getUserName(),
            m_server.getUserLevel(),
            mnuAway.isSelected() ?
                StatusChangeMessage.STATUS_AWAY : StatusChangeMessage.STATUS_HERE,
            -1));
}//GEN-LAST:event_mnuAwayStateChanged

private void mnuBanSelfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBanSelfActionPerformed
    String user = m_server.getUserName();
    long date = BanDialog.getBanDate(LobbyWindow.this, "yourself");
    if (date != -1) {
        m_server.sendMessage(new BanMessage(user, date));
    }
}//GEN-LAST:event_mnuBanSelfActionPerformed

private void mnuFeaturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFeaturesActionPerformed
    viewWebPage("http://sourceforge.net/tracker/?group_id=186888&atid=919098");
}//GEN-LAST:event_mnuFeaturesActionPerformed

    public static void viewWebPage(String page) {
        try {
            viewWebPage(new URL(page));
        } catch (Exception e) {
            
        }
    }

    public static void viewWebPage(URL page) {
        try {
            ((BasicService)ServiceManager.lookup("javax.jnlp.BasicService")).showDocument(
                    page
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
private void mnuBugsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBugsActionPerformed
    viewWebPage("http://sourceforge.net/tracker/?group_id=186888&atid=919095");
}//GEN-LAST:event_mnuBugsActionPerformed

private void mnuWebSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuWebSiteActionPerformed
    viewWebPage("http://shoddybattle.com");
}//GEN-LAST:event_mnuWebSiteActionPerformed

private void mnuBattlesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mnuBattlesStateChanged
    Preferences.userRoot().putBoolean("messages.battles", mnuBattles.getState());
}//GEN-LAST:event_mnuBattlesStateChanged

private void mnuWelcomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuWelcomeTextActionPerformed
    if (m_server.getUserLevel() < AccountRegistry.LEVEL_ADMIN) {
        JOptionPane.showMessageDialog(this, "Sorry, only server administrators can modify the welcome message.");
    } else {
        if (m_welcomeTextEditor == null) {
            m_welcomeTextEditor = new WelcomeTextEditor(m_server, false);
            m_welcomeTextEditor.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    m_welcomeTextEditor = null;
                }
            });
        }
        m_welcomeTextEditor.setVisible(true);
    }
}//GEN-LAST:event_mnuWelcomeTextActionPerformed

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want exit Shoddy Battle?",
                "Exit",
                JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        System.exit(0);
    }//GEN-LAST:event_mnuExitActionPerformed

    private void mnuLeaveServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLeaveServerActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to leave this server and return to the list of servers?",
                "Leave Server", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        
        m_server.close();
        dispose();
        new WelcomeWindow().setVisible(true);
    }//GEN-LAST:event_mnuLeaveServerActionPerformed

    private void mnuOpenTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenTeamActionPerformed
        final TeamBuilder team = new TeamBuilder(false);
        if (!team.loadTeam()) {
            team.dispose();
        } else {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    team.setVisible(true);
                }
            });
        }
    }//GEN-LAST:event_mnuOpenTeamActionPerformed

    private void mnuNewTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewTeamActionPerformed
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TeamBuilder(m_modData).setVisible(true);
            }
        });
    }//GEN-LAST:event_mnuNewTeamActionPerformed

    private void mnuUsersListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUsersListActionPerformed
        m_server.sendMessage(new UserTableMessage());
    }//GEN-LAST:event_mnuUsersListActionPerformed

    private void lstUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstUsersMouseClicked
        int button = evt.getButton();
        if ((button != MouseEvent.BUTTON3) && !((button == MouseEvent.BUTTON1) && evt.isControlDown())) {
            return;
        }
        int level = m_server.getUserLevel();
        if (level == AccountRegistry.LEVEL_USER) {
            return;
        }
        if (lstUsers.getSelectedIndex() == -1) {
            return;
        }
        JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Kick") {
               public void actionPerformed(ActionEvent e) {
                    String user = ((User)lstUsers.getSelectedValue()).getName();
                    m_server.sendMessage(new BanMessage(user, -1));
               } 
            });
        class BanAction extends AbstractAction {
            private long m_time;
            public BanAction(String name, long time) {
                super(name);
                m_time = time;
            }
            public void actionPerformed(ActionEvent e) {
                String user = ((User)lstUsers.getSelectedValue()).getName();
                long date = new Date().getTime() + m_time;
                if (JOptionPane.showConfirmDialog(LobbyWindow.this,
                        "Are you sure you want to ban " + user + " for "
                        + new TimeInterval(m_time).getApproximation()
                        + "?", "Banning " + user, JOptionPane.YES_NO_OPTION) !=
                            JOptionPane.YES_OPTION) {
                    return;
                }
                m_server.sendMessage(new BanMessage(user, date));
            }
        }
        menu.add(new BanAction("Ban (Day)", 60 * 60 * 24 * 1000L));
        menu.add(new BanAction("Ban (Week)", 60 * 60 * 24 * 7 * 1000L));
        menu.add(new BanAction("Ban (Month)", 60 * 60 * 24 * 30 * 1000L));
        menu.add(new AbstractAction("Ban (Arbitrary Time)") {
               public void actionPerformed(ActionEvent e) {
                    String user = ((User)lstUsers.getSelectedValue()).getName();
                    long date = BanDialog.getBanDate(LobbyWindow.this, user);
                    if (date != -1) {
                        m_server.sendMessage(new BanMessage(user, date));
                    }
               }
            });
        menu.add(new JSeparator());
        menu.add(new AbstractAction("Find Aliases") {
               public void actionPerformed(ActionEvent e) {
                    String user = ((User)lstUsers.getSelectedValue()).getName();
                    m_server.sendMessage(new FindAliasesMessage(user, null));
               } 
            });
        menu.show(lstUsers, evt.getX(), evt.getY());
    }//GEN-LAST:event_lstUsersMouseClicked

    private void cmdWatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdWatchActionPerformed
        if (lstBattles.getModel().getSize() == 0) {
            return;
        }
        String desc = (String)lstBattles.getSelectedValue();
        m_server.watchBattle(desc);
    }//GEN-LAST:event_cmdWatchActionPerformed

    private void cmdChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdChallengeActionPerformed
        User user = (User)lstUsers.getSelectedValue();
        if (user == null)
            return;
        String opponent = user.getName();
        if (opponent.equals(m_userName)) {
            new MessageBox(this, "Error", "You cannot challenge yourself.")
                .setVisible(true);
            return;
        }
        if (user.getStatus() == StatusChangeMessage.STATUS_AWAY) {
            JOptionPane.showMessageDialog(this, opponent + " is away.");
            return;
        }
        if (getChallengeByOpponent(opponent) != null) {
            new MessageBox(this,
                    "Error",
                    "You have already issued a challenge to "
                    + opponent
                    + " that remains outstanding.")
                        .setVisible(true);
            return;
        }
        
        ChallengeWindow challenge = new ChallengeWindow(m_server, opponent);
        m_challenge.add(challenge);
        challenge.setVisible(true);
    }//GEN-LAST:event_cmdChallengeActionPerformed

    private void txtChatMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChatMessageKeyPressed
        if (evt.getKeyChar() == '\n') {
            submitMessage();
        }
    }//GEN-LAST:event_txtChatMessageKeyPressed

    private void cmdSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSubmitActionPerformed
       submitMessage();
    }
    
    private void submitMessage() {
        String message = txtChatMessage.getText().trim();
        if (message.length() == 0)
            return;
        txtChatMessage.setText(null);

        if (message.charAt(0) != '/') {
            m_server.sendChatMessage(-1, message);
            return;
        }
        
        message = message.substring(1);
        int idx = message.indexOf(' ');
        String command = message, parameter = null;
        if (idx != -1) {
            command = message.substring(0, idx).toLowerCase();
            parameter = message.substring(idx + 1).trim();
        }

        if (command.equals("clear")) {
            m_colouriser.clear();
        } else if (command.equals("wall")) {
            if (parameter != null) {
                m_server.sendChatMessage(-2, parameter);
            }
        } else if (command.equals("kick")) {
            m_server.sendMessage(new BanMessage(parameter, -1));
        } else if (command.equals("ban")) {
            String[] parts = parameter.split(" ");
            String number = parts[parts.length - 1];
            int days = 1;
            int last = parts.length - 1;
            try {
                days = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                last = parts.length;
            }
            StringBuffer name = new StringBuffer();
            for (int i = 0; i < last; ++i) {
                name.append(parts[i]);
            }
            m_server.sendMessage(new BanMessage(new String(name).trim(),
                    new Date().getTime() + 1000 * 60 * 60 * 24L * days));
        } else {
            m_colouriser.addMessage("Invalid command: /" + command, false);
        }
    }//GEN-LAST:event_cmdSubmitActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdChallenge;
    private javax.swing.JButton cmdSubmit;
    private javax.swing.JButton cmdWatch;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList lstBattles;
    private javax.swing.JList lstUsers;
    private javax.swing.JMenu mnuAdmin;
    private javax.swing.JCheckBoxMenuItem mnuAway;
    private javax.swing.JMenuItem mnuBanSelf;
    private javax.swing.JCheckBoxMenuItem mnuBattles;
    private javax.swing.JMenuItem mnuBugs;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuFeatures;
    private javax.swing.JMenuItem mnuLeaveServer;
    private javax.swing.JMenuItem mnuNewTeam;
    private javax.swing.JMenuItem mnuOpenTeam;
    private javax.swing.JMenuItem mnuUsersList;
    private javax.swing.JMenuItem mnuWebSite;
    private javax.swing.JMenuItem mnuWelcomeText;
    private javax.swing.JTextPane txtChat;
    private javax.swing.JTextField txtChatMessage;
    // End of variables declaration//GEN-END:variables
    
}
