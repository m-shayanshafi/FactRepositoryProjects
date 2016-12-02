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

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.databases.parsers.GenericParser;
import pl.org.minions.stigma.databases.parsers.ParserFactory;

/**
 * Abstract class representing simple database - database
 * stored in one file. Those databases are always loaded
 * during construction. In simple databases no converters
 * and StorageTypes are used.
 * @param <Container>
 *            Container which wraps object class to XML
 *            file. It must extend abstract, generic
 *            {@link SimpleDataWrapper} with use of
 *            ObjectType.
 * @param <ObjectType>
 *            object which is database element (has id)
 * @param <StorageType>
 *            object which is directly stored in XML
 *            database (is parsable and convertible to
 *            ObjectType)
 */
public abstract class SimpleXmlDB<Container extends SimpleDataWrapper<StorageType>, ObjectType extends XmlDbElem, StorageType extends XmlDbElem>
{
    private URI uri;
    private boolean validate;

    private GenericParser<Container> cachedParser;
    private Class<Container> clazz;
    private Map<Short, ObjectType> map = new HashMap<Short, ObjectType>();
    private Converter<ObjectType, StorageType> converter;

    /**
     * Constructor for derived classes.
     * @param uri
     *            URI to where base is located
     * @param clazz
     *            class used by XML parser
     * @param converter
     *            converter between stored in XML and in
     *            database types
     * @param validate
     *            whether or not loaded files should be
     *            validate against proper schemas
     */
    protected SimpleXmlDB(URI uri,
                          Class<Container> clazz,
                          Converter<ObjectType, StorageType> converter,
                          boolean validate)
    {
        this.validate = validate;
        this.uri = uri;
        this.clazz = clazz;
        this.converter = converter;
    }

    /**
     * Returns keys stored in database.
     * @return keys stored in database.
     */
    protected final Collection<Short> getKeys()
    {
        return map.keySet();
    }

    /**
     * Creates parser for database element.
     * @return database element's parser
     */
    protected final GenericParser<Container> getParser()
    {
        if (cachedParser == null)
            cachedParser =
                    ParserFactory.getInstance().getParser(validate, clazz);
        return cachedParser;
    }

    /**
     * Checks whether database contains object which
     * specified id. For derived classes.
     * @param id
     *            id of object
     * @return true if database contains object, otherwise
     *         false
     */
    protected final boolean hasId(short id)
    {
        return map.containsKey(id);
    }

    /**
     * Returns set of currently available identifiers in
     * database (there might be more, yet not read).
     * @return available identifiers
     */
    public Set<Short> keys()
    {
        return map.keySet();
    }

    /**
     * Puts object into database. For derived classes.
     * @param object
     *            object to put into database
     * @return converted object
     */
    protected final ObjectType put(StorageType object)
    {
        ObjectType obj = converter.buildObject(object);
        short id = obj.getId();
        map.put(id, obj);
        return obj;
    }

    /**
     * Returns all currently available values stored in
     * database.
     * @return available objects
     */
    public Collection<ObjectType> values()
    {
        return map.values();
    }

    /**
     * Gets object by id form database.
     * @param id
     *            id of object
     * @return object or null if it is not in database
     */
    protected final ObjectType get(short id)
    {
        return map.get(id);
    }

    /**
     * Creates URL from id of database element.
     * @return URL to file representing database element
     */
    protected final URL getFileUrl()
    {
        String path = getDbDir() + "/" + getFilePrefix() + ".xml";
        return Resourcer.getXMLResourceUrl(uri, path);
    }

    /**
     * Returns "object" converted to "data".
     * @param obj
     *            object to be converted
     * @return data representing object
     * @see Converter#buildData(Object)
     */
    protected final StorageType getData(ObjectType obj)
    {
        if (obj == null)
            return null;
        return converter.buildData(obj);
    }

    /**
     * Returns "object" for given id converted to "data".
     * May return {@code null} when no such object in
     * database.
     * @param id
     *            id of object to be converted
     * @return data representing object
     * @see Converter#buildData(Object)
     */

    protected final StorageType getData(short id)
    {
        return getData(get(id));
    }

    /**
     * Return object form database by it's id.
     * @param id
     *            id of object
     * @return object if it exists in database, otherwise
     *         null
     */
    public abstract ObjectType getById(short id);

    /**
     * Gets directory to database.
     * @return directory to database
     */
    public abstract String getDbDir();

    /**
     * Gets database file default prefix.
     * @return database file prefix
     */
    public abstract String getFilePrefix();
}
