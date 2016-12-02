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
package pl.org.minions.stigma.editor.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.xml.Modifiable;

/**
 * Represents command for editing given document. It is
 * based on Command design pattern.
 * @param <DocumentType>
 *            type of document edited by this command
 */
public abstract class EditorCommand<DocumentType>
{
    private List<Modifiable> modifiedList = new LinkedList<Modifiable>();
    private ArrayList<Boolean> previousModified;

    /**
     * Return {@code true} when this command can be undone
     * and should be put in undo queue after execution. By
     * default: {@code true}.
     * @return whether or not this command should be added
     *         to undo queue after execution
     */
    public boolean canBeUndone()
    {
        return true;
    }

    /**
     * Returns {@code true} when execution of this command
     * should clear undo queue. By default equals to:
     * {@code modifies() && !canBeUndone()}.
     * @return whether or not undo queue should be cleared
     *         after execution of this command
     */
    public boolean clearsUndoQueue()
    {
        return modifies() && !canBeUndone();
    }

    /**
     * Executes this command in given context. Sets
     * {@link Modifiable#isModified()} flag in all modified
     * objects.
     * @param context
     *            context in which command should be
     *            executed and which it should modify if
     *            needed
     * @return {@code true} when execution was successful
     */
    public final boolean execute(DocumentType context)
    {
        modifiedList.clear();
        if (!innerExecute(context, modifiedList))
            return false;
        previousModified = new ArrayList<Boolean>(modifiedList.size());

        for (Modifiable m : modifiedList)
        {
            previousModified.add(m.isModified());
            m.setModified();
        }
        return true;
    }

    /**
     * Performs 'implementation specific' execute actions of
     * command.
     * @param context
     *            context in which this command should be
     *            executed
     * @param modifiedList
     *            list to which should be added every object
     *            modified by this command
     * @return {@code true} when execution was successful
     */
    protected abstract boolean innerExecute(DocumentType context,
                                            List<Modifiable> modifiedList);

    /**
     * Performs 'implementation specific' undo actions of
     * command.
     * @param context
     *            context in which this command should be
     *            executed
     * @return {@code true} when undo was successful
     */
    protected abstract boolean innerUndo(DocumentType context);

    /**
     * Returns {@code true} when this command modifies
     * anything and redo queue should be cleared after it's
     * execution. By default: {@code true}.
     * @return whether or not redo queue should be cleared
     *         after execution of this command
     */
    public boolean modifies()
    {
        return true;
    }

    /**
     * Undoes this command using given context. Removes
     * {@link Modifiable#isModified()} flag from every
     * object which was initially modified by
     * {@link #execute(Object)}.
     * @param context
     *            context in which command should be undone
     *            and which it should modify if needed
     * @return {@code true} when undoing was successful
     */
    public final boolean undo(DocumentType context)
    {
        assert previousModified != null;

        if (!innerUndo(context))
            return false;

        int i = 0;
        for (Modifiable m : modifiedList)
            if (!previousModified.get(i++).booleanValue())
                m.clearModified();

        return true;
    }
}
