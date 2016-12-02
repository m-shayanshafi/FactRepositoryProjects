package neuralNetwork.neuralNetworkStructures;

import neuralNetwork.FeedForwardNN;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;
import neuralNetwork.perceptrons.SigmoidPerceptron;

/**
 * A Feed-Forward Neural Network with 4 hidden layers.
 * 
 * @author Amos Yuen
 * @version 1.00 - 9 July 2008
 */

public class NeuralNetwork4L extends FeedForwardNN {
	public NeuralNetwork4L() {
		super(32);

		// Layer1 Perceptrons
		Perceptron[] layer1 = new Perceptron[32];
		layer1[0] = new SigmoidPerceptron(inputs[0], inputs[4], inputs[5]);
		layer1[1] = new SigmoidPerceptron(inputs[1], inputs[5], inputs[6]);
		layer1[2] = new SigmoidPerceptron(inputs[2], inputs[6], inputs[7]);
		layer1[3] = new SigmoidPerceptron(inputs[3], inputs[7]);
		layer1[4] = new SigmoidPerceptron(inputs[4], inputs[0], inputs[8]);
		layer1[5] = new SigmoidPerceptron(inputs[5], inputs[0], inputs[1],
				inputs[8], inputs[9]);
		layer1[6] = new SigmoidPerceptron(inputs[6], inputs[1], inputs[2],
				inputs[9], inputs[10]);
		layer1[7] = new SigmoidPerceptron(inputs[7], inputs[2], inputs[3],
				inputs[10], inputs[11]);
		layer1[8] = new SigmoidPerceptron(inputs[8], inputs[4], inputs[5],
				inputs[12], inputs[13]);
		layer1[9] = new SigmoidPerceptron(inputs[9], inputs[5], inputs[6],
				inputs[13], inputs[14]);
		layer1[10] = new SigmoidPerceptron(inputs[10], inputs[6], inputs[7],
				inputs[14], inputs[15]);
		layer1[11] = new SigmoidPerceptron(inputs[11], inputs[7], inputs[15]);
		layer1[12] = new SigmoidPerceptron(inputs[12], inputs[8], inputs[16]);
		layer1[13] = new SigmoidPerceptron(inputs[13], inputs[8], inputs[9],
				inputs[16], inputs[17]);
		layer1[14] = new SigmoidPerceptron(inputs[14], inputs[9], inputs[10],
				inputs[17], inputs[18]);
		layer1[15] = new SigmoidPerceptron(inputs[15], inputs[10], inputs[11],
				inputs[18], inputs[19]);
		layer1[16] = new SigmoidPerceptron(inputs[16], inputs[12], inputs[13],
				inputs[20], inputs[21]);
		layer1[17] = new SigmoidPerceptron(inputs[17], inputs[13], inputs[14],
				inputs[21], inputs[22]);
		layer1[18] = new SigmoidPerceptron(inputs[18], inputs[14], inputs[15],
				inputs[22], inputs[23]);
		layer1[19] = new SigmoidPerceptron(inputs[19], inputs[15], inputs[23]);
		layer1[20] = new SigmoidPerceptron(inputs[20], inputs[16], inputs[24]);
		layer1[21] = new SigmoidPerceptron(inputs[21], inputs[16], inputs[17],
				inputs[24], inputs[25]);
		layer1[22] = new SigmoidPerceptron(inputs[22], inputs[17], inputs[18],
				inputs[25], inputs[26]);
		layer1[23] = new SigmoidPerceptron(inputs[23], inputs[18], inputs[19],
				inputs[26], inputs[27]);
		layer1[24] = new SigmoidPerceptron(inputs[24], inputs[20], inputs[21],
				inputs[28], inputs[29]);
		layer1[25] = new SigmoidPerceptron(inputs[25], inputs[21], inputs[22],
				inputs[29], inputs[30]);
		layer1[26] = new SigmoidPerceptron(inputs[26], inputs[22], inputs[23],
				inputs[30], inputs[31]);
		layer1[27] = new SigmoidPerceptron(inputs[27], inputs[23], inputs[31]);
		layer1[28] = new SigmoidPerceptron(inputs[28], inputs[24]);
		layer1[29] = new SigmoidPerceptron(inputs[29], inputs[24], inputs[25]);
		layer1[30] = new SigmoidPerceptron(inputs[30], inputs[25], inputs[26]);
		layer1[31] = new SigmoidPerceptron(inputs[31], inputs[26], inputs[27]);
		layers.add(new NeuronLayer<Perceptron>(layer1));

		// Layer2 Perceptrons
		Perceptron[] layer2 = new Perceptron[18];
		layer2[0] = new SigmoidPerceptron(layer1[5], layer1[0], layer1[1],
				layer1[8], layer1[9]);
		layer2[1] = new SigmoidPerceptron(layer1[6], layer1[1], layer1[2],
				layer1[9], layer1[10]);
		layer2[2] = new SigmoidPerceptron(layer1[7], layer1[2], layer1[3],
				layer1[10], layer1[11]);
		layer2[3] = new SigmoidPerceptron(layer1[8], layer1[4], layer1[5],
				layer1[12], layer1[13]);
		layer2[4] = new SigmoidPerceptron(layer1[9], layer1[5], layer1[6],
				layer1[13], layer1[14]);
		layer2[5] = new SigmoidPerceptron(layer1[10], layer1[6], layer1[7],
				layer1[14], layer1[15]);
		layer2[6] = new SigmoidPerceptron(layer1[13], layer1[8], layer1[9],
				layer1[16], layer1[17]);
		layer2[7] = new SigmoidPerceptron(layer1[14], layer1[9], layer1[10],
				layer1[17], layer1[18]);
		layer2[8] = new SigmoidPerceptron(layer1[15], layer1[10], layer1[11],
				layer1[18], layer1[19]);
		layer2[9] = new SigmoidPerceptron(layer1[16], layer1[12], layer1[13],
				layer1[20], layer1[21]);
		layer2[10] = new SigmoidPerceptron(layer1[17], layer1[13], layer1[14],
				layer1[21], layer1[22]);
		layer2[11] = new SigmoidPerceptron(layer1[18], layer1[14], layer1[15],
				layer1[22], layer1[23]);
		layer2[12] = new SigmoidPerceptron(layer1[21], layer1[16], layer1[17],
				layer1[24], layer1[25]);
		layer2[13] = new SigmoidPerceptron(layer1[22], layer1[17], layer1[18],
				layer1[25], layer1[26]);
		layer2[14] = new SigmoidPerceptron(layer1[23], layer1[18], layer1[19],
				layer1[26], layer1[27]);
		layer2[15] = new SigmoidPerceptron(layer1[24], layer1[20], layer1[21],
				layer1[28], layer1[29]);
		layer2[16] = new SigmoidPerceptron(layer1[25], layer1[21], layer1[22],
				layer1[29], layer1[30]);
		layer2[17] = new SigmoidPerceptron(layer1[26], layer1[22], layer1[23],
				layer1[30], layer1[31]);
		layers.add(new NeuronLayer<Perceptron>(layer2));

		// Layer3 Perceptrons
		Perceptron[] layer3 = new Perceptron[8];
		layer3[0] = new SigmoidPerceptron(layer2[4], layer2[0], layer2[1],
				layer2[6], layer2[7]);
		layer3[1] = new SigmoidPerceptron(layer2[5], layer2[1], layer2[2],
				layer2[7], layer2[8]);
		layer3[2] = new SigmoidPerceptron(layer2[6], layer2[3], layer2[4],
				layer2[9], layer2[10]);
		layer3[3] = new SigmoidPerceptron(layer2[7], layer2[4], layer2[5],
				layer2[10], layer2[11]);
		layer3[4] = new SigmoidPerceptron(layer2[10], layer2[6], layer2[7],
				layer2[12], layer2[13]);
		layer3[5] = new SigmoidPerceptron(layer2[11], layer2[7], layer2[8],
				layer2[13], layer2[14]);
		layer3[6] = new SigmoidPerceptron(layer2[12], layer2[9], layer2[10],
				layer2[15], layer2[16]);
		layer3[7] = new SigmoidPerceptron(layer2[13], layer2[10], layer2[11],
				layer2[16], layer2[17]);
		layers.add(new NeuronLayer<Perceptron>(layer3));

		// Layer4 Perceptrons
		Perceptron[] layer4 = new Perceptron[2];
		layer4[0] = new SigmoidPerceptron(layer3[3], layer3[0], layer3[1],
				layer3[4], layer3[5]);
		layer4[1] = new SigmoidPerceptron(layer3[4], layer3[2], layer3[3],
				layer3[6], layer3[7]);
		layers.add(new NeuronLayer<Perceptron>(layer4));
		layers.trimToSize();

		// Output Layer
		output = new SigmoidPerceptron(layer4);
	}
}
