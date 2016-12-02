package fruitwar.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Add color (HTML format) to the given java source code
 * @author wangnan
 *
 */
public class SourceCodeRenderer {

	public static final String COLOR_COMMENT = "3F7F5F";
	private static final String STR_FONT_COLOR = "<FONT color='" + COLOR_COMMENT + "'>";
	
	public static String render(String source) {
		StringBuffer buf = new StringBuffer(source.length() + source.length() / 5);
		StringReader rdr = new StringReader(source);
		BufferedReader br = new BufferedReader(rdr);
		boolean isInComments = false;	//whether we're between "/*" and "*/" 
		while(true){
			String s = null;
			try {
				s = br.readLine();
			} catch (IOException e) {
				break;
			}
			//if we've done
			if(s == null)
				break;
			
			int i = -1;
			
			//if we're between "/*" and "*/"
			if(isInComments){
				i = s.indexOf("*/");
				if(i >= 0){
					s = insertString(s, i + 2, "</FONT>");
					isInComments = false;
				}
			}else{
				//normal lines.
				
				//mark "//" first
				i = s.indexOf("//");
				if(i >= 0){
					String tmp = insertString(s, i, STR_FONT_COLOR);
					s = tmp + "</FONT>";
				}else{
					//("/*" can be disabled by "//")
					i = s.indexOf("/*");
					if(i >= 0){
						isInComments = true;
						s = insertString(s, i, STR_FONT_COLOR);
					}
				}
			}
			
			//append to buffer
			buf.append(s);
			buf.append("\n");
		}
		
		return buf.toString();
	}

	/**
	 * Insert string "insert" in "src" at position "n".
	 * @param src
	 * @param n
	 * @param insert
	 * @return
	 */
	private static String insertString(String src, int n, String insert){
		return src.substring(0, n) + insert + src.substring(n);
	}
}
