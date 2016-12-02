/*
 * TeamBuilder.java
 *
 * Created on December 18, 2006, 10:36 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick and contributors
 * 
 * Contributors:
 *     Percival "Dragontamer" Tiglao (basic plugin framework)
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
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;
import java.util.prefs.*;
import shoddybattle.*;
import mechanics.*;
import mechanics.moves.MoveListEntry;
import mechanics.moves.HiddenPowerMove;

/**
 *
 * @author  Colin
 */
public class TeamBuilder extends JFrame {
    
    public static String TEAM_KEY = "shoddybattle.default_team";
    public static final String PLUGIN_KEY_PREFIX = "shoddybattle.plugin.";
    public static final String NUM_PLUGIN_KEY = "shoddybattle.numplugins";
    
    private PokemonStats[] m_stats = new PokemonStats[6];
    private boolean m_changed = false;
    private BattleMechanics m_mech = new AdvanceMechanics(4);
    private ModData m_data = ModData.getDefaultData();
    private String m_file;
    private final int defaultSizeOfmnuPluginMenu;
    
    public static String getDefaultTeam() {
        return Preferences.userRoot().get(TEAM_KEY, null);
    }
    
    public ModData getModData() {
        return m_data;
    }
    
    public void informChangeMade() {
        m_changed = true;
    }
    
    public TeamBuilder() {
        this(ModData.getDefaultData(), true);
    }
    
    public TeamBuilder(boolean refresh) {
        this(ModData.getDefaultData(), refresh);
    }
    
    public TeamBuilder(ModData data) {
        this(data, true);
    }
    
