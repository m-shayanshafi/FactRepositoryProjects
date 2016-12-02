package fruitwar.web.server;

import fruitwar.util.Logger;

public class ServerConfig {
	static final String ROBOT_PATH = "robots/";
	static final String UPLOAD_PATH = "upload/";
	static final String TEMP_PATH = UPLOAD_PATH + "temp/";
	static final String DATA_PATH = "data/";
	
	static final String ROBOT_FILE_EXT = ".jar";
	static final String SERVER_RECORD_FILE = "server.record";
	
	public static final String KEY_SERVER_REPOS = "fruitwar.server.repos";
	public static final String KEY_LOGIN_TYPE = "fruitwar.login";
	
	
	static String basePath = null;
	
	static {
		basePath = System.getProperty(KEY_SERVER_REPOS);
	}

	static boolean init(String serverBasePath) {

		Logger.log("ServerConfig.init start.");
		Logger.log("Repository path from Web XML: " + serverBasePath);
		
		if(serverBasePath != null){
			if(basePath == null)
				basePath = serverBasePath;
			else
				Logger.log("Base path already defined in startup parameter " + KEY_SERVER_REPOS + ". Ignore servlet config file.");
		}
		
		if(basePath == null){
			Logger.error("CRITICAL! Base path is null. Config it using JVM start parameter (" 
					+ KEY_SERVER_REPOS + "), or serverlet config file (WEB-INF/web.xml).");
			return false;
		}
		
		basePath = serverBasePath;
		
		Logger.log("basePath = " + basePath);
		Logger.log("ServerConfig.init end.");
		return true;
	}
	
	static String getBasePath(){
		return basePath;
	}
	
	static String getDataPath(){
		return basePath + DATA_PATH;
	}
	
	static String getRobotPath(){
		return basePath + ROBOT_PATH;
	}
	
	static String getUploadPath(){
		return basePath + UPLOAD_PATH;
	}
	
	static String getTempPath(){
		return basePath + TEMP_PATH;
	}
}
