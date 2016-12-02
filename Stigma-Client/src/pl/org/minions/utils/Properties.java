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
package pl.org.minions.utils;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

import pl.org.minions.utils.logger.Log;

/**
 * Wrapper class for standard {@link java.util.Properties}.
 * Allows to get different types of properties than just
 * strings. Provides some useful overloaded methods.
 */
public class Properties extends java.util.Properties
{
    private static final long serialVersionUID = 1L;

    /**
     * Return boolean property of given name.
     * @param name
     *            name of property
     * @param def
     *            default value
     * @return property value or default value if not
     *         present, or bad format
     */
    public boolean getProperty(String name, boolean def)
    {
        String val = this.getProperty(name);
        if (val == null)
            return def;
        try
        {
            return Boolean.parseBoolean(val);
        }
        catch (NumberFormatException e)
        {
            Log.logger.info(MessageFormat.format("Property: {0} is not proper boolean: {1}",
                                                 name,
                                                 e));
            return def;
        }
    }

    /**
     * Return byte property of given name.
     * @param name
     *            name of property
     * @param def
     *            default value
     * @return property value or default value if not
     *         present, or bad format
     */
    public byte getProperty(String name, byte def)
    {
        String val = this.getProperty(name);
        if (val == null)
            return def;
        try
        {
            return Byte.parseByte(val);
        }
        catch (NumberFormatException e)
        {
            Log.logger.info(MessageFormat.format("Property: {0} is not proper byte: {1}",
                                                 name,
                                                 e));
            return def;
        }
    }

    /**
     * Return integer property of given name.
     * @param name
     *            name of property
     * @param def
     *            default value
     * @return property value or default value if not
     *         present, or bad format
     */
    public int getProperty(String name, int def)
    {
        String val = this.getProperty(name);
        if (val == null)
            return def;
        try
        {
            return Integer.parseInt(val);
        }
        catch (NumberFormatException e)
        {
            Log.logger.info(MessageFormat.format("Property: {0} is not proper integer: {1}",
                                                 name,
                                                 e));
            return def;
        }
    }

    /**
     * Return short property of given name.
     * @param name
     *            name of property
     * @param def
     *            default value
     * @return property value or default value if not
     *         present, or bad format
     */
    public short getProperty(String name, short def)
    {
        String val = this.getProperty(name);
        if (val == null)
            return def;
        try
        {
            return Short.parseShort(val);
        }
        catch (NumberFormatException e)
        {
            Log.logger.info(MessageFormat.format("Property: {0} is not proper short: {1}",
                                                 name,
                                                 e));
            return def;
        }
    }

    /**
     * Return date property of given name.
     * @param name
     *            name of property
     * @param def
     *            default value
     * @return property value or default value if not
     *         present, or bad format
     */
    public Date getProperty(String name, Date def)
    {
        String val = this.getProperty(name);
        if (val == null)
            return def;
        try
        {
            return DateFormat.getInstance().parse(val);
        }
        catch (ParseException e)
        {
            Log.logger.info(MessageFormat.format("Property: {0} is not proper date: {1}",
                                                 name,
                                                 e));
            return def;
        }
    }

    /**
     * Loads properties (as key=value pairs) from string
     * array. May be useful for loading application
     * arguments.
     * @param args
     *            array of strings to load from
     */
    public void load(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        for (String str : args)
        {
            buf.append(str);
            buf.append('\n');
        }

        StringReader reader = new StringReader(buf.toString());

        try
        {
            load(reader);
        }
        catch (IOException e)
        {
            Log.logger.info("Unexpected exception: " + e);
        }
    }

    /**
     * Function provided for convenience.
     * @param key
     *            property name
     * @param value
     *            property value
     * @param <T>
     *            type of property
     */
    public <T> void setProperty(String key, T value)
    {
        setProperty(key, String.valueOf(value));
    }
}
