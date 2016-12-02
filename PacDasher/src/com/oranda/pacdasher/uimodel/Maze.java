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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.GlobVars;
import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.controller.AppEvents;
import com.oranda.pacdasher.controller.AppEventStateManager;
import com.oranda.pacdasher.controller.StateHolder;
import com.oranda.pacdasher.uimodel.persistence.MazeRaw;
import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.pacdasher.uimodel.ghosts.*;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.visualobjects.*;
import com.oranda.util.Str;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;
import java.lang.reflect.Constructor;

/**
 * Maze - main class under UIModel. Actively used for the animation.
 * Note: a Maze is a complex object with interrelated parts 
 * (unlike the MazeRaw object it may be derived from).
 */
public class Maze implements Cloneable
{                    
    /**
     * Pixel dimensions 
     */
    public static final int WIDTH 
        = (UIModelConsts.MAZE_WIDTH) * UIModelConsts.X_TILE_SIZE;         
    public static final int HEIGHT
        = UIModelConsts.MAZE_HEIGHT * UIModelConsts.Y_TILE_SIZE;
    public static final int DRAWABLE_WIDTH 
        = (UIModelConsts.MAZE_WIDTH - 2) * UIModelConsts.X_TILE_SIZE;         
    public static final int DRAWABLE_HEIGHT
        = UIModelConsts.MAZE_HEIGHT * UIModelConsts.Y_TILE_SIZE;

    /**
     * After a certain number of (dots + energizers) have been
     * consumed by PacDasher, fruit appears.
     */
    private static final int NUM_PELLETS_BEFORE_FRUIT = 57;

    /**
     * After more (dots + energizers) have been consumed by PacDasher, 
     * the fruit disappears.
     */
    private static final int NUM_PELLETS_FRUIT_DURATION = 40;
    
    /**
     * Fruit may reappear: if the number of (dots + energizers) is
     * NUM_PELLETS_BEFORE_FRUIT + NUM_PELLETS_FRUIT_PERIOD or
     * NUM_PELLETS_BEFORE_FRUIT + NUM_PELLETS_FRUIT_PERIOD * 2, etc.
     */
    private static final int NUM_PELLETS_FRUIT_PERIOD = 80;
    
    /** 
     * General data
     */   
    private int xCoarseBoundary;
    private int yCoarseBoundary;    
            
    private boolean isMazeFinished; // false
 
    private int numMazeAccesses; // 0
    
    /**
     * A list of fruits for this maze. 
     */
    private ArrayList<Fruit> fruits;
    
    private VisualObjectFactory voFactory = new VisualObjectFactory();
    
    private Fruit sampleFruit = new Cherry();
    
    private Set<Integer> levels; // a single Maze may be used at many levels
    
    /** 
     * Major components of the Maze. They are:
     * 
     * 1. The Static Background = XYCoarse -> walls and background tiles
     *        (placed on blocks of grid)
     * 2. The Dynamic Background = XYCoarse -> dots, energizers, etc.
     *        Includes non-mobile animated objects                  
     *        (placed on intersections of grid)
     * 3. The lists of sprites 
     *        They overlap with each other and the Dynamic Background  
     *        ghostList is a subset of mobileList is a subset of animatedList.     
     */
    private MazeBackgroundStatic staticBackground;
    private MazeBackgroundDynamic dynamicBackground;
    private MazeAnimatedObjects animatedObjects;
  
    public Maze()
    {
        xCoarseBoundary = UIModelConsts.MAZE_WIDTH - 1;
        yCoarseBoundary = UIModelConsts.MAZE_HEIGHT - 1;      
        staticBackground = new MazeBackgroundStatic(
                xCoarseBoundary, yCoarseBoundary);
        dynamicBackground = new MazeBackgroundDynamic(
                xCoarseBoundary, yCoarseBoundary);
        animatedObjects = new MazeAnimatedObjects(); 
        isMazeFinished = false;
        fruits = new ArrayList();
        levels = new HashSet();
    }

