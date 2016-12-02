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
package pl.org.minions.stigma.databases.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import pl.org.minions.stigma.databases.parsers.Parsable;

/**
 * Class representing interior of a XML data file containing
 * few objects.
 * @param <T>
 *            is a type of objects which is contained in the
 *            simple database
 */
@XmlTransient
public abstract class SimpleDataWrapper<T extends XmlDbElem> implements
                                                             Parsable
{

    /**
     * Default constructor used by JAXB.
     */
    protected SimpleDataWrapper()
    {
    }

    /**
     * Returns types. Should be re-implemented and
     * re-annotated in subclasses.
     * @return types
     */
    @XmlTransient
    public abstract List<T> getTypes();

    /**
     * Sets new value of types. Should be re-implemented and
     * re-annotated in subclasses.
     * @param types
     *            the types to set
     */
    @XmlTransient
    public abstract void setTypes(List<T> types);

}
