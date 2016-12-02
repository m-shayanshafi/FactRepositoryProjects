/**
 * Party.java
 *
 * Partie de Tablut.
 *
 * @author Thierry Maurigault
 * @date 28.05.10
 */

package network.client;



public class Party
{
	public static String SUEDOIS_SIDE 	= "s";
	public static String MOSCOVITE_SIDE = "m";
	
	public int 		id; 		// identifiant de la partie
	public String user;
	public int 		lvl;
	public String 	type; 		// s : suedois, m : moscovites
	public boolean 	skill; 		// true : attaques activees, false : attaques inactivees

	// idee : partie privee (avec mot de passe)

	public Party(int _id, String _user, int _lvl, String _type, String _skill)
	{
		id = _id;
		user = _user;
		lvl = _lvl;
		type = _type.trim();
		if (_skill.trim().equalsIgnoreCase("a"))
			skill = true;
		else
			skill = false;
	}
}
