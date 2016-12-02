package fruitwar.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import fruitwar.Rules;
import fruitwar.supp.RobotProps;
import fruitwar.core.BattleResult;
import fruitwar.web.server.Server;
import fruitwar.web.server.ServerBattleResult;
import fruitwar.web.server.ServerInfo;
import fruitwar.web.server.ServerRobotProps;
import fruitwar.web.util.HtmlPrintWriter;
import fruitwar.web.util.SourceCodeRenderer;
import fruitwar.web.util.TimeInterval;

/**
 * A simple class to forge html file from template.
 * Just like jsp.
 *  
 * @author wangnan
 *
 */
public class HtmlForger {

	private static final String LINK_ALL_RANKING = "<a href='" + Main.URL_ALL_RANKING + "'>All Ranking</a>";
	private static final String LINK_ADMIN = "<a href='/fruitwar/static/admin.html'>Admin</a>";
	private static final int DESCRIPTION_SHORT_LEN = 40;
	private static final int ENABLE_ROBOT_DELETE_THRESHOLD = 6;
	
	static final String URL_BACK = "javascript:history.go(-1)";
	
	private HtmlForger(){
	}
	
	static StringBuffer forge(String templateFileName, Vector replacements){
		StringBuffer buf = new StringBuffer();
		return buf;
	}
	
	public static synchronized StringBuffer getHomePage(){
		
		StringBuffer buf = new StringBuffer();
		buf.append("<html>\n");
		buf.append("<head>\n");
		buf.append("<title>FruitWar</title>\n");
		buf.append("</head>\n");

		buf.append("<body>\n");
		buf.append("<table border=0 width=100%>\n");
		buf.append("<tr>\n");
		buf.append("	<td width=60%>\n");
		buf.append("		<table border=0 width=100% height=100%>\n");
		buf.append("			<tr>\n");
		buf.append("				<td>\n");
		buf.append("					<iframe src='/fruitwar/static/home.html' width=100% height=300 scrolling='Auto' frameborder='0'></iframe>\n");
		buf.append("				</td>\n");
		buf.append("			</tr>\n");
		buf.append("			<tr><td><br><hr></td></tr>\n");
		buf.append("			<tr><td><iframe src='/fruitwar/static/news.html' width=100% height=200 scrolling='Auto' frameborder='0'></iframe></td></tr>\n");
		buf.append("		</table>\n");
		buf.append("	</td>\n");
			
		buf.append("	<td bgcolor='#E1E8F0'></td>");
		buf.append("	<td valign=top>\n");
		buf.append("		<table border=0 width=100% height=100%>\n");
		buf.append("			<tr height=400>\n");
		buf.append("				<td valign=top>\n");
		buf.append(getRankingTable(0, 10, false));
		buf.append("<br>" + LINK_ALL_RANKING + "\n");
		buf.append("				</td>\n");
		buf.append("			</tr>\n");
		buf.append("		</table>\n");
		buf.append("	</td>\n");
		buf.append("</tr>\n");
		buf.append("</table>\n");

		
		buf.append("</body>\n");

		buf.append("</html>\n");

		return buf;
	}

	
	static synchronized StringBuffer getRankingTable(int from, int count, boolean detail){
		
		ServerRobotProps[] rankedProps = Server.queryRanking(from, count);

		String lastUpdateTime = new Date().toString();
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("<b>Current Ranking</b><br>\n");
		buf.append("<font size=-1>&nbsp;" + lastUpdateTime + "</font>\n");
		buf.append("<br><br>\n");

		buf.append("<table border=0 width=100% bordercolor='#697E8F'>\n");
		buf.append("	<tr bgcolor='#697E8F' style='color:#ffffff;'><td width=20>#</td><td>Name</td>");
		buf.append("<td width=50><a href='/fruitwar/static/help.html#Ranking'><font color='#ffffff'>Ranking</font></a></td>");
		if(detail)
			buf.append("<td>Win</td><td>Draw</td><td>Total</td><td>Win ratio</td><td>Author</td><td>Description</td>");
		buf.append("</tr>\n");
		
		if(rankedProps != null){
			for(int i = 0; i < rankedProps.length; i++){
				ServerRobotProps prop = (ServerRobotProps) rankedProps[i];
				
				String color = (i % 2) == 0 ? "#FFFFFF" : "#EEEEEE";
				buf.append("	<tr bgcolor='" + color + "'><td width=50>" + (i+1) 
						+ "</td><td>" + makeRobotLink(prop.getName(), prop.isExceptionalRobot())  
						+ "</td><td>" + prop.getRanking() + "</td>");
				
				if(detail){
					buf.append("<td>" + prop.getWinCount() + "</td>");
					buf.append("<td>" + prop.getDrawCount() + "</td>");
					buf.append("<td>" + prop.getTotalBattleCount() + "</td>");
					String ratio = "-";
					if(prop.getTotalBattleCount() > 0)
						ratio = Integer.toString(prop.getWinCount() * 100 / prop.getTotalBattleCount()) + '%';
					buf.append("<td>" + ratio + "</td>");
				
					//author
					String author = prop.get(RobotProps.AUTHOR);
					
					//in general mode, show only short name
					int n = author.indexOf('@');
					if(n > 0)
						author = author.substring(0, n);
					
					buf.append("<td>" + author + "</td>");
					String desc = prop.get(RobotProps.DESCRIPTION);
					if(desc.length() > DESCRIPTION_SHORT_LEN)
						desc = desc.substring(0, DESCRIPTION_SHORT_LEN) + "...";
					buf.append("<td>" + desc + "</td>");
				}
				
				buf.append("</tr>\n");
			}
		}else{
			buf.append("<tr><td>&nbsp;</td></tr><tr><td colspan=10><i>The table is empty.</i></td></tr><tr><td>&nbsp;</td></tr>\n");
		}
		
		buf.append("</table>\n");

		return buf;
	}

