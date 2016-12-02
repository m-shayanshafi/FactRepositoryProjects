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

import java.util.HashMap;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import pl.org.minions.stigma.client.ui.swing.game.components.items.ItemTable;
import pl.org.minions.stigma.client.ui.swing.game.renderers.ActorTableCellRenderer;
import pl.org.minions.stigma.client.ui.swing.game.renderers.ButtonTableHeaderRenderer;
import pl.org.minions.utils.i18n.Translated;

/**
 * Column model for {@link ItemTable}.
 */
public class ActorTableColumnModel extends DefaultTableColumnModel
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String NAME_HEADER = "Name";
    @Translated
    private static String STATUS_HEADER = "Status";

    /**
     * All supported columns enum.
     */
    public enum Column
    {
        Name
        {
            @Override
            public String getHeader()
            {
                return NAME_HEADER;
            }
        },
        STATUS
        {
            @Override
            public String getHeader()
            {
                return STATUS_HEADER;
            }
        };

        /**
         * Returns translated column name.
         * @return column name.
         */
        public abstract String getHeader();
    }

    /**
     * Constructor.
     */
    public ActorTableColumnModel()
    {
        // CHECKSTYLE:OFF
        TableCellRenderer headerRenderer = new ButtonTableHeaderRenderer();
        TableColumn tc;
        tc = new TableColumn(0);
        tc.setHeaderValue(Column.Name.getHeader());
        tc.setCellRenderer(new ActorTableCellRenderer(Column.Name));
        tc.setHeaderRenderer(headerRenderer);
        addColumn(tc);
        tc = new TableColumn(1);
        tc.setHeaderValue(Column.STATUS.getHeader());
        tc.setCellRenderer(new ActorTableCellRenderer(Column.STATUS));
        tc.setHeaderRenderer(headerRenderer);
        tc.setMinWidth(30);
        tc.setMaxWidth(30);
        addColumn(tc);
        // CHECKSTYLE:ON
    }

    /**
     * Creates default column mapping.
     * @return default column mapping.
     */
    public static HashMap<Integer, Column> createDefaultColumns()
    {
        int i = 0;
        HashMap<Integer, ActorTableColumnModel.Column> names =
                new HashMap<Integer, ActorTableColumnModel.Column>();
        names.put(i++, ActorTableColumnModel.Column.Name);
        names.put(i++, ActorTableColumnModel.Column.STATUS);
        return names;
    }
}
