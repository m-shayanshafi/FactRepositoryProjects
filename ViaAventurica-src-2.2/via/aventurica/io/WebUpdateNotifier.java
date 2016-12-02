package via.aventurica.io;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import via.aventurica.ConfigManager;
import via.aventurica.ViaAventurica;
import via.aventurica.view.newVersionInfo.NewVersionInformationDialog;

/**
 * @author Matthias
 * Öffnet die Verbindung zu dem RSS2 Release Newsfeed 
 * und liest alle neueren Releases aus. Das oberste Release wird als 
 * das neueste angenommen und angezeigt. 
 */
public class WebUpdateNotifier {
		
	public final static SimpleDateFormat RSS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // RSS Format: 2008-05-21 08:59:04
	
	public class NewVersionInfo {
		public String title, description; 
		public GregorianCalendar date;
		public final String getTitle() {
			return title;
		}
		public final void setTitle(String title) {
			this.title = title;
		}
		public final String getDescription() {
			return description;
		}
		public final void setDescription(String description) {
			this.description = description;
		}
		public final GregorianCalendar getDate() {
			return date;
		}
		public final void setDate(GregorianCalendar date) {
			this.date = date;
		} 
		
		
	}
	
	private NewVersionInfo newVersion; 
	private StringBuffer titleBuff = new StringBuffer(), descriptionBuff = new StringBuffer(), dateBuff = new StringBuffer(); 
	
	private GregorianCalendar lastUpdateCheckDate; 
	
	private WebUpdateNotifier() throws IOException, ParserConfigurationException, SAXException {
		lastUpdateCheckDate = ConfigManager.getInstance().getLastUpdateCheckDate(); 
		parseInfos(); 		
		ConfigManager.getInstance().setLastUpdateCheckDate(new GregorianCalendar()); 
	}
	
	private void parseInfos() throws ParserConfigurationException, SAXException, IOException { 
		SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        
        saxParser.parse(ViaAventurica.UPDATE_URL, new DefaultHandler() {
        	
        	private int updateCount = 0;
        	private String currentTag;  
        	
        	@Override
        	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        		
        		if(name.equals("item")) { 
        			updateCount++; 
        			newVersion = new NewVersionInfo(); 
        		}
        		currentTag = name; 
        		
 
        	}
        	
        	@Override
        	public void characters(char[] ch, int start, int length) throws SAXException {
        		if(updateCount==1) { 
        			String chars = new String(ch, start, length);
        			if(currentTag.equals("title"))
        				titleBuff.append(chars);  
        			else if(currentTag.equals("description"))
        				descriptionBuff.append(chars); 
        			else if(currentTag.equals("pubDate")) {
        				dateBuff.append(chars); 
        			}
        		}

        	}
        	
        	@Override
        	public void endDocument() throws SAXException {
        		if(newVersion!=null) { 
        			newVersion.setTitle(titleBuff.toString()); 
        			newVersion.setDescription(descriptionBuff.toString());
        			

        			
        			GregorianCalendar cal = new GregorianCalendar(); 
        			try {
						cal.setTime(RSS_DATE_FORMAT.parse(dateBuff.toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					} 
        			newVersion.setDate(cal); 
        		
        			System.out.println("gefundene Version: "+newVersion+ "   "+lastUpdateCheckDate);
        			
        			if(lastUpdateCheckDate!= null && cal.compareTo(lastUpdateCheckDate) < 0)
        				newVersion = null; 
        			
        			
        		}
        		
        		
        	}
   
      	
        });
	}
	
	public static void runUpdateCheck() { 
		new Thread(new Runnable() { 
			public void run() {
				try {
					System.out.println("Suche nach neuer Version im Internet: "+ViaAventurica.UPDATE_URL);
					final WebUpdateNotifier notifier = new WebUpdateNotifier();
					ConfigManager.getInstance().setLastUpdateCheckDate(new GregorianCalendar()); 
					if(notifier.newVersion!=null)
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new NewVersionInformationDialog(notifier.newVersion); 				 
							}}
						); 
				} catch(IOException ex) { 
					JOptionPane.showMessageDialog(ViaAventurica.APPLICATION 
							, "Beim suchen nach Programm Updates für ViaAventurica\nkonnte der Server nicht erreicht werden.\n\nÜberprüfe deine Firewall Einstellungen oder besuche die ViaAventurica Website", 
							"Fehler bei Suche nach neuer Version", 
							JOptionPane.WARNING_MESSAGE);
					ex.printStackTrace(); 
				} catch (Exception ex) {
					ex.printStackTrace(); 
				}
				
			}
		}).start(); 
	}
	
	
	public static void main(String[] args) {
		runUpdateCheck(); 
	}
	
}
