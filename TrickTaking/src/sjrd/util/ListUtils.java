/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.util;

import java.util.*;

/**
 * Fonctions utilitaires sur des listes
 * @author sjrd
 */
public class ListUtils
{
	/**
	 * Convertit les éléments d'une liste en un autre type, avec un mapper
	 * <p>
	 * Chaque élément de la liste source est converti <i>via</i> la méthode
	 * <tt>map</tt> du mapper, et le résultat est placé à l'index correspondant
	 * du résultat.
	 * </p>
	 * @param <T> Type d'origine des éléments
	 * @param <V> Type de destination des éléments
	 * @param source Liste source
	 * @param mapper Mapper
	 * @return Liste résultat
	 */
	public static <T, V> List<V> mapList(List<T> source,
		Mapper<? super T, ? extends V> mapper)
	{
		List<V> result = new ArrayList<V>(source.size());
		
		for (T sourceItem: source)
			result.add(mapper.map(sourceItem));
		
		return result;
	}
}
