/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

/**
 * The class used to dynamically load classes contained in jar files.
 *
 * @author Zleurtor
 */
public class JarDynamicLoader extends ClassLoader {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The temporary filename prefix.
     */
    private static final String PREFIX_TMP_FILE = "tmp.";
    /**
     * The temporary filename suffix.
     */
    private static final String SUFFIX_TMP_FILE = ".jar";

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The Collection containing the urls of the Jars to dynamically load.
     */
    private final Collection<URL> urls = new ArrayList<URL>();
    /**
     * The Collection containing the already defined classes.
     */
    private final Map<String, Class<?>> definedClasses = new HashMap<String, Class <?>>();

    //==========================================================================
    // Constructors
    //==========================================================================

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * The method used to dynamically load a class.
     *
     * @param name The name of the class to load.
     * @return The first found class matching with the given name.
     * @throws ClassNotFoundException If no class matching with the given
     *                                name was found.
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @Override
    public final Class<?> loadClass(final String name) throws ClassNotFoundException {
        for (URL url : urls) {
            String entryName = name.replace('.', '/').concat(".class");
            String fileName = url.getFile().replace("%20", " ");

            try {
                JarFile jar = new JarFile(fileName);
                ZipEntry entry = jar.getEntry(entryName);

                Class<?> ret = null;
                if (entry != null) {
                    InputStream in = jar.getInputStream(entry);
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    in.close();

                    ret = defineClass(name, buffer);
                }

                jar.close();

                if (ret != null) {
                    return ret;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }

        return super.loadClass(name);
    }

    /**
     * The method used to dynamically load a resource.
     *
     * @param name The name of the resource to load.
     * @return The first found resource matching with the given name.
     * @see java.lang.ClassLoader#getResource(java.lang.String)
     */
    @Override
    public final URL getResource(final String name) {
        for (URL url : urls) {
            try {
                String fileName = url.getFile().replace("%20", " ");
                JarFile jar = new JarFile(fileName);

                for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(name)) {
                        return new URL("jar:" + url + "!/" + entry.getName());
                    }
                }

                jar.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }

        return super.getResource(name);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Define a class giving its name and its byte code.
     *
     * @param className The name of the class to define.
     * @param buffer The byte code of the class to define.
     * @return The class defined byt its name and byte code.
     */
    private Class<?> defineClass(final String className, final byte[] buffer) {
        if (definedClasses.keySet().contains(className)) {
            return definedClasses.get(className);
        } else {
            Class<?> ret = defineClass(className, buffer, 0, buffer.length);
            resolveClass(ret);

            definedClasses.put(className, ret);

            return ret;
        }
    }

    /**
     * Add a URL to the Collection containing the urls of the Jars to
     * dynamically load.
     *
     * @param url The URL of the new jar to dynamically load.
     */
    public final void addURL(final URL url) {
        // Check the URL is not null was not ever added
        if (url == null) {
            return;
        } else {
            for (URL tmp : urls) {
                if (tmp.toString().equals(url.toString())) {
                    return;
                }
            }
        }

        // Check the jar is in another jar (or not)
        URL jarURL = url;
        if (url.getProtocol().equals("jar")) {
            try {
                // Copy the jar to a temporary file (this file will be
                // deleted at the virtual machine closure).
                File tmp = File.createTempFile(PREFIX_TMP_FILE, SUFFIX_TMP_FILE);
                tmp.deleteOnExit();

                InputStream in = url.openStream();
                OutputStream out = new FileOutputStream(tmp);

                for (int read = in.read(); read != -1; read = in.read()) {
                    out.write(read);
                }

                in.close();
                out.flush();
                out.close();

                jarURL = tmp.toURI().toURL();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }

        // Check the jar is a file
        if (jarURL.getProtocol().equals("file")) {
            urls.add(jarURL);
        }
    }
}
