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

import pl.org.minions.stigma.network.Buffer;

/**
 * Generic class used for encoding/decoding class derived
 * from {@link NetworkObject}. Using given enum type it will
 * find proper classes for representing decoded object.
 * @param <Type>
 *            enum type used for describing type of object
 * @param <ObjectType>
 *            base class for all encoded/decoded network
 *            objects
 */
public class NetworkObjectCodec<Type extends Enum<Type> & NetworkObjectCodec.ObjectFactory<ObjectType>, ObjectType extends NetworkObject<Type>>
{
    /**
     * Factory for creating empty objects needed for
     * decoding.
     */
    public interface ObjectFactory<T>
    {
        /**
         * Creates empty object.
         * @return empty object
         */
        T buildObject();
    }

    private Type[] values;

    /**
     * Creates NetworkObjectCodec for given enum type.
     * @param clazz
     *            object representing enum type class,
     *            needed for obtaining list of all enum
     *            variables.
     */
    public NetworkObjectCodec(Class<Type> clazz)
    {
        values = clazz.getEnumConstants();
    }

    /**
     * Decodes network object from buffer. Decoded object
     * will have class based on factory connected with it's
     * type.
     * @param buffer
     *            buffer from which object should be decoded
     * @return decoded object or {@code null} on error
     */
    public ObjectType decode(Buffer buffer)
    {
        short type = buffer.decodeShort();
        if (type < 0 || type > values.length)
            return null;
        ObjectType o = values[type].buildObject();
        if (!o.decodeParams(buffer))
            return null;
        return o;
    }

    /**
     * Encodes given object using given buffer.
     * @param object
     *            network object to encode
     * @param buffer
     *            buffer on which object should be encoded
     * @return {@code true} when encoding was successful
     */
    public boolean encode(ObjectType object, Buffer buffer)
    {
        return object.encode(buffer);
    }
}
