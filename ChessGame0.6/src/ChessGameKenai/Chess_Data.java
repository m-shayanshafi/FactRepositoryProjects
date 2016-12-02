package ChessGameKenai;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javax.swing.JOptionPane;

import ChessGameKenai.model.CPiece;

/**
 * The Chess_Data Class are the model for our chess game This model is an
 * Observable object which when changed notifies the observers in order to
 * update observer views The model contains all possible moves for the game
 * 
 * @author Dimitri Pankov
 * @see Observable Class
 * @see ArrayList object
 * @version 1.1
 */
public final class Chess_Data extends Observable {

	private ArrayList<NonVisualPiece> capturedPieces = new ArrayList<NonVisualPiece>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> loadedPlayer = new ArrayList<Player>();

	private ArrayList<NonVisualPiece> _activePieces = new ArrayList<NonVisualPiece>();
	private boolean isWhiteTurn = true;
	private boolean isServer = true;
	private boolean isGameOnLine = false;
	private static Chess_Data _instance;
	
	public static Chess_Data getInstance(){
		if(_instance == null)
			_instance = new Chess_Data();
		
		return _instance;
	}
	/**
	 * Empty Constructor of the Chess_Data Class When the object of Chess_Data
	 * is created it will contain the information that is specified inside the
	 * constructor the constructor creates the non visual pieces and fills the
	 * array with them
	 */
	private Chess_Data() {
		this.createNonVisualPieces();
	}

