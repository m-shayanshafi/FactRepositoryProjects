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
package pl.org.minions.stigma.game.item;

/**
 * Class representing currency in game world. Currently
 * currency looks like that: {@code 1 gold = 100 silver =
 * 10000 copper}.
 */
public final class CurrencyRepresentation
{
    private static final int SILVER_DIVIDER = 100;
    private static final int GOLD_DIVIDER = 100;

    private static final int GOLD_REAL_DIVIDER = SILVER_DIVIDER * GOLD_DIVIDER;

    private int copper;
    private int silver;
    private int gold;

    /**
     * Constructor. Creates currency representation for
     * given amount of money.
     * @param money
     *            money to be represented in currency
     */
    public CurrencyRepresentation(int money)
    {
        this.gold = money / GOLD_REAL_DIVIDER;
        money = money % GOLD_REAL_DIVIDER;

        this.silver = money / SILVER_DIVIDER;
        this.copper = money % SILVER_DIVIDER;
    }

    /**
     * Returns money amount represented by this object.
     * @return money amount represented by this object.
     */
    public int getMoney()
    {
        return copper + SILVER_DIVIDER * silver + GOLD_REAL_DIVIDER * gold;
    }

    /**
     * Returns copper part of currency.
     * @return copper part of currency.
     */
    public int getCopper()
    {
        return copper;
    }

    /**
     * Returns silver part of currency.
     * @return silver part of currency.
     */
    public int getSilver()
    {
        return silver;
    }

    /**
     * Returns gold part of currency.
     * @return gold part of currency.
     */
    public int getGold()
    {
        return gold;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (!(o instanceof CurrencyRepresentation))
            return false;

        CurrencyRepresentation other = (CurrencyRepresentation) o;

        return getMoney() == other.getMoney();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return getMoney();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return gold + " gold " + silver + " silver " + copper + " copper";
    }
}
