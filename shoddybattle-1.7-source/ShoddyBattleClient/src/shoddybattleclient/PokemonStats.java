/*
 * PokemonStats.java
 *
 * Created on December 16, 2006, 7:04 PM
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
import shoddybattle.*;
import mechanics.*;
import mechanics.moves.*;
import java.io.*;
import java.awt.Color;
import java.util.*;
import java.awt.event.*;
import mechanics.statuses.abilities.IntrinsicAbility;
import mechanics.statuses.items.HoldItem;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.color.*;
import netbattle.NetBattleField;

/**
 *
 * @author  Colin
 */
public class PokemonStats extends javax.swing.JInternalFrame {
    
    private static class Gender {
        private int m_value;
        public Gender(int gender) {
            m_value = gender;
        }
        public int getGender() {
            return m_value;
        }
        public String toString() {
            switch (m_value) {
                case PokemonSpecies.GENDER_MALE: return "Male";
                case PokemonSpecies.GENDER_FEMALE: return "Female";
                case PokemonSpecies.GENDER_NONE: return "None";
            }
            return null;
        }
        public boolean equals(Object o2) {
            if (o2 instanceof Gender) {
                return (((Gender)o2).m_value == m_value);
            }
            return false;
        }
    }
    
    private PokemonSpecies m_species = null;
    private Pokemon m_pokemon = null;
    private TeamBuilder m_team = null;
    private int m_index = -1;
    private SortedSet m_items = null;
    private MoveTableModel m_moves;
    private Image m_sprite;
    private JPanel m_spritePanel;
    private boolean m_enableRefreshing = false;
    private String m_lastSpecies;
    private boolean m_lastShiny, m_lastMale;
    
    private static final Map m_hiddenPowers = new HashMap();
    
    static {
        m_hiddenPowers.put(PokemonType.T_DARK, new int[] { 31, 31, 31, 31, 31, 31 });
        m_hiddenPowers.put(PokemonType.T_FIRE, new int[] { 31, 30, 31, 30, 30, 31 });
        m_hiddenPowers.put(PokemonType.T_WATER, new int[] { 31, 31, 31, 30, 30, 31 });
        m_hiddenPowers.put(PokemonType.T_GRASS, new int[] { 31, 30, 31, 31, 30, 31 });
        m_hiddenPowers.put(PokemonType.T_ELECTRIC, new int[] { 31, 31, 31, 31, 30, 31 });
        m_hiddenPowers.put(PokemonType.T_ICE, new int[] { 31, 30, 30, 31, 31, 31 });
        m_hiddenPowers.put(PokemonType.T_FIGHTING, new int[] { 31, 31, 30, 30, 30, 30 });
        m_hiddenPowers.put(PokemonType.T_POISON, new int[] { 31, 31, 30, 31, 30, 30 });
        m_hiddenPowers.put(PokemonType.T_GROUND, new int[] { 31, 31, 31, 31, 30, 30 });
        m_hiddenPowers.put(PokemonType.T_FLYING, new int[] { 31, 31, 31, 30, 30, 30 });
        m_hiddenPowers.put(PokemonType.T_PSYCHIC, new int[] { 31, 30, 31, 30, 31, 31 });
        m_hiddenPowers.put(PokemonType.T_BUG, new int[] { 31, 31, 31, 30, 31, 30 });
        m_hiddenPowers.put(PokemonType.T_ROCK, new int[] { 31, 31, 30, 30, 31, 30 });
        m_hiddenPowers.put(PokemonType.T_GHOST, new int[] { 31, 31, 30, 31, 31, 30 });
        m_hiddenPowers.put(PokemonType.T_DRAGON, new int[] { 31, 30, 31, 31, 31, 31 });
        m_hiddenPowers.put(PokemonType.T_STEEL, new int[] { 31, 31, 31, 31, 31, 30 });
    }
    
    public Pokemon getPokemon() {
        updateStats();
        return m_pokemon;
    }
    
