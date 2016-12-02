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

import java.util.List;

import pl.org.minions.stigma.game.item.Armor;
import pl.org.minions.stigma.game.item.type.ArmorType;

/**
 * Stub for {@link ArmorType}.
 */
public class ArmorTypeStub extends ArmorType
{
    private Armor armor;
    private List<Short> dynamicModifierList;

    /**
     * Constructor.
     * @param id
     *            id of stubbed object
     */
    public ArmorTypeStub(short id)
    {
        super(id,
              (short) 0,
              0,
              null,
              null,
              null,
              null,
              (byte) -1,
              (byte) -1,
              (byte) -1,
              (byte) -1,
              null,
              null,
              null,
              null,
              null);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "STUB: " + super.toString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStub()
    {
        return true;
    }

    /**
     * Assigns item connected with this type stub.
     * @param armor
     *            item to assign
     * @param dynamicModifierList
     *            list to assign
     */
    public synchronized void assignItem(Armor armor,
                                        List<Short> dynamicModifierList)
    {
        this.armor = armor;
        this.dynamicModifierList = dynamicModifierList;
    }

    /**
     * Returns armor. REMEMBER - due to some thread race
     * this function may return {@code null}.
     * @return armor
     */
    public synchronized Armor getArmor()
    {
        return armor;
    }

    /**
     * Returns dynamicModifierList. REMEMBER - due to some
     * thread race this function may return {@code null}.
     * @return dynamicModifierList
     */
    public synchronized List<Short> getDynamicModifierList()
    {
        return dynamicModifierList;
    }

}
