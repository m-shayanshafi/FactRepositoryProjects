/*
 *  :tabSize=4:indentSize=2:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2006 Free Tarot Foundation.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.lazy8.nu/tarot or email tarot@lazy8.nu
 */
 
package jtarot;

 
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

// Standard XML API
// DOM
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
// SAX.
import org.xml.sax.*;
import org.xml.sax.helpers.*;

//JAXP 1.1
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
// Transform
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

public class HelpTranslationPanel extends JPanel {
	private JEditorPane helpEditor;
	private TransCell[] editCells;
	public JTextArea[] titles;
	public JTextArea[] texts;


	public HelpTranslationPanel( String mainBorderText, String[] origTitles, String[] origTexts,
			String[] transTitles, String[] transTexts ) {
		super( true );

		setBorder( BorderFactory.createTitledBorder( mainBorderText ) );
		setLayout( new BorderLayout() );

		//create the translation panel
		JPanel dataEntryPane = new JPanel();
		JScrollPane scroller1 = new JScrollPane( dataEntryPane );
		scroller1.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

		// create the hypertext help panel
		ClassLoader loader = this.getClass().getClassLoader();
		try {
			helpEditor = new JEditorPane( loader.getResource( "res/htmlhelp.html" ).toString() );
		} catch ( Exception eee ) {
			System.out.println( "Probably cant find the resource res/htmlhelp.html, error=  " + eee.getMessage() );
			return;
		}
		helpEditor.setEditable( false );
		JScrollPane scroller2 = new JScrollPane( helpEditor );
		scroller2.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		scroller2.setMinimumSize( new Dimension( 200, 200 ) );
		scroller2.setMaximumSize( new Dimension( 200, 200 ) );
//	setMinimumSize(new Dimension(800,800));
//	setMaximumSize(new Dimension(800,800));
		setPreferredSize( new Dimension( 1000, 800 ) );
		JSplitPane splitCenter = new JSplitPane( JSplitPane.VERTICAL_SPLIT, scroller2, scroller1 );
		add( splitCenter );

		editCells = new TransCell[origTitles.length];
		texts = new JTextArea[origTitles.length];
		titles = new JTextArea[origTitles.length];
		dataEntryPane.setLayout( new GridLayout( origTitles.length, 1 ) );
		for ( int i = 0; i < origTitles.length; i++ ) {
			texts[i] = new JTextArea( transTexts[i] );
			texts[i].setWrapStyleWord( true );
			texts[i].setLineWrap( true );
			titles[i] = new JTextArea( transTitles[i] );
			titles[i].setWrapStyleWord( true );
			titles[i].setLineWrap( true );
			editCells[i] = new TransCell( origTitles[i], origTexts[i], titles[i], texts[i] );
			dataEntryPane.add( editCells[i] );
		}
	}


	class TransCell extends JPanel implements DocumentListener {
		private JEditorPane resultEditor;
		private JTextArea transTitle, transText;


		TransCell( String origTitle, String origText, JTextArea transTitle,
				JTextArea transText ) {
			this.transTitle = transTitle;
			this.transText = transText;
			JPanel originalPanel = new JPanel();
			JPanel title1Panel = new JPanel();
			resultEditor = new JEditorPane( "text/html", "" );
			JPanel translatedPanel = new JPanel();
			originalPanel.setBorder( BorderFactory.createTitledBorder( "OriginalTexts" ) );
			translatedPanel.setBorder( BorderFactory.createTitledBorder( "Translations" ) );

			// create the hypertext result panel
			resultEditor.setEditable( false );
			//resultEditor.setWrapStyleWord(true);
			//resultEditor.setFont(new Font("monospaced", Font.PLAIN, 12));
			JScrollPane scroller = new JScrollPane( resultEditor );
			scroller.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			setLayout( new GridLayout( 1, 3 ) );
			add( scroller );
			scroller.setMinimumSize( new Dimension( 300, 300 ) );
			scroller.setMaximumSize( new Dimension( 300, 300 ) );
			scroller.setPreferredSize( new Dimension( 300, 300 ) );
			originalPanel.setMinimumSize( new Dimension( 300, 300 ) );
			translatedPanel.setMinimumSize( new Dimension( 300, 300 ) );

			originalPanel.setMaximumSize( new Dimension( 300, 300 ) );
			translatedPanel.setMaximumSize( new Dimension( 300, 300 ) );

			originalPanel.setPreferredSize( new Dimension( 300, 300 ) );
			translatedPanel.setPreferredSize( new Dimension( 300, 300 ) );

			transTitle.getDocument().addDocumentListener( this );
			transText.getDocument().addDocumentListener( this );
			//create the main original/translation interface
			title1Panel.setBorder( BorderFactory.createTitledBorder( "Title" ) );
			title1Panel.setLayout( new GridLayout( 1, 1 ) );
			JTextArea jta = new JTextArea( origTitle );
			title1Panel.add( jta );
			jta.setWrapStyleWord( true );
			jta.setLineWrap( true );
			jta.setEditable( false );
			JPanel text1Panel = new JPanel();
			text1Panel.setLayout( new GridLayout( 1, 1 ) );
			text1Panel.setBorder( BorderFactory.createTitledBorder( "Texts" ) );
			JTextArea jta2 = new JTextArea( origText );
			jta2.setWrapStyleWord( true );
			jta2.setLineWrap( true );
			jta2.setEditable( false );
			text1Panel.add( jta2 );
			JScrollPane scroller1 = new JScrollPane( text1Panel );
			scroller1.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			originalPanel.setLayout( new BorderLayout() );
			originalPanel.add( "North", title1Panel );
			originalPanel.add( "Center", scroller1 );

			JPanel title2Panel = new JPanel();
			title2Panel.setLayout( new GridLayout( 1, 1 ) );
			title2Panel.setBorder( BorderFactory.createTitledBorder( "Title" ) );
			title2Panel.add( transTitle );
			JPanel text2Panel = new JPanel();
			text2Panel.setLayout( new GridLayout( 1, 1 ) );
			text2Panel.setBorder( BorderFactory.createTitledBorder( "Texts" ) );
			text2Panel.add( transText );
			JScrollPane scroller2 = new JScrollPane( text2Panel );
			scroller2.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			translatedPanel.setLayout( new BorderLayout() );
			translatedPanel.add( "North", title2Panel );
			translatedPanel.add( "Center", scroller2 );

			changedUpdate( null );
			add( originalPanel );
			add( translatedPanel );
		}


