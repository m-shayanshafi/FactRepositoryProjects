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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.requests.DropRequest;
import pl.org.minions.stigma.client.requests.PickUpAllRequest;
import pl.org.minions.stigma.client.requests.PickUpRequest;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorChangedMapListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorWalkListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.ExamineItemAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.items.PickUpItemAction;
import pl.org.minions.stigma.client.ui.swing.game.components.items.ItemTable;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableModel;
import pl.org.minions.stigma.client.ui.swing.handlers.ItemTransferHandler;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.i18n.StandardText;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel showing items that are laying on ground.
 */
public class ItemsOnGroundPanel extends JPanel
{
    @Translated
    private static String NAME = "Ground";

    @Translated
    private static String WAITING_FOR_ITEMS_LABEL = "Waiting for items...";

    @Translated
    private static String TAKE_ALL_LABEL = "Take all";

    private static final int PANEL_HEIGHT = 180;
    private static final int PANEL_WIDTH = 240;

    private static final long serialVersionUID = 1L;

    private boolean isWaitingForItemData;

    private JButton takeAllButton;
    private JButton closeButton;

    private JScrollPane tableScrollPane;

    private ItemTable itemsTable;
    private ItemTableColumnModel dcm;
    private ItemTableModel dtm;

    private JPanel bottomPanel;

    private JPanel itemsWaitPanel;

    private JLabel itemsWaitLabel;

