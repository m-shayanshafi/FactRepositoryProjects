/**
 * Party.java
 *
 * Partie de Tablut.  (cote server)
 *
 * @author Si-Mohamed Lamraoui
 * @date 21.05.10
 */

package network.server;



public class Party
{

	public static String SUEDOIS_SIDE 	= "s";
	public static String MOSCOVITE_SIDE = "m";
	
	public Client 	client1; 	// joueur 1
	public Client 	client2; 	// joueur 2
	public boolean 	skill; 		// true : attaques activees, false : attaques inactivees
	public int 		id; 		// identifiant de la partie
	public String 	type; 		// s : suedois, m : moscovites
	public boolean 	started; 	// la partie a t'elle demarree
	// idee : partie privee (avec mot de passe)

	public Party(int i, Client c, String t, boolean s)
	{
		id = i;
		client1 = c;
		type = t;
		skill = s;
		started = false;
		// TODO recup info dans BD
	}



}
