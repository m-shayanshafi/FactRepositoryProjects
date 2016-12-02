package fruitwar.web.util;

import java.io.PrintWriter;
import java.io.Writer;


/**
 * Simple printer writer for Html. Only for method println(String)
 * 
 * @author wangnan
 *
 */
public class HtmlPrintWriter extends PrintWriter {

	public HtmlPrintWriter(Writer out) {
		super(out, true);
	}
	
	/**
	 * Convert any "\n" in s to "<br>\n".
	 * 
	 * @param s
	 */
	public synchronized void println(String s) {
		s = convertStringToHTMLText(s, false);
		super.println(s + "<br>\n");
	}
	
	/**
	 * Convert the given string to html format.
	 * Replace "\n" in the given string to "<br>\n".
	 * Make sure space and \t at the beginning of line is
	 * converted to &nbsp;
	 * @param s
	 * @return
	 */
	public static String convertStringToHTMLText(String s, boolean convertSpecialChars){
		//s = s.replaceAll("&", "&amp;");
		//s = s.replaceAll("\"", "&quot;");
		//s = s.replaceAll(" ", "&nbsp;");
		//s = s.replaceAll("<", "&lt;");
		//s = s.replaceAll(">", "&gt;");
		//s = s.replaceAll("\n", "<br>\n");
		//s = s.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		//s = s.replaceAll("	", "&nbsp;&nbsp;&nbsp;&nbsp;");	//replace tab with 4 blanks
		
		//replace space at start of line to &nbsp;
		StringBuffer buf = new StringBuffer();
		boolean startOfLine = false;
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c == '\n'){
				startOfLine = true;
				buf.append("<br>\n");
			}else if(c == ' '){
				if(startOfLine)
					buf.append("&nbsp;");
				else
					buf.append(' ');
			}else if(c == '\t' || c == '	'){
				buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}else if(c == '\r'){
				//skip
			}else{
				startOfLine = false;
				if(convertSpecialChars){
					if(c == '<')
						buf.append("&lt;");
					else if(c == '>')
						buf.append("&gt;");
					else if(c == '&')
						buf.append("&amp;");
					else if(c == '\"')
						buf.append("&quot;");
					else
						buf.append(c);
				}else
					buf.append(c);
			}
		}
		return buf.toString();
	}
	
	public static void main(String[] args){
		String s = "line1\n line2\n   line3";
		s = convertStringToHTMLText(s, false);
		System.out.println(s);
	}
}
