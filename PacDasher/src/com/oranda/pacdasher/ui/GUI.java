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
 
package com.oranda.pacdasher.ui;

import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.uimodel.Maze;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.pacdasher.uimodel.util.XY;
import com.oranda.util.Str;
 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;


/**
 * GUI - manages the main Frame: containing 3 parts: the score canvas
 * at the top, the main mazeCanvas taking up most of the area,
 * and a little canvas showing lives at the bottom.
 */ 
public class GUI
{    
    // the one true instance
    private static GUI gui;
    
    private PacFrame pacFrame;
    private MazeCanvas mazeCanvas;
    private LivesCanvas livesCanvas;
    private InfoCanvas infoCanvas;
    private SplashCanvas splashCanvas;
    private MenuCanvas menuCanvas;
    
    private BufferStrategy strategy;

    private int xOffsetGeneral, yOffsetGeneral;
    private int viewableWidth, viewableHeight;
    
    private boolean available = false;
    private boolean useFullScreen = false;
    private boolean isIconified = false;
    
    GraphicsEnvironment graphicsEnvironment
            = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice graphicsDevice 
            = graphicsEnvironment.getDefaultScreenDevice();
 
        
    public static GUI getInstance()
    {
        if (GUI.gui == null) 
        {
            GUI.gui = new GUI();
        }
        return gui;
    }
                 
    /*
     * Constructor is private because GUI is a singleton
     */
    private GUI()
    {        
        if (graphicsDevice.isFullScreenSupported()) 
        {            
            useFullScreen = true;
        }
        GraphicsConfiguration graphicsConfiguration
            = graphicsDevice.getDefaultConfiguration();          
        pacFrame = new PacFrame("PacDasher!", graphicsConfiguration);
        requestFocus();
            
        splashCanvas = new SplashCanvas();
       
        int mazeWidth = Maze.DRAWABLE_WIDTH; 
        int mazeHeight = Maze.DRAWABLE_HEIGHT;                

        mazeCanvas = new MazeCanvas();
        mazeCanvas.setSize(mazeWidth, mazeHeight);    
                
        infoCanvas = new InfoCanvas();
        int infoWidth = mazeWidth;
        infoCanvas.setSize(infoWidth, UIModelConsts.INFO_HEIGHT);
        
        livesCanvas = new LivesCanvas();
        int livesWidth = mazeWidth;
        livesCanvas.setSize(livesWidth, UIModelConsts.LIVES_HEIGHT);
            
        pacFrame.setBackground(UIModelConsts.MAZE_BG_COLOR);

        XY xyOffsetSplash = new XY(0, 0);
        if (useFullScreen) 
        {            
            xyOffsetSplash = initializeFullScreen();
        }
        else
        {
            xyOffsetSplash = initializeFrame();                
        }           
        pacFrame.setVisible(true);
        setBufferStrategy(); 

        this.available = true;
        
        createWindowListeners();
        
        splashCanvas.initialize(this.strategy, xyOffsetSplash.getX(), 
                xyOffsetSplash.getY());
    }
    
    public boolean isIconified()
    {
        return this.isIconified;
    }
    
    public void startGame()
    {
        clearScreen();
        infoCanvas.initialize(this.strategy, xOffsetGeneral, yOffsetGeneral);
        livesCanvas.initialize(this.strategy, xOffsetGeneral, yOffsetGeneral 
                + UIModelConsts.INFO_HEIGHT + Maze.DRAWABLE_HEIGHT);
        mazeCanvas.initialize(this.strategy, xOffsetGeneral, yOffsetGeneral
                + UIModelConsts.INFO_HEIGHT);
        if (useFullScreen)
        {
            
            int xOffsetMenu = (this.viewableWidth * 3)/4;
            //Const.logger.fine(xOffsetMenu + " xOffsetGeneral: " + this.xOffsetGeneral);
            this.menuCanvas.initialize(this.strategy, xOffsetMenu, 0);
        }
    }
    
    public void addFruitImage(Image img)
    {
        livesCanvas.addFruitImage(img);
    }
    
    /*
     * @return the top left corner of where to place the splash image
     */
    protected XY initializeFullScreen()
    {        
        this.pacFrame.setUndecorated(true); // no menu bar, borders, etc.
        // turn off paint events because doing active rendering 
        this.pacFrame.setIgnoreRepaint(true);        
        this.pacFrame.setResizable(false);
        graphicsDevice.setFullScreenWindow(this.pacFrame); // switch on FSEM
        // possible to adjust the display modes now
        // setDisplayMode(800, 600, 8); // or try 16 bits
        
        DisplayMode dm = graphicsDevice.getDisplayMode();
        
        Image gSplashScreen = ResourceMgr.getInstance().gSplashScreen;
        int splashWidth = gSplashScreen.getWidth(null);
        int splashHeight =gSplashScreen.getHeight(null);
        int screenWidth = dm.getWidth();
        int screenHeight = dm.getHeight();
        this.viewableWidth = screenWidth;
        this.viewableHeight = screenHeight;
        
        this.menuCanvas = new MenuCanvas();
        int menuWidth = (screenWidth - mazeCanvas.getWidth()) / 2;
        this.menuCanvas.setSize(menuWidth, screenHeight);
        
        Const.logger.fine("Current Display Mode: (" 
                + "," + screenWidth + "," + screenHeight + ","
                + dm.getBitDepth() + "," + dm.getRefreshRate() + ") ");  
        
        this.xOffsetGeneral = (screenWidth - Maze.DRAWABLE_WIDTH) / 6; // 2
        this.yOffsetGeneral += 10;
        int xOffsetSplash = (screenWidth - splashWidth) / 2;
        int yOffsetSplash = 0;
        
        splashWidth = gSplashScreen.getWidth(null);
        splashHeight =gSplashScreen.getHeight(null);
        splashCanvas.setSize(gSplashScreen.getWidth(null), screenHeight);
        return new XY(xOffsetSplash, yOffsetSplash);
    }

    /*
     * @return the top left corner of where to place the splash image
     */
    protected XY initializeFrame()
    {
        Const.logger.severe("Full-screen exclusive mode not supported: "
                + "Using normal frame");
        this.viewableWidth = mazeCanvas.getWidth();
        this.viewableHeight = mazeCanvas.getHeight() + infoCanvas.getHeight() 
        		+ livesCanvas.getHeight();
        pacFrame.setSize(mazeCanvas.getWidth() 
                + UIModelConsts.FRAME_EDGE_SIZE * 2, 
                this.viewableHeight + UIModelConsts.FRAME_BAR_SIZE 
                + UIModelConsts.FRAME_EDGE_SIZE * 3);
        pacFrame.setResizable(false);            
        pacFrame.setLocation(10, 0);


    
        this.yOffsetGeneral += UIModelConsts.FRAME_BAR_SIZE;
        splashCanvas.setSize(this.viewableWidth, this.viewableHeight);     
        return new XY(UIModelConsts.FRAME_EDGE_SIZE, yOffsetGeneral);                
    }
    
    public void clearScreen()
    {
        //Const.logger.fine("Clearing screen");       
        Graphics drawGraphics = this.strategy.getDrawGraphics();
        drawGraphics.setColor(UIModelConsts.MAZE_BG_COLOR);
        drawGraphics.fillRect(0, 0,  
                this.viewableWidth + 2 * UIModelConsts.FRAME_EDGE_SIZE, 
                this.viewableHeight + 2 * UIModelConsts.FRAME_BAR_SIZE);
        this.strategy.show();

        if (useFullScreen)
        {
            setBufferStrategy(); // reset this so we don't get old content
        }
    }
    
    protected void createWindowListeners()
    {
        pacFrame.addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();
                Const.logger.fine("" + x + "," + y);
                if (infoCanvas.isClickOnExit(x, y)
                        || menuCanvas.isClickOnExit(x, y) 
                        || splashCanvas.isClickOnExit(x, y))
                {
                    PacDasherMain.exit();
                }
                else if (menuCanvas.isClickOnMinimize(x, y) 
                        || splashCanvas.isClickOnMinimize(x, y))
                {
                    minimize();
                }
                else if (splashCanvas.isClickOnStartGame(x, y))
                {
                    pacFrame.simulateKeyEvent(KeyEvent.VK_1);
                }
            }
        } 
        );
            
       pacFrame.addWindowListener(new WindowAdapter() 
       {
           public void windowClosing(WindowEvent e) 
    	   {
    	       PacDasherMain.exit();
    	   }
           public void windowDeiconified(WindowEvent e) 
           {
               isIconified = false;
               ResourceMgr.getInstance().setRestarted();
           }
           public void windowIconified(WindowEvent e) 
           {
               isIconified = true;
               ResourceMgr.getInstance().setPaused();
           } 
       } 
       );       
    }
    
    private void setBufferStrategy()
    { 
        try 
        {
            EventQueue.invokeAndWait( new Runnable() 
            {
                public void run()
                { 
                    pacFrame.createBufferStrategy(2); 
                }
            });
        }
        catch (Exception e) 
        {
            Const.logger.severe("Error creating buffer strategy "
                    + Str.getStackTraceAsStr(e));
            System.exit(-1);
        }
        try 
        { 
            // sleep to give time for buffer strategy to be done
            Thread.sleep(500); 
        }
        catch(InterruptedException ex) {}
        this.strategy = pacFrame.getBufferStrategy(); // store for later
    }


    public void exerciseKeyInput()
    {
        pacFrame.dispatchEvent(new KeyEvent(pacFrame, KeyEvent.KEY_PRESSED,
            0, 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));
    }

    public KeyEvent getLastKeyEvent()
    {
    	return pacFrame.getLastKeyEvent();
    }
    
    
    public void clearLastKeyEvent()
    {
    	pacFrame.clearLastKeyEvent();
    }
    
    	
    public SplashCanvas getSplashCanvas()
    {
        return this.splashCanvas;        
    }
    
    public MazeCanvas getMazeCanvas()
    {
        return this.mazeCanvas;
    }

    public InfoCanvas getInfoCanvas()
    {
        return this.infoCanvas;
    }

    public LivesCanvas getLivesCanvas()
    {
        return this.livesCanvas;
    }

    public MenuCanvas getMenuCanvas()
    {
        return this.menuCanvas;
    }
    
    public Frame getPacFrame()
    {
        return this.pacFrame;
    }
    public void requestFocus()
    {
    	pacFrame.requestFocusInWindow();
    }
    
    public void refreshCanvas()
	{      
        //Const.logger.fine("refreshCanvas");
        if (this.available)
        {
            this.strategy.show();
        }
	}
	
    public void minimize()
    {
        this.pacFrame.setExtendedState(Frame.ICONIFIED);
    }
    
    /**
     * Used on exiting.
     */
    public void dispose()
    {
        this.available = false;
        pacFrame.dispose();
    }
    
    public BufferCapabilities getBufferCapabilities()
    {
        return this.strategy.getCapabilities();
    }
    
	public Graphics getDrawGraphics()
	{
		return this.strategy.getDrawGraphics();
	}    
    
	public void paintMenuCanvas()
	{
	    // unlike other canvases, the menu canvas only exists for full screen
	    if (menuCanvas != null)
	    {
	        menuCanvas.paint();
	    }
	}
    /* 
     NOT USED
    public void addKeyListener(KeyListener keyListener)
    {
        pacFrame.addKeyListener(keyListener);
    }
    */
} 