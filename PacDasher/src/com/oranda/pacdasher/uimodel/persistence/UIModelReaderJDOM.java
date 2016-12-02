/**
 *  PacDasher application. For explanation of this class, see below. 
 *  Copyright (c) 2003-2005 James McCabe. Email: code@oranda.com 
 *  http://www.oranda.com/java/pacdasher/
 * 
 *  PacDasher is free software under the Aladdin license (see license  
 *  directory). You are free to play, copy, distribute, and modify it
 *  except for commercial purposes. You may not sell this code, or
 *  compiled versions of it, or anything which incorporates either of these.
 * 
 */
package com.oranda.pacdasher.uimodel.persistence;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import com.oranda.pacdasher.GlobVars;
import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.uimodel.Const;
import com.oranda.pacdasher.uimodel.GameInfo;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

import com.oranda.util.ResourceUtils;
import com.oranda.util.Str;


/**
 * Code to read a maze from persistent storage using JDOM and create a
 * MazeRaw object. This class does not do any validation except validating
 * against the XML schema; other classes may complain about deeper 
 * validation/consistency issues.
 */
public class UIModelReaderJDOM implements IUIModelReader
{
	private final static String pathXMLSchema 
    		= UIModelConsts.DIR_XML + UIModelConsts.FILENAME_XML_SCHEMA;
	
    // key names recognized by the Xerces parser
	private static final String JAXP_SCHEMA_SOURCE 
    		= "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String XML_SCHEMA 
    		= "http://apache.org/xml/features/validation/schema";

    private String pathXML;
    private Element elRoot;
    
    public UIModelReaderJDOM()
    {
        String pathXML = "";
        if (GlobVars.relPathXML != null)
        {
            pathXML = UIModelConsts.DIR_XML + GlobVars.relPathXML;
            Const.logger.fine("not null: Set path XML to " + pathXML);
        }
        else
        {
            pathXML = UIModelConsts.DIR_XML + UIModelConsts.FILENAME_XML;
            Const.logger.fine("null: Set path XML to " + pathXML);
        }
        this.pathXML = pathXML;
        try
        {
            readRootElement();
        }
        catch (ParseException pe)
        {
            Const.logger.severe("Could not read XML doc" 
                    + Str.getStackTraceAsStr(pe));
            PacDasherMain.exit();
        }
    }
    
    private void readRootElement() throws ParseException
    {    
        Document doc = null;
        try 
        { 
            doc = findXMLDocument();
        }
        catch (JDOMException je) 
        { 
            throw new ParseException("Is the XML in " + this.pathXML 
                    + " well formed?", je);
        }  
        catch (IOException ioe) 
        { 
            throw new ParseException("Problem reading file " + this.pathXML, ioe);
        }             
        
        Element root = doc.getRootElement();
        String rootNameExpected = "game";
        if (!rootNameExpected.equals(root.getName())) {
            throw new ParseException("Could not find root element "
                    + rootNameExpected + " at " + pathXML);
        }            
        this.elRoot = root;
    }
    
    private Document findXMLDocument() throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder(true);
        // set validation to XML Schema
        builder.setFeature(XML_SCHEMA, true);
        
        /*
         * First attempt to get the XSD (schema) from a local file,
         * otherwise look in the JAR for the same path.
         */
        File fXSD = new File(pathXMLSchema);
        if (fXSD.exists())
        {
            builder.setProperty(JAXP_SCHEMA_SOURCE, fXSD);
        }
        else
        {
            ResourceUtils r = new ResourceUtils();
            InputStream is = r.getFileInputStreamFromJar(pathXMLSchema);
            builder.setProperty(JAXP_SCHEMA_SOURCE, is);
        }
        
