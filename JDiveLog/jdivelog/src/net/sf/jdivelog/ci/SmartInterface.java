/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SmartInterface.java
 *
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
 *
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.ci;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.irsocket.IrSocket;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.SmartAdapter;
import net.sf.jdivelog.model.smart.DiveParser;
import net.sf.jdivelog.model.smart.SmartData;

/**
 * Description: Configuration dialog for Uwatec Smart computers.
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public class SmartInterface implements ComputerInterface
{
    private static final Logger LOGGER =
        Logger.getLogger(SmartInterface.class.getName());

    public static enum PROPERTY
    {
        COMPUTER_MODEL ("smart.computermodel");

        private final String label;

        PROPERTY (String label)
        {
            this.label = label;
        }

        public String toString ()
        {
            return label;
        }
    }

    private IrSocket socket = null;
    private InputStream is = null;
    private OutputStream os = null;
    private TreeSet<JDive> jdives = null;
    private JPanel configurationPanel = null;
    private JComboBox modelList = null;

    /**
     * Connect to the dive computer.
     *
     * @throws IOException
     * @throws UnsupportedOSException
     */
    private void connect () throws IOException, UnsupportedOSException
    {
        socket = new IrSocket (1);
        is = socket.getInputStream ();
        os = socket.getOutputStream ();
    }

    /**
     * Disconnect from the dive computer.
     *
     * @throws IOException
     */
    private void disconnect () throws IOException
    {
        if (socket != null) {
            socket.close ();
            socket = null;
            is = null;
            os = null;
        }
    }

    /**
     * Get the computer model
     *
     * @return model
     */
     private SmartComputerType getComputerModel ()
     {
         return (SmartComputerType) getComputerModelList ().getSelectedItem ();
     }

     /**
      * Get a combo box with all Uwatec Smart computer models.
      *
      * @return model list
      */
     private JComboBox getComputerModelList ()
     {
         if (modelList == null) {
             List <SmartComputerType> names =
                 new LinkedList <SmartComputerType> ();

             for (SmartComputerType type : SmartComputerType.values ()) {
                 names.add (type);
             }
             modelList = new JComboBox (names.toArray ());
         }
         return modelList;
     }

     /**
      * Get an enum describing the computer model type.
      *
      * @param identifier internal identifier for the dive computer model
      *
      * @return computer model
      */
     public static SmartComputerType getComputerType (int identifier)
     {
         SmartComputerType result = null;

         for (SmartComputerType e : SmartComputerType.values ()) {
             if (e.getIdentifier () == identifier) {
                 result = e;
                 break;
             }
         }
         return result;
     }

    /**
     * Return the panel to configure the dive computer
     *
     * @return ConfigurationPanel
     */
    public synchronized JPanel getConfigurationPanel ()
    {
        if (configurationPanel == null) {
            configurationPanel = new JPanel ();

            JLabel labelComputermodel =
                new JLabel (Messages.getString ("aladin.computermodel"));
            configurationPanel.setLayout (new GridBagLayout ());

            GridBagConstraints gc = new GridBagConstraints ();

            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            configurationPanel.add (labelComputermodel, gc);
            gc.gridx = 1;
            configurationPanel.add (getComputerModelList (), gc);
        }
        return configurationPanel;
    }

    /**
     * @return the transferred dives.
     */
    public TreeSet <JDive> getDives ()
    {
        return jdives;
    }

    /**
     * Return the name of the driver used
     *
     * @return driver name
     */
    public String getDriverName ()
    {
        return "Uwatec Smart Computer";
    }

    /**
     * Return the properties to use
     *
     * @return PROPERTIES
     */
    public String [] getPropertyNames ()
    {
        List <String> result = new LinkedList <String> ();

        for (PROPERTY property : PROPERTY.values ()) {
            result.add (property.toString ());
        }
        return result.toArray (new String [0]);
    }

    /**
     * Initialization of the interface with the configuration properties
     *
     * @param properties
     */
    public void initialize (Properties properties)
    {
        if (properties != null) {
            try {
                String model = properties.getProperty
                    (PROPERTY.COMPUTER_MODEL.toString ());

                if (model != null) {
                    setComputerModel
                        (getComputerType (Integer.parseInt (model)));
                }
            }
            catch (NumberFormatException e) {
            }
        }
    }

    private int read (StatusInterface status) throws IOException
    {
    	status.commReceive ();
    	return is.read ();
    }

    /**
     * Return the configuration of the interface with the computer
     *
     * @return Properties
     */
    public Properties saveConfiguration ()
    {
        Properties result = new Properties ();

        result.setProperty
            (PROPERTY.COMPUTER_MODEL.toString (),
             String.valueOf (getComputerModel ().getIdentifier ()));
        return result;
    }

    /**
     * Save the downloaded data to /tmp for debugging.
     *
     * @param file file in /tmp
     * @param data bytes to save
     *
     * @throws IOException
     */
    private void saveDataFile (File file, byte data []) throws IOException
    {
        FileOutputStream fileoutputstream = new FileOutputStream (file);

        fileoutputstream.write (data);
        fileoutputstream.close ();
    }

    /**
     * Set the computer model
     *
     * @param model
     */
    private void setComputerModel (SmartComputerType model)
    {
        getComputerModelList ().setSelectedIndex (model.ordinal ());
        getConfigurationPanel ().revalidate ();
    }

    /**
     * Transfer the data from the dive computer
     *
     * @param status
     * @param logbook
     * @throws TransferException
     * @throws NotInitializedException
     * @throws InvalidConfigurationException
     */
    public void transfer (StatusInterface status, JDiveLog logbook)
        throws TransferException, NotInitializedException,
               InvalidConfigurationException
    {
        try {
            status.messageInfo (Messages.getString ("aladin.initializing"));

            connect ();

            // handshake
            byte response [] = new byte [4];

            write (new byte [] {0x1b}, status);
            response [0] = (byte) read (status);
            write (new byte [] {0x1c, 0x10, 0x27, 0, 0}, status);
            response [1] = (byte) read (status);
            if ((response [0] != 1) && (response [1] != 1)) {
                throw new IOException ("download failed");
            }

            // get dive computer type
            write (new byte [] {0x10}, status);

            SmartComputerType computerType = getComputerType (read (status));

            // get the number of byte of data to be returned by dive computer
            write (new byte []
                {(byte) 0xc6, 0, 0, 0, 0, 0x10, 0x27, 0x00, 0x00}, status);
            response [0] = (byte) read (status);
            response [1] = (byte) read (status);
            response [2] = (byte) read (status);
            response [3] = (byte) read (status);

            long length = DiveParser.ByteToU32 (response);
            
            status.countingProgressbarStart ((int) length, true);

            // get the dive data
            byte read_data [] = new byte [(int) length];

            status.messageInfo (Messages.getString ("aladin.reading"));

            write (new byte []
                {(byte) 0xc4, 0, 0, 0, 0, 0x10, 0x27, 0x00, 0x00}, status);
            for (int j = 0; (long) j < length; j++) {
                status.countingProgressbarIncrement ();
                read_data [j] = (byte) read (status);
            }

            // save Smart log file to disk
            java.io.File file = new java.io.File
                (System.getProperty ("java.io.tmpdir"), "smart.data");

            saveDataFile (file, read_data);

            // create the dive objects
            jdives = new SmartAdapter
                (new SmartData (computerType, read_data));
        }
        catch (IOException e) {
            LOGGER.log (Level.SEVERE, "transfer failed, IOException", e);
            throw new TransferException
                (Messages.getString ("aladin.ioexception"));
        }
        catch (UnsupportedOSException e) {
            throw new TransferException ("operating system not supported");
        }
        finally {
            try {
                disconnect ();
            }
            catch (IOException e) {
            }
            status.countingProgressbarEnd ();
            status.messageClear ();
        }
    }

    private void write (byte [] bytes, StatusInterface status) throws IOException
    {
    	status.commSend ();
    	os.write (bytes);
    }
}
