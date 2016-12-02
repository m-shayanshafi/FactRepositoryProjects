/*
 * Main.java
 *
 * Created on December 13, 2006, 5:35 PM
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

package shoddybattle;

import mechanics.*;
import mechanics.moves.*;
import netbattle.*;
import netbattle.database.*;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.io.FileInputStream;
import mechanics.intelligence.Situation;
import netbattle.database.registry.*;
import java.util.zip.GZIPOutputStream;
import java.util.prefs.Preferences;

/**
 *
 * @author Colin
 */
public class Main {
    
    private static File m_working;
    
    /**
     * Get the working directory of the program.
     * @param File the working directory
     */
    public static File getWorkingDirectory() {
        return m_working;
    }
    
    /**
     * A GZIPOutputStream that remembers the total number of bytes that have
     * been written to it.
     */
    private static class LoggingGZIPOutputStream extends GZIPOutputStream {
        private long m_written = 0;
        
        public LoggingGZIPOutputStream(FileOutputStream f) throws IOException {
            super(f);
        }
        
        /**
         * Write bytes to the stream.
         */
        public void write(byte[] buf, int off, int len) throws IOException {
            m_written += len;
            super.write(buf, off, len);
        }
        
        /**
         * Return the number of bytes written to the stream.
         */
        public long getBytesWritten() {
            return m_written;
        }
    }
    
    /**
     * Simple class that checks for updates from shoddybattle.com when
     * the server is in the process of starting up. If the file on the web site
     * is found to be newer than the version in $working/dist/ then the latter
     * is replaced by the former. The updater seamlessly replaces the files
     * before the start up process completes.
     */
    private static class AutoUpdater {
        private static final URL m_url;
        static {
            try {
                m_url = new URL("http://shoddybattle.com/server/ShoddyBattle.jar");
            } catch (MalformedURLException e) {
                throw new InternalError();
            }
        }
        public static void downloadUpdate(File f) {
            try {
                InputStream stream = m_url.openStream();
                FileOutputStream output = new FileOutputStream(f);
                byte[] data = new byte[255];
                int offset = 0;
                while (true) {
                    int read = stream.read(data, offset, data.length);
                    output.write(data, 0, read);
                    if (read == -1)
                        break;
                }
                output.flush();
                output.close();
                stream.close();
            } catch (Exception e) {
                
            }
        }
        public static boolean isUpdateAvailable() {
            long present = new File("dist", "ShoddyBattle.jar").lastModified();
            Preferences user = Preferences.userRoot();
            present = user.getLong("shoddybattle.timestamp", present);
            try {
                URLConnection connection = m_url.openConnection();
                connection.connect();
                long modified = connection.getLastModified();
                if (modified > present) {
                    user.putLong("shoddybattle.timestamp", modified);
                    return true;
                }
            } catch (IOException e) {
                
            }
            return false;
        }
    }
    
