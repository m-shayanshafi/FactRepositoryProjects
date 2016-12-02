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
package pl.org.minions.stigma.client.ui.swing.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.requests.PickUpRequest;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemEquippedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemUnEquippedListener;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.DropItemAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.EquipItemAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.ExamineItemAction;
import pl.org.minions.stigma.client.ui.swing.game.components.CurrencyPanel;
import pl.org.minions.stigma.client.ui.swing.game.components.ValueIconLabel;
import pl.org.minions.stigma.client.ui.swing.game.components.items.ItemTable;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemKindRowFilter;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableModel;
import pl.org.minions.stigma.client.ui.swing.handlers.ItemTransferHandler;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.utils.i18n.StandardText;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing inventory panel.
 */
public class InventoryPanel extends JPanel
{

    @Translated
    private static String CARRIED_WEIGHT_TOOLTIP = "Carried weight.";
    @Translated
    private static String NAME = "Inventory";
    @Translated
    private static String ARMOR_FILTER_NAME = "Armor";
    @Translated
    private static String WEAPON_FILTER_NAME = "Weapon";
    @Translated
    private static String OTHER_FILTER_NAME = "Other";
    @Translated
    private static String ALL_FILTER_NAME = "All";
    @Translated
    private static String FILLTER_INFO = "Filter:";

    private static final int PANEL_HEIGHT = 250;
    private static final int PANEL_WIDTH = 240;

    private static final long serialVersionUID = 1L;

    private JScrollPane tableScrollPane;
    private ItemTable itemsTable;
    private JButton closeButton;

    private JComboBox filterBox;
    private JLabel filterLabel;

    private JPanel topPanel;
    private JPanel bottomPanel;

    private ItemTableColumnModel dcm;
    private ItemTableModel dtm;
    private ValueIconLabel weightLabel;
    private CurrencyPanel goldPanel;

