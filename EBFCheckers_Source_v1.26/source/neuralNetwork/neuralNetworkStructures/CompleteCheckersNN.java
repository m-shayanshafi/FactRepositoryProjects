package neuralNetwork.neuralNetworkStructures;

import java.lang.reflect.Constructor;

import neuralNetwork.FeedForwardNN;
import neuralNetwork.Neuron;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;

/**
 * A constant {@link NeuronLayer} size Feed-Forward Neural Network without brain
 * damage.
 * 
 * @author Amos Yuen
 * @version 1.10 - 26 September 2008
 * 
 */
public class CompleteCheckersNN extends FeedForwardNN {

	/**
	 * Constructs a constant {@link NeuronLayer} size Feed-Forward Neural
	 * Network without brain damage.
	 * 
	 * @param pClass
	 *            The class of the {@link Perceptron} to be used in this
	 *            {@link FeedForwardNN}.
	 * @param numPerceptrons
	 *            The size of each {@link NeuronLayer}.
	 * @param numLayers
	 *            The number of hidden {@link NeuronLayer}s.
	 */
	public CompleteCheckersNN(Class<? extends Perceptron> pClass,
			int numInputs, int numPerceptrons, int numLayers) {
		super(numInputs);

		try {
			Constructor<? extends Perceptron> c = pClass
					.getConstructor(new Class[] { Neuron[].class });

			Neuron[] lastLayer = inputs;
			for (int i = 0; i < numLayers; i++) {
				Perceptron[] layer = new Perceptron[numPerceptrons];
				for (int j = 0; j < layer.length; j++)
					layer[j] = c.newInstance(new Object[] { lastLayer });
				layers.add(new NeuronLayer<Perceptron>(layer));
				lastLayer = layer;
			}
			layers.trimToSize();

			output = c.newInstance(new Object[] { lastLayer });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		StringBuffer strBuff = new StringBuffer(getClass().getSimpleName());
		strBuff.append('-');
		strBuff.append(output.getClass().getSimpleName());
		strBuff.append('_');
		strBuff.append(layers.get(0).getNumNeurons());
		strBuff.append('x');
		strBuff.append(layers.size());

		return strBuff.toString();
	}
}