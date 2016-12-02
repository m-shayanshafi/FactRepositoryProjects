/*
 * BattleWindow.java
 *
 * Created on December 20, 2006, 10:24 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package shoddybattleclient;
import netbattle.*;
import java.util.*;
import netbattle.messages.*;
import shoddybattle.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import mechanics.moves.*;
import java.net.URL;

/**
 *
 * @author  Colin
 */
public class BattleWindow extends JFrame implements ChatWindow {
    
    private HumanClient m_server;
    private int m_id;
    private int m_active = 0, m_oldActive = 0;
    private String[] m_pokemon;
    private String[][] m_moves;
    private String m_item;
    private boolean m_fainted = false;
    private Image m_img[] = new Image[] { null, null };
    private boolean m_finished = false;
    private double m_health[][] = new double[2][6];
    private ArrayList m_history = new ArrayList();
    private GameVisualisation m_visual;
    private HealthBar[] m_healthBar = new HealthBar[2];
    private boolean m_watching = false;
    private ChatColouriser m_colouriser;
    private int m_participant = 0;
    
    /** Log of the battle. */
    private StringBuffer m_log = new StringBuffer();
    
    public void setPokemon(int id1, String user, int g1, boolean s1,
            int id2, String opponent, int g2, boolean s2) {
        m_visual.setPokemon(id1, user, g1, s1, id2, opponent, g2, s2);
        m_visual.repaint();
        m_active = id1;
    }
    
    private String getActivePokemon() {
        return m_pokemon[m_active];
    }
    
    private void formatMoveList() {
        MoveList data = m_server.getLobbyWindow().getModData().getMoveData();
        for (int i = 0; i < m_moves.length; ++i) {
            String[] moves = m_moves[i];
            for (int j = 0; j < moves.length; ++j) {
                if (moves[j] == null) {
                    moves[j] = "None";
                    continue;
                }
                MoveListEntry entry = data.getMove(moves[j]);
                if (entry == null) {
                    continue;
                }
                PokemonMove move = entry.getMove();
                moves[j] += " - " + move.getType().toString();
            }
        }
    }
    
    public void declareBattleEnd(int victor) {
        m_finished = true;
    }
    
    public void informDamage(InformDamageMessage msg) {
        double ratio = msg.getRatio();
        int party = msg.getParty();
        int t = msg.getTarget();
        double r = (m_health[party][t] += ratio);
        if (r > 1.0) {
            r = 1.0;
        } else if (r < 0.0) {
            r = 0.0;
        }
        m_health[party][t] = r;
        m_history.add(msg);
        if (ratio != 0.0) {
            long percent = Math.abs(Math.round(ratio * 100.0));
            String verb = (ratio <= 0) ? " lost " : " restored ";
            String message = msg.getName() + verb + percent + "% of its health.";
            sendMessage(message, false);
        }
    }
    
    public BattleWindow(HumanClient server, int fid) {
        m_server = server;
        m_id = fid;
        m_participant = 0;
        init();
        tabs.remove(0);
        tabs.remove(0);
        lstUsers.setModel(new UserListModel(new ArrayList()));
        m_watching = true;
    }
    
    public void setUserList(String[] users) {
        ArrayList list = new ArrayList(Arrays.asList(users));
        lstUsers.setModel(new UserListModel(list));
    }
    
    private void init() {
        initComponents();
        m_visual = new GameVisualisation(m_server.getLobbyWindow().getModData(), m_participant);
        m_colouriser = new ChatColouriser(txtChat, m_server.getUserName(), null) {
            public void openPage(URL url) {
                LobbyWindow.viewWebPage(url);
            }
        };
        
        m_visual.setSize(m_visual.getPreferredSize());
        m_visual.setVisible(true);
        int height = (panel.getHeight() - m_visual.getHeight()) / 2;
        m_visual.setLocation(0, height);
        panel.add(m_visual);
        
        for (int i = 0; i < 2; ++i) {
            m_healthBar[i] = new HealthBar();
            m_healthBar[i].setSize(m_visual.getWidth(), height - 5);
            m_healthBar[i].setVisible(true);
            panel.add(m_healthBar[i]);
        }
        m_healthBar[0].setLocation(0, 0);
        m_healthBar[1].setLocation(0, panel.getHeight() - height + 5);
        
        for (int i = 0; i < m_health.length; ++i) {
            double[] row = m_health[i];
            for (int j = 0; j < row.length; ++j) {
                row[j] = 1.0;
            }
        }
    }
    
