package fruitwar.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fruitwar.web.server.Server;
import fruitwar.web.util.HtmlPrintWriter;


import java.io.*;

public class Admin extends MyServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2909001361287760774L;

	static final String URI_ADMIN = "/admin";
	static final String URL_ADMIN = MyServletBase.BaseServletURL + "/admin";
	static final String URI_SERVER_INFO = URI_ADMIN + "/serverinfo";
	static final String URI_REFRESH_ROBOTS = URI_ADMIN + "/refreshrobots";
	static final String URI_TEST = URI_ADMIN + "/test";
	static final String URI_START_DAEMON = URI_ADMIN + "/startbattledaemon";
	static final String URI_STOP_DAEMON = URI_ADMIN + "/stopbattledaemon";
	
	public StringBuffer handleRequest(HttpServletRequest request, HttpServletResponse response, String subURI)
			throws ServletException, IOException {
		
		if(subURI.equalsIgnoreCase(URI_REFRESH_ROBOTS)){
			return refreshRobots(request);
		}else if(subURI.equalsIgnoreCase(URI_SERVER_INFO)){
			return getServerInfo(request);
		}else if(subURI.equalsIgnoreCase(URI_TEST)){
			return doTest(request);
		}else if(subURI.equalsIgnoreCase(URI_STOP_DAEMON)){
			return startBattleDaemon(false);
		}else if(subURI.equalsIgnoreCase(URI_START_DAEMON)){
			return startBattleDaemon(true);
		}
		else
			return showSss(request);
	}

	private StringBuffer showSss(HttpServletRequest request) throws IOException {
		return new StringBuffer("Admin - sss");
	}
	
	private StringBuffer refreshRobots(HttpServletRequest request) throws IOException {
		Server.refreshRobotList();
		
		return new StringBuffer("Refreshed robot count = " + Server.getRobotCount() + "<br>\n");
	}
	
	private StringBuffer getServerInfo(HttpServletRequest request) throws IOException{
		return HtmlForger.getServerInfoPage();
	}
	
	private StringBuffer doTest(HttpServletRequest request) throws IOException {
		StringWriter sw = new StringWriter();
		HtmlPrintWriter out = new HtmlPrintWriter(sw); 
		String r1 = request.getParameter("r1");
		String r2 = request.getParameter("r2");
		
		do{
			if(r1 == null || r2 == null){
				out.println("No param.");
				break;
			}
			
			r1 = r1.trim();
			r2 = r2.trim();
			if(r1.length() == 0 || r2.length() == 0){
				out.println("No param.");
				break;
			}
			if(!Server.isRobotExist(r1)){
				out.println("No such robot: " + r1);
				break;
			}
			if(!Server.isRobotExist(r2)){
				out.println("No such robot: " + r2);
				break;
			}
			
			out.println(Server.doBattle(r1, r2).formatBattleLogs(true));
			out.close();
		}while(false);
		
		return sw.getBuffer();
	}
	
	
	private StringBuffer startBattleDaemon(boolean start) {
		boolean running = Server.startBattleDaemon(start);
		StringBuffer buf = new StringBuffer();
		buf.append("Battle daemon is now: " + Server.getBattleDaemonStatus() + ".<br>\n");
		buf.append(running == start ? "SUCCESS." : "FAIL.");
		return HtmlForger.getMessagePage(buf.toString());
	}
}