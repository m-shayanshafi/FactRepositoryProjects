package via.aventurica.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BasicXMLIO {
	private final static long serialVersionUID = 1L;
	
	private Writer output; 
	private String prepender = ""; 
	private Stack<String> openTags = new Stack<String>();
	
	public final void readFrom(File f) throws IOException, SAXException, ParserConfigurationException { 
     	
		SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        saxParser.parse(f, new DefaultHandler() {	
     	
        	@Override
        	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        		foundElement(name, attributes); 
        	}
        });
  
	}
	
	void foundElement(String name, Attributes attributes) { 
		
	}
	
	public final void writeTo(File f) throws IOException { 
		if(!f.exists())
			if(!f.createNewFile())
				throw new IOException("Konnte Datei nicht erstellen: "+f.getAbsolutePath()); 
		output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
	}
	
	public final void openTag(final Object tagname, final boolean terminate, final Object...paras) throws IOException {
		if(!terminate)
			openTags.push(tagname.toString());
		
		output.write(prepender);
		output.write("<");  
		output.write(tagname.toString());  
		if(paras.length > 0) { 
			output.write(" "); 
			for(int i=0; i< paras.length; i+=2) { 
				output.write(paras[i].toString()); 
				output.write("=\""); 
				output.write(prepareString(paras[i+1].toString())); 
				output.write("\" "); 
			}
		}
		if(terminate)
			output.write("/"); 
		output.write(">\n");  
		if(!terminate)
			prepender+="\t"; 
	}
	
	public final void closeTag() throws IOException { 
		prepender = prepender.substring(1);
		output.write(prepender); 
		output.write("</"); 
		output.write(openTags.pop()); 
		output.write(">\n"); 
		 
	}
	
	public final void writingDone() { 
		try { 
			output.close(); 
		} catch(Exception e) { }
	}
	
	public final static String prepareString(final String data) { 
		return data.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"); 
	}
	
}
