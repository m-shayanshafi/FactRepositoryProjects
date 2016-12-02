package org.rpg.entitys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rpg.attacks.DataAttacks;
import org.rpg.attacks.DataAttacks.AttackE;
import org.rpg.types.DataTypes;
import org.rpg.types.DataTypes.TypesE;

public class DataEntity {

	public enum EntityE {
		Hero, Voldemort, Entchen, Link, Zelda
	}

	public static EntityE stringToEntity(String s) {
		EntityE e = null;
		EntityE[] entitys = EntityE.values();
		int max = entitys.length;
		for (int i = 0; i < max; i++) {
			if (entitys[i].name().equals(s)) {
				e = entitys[i];
			}
		}
		return e;
	}

	public static int getHP(EntityE e) {
		int hp = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("/org/rpg/entitys/" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long hpl = (long) jsonObj.get("hp");
			hp = (int) hpl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

		return hp;
	}

	public static int getAttack(EntityE e) {
		int attack = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("/org/rpg/entitys/" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long attackl = (long) jsonObj.get("attack");
			attack = (int) attackl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return attack;
	}

	public static int getDef(EntityE e) {
		int def = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long defl = (long) jsonObj.get("def");
			def = (int) defl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return def;
	}

	public static int getInit(EntityE e) {
		int init = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long initl = (long) jsonObj.get("init");
			init = (int) initl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return init;
	}

	public static TypesE getType(EntityE e) {
		TypesE type = null;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			String types = (String) jsonObj.get("type");
			type = DataTypes.stringToAttack(types);
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return type;
	}

	public static int getMP(EntityE e) {
		int mp = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long mpl = (long) jsonObj.get("mp");
			mp = (int) mpl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return mp;
	}

	public static int getXP(EntityE e) {
		int xp = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("" + e.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long xpl = (long) jsonObj.get("xp");
			xp = (int) xpl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return xp;
	}
	
	public static AttackE[] getAttacks(EntityE e) {
		JSONParser parser = new JSONParser();
		InputStream in = DataEntity.class.getResourceAsStream("/org/rpg/entitys/" + e.name() + ".json");
		AttackE[] attacks = null;
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			JSONArray jsonArr = (JSONArray) jsonObj.get("attacks");
			attacks = new AttackE[jsonArr.size()];
			for (int i = 0; i < jsonArr.size(); i++) {
				String s = (String) jsonArr.get(i);
				attacks[i] = DataAttacks.stringToAttack(s);
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return attacks;
	}
}