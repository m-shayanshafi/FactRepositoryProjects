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

import pl.org.minions.utils.logger.Log;

/**
 * Singleton responsible for building {@link AiScript} from
 * {@link AiDescription}.
 */
public class AiScriptFactory
{
    private static AiScriptFactory instance = new AiScriptFactory();
    private AiScript defaultAiScript = new DefaultAiScript();
    private Map<String, AiScript> scriptPrototypes =
            new HashMap<String, AiScript>();

    {
        scriptPrototypes.put("default", new DefaultAiScript());
        scriptPrototypes.put("idle", new IdleAiScript());
    }

    /**
     * Returns singleton instance.
     * @return singleton instance.
     */
    public static AiScriptFactory globalInstance()
    {
        return instance;
    }

    /**
     * Builds script using given description.
     * @param aiDescription
     *            description of AI script
     * @return builded AI script (on error
     *         {@link DefaultAiScript} will be returned.
     */
    public AiScript buildAiScript(AiDescription aiDescription)
    {
        AiScript scriptPrototype =
                scriptPrototypes.get(aiDescription.getName());
        if (scriptPrototype == null)
        {
            Log.logger.warn("Couldn't find AiScript implementation for name: "
                + aiDescription.getName());
            scriptPrototype = defaultAiScript;
        }

        AiScript script = scriptPrototype.create();
        script.initialize(aiDescription);
        return script;

    }
}