    public void resetNumMazeAccesses()
    {
        numMazeAccesses = 0;
    }
    
	public void setDefaultBackgroundClass(Class visualObjectClass)
	{
        staticBackground.setDefaultBackgroundClass(visualObjectClass);   
	}

	public void setDefaultForegroundClass(Class visualObjectClass)
	{
		dynamicBackground.setDefaultForegroundClass(visualObjectClass);
	}

    public Set<Integer> getLevels()
    {
        return this.levels;
    }
    
    /**
     * @param levelsStr example: 1 2 3 4 5 6 7 8
     */
    public void setLevels(Set levels)
    {
        Const.logger.fine("setLevels: " + levels);
        this.levels = levels;
    }
    
    public boolean isMazeForLevel(int level)
    {
        return levels.contains(level);
    }
    
    public void setWallColor(Color color)
    {
        staticBackground.setWallColor(color);
    }
    
	public void setGateColor(Color color)
	{
		dynamicBackground.setGateColor(color);
	}	
	
	public void setClearAreaBounds(int left, int top, int right, int bottom)
	{
		dynamicBackground.setClearAreaBounds(left, top, right, bottom);
	}	
	
	public void setClearAreaExtra(Set<XYCoarse> coords)
	{
        dynamicBackground.setClearAreaExtra(coords);
	}		
    
    public void setFruits(List<Class> fruitClasses, XYCoarse xycFruit)
    {
        for (Class fruitClass : fruitClasses)
        {
            Fruit fruit = (Fruit) voFactory.construct(fruitClass);
            fruit.initialize(xycFruit);           
            this.fruits.add(fruit);            
        }
        if (this.fruits.size() < 1)
        {
            Const.logger.info("Could not load fruit classes " + fruitClasses);
        }
    }
    
    public Fruit getCurrentFruit()
    {
        Fruit fruit = null;
        if (!this.fruits.isEmpty())
        {
            fruit = this.fruits.get(this.numMazeAccesses - 1);
        }
        Const.logger.fine("fruit: " + fruit);
        return fruit;
    }

    public GhostState getGeneralGhostState()
    {
        return animatedObjects.getGeneralGhostState();
    }
    
    public GhostCollection getGhostCollection()
    {
        return animatedObjects.getGhostCollection();
    }
    
    public List getAnimatedList()
    {
        return animatedObjects.getAnimatedList();
    }
    
    public void doInitializationAfterXML() 
    {
        animatedObjects.initializeLists(getPacDasher());         
        dynamicBackground.setBackgroundRemainder(staticBackground);
        staticBackground.createStaticBgGraphics();
        UIModel.getInstance().addListeners(this);
    }
        
    public void setPacDasherInitPos(int xCoarse, int yCoarse)
    {
    	XYCManager xycm = XYCManager.getInstance();
    	XYCoarse xyCoarse = xycm.createXYC(xCoarse, yCoarse);
    	getPacDasher().initialize(xyCoarse);
    }
    
    boolean isMazeFinished()
    {
        return isMazeFinished;
    }
    
    boolean checkMazeFinished()
    {
        boolean isMazeFinished = dynamicBackground.checkMazeFinished();
        return isMazeFinished;
    }
             
    void resetMobiles()
    {
        animatedObjects.resetMobiles();
    }
    
    /** 
     * Everything except animated/mobile objects.
     */
    public void renderObjectsAll(Graphics g)
    {
    	//Const.logger.fine("renderObjects");
        staticBackground.renderObjects(g);
        dynamicBackground.renderObjects(g);
    }

    public void renderAnimated(Graphics g)
    {
        animatedObjects.renderAnimated(g);
    }
    
    /*
     * @return a List of DirectionCode's
     */
    public List getNeighbors(XYCoarse xyCoarse, VisualObject visualObject)
    {        
        return staticBackground.getNeighbors(xyCoarse, visualObject);
    }
    
