/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.scenario.loader;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import src.exceptions.BadScenarioFileException;
import src.file.scenario.ScenarioFile;
import src.scenario.MiscScenarioData;

/**
 * Loads the scenario file into a DOM and parses it to find all of the
 * relevant elements.  It will then send that element to its corresponding
 * loader class to be processed.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class ScenarioLoader {

	public ScenarioLoader() {
		try {
			Document dom = parse(src.Constants.SCENARIO_XML);
			Element docElem = dom.getDocumentElement();

			try {
				MiscScenarioData.NAME = docElem.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				
				String mapPath = docElem.getElementsByTagName("map").item(0).getAttributes().item(0).getNodeValue();
				
				MiscScenarioData.MAP_PATH = mapPath;
				
				MiscScenarioData.ENDING_MESSAGE = docElem.getElementsByTagName(
						"endingMessage").item(0).getFirstChild().getNodeValue();
				
				MiscScenarioData.RUN_HP_AMOUNT = Integer.parseInt(docElem.getElementsByTagName(
				"runHitPointAmount").item(0).getFirstChild().getNodeValue());
				
				MiscScenarioData.RUN_CHANCE = Double.parseDouble(docElem.getElementsByTagName(
				"runChance").item(0).getFirstChild().getNodeValue());
				
				MiscScenarioData.SPAWN_CHANCE = Double.parseDouble(docElem.getElementsByTagName(
				"spawnChanceCity").item(0).getFirstChild().getNodeValue());
				
				MiscScenarioData.SPAWN_CHANCE_DUN = Double.parseDouble(docElem.getElementsByTagName(
				"spawnChanceDungeon").item(0).getFirstChild().getNodeValue());
				
				MiscScenarioData.SPAWN_CHANCE_ITEM = Double.parseDouble(docElem.getElementsByTagName(
				"itemDropChance").item(0).getFirstChild().getNodeValue());
				
				/*String startLocationImage = docElem.getElementsByTagName("startLocation").item(0).getAttributes().item(0).getNodeValue();
				MiscScenarioData.START_LOCATION_IMAGE = startLocationImage;
				
				Images.getInstance().add(startLocationImage);*/
				
				/*MiscScenarioData.NUM_CITIES = Integer.parseInt(docElem.getElementsByTagName(
						"numCities").item(0).getAttributes().item(0).getNodeValue());*/
				
				setNumCities(docElem.getElementsByTagName("citySpawns").item(0));
				setNumPlayerImages(docElem.getElementsByTagName("weapons").item(0));
				
				new ArmorLoader(docElem.getElementsByTagName("defense").item(0));
				new EnemyLoader(docElem.getElementsByTagName("enemies").item(0));
				new NPCLoader(docElem.getElementsByTagName("NPCs").item(0));
				new ShopLoader(docElem.getElementsByTagName("shops").item(0));
				new AmmoPresetLoader(docElem.getElementsByTagName("ammoPresets").item(0));
				new ArmorPresetLoader(docElem.getElementsByTagName("armorPresets").item(0));
				new GenericPresetLoader(docElem.getElementsByTagName("genericPresets").item(0));
				new MagicPresetLoader(docElem.getElementsByTagName("magicPresets").item(0));
				new WeaponPresetLoader(docElem.getElementsByTagName("weaponPresets").item(0));
				new WeaponLoader(docElem.getElementsByTagName("weapons").item(0));
				new ExitTypeLoader(docElem.getElementsByTagName("exits").item(0));
				new DungeonSpawnLoader(docElem.getElementsByTagName("dungeonSpawns").item(0));
				new CitySpawnLoader(docElem.getElementsByTagName("citySpawns").item(0));
				new TriggerLoader(docElem.getElementsByTagName("triggers").item(0));
				new GroundItemsLoader(docElem.getElementsByTagName("items").item(0));
				new StreetLoader(docElem.getElementsByTagName("streets").item(0));
				new HitImagesLoader(docElem.getElementsByTagName("hitImages").item(0));
				new PlayerImagesLoader(docElem.getElementsByTagName("playerImages").item(0));
				new ExplosionImageLoader(docElem.getElementsByTagName("explosionImages").item(0));
				new ObstructionLoader(docElem.getElementsByTagName("obstructionImages").item(0));
				new BarLoader(docElem.getElementsByTagName("bars").item(0));
				new DrinksLoader(docElem.getElementsByTagName("drinks").item(0));
				new ShopkeeperSpawnLoader(docElem.getElementsByTagName("shopkeeperSpawns").item(0));
				new InventoryLimitsLoader(docElem.getElementsByTagName("inventoryLimits").item(0));
				new StartingInventoryLoader(docElem.getElementsByTagName("startingInventory").item(0));
				new DifficultyLoader(docElem.getElementsByTagName("difficulty").item(0));
				new ExplosionDamageLoader(docElem.getElementsByTagName("explosionDamage").item(0));
				
				if(docElem.getElementsByTagName("sounds").getLength() > 0)
					new SoundsLoader(docElem.getElementsByTagName("sounds").item(0));
			} catch(BadScenarioFileException ex){
				//ex.printStackTrace();
				System.err.println("Scenario data not correct.");
			}

		} catch(Exception e){
			System.err.println("XML not well formatted. Scenario data can't be read.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks how many cities are used for city spawns and sets the number of
	 * cities based on that.
	 * @param node Node : citySpawns node
	 */
	private void setNumCities(Node node){
		MiscScenarioData.NUM_CITIES = ScenarioLoader.getFilteredList(node).size();
	}
	
	/**
	 * Checks how many weapons are used and sets the number of
	 * player images based on that.
	 * @param node Node : weapons node
	 */
	private void setNumPlayerImages(Node node){
		MiscScenarioData.NUM_PLAYER_IMAGES = ScenarioLoader.getFilteredList(node).size()+1;
	}

	/**
	 * Parses the xml file at the specified path and returns a DOM.
	 * @param xml String : xml file
	 * @return Document : DOM for the xml file.
	 */
	private Document parse(String xml){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document dom;

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			if(src.Constants.SCENARIO_DEBUG || src.Constants.EDITOR){
				dom = db.parse(xml);
			}
			else {
				dom = db.parse(ScenarioFile.getInstance().getStream(xml));
			}
			return dom;
		}catch(Exception e) {
			//e.printStackTrace();
			System.err.println("Error processing scenario file.");
		}
		return null;
	}
	
	/**
	 * Reads the raw data from the node, and filters out the
	 * stuff that isn't relevant.  It stores the nodes that are important
	 * in an ArrayList and returns it.
	 * @param node Node : node to filter
	 * @return ArrayList<Node> : contains relevant nodes.
	 */
	public static ArrayList<Node> getFilteredList(Node node){
		ArrayList<Node> list = new ArrayList<Node>();

		for(int i = 0; i < node.getChildNodes().getLength(); i++){
			if(!node.getChildNodes().item(i).getNodeName().equals("#text") &&
					!node.getChildNodes().item(i).getNodeName().equals("#comment"))
				list.add(node.getChildNodes().item(i));
		}
		return list;
	}
}
