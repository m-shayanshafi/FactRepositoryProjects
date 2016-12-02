//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package main;


import java.util.logging.Level;
import java.util.logging.Logger;

import world.World;
import entity.Message;
import input.LoadHelper;
import audio.AudioSystem;

import com.jme.app.AbstractGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;
import com.jme.util.TextureManager;


public class Main extends AbstractGame {
	
	
	private static final Logger logger = Logger.getLogger(Main.class
            .getName());

    /**
     * The camera that we see through.
     */
    protected Camera cam;

    /**
     * The root of our normal scene graph.
     */
    protected Node rootNode;

    /**
     * Handles our mouse/keyboard input.
     */
    protected InputHandler input;

    /**
     * Alpha bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int alphaBits = 0;

    /**
     * Depth bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int depthBits = 8;

    /**
     * Stencil bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int stencilBits = 0;

    /**
     * Number of samples to use for the multisample buffer. Any changes must be made prior to call of start().
     */
    protected int samples = 0;

    /**
     * Simply an easy way to get at timer.getTimePerFrame(). Also saves math cycles since
     * you don't call getTimePerFrame more than once per frame.
     */
    protected float tpf;

    /**
     * A lightstate to turn on and off for the rootNode
     */
    protected LightState lightState;


	// Game sequences
	public static int INTRO = 0;
	public static int LOADING = 1;
	public static int INGAME = 2;
	public static int KINEMATIC = 3;
	public static int PAUSE = 4;
	public static int GAME_OVER = 5;
	public static int GAME_DONE = 6;
	public static int LOAD = 7;
	public static int SAVE = 8;
	public static int FINISHED = 9;
	
	private int sequence;
	
    
	/**
	 * Entry point for the game
	 * 
	 * @param args
	 *            arguments passed to the program
	 */
	public static void main(String[] args) {
		Main app = new Main();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
		logger.info( "GOOD BYE !!!");
		System.exit(0);
	}
    

    /**
     * Updates the timer, sets tpf, updates the input and updates the fps
     * string. Also checks keys for toggling pause, bounds, normals, lights,
     * wire etc.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#update(float interpolation)
     */
    public final void start() {
        
    	getAttributes();
    	//settings = getNewSettings();
    	if (finished)
    		return;
    	
    	initSystem();
    	initGame();
    	AudioSystem audio = AudioSystem.getSystem();
    	
    	sequence = INTRO;
        
        while (!finished) {
    		if (sequence == INTRO) sequence = DisplayIntro.display(rootNode, lightState, cam, input);
    		else if (sequence == LOADING) sequence = DisplayLoading.display(rootNode, lightState, cam, input);
    		else if (sequence == INGAME) sequence = DisplayInGame.display(rootNode, lightState, cam, input);
    		else if (sequence == KINEMATIC) sequence = DisplayKinematic.display(rootNode, lightState, cam, input);
    		else if (sequence == PAUSE) sequence = DisplayPause.display(rootNode, lightState, cam, input);
    		else if (sequence == GAME_OVER) sequence = DisplayGameOver.display(rootNode, lightState, cam, input);
    		else if (sequence == GAME_DONE) sequence = DisplayGameDone.display(rootNode, lightState, cam, input);
    		else if (sequence == LOAD) sequence = DisplayLoad.display(rootNode, lightState, cam, input);
    		else if (sequence == SAVE) sequence = DisplaySave.display(rootNode, lightState, cam, input);
    		else if (sequence == FINISHED) finish();
    	}
        
        audio.cleanup();
        cleanup();
        logger.info( "Application ending.");

        if (display != null) {
            display.reset();
        }
        quit();
    }

    protected void update(float interpolation) {}

    /**
     * Clears stats, the buffers and renders bounds and normals if on.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected void render( float interpolation ) {}
    

    /**
     * Creates display, sets up camera, and binds keys. Called in
     * BaseGame.start() directly after the dialog box.
     *
     * @see AbstractGame#initSystem()
     */
    protected void initSystem() throws JmeException {
        logger.info(getVersion());
        try {
            /**
             * Get a DisplaySystem acording to the renderer selected in the
             * startup box.
             */
            display = DisplaySystem.getDisplaySystem(settings.getRenderer() );
            
            display.setMinDepthBits( depthBits );
            display.setMinStencilBits( stencilBits );
            display.setMinAlphaBits( alphaBits );
            display.setMinSamples( samples );

            /** Create a window with the startup box's information. */
            display.createWindow(settings.getWidth(), settings.getHeight(),
                    settings.getDepth(), settings.getFrequency(),
                    settings.isFullscreen() );
            logger.info("Running on: " + display.getAdapter()
                    + "\nDriver version: " + display.getDriverVersion() + "\n"
                    + display.getDisplayVendor() + " - "
                    + display.getDisplayRenderer() + " - "
                    + display.getDisplayAPIVersion());
            
            display.setVSyncEnabled(false);
            
            /**
             * Create a camera specific to the DisplaySystem that works with the
             * display's width and height
             */
            cam = display.getRenderer().createCamera( display.getWidth(),
                    display.getHeight() );

        } catch ( JmeException e ) {
            /**
             * If the displaysystem can't be initialized correctly, exit
             * instantly.
             */
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
            System.exit( 1 );
        }

        /** Set a black background. */
        display.getRenderer().setBackgroundColor( ColorRGBA.black.clone() );

        /** Set up how our camera sees. */
        cam.setFrustumPerspective( 65.0f, (float) display.getWidth()
                / (float) display.getHeight(), 1f, 3000f );
        cam.setParallelProjection( false );
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 0.0f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        /** Move our camera to a correct place and orientation. */
        cam.setFrame( loc, left, up, dir );
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        /** Assign the camera to this renderer. */
        display.getRenderer().setCamera( cam );

        /** Create a basic input controller. */
        input = new DummyHandler();
    }
    
    
    /**
     * Creates rootNode, lighting, statistic text, and other basic render
     * states. Called in BaseGame.start() after initSystem().
     *
     * @see AbstractGame#initGame()
     */
    protected void initGame() {
        /** Create rootNode */
        rootNode = new Node( "rootNode" );

        /**
         * Create a ZBuffer to display pixels closest to the camera above
         * farther ones.
         */
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled( true );
        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo );
        rootNode.setRenderState( buf );
        
        
        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled( true );
        rootNode.setRenderState( lightState );
        
		try {
			display.setTitle("Flesh Snatcher");
			
			LoadHelper.setTextureLocator("data/map/textures/");
			
	        World.loadMaps("data/map/index.txt");
	        
			DisplayIntro.init();
			DisplayLoading.init();
			DisplayPause.init();
			DisplayGameOver.init();
	        DisplayGameDone.init();
	        
	        Message.init();
	        
	        Player.initHud();
		}
	    catch(Exception ex) {
	    	System.out.println("EXCEPTION !!!!!");
	    	finish();
	    }
    }
    
    
    /**
     * unused
     *
     * @see AbstractGame#reinit()
     */
    protected void reinit() {}

    /**
     * Cleans up the keyboard.
     *
     * @see AbstractGame#cleanup()
     */
    protected void cleanup() {
        logger.info( "Cleaning up resources." );

        TextureManager.doTextureCleanup();
        if (display != null && display.getRenderer() != null)
            display.getRenderer().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
    }

    protected GameSettings getNewSettings() {
        return new BaseGameSettings();
    }
    
    /**
     * Calls the quit of BaseGame to clean up the display and then closes the JVM.
     */
    protected void quit() {
    	if (display != null)
            display.close();
    }
    
    
    /**
     * A PropertiesGameSettings which defaults Fullscreen to TRUE.
     */
    static class BaseGameSettings extends PropertiesGameSettings {
        static {
            // This is how you programmatically override the DEFAULT_*
            // settings of GameSettings.
            // You can also make declarative overrides by using
            // "game-defaults.properties" in a CLASSPATH root directory (or
            // use the 2-param PropertiesGameSettings constructor for any name).
            // (This is all very different from the user-specific
            // "properties.cfg"... or whatever file is specified below...,
            // which is read from the current directory and is session-specific).
            defaultFullscreen = Boolean.TRUE;
            //defaultSettingsWidgetImage = "/jmetest/data/images/Monkey.png";
            defaultSettingsWidgetImage = "/data/system/pentagram.png";
        }
        /**
         * Populates the GameSettings from the (session-specific) .properties
         * file.
         */
        BaseGameSettings() {
            super("properties.cfg");
            load();
        }
    }

}
