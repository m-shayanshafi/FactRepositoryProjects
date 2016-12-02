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
package pl.org.minions.stigma.client.ui.swing.game.renderers;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * An implementation of {@link TableCellRenderer} using
 * JButton.
 * <p>
 * Named {@code ButtonTableHeader} by default (for synth
 * matching).
 * <p>
 * If an instance of {@link Icon} is passed as the header
 * value, the icon will be used instead of a label.
 */
public class ButtonTableHeaderRenderer extends JButton implements
                                                      TableCellRenderer
{
    private static final String DEFAULT_NAME = "ButtonTableHeader.renderer";

    private static final long serialVersionUID = 1L;

    private int pushedColumn;

    private int enteredColumn;

    /**
     * Constructor.
     */
    public ButtonTableHeaderRenderer()
    {
        super();
        setName(DEFAULT_NAME);
        pushedColumn = -1;
        enteredColumn = -1;

    }

    /** {@inheritDoc} */
    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        SortOrder sortOrder = null;
        boolean sortable = false;
        final RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
        if (rowSorter != null)
        {
            sortable =
                    ((TableRowSorter<? extends TableModel>) rowSorter).isSortable(column);
            if (rowSorter.getSortKeys().size() > 0)
            {
                final SortKey sortKey = rowSorter.getSortKeys().get(0);
                if (sortKey.getColumn() == column)
                {
                    sortOrder = sortKey.getSortOrder();
                }

            }
        }
        if (value == null) //In case of missing icons.
        {
            setText("?");
            setIcon(null);
        }
        else if (value instanceof Icon)
        {
            setText("");
            setIcon((Icon) value);
        }
        else
        {
            setText(value.toString());
            setIcon(null);
        }

        boolean isPressed =
                sortable && (column == pushedColumn || sortOrder != null);
        boolean isEntered = sortable && column == enteredColumn;

        getModel().setEnabled(sortable);

        getModel().setPressed(isPressed);
        getModel().setArmed(isPressed);
        getModel().setSelected(isPressed);
        getModel().setRollover(isEntered);

        return this;
    }

    /**
     * Sets the pressed column.
     * @param col
     *            pressed header column; <code>-1</code>
     *            means "no column"
     */
    public void setPressedColumn(int col)
    {
        pushedColumn = col;
    }

    /**
     * Sets the column under mouse pointer.
     * @param col
     *            header column entered by mouse;
     *            <code>-1</code> means "no column"
     */
    public void setEnteredColumn(int col)
    {
        enteredColumn = col;
    }

}
