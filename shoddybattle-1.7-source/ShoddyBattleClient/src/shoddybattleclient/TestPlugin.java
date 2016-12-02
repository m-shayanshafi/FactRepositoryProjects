/*
 * TestPlugin.java
 *
 * Created on August 19, 2007, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package shoddybattleclient;
import shoddybattle.*;
import javax.swing.*;

/**
 *
 * @author pr
 */
public class TestPlugin implements PluginInterface {
    
    /** Creates a new instance of TestPlugin */
    public TestPlugin() {
    }
    
    public String getName(){
        return "Calculate Defense Tier";
    }
    
    public void invoke(ModData mods, Pokemon[] pokemon, int selected) {
        if (selected == -1)
            return;
        
        Pokemon pkmn = pokemon[selected];
        double defenceTier = Math.log(pkmn.getStat(Pokemon.S_HP) *
                pkmn.getStat(Pokemon.S_DEFENCE)) / Math.log(1.1);
        double spDefenceTier = Math.log(pkmn.getStat(Pokemon.S_HP)
                * pkmn.getStat(Pokemon.S_SPDEFENCE)) / Math.log(1.1);
        
        defenceTier = ((double)((int) (defenceTier * 100.0))) / 100;
        spDefenceTier = ((double)((int) (spDefenceTier * 100.0))) / 100;
        JOptionPane.showMessageDialog(null, pkmn.getName()
                + " has Def. Tier: " + defenceTier + " and SpDef. Tier: "
                + spDefenceTier);
    }
    
}
