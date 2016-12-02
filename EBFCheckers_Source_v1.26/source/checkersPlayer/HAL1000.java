package checkersPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;


import neuralNetwork.FeedForwardNN;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;
import neuralNetwork.neuralNetworkStructures.PyramidCheckersNN;
import neuralNetwork.perceptrons.LinearPerceptron;
import checkersMain.CheckersBoard;
import checkersMain.CheckersGameManager;

/**
 * The HAL1000 is an advanced learning Artificial Intelligence for Checkers. It
 * uses a depth-limited search and a mini-max algorithm with alpha-beta pruning.
 * It uses a neural network to evaluate the board.
 * 
 * @author Amos Yuen and Louis Wang
 * @version {@value #VERSION}
 */

public class HAL1000 extends HAL900 {
	protected static class CheckersNN {
		private float kingValue;
		private FeedForwardNN neuralNetwork;

		public CheckersNN(FeedForwardNN neuralNetwork) {
			super();
			this.neuralNetwork = neuralNetwork;
			kingValue = 1.5f;
		}

		public void applyBackPropagation(CheckersBoard board,
				float learningFactor, float error) {
			neuralNetwork.applyBackPropagation(board, learningFactor, error);
		}

		@Override
		public boolean equals(Object obj) {
			return neuralNetwork.equals(obj);
		}

		public float getKingValue() {
			return kingValue;
		}

		public NeuronLayer<Perceptron> getLayer(int layer) {
			return neuralNetwork.getLayer(layer);
		}

		public float getMaxOutput() {
			return neuralNetwork.getMaxOutput();
		}

		public float getMinOutput() {
			return neuralNetwork.getMinOutput();
		}

		public FeedForwardNN getNeuralNetwork() {
			return neuralNetwork;
		}

		public int getNumLayers() {
			return neuralNetwork.getNumLayers();
		}

		public Perceptron getOutputLayer() {
			return neuralNetwork.getOutputLayer();
		}

		public float getValue() {
			return neuralNetwork.getValue();
		}

		public float getValue(CheckersBoard board) {
			for (int i = 0; i < 32; i++) {
				switch (board.getPiece(i)) {
				case CheckersBoard.PLAYER1_CHECKER:
					neuralNetwork.setInput(i, 1f);
					break;
				case CheckersBoard.PLAYER2_CHECKER:
					neuralNetwork.setInput(i, -1f);
					break;
				case CheckersBoard.PLAYER1_KING:
					neuralNetwork.setInput(i, kingValue);
					break;
				case CheckersBoard.PLAYER2_KING:
					neuralNetwork.setInput(i, -kingValue);
					break;
				}
			}

			return neuralNetwork.getValue();
		}

		public float getValue(float[] inputValues) {
			return neuralNetwork.getValue(inputValues);
		}

		@Override
		public int hashCode() {
			return neuralNetwork.hashCode();
		}

		public void setInput(int index, float inputValue) {
			neuralNetwork.setInput(index, inputValue);
		}

		public void setInputs(float[] inputValues) {
			neuralNetwork.setInputs(inputValues);
		}

		public void setKingValue(float kingValue) {
			this.kingValue = kingValue;
		}

		public void setNeuralNetwork(FeedForwardNN neuralNetwork) {
			if (neuralNetwork != null)
				this.neuralNetwork = neuralNetwork;
		}

		@Override
		public String toString() {
			return neuralNetwork.toString();
		}
	}

	protected static final float DRAW_VALUE = 0f;

	protected static final String HEURISTICS_FILE_PATH = "_Heuristics.txt";
	protected static final float LEARNING_FACTOR = 0.001f;

	public static final String VERSION = "1.13 - 26 September 2008";

	private static synchronized void backPropagation(CheckersNN neuralNetwork,
			List<CheckersBoard> oldMoves, CheckersPlayerEvent cpe) {
		if (cpe.gameOutcome == CheckersGameManager.GAME_IN_PROGRESS
				|| cpe.gameOutcome == CheckersGameManager.INTERRUPTED)
			return;

		training4(neuralNetwork, oldMoves, cpe.gameOutcome, LEARNING_FACTOR);

		saveNeuralNetwork(neuralNetwork);
	}

