package com.game.game;

import com.game.board.BaseBoard;
import com.game.entity.Move;
import com.game.entity.Player;


public abstract class AbstractGame implements Game{
	protected BaseBoard board;
	private Player currentPlayer;
	private Player winningPlayer;
	
	public AbstractGame(BaseBoard b) {
		currentPlayer = Player.A;
		this.board = b;
	}
	
	/*
	 * These conditions are dependent on the game
	 */
	protected abstract boolean winsAnyCondition(Move move);

	protected abstract boolean winsVertical(Move move);

	protected abstract boolean winsHorizontal(Move move);

	protected abstract boolean winsDiagnal(Move move);
	
	public void play(Move move) {
		if (board.isInvalidMove(move)) {
			throw new IllegalArgumentException("Invalid move: " + move.toString());
		}

		board.setMove(move, getCurrentPlayer().getValue());
		if (winsAnyCondition(move)) {
			winningPlayer = getCurrentPlayer();
			return;
		}
	}

	public Player switchPlayer(){
		currentPlayer = (currentPlayer == Player.A) ? Player.B : Player.A;
		return currentPlayer;
	}
	
	public boolean isGameOver() {
		boolean isOver = false;
		
		System.out.println(this.board);
		if (getWinner() != null) {
			System.out.println("Winner: Player "+ getWinner().getValue());
			isOver = true;
		} else if (board.movesRemaining() == 0) {
			System.out.println("Winner: Its a tie");
			isOver = true;
		}
		System.out.println("GAMEOVER? " + isOver + "\n");
		
		
		return isOver;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player getWinner() {
		return winningPlayer;
	}


}
