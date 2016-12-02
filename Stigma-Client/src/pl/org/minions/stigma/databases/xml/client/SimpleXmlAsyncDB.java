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
package pl.org.minions.stigma.databases.xml.client;

import java.net.URI;

import org.w3c.dom.Document;

import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.SimpleDataWrapper;
import pl.org.minions.stigma.databases.xml.SimpleXmlDB;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.utils.logger.Log;

/**
 * Base class for Simple XML database on client side.
 * @param <Container>
 *            wrapper containing ObjectType elements
 * @param <ObjectType>
 *            Objects stored in database
 * @param <StorageType>
 *            object which is directly stored in XML
 *            database (is parsable and convertible to
 *            ObjectType)
 */
public abstract class SimpleXmlAsyncDB<Container extends SimpleDataWrapper<StorageType>, ObjectType extends XmlDbElem, StorageType extends XmlDbElem> extends
                                                                                                                                                      SimpleXmlDB<Container, ObjectType, StorageType> implements
                                                                                                                                                                                                     XmlReceiver
{
    private volatile boolean loaded;

    /**
     * Constructor for derived classes.
     * @param uri
     *            URI to where base is located
     * @param clazz
     *            class used by XML parser
     * @param converter
     *            converter between stored in XML and in
     *            database types
     */
    protected SimpleXmlAsyncDB(URI uri,
                               Class<Container> clazz,
                               Converter<ObjectType, StorageType> converter)
    {
        super(uri, clazz, converter, false);
        XmlReader.globalInstance().request(getFileUrl(), this, true);
    }

    /** {@inheritDoc} */
    @Override
    public final ObjectType getById(short id)
    {
        if (id == -1)
            return null;

        if (hasId(id))
            return super.get(id);
        return getStub();
    }

    /**
     * Default implementation. Returns stub for not yet
     * downloaded object.
     * @return stub for not yet downloaded object
     */
    protected ObjectType getStub()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final void parseXmlDocument(Document document)
    {
        Container container = getParser().parse(document);
        if (container == null)
        {
            Log.logger.warn("Parsing failed");
            loaded = true;
            return;
        }

        //loads whole database from downloaded container
        for (StorageType object : container.getTypes())
        {
            ObjectType obj = super.put(object);
            postParsing(obj);
        }

        loaded = true;
    }

    /**
     * Will be called after parsing object and putting it in
     * library. Executed by {@link XmlReader} thread.
     * Default implementation does nothing.
     * @param obj
     *            newly parsed object
     */
    protected void postParsing(ObjectType obj)
    {
        return;
    }

    /**
     * Returns {@code true} when this database is fully
     * loaded.
     * @return {@code true} when this database is fully
     *         loaded.
     */
    public boolean isLoaded()
    {
        return loaded;
    }

}
