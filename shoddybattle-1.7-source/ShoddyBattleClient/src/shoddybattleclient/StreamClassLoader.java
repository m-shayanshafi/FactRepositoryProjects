/*
 * StreamClassLoader.java
 *
 * Created on January 7, 2007, 8:45 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package shoddybattleclient;
import java.io.*;
import mechanics.statuses.PoisonEffect;

/**
 *
 * @author Colin
 */
public class StreamClassLoader extends ClassLoader {
    
    public StreamClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }
    
    public Class loadClass(File f) throws IOException {
        int size = (int)f.length();
        byte[] data = new byte[size];
        int read = 0;
        InputStream input = f.toURL().openStream();
        while ((size - read) > 0) {
            int chunk = input.read(data, read, size - read);
            if (chunk == -1) {
                throw new IOException();
            }
            read += chunk;
        }
        Class c = defineClass(null, data, 0, data.length);
        resolveClass(c);
        return c;
    }
    
}
