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

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import pl.org.minions.stigma.client.ui.swing.game.ActorStatusPanel;
import pl.org.minions.stigma.client.ui.swing.game.models.ActorTableColumnModel;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.logger.Log;

/**
 * Cell renderer for stigma table which contains actors.
 */
public class ActorTableCellRenderer implements TableCellRenderer
{
    private ActorTableColumnModel.Column actorColumn;

    /**
     * Constructor.
     * @param column
     *            column id for which this renderer should
     *            be created
     */
    public ActorTableCellRenderer(ActorTableColumnModel.Column column)
    {
        super();
        this.actorColumn = column;
        assert this.actorColumn != null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        Actor a = (Actor) value;
        JComponent result = null;

        assert column >= 0 && column < table.getColumnCount();

        switch (actorColumn)
        {
            case Name:
                result = new ActorTableLabel(a.getName());
                break;
            case STATUS:
                result = new ActorStatusPanel(a)
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Overridden for performance purposes -
                     * see bug <a
                     * href=
                     * 
                     * "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6700748"
                     * >6700748</a> .
                     * @return {@code false}
                     */
                    @Override
                    public boolean isVisible()
                    {
                        return false;
                    }
                };
                result.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                result.setForeground(Color.RED);
                break;
            default:
                result = new ActorTableLabel("#ERROR");
                Log.logger.error("Unknown ActorTableModel column name.");
        }
        if (isSelected)
        {
            result.setOpaque(true);
            result.setBackground(Color.lightGray);
        }
        else
            result.setOpaque(false);

        result.setToolTipText(Integer.toString(row));

        return result;
    }
}
