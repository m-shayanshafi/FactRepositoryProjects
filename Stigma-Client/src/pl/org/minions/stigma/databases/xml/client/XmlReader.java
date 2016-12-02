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
package pl.org.minions.stigma.databases.xml.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.utils.logger.Log;
import pl.org.minions.utils.streams.CountingInputStream;

/**
 * Reader which reads XML documents. It works in thread and
 * waits for XML document requests.
 */
public class XmlReader implements Runnable
{
    /**
     * Request for asynchronous read of XML document. Read
     * document should be send using given callback.
     */
    private static class XmlRequest
    {
        private URL url;
        private XmlReceiver receiver;
        private boolean monitor;

        /**
         * Constructor.
         * @param url
         *            URL of requested XML document
         * @param receiver
         *            object that should be informed about
         *            successful receiving of document
         * @param monitor
         *            whether or not reading of this request
         *            should be monitored
         */
        public XmlRequest(URL url, XmlReceiver receiver, boolean monitor)
        {
            this.url = url;
            this.receiver = receiver;
            this.monitor = monitor;
        }

        /**
         * Returns final receiver of requested document.
         * @return final receiver of requested document
         */
        public XmlReceiver getReceiver()
        {
            return receiver;
        }

        /**
         * Returns the URL of requested XML document.
         * @return the URL of requested XML document
         */
        public URL getUrl()
        {
            return url;
        }

        public boolean isMonitored()
        {
            return monitor;
        }
    }

    private static XmlReader instance;

    private BlockingQueue<XmlRequest> requests =
            new LinkedBlockingQueue<XmlRequest>();

    private List<XmlProgressObserver> observers =
            new LinkedList<XmlProgressObserver>();

    /**
     * Hidden constructor.
     */
    protected XmlReader()
    {
        assert instance == null;
    }

    /**
     * Returns instance of XMLReader.
     * @return instance of XMLReader
     */
    public static XmlReader globalInstance()
    {
        if (instance == null)
        {
            instance = new XmlReader();
            instance.init();
        }
        return instance;
    }

    /**
     * Initializes reader thread.
     */
    protected void init()
    {
        Thread t = new Thread(this, "XmlReader thread");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Create request for xml document described by URL.
     * @param url
     *            URL to xml document
     * @param receiver
     *            XMLReceiver to which request will be
     *            passed
     * @param monitor
     *            whether or not this request should result
     *            in generation of
     *            {@link XmlProgressMonitor}.
     */
    public void request(URL url, XmlReceiver receiver, boolean monitor)
    {
        try
        {
            requests.put(new XmlRequest(url, receiver, monitor));
        }
        catch (InterruptedException e)
        {
            Log.logger.error(e);
        }
    }

    /**
     * Adds observer of processing of XML files.
     * @param observer
     *            observer to add
     * @see XmlReader#removeObserver(XmlProgressObserver)
     */
    public void addObserver(XmlProgressObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Removes observer.
     * @param observer
     *            observer to remove
     * @see XmlReader#addObserver(XmlProgressObserver)
     */
    public void removeObserver(XmlProgressObserver observer)
    {
        observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        while (true)
        {
            XmlRequest msg = null;
            try
            {
                msg = requests.take();
            }
            catch (InterruptedException e)
            {
                Log.logger.warn(e);
                break;
            }
            assert msg != null;

            URLConnection connection;
            InputStream in;
            try
            {
                connection = msg.getUrl().openConnection();
                connection.connect();
                in = connection.getInputStream();
            }
            catch (IOException e2)
            {
                Log.logger.error("Resource retrieving failed: " + msg.getUrl()
                    + " because of: " + e2);
                continue;
            }

            if (msg.isMonitored() && !observers.isEmpty())
            {
                CountingInputStream cnt = new CountingInputStream(in);
                in = cnt;
                XmlProgressMonitor monit =
                        new XmlProgressMonitor(msg.getUrl(), connection, cnt);
                for (XmlProgressObserver observer : observers)
                    observer.processingStarted(monit);
            }

            if (GlobalConfig.globalInstance().isResourceCompression())
            {
                try
                {
                    in = new GZIPInputStream(in);
                }
                catch (IOException e)
                {
                    Log.logger.error("GZIPInputStream creation failed!", e);
                    continue;
                }
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            Document document;

            try
            {
                builder = factory.newDocumentBuilder();
                document = builder.parse(in);
            }
            catch (ParserConfigurationException e1)
            {
                Log.logger.error("newDocumentBuilder failed!", e1);
                continue;
            }
            catch (SAXException e)
            {
                Log.logger.error("parse failed, SAX exception: ", e);
                continue;
            }
            catch (IOException e)
            {
                Log.logger.error("parse failed, IO exception", e);
                continue;
            }

            msg.getReceiver().parseXmlDocument(document);
        }
    }
}
