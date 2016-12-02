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

import pl.org.minions.stigma.game.item.Item;

/**
 * Class which represents item transfered through
 * drag'n'drop mechanism.
 */
public class TransferableItem implements Transferable
{
    private static final String CLASS_FLAVOR_PART = ";class=";
    private static final DataFlavor FLAVOR = createDataFlavor();
    private static final DataFlavor SOURCE_FLAVOR = createSourceFlavor();

    private Item item;
    private ItemTransferHandler source;

    /**
     * Constructor.
     * @param item
     *            item to transfer
     * @param source
     *            source of transfer
     */
    public TransferableItem(Item item, ItemTransferHandler source)
    {
        this.item = item;
        this.source = source;
    }

    private static DataFlavor createSourceFlavor()
    {
        try
        {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                + CLASS_FLAVOR_PART + ItemTransferHandler.class.getName());
        }
        catch (ClassNotFoundException e)
        {
            //This should not happen
            e.printStackTrace();
            return null;
        }
    }

    private static DataFlavor createDataFlavor()
    {
        try
        {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                + CLASS_FLAVOR_PART + Item.class.getName());
        }
        catch (ClassNotFoundException e)
        {
            //This should not happen
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns object which represents item data flavor.
     * @return object which represents item data flavor
     */
    public static DataFlavor getItemDataFlavor()
    {
        return FLAVOR;
    }

    /**
     * Returns object which represents item source flavor.
     * @return object which represents item source flavor
     */
    public static DataFlavor getItemSourceFlavor()
    {
        return SOURCE_FLAVOR;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor.equals(FLAVOR) || flavor.equals(SOURCE_FLAVOR);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]
        { FLAVOR };
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                                                    IOException
    {
        if (flavor.equals(FLAVOR))
        {
            return item;
        }
        else if (flavor.equals(SOURCE_FLAVOR))
        {
            return source;
        }
        else
            return null;
    }
}
