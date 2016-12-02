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
package pl.org.minions.stigma.network.messaging;

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Abstract base class representing object described by it's
 * enum type. This class simplifies encoding/decoding such
 * object. Combined with {@link NetworkObjectCodec} makes it
 * almost automatic. Developer has to write only some
 * function for encode derived class special parameters and
 * factory for creating empty objects for decoding.
 * @param <Type>
 *            enum type for describing type of object
 */
public abstract class NetworkObject<Type extends Enum<Type>>
{
    private Type type;

    /**
     * Creates object with given type.
     * @param type
     *            type of new object.
     */
    protected NetworkObject(Type type)
    {
        this.type = type;
    }

    /**
     * Decodes derived class specific parameters.
     * @param buffer
     *            buffer form which parameters should be
     *            decoded
     * @return {@code true} when decoding was successful
     */
    protected abstract boolean decodeParams(Buffer buffer);

    /**
     * Encodes object using given buffer.
     * @param buffer
     *            buffer on which object should be encoded
     * @return {@code true} when encoding was successful
     */
    final boolean encode(Buffer buffer)
    {
        buffer.encode((short) getType().ordinal());
        return encodeParams(buffer);
    }

    /**
     * Encodes derived class specific parameters.
     * @param buffer
     *            buffer in which parameters should be
     *            encoded
     * @return {@code true} when encoding was successful
     */
    protected abstract boolean encodeParams(Buffer buffer);

    /**
     * Returns amount of needed bytes to encode object.
     * @return amount of needed bytes to encode object
     */
    public final int getParamsLength()
    {
        return SizeOf.SHORT + innerGetParamsLength();
    }

    /**
     * Returns type of given object.
     * @return type of given object
     */
    public final Type getType()
    {
        return type;
    }

    /**
     * Returns derived class specific parameters length in
     * bytes.
     * @return derived class specific parameters length in
     *         bytes.
     */
    protected abstract int innerGetParamsLength();

    /**
     * This implementation returns {@code
     * getType().toString()}.
     * @return {@code getType().toString()}
     */
    @Override
    public String toString()
    {
        return getType().toString();
    }
}