    public void renderDirtyAreas(Graphics g)
    {    
    	int numObjectsDrawn = 0;
        List<MobileVisualObject> mobileList = animatedObjects.getMobileList();
        for (MobileVisualObject mvo : mobileList)
        {
            DirtyArea dirtyArea = mvo.getDirtyArea();
            numObjectsDrawn += renderDirtyArea(g, dirtyArea);
        }        
        // Const.logger.fine("num dirty drawn: " + numObjectsDrawn);
    }

    public int renderDirtyArea(Graphics g, DirtyArea dirtyArea)
    {	
        if (dirtyArea == null || dirtyArea.getXYCLeftTop() == null)
        {
            return 0;
        }
        int numObjectsDrawn = 0;
        
        int xycLeft = dirtyArea.getXYCLeftTop().getX();
        int xycTop = dirtyArea.getXYCLeftTop().getY();
        int xycRight = dirtyArea.getXYCRightBottom().getX();
        int xycBottom = dirtyArea.getXYCRightBottom().getY();
    
        XYCManager xycm = XYCManager.getInstance();
               
        // render static background
        numObjectsDrawn += staticBackground.renderDirtyArea(g,
                xycLeft, xycTop, xycRight, xycBottom);
                
        // render dynamic background - dots, etc.
        numObjectsDrawn += dynamicBackground.renderDirtyArea(g, 
                xycLeft, xycTop, xycRight, xycBottom);
 
        return numObjectsDrawn;
    }    
                 
    public boolean isBlocked(MobileVisualObject mvo, int xCoarse, int yCoarse)
    {
        return staticBackground.isBlocked(xCoarse, yCoarse)
                || dynamicBackground.isBlocked(mvo, xCoarse, yCoarse);
    }
    

    private void setPosAndDirection(Ghost ghost, int[][] arrayInt2D)
    {
        Direction directionInit = new Direction(DirectionCode.LEFT);
        XYCoarse xyCoarseInit 
           = XYCManager.getInstance().createXYC(arrayInt2D[0][0], arrayInt2D[0][1]);
        ghost.setPosAndDirection(
            new PosAndDirection(ghost, xyCoarseInit, directionInit));
    }    
    
    public void move()
    {    
        //Const.logger.fine("this.numMazeAccesses " + this.numMazeAccesses);
        animatedObjects.move();
        
        /*
         * Adjust two major components of the model so they are in sync:
         * the dynamicBackground and the animatedObjects.
         **/
        int numEatenBefore = dynamicBackground.getNumDotsEnergizersEaten();
        Collection coordsAVOsToRemove 
                = dynamicBackground.adjustModel(getPacDasher());   
        int numEatenAfter = dynamicBackground.getNumDotsEnergizersEaten();
        this.isMazeFinished = checkMazeFinished();
        //Const.logger.fine("AVOs to remove: " + coordsAVOsToRemove);
        animatedObjects.adjustModel(getPacDasher(), coordsAVOsToRemove,
                this.isMazeFinished);

        /**
         * Add or remove fruit depending on how much PacDasher has eaten.
         **/
        if (numEatenAfter > numEatenBefore)
        {
            int numDotsEnergizersEaten = numEatenAfter;
        
            if (numDotsEnergizersEaten % NUM_PELLETS_FRUIT_PERIOD 
                    == NUM_PELLETS_BEFORE_FRUIT)
            {
                addFruit();
                Const.logger.fine(numDotsEnergizersEaten + "," 
                        + NUM_PELLETS_FRUIT_PERIOD + "," 
                        + NUM_PELLETS_BEFORE_FRUIT);
                Const.logger.fine("animatedObjects: " + animatedObjects);
            }
            else if (numDotsEnergizersEaten % NUM_PELLETS_FRUIT_PERIOD 
                    == NUM_PELLETS_BEFORE_FRUIT - NUM_PELLETS_FRUIT_DURATION)
            {
                removeFruit();
            }
        }
    }    

    private void addFruit()
    {
        dynamicBackground.addFruit(getCurrentFruit());
        animatedObjects.addFruit(getCurrentFruit());
    }
    
    private void removeFruit()
    {        
        //Const.logger.fine("xyc: " + getCurrentFruit().getXYCoarse());
        dynamicBackground.removeFruit(getCurrentFruit());
        animatedObjects.removeFruit(getCurrentFruit());  
    }
    
    public void reset()
    {
        this.isMazeFinished = false;
        animatedObjects.resetMobiles();
        setGeneralGhostState(GhostState.NORMAL_GHOST_STATE);
    }        
    
    void setGeneralGhostState(GhostState ghostState)
    {
        animatedObjects.setGeneralGhostState(ghostState);
    }
    
    private PacDasher getPacDasher()
    {
        return UIModel.getInstance().getPacDasher();
    }
   
    public void removeTempVisualObjects()
    {
        animatedObjects.removeTempVisualObjects();
    }
    
    public void createScoreVOFruit(XY eatXY, int score)
    {
        animatedObjects.createScoreVOFruit(eatXY, score);
    }
    
    /***********************************************************************
     * Factory methods -- TODO consider putting them into a factory class
     **********************************************************************/
        
    public static Maze create(MazeRaw mazeRaw)
    {
        Maze maze = new Maze();

        Set<Integer> levels = mazeRaw.getLevels();
        maze.setLevels(levels);
        Color wallColor = mazeRaw.getWallColor();
        maze.setWallColor(wallColor);
        Color gateColor = mazeRaw.getGateColor();
        maze.setGateColor(gateColor);
        Class defaultBackgroundClass = mazeRaw.getDefaultBackgroundObject();
        maze.setDefaultBackgroundClass(defaultBackgroundClass);
        Class defaultForegroundClass = mazeRaw.getDefaultForegroundObject();
        maze.setDefaultForegroundClass(defaultForegroundClass);
        List<XYCoarse> xycClearAreaBounds = mazeRaw.getXycClearAreaBounds();
        if (xycClearAreaBounds.size() == 2)
        {
            Iterator<XYCoarse> it = xycClearAreaBounds.iterator();
            XYCoarse xycUpperLeft = it.next();
            XYCoarse xycBottomRight = it.next();
            maze.setClearAreaBounds(xycUpperLeft.getX(), xycUpperLeft.getY(), 
                    xycBottomRight.getX(), xycBottomRight.getY());
        }	
        else
        {
            Const.logger.severe("xycClearAreaBounds: " + xycClearAreaBounds);
            maze.setClearAreaBounds(0, 0, 0, 0);
        }
        
        Set<XYCoarse> xycClearAreaExtra = mazeRaw.getXycClearAreaExtra();
        maze.setClearAreaExtra(xycClearAreaExtra);
        
        XYCoarse xycPacDasher = mazeRaw.getXycPacDasher();
        maze.setPacDasherInitPos(xycPacDasher.getX(), xycPacDasher.getY());

        XYCoarse xycFruit = mazeRaw.getXycFruit();
        List<Class> fruitClasses = mazeRaw.getClassFruits();
        maze.setFruits(fruitClasses, xycFruit);
        
        Set<MazeRaw.VisualObjectCollection> voCollections
        		= mazeRaw.getVisualObjectCollections();
        for (MazeRaw.VisualObjectCollection voCollection: voCollections)
        {
             if (voCollection.xycs == null || voCollection.voClass == null)
             {
                 Const.logger.severe("voCollection had null element for " 
                         + voCollection.voClass);
             }
             else
             {
                 maze.setVisualObjectData(voCollection.xycs, voCollection.voClass,
                         voCollection.isForeground);
             }
        }
      
        maze.doInitializationAfterXML();
        return maze;
    }

    public void setVisualObjectData(Set<XYCoarse> xycs, 
            Class visualObjectClass, boolean isForeground)
    {	
        // Note: better to read this information into an intermediate
        // MazeData mapping, and then loop through this to do this
        // processing. Helps for cloning to have a data structure
        // without internal dependencies.
        
        VisualObject visualObject = voFactory.construct(visualObjectClass);
        Const.logger.fine("" + visualObjectClass.getName() + " " 
                + isForeground + "" + xycs);
        
        if (!isForeground)
        {
            staticBackground.setBackground(xycs, visualObject);
        }
        else
        {            
            if (!(visualObject instanceof MobileVisualObject))
            {
                //Const.logger.fine("Setting part of dynamic bg: "
                //        + visualObjectClass);
                if (visualObject.getTypeID() != sampleFruit.getTypeID())
                {
                    dynamicBackground.setBackground(xycs, visualObject);
                }
            }
            if (visualObject instanceof AnimatedVisualObject)
            {
                animatedObjects.constructAnimateds(xycs, visualObjectClass);
            }
        }
    }
   
    /***********************************************************************
     * Utility methods
     **********************************************************************/

    // not currently used
    public static HashSet arrayInt2DToSet(int[][] array)
    {
         HashSet<XYCoarse> set = new HashSet<XYCoarse>();
         for (int[] e : array)
         {
             int xCoarse = e[0];
             int yCoarse = e[1];
             if (XYCManager.isInitialized())
             {
                 set.add(XYCManager.getInstance().createXYC(xCoarse, yCoarse));
             }             
             else
             {
                 set.add(new XYCoarse(xCoarse, yCoarse));
             }
         }
         return set;
    }

    public void incNumMazeAccesses()
    {
        this.numMazeAccesses++;
        Const.logger.fine("this.numMazeAccesses: " + this.numMazeAccesses);
    }
    
    /**
     * Not used. Should not be necessary.
     */
    public static int[][] xycCollectionToArray(
            Collection<XYCoarse> xycCollection)
    {
        int [][] coords = new int[xycCollection.size()][2];
        int i = 0;
        for (XYCoarse xyc: xycCollection)
        {            
            coords[i][0] = xyc.getX();
            coords[i][1] = xyc.getY();
            i++;
        }
        return coords;
    }
    
    /**
     * Not used. Should not be necessary.
     */
    public XYCoarse getXYCoarse(int[][] arraySingleItem)
    {
        return XYCManager.getInstance().createXYC(
            arraySingleItem[0][0], arraySingleItem[0][1]);    
    }   
    
    /***********************************************************************
     * Standard methods
     **********************************************************************/
    
    public Object clone()
    {
        Maze cloneMaze = new Maze();
   
        cloneMaze.staticBackground = (MazeBackgroundStatic) 
                this.staticBackground.clone();
        cloneMaze.dynamicBackground = (MazeBackgroundDynamic) 
                this.dynamicBackground.clone();
        cloneMaze.animatedObjects = (MazeAnimatedObjects)
                this.animatedObjects.clone();
        
        cloneMaze.xCoarseBoundary = this.xCoarseBoundary;
        cloneMaze.yCoarseBoundary = this.yCoarseBoundary;
            
        cloneMaze.isMazeFinished = this.isMazeFinished;
       
        cloneMaze.fruits = (ArrayList) this.fruits.clone();
        cloneMaze.numMazeAccesses = this.numMazeAccesses;
        cloneMaze.setGeneralGhostState(GhostState.NORMAL_GHOST_STATE);
        Const.logger.fine("cloneMaze.numMazeAccesses " 
                + cloneMaze.numMazeAccesses);
        /*if (cloneMaze.fruits != null)
        {
            Const.logger.fine("current fruit of clonedMaze: "
                    + cloneMaze.fruits.get(cloneMaze.numMazeAccesses - 1));
        }*/
                
        return cloneMaze;
    }
}