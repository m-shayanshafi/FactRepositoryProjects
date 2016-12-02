package org.rpg.attacks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rpg.entitys.DataEntity;
import org.rpg.entitys.DataEntity.EntityE;
import org.rpg.types.DataTypes;
import org.rpg.types.DataTypes.TypesE;

public class DataAttacks {
	public enum AttackE{
		Stubser, Feuerwind, Monsun, Blitzen
	}
	
	public static AttackE stringToAttack(String s){
		AttackE a = null;
		AttackE[] attacks = AttackE.values();
		int max = attacks.length;
		for(int i = 0; i < max; i++){
			if (attacks[i].name().equals(s)){
				a = attacks[i];
			}
		}
		return a;
	}


	public static int getAttack(AttackE a) {
		int attack = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataAttacks.class.getResourceAsStream("/org/rpg/attacks/" + a.name() + ".json");
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

	public static int getDef(AttackE a) {
		int def = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataAttacks.class.getResourceAsStream("/org/rpg/attacks/" + a.name() + ".json");
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

	public static int getAccuracy(AttackE a) {
		int acc = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataAttacks.class.getResourceAsStream("/org/rpg/attacks/" + a.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			long accl = (long) jsonObj.get("acc");
			acc = (int) accl;
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return acc;
	}
	
	public static boolean isSpecial(AttackE a){
		boolean special = false;
		JSONParser parser = new JSONParser();
		InputStream in = DataAttacks.class.getResourceAsStream("/org/rpg/attacks/" + a.name() + ".json");
		try {
			Object obj = parser.parse(new InputStreamReader(in));
			JSONObject jsonObj = (JSONObject) obj;
			String specials = (String) jsonObj.get("special");
			special = Boolean.parseBoolean(specials);
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return special;
	}
	
	public static int getMP(AttackE a) {
		int mp = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataAttacks.class.getResourceAsStream("/org/rpg/attacks/" + a.name() + ".json");
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
	
	
}