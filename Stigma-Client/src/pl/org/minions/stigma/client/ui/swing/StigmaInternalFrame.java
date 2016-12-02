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
package pl.org.minions.stigma.client.ui.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Our version of internal frame class - it sets few
 * JInternalFrame options by default: disables icon i left
 * upper corner of frame and disables popup menu.
 */
public class StigmaInternalFrame extends JInternalFrame
{
    /**
     * StigmaInternalFrame UID
     */
    private static final long serialVersionUID = 1321089193045917767L;

    /**
     * Class which represents icon stub with no graphics and
     * size.
     */
    private class EmptyIcon implements Icon
    {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            //no implementation
        }

        @Override
        public int getIconWidth()
        {
            return 0;
        }

        @Override
        public int getIconHeight()
        {
            return 0;
        }
    }

    @Translated
    private static String POPUP_MENU_MIMIMIZE = "Minimize";
    @Translated
    private static String POPUP_MENU_CLOSE = "Close";
    @Translated
    private static String POPUP_MENU_MAXIMIZE = "Maximize";
    @Translated
    private static String POPUP_MENU_RESTORE = "Restore";

    /**
     * Stigma version of JPopupMenu class.
     */
    public class StigmaPopupMenu extends JPopupMenu
    {
        /**
         * StigmaPopupMenu UID
         */
        private static final long serialVersionUID = -2374024525781373935L;

        private JMenuItem minimizeMenuItem;
        private JMenuItem maximizeMenuItem;
        private JMenuItem restoreMenuItem;
        private Separator separator;
        private JMenuItem closeMenuItem;

        /**
         * Default constructor.
         */
        public StigmaPopupMenu()
        {
            super();
            updateMenuItems();
        }

        /**
         * Updates menu positions of popup menu.
         */
        public void updateMenuItems()
        {
            //RESTORE
            if (isIconifiable() || isMaximizable())
            {
                if (restoreMenuItem == null)
                {
                    restoreMenuItem = new JMenuItem(POPUP_MENU_RESTORE);
                    restoreMenuItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            restore();

                        }
                    });
                    //added to make sure that restore is disabled ad the beginning
                    restoreMenuItem.setEnabled(isIcon() || isMaximum());
                    this.add(restoreMenuItem);
                }
            }
            else
            {
                if (restoreMenuItem != null)
                {
                    this.remove(restoreMenuItem);
                    restoreMenuItem = null;
                }
            }

            //MIMINIZE
            if (isIconifiable())
            {
                if (minimizeMenuItem == null)
                {
                    minimizeMenuItem = new JMenuItem(POPUP_MENU_MIMIMIZE);
                    minimizeMenuItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            minimize();
                        }
                    });
                    minimizeMenuItem.setEnabled(!isIcon());
                    this.add(minimizeMenuItem);
                }
            }
            else
            {
                if (minimizeMenuItem != null)
                {
                    this.remove(minimizeMenuItem);
                    minimizeMenuItem = null;
                }
            }

            //MAXIMIZE
            if (isMaximizable())
            {
                if (maximizeMenuItem == null)
                {
                    maximizeMenuItem = new JMenuItem(POPUP_MENU_MAXIMIZE);
                    maximizeMenuItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            maximize();

                        }
                    });
                    maximizeMenuItem.setEnabled(!isMaximum());
                    this.add(maximizeMenuItem);
                }
            }
            else
            {
                if (maximizeMenuItem != null)
                {
                    this.remove(maximizeMenuItem);
                    maximizeMenuItem = null;
                }
            }

            //SEPARATOR
            if (isMaximizable() || isIconifiable())
            {
                if (separator == null)
                {
                    separator = new Separator();
                    this.add(separator);
                }
            }
            else
            {
                if (separator != null)
                {
                    this.remove(separator);
                    this.separator = null;
                }

            }

            //CLOSE
            if (closeMenuItem == null)
            {
                closeMenuItem = new JMenuItem(POPUP_MENU_CLOSE);
                closeMenuItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        close();

                    }
                });
                this.add(closeMenuItem);
            }

            closeMenuItem.setEnabled(isClosable());
        }

        private void minimize()
        {
            try
            {
                if (!isIcon())
                    setIcon(true);
            }
            catch (PropertyVetoException e)
            {
                Log.logger.error(e);
            }
        }

        private void maximize()
        {
            try
            {
                if (isIcon())
                    setIcon(false);
                if (!isMaximum())
                    setMaximum(true);
            }
            catch (PropertyVetoException e)
            {
                Log.logger.error(e);
            }
        }

        private void close()
        {
            doDefaultCloseAction();
        }

        private void restore()
        {
            try
            {
                if (isIcon())
                {
                    setIcon(false);
                }
                else if (isMaximum())
                {
                    setMaximum(false);
                }
            }
            catch (PropertyVetoException e)
            {
                Log.logger.error(e);
            }
        }

        /**
         * Sets enabled state of minimize menu item.
         * @param enabled
         *            state of item
         */
        public void setMinimizeEnabled(boolean enabled)
        {
            if (minimizeMenuItem != null)
                this.minimizeMenuItem.setEnabled(enabled);
        }

        /**
         * Sets enabled state of maximize menu item.
         * @param enabled
         *            state of item
         */
        public void setMaximizeEnabled(boolean enabled)
        {
            if (maximizeMenuItem != null)
                this.maximizeMenuItem.setEnabled(enabled);
        }

        /**
         * Sets enabled state of restore menu item.
         * @param enabled
         *            state of item
         */
        public void setRestoreEnabled(boolean enabled)
        {
            if (restoreMenuItem != null)
                this.restoreMenuItem.setEnabled(enabled);
        }
    }

    private StigmaPopupMenu framePopupMenu;
    private Point defaultLocation = getLocation();

    /**
     * Default constructor.
     */
    public StigmaInternalFrame()
    {
        this("", false, false, false, false);
    }

    /**
     * Default constructor.
     * @param name
     *            name of frame
     */
    public StigmaInternalFrame(String name)
    {
        this(name, false, false, false, false);
    }

    /**
     * Default constructor.
     * @param name
     *            name of frame
     * @param resizeable
     *            can frame be resized
     */
    public StigmaInternalFrame(String name, boolean resizeable)
    {
        this(name, resizeable, false, false, false);
    }

    /**
     * Default constructor.
     * @param name
     *            name of frame
     * @param resizeable
     *            can frame be resized
     * @param closeable
     *            can frame be closed
     * @param maximizable
     *            can frame be maximized
     * @param iconificable
     *            can frame be iconified
     */
    public StigmaInternalFrame(String name,
                               boolean resizeable,
                               boolean closeable,
                               boolean maximizable,
                               boolean iconificable)
    {
        super(name, resizeable, closeable, maximizable, iconificable);
        setDefault();
    }

    private void setDefault()
    {
        this.setFrameIcon(new EmptyIcon());
        JDesktopIcon jdi = new JDesktopIcon(this);
        this.framePopupMenu = new StigmaPopupMenu();
        this.setComponentPopupMenu(framePopupMenu);
        jdi.setComponentPopupMenu(framePopupMenu);

        this.setDesktopIcon(jdi);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        this.addPropertyChangeListener(IS_ICON_PROPERTY,
                                       new PropertyChangeListener()
                                       {
                                           @Override
                                           public void propertyChange(PropertyChangeEvent evt)
                                           {
                                               if (framePopupMenu != null)
                                               {
                                                   if (isIcon())
                                                   {
                                                       framePopupMenu.setMinimizeEnabled(false);
                                                       framePopupMenu.setRestoreEnabled(true);
                                                   }
                                                   else
                                                   {
                                                       framePopupMenu.setMinimizeEnabled(true);
                                                       framePopupMenu.setRestoreEnabled(false);
                                                   }
                                               }
                                           }
                                       });
        this.addPropertyChangeListener(IS_MAXIMUM_PROPERTY,
                                       new PropertyChangeListener()
                                       {
                                           @Override
                                           public void propertyChange(PropertyChangeEvent evt)
                                           {
                                               if (framePopupMenu != null)
                                               {
                                                   if (isMaximum())
                                                   {
                                                       framePopupMenu.setMaximizeEnabled(false);
                                                       framePopupMenu.setRestoreEnabled(true);
                                                   }
                                                   else
                                                   {
                                                       framePopupMenu.setMaximizeEnabled(true);
                                                       framePopupMenu.setRestoreEnabled(false);
                                                   }
                                               }
                                           }
                                       });
    }

    /** {@inheritDoc} */
    @Override
    public void setIconifiable(boolean b)
    {
        super.setIconifiable(b);
        if (framePopupMenu != null)
        {
            framePopupMenu.updateMenuItems();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setMaximizable(boolean b)
    {
        super.setMaximizable(b);
        if (framePopupMenu != null)
        {
            framePopupMenu.updateMenuItems();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setClosable(boolean b)
    {
        super.setClosable(b);
        if (framePopupMenu != null)
        {
            framePopupMenu.updateMenuItems();
        }
    }

    /**
     * Returns defaultLocation.
     * @return defaultLocation
     */
    public Point getDefaultLocation()
    {
        return defaultLocation;
    }

    /**
     * Sets new value of defaultLocation.
     * @param defaultLocation
     *            the defaultLocation to set
     */
    public void setDefaultLocation(Point defaultLocation)
    {
        this.defaultLocation = defaultLocation;
        setLocation(defaultLocation);
    }

    /**
     * Sets new value of defaultLocation.
     * @param x
     *            x coordinate of new default location
     * @param y
     *            y coordinate of new default location
     */
    public void setDefaultLocation(int x, int y)
    {
        setDefaultLocation(new Point(x, y));
    }

    /**
     * Restores frame to its default location.
     * @see #setDefaultLocation(Point)
     */
    public void restoreDefaultLocation()
    {
        if (defaultLocation != null)
            setLocation(defaultLocation);
    }

}
