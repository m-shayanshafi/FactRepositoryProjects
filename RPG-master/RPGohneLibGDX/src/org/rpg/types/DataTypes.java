package org.rpg.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataTypes {
	public enum TypesE{
		Stubser, Feuerwind, Monsun, Blitzen
	}
	
	public static TypesE stringToAttack(String s){
		TypesE t = null;
		TypesE[] types = TypesE.values();
		int max = types.length;
		for(int i = 0; i < max; i++){
			if (types[i].name().equals(s)){
				t = types[i];
			}
		}
		return t;
	}
	
	
	/**
	 * Gibt die Effiezienz der Attacken an. 
	 * @param t1
	 * Der Angreifer-Typ
	 * @param t2
	 * Der Verteidiger-Typ
	 * @return
	 * Gibt einen Integer zwischen 0 und 2 zurück.
	 * 0 = resistent; 1 = normal; 2 = sehr effektiv;
	 */
	public static int effeciency(TypesE t1, TypesE t2) {
		int attack = 0;
		JSONParser parser = new JSONParser();
		InputStream in = DataTypes.class.getResourceAsStream("/org/rpg/attacks/" + t1.name() + ".json");
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
}