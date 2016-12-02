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

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.CurrencyPanel;
import pl.org.minions.stigma.client.ui.swing.game.components.ImageProxyComponent;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableEntry;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.utils.logger.Log;

/**
 * Cell renderer for stigma table which contains items.
 */
public class ItemTableCellRenderer implements TableCellRenderer
{
    private static final String STAR_ICON_PATH = "img/client/icons/Star.png";
    private static final Icon STAR_ICON = Resourcer.loadIcon(STAR_ICON_PATH);

    private ItemTableColumnModel.Column itemColumn;

    /**
     * Constructor.
     * @param column
     *            column id for which this renderer should
     *            be created
     */
    public ItemTableCellRenderer(ItemTableColumnModel.Column column)
    {
        super();
        this.itemColumn = column;
        assert this.itemColumn != null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        ItemTableEntry entry = (ItemTableEntry) value;
        Item i = entry.getItem();
        final Rectangle cellRect = table.getCellRect(row, column, false);
        JComponent result = null;

        assert column >= 0 && column < table.getColumnCount();

        switch (itemColumn)
        {
            case Id:
                result = new ItemTableLabel(Integer.toString(row));
                break;
            case Name:
                result = new ItemTableLabel(i.getName());
                if (i.getKind() != ItemKind.OTHER
                    && !Client.globalInstance().getPlayerActor().canEquip(i))
                    result.setForeground(Color.red);
                break;
            case Value:
                result = new CurrencyPanel(i.getValue())
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Overridden for performance purposes -
                     * see bug <a
                     * href=
                     * "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6700748"
                     * >6700748</a> .
                     * @return {@code false}
                     */
                    @Override
                    public boolean isVisible()
                    {
                        return false;
                    }
                };
                break;
            case Weight:
                result =
                        new ItemTableLabel(Integer.toString(i.getWeight()),
                                           SwingConstants.TRAILING);
                result.setSize(cellRect.getSize());
                break;
            case Type:
                ItemKind ik = i.getKind();
                switch (ik)
                {
                    case ARMOR:
                        result = new ItemTableLabel("A");
                        break;
                    case OTHER:
                        result = new ItemTableLabel("O");
                        break;
                    case WEAPON:
                        result = new ItemTableLabel("W");
                        break;
                    default:
                        result = new ItemTableLabel("!");
                }
                break;
            case Icon:
                ImageProxy ip = null;
                if (i.isComplete() && i.getType().getInventoryIcon() != null
                    && !i.getType().getInventoryIcon().isEmpty())
                {
                    ip =
                            ImageDB.globalInstance()
                                   .getItemIcon(i.getKind(),
                                                i.getType().getInventoryIcon());
                }
                ImageProxyComponent ic = new ImageProxyComponent(ip)
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Overridden for performance purposes -
                     * see bug <a
                     * href=
                     * "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6700748"
                     * >6700748</a> .
                     * @return {@code false}
                     */
                    @Override
                    public boolean isVisible()
                    {
                        return false;
                    }
                };
                ic.setDefaultImage(VisualizationGlobals.DEFAULT_ITEM_IMAGE);
                result = ic;
                break;
            case Recency:
                if (entry.isRecent())
                    result = new ItemTableLabel(STAR_ICON);
                else
                    result = new ItemTableLabel("");
                result.setSize(cellRect.getSize());

                break;
            default:
                result = new ItemTableLabel("#ERROR");
                Log.logger.error("Unknown ItemTableModel column name.");
                break;
        }
        if (isSelected)
        {
            result.setOpaque(true);
            result.setBackground(Color.lightGray);
        }
        else
            result.setOpaque(false);

        result.setToolTipText(Integer.toString(row));

        return result;
    }
}
