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
package pl.org.minions.stigma.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The listener interface for receiving field change events.
 */
public abstract class FieldChangeListener implements
                                         ActionListener,
                                         CaretListener,
                                         ChangeListener
{
    /**
     * Invoked when field is changed.
     */
    public abstract void fieldChanged();

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        fieldChanged();
    }

    /** {@inheritDoc} */
    @Override
    public void caretUpdate(CaretEvent e)
    {
        fieldChanged();
    }

    /** {@inheritDoc} */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        fieldChanged();
    }

}
