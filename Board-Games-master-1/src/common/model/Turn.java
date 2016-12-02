package common.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Turn implements Serializable {
	private static final long serialVersionUID = 3427204942834160831L;
	
	private int player;
	private Position position;

	public Turn(int player, Position position) {
		setPlayer(player);
		setPosition(position);
	}

	public static boolean serializeTurn(Turn turn) {
		ObjectOutputStream objectWriterStrem = null;

		try {
			FileOutputStream fichier = new FileOutputStream("serializer/lastTurn.ser");
			objectWriterStrem = new ObjectOutputStream(fichier);
			objectWriterStrem.writeObject(turn);
			objectWriterStrem.flush();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectWriterStrem != null) {
					objectWriterStrem.flush();
					objectWriterStrem.close();
					return true;
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		} return false;
	}

	public static Turn deserializeTurn() {

		ObjectInputStream objectReadStrem = null;

		try {
			FileInputStream fichier = new FileInputStream("serializer/lastTurn.ser");
			objectReadStrem = new ObjectInputStream(fichier);
			Turn turn = (Turn) objectReadStrem.readObject();
			return turn;
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectReadStrem != null) {
					objectReadStrem.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String toString() {
		return "Turn [player=" + player + ", position=" + position.toString() + "]";
	}
}
