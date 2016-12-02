package inf101.games;


/**
 * Abstrakt superklasse for spill, som implementerer en del av
 * av de enkle metodene.
 * 
 * @author Anya Helene Bagge
 *
 */
public abstract class AbstractGame implements IGame {
	private int width;
	private int height;
	
	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean hasStepButton() {
		return true;
	}

	@Override
	public boolean hasStartStopButtons() {
		return true;
	}

	@Override
	public boolean canChangeSize() {
		return false;
	}
}
