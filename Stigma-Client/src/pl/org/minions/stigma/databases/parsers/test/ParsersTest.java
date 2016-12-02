/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.databases.parsers.test;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.org.minions.stigma.databases.actor.wrappers.ArchetypeWrapper;
import pl.org.minions.stigma.databases.actor.wrappers.ProficiencyWrapper;
import pl.org.minions.stigma.databases.item.wrappers.ArmorModifierWrapper;
import pl.org.minions.stigma.databases.item.wrappers.ModifierCategoryWrapper;
import pl.org.minions.stigma.databases.item.wrappers.WeaponModifierWrapper;
import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.parsers.ParserFactory;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.AttackType;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Proficiency;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.stigma.game.item.LogicalSlotType;
import pl.org.minions.stigma.game.item.modifier.ArmorModifier;
import pl.org.minions.stigma.game.item.modifier.ModifierCategory;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.game.item.type.data.ArmorTypeData;
import pl.org.minions.stigma.game.item.type.data.OtherTypeData;
import pl.org.minions.stigma.game.item.type.data.WeaponAttackData;
import pl.org.minions.stigma.game.item.type.data.WeaponTypeData;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.data.MapTypeData;
import pl.org.minions.stigma.game.map.data.TerrainSetData;
import pl.org.minions.stigma.game.map.data.TerrainTypeData;
import pl.org.minions.stigma.game.map.data.TileTypeData;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.server.item.ItemDescription;
import pl.org.minions.stigma.server.item.StaticItems;
import pl.org.minions.stigma.server.npc.NpcDescription;
import pl.org.minions.stigma.server.npc.StaticNpcs;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

// this is test, it doesn't have to be very 'clean'
// CHECKSTYLE:OFF

/**
 * Class containing tests for MapType and TerrainSet
 * parsers.
 */
