/*
 * BattleServer.java
 *
 * Created on December 20, 2006, 6:20 PM
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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package netbattle;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import netbattle.messages.NetMessage;
import netbattle.messages.WelcomeMessage;
import netbattle.messages.FieldTextMessage;
import netbattle.messages.UserTableMessage;
import netbattle.database.Metaserver;
import netbattle.database.ServerListEntry;
import netbattle.database.registry.AccountRegistry;
import shoddybattle.util.TimeInterval;
import shoddybattle.util.ThreadedQueue;

/**
 *
 * @author Colin
 */
public class BattleServer implements Runnable {
    
    public static class BattleServerException extends Exception {
        public BattleServerException(Exception cause) {
            super(cause);
        }
    }
    
    /** Version of the server. */
    public static final int SERVER_VERSION = 3;
    
    /** File containing the welcome text message. This file must be writable
     *  so that the welcome text can be changed by administrators without
     *  having to restart the server. */
    private static final File m_welcomeFile = new File("welcome");

    /** Welcome message sent to each client when he connects to the server. */
    private WelcomeMessage m_welcome;
    
    /** List of clients currently using this server. */
    private List m_clients = Collections.synchronizedList(new ArrayList());
    
    /** Port this server is running on. */
    private int m_port;
    
    /** Human-readable name of this server. */
    private String m_name;
    
    /** Brief description of this server (currently unused). */
    private String m_description;
    
    /** UUID of the server. */
    private String m_uniqueName;
    
    /** Maximum capacity of the server (currently unenforced). */
    private int m_capacity;
    
    /** Secret code used for communication with metaserver. Code is received
     *  from metaserver when a server is posted; the code must be provided
     *  in order to remove the server from the metaserver. */
    private int m_code;
    
    /** Class providing the default mechanics for battles (via NetBattleField)
     *  taking place on this server. */
    private Class m_mechanics;
    
    /** Entry in the chat log queue.
     *  This class also exposes a couple of stock time formatting methods. */
    public static class ChatQueueItem {
        private Date m_date;
        private String m_message;
        public ChatQueueItem(Date date, String message) {
            m_date = date;
            m_message = message;
        }
        private static String format(int n) {
            if (n < 10) {
                return "0" + n;
            }
            return String.valueOf(n);
        }
        public static String formatDate(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) + "-"
                    + format(calendar.get(Calendar.MONTH) + 1) + "-"
                    + format(calendar.get(Calendar.DAY_OF_MONTH));
        }
        public String getFileName() {
            return formatDate(m_date);
        }
        public static String getPrefix() {
            return getPrefix(new Date());
        }
        public static String getPrefix(Date date) {
            return "(" + formatTime(date) + ") ";
        }
        public static String formatTime(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return format(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + format(calendar.get(Calendar.MINUTE)) + ":"
                    + format(calendar.get(Calendar.SECOND));
        }
        public String toString() {
            return getPrefix(m_date) + m_message;
        }
    }
    
    /** Thread that updates the log files as chat takes place. */
    private ThreadedQueue m_chatQueue = new ThreadedQueue(
        new ThreadedQueue.QueueDelegate() {
            public void handleItem(Object item) {
                ChatQueueItem message = (ChatQueueItem)item;
                try {
                    FileOutputStream output = new FileOutputStream(
                            "logs/chat/" + message.getFileName(), true);
                    new PrintWriter(output, true).println(message.toString());
                    output.close();
                } catch (IOException e) {
                    
                }
            }
    });
    
    /** Creates a new instance of BattleServer */
    public BattleServer(String name,
            String description,
            String unique,
            int port,
            int capacity,
            Class mechanics) {
        m_port = port;
        m_name = name;
        m_uniqueName = unique;
        m_description = description;
        m_capacity = capacity;
        m_mechanics = mechanics;
    }
    
    /**
     * Get the default mechanics for battles on the server.
     */
    public Class getMechanicsClass() {
        return m_mechanics;
    }
    
    public String getServerName() {
        return m_name;
    }
    
    /**
     * Remove server from the metaserver.
     */
    public void removeFromMetaserver() {
        if (m_code == 0) {
            return;
        }
        
        Metaserver.removeServer(m_name, m_code);
    }
    
    /**
     * A lightweight server that provides user count information upon request.
     */
    private class UserQueryServer implements Runnable {
        private DatagramSocket m_server;
        
        public UserQueryServer() throws IOException {
            System.out.println("Opening a UDP socket on port " + m_port + "...");
            m_server = new DatagramSocket(m_port);
        }
        
        public void run() {
            byte[] data = new byte[4];
            ByteBuffer buffer = ByteBuffer.wrap(data);
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while (true) {
                try {
                    m_server.receive(packet);
                    int l = packet.getLength();
                    if (l == 0) {
                        packet.setLength(4);
                        buffer.putInt(0, m_clients.size());
                    } else if (l == 1) {
                        StringBuffer string = new StringBuffer();
                        synchronized (m_clients) {
                            Iterator i = m_clients.iterator();
                            while (i.hasNext()) {
                                NetClient client = (NetClient)i.next();
                                String user = client.getUserName();
                                if ((user != null) && (user.length() != 0)) {
                                    string.append(user);
                                    string.append(", ");
                                }
                            }
                        }
                        if (string.length() == 0) {
                            string.append("Empty, ");
                        }
                        String datum = string.substring(0, string.length() - 2);
                        packet.setLength(4);
                        int size = datum.length();
                        buffer.putInt(0, size);
                        m_server.send(packet);
                        packet.setData(data = datum.getBytes("ISO-8859-1"));
                        buffer = ByteBuffer.wrap(data);
                        packet.setLength(size);
                    } else if (l == 2) {
                        packet.setLength(4);
                        buffer.putInt(0, SERVER_VERSION);
                    }
                    m_server.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Run the server.
     */
    public void run() {
        System.out.println("Opening a TCP/IP socket on port " + m_port + "...");
        ServerSocket server = null;
        try {
            server = new ServerSocket(m_port);
            new Thread(new UserQueryServer()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (server == null) {
            return;
        }
        
        System.out.println("Posting server to the metaserver...");
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    int code = Metaserver.postServer(new ServerListEntry(
                        m_name, m_description, null, m_port, 0, m_capacity
                    ));
                    if (code != 0) {
                        m_code = code;
                    }
                    synchronized (this) {
                        try {
                            wait(1000 * 60 * 5);
                        } catch (InterruptedException e) {
                            
                        }
                    }
                }
            }
        }).start();
        
        try {
            m_welcome = new WelcomeMessage(m_name, getWelcomeText(), m_uniqueName);
        } catch (BattleServerException e) {
            e.printStackTrace();
            return;
        }
        
        System.out.println("Starting chat logging thread...");
        new File("logs", "chat/").mkdirs();
        m_chatQueue.start();
        
        System.out.println("Server is running.");
        while (true) {
            try {
                final Socket socket = server.accept();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            NetClient client = new NetClient(BattleServer.this,
                                    socket);
                            m_clients.add(client);
                            client.sendMessage(m_welcome);
                            client.start();
                            System.out.println("Accepted new client: "
                                    + socket.getInetAddress().getHostName()
                                    + ".");
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * Set the text of the welcome message.
     */
    public void setWelcomeText(String text) throws BattleServerException {
        synchronized (m_welcomeFile) {
            if (m_welcome != null) {
                m_welcome.setText(text);
            }
            
            try {
                PrintWriter writer = new PrintWriter(
                        new FileWriter(m_welcomeFile));
                writer.print(text);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new BattleServerException(e);
            }
        }
    }
    
    /**
     * Get the text of the welcome message from disc.
     */
     public String getWelcomeText() throws BattleServerException {
         if (m_welcome != null) {
             return m_welcome.getText();
         }
         synchronized (m_welcomeFile) {
             try {
                FileInputStream reader = new FileInputStream(m_welcomeFile);
                StringBuffer buffer = new StringBuffer();
                int read;

                while ((read = reader.read()) != -1) {
                    buffer.append((char)read);
                }
                reader.close();
                return new String(buffer);
             } catch (FileNotFoundException e) {
                 throw new BattleServerException(e);
             } catch (IOException e) {
                 throw new BattleServerException(e);
             }
         }
     }

    /**
     * Get the port this server is running on.
     */
    public int getPort() {
        return m_port;
    }
    
    /**
     * Return the number of clients.
     */
    public int getClientCount() {
        return m_clients.size();
    }
    
    /**
     * Get a list of user names.
     */
    public String[] getClientNames(int[] level, int[] status) {
        synchronized (m_clients) {
            List list = new ArrayList();
            Iterator i = m_clients.iterator();
            int j = -1;
            while (i.hasNext()) {
                NetClient client = (NetClient)i.next();
                String name = client.getUserName();
                if ((name == null) || (name.length() == 0))
                    continue;
                ++j;
                list.add(name);
                if (level != null) {
                    level[j] = client.getUserLevel();
                }
                if (status != null) {
                    status[j] = client.getStatus();
                }
            }
            return (String[])list.toArray(new String[list.size()]);
        }
    }
    
    /**
     * Kick a client by name.
     */
    public void kickClient(NetClient mod, String name) {
        NetClient client = getClientByName(name);
        if ((client != null)
                && ((mod.getUserLevel() > client.getUserLevel())
                    || mod.getUserName().equals(name))) {
            sendChatMessage(name + " was kicked.");
            removeClient(client);
        }
    }
    
    public void banClient(NetClient mod, String name, long date) {
        banClient(mod, name, date, " was banned for ");
    }
    
    /**
     * Send a chat message to all clients.
     */
    public void sendChatMessage(String message) {
        sendChatMessage(message, false);
    }
    
    public void sendChatMessage(String message, boolean important) {
        FieldTextMessage msg = new FieldTextMessage(-1, message);
        msg.setImportant(important);
        broadcast(msg);
        m_chatQueue.post(new ChatQueueItem(new Date(), message));
    }
    
    /**
     * Ban a client by name.
     */
    public void banClient(NetClient mod, String name, long date, String message) {
        AccountRegistry registry = AccountRegistry.getInstance();
        NetClient client = getClientByName(name);
        int modLevel = mod.getUserLevel();
        if (client != null) {
            if ((modLevel <= client.getUserLevel()) && !mod.equals(client))
                return;
            client.setBanExpiry(date);
            if (date != 0) {
                try {
                    TimeInterval interval = TimeInterval.getDeltaInterval(
                        new Date(date));
                    sendChatMessage(name + message + interval.getApproximation() + ".");
                } catch (Exception e) {
                    /** In the unlikely (but possible) event that TimeInterval
                     *  proves to have a bug, we do not want to bring down this
                     *  whole thread.
                     */
                    e.printStackTrace();
                }
            }
        } else {
            int userLevel = registry.getUserLevel(name);
            if (modLevel <= userLevel)
                return;
            registry.banUser(name, date);
        }
        
        // Now kick the user as well.
        String ip = registry.getIpAddress(name);
        if (ip == null)
            return;
        synchronized (m_clients) {
            Iterator i = m_clients.iterator();
            while (i.hasNext()) {
                NetClient item = (NetClient)i.next();
                if (ip.equals(item.getIpAddress())) {
                    i.remove();
                    item.close();
                }
            }
        }
    }
    
    /**
     * Get a client by his user name.
     */
    public NetClient getClientByName(String name) {
        synchronized (m_clients) {
            Iterator i = m_clients.iterator();
            while (i.hasNext()) {
                NetClient client = ((NetClient)i.next());
                if (name.equalsIgnoreCase(client.getUserName())) {
                    return client;
                }
            }
            return null;
        }
    }
    
    /**
     * Determine whether a user is logged on.
     */
    public boolean isLoggedOn(String name) {
        return (getClientByName(name) != null);
    }
    
    /**
     * Remove a client.
     */
    public void removeClient(NetClient client) {
        m_clients.remove(client);
        try {
            client.close();
        } catch (Exception e) {
            // This thread should not be killed by a bug in close().
        }
    }
    
    /**
     * Set the level of a user.
     */
    public void setUserLevel(String user, int level) {
        AccountRegistry.getInstance().setUserLevel(user, level);
        NetClient client = getClientByName(user);
        if (client != null) {
            client.setUserLevel(level);
        }
    }
    
    /**
     * Get a list of users and their information on this server.
     */
    public UserTableMessage getUserInformation() {
        return AccountRegistry.getInstance().getUserList();
    }
    
    /**
     * Broadcast a message to all clients.
     */
    public void broadcast(NetMessage msg) {
        synchronized (m_clients) {
            Iterator i = m_clients.iterator();
            while (i.hasNext()) {
                NetClient client = (NetClient)i.next();
                client.sendMessage(msg);
            }
        }
    }
    
}