    private void updateSwitchTab() {
        for (int i = 0; i < 6; ++i) {
            getSwitch(i).setText(m_pokemon[i] + " ("
                    + Math.abs(Math.round(m_health[m_participant][i] * 100.0))
                    + "%)");
        }
    }
    
    /** Creates new form BattleWindow */
    public BattleWindow(HumanClient server,
            int fid,
            int participant,
            String[] users,
            String[][] moves,
            String[] party) {
        m_server = server;
        m_id = fid;
        m_moves = moves;
        m_pokemon = party;
        m_participant = participant;
        
        init();
        
        setUserList(users);
        formatMoveList();
        setMoves(0);
        
        updateSwitchTab();
    }
    
    public void updatePartyStates(int[][] state, String[][][] statuses) {
        int[] participant = state[m_participant];
        for (int i = 0; i < participant.length; ++i) {
            int status = participant[i];
            boolean alive = (status != UpdatePokemonStatusMessage.STATUS_FAINTED);
            getSwitch(i).setEnabled(alive);
            if (!alive) {
                m_health[m_participant][i] = 0.0;
            }
        }
        m_visual.setStatus(state, statuses);
        m_visual.repaint();
    }
    
    private JRadioButton getSwitch(int i) {
        switch (i) {
            case 0: return radSwitch0;
            case 1: return radSwitch1;
            case 2: return radSwitch2;
            case 3: return radSwitch3;
            case 4: return radSwitch4;
            case 5: return radSwitch5;
        }
        return null;
    }
    
    public void refreshStats(double[] ratio) {
        for (int i = 0; i < m_healthBar.length; ++i) {
            HealthBar bar = m_healthBar[ratio.length - i - 1];
            bar.setRatio(ratio[i]);
            bar.repaint();
        }
    }
    
    public void refreshStats(StatRefreshMessage msg) {
        setMoves(m_active);
        
        int hp = msg.getHp();
        int maxHp = msg.getMaxHp();
        double ratio = ((double)hp) / maxHp;
        HealthBar bar = m_healthBar[1];
        bar.setRatio(ratio);
        bar.repaint();
        
        bar = m_healthBar[0];
        bar.setRatio(msg.getEnemyRatio());
        bar.repaint();
        
        lblPokemon.setText(m_pokemon[m_active]
                + " ("
                + String.valueOf(hp)
                + "/"
                + String.valueOf(maxHp)
                + " HP)"
            );
        
        String item = msg.getItem();
        if ((item == null) || (item.length() == 0)) {
            item = "None";
        }
        lblItem.setText("Hold item: " + item);
        
        int current = 0;
        for (int i = 0; i < 4; ++i) {
            JRadioButton move = getMoveById(i);
            if (move.isSelected()) {
                current = i;
                break;
            }
        }
        
        boolean selected = false;
        boolean hasMoves = false;
        for (int i = 0; i < 4; ++i) {
            JRadioButton move = getMoveById(i);
            int pp = msg.getPp(i);
            if (pp > 0) {
                hasMoves = true;
            }
            move.setText(move.getText()
                    + " ("
                    + String.valueOf(pp)
                    + "/"
                    + String.valueOf(msg.getMaxPp(i))
                    + " PP)"
                );
            move.setVisible(true);
            move.setEnabled(true/*pp != 0*/);
            if (!selected && (pp != 0)) {
                selected = true;
                move.setSelected(true);
            }
        }
        
        if (!hasMoves) {
            JRadioButton struggle = getMoveById(0);
            struggle.setText("Struggle");
            struggle.setEnabled(true);
            for (int i = 1; i < 4; ++i) {
                getMoveById(i).setVisible(false);
            }
        }
        
        JRadioButton old = getMoveById(current);
        if (old.isEnabled()) {
            old.setSelected(true);
        }
        
        updateSwitchTab();
    }
    
    public JRadioButton getMoveById(int i) {
        switch (i) {
            case 0: return radMove0;
            case 1: return radMove1;
            case 2: return radMove2;
            case 3: return radMove3;
        }
        return null;
    }
    
