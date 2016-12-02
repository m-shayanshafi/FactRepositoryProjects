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
package pl.org.minions.stigma.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.zip.ZipException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

import pl.org.minions.stigma.client.ui.ClientSwingUI;
import pl.org.minions.stigma.client.ui.ClientUI;
import pl.org.minions.stigma.client.ui.swing.FullReapintManager;
import pl.org.minions.stigma.license.LicenseInfo;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.collections.CollectionFactory;
import pl.org.minions.utils.collections.CollectionFactory.CollectionType;
import pl.org.minions.utils.i18n.AllClassesLoader;
import pl.org.minions.utils.i18n.Translator;
import pl.org.minions.utils.i18n.TranslatorException;
import pl.org.minions.utils.logger.Log;

/**
 * Main stigma client application entry point. Can be
 * executed either as an applet or an application.
 */
public class Main extends JApplet
{
    private static final String PARAM_SERVER_PORT = "server-port";

    private static final String SERVER_URL_PREFIX = "--server-url=";

    private static final String AUTOLOGIN_FLAG = "--autologin";

    private static final long serialVersionUID = 1L;

    private static URL serverURL;

    private Client client;

    private boolean autologin;

    /**
     * Constructor.
     */
    public Main()
    {
    }

    static
    {
        Version.initialize("Stigma-Client");
        Log.initialize("log_config.properties");
        try
        {
            AllClassesLoader.standardLoadAll();
        }
        catch (ZipException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (SecurityException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (TranslatorException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (IOException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
    }

    /**
     * Sets new value of autologin.
     * @param autologin
     *            the autologin to set
     */
    private void setAutologin(boolean autologin)
    {
        this.autologin = autologin;
    }

    /**
     * For stand-alone mode, create a JFrame and add an
     * instance of this applet to it.
     * @param args
     *            application arguments
     */
    public static void main(String[] args)
    {
        boolean autologin = false;
        // Running as an application
        for (String arg : args)
        {
            if (arg.startsWith(SERVER_URL_PREFIX))
            {
                final String urlString =
                        arg.substring(SERVER_URL_PREFIX.length());
                try
                {

                    serverURL = new URL(urlString);
                }
                catch (MalformedURLException e)
                {
                    Log.logger.error("Bad URL: " + urlString + " error: " + e);
                    JOptionPane.showMessageDialog(null,
                                                  MessageFormat.format("URL:\n {0}\nError message:\n {1}",
                                                                       urlString,
                                                                       e.getLocalizedMessage()),
                                                  "Error: Bad URL!",
                                                  JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

            }
            else if (arg.equals(AUTOLOGIN_FLAG))
            {
                autologin = true;
            }
            else
                try
                {
                    if (!Translator.processArgument(Version.getAppName(), arg))
                    {
                        Log.logger.error("Unhandled program argument: '" + arg
                            + "'");
                    }
                }
                catch (FileNotFoundException e)
                {
                    Log.logger.fatal(e);
                    System.exit(1);
                }
                catch (IOException e)
                {
                    Log.logger.fatal(e);
                    System.exit(1);
                }
        }

        if (serverURL == null)
            try
            {
                serverURL = new URL("http://127.0.0.1");
            }
            catch (MalformedURLException e)
            {
                Log.logger.fatal(e);
                System.exit(1);
            }

        // Create Frame
        JFrame f = new JFrame("Stigma " + Version.SIMPLIFIED_VERSION);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create applet object
        Main applet = new Main();
        applet.setAutologin(autologin);

        // Add applet to the frame
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(applet);
        f.setResizable(false);
        f.setFocusable(false);

        f.pack();
        // Initialize the applet
        applet.init();
        f.pack();
        f.setVisible(true);
    } // main

    /** {@inheritDoc} */
    @Override
    public void destroy()
    {
        if (client != null)
            client.stop();
        super.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public String[][] getParameterInfo()
    {
        String[][] pinfo =
        {
        { PARAM_SERVER_PORT, "0-65535", "server tcp port" }, };

        return pinfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        Translator.globalInstance().translate();

        System.out.println(LicenseInfo.getLicenseText(LicenseInfo.Format.TEXT));

        Container container = this.getContentPane();

        CollectionFactory.init(CollectionType.ThreadSafe);

        if (serverURL == null)
            serverURL = getCodeBase();

        String serverPort = null;
        try
        {
            serverPort = getParameter(PARAM_SERVER_PORT);
        }
        catch (NullPointerException e)
        {
            /*
             * NullPointerException will be raised by
             * getParameter when application is run in
             * standalone mode (getParameer works only in
             * applet mode)
             */
            serverPort = null;
        }
        if (serverPort != null)
        {
            try
            {
                serverURL =
                        new URL(serverURL.getProtocol(),
                                serverURL.getHost(),
                                Integer.valueOf(serverPort),
                                serverURL.getFile());
            }
            catch (NumberFormatException e)
            {
                Log.logger.error("Specified port is not a number.", e);
                return;
            }
            catch (MalformedURLException e)
            {
                Log.logger.error("Error setting server tcp port.", e);
                return;
            }
        }

        client = new Client(serverURL);
        client.setUI(chooseUIImplementation(container));

        if (autologin)
        {
            client.getStateManager().setAutoLogin(true);
        }

        client.reconnect();
        client.start();

        this.setSize(container.getComponent(0).getSize());
    }

    private ClientUI chooseUIImplementation(Container container)
    {
        final ClientUI ui;

        final SynthLookAndFeel synth = new SynthLookAndFeel();
        final String synthConfigFileName = "synth/client/synth_cfg.xml";
        URL synthCfgURL =
                Main.class.getClassLoader().getResource(synthConfigFileName);
        try
        {
            if (synthCfgURL == null)
            {
                final File synthCfgFile = new File(synthConfigFileName);
                synthCfgURL = synthCfgFile.toURI().toURL();
            }

            synth.load(synthCfgURL);

            RepaintManager.setCurrentManager(new FullReapintManager());
            UIManager.setLookAndFeel(synth);
        }
        catch (MalformedURLException e)
        {
            Log.logger.fatal("Failed to load synth configuration file.", e);
            System.exit(1);
        }
        catch (UnsupportedLookAndFeelException e)
        {
            Log.logger.fatal("Failed to set look and feel to synth.", e);
            System.exit(1);
        }
        catch (ParseException e)
        {
            Log.logger.fatal("Error while parsing synth config file.", e);
            System.exit(1);
        }
        catch (IOException e)
        {
            Log.logger.fatal("Error while loading synth config file.", e);
            System.exit(1);
        }
        ui = new ClientSwingUI(container);

        return ui;
    }
}
