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

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.items.ItemTable;
import pl.org.minions.stigma.client.ui.swing.game.renderers.ButtonTableHeaderRenderer;
import pl.org.minions.stigma.client.ui.swing.game.renderers.ItemTableCellRenderer;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Column model for {@link ItemTable}.
 */
public class ItemTableColumnModel extends DefaultTableColumnModel
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ID_HEADER = "Id";
    @Translated
    private static String NAME_HEADER = "Name";
    @Translated
    private static String WEIGHT_HEADER = "Weight";
    @Translated
    private static String VALUE_HEADER = "Value";
    @Translated
    private static String TYPE_HEADER = "T";
    @Translated
    private static String ICON_HEADER = "Icon";
    @Translated
    private static String RECENCY_HEADER = "*";

    private static final ImageIcon WEIGHT_ICON =
            Resourcer.loadIcon(VisualizationGlobals.WEIGHT_PATH);
    private static final String STAR_ICON_PATH = "img/client/icons/Star.png";
    private static final Icon STAR_ICON = Resourcer.loadIcon(STAR_ICON_PATH);

    /**
     * All supported columns enum.
     */
    public enum Column
    {
        Id
        {
            @Override
            public String getHeaderLabel()
            {
                return ID_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {
                    /** {@inheritDoc} */
                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        return o1.getItem().getId() - o2.getItem().getId();
                    }
                };
            }
        },
        Name
        {
            @Override
            public String getHeaderLabel()
            {
                return NAME_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {

                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        if (o1.getItem().isComplete()
                            && o2.getItem().isComplete())
                            return Collator.getInstance().compare(o1.getItem()
                                                                    .getName(),
                                                                  o2.getItem()
                                                                    .getName());
                        else
                            return 0;
                    }
                };
            }
        },
        Weight
        {
            @Override
            public String getHeaderLabel()
            {
                return WEIGHT_HEADER;
            }

            @Override
            public Icon getHeaderIcon()
            {
                return WEIGHT_ICON;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {

                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        return o1.getItem().getWeight()
                            - o2.getItem().getWeight();
                    }
                };
            }
        },
        Type
        {
            @Override
            public String getHeaderLabel()
            {
                return TYPE_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {
                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        return o1.getItem()
                                 .getKind()
                                 .compareTo(o2.getItem().getKind());
                    }
                };
            }
        },
        Icon
        {
            @Override
            public String getHeaderLabel()
            {
                return ICON_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return null;
            }
        },
        Value
        {
            @Override
            public String getHeaderLabel()
            {
                return VALUE_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {
                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        return o1.getItem().getValue()
                            - o2.getItem().getValue();
                    }
                };
            }
        },
        Recency
        {
            /** {@inheritDoc} */
            @Override
            public String getHeaderLabel()
            {
                return RECENCY_HEADER;
            }

            /** {@inheritDoc} */
            @Override
            public javax.swing.Icon getHeaderIcon()
            {
                return STAR_ICON;
            }

            /** {@inheritDoc} */
            @Override
            public Comparator<ItemTableEntry> getDefaultSortOrder()
            {
                return new Comparator<ItemTableEntry>()
                {
                    @Override
                    public int compare(ItemTableEntry o1, ItemTableEntry o2)
                    {
                        return o1.compareTo(o2);
                    }
                };
            }
        };

        /**
         * Returns translated column name.
         * @return column name.
         */
        public abstract String getHeaderLabel();

        /**
         * Returns the optional icon for this column.
         * @return column icon.
         */
        public Icon getHeaderIcon()
        {
            return null;
        }

        /**
         * Provides the default ascending ordering
         * comparator for the column.
         * @return comparator for the column
         */
        public abstract Comparator<ItemTableEntry> getDefaultSortOrder();
    }

    private class ItemTableColumn extends TableColumn
    {
        private static final long serialVersionUID = 1L;

        ItemTableColumn(int modelIndex, Column column)
        {
            super(modelIndex);
            setIdentifier(column);
            if (column.getHeaderIcon() != null)
                setHeaderValue(column.getHeaderIcon());
            else
                setHeaderValue(column.getHeaderLabel());

            setCellRenderer(new ItemTableCellRenderer(column));
            setHeaderRenderer(itemTableHeaderRenderer);
        }
    }

    private final TableCellRenderer itemTableHeaderRenderer =
            new ButtonTableHeaderRenderer();

    /**
     * Constructs a model without {@link Column#Recency
     * recency column} .
     */
    public ItemTableColumnModel()
    {
        this(false);
    }

    /**
     * Constructs a model with optional recency column.
     * @param useRecency
     *            should the recency column be added
     */
    public ItemTableColumnModel(boolean useRecency)
    {
        // CHECKSTYLE:OFF
        int col = 0;
        TableColumn tc;
        if (Log.isTraceEnabled())
        {
            tc = new ItemTableColumn(col++, Column.Id);
            tc.setMinWidth(20);
            tc.setMaxWidth(20);
            addColumn(tc);
            tc = new ItemTableColumn(col++, Column.Type);
            tc.setMinWidth(30);
            tc.setMaxWidth(30);
            addColumn(tc);
        }
        if (useRecency)
        {
            tc = new ItemTableColumn(col++, Column.Recency);
            tc.setMaxWidth(12);
            addColumn(tc);
        }
        tc = new ItemTableColumn(col++, Column.Icon);
        tc.setMinWidth(34);
        tc.setMaxWidth(34);
        addColumn(tc);
        tc = new ItemTableColumn(col++, Column.Name);
        tc.setPreferredWidth(200);
        addColumn(tc);
        tc = new ItemTableColumn(col++, Column.Weight);
        addColumn(tc);
        tc = new ItemTableColumn(col++, Column.Value);
        tc.setMinWidth(62);
        addColumn(tc);
        // CHECKSTYLE:ON
    }

    /**
     * Creates default column mapping.
     * @return default column mapping.
     */
    public Map<Integer, Column> getColumnsMapping()
    {
        HashMap<Integer, ItemTableColumnModel.Column> names =
                new HashMap<Integer, ItemTableColumnModel.Column>();

        for (TableColumn column : Collections.list(getColumns()))
        {
            names.put(column.getModelIndex(), (Column) column.getIdentifier());
        }

        return names;
    }
}
