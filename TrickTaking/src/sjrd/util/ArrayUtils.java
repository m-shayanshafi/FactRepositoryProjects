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

import java.lang.reflect.*;

/**
 * Fonctions utilitaires sur des tableaux
 * @author sjrd
 */
public class ArrayUtils
{
	/**
	 * Recherche un élément dans un tableau
	 * @param array Tableau
	 * @param item Élément à rechercher
	 * @return Index de l'élément dans le tableau, ou -1 si non trouvé
	 */
	public static int arrayIndexOf(Object[] array, Object item)
	{
		for (int i = 0; i < array.length; i++)
			if (array[i] == item)
				return i;
		return -1;
	}
	
	/**
	 * Teste si un élément est présent dans un tableau
	 * @param array Tableau
	 * @param item Élément à rechercher
	 * @return <tt>true</tt> si l'élément est présent, <tt>false</tt> sinon
	 */
	public static boolean arrayContains(Object[] array, Object item)
	{
		return arrayIndexOf(array, item) >= 0;
	}
	
	/**
	 * Crée un tableau d'un type de données générique
	 * @param <T> Type des éléments du tableau
	 * @param clazz Classe des éléments du tableau
	 * @param length Longueur du tableau à créer
	 * @return Tableau créé
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createArray(Class<T> clazz, int length)
	{
		return (T[]) Array.newInstance(clazz, length);
	}

	/**
	 * Crée un tableau d'éléments du même type qu'un autre tableau
	 * @param <T> Type des éléments du tableau
	 * @param base Tableau modèle
	 * @param length Longueur du tableau à créer
	 * @return Tableau créé
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createArrayLike(T[] base, int length)
	{
		return (T[]) Array.newInstance(base.getClass().getComponentType(),
			length);
	}
	
	/**
	 * Concatène des tableaux
	 * @param <T> Type des éléments du tableau
	 * @param clazz Classe des éléments du tableau
	 * @param arrays Tableaux à concaténer
	 * @return Tableau contenant les éléments de tous les tableaux, dans l'ordre
	 */
	public static <T> T[] concatArrays(Class<T> clazz, T[] ... arrays)
	{
		int count = 0;
		for (T[] array: arrays)
			count += array.length;
		
		T[] result = createArray(clazz, count);
		
		int index = 0;
		for (T[] array: arrays)
		{
			for (int i = 0; i < array.length; i++)
				result[index++] = array[i];
		}
		
		return result;
	}

	/**
	 * Concatène des tableaux
	 * <p>
	 * Au moins un tableau doit être donné en argument.
	 * </p>
	 * @param <T> Type des éléments du tableau
	 * @param arrays Tableaux à concaténer
	 * @return Tableau contenant les éléments de tous les tableaux, dans l'ordre
	 */
	public static <T> T[] concatArrays(T[] ... arrays)
	{
		assert arrays.length > 0;
		
		int count = 0;
		for (T[] array: arrays)
			count += array.length;
		
		T[] result = createArrayLike(arrays[0], count);
		
		int index = 0;
		for (T[] array: arrays)
		{
			for (int i = 0; i < array.length; i++)
				result[index++] = array[i];
		}
		
		return result;
	}
	
	/**
	 * Convertit les éléments d'un tableau en un autre type, avec un mapper
	 * <p>
	 * Si le tableau <tt>dest</tt> est suffisamment grand pour accueillir le
	 * résultat, il est utilisé. Sinon, un nouveau tableau est créé, et
	 * renvoyé.
	 * </p>
	 * <p>
	 * Chaque élément du tableau source est converti <i>via</i> la méthode
	 * <tt>map</tt> du mapper, et le résultat est placé à l'index correspondant
	 * du résultat.
	 * </p>
	 * @param <T> Type d'origine des éléments
	 * @param <V> Type de destination des éléments
	 * @param source Tableau source
	 * @param dest Tableau destination
	 * @param mapper Mapper
	 * @return Tableau résultat (éventuellement égal à <tt>dest</tt>)
	 */
	public static <T, V> V[] mapArray(T[] source, V[] dest,
		Mapper<? super T, ? extends V> mapper)
	{
		V[] result;
		
		if (dest.length >= source.length)
			result = dest;
		else
			result = createArrayLike(dest, source.length);
		
		for (int i = 0; i < source.length; i++)
			result[i] = mapper.map(source[i]);
		
		return result;
	}
}