    public PokemonStats(TeamBuilder team, int index) {
        initComponents();
        m_spritePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (m_sprite == null)
                    return;
                
                int w = panelSprite.getWidth() - 5,
                        h = panelSprite.getHeight() - 5;
                
                int iw = m_sprite.getWidth(this), ih = m_sprite.getHeight(this);
                
                while ((iw > w) || (ih > h)) {
                    --iw;
                    --ih;
                }
                
                int x = (w - iw) / 2;
                int y = (h - ih) / 2;
                
                g.drawImage(m_sprite, x, y, iw, ih, this);
            }
        };
        panelSprite.add(m_spritePanel);
        m_spritePanel.setSize(100, 100);
        m_spritePanel.setVisible(true);
        m_spritePanel.setLocation(0, 0);
        m_team = team;
        m_index = index;
        refreshPokemonList();
        String[] natures = PokemonNature.getNatureNames();
        for (int i = 0; i < natures.length; ++i) {
            cboNature.addItem(natures[i]);
        }
        refreshMoveList();
    }
    
    public void refreshPokemonList() {
        String choice = null;
        if (cboPokemon.getSelectedIndex() != -1) {
            choice = (String)cboPokemon.getSelectedItem();
        }
        cboPokemon.removeAllItems();
        String[] names = m_team.getModData().getSpeciesData().getSpeciesNames();
        for (int i = 0; i < names.length; ++i) {
            cboPokemon.addItem(names[i]);
        }
        if (choice != null) {
            cboPokemon.setSelectedItem(choice);
        }
    }
    
    public void refreshMoveList() {
        if (m_species == null) {
            return;
        }
        
        if (!m_enableRefreshing)
            return;
        
        ModData data = m_team.getModData();
        MoveSet moves = m_species.getMoveSet(data.getMoveSetData());
        if (moves == null) {
            new MessageBox(null, "Error",
                    "This pokemon's move selection is unimplemented "
                    + "(only Deoxys and Jirachi have this bug).")
                        .setVisible(true);
            return;
        }
        String[][] strMoves = moves.getMoves();
        m_moves = new MoveTableModel(
                m_team.getModData().getMoveData(), strMoves);
        JTable table = new JTable(m_moves);
        table.setVisible(true);
        moveset.setViewportView(table);
        
        cboAbility.removeAllItems();
        SortedSet set = m_species.getPossibleAbilities(data.getSpeciesData());
        if (set != null) {
            Iterator i = set.iterator();
            while (i.hasNext()) {
                String obj = (String)i.next();
                cboAbility.addItem(obj);
            }
        }
        
        cboItem.removeAllItems();
        m_items = data.getHoldItemData().getItemSet(m_species.getName());
        Iterator i = m_items.iterator();
        cboItem.addItem("None");
        while (i.hasNext()) {
            String itm = (String)i.next();
            cboItem.addItem(itm);
        }
        
        cboGender.removeAllItems();
        int genders = m_species.getPossibleGenders();
        if (genders == PokemonSpecies.GENDER_NONE) {
            cboGender.addItem(new Gender(PokemonSpecies.GENDER_NONE));
        } else {
            int[] list = { PokemonSpecies.GENDER_MALE, PokemonSpecies.GENDER_FEMALE };
            for (int j = 0; j < list.length; ++j) {
                if ((genders & list[j]) != 0) {
                    cboGender.addItem(new Gender(list[j]));
                }
            }
        }
        
        for (int idx = 0; idx < 4; ++idx) {
            getPpBox(idx).setSelectedIndex(3);
        }
        txtNickname.setText(m_species.getName());
        
        cboHiddenPower.removeAllItems();
        PokemonType[] types = PokemonType.getTypes();
        for (int idx = 0; idx < types.length; ++idx) {
            cboHiddenPower.addItem(types[idx]);
        }
        //cboHiddenPower.setSelectedItem(PokemonType.T_DARK);
        //txtHpPower.setText("70");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        cboAbility = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        cboItem = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        cboGender = new javax.swing.JComboBox();
        cboPp1 = new javax.swing.JComboBox();
        cboPp2 = new javax.swing.JComboBox();
        cboPp3 = new javax.swing.JComboBox();
        cboPp4 = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        chkShiny = new javax.swing.JCheckBox();
        cboPokemon = new javax.swing.JComboBox();
        cmdSelectPokemon = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtLevel = new javax.swing.JTextField();
        panelSprite = new javax.swing.JPanel();
        moveset = new javax.swing.JScrollPane();
        cboNature = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cboHiddenPower = new javax.swing.JComboBox();
        txtHpPower = new javax.swing.JTextField();
        finalHp = new javax.swing.JLabel();
        finalAttack = new javax.swing.JLabel();
        finalDefence = new javax.swing.JLabel();
        finalSpeed = new javax.swing.JLabel();
        finalSpcAttack = new javax.swing.JLabel();
        finalSpcDefence = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblSpcDefence = new javax.swing.JLabel();
        lblSpcAttack = new javax.swing.JLabel();
        lblSpeed = new javax.swing.JLabel();
        lblDefence = new javax.swing.JLabel();
        lblAttack = new javax.swing.JLabel();
        lblHp = new javax.swing.JLabel();
        txtSpcDefenceIv = new javax.swing.JTextField();
        txtSpcAttackIv = new javax.swing.JTextField();
        txtSpeedIv = new javax.swing.JTextField();
        txtDefenceIv = new javax.swing.JTextField();
        txtAttackIv = new javax.swing.JTextField();
        txtHpIv = new javax.swing.JTextField();
        txtSpcDefenceEv = new javax.swing.JTextField();
        txtSpcAttackEv = new javax.swing.JTextField();
        txtDefenceEv = new javax.swing.JTextField();
        txtSpeedEv = new javax.swing.JTextField();
        txtAttackEv = new javax.swing.JTextField();
        txtHpEv = new javax.swing.JTextField();
        lblDistrubuted = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel9.setOpaque(false);

        jLabel28.setText("Ability");

        jLabel29.setText("Item");

        jLabel30.setText("Gender:");

        cboGender.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboGenderItemStateChanged(evt);
            }
        });

        cboPp1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3" }));
        cboPp1.setSelectedIndex(3);

        cboPp2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3" }));
        cboPp2.setSelectedIndex(3);

        cboPp3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3" }));
        cboPp3.setSelectedIndex(3);

        cboPp4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3" }));
        cboPp4.setSelectedIndex(3);

        jLabel31.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel31.setText("PP Ups:");

        txtNickname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNicknameKeyTyped(evt);
            }
        });

        chkShiny.setText("Shiny");
        chkShiny.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkShiny.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkShiny.setOpaque(false);
        chkShiny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShinyActionPerformed(evt);
            }
        });
        chkShiny.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkShinyStateChanged(evt);
            }
        });

        cboPokemon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboPokemonItemStateChanged(evt);
            }
        });

        cmdSelectPokemon.setText("...");
        cmdSelectPokemon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectPokemonActionPerformed(evt);
            }
        });

        jLabel13.setText("Level");

        txtLevel.setColumns(3);
        txtLevel.setText("100");
        txtLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLevelActionPerformed(evt);
            }
        });
        txtLevel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLevelKeyReleased(evt);
            }
        });

        panelSprite.setBackground(new java.awt.Color(255, 255, 255));
        panelSprite.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        org.jdesktop.layout.GroupLayout panelSpriteLayout = new org.jdesktop.layout.GroupLayout(panelSprite);
        panelSprite.setLayout(panelSpriteLayout);
        panelSpriteLayout.setHorizontalGroup(
            panelSpriteLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 79, Short.MAX_VALUE)
        );
        panelSpriteLayout.setVerticalGroup(
            panelSpriteLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 70, Short.MAX_VALUE)
        );

        cboNature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNatureItemStateChanged(evt);
            }
        });

        jLabel19.setText("Nature");

        jLabel32.setText("Hidden Power");

        cboHiddenPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHiddenPowerActionPerformed(evt);
            }
        });

        txtHpPower.setEditable(false);
        txtHpPower.setText("70");

        finalHp.setText("000");

        finalAttack.setText("000");

        finalDefence.setText("000");

        finalSpeed.setText("000");

        finalSpcAttack.setText("000");

        finalSpcDefence.setText("000");

        jLabel6.setText("Spc. Defence");

        jLabel5.setText("Spc. Attack");

        jLabel4.setText("Speed");

        jLabel3.setText("Defence");

        jLabel2.setText("Attack");

        jLabel1.setText("HP");

        lblSpcDefence.setText("0");

        lblSpcAttack.setText("0");

        lblSpeed.setText("0");

        lblDefence.setText("0");

        lblAttack.setText("0");

        lblHp.setText("0");

        txtSpcDefenceIv.setColumns(2);
        txtSpcDefenceIv.setText("31");
        txtSpcDefenceIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpcDefenceIvKeyTyped(evt);
            }
        });

        txtSpcAttackIv.setColumns(2);
        txtSpcAttackIv.setText("31");
        txtSpcAttackIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpcAttackIvKeyTyped(evt);
            }
        });

        txtSpeedIv.setColumns(2);
        txtSpeedIv.setText("31");
        txtSpeedIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpeedIvKeyTyped(evt);
            }
        });

        txtDefenceIv.setColumns(2);
        txtDefenceIv.setText("31");
        txtDefenceIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDefenceIvKeyTyped(evt);
            }
        });

        txtAttackIv.setColumns(2);
        txtAttackIv.setText("31");
        txtAttackIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAttackIvKeyTyped(evt);
            }
        });

        txtHpIv.setColumns(2);
        txtHpIv.setText("31");
        txtHpIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHpIvKeyTyped(evt);
            }
        });

        txtSpcDefenceEv.setText("85");
        txtSpcDefenceEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpcDefenceEvKeyTyped(evt);
            }
        });

        txtSpcAttackEv.setText("85");
        txtSpcAttackEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpcAttackEvKeyTyped(evt);
            }
        });

        txtDefenceEv.setText("85");
        txtDefenceEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDefenceEvKeyTyped(evt);
            }
        });

        txtSpeedEv.setText("85");
        txtSpeedEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSpeedEvKeyTyped(evt);
            }
        });

        txtAttackEv.setText("85");
        txtAttackEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAttackEvKeyTyped(evt);
            }
        });

        txtHpEv.setText("85");
        txtHpEv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHpEvKeyTyped(evt);
            }
        });

        lblDistrubuted.setText("(510/510)");

        jLabel7.setText("IVs");

        jLabel8.setText("Base");

        jLabel9.setText("Total");

        jLabel10.setText("Stat");

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(cboPokemon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmdSelectPokemon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(finalSpcDefence)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblSpcDefence, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabel4)
                                            .add(jLabel2)
                                            .add(jLabel3))
                                        .add(34, 34, 34)
                                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(finalDefence)
                                            .add(finalSpeed)
                                            .add(finalAttack)
                                            .add(finalSpcAttack)))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .add(jLabel1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 68, Short.MAX_VALUE)
                                        .add(finalHp))
                                    .add(jLabel5)
                                    .add(jPanel9Layout.createSequentialGroup()
                                        .add(jLabel10)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 52, Short.MAX_VALUE)
                                        .add(jLabel9)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                    .add(lblSpcAttack, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                    .add(lblHp, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                    .add(lblAttack, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                    .add(lblDefence, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                    .add(lblSpeed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel9Layout.createSequentialGroup()
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(txtAttackIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(txtDefenceIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(txtSpeedIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(txtSpcAttackIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel7)
                                    .add(txtHpIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel9Layout.createSequentialGroup()
                                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtSpcAttackEv)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtHpEv)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtAttackEv)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtDefenceEv)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtSpeedEv)
                                            .add(txtSpcDefenceEv, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 12, Short.MAX_VALUE))
                                    .add(lblDistrubuted, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)))
                            .add(txtSpcDefenceIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel32)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboHiddenPower, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtHpPower, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE))
                    .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                            .add(jLabel19)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(cboNature, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 206, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel28)
                                .add(jLabel29))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(cboItem, 0, 206, Short.MAX_VALUE)
                                .add(cboAbility, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel31)
                            .add(jPanel9Layout.createSequentialGroup()
                                .add(jLabel13)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                                .add(3, 3, 3)
                                .add(cboPp1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cboPp2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cboPp3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cboPp4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9Layout.createSequentialGroup()
                                .add(12, 12, 12)
                                .add(jLabel30)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cboGender, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(24, 24, 24)
                                .add(panelSprite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(chkShiny))))
                    .add(moveset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 524, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cboPokemon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmdSelectPokemon)
                            .add(jLabel13)
                            .add(txtLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel30)
                            .add(cboGender, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkShiny)
                        .add(7, 7, 7)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblDistrubuted)
                            .add(jLabel7)
                            .add(jLabel8)
                            .add(jLabel10)
                            .add(jLabel9)))
                    .add(panelSprite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(moveset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 274, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel31)
                            .add(cboPp1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cboPp2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cboPp3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cboPp4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(lblHp)
                                .add(txtHpEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(txtHpIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(finalHp))
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(lblAttack)
                                .add(txtAttackIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(finalAttack)
                                .add(txtAttackEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(finalDefence)
                            .add(lblDefence)
                            .add(txtDefenceEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtDefenceIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(finalSpeed)
                            .add(jLabel4)
                            .add(lblSpeed)
                            .add(txtSpeedEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtSpeedIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(finalSpcAttack)
                            .add(lblSpcAttack)
                            .add(txtSpcAttackIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtSpcAttackEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(finalSpcDefence)
                            .add(txtSpcDefenceIv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtSpcDefenceEv, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblSpcDefence))
                        .add(8, 8, 8)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel32)
                            .add(cboHiddenPower, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtHpPower, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel19)
                            .add(cboNature, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cboAbility, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel28))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cboItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel29))
                        .add(88, 88, 88)))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 424, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtAttackEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAttackEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtAttackEvKeyTyped

private void cboGenderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboGenderItemStateChanged
    try {
        updateStats();
        updateSprite(m_pokemon);
    } catch (Exception e) {
        
    }
}//GEN-LAST:event_cboGenderItemStateChanged