        /*
         * First attempt to get the XML from a local file,
         * otherwise look in the JAR for the same path.
         */
        File fXML = new File(pathXML);
        if (fXML.exists())
        {
            return builder.build(this.pathXML);
        }
        else
        {
            ResourceUtils r = new ResourceUtils();
            InputStream is = r.getFileInputStreamFromJar(this.pathXML);
            if (is == null)
            {
                Const.logger.severe("Could not read XML file " + this.pathXML
                        + " so reverting to default");
                this.pathXML = UIModelConsts.DIR_XML 
                        + UIModelConsts.FILENAME_XML;

                is = r.getFileInputStreamFromJar(this.pathXML);
            }
            return builder.build(is);
        }
    }
    
    /********************************************************************
     * Core functionality
     ********************************************************************/
    
    public GameInfo parseForGameInfo() throws ParseException
    {        
        GameInfo gameInfo = new GameInfo();    
            
        String framesPerSecond = this.elRoot.getAttributeValue("framesPerSecond");
        String framesLevelSpeedup = this.elRoot.getAttributeValue(
        		"framesLevelSpeedup");
        String numLevels = this.elRoot.getAttributeValue("numLevels");
        
        gameInfo.setFramesLevelSpeedup(
                ParseUtils.parseStringToInt(framesLevelSpeedup));
        gameInfo.setInitFramesPerSecond(
                ParseUtils.parseStringToInt(framesPerSecond));
        gameInfo.setNumLevels(ParseUtils.parseStringToInt(numLevels));
    
    	return gameInfo;
	}

    public MazeRaw parseForMaze(int level) throws ParseException
    {
        return findAndParseMaze(this.elRoot, level);
    }
    
    private MazeRaw findAndParseMaze(Element elRoot, int level)
    		throws ParseException
    {
        List<Element> childrenMazes = elRoot.getChildren();
        Const.logger.fine("num of children: " + childrenMazes.size());
        Element elFoundMaze = null; // to find
        for (Element elMaze : childrenMazes) 
        {
            if (mazeAppliesToLevel(elMaze, level)) 
            {
                elFoundMaze = elMaze;
                break;
            }
        }
        if (elFoundMaze == null)
        {
            throw new ParseException("Could not find maze for level " 
                    + level + " in " + pathXML);
        }
        MazeRaw mazeRaw = parseMaze(elFoundMaze);
        return mazeRaw;
    }
    
    private MazeRaw parseMaze(Element elMaze) throws ParseException
    {
        MazeRaw mazeRaw = new MazeRaw();
        String number = elMaze.getChildText("number");
        mazeRaw.setNumber(number);
        String levels = elMaze.getChildText("level");
        mazeRaw.setLevels(levels);
        String wallColor = elMaze.getChildText("wallColor");
        mazeRaw.setWallColor(wallColor); 
        String gateColor = elMaze.getChildText("gateColor");
        mazeRaw.setGateColor(gateColor);
        String defaultBackgroundObject = elMaze.getChild(
                "defaultBackgroundObject").getAttributeValue("class");
        mazeRaw.setDefaultBackgroundObject(defaultBackgroundObject);
        String defaultForegroundObject = elMaze.getChild(
                "defaultForegroundObject").getAttributeValue("class");
        mazeRaw.setDefaultForegroundObject(defaultForegroundObject);
        String xycClearAreaBounds = elMaze.getChildText("clearAreaBounds");
        mazeRaw.setXycClearAreaBounds(xycClearAreaBounds);
        String xycClearAreaExtra = elMaze.getChildText("clearAreaExtra");
        mazeRaw.setXycClearAreaExtra(xycClearAreaExtra);
        String xycPacDasher = elMaze.getChildText("pacDasher");
        mazeRaw.setXycPacDasher(xycPacDasher);

        Element elMazeObjects = elMaze.getChild("mazeObjects");
        parseMazeObjects(mazeRaw, elMazeObjects);
        return mazeRaw;
    }
    
    /**
     * @params mazeRaw is the output as well as an input
     */
    private void parseMazeObjects(MazeRaw mazeRaw, Element elMazeObjects)
    		throws ParseException
    {
        List<Element> visualObjects 
        		= elMazeObjects.getChildren("visualObject");
        for (Element visualObject : visualObjects)
        {
            String voClass = visualObject.getAttributeValue("class").trim();
            String xycCoords = visualObject.getText();
            String isForeground = visualObject.getAttributeValue("layer").
            		trim();
            mazeRaw.addVisualObjectCollection(voClass, isForeground, 
                    xycCoords);
        }

        String xycFruit = elMazeObjects.getChildText("fruits");
        if (xycFruit != null)
        {
            mazeRaw.setXycFruit(xycFruit);
            String classFruits = elMazeObjects.getChild(
            		"fruits").getAttributeValue("classes");
            mazeRaw.setClassFruits(classFruits);        
        }
        else
        {
            mazeRaw.setClassFruits(null);
        }
    }
    
    /********************************************************************
     * Helper methods
     * ******************************************************************/
    
    private boolean mazeAppliesToLevel(Element elMaze, int level) 
    		throws ParseException
    {
        Element elLevel = elMaze.getChild("level");
        String levelsStr = elLevel.getText();
        Set levels = ParseUtils.parseStringToInts(levelsStr);
        Integer levelObj = new Integer(level);
        if (levels.contains(levelObj))
        {
            return true;
        }
        return false;
    }
    
    private static void listChildren(Element current, int depth) 
    {
        printSpaces(depth);
        System.out.println(current.getName());
        List<Attribute> attributes = current.getAttributes();
        for (Attribute attribute : attributes)
        {
            System.out.println(attribute.getName() + " : " + attribute.getValue());
        }
        List children = current.getChildren(); // the key JDOM-using line
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            listChildren(child, depth+1);
        }
         
    }
       
    private static void printSpaces(int n) {         
        for (int i = 0; i < n; i++) {
            System.out.print("    "); 
        }         
    }

    /********************************************************************
     * Standard and test methods
     * ******************************************************************/
    public static void main(String [] args) 
    {
        SAXBuilder builder = new SAXBuilder(true);
        
        // set validation to XML Schema
        builder.setFeature("http://apache.org/xml/features/validation/schema",
                true);
        
        String fileName = "pacdasherVariety.xml";
        
        try 
        {
            Document doc = builder.build(fileName);
            Element root = doc.getRootElement();
            listChildren(root, 0);      
        }
        // may indicate a well-formedness error
        catch (JDOMException e) 
        { 
            System.out.println(fileName + " may not be well-formed.");
            System.out.println(e.getMessage());
        }  
        catch (IOException e) 
        { 
            System.out.println(e);
        }  
    }
}
