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
package pl.org.minions.stigma.databases.xml;

/**
 * Simple interface of converter between two given object
 * types.
 * @param <ObjectType>
 *            target type
 * @param <StorageType>
 *            source type
 */
public interface Converter<ObjectType, StorageType>
{
    /**
     * Simple implementation for 1:1 converters. Should be
     * used when 'data' and 'object' types are equal.
     */
    public static class SimpleConverter<BasicType> implements
                                                   Converter<BasicType, BasicType>

    {
        /**
         * Returns {@code object}.
         * @param object
         *            object that will be returned
         *            unmodified
         * @return {@code object}
         */
        @Override
        public BasicType buildData(BasicType object)
        {
            return object;
        }

        /**
         * Returns {@code data}.
         * @param data
         *            data that will be returned unmodified
         * @return {@code data}
         */
        @Override
        public BasicType buildObject(BasicType data)
        {
            return data;
        }
    }

    /**
     * Converts given object (concrete - used in application
     * logic) to target type.
     * @param object
     *            object to convert
     * @return converted data
     */
    StorageType buildData(ObjectType object);

    /**
     * Converts given object (data - serializable) to target
     * type.
     * @param data
     *            data to convert
     * @return converted object
     */
    ObjectType buildObject(StorageType data);
}
