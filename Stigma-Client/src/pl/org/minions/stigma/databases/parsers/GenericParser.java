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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Generic parser which allows to parse XML database objects
 * which implement Parsable interface. Build in JAXB
 * technology is used in this parser. <br/>
 * <br/>
 * CAUTION!<br/>
 * Do not initialize this class manually - use ParserFactory
 * instead.
 * @param <T>
 *            Class for which generic XML parser should be
 *            created
 */
public class GenericParser<T extends Parsable>
{
    private static final String SCHEMA_EXTENSION = ".xsd";
    private static final String SCHEMA_BASE =
            "http://stigma.sourceforge.net/schemes/";
    private static final String LOCAL_SCHEMA_BASE = "res/metadata/xsd/";
    private static final SchemaFactory SCHEMA_FACTORY =
            SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private Class<T> clazz; // for error logging only

    /**
     * Special constructor for wrapping classes.
     */
    protected GenericParser()
    {
    }

    /**
     * Constructor.
     * @param clazz
     *            runtime class of hierarchy base
     * @param childrenClasses
     *            all classes derived from base which should
     *            be known by parser
     * @param validate
     *            whether or not parser should validate
     *            unmarshalled files
     * @throws JAXBException
     *             thrown when error occurs during JAXB
     *             initialization
     */
    public GenericParser(boolean validate,
                         Class<T> clazz,
                         Class<? extends T>... childrenClasses) throws JAXBException
    {
        this.clazz = clazz;
        JAXBContext jc;
        if (childrenClasses == null || childrenClasses.length == 0)
            jc = JAXBContext.newInstance(clazz);
        else
        {
            Class<? extends T>[] classes =
                    Arrays.copyOf(childrenClasses, childrenClasses.length + 1);
            classes[childrenClasses.length] = clazz;
            jc = JAXBContext.newInstance(classes);
        }
        unmarshaller = jc.createUnmarshaller();
        marshaller = jc.createMarshaller();

        String schemaLocation =
                SCHEMA_BASE + clazz.getSimpleName() + '-'
                    + Version.FULL_VERSION + SCHEMA_EXTENSION;
        marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
                               schemaLocation);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                               new Boolean(true));

        if (validate && SCHEMA_FACTORY != null)
        {
            File file =
                    new File(LOCAL_SCHEMA_BASE + clazz.getSimpleName() + '-'
                        + Version.FULL_VERSION + SCHEMA_EXTENSION);
            try
            {
                Schema schema = SCHEMA_FACTORY.newSchema(file);
                unmarshaller.setSchema(schema);
            }
            catch (SAXException e)
            {
                Log.logger.error("Bad schema: " + file + " error: " + e);
            }

        }
    }

    private boolean compareVersions(String readVersion)
    {
        if (readVersion == null || readVersion.isEmpty())
        {
            Log.logger.warn(clazz.getSimpleName()
                + ": XML resource without version");
            return false;
        }

        // if in dev - accept everything
        if (Version.isDevelopment())
            return true;

        String[] fileVersion = readVersion.split("\\.");
        final int verSize = 3;

        if (fileVersion.length != verSize)
        {
            Log.logger.warn(clazz.getSimpleName()
                + ": Unrecognized version format: " + readVersion);
            return false;
        }

        if (!fileVersion[0].equals(Version.MAJOR_NUMBER)
            || !fileVersion[1].equals(Version.MINOR_NUMBER))
        {
            Log.logger.warn(clazz.getSimpleName()
                + ": Incompatible XML version: " + readVersion);
            return false;
        }

        if (!fileVersion[2].equals(Version.PATCH_NUMBER))
            Log.logger.warn(clazz.getSimpleName()
                + ": Loading older version of XML file: " + readVersion);

        return true;
    }

    /**
     * Creates XML file from specified Parsable to specified
     * output.
     * @param parsable
     *            Parsable to create XML from
     * @param node
     *            output
     * @return true if operation ends successfully false if
     *         error occurred
     */
    public boolean createXML(T parsable, Node node)
    {
        boolean success = false;
        try
        {
            marshaller.marshal(parsable, node);
            success = true;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        return success;
    }

    /**
     * Creates XML file from specified Parsable to specified
     * output.
     * @param parsable
     *            Parsable to create XML from
     * @param os
     *            output
     * @return true if operation ends successfully false if
     *         error occurred
     */
    public boolean createXML(T parsable, OutputStream os)
    {
        boolean success = false;
        try
        {
            marshaller.marshal(parsable, os);
            success = true;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        return success;
    }

    /**
     * Creates XML file from specified Parsable to specified
     * output.
     * @param parsable
     *            Parsable to create XML from
     * @param writer
     *            output
     * @return true if operation ends successfully false if
     *         error occurred
     */
    public boolean createXML(T parsable, Writer writer)
    {
        boolean success = false;
        try
        {
            marshaller.marshal(parsable, writer);
            success = true;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        return success;
    }

    /**
     * Creates Parsable from XML.
     * @param stream
     *            input
     * @return mapType or null if error occurred
     */
    @SuppressWarnings("unchecked")
    public T parse(InputStream stream)
    {
        T object = null;
        try
        {
            object = (T) unmarshaller.unmarshal(stream);
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        if (object == null || !compareVersions(object.getVersion())
            || !object.isGood())
        {
            return null;
        }

        return object;
    }

    /**
     * Creates Parsable from XML.
     * @param node
     *            input
     * @return mapType or null if error occurred
     */
    @SuppressWarnings("unchecked")
    public T parse(Node node)
    {
        T object = null;
        try
        {
            object = (T) unmarshaller.unmarshal(node);
        }
        catch (ClassCastException e)
        {
            Log.logger.error(e);
            object = null;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        if (object == null || !compareVersions(object.getVersion())
            || !object.isGood())
        {
            return null;
        }

        return object;
    }

    /**
     * Creates Parsable from XML.
     * @param source
     *            input
     * @return mapType or null if error occurred
     */
    @SuppressWarnings("unchecked")
    public T parse(Source source)
    {
        T object = null;
        try
        {
            object = (T) unmarshaller.unmarshal(source);
        }
        catch (ClassCastException e)
        {
            Log.logger.error(e);
            object = null;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        if (object == null || !compareVersions(object.getVersion())
            || !object.isGood())
        {
            return null;
        }

        return object;
    }

    /**
     * Creates from XML.
     * @param url
     *            input
     * @return parsed object or null if error occurred
     */
    @SuppressWarnings("unchecked")
    public T parse(URL url)
    {
        T object = null;
        try
        {
            object = (T) unmarshaller.unmarshal(url);
        }
        catch (ClassCastException e)
        {
            Log.logger.error(e);
            object = null;
        }
        catch (JAXBException jaxbe)
        {
            Log.logger.error(jaxbe);
        }
        if (object == null || !compareVersions(object.getVersion())
            || !object.isGood())
        {
            return null;
        }
        return object;
    }

    /**
     * Sets schema for parser.
     * @param schema
     *            schema to validate input
     * @throws JAXBException
     *             thrown when error occurs during schema
     *             initialization
     */
    public void setSchema(Schema schema) throws JAXBException
    {
        if (schema != null)
        {
            unmarshaller.setSchema(schema);
            marshaller.setSchema(schema);
        }
    }

    /**
     * Sets ValidationEventHandler for parser.
     * @param handler
     *            own validation error event handler
     * @throws JAXBException
     *             thrown when error occurs during handler
     *             initialization
     */
    public void setValidationEventHandler(ValidationEventHandler handler) throws JAXBException
    {
        if (handler != null)
        {
            unmarshaller.setEventHandler(handler);
            marshaller.setEventHandler(handler);
        }
    }
}
