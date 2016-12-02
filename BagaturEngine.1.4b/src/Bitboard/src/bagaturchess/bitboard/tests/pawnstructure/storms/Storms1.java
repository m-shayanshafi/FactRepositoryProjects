/*
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  Open Source project location: http://sourceforge.net/projects/bagaturchess/develop
 *  SVN repository https://bagaturchess.svn.sourceforge.net/svnroot/bagaturchess
 *
 *  This file is part of BagaturChess program.
 * 
 *  BagaturChess is open software: you can redistribute it and/or modify
 *  it under the terms of the Eclipse Public License version 1.0 as published by
 *  the Eclipse Foundation.
 *
 *  BagaturChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Eclipse Public License for more details.
 *
 *  You should have received a copy of the Eclipse Public License version 1.0
 *  along with BagaturChess. If not, see <http://www.eclipse.org/legal/epl-v10.html/>.
 *
 */
package bagaturchess.bitboard.tests.pawnstructure.storms;

import org.junit.Test;

import bagaturchess.bitboard.impl.Figures;
import bagaturchess.bitboard.tests.pawnstructure.PawnStructureTest;

public class Storms1 extends PawnStructureTest {
	
	@Override
	public String getFEN() {
		return "6k1/7p/1pP2p1/6p1/8/8/5PKP/8 w";
	}

	@Test
	public void validate() {
		//System.out.println(bitboard);
		validateStorms(Figures.COLOUR_WHITE, F2 | H2, 12);
		validateStorms(Figures.COLOUR_BLACK, F6 | G5 | H7, 12);
		
		validateKingVerticals(Figures.COLOUR_WHITE, LETTER_F | LETTER_G | LETTER_H);
		validateKingVerticals(Figures.COLOUR_BLACK, LETTER_F | LETTER_G | LETTER_H);
	}
}