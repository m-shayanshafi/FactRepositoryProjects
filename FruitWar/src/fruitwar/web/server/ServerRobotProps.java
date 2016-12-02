package fruitwar.web.server;

import java.io.File;

import fruitwar.supp.RobotProps;
import fruitwar.web.util.PropertiesFile;

/**
 * Properties of a robot in server context.
 * This object consists of two parts. The first part is the base properties
 * from the FruitWar Robot jar. It's constant and will never be changed.
 * The second part is for server related properties, such as ranking,
 * win/lose count, etc. This part will be updated frequently.
 *  
 * @author wangnan
 *
 */
public class ServerRobotProps extends PropertiesFile {


	private static final String RANKING = "fwrobot.server.ranking";
	private static final String EXCEPTION = "fwrobot.server.exception";
	private static final String COUNT_WIN = "fwrobot.server.winCount";
	private static final String COUNT_DRAW = "fwrobot.server.drawCount";
	private static final String COUNT_TOTAL_BATTLE = "fwrobot.server.totalBattleCount";
	private static final String LAST_BATTLE_TIME = "fwrobot.server.lastBattleTime";
	private static final String IS_SIMPLE_ROBOT = "fwrobot.server.isSimpleRobot";
	
	public static final String PROP_FILE_EXT = ".properties";
	
	private static final int DEFAULT_RANKING = 1500;
	
	
	private RobotProps baseProp;	//constant value. This will not be modified/saved.
	
	private long cacheLastBattleTime;	//cached value for Last Battle Time.
	private String name;
	
	
	

	
	private ServerRobotProps(String fileName) {
		super(fileName);
		
		this.setInt(RANKING, DEFAULT_RANKING);
	}
	
	private ServerRobotProps(String fileName, String robotName) {
		this(fileName);
		baseProp = new RobotProps();
		baseProp.set(RobotProps.ROBOT_CLASS, robotName);
		name = robotName;
		setSimpleRobot(true);
	}

	/**
	 * Constructor used to to create new robot
	 * @param fileName
	 * @param robotName
	 */
	static ServerRobotProps createNewProp(String fileName, String robotName){
		return new ServerRobotProps(fileName, robotName);
	}
	
	static ServerRobotProps load(String fileName, String robotPath){
		
		//
		//	1. load the prop file directly
		//
		ServerRobotProps prop = new ServerRobotProps(fileName);
		if(!prop.load())
			return null;
		
		//
		//	2. load the robot base properties from the robot jar
		//
		String shortName = new File(fileName).getName();
		String robotJarName = shortName.substring(
				0, shortName.length() - PROP_FILE_EXT.length()) 
				+ ServerConfig.ROBOT_FILE_EXT;
		String robotJarFullPathName = robotPath + robotJarName;
		
		prop.baseProp = RobotProps.loadFromFile(robotJarFullPathName);
		if(prop.baseProp == null)
			return null;
		
		//validate name
		prop.name = prop.baseProp.getName();
		if(prop.name == null)
			return null;
		
		prop.setChanged(false);
		
		return prop;
	}
	
	public String get(String key){
		String val = super.get(key);
		if(val == null && baseProp != null){
			val = baseProp.get(key);
		}
		return val;
	}
	
	public String set(String key, String value){
		if(baseProp != null && baseProp.get(key) != null)
			throw new RuntimeException("Setting value of base RobotProperties is forbidden. key=" + key + ", value=" + value);
		
		return super.set(key, value);
	}
	
	int getInt(String key, int defaultValue){
		String s = get(key);
		
		if(s != null && s.length() > 0){
			try{
				return Integer.parseInt(s);
			}catch(Exception e){
			}
		}
		return defaultValue;
	}

	void setInt(String key, int n){
		set(key, Integer.toString(n));
	}
	
	/**
	 * Get the name of the robot.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public int getRanking() {
		return getInt(RANKING, 0);
	}

	void setRanking(int n){
		set(RANKING, String.valueOf(n));
	}
	
	public int getTotalBattleCount(){
		return getInt(COUNT_TOTAL_BATTLE, 0);
	}
	
	void increaseTotalBattleCount(){
		setInt(COUNT_TOTAL_BATTLE, getTotalBattleCount() + 1);
	}
	
	public int getWinCount(){
		return getInt(COUNT_WIN, 0);
	}
	
	void increaseWinCount(){
		setInt(COUNT_WIN, getWinCount() + 1);
	}
	
	public int getDrawCount(){
		return getInt(COUNT_DRAW, 0);
	}
	
	void increaseDrawCount(){
		setInt(COUNT_DRAW, getDrawCount() + 1);
	}
	
	void setException(String exception){
		set(EXCEPTION, exception);
	}

	public String getException(){
		return get(EXCEPTION);
	}
	
	/**
	 * Whether the robot is an exceptional robot 
	 * (encounter exception during runtime).
	 * @return
	 */
	public boolean isExceptionalRobot(){
		return getException() != null;
	}

	
	void setLastBattleTime(long time) {
		cacheLastBattleTime = time;
		set(LAST_BATTLE_TIME, String.valueOf(time));
	}

	public long getLastBattleTime() {
		if(cacheLastBattleTime == 0){
			String s = get(LAST_BATTLE_TIME);
			if(s != null && s.length() > 0){
				try{
					cacheLastBattleTime = Long.parseLong(s);
				}catch(NumberFormatException e){
				}
			}
		}
		return cacheLastBattleTime;
	}
	
	public void setSimpleRobot(boolean simple){
		set(IS_SIMPLE_ROBOT, Boolean.toString(simple));
	}
	
	public boolean isSimpleRobot(){
		String s = get(IS_SIMPLE_ROBOT);
		return s == null ? false : Boolean.valueOf(s).booleanValue();
	}
	
	public String getDescription(){
		String s = baseProp.get(RobotProps.DESCRIPTION);
		if(s == null)
			s = "";
		return s;
	}
	
	public String getAuthor(){
		return baseProp.get(RobotProps.AUTHOR);
	}

	
	/*
	private static class BattleHistory{
		static final int LIMIT = 50;
		long[] history = new long [LIMIT];
		int tail = 0;
		
		void add(long id){
			if(id <= 0)
				throw new RuntimeException("No...");
			
			history[tail] = id;
			tail++;
			if(tail >= LIMIT){
				tail = 0;
			}
		}
		
		long get(int n){
			if(n >= LIMIT || n < 0)
				return 0;
			
			int head = tail + 1;
			if(head > LIMIT)
				head = 0;
			long ret = history[head];
			if(ret <= 0)
				head = 0;
			return history[(head + n) % LIMIT];
		}
	}
	
	private BattleHistory history = new BattleHistory();
	
	public void addBattleHistoryID(long id){
		history.add(id);
	}
	
	public long getBattleHistoryID(int index){
		return history.get(index);
	}
	*/
}
