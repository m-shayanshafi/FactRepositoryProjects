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
package pl.org.minions.stigma.server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.license.LicenseInfoPanel;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing system tray icon for managing server.
 */
public final class ServerTray
{

    private static class Closer implements ServerObserver
    {
        /** {@inheritDoc} */
        @Override
        public void stateChanged()
        {
            if (Server.globalInstance().isStopped())
            {
                try
                {
                    Thread.sleep(SLEEP_BEFORE_KILL);
                }
                catch (InterruptedException e1)
                {
                    Log.logger.error("This sleep shouldn't been interrupted");
                }
                System.exit(0);
            }
        }

    }

    private static class Restarter implements ServerObserver
    {
        private boolean fireRestart;

        public void restart()
        {
            if (Server.globalInstance().isWorking())
            {
                fireRestart = true;
                Server.globalInstance().stop(true);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void stateChanged()
        {
            if (fireRestart && Server.globalInstance().isStopped())
            {
                fireRestart = false;
                Server.globalInstance().start();
            }
        }
    }

    private static class TrayAppender extends AppenderSkeleton
    {
        private TrayIcon trayIcon;

        public TrayAppender(TrayIcon trayIcon)
        {
            this.trayIcon = trayIcon;
        }

        /** {@inheritDoc} */
        @Override
        protected void append(LoggingEvent arg0)
        {
            if (trayIcon == null)
                return;

            TrayIcon.MessageType msgType;
            switch (arg0.getLevel().toInt())
            {
                case Priority.FATAL_INT:
                case Priority.ERROR_INT:
                    msgType = TrayIcon.MessageType.ERROR;
                    break;
                case Priority.INFO_INT:
                    msgType = TrayIcon.MessageType.INFO;
                    break;
                case Priority.WARN_INT:
                    msgType = TrayIcon.MessageType.WARNING;
                    break;
                default:
                    return;
            }

            trayIcon.displayMessage(STIGMA_SERVER_TOOLTIP,
                                    arg0.getMessage().toString(),
                                    msgType);
        }

        /** {@inheritDoc} */
        @Override
        public void close()
        {
            trayIcon = null;
        }

        /** {@inheritDoc} */
        @Override
        public boolean requiresLayout()
        {
            return false;
        }

    }

    @Translated
    private static String KILL_TXT = "Kill";
    @Translated
    private static String RESTART_SERVER_TXT = "Restart server";
    @Translated
    private static String STOP_SERVER_TXT = "Stop server";
    @Translated
    private static String KILL_SERVER_TXT = "Kill server";

    @Translated
    private static String START_SERVER_TXT = "Start server";
    @Translated(name = "EXIT")
    private static String EXIT_TXT = "Exit";
    @Translated(name = "ABOUT")
    private static String ABOUT_TXT = "About";

    private static final String PATH_TO_ICON =
            "img/default/stigma_icon_64x64.png";

    private static final int SLEEP_BEFORE_KILL = 500;

    private static final String STIGMA_SERVER_TOOLTIP =
            "Stigma Server (v. " + Version.FULL_VERSION + ")";

    private ServerTray()
    {
    }

    /**
     * Creates system icon tray icon for managing given
     * server.
     */
    public static void create()
    {
        if (SystemTray.isSupported())
        {
            Image image = Resourcer.loadImage(PATH_TO_ICON);

            final PopupMenu popup = new PopupMenu();

            final TrayIcon trayIcon =
                    new TrayIcon(image, STIGMA_SERVER_TOOLTIP, popup);

            final MenuItem startMenuItem = new MenuItem(START_SERVER_TXT);
            startMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Server server = Server.globalInstance();
                    if (!server.isWorking())
                        server.start();
                }
            });
            popup.add(startMenuItem);

            final MenuItem stopMenuItem = new MenuItem(STOP_SERVER_TXT);
            stopMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Server server = Server.globalInstance();
                    if (server.isWorking())
                        server.stop(true);
                }
            });
            popup.add(stopMenuItem);

            final MenuItem killMenuItem = new MenuItem(KILL_SERVER_TXT);
            killMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Server server = Server.globalInstance();
                    if (server.isWorking())
                        server.stop(false);
                }
            });
            popup.add(killMenuItem);

            final MenuItem restartMenuItem = new MenuItem(RESTART_SERVER_TXT);

            restartMenuItem.addActionListener(new ActionListener()
            {
                private Restarter restarter;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (restarter == null)
                    {
                        restarter = new Restarter();
                        Server.globalInstance().addObserver(restarter);
                    }
                    restarter.restart();
                }
            });
            popup.add(restartMenuItem);

            popup.addSeparator();

            MenuItem mi = new MenuItem(ABOUT_TXT);
            mi.addActionListener(new ActionListener()
            {
                private LicenseInfoPanel panel;
                private JFrame frame;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (panel == null)
                    {
                        panel = new LicenseInfoPanel();
                        frame = panel.createFrame();
                    }
                    panel.rewind();
                    frame.setVisible(true);
                }
            });

            popup.add(mi);

            mi = new MenuItem(KILL_TXT);
            mi.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Log.logger.fatal("Server killed");
                    System.exit(-1);
                }
            });

            popup.add(mi);

            mi = new MenuItem(EXIT_TXT);
            mi.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Server server = Server.globalInstance();
                    ServerObserver ob = new Closer();
                    server.addObserver(ob);
                    if (server.isWorking())
                        server.stop(true);
                    else
                        ob.stateChanged();
                }
            });
            popup.add(mi);

            trayIcon.setImageAutoSize(true);

            ServerObserver ob = new ServerObserver()
            {
                @Override
                public void stateChanged()
                {
                    Server server = Server.globalInstance();

                    startMenuItem.setEnabled(server.isStopped());
                    stopMenuItem.setEnabled(server.isWorking());
                    killMenuItem.setEnabled(server.isWorking());
                    restartMenuItem.setEnabled(server.isWorking());
                }
            };
            ob.stateChanged(); // initial update
            Server.globalInstance().addObserver(ob);

            SystemTray tray = SystemTray.getSystemTray();
            if (tray != null)
            {
                try
                {
                    tray.add(trayIcon);
                    Log.logger.addAppender(new TrayAppender(trayIcon));
                    Log.logger.debug("Tray icon added");
                }
                catch (AWTException e1)
                {
                    Log.logger.error("Couldn't add icon to tray: " + e1);
                }
            }
            else
            {
                Log.logger.error("Null SystemTray");
            }
        }
        else
        {
            Log.logger.info("SystemTray not supported");
        }
    }
}
