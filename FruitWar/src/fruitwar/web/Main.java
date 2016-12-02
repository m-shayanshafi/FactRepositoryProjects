package fruitwar.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fruitwar.util.Logger;
import fruitwar.web.server.Server;
import fruitwar.web.server.ServerConfig;
import fruitwar.web.server.ServerRobotProps;


import java.io.*;

/**
 * Main servlet. 
 * @author wangnan
 *
 */
public class Main extends MyServletBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4422862864673011515L;
	
	public static final String URI_MAIN = "/main";
	public static final String URI_ALL_RANKING = URI_MAIN + "/allranking";
	public static final String URI_BATTLE_HISTORY = URI_MAIN + "/battlehistory";
	public static final String URI_ROBOT = URI_MAIN + "/robot";
	public static final String URI_MY_ROBOTS = URI_MAIN + "/myrobots";
	public static final String URI_BATTLE_RESULT = URI_MAIN + "/battleresult";
	
	public static final String URL_HOME = MyServletBase.BaseServletURL + URI_MAIN;
	public static final String URL_MY_ROBOTS = MyServletBase.BaseServletURL + URI_MY_ROBOTS;
	public static final String URL_ALL_RANKING = MyServletBase.BaseServletURL + URI_ALL_RANKING;
	public static final String URL_BATTLE_HISTORY = MyServletBase.BaseServletURL + URI_BATTLE_HISTORY;
	public static final String URL_BATTLE_RESULT = MyServletBase.BaseServletURL + URI_BATTLE_RESULT;
	public static final String URL_UPLOAD = MyServletBase.BaseServletURL + "/static/upload.html";
	
	public static final String ROBOT_OP_VIEWSRC = "viewsrc";
	public static final String ROBOT_OP_DOWNLOAD = "download";
	public static final String ROBOT_OP_DELETE = "delete";
	public static final String ROBOT_OP_DO_DELETE = "do_delete";
	
	public void init() throws ServletException {
		super.init();
		String s = getServletConfig().getInitParameter(ServerConfig.KEY_SERVER_REPOS);
		Server.init(s);
	}

	public void destroy(){
		Server.destroy();
		super.destroy();
	}
	
	public StringBuffer handleRequest(HttpServletRequest request, HttpServletResponse response, String subURI)
			throws ServletException, IOException {

		//if server is not initialized, the repository is
		//not configured correctly. Redirect to a special help page
		//to guide the configuration.
		if(!Server.isInitialized()){
			response.sendRedirect("/fruitwar/static/err_configrepos.html");
			return null;
		}
		
		if(subURI.equalsIgnoreCase(URI_ALL_RANKING)){
			return getAllRanking(request);
		}else if(subURI.equalsIgnoreCase(URI_BATTLE_HISTORY)){
			return getBattleHistory(request);
		}else if(subURI.equalsIgnoreCase(URI_ROBOT)){
			return handleRobotRequest(request, response);
		}else if(subURI.equalsIgnoreCase(URI_MY_ROBOTS)){
			return handleMyRobotsRequest(request, response);
		}else if(subURI.equalsIgnoreCase(URI_BATTLE_RESULT)){
			return handleBattleResultRequest(request, response);
		}else{
			return getHomePage();
		}
	}

	private StringBuffer getHomePage() throws IOException {
		return HtmlForger.getHomePage();
	}
	
	private StringBuffer getAllRanking(HttpServletRequest request) throws IOException{
		int from = 0;
		int count = 100;
		
		String sFrom = request.getParameter("from");
		if(sFrom != null)
			from = Integer.parseInt(sFrom);
		String sCount = request.getParameter("count");
		if(sCount != null)
			count = Integer.parseInt(sCount);
		if(count < 0 || count > 200)
			count = 100;
		
		return HtmlForger.getRankingPage(from, count);
	}
	
	private StringBuffer getBattleHistory(HttpServletRequest request) throws IOException{
		return HtmlForger.getBattleHistoryPage();
	}
	
	private StringBuffer handleRobotRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String op = request.getParameter("op");
		String name = request.getParameter("name");
		if(op != null && op.equalsIgnoreCase(ROBOT_OP_DOWNLOAD)){
			return new StringBuffer("TODO");
		}else if(op != null && op.equalsIgnoreCase(ROBOT_OP_VIEWSRC)){
			return HtmlForger.getViewRobotSrcPage(name);
		}else if(op != null && op.equalsIgnoreCase(ROBOT_OP_DELETE)){
			return HtmlForger.getConfirmDeleteRobotPage(name);
		}else if(op != null && op.equalsIgnoreCase(ROBOT_OP_DO_DELETE)){
			return handleDeleteRobotRequest(request, response, name);
		}else{
			//no op or unknown op. Display robot page.
			return HtmlForger.getRobotPage(name);
		}
	}

	private StringBuffer handleDeleteRobotRequest(HttpServletRequest request, HttpServletResponse response, String robotName) throws IOException{
		//get login user name
		String loginName = Access.requestLoginName(request, response); 
		if(loginName == null)
			return null;
		
		Logger.log("User [" + loginName + "] is trying to delete robot <" + robotName + ">");
		
		//check whether the robot exists
		ServerRobotProps prop = Server.getRobot(robotName);
		if(prop == null){
			return HtmlForger.getMessagePage("FAIL: No such robot: " + robotName);
		}
		
		//check whether the current user is the author of the robot
		if(!loginName.equals(prop.getAuthor())) {
			return HtmlForger.getMessagePage(
					"<font color='#EE1111'>FAIL</font>: You are not the author of robot <b>" + robotName 
					+ "</b> and can not perform the operation.");
		}
		
		//do delete
		boolean success = Server.deleteRobot(robotName);
		
		Logger.log("User [" + loginName + "] deletes robot <" + robotName + "> " + (success ? "success." : "failed."));
		
		//report status.
		if(success)
			return HtmlForger.getMessagePage(
				"SUCCESS: " + robotName + " deleted.", URL_MY_ROBOTS, "My Robots");
		return HtmlForger.getMessagePage(
				"<font color='#EE1111'>FAIL</font>: Deleting " + robotName + " failed.");
	}
	
	private StringBuffer handleMyRobotsRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//retrieve login name.
		//client will be redirected to login page if not logged in.
		String loginName = Access.requestLoginName(request, response);
		
		if(loginName != null){
			//if user has robots, show robots page
			if(Server.queryRobotsByAuthor(loginName).length > 0)
				return HtmlForger.getMyRobotsPage(loginName);
			
			//user has no robots, redirect to upload page.
			response.sendRedirect(URL_UPLOAD);
		}

		return null;
	}


	private StringBuffer handleBattleResultRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		return HtmlForger.getBattleResultPage(id);
	}
}