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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import com.oranda.pacdasher.uimodel.Const;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.pacdasher.uimodel.util.XYCManager;
import com.oranda.pacdasher.uimodel.util.XYCoarse;

/**
 * Class description goes here
 */
public class MazeRaw
{    
    private int number; // an ID for the Maze
    private Set<Integer> levels; // levels to which the Maze applies
    private Color wallColor; 
    private Color gateColor;
    private Class defaultBackgroundObject;
    private Class defaultForegroundObject;
    private Set<VisualObjectCollection> visualObjectCollections;
    
    private XYCoarse xycPacDasher;
    private List<XYCoarse> xycClearAreaBounds;
    private Set<XYCoarse> xycClearAreaExtra;
    private XYCoarse xycFruit;
    private List<Class> classFruits;

    public MazeRaw() 
    {
        visualObjectCollections = new HashSet<VisualObjectCollection>();
    }
    
    /**
     * @return Returns the classes of fruits.
     */
    public List<Class> getClassFruits()
    {
        // Const.logger.info("Class fruits: " + this.classFruits);
        return classFruits;
    }
    /**
     * @param classfruits classNames of fruits
     */
    public void setClassFruits(String classFruits)
    		throws ParseException
    {
        if (classFruits != null)
        {
            this.classFruits = ParseUtils.parseStringToClasses(classFruits);
        }
        else
        {
            // Const.logger.info("Class fruits: " + this.classFruits);
            this.classFruits = new ArrayList<Class>();
        }
    }
    
    /**
     * @return Returns the defaultBackgroundObject.
     */
    public Class getDefaultBackgroundObject()
    {
        return defaultBackgroundObject;
    }
    /**
     * @param defaultBackgroundObject The defaultBackgroundObject to set.
     */
    public void setDefaultBackgroundObject(String defaultBackgroundObject)
			throws ParseException
    {
        this.defaultBackgroundObject 
        		= ParseUtils.parseStringToClass(defaultBackgroundObject);
    }
    /**
     * @return Returns the defaultForegroundObject.
     */
    public Class getDefaultForegroundObject()
    {
        return defaultForegroundObject;
    }
    /**
     * @param defaultForegroundObject The defaultForegroundObject to set.
     */
    public void setDefaultForegroundObject(String defaultForegroundObject)
    		throws ParseException
    {
        this.defaultForegroundObject 
        		= ParseUtils.parseStringToClass(defaultForegroundObject);
    }
    /**
     * @return Returns the gateColor.
     */
    public Color getGateColor()
    {
        return gateColor;
    }
    /**
     * @param gateColor The gateColor to set.
     */
    public void setGateColor(String gateColor) throws ParseException
    {
        this.gateColor = ParseUtils.parseStringToColor(gateColor);
    }
    /**
     * @return Returns the level.
     */
    public Set<Integer> getLevels()
    {
        return levels;
    }
    /**
     * @param level The level to set.
     */
    public void setLevels(String levels) throws ParseException
    {
        this.levels = ParseUtils.parseStringToInts(levels);
    }
    /**
     * @return Returns the number.
     */
    public int getNumber()
    {
        return number;
    }
    /**
     * @param number The number to set.
     */
    public void setNumber(String number) throws ParseException
    {
        this.number = ParseUtils.parseStringToInt(number);
    }
    /**
     * @return Returns the wallColor.
     */
    public Color getWallColor()
    {
        return wallColor;
    }
    /**
     * @param wallColor The wallColor to set.
     */
    public void setWallColor(String wallColor) throws ParseException
    {
        this.wallColor = ParseUtils.parseStringToColor(wallColor);
    }
 
    public Set<VisualObjectCollection> getVisualObjectCollections()
    {
        return this.visualObjectCollections;
    }
    
    public void addVisualObjectCollection(String voClassStr, 
            String isForegroundStr, String xycCoordsStr) throws ParseException
    {
        Class voClass = ParseUtils.parseStringToClass(voClassStr);
        
        boolean isForeground = false;
        if ("foreground".equals(isForegroundStr.trim().toLowerCase())) 
        {
            isForeground = true;
        }
        Const.logger.fine( voClassStr + "; isForeground: " + isForegroundStr 
                + "," + isForeground);
        Set<XYCoarse> xycCoords = ParseUtils.parseStringToXYCs(xycCoordsStr);
        VisualObjectCollection voCollection = new VisualObjectCollection(
                voClass, isForeground, xycCoords);
        visualObjectCollections.add(voCollection);
    }

