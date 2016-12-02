/*
 * MoveTableModel.java
 *
 * Created on March 21, 2007, 5:20 PM
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
 */

package shoddybattleclient;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import mechanics.moves.*;

/**
 *
 * @author Colin
 */
public class MoveTableModel extends AbstractTableModel {
    
    private TableRow[] m_row;
    private ArrayList m_selected = new ArrayList();
    
    private static class TableRow {
        private Boolean m_enabled = new Boolean(false);
        private String m_category;
        private String m_move;
        private String m_type;
        private Integer m_pp;
        private Integer m_power;
        private Integer m_accuracy;
        
        public TableRow(String category,
                String move,
                String type,
                int pp,
                int power,
                int accuracy) {
            m_category = category;
            m_move = move;
            m_type = type;
            m_pp = new Integer(pp);
            m_power = new Integer(power);
            m_accuracy = new Integer(accuracy);
        }
    }
    
    /**
     * Get a value from the table at (i, j).
     */
    public Object getValueAt(int i, int j) {
        TableRow row = m_row[i];
        switch (j) {
            case 0: return row.m_enabled;
            case 1: return row.m_move;
            case 2: return row.m_category;
            case 3: return row.m_type;
            case 4: return row.m_pp;
            case 5: return row.m_power;
            case 6: return row.m_accuracy;
        }
        assert false;
        return null;
    }
    
    public String getColumnName(int col) {
        switch (col) {
            case 0: return "Selected?";
            case 1: return "Name";
            case 2: return "Origin";
            case 3: return "Type";
            case 4: return "PP";
            case 5: return "Power";
            case 6: return "Accuracy";
        }
        assert false;
        return null;
    }
    
    public int getColumnCount() {
        return 7;
    }
    
    public int getRowCount() {
        return m_row.length;
    }
    
    public Class getColumnClass(int c) {
         return getValueAt(0, c).getClass();
    }
    
    public boolean isCellEditable(int row, int col) {
        return (col == 0);
    }
     
    public void setValueAt(Object value, int i, int j) {
        TableRow row = m_row[i];
        switch (j) {
            case 0:
                Boolean bol = (Boolean)value;
                boolean b = bol.booleanValue();
                if (b) {
                    if (m_selected.size() < 4) {
                        row.m_enabled = bol;
                        m_selected.add(row.m_move);
                    }
                } else {
                    Iterator itr = m_selected.iterator();
                    while (itr.hasNext()) {
                        String name = (String)itr.next();
                        if (name.equals(row.m_move)) {
                            itr.remove();
                            break;
                        }
                    }
                    for (int k = 0; k < m_row.length; ++k) {
                        if (m_row[k].m_move.equals(row.m_move)) {
                            m_row[k].m_enabled = new Boolean(false);
                            fireTableCellUpdated(k, 0);
                        }
                    }
                }
                break;
            case 1:
                row.m_move = (String)value;
                break;
            case 2:
                row.m_category = (String)value;
                break;
            default:
                assert false;
        }
        fireTableCellUpdated(i, j);
    }
    
    /**
     * Set the moves that have been selected.
     */
    public void setSelectedMoves(String[] moves) {
        List list = Arrays.asList(moves);
        m_selected = new ArrayList(list);
        Set set = new HashSet(list);
        for (int i = 0; i < m_row.length; ++i) {
            m_row[i].m_enabled = new Boolean(set.contains(m_row[i].m_move));
            fireTableCellUpdated(i, 0);
        }
    }
    
    /**
     * Get the moves that have been selected.
     */
    public String[] getSelectedMoves() {
        return (String[])m_selected.toArray(new String[m_selected.size()]);
    }
    
    /**
     * Creates a new instance of MoveTableModel
     */
    public MoveTableModel(MoveList moveList, String[][] moves) {
        ArrayList list = new ArrayList();
        Set set = new HashSet();
        for (int i = 0; i < moves.length; ++i) {
            String[] type = moves[i];
            String category = MoveSet.getMoveType(i);
            for (int j = 0; j < type.length; ++j) {
                if (type[j] == null) {
                    continue;
                }
                String name = type[j];
                MoveListEntry entry = moveList.getMove(name);
                if (entry == null) {
                    continue;
                }
                if (set.contains(name)) {
                    continue;
                }
                set.add(name);
                PokemonMove move = entry.getMove();
                String ptype = move.getType().toString();
                int pp = move.getPp();
                int power = move.getPower();
                int accuracy = (int)(move.getAccuracy() * 100.0);
                TableRow row = new TableRow(
                        category, name, ptype, pp, power, accuracy);
                list.add(row);
            }
        }
        Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    TableRow t1 = (TableRow)o1;
                    TableRow t2 = (TableRow)o2;
                    return t1.m_move.compareToIgnoreCase(t2.m_move);
                }
            });
        m_row = (TableRow[])list.toArray(new TableRow[list.size()]);
    }
    
}
