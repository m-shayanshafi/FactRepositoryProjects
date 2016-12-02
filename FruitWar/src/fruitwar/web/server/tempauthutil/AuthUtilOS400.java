package fruitwar.web.server.tempauthutil;
import fruitwar.util.Logger;

/*
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.security.auth.AS400Credential;
import com.ibm.as400.security.auth.ProfileTokenCredential;



public class AuthUtilOS400 extends AuthUtil {

	public static final String SWAP_USER_KEY = "fruitwar.os400.swapUser"; 
	private static final int EXPIRE_TIME = 3500;	//max 3600, we make it 3500
	
	
	private AS400 system = new AS400();
	private AS400Credential previousUserCredential = null;
	private ProfileTokenCredential tempProfileTC = null;
	private String user = System.getProperty(SWAP_USER_KEY, null); 
	private long tokenCreatedTime = 0;
	
	protected void swapToRestrictedUserImpl() {
		
		if(user == null || user.length() == 0) {
			Logger.log("Swap user key (" + SWAP_USER_KEY + ")not set. Skip user swapping.");
			return;
		}
		//Logger.log("Swapping user to [" + user + "]...");
		
		tempProfileTC = new ProfileTokenCredential();
		try {
			tempProfileTC.setSystem(new AS400());
			tempProfileTC.setTimeoutInterval(EXPIRE_TIME);
			tempProfileTC.setTokenType(ProfileTokenCredential.TYPE_SINGLE_USE);
			tempProfileTC.setToken(user, ProfileTokenCredential.PW_NOPWD);
	        previousUserCredential = tempProfileTC.swap(true);
	        tokenCreatedTime = System.currentTimeMillis();
	        //Logger.log("Swap to user [" + user + "] success.");
		} catch (Exception e) {
			Logger.exception(e);
			
			//if there's exception, do not swap and keep working
			previousUserCredential = null;
			try {
				tempProfileTC.destroy();
			} catch (AS400SecurityException e1) {
				Logger.exception(e1);
			}
			tempProfileTC = null;
		} 
	}

	protected void swapToServerUserImpl() {

		if(tempProfileTC != null){
			try {
				tempProfileTC.destroy();
			} catch (AS400SecurityException e) {
				Logger.exception(e);
			}
	
	        tempProfileTC = null;
		}
		
        //swap back
		if(previousUserCredential != null){
	        try {
				previousUserCredential.swap();
				//Logger.log("Swap back to server user success.");
			} catch (Exception e) {
				Logger.exception(e);
			}
	        try {
				previousUserCredential.destroy();
			} catch (AS400SecurityException e) {
				Logger.exception(e);
			}
			previousUserCredential = null;
		}
	}

	protected void finalize(){
		//this is need normally but it's not needed in our case.
		//Just make this to mark the whole logic complete. 
		system.disconnectAllServices();
	}

	protected boolean isExpiredImpl() {
		
		long secondsPassed = 
			(System.currentTimeMillis() - tokenCreatedTime) / 1000;
		//make 1 minute buffer.
		return (EXPIRE_TIME - 60) <= secondsPassed; 
	}
}
*/

public class AuthUtilOS400 extends AuthUtil {

	protected boolean isExpiredImpl() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void swapToRestrictedUserImpl() {
		Logger.log("xxxx ----> Swap the temp code back, or create a separated jar for OS400...");
		//throw new RuntimeException("xxxx ----> Swap the temp code back, or create a separated jar for OS400...");
	}

	protected void swapToServerUserImpl() {
		// TODO Auto-generated method stub
		
	}
	
}