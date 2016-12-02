/*
 * BotClient.java
 *
 * Created on August 15, 2007, 6:01 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
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
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattleclient;

import java.io.File;
import java.io.IOException;
import shoddybattle.ModData;
import netbattle.messages.UserListMessage;
import netbattle.messages.SuccessMessage;
import netbattle.database.Metaserver;

/**
 * This class allows a bot to control an instance of the client, rather than
 * a human.
 * 
 * @see HumanClient
 * @author Colin
 */
public class BotClient extends ServerLink {

    /** Creates a new instance of BotClient */
    public BotClient() {
        /** We need a no-arg constructor so that we can extend this class from
         *  a Rhino javascript script. */
    }
    
    /**
     * This method is intended to allow the bot to initialise.
     * @param data the mod data for this server
     * @param msg list of users on the server
     */
    public void initialise(ModData data, UserListMessage msg) {
        
    }
    
    /**
     * Returns the default host used by this bot.
     */
    public String getDefaultHost() {
        return null;
    }
    
    /**
     * Returns the default port used by this bot.
     */
    public int getDefaultPort() {
        return 0;
    }
    
    /**
     * Returns the default user name used by this bot.
     */
    public String getDefaultUser() {
        return null;
    }
    
    /**
     * Returns the password for the default user name used by this bot.
     */
    public String getDefaultPassword() {
        return null;
    }
    
    /**
     * <p>Allows for starting a bot from the command line. Try
     * 
     * <code>java -cp ShoddyBattleClient.jar:. shoddybattleclient.BotClient CLASS [HOST PORT USER PASSWORD]</code>
     * 
     * <p>where CLASS is the name of the bot class, HOST is the server to
     * connect to, PORT is the port on which the server is running, USER is the
     * user account to log in the bot under, and PASSWORD is the password to
     * said user account.
     * 
     * <p>If the optional parameters are not passed then the defaults are
     * obtained by calling the suitable methods on the BotClient object.
     */
    public static void main(String[] args) {
        if ((args.length != 5) && (args.length != 1)) {
            System.out.println("Usage: java -cp ShoddyBattleClient.jar:. "
                    + "shoddybattleclient.BotClient CLASS [HOST PORT USER PASSWORD]");
            return;
        }
        
        Class c;
        try {
            c = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + args[0]);
            System.exit(0);
            return;
        }
        
        Object o;
        try {
            o = c.newInstance();
        } catch (InstantiationException e) {
            System.out.println("Could not instantiate class.");
            System.exit(0);
            return;
        } catch (IllegalAccessException e) {
            System.out.println("No public access to class.");
            System.exit(0);
            return;
        }
        
        BotClient client;
        try {
            client = (BotClient)o;
        } catch (ClassCastException e) {
            System.out.println("Class does not extend BotClient.");
            System.exit(0);
            return;
        }
        
        boolean defaults = (args.length == 1);
        String host = defaults ? client.getDefaultHost() : args[1];
        int port = defaults ? client.getDefaultPort() : Integer.parseInt(args[2]);
        try {
            client.connect(host, port);
        } catch (IOException e) {
            System.out.println("Could not connect to the specified server.");
            e.printStackTrace();
            System.exit(0);
            return;
        }
        
        try {
            /** The server is supposed to send a welcome message first, but we
             *  don't care what it says. */
            client.getNextMessage();
            
            int version = Metaserver.getServerVersion(host, port);
            if (version > 2) {
                SuccessMessage msg = (SuccessMessage)client.getNextMessage();
                if (msg.isCaptchaRequired()) {
                    System.out.println("A CAPTCHA is required from your "
                            + "IP because of too many failed log on attempts. "
                            + "Wait five minutes and try again.");
                    System.exit(0);
                }
            }
            
            // Now log on.
            client.joinServer(defaults ? client.getDefaultUser() : args[3],
                    defaults ? client.getDefaultPassword() : args[4],
                    null);
            
            // Retrieve the success message.
            SuccessMessage msg = (SuccessMessage)client.getNextMessage();
            if (!msg.getSuccess()) {
                System.out.println(msg.getMessageText());
                System.exit(0);
                return;
            }
            
            // Download the mod data to a temp file.
            File f = File.createTempFile(".shoddybattle", "temp");
            client.receiveFile(f);
            ModData data = new ModData(f);
            f.delete();
            
            // Receive the user list message.
            UserListMessage users = (UserListMessage)client.getNextMessage();
            
            // The rest is up to the bot.
            client.start();
            client.initialise(data, users);
            
        } catch (IOException e) {
            System.out.println("Could not log on to the server.");
            System.exit(0);
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("Server sent unknown message.");
            System.exit(0);
            return;
        }
        
    }

}
