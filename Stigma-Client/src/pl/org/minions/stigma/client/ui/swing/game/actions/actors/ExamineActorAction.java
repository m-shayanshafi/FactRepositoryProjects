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
import pl.org.minions.stigma.client.ui.ClientSwingUI;
import pl.org.minions.stigma.client.ui.swing.game.components.actors.DynamicActorStatisticPanel;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.i18n.Translated;

/**
 * ActorAction implementation used to display informations
 * about an {@link Actor} in a floating window.
 */
public class ExamineActorAction extends ActorAction
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String NAME = "Examine";
    @Translated
    private static String DESCRIPTION = "Examine the actor";

    /**
     * Creates a new instance of ExamineActorAction.
     */
    public ExamineActorAction()
    {
        super(NAME, DESCRIPTION);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        ClientSwingUI ui = (ClientSwingUI) Client.globalInstance().getUi();
        ui.showCustomFrame("Examine: " + getActor().getName(),
                           new DynamicActorStatisticPanel(getActor()),
                           false);
    }

}
