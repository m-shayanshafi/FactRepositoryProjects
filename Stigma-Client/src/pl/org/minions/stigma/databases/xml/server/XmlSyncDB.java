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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.TreeSet;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.databases.xml.XmlDB;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.utils.logger.Log;

/**
 * Base class for all synchronous XML databases. It's
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
public abstract class XmlSyncDB<ObjectType extends XmlDbElem, StorageType extends Parsable> extends
                                                                                            XmlDB<ObjectType, StorageType>
{
    private boolean autoload;
    private TreeSet<Short> keySet;

    /**
     * Constructor for derived classes.
     * @param clazz
     *            class used by XML parser
     * @param converter
     *            converter between stored in XML and in
     *            database types
     * @param uri
     *            URI to where base is located
     * @param autoload
     *            {@code true} when DB should try to load
     *            all object from given location at creation
     *            time (may take a while)
     * @param childrenClasses
     *            classes derived from base class which also
     *            could be included in DB
     */
    protected XmlSyncDB(URI uri,
                        Class<StorageType> clazz,
                        Converter<ObjectType, StorageType> converter,
                        boolean autoload,
                        Class<? extends StorageType>... childrenClasses)
    {
        super(uri, true, clazz, converter, childrenClasses);
        if (autoload && scanFolder(uri))
        {
            this.autoload = true;
        }
        else
        {
            Log.logger.info("'Auto-load' functionality of this database will be disabled");
            this.autoload = false;
        }
    }

    /**
     * Adds object to database.
     * @param obj
     *            object to add - should have proper id
     * @param replace
     *            if {@code true} then existing object will
     *            be replaced, otherwise if id is not unique
     *            function will fail
     * @param save
     *            if {@code true} object will be saved
     *            immediately after adding
     * @param forceIdSet
     *            if {@code true} object will have new id
     *            set (first free in database). Uses
     *            {@link #generateId()}.
     * @return {@code true} when object was added
     *         successfully (and saved if {@code save ==
     *         true})
     */
    public final boolean add(ObjectType obj,
                             boolean replace,
                             boolean save,
                             boolean forceIdSet)
    {
        if (!replace && !forceIdSet && hasId(obj.getId()))
        {
            Log.logger.error("Object already exists: " + obj.getId());
            return false;
        }

        if (forceIdSet)
            obj.setId(generateId());

        super.add(obj);

        if (keySet != null)
            keySet.add(obj.getId());

        if (save && !save(obj))
            return false;

        return hasId(obj.getId());
    }

    /** {@inheritDoc} */
    @Override
    public final ObjectType getById(short id)
    {
        if (autoload || hasId(id))
        {
            return super.get(id);
        }
        else
        {
            StorageType object = getParser().parse(createUrlFromId(id));
            if (object == null)
            {
                Log.logger.error("Parsing failed");
                return null;
            }
            return super.put(object);
        }
    }

    /**
     * Checks if any of the objects in it was modified.
     * @return modification state of this DB
     */
    public boolean isModified()
    {
        for (ObjectType obj : values())
        {
            if (obj.isModified())
                return true;
        }
        return false;
    }

    private boolean save(ObjectType obj)
    {
        URL url = createUrlFromId(obj.getId());
        if (url == null)
        {
            Log.logger.error("Null URL for id: " + obj.getId());
            return false;
        }
        if (!url.getProtocol().equals("file"))
        {
            Log.logger.error("Tried to safe with protocol: "
                + url.getProtocol());
            return false;
        }

        OutputStream s;
        try
        {
            URI uri = url.toURI();
            File file = new File(uri);
            s = new FileOutputStream(file);
        }
        catch (URISyntaxException e)
        {
            Log.logger.error("URI syntax error for url: " + url
                + " with exception" + e);
            return false;
        }
        catch (FileNotFoundException e)
        {
            Log.logger.error("Opening stream failed for path: " + url.getPath()
                + " with exception: " + e);
            return false;
        }

        StorageType data = super.getData(obj);
        assert data != null;
        boolean res = getParser().createXML(data, s);
        try
        {
            s.close();
        }
        catch (IOException e)
        {
            Log.logger.error("close failed: " + e);
            return false;
        }
        if (res)
            obj.clearModified();
        return res;
    }

    /**
     * Saves all {@link Modifiable#isModified() modified}
     * objects from database.
     * @param forceForward
     *            when {@code true} function will proceed
     *            event if some saves fail.
     * @return {@code true} when all saves succeeded
     */
    public final boolean saveAll(boolean forceForward)
    {
        boolean res = true;
        for (ObjectType obj : values())
            if (obj.isModified())
            {
                res = res && save(obj);
                if (!res && !forceForward)
                    return false;
            }
        return res;
    }

    /**
     * Saves object with given id on disk.
     * @param id
     *            id of object to be saved
     * @return {@code true} when save was successful
     */
    public final boolean saveById(short id)
    {
        if (!hasId(id))
        {
            Log.logger.error("Tried to save object which is not in database");
            return false;
        }

        return save(super.get(id));
    }

    /**
     * Returns new id that can be used in this database for
     * new object. First call to this function may be little
     * slower than subsequent (on first call this method
     * creates some cache for speeding up next calls).
     * Normal calls should be constant time, but when
     * database will be 'almost full' method may slow down
     * and become linear time.
     * @return 'not used' id
     * @throws IllegalStateException
     *             when database is full
     */
    public short generateId() throws IllegalStateException
    {
        if (keySet == null)
            keySet = new TreeSet<Short>(keys());

        if (keySet.isEmpty())
            return Short.MIN_VALUE;

        if (keySet.size() == Short.MAX_VALUE - Short.MIN_VALUE)
            throw new IllegalStateException("No more free ids - database is full");

        short id = keySet.last();
        if (id == Short.MAX_VALUE)
        {
            // so there must be some free id inside set 
            id = Short.MIN_VALUE;
            for (short s : keySet)
            {
                if (s != id)
                    return id;
                ++id;
            }
            assert "We should have found some id!".equals(null);
        }

        return ++id;
    }

    private boolean scanFolder(URI uri)
    {
        File dir = new File(uri.getPath() + File.separator + getDbDir());

        if (!dir.isDirectory())
        {
            Log.logger.warn("Given URI does not point to directory: " + uri);
            return false;
        }

        File[] files = dir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".xml")
                    && name.startsWith(getFilePrefix());
            }
        });

        final int begIdx = getFilePrefix().length();
        final int endOff = 4; //.xml
        for (File file : files)
        {
            String name = file.getName();
            Log.logger.debug("XmlDB will try to (auto) load file: " + name);
            short id;
            try
            {
                id =
                        Short.parseShort(name.substring(begIdx, name.length()
                            - endOff));
            }
            catch (NumberFormatException e)
            {
                Log.logger.warn("File: " + file
                    + " has name in improper format");
                continue;
            }

            if (getById(id) == null)
            {
                Log.logger.warn("Problem loading file: " + file);
                continue;
            }
        }

        Log.logger.debug("Folder scan finished for: " + dir);
        return true;
    }
}