	/**
	 * The method createNonVisualPieces simply creates non visual pieces the
	 * pieces are stored in the ArrayList of activePieces
	 */
	public void createNonVisualPieces() {
		if (!_activePieces.isEmpty()) {
			_activePieces.clear();
		}

		for (int pos = 0; pos < 64; pos++) {
			if (CPiece.isBRookDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BRook, pos);

			} else if (CPiece.isBKnightDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BKnight, pos);

			} else if (CPiece.isBBishopDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BBishop, pos);

			} else if (CPiece.isBQueenDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BQueen, pos);

			} else if (CPiece.isBKingDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BKing, pos);

			} else if (CPiece.isBPawnDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.BPawn, pos);

			} else if (CPiece.isWPawnDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WPawn, pos);

			} else if (CPiece.isWRookDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WRook, pos);

			} else if (CPiece.isWKnightDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WKnight, pos);

			} else if (CPiece.isWBishopDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WBishop, pos);

			} else if (CPiece.isWQueenDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WQueen, pos);

			} else if (CPiece.isWKingDefaultPosition(pos)) {
				addNonVisualActivePiece(CPiece.WKing, pos);

			} else {
				_activePieces.add(null);
			}
		}
	}

	private void addNonVisualActivePiece(String piece, int pos) {
		_activePieces.add(NonVisualPiece.create(this, piece, (pos + 1)));
	}

	/**
	 * The method isGameOnLine simply sets the boolean value to true or false
	 * depending if the game is online to true else to false
	 * 
	 * @param isGameOnLine
	 *            as a boolean
	 */
	public void isGameOnLine(boolean isGameOnLine) {
		this.isGameOnLine = isGameOnLine;
		this.notifyView();
	}

	/**
	 * The method isGameOnLine returns true or false depending if the game is
	 * online true else to false
	 * 
	 * @return as a boolean
	 */
	public boolean isGameOnLine() {
		return isGameOnLine;
	}

	/**
	 * The method isWhiteTurn simply returns true or false if it is white turn
	 * to play true else false
	 * 
	 * @return isWhiteTurn as a boolean
	 */
	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}

	/**
	 * The method isServer simply sets the boolean value to true or false if the
	 * current player is server true else false
	 * 
	 * @param isServer
	 *            as a boolean
	 */
	public void isServer(boolean isServer) {
		this.isServer = isServer;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * The method isServer simply returns true or false if the current player is
	 * server true else false
	 * 
	 * @return isServer as a boolean
	 */
	public boolean isServer() {
		return isServer;
	}

	/**
	 * The method isWhiteTurn simply sets the boolean value to true or false if
	 * it is the white turn to play true else false
	 * 
	 * @param isWhiteTurn
	 *            as a boolean
	 */
	public void isWhiteTurn(boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}
	
	public NonVisualPieceIterator getPiecesIterator(){
		NonVisualPieceIterator iterator = new NonVisualPieceIterator(_activePieces);
		return iterator;
	}
	public void clearActivePieces(){
		this._activePieces.clear();
	}
	public NonVisualPiece getPieceAtPosition(int pos){
		return _activePieces.get(pos);
	}
	public void setPieceAtPosition(NonVisualPiece nvp, int pos){
		_activePieces.set(pos, nvp);
	}
	public int getPiecesSize(){
		return _activePieces.size();
	}
	public ArrayList<NonVisualPiece> getActivePieces() {
		return _activePieces;
	}

	public void setActivePieces(ArrayList<NonVisualPiece> activePieces) {
		this._activePieces = activePieces;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * The method getLoadedPlayers simply returns the Player ArrayList that just
	 * been loaded from the file to the user so he will choose what to do next
	 * what player he wants to load up
	 * 
	 * @return ArrayList<Player> loadedPlayer
	 */
	public ArrayList<Player> getLoadedPlayers() {
		return loadedPlayer;
	}

	/**
	 * The method savePlayer saves the player properties to a file Using
	 * ObjectOutputStream it saves ArrayList<Player> players
	 */
	public void savePlayer() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File("player.dat")));
			oos.writeObject(players);
			oos.flush();
			oos.close();
		} catch (Exception ioe) {
			JOptionPane.showMessageDialog(null, ioe.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The method loadPlayer loads the player properties from a file Using
	 * ObjecInputStream it loads and stores player data to the ArrayList<Player>
	 * players
	 */
	public void loadPlayer() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File("player.dat")));
			loadedPlayer = (ArrayList<Player>) ois.readObject();
			players.clear();
			this.setPlayers(loadedPlayer);
			ois.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error " + e.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}

		this.setChanged();
		this.notifyObservers();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayers(Player player) {
		this.getPlayers().add(player);
	}

	public Player getPlayer(int index) {
		return this.getPlayers().get(index);
	}

	public Player getPlayerOne() {
		return getPlayer(0);
	}

	public Player getPlayerTwo() {
		return getPlayer(1);
	}

	public void notifyView() {
		this.setChanged();
		this.notifyObservers();
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * The method save simply saves the current game Using ObjectOutputStream
	 * all Serialized objects are able to be saved
	 */
	public void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File("saved_game.dat")));
			ObjectOutputStream oss = new ObjectOutputStream(
					new FileOutputStream(new File(
							"saved_game_captured_pieces.dat")));
			oos.writeObject(_activePieces);
			oss.writeObject(capturedPieces);
			oss.flush();
			oos.flush();
			oss.close();
			oos.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error " + ioe.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The overloaded save method simply saves the current game Using
	 * ObjectOutputStream all Serialized objects are able to be saved
	 * 
	 * @param file
	 *            is the file to save
	 */
	public void save(File file) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file));
			ObjectOutputStream oss = new ObjectOutputStream(
					new FileOutputStream(file.getName() + ".bak"));
			oos.writeObject(_activePieces);
			oss.writeObject(capturedPieces);
			oss.flush();
			oos.flush();
			oss.close();
			oos.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error " + ioe.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The load method simply loads the old game Using ObjectInputStream it
	 * loads the game from the file into an ArrayList and then notifies
	 * observers which in turn update their views
	 */
	public void load() {
		try {
			final ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(new File("saved_game.dat")));
			final ObjectInputStream iis = new ObjectInputStream(
					new FileInputStream(new File(
							"saved_game_captured_pieces.dat")));
			_activePieces = (ArrayList) ois.readObject();
			capturedPieces = (ArrayList) iis.readObject();
			this.setPieces();
			iis.close();
			ois.close();
		} catch (Exception ioe) {
			JOptionPane.showMessageDialog(null, "Error " + ioe.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The overloaded load method simply loads the old game Using
	 * ObjectInputStream it loads the game from the file into an ArrayList and
	 * then notifies observers which in turn update their views
	 * 
	 * @param file
	 *            to load from the System
	 */
	public void load(File file) {
		try {
			final ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(file));
			final ObjectInputStream iis = new ObjectInputStream(
					new FileInputStream(file + ".bak"));
			_activePieces = (ArrayList) ois.readObject();
			this.setPieces();
			capturedPieces = (ArrayList) iis.readObject();
			iis.close();
			ois.close();
		} catch (Exception ioe) {
			JOptionPane.showMessageDialog(null, "Error " + ioe.toString(),
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * The method setPieces simply sets the pieces that were just loaded from
	 * the file back on the board
	 */
	private void setPieces() {
		for (int i = 0; i < _activePieces.size(); i++) {
			if (_activePieces.get(i) == null) {
				continue;
			}

			if (isActivePieceEqualsTo(i, CPiece.WKing)) {
				setNonVisualActivePiece(CPiece.WKing, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BKing)) {
				setNonVisualActivePiece(CPiece.BKing, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BQueen)) {
				setNonVisualActivePiece(CPiece.BQueen, i);

			} else if (isActivePieceEqualsTo(i, CPiece.WQueen)) {
				setNonVisualActivePiece(CPiece.WQueen, i);

			} else if (isActivePieceEqualsTo(i, CPiece.WBishop)) {
				setNonVisualActivePiece(CPiece.WBishop, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BBishop)) {
				setNonVisualActivePiece(CPiece.BBishop, i);

			} else if (isActivePieceEqualsTo(i, CPiece.WKnight)) {
				setNonVisualActivePiece(CPiece.WKnight, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BKnight)) {
				setNonVisualActivePiece(CPiece.BKnight, i);

			} else if (isActivePieceEqualsTo(i, CPiece.WRook)) {
				setNonVisualActivePiece(CPiece.WRook, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BRook)) {
				setNonVisualActivePiece(CPiece.BRook, i);

			} else if (isActivePieceEqualsTo(i, CPiece.WPawn)) {
				setNonVisualActivePiece(CPiece.WPawn, i);

			} else if (isActivePieceEqualsTo(i, CPiece.BPawn)) {
				setNonVisualActivePiece(CPiece.BPawn, i);
			}
		}
	}

	private boolean isActivePieceEqualsTo(final int pos, final String pieceType) {
		return _activePieces.get(pos).isTypeEquals(pieceType);
	}

	private void setNonVisualActivePiece(final String pieceType, final int i) {
		_activePieces.set(i, NonVisualPiece.create(this, pieceType, (i + 1)));
	}

	/**
	 * The method isWinner checks if there is a winner in the game
	 * 
	 * @return boolean value true if isWinner and false otherwise
	 */
	public boolean isWinner() {
		for (int i = 0; i < capturedPieces.size(); i++) {
			final NonVisualPiece piece = capturedPieces.get(i);
			if (piece.getPieceTypeOnly().equals("King")) {
				return true;
			}
		}
		return false;
	}

	public synchronized ArrayList<NonVisualPiece> getCapturedPieces() {
		return capturedPieces;
	}

	/**
	 * The method setCapturedPieces sets ArrayList<Piece>
	 * 
	 * @param capturedPieces
	 *            ArrayList<Piece>
	 */
	public void setCapturedPieces(ArrayList<NonVisualPiece> capturedPieces) {

		this.capturedPieces = capturedPieces;

		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * The getPiecePosition method of this class simply looks for the piece
	 * thats been clicked when it finds the piece with the clickCount equal 1 it
	 * returns that piece position to the caller
	 * 
	 * @return clicked piece position as an integer
	 */
	public int getPiecePosition() {
		NonVisualPiece piece;
		for (int i = 0; i < _activePieces.size(); i++) {
			if (_activePieces.get(i) != null) {
				piece = _activePieces.get(i);
				if (piece.getClickCount() == 1) {
					return piece.getPosition();
				}
			}
		}
		return 0;
	}

	/**
	 * The method isMoveable will check if the current piece can be moved To the
	 * specified position depending on which piece it is and the rules of the
	 * game will be checked in order to play the game as it should be played.
	 * This method will also ensure that pieces do not jump over other pieces
	 * except for the Knight_View which can jump over any piece. This method
	 * will also ensure that pieces capture only opposite color piece.The method
	 * will also verify that the pieces like Rook_View and Queen_View when going left
	 * or right always stay on the same row which is very important because you
	 * do not want your piece jumping rows.
	 * 
	 * @param pos
	 *            current piece position
	 * @param des
	 *            current piece destination
	 * @return true if the piece can be moved false otherwise
	 */
	public boolean isMoveable(final int pos, final int des) {
		NonVisualPiece piece = _activePieces.get(pos - 1);
		return piece.isMoveable(pos, des);
	}

	/**
	 * The method move simply takes two arguments the first one(x) is the
	 * current piece position and the second(y) is the destination of the
	 * current piece The piece will be moved if and only if the isMoveable
	 * method will return true if the method isMoveable returns false that means
	 * the piece is not Movable list array that we have declared in the method
	 * is used to wrap the x and y position to send it to the client or server
	 * through socket if and only if the game is played online
	 * 
	 * @param pos
	 *            current piece position
	 * @param des
	 *            current piece destination
	 */
	public void move(final int pos, final int des) {

		if (_activePieces.get(pos - 1) == null) {
			return;
		}

		if (!this.isMoveable(pos, des)) {
			return;
		}

		makeMove(pos, des);
		notifyObserver(pos, des);

		if (!isWhiteTurn && getPlayerTwo().getPlaySelf()) {
			ArrayList<NonVisualPiece> playerTwoPieces = getPlayerPieces(Color.BLACK);
			ArrayList<NonVisualPiece> playerOnePieces = getPlayerPieces(Color.WHITE);
			
			boolean isKillMade = makeKillMoveIfAvailable(playerTwoPieces,
					playerOnePieces);

			if (!isKillMade) {
				autoMovePiece(playerTwoPieces, 0, playerTwoPieces.size() + 2);
			}
		}
	}

	private void makeMove(final int pos, final int des) {
		NonVisualPiece piece = _activePieces.get(pos - 1);
		piece.makeMove(pos, des);
		isWhiteTurn = piece.isBlack();
	}

	private void notifyObserver(final int pos, final int des) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(pos);
		list.add(des);
		this.setChanged();
		this.notifyObservers(list);
	}

	public ArrayList<NonVisualPiece> shufflePieces(
			ArrayList<NonVisualPiece> pieces) {

		for (int i = 0; i < pieces.size(); i++) {
			NonVisualPiece nvp = pieces.get(i);

			if (nvp == null) {
				continue;
			}
			if (nvp.isTypeEquals(CPiece.Bishop)
					|| nvp.isTypeEquals(CPiece.BBishop)) {
				pieces.remove(i);
			}
		}

		return pieces;
	}

	public boolean makeKillMoveIfAvailable(ArrayList<NonVisualPiece> piecesP2,
			ArrayList<NonVisualPiece> peicesP1) {

		for (int i = 0; i < piecesP2.size(); i++) {
			NonVisualPiece _blackPiece = piecesP2.get(i);

			if (!_blackPiece.canMove()) {
				continue;
			}

			for (int j = 0; j < peicesP1.size(); j++) {
				NonVisualPiece _whitePiece = peicesP1.get(j);

				int pos = _blackPiece.getPosition();
				int des = _whitePiece.getPosition();

				if (_blackPiece.isMoveable(pos, des)) {

					makeMove(pos, des);
					notifyObserver(pos, des);
					return true;
				}
			}
		}

		return false;
	}

	public void autoMovePiece(ArrayList<NonVisualPiece> playerTwoPieces,
			int tryCount, int tryCountLimit) {
		int randomPieceIndex = getRandomNumberInRange(0, playerTwoPieces.size());

		NonVisualPiece nvp = playerTwoPieces.get(randomPieceIndex);

		if (nvp == null) {
			autoMovePiece(playerTwoPieces, tryCount, tryCountLimit);
			return;
		}

		boolean pieceMoved = movePieceIfPossible(nvp, 10);

		if (!pieceMoved && tryCount < tryCountLimit) {
			tryCount++;
			autoMovePiece(playerTwoPieces, tryCount, tryCountLimit);
		}
	}

	public NonVisualPiece getPieceUsingType(ArrayList<NonVisualPiece> pieces,
			String pieceType) {
		for (int i = 0; i < pieces.size(); i++) {
			NonVisualPiece p = pieces.get(i);

			if ((p).isTypeEquals(pieceType)) {
				return p;
			}
		}

		return null;
	}

	public boolean movePieceIfPossible(NonVisualPiece piece, int tryCountLimit) {
		int minDes = 1, maxDes = 64;
		tryCountLimit = maxDes - 1;

		if (!piece.canMove()) {
			return false;
		}
		for (int tryCount = 0; tryCount < tryCountLimit; tryCount++) {
			int randomPosToMoveTo = getRandomNumberInRange(minDes, maxDes);

			if (randomPosToMoveTo == piece.getPosition()) {
				continue;
			}

			boolean canMovePiece = piece.isMoveable(piece.getPosition(),
					randomPosToMoveTo);

			if (canMovePiece) {

				move(piece.getPosition(), randomPosToMoveTo);
				return true;
			}

		}
		return false;
	}

	public ArrayList<NonVisualPiece> getPlayerPieces(Color color) {
		ArrayList<NonVisualPiece> playerPieces = new ArrayList<NonVisualPiece>();

		for (int i = 0; i < _activePieces.size(); i++) {
			NonVisualPiece nvp = _activePieces.get(i);

			if (nvp != null && nvp.getColor() == color) {
				playerPieces.add(nvp);
			}
		}

		return playerPieces;
	}

	public int getRandomNumberInRange(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min)) + min; // + 1

		return randomNum;
	}
}