	public static float getExpectedValue(CheckersBoard board) {
		float player1Value = 0, player2Value = 0;

		for (int i = 0; i < 32; i++) {
			switch (board.getPiece(i)) {
			case CheckersBoard.PLAYER1_CHECKER:
				player1Value += CHECKER_VALUE + i / 4 * CHECKER_ROW_FACTOR;
				break;
			case CheckersBoard.PLAYER1_KING:
				player1Value += KING_VALUE;
				break;
			case CheckersBoard.PLAYER2_CHECKER:
				player2Value -= CHECKER_VALUE + i / 4 * CHECKER_ROW_FACTOR;
				break;
			case CheckersBoard.PLAYER2_KING:
				player2Value -= KING_VALUE;
				break;
			}
		}

		return player1Value + player2Value * ENEMY_FACTOR;
	}

	public static synchronized boolean loadNeuralNetwork(
			CheckersNN neuralNetwork) {
		File file = new File(neuralNetwork.toString() + HEURISTICS_FILE_PATH);
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			StringTokenizer tokens = new StringTokenizer(br.readLine());
			neuralNetwork.setKingValue(Float.valueOf(tokens.nextToken()));
			br.readLine();

			for (int l = 0; l < neuralNetwork.getNumLayers(); l++) {
				NeuronLayer<Perceptron> layer = neuralNetwork.getLayer(l);
				for (int n = 0; n < layer.getNumNeurons(); n++) {
					Perceptron p = layer.getNeuron(n);
					tokens = new StringTokenizer(br.readLine());

					for (int w = 0; w < p.getNumInputs(); w++)
						p.setWeight(w, Float.valueOf(tokens.nextToken()));
				}
				br.readLine();
			}

			tokens = new StringTokenizer(br.readLine());
			Perceptron output = neuralNetwork.getOutputLayer();
			for (int w = 0; w < output.getNumInputs(); w++)
				output.setWeight(w, Float.valueOf(tokens.nextToken()));

			br.close();
			isr.close();
			fis.close();
			System.out.println("File Loaded - " + file.toURI().getPath());
			return true;
		} catch (Exception e) {
			System.out.println("ERROR: Loading File - "
					+ file.toURI().getPath());
			return false;
		}
	}

	public static synchronized boolean saveNeuralNetwork(
			CheckersNN neuralNetwork) {
		File file = new File(neuralNetwork.toString() + HEURISTICS_FILE_PATH);
		try {
			PrintStream pStream = new PrintStream(file);
			pStream.print(neuralNetwork.getKingValue());
			pStream.println();
			pStream.println();

			for (int l = 0; l < neuralNetwork.getNumLayers(); l++) {
				NeuronLayer<Perceptron> layer = neuralNetwork.getLayer(l);
				for (int n = 0; n < layer.getNumNeurons(); n++) {
					Perceptron p = layer.getNeuron(n);
					for (int w = 0; w < p.getNumInputs(); w++)
						pStream.print(p.getWeight(w) + " ");
					pStream.println();
				}
				pStream.println();
			}

			Perceptron output = neuralNetwork.getOutputLayer();
			for (int w = 0; w < output.getNumInputs(); w++)
				pStream.print(output.getWeight(w) + " ");

			pStream.flush();
			pStream.close();
			System.out.println("File Saved - " + file.toURI().getPath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("ERROR: Saving File - " + file.toURI().getPath());
			return false;
		}
	}

	/**
	 * This training goes through each CheckersBoard in the moves list and
	 * computes the error based on the value they would be assigned using the
	 * getExpectedValue(CheckersBoard board) method. Boards earlier in the game
	 * will have their calculated error reduced by a percentage.
	 * 
	 * @param neuralNetwork
	 *            - the {@link FeedForwardNN} to be trained
	 * @param moves
	 *            - a list of {@link CheckersBoard}s from the Checkers game in
	 *            this player's perspective
	 * @param gameOutcome
	 *            - the outcome of the game, where this player is Player1
	 * @param learningFactor
	 *            - the learning factor
	 */
	public static void training1(CheckersNN neuralNetwork,
			List<CheckersBoard> moves, int gameOutcome, float learningFactor) {
		float meanSquaresError = 0;
		int i = moves.size() - 1;
		ListIterator<CheckersBoard> iter = moves.listIterator(moves.size());
		while (iter.hasPrevious()) {
			CheckersBoard board = iter.previous();
			float expectedValue = Math.max(neuralNetwork.getMinOutput(), Math
					.min(neuralNetwork.getMaxOutput(),
							getExpectedValue(board) / 7.5f));

			float value = neuralNetwork.getValue();
			float error = (expectedValue - value)
					* (0.1f + 0.9f * (i + 1f) / moves.size());
			meanSquaresError += error * error;

			// System.out.println("Expected Value: " + expectedValue +
			// ", value: "
			// + value + " error: " + error);
			neuralNetwork.applyBackPropagation(board, learningFactor, error);
			i--;
		}

		meanSquaresError *= 0.5f;
		System.out.println("Mean-Squares Error: " + meanSquaresError);
	}

	/**
	 * This training goes through each CheckersBoard in the moves list and
	 * computes the error depending on the gameOutcome. Boards earlier in the
	 * game will have their calculated error reduced by a percentage.
	 * 
	 * @param neuralNetwork
	 *            - the {@link FeedForwardNN} to be trained
	 * @param moves
	 *            - a list of {@link CheckersBoard}s from the Checkers game in
	 *            this player's perspective
	 * @param gameOutcome
	 *            - the outcome of the game, where this player is Player1
	 * @param learningFactor
	 *            - the learning factor
	 */
	public static void training2(CheckersNN neuralNetwork,
			List<CheckersBoard> moves, int gameOutcome, float learningFactor) {
		float expectedValue;
		if (gameOutcome == CheckersGameManager.PLAYER1_WINS)
			expectedValue = neuralNetwork.getMaxOutput();
		else if (gameOutcome == CheckersGameManager.PLAYER2_WINS)
			expectedValue = neuralNetwork.getMinOutput();
		else
			expectedValue = (neuralNetwork.getMaxOutput() + neuralNetwork
					.getMinOutput()) * 0.5f;

		System.out.println("Expected Value: " + expectedValue);
		float meanSquaresError = 0;
		int i = moves.size() - 1;
		ListIterator<CheckersBoard> iter = moves.listIterator(moves.size());
		while (iter.hasPrevious()) {
			CheckersBoard board = iter.previous();
			float value = neuralNetwork.getValue();
			float error = (expectedValue - value)
					* (0.1f + 0.9f * (i + 1f) / moves.size());
			meanSquaresError += error * error;

			// System.out.println("Value: " + value + " error: " + error);
			neuralNetwork.applyBackPropagation(board, learningFactor, error);
			i--;
		}

		meanSquaresError *= 0.5f;
		System.out.println("Mean-Squares Error: " + meanSquaresError);
	}

	/**
	 * This training is a combination of training1 and trianing2
	 * 
	 * @param neuralNetwork
	 *            - the {@link FeedForwardNN} to be trained
	 * @param moves
	 *            - a list of {@link CheckersBoard}s from the Checkers game in
	 *            this player's perspective
	 * @param gameOutcome
	 *            - the outcome of the game, where this player is Player1
	 * @param learningFactor
	 *            - the learning factor
	 */
	public static void training3(CheckersNN neuralNetwork,
			List<CheckersBoard> moves, int gameOutcome, float learningFactor) {
		float expectedValue1;
		if (gameOutcome == CheckersGameManager.PLAYER1_WINS)
			expectedValue1 = neuralNetwork.getMaxOutput();
		else if (gameOutcome == CheckersGameManager.PLAYER2_WINS)
			expectedValue1 = neuralNetwork.getMinOutput();
		else
			expectedValue1 = (neuralNetwork.getMaxOutput() + neuralNetwork
					.getMinOutput()) * 0.5f;

		System.out.println("Expected Value1: " + expectedValue1);
		float meanSquaresError1 = 0, meanSquaresError2 = 0;
		int i = moves.size() - 1;
		ListIterator<CheckersBoard> iter = moves.listIterator(moves.size());
		while (iter.hasPrevious()) {
			CheckersBoard board = iter.previous();
			float value = neuralNetwork.getValue();
			float error1 = (expectedValue1 - value)
					* (0.1f + 0.9f * (i + 1f) / moves.size());
			meanSquaresError1 += error1 * error1;

			float expectedValue2 = Math.max(neuralNetwork.getMinOutput(), Math
					.min(neuralNetwork.getMaxOutput(),
							getExpectedValue(board) / 7.5f));
			float error2 = (expectedValue2 - value)
					* (0.1f + 0.9f * (i + 1f) / moves.size());
			meanSquaresError2 += error2 * error2;

			// System.out.println("Value: " + value + " error: " + error);
			neuralNetwork.applyBackPropagation(board, 0.25f * learningFactor,
					error1);
			neuralNetwork.applyBackPropagation(board, 0.75f * learningFactor,
					error2);
			i--;
		}

		meanSquaresError1 *= 0.5f;
		meanSquaresError2 *= 0.5f;
		System.out.println("Mean-Squares Error1: " + meanSquaresError1
				+ " Mean-Squares Error2: " + meanSquaresError2);
	}

	/**
	 * This is a reduced training2, that just does training for the last board.
	 * 
	 * @param neuralNetwork
	 *            - the {@link FeedForwardNN} to be trained
	 * @param moves
	 *            - a list of {@link CheckersBoard}s from the Checkers game in
	 *            this player's perspective
	 * @param gameOutcome
	 *            - the outcome of the game, where this player is Player1
	 * @param learningFactor
	 *            - the learning factor
	 */
	public static void training4(CheckersNN neuralNetwork,
			List<CheckersBoard> moves, int gameOutcome, float learningFactor) {

		CheckersBoard board = moves.get(moves.size() - 1);
		float expectedValue;
		if (gameOutcome == CheckersGameManager.PLAYER1_WINS)
			expectedValue = neuralNetwork.getMaxOutput();
		else if (gameOutcome == CheckersGameManager.PLAYER2_WINS)
			expectedValue = neuralNetwork.getMinOutput();
		else
			expectedValue = (neuralNetwork.getMaxOutput() + neuralNetwork
					.getMinOutput()) * 0.5f;

		float value = neuralNetwork.getValue();
		float error = expectedValue - value;
		float meanSquaresError = 0.5f * error * error;
		System.out.println("Value: " + value);
		System.out.println("Expected Value: " + expectedValue);

		// System.out.println("Value: " + value + " error: " + error);
		neuralNetwork.applyBackPropagation(board, learningFactor, error);

		System.out.println("Mean-Squares Error: " + meanSquaresError);
	}

	protected float kingValue = 1.5f;

	protected CheckersNN neuralNetwork = new CheckersNN(new PyramidCheckersNN(
			LinearPerceptron.class, 32, 0.5f));

	protected List<CheckersBoard> oldMoves;

	public HAL1000() {
		super();
	}

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		oldMoves.add(cpe.board);
		return super.choosePlyIndex(cpe);
	}

	@Override
	protected float evaluateBoard(CheckersBoard board, boolean max,
			int minMoves, int maxMoves, int levels) {
		return neuralNetwork.getValue();
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
		super.gameEnded(cpe);
		System.out.println(getName() + " - Apply Back Propagation");
		backPropagation(neuralNetwork, oldMoves, cpe);
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
		super.gameStarted(cpe);
		oldMoves = new LinkedList<CheckersBoard>();

		loadNeuralNetwork(neuralNetwork);
	}

	@Override
	public String getDescription() {
		return "An advanced learning Checkers AI that uses a depth-limited"
				+ " search with a mini-max algorithm and alpha-beta pruning."
				+ " It also uses a unique heuristic in combination with a"
				+ " neural network, and it dynamically modifies its search depth."
				+ "\n\nAuthor: Amos Yuen and Louis Wang\nVersion: " + VERSION;
	}
}
