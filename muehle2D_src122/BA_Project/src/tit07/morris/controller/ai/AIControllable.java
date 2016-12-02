package tit07.morris.controller.ai;

/**
 * Schnittstellen, welche die KI zu ihrer Steuerung bereitstellen muss.
 */
public interface AIControllable {

    /**
     * Startet die KI
     */
    public void start();

    /**
     * Unterbricht die KI
     */
    public void stop();
}
