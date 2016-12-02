package org.rpg.read;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rpg.attacks.DataAttacks;
import org.rpg.attacks.DataAttacks.AttackE;
import org.rpg.entitys.DataEntity;
import org.rpg.entitys.DataEntity.EntityE;

public class ReadEntity {

	public static int readMaxHP(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int maxHP = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			maxHP = (Integer) jsonObject.get("MaxHP");
		} catch (IOException | ParseException e) {
		}
		return maxHP;
	}

	public static int readMaxMP(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int maxMP = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			maxMP = (Integer) jsonObject.get("MaxMP");
		} catch (IOException | ParseException e) {
		}
		return maxMP;
	}

	public static int readHP(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int HP = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			long HPL = (long) jsonObject.get("HP");
			HP = (int) HPL;
		} catch (IOException | ParseException e) {
		}
		return HP;
	}

	public static int readMP(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int MP = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			MP = (Integer) jsonObject.get("MP");
		} catch (IOException | ParseException e) {
		}
		return MP;
	}

	public static EntityE readEntity(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		EntityE Entity = null;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			String s = (String) jsonObject.get("Entity");
			Entity = DataEntity.stringToEntity(s);
		} catch (IOException | ParseException e) {
		}
		return Entity;
	}

	public static int readXP(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int XP = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			XP = (Integer) jsonObject.get("XP");
		} catch (IOException | ParseException e) {
		}
		return XP;
	}

	public static int readLvl(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int Lvl = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			Lvl = (Integer) jsonObject.get("Lvl");
		} catch (IOException | ParseException e) {
		}
		return Lvl;
	}

	public static int readAttack(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int Attack = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			Attack = (Integer) jsonObject.get("Attack");
		} catch (IOException | ParseException e) {
		}
		return Attack;
	}

	public static int readDef(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int Def = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			Def = (Integer) jsonObject.get("Def");
		} catch (IOException | ParseException e) {
		}
		return Def;
	}
	
	public static int readInit(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		int Init = 0;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			Init = (Integer) jsonObject.get("Init");
		} catch (IOException | ParseException e) {
		}
		return Init;
	}

	public static AttackE[] readAttacks(String name) {
		File file = new File(System.getProperty("user.home") + "/.rpg/Entitys/" + name + ".json");
		JSONParser parser = new JSONParser();
		AttackE[] attacks = null;
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray jsonArr = (JSONArray) jsonObject.get("Attacks");
			attacks = new AttackE[jsonArr.size()];
			for (int i = 0; i < jsonArr.size(); i++) {
				String s = (String) jsonArr.get(i);
				attacks[i] = DataAttacks.stringToAttack(s);
			}
		} catch (IOException | ParseException e) {
		}
		return attacks;
	}
}