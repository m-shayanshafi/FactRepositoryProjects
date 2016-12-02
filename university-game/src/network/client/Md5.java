/**
 * Md5.java
 *
 * @author Si-Mohamed Lamraoui
 * @date 21.05.10
 */

package network.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5
{

	/**
	 * Convertie la chaine password en md5.
	 */
	public static String encode(String password)
    	{
		byte[] pwd = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(pwd);
		}
		catch(NoSuchAlgorithmException e) { System.out.println("Md5 non supporte"); }

		StringBuilder hashString = new StringBuilder();
		for (int i=0;i<hash.length;i++) {
		    	String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1)  {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} 
			else
		        	hashString.append(hex.substring(hex.length() - 2));
		}
		return hashString.toString();
    	}

}
