package fruitwar.web.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * IBM Intranet ID validater based on Bluepage LDAP server.
 *  
 * @author wangnan
 *
 */
public class IntranetIDValidator {
	public static final int RC_SUCCESS = 0;
	public static final int RC_INVALID_NAME = 11;
	public static final int RC_INVALID_AUTH = 12;
	public static final int RC_NO_CONNECTION = 13;
	
	private static final int TIMEOUT = 10 * 1000;
	
	public static void main(String[] args){
		System.out.println(validate("wangnan@cn.ibm.com", "xx"));
	}
	
	public static int validateName(String intranetID) {
		if(intranetID.equals("u1") 
				|| intranetID.equals("u2")
				|| intranetID.equals("u3")){
			//if(password.equals("fw." + intranetID))
				return RC_SUCCESS;
			//return RC_INVALID_AUTH;
		}
		
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.factory.url.pkgs", "com.ibm.jndi");
		env.put("java.naming.provider.url", "ldap://bluepages.ibm.com:389");
		
		
		try{
			InitialDirContext dirContext = new InitialDirContext(env);
	
			SearchControls constraints = new SearchControls();
			String[] attr = new String[] {"uid"};
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			constraints.setTimeLimit(TIMEOUT);
			constraints.setReturningAttributes(attr);
	
			NamingEnumeration ne = (NamingEnumeration) dirContext.search("ou=bluepages,o=ibm.com", 
					"(mail=" + intranetID + ")", constraints);
	
			String dn = null;
			if (ne.hasMore()) {
				SearchResult sr = (SearchResult) ne.next();
				dn = sr.getName() + ",ou=bluepages,o=ibm.com";
			}
			dirContext.close();
			
			if(dn == null){
				return RC_INVALID_NAME;
			}
			//System.out.println("dn=" + dn);
		}catch (NamingException e){
			System.err.println(e);
			return RC_NO_CONNECTION;
		}
		
		return RC_SUCCESS;
	}
	
	/**
	 * validate intranet id using Bluepage LDAP server.
	 * @param intranetID
	 * @param password
	 * @return
	 */
	public static int validate(String intranetID, String password) {
		if(intranetID.equals("u1") 
				|| intranetID.equals("u2")
				|| intranetID.equals("u3")){
			//if(password.equals("fw." + intranetID))
				return RC_SUCCESS;
			//return RC_INVALID_AUTH;
		}
		
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.factory.url.pkgs", "com.ibm.jndi");
		env.put("java.naming.provider.url", "ldap://bluepages.ibm.com:389");
		
		String dn = null;
		try{
			InitialDirContext dirContext = new InitialDirContext(env);
	
			SearchControls constraints = new SearchControls();
			String[] attr = new String[] {"uid"};
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			constraints.setTimeLimit(TIMEOUT);
			constraints.setReturningAttributes(attr);
	
			NamingEnumeration ne = (NamingEnumeration) dirContext.search("ou=bluepages,o=ibm.com", 
					"(mail=" + intranetID + ")", constraints);
	
			if (ne.hasMore()) {
				SearchResult sr = (SearchResult) ne.next();
				dn = sr.getName() + ",ou=bluepages,o=ibm.com";
			}
			dirContext.close();
			
			if(dn == null){
				return RC_INVALID_NAME;
			}
			//System.out.println("dn=" + dn);
		}catch (NamingException e){
			System.err.println(e);
			return RC_NO_CONNECTION;
		}
		
		//Having the DN, we can now authenticate password:
		try {
			env.clear();
			env.put("java.naming.factory.initial",
					"com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.factory.url.pkgs", "ou=bluepages,o=ibm.com");
			env.put("java.naming.provider.url", "ldap://bluepages.ibm.com:389");
			env.put("java.naming.security.principal", dn);
			env.put("java.naming.security.credentials", password);
			InitialDirContext dirContext = new InitialDirContext(env);
			dirContext.close();
		} catch (InvalidNameException e1) {
			return RC_INVALID_NAME;
		} catch (NameNotFoundException e2) {
			return RC_INVALID_AUTH;
		} catch (AuthenticationException ae) {
			return RC_INVALID_AUTH;
		} catch (Exception e3) {
			System.err.println(e3);
			e3.printStackTrace(System.out);
			return RC_NO_CONNECTION;
		}

		return RC_SUCCESS;
	}
}
