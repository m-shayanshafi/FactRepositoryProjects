package fruitwar.web.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import fruitwar.supp.RobotProps;
import fruitwar.core.BattleResult;
import fruitwar.util.FileUtil;
import fruitwar.util.Logger;

/**
 * Server function helper.
 * 
 * 
 * @author wangnan
 *
 */
class ServerImpl {

	
	private static boolean initialized = false;
	private static final String ROBOT_TESTER = "RobotTester";
	
	synchronized static boolean init(String serverHome){
		if(initialized)
			return true;
		
		Logger.log("Server.init start.");
		
		if(!ServerConfig.init(serverHome))
			return false;
		
		//validate server repository
		if(!validateRepository())
			return false;
		
		//init server settings
		ServerInfo.init();
		
		//load robots
		RobotDataCentre.init();
		
		//init battle daemon
		BattleDaemon.init();
		
		//init DataFlusher to ensure data is saved.
		DataFlusherDaemon.init();
		
		Logger.log("Server.init end.");
		
		initialized = true;
		return true;
	}
	
	/**
	 * Check server repository structure based on ServerConfig.
	 * Create repository if necessary.
	 */
	private static boolean validateRepository() {
		return validateServerFile(ServerConfig.getBasePath(), true)
				&& validateServerFile(ServerConfig.getDataPath(), true)
				&& validateServerFile(ServerConfig.getUploadPath(), true)
				&& validateServerFile(ServerConfig.getRobotPath(), true)
				//TTTTTTTTT
				&& validateServerFile(ServerConfig.getRobotPath() + '/' + ROBOT_TESTER + ".jar", false);
				
	}

	private static boolean validateServerFile(String path, boolean isDir) {
		File f;
		f = new File(path);
		
		if(!f.exists()){
				Logger.error("File does not exists: " + path);
				return false;
		}else{
			if(isDir){
				if(f.isDirectory())
					return true;
				Logger.error("File " + path + " is not a directoy!");
				return false;
			}else{
				if(!f.isDirectory())
					return true;
				Logger.error("File " + path + " is not a file!");
				return false;
			}
		}
	}

	
	/**
	 * stop/destroy the server.
	 * 
	 * @param serverHome
	 */
	synchronized static void destroy(){
		Logger.log("Server destroying...");
		
		initialized = false;
		
		//data flusher must be stopped before every data object.
		DataFlusherDaemon.destroy();
		
		//we must stop battle daemon before destroy data centre.
		BattleDaemon.destroy();
		
		RobotDataCentre.destroy();
		
		Logger.log("Server destroyed.");
	}
	
	synchronized static boolean isInitialized(){
		return initialized;
	}
	
	static synchronized ServerInfo getServerInfo() {
		return ServerInfo.instance();
	}
		
