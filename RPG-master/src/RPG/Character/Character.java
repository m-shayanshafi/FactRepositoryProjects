package RPG.Character;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Base class for characters.</p>
 *
 * <p>Definition of attributes.</p>
 */
public abstract class Character {
    protected final static Map<Attribute, Integer> defaultAttributes = new HashMap<Attribute, Integer>();

    private final Map<Attribute, Integer> attributes = new HashMap<Attribute, Integer>();

    public static void init() {
        setDefaultAttributes(null);
    }

    public Integer getAttribute(Attribute attribute) {
        return attributes.get(attribute);
    }

    protected static Map<Attribute, Integer> getDefaultAttributes() {
        return defaultAttributes;
    }

    protected static void setDefaultAttributes(Map<Attribute, Integer> attributes) {
        Map<Attribute, Integer> defaultAttributes = new HashMap<Attribute, Integer>();
        for(Attribute attribute : Attribute.values()) {
            Integer value = attributes.get(attribute);
        }
    }
}
