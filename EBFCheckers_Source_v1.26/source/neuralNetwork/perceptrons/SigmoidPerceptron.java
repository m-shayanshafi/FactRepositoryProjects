package neuralNetwork.perceptrons;

import neuralNetwork.Neuron;
import neuralNetwork.Perceptron;

/**
 * A basic perceptron that uses a modified sigmoid function for its activation
 * function. The sigmoid function's range is stretched by a factor of 2 and
 * decreased by 1, so as to make the sigmoid function's range from -1 to 1.
 * <p>
 * Note: The input should be <= 27, or the activation derivative will return 0.
 * Also a very negative number may cause other unforseen problems.
 * 
 * @author Amos Yuen
 * @version 1.00 - 9 July 2008
 */
public class SigmoidPerceptron extends Perceptron {
	
	public static final float MIN_DERIVATIVE = 0.00001f;
	
	public SigmoidPerceptron(Neuron... inputs) {
		super(inputs);
	}

	@Override
	public float activationDerivative(float input) {
		float function = activationFunction(input);
		return Math.max(MIN_DERIVATIVE, function * (1f - function));
	}

	@Override
	public float activationFunction(float input) {
		return 1f / (1f + (float) Math.pow(Math.E, -input));
	}

	@Override
	public float getMaxOutput() {
		return 1.0f;
	}

	@Override
	public float getMinOutput() {
		return 0f;
	}
}