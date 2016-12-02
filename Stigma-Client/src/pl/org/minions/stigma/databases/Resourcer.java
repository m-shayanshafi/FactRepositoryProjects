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
package pl.org.minions.stigma.databases;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.utils.logger.Log;

/**
 * Class which is responsible for returning coherent streams
 * to data, using proper application configuration.
 * @see GlobalConfig
 */
public abstract class Resourcer
{
    private Resourcer()
    {
    }

    /**
     * Computes given resource root URI and current resource
     * path into single URL. It is independent from
     * client/server resources storage differences.
     * @param base
     *            URI to resources root
     * @param path
     *            path to current resource
     * @return URL for resource, based on {@code base} and
     *         {@code path}
     */
    public static URL getXMLResourceUrl(URI base, String path)
    {
        if (GlobalConfig.globalInstance().isResourceCompression())
        {
            path += ".gz";
        }

        path = base.getPath() + '/' + path;
        URI file;
        try
        {
            file =
                    new URI(base.getScheme(),
                            base.getUserInfo(),
                            base.getHost(),
                            base.getPort(),
                            path,
                            base.getQuery(),
                            base.getFragment());
        }
        catch (URISyntaxException e)
        {
            Log.logger.error("URI creation failed: " + e);
            return null;
        }

        URL url;
        if (file.isAbsolute())
            try
            {
                url = file.toURL();
            }
            catch (MalformedURLException e)
            {
                Log.logger.error("URI -> URL failed: " + e);
                return null;
            }
        else
        {
            try
            {
                url = new File(file.getPath()).toURI().toURL();
            }
            catch (MalformedURLException e)
            {
                Log.logger.error("relative URI -> URL failed: " + e);
                return null;
            }
        }

        return url;
    }

    /**
     * Loads a file as an array of bytes, independent from
     * application/applet differences. Call is synchronous.
     * May be used to load files from inside of JAR package
     * file.
     * @param name
     *            name of file
     * @return loaded file
     */
    public static byte[] loadFileBytes(String name)
    {
        try
        {
            URL url = Resourcer.class.getClassLoader().getResource(name);

            if (url != null) // in JAR
            {
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[connection.getContentLength()];

                for (int read = 0; read < buffer.length; read +=
                        inputStream.read(buffer, read, buffer.length - read))
                    ;

                return buffer;

            }
            else
            {
                FileInputStream inputStream = new FileInputStream(name);
                byte[] buffer = new byte[(int) inputStream.getChannel().size()];

                for (int read = 0; read < buffer.length; read +=
                        inputStream.read(buffer, read, buffer.length - read))
                    ;

                return buffer;
            }
        }
        catch (IOException e)
        {
            return null;
        }

    }

    /**
     * Loads icon, independent from application/applet
     * differences. Call is synchronous. May be used to load
     * icons from inside of JAR package file.
     * @param name
     *            name of icon
     * @return loaded icon
     */
    public static ImageIcon loadIcon(String name)
    {
        Image img = loadImage(name);
        if (img == null)
            return null;

        return new ImageIcon(img);
    }

    /**
     * Loads image, independent from application/applet
     * differences. Call is synchronous. May be used to load
     * images from inside of JAR package file.
     * @param name
     *            name of image
     * @return loaded image
     */
    public static BufferedImage loadImage(String name)
    {
        try
        {
            URL url = Resourcer.class.getClassLoader().getResource(name);
            if (url != null) // in JAR
                return ImageIO.read(url);
            return ImageIO.read(new File(name));
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
