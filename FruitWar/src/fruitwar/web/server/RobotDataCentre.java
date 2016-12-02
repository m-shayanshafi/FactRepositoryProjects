package fruitwar.web.server;

import java.io.File;
import java.io.FilenameFilter;

import fruitwar.core.BattleResult;
import fruitwar.util.Logger;

/**
 * Data model. Which handles robot instances
 * and related data(ranking, etc).
 * 	[must have]
 * 	add robot
 * 	query ranking
 * 	query robot props
 *	get robot pair for next battle
 *	update battle result
 * 
 * 	[should have]
 * 	remove robot
 *  
 *  
 *  [[[[synchronization]]]
 *  This class also is the synchronization point. Make all
 *  synchronization concern here. Also it's acceptable to make
 *  more separated synchronization decision in sub containers.
 *  
 *  Due to method refreshList rebuild three containers together, 
 *  every function accessing these three containers should sync
 *  with refreshList. The only container level sync is valid
 *  for battleHistory.
 *  
 *  refreshList		[client threads]	!sync!
 *		--robots
 *		--ranking
 *		--battle
 *		loadRobot
 *			addRobot					
 *				--robots
 *				--ranking
 *				--battle				
 *	registerRobot	[client threads]
 *		loadRobot
 *			addRobot					!sync!
 *				--robots
 *				--ranking
 *				--battle				
 *	queryRanking	[client threads]	!sync!
 *		--ranking
 *		getRobot
 *			--robots
 *	getRobot		[client threads, battle daemon thread]	!sync!
 *		--robots
 *	getRobotCount	[client threads]	!sync!
 *		--robots
 *	isRobotExist	[client threads]	!sync!
 *		--robots
 *	popNextBattlePair	[client threads, battle daemon thread]
 *		--battle						
 *	updateBattleResult	[battle daemon thread]	!sync!
 *		getRobot
 *			--robots
 *		updateRobotProp
 *			--ranking
 *		--history						!container native sync!
 *	getBattleHistory	[client threads]
 *		--history						!container native sync!
 
 *  
 *  [[[Known problem]]]
 *  There's potential risk that the "get robot" method returns
 *  the robot properties file, which is can be modified anywhere
 *  and it is not synchronized, though currently we have no such
 *  problem. A more safer way is to return copy, but this requires
 *  refactor of the current ServerRobotProp->PropertiesFile structure.
 *  Probably future.
 *  
 * @author wangnan
 *
 */
class RobotDataCentre {

	private static final int BATTLE_HISTORY_LIMIT = 2000;
	
	private static RobotRepository robots = new RobotRepository();
	
	//these two members should be handled in some other class.....
	private static RankingQueue rankingQueue = new RankingQueue();
	private static BattleQueue battleQueue = new BattleQueue();
	private static BattleHistory battleHistory = new BattleHistory(BATTLE_HISTORY_LIMIT);
	
	private static final String[] perservedRobotNames = {
		"RobotTester",
	};
	
	private static final String[] perservedRobotNamePrefixes = {
		"fruitwar.core.",
		"fruitwar.sample.",
		"sample.",
	};
	
	synchronized static void init(){
		Logger.log("RobotRepository.init start.");
		refreshRobotList();
		Logger.log("RobotRepository.init end.");
	}
	
	synchronized static void destroy(){
		Logger.log("RobotRepository.destory start.");
		robots.save();
		robots.clear();
		rankingQueue.clear();
		battleQueue.clear();
		battleHistory.clear();
		Logger.log("RobotRepository.destory end.");
	}
	