	public static String getStandardHeader() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<html>\n<head><title>Fruit War</title></head><body>\n");
		
		return buf.toString();
	}

	public static String getStandardClosing() {
		return "<br>\n</body>\n</html>";
	}

	private static String makeRobotLink(String name, boolean strikethrough){
		return makeRobotLink(name, name, null, strikethrough);
	}
	
	private static String makeRobotLink(String robotName, String displayName, String operation, boolean strikethrough){
		
		String URL = "/fruitwar/main/robot?name=" + robotName;
		if(operation != null)
			URL += "&op=" + operation;
		
		String ret;
		if(strikethrough)
			ret = "<a href='" + URL + "' title='This is an exceptional robot.'><strike>" + displayName + "</strike></a>";
		else
			ret = "<a href='" + URL + "'>" + displayName + "</a>"; 
			
		return ret;
	}
	
	public static StringBuffer getRankingPage(int from, int count) {
		StringBuffer buf = new StringBuffer();
		
		//buf.append("<p align='right'>" + URL_BATTLE_HISTORY + "</p>");
		buf.append(getRankingTable(from, count, true));
		
		buf.append(getBackLinkHtml());
		return buf;
	}

	public static StringBuffer getBattleHistoryPage() {
		StringBuffer buf = new StringBuffer();
		
		ServerBattleResult[] history = Server.getBattleHistory();
		buf.append(getBattleHistoryTable(history, null));
		
		buf.append(getBackLinkHtml());
		return buf;
	}
	
	private static StringBuffer getBattleHistoryTable(ServerBattleResult[] history, String focusRobot){
		StringBuffer buf = new StringBuffer();
	
		String lastUpdateTime = new Date().toString();
		buf.append("<b>Battle History</b><br>\n");
		buf.append("<font size=-1>&nbsp;" + lastUpdateTime + "</font>\n");
		buf.append("<br><br>\n");

		buf.append("<table border=0 width=100% bordercolor='#697E8F'>\n");
		buf.append("	<tr bgcolor='#697E8F' style='color:#ffffff;'><td width=20>#</td><td>Name</td>");
		buf.append("<td><a href='/fruitwar/static/help.html#Result'><font color='#ffffff'>Result</font></a></td>");
		buf.append("<td><a href='/fruitwar/static/help.html#Ranking'><font color='#ffffff'>Delta Ranking</font></a></td><td>Time</td></tr>\n");
		
		if(history.length > 0){
			for(int i = history.length - 1; i >= 0; i--){
				ServerBattleResult serverResult = history[i];
				BattleResult result = serverResult.getPrimalResult();

				//column 1: id. Also adjust row color.
				String color = (i % 2) == 0 ? "#FFFFFF" : "#EEEEEE";
				buf.append("	<tr bgcolor='" + color + "'><td width=50>" + (i+1) + "</td><td>");

				//whether swap the two sides
				boolean swap = focusRobot == null ? false 
						: result.getName2().equals(focusRobot);

				//column 2: robot names & links
				String robot1 = makeRobotLink(result.getName1(), result.getExceptionType() == BattleResult.TEAM_1);
				if(result.getWinner() == BattleResult.TEAM_1)
					robot1 = "<b>" + robot1 + "</b>";
				
				String robot2 = makeRobotLink(result.getName2(), result.getExceptionType() == BattleResult.TEAM_2);
				if(result.getWinner() == BattleResult.TEAM_2)
					robot2 = "<b>" + robot2 + "</b>";
				
				if(swap)
					buf.append(robot2 + " VS " + robot1);
				else
					buf.append(robot1 + " VS " + robot2);
				
				//column 3: result description.
				String resultDesc = "";
				if(!result.isValid())
					resultDesc = "Invalid result.";
				else if(result.getException() != null)
					resultDesc = "Exception.";
				else{
					if(swap)
						resultDesc = "" + result.getScore2() + ":" + result.getScore1();
					else
						resultDesc = "" + result.getScore1() + ":" + result.getScore2();
				}
				
				resultDesc = "<a href='" + Main.URL_BATTLE_RESULT + "?id=" + result.getTimeID() + "'>" + resultDesc + "</a>";
				//if we are focusing on a robot, mark its lose
				boolean mark = false;
				if(focusRobot != null){
					//check if we lose
					boolean lose = swap && result.getWinner() == BattleResult.TEAM_1
						|| !swap && result.getWinner() == BattleResult.TEAM_2;
					mark = lose;
				}
				if(mark)
					buf.append("</td><td bgcolor='#FFAAAA'>");
				else
					buf.append("</td><td>");
				buf.append(resultDesc	+ "</td>\n");

				//column 4: delta ranking
				if(swap)
					buf.append("<td>" + (-serverResult.getDeltaRanking1()) + ':' + (serverResult.getDeltaRanking1()) + "</td>\n");
				else
					buf.append("<td>" + serverResult.getDeltaRanking1() + ':' + (-serverResult.getDeltaRanking1()) + "</td>\n");
				
				//column 5: time
				Date time = new Date(result.getTimeID());	//this may not be accurate, but enough.
				buf.append("<td>" + time.toString() + "</td>\n");
				buf.append("</tr>\n");
			}
		}else{
			buf.append("<tr><td>&nbsp;</td></tr><tr><td colspan=10><i>The table is empty.</i></td></tr><tr><td>&nbsp;</td></tr>\n");
		}
		
		buf.append("</table>\n");
		
		return buf;
	}
	
	public static StringBuffer getRobotPage(String robotName) {
		
		ServerRobotProps prop = Server.getRobot(robotName);
		
		if(prop == null){
			return getMessagePage("Robot <b>" + robotName + "</b> not found. It may have been deleted.<br>\n");
		}
		
		StringBuffer buf = new StringBuffer();
		boolean isExceptional = prop.isExceptionalRobot();

		if(isExceptional)
			buf.append("<b><strike>" + robotName + "</strike></b><br><br>\n");
		else
			buf.append("<b>" + robotName + "</b><br><br>\n");
		
		
		//TODO
		//buf.append(makeRobotLink(robotName, "Download this robot", Main.ROBOT_OP_DOWNLOAD, false) + "<br>\n");
		
		if(prop.isSimpleRobot()){
			buf.append(makeRobotLink(robotName, "View source (this is a simple robot)", Main.ROBOT_OP_VIEWSRC, false) + "<br>\n");
		}else{
			buf.append("This robot is a complex robot consists of multiple source files. This download function is not ready yet. Contact dev.");
		}
		
		buf.append("<br>\n");
		buf.append("<b>Author</b>: " + prop.getAuthor() + "<br>\n");
		buf.append("<b>Description</b>");
		buf.append("<blockquote>\n");
		String desc = prop.getDescription();
		if(desc == null || desc.length() == 0)
			desc = "<i>(No description)</i>";
		else
			desc = "<font color='" + SourceCodeRenderer.COLOR_COMMENT + "'>" + desc + "</font>"; 
		buf.append(desc + "\n");
		buf.append("</blockquote><br><br>\n");
		
		//display battle record section
		int total = prop.getTotalBattleCount();
		int win = prop.getWinCount();
		int draw = prop.getDrawCount();
		buf.append("Battle record<br>\n");
		buf.append("<blockquote>\n");
		buf.append("<table>\n");
		buf.append("<tr><td>Total</td><td>" + total + "</td></tr>\n");
		buf.append("<tr><td>Win</td><td>" + win);
		if(total > 0)
			buf.append(" (" + win * 100 / total + "%)");
		buf.append("</td></tr>\n");
		buf.append("<tr><td>Draw</td><td>" + draw + "</td></tr>\n");
		buf.append("<tr><td>Lose</td><td>" + (total - win - draw) + "</td></tr>\n");
		buf.append("</table><br>\n");
		buf.append("</blockquote>\n");
		
		//display exception
		if(isExceptional){
			buf.append("<font color='#EE0000'>This is an exceptional robot.</font><br>\n");
			buf.append("<blockquote>\n");
			String exception = prop.getException();
			//java exception uses "\t". This char and any blank character
			//at the begin of a line will be discarded if we use Properties
			//class to manage. Restore them.
			exception = exception.replaceAll("\nat ", "\n&nbsp;&nbsp;at ");
			//convert the string to html format.
			exception = exception.replaceAll("\n", "<br>\n");
			buf.append(exception);
			buf.append("</blockquote>\n");
		}
		
		buf.append("<br>");
		
		//Recent battle record
		buf.append(getBattleHistoryTable(Server.getBattleHistory(robotName), robotName));
		
		buf.append(getBackLinkHtml());
		return buf;
	}

	private static String getBackLinkHtml() {
		return "\n<hr><a href='" + URL_BACK + "'>Back</a><br>\n";
	}

	public static StringBuffer getServerInfoPage() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Server Info&nbsp;&nbsp;&nbsp;&nbsp;" + LINK_ADMIN + "<br><br>\n");
		
		ServerInfo info = Server.getServerInfo();
		if(info ==  null){
			buf.append("Error retrieving server info.");
			return buf;
		}
		
		//general info
		buf.append("<b>General</b><br>\n");
		Runtime rt = Runtime.getRuntime();
		buf.append("<table border=1>\n");
		buf.append("<tr><td>Server name: </td><td>" 					+ info.getName() + "</td></tr>\n");
		buf.append("<tr><td>Fruit War version: </td><td>" 				+ Version.VERSION + "</td></tr>\n");
		buf.append("<tr><td>Build time: </td><td>" 						+ Version.BUILD_TIME + "</td></tr>\n");
		buf.append("<tr><td>Java version: </td><td>"					+ System.getProperty("java.version") + "</td></tr>\n");
		buf.append("<tr><td>Repository path: </td><td>"					+ Server.getRepository() + "</td></tr>\n");
		buf.append("<tr><td>Battle Daemon: </td><td>"					+ Server.getBattleDaemonStatus() + "</td></tr>\n");
		buf.append("<tr><td>Battle count this time: </td><td>"			+ info.getBattleCount() + "</td></tr>\n");
		buf.append("<tr><td>Total battle count in history: </td><td>"	+ info.getBattleCountHistory() + "</td></tr>\n");
		buf.append("<tr><td>Server up time this time: </td><td>"		+ new TimeInterval(0, info.getServerUpTime()) + "</td></tr>\n");
		buf.append("<tr><td>Total server up time in history: </td><td>"	+ new TimeInterval(0, info.getServerUpTimeHistory()) + "</td></tr>\n");
		buf.append("<tr><td>Total robot uploaded: </td><td>"			+ info.getRobotUploadedHistory() + "</td></tr>\n");
		buf.append("<tr><td>Total successful robot upload: </td><td>"	+ info.getRobotUploadedSuccessCountHistory() + "</td></tr>\n");
		buf.append("<tr><td>Total robot deleted: </td><td>"				+ info.getRobotDeleteCountHistory() + "</td></tr>\n");
		
		buf.append("<tr><td>Max memory:	</td><td>"						+ rt.maxMemory() + " bytes</td></tr>\n");
		buf.append("<tr><td>Total memory: </td><td>"					+ rt.totalMemory() + " bytes</td></tr>\n");
		buf.append("<tr><td>Free memory: </td><td>"						+ rt.freeMemory() + " bytes</td></tr>\n");
		buf.append("</table><br><br>\n");
		
		
		//Fruit War setting
		buf.append("<b>Fruit War Rules</b><br>\n");
		buf.append("<table border=1>\n");
		buf.append("<tr><td>fruitwar.Rules.BASKET_CNT:</td><td>"						+ Rules.BASKET_CNT ()+ "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.BASKET_ID_MAX:</td><td>"						+ Rules.BASKET_ID_MAX() + "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.COWARD_LIMIT:</td><td>"						+ Rules.COWARD_LIMIT() + "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.ROUND_TOTAL:</td><td>"						+ Rules.ROUND_TOTAL() + "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.FRUIT_THROWER_HP:</td><td>"					+ Rules.FRUIT_THROWER_HP() + "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.ROBOT_CALCULATION_TIMEOUT:</td><td>"			+ Rules.ROBOT_CALCULATION_TIMEOUT() + "</td></tr>\n");
		buf.append("<tr><td>fruitwar.Rules.ROBOT_CALCULATION_TOTAL_TIMEOUT:</td><td>"	+ Rules.ROBOT_CALCULATION_TOTAL_TIMEOUT() + "</td></tr>\n");
		buf.append("</table><br><br>\n");
		
		//session status
		//buf.append("<hr>\n");
		buf.append("<b>Session</b><br>\n");
		buf.append("Current session count: " + SessionListener.getSessionCount() + "<br>\n");
		buf.append("Total session count this time: " + SessionListener.getHistorySessionCount() + "<br>\n");
		buf.append("-----session table-----<br>\n");
		buf.append("<table border=1>\n");
		buf.append("<tr><b><td>User</td><td>SessionID</td><td>Timeout></td><td>Idle</td></b></tr>\n");
		List sessions = SessionListener.getSessions();
		for(Iterator i = sessions.iterator(); i.hasNext();){
			HttpSession s = (HttpSession) i.next();
			String loginName = (String) s.getAttribute(Access.KEY_LOGIN_NAME);
			String sessionID = "null";
			int sessionTimeout = 0;
			String idleTime = "";
			if(s != null){
				sessionID = s.getId();
				sessionTimeout = s.getMaxInactiveInterval();
				idleTime = new TimeInterval(s.getLastAccessedTime()).toString();
			}
			buf.append("<tr><td>" + loginName + "</td><td>" 
					+ sessionID + "</td><td>" 
					+ sessionTimeout + "</td><td>" 
					+ idleTime + "</td><td>"
					+ "</td></tr>\n");
		}
		buf.append("</table><br><br>\n");
		
		//properties
		buf.append("<b>System properties</b><br>");
		String[] sysProps = System.getProperties().toString().split(",");
		for(int i = 0; i < sysProps.length; i++){
			buf.append(sysProps[i]);
			buf.append("<br>\n");
		}
		
		buf.append(getBackLinkHtml());
		return buf;
	}

	public static StringBuffer getMyRobotsPage(String loginName) {
		StringBuffer buf = new StringBuffer();
		buf.append("Welcome <font color='#1133FF'><i>" + loginName + "</i></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size=-1><a href='/fruitwar/static/login.html'>(This is not me)</a></font><br><br>\n");
		buf.append("<b><a href='/fruitwar/static/upload.html'><font color='#FF2222'>Upload new robot</font></a></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n");
		buf.append("<a href='/fruitwar/static/help.html#HowToWriteARobot'>How to write a robot?</a><br><br><br>\n");
		//buf.append("<a href='/fruitwar/access/logout'>Logout</a><br>\n");
		ServerRobotProps[] myRobots = Server.queryRobotsByAuthor(loginName);
		
		//if we have no robot or only 1 robot, disable delete option.
		boolean enableDelete = myRobots.length >= ENABLE_ROBOT_DELETE_THRESHOLD;
		
		buf.append("<b>My Robots</b><br>\n");

		buf.append("<table border=0 width=100% bordercolor='#697E8F'>\n");
		buf.append("	<tr bgcolor='#697E8F' style='color:#ffffff;'><td width=20>#</td><td>Name</td><td width=50>Ranking</td>");
		buf.append("<td>Win</td><td>Draw</td><td>Total</td><td>Win ratio</td>");
		buf.append("<td>Description</td>");
		buf.append("<td>X</td>");
		buf.append("</tr>\n");
		if(myRobots.length > 0){
			for(int i = 0; i < myRobots.length; i++){
				ServerRobotProps robot = myRobots[i];
				
				String color = (i % 2) == 0 ? "#FFFFFF" : "#EEEEEE";
				buf.append("	<tr bgcolor='" + color + "'><td width=50>" + (i+1) 
						+ "</td><td>" + makeRobotLink(robot.getName(), robot.isExceptionalRobot())  
						+ "</td><td>" + robot.getRanking());
				buf.append("<td>" + robot.getWinCount() + "</td>");
				buf.append("<td>" + robot.getDrawCount() + "</td>");
				buf.append("<td>" + robot.getTotalBattleCount() + "</td>");
				String ratio = "-";
				if(robot.getTotalBattleCount() > 0)
					ratio = Integer.toString(robot.getWinCount() * 100 / robot.getTotalBattleCount()) + '%';
				buf.append("<td>" + ratio + "</td>");
				
				String desc = robot.get(RobotProps.DESCRIPTION);
				if(desc.length() > DESCRIPTION_SHORT_LEN)
					desc = desc.substring(0, DESCRIPTION_SHORT_LEN) + "...";
				buf.append("<td>" + desc + "</td>");
				
				buf.append("<td>");
				if(enableDelete)
					 buf.append(makeRobotLink(robot.getName(), "Delete", Main.ROBOT_OP_DELETE, false));
				buf.append("</td>");
				
				buf.append("</tr>\n");
			}
		}else{
			buf.append("<tr><td>&nbsp;</td></tr><tr><td colspan=10><i>The table is empty.</i></td></tr><tr><td>&nbsp;</td></tr>\n");
		}
		buf.append("</table>");
		buf.append(getBackLinkHtml());
		return buf;
	}
	
	public static StringBuffer getMessagePage(String message) {
		return getMessagePage(message, URL_BACK, "Back");
	}
	
	public static StringBuffer getMessagePage(String message, String nextLinkURL, String nextLinkName) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<br><br><br><br><center><table><tr><td>\n");
		
		if(message == null)
			message = "Unknown error.";
		
		buf.append(HtmlPrintWriter.convertStringToHTMLText(message, false));
		
		buf.append("</td></tr></table></center>\n");
		
		if(nextLinkURL != null && nextLinkName != null)
			buf.append("<hr><center><a href='" + nextLinkURL + "'>" + nextLinkName + "</a></center><br>\n");
		
		return buf;
	}

	public static StringBuffer getViewRobotSrcPage(String name) {
		
		StringBuffer buf = Server.getSimpleRobotSrc(name);

		//post unknown error 
		if(buf == null)
			return getMessagePage("Error: The robot <b>" + name + "</b> does not exist, or unknown error occures.");
		
		//convert the string to html format
		String s = buf.toString();
		s = HtmlPrintWriter.convertStringToHTMLText(s, true);
		s = SourceCodeRenderer.render(s);
		
		String tail = getBackLinkHtml();
		buf = new StringBuffer(s.length() + tail.length());
		buf.append(s);
		buf.append(tail);
		return buf;
	}

	public static StringBuffer getConfirmDeleteRobotPage(String name) {
		StringBuffer buf = new StringBuffer();
		buf.append("Confirm to delete robot:<br>\n");
		buf.append("<blockquote>\n");
		buf.append("<b>" + name + "</b>");
		buf.append("</blockquote>\n");
		buf.append(makeRobotLink(name, "Delete", Main.ROBOT_OP_DO_DELETE, false));
		buf.append("&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + URL_BACK + "'>Cancel</a>\n");
		return getMessagePage(buf.toString(), null, null);
	}

	public static StringBuffer getBattleResultPage(String id) {
		ServerBattleResult result = Server.getBattleResult(id);
		if(result == null)
			return getMessagePage("Battle result " + id + " not found. Maybe it's too old and has been deleted.");
		StringBuffer buf = new StringBuffer();
		buf.append("<p align=right><a href='/fruitwar/static/help.html#HowToViewDetailedResult'>What does this mean</a></p>");
		buf.append(HtmlPrintWriter.convertStringToHTMLText(result.formatString(), false));
		buf.append("<hr><center><a href='" + URL_BACK + "'>Back</a></center><br>\n");
		return buf;
	}
}
