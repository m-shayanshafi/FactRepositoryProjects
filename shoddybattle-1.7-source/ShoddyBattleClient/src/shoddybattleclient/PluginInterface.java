/*
 * PluginInterface.java
 *
 * Created on August 19, 2007, 11:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package shoddybattleclient;

import shoddybattle.*;

/**
 * All plugins must adhere to this interface.
 * @author Percival "Dragontamer" Tiglao
 */
public interface PluginInterface {
    
    /**
     * Get the display name of this plugin.
     * @return the name
     */
    public String getName();
    
    /**
     * Invoke this plugin (e.g. by selecting it from the menu).
     * @param mods ModData in play in the TeamBuilder
     * @param pokemon the team currently being edited (can be modified)
     * @param selected the currently selected pokemon in the editor
     */
    public void invoke(ModData mods, Pokemon[] pokemon, int selected);
    
}
