/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.client.ui.swing.game.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.game.item.Item;

/**
 * This is an specific table model written to support
 * containing item data.
 */
public class ItemTableModel extends AbstractTableModel implements
                                                      ItemDataChangedListener,
                                                      ItemTypeLoadedListener
{
    private static final long serialVersionUID = 1L;

    private ArrayList<ItemTableEntry> items;

    private int sequentialNumber;

    private Map<Integer, ItemTableColumnModel.Column> columns;

    /**
     * Constructor.
     * @param columns
     *            columns mapping
     */
    public ItemTableModel(Map<Integer, ItemTableColumnModel.Column> columns)
    {
        this(columns, null);
    }

    /**
     * Constructor.
     * @param columns
     *            columns mapping
     * @param data
     *            initial data
     */
    public ItemTableModel(Map<Integer, ItemTableColumnModel.Column> columns,
                          Collection<Item> data)
    {
        items = new ArrayList<ItemTableEntry>();

        this.columns = columns;

        if (data != null)
            addItems(data);
    }

    /**
     * Adds items to model.
     * @param data
     *            collection of items
     */
    public void addItems(Collection<Item> data)
    {
        int firstIndex = items.size();

        for (Item item : data)
            items.add(new ItemTableEntry(item, sequentialNumber++));

        int lastIndex = items.size() - 1;

        if (lastIndex >= firstIndex)
            fireTableRowsInserted(firstIndex, lastIndex);
    }

    /**
     * Adds item to model.
     * @param item
     *            item to add
     */
    public void addItem(Item item)
    {
        int index = items.size();
        items.add(new ItemTableEntry(item, sequentialNumber++));
        fireTableRowsInserted(index, index);
    }

    /**
     * Removes item from model.
     * @param index
     *            index (row number) of item
     */
    public void removeByIndex(int index)
    {
        items.remove(index);
        fireTableRowsDeleted(index, index);
    }

    /**
     * Removes item from model.
     * @param itemId
     *            id of item to remove
     */
    public void removeById(int itemId)
    {
        final int index = findIndexForId(itemId);
        items.remove(index);
        fireTableRowsDeleted(index, index);
    }

    private int findIndexForId(int itemId)
    {
        for (int index = 0; index < items.size(); index++)
        {
            if (items.get(index).getItem().getId() == itemId)
            {
                return index;
            }
        }
        return -1;
    }

    /**
     * Clears the contents of the model.
     */
    public void clear()
    {
        int itemsSize = items.size();
        items.clear();
        if (itemsSize > 0)
            fireTableRowsDeleted(0, itemsSize - 1);
    }

    /**
     * Returns item in selected row.
     * @param row
     *            selected row
     * @return item or null if no item was found
     */
    public Item getItem(int row)
    {
        return items.get(row).getItem();
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int column)
    {
        return columns.get(column).getHeaderLabel();
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return TableColumn.class;
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount()
    {
        return items.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return items.get(rowIndex);
    }

    /**
     * Returns {@code true} when model contains item.
     * @param id
     *            id of item to check
     * @return {@code true} when model contains item.
     */
    public boolean contains(int id)
    {
        for (ItemTableEntry e : items)
            if (e.getItem().getId() == id)
                return true;
        return false;
    }

    /**
     * Returns {@code true} when model contains item.
     * @param i
     *            item to check
     * @return {@code true} when model contains item.
     */
    public boolean contains(Item i)
    {
        return items.contains(i);
    }

    /** {@inheritDoc} */
    @Override
    public void itemDataChanged(int id)
    {
        for (int row = 0; row < getRowCount(); ++row)
        {
            Item item = items.get(row).getItem();
            if (id == item.getId())
            {
                fireTableRowsUpdated(row, row);
                return;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void itemTypeLoaded(short id)
    {
        for (int row = 0; row < getRowCount(); ++row)
        {
            Item item = items.get(row).getItem();
            if (id == item.getType().getId())
            {
                fireTableRowsUpdated(row, row);
                return;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    /**
     * Sets the recency attribute of an entry.
     * @param row
     *            data row
     * @param recent
     *            new recency attribute value
     */
    public void setItemRecent(int row, boolean recent)
    {
        items.get(row).setRecent(recent);
        fireTableCellUpdated(row, row);
    }

}
