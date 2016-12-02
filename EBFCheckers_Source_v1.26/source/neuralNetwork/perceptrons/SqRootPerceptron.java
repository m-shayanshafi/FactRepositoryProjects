package neuralNetwork.perceptrons;

import neuralNetwork.Neuron;
import neuralNetwork.Perceptron;

public class SqRootPerceptron extends Perceptron {

	public SqRootPerceptron(Neuron... inputs) {
		super(inputs);
	}
	
	@Override
	public float activationDerivative(float input) {
		float returnVal = 0.5f / (float)Math.sqrt(Math.abs(input));
		return (input >= 0)? returnVal : -returnVal;
	}

	@Override
	public float activationFunction(float input) {
		return (input >= 0)? (float)Math.sqrt(input) : -(float)Math.sqrt(-input);
	}

	@Override
	public float getMaxOutput() {
		return (float)Math.sqrt(Float.MAX_VALUE);
	}

	@Override
	public float getMinOutput() {
		return -(float)Math.sqrt(Float.MAX_VALUE);
	}
}
