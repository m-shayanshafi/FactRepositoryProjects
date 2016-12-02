package neuralNetwork;


/**
 * A basic abstract perceptron that stores its inputs, weights, and output. It
 * handles back propagation training. Subclasses must define the activation
 * function and its derivative and the min and max output and weights.
 * 
 * @author Amos Yuen
 * @version 1.05 - 16 August 2008
 */
public abstract class Perceptron implements Neuron {

	protected float error;
	protected float output;
	protected final Neuron[] inputs;
	protected final float[] weights;
	protected int numErrors;

	public Perceptron(Neuron... inputs) {
		this.inputs = inputs;
		weights = new float[inputs.length];

		float range = getMaxWeight() - getMinWeight();
		for (int i = 0; i < weights.length; i++)
			weights[i] = (float) Math.random() * range + getMinWeight();
	}

	public abstract float activationDerivative(float input);

	public abstract float activationFunction(float input);

	@Override
	public void addError(float error) {
		this.error += error;
		numErrors++;
	}

	public void applyBackPropagation(float learningFactor) {
		float input = getInput();
		// Compute the difference of the input from
		// the expected input using linear approximation.
		float delta = getError() / activationDerivative(input);

		for (byte i = 0; i < weights.length; i++) {
			// Recalculate the weight for this input using the percentage this
			// input contributes to the overall input.
			float inputError = delta * weights[i] / input;
			setWeight(i, weights[i] + inputError * learningFactor);

			// Add this input's percentage of the error. Multiply by
			// (1f - learningFactor) to compensate for the recalculated weight
			inputs[i].addError(inputError * (1f - learningFactor));
		}
	}

	@Override
	public float fireNeuron() {
		output = activationFunction(getInput());
		return output;
	}

	@Override
	public float getError() {
		return error / numErrors;
	}

	public float getInput() {
		float input = 0;
		for (int i = 0; i < inputs.length; i++)
			input += inputs[i].getOutput() * weights[i];
		return input;
	}

	public Neuron getInput(int index) {
		return inputs[index];
	}

	public abstract float getMaxOutput();

	public float getMaxWeight() {
		return 1f;
	}

	public abstract float getMinOutput();

	public float getMinWeight() {
		return 0.001f;
	}

	public int getNumInputs() {
		return inputs.length;
	}

	@Override
	public float getOutput() {
		return output;
	}

	public float getWeight(int index) {
		return weights[index];
	}

	@Override
	public void resetError() {
		error = 0;
		numErrors = 0;
	}

	public void setWeight(int index, float weight) {
		// Check to prevent setting NaN values
		if (weight > 0)
			weights[index] = Math.max(getMinWeight(), Math.min(getMaxWeight(),
					weight));
	}
}