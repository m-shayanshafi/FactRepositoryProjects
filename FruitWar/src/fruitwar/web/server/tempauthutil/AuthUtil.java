package fruitwar.web.server.tempauthutil;

public abstract class AuthUtil {
	
	private static AuthUtil instance = null;
	
	static {
		String osName = System.getProperty("os.name");
        //String osVersion = System.getProperty("os.version");
        
        if(osName.startsWith("OS/400"))
        	instance = new AuthUtilOS400();
        else 
        	instance = new AuthUtilDefault();
	}
	
	public static void swapToRestrictedUser(){
		instance.swapToRestrictedUserImpl();
	}
	
	public static void swapToServerUser(){
		instance.swapToServerUserImpl();
	}
	
	public static boolean isExpired(){
		return instance.isExpiredImpl();
	}
	
	protected abstract void swapToRestrictedUserImpl();
	protected abstract void swapToServerUserImpl();
	protected abstract boolean isExpiredImpl();
}
