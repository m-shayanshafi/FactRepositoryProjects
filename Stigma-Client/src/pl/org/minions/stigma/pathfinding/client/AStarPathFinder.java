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
package pl.org.minions.stigma.pathfinding.client;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import pl.org.minions.stigma.game.path.Passable;
import pl.org.minions.stigma.game.path.Path;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.pathfinding.PathFinder;

/**
 * Implementation of AStarPathFinder which implement
 * PathFinder algorithm by use of A* heuristic algorithm to
 * determine Path. Implementation is based on A* example by
 * Kevin Glass.
 */
public class AStarPathFinder implements PathFinder
{
    /**
     * A single node in the search graph
     */
    private class Node implements Comparable<Node>
    {
        /** position of Node on map */
        private Position position;
        private float cost;
        /**
         * The parent of this node, how we reached it in the
         * search
         */
        private Node parent;
        /** The heuristic cost of this node */
        private float heuristic;
        /** The search depth of this node */
        private int depth;

        /**
         * Create a new node
         * @param pos
         *            The position of node
         */
        public Node(Position pos)
        {
            this.position = pos;
            reset();
        }

        /**
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(Node other)
        {
            Node o = other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of)
            {
                return -1;
            }
            else if (f > of)
            {
                return 1;
            }
            else
            {
                // just to allow multiple keys...
                return hashCode() - o.hashCode();
            }
        }

        /**
         * Returns cost.
         * @return cost
         */
        public float getCost()
        {
            return cost;
        }

        /**
         * Return position of node
         * @return position of node
         */
        public Position getPosition()
        {
            return position;
        }

        /**
         * Return X coordinate of node
         * @return X coordinate of node position
         */
        public int getX()
        {
            return position.getX();
        }

        /**
         * Return Y coordinate of node
         * @return Y coordinate of node position
         */
        public int getY()
        {
            return position.getY();
        }

        /**
         * Resets costs etc.
         */
        public void reset()
        {
            parent = null;
            depth = 0;
            cost = Float.MAX_VALUE;
            heuristic = Float.MAX_VALUE;
        }

        /**
         * Sets new value of cost.
         * @param cost
         *            the cost to set
         */
        public void setCost(float cost)
        {
            this.cost = cost;
        }

        /**
         * Sets new value of heuristic.
         * @param heuristic
         *            the heuristic to set
         */
        public void setHeuristic(float heuristic)
        {
            this.heuristic = heuristic;
        }

        /**
         * Set the parent of this node
         * @param parent
         *            The parent node which lead us to this
         *            node
         * @return The depth we have no reached in searching
         */
        public int setParent(Node parent)
        {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }
    }

    private static final float ADJACENT_DIRECTION_COST = 1.f;

    private static final float DIAGONAL_DIRECTION_COST = 1.2f;

    /**
     * List of passable elements on which path finder is
     * executed
     */
    private Passable passable;
    /** Dimension of passable zone */
    private Dimension analizedZone;
    /** List of nodes which had been searched through */
    private ArrayList<Node> closed;
    /** List of nodes to be searched */
    private SortedSet<Node> open;
    /**
     * The maximum depth of search we're willing to accept
     * before giving up
     */
    private int maxSearchDistance;
    /** The complete set of nodes across the map */
    private Node[][] nodes;
    /**
     * The heuristic we're applying to determine which nodes
     * to search first
     */
    private AStarHeuristic heuristic;

