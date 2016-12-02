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
package sjrd.tricktakinggame.rules.manille;

import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Fournisseur de règles de manille
 * @author sjrd
 */
public class ManilleProvider implements RulesProvider
{
    /**
     * {@inheritDoc}
     */
    public String getID()
    {
	    return ManilleRules.class.getName();
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
	    return "Manille à 4";
    }

    /**
     * {@inheritDoc}
     */
    public int getMinPlayerCount()
    {
	    return 4;
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxPlayerCount()
    {
	    return 4;
    }

    /**
     * {@inheritDoc}
     */
    public Rules newRules(int playerCount)
    {
	    assert playerCount == 4;
	    
	    return new ManilleRules();
    }
}
