package RPG;

import RPG.Character.CharacterInitializer;
import RPG.Data.CharacterData.CharacterDataLoader;
import RPG.Data.RPGData;

/**
 *
 */
public class RPGInitializer {
    public static void initialize() {
        RPGData characterData = new CharacterDataLoader();

        CharacterInitializer.initialize();
    }
}
