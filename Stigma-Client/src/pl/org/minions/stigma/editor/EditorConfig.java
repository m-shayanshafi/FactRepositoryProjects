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

import java.io.File;
import java.net.URI;
import java.util.Date;

import pl.org.minions.stigma.globals.GlobalConfig;

/**
 * Stub - ensures {@link GlobalConfig#globalInstance()} will
 * work.
 */
public final class EditorConfig extends GlobalConfig
{
    private static EditorConfig instance = new EditorConfig();

    private URI uri;

    private EditorConfig()
    {
        super(false, true);
    }

    /**
     * Returns singleton instance.
     * @return singleton instance.
     */
    public static EditorConfig globalInstance()
    {
        return instance;
    }

    /** {@inheritDoc} */
    @Override
    public URI getClientResourceUri()
    {
        return uri;
    }

    /**
     * Sets {@link URI} which will be returned by
     * {@link GlobalConfig#getClientResourceUri()}.
     * @param uri
     *            new URI
     * @return modifiable resource set properties
     */
    public EditableResourceSetProperties setUri(URI uri)
    {
        this.uri = uri;

        EditableResourceSetProperties resProp =
                new EditableResourceSetProperties(uri.getPath()
                    + File.separator + RESOURCE_SET_FILE);
        setResourceSetProperties(resProp);

        resProp.setValidated(false, new Date(0));

        return resProp;
    }

}