    /**
     * Default constructor of finder.
     * @param passable
     *            entity with passability detection
     *            (typically map) on which path is going to
     *            be found
     * @param analizedZone
     *            size of zone to analyze
     * @param heuristic
     *            heuristic which should be used by this
     *            path-finder
     * @param maxSearchDistance
     *            max search depth (influences number time
     *            of computation)
     */
    public AStarPathFinder(Passable passable,
                           Dimension analizedZone,
                           AStarHeuristic heuristic,
                           int maxSearchDistance)
    {
        this.passable = passable;
        this.analizedZone = analizedZone;
        this.maxSearchDistance = maxSearchDistance;
        this.closed = new ArrayList<Node>();
        this.open = new TreeSet<Node>();
        this.heuristic = heuristic;

        nodes = new Node[analizedZone.width][analizedZone.height];
        for (int x = 0; x < analizedZone.width; x++)
        {
            for (int y = 0; y < analizedZone.height; y++)
            {
                nodes[x][y] = new Node(new Position((short) x, (short) y));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Path generatePath(Position start, Position target, Distance range)
    {
        // clear after previous search
        for (Node n : closed)
            n.reset();
        closed.clear();
        for (Node n : open)
            n.reset();
        open.clear();

        int sx = start.getX();
        int sy = start.getY();
        int tx = target.getX();
        int ty = target.getY();

        // initial state for A*. The closed group is empty. Only the starting
        // tile is in the open list and it's cost is zero, i.e. we're already there
        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        nodes[sx][sy].heuristic = heuristic.getHeuristicCost(sx, sy, tx, ty);

        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        Node current = null;
        // while we haven't found the goal and haven't exceeded our max search depth
        int maxDepth = 0;
        Node targetNode = nodes[tx][ty];
        while (maxDepth < maxSearchDistance && open.size() != 0)
        {
            current = open.first();

            if (current == targetNode)
            {
                break;
            }

            if (range != null
                && range.getValue() > 0
                && range.getValue() >= range.getMetric()
                                            .getDistance(current.position,
                                                         targetNode.position))
            {
                targetNode = current;
                break;
            }

            open.remove(current);
            closed.add(current);

            // search through all the neighbors of the current node evaluating
            // them as next steps
            for (int x = -1; x < 2; x++)
            {
                for (int y = -1; y < 2; y++)
                {
                    // not a neighbor, its the current tile
                    if (x == 0 && y == 0)
                    {
                        continue;
                    }

                    // determine the location of the neighbor and evaluate it
                    int xp = x + current.getX();
                    int yp = y + current.getY();

                    if (isValidLocation(start, xp, yp))
                    {
                        Node neighbour = nodes[xp][yp];

                        // the cost to get to this node is cost the current plus the movement
                        // cost to reach this node. Note that the heuristic value is only used
                        // in the sorted open list
                        float nextStepCost =
                                current.getCost()
                                    + getDirectionConst(current, neighbour);

                        // if the new cost we've determined for this node is lower than 
                        // it has been previously makes sure the node hasn't been discarded. We've
                        // determined that there might have been a better path to get to
                        // this node so it needs to be re-evaluated
                        if (nextStepCost < neighbour.getCost())
                        {
                            open.remove(neighbour);
                            closed.remove(neighbour);
                        }

                        // if the node hasn't already been processed and discarded then
                        // reset it's cost to our current cost and add it as a next possible
                        // step (i.e. to the open list)
                        if (!open.contains(neighbour)
                            && !closed.contains(neighbour))
                        {
                            neighbour.setCost(nextStepCost);
                            neighbour.setHeuristic(heuristic.getHeuristicCost(xp,
                                                                              yp,
                                                                              tx,
                                                                              ty));
                            maxDepth =
                                    Math.max(maxDepth,
                                             neighbour.setParent(current));
                            open.add(neighbour);
                        }
                    }
                }
            }
        }

        // since we've got an empty open list or we've run out of search 
        // there was no path. Just return null
        if (targetNode.parent == null)
        {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent
        // references of the nodes to find out way from the target location back
        // to the start recording the nodes on the way.
        LinkedList<Position> positions = new LinkedList<Position>();
        while (targetNode != nodes[sx][sy])
        {
            positions.addFirst(targetNode.getPosition());
            targetNode = targetNode.parent;
        }

        Path path = new Path(start);
        path.addAll(positions);

        // thats it, we have our path 
        return path;
    }

    /** {@inheritDoc} */
    @Override
    public Path generatePath(Position start, Position target)
    {
        return generatePath(start, target, null);
    }

    private float getDirectionConst(Node current, Node neighbour)
    {
        Direction dir =
                current.getPosition().getDirection(neighbour.getPosition());
        switch (dir)
        {
            case N:
            case E:
            case W:
            case S:
                return ADJACENT_DIRECTION_COST;
            default:
                return DIAGONAL_DIRECTION_COST;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Passable getPassable()
    {
        return this.passable;
    }

    /**
     * Check if a given location is valid for the supplied
     * mover
     * @param start
     *            Beginning of path
     * @param x
     *            The x coordinate of the location to check
     * @param y
     *            The y coordinate of the location to check
     * @return True if the location is valid for the given
     *         mover
     */
    private boolean isValidLocation(Position start, int x, int y)
    {
        boolean invalid =
                x < 0 || y < 0 || x >= analizedZone.width
                    || y >= analizedZone.height;

        if (!invalid && (start.getX() != x || start.getY() != y))
        {
            invalid = !passable.isPassable(new Position((short) x, (short) y));
        }

        return !invalid;
    }
}
