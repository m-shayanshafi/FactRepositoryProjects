JC = javac
OPTIONS = 
JAR = jar
src = src
gui = $(src)/gui
ed_gui = $(gui)/editor
game = $(src)/game
land = $(src)/land
enums = $(src)/enums
scenario = $(src)/scenario
exceptions = $(src)/exceptions
src_data = $(src)/data
file = $(src)/file
.SUFFIXES: .java .class
.java.class:
	$(JC) $(OPTIONS) $*.java

CLASSES = \
	$(src)/Constants.java \
	$(src)/NitsLoch.java \
	$(src)/RoofNotifier.java \
	$(gui)/AboutFrame.java \
	$(gui)/BarShop.java \
	$(gui)/Controller.java \
	$(gui)/CreatePlayerFrame.java \
	$(gui)/GameFrame.java \
	$(gui)/HospitalShop.java \
	$(gui)/ItemShop.java \
	$(gui)/LevelSkipModel.java \
	$(gui)/LevelSkipFrame.java \
	$(gui)/ShopWindow.java \
	$(gui)/UseFrame.java \
	$(ed_gui)/AddLevelFrame.java \
	$(ed_gui)/EditorControlFrame.java \
	$(ed_gui)/EditorWindow.java \
	$(ed_gui)/InitEditorFrame.java \
	$(enums)/AmmoPresets.java \
	$(enums)/Armor.java \
	$(enums)/ArmorPresets.java \
	$(enums)/Bars.java \
	$(enums)/DamageType.java \
	$(enums)/Direction.java \
	$(enums)/Drinks.java \
	$(enums)/DungeonWalls.java \
	$(enums)/EditorMainTypes.java \
	$(enums)/Enemies.java \
	$(enums)/EnemyBehavior.java \
	$(enums)/ExitType.java \
	$(enums)/ExplosionImages.java \
	$(enums)/ExplosionType.java \
	$(enums)/Facing.java \
	$(enums)/GenericPresets.java \
	$(enums)/GroundItems.java \
	$(enums)/HitImages.java \
	$(enums)/InventoryLimits.java \
	$(enums)/LandType.java \
	$(enums)/MagicPresets.java \
	$(enums)/NPCs.java \
	$(enums)/ObstructionLandType.java \
	$(enums)/PlayerImages.java \
	$(enums)/Shops.java \
	$(enums)/Sounds.java \
	$(enums)/StartingInventory.java \
	$(enums)/StoreItems.java \
	$(enums)/StreetType.java \
	$(enums)/VehicleType.java \
	$(enums)/Weapon.java \
	$(enums)/WeaponPresets.java \
	$(exceptions)/BadScenarioFileException.java \
	$(exceptions)/CityNameExistsException.java \
	$(exceptions)/EnemyDiedException.java \
	$(exceptions)/EnemySpawnNotDefinedException.java \
	$(exceptions)/NoPlayerException.java \
	$(exceptions)/NoSuchCityException.java \
	$(exceptions)/PlayerEnteredException.java \
	$(game)/Battles.java \
	$(game)/CitySpawns.java \
	$(game)/DungeonSpawns.java \
	$(game)/DungeonView.java \
	$(game)/Enemy.java \
	$(game)/EnemyMovement.java \
	$(game)/GameWorld.java \
	$(game)/Item.java \
	$(game)/Messages.java \
	$(game)/NPC.java \
	$(game)/OutsideView.java \
	$(game)/Player.java \
	$(game)/ShopkeeperSpawns.java \
	$(game)/SoundThread.java \
	$(game)/TheEditor.java \
	$(game)/TheGame.java \
	$(game)/Vehicle.java \
	$(land)/Exit.java \
	$(land)/Land.java \
	$(land)/Obstruction.java \
	$(land)/Shop.java \
	$(land)/Street.java \
	$(land)/Water.java \
	$(scenario)/AmmoPresetLoader.java \
	$(scenario)/ArmorLoader.java \
	$(scenario)/ArmorPresetLoader.java \
	$(scenario)/BarLoader.java \
	$(scenario)/CitySpawnLoader.java \
	$(scenario)/DifficultyLoader.java \
	$(scenario)/DrinksLoader.java \
	$(scenario)/DungeonSpawnLoader.java \
	$(scenario)/EnemyLoader.java \
	$(scenario)/ExitTypeLoader.java \
	$(scenario)/ExplosionImageLoader.java \
	$(scenario)/GenericPresetLoader.java \
	$(scenario)/GroundItemsLoader.java \
	$(scenario)/HitImagesLoader.java \
	$(scenario)/ItemNameToStoreID.java \
	$(scenario)/Images.java \
	$(scenario)/InventoryLimitsLoader.java \
	$(scenario)/MagicPresetLoader.java \
	$(scenario)/MiscScenarioData.java \
	$(scenario)/NPCLoader.java \
	$(scenario)/ObstructionLoader.java\
	$(scenario)/PlayerImagesLoader.java \
	$(scenario)/ScenarioLoader.java \
	$(scenario)/ShopkeeperSpawnLoader.java \
	$(scenario)/ShopLoader.java \
	$(scenario)/SoundsLoader.java \
	$(scenario)/StartingInventoryLoader.java \
	$(scenario)/StreetLoader.java \
	$(scenario)/TriggerLoader.java \
	$(scenario)/WeaponLoader.java \
	$(scenario)/WeaponPresetLoader.java\
	$(file)/map/MapFile.java\
	$(file)/map/MapData.java\
	$(file)/map/MapHeader.java\
	$(file)/level/Level.java\
	$(file)/level/LevelHeader.java\
	$(file)/chunk/Chunk.java\
	$(file)/chunk/ChunkHeader.java\
	$(file)/entry/Entry.java\
	$(file)/enemy/EnemyChunk.java\
	$(file)/enemy/EnemyEntry.java\
	$(file)/exit/ExitChunk.java\
	$(file)/exit/ExitEntry.java\
	$(file)/item/ItemChunk.java\
	$(file)/item/ItemEntry.java\
	$(file)/npc/NPCChunk.java\
	$(file)/npc/NPCEntry.java\
	$(file)/obstruction/ObstructionChunk.java\
	$(file)/obstruction/ObstructionEntry.java\
	$(file)/player/PlayerFile.java\
	$(file)/shop/ShopChunk.java\
	$(file)/shop/ShopEntry.java\
	$(file)/street/StreetChunk.java\
	$(file)/street/StreetEntry.java\
	$(file)/scenario/ScenarioFile.java

