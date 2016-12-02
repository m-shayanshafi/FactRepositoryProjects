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
package pl.org.minions.stigma.chat.client;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.ChatType;

/**
 * Class representing chat target.
 */
public abstract class ChatTarget
{
    private boolean silent;
    private int idx;

    /**
     * Constructor. Package-wide, because it is utility
     * class which should be used only by
     * {@link ChatProcessor}.
     * @param idx
     *            index of this target in
     *            {@link ChatProcessor}.
     */
    ChatTarget(int idx)
    {
        this.idx = idx;
    }

    /**
     * Builds chat message containing given string and
     * addressed according to target represented by this
     * class.
     * @param msg
     *            string to be put in message
     * @return new chat message
     */
    public abstract Chat build(String msg);

    /**
     * Returns label representing given target.
     * @return label representing given target.
     */
    public abstract String getLabel();

    /**
     * Returns chat type which should be sent to target.
     * Used for intermediate filtering.
     * @return chat type which should be sent to target.
     */
    public abstract ChatType getType();

    /** {@inheritDoc} */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Check whether this class matches incoming chat's
     * sender. Used for filtering. Should be called only for
     * class which already matched chat type.
     * @param msg
     *            chat message
     * @return {@code true} when this class matches given
     *         message sender
     */
    public abstract boolean match(Chat msg);

    /**
     * Sets new value of silent.
     * @param silent
     *            the silent to set
     */
    public final void setSilent(boolean silent)
    {
        this.silent = silent;
    }

    /**
     * Returns silent. If {@code true} no messages will be
     * received from given target.
     * @return silent
     */
    public final boolean isSilent()
    {
        return silent;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just to remove checkstyle warnings 
        return super.hashCode();
    }

    /**
     * Returns index of this target in {@link ChatProcessor}
     * .
     * @return index of this target
     */
    public final int getIdx()
    {
        return idx;
    }

    /**
     * Returns {@code true} when this target is 'self
     * whisper'.
     * @return {@code true} when this target points to
     *         player himself
     */
    public boolean isEcho()
    {
        return false;
    }
}
