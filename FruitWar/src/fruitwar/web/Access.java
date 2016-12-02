package fruitwar.web;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fruitwar.util.Logger;
import fruitwar.web.server.ServerConfig;
import fruitwar.web.util.CookieHelper;
import fruitwar.web.util.IntranetIDValidator;


import java.io.*;

/**
 * Handler of login request
 * @author wangnan
 *
 */
public class Access extends MyServletBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5769093168579747782L;
	
	
	static final String URI_LOGIN = "/access";
	static final String URI_LOGOUT = "/access/logout";
	private static final String URL_LOGIN_SUCC_PAGE = "/fruitwar/static/loginsucc.html";
	
	public static final String KEY_LOGIN_NAME = CookieHelper.KEY_LOGIN_NAME;

	public StringBuffer handleRequest(HttpServletRequest request, HttpServletResponse response, String subURI)
			throws ServletException, IOException {

		StringBuffer ret = null;
		if(subURI.equalsIgnoreCase(URI_LOGIN)){
			ret = handleLogin(request, response);
		}else if(subURI.equalsIgnoreCase(URI_LOGOUT)){
			ret = handleLogout(request, response);
		}
		
		if(ret == null)
			return HtmlForger.getMessagePage("Unknown error during login/logout");
		else
			return ret;
	}


	private StringBuffer handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String loginName = (String) request.getParameter("name");
		String password = "";	//(String) request.getParameter("password");
		if(loginName == null)// || password == null)
			return null;
		
		loginName = loginName.trim();
		//password = password.trim();
		if(loginName.length() == 0)	// || password.length() == 0)
			return null;
		
		
		
		//validate the login
		
		int ret = validateLogin(loginName, password);
		
		if(ret != IntranetIDValidator.RC_SUCCESS){
			String reason = null;
			switch(ret){
			//case IntranetIDValidator.RC_SUCCESS:
			case IntranetIDValidator.RC_INVALID_NAME:
				reason = "Invalid user name.";
				break;
			case IntranetIDValidator.RC_INVALID_AUTH:
				reason = "Invalid password.";
				break;
			case IntranetIDValidator.RC_NO_CONNECTION:
				reason = "Network connection problem.";
				break;
			default:
				reason = "Unknown error: " + ret;
				break;
			}
			
			StringBuffer buf = new StringBuffer();			
			buf.append(HtmlForger.getMessagePage("Login failed: " + reason + " Please use a valid <b>IBM Intranet ID</b> to login."));
			Logger.log("Login request: [" + loginName + "] failed: " + reason);
			return buf;
		}else{
			//login success.
			
			//store login info in session
			HttpSession session = request.getSession();
			session.setAttribute(KEY_LOGIN_NAME, loginName);
			
			//store login status in cookie if selected
			//String rememberPasswd = (String) request.getParameter("remember");
			//if(rememberPasswd != null 
			//		&& (rememberPasswd.equals("on") 
			//				|| Boolean.valueOf(rememberPasswd).booleanValue())){

			//always remember login since we do not need password now.
				Cookie c = CookieHelper.createLoginCipherCookie(loginName, password);
				response.addCookie(c);
			//}
			
			response.sendRedirect(URL_LOGIN_SUCC_PAGE);
			
			Logger.log("Login request: [" + loginName + "] success.");
			
			return null;
		}
	}

	/**
	 * Get the current login name.
	 * Set 
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	static String requestLoginName(HttpServletRequest request, HttpServletResponse response) throws IOException {

		//1. check session
		HttpSession session = request.getSession();
		String loginName = (String) session.getAttribute(KEY_LOGIN_NAME);
		if(loginName != null)
			return loginName;
		
		//we have no loginName in session, check cookie.
		Cookie[] cookies = request.getCookies();
		loginName = CookieHelper.validateCookieLogin(cookies);
		
		//if we're not logged in, redirect to login page
		if(loginName == null){
			response.sendRedirect("/fruitwar/static/login.html");
			return null;
		}
		
		//we have loginName stored in cookie. Set it in session
		session.setAttribute(KEY_LOGIN_NAME, loginName);
		
		return loginName;
	}


	private StringBuffer handleLogout(HttpServletRequest request,
			HttpServletResponse response) {
		//remove sesstion attr.
		HttpSession session = request.getSession();
		session.removeAttribute(KEY_LOGIN_NAME);
		session.invalidate();
		
		//remove cookie attr.
		Cookie c = new Cookie(KEY_LOGIN_NAME, "");
		c.setMaxAge(0);
		response.addCookie(c);

		return HtmlForger.getMessagePage("Logout successfully.", Main.URL_HOME, "Home");
	}
	
	private int validateLogin(String name, String password){
		//handle special accounts
		if(name.equalsIgnoreCase("FruitWar")
				|| name.equalsIgnoreCase("FruitWar.test")
				|| name.startsWith("FruitWar"))
			return IntranetIDValidator.RC_INVALID_NAME;
		
		//currently no password required. Check user name only
		//int ret = IntranetIDValidator.validate(loginName, password);
		//int
		int ret = IntranetIDValidator.RC_SUCCESS;
		String loginSetting = System.getProperty(ServerConfig.KEY_LOGIN_TYPE); 
		if(loginSetting != null && !loginSetting.equalsIgnoreCase("any"))
			ret = IntranetIDValidator.validateName(name);
		
		return ret;
	}
}