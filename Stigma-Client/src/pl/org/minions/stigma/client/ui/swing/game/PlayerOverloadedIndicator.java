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

import javax.swing.ImageIcon;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.utils.i18n.Translated;

/**
 * Used to indicate when player actor is overloaded.
 */
public class PlayerOverloadedIndicator extends IndicatorIcon
{
    private static final long serialVersionUID = 6400485915752636535L;

    @Translated
    private static String ENCUMBERED_TEXT = "Overloaded!";

    private static final String ICON_PATH = "img/client/icons/overloaded.png";
    private static final ImageIcon ICON_IMAGE = Resourcer.loadIcon(ICON_PATH);

    /**
     * Creates a new PlayerOverloadedIndicator.
     */
    public PlayerOverloadedIndicator()
    {
        super(ICON_IMAGE, ENCUMBERED_TEXT);

        final UiEventRegistry uiEvtReg =
                Client.globalInstance().uiEventRegistry();

        uiEvtReg.addItemPickedUpListener(new ItemPickedUpListener()
        {
            @Override
            public void itemPickedUp(ItemPickedUp event, boolean playerActor)
            {
                if (playerActor)
                    update();
            }
        });

        uiEvtReg.addItemDroppedListener(new ItemDroppedListener()
        {
            @Override
            public void itemDropped(ItemDropped event, boolean playerActor)
            {
                if (playerActor)
                    update();
            }
        });

        uiEvtReg.addActorAddedListener(new ActorAddedListener()
        {
            @Override
            public void actorAdded(ActorAdded event, boolean playerActor)
            {
                if (playerActor)
                    update();
            }
        });
    }

    private void update()
    {
        Actor playerActor = Client.globalInstance().getPlayerActor();
        setVisible(playerActor.isOverloaded());
    }
}