    public void requestMove() {
        cmdUse.setEnabled(true);
        cmdSwitch.setEnabled(true);
        cmdCancel.setEnabled(false);
        cmdCancel2.setEnabled(false);
        tabs.setSelectedIndex(0);
    }
    
    public void sendMessage(String message, boolean important) {
        m_colouriser.addMessage(message, important);
        m_log.append(message);
        m_log.append("\n");
    }
    
    public void setPokemonStatus(int party, int idx, int state, String status) {
        m_visual.setStatus(party, idx, state);
        if ((party == m_participant)
                && (state == UpdatePokemonStatusMessage.STATUS_FAINTED)) {
            getSwitch(idx).setEnabled(false);
            m_health[party][idx] = 0.0;
        }
        if (status != null) {
            m_visual.toggleStatus(party, idx, status);
        }
        m_visual.repaint();
    }
    
    public void updateStatus(boolean online, String user, int level, int status) {
        UserListModel model = (UserListModel)lstUsers.getModel();
        if (online) {
            model.add(user);
            sendMessage(user + " has entered the room.", false);
        } else {
            sendMessage(user + " has left the room.", false);
            model.remove(user);
        }
        lstUsers.setModel(new UserListModel(model.getList()));
    }
    
    public void replaceFaintedPokemon() {
        m_fainted = true;
        
        cmdSwitch.setEnabled(true);
        tabs.setSelectedIndex(1);
    }
    
    public int getFieldId() {
        return m_id;
    }
    
    public void endSelection() {
        cmdUse.setEnabled(false);
        cmdSwitch.setEnabled(false);
        cmdCancel.setEnabled(false);
        cmdCancel2.setEnabled(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpMoves = new javax.swing.ButtonGroup();
        grpSwitch = new javax.swing.ButtonGroup();
        panel = new javax.swing.JPanel();
        tabs = new javax.swing.JTabbedPane();
        tabMoves = new javax.swing.JPanel();
        radMove0 = new javax.swing.JRadioButton();
        radMove1 = new javax.swing.JRadioButton();
        radMove2 = new javax.swing.JRadioButton();
        radMove3 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        cmdUse = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        lblPokemon = new javax.swing.JLabel();
        lblItem = new javax.swing.JLabel();
        tabSwitch = new javax.swing.JPanel();
        radSwitch0 = new javax.swing.JRadioButton();
        radSwitch1 = new javax.swing.JRadioButton();
        radSwitch2 = new javax.swing.JRadioButton();
        radSwitch3 = new javax.swing.JRadioButton();
        radSwitch4 = new javax.swing.JRadioButton();
        radSwitch5 = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        cmdSwitch = new javax.swing.JButton();
        cmdCancel2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cmdSendChat = new javax.swing.JButton();
        cmdSaveLog = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList();
        txtMessage = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panel.setPreferredSize(new java.awt.Dimension(240, 112));

        org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 249, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 198, Short.MAX_VALUE)
        );

        tabMoves.setOpaque(false);

        grpMoves.add(radMove0);
        radMove0.setSelected(true);
        radMove0.setText("Close Combat (15/15) - Fighting");
        radMove0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radMove0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radMove0.setOpaque(false);

        grpMoves.add(radMove1);
        radMove1.setText("Move 1");
        radMove1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radMove1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radMove1.setOpaque(false);

        grpMoves.add(radMove2);
        radMove2.setText("Move 2");
        radMove2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radMove2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radMove2.setOpaque(false);

        grpMoves.add(radMove3);
        radMove3.setText("Move 3");
        radMove3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radMove3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radMove3.setOpaque(false);

        jPanel4.setOpaque(false);

