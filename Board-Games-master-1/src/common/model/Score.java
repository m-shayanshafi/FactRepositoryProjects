package common.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe modele des scores
 * 
 * @author LETOURNEUR Léo
 *
 */
public class Score implements Serializable{
	private static final long serialVersionUID = 1064656934039727537L;

	private int scorePlayer1;
	private int scorePlayer2;
	
	public Score()
	{
		setScorePlayer1(0);
		setScorePlayer2(0);
	}
	
	/**
	 * Methode qui permet d'incrementer un score
	 * 
	 */
	public void add(int player, int score)
	{
		if(player==1)
			setScorePlayer1(getScorePlayer1() + score);
		else
			setScorePlayer2(getScorePlayer2() + score);
	}
	
	/**
	 * Methode static qui permet de supprimer les scores
	 * 
	 */
	public static void eraseScore()
	{
		Score.serializeScore(new Score());
	}

	/**
	 * Methode static qui permet d'enregistrer les scores
	 * 
	 */
	public static boolean serializeScore(Score score) {
		ObjectOutputStream objectWriterStrem = null;

		try {
			FileOutputStream fichier = new FileOutputStream("serializer/score.ser");
			objectWriterStrem = new ObjectOutputStream(fichier);
			objectWriterStrem.writeObject(score);
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

	/**
	 * Methode static qui permet de lire les scores
	 * 
	 */
	public static Score deserializeScore() {

		ObjectInputStream objectReadStrem = null;

		try {
			FileInputStream fichier = new FileInputStream("serializer/score.ser");
			objectReadStrem = new ObjectInputStream(fichier);
			Score score = (Score) objectReadStrem.readObject();
			return score;
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
		return new Score();
	}
	
	public int getScorePlayer1() {
		return scorePlayer1;
	}

	public void setScorePlayer1(int scorePlayer1) {
		this.scorePlayer1 = scorePlayer1;
	}

	public int getScorePlayer2() {
		return scorePlayer2;
	}

	public void setScorePlayer2(int scorePlayer2) {
		this.scorePlayer2 = scorePlayer2;
	}
	
	
}
