package neuralNetwork.neuralNetworkStructures;

import neuralNetwork.FeedForwardNN;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;
import neuralNetwork.perceptrons.SigmoidPerceptron;

/**
 * A Feed-Forward Neural Network with 5 hidden layers.
 * 
 * @author Amos Yuen
 * @version 1.00 - 16 August 2008
 */

public class NeuralNetwork3L extends FeedForwardNN {
	public NeuralNetwork3L() {
		super(32);

		// Layer1 Perceptrons
		Perceptron[] layer1 = new Perceptron[32];
		for(int i = 0; i < layer1.length; i++)
			layer1[i] = new SigmoidPerceptron(inputs);
		layers.add(new NeuronLayer<Perceptron>(layer1));

		// Layer2 Perceptrons
		Perceptron[] layer2 = new Perceptron[8];
		for(int i = 0; i < layer2.length; i++)
			layer2[i] = new SigmoidPerceptron(layer1);
		layers.add(new NeuronLayer<Perceptron>(layer2));

		// Layer3 Perceptrons
		Perceptron[] layer3 = new Perceptron[2];
		for(int i = 0; i < layer3.length; i++)
			layer3[i] = new SigmoidPerceptron(layer2);
		layers.add(new NeuronLayer<Perceptron>(layer3));
		layers.trimToSize();

		// Output Layer
		output = new SigmoidPerceptron(layer3);
	}
}