        cmdUse.setText("Use");
        cmdUse.setEnabled(false);
        cmdUse.setOpaque(false);
        cmdUse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUseActionPerformed(evt);
            }
        });

        cmdCancel.setText("Cancel");
        cmdCancel.setEnabled(false);
        cmdCancel.setOpaque(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cmdUse, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .add(cmdCancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(cmdUse)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmdCancel)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblPokemon.setText("0");

        lblItem.setText("0");

        org.jdesktop.layout.GroupLayout tabMovesLayout = new org.jdesktop.layout.GroupLayout(tabMoves);
        tabMoves.setLayout(tabMovesLayout);
        tabMovesLayout.setHorizontalGroup(
            tabMovesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabMovesLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabMovesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblPokemon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 228, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblItem)
                    .add(tabMovesLayout.createSequentialGroup()
                        .add(tabMovesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, radMove2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, radMove1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, tabMovesLayout.createSequentialGroup()
                                .add(radMove3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, radMove0))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        tabMovesLayout.setVerticalGroup(
            tabMovesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabMovesLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblPokemon)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblItem)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabMovesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabMovesLayout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(radMove0)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radMove1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radMove2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radMove3))
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        tabs.addTab("Moves", tabMoves);

        tabSwitch.setOpaque(false);

        grpSwitch.add(radSwitch0);
        radSwitch0.setSelected(true);
        radSwitch0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch0.setOpaque(false);

        grpSwitch.add(radSwitch1);
        radSwitch1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch1.setOpaque(false);

        grpSwitch.add(radSwitch2);
        radSwitch2.setText("Garchomp (100%)");
        radSwitch2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch2.setOpaque(false);

        grpSwitch.add(radSwitch3);
        radSwitch3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch3.setOpaque(false);

        grpSwitch.add(radSwitch4);
        radSwitch4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch4.setOpaque(false);

        grpSwitch.add(radSwitch5);
        radSwitch5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radSwitch5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radSwitch5.setOpaque(false);

        jPanel5.setOpaque(false);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 56, Short.MAX_VALUE)
        );

        cmdSwitch.setText("Switch");
        cmdSwitch.setEnabled(false);
        cmdSwitch.setOpaque(false);
        cmdSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSwitchActionPerformed(evt);
            }
        });

        cmdCancel2.setText("Cancel");
        cmdCancel2.setEnabled(false);
        cmdCancel2.setOpaque(false);
        cmdCancel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancel2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tabSwitchLayout = new org.jdesktop.layout.GroupLayout(tabSwitch);
        tabSwitch.setLayout(tabSwitchLayout);
        tabSwitchLayout.setHorizontalGroup(
            tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabSwitchLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(radSwitch1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(radSwitch0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .add(radSwitch5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(radSwitch4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(radSwitch3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(tabSwitchLayout.createSequentialGroup()
                        .add(radSwitch2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 44, Short.MAX_VALUE)
                .add(tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cmdSwitch)
                    .add(cmdCancel2))
                .add(26, 26, 26)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(255, Short.MAX_VALUE))
        );
        tabSwitchLayout.setVerticalGroup(
            tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabSwitchLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tabSwitchLayout.createSequentialGroup()
                        .add(tabSwitchLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(tabSwitchLayout.createSequentialGroup()
                                .add(cmdSwitch)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmdCancel2)
                                .add(6, 6, 6))
                            .add(tabSwitchLayout.createSequentialGroup()
                                .add(radSwitch0)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radSwitch1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radSwitch2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radSwitch3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radSwitch4)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radSwitch5)))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        tabs.addTab("Switch", tabSwitch);

        jPanel1.setOpaque(false);

        cmdSendChat.setText("Send");
        cmdSendChat.setOpaque(false);
        cmdSendChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSendChatActionPerformed(evt);
            }
        });

        cmdSaveLog.setText("Save Log");
        cmdSaveLog.setOpaque(false);
        cmdSaveLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveLogActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(lstUsers);

        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMessageKeyPressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(txtMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 389, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cmdSendChat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmdSaveLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(txtMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdSendChat)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdSaveLog)))
                .addContainerGap())
        );

        tabs.addTab("Chat", jPanel1);

        jScrollPane1.setViewportView(txtChat);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, tabs, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 285, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 192, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
    if (evt.getKeyChar() == '\n') {
        submitMessage();
    }
}//GEN-LAST:event_txtMessageKeyPressed