public class ParsersTest
{
    public class RandomString
    {
        private final char[] symbols = new char[36];

        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length)
        {
            for (int idx = 0; idx < 10; ++idx)
                symbols[idx] = (char) ('0' + idx);
            for (int idx = 10; idx < 36; ++idx)
                symbols[idx] = (char) ('a' + idx - 10);
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString()
        {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Log.initialize("log_config.properties", "ParsersTest");

        SchemaGenerator.generateAll();

        ParsersTest test = new ParsersTest();

        // initialize data:
        test.createMaptype();
        test.createTerrainSet();
        test.createArchetype();
        test.createNpcLayer();
        test.createWeaponType();
        test.createArmorType();
        test.createOtherType();
        test.createItemLayer();
        test.createArmorModifier();
        test.createWeaponModifier();
        test.createProficiency();
        test.createModifierCategory();

        // test
        ArchetypeWrapper wrapper =
                new ArchetypeWrapper(test.getTestArchetype());
        test.testParser("archetype",
                        "archetype.xml",
                        ArchetypeWrapper.class,
                        wrapper);

        test.testParser("proficiency",
                        "proficiency.xml",
                        ProficiencyWrapper.class,
                        test.getTestProficiencyWrapper());
        test.testParser("modcat",
                        "modcat.xml",
                        ModifierCategoryWrapper.class,
                        test.getModifierCategory());
        test.testParser("maptype",
                        "mapType.xml",
                        MapTypeData.class,
                        test.getTestMapType());
        test.testParser("terrainset",
                        "terrainset.xml",
                        TerrainSetData.class,
                        test.getTestTerrainSet());
        test.testParser("static npc layer",
                        "staticnpcs.xml",
                        StaticNpcs.class,
                        test.getTestNpcLayer());
        test.testParser("armor type",
                        "armor.xml",
                        ArmorTypeData.class,
                        test.getTestArmorType());
        test.testParser("weapon type",
                        "weapon.xml",
                        WeaponTypeData.class,
                        test.getTestWeaponType());
        test.testParser("other type",
                        "other.xml",
                        OtherTypeData.class,
                        test.getTestOtherType());
        test.testParser("static item layer",
                        "staticitems.xml",
                        StaticItems.class,
                        test.getTestItemLayer());

        test.testParser("armor modifier",
                        "modifier_armor.xml",
                        ArmorModifierWrapper.class,
                        test.getTestArmorModifier());

        test.createRandomArmorModifier(5000);
        test.testParser("random armor modifier",
                        "modifier_armor_rand.xml",
                        ArmorModifierWrapper.class,
                        test.getTestArmorModifier());

        test.testParser("weapon modifier",
                        "modifier_weapon.xml",
                        WeaponModifierWrapper.class,
                        test.getTestWeaponModifier());
        // 

    }

    //map test data
    private TerrainSetData testTerrainSet;
    private MapTypeData testMapType;

    //actor test data
    private Archetype testArchetype;
    private ProficiencyWrapper testProficiencyWrapper;

    //item tests data
    private WeaponTypeData testWeaponType;
    private ArmorTypeData testArmorType;
    private OtherTypeData testOtherType;
    private ModifierCategoryWrapper testModifierCategory;

    //modifier tests data
    private ArmorModifierWrapper testArmorModifier;
    private WeaponModifierWrapper testWeaponModifier;

    private StaticNpcs testNpcLayer;
    private StaticItems testItemLayer;

    public void createRandomArmorModifier(int number)
    {
        testArmorModifier = new ArmorModifierWrapper();
        for (int i = 0; i <= number; i++)
        {
            ArmorModifier am =
                    new ArmorModifier((short) i,
                                      new RandomString(1 + (int) (Math.random() * 20)).nextString(),
                                      (short) Math.floor(Math.random() * 4));
            am.setValueModifier((int) Math.floor(Math.random() * 100));
            am.setWeightModifier((int) Math.floor(Math.random() * 100));
            am.setValueMultiplier((int) Math.floor(Math.random() * 100));
            am.setWeightMultiplier((int) Math.floor(Math.random() * 100));
            testArmorModifier.getTypes().add(am);
        }
    }

    public Parsable getModifierCategory()
    {
        return testModifierCategory;
    }

    public void createModifierCategory()
    {
        testModifierCategory = new ModifierCategoryWrapper();
        ModifierCategory mc1 = new ModifierCategory((short) 1, "WEARINESS");
        ModifierCategory mc2 = new ModifierCategory((short) 2, "WORKMANSHIP");
        testModifierCategory.getTypes().add(mc1);
        testModifierCategory.getTypes().add(mc2);
    }

    public void createArchetype()
    {
        testArchetype = new Archetype((short) 1, "testArchetype");
        testArchetype.setAgility((byte) 10);
        testArchetype.setFinesse((byte) 10);

        EnumMap<DamageType, Resistance> resistances =
                new EnumMap<DamageType, Resistance>(DamageType.class);
        Resistance res = new Resistance();
        res.setRelative((byte) 10);
        res.setThreshold((short) 30);
        resistances.put(DamageType.BLUNT, res);
        testArchetype.setResistanceMap(resistances);

        testArchetype.getProficiencies().add((short) 1);
        testArchetype.getParentArchetypes().add((short) 2);

        testArchetype.setPersistent(true);
    }

    public void createProficiency()
    {
        testProficiencyWrapper = new ProficiencyWrapper();
        Proficiency pr1 = new Proficiency((short) 1, "Heavy weapons");
        Proficiency pr2 = new Proficiency((short) 2, "Heavy armors");
        Proficiency pr3 = new Proficiency((short) 3, "Double weapons");
        Proficiency pr4 = new Proficiency((short) 4, "Shield");
        testProficiencyWrapper.getTypes().add(pr1);
        testProficiencyWrapper.getTypes().add(pr2);
        testProficiencyWrapper.getTypes().add(pr3);
        testProficiencyWrapper.getTypes().add(pr4);
    }

    public void createMaptype()
    {
        testMapType = new MapTypeData();
        testMapType.setVersion(Version.FULL_VERSION);
        testMapType.setId((short) 1);
        testMapType.setDescription("this map is going to be tested");
        testMapType.setSizeX((short) 2);
        testMapType.setSizeY((short) 2);
        testMapType.setName("TestMap");
        testMapType.setTerrainSetId((short) 1);
        testMapType.setId((short) 10);
        testMapType.setMaxActors((short) 100);
        testMapType.setSegmentSizeX((byte) 2);
        testMapType.setSegmentSizeY((byte) 2);

        List<Short> tilesList = new LinkedList<Short>();
        tilesList.add((short) 11);
        tilesList.add((short) 12);
        tilesList.add((short) 13);
        tilesList.add((short) 14);
        testMapType.setTiles(MapTypeData.DataConverter.encodeTilesList(tilesList));

        List<EntryZone> entriesList = testMapType.getEntryZoneList();
        EntryZone gatein = new EntryZone((byte) 1);
        gatein.addPosition(new Position((short) 1, (short) 1));
        gatein.addPosition(new Position((short) 2, (short) 2));
        entriesList.add(gatein);
        gatein = new EntryZone((byte) 2);
        gatein.addPosition(new Position((short) 1, (short) 2));
        gatein.addPosition(new Position((short) 2, (short) 1));
        entriesList.add(gatein);

        List<ExitZone> exitsList = testMapType.getExitZoneList();
        ExitZone gateout = new ExitZone((byte) 0, (short) 1, (byte) 1);
        gateout.addPosition(new Position((short) 2, (short) 2));
        exitsList.add(gateout);
    }

    public void createNpcLayer()
    {
        StaticNpcs layer = new StaticNpcs();

        NpcDescription desc = new NpcDescription();
        desc.setName("Test Name 1");
        desc.getAiDescription().setName("default");
        desc.getAiDescription().setParamValue("aggression", 1);

        layer.addNpc(new Position((short) 1, (short) 1), desc);

        desc = new NpcDescription();
        desc.setName("Test Name 2");
        desc.getAiDescription().setName("default");
        desc.getArchetypes().add((short) 1);
        layer.addNpc(new Position((short) 2, (short) 2), desc);

        testNpcLayer = layer;
    }

    public void createItemLayer()
    {
        StaticItems layer = new StaticItems();

        ItemDescription desc = new ItemDescription();
        desc.setName("Test Item 1");
        desc.setType((short) 1);
        desc.setKind(ItemKind.WEAPON);

        layer.addItem(new Position((short) 1, (short) 1), desc);

        desc = new ItemDescription();
        desc.setName("Test Item 2");
        desc.setType((short) 2);
        desc.setKind(ItemKind.OTHER);
        layer.addItem(new Position((short) 2, (short) 2), desc);

        testItemLayer = layer;
    }

    public void createTerrainSet()
    {
        testTerrainSet = new TerrainSetData();
        testTerrainSet.setId((short) 1);
        testTerrainSet.setVersion(Version.FULL_VERSION);

        TerrainTypeData d = new TerrainTypeData();
        d.setId(-1);
        d.setPassable(false);
        d.setColor("#000000");
        d.getTileList().add(new TileTypeData(0));
        testTerrainSet.getTerrainList().add(d);

        d = new TerrainTypeData();
        d.setId(1);
        d.setPassable(true);
        d.setColor("#FFFFFF");
        d.getTileList().add(new TileTypeData(0));
        testTerrainSet.getTerrainList().add(d);
    }

    public void createWeaponType()
    {
        testWeaponType = new WeaponTypeData();
        testWeaponType.setId((short) 1);
        testWeaponType.setName("Short Sword");
        testWeaponType.setDescription("Test short sword");
        testWeaponType.setEquipementSlot(LogicalSlotType.ONE_HAND);
        testWeaponType.setRequiredStrength((byte) 10);
        testWeaponType.setValue(10);
        testWeaponType.setWeight((short) 15);
        WeaponAttackData wdstats1 =
                new WeaponAttackData(DamageType.CUTTING,
                                     (short) 1,
                                     (short) 2,
                                     (short) 15,
                                     (short) 30,
                                     (short) 2,
                                     (short) 2,
                                     null,
                                     null,
                                     null,
                                     (short) 10,
                                     null,
                                     null,
                                     null,
                                     (short) 30,
                                     null,
                                     null,
                                     null);
        WeaponAttackData wdstats2 =
                new WeaponAttackData(DamageType.PIERCING,
                                     (short) 1,
                                     (short) 5,
                                     (short) 10,
                                     (short) 20,
                                     (short) 2,
                                     (short) 5,
                                     null,
                                     null,
                                     null,
                                     (short) 10,
                                     null,
                                     null,
                                     null,
                                     (short) 30,
                                     null,
                                     null,
                                     null);
        EnumMap<AttackType, WeaponAttackData> dmmap =
                new EnumMap<AttackType, WeaponAttackData>(AttackType.class);
        dmmap.put(AttackType.SWING, wdstats1);
        dmmap.put(AttackType.THRUST, wdstats2);
        testWeaponType.setAttackMap(dmmap);
    }

    public void createArmorType()
    {
        testArmorType = new ArmorTypeData();
        testArmorType.setId((short) 1);
        testArmorType.setName("Full Plate");
        testArmorType.setEquipementSlot(LogicalSlotType.FULL_ARMOR);
        testArmorType.setValue(1000);
        testArmorType.setWeight((short) 220);
        testArmorType.setRequiredStrength((byte) 80);
        LinkedList<Short> prlist = new LinkedList<Short>();
        prlist.add((short) 2);
        testArmorType.setRequiredProficiencyIds(prlist);
        EnumMap<DamageType, Resistance> resistances =
                new EnumMap<DamageType, Resistance>(DamageType.class);
        Resistance blunt = new Resistance();
        blunt.setRelative((byte) 60);
        Resistance pierce = new Resistance();
        pierce.setRelative((byte) 70);
        Resistance cut = new Resistance();
        cut.setRelative((byte) 70);
        resistances.put(DamageType.BLUNT, blunt);
        resistances.put(DamageType.PIERCING, pierce);
        resistances.put(DamageType.CUTTING, cut);
        testArmorType.setResistances(resistances);
    }

    public void createOtherType()
    {
        testOtherType = new OtherTypeData();
        testOtherType.setId((short) 1);
        testOtherType.setName("Gemstone");
    }

    public void createArmorModifier()
    {
        testArmorModifier = new ArmorModifierWrapper();
        ArmorModifier am1 = new ArmorModifier((short) 1, "Rusty", (short) 1);
        am1.setValueModifier(-200);
        ArmorModifier am2 =
                new ArmorModifier((short) 2, "Very rusty", (short) 1);
        am2.setValueModifier(-900);
        ArmorModifier am3 = new ArmorModifier((short) 3, "Jeveled", (short) 2);
        am3.setValueModifier(10000);
        testArmorModifier.getTypes().add(am1);
        testArmorModifier.getTypes().add(am2);
        testArmorModifier.getTypes().add(am3);
    }

    public void createWeaponModifier()
    {
        testWeaponModifier = new WeaponModifierWrapper();
        WeaponModifier wm1 = new WeaponModifier((short) 1, "Rusty", (short) 1);
        wm1.setValueModifier(-200);
        wm1.setDescription("desc1");
        WeaponModifier wm2 =
                new WeaponModifier((short) 2, "Very rusty", (short) 1);
        wm2.setValueModifier(-900);
        wm2.setDescription("desc2");
        WeaponModifier wm3 =
                new WeaponModifier((short) 3, "Jeveled", (short) 2);
        wm3.setValueModifier(10000);
        wm3.setDescription("desce");
        testWeaponModifier.getTypes().add(wm1);
        testWeaponModifier.getTypes().add(wm2);
        testWeaponModifier.getTypes().add(wm3);
    }

    @SuppressWarnings("unchecked")
    public void testParser(String parserName,
                           String filename,
                           Class parsedClass,
                           Parsable parsedObject)
    {
        System.out.println("*** " + parserName.toUpperCase() + " ***");
        ParserFactory.getInstance()
                     .getParser(true, parsedClass)
                     .createXML(parsedObject, System.out);
        System.out.println();

        boolean fileWrittenSuccessfully = false;
        try
        {
            System.out.println("Saving " + parserName + " to file...");
            FileWriter fwriter = new FileWriter(filename);
            ParserFactory.getInstance()
                         .getParser(true, parsedClass)
                         .createXML(parsedObject, fwriter);
            fwriter.close();
            fileWrittenSuccessfully = true;
            System.out.println("File " + filename + " saved successfully");
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Saving " + parserName
                + " to file failed");
            e.printStackTrace();
        }

        if (fileWrittenSuccessfully)
        {
            try
            {
                System.out.println("Reading " + parserName + " from file..");
                FileInputStream fstream = new FileInputStream(filename);
                Parsable test =
                        ParserFactory.getInstance()
                                     .getParser(true, parsedClass)
                                     .parse(fstream);
                fstream.close();
                if (test == null)
                {
                    System.out.println("Reading failed - null " + parserName);
                }
                else
                {

                    System.out.println("Reading completed successfully");
                    System.out.println("Printing result:");
                    System.out.println(test.toString());
                }
            }
            catch (IOException e)
            {
                System.out.println("ERROR: Reading " + parserName
                    + " from file failed");
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Unable to test file reading");
        }

    }

    /**
     * Returns testTerrainSet.
     * @return testTerrainSet
     */
    public TerrainSetData getTestTerrainSet()
    {
        return testTerrainSet;
    }

    /**
     * Returns testMapType.
     * @return testMapType
     */
    public MapTypeData getTestMapType()
    {
        return testMapType;
    }

    /**
     * Returns testArchetype.
     * @return testArchetype
     */
    public Archetype getTestArchetype()
    {
        return testArchetype;
    }

    /**
     * Returns testWeaponType.
     * @return testWeaponType
     */
    public WeaponTypeData getTestWeaponType()
    {
        return testWeaponType;
    }

    /**
     * Returns testArmorType.
     * @return testArmorType
     */
    public ArmorTypeData getTestArmorType()
    {
        return testArmorType;
    }

    /**
     * Returns testOtherType.
     * @return testOtherType
     */
    public OtherTypeData getTestOtherType()
    {
        return testOtherType;
    }

    /**
     * Returns testProficiencyWrapper.
     * @return testProficiencyWrapper
     */
    public ProficiencyWrapper getTestProficiencyWrapper()
    {
        return testProficiencyWrapper;
    }

    /**
     * Returns testArmorModifier.
     * @return testArmorModifier
     */
    public ArmorModifierWrapper getTestArmorModifier()
    {
        return testArmorModifier;
    }

    /**
     * Returns testWeaponModifier.
     * @return testWeaponModifier
     */
    public WeaponModifierWrapper getTestWeaponModifier()
    {
        return testWeaponModifier;
    }

    /**
     * Returns testNpcLayer.
     * @return testNpcLayer
     */
    public StaticNpcs getTestNpcLayer()
    {
        return testNpcLayer;
    }

    /**
     * Returns testItemLayer.
     * @return testItemLayer
     */
    public StaticItems getTestItemLayer()
    {
        return testItemLayer;
    }

}
