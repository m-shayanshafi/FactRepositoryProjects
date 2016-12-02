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
package sjrd.util.appproperties;

/**
 * Propriété d'une application
 * @author sjrd
 */
public interface ApplicationProperty
{
	/**
	 * Nom de la propriété
	 * @return Nom de la propriété
	 */
	public String getName();
	
	/**
	 * Valeur par défaut de la propriété
	 * @return Valeur par défaut de la propriété (peut être <tt>null</tt>)
	 */
	public String getDefaultValue();
}
