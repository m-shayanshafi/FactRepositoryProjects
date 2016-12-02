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
package pl.org.minions.stigma.client.ui.swing.game.actions.actors;

import java.awt.event.ActionEvent;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.requests.MoveToRequest;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.StandardMetrics;
import pl.org.minions.utils.i18n.Translated;

/**
 * ActorAction implementation for moving into actor's
 * proximity.
 */
public class MoveToAction extends ActorAction
{
    private static final long serialVersionUID = 1L;
    @Translated
    private static String NAME = "Goto";
    @Translated
    private static String DESCRIPTION = "Move to position of actor";

    private static Distance ONE_STEP_AWAY =
            StandardMetrics.STEPS.createDistance(1);

    /**
     * Constructor.
     */
    public MoveToAction()
    {
        super(NAME, DESCRIPTION);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Client.globalInstance()
              .getPlayerController()
              .playerRequest(new MoveToRequest(getActor().getPosition(),
                                               ONE_STEP_AWAY));
    }

}
