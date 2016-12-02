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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import pl.org.minions.stigma.game.actor.Actor;

/**
 * Base class for swing {@link Action actions} that are used
 * to interact with actors.
 */
public abstract class ActorAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    private Actor actor;

    /**
     * Creates a new action with given name,
     * description, and icon.
     * @param name
     *            action name
     * @param description
     *            action short description
     * @param icon
     *            action icon
     */
    ActorAction(String name, String description, Icon icon)
    {
        super(name, icon);
        setActor(null);
        putValue(SHORT_DESCRIPTION, description);
    }

    /**
     * Creates a new action with given name and
     * description.
     * @param name
     *            action name
     * @param description
     *            action short description
     */
    ActorAction(String name, String description)
    {
        super(name);
        setActor(null);
        putValue(SHORT_DESCRIPTION, description);
    }

    /**
     * Sets actor for which this action should be performed.
     * @param actor
     *            new actor
     */
    public final void setActor(Actor actor)
    {
        this.actor = actor;
        setEnabled(shouldBeEnabled());
    }

    /**
     * Returns actor for which this action should be
     * performed.
     * @return actor assigned for this action
     */
    protected final Actor getActor()
    {
        return actor;
    }

    /**
     * Returns {@code true} when this action can be called
     * for NPC. Default: {@code true}, override to change
     * action behavior.
     * @return {@code true} when this action can be called
     *         for NPC
     */
    protected boolean supportsNpcs()
    {
        return true;
    }

    private boolean shouldBeEnabled()
    {
        return actor != null && (actor.isPC() || supportsNpcs());
    }

}
