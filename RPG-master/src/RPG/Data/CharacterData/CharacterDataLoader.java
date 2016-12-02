package RPG.Data.CharacterData;

import RPG.Character.Attribute;
import RPG.Character.CharacterType;
import RPG.Data.DataFileType;
import RPG.Data.RPGDataLoader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CharacterDataLoader extends RPGDataLoader {
    private static final DataFileType dataFileType = DataFileType.Character;

    private final Map<CharacterType, AttributeData> attributeDataMap = new HashMap<CharacterType, AttributeData>();

    public CharacterDataLoader() {
        super();
        loadCharacterData();
    }

    private void loadCharacterData() {
        NodeList characterNodeList = dataDocument.getElementsByTagName("character");
        for (int i = 0; i < characterNodeList.getLength(); i++) {
            Node characterNode = characterNodeList.item(i);
            CharacterType characterType = CharacterType.valueOf(getAttributeValue(characterNode, "characterType"));
            attributeDataMap.put(characterType, loadAttributes(characterNode));
        }
    }

    private AttributeData loadAttributes(Node characterNode) {
        AttributeData attributeData = new AttributeData();
        for (Attribute attribute : Attribute.values()) {
            Integer attributeValue = new Integer(getAttributeValue(characterNode, attribute.name(), "value"));
            attributeData.setAttribute(attribute, attributeValue);
        }
        return attributeData;
    }

    protected DataFileType getDataFileType() {
        return dataFileType;
    }
}