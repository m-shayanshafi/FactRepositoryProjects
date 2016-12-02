package fruitwar.web.server;

import fruitwar.util.Logger;
import fruitwar.web.util.PropertiesFile;

public class ServerInfo {

	private static final String NAME = "name";
	private static final String BATTLE_COUNT_HISTORY = "battleCountHistory";
	private static final String UP_TIME_HISTORY = "upTimeHistory";
	private static final String ROBOT_UPLOADED_HISTORY = "robotUploadedHistory";
	private static final String ROBOT_UPLOADED_SUCCESS_HISTORY = "robotUploadedSuccessHistory";
	private static final String ROBOT_DELETE_HISTORY = "robotDeleteHistory";
	
	private PropertiesFile prop;
	private long upTime;		//time record for server up (this time)
	private long upTimeHistory;	//time record for history server up (all previous up time plus this time)
	private long lastRecordTime;	//The last time we store upTimeHistory.
	private int battleCount;
	private int battleCountHistory;
	private int robotUploadedHistory;
	private int robotUploadedSuccessHistory;
	private int robotDeleteHistory;
	
	private static ServerInfo instance = new ServerInfo();
	
	static ServerInfo instance(){
		return instance;
	}
	
	synchronized static void init(){
		instance.upTime = System.currentTimeMillis();
		instance.lastRecordTime = instance.upTime;
		instance.loadRecord();
	}
	
	private void loadRecord() {
		Logger.log("ServerInfo.loadRecord start.");
		
		if(prop == null){
			String recordFileName = ServerConfig.getDataPath() + ServerConfig.SERVER_RECORD_FILE;
			prop = new PropertiesFile(recordFileName);
		}
		
		prop.loadOrCreate();
		
		//load values, no matter load succeeded or not
		battleCountHistory = getInt(BATTLE_COUNT_HISTORY, 0);
		upTimeHistory = getLong(UP_TIME_HISTORY, 0);
		robotUploadedHistory = getInt(ROBOT_UPLOADED_HISTORY, 0);
		robotUploadedSuccessHistory = getInt(ROBOT_UPLOADED_SUCCESS_HISTORY, 0);
		robotDeleteHistory = getInt(ROBOT_DELETE_HISTORY, 0);
		
		Logger.log("ServerInfo.loadRecord done.");
	}
	
	///////////////////////////////////////////////////////////////////
	private int getInt(String key, int defaultValue){
		String s = prop.get(key);
		
		if(s != null && s.length() > 0){
			try{
				return Integer.parseInt(s);
			}catch(Exception e){
			}
		}
		return defaultValue;
	}
	
	private void setInt(String key, int n){
		prop.set(key, Integer.toString(n));
	}
	
	private long getLong(String key, long defaultValue){
		String s = prop.get(key);
		
		if(s != null && s.length() > 0){
			try{
				return Long.parseLong(s);
			}catch(Exception e){
			}
		}
		return defaultValue;
	}
	
	private void setLong(String key, long n){
		prop.set(key, Long.toString(n));
	}
	

	///////////////////////////////////////////////////////////////////
	public String getName() {
		return prop.get(NAME, "(No name specified)");
	}
	
	synchronized void addBattleCount(){
		battleCount++;
		battleCountHistory++;
	}
	
	public int getBattleCount(){
		return battleCount;
	}

	public int getBattleCountHistory() {
		return battleCountHistory;
	}

	void addRobotUploadedCount(){
		robotUploadedHistory++;
	}
	
	public int getRobotUploadedHistory(){
		return robotUploadedHistory;
	}

	void addRobotUploadedSuccessCountHistory(){
		robotUploadedSuccessHistory++;
	}

	public int getRobotUploadedSuccessCountHistory(){
		return robotUploadedSuccessHistory;
	}
	
	void addRobotDeleteCountHistory(){
		robotDeleteHistory++;
	}
	
	public int getRobotDeleteCountHistory(){
		return robotDeleteHistory;
	}
	
	/**
	 * get time record for server up (only this time)
	 * @return
	 */
	public long getServerUpTime() {
		return System.currentTimeMillis() - upTime;
	}

	/**
	 * get time record for history server up (all previous up time plus this time)
	 * @return
	 */
	public synchronized long getServerUpTimeHistory() {
		long currentTime = System.currentTimeMillis(); 
		upTimeHistory += currentTime - lastRecordTime;
		lastRecordTime = currentTime;
		
		return upTimeHistory;
	}
	
	synchronized void save(){

		//update history uptime
		getServerUpTimeHistory();
		
		//set props
		setInt(BATTLE_COUNT_HISTORY, battleCountHistory);
		setLong(UP_TIME_HISTORY, upTimeHistory);
		setInt(ROBOT_UPLOADED_HISTORY, robotUploadedHistory);
		setInt(ROBOT_UPLOADED_SUCCESS_HISTORY, robotUploadedSuccessHistory);
		setInt(ROBOT_DELETE_HISTORY, robotDeleteHistory);
		
		prop.save();
		
	}
}
