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

package ca.smu.cs.csci3465.project.stratego;


public abstract class Engine
{
	protected Board board = null;
	protected Status status = Status.STOPPED;
	protected int turn = 0;

	public Piece getBoardPiece(int x, int y)
	{
		return board.getPiece(new Spot(x, y));
	}
	
	public Piece getTrayPiece(int i)
	{
		return board.getTrayPiece(i);
	}

	public int getTraySize()
	{
		return board.getTraySize();
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	private boolean move(Move m)
	{
		if (status == Status.PLAYING)
		{
			if (m.getFrom().equals(Board.IN_TRAY))
				return false;
			if (m.getTo().equals(Board.IN_TRAY))
				return false;
			
			if (board.attack(m))
				return true;
	
			return board.move(m);
		}
		return false;
	}
	
	public void newGame()
	{
		board.clear();
		turn = 0;
		status = Status.SETUP;
		
		if (Settings.bShowAll)
			board.showAll();
	}
	
	public void play()
	{
		if (status != Status.SETUP)
			return;
		
		if (board.getTraySize() == 0)
			status = Status.PLAYING;
		
		if (Settings.bShowAll)
			board.showAll();
	}


	protected boolean requestMove(Move m)
	{
		if (m == null || !move(m))
			return false;
		
		if (status == Status.PLAYING)
		{
			int winner;
			if ((winner = board.checkWin()) >= 0)
			{
				status = Status.STOPPED;
				board.showAll();
				gameOver(winner);
			}

			turn = (turn + 1) % 2;
		}

		update();
		return true;
	}
	
	public boolean setupPlacePiece(Piece p, Spot s)
	{
		if (status == Status.SETUP)
			return board.add(p, s);
		
		return false;
	}
	
	public boolean setupRemovePiece(Spot s)
	{
		if (status == Status.SETUP)
			return board.remove(s);
		
		return false;
	}
	
	protected abstract void gameOver(int winner);
	protected abstract void update();
}
