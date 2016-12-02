package tit07.morris.model;

import tit07.morris.model.config.GameConfig;


/**
 * Gibt an, welche Schnittstellen das Model zur Verf�gung stellen muss, um das
 * Spiel zu konfigurieren und die Konfiguration abzufragen.
 */
public interface Configurateable {

    /**
     * Gibt die aktuelle Konfiguration des Spiels zur�ck.
     */
    public GameConfig getConfig();
}