    /**
     * @return Returns the xycPacDasher.
     */
    public XYCoarse getXycPacDasher()
    {
        return xycPacDasher;
    }
    /**
     * @param xycPacDasher The xycPacDasher to set.
     */
    public void setXycPacDasher(String xycPacDasher) throws ParseException
    {
        this.xycPacDasher = ParseUtils.parseStringToXYC(xycPacDasher);
    }
    
    /**
     * @return Returns the xycClearAreaBounds.
     */
    public List<XYCoarse> getXycClearAreaBounds()
    {
        return xycClearAreaBounds;
    }
    /**
     * @param xycClearAreaBounds The xycClearAreaBounds to set.
     */
    public void setXycClearAreaBounds(String xycClearAreaBounds)
    		throws ParseException
    {
        if (xycClearAreaBounds == null) // optional element 
        {
            this.xycClearAreaBounds = new ArrayList<XYCoarse>();
        }
        else
        {
            this.xycClearAreaBounds = ParseUtils.parseStringToXYCsOrdered(
                    xycClearAreaBounds);
        }                
    }
    /**
     * @return Returns the xycClearAreaExtra.
     */
    public Set<XYCoarse> getXycClearAreaExtra()
    {
        return xycClearAreaExtra;
    }
    /**
     * @param xycClearAreaExtra The xycClearAreaExtra to set.
     */
    public void setXycClearAreaExtra(String xycClearAreaExtra)
			throws ParseException
    {
        if (xycClearAreaExtra == null)
        {
            this.xycClearAreaExtra = new HashSet<XYCoarse>();
        }
        else
        {
            this.xycClearAreaExtra = ParseUtils.parseStringToXYCs(
                    xycClearAreaExtra);
        }
    }
    
    /**
     * @return Returns the xycFruit.
     */
    public XYCoarse getXycFruit()
    {
        return xycFruit;
    }
    /**
     * @param xycFruit The xycFruit to set.
     */
    public void setXycFruit(String xycFruit) throws ParseException
    {
        if (xycFruit != null)
        {
            this.xycFruit = ParseUtils.parseStringToXYC(xycFruit);
        }
    }


    
    /********************************************************************
     * Standard methods
     *******************************************************************/
    
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nnumber: " + this.number);
        sb.append("\nlevels: " + this.levels);
        sb.append("\nwallColor: " + this.wallColor);
        sb.append("\ngateColor: " + this.gateColor);
        sb.append("\ndefaultBackgroundObject: " 
                + this.defaultBackgroundObject);
        sb.append("\ndefaultForegroundObject: " 
                + this.defaultForegroundObject);
        sb.append("\nxycClearAreaBounds: " + this.xycClearAreaBounds); 
        sb.append("\nxycClearAreaExtra: " + this.xycClearAreaExtra);
        for (VisualObjectCollection voCollection : 
            this.visualObjectCollections)
        {
            sb.append("\n" + voCollection);
        }
        sb.append("\nxycFruit: " + this.xycFruit);
        sb.append("\nclassFruits: " + this.classFruits);
        return sb.toString();                                                                                                                                              
    }
    
    public static void main(String[] args)
    {
        UIModelReaderJDOM reader = new UIModelReaderJDOM();
        try 
        {
            MazeRaw mazeRaw = reader.parseForMaze(2);
            Const.logger.info(mazeRaw.toString());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
    
    /********************************************************************
     * Supporting classes
     *******************************************************************/
    public class VisualObjectCollection
    {
        public Class voClass;
        public boolean isForeground;       
        public Set<XYCoarse> xycs;
        
        private VisualObjectCollection() {};
        public VisualObjectCollection(Class voClass, boolean isForeground, 
                Set<XYCoarse> xycs) 
        {           
            this.voClass = voClass;
            this.isForeground = isForeground;
            this.xycs = xycs;
        }
        
        public String toString()
        {
            return new StringBuilder().append(this.voClass).append(" ").
            		append(this.isForeground).append(" ").append(this.xycs).
            		toString();
        }
    }

}