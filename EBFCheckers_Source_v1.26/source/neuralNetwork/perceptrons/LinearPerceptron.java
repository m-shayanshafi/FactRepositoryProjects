package neuralNetwork.perceptrons;

import neuralNetwork.Neuron;
import neuralNetwork.Perceptron;

public class LinearPerceptron extends Perceptron {

	public LinearPerceptron(Neuron... inputs) {
		super(inputs);
	}

	@Override
	public float activationDerivative(float input) {
		return 1f;
	}

	@Override
	public float activationFunction(float input) {
		return input;
	}

	@Override
	public float getMaxOutput() {
		return Float.MAX_VALUE;
	}

	@Override
	public float getMinOutput() {
		return -Float.MAX_VALUE;
	}
}