private void cmdSaveLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveLogActionPerformed
    JFileChooser chooser = new JFileChooser();
    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File f = chooser.getSelectedFile();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(f));
            writer.print(new String(m_log));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save the log.");
        }
    }
}//GEN-LAST:event_cmdSaveLogActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!m_watching && !m_finished) {
            String message = "You are in the middle of a battle. If you leave, "
                    + "the match will be forfeit. Are you sure that you want "
                    + "to forfeit this match?";
            
            int result = JOptionPane.showConfirmDialog(this,
                message,
                "Close window?",
                JOptionPane.YES_NO_OPTION);
        
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        m_server.sendMessage(new StatusChangeMessage(false, m_id));
        m_server.removeBattle(this);
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void cmdSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSendChatActionPerformed
        submitMessage();
    }//GEN-LAST:event_cmdSendChatActionPerformed

    private void submitMessage() {
        String text = txtMessage.getText().trim();
        if (text.length() != 0) {
            txtMessage.setText(null);
            if (text.equals("/clear")) {
                m_colouriser.clear();
            } else {
                NetMessage msg = new FieldTextMessage(m_id, text);
                m_server.sendMessage(msg);
            }
        }
    }
    
    private void setMoves(int i) {
        for (int j = 0; j < 4; ++j) {
            getMoveById(j).setText(m_moves[i][j]);
        }
        lblPokemon.setText(m_pokemon[i]);
    }
    
    private void cmdSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSwitchActionPerformed
        int id = 0;
        if (radSwitch0.isSelected()) {
            id = 0;
        } else if (radSwitch1.isSelected()) {
            id = 1;
        } else if (radSwitch2.isSelected()) {
            id = 2;
        } else if (radSwitch3.isSelected()) {
            id = 3;
        } else if (radSwitch4.isSelected()) {
            id = 4;
        } else {
            id = 5;
        }
        
        BattleTurn turn = BattleTurn.getSwitchTurn(id);
        
        m_server.sendMessage(new UseMoveMessage(turn, m_id));
        
        cmdUse.setEnabled(false);
        cmdSwitch.setEnabled(false);
        cmdCancel2.setEnabled(true);
        
        m_fainted = false;
    }//GEN-LAST:event_cmdSwitchActionPerformed

    private void cmdCancel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancel2ActionPerformed
        m_server.sendMessage(new UseMoveMessage(null, m_id));
        
        if (!m_fainted) {
            cmdUse.setEnabled(true);
        }
        cmdSwitch.setEnabled(true);
        cmdCancel2.setEnabled(false);
    }//GEN-LAST:event_cmdCancel2ActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        m_server.sendMessage(new UseMoveMessage(null, m_id));
        
        cmdUse.setEnabled(true);
        cmdSwitch.setEnabled(true);
        cmdCancel.setEnabled(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdUseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUseActionPerformed
        int id = -1;
        for (int i = 0; i < 4; ++i) {
            if (getMoveById(i).isSelected()) {
                id = i;
                break;
            }
        }
        if (id == -1) return;
        
        NetMessage msg = new UseMoveMessage(BattleTurn.getMoveTurn(id), m_id);
        m_server.sendMessage(msg);
        cmdSwitch.setEnabled(false);
        cmdCancel.setEnabled(true);
        
        cmdUse.setEnabled(false);
    }//GEN-LAST:event_cmdUseActionPerformed
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCancel2;
    private javax.swing.JButton cmdSaveLog;
    private javax.swing.JButton cmdSendChat;
    private javax.swing.JButton cmdSwitch;
    private javax.swing.JButton cmdUse;
    private javax.swing.ButtonGroup grpMoves;
    private javax.swing.ButtonGroup grpSwitch;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblItem;
    private javax.swing.JLabel lblPokemon;
    private javax.swing.JList lstUsers;
    private javax.swing.JPanel panel;
    private javax.swing.JRadioButton radMove0;
    private javax.swing.JRadioButton radMove1;
    private javax.swing.JRadioButton radMove2;
    private javax.swing.JRadioButton radMove3;
    private javax.swing.JRadioButton radSwitch0;
    private javax.swing.JRadioButton radSwitch1;
    private javax.swing.JRadioButton radSwitch2;
    private javax.swing.JRadioButton radSwitch3;
    private javax.swing.JRadioButton radSwitch4;
    private javax.swing.JRadioButton radSwitch5;
    private javax.swing.JPanel tabMoves;
    private javax.swing.JPanel tabSwitch;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTextPane txtChat;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
    
}

