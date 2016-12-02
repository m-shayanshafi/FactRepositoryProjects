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
package pl.org.minions.stigma.game.actor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Description of resistance.
 */
@XmlRootElement(name = "res")
@XmlType(propOrder = {})
public class Resistance
{
    private short threshold;
    private byte relative;

    /**
     * Returns deep copy of this class.
     * @return deep copy of this class.
     */
    public Resistance deepCopy()
    {
        Resistance copy = new Resistance();
        copy.relative = this.relative;
        copy.threshold = this.threshold;
        return copy;
    }

    /**
     * Gets relative resistance.
     * @return the relative resistance
     */
    @XmlElement
    public byte getRelative()
    {
        return relative;
    }

    /**
     * Gets threshold resistance.
     * @return the threshold resistance
     */
    @XmlElement
    public short getThreshold()
    {
        return threshold;
    }

    /**
     * Sets relative resistance.
     * @param i
     *            the relative resistance to set
     */
    public void setRelative(byte i)
    {
        // CHECKSTYLE:OFF
        if (i > 100)
            this.relative = 100;
        else
            this.relative = i;
        // CHECKSTYLE:ON
    }

    /**
     * Sets threshold resistance.
     * @param threshold
     *            the threshold resistance to set
     */
    public void setThreshold(short threshold)
    {
        this.threshold = threshold;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append(relative);
        buf.append("%, ");
        buf.append(threshold);
        buf.append("]\0");
        return buf.toString();
    }
}
