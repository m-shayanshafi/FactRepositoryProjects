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
package sjrd.tricktakinggame.network.commands;

import java.io.*;

/**
 * Commande gérée par une connexion à base de commandes
 * @author sjrd
 */
public interface Command
{
	/**
	 * Nom de la commande
	 * @return Nom de la commande
	 */
	public String getName();

	/**
	 * Indique si la commande est disponible
	 * <p>
	 * Si on lit une commande qui n'est pas disponible, celle-ci n'est pas pas
	 * exécutée, et la réponse envoyée indique que la commande est inconnue.
	 * </p>
	 * @return <tt>true</tt> si la commande est disponible, <tt>false</tt>
	 *         sinon
	 */
	public boolean isAvailable();
	
	/**
	 * Indique quels sont les paramètres attendus par la commande
	 * <p>
	 * Lorsqu'une commande est invoquée, et qu'elle est disponible, sa validité
	 * syntaxique est encore vérifiée d'après les types des paramètres
	 * attendus. Si les types de paramètres ne correspondent pas, une erreur de
	 * protocole est envoyée.
	 * </p>
	 * <p>
	 * Si une commande ne désire pas être affectée par cette vérification,
	 * <tt>getParameterKinds()</tt> doit renvoyer <tt>null</tt>.
	 * </p>
	 * @return Types des paramètres attendus, ou <tt>null</tt> pour ne pas
	 *         activer la vérification
	 */
	public ParameterKind[] getParameterKinds();

	/**
	 * Exécute la commande
	 * @param parameters Tableau des paramètres envoyés à la commande
	 */
	public void execute(String[] parameters) throws IOException;
}