    /**
     * This is the default constructor.
     */
    public InventoryPanel()
    {
        super();

        UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addItemDroppedListener(new ItemDroppedListener()
        {
            @Override
            public void itemDropped(ItemDropped event, boolean playerActor)
            {
                if (!playerActor)
                    return;
                dtm.removeById(event.getItemId());
                setWeight(Client.globalInstance()
                                .getPlayerActor()
                                .getCurrentLoad(), Client.globalInstance()
                                                         .getPlayerActor()
                                                         .getMaxLoad());
                itemsTable.repaint();
            }
        });

        uiEventRegistry.addItemPickedUpListener(new ItemPickedUpListener()
        {
            @Override
            public void itemPickedUp(ItemPickedUp event, boolean playerActor)
            {
                if (!playerActor)
                    return;
                dtm.addItem(Client.globalInstance()
                                  .getWorld()
                                  .getItem(event.getItemId()));
                setWeight(Client.globalInstance()
                                .getPlayerActor()
                                .getCurrentLoad(), Client.globalInstance()
                                                         .getPlayerActor()
                                                         .getMaxLoad());
                itemsTable.repaint();
            }
        });

        uiEventRegistry.addItemEquippedListener(new ItemEquippedListener()
        {
            @Override
            public void itemEquipped(ItemEquipped event, boolean playerActor)
            {
                if (!playerActor)
                    return;
                dtm.removeById(event.getItemId());
                itemsTable.repaint();
            }
        });

        uiEventRegistry.addItemUnEquippedListener(new ItemUnEquippedListener()
        {
            @Override
            public void itemUnEquipped(ItemUnEquipped event, boolean playerActor)
            {
                if (!playerActor)
                    return;
                dtm.addItem(event.getUnequippedItem());
                itemsTable.repaint();
            }
        });

        uiEventRegistry.addItemTypeLoadedListener(new ItemTypeLoadedListener()
        {
            @Override
            public void itemTypeLoaded(short id)
            {
                itemsTable.repaint();
            }
        });

        uiEventRegistry.addItemDataChangedListener(new ItemDataChangedListener()
        {
            @Override
            public void itemDataChanged(int id)
            {
                if (dtm.contains(id))
                    itemsTable.repaint();
            }
        });

        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize()
    {
        //CHECKSTYLE:OFF
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.anchor = GridBagConstraints.EAST;
        gridBagConstraints2.weightx = 0.0;
        gridBagConstraints2.gridy = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.ipadx = 0;
        gridBagConstraints1.weightx = 0.2;
        gridBagConstraints1.anchor = GridBagConstraints.EAST;
        gridBagConstraints1.fill = GridBagConstraints.NONE;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridx = 0;
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(1);
        gridLayout.setHgap(4);
        gridLayout.setVgap(0);
        gridLayout.setColumns(3);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLayout(new BorderLayout());

        this.filterBox = new JComboBox();
        filterBox.addItem(ALL_FILTER_NAME);
        filterBox.addItem(ARMOR_FILTER_NAME);
        filterBox.addItem(WEAPON_FILTER_NAME);
        filterBox.addItem(OTHER_FILTER_NAME);
        filterBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selected = filterBox.getSelectedItem().toString();
                if (selected.equals(ARMOR_FILTER_NAME))
                {
                    itemsTable.setItemKindFilter(ItemKindRowFilter.ARMOR_FILTER);
                }
                else if (selected.equals(WEAPON_FILTER_NAME))
                {
                    itemsTable.setItemKindFilter(ItemKindRowFilter.WEAPON_FILTER);
                }
                else if (selected.equals(ALL_FILTER_NAME))
                {
                    itemsTable.setItemKindFilter(null);
                }
                else if (selected.equals(OTHER_FILTER_NAME))
                {
                    itemsTable.setItemKindFilter(ItemKindRowFilter.OTHER_FILTER);
                }
                else
                    Log.logger.error("Unknown filter: " + selected);

                itemsTable.repaint();
            }
        });

        this.closeButton = new JButton();
        closeButton.setText(StandardText.CLOSE.get());
        closeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        this.dcm = new ItemTableColumnModel(true);
        this.dtm = new ItemTableModel(dcm.getColumnsMapping());

        this.itemsTable = new ItemTable(dtm, dcm);
        itemsTable.setPopupActions(new EquipItemAction(),
                                   new DropItemAction(),
                                   null,
                                   new ExamineItemAction());
        itemsTable.setDragEnabled(true);
        itemsTable.setDropMode(DropMode.ON_OR_INSERT_ROWS);
        itemsTable.setFillsViewportHeight(true);
        itemsTable.setTransferHandler(new ItemTransferHandler(new ItemTransferHandler.DropAction()
        {
            @Override
            public void itemDrop(Item item)
            {
                Client.globalInstance()
                      .getPlayerController()
                      .playerRequest(new PickUpRequest(item.getId()));
            }
        }));

        filterLabel = new JLabel();
        filterLabel.setText(FILLTER_INFO);

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(filterLabel);
        topPanel.add(filterBox);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.add(getGoldPanel(), gridBagConstraints);
        bottomPanel.add(getWeightLabel(), gridBagConstraints1);
        bottomPanel.add(closeButton, gridBagConstraints2);
        this.tableScrollPane = new JScrollPane(itemsTable);
        tableScrollPane.setHorizontalScrollBar(null);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        //CHECKSTYLE:ON
    }

    /**
     * Sets items on panel (clears its previous state).
     */
    public void loadItems()
    {
        dtm.clear();
        Actor actor = Client.globalInstance().getPlayerActor();
        dtm.addItems(actor.getInventory());
        setWeight(actor.getCurrentLoad(), actor.getMaxLoad());
        goldPanel.setValue(actor.getMoney());
        itemsTable.setModel(dtm);
    }

    private void close()
    {
        assert this.getRootPane() != null;
        this.getRootPane().getParent().setVisible(false);
    }

    private void setWeight(short value, short max)
    {
        getWeightLabel().setText(value + "/" + max);
    }

    /**
     * Returns default panel name.
     * @return panel name
     */
    public static String getDefaultName()
    {
        return NAME;
    }

    /**
     * Returns default panel height.
     * @return panel height
     */
    public static int getDefaultHeight()
    {
        return PANEL_HEIGHT;
    }

    /**
     * Returns default panel width.
     * @return panel width
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * This method initializes weightLabel
     * @return pl.org.minions.stigma.client.ui.swing.game.
     *         components.ValueIconLabel
     */
    private ValueIconLabel getWeightLabel()
    {
        if (weightLabel == null)
        {
            weightLabel = new ValueIconLabel();
            weightLabel.setIcon(VisualizationGlobals.WEIGHT_IMG);
            weightLabel.setToolTipText(CARRIED_WEIGHT_TOOLTIP);
        }
        return weightLabel;
    }

    /**
     * This method initializes goldPanel
     * @return pl.org.minions.stigma.client.ui.swing.game.
     *         components.CurrencyPanel
     */
    private CurrencyPanel getGoldPanel()
    {
        if (goldPanel == null)
        {
            goldPanel = new CurrencyPanel();
        }
        return goldPanel;
    }
}