private void chkShinyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShinyActionPerformed
    updateStats();
    updateSprite(m_pokemon);
}//GEN-LAST:event_chkShinyActionPerformed

private void cboHiddenPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHiddenPowerActionPerformed
    int[] ivs = (int[])m_hiddenPowers.get(cboHiddenPower.getSelectedItem());
    try {
        txtHpIv.setText(String.valueOf(ivs[0]));
        txtAttackIv.setText(String.valueOf(ivs[1]));
        txtDefenceIv.setText(String.valueOf(ivs[2]));
        txtSpeedIv.setText(String.valueOf(ivs[3]));
        txtSpcAttackIv.setText(String.valueOf(ivs[4]));
        txtSpcDefenceIv.setText(String.valueOf(ivs[5]));
        updateStats();
    } catch (Exception e) {
        
    }
}//GEN-LAST:event_cboHiddenPowerActionPerformed

private void txtSpcDefenceIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpcDefenceIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpcDefenceIvKeyTyped

private void txtSpcAttackIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpcAttackIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpcAttackIvKeyTyped

private void txtSpeedIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpeedIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpeedIvKeyTyped

private void txtDefenceIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDefenceIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtDefenceIvKeyTyped

private void txtAttackIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAttackIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtAttackIvKeyTyped

private void txtHpIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHpIvKeyTyped
    updateStats();
}//GEN-LAST:event_txtHpIvKeyTyped