	static synchronized void uploadRobotSource(String src, RobotProps prop, PrintWriter out) {
		
		out.println("Generating robot jar...");
		
		boolean success = false;
		
		do{
			RobotJarUtil rju = new RobotJarUtil(out, out);
			
			out.println("Parsing public class name...");
			RobotJarUtil.ClassNameInfo cni = rju.identifyRobotClassName(src);
			if(cni == null){
				out.println("ERROR! -- Unable to identify main class name.");
				break;
			}
			String robotName = cni.getFullName();
			
			out.println("Identified robot class name = <b>" + robotName + "</b>");
			
			out.println("Checking name...");
			if(robotName.length() > 128){
				out.println("Such a long name (" + robotName.length() + ")...are you going to trash me?");
				break;
			}
			
			if(RobotDataCentre.isRobotExist(robotName)){
				out.println("ERROR! -- The robot with the same name already exists. The robot name is the public class name, please change it and try again.");
				break;
			}

			if(!RobotDataCentre.isRobotNameValid(robotName)){
				out.println("ERROR! -- The name is not valid. Please try another name. For example, do not use preserved package names such as fruitwar.sample.");
				break;
			}
			
			//create user dirs if necessary
			String uploadPath = ServerConfig.getUploadPath() + prop.getAuthor() + '/';
			String tempPath = uploadPath + "temp/";
			if(!FileUtil.makeDirs(uploadPath)){
				out.println("ERROR! -- Fail making dir: " + uploadPath);
				break;
			}
			if(!FileUtil.makeDirs(tempPath)){
				out.println("ERROR! -- Fail making dir: " + tempPath);
				break;
			}
			
			//save file
			out.println("Saving file...");
			String fileName = uploadPath + cni.getShortName() + ".java";
	
			try {
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(src.getBytes());
				fos.close();
			} catch (IOException e) {
				out.println("Exception: " + e);
				e.printStackTrace(out);
				break;
			}
			
			//clean temp dir
			out.println("Cleaning up old temporary files...");
			if(!FileUtil.cleanDirFiles(tempPath)){
				out.print("ERROR! -- Fail cleaning up temp dir.");
				break;
			}
			
			//copy file to temp dir to prepare packaging
			out.println("Preparing temporary files...");
			File f = new File(fileName);
			String dstFileName = tempPath + f.getName();
			if(!FileUtil.copy(fileName, dstFileName)){
				out.println("ERROR! -- Fail copying file.");
				break;
			}
			
			//compile, package, test
			out.println("Compiling...");
			prop.set(RobotProps.ROBOT_CLASS, robotName);	
			if(!rju.packFileToFWRJ(dstFileName, prop)){
				out.println("ERROR! -- Fail making robot jar.");
				break;
			}
			
			//test whether the robot can do a battle.
			out.println("Testing...");
			if(!testRobot(robotName, out)){
				out.println("ERROR! -- Fail testing robot. If the error above is not caused by your robot, please contact dev.");
				break;
			}
			
			out.println("Registering...");
			if(!RobotDataCentre.registerRobot(robotName)){
				out.println("ERROR! -- Error registering robot.");
				break;
			}
			
			//clean temp dir again
			out.println("Cleaning up temporary files...");
			if(!FileUtil.cleanDirFiles(tempPath)){
				out.print("ERROR! -- Fail cleaning up temp dir.");
				break;
			}
			
			out.println("Restarting battle daemon if necessary...");
			BattleDaemon.setRunFlag(true);
			
			out.println("\nYour FruitWar robot name is:  <b>" + cni.getFullName() + "</b>\n");
			
			success = true;
		}while(false);
			
		
		getServerInfo().addRobotUploadedCount();
		
		if(success){
			out.println("<font color='#00FF00'><b>SUCCESS!</b></font>");
			getServerInfo().addRobotUploadedSuccessCountHistory();
			Logger.log("Robot upload success: <" + prop.getName() + ">, author = [" + prop.getAuthor() + "]");
		}
		else{
			out.println("<font color='#FF0000'><b>FAIL!</b></font>");
			Logger.log("Robot upload failed: <" + prop.getName() + ">, author = [" + prop.getAuthor() + "]");
		}
	}
	
	static boolean testRobot(String robotName, PrintWriter out){
		BattleResult result = doBattle(robotName, ROBOT_TESTER);
		out.println("<blockquote>");
		out.println(result.formatString());
		out.println("</blockquote>");
		if(result.getException() != null){
			out.println("Exception occured.");
			return false;
		}
		if(result.getWinner() == BattleResult.TEAM_1){
			out.println("<font color='#00AA00'>Your robot has won the test battle. A great start!</font>\n");
		}
		return true;
	}

	public static BattleResult doBattle(String robot1, String robot2){
		return BattleDaemon.getBattleUtil().doBattle(robot1, robot2);
	}
	
	static String getBattleDaemonStatus(){
		int status = BattleDaemon.getStatus();
		switch(status){
		case BattleDaemon.STATUS_STOPPED:			return "STOPPED";
		case BattleDaemon.STATUS_RUNNING:			return "RUNNING";
		case BattleDaemon.STATUS_END:				return "END";
		default:									return null;
		}
	}
}
