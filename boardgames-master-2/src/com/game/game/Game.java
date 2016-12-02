package com.game.game;

import com.game.entity.Move;
import com.game.entity.Player;

public interface Game {
	public void play(Move move);

	public Player switchPlayer();
	
	public boolean isGameOver();
	
	public Player getWinner();
}
