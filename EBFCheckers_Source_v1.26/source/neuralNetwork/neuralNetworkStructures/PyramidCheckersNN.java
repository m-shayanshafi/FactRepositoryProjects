package neuralNetwork.neuralNetworkStructures;

import java.lang.reflect.Constructor;

import neuralNetwork.FeedForwardNN;
import neuralNetwork.Neuron;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;

/**
 * A Feed-Forward Neural Network without brain-damage that is constructed in a pyramid shape.
 * 
 * @author Amos Yuen
 * @version 1.03 - 26 September 2008
 */

public class PyramidCheckersNN extends FeedForwardNN {

	/**
	 * Constructs a Feed-Forward Neural Network without brain-damage with a pyramidal topography.
	 * 
	 * @param pClass
	 *            The class of the {@link Perceptron} to be used in this
	 *            {@link FeedForwardNN}.
	 * @param initNumPerceptrons
	 *            The size of the first {@link NeuronLayer}.
	 * @param multiplyFactor
	 *            The factor that is multiplied with the {@code initNumPerceptrons} to
	 *            get the size of consecutive {@link NeuronLayer}s.
	 */
	public PyramidCheckersNN(Class<? extends Perceptron> pClass,
			int initNumPerceptrons, float multiplyFactor) {
		super((int) (Math.log(initNumPerceptrons) / Math
				.log(1f / multiplyFactor)));

		try {
			Constructor<? extends Perceptron> c = pClass
					.getConstructor(new Class[] { Neuron[].class });

			Neuron[] lastLayer = inputs;
			while (initNumPerceptrons > 1) {
				Perceptron[] layer = new Perceptron[initNumPerceptrons];
				for (int j = 0; j < layer.length; j++)
					layer[j] = c.newInstance(new Object[] { lastLayer });
				layers.add(new NeuronLayer<Perceptron>(layer));
				lastLayer = layer;

				initNumPerceptrons = Math.min(initNumPerceptrons - 1,
						(int) (initNumPerceptrons * multiplyFactor));
			}

			output = c.newInstance(new Object[] { lastLayer });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer(getClass().getSimpleName());
		strBuff.append('-');
		strBuff.append(output.getClass().getSimpleName());
		strBuff.append('_');
		for(int i = 0; i < layers.size(); i++) {
			strBuff.append(layers.get(i).getNumNeurons());
			if(i < layers.size() - 1)
				strBuff.append(',');
		}
		
		return strBuff.toString();
	}
}
