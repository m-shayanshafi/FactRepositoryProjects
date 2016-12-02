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
package pl.org.minions.stigma.editor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import pl.org.minions.stigma.globals.ResourceSetProperties;
import pl.org.minions.utils.Properties;
import pl.org.minions.utils.Version;

/**
 * {link ResourceSetProperties} extension class which allows
 * edition of some values.
 */
public class EditableResourceSetProperties extends ResourceSetProperties
{
    private String fileName;

    /**
     * Constructor.
     * @param fileName
     *            path to file with resource set properties
     */
    public EditableResourceSetProperties(String fileName)
    {
        super(fileName);
        this.fileName = fileName;
    }

    /** {@inheritDoc} */
    @Override
    public void setDescription(String desc)
    {
        super.setDescription(desc);
    }

    /** {@inheritDoc} */
    @Override
    public void setName(String name)
    {
        super.setName(name);
    }

    /** {@inheritDoc} */
    @Override
    public void setValidated(boolean validated, Date modificationDate)
    {
        super.setValidated(validated, modificationDate);
    }

    /**
     * Saves resource set properties. Uses same file name as
     * was used for loading.
     * @throws IOException
     *             thrown when save fails
     */
    public void save() throws IOException
    {
        Properties props = new Properties();

        props.setProperty(NAME, getName());
        props.setProperty(DESCRIPTION, getDescription());
        props.setProperty(VALIDATED, isValidated());
        props.setProperty(MODIFIED, getModifiedDate());

        OutputStream out = new FileOutputStream(fileName);

        props.store(out, "Edited by " + Version.getAppName() + ' '
            + Version.FULL_VERSION);
    }

}
