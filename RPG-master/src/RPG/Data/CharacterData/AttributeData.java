package RPG.Data.CharacterData;

import RPG.Character.Attribute;
import RPG.Character.CharacterType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ttr
 * Date: 2012-11-23
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class AttributeData {
    private final Map<Attribute, Integer> attributes = new HashMap<Attribute, Integer>();

    protected void setAttribute(Attribute attribute, Integer value) {
        attributes.put(attribute, value);
    }

    protected Integer getAttribute(Attribute attribute) {
        return attributes.get(attribute);
    }
}
