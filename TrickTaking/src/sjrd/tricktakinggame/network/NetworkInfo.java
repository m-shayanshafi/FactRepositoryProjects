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
package sjrd.tricktakinggame.network;

import java.nio.charset.*;

/**
 * Caractéristiques du réseau lié à un jeu à plis
 * @author sjrd
 */
public class NetworkInfo
{
	/**
	 * Port par défaut pour le réseau
	 */
	public static final int defaultPort = 3425;
	
	/**
	 * Chaîne représentant la valeur <tt>null</tt> dans les communications
	 */
	public static final String nullString = "<null>";
    
    /**
     * Charset utilisé sur le réseau
     */
    public static final Charset charset = getCharset();
    
    /**
     * Obtient le charset à utiliser pour le réseau
     * @return Le charset UTF-8 si disponible, sinon celui par défaut
     */
    private static Charset getCharset()
    {
        try
        {
            return Charset.forName("UTF-8");
        }
        catch (UnsupportedCharsetException error)
        {
            return Charset.defaultCharset();
        }
    }
}
