package src;

import src.gui.editor.scenario.ScenarioEditorFrame;

public class RoofDamager {

	public static void main(String args[]){
		src.Constants.EDITOR = true;
		//new src.scenario.ScenarioLoader(); // Read in scenario data
		new ScenarioEditorFrame();
	}
}
