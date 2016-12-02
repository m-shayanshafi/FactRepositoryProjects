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
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.XmlDB;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.utils.logger.Log;

/**
 * Base class for all asynchronous XML databases. It's
 * represents local cache - if requested item is not in
 * database it is read from URL (disk) and inserted into
 * database. Request always returns proper, complete item.
 * @param <ObjectType>
 *            object which is database element (has id)
 * @param <StorageType>
 *            object which is directly stored in XML
 *            database (is parsable and convertible to
 *            ObjectType)
 */
public abstract class XmlAsyncDB<ObjectType extends XmlDbElem, StorageType extends Parsable> extends
                                                                                             XmlDB<ObjectType, StorageType> implements
                                                                                                                           XmlReceiver
{
    private Set<Short> requestedSet = new HashSet<Short>();
    private List<XmlCompletionObserver> observers =
            new LinkedList<XmlCompletionObserver>();
    private boolean monitor;

    /**
     * Constructor for derived classes.
     * @param clazz
     *            class used by XML parser
     * @param uri
     *            URI to where base is located
     * @param childrenClasses
     *            classes derived from base class which also
     *            could be included in DB
     * @param converter
     *            converter between stored in XML and in
     *            database types
     * @param monitor
     *            when {@code true} {@link XmlReader} will
     *            generate {@link XmlProgressMonitor
     *            monitoring} information
     */
    protected XmlAsyncDB(URI uri,
                         Class<StorageType> clazz,
                         Converter<ObjectType, StorageType> converter,
                         boolean monitor,
                         Class<? extends StorageType>... childrenClasses)
    {
        super(uri, false, clazz, converter, childrenClasses);
        this.monitor = monitor;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized ObjectType getById(short id)
    {
        if (hasId(id))
            return super.get(id);
        else
        {
            if (!requestedSet.contains(id))
            {
                requestedSet.add(id);
                URL url = createUrlFromId(id);
                if (url != null)
                    request(url);
            }

            return getStub(id);
        }
    }

    /**
     * Default implementation. Returns stub for not yet
     * downloaded object.
     * @param id
     *            id of stubbed object
     * @return stub for not yet downloaded object
     */
    protected ObjectType getStub(short id)
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final void parseXmlDocument(Document document)
    {
        StorageType object = getParser().parse(document);
        if (object == null)
        {
            Log.logger.warn("Parsing failed");
            return;
        }

        ObjectType obj;
        short id;
        synchronized (this)
        {
            obj = super.put(object);
            id = obj.getId();

            requestedSet.remove(id);
        }

        postParsing(obj);

        for (XmlCompletionObserver observer : observers)
            observer.objectComplete(id);

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

    private void request(URL url)
    {
        XmlReader.globalInstance().request(url, this, monitor);
    }

    /**
     * Adds completion observer. Given observer will be
     * notified about every completed item downloaded by
     * this database.
     * @param observer
     *            observer to add
     */

    public final void addObserver(XmlCompletionObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Removes completion observer. Given observer will be
     * no longer notified.
     * @param observer
     *            observer to remove
     */
    public final void removeObserver(XmlCompletionObserver observer)
    {
        observers.remove(observer);
    }
}
