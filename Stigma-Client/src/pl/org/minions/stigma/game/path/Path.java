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
package pl.org.minions.stigma.game.path;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Position;

/**
 * Class that represents a sequence of steps from a start
 * position resulting in an end position.
 */
public class Path
{

    private static final String STEP_SEPARATOR = ">";
    private Position start;
    private Position end;
    private Deque<Position> steps = new LinkedList<Position>();

    /**
     * Creates an empty path leading from start position to
     * itself.
     * @param start
     *            start position
     */
    public Path(Position start)
    {
        this.start = start;
        this.end = this.start;
    }

    /**
     * Creates a path from start position along given steps.
     * @param start
     *            start position
     * @param steps
     *            sequence of steps
     */
    public Path(Position start, Position... steps)
    {
        this(start);
        end = addAll(steps);
    }

    /**
     * Creates a path from start position along given steps.
     * @param start
     *            start position
     * @param steps
     *            sequence of steps
     */
    public Path(Position start, Queue<Position> steps)
    {
        this(start);
        end = addAll(steps);
    }

    /**
     * Tries to add new position to end of path.
     * @param newEnd
     *            position to add
     * @return new end position or <code>null</code> if
     *         position can't be added to path
     */
    public Position add(Position newEnd)
    {
        if (end.isNeighbor(newEnd))
        {
            steps.add(newEnd);
            end = newEnd;
            return end;
        }
        else
            return null;
    }

    /**
     * Tries to add list of positions to path. If one of
     * positions cannot be added to path return
     * <code>null</code>.
     * @param positions
     *            to be added to path
     * @return end of path position or <code>null</code> if
     *         error occurred
     */
    public Position addAll(Collection<Position> positions)
    {
        if (positions == null)
            return null;

        for (Position position : positions)
        {
            if (add(position) == null)
                return null;
        }

        return end;
    }

    /**
     * Tries to add list of positions to path. If one of
     * positions cannot be added to path return null.
     * @param positions
     *            to be added to path
     * @return end of path position or <code>null</code> if
     *         error occurred
     */
    public Position addAll(Position... positions)
    {
        if (positions == null)
            return null;

        for (Position position : positions)
        {
            if (add(position) == null)
                return null;
        }
        return end;
    }

    /**
     * Adds a step in given direction to path.
     * @param direction
     *            step direction
     * @return new path end position
     */
    public Position addStep(Direction direction)
    {
        end = end.newPosition(direction);
        steps.add(end);
        return end;
    }

    /**
     * Adds a list of steps to path.
     * @param directions
     *            steps to be added to path
     * @return new path end position
     */
    public Position addSteps(Collection<Direction> directions)
    {
        if (directions == null)
            return null;

        for (Direction direction : directions)
        {
            addStep(direction);
        }

        return end;
    }

    /**
     * Adds a list of steps to path.
     * @param directions
     *            steps to be added to path
     * @return new path end position
     */
    public Position addSteps(Direction... directions)
    {
        if (directions == null)
            return null;

        for (Direction direction : directions)
        {
            addStep(direction);
        }

        return end;
    }

    /**
     * Advances the path by one step, changing start
     * position.
     * @return new start position
     * @throws NoSuchElementException
     *             if the path is empty
     */
    public Position advance() throws NoSuchElementException
    {
        start = steps.remove();
        return start;
    }

    /**
     * Appends another path to the end of this path, if the
     * beginning of the other path matches the end of this
     * path.
     * @param other
     *            path to append
     * @throws IllegalArgumentException
     *             if the end of this path does not match
     *             the beginning of the other path
     */
    public void append(Path other) throws IllegalArgumentException
    {
        if (!this.getEnd().equals(other.getEnd()))
            throw new IllegalArgumentException("Paths do not match.");
        steps.removeLast();
        steps.addAll(other.steps);
        end = other.end;
    }

    /**
     * Returns end.
     * @return end position
     */
    public Position getEnd()
    {
        return end;
    }

    /**
     * Returns the value of start position the next time the
     * {@link #advance()} method would be called.
     * @return position after next step
     * @throws NoSuchElementException
     *             if the path is empty
     */
    public Position getNextPosition() throws NoSuchElementException
    {
        return steps.element();
    }

    /**
     * Returns the direction between start and next position
     * .
     *@see #getNextPosition()
     * @return direction of next step
     * @throws NoSuchElementException
     *             if the path is empty
     */
    public Direction getNextStepDirection() throws NoSuchElementException
    {
        return start.getDirection(getNextPosition());
    }

    /**
     * Gets subpath of this path which is passable (if whole
     * path is passable returns whole path).
     * @param passable
     *            reference passable
     * @return passable subpath of this path
     */
    public Path getPassableSubPath(Passable passable)
    {
        Path path = new Path(start);
        for (Position pos : steps)
            if (!passable.isPassable(pos))
                break;
            else
                path.add(pos);
        return path;
    }

    /**
     * Returns start.
     * @return start position
     */
    public Position getStart()
    {
        return start;
    }

    /**
     * Checks if this path is empty.
     * @return <code>false</code> if the path consists of at
     *         least one step, <code>false</code> otherwise.
     */
    public boolean isEmpty()
    {
        return steps.isEmpty();
    }

    /**
     * Checks whether path is passable.
     * @param passable
     *            reference passable
     * @return true if whole path is passable
     */
    public boolean isPassable(Passable passable)
    {
        for (Position pos : steps)
            if (!passable.isPassable(pos))
                return false;
        return true;
    }

    /**
     * Returns the size of this path.
     * @return number of steps in this path
     */
    public int size()
    {
        return steps.size();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        Position nextPosition = start;

        buffer.append(nextPosition.toString());
        for (Position position : steps)
        {
            buffer.append(STEP_SEPARATOR).append(position);
        }

        return buffer.toString();
    }
}