    /** Creates new form TeamBuilder */
    public TeamBuilder(ModData data, boolean refresh) {
        m_data = data;
        initComponents();
        defaultSizeOfmnuPluginMenu = mnuPluginMenu.getItemCount();
        
        for (int i = 0; i < 6; ++i) {
            PokemonStats stats = new PokemonStats(this, i);
            stats.setRefreshingEnabled(refresh);
            String tabName;
            if (refresh) {
                stats.refreshMoveList();
                //stats.updateStats();
                stats.updateSprite(null);
                tabName = stats.getPokemon().getName();
            } else {
                tabName = "";
            }
            m_stats[i] = stats;
            
            tabs.addTab(tabName, stats.getContentPane());
        }
        
        Component c = m_stats[0];
        setSize(c.getWidth() + 50, c.getHeight() + 100);
        String[] keys;
        try {
            Preferences userRoot = Preferences.userRoot();
            keys = userRoot.keys();
            for(int i=0; i<keys.length; i++){
                System.out.println("Looking at key: " + keys[i]);
                if(keys[i].startsWith(PLUGIN_KEY_PREFIX)){
                    String toLoad = userRoot.get(keys[i], null);
                    if(toLoad != null) loadPluginRaw(new File(toLoad));
                }
            }
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updatePokemonName(int i, String name) {
        if (tabs.getTabCount() <= i) {
            return;
        }
        tabs.setTitleAt(i, name);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuSave = new javax.swing.JMenuItem();
        mnuOpen = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuUseThisTeam = new javax.swing.JMenuItem();
        mnuExport = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuMoveToFront = new javax.swing.JMenuItem();
        mnuRandomise = new javax.swing.JMenuItem();
        mnuPluginMenu = new javax.swing.JMenu();
        mnuPlugin = new javax.swing.JMenuItem();
        mnuRemovePlugin = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        mnuSave.setText("Save");
        mnuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSave);

        mnuOpen.setText("Open");
        mnuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenActionPerformed(evt);
            }
        });
        jMenu1.add(mnuOpen);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Tools");

        mnuUseThisTeam.setText("Use This Team");
        mnuUseThisTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUseThisTeamActionPerformed(evt);
            }
        });
        jMenu3.add(mnuUseThisTeam);

        mnuExport.setText("Export to Text");
        mnuExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportActionPerformed(evt);
            }
        });
        jMenu3.add(mnuExport);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Edit");

        mnuMoveToFront.setText("Move to Front");
        mnuMoveToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMoveToFrontActionPerformed(evt);
            }
        });
        jMenu2.add(mnuMoveToFront);

        mnuRandomise.setText("Randomise Team");
        mnuRandomise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRandomiseActionPerformed(evt);
            }
        });
        jMenu2.add(mnuRandomise);

        jMenuBar1.add(jMenu2);

        mnuPluginMenu.setText("Plugin");
        mnuPluginMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPluginMenuActionPerformed(evt);
            }
        });

        mnuPlugin.setText("Load Plugin...");
        mnuPlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPluginActionPerformed(evt);
            }
        });
        mnuPluginMenu.add(mnuPlugin);

        mnuRemovePlugin.setText("Remove Plugin...");
        mnuRemovePlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemovePluginActionPerformed(evt);
            }
        });
        mnuPluginMenu.add(mnuRemovePlugin);
        mnuPluginMenu.add(jSeparator1);

        jMenuBar1.add(mnuPluginMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(tabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(tabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void mnuRemovePluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemovePluginActionPerformed
        System.out.println("Removing Plugin");
        String pluginNames[] = new String[mnuPluginMenu.getItemCount() - 2];
        for(int i=defaultSizeOfmnuPluginMenu; i<mnuPluginMenu.getItemCount(); i++){
            pluginNames[i-defaultSizeOfmnuPluginMenu] = mnuPluginMenu.getItem(i).toString();
        }
        JList dataList = new JList(pluginNames);
        int toRemove[] = RemovePluginDialogue.load(this, dataList);
        
        Preferences userRoot = Preferences.userRoot();
        for(int i=0; i<toRemove.length; i++){
            System.out.println("Removing index" + (toRemove[i] + defaultSizeOfmnuPluginMenu - i));
            mnuPluginMenu.remove(toRemove[i] + defaultSizeOfmnuPluginMenu - i);
            userRoot.remove(PLUGIN_KEY_PREFIX + pluginNames[toRemove[i]]);
        }
        
        int pluginNum = userRoot.getInt(NUM_PLUGIN_KEY, toRemove.length);
        userRoot.putInt(NUM_PLUGIN_KEY, pluginNum-toRemove.length);
        try {
            userRoot.flush();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_mnuRemovePluginActionPerformed

    private void mnuExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportActionPerformed
        exportTeam();
}//GEN-LAST:event_mnuExportActionPerformed

    private void mnuPluginMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPluginMenuActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_mnuPluginMenuActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void mnuPluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPluginActionPerformed
         JFileChooser chooser = new JFileChooser();
         chooser.setMultiSelectionEnabled(false);
         switch(chooser.showOpenDialog(this)){
             case JFileChooser.CANCEL_OPTION:
                 break;
             case JFileChooser.APPROVE_OPTION:
                 if (!loadPlugin(chooser.getSelectedFile())) {
                    JOptionPane.showMessageDialog(this, "Plugin Failed to Load");
                 } else {
                    JOptionPane.showMessageDialog(this, "Plugin Loaded");
                 }
                 break;
             case JFileChooser.ERROR_OPTION:
                 break;
             default:
                 throw new RuntimeException(
                         "JDialog.showOpenDialog returned unexpected value");
         }
    }//GEN-LAST:event_mnuPluginActionPerformed

    class CustomMenuItem extends JMenuItem{
        private PluginInterface m_pi;
        public CustomMenuItem(Class c) throws InstantiationException, IllegalAccessException {
            Object newInstance = c.newInstance();
            if (newInstance instanceof PluginInterface) {
                m_pi = (PluginInterface)newInstance;
            } else {
                throw new IllegalArgumentException(
                        "Class passed must implement PluginInterface");
            }
            setText(m_pi.getName());
            addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Pokemon pkmn[] = new Pokemon[m_stats.length];
                    for (int i = 0; i < pkmn.length; ++i) {
                        pkmn[i] = m_stats[i].getPokemon();
                    }
                    m_pi.invoke(m_data, pkmn, tabs.getSelectedIndex());
                    for (int i = 0; i < pkmn.length; ++i) {
                        m_stats[i].loadFromPokemon(pkmn[i]);
                    }
                }
            });
        }
        
        public String toString(){
            return m_pi.getName();
        }
        
    }
    
    public boolean loadPlugin(File f){
        String name = loadPluginRaw(f);
        if(name != null){
            Preferences userRoot = Preferences.userRoot();
            int pluginNum = userRoot.getInt(NUM_PLUGIN_KEY, 0);
            userRoot.put(PLUGIN_KEY_PREFIX + name, f.getAbsolutePath());
            userRoot.putInt(NUM_PLUGIN_KEY, ++pluginNum);
            try {
                userRoot.flush();
            } catch (BackingStoreException ex) {
                ex.printStackTrace();
            }
        }
        return name != null;
    }
    
    public String loadPluginRaw(File f) {
        JMenu pluginMenu = mnuPluginMenu;
        Class theClass = null;
        String name = null;
        
        URL urls[] = new URL[1];
        FileInputStream fis = null;
        JarInputStream jis = null;
        try {
            urls[0] = f.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(urls,
                    getClass().getClassLoader());
            fis = new FileInputStream(f);
            jis = new JarInputStream(fis);
            Manifest m = jis.getManifest();
            String mainclass = m.getMainAttributes().getValue("Main-Class");
            theClass = classLoader.loadClass(mainclass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (jis != null) jis.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Arrays.asList(theClass.getInterfaces())
                .contains(PluginInterface.class)) {
            return null;
        }
        try {
            pluginMenu.add(new CustomMenuItem(theClass));
            Object o = theClass.newInstance();
            name = ((PluginInterface)o).getName();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            return null;
        }
        
        return name;
    }
    
private void mnuUseThisTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUseThisTeamActionPerformed
    if (m_changed && !saveTeam())
        return;
    Preferences.userRoot().put(TEAM_KEY, m_file);
}//GEN-LAST:event_mnuUseThisTeamActionPerformed

private void mnuMoveToFrontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMoveToFrontActionPerformed
    Pokemon first = m_stats[0].getPokemon();
    int current = tabs.getSelectedIndex();
    m_stats[0].loadFromPokemon(m_stats[current].getPokemon());
    m_stats[current].loadFromPokemon(first);
    tabs.setSelectedIndex(0);
}//GEN-LAST:event_mnuMoveToFrontActionPerformed

    private void mnuRandomiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRandomiseActionPerformed
        for (int i = 0; i < m_stats.length; ++i) {
            Pokemon p = null;
            do {
                p = Pokemon.getRandomPokemon(m_data, m_mech);
            } while (p == null);
            m_stats[i].loadFromPokemon(p);
        }
    }//GEN-LAST:event_mnuRandomiseActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (m_changed) {
            int result = JOptionPane.showConfirmDialog(this,
                    "Unsaved changes may remain in this team. "
                    + "Do you want to save before closing?",
                    "Unsaved changes",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (!saveTeam()) {
                    return;
                }
            }
        }
        dispose();
    }//GEN-LAST:event_formWindowClosing

    
    /**
     * Allow a team to be loaded from disc.
     */
    public boolean loadTeam() {
        JFileChooser choose = new JFileChooser();
        if (choose.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return false;
        
        File f = choose.getSelectedFile();
        if (!f.exists()) {
            // Cannot open a file that doesn't exist!
            return false;
        }
        
        Pokemon[] pokemon = new Pokemon[6];
        ModData modData = Pokemon.loadTeam(f, pokemon);
        
        if (modData == null) {
            new MessageBox(this, "Error", "The team could not be opened.")
                .setVisible(true);
            return false;
        }
        
        m_file = f.getAbsolutePath();
        m_data = modData;
        
        if (modData == null) {
            new MessageBox(this, "Error", "The team could not be opened.")
                .setVisible(true);
            return false;
        }
        
        m_file = f.getAbsolutePath();
        m_data = modData;
        for (int i = 0; i < pokemon.length; ++i) {
            PokemonStats stats = m_stats[i];
            stats.setRefreshingEnabled(true);
            stats.refreshPokemonList();
            stats.loadFromPokemon(pokemon[i]);
        }
        
        m_changed = false;
        return true;
    }
    
    private void mnuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenActionPerformed
        loadTeam();
    }//GEN-LAST:event_mnuOpenActionPerformed

    private boolean saveTeam() {
        // Save this team.
        Pokemon[] pokemon = new Pokemon[6];
        for (int i = 0; i < 6; ++i) {
            Pokemon p = pokemon[i] = m_stats[i].getPokemon();
            try {
                p.validate(m_data);
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(this, p.getName() +
                        " is an invalid pokemon for the following reason:\n\n"
                        + e.getMessage() + "\n\n"
                        + "This error does not prevent you from saving the "
                        + "team,\nbut you will not be able to use the team in a "
                        + "battle until you correct it.");
            }
        }
        
        JFileChooser choose = new JFileChooser();
        if (choose.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return false;
        
        File f = choose.getSelectedFile();
        if (f.exists()) {
            // File exists. Are you sure you want to overwrite?
            String message = f.getPath() + " already exists. "
                    + "Are you sure that you want to overwrite this file?";
            int result = JOptionPane.showConfirmDialog(this,
                    message,
                    "File exists",
                    JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        try {
            FileOutputStream file = new FileOutputStream(f);
            ObjectOutputStream obj = new ObjectOutputStream(file);
            // First thing in file is mod data identifier.
            obj.writeObject(m_data.getName());
            obj.writeObject(pokemon);
            obj.flush();
            obj.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        m_file = f.getAbsolutePath();
        m_changed = false;
        return true;
    }
    
    private void exportTeam() {
        // Export this team to text.
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < m_stats.length; ++i) {
            Pokemon p = m_stats[i].getPokemon();
            buffer.append(p.getName());
            int gender = p.getGender();
            if (gender != PokemonSpecies.GENDER_NONE) {
                buffer.append(" ");
                buffer.append(gender == PokemonSpecies.GENDER_MALE ? "(♂)" : "(♀)");
            }
            String itemName = p.getItemName();
            if (itemName.equals("")) itemName = "Nothing";
            buffer.append(" @ ");
            buffer.append(itemName);
            buffer.append("\n");
            buffer.append("Ability: ");
            buffer.append(p.getAbilityName());
            buffer.append("\n");
            buffer.append("EVs: ");
            for (int j = 0; j < 6; ++j) {
                final int ev = p.getEv(j);
                if (ev == 0) continue;
                buffer.append(ev);
                buffer.append(" ");
                buffer.append(Pokemon.getStatShortName(j));
                buffer.append("/");
            }
            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append("\n");
            PokemonNature nature = p.getNature();
            int benefits = nature.getBenefits();
            int harms = nature.getHarms();
            buffer.append(nature.getName());
            buffer.append(" nature ");
            buffer.append("(");
            if ((benefits == -1) || (harms == -1)) {
                buffer.append("neutral)");
            } else {
                buffer.append("+");
                buffer.append(Pokemon.getStatShortName(benefits));
                buffer.append(", ");
                buffer.append("-");
                buffer.append(Pokemon.getStatShortName(harms));
                buffer.append(")");
            }
            buffer.append("\n");
            for (int j = 0; j < 4; ++j) {
                MoveListEntry entry = p.getMove(j);
                if (entry == null) continue;
                String name = entry.getName();
                buffer.append("- ");
                buffer.append(name);
                if (name.equals("Hidden Power")) {
                    buffer.append(" [");
                    HiddenPowerMove hiddenPower = new HiddenPowerMove();
                    PokemonType type = null;
                    try {
                        hiddenPower.switchIn(p);
                        type = hiddenPower.getType();
                    } catch (Exception e) {
                        type = PokemonType.T_TYPELESS;
                    }
                    buffer.append(type);
                    buffer.append("]");
                }
                buffer.append("\n");
            }
            buffer.append("---\n");
        }
        new MessageBox(this, "Export to Text", new String(buffer)).setVisible(true);
    }
    
    private void mnuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveActionPerformed
        saveTeam();
    }//GEN-LAST:event_mnuSaveActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TeamBuilder().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem mnuExport;
    private javax.swing.JMenuItem mnuMoveToFront;
    private javax.swing.JMenuItem mnuOpen;
    private javax.swing.JMenuItem mnuPlugin;
    private javax.swing.JMenu mnuPluginMenu;
    private javax.swing.JMenuItem mnuRandomise;
    private javax.swing.JMenuItem mnuRemovePlugin;
    private javax.swing.JMenuItem mnuSave;
    private javax.swing.JMenuItem mnuUseThisTeam;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
    
}