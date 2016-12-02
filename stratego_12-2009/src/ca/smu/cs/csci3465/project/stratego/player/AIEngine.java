/*
    This file is part of Stratego.

    Stratego is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Stratego is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Stratego.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.smu.cs.csci3465.project.stratego.player;

import java.io.IOException;

import javax.swing.JOptionPane;

import ca.smu.cs.csci3465.project.stratego.Board;
import ca.smu.cs.csci3465.project.stratego.Engine;
import ca.smu.cs.csci3465.project.stratego.Move;
import ca.smu.cs.csci3465.project.stratego.Piece;
import ca.smu.cs.csci3465.project.stratego.Settings;
import ca.smu.cs.csci3465.project.stratego.Spot;
import ca.smu.cs.csci3465.project.stratego.Status;


public class AIEngine extends Engine implements CompControls, UserControls
{
	private View view = null;
	private AI ai = null;
	private int userColor = 0;
	
	public AIEngine(View v)
	{
		view = v;
		board = new Board();
		ai = new AI(board, this);
	}
	
	public void play()
	{
		if (status != Status.SETUP)
			return;
		
		if (board.getTraySize() == 0)
		{
			status = Status.PLAYING;
			if (turn!=userColor)
				requestCompMove();
		}
		else 
		{
			try {
				ai.getBoardSetup();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (Settings.bShowAll)
			board.showAll();
	}
	
	private void requestCompMove()
	{
		if (status == Status.PLAYING)
		if (turn != userColor)
			ai.getMove();
	}
	
	public void userRequestMove(Move m)
	{
		if (m == null) return;
		
		if (status == Status.SETUP)
		{
			if (setupRemovePiece(m.getFrom()))
					setupPlacePiece(m.getPiece(), m.getTo());
		}
		else
		{
			if (turn != userColor)
			{
				if (!AI.aiLock.isLocked())
					requestCompMove();
			}
			else
			{
				if (Settings.bShowAll)
					board.showAll();
				else if (!Settings.bNoHideAll)
					board.hideAll();
				
				if (requestMove(m))
					requestCompMove();
			}
		}
	}

	public void aiReturnMove(Move m)
	{
		if (turn == userColor)
			return;
		
		if (m==null || m.getPiece()==null || m.getFrom()==null || m.getTo()==null)
		{
			//AI trapped
			status = Status.STOPPED;
			board.showAll();
			view.gameOver(userColor);
			return;
		}
		if (m.getPiece().getColor() == userColor)
			return; // shoulden't happen anyway

		requestMove(m);
		if (turn != userColor)
			JOptionPane.showMessageDialog(null,
					"AI error" , "Critical Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void aiReturnPlace(Piece p, Spot s)
	{
		if (!setupPlacePiece(p, s))
			return;
		view.update();
	}

	@Override
	protected void gameOver(int winner)
	{
		view.gameOver(winner);
	}

	@Override
	protected void update()
	{
		view.update();
	}

	@Override
	public void newGame(int c)
	{
		super.newGame();
		userColor = c;
		board.setTopColor((userColor+1)%2);
	}
}
