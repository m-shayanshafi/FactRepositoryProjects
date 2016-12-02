package org.rpg.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rpg.attacks.DataAttacks.AttackE;

public class SaveEntity {
	
	@SuppressWarnings("unchecked")
	public static void saveattacks(String name, int attacks) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("attacks", attacks);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveMaxMP(String name, int maxMP) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("maxMP", maxMP);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveHP(String name, int HP) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("HP", HP);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveMP(String name, int MP) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("MP", MP);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveXP(String name, int XP) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("XP", XP);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveLvl(String name, int Lvl) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("Lvl", Lvl);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveAttack(String name, int attack) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("attack", attack);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveDef(String name, int def) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject.put("def", def);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveAttacks(String name, AttackE[] attacks) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray jsonArr = new JSONArray();
			for(int i = 0; i < attacks.length; i++){
				jsonArr.add(attacks[i].name());
			}
			jsonObject.put("attacks", jsonArr);
			file.delete();
			FileWriter fw = new FileWriter(file);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
