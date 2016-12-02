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
package pl.org.minions.stigma.client.ui.swing.handlers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import pl.org.minions.stigma.client.ui.swing.game.models.ItemTableModel;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.utils.logger.Log;

/**
 * TransferHandler responsible for dropping and equipping
 * items.
 */
public class ItemTransferHandler extends TransferHandler
{
    private static final long serialVersionUID = 1L;

    /**
     * Interface used to perform different 'drop' actions.
     */
    public interface DropAction
    {
        /**
         * Action performed when item is 'dropped' into
         * component.
         * @param item
         *            dragged and dropped item
         */
        void itemDrop(Item item);
    }

    /**
     * Interface used to perform different 'can drop'
     * checks.
     */
    public interface ImportCheck
    {
        /**
         * Method called to check if item can be dropped at
         * current mouse location.
         * @param item
         *            dragged item
         * @return {@code true} when item can be dropped
         */
        boolean canImport(Item item);
    }

    private DropAction dropAction;
    private ImportCheck importCheck;

    /**
     * Constructor.
     * @param action
     *            action to perform on 'drop'
     * @param importCheck
     *            additional check for allowing dropping
     */
    public ItemTransferHandler(DropAction action, ImportCheck importCheck)
    {
        this.dropAction = action;
        this.importCheck = importCheck;
        assert dropAction != null;
    }

    /**
     * Constructor.
     * @param action
     *            action to perform on 'drop'
     */
    public ItemTransferHandler(DropAction action)
    {
        this(action, null);
    }

    /** {@inheritDoc} */
    @Override
    protected Transferable createTransferable(JComponent c)
    {
        assert c instanceof JTable;
        JTable table = (JTable) c;
        assert table.getModel() instanceof ItemTableModel;
        ItemTableModel model = (ItemTableModel) table.getModel();
        return new TransferableItem(model.getItem(table.getSelectedRows()[0]),
                                    this);
    }

    /** {@inheritDoc} */
    @Override
    public boolean canImport(TransferSupport support)
    {
        support.setShowDropLocation(false);
        if (!support.isDrop())
            return false;

        for (DataFlavor flv : support.getDataFlavors())
            if (flv.equals(TransferableItem.getItemDataFlavor()))
            {
                Item item;
                ItemTransferHandler source;
                try
                {
                    item =
                            (Item) support.getTransferable()
                                          .getTransferData(TransferableItem.getItemDataFlavor());
                }
                catch (UnsupportedFlavorException e)
                {
                    Log.logger.error("Unsupported data during drag.", e);
                    return false;
                }
                catch (IOException e)
                {
                    Log.logger.error("I/O error during drag", e);
                    return false;
                }
                assert item != null;

                try
                {
                    source =
                            (ItemTransferHandler) support.getTransferable()
                                                         .getTransferData(TransferableItem.getItemSourceFlavor());
                }
                catch (UnsupportedFlavorException e)
                {
                    source = null;
                }
                catch (IOException e)
                {
                    Log.logger.error("I/O error during drag (reading source)",
                                     e);
                    return false;
                }

                // prevent self-drop
                if (source == this)
                    return false;

                if (importCheck != null)
                    return importCheck.canImport(item);
                else
                    return true;
            }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean importData(TransferSupport support)
    {
        if (support.isDrop())
        {
            Item item;
            try
            {
                item =
                        (Item) support.getTransferable()
                                      .getTransferData(TransferableItem.getItemDataFlavor());
                assert item != null;
                dropAction.itemDrop(item);
            }
            catch (UnsupportedFlavorException e)
            {
                Log.logger.error("Dropping item error: unsupported data during importData.",
                                 e);
            }
            catch (IOException e)
            {
                Log.logger.error("Dropping item error: importData IO error.", e);
            }
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int getSourceActions(JComponent c)
    {
        return MOVE;
    }
}
