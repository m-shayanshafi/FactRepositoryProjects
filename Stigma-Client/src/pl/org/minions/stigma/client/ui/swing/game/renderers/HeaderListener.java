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
package pl.org.minions.stigma.client.ui.swing.game.renderers;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;
import javax.swing.table.JTableHeader;

import pl.org.minions.utils.logger.Log;

/**
 * Used to update the state if
 * {@link ButtonTableHeaderRenderer} in reaction to mouse
 * events happening over a table's header.
 */
public class HeaderListener implements MouseInputListener
{
    private JTableHeader header;

    private ButtonTableHeaderRenderer renderer;

    /**
     * Creates a header listener to notify a table header
     * renderer
     * about mouse events over the header.
     * @param header
     *            table header to observer
     * @param renderer
     *            table header renderer to notify
     */
    public HeaderListener(JTableHeader header,
                          ButtonTableHeaderRenderer renderer)
    {
        this.header = header;
        this.renderer = renderer;
    }

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e)
    {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setPressedColumn(col);
        header.repaint();

        Log.logger.debug("Pressed column " + col);
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        renderer.setPressedColumn(-1); // clear
        header.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setEnteredColumn(col);
        header.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseExited(MouseEvent e)
    {
        renderer.setEnteredColumn(-1);
        header.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setEnteredColumn(col);
        header.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setEnteredColumn(col);
        renderer.setPressedColumn(col);
        header.repaint();

    }
}
