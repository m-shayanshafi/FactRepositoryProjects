/*
 * Metaserver.java
 *
 * Created on February 7, 2007, 7:54 PM
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

package netbattle.database;
import java.net.*;
import java.io.*;
import java.nio.*;

/**
 * An exception when interacting with the metaserver.
 */
class MetaserverException extends RuntimeException {
    public MetaserverException(Exception e) {
        super(e);
    }
}

/**
 *
 * @author Colin
 */
public class Metaserver {
    
    private final static InetAddress m_host;
    private final static int m_port = 9757;
    private final static ByteOrder m_order = ByteOrder.LITTLE_ENDIAN;
    
    static {
        InetAddress host = null;
        try {
            host = InetAddress.getByName("official.shoddybattle.com");
        } catch (UnknownHostException e) {
            System.out.println("Could not connect to the metaserver.");
        }
        m_host = host;
    }
    
    /**
     * Post a server to the metaserver.
     */
    public static int postServer(ServerListEntry entry) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(entry.getName());
        buffer.append('\001');
        String desc = entry.getDescription();
        if (desc.length() == 0) {
            desc = " ";
        }
        buffer.append(desc);
        buffer.append('\001');
        //buffer.append(entry.getHost());
        //buffer.append('\001');
        String result = null;
        try {
            result = new String(new String(buffer).getBytes(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new MetaserverException(e);
        }
        
        byte[] temp = result.getBytes();
        byte[] raw = new byte[temp.length + 16];
        for (int i = 0; i < temp.length; ++i) {
            raw[i + 4] = temp[i];
        }
        raw[0] = 2;
        
        ByteBuffer bytes = ByteBuffer.wrap(raw, raw.length - 12, 12)
            .order(m_order);
        bytes.putInt(entry.getPort());
        bytes.putInt(entry.getUsers());
        bytes.putInt(entry.getMaxUsers());

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            throw new MetaserverException(e);
        }

        DatagramPacket data = new DatagramPacket(raw, raw.length, m_host, m_port);
        int code = 0;
        try {
            socket.send(data);
            data.setLength(4);
            data.setData(new byte[4]);
            socket.receive(data);
            code = ByteBuffer.wrap(data.getData()).order(m_order).getInt();
            
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MetaserverException(e);
        }
        
        return code;
    }
    
    /**
     * Remove a server from the metaserver's list.
     */
    public static void removeServer(String name, int code) {
        byte[] raw = new byte[name.length() + 8];
        byte[] str = null;
        try {
            str = name.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new MetaserverException(e);
        }
        for (int i = 8; i < raw.length; ++i) {
            raw[i] = str[i - 8];
        }
        ByteBuffer.wrap(raw).order(m_order)
            .putInt(4) // METASERVER_REMOVE
            .putInt(code);
        
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            throw new MetaserverException(e);
        }

        DatagramPacket data = new DatagramPacket(raw, raw.length, m_host, m_port);
        try {
            socket.send(data);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MetaserverException(e);
        }
    }
    
    private static void queryServer(final String host, final int port,
            final ServerListEntry entry, final Runnable informUpdate) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    DatagramPacket query = new DatagramPacket(
                            new byte[0], 0,
                            InetAddress.getByName(host), port);
                    DatagramSocket socket = new DatagramSocket();
                    socket.setSoTimeout(60000);
                    socket.send(query);
                    byte[] response = new byte[4];
                    query.setData(response);
                    query.setLength(response.length);
                    socket.receive(query);
                    entry.m_users =
                            ByteBuffer.wrap(response).getInt();
                    informUpdate.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Get the version of Shoddy Battle a server is running. Returns zero if
     * the server predates the addition of the version functionality.
     */
    public static int getServerVersion(String host, int port) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(2500); // Allow 2.5 seconds to respond.
        } catch (Exception e) {
            return 0;
        }
        byte[] raw = { 1, 0 };
        try {
            DatagramPacket data = new DatagramPacket(raw, raw.length, 
                    InetAddress.getByName(host), port);
            socket.send(data);
            raw = new byte[] { 0, 0, 0, 0 };
            data.setData(raw);
            data.setLength(raw.length);
            socket.receive(data);
            if (data.getLength() != 4)
                return 0;
            return (raw[3] << 0)
                + (raw[2] << 8)
                + (raw[1] << 16)
                + (raw[0] << 24);
        } catch (Exception e) {
            socket.close();
            return 0;
        }
    }
    
    /**
     * Get a list of servers from the metaserver.
     */
    public static ServerListEntry[] getServerList(final Runnable informUpdate) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            return null;
        }
        
        byte[] raw = { 1, 0, 0, 0 };
        DatagramPacket data = new DatagramPacket(raw, raw.length, m_host, m_port);
        try {
            socket.send(data);
            socket.receive(data);
            int size = ByteBuffer.wrap(raw).order(m_order).getInt();
            
            if (size == 0) {
                return null;
            }
            
            raw = new byte[size];
            data.setData(raw);
            data.setLength(raw.length);
            socket.receive(data);
            
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            return null;
        }
        
        // Treat the data as an ASCII string.
        String str = null;
        try {
            str = new String(raw, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new MetaserverException(e);
        }
        String[] servers = str.split("\002");
        
        final ServerListEntry[] entries = new ServerListEntry[servers.length];
        
        try {
            // One minute timeout is quite lengthy, but will stop the thread
            // eventually.
            socket.setSoTimeout(60000);
        } catch (SocketException e) {
            throw new MetaserverException(e);
        }
        
        int distance = 0;
        for (int i = 0; i < servers.length; ++i) {
            try {
                final String[] parts = servers[i].split("\001");
                for (int j = 0; j < 3; ++j) {
                    distance += parts[j].length() + 1;
                }
                ByteBuffer buffer = ByteBuffer.wrap(raw, distance, 12).order(m_order);
                final int port = buffer.getInt();
                buffer.getInt(); // "users"
                final int maximum = buffer.getInt();
                distance += 3 * 4 + 1; // Read three ints and one \002
            
                entries[i] = new ServerListEntry(parts[0], parts[1], parts[2], port, 0, maximum);
                
                // Contact server to determine number of users online.
                queryServer(parts[2], port, entries[i], informUpdate);
                
            } catch (Exception e) {
                entries[i] = new ServerListEntry("", "", "", 0, 0, 0);
            }
        }
        
        socket.close();
        return entries;
    }
    
}
