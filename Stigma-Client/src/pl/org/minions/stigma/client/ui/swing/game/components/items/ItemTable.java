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
package pl.org.minions.stigma.client.ui.swing.game.components.items;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.ItemAction;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemKindRowFilter;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableEntry;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableModel;
import pl.org.minions.stigma.client.ui.swing.game.renderers.ButtonTableHeaderRenderer;
import pl.org.minions.stigma.client.ui.swing.game.renderers.HeaderListener;
import pl.org.minions.stigma.client.ui.swing.game.tooltips.ItemToolTip;
import pl.org.minions.stigma.game.item.Item;

/**
 * Table for displaying list of items (for example on
 * ground, in inventory or during trade).
 */
public class ItemTable extends JTable
{
    private final class UiEventListenersRemover implements AncestorListener
    {
        @Override
        public void ancestorRemoved(AncestorEvent event)
        {
            final UiEventRegistry uiEventRegistry =
                    Client.globalInstance().uiEventRegistry();
            final ItemTableModel dm = (ItemTableModel) getModel();
            uiEventRegistry.removeItemDataChangedListener(dm);
            uiEventRegistry.removeItemTypeLoadedListener(dm);
        }

        @Override
        public void ancestorMoved(AncestorEvent event)
        {

        }

        @Override
        public void ancestorAdded(AncestorEvent event)
        {

        }
    }

    private final class ItemTableMouseListener extends MouseAdapter
    {
        /** {@inheritDoc} */
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (e.isPopupTrigger())
                showPopup(e);
        }

        /** {@inheritDoc} */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (e.isPopupTrigger())
                showPopup(e);
        }

        /** {@inheritDoc} */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 1)
            {
                final int row = rowAtPoint(ItemTable.this.getMousePosition());
                if (row < 0)
                    return;

                final int modelRow = convertRowIndexToModel(row);
                if (modelRow < 0)
                    return;

                ((ItemTableModel) getModel()).setItemRecent(modelRow, false);
            }
            else if (e.getClickCount() == 2
                && SwingUtilities.isLeftMouseButton(e))
            {
                if (ItemTable.this.actions.length == 0)
                    return;

                final ItemAction action = ItemTable.this.actions[0];

                if (action == null)
                    return;

                e.consume();

                final int row = rowAtPoint(ItemTable.this.getMousePosition());

                if (row < 0)
                    return;

                final int modelRow = convertRowIndexToModel(row);
                if (modelRow < 0)
                    return;

                final Item item =
                        ((ItemTableModel) ItemTable.this.getModel()).getItem(modelRow);

                action.setItem(item);

                if (action.isEnabled())
                    action.actionPerformed(null);
            }
        }

        private void showPopup(MouseEvent e)
        {
            if (actions.length == 0)
                return;

            final int row = rowAtPoint(ItemTable.this.getMousePosition());

            if (row < 0)
                return;

            final Item item =
                    ((ItemTableModel) ItemTable.this.getModel()).getItem(row);

            for (ItemAction action : ItemTable.this.actions)
                if (action != null)
                    action.setItem(item);

            itemPopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private static final int ROW_HEIGHT = 34;
    private static final long serialVersionUID = 1L;

    private ItemAction[] actions = {};
    private JPopupMenu itemPopupMenu = new JPopupMenu();;

    /**
     * Constructor.
     * @param dm
     *            table model
     * @param cm
     *            column model
     */
    public ItemTable(ItemTableModel dm, ItemTableColumnModel cm)
    {
        super(dm, cm);
        final TableRowSorter<ItemTableModel> sorter =
                new TableRowSorter<ItemTableModel>(dm);

        final UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();
        uiEventRegistry.addItemDataChangedListener(dm);
        uiEventRegistry.addItemTypeLoadedListener(dm);

        setRowSorter(sorter);
        for (TableColumn column : Collections.list(cm.getColumns()))
        {
            final Comparator<ItemTableEntry> defaultSortOrder =
                    ((ItemTableColumnModel.Column) column.getIdentifier()).getDefaultSortOrder();
            if (defaultSortOrder != null)
                sorter.setComparator(column.getModelIndex(), defaultSortOrder);
            else
                sorter.setSortable(column.getModelIndex(), false);
        }

        if (cm.getColumnsMapping()
              .containsValue(ItemTableColumnModel.Column.Recency))
        {
            sorter.setSortKeys(Collections.singletonList(new SortKey(cm.getColumnIndex(ItemTableColumnModel.Column.Recency),
                                                                     SortOrder.ASCENDING)));

        }
        else
        {
            sorter.setSortKeys(Collections.singletonList(new SortKey(cm.getColumnIndex(ItemTableColumnModel.Column.Name),
                                                                     SortOrder.ASCENDING)));
        }
        setName("ItemTable");
        setRowHeight(ROW_HEIGHT);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setBorder(BorderFactory.createEmptyBorder()); //Any other border will obscure items. This is strange.

        getTableHeader().setReorderingAllowed(false);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setColumnSelectionAllowed(false);

        ButtonTableHeaderRenderer renderer = new ButtonTableHeaderRenderer();
        TableColumnModel model = getColumnModel();
        int n = model.getColumnCount();
        for (int i = 0; i < n; i++)
        {
            model.getColumn(i).setHeaderRenderer(renderer);
        }
        JTableHeader header = getTableHeader();
        final HeaderListener headerListener =
                new HeaderListener(header, renderer);
        header.addMouseListener(headerListener);
        header.addMouseMotionListener(headerListener);

        addMouseListener(new ItemTableMouseListener());

        addAncestorListener(new UiEventListenersRemover());
    }

    /**
     * Sets pop-up menu actions.
     * @param actions
     *            enumeration of actions, <code>null</code>
     *            entries are converted to separators, first
     *            entry is default
     */
    public void setPopupActions(ItemAction... actions)
    {
        itemPopupMenu.removeAll();

        for (ItemAction action : actions)
        {
            if (action == null)
                itemPopupMenu.addSeparator();
            else
                itemPopupMenu.add(action);
        }

        this.actions = actions;
    }

    /** {@inheritDoc} */
    @Override
    public JToolTip createToolTip()
    {
        if (getMousePosition() == null)
            return super.createToolTip();

        final int row = this.rowAtPoint(getMousePosition());

        if (row < 0)
            return super.createToolTip();

        final int modelRow = convertRowIndexToModel(row);

        if (modelRow < 0)
            return super.createToolTip();

        ItemTableModel model = (ItemTableModel) this.getModel();

        JToolTip tip = new ItemToolTip(model.getItem(modelRow));
        tip.setComponent(this);

        //model.setItemRecent(0, false);
        return tip;
    }

    /**
     * Sets an {@link ItemKindRowFilter} as the filter in
     * underlying {@link TableRowSorter}.
     * @param filter
     *            filter for including items of selected
     *            type
     */
    public void setItemKindFilter(ItemKindRowFilter filter)
    {
        @SuppressWarnings("unchecked")
        final TableRowSorter<ItemTableModel> rowSorter =
                (TableRowSorter<ItemTableModel>) getRowSorter();
        rowSorter.setRowFilter(filter);
    }
}