	synchronized static void refreshRobotList(){
		Logger.log("Loading robots....");
		
		//clean current list
		robots.clear();
		rankingQueue.clear();
		battleQueue.clear();
		
		//load list according to data path files
		String dataPath = ServerConfig.getDataPath();
		
		File dataDir = new File(dataPath);
		
		if(!dataDir.exists() || !dataDir.isDirectory()){
			Logger.error("Data dir inexist: " + dataPath);
			return;
		}
		
		//list all ".properties" files.
        File[] entries = dataDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.toLowerCase().
					endsWith(ServerRobotProps.PROP_FILE_EXT);
			}
        });
        
        if(entries != null){
	        //iterate contents
	        for(int i = 0; i < entries.length; i++){
	        	File f = entries[i];
	            if(f.isFile()){
	            	loadRobot(f.getAbsolutePath());
	            }
	        }
        }        
        Logger.log("Done loading robots. Robots loaded: " + robots.size() + ".");
	}

	private static boolean loadRobot(String dataFileName){
   		ServerRobotProps p = 
   			ServerRobotProps.load(dataFileName, ServerConfig.getRobotPath());
   		if(p == null){
   			Logger.log("Fail loading robot info: " + dataFileName);
   			return false;
   		}
   		
		addRobot(p);
		return true;
	}
	
	static boolean registerRobot(String robotName) {
		
		Logger.log("Registering robot: " + robotName);

		String serverRobotPropFileName = 
			ServerConfig.getDataPath() + robotName + ServerRobotProps.PROP_FILE_EXT; 
		
		ServerRobotProps prop = ServerRobotProps.createNewProp(serverRobotPropFileName, robotName);
		
		if(!prop.save()) {
			return false;
		}
		
		if(!loadRobot(serverRobotPropFileName)){
			return false;
		}
			
		
		//let the new robot fight every one 
		battleQueue.arrangeBattle(robotName);
		return true;
	}
	
	synchronized static ServerRobotProps[] queryRanking(int from, int count) {
		RankingQueue.Node[] nodes = rankingQueue.query(from, count);
		if(nodes == null)
			return null;
		
		ServerRobotProps[] props = new ServerRobotProps[nodes.length]; 
		for(int i = 0; i < nodes.length; i++){
			ServerRobotProps p = getRobot(nodes[i].name);
			props[i] = p;
		}
		return props;
	}
	
	synchronized static ServerRobotProps[] queryRobotsByAuthor(String author){
		return robots.queryRobotsByAuthor(author);
	}
	
	synchronized static ServerRobotProps getRobot(String name) {
		return robots.get(name);
	}

	synchronized static int getRobotCount() {
		return robots.size();
	}
	
	private synchronized static void addRobot(ServerRobotProps prop){
		robots.put(prop.getName(), prop);
   		rankingQueue.add(prop.getName(), prop.getRanking());
   		battleQueue.add(prop);
	}
	
	/**
	 * Check whether the robot with the same name exists, as well
	 * as whether a jar 
	 * @param name
	 * @return
	 */
	synchronized static boolean isRobotExist(String name){
		if(robots.containsKey(name))
			return true;
		
		//should check jar file conflict
		
		return false;
	}
	
	/**
	 * Check whether the robot name is valid. User robot can
	 * not use preserved names, or start with package fruitwar.core.
	 * @param name
	 * @return
	 */
	static boolean isRobotNameValid(String name){
		for(int i = 0; i < perservedRobotNamePrefixes.length; i++){
			if(name.startsWith(perservedRobotNamePrefixes[i]))
				return false;
		}
		for(int i = 0; i < perservedRobotNames.length; i++){
			if(name.equals(perservedRobotNames[i]))
				return false;
		}
		return true;
	}

	synchronized static void popNextBattlePair(String[] robotPair) {
		battleQueue.popNextBattlePair(robotPair);
	}
	
	synchronized static void updateBattleResult(BattleResult result){

		//update robot properties according to result
		ServerRobotProps prop1 = getRobot(result.getName1());
		ServerRobotProps prop2 = getRobot(result.getName2());
		
		//currently we have not sync during the battle time (sync that will
		//cause great performance decrease), so the robots may be deleted
		//during battle, and we get null here.
		if(prop1 == null){
			Logger.error("Fail retrieving robot: " + result.getName1());
			return;
		}
		if(prop2 == null){
			Logger.error("Fail retrieving robot: " + result.getName2());
			return;
		}
		
		//calc delta ranking for team 1
		int deltaRanking1 = 0;
		if(result.getWinner() == BattleResult.TEAM_1)
			deltaRanking1 = calcDeltaRanking(prop1.getRanking(), prop2.getRanking());
		else if(result.getWinner() == BattleResult.TEAM_2)
			deltaRanking1 = - calcDeltaRanking(prop2.getRanking(), prop1.getRanking());
		
		updateRobotProp(prop1, result, deltaRanking1);
		updateRobotProp(prop2, result, -deltaRanking1);

		//update history
		battleHistory.add(new ServerBattleResult(result, deltaRanking1));
		
	}
	
	static ServerBattleResult[] getBattleHistory(){
		return battleHistory.getHistory();
	}
	
	static ServerBattleResult[] getBattleHistory(String robotName){
		return battleHistory.getHistory(robotName);
	}
	
	/**
	 * @param myRanking
	 * @param opponentRanking
	 * @return
	 */
	private static int calcDeltaRanking(int winnerRanking, int loserRanking){
		int ret = 15 - (winnerRanking - loserRanking) / 15;
		if(ret < 0)
			ret = 0;
		return ret;
	}
	

	/**
	 * Update robot properties according to battle result.
	 * @param battleResult
	 */
	private static void updateRobotProp(ServerRobotProps prop, 
			BattleResult battleResult, int deltaRanking){
		
		//get our team id in the BattleResutl
		int myTeamID = 0;
		if(prop.getName().equals(battleResult.getName1()))
			myTeamID = BattleResult.TEAM_1;
		else if(prop.getName().equals(battleResult.getName2()))
			myTeamID = BattleResult.TEAM_2;
		else{
			String msg = "How cound this be? myName=" + prop.getName() + " battleResult=" + battleResult; 
			Logger.log(msg);
			throw new RuntimeException(msg);
		}
		
		//if we have an exception, log this as "Exceptional Robot".
		if(battleResult.getExceptionType() == myTeamID){
			prop.setException(battleResult.getException());
			prop.setRanking(0);
			//notify battle queue that I'm exceptional. Do not
			//make any battle for me.
			battleQueue.remove(prop.getName());
		}else{
			
			//add battle count
			prop.increaseTotalBattleCount();
			
			//process win/draw/lose, update count
			if(battleResult.getWinner() == BattleResult.DRAW){
				//draw
				prop.increaseDrawCount();
			}else if(battleResult.getWinner() == myTeamID){
				//win
				prop.increaseWinCount();
			}
			//else lose. Nothing
			
			//modify ranking
			prop.setRanking(prop.getRanking() + deltaRanking);
			
			//update time
			prop.setLastBattleTime(System.currentTimeMillis());
		}

		//update ranking
		rankingQueue.update(prop.getName(), prop.getRanking());
		
		if(!prop.save())
			Logger.log("Error saving prop file: " + prop.getName());
	}
	
	
	synchronized static boolean deleteRobot(String name){

		boolean ret = true;
		//delete from each container.
		ret &= rankingQueue.remove(name);
		battleQueue.remove(name);
		ServerRobotProps prop = robots.delete(name);
		ret &= prop.deleteFile();
		
		//the actual jar file left undeleted.

		Logger.log("Delet robot: <" + name + "> " + (ret ? "success" : "failed"));
		
		return ret;
	}

	synchronized static ServerBattleResult getBattleResult(String id) {
		return battleHistory.get(id);
	}

	synchronized static void save() {
		//save history
		//battleHistory.save();	//we do not save this now.
	}
}
