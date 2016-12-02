/*
 * UserTableModel.java
 *
 * Created on April 22, 2007, 6:09 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
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
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattleclient;
import shoddybattle.util.TimeInterval;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import netbattle.messages.*;
import netbattle.database.registry.AccountRegistry;

/**
 *
 * @author Colin
 */
public class UserTableModel extends AbstractTableModel {
    
    private TableRow[] m_row;
    private HumanClient m_server;
    
    private class TableRow implements Comparable {
        public String user, ip, time;
        public Boolean banned;
        public Integer level;
        public long date;
        public JButton modify;
        
        public TableRow(String user, String ip, int level, long expiration) {
            this.user = user;
            this.ip = (ip != null) ? ip : "-";
            this.date = expiration;
            this.level = new Integer(level);
            Date d = new Date(date);
            boolean isBanned = Calendar.getInstance().getTime().before(d);
            banned = new Boolean(isBanned);
            if (isBanned) {
                TimeInterval interval = TimeInterval.getDeltaInterval(d);
                time = interval.getApproximation();
            } else {
                time = "-";
            }
            modify = new JButton(isBanned ? "Change" : "Ban");
            modify.addMouseListener(new MouseInputAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        String user = TableRow.this.user;
                        long date = BanDialog.getBanDate(null, user);
                        if (date != -1) {
                            m_server.sendMessage(new BanMessage(user, date));
                        }
                        updateData();
                    }
                });
        }
        
        public int compareTo(Object o2) {
            TableRow r2 = (TableRow)o2;
            return user.compareToIgnoreCase(r2.user);
        }
    }
    
    public String getColumnName(int col) {
        switch (col) {
            case 0: return "User";
            case 1: return "Level";
            case 2: return "IP Address";
            case 3: return "Banned?";
            case 4: return "Remaining Time";
            case 5: return "Set Ban Time";
        }
        return null;
    }
    
    public Object getValueAt(int i, int j) {
        TableRow row = m_row[i];
        if (row == null) {
            return null;
        }
        switch (j) {
            case 0: return row.user;
            case 1: return row.level;
            case 2: return row.ip;
            case 3: return row.banned;
            case 4: return row.time;
            case 5: return row.modify;
        }
        return null;
    }
    
    public int getColumnCount() {
        return 6;
    }
    
    public int getRowCount() {
        return m_row.length;
    }
    
    public Class getColumnClass(int c) {
        Object obj = getValueAt(0, c);
        if (obj != null) {
            return obj.getClass();
        }
        return null;
    }
    
    public boolean isCellEditable(int row, int col) {
        if (col == 3) {
            return true;
        }
        if (col != 1) {
            return false;
        }
        return (m_server.getUserLevel() >= AccountRegistry.LEVEL_ADMIN);
    }
    
    public void setValueAt(Object value, int i, int j) {
        TableRow row = m_row[i];
        
        if (j == 1) {
            int level = ((Integer)value).intValue();
            String str;
            if (level == 0) {
                str = "a regular user";
            } else if (level == 1) {
                str = "a moderator";
            } else if (level == 2) {
                str = "an administrator";
            } else {
                JOptionPane.showMessageDialog(null, "Level must be one of 0 (regular user), 1 (moderator), or 2 (administrator).");
                return;
            }
            
            if (JOptionPane.showConfirmDialog(null,
                    "Are you sure that you want to make "
                        + row.user + " into " + str + "?", "Change Level",
                    JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }
            
            m_server.sendMessage(new WelcomeMessage(row.user, level));
            updateData();
            
            return;
        }
        
        if (((Boolean)value).booleanValue()) {
            return;
        }
        
        if (JOptionPane.showConfirmDialog(null,
                "Are you sure that you want to unban " + row.user + "?",
                "Unban", JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        
        m_server.sendMessage(new BanMessage(row.user, 0));
        updateData();
    }
    
    private void updateData() {
        m_server.sendMessage(new UserTableMessage());
    }
    
    public void updateData(String[] user, String[] ip, int[] level, long[] date) {
        m_row = new TableRow[user.length];
        for (int i = 0; i < m_row.length; ++i) {
            m_row[i] = new TableRow(user[i], ip[i], level[i], date[i]);
        }
        Collections.sort(Arrays.asList(m_row));
        fireTableDataChanged();
    }
    
    /**
     * Creates a new instance of UserTableModel
     */
    public UserTableModel(HumanClient server, String[] user, String[] ip, int[] level, long[] date) {
        m_server = server;
        updateData(user, ip, level, date);
    }
    
    public static void showUserTable(final HumanClient server,
            final JFrame parent,
            final String[] user,
            final String[] ip,
            final int[] level,
            final long[] date) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UserTableModel model = new UserTableModel(server, user, ip, level, date);
                JTable table = new JButtonTable();
                table.setModel(model);
                JScrollPane scroll = new JScrollPane(table);
                JFrame frame = new JFrame("Server Membership");
                frame.getContentPane().add(scroll);
                table.setVisible(true);
                scroll.setVisible(true);
                frame.setSize(750, 400);
                server.setUserTable(model);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            server.setUserTable(null);
                        }
                    });
                frame.setVisible(true);
            }
        });
    }
    
    public static void main(String[] params) {
        showUserTable(
                null,
                new JFrame(),
                new String[] { "Colin", "Ben" },
                new String[] { "serenuscms.com", "100.100.100.100" },
                new int[] { 2, -1 },
                new long[] { 0, 1178852622712L }
            );
    }
    
}
