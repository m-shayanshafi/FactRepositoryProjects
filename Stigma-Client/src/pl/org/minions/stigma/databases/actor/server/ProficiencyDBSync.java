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
package pl.org.minions.stigma.databases.actor.server;

import java.net.URI;

import pl.org.minions.stigma.databases.actor.ProficiencyDB;
import pl.org.minions.stigma.databases.actor.wrappers.ProficiencyWrapper;
import pl.org.minions.stigma.databases.xml.Converter.SimpleConverter;
import pl.org.minions.stigma.databases.xml.server.SimpleXmlSyncDB;
import pl.org.minions.stigma.game.actor.Proficiency;

/**
 * Proficiency simple database.
 * @see ProficiencyDB
 * @see SimpleXmlSyncDB
 */
public class ProficiencyDBSync extends
                              SimpleXmlSyncDB<ProficiencyWrapper, Proficiency, Proficiency> implements
                                                                                           ProficiencyDB
{

    /**
     * Default constructor.
     * @param uri
     *            root URI
     */
    public ProficiencyDBSync(URI uri)
    {
        super(uri, ProficiencyWrapper.class, new SimpleConverter<Proficiency>());
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return FILE_PREFIX;
    }

    /** {@inheritDoc} */
    @Override
    public Proficiency getProficiency(short id)
    {
        return super.getById(id);
    }

}
