package RPG.Data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Attempt to read files in datafiles.xml
 */
public abstract class RPGDataLoader implements RPGData {
    protected Document dataDocument;

    private static final String dataFolder = "data";

    protected RPGDataLoader() {
        dataDocument = loadDocument();
    }

    private Document loadDocument() {
        String fileToRead = dataFolder + "/" + getDataFileType().getFileName();
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new File(fileToRead));
            doc.getDocumentElement().normalize();
        } catch(SAXParseException err) {
            System.out.println("Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(err.getMessage());
        } catch(SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return doc;
    }

    protected String getAttributeValue(Node node, String attribute) {
        return node.getAttributes().getNamedItem(attribute).getNodeValue();
    }

    protected String getAttributeValue(Node node, String element, String attribute) {
        NodeList elementList = ((DeferredElementImpl) node).getElementsByTagName(element);
        assert (elementList.getLength() == 1);
        return elementList.item(0).getAttributes().getNamedItem(attribute).getNodeValue();
    }

    protected abstract DataFileType getDataFileType();
}