    /**
     * Initialise the default ModData from database files.
     */
    public static void initialise(Properties props) {
        String parent = props.getProperty("filesystem.parent");
        
        System.out.println("Loading pokemon database...");
        try {
            PokemonSpecies.getDefaultData().loadSpeciesDatabase(
                    new File(parent, props.getProperty("metagame.species", "species.db")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Loading move types for the Jewel generation...");
        try {
            JewelMechanics.loadMoveTypes(
                    new File(parent,
                    props.getProperty("server.movetypes", "movetypes.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /** Read move sets in from a file. */
        MoveSetData moveSetData = MoveSet.getDefaultData();
        moveSetData.loadFromFile(new File(parent, props.getProperty("metagame.moveset")).toString());   
        
        /**
         * Initialise hold items, via this obfuscated maneuver.
         * The line causes HoldItem's static code to be run. This will not
         * happen otherwise, since HoldItem is not referenced anywhere (or at
         * at least it doesn't need to be yet). Do not remove this line.
         */
        System.out.println("Initialising hold items...");
        Class c = mechanics.statuses.items.HoldItem.class;
    }
    
    /**
     * Initialise the user accounts registry.
     */
    public static boolean initialiseAccountRegistry(Properties props) {
        if (!props.containsKey("server.use_database")) {
            System.out.println("Error: server.use_database not set.");
            System.out.println("Please update your properties file.");
            return false;
        }
        
        if (props.getProperty("server.use_database").toLowerCase().equals("yes")) {
            System.out.println("Connecting to database...");
            DatabaseImpl registry = new DatabaseImpl();
            if (!registry.connect(
                    props.getProperty("server.database_host"),
                    props.getProperty("server.database"),
                    props.getProperty("server.database_user"),
                    props.getProperty("server.database_password", ""))) {
                return false;
            }
             AccountRegistry.setInstance(registry);
        } else {
            AccountRegistry.setInstance(new FileImpl(
                    new File(props.getProperty("filesystem.parent"),
                        props.getProperty("server.flat_file")).toString(),
                    Integer.valueOf(props.getProperty("server.backup_delta")).intValue()));
        }
        return true;
    }
    
    /**
     * Read the properties from a file.
     * @param file file to read properties from
     */
    public static Properties getProperties(String file) {
        /**
         * Load the properties from a file passed on the command line.
         */
        System.out.println("Loading properties file...");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(file));
            System.out.println("Loading metagame definition file...");
            
            File f = new File(file);
            String parent = f.getAbsoluteFile().getParent();
            props.put("filesystem.parent", parent);
            f = new File(parent, props.getProperty("server.metagame"));
            
            props.load(new FileInputStream(f));
            return props;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Shoddy Battle (June 2007) - http://shoddybattle.com");
        System.out.println("  By Colin James Fitzpatrick and Benjamin Gwin.");
        System.out.println("  This program is free software.");
        System.out.println("  Licensed under the GNU General Public Licence. No warranty.");
        System.out.println();
        
        String file = "server.properties";
        
        if (args.length == 1) {
            file = args[0];
        } else if (args.length != 0) {
            System.out.println("Invalid command line!");
            return;
        }

        // Find working directory.
        m_working = new File(file).getAbsoluteFile().getParentFile();
        
        Properties props = getProperties(file);
        if (props == null) {
            return;
        }
        
        if (props.getProperty("server.auto_update", "yes").equalsIgnoreCase("yes")) {
            System.out.println("Checking for update...");
            if (AutoUpdater.isUpdateAvailable()) {
                System.out.println("Downloading update...");
                File f = new File("dist", "ShoddyBattleU.jar");
                if (f.exists()) {
                    f.delete();
                }
                AutoUpdater.downloadUpdate(f);
                String command = "sh update.sh";
                if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
                    command = "cmd /c update.bat";
                }
                try {
                    Runtime.getRuntime().exec(command);
                } catch (IOException e) {
                    
                }
                System.exit(0);
            }
        }
        
        String parent = props.getProperty("filesystem.parent");
        
        initialise(props);
        initialiseAccountRegistry(props);
        
        System.out.println("Initialising random number generator...");
        Class c = BattleMechanics.class;
        
        System.out.println("Loading battle mechanics...");
        Class mechanics;
        try {
            mechanics = Class.forName(props.getProperty("metagame.mechanics"));
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find mechanics class chosen in properties file.");
            return;
        }
        
        System.out.println("Creating server...");
        final BattleServer server = new BattleServer(
                props.getProperty("server.name"),
                props.getProperty("server.description"),
                props.getProperty("server.unique_name"),
                Integer.valueOf(props.getProperty("server.port")).intValue(),
                Integer.valueOf(props.getProperty("server.capacity")).intValue(),
                mechanics
            );
        
        System.out.println("Adding shut down hook...");
        Runtime.getRuntime().addShutdownHook(new Thread(
            new Runnable() {
                    public void run() {
                        System.out.println("Shutting down...");
                        server.removeFromMetaserver();
                        AccountRegistry registry = AccountRegistry.getInstance();
                        if (registry != null) {
                            registry.close();
                        }
                        Situation.getInstance().saveToFile();
                    }
                }
            ));
        
        ModData data = ModData.getDefaultData();
        String patch = props.getProperty("server.patch", null);
        if ((patch != null) && (patch.length() != 0)) {
            System.out.println("Applying patch file to database...");
            try {
                FileInputStream in = new FileInputStream(new File(parent, patch));
                data.applyPatch(in);
                in.close();
            } catch (IOException e) {
                System.out.println("Patch file error: " + e.getMessage());
                System.exit(0);
            }
        }
        
        MoveSetData moveSetData = MoveSet.getDefaultData();
        moveSetData.pruneMoveSet();
        
        System.out.println("Saving mod data for download by client...");
        try {
            LoggingGZIPOutputStream output = new LoggingGZIPOutputStream(
                    new FileOutputStream(new File(parent, ModData.MOD_DATA_FILE.toString())));
            data.saveModData(output);
            output.close();
            data.setModDataLength(output.getBytesWritten());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Caching move sets...");
        PokemonSpecies.getDefaultData().cacheMoveSets(
                MoveList.getDefaultData(), moveSetData, true);
        
        System.out.println("Running server...");
        server.run();
    }
    
}
