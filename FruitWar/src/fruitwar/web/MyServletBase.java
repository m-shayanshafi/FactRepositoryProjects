package fruitwar.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fruitwar.web.server.Server;

public abstract class MyServletBase extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String BaseServletURL = "/fruitwar";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		//set encoding
		response.setContentType("text/html; charset=UTF-8");

		//check init status. Trick to avoid main servlet.
		if(!Server.isInitialized() && !getClass().getName().equals("fruitwar.web.Main")){
			response.sendRedirect("/fruitwar/static/serverreboot.html");
			return;
		}

		//get sub URI
		String uri = request.getRequestURI();
		if(uri.endsWith("/"))
			uri = uri.substring(0, uri.length() - 1);
		
		String subURI = uri.substring(BaseServletURL.length());
		
		//wrap with standard HTML header code
		StringBuffer buf = new StringBuffer();
		buf.append(HtmlForger.getStandardHeader());
		
		
		//call handler.
		StringBuffer ret = handleRequest(request, response, subURI);
		if(ret == null)
			return;
		buf.append(ret);
		
		//close html page
		buf.append(HtmlForger.getStandardClosing());
		
		//print html out.
		response.getWriter().print(buf);
	}
	
	public abstract StringBuffer handleRequest(HttpServletRequest request, HttpServletResponse response, String subURI)
		throws ServletException, IOException;
	
}
