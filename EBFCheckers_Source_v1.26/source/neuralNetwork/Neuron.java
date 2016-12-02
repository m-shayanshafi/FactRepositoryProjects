package neuralNetwork;

/**
 * A basic interface for a Neuron.
 * 
 * @author Amos Yuen
 * @version 1.00 - 9 July 2008
 */
public interface Neuron {

	/**
	 * Adds an error factor.
	 * 
	 * @param error
	 *            - the error factor
	 */
	public void addError(float error);

	public void applyBackPropagation(float learningFactor);

	/**
	 * Calculates the output of this neuron using its inputs and stores and
	 * returns the calculated output.
	 * 
	 * @return the calculated output
	 */
	public float fireNeuron();

	/**
	 * Returns the average error.
	 * 
	 * @return the average error
	 */
	public float getError();

	/**
	 * Returns the stored output of this neuron.
	 * 
	 * @return the stored output of this neuron
	 */
	public float getOutput();

	/**
	 * Resets the error factor.
	 */
	public void resetError();
}
