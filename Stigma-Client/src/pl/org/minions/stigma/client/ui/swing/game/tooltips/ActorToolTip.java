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
package pl.org.minions.stigma.client.ui.swing.game.tooltips;

import java.awt.Dimension;

import javax.swing.JToolTip;
import javax.swing.border.EmptyBorder;

import pl.org.minions.stigma.client.ui.swing.game.components.actors.ActorStatisticPanel;
import pl.org.minions.stigma.game.actor.Actor;

/**
 * Class representing actor (non-player's) tool tip. Embedes
 * {@link ActorStatisticPanel}.
 */
public class ActorToolTip extends JToolTip
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;
    private static final int INNER_MARGIN = 5;

    private static final long serialVersionUID = 1L;

    private ActorStatisticPanel actorPanel;

    /**
     * Default constructor.
     * @param actor
     *            actor to be drawn
     */
    public ActorToolTip(Actor actor)
    {
        assert actor != null;
        actorPanel = new ActorStatisticPanel(actor);
        actorPanel.setBorder(new EmptyBorder(INNER_MARGIN,
                                             INNER_MARGIN,
                                             INNER_MARGIN,
                                             INNER_MARGIN));
        this.add(actorPanel);
        Dimension d = actorPanel.getPreferredSize();
        d = new Dimension(Math.max(d.width, WIDTH), Math.max(d.height, HEIGHT));
        actorPanel.setSize(d);
        this.setPreferredSize(d);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation ignores the argument and does
     * nothing.
     */
    @Override
    public void setTipText(String tipText)
    {
        ;
    }
}
