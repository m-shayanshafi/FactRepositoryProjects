package neuralNetwork;


public class NeuronLayer<E extends Neuron> {

	private E[] neurons;

	public NeuronLayer(E... neurons) {
		this.neurons = neurons;
	}

	public void applyBackPropagation(float learningFactor) {
		for (Neuron n : neurons)
			n.applyBackPropagation(learningFactor);
	}

	public void resetError() {
		for (Neuron n : neurons)
			n.resetError();
	}

	public void fireNeuronLayer() {
		for (Neuron n : neurons)
			n.fireNeuron();
	}

	public E getNeuron(int index) {
		return neurons[index];
	}

	public int getNumNeurons() {
		return neurons.length;
	}
}
