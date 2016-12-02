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
package sjrd.tricktakinggame.rules.whist.announces;

/**
 * Valeurs de référence des annonces
 * @author sjrd
 */
public class AnnounceValues
{
	public static final int TrickCountCoeff = 6;
	
	public static final int None = 0;
	
	public static final int Pass = 1;
	
	public static final int Wait = 2;
	
	public static final int Propose = 3;
	
	public static final int PackBase = 10;
	
	public static final int PackMax = 43; // PackBase + 6*5 + 3
	
	public static final int VarSoloBase = 50;
	
	public static final int VarSoloMax = 65; // VarSoloBase + 6*2 + 3
	
	public static final int SmallMisery = VarSoloBase + 6*2 - 2;
	
	public static final int DuoSmallMisery = SmallMisery + 1;
	
	public static final int Abondance = 70;
	
	public static final int Gap = 80;
	
	public static final int GrandMisery = 90;
	
	public static final int ElevenOnMisery = 100;
	
	public static final int SmallShelem = 110;
	
	public static final int GrandShelem = 120;
	
	/**
	 * Empêcher toute instanciation de cette classe
	 */
	private AnnounceValues()
	{
	}
}
