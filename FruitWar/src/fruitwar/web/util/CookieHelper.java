package fruitwar.web.util;

import javax.servlet.http.Cookie;

/**
 * Utility for generating cookie for login and 
 * validating login info stored in cookie.
 * 
 * @author wangnan
 *
 */
public class CookieHelper {


	public static final String KEY_LOGIN_NAME = "loginName";
	
	/**
	 * Validate whether the cookie contains a valid login.
	 * Return the login name. Return null if any error occurs. 
	 * @param cookies
	 * @return
	 */
	public static String validateCookieLogin(Cookie[] cookies){
		if(cookies == null)
			return null;
		
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if(c.getName().equals(KEY_LOGIN_NAME)){
            	String value = c.getValue();
            	return CookieHelper.validateLoginCipher(value);
            }
        }
        
        return null;
	}

	/**
	 * Create an encrypted cookie to remember login.
	 * The major purpose contains:
	 * 	1. The password can not be deduced from the cipher.
	 * 	2. The validation process does not need the password.
	 * So it's... public-key encryption algorithm. But that's too 
	 * complex. We use a very very simple method (the validation is not 
	 * that exact). 
	 * Surely, it's easy to use a program to generate such a cipher 
	 * to cheat the checker.
	 *  
	 * @param loginName
	 * @param password
	 * @return
	 */
	public static Cookie createLoginCipherCookie(String loginName, String password) {
		String loginCipher = CookieHelper.generateLoginCipher(loginName, password);
		Cookie c = new Cookie(KEY_LOGIN_NAME, loginCipher);
		c.setMaxAge(2592000);	//30 days
		return c;
	}
	
	private static String validateLoginCipher(String loginCipher){
		String[] tmp = loginCipher.split(":");
		if(tmp.length != 2)
			return null;
		
		String loginName = tmp[0];
		String cipher = tmp[1];
		
		int n = hash(loginName);
		String calculatedCipher = Integer.toString(n);
		if(!cipher.equals(calculatedCipher))
			return null;

		return loginName;
	}

	/**
	 * Create an encrypted string from user name and password.
	 * The algorithm maps a big set into a small set. So it's impossible
	 * to deduce the exact password from the cipher.
	 * @param loginName
	 * @param password
	 * @return
	 */
	private static String generateLoginCipher(String loginName, String password) {
		String passwordEncode;
		
		//
		//TODO
		//The method used below does not work as description.
		//
		int n = hash(loginName);
		//name hash
		
		passwordEncode = Integer.toString(n);
		
		String cipher = loginName + ":" + passwordEncode;
		return cipher;
	}
	
	private static int hash(String s){
		int hash = 0, multiplier = 1;
		for (int i = s.length() - 1; i >= 0; i--) {
			hash += s.charAt(i) * multiplier;
			int shifted = multiplier << 5;
			multiplier = shifted - multiplier;
		}
		return hash;
	}

}
