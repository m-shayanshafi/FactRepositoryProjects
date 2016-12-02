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
package pl.org.minions.stigma.client.ui.swing.game.components;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.JToggleButton;
import javax.swing.JToolTip;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.requests.EquipRequest;
import pl.org.minions.stigma.client.requests.UnEquipRequest;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemEquippedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemTypeLoadedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemUnEquippedListener;
import pl.org.minions.stigma.client.ui.swing.game.EquipmentPanel;
import pl.org.minions.stigma.client.ui.swing.game.tooltips.ItemToolTip;
import pl.org.minions.stigma.client.ui.swing.handlers.ItemTransferHandler;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.LogicalSlotType;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.globals.GlobalTimer;

/**
 * Button representing physical slot (used on
 * {@link EquipmentPanel}).
 */
public class PhysicalSlotButton extends JToggleButton
{
    /**
     * Width and height of this square shaped button.
     */
    public static final int DIMENSION = 35;

    private static final long serialVersionUID = 1L;

    private PhysicalSlotType slot;
    private ImageProxyComponent image;

    /**
     * Constructor.
     * @param slot
     *            physical slot represented by this button
     */
    public PhysicalSlotButton(PhysicalSlotType slot)
    {
        super();
        this.slot = slot;

        image = new ImageProxyComponent(null);
        image.setBounds(new Rectangle(0, 0, DIMENSION, DIMENSION));
        setLayout(null);
        add(image);

        setName("PhysicalSlotButton_" + slot.name());
        setFocusable(false);

        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Item i =
                        Client.globalInstance()
                              .getPlayerActor()
                              .getEquipedItems()
                              .get(PhysicalSlotButton.this.slot);
                assert i != null;
                if (i.getType().isStub())
                    return;

                setSelected(false);
                setEnabled(false);
                Client.globalInstance()
                      .getPlayerController()
                      .playerRequest(new UnEquipRequest(PhysicalSlotButton.this.slot));
            }
        });

        setTransferHandler(new ItemTransferHandler(new ItemTransferHandler.DropAction()
                                                   {
                                                       @Override
                                                       public void itemDrop(Item item)
                                                       {
                                                           Client.globalInstance()
                                                                 .getPlayerController()
                                                                 .playerRequest(new EquipRequest(item.getId(),
                                                                                                 PhysicalSlotButton.this.slot));
                                                       }
                                                   },
                                                   new ItemTransferHandler.ImportCheck()
                                                   {
                                                       @Override
                                                       public boolean canImport(Item item)
                                                       {
                                                           return Client.globalInstance()
                                                                        .getPlayerActor()
                                                                        .canEquip(item)
                                                               && item.getType()
                                                                      .getEquipementSlot()
                                                                      .getAvailablePhysicalSlots()
                                                                      .contains(PhysicalSlotButton.this.slot);
                                                       }
                                                   }));

        UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addItemEquippedListener(new ItemEquippedListener()
        {
            @Override
            public void itemEquipped(ItemEquipped event, boolean playerActor)
            {
                if (playerActor)
                    load(Client.globalInstance().getPlayerActor());
            }
        });

        uiEventRegistry.addItemUnEquippedListener(new ItemUnEquippedListener()
        {
            @Override
            public void itemUnEquipped(ItemUnEquipped event, boolean playerActor)
            {
                if (playerActor)
                    load(Client.globalInstance().getPlayerActor());
            }
        });

        uiEventRegistry.addItemTypeLoadedListener(new ItemTypeLoadedListener()
        {
            @Override
            public void itemTypeLoaded(short id)
            {
                Actor a = Client.globalInstance().getPlayerActor();
                Item item =
                        a.getEquipedItems().get(PhysicalSlotButton.this.slot);
                if (item != null && item.getType().getId() == id)
                    load(a);
            }
        });

        uiEventRegistry.addItemDataChangedListener(new ItemDataChangedListener()
        {
            @Override
            public void itemDataChanged(int id)
            {
                Actor a = Client.globalInstance().getPlayerActor();
                Item item =
                        a.getEquipedItems().get(PhysicalSlotButton.this.slot);
                if (item != null && item.getId() == id)
                    load(a);
            }
        });
    }

    /**
     * Loads equipped item from given actor (should be
     * player actor).
     * @param actor
     *            actor to load item from
     */
    public void load(final Actor actor)
    {
        Item item = actor.getEquipedItems().get(slot);
        if (item == null)
        {
            image.setProxy(null);
            image.setDefaultImage(null);
            setToolTipText(null);
            setEnabled(false);
        }
        else
        {
            final int refreshRate = 500;
            if (item.getType().isStub())
            {
                image.setProxy(null);
                GlobalTimer.getTimer().schedule(new TimerTask()
                {

                    @Override
                    public void run()
                    {
                        load(actor);

                    }
                }, refreshRate);
            }
            else
                image.setProxy(ImageDB.globalInstance()
                                      .getItemIcon(item.getKind(),
                                                   item.getType()
                                                       .getInventoryIcon()));
            image.setDefaultImage(VisualizationGlobals.DEFAULT_ITEM_IMAGE);
            setToolTipText(slot.toString());
            setEnabled(true);
        }
    }

    /**
     * Highlights button if it could accept given item. If
     * equipping such item would block slot represented by
     * this button, different highlight is used.
     * @param item
     *            item to determine highlight for
     */
    public void highlight(Item item)
    {
        if (!item.isComplete())
            return;
        // TODO: different highlight for blocked...

        LogicalSlotType logicSlot = item.getType().getEquipementSlot();
        boolean selected = logicSlot.getAvailablePhysicalSlots().contains(slot);
        if (selected != isSelected())
            setSelected(selected);
    }

    /**
     * Removes highlight from button.
     */
    public void unhighlight()
    {
        if (isSelected())
            setSelected(false);
    }

    /** {@inheritDoc} */
    @Override
    public JToolTip createToolTip()
    {
        Item item =
                Client.globalInstance()
                      .getPlayerActor()
                      .getEquipedItems()
                      .get(slot);
        if (item == null)
            return super.createToolTip();

        JToolTip tip = new ItemToolTip(item);
        tip.setComponent(this);
        return tip;
    }

}
