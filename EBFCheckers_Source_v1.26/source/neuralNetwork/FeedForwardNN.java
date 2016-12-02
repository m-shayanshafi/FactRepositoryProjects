package neuralNetwork;

import java.util.ArrayList;


import checkersMain.CheckersBoard;

/**
 * An abstract Feed-Forward neural network. It handles inputs, firing of
 * neurons, evaluation boards, and saving and loading neural networks.
 * Subclasses should create the actual neurons, define the topography of the
 * neural network and set the initial weights.
 * <p>
 * <b>Note:</b> Subclasses should create NeuronLayers whose neurons have inputs from the
 * layer before. The NeuronLayers should be added to the layers list. And the
 * output neuron should have the last NeuronLayer as its inputs.
 * 
 * @author Amos Yuen
 * @version 1.10 - 26 September 2008
 */

public abstract class FeedForwardNN {
	protected Input[] inputs;
	protected ArrayList<NeuronLayer<Perceptron>> layers;

	protected Perceptron output;

	public FeedForwardNN(int numInputs) {
		layers = new ArrayList<NeuronLayer<Perceptron>>();

		// Layer 0 (Input Layer)
		inputs = new Input[numInputs];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = new Input();
	}

	public void applyBackPropagation(CheckersBoard board, float learningFactor,
			float error) {
		if (learningFactor < 0 || learningFactor > 1)
			throw new IllegalArgumentException();

		// Reset Error
		for (Input input : inputs)
			input.resetError();
		for (NeuronLayer<Perceptron> layer : layers)
			layer.resetError();
		output.resetError();

		// Calculate the output delta
		// MSE = 1/2 * Sum of [(expected - actual)^2];
		// W(new) = W(old) + (learning factor)*(expected - actual) * input
		output.addError(error);
		output.applyBackPropagation(learningFactor);

		for (int i = layers.size() - 1; i >= 0; i--)
			layers.get(i).applyBackPropagation(learningFactor);

		// Apply Back Propagation for kingWeight
		// Get average delta and error
		/*
		 * float kingInput = 0; for(int i = 0; i < inputs.length; i++) { byte
		 * pieceType = board.getSpacePos(i); float weight =
		 * getWeight(pieceType); if(pieceType == CheckersBoard.PLAYER1_KING ||
		 * pieceType == CheckersBoard.PLAYER2_KING) { kingInput += weight; error
		 * += inputs[i].getError(); } } error /= inputs.length;
		 * 
		 * float newKingWeight = Math.max(1.0f, kingWeight + learningFactor
		 * 0.25f kingInput / inputs.length error); if(newKingWeight !=
		 * Float.NaN) kingWeight = newKingWeight;
		 */
	}

	public void setInput(int index, float inputValue) {
		inputs[index].setValue(inputValue);
	}

	public void setInputs(float[] inputValues) {
		for (int i = 0; i < inputValues.length; i++)
			setInput(i, inputValues[i]);
	}

	public float getValue() {
		// Hidden Layers
		for (NeuronLayer<Perceptron> layer : layers)
			layer.fireNeuronLayer();

		// Output Layer
		return output.fireNeuron();
	}

	public float getValue(float[] inputValues) {
		// Input Layer
		setInputs(inputValues);

		return getValue();
	}

	public NeuronLayer<Perceptron> getLayer(int layer) {
		return layers.get(layer);
	}

	public float getMaxOutput() {
		return Math.min(10f, output.getMaxOutput());
	}

	public float getMinOutput() {
		return Math.max(-10f, output.getMinOutput());
	}

	public int getNumLayers() {
		return layers.size();
	}

	public Perceptron getOutputLayer() {
		return output;
	}

	public String toString() {
		StringBuffer strBuff = new StringBuffer(getClass().getSimpleName());
		strBuff.append('-');
		strBuff.append(output.getClass().getSimpleName());

		return strBuff.toString();
	}
}