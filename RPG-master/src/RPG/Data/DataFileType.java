package RPG.Data;

/**
 * Created with IntelliJ IDEA.
 * User: ttr
 * Date: 2012-11-21
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public enum DataFileType {
    Character("character.xml"),
    Weapon("weapon.xml"),
    Spell("spell.xml");

    private String fileName;

    DataFileType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
