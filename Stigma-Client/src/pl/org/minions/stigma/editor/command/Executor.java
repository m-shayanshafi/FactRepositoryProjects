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

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Queue;

import pl.org.minions.utils.logger.Log;

/**
 * Executes actions for given document type and stores
 * undo/redo queues.
 * @param <DocumentType>
 *            type of document edited by command executed by
 *            this class
 */
public final class Executor<DocumentType>
{
    private Queue<EditorCommand<DocumentType>> undoQueue =
            new LinkedList<EditorCommand<DocumentType>>();
    private Queue<EditorCommand<DocumentType>> redoQueue =
            new LinkedList<EditorCommand<DocumentType>>();

    private DocumentType document;

    /**
     * Constructor.
     * @param document
     *            context in which all commands should be
     *            executed/undone etc.
     */
    public Executor(DocumentType document)
    {
        this.document = document;
    }

    /**
     * Returns {@code true} when {@link #redo()} can be
     * called.
     * @return {@code true} when there are any commands in
     *         redo queue
     */
    public boolean canRedo()
    {
        return !redoQueue.isEmpty();
    }

    /**
     * Returns {@code true} when {@link #undo()} can be
     * called.
     * @return {@code true} when there are any commands in
     *         undo queue
     */
    public boolean canUndo()
    {
        return !undoQueue.isEmpty();
    }

    /**
     * Executes command, adds it to undo queue if necessary.
     * @param command
     *            command to execute.
     */
    public void execute(EditorCommand<DocumentType> command)
    {
        if (!command.execute(document))
        {
            Log.logger.error(MessageFormat.format("Execution of command: {0}failed",
                                                  command));
            return;
        }

        if (command.clearsUndoQueue())
        {
            undoQueue.clear();
            redoQueue.clear();
            return;
        }

        if (command.modifies())
        {
            redoQueue.clear();
            if (command.canBeUndone())
                undoQueue.offer(command);
        }
    }

    /**
     * Redoes first command in redo queue.
     */
    public void redo()
    {
        EditorCommand<DocumentType> command = redoQueue.poll();
        if (command == null)
        {
            Log.logger.error("Nothing can be redone");
            return;
        }

        execute(command);
    }

    /**
     * Undoes first command in undo queue.
     */
    public void undo()
    {
        EditorCommand<DocumentType> command = undoQueue.poll();
        if (command == null)
        {
            Log.logger.error("Nothing can be undone");
            return;
        }

        if (!command.undo(document))
        {
            Log.logger.error("Undo of command: " + command + "failed");
            return;
        }

        redoQueue.offer(command);
    }
}
