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
package pl.org.minions.stigma.client.ui.swing.game.components.actors;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import pl.org.minions.stigma.client.ui.swing.game.actions.actors.ActorAction;
import pl.org.minions.stigma.client.ui.swing.game.models.ActorTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ActorTableModel;
import pl.org.minions.stigma.client.ui.swing.game.tooltips.ActorToolTip;
import pl.org.minions.stigma.game.actor.Actor;

/**
 * Table for displaying list of actors.
 */
public class ActorTable extends JTable
{
    private final class ActorTableMouseListener extends MouseAdapter
    {
        /** {@inheritDoc} */
        @Override
        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        /** {@inheritDoc} */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        /** {@inheritDoc} */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
            {
                if (ActorTable.this.actions.length == 0)
                    return;

                final ActorAction action = ActorTable.this.actions[0];

                if (action == null)
                    return;

                e.consume();

                final int row = rowAtPoint(ActorTable.this.getMousePosition());

                if (row < 0)
                    return;

                final Actor actor =
                        ((ActorTableModel) ActorTable.this.getModel()).getActor(row);

                action.setActor(actor);

                if (action.isEnabled())
                    action.actionPerformed(null);
            }
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                if (actions.length == 0)
                    return;

                final int row = rowAtPoint(ActorTable.this.getMousePosition());

                if (row < 0)
                    return;

                final Actor actor =
                        ((ActorTableModel) ActorTable.this.getModel()).getActor(row);

                for (ActorAction action : ActorTable.this.actions)
                    if (action != null)
                        action.setActor(actor);

                itemPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private static final long serialVersionUID = 1L;
    private ActorAction[] actions;
    private JPopupMenu itemPopupMenu = new JPopupMenu();

    /**
     * Constructor.
     * @param dm
     *            table model
     * @param cm
     *            column model
     */
    public ActorTable(ActorTableModel dm, ActorTableColumnModel cm)
    {
        super(dm, cm);
        setShowVerticalLines(false);

        setTableHeader(null);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setColumnSelectionAllowed(false);

        addMouseListener(new ActorTableMouseListener());
    }

    /** {@inheritDoc} */
    @Override
    public JToolTip createToolTip()
    {
        final Point mousePosition = getMousePosition();
        if (mousePosition == null)
            return super.createToolTip();

        final int rowAtPoint = this.rowAtPoint(mousePosition);
        if (rowAtPoint < 0)
            return super.createToolTip();

        final int modelRowIndex = convertRowIndexToModel(rowAtPoint);
        if (modelRowIndex < 0)
            return super.createToolTip();

        final ActorTableModel model = (ActorTableModel) this.getModel();
        final JToolTip tip = new ActorToolTip(model.getActor(modelRowIndex));
        tip.setComponent(this);
        return tip;
    }

    /**
     * Sets pop-up menu actions.
     * @param actions
     *            enumeration of actions, <code>null</code>
     *            entries are converted to separators, first
     *            entry is default
     */
    public void setPopupActions(ActorAction... actions)
    {
        itemPopupMenu.removeAll();

        for (ActorAction action : actions)
        {
            if (action == null)
                itemPopupMenu.addSeparator();
            else
                itemPopupMenu.add(action);
        }

        this.actions = actions;
    }
}
