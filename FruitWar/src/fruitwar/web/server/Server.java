package fruitwar.web.server;

import java.io.PrintWriter;

import fruitwar.supp.RobotProps;
import fruitwar.core.BattleResult;


/**
 * The always-running server object, for actual task management.
 * 
 * This is the only public point of server package.
 * 
 * Provided methods:
 * 	Add new robot
 *  Delete robot
 * 	Query robot
 * 		by ranking
 * 		by name
 * 		by author
 * 	Query server status.
 * 	Query server info
 * 	Do battle
 * 	Get battle history
 * 
 * @author wangnan
 *
 */
public class Server {

	public static void init(String serverHome){
		ServerImpl.init(serverHome);
	}
	
	/**
	 * stop/destroy the server.
	 * 
	 * @param serverHome
	 */
	public static void destroy(){
		ServerImpl.destroy();
	}
	
	public static void refreshRobotList(){
		RobotDataCentre.refreshRobotList();
	}
	
	public static boolean isInitialized(){
		return ServerImpl.isInitialized();
	}
	
	
	public static ServerRobotProps[] queryRanking(int from, int count) {
		return RobotDataCentre.queryRanking(from, count);
	}

	public static ServerRobotProps getRobot(String name) {
		return RobotDataCentre.getRobot(name);
	}
	
	public static synchronized boolean deleteRobot(String name) {
		boolean ret = RobotDataCentre.deleteRobot(name);
		if(ret)
			getServerInfo().addRobotDeleteCountHistory();
		return ret;
	}
	
	public static ServerInfo getServerInfo() {
		return ServerImpl.getServerInfo();
	}

	/**
	 * Check whether the robot with the same name exists, as well
	 * as whether a jar 
	 * @param name
	 * @return
	 */
	public static boolean isRobotExist(String name){
		return RobotDataCentre.isRobotExist(name);
	}
	
	/**
	 * Check whether the robot name is valid. User robot can
	 * not use preserved names, or start with package fruitwar.core.
	 * @param name
	 * @return
	 */
	public static boolean isRobotNameValid(String name){
		return RobotDataCentre.isRobotNameValid(name);
	}
	
	/**
	 * Get how many robots we currently have.
	 * @return
	 */
	public static int getRobotCount() {
		return RobotDataCentre.getRobotCount();
	}
	
	public static synchronized void uploadRobotSource(String src, RobotProps prop, PrintWriter out) {
		ServerImpl.uploadRobotSource(src, prop, out);
	}

	public static StringBuffer getSimpleRobotSrc(String name) {
		return RobotJarUtil.getSimpleRobotSrc(name);
	}
	
	public static BattleResult doBattle(String robot1, String robot2){
		return ServerImpl.doBattle(robot1, robot2);
	}
	
	public static ServerBattleResult[] getBattleHistory(){
		return RobotDataCentre.getBattleHistory();
	}

	public static ServerBattleResult[] getBattleHistory(String robotName){
		return RobotDataCentre.getBattleHistory(robotName);
	}
	
	public static ServerRobotProps[] queryRobotsByAuthor(String author) {
		return RobotDataCentre.queryRobotsByAuthor(author);
	}

	public static ServerBattleResult getBattleResult(String id) {
		return RobotDataCentre.getBattleResult(id);
	}

	public static boolean startBattleDaemon(boolean start) {
		return BattleDaemon.setRunFlag(start);
	}

	public static String getBattleDaemonStatus(){
		return ServerImpl.getBattleDaemonStatus();
	}

	public static String getRepository() {
		return ServerConfig.getBasePath();
	}
}
