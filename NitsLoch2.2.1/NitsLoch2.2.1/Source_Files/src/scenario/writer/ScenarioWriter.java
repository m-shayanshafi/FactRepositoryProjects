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

package src.scenario.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import src.scenario.MiscScenarioData;
import src.scenario.loader.ScenarioLoader;

public class ScenarioWriter {

	public ScenarioWriter() {
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document scenarioDoc = docBuilder.newDocument();
			
			Element rootElem = scenarioDoc.createElement("NitsLoch");
			scenarioDoc.appendChild(rootElem);
			
			Element currentElem = scenarioDoc.createElement("name");
			currentElem.setTextContent(MiscScenarioData.NAME);
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("map");
			currentElem.setAttribute("path", MiscScenarioData.MAP_PATH);
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("endingMessage");
			currentElem.setTextContent(MiscScenarioData.ENDING_MESSAGE);
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("runHitPointAmount");
			currentElem.setTextContent(String.valueOf(MiscScenarioData.RUN_HP_AMOUNT));
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("runChance");
			currentElem.setTextContent(String.valueOf(MiscScenarioData.RUN_CHANCE));
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("spawnChanceCity");
			currentElem.setTextContent(String.valueOf(MiscScenarioData.SPAWN_CHANCE));
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("spawnChanceDungeon");
			currentElem.setTextContent(String.valueOf(MiscScenarioData.SPAWN_CHANCE_DUN));
			rootElem.appendChild(currentElem);
			
			currentElem = scenarioDoc.createElement("itemDropChance");
			currentElem.setTextContent(String.valueOf(MiscScenarioData.SPAWN_CHANCE_ITEM));
			rootElem.appendChild(currentElem);
			
			new ArmorWriter(scenarioDoc, rootElem);
			new EnemyWriter(scenarioDoc, rootElem);
			new NPCWriter(scenarioDoc, rootElem);
			new ShopWriter(scenarioDoc, rootElem);
			new AmmoPresetWriter(scenarioDoc, rootElem);
			new ArmorPresetWriter(scenarioDoc, rootElem);
			new GenericPresetWriter(scenarioDoc, rootElem);
			new MagicPresetWriter(scenarioDoc, rootElem);
			new WeaponPresetWriter(scenarioDoc, rootElem);
			new WeaponWriter(scenarioDoc, rootElem);
			new ExitTypeWriter(scenarioDoc, rootElem);
			new DungeonSpawnWriter(scenarioDoc, rootElem);
			new CitySpawnWriter(scenarioDoc, rootElem);
			new ShopkeeperSpawnWriter(scenarioDoc, rootElem);
			new TriggerWriter(scenarioDoc, rootElem);
			new GroundItemsWriter(scenarioDoc, rootElem);
			new StreetWriter(scenarioDoc, rootElem);
			new HitImagesWriter(scenarioDoc, rootElem);
			new PlayerImagesWriter(scenarioDoc, rootElem);
			new ExplosionImageWriter(scenarioDoc, rootElem);
			new ObstructionWriter(scenarioDoc, rootElem);
			new BarWriter(scenarioDoc, rootElem);
			new DrinksWriter(scenarioDoc, rootElem);
			new InventoryLimitsWriter(scenarioDoc, rootElem);
			new StartingInventoryWriter(scenarioDoc, rootElem);
			new DifficultyWriter(scenarioDoc, rootElem);
			new ExplosionDamageWriter(scenarioDoc, rootElem);
			new SoundsWriter(scenarioDoc, rootElem);

			writeScenarioFile(scenarioDoc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void writeScenarioFile(Document doc) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		String saveLocation = "";
		
		try{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int retVal = fc.showSaveDialog(null);
			if(retVal == JFileChooser.APPROVE_OPTION){
				saveLocation = fc.getSelectedFile().getAbsolutePath();
			}
		} catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Problem saving.");
		}
		
		if(!saveLocation.equals("")) {
			BufferedWriter out = new BufferedWriter(new FileWriter(saveLocation));
	        out.write(xmlString);
	        out.close();
		}
	}
	
	public static void main(String[] args) {
		try {
		src.Constants.EDITOR = true;
		MiscScenarioData.SCENARIO_FILE = "/home/darren/Scenario.xml";
		new ScenarioLoader();
		new ScenarioWriter();
		} catch (Exception ex) {}
	}
}