private void txtHpEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHpEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtHpEvKeyTyped

private void txtDefenceEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDefenceEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtDefenceEvKeyTyped

private void txtSpeedEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpeedEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpeedEvKeyTyped

private void txtSpcAttackEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpcAttackEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpcAttackEvKeyTyped

private void txtSpcDefenceEvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpcDefenceEvKeyTyped
    updateStats();
}//GEN-LAST:event_txtSpcDefenceEvKeyTyped

private void txtLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLevelActionPerformed
    updateStats();
}//GEN-LAST:event_txtLevelActionPerformed

private void txtLevelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLevelKeyReleased
    updateStats();
}//GEN-LAST:event_txtLevelKeyReleased

private void txtNicknameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNicknameKeyTyped
    updateStats();
}//GEN-LAST:event_txtNicknameKeyTyped

private void chkShinyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkShinyStateChanged
    updateStats();
}//GEN-LAST:event_chkShinyStateChanged

private void cboPokemonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboPokemonItemStateChanged
    // Load in the new base stats.
    if (cboPokemon.getSelectedIndex() == -1) {
        return;
    }
    
    m_species = new PokemonSpecies(
            m_team.getModData().getSpeciesData(),
            cboPokemon.getSelectedIndex());
    int[] stats = m_species.getBaseStats();
    lblHp.setText(String.valueOf(stats[Pokemon.S_HP]));
    lblAttack.setText(String.valueOf(stats[Pokemon.S_ATTACK]));
    lblDefence.setText(String.valueOf(stats[Pokemon.S_DEFENCE]));
    lblSpeed.setText(String.valueOf(stats[Pokemon.S_SPEED]));
    lblSpcAttack.setText(String.valueOf(stats[Pokemon.S_SPATTACK]));
    lblSpcDefence.setText(String.valueOf(stats[Pokemon.S_SPDEFENCE]));
    
    refreshMoveList();
    updateStats();
    updateSprite(m_pokemon);
}//GEN-LAST:event_cboPokemonItemStateChanged