		public void insertUpdate( DocumentEvent e ) {
			changedUpdate( e );
		}


		public void removeUpdate( DocumentEvent e ) {
			changedUpdate( e );
		}


		public void changedUpdate( DocumentEvent e ) {
			resultEditor.setText(
					"<h2>" + transTitle.getText() + "</h2>" + transText.getText()
					 );
		}
	}


	public static void SaveJAXPTranlsationsWithDOM( File f, String language, String[] transTitles, String[] transTexts, Boolean[] isSubTopic ) {
		PrintWriter out = null;
		try {
			out = new PrintWriter( f, "UTF-8" );
		} catch ( Exception e ) {
			System.out.println( "error=" + e.getMessage() );
			e.printStackTrace();
		}
		// Create XML DOM document (Memory consuming).
		Document xmldoc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			Element e = null;
			Element ee = null;
			Element eee = null;
			Node n = null;
			Node nn = null;
			// Document.
			xmldoc = impl.createDocument( null, "jTarotHelp", null );
			// Root element.
			Element root = xmldoc.getDocumentElement();
			root.setAttributeNS( null, "version", "1.0" );
			root.setAttributeNS( null, "language", language );

			//Print out translations to the cards
			for ( int i = 0; i < transTitles.length; i++ ) {
              
				e = xmldoc.createElementNS( null, "Topic" );
				e.setAttributeNS( null, "index", "" + i );
				e.setAttributeNS( null, "issubtopic", isSubTopic[i].toString() );
                
				ee = xmldoc.createElementNS( null, "title" );
				nn = xmldoc.createTextNode(  transTitles[i] );
				ee.appendChild( nn );
				e.appendChild( ee );
                
				n = xmldoc.createTextNode( transTexts[i] );
 				eee = xmldoc.createElementNS( null, "TopicText" );
                eee.appendChild( n );
				e.appendChild( eee );
				root.appendChild( e );
			}

		} catch ( FactoryConfigurationError e ) {
			System.out.println( "Could not locate a JAXP DocumentBuilderFactory class" );
		} catch ( ParserConfigurationException e ) {
			System.out.println( "Could not locate a JAXP DocumentBuilder class" );
		}

		// DOM Transform.
		try {
			DOMSource domSource = new DOMSource( xmldoc );
			StreamResult streamResult = new StreamResult( out );
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
			serializer.setOutputProperty( OutputKeys.MEDIA_TYPE, "UTF-8" );
//        serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"jtarot.dtd");
			serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
			serializer.transform( domSource, streamResult );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


	public static void OpenJAXPTranslationsWithDOM( File f, InputStream in, StringBuffer language, ArrayList titles, ArrayList texts, ArrayList subtopics ) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		//factory.setValidating(true);
		//factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			if ( f != null )
				document = builder.parse( f );
			else
				document = builder.parse( in );
			document.normalizeDocument();
		} catch ( SAXParseException spe ) {
			// Error generated by the parser
			System.out.println( "\n** Parsing error" + ", line " +
					spe.getLineNumber() + ", uri " + spe.getSystemId() );
			System.out.println( "   " + spe.getMessage() );

			// Use the contained exception, if any
			Exception x = spe;

			if ( spe.getException() != null )
				x = spe.getException();

			x.printStackTrace();
		} catch ( SAXException sxe ) {
			// Error generated during parsing)
			Exception x = sxe;

			if ( sxe.getException() != null )
				x = sxe.getException();

			x.printStackTrace();
		} catch ( ParserConfigurationException pce ) {
			// Parser with specified options can't be built
			pce.printStackTrace();
		} catch ( IOException ioe ) {
			// I/O error
			ioe.printStackTrace();
		}
		org.w3c.dom.Node root;
		org.w3c.dom.Node node;
		org.w3c.dom.Node cardnode;
		language.setLength( 0 );
		root = document.getFirstChild();
		if ( !root.getNodeName().equals( "jTarotHelp" ) )
			return;
		String nodename = "";
		int index = 0;
		language.append( root.getAttributes().getNamedItem( "language" ).getNodeValue() );
		for ( int i = 0; i < root.getChildNodes().getLength(); i++ ) {
			node = root.getChildNodes().item( i );
			nodename = node.getNodeName();
			if ( nodename.equals( "Topic" ) ) {
				index = Integer.parseInt( node.getAttributes().getNamedItem( "index" ).getNodeValue() );
                for ( int j = 0; j < node.getChildNodes().getLength();j++ ) {
                  	if ( node.getChildNodes().item( j ).getNodeName().equals( "title" ) ) 
                      titles.add( node.getChildNodes().item( j ).getTextContent());
                    else if (node.getChildNodes().item( j ).getNodeName().equals( "TopicText" ))
                      texts.add( node.getChildNodes().item( j ).getTextContent());
                }
				subtopics.add( new Boolean( node.getAttributes().getNamedItem( "issubtopic" ).getNodeValue() ) );
            }
		}
	}

}

