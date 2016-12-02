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
package pl.org.minions.stigma.globals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import pl.org.minions.utils.Properties;
import pl.org.minions.utils.logger.Log;

/**
 * Class describing properties of resource set. Contains
 * resource meta-data. It should be sent to client.
 */
public class ResourceSetProperties
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VALIDATED = "validated";
    public static final String MODIFIED = "modified";

    public static final String DEFAULT_NAME = "test";
    public static final String DEFAULT_DESCRIPTION = "No name world";
    public static final boolean DEFAULT_VALIDATED = false;
    public static final Date DEFAULT_MODIFIED = new Date(0);

    private String name;
    private String desc;
    private boolean validated;
    private Date modified;

    /**
     * Constructor.
     * @param name
     *            see {@link #getName()}
     * @param desc
     *            see {@link #getDescription()}
     */
    public ResourceSetProperties(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
        this.validated = false;
    }

    /**
     * Constructor. Loads properties from given file.
     * @param fileName
     *            file from which properties should be read
     */
    public ResourceSetProperties(String fileName)
    {
        Properties prop = new Properties();

        try
        {
            InputStream in = new FileInputStream(fileName);
            prop.load(in);
            in.close();
        }
        catch (IOException e)
        {
            Log.logger.error(e);
        }

        name = prop.getProperty(NAME, DEFAULT_NAME);
        desc = prop.getProperty(DESCRIPTION, DEFAULT_DESCRIPTION);
        validated = prop.getProperty(VALIDATED, DEFAULT_VALIDATED);
        modified = prop.getProperty(MODIFIED, DEFAULT_MODIFIED);
    }

    /**
     * Returns descriptive name of resource set (world), as
     * not in directory name, but as configured. Stored in
     * file as {@value #NAME} with default value:
     * {@value #DEFAULT_NAME}.
     * @return descriptive name of world
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns description of resource set (world). Stored
     * in file as {@value #DESCRIPTION} with default value:
     * {@value #DEFAULT_DESCRIPTION}.
     * @return description of world
     */
    public String getDescription()
    {
        return desc;
    }

    /**
     * Returns whether or not this resource set was
     * validated in editor. Stored in file as
     * {@value #VALIDATED} with default value:
     * {@value #DEFAULT_VALIDATED}.
     * @return {@code true} when resource set was validated
     *         in editor
     */
    public boolean isValidated()
    {
        return validated;
    }

    /**
     * Returns date of last modification of resource set.
     * Stored in file as {@value #MODIFIED} with default
     * value: {@code new Date(0)}.
     * @return date of last modification of resource set
     */
    public Date getModifiedDate()
    {
        return modified;
    }

    /**
     * Sets new value of name. Should be called by editor or
     * similar application only.
     * @param name
     *            new value
     */
    protected void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new value of description. Should be called by
     * editor or similar application only.
     * @param desc
     *            new value
     */
    protected void setDescription(String desc)
    {
        this.desc = desc;
    }

    /**
     * Sets new value for validation parameters. Should be
     * called by editor or similar application only.
     * Remember - server accepts "validated" resource sets
     * only.
     * @param validated
     *            new value of validation process result
     * @param modificationDate
     *            time that should be visible as last
     *            edition time
     */
    protected void setValidated(boolean validated, Date modificationDate)
    {
        this.validated = validated;
        this.modified = modificationDate;
    }

}