default: NitsLoch.jar RoofNotifier.jar

all: NitsLoch.jar RoofNotifier.jar

NitsLoch: NitsLoch.jar

RoofNotifier: RoofNotifier.jar

NitsLoch.jar: classes
	$(JAR) cmf nitsClass.txt NitsLoch.jar $(src)/*.class $(ed_gui)/*.class $(gui)/*.class $(game)/*.class $(enums)/*.class $(land)/*.class $(scenario)/*.class $(exceptions)/*.class $(src_data) $(file)/*/*.class

RoofNotifier.jar: classes
	$(JAR) cmf roofClass.txt RoofNotifier.jar  $(src)/*.class $(gui)/*.class $(ed_gui)/*.class $(game)/*.class $(enums)/*.class $(land)/*.class $(scenario)/*.class $(exceptions)/*.class $(src_data) $(file)/*/*.class

classes: $(CLASSES:.java=.class)

clean:
	-$(RM) NitsLoch.jar
	-$(RM) RoofNotifier.jar
	-$(RM) $(src)/*.class
	-$(RM) $(gui)/*.class
	-$(RM) $(ed_gui)/*.class
	-$(RM) $(land)/*.class
	-$(RM) $(game)/*.class
	-$(RM) $(enums)/*.class
	-$(RM) $(scenario)/*.class
	-$(RM) $(exceptions)/*.class
	-$(RM) $(file)/*/*.class
