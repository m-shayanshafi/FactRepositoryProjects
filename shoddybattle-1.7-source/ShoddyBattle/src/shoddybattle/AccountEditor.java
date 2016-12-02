/*
 * AccountEditor.java
 *
 * Created on July 7, 2007, 2:11 AM
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

package shoddybattle;
import java.security.*;
import java.io.*;
import java.util.*;
import netbattle.database.registry.AccountRegistry;

/**
 * <p>This class offers a trivial application for creating a new user account
 * with a specified user level. It should be invoked as follows:
 * 
 * <pre>java -cp dist/ShoddyBattle.jar shoddybattle.AccountEditor [PROPERTIES] USER PASSWORD LEVEL</pre>
 * 
 * <p>where PROPERTIES is the server properties file to load, USER is the
 * name of the user account to create, PASSWORD is the password (in plain
 * text) to give the user account, and LEVEL is the user level (0, 1, or 2)
 * to assign to the account. If a properties file is not provided then a
 * default of <code>server.properties</code> is presumed.
 * 
 * <p>In the future the functionality of this class should be expanded. At
 * present, the main use of this class is for the installer to create to create
 * an administrator account for the server operator.
 * 
 * @author Colin
 */
public class AccountEditor {

    /**
     * Return an SHA-1 hash of a password. This method is not especially
     * secure as it could potentially keep the password in memory for quite
     * some time.
     */
    public static String getHash(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(password.getBytes("ISO-8859-1"));
        return new String(hash, "ISO-8859-1");
    }
    
    /**
     * Invoke the account editor.
     */
    public static void main(String[] args) {
        String properties = "server.properties";
        String user, password, strLevel;
        if (args.length == 4) {
            properties = args[0];
            user = args[1];
            password = args[2];
            strLevel = args[3];
        } else if (args.length == 3) {
            user = args[0];
            password = args[1];
            strLevel = args[2];
        } else {
            System.err.println("AccountEditor [PROPERTIES] USER PASSWORD LEVEL");
            return;
        }
        
        Properties props = Main.getProperties(properties);
        if (props == null) {
            System.err.println("Could not read the properties file.");
            return;
        }
        if (!Main.initialiseAccountRegistry(props)) {
            System.err.println("Failed to initialise the account registry.");
            return;
        }
        
        int level = Integer.parseInt(strLevel);
        if ((level < 0) || (level > 2)) {
            System.err.println("AccountEditor: level must be one of 0 (user), 1 (moderator), or 2 (admin)");
            return;
        }
        
        AccountRegistry registry = AccountRegistry.getInstance();
        try {
            if (!registry.addNewUser(user, getHash(password))) {
                System.out.println("User already exists; password will not be modified.");
            } else {
                System.out.println("User \"" + user + "\" created successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        registry.setUserLevel(user, level);
        System.out.println("User level set successfully.");
        registry.close();
        
        System.exit(0);
    }

}
