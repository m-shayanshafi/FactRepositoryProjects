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
package pl.org.minions.stigma.databases.parsers.test;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import pl.org.minions.stigma.databases.actor.wrappers.ArchetypeWrapper;
import pl.org.minions.stigma.databases.actor.wrappers.ProficiencyWrapper;
import pl.org.minions.stigma.databases.item.wrappers.ArmorModifierWrapper;
import pl.org.minions.stigma.databases.item.wrappers.ModifierCategoryWrapper;
import pl.org.minions.stigma.databases.item.wrappers.WeaponModifierWrapper;
import pl.org.minions.stigma.game.item.type.data.ArmorTypeData;
import pl.org.minions.stigma.game.item.type.data.OtherTypeData;
import pl.org.minions.stigma.game.item.type.data.WeaponTypeData;
import pl.org.minions.stigma.game.map.data.MapTypeData;
import pl.org.minions.stigma.game.map.data.TerrainSetData;
import pl.org.minions.stigma.server.item.StaticItems;
import pl.org.minions.stigma.server.npc.StaticNpcs;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Utility class for XSD schemas generation.
 */
public final class SchemaGenerator
{
    private static class MySchemaOutputResolver extends SchemaOutputResolver
    {
        private String className;
        private String version;

        public MySchemaOutputResolver(Class<?> clazz, String version)
        {
            this.className = clazz.getSimpleName();
            this.version = version;
        }

        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException
        {
            FileOutputStream fstream =
                    new FileOutputStream("res/metadata/xsd/" + className + '-'
                        + version + ".xsd");
            StreamResult result = new StreamResult(fstream);
            result.setSystemId(Double.toString(Math.random()));
            return result;
        }
    }

    private SchemaGenerator()
    {
    }

    /**
     * Generates all XSD schemas used in Stigma.
     */
    public static void generateAll()
    {
        generateXSD(MapTypeData.class);
        generateXSD(TerrainSetData.class);
        generateXSD(ArchetypeWrapper.class);
        generateXSD(StaticNpcs.class);
        generateXSD(WeaponTypeData.class);
        generateXSD(ArmorTypeData.class);
        generateXSD(OtherTypeData.class);
        generateXSD(StaticItems.class);
        generateXSD(ProficiencyWrapper.class);
        generateXSD(ModifierCategoryWrapper.class);
        generateXSD(WeaponModifierWrapper.class);
        generateXSD(ArmorModifierWrapper.class);
    }

    private static void generateXSD(Class<?> clazz)
    {
        try
        {
            Log.logger.debug("Generating: " + clazz);
            JAXBContext context = JAXBContext.newInstance(clazz);
            context.generateSchema(new MySchemaOutputResolver(clazz,
                                                              Version.FULL_VERSION));
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Main procedure.
     * @param args
     *            arguments (not used)
     */
    public static void main(String[] args)
    {
        Log.initialize("log_config.properties", "SchemaGenerator");

        generateAll();
    }
}
