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
package pl.org.minions.stigma.databases.parsers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.w3c.dom.Node;

import pl.org.minions.utils.logger.Log;

/**
 * Factory used to generate and keep instances of parsers.
 * Accessing parsers from it is recommended.
 */
public class ParserFactory
{
    private static class DummyParser<T extends Parsable> extends
                                                         GenericParser<T>
    {
        private Class<T> clazz;

        public DummyParser(Class<T> clazz)
        {
            this.clazz = clazz;
        }

        @Override
        public boolean createXML(T parsable, Node node)
        {
            Log.logger.warn("Calling DummyParser.createXML(Node) for: "
                + clazz.getName());
            return false;
        }

        @Override
        public boolean createXML(T parsable, OutputStream os)
        {
            Log.logger.warn("Calling DummyParser.createXML(OutputStream) for: "
                + clazz.getName());
            return false;
        }

        @Override
        public boolean createXML(T parsable, Writer writer)
        {
            Log.logger.warn("Calling DummyParser.createXML(Writer) for: "
                + clazz.getName());
            return false;
        }

        @Override
        public T parse(InputStream stream)
        {
            Log.logger.warn("Calling DummyParser.parse(InputStream) for: "
                + clazz.getName());
            return null;
        }

        @Override
        public T parse(Node node)
        {
            Log.logger.warn("Calling DummyParser.parse(Node) for: "
                + clazz.getName());
            return null;
        }

        @Override
        public T parse(Source source)
        {
            Log.logger.warn("Calling DummyParser.parse(Source) for: "
                + clazz.getName());
            return null;
        }

        @Override
        public T parse(URL url)
        {
            Log.logger.warn("Calling DummyParser.parse(URL) for: "
                + clazz.getName());
            return null;
        }

        @Override
        public void setSchema(Schema schema) throws JAXBException
        {
            Log.logger.warn("Calling DummyParser.setSchema for: "
                + clazz.getName());
        }

        @Override
        public void setValidationEventHandler(ValidationEventHandler handler) throws JAXBException
        {
            Log.logger.warn("Calling DummyParser.setValidationEventHandler for: "
                + clazz.getName());
        }
    }

    private static ParserFactory instance;

    private Map<Class<?>, GenericParser<?>> parserMap =
            new HashMap<Class<?>, GenericParser<?>>();

    /**
     * Hidden constructor.
     */
    protected ParserFactory()
    {
    }

    /**
     * Gets instance of ParserFactory if it is not
     * initialized initializes it.
     * @return instance of ParserFactory
     */
    public static ParserFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ParserFactory();
        }

        return instance;
    }

    /**
     * Returns parser for given class.
     * @param clazz
     *            class description for requested parser
     * @param childrenClasses
     *            classes derived from base class which
     *            should be known by parser
     * @param validate
     *            whether or not parser should validate
     *            unmarshalled files
     * @param <T>
     *            class for which generic parser should be
     *            returned
     * @return parser for given class
     */
    @SuppressWarnings("unchecked")
    public <T extends Parsable> GenericParser<T> getParser(boolean validate,
                                                           Class<T> clazz,
                                                           Class<? extends T>... childrenClasses)
    {
        GenericParser<T> parser = null;
        try
        {
            parser = (GenericParser<T>) parserMap.get(clazz);
        }
        catch (ClassCastException e)
        {
            parser = null;
            Log.logger.error("Bad cast: " + e.getMessage());
        }

        if (parser == null)
        {
            try
            {
                parser = new GenericParser<T>(validate, clazz, childrenClasses);
            }
            catch (JAXBException e)
            {
                Log.logger.error("Error while initializing parser for class: "
                    + clazz.getName() + " error: " + e.getMessage(), e);
                parser = new DummyParser<T>(clazz);
            }
            parserMap.put(clazz, parser);
        }

        return parser;
    }
}
