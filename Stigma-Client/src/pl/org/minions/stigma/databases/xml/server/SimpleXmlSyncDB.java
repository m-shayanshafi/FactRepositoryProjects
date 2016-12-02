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
package pl.org.minions.stigma.databases.xml.server;

import java.net.URI;

import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.SimpleDataWrapper;
import pl.org.minions.stigma.databases.xml.SimpleXmlDB;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.utils.logger.Log;

/**
 * Abstract class which represents database which reads data
 * from file.
 * @param <Container>
 *            wrapper containing ObjectType elements
 * @param <ObjectType>
 *            Objects stored in database
 * @param <StorageType>
 *            object which is directly stored in XML
 *            database (is parsable and convertible to
 *            ObjectType)
 */
public abstract class SimpleXmlSyncDB<Container extends SimpleDataWrapper<StorageType>, ObjectType extends XmlDbElem, StorageType extends XmlDbElem> extends
                                                                                                                                                     SimpleXmlDB<Container, ObjectType, StorageType>
{

    /**
     * Constructor for derived classes.
     * @param uri
     *            URI to where base is located
     * @param clazz
     *            class used by XML parser * @param
     *            converter converter between stored in XML
     *            and in database types
     * @param converter
     *            converter between stored in XML and in
     *            database types
     */
    protected SimpleXmlSyncDB(URI uri,
                              Class<Container> clazz,
                              Converter<ObjectType, StorageType> converter)
    {
        super(uri, clazz, converter, true);
        if (!readFile())
        {
            Log.logger.error("Database not initialized properly.");
        }
    }

    /**
     * @param uri
     * @return
     */
    private boolean readFile()
    {
        SimpleDataWrapper<StorageType> container =
                getParser().parse(getFileUrl());

        if (container == null)
            return false;

        for (StorageType obj : container.getTypes())
        {
            super.put(obj);
        }

        Log.logger.debug("Database reading finished for: " + getFileUrl());
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public ObjectType getById(short id)
    {
        if (super.hasId(id))
        {
            return super.get(id);
        }
        else
        {
            Log.logger.warn("Object with id: " + id + " not found in database.");
            return null;
        }
    }
}
