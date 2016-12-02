/**
 * DataBase.java
 *
 * Notice, do not import com.mysql.jdbc.*
 * or you will have problems!
 *
 * @author Si-Mohamed Lamraoui
 * @data 29.05.10
 */

package network.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class DataBase
{

	public static String DB_NAME = "tablut";
	public static String DB_USER = "root";
	public static String DB_PASSWORD = "groupe3";

	private Connection connection = null;

	public DataBase()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance(); // chargement du driver mysql
		} catch (Exception ex) { System.out.println("[erreur] impossible d'ouvrir le driver mysql"); }
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/"+DB_NAME +"?user="+DB_USER+"&password="+DB_PASSWORD);
		} catch (SQLException ex) { System.out.println("[erreur] connexion a la base de donnee impossible"); }
	}


	/**
	 *
	 */
	public boolean isAdmin(String pseudo)
	{
		Statement stmt = null;
		ResultSet results = null;
		int value = 0;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT exp FROM players WHERE pseudo=\""+pseudo+"\"");
			if(results.next())
		 		value = results.getInt("admin");
			else
				return false;
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return false;
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		
		if(value==1)
			return true;
		else
			return false;
	}
	
	
	/**
	 *
	 */
	public int getExp(String pseudo)
	{
		Statement stmt = null;
		ResultSet results = null;
		int value = 0;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT exp FROM players WHERE pseudo=\""+pseudo+"\"");
			if(results.next())
		 		value = results.getInt("exp");
			else
				return 0;
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return 0;
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		return value;
	}


	/**
	 *
	 */
	public int getLvl(String pseudo)
	{
		Statement stmt = null;
		ResultSet results = null;
		int value = 0;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT level FROM players WHERE pseudo=\""+pseudo+"\"");
			if(results.next())
		 		value = results.getInt("level");
			else
				return 0;
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return 0;
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		return value;
	}


	/**
	 *
	 */
	public String getPassword(int id)
	{
		Statement stmt = null;
		ResultSet results = null;
		String value;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT password FROM players WHERE id="+id);
			if(results.next())
		 		value = results.getString("password");
			else return "";
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return "";
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		return value;
	}
	
	
	/**
	 *
	 */
	public String getPseudo(int id)
	{
		Statement stmt = null;
		ResultSet results = null;
		String value;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT pseudo FROM players WHERE id="+id);
			if(results.next())
		 		value = results.getString("pseudo");
			else return "";
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return "";
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		return value;
	}
	
	
	/**
	 *
	 */
	public int getId(String pseudo)
	{
		Statement stmt = null;
		ResultSet results = null;
		int value;
		try {
			stmt = connection.createStatement();
			results = stmt.executeQuery("SELECT id FROM players WHERE pseudo=\""+pseudo+"\"");
			if(results.next())
		 		value = results.getInt("id");
			else return 0;
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return 0;
		}
		finally {
			closeQuery(stmt, results); // libere les ressources
		}
		return value;
	}




	/**
	 *
	 */
	public boolean setExp(String pseudo, int exp)
	{
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE players SET exp="+exp+" WHERE pseudo=\""+pseudo+"\"");
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql");
			return false;
		}
		finally {
			closeQuery(stmt, null); // libere les ressources
		}
		return true;
	}



	/**
	 *
	 */
	public boolean setLvl(String pseudo, int lvl)
	{
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE players SET level="+lvl+" WHERE pseudo=\""+pseudo+"\"");
		} catch (SQLException ex) { 
			System.out.println("[erreur] impossible d'executer la requete sql"); 
			return false;
		}
		finally {
			closeQuery(stmt, null); // libere les ressources
		}
		return true;
	}
	


	/**
	 *
	 */
	public void closeQuery(Statement stmt, ResultSet rs)
	{
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException sqlEx){}
			rs = null;
		}
		if(stmt!=null) {
			try {
			    stmt.close();
			} catch (SQLException sqlEx){}
			stmt = null;
		}
	}


	/**
	 *
	 */
	public void closeConnection() 
	{
		try {
			connection.close();
		}
		catch (SQLException e) { System.out.println("[erreur] impossible de fermer la connection avec la base de donnee"); }
	} 



	// TEST
	public static void main(String args[])
	{

		DataBase db = new DataBase();
		System.out.println("exp : " + db.getExp("simo"));
		System.out.println("lvl : " + db.getLvl("simo"));
		//System.out.println("pass : " + db.getPassword("simo"));

		db.setLvl("simo", 10);
		db.setExp("simo", 8888);

		System.out.println("exp : " + db.getExp("simo"));
		System.out.println("lvl : " + db.getLvl("simo"));
		//System.out.println("pass : " + db.getPassword("simo"));
			
		db.closeConnection();
	}

}