private void cmdSelectPokemonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectPokemonActionPerformed
    SelectPokemon select = new SelectPokemon(
            m_team.getModData().getSpeciesData(), m_team, true);
    select.setVisible(true);
    select.setAlwaysOnTop(true);
    select.setModal(true);
    int selected = select.getSelectedItem();
    select.dispose();
    
    if (selected != -1) {
        cboPokemon.setSelectedIndex(selected);
    }
}//GEN-LAST:event_cmdSelectPokemonActionPerformed

private void cboNatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNatureItemStateChanged
    updateStats();
}//GEN-LAST:event_cboNatureItemStateChanged
    
    private JComboBox getPpBox(int i) {
        switch (i) {
            case 0: return cboPp1;
            case 1: return cboPp2;
            case 2: return cboPp3;
            case 3: return cboPp4;
        }
        return null;
    }
    
    public void setRefreshingEnabled(boolean enabled) {
        m_enableRefreshing = enabled;
    }
    
    public void loadFromPokemon(Pokemon p) {
        BattleField field = new NetBattleField(null, new AdvanceMechanics(4));
        p.attachToField(field, 0, 0);
        m_species = m_pokemon = p;
        refreshMoveList();

        m_enableRefreshing = false;
        
        txtHpEv.setText(String.valueOf(p.getEv(0)));
        txtAttackEv.setText(String.valueOf(p.getEv(1)));
        txtDefenceEv.setText(String.valueOf(p.getEv(2)));
        txtSpeedEv.setText(String.valueOf(p.getEv(3)));
        txtSpcAttackEv.setText(String.valueOf(p.getEv(4)));
        txtSpcDefenceEv.setText(String.valueOf(p.getEv(5)));
            
        txtHpIv.setText(String.valueOf(p.getIv(0)));
        txtAttackIv.setText(String.valueOf(p.getIv(1)));
        txtDefenceIv.setText(String.valueOf(p.getIv(2)));
        txtSpeedIv.setText(String.valueOf(p.getIv(3)));
        txtSpcAttackIv.setText(String.valueOf(p.getIv(4)));
        txtSpcDefenceIv.setText(String.valueOf(p.getIv(5)));
        
        cboPokemon.setSelectedItem(p.getSpeciesName());
        cboNature.setSelectedItem(p.getNature().getName());
        cboAbility.setSelectedItem(p.getAbilityName());
        cboItem.setSelectedItem(p.getItemName());
        cboGender.setSelectedItem(new Gender(p.getGender()));
        txtLevel.setText(String.valueOf(p.getLevel()));
        txtNickname.setText(p.getName());
        
        ArrayList moves = new ArrayList();
        for (int i = 0; i < 4; ++i) {
            String name = p.getMoveName(i);
            if ((name != null) && (name.length() != 0)) {
                moves.add(name);
            }
            getPpBox(i).setSelectedIndex(p.getPpUpCount(i));
        }
        m_moves.setSelectedMoves((String[])moves.toArray(new String[moves.size()]));
        chkShiny.setSelected(p.isShiny());
        
        m_enableRefreshing = true;
        
        updateStats();
    }
    
    private int getTextFieldInt(JTextField text) {
        try {
            return Integer.parseInt(text.getText());
        } catch (NumberFormatException e) {
            text.setText("");
            return 0;
        }
    }
    
    public void updateStats() {
        if (m_species == null)
            return;
        
        if (!m_enableRefreshing)
            return;
        
        if (m_team != null) {
            m_team.informChangeMade();
        }
        
        int evs[] = {
            getTextFieldInt(txtHpEv),
            getTextFieldInt(txtAttackEv),
            getTextFieldInt(txtDefenceEv),
            getTextFieldInt(txtSpeedEv),
            getTextFieldInt(txtSpcAttackEv),
            getTextFieldInt(txtSpcDefenceEv)
        };
        
        int ivs[] = {
            getTextFieldInt(txtHpIv),
            getTextFieldInt(txtAttackIv),
            getTextFieldInt(txtDefenceIv),
            getTextFieldInt(txtSpeedIv),
            getTextFieldInt(txtSpcAttackIv),
            getTextFieldInt(txtSpcDefenceIv)
        };
        
        int total = 0;
        for (int i = 0; i < 6; ++i) {
            total += evs[i];
        }
   
        lblDistrubuted.setText("(" + String.valueOf(total) + "/510)");
        lblDistrubuted.setForeground((total > 510) ? Color.RED : Color.BLACK);
        
        MoveListEntry[] moves = new MoveListEntry[4];
        int[] pp = new int[moves.length];
        if (m_moves != null) {
            String[] names = m_moves.getSelectedMoves();
            MoveList list = m_team.getModData().getMoveData();
            for (int i = 0; i < names.length; ++i) {
                moves[i] = list.getMove(names[i]);
                pp[i] = getPpBox(i).getSelectedIndex();
            }
        }
        
        if (m_team != null) {
            m_team.updatePokemonName(m_index, m_species.getName());
        }
        
        String item = (String)cboItem.getSelectedItem();
        if (item.equals("None")) item = null;
        
        Pokemon p = m_pokemon = new Pokemon(
                new AdvanceMechanics(4),
                m_species,
                PokemonNature.getNature(cboNature.getSelectedIndex()),
                (String)cboAbility.getSelectedItem(),
                item,
                ((Gender)cboGender.getSelectedItem()).getGender(),
                Integer.valueOf(txtLevel.getText()).intValue(),
                ivs,
                evs,
                moves,
                pp,
                false
            );
        p.setName(txtNickname.getText());
        p.setShiny(chkShiny.isSelected());
        BattleField field = new NetBattleField(null, new AdvanceMechanics(4));
        p.attachToField(field, 0, 0);
        
        finalHp.setText(String.valueOf(p.getStat(Pokemon.S_HP)));
        finalAttack.setText(String.valueOf(p.getStat(Pokemon.S_ATTACK)));
        finalDefence.setText(String.valueOf(p.getStat(Pokemon.S_DEFENCE)));
        finalSpeed.setText(String.valueOf(p.getStat(Pokemon.S_SPEED)));
        finalSpcAttack.setText(String.valueOf(p.getStat(Pokemon.S_SPATTACK)));
        finalSpcDefence.setText(String.valueOf(p.getStat(Pokemon.S_SPDEFENCE)));
        
        HiddenPowerMove hiddenPower = new HiddenPowerMove();
        PokemonType type = null;
        int power = -1;
        try {
            hiddenPower.switchIn(p);
            type = hiddenPower.getType();
            power = hiddenPower.getPower();
        } catch (Exception e) {
            type = PokemonType.T_TYPELESS;
            power = -1;
        }
        ActionListener[] list = cboHiddenPower.getActionListeners();
        for (int i = 0; i < list.length; ++i) {
            cboHiddenPower.removeActionListener(list[i]);
        }
        cboHiddenPower.setSelectedItem(type);
        for (int i = 0; i < list.length; ++i) {
            cboHiddenPower.addActionListener(list[i]);
        }
        txtHpPower.setText(String.valueOf(power));
    }
    
    public void updateSprite(Pokemon p) {
        try {
            String species = p.getSpeciesName();
            boolean male = (p.getGender() != PokemonSpecies.GENDER_FEMALE);
            boolean shiny = p.isShiny();
            
            if (species.equals(m_lastSpecies)
                    && (male == m_lastMale)
                    && (shiny == m_lastShiny))
                return;
            
            m_lastSpecies = species;
            m_lastMale = male;
            m_lastShiny = shiny;
            
            m_sprite = SpriteLoader.getImage(species, true, male, shiny);
            if (m_sprite != null) {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(m_sprite, 0);
                tracker.waitForAll();
                m_spritePanel.repaint();
            }
        } catch (Exception e) {
            m_sprite = null;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboAbility;
    private javax.swing.JComboBox cboGender;
    private javax.swing.JComboBox cboHiddenPower;
    private javax.swing.JComboBox cboItem;
    private javax.swing.JComboBox cboNature;
    private javax.swing.JComboBox cboPokemon;
    private javax.swing.JComboBox cboPp1;
    private javax.swing.JComboBox cboPp2;
    private javax.swing.JComboBox cboPp3;
    private javax.swing.JComboBox cboPp4;
    private javax.swing.JCheckBox chkShiny;
    private javax.swing.JButton cmdSelectPokemon;
    private javax.swing.JLabel finalAttack;
    private javax.swing.JLabel finalDefence;
    private javax.swing.JLabel finalHp;
    private javax.swing.JLabel finalSpcAttack;
    private javax.swing.JLabel finalSpcDefence;
    private javax.swing.JLabel finalSpeed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblAttack;
    private javax.swing.JLabel lblDefence;
    private javax.swing.JLabel lblDistrubuted;
    private javax.swing.JLabel lblHp;
    private javax.swing.JLabel lblSpcAttack;
    private javax.swing.JLabel lblSpcDefence;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JScrollPane moveset;
    private javax.swing.JPanel panelSprite;
    private javax.swing.JTextField txtAttackEv;
    private javax.swing.JTextField txtAttackIv;
    private javax.swing.JTextField txtDefenceEv;
    private javax.swing.JTextField txtDefenceIv;
    private javax.swing.JTextField txtHpEv;
    private javax.swing.JTextField txtHpIv;
    private javax.swing.JTextField txtHpPower;
    private javax.swing.JTextField txtLevel;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextField txtSpcAttackEv;
    private javax.swing.JTextField txtSpcAttackIv;
    private javax.swing.JTextField txtSpcDefenceEv;
    private javax.swing.JTextField txtSpcDefenceIv;
    private javax.swing.JTextField txtSpeedEv;
    private javax.swing.JTextField txtSpeedIv;
    // End of variables declaration//GEN-END:variables
    
}
