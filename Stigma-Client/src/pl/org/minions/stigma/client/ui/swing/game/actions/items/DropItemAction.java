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
package pl.org.minions.stigma.client.ui.swing.game.actions.items;

import java.awt.event.ActionEvent;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.requests.DropRequest;
import pl.org.minions.utils.i18n.Translated;

/**
 * ItemAction implementation for dropping items.
 */
public class DropItemAction extends ItemAction
{
    private static final long serialVersionUID = 1L;
    @Translated
    private static String NAME = "Drop";
    @Translated
    private static String DESCRIPTION = "Drop the item to ground";

    /**
     * Creates a new instance of DropItemAction.
     */
    public DropItemAction()
    {
        super(NAME, DESCRIPTION);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Client.globalInstance()
              .getPlayerController()
              .playerRequest(new DropRequest(getItem().getId()));
    }
}
