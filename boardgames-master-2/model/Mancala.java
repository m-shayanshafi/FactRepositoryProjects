package model;

public class Mancala extends BoardGame {

	public static final int ROW = 2;
	public static final int COLOUM = 6;

	@Override
	public void init(boolean isSingleMode) {
		matrix = new int[COLOUM][ROW];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++)
				matrix[j][i] = 4;
		}
		int[][] mb = { { 0, 5 }, { 0, 5 } };
		bounds = mb;
		players[0] = new HumanPlayer();
		if(isSingleMode)
		players[1] = new MPlayer();
		else
		players[1] = new HumanPlayer();
		this.initPlayers();
	}

	public void moveFrom(Location l) {
		int val = this.getValue(l);
		int inc = 0;
		Location next;
		if (l.getY() == 0) {
			next = new Location(l.getX() - 1, l.getY());
		} else {
			next = new Location(l.getX() + 1, l.getY());
		}
		this.setValue(l, 0);
	
		while (inc < val) {
			if (next.getY() == 0) {
				if (this.isOutOfBound(next)) {
					// this.p1score ++;
					players[0].gainScore(1);
					if (inc < val - 1)
						next = new Location(next.getX() + 1, next.getY() + 1);
				} else {
					this.moveOver(next);
					if (inc < val - 1)
						next = new Location(next.getX() - 1, next.getY());
				}

			}

			else {
				if (this.isOutOfBound(next)) {
					// this.p2score ++;
					players[1].gainScore(1);
					if (inc < val - 1)
						next = new Location(next.getX() - 1, next.getY() - 1);
				} else {
					this.moveOver(next);
					if (inc < val - 1)
						next = new Location(next.getX() + 1, next.getY());
				}

			}
			inc++;
		}


		if (this.isOutOfBound(next)) {
			System.out.println("in store");

		    this.computerTurn();

			return;
		}

		if (this.getValue(next) == 1) {
			Location opposite = null;
			if (next.getY() == 0 && this.currentPlayer.getFlag() == Player.PLAYER1_FLAG) {
				opposite = new Location(next.getX(), next.getY() + 1);
			}

			else if (next.getY() == 1 && this.currentPlayer.getFlag() == Player.PLAYER2_FLAG) {
				opposite = new Location(next.getX(), next.getY() - 1);
			}
			if (opposite != null) {
				if (this.getValue(opposite) != 0) {
					int v = this.getValue(opposite) + 1;
					currentPlayer.gainScore(v);
					System.out.println("gain score   " + v);
					this.setValue(opposite, 0);
				//	this.setChanged();
				//	this.notifyObservers(new dNotification(opposite));
					this.setValue(next, 0);
				//	this.setChanged();
				//	this.notifyObservers(new ValueChangedNotification(next));

				}
			}

		}

          this.end();
	}
	
	public void checkWon(){
	if (players[0].getScore() + players[1].getScore() == 48) {
		this.win(players[0].getScore() >players[1].getScore() ? players[0]:players[1]);
	}
	}

	@Override
	protected boolean isValidSelection(Location l) {
		if (super.isValidSelection(l)) {

			if ((this.currentPlayer.getFlag() == Player.PLAYER1_FLAG && l.getY() == 0)
					|| (this.currentPlayer.getFlag() == Player.PLAYER2_FLAG && l.getY() == 1)) {
				return true;
			}
		}
		// System.out.println(this.getFlag());
		return false;
	}

/*	public String getScore() {
		return players[0].getScore() + " : " + players[1].getScore();
	}*/

	public void moveOver(Location l) {
		System.out.println("MOVE" + l.getX() + "y=" + l.getY());
		// System.out.println(l.getY());
		this.setValue(l, this.getValue(l) + 1);
		// return val+1;
	
	}

	@Override
	public boolean selectPiece(Location l) {
		if (isAllEmpty()) {
			this.end();
			return true;
		}
		if (super.selectPiece(l)) {
			this.moveFrom(l);
			this.selectedPiece = null;
			return true;
		}
		return false;

	}

	@Override
	protected boolean computerTurn(){
		if (this.currentPlayer instanceof MPlayer) {
			while(!currentPlayer.select(null));
			return true;
		}
		return false;
	}
	
	
	private boolean isAllEmpty() {
		if (this.currentPlayer.getFlag() == Player.PLAYER1_FLAG) {
			for (int i = 0; i < 6; i++) {
				if (this.getValue(new Location(i, 0)) != 0)
					return false;
			}

		} else {
			for (int i = 0; i < 6; i++) {
				if (this.getValue(new Location(i, 1)) != 0)
					return false;
			}
		}
		// System.out.println("all empty player is " +
		// this.currentPlayer.getFlag());
		return true;
	}

	@Override
	public void end() {
		
		this.currentPlayer.endTurn();
        this.computerTurn();
	}
}
