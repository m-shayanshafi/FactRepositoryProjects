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
package pl.org.minions.stigma.server.ai;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

import pl.org.minions.stigma.databases.xml.Modifiable;

/**
 * Class describing AI of NPC. It contains reference to
 * {@link AiScript} and it parameters.
 */
public class AiDescription implements Modifiable
{
    private String name = "default";
    private Map<String, String> params = new HashMap<String, String>();
    private boolean modified;

    /**
     * Constructor.
     */
    public AiDescription()
    {
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
    }

    /**
     * Returns AI script name.
     * @return AI script name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns AI script parameters.
     * @return AI script parameters
     */
    public Map<String, String> getParams()
    {
        return params;
    }

    /**
     * Returns parameter value. May return {@code null} if
     * no such parameter.
     * @param paramName
     *            parameter name
     * @return parameter value
     */
    public String getParamValue(String paramName)
    {
        return params.get(paramName);
    }

    /**
     * Returns parameter value as boolean. If no such
     * parameter present - returns default value.
     * @param paramName
     *            parameter name
     * @param defaultValue
     *            default value
     * @return parameter value
     */
    public boolean getParamValue(String paramName, boolean defaultValue)
    {
        String str = getParamValue(paramName);
        if (str == null)
            return defaultValue;

        return Boolean.parseBoolean(str);
    }

    /**
     * Returns parameter value as integer. If no such
     * parameter present - returns default value.
     * @param paramName
     *            parameter name
     * @param defaultValue
     *            default value
     * @return parameter value
     */
    public int getParamValue(String paramName, int defaultValue)
    {
        String str = getParamValue(paramName);
        if (str == null)
            return defaultValue;
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    /**
     * Returns parameter value. If no such parameter present
     * - returns default value.
     * @param paramName
     *            parameter name
     * @param defaultValue
     *            default value
     * @return parameter value
     */
    public String getParamValue(String paramName, String defaultValue)
    {
        String str = getParamValue(paramName);
        if (str == null)
            return defaultValue;
        return str;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        return modified;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        this.modified = true;
    }

    /**
     * Sets new AI script name.
     * @param name
     *            new AI script name
     */
    @XmlAttribute(name = "name", required = false)
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new AI script parameters.
     * @param params
     *            new AI script parameters
     */
    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }

    /**
     * Sets parameter value.
     * @param <T>
     *            parameter value type, should have {@code
     *            toString()} defined properly
     * @param paramName
     *            parameter name
     * @param value
     *            parameter value
     */
    public <T> void setParamValue(String paramName, T value)
    {
        params.put(paramName, String.valueOf(value));
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[AiDescription name=" + name + " params= " + params + "]";
    }
}
