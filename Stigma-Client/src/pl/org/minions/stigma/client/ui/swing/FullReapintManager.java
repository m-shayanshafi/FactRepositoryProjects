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

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 * Repaint manager that enforces full hierarchy repaint (on
 * given dirty region). This is necessary with non-opaque
 * components on translucent containers (because of some
 * artifacts left by Swing repaint optimization). Based on:
 * 
 * <pre>
 *  Swing Hacks
 *   Tips and Tools for Killer GUIs
 * By Joshua Marinacci, Chris Adamson
 *   First Edition June 2005
 *   Series: Hacks
 *   ISBN: 0-596-00907-0
 *   http://www.oreilly.com/catalog/swinghks/
 * </pre>
 */
public class FullReapintManager extends RepaintManager
{
    /** {@inheritDoc} */
    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
    {
        final Container parent = c.getParent();
        if (parent != null && parent instanceof JComponent)
            addDirtyRegion((JComponent) parent,
                           c.getX() + x,
                           c.getY() + y,
                           w,
                           h);
        else
            super.addDirtyRegion(c, x, y, w, h);
    }

    /**
     * Returns top component in component hierarchy.
     * @param comp
     *            component to get root for
     * @return top component in component hierarchy.
     */
    public JComponent getRootComponent(JComponent comp)
    {
        Container parent = comp.getParent();
        if (parent instanceof JComponent)
            return getRootComponent((JComponent) parent);
        return comp;
    }
}
