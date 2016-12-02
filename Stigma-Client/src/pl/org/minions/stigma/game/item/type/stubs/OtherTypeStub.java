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
package pl.org.minions.stigma.game.item.type.stubs;

import pl.org.minions.stigma.game.item.OtherItem;
import pl.org.minions.stigma.game.item.type.OtherType;

/**
 * Stub for {@link OtherType}.
 */
public class OtherTypeStub extends OtherType
{
    private OtherItem other;

    /**
     * Constructor.
     * @param id
     *            id of stubbed object
     */
    public OtherTypeStub(short id)
    {
        super(id, (short) 0, 0, null, null, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStub()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "STUB: " + super.toString();
    }

    /**
     * Assigns item connected with this type stub.
     * @param other
     *            item to assign
     */
    public synchronized void assignItem(OtherItem other)
    {
        this.other = other;
    }

    /**
     * Returns other. REMEMBER - due to some thread race
     * this function may return {@code null}
     * @return other
     */
    public synchronized OtherItem getOtherItem()
    {
        return other;
    }

}
