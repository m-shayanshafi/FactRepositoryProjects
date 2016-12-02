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
package pl.org.minions.stigma.game.command.data;

import pl.org.minions.stigma.game.TimestampedObject;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class describing synchronization request for single
 * object.
 */
final class RequestDesc
{
    private static final int SIZE = SizeOf.INT + SizeOf.INT;

    private int objectId;

    private int objectTS;

    /**
     * Creates request description by decoding it from given
     * buffer.
     * @param buf
     *            buffer to decode request from
     */
    public RequestDesc(Buffer buf)
    {
        this.objectId = buf.decodeInt();
        this.objectTS = buf.decodeInt();
    }

    /**
     * Creates request for synchronization.
     * @param objectId
     *            requested object's id
     * @param objectTS
     *            requested object's known time-stamp
     */
    public RequestDesc(int objectId, int objectTS)
    {
        this.objectId = objectId;
        this.objectTS = objectTS;
    }

    /**
     * Creates request basing on given object.
     * @param object
     *            object for which request should be created
     */
    public RequestDesc(TimestampedObject<?> object)
    {
        this(object.getId(), object.getNewestTS());
    }

    /**
     * Returns size needed for encoding.
     * @return size needed for encoding.
     */
    public static int sizeOf()
    {
        return SIZE;
    }

    /**
     * Encodes request on buffer.
     * @param buf
     *            buffer to encode to
     * @return {@code true} on success
     */
    public boolean encode(Buffer buf)
    {
        buf.encode(objectId);
        buf.encode(objectTS);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        RequestDesc other = (RequestDesc) obj;
        return objectId == other.objectId && objectTS == other.objectTS;
    }

    /**
     * Returns requested object's id.
     * @return requested object's id.
     */
    public int getObjectId()
    {
        return objectId;
    }

    /**
     * Returns requested object's known time-stamp.
     * @return requested object's known time-stamp.
     */
    public int getObjectTS()
    {
        return objectTS;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just to remove checkstyle warnings
        return super.hashCode();
    }

    /**
     * Prints request in human readable form.
     * @param buf
     *            buffer on which request should be print
     */
    public void print(StringBuilder buf)
    {
        buf.append("[objectId: ");
        buf.append(getObjectId());
        buf.append(", TS:");
        buf.append(getObjectTS());
        buf.append("], ");
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        final int defSize = 50;
        StringBuilder b = new StringBuilder(defSize);
        print(b);
        return b.toString();
    }

}