    /**
     * This is the default constructor.
     */
    public ItemsOnGroundPanel()
    {
        super();

        UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addActorAddedListener(new ActorAddedListener()
        {
            @Override
            public void actorAdded(ActorAdded event, boolean playerActor)
            {
                if (!playerActor)
                    return;
                Actor a = event.getActor();
                List<Item> items =
                        Client.globalInstance()
                              .getWorld()
                              .getMap(a.getMapId(), a.getMapInstanceNo())
                              .getItems(a.getPosition());
                if (items != null)
                {
                    addItems(items);
                }
                else
                    clearItems();
            }
        });

        uiEventRegistry.addActorChangedMapListener(new ActorChangedMapListener()
        {
            @Override
            public void actorChangedMap(ActorChangedMap event,
                                        boolean playerActor)
            {
                if (!playerActor)
                    return;
                List<Item> items =
                        Client.globalInstance()
                              .getWorld()
                              .getMap(event.getMapId(), event.getInstanceNo())
                              .getItems(event.getPosition());
                if (items != null)
                {
                    addItems(items);
                }
                else
                    clearItems();
            }
        });

        uiEventRegistry.addActorWalkListener(new ActorWalkListener()
        {
            @Override
            public void actorWalked(ActorWalk event,
                                    Move command,
                                    boolean playerActor)
            {
                if (playerActor)
                {
                    Position newPos = event.getNewPosition();
                    short mapId =
                            Client.globalInstance().getPlayerActor().getMapId();
                    short mapInstanceId =
                            Client.globalInstance()
                                  .getPlayerActor()
                                  .getMapInstanceNo();
                    List<Item> items =
                            Client.globalInstance()
                                  .getWorld()
                                  .getMap(mapId, mapInstanceId)
                                  .getItems(newPos);
                    if (items != null)
                    {
                        addItems(items);
                    }
                    else
                        clearItems();
                }
            }
        });

        uiEventRegistry.addItemTypeLoadedListener(new ItemTypeLoadedListener()
        {
            @Override
            public void itemTypeLoaded(short id)
            {
                if (isWaitingForItemData)
                {
                    setWaiting(areItemsComplete());
                }
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

        uiEventRegistry.addItemDroppedListener(new ItemDroppedListener()
        {
            @Override
            public void itemDropped(ItemDropped event, boolean playerActor)
            {
                if (!playerActor)
                    return;

                short mapId =
                        Client.globalInstance().getPlayerActor().getMapId();
                short mapInstanceId =
                        Client.globalInstance()
                              .getPlayerActor()
                              .getMapInstanceNo();
                Position pos =
                        Client.globalInstance().getPlayerActor().getPosition();
                List<Item> items =
                        Client.globalInstance()
                              .getWorld()
                              .getMap(mapId, mapInstanceId)
                              .getItems(pos);
                if (items != null)
                    addItems(items);
                else
                    clearItems();
            }
        });

        uiEventRegistry.addItemPickedUpListener(new ItemPickedUpListener()
        {
            @Override
            public void itemPickedUp(ItemPickedUp event, boolean playerActor)
            {
                if (playerActor)
                    dtm.removeById(event.getItemId());

                short mapId =
                        Client.globalInstance().getPlayerActor().getMapId();
                short mapInstanceId =
                        Client.globalInstance()
                              .getPlayerActor()
                              .getMapInstanceNo();
                Position pos =
                        Client.globalInstance().getPlayerActor().getPosition();
                List<Item> items =
                        Client.globalInstance()
                              .getWorld()
                              .getMap(mapId, mapInstanceId)
                              .getItems(pos);
                if (items != null)
                    addItems(items);
                else
                    clearItems();
            }
        });

        initialize();
        setWaiting(false);
    }

    private void initialize()
    {
        //CHECKSTYLE:OFF
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setHgap(0);
        gridLayout.setVgap(5);
        this.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(2);
        this.setLayout(borderLayout);

        this.itemsWaitPanel = new JPanel();
        itemsWaitPanel.setLayout(new FlowLayout());

        itemsWaitLabel = new JLabel(WAITING_FOR_ITEMS_LABEL);

        itemsWaitPanel.add(itemsWaitLabel);

        this.takeAllButton = new JButton();
        takeAllButton.setText(TAKE_ALL_LABEL);
        takeAllButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pickUpAllItems();
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

        this.dcm = new ItemTableColumnModel();
        this.dtm = new ItemTableModel(dcm.getColumnsMapping());

        this.itemsTable = new ItemTable(dtm, dcm);
        itemsTable.setPopupActions(new PickUpItemAction(),
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
                dropItem(item);
            }
        }));

        itemsTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getClickCount() < 2)
                    return;
                JTable table = (JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                if (row < 0)
                    return;
                Item item = dtm.getItem(row);
                if (item == null)
                    return;
                pickUpItem(item);
            }
        });

        this.tableScrollPane = new JScrollPane(itemsTable);
        tableScrollPane.setHorizontalScrollBar(null);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(gridLayout);
        bottomPanel.add(takeAllButton);
        bottomPanel.add(closeButton, closeButton.getName());
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        //CHECKSTYLE:ON
    }

    private void close()
    {
        assert this.getRootPane() != null;
        this.getRootPane().getParent().setVisible(false);
    }

    /**
     * Sets items on panel (clears its previous state).
     * @param items
     *            collection of items to add
     */
    public void addItems(Collection<Item> items)
    {
        this.dtm.clear();
        dtm.addItems(items);
        itemsTable.setModel(dtm);
    }

    /**
     * Clears all items on panel.
     */
    public void clearItems()
    {
        this.dtm.clear();
    }

    private void dropItem(Item item)
    {
        Client.globalInstance()
              .getPlayerController()
              .playerRequest(new DropRequest(item.getId()));
    }

    private void pickUpItem(Item item)
    {
        Client.globalInstance()
              .getPlayerController()
              .playerRequest(new PickUpRequest(item.getId()));
    }

    private void pickUpAllItems()
    {
        Client.globalInstance()
              .getPlayerController()
              .playerRequest(new PickUpAllRequest());
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

    private boolean areItemsComplete()
    {
        for (int i = 0; i < dtm.getRowCount(); i++)
        {
            if (!dtm.getItem(i).isComplete())
                return false;
        }

        return true;
    }

    private void setWaiting(boolean isWaiting)
    {
        if (isWaiting)
        {
            this.remove(tableScrollPane);
            this.add(itemsWaitPanel, BorderLayout.CENTER);
        }
        else
        {
            this.remove(itemsWaitPanel);
            this.add(tableScrollPane, BorderLayout.CENTER);
        }
        isWaitingForItemData = isWaiting;
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
