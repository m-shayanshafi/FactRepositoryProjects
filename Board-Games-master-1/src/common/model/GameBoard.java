package common.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Classe modele generale des 3 jeux
 * 
 * @author LETOURNEUR Léo
 *
 */
public class GameBoard extends Observable implements Serializable {
	private static final long serialVersionUID = 575263890429289200L;
	
	//public Player joueur1;
	//public Player joueur2;
	
	//Les joueurs sont géres grâce à une énumération
	public static enum Cell { 
		EMPTY(" ","   ",0), PLAYER1("X","RED",1), PLAYER2("O","BLU",2);
		public final String pictogram;
		public final String color;
		public final int value;
		  
		Cell(String name, String color, int valeur){
		    this.pictogram = name;
		    this.color = color;
		    this.value = valeur;
		}
	}
	
	protected boolean fin;
	protected int nbCasesGagnante;
	protected String nomJeu;
	protected int pointVictoire;
	private Score score;
	private int previousFirstPlayer;

	public final int WIDTH;
	public final int LENGTH;
	protected int[][] board;
	protected List<Turn> history;
	
	public GameBoard(int width, int length) {
		this.previousFirstPlayer = 2;
		this.score = Score.deserializeScore();
		this.fin = false;
		this.WIDTH = width;
		this.LENGTH = length;
		setBoard(new int[width][length]);
		setHistory(new ArrayList<Turn>());
		for (int row = 0; row < width ; row++) {
		      for (int col = 0; col < length; col++) {
		    	  getBoard()[row][col] = Cell.EMPTY.value;
		      }
		}
	}
	
	public GameBoard(int width, int length, List<Turn> history) {
		this(width,length);
		setHistory(history);
		for(Turn turn : getHistory()) {
			getBoard()[turn.getPosition().getX()][turn.getPosition().getY()] = turn.getPlayer();
		}
	}
	
	/**
	 * Methode ajoute un tour
	 * 
	 */
	public void play(Turn turn) {
		getBoard()[turn.getPosition().getX()][turn.getPosition().getY()] = turn.getPlayer();
		getHistory().add(turn);
	}
	
	/**
	 * Methode qui supprime le dernier tour
	 * 
	 */
	public void cancel() {
		if(lastTurn()!=null)
		{
			getBoard()[lastTurn().getPosition().getX()][lastTurn().getPosition().getY()] = Cell.EMPTY.value;
			getHistory().remove(getHistory().size() - 1);
		}
	}
	
	/**
	 * Methode qui renvoie le dernier tour
	 * 
	 */
	public Turn lastTurn() {
		if(!getHistory().isEmpty())
			return getHistory().get(getHistory().size() - 1);
		return null;
	}
	
	/**
	 * Methode qui renvoie le dernier joueur
	 * 
	 */
	public int lastPlayer() {
		if (getHistory().isEmpty())
			return previousFirstPlayer;
		if(lastTurn()!=null)
			return lastTurn().getPlayer();
		return 0;
	}
	
	/**
	 * Methode qui renvoie le prochain joueur
	 * 
	 */
	public int nextPlayer() {
		
		//Utilisation du modulo ?
		if (lastPlayer() == Cell.PLAYER1.value)
			return Cell.PLAYER2.value;
		else
			return Cell.PLAYER1.value;
	}
	
	/**
	 * Methode static qui permet d'enregistrer une configuration de jeu
	 * 
	 */
	public static boolean serializeHistory(List<Turn> history, String instance) {
		ObjectOutputStream objectWriterStrem = null;

		try {
			FileOutputStream fichier = new FileOutputStream("serializer/history"+instance+".ser");
			objectWriterStrem = new ObjectOutputStream(fichier);
			objectWriterStrem.writeObject(history);
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
		}
		return false;
	}
	
	/**
	 * Methode static qui permet de lire une configuration de jeu
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Turn>  deserializeHistory(String instance) {

		ObjectInputStream objectReadStrem = null;

		ArrayList<Turn> history = new ArrayList<Turn>();
		try {
			File f = new File("serializer/history"+instance+".ser");
			if ( f.exists() ) {
				FileInputStream fichier = new FileInputStream("serializer/history"+instance+".ser");
				objectReadStrem = new ObjectInputStream(fichier);
				Object obj = objectReadStrem.readObject();
				
				if (obj instanceof ArrayList)
					history = (ArrayList<Turn>) obj;
			}
			
		} catch (final java.io.IOException e) { e.printStackTrace();
		} catch (final ClassNotFoundException e) { e.printStackTrace();
		} finally {
			try {
				if (objectReadStrem != null) {
					objectReadStrem.close();
				}
			} catch (final IOException ex) { ex.printStackTrace(); }
		}
		
		return history;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public List<Turn> getHistory() {
		return history;
	}

	public void setHistory(List<Turn> history) {
		this.history = history;
	}
	
	public boolean isFin() {
		return fin;
	}

	public void setFin(boolean fin) {
		this.fin = fin;
	}

	public int getNbCasesGagnante() {
		return nbCasesGagnante;
	}

	public void setNbCasesGagnante(int nbCasesGagnante) {
		this.nbCasesGagnante = nbCasesGagnante;
	}

	public String getNomJeu() {
		return nomJeu;
	}

	public void setNomJeu(String nomJeu) {
		this.nomJeu = nomJeu;
	}

	public int getPreviousFirstPlayer() {
		return previousFirstPlayer;
	}

	public void setPreviousFirstPlayer(int previousFirstPlayer) {
		this.previousFirstPlayer = previousFirstPlayer;
	}

	@Override
	public String toString() {
		return "GameBoard [width=" + WIDTH + ", length=" + LENGTH + ", board=" + Arrays.toString(board) + "]";
	}

	public void afficher() {
		System.out.println("-------");
		for (int row = 0; row < WIDTH; row++) {
			for (int col = 0; col < LENGTH; col++) {
				System.out.print("|" + Cell.values()[getBoard()[row][col]].pictogram);
			}
			System.out.println("|");
		}
		System.out.println("-------");
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
	public int getPointVictoire() {
		return pointVictoire;
	}

	public void setPointVictoire(int pointVictoire) {
		this.pointVictoire = pointVictoire;
	}
}
