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
package pl.org.minions.stigma.client;

import pl.org.minions.stigma.globals.GlobalConfig;

/**
 * Singleton class for storing {@link GlobalConfig} on
 * client.
 */
public final class ClientConfig
{
    private static GlobalConfig instance;

    private ClientConfig()
    {
    }

    /**
     * Returns global instance of configuration on client.
     * @return global instance
     */
    public static GlobalConfig globalInstance()
    {
        assert instance != null;
        return instance;
    }

    /**
     * Sets new global instance of configuration.
     * @param conf
     *            new configuration
     */
    public static void setGlobalConfig(GlobalConfig conf)
    {
        instance = conf;
    }
}
