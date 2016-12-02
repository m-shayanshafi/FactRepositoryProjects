package neuralNetwork;


/**
 * An input neuron for that should be used for the first layer of a neural
 * network.
 * 
 * @author Amos Yuen
 * @version 1.00 - 9 July 2008
 */
public class Input implements Neuron {
	protected float error;
	protected float value;
	protected int numErrors;

	@Override
	public void addError(float error) {
		this.error = error;
		numErrors++;
	}

	public void applyBackPropagation(float learningFactor) {
	}

	@Override
	public float fireNeuron() {
		return value;
	}

	@Override
	public float getError() {
		return error / numErrors;
	}

	@Override
	public float getOutput() {
		return value;
	}

	@Override
	public void resetError() {
		error = 0;
		numErrors = 0;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
