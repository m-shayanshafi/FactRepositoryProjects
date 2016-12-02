package com.otabi.firestar.rpg;

//import org.lwjgl.glfw.GLFWErrorCallback;
//import org.lwjgl.glfw.GLFWKeyCallback;
//import org.lwjgl.glfw.GLFWvidmode;
//import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
//import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.system.MemoryUtil.NULL;

import com.otabi.firestar.rpg.menu.Menu;
import com.otabi.firestar.rpg.player.Player;
import com.otabi.firestar.rpg.player.Wizard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

//import java.awt.*;

public class RPG {

//    // We need to strongly reference callback instances.
//    private GLFWErrorCallback errorCallback;
//    private GLFWKeyCallback keyCallback;
//
//    // The window handle
//    private long window;
//
//    public void run() {
//        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
//
//        try {
//            init();
//            loop();
//
//            // Release window and window callbacks
//            glfwDestroyWindow(window);
//            keyCallback.release();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Terminate GLFW and release the GLFWerrorfun
//            glfwTerminate();
//            errorCallback.release();
//        }
//    }
//
//    private void init() {
//        // Setup an error callback. The default implementation
//        // will print the error message in System.err.
//        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
//
//        // Initialize GLFW. Most GLFW functions will not work before doing this.
//        if (glfwInit() != GL11.GL_TRUE)
//            throw new IllegalStateException("Unable to initialize GLFW");
//
//        // Configure our window
//        glfwDefaultWindowHints(); // optional, the current window hints are already the default
//        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
//        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be resizable
//
//        int WIDTH = 800;
//        int HEIGHT = 600;
//
//        // Create the window
//        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
//        if (window == NULL)
//            throw new RuntimeException("Failed to create the GLFW window");
//
//        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
//        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
//            @Override
//            public void invoke(long window, int key, int scancode, int action, int mods) {
//                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
//                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
//            }
//        });
//
//        // Get the resolution of the primary monitor
//        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//        // Center our window
//        glfwSetWindowPos(
//                window,
//                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
//                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
//        );
//
//
//        // Make the OpenGL context current
//        glfwMakeContextCurrent(window);
//        // Enable v-sync
//        glfwSwapInterval(1);
//
//        // Make the window visible
//        glfwShowWindow(window);
//    }

    public static GameState state = GameState.MENU;

//    private void loop() throws IOException {
//        // This line is critical for LWJGL's interoperation with GLFW's
//        // OpenGL context, or any context that is managed externally.
//        // LWJGL detects the context that is current in the current thread,
//        // creates the ContextCapabilities instance and makes the OpenGL
//        // bindings available for use.
//
//        GLContext.createFromCurrent();
//
//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        glOrtho(0, 800, 600 , 0, 0, 1);
//        glMatrixMode(GL_MODELVIEW);
//
//        // Set the clear color
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//
//        // Run the rendering loop until the user has attempted to close
//        // the window or has pressed the ESCAPE key.
//        while (glfwWindowShouldClose(window) == GL_FALSE) {
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//            glPushMatrix();
//            glTranslated(-1, -1, 0);
//            switch (state) {
//                case MENU:
//                    Menu.drawMenu();
//                    break;
//            }
//            glPopMatrix();
//            glfwSwapBuffers(window); // swap the color buffers
//
//
//
//            // Poll for window events. The key callback above will only be
//            // invoked during this call.
//            glfwPollEvents();
//        }
//    }

    public static void main(String[] args) throws Throwable {
//        new RPG().run();
        System.out.println("Setting display mode.");
        Display.setDisplayMode(new DisplayMode(800, 600));
        System.out.println("Creating display.");
        Display.create();
        System.out.println("Setting mode to projection.");
        glMatrixMode(GL_PROJECTION);
        System.out.println("Loading identity.");
        glLoadIdentity();
        System.out.println("Creating orthographic view.");
        glOrtho(0, 800, 600, 0, 1, -1);
        System.out.println("Setting mode to modelview.");
        glMatrixMode(GL_MODELVIEW);
        System.out.println("Initializing textures.");
        textureInit();
        System.out.println("Enabling textures.");
        glEnable(GL_TEXTURE_2D);
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glEnable (GL_BLEND);
        glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        System.out.println("Starting loop.");
        glClearColor(0.0f,0.25f,0.0f,0.0f);
        while(!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);
            switch (state) {
                case MENU:
                    Menu.drawMenu();
                    break;
                case PLAY:
                    Player.drawPlayer();
                    break;
            }
            Display.update();
        }
        textureRelease();
        Display.destroy();
        System.exit(0);
    }

    private static void textureRelease() {
        Wizard.textures[0].release();
    }

    private static void textureInit() throws IOException {
        System.out.println("Loading wizard0.");
        Wizard.textures[0] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/wizard0.png"));
    }

}