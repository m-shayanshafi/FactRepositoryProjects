package essentials;

import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;

import essentials.Drawer;

public class Main implements Game, KeyListener, MouseListener {
	private Drawer drawer = new Drawer();
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		try {
			AppGameContainer agc = new AppGameContainer(new Main(), 718, 669, false);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public boolean closeRequested() {
		return true;
	}
	public String getTitle() {
		return "Hunger Games Board Game";
	}
	public void init(GameContainer gc) throws SlickException {
        drawer.drawWhat = 6;
        drawer.logMemory();
        drawer.loadFonts();
        drawer.initializeArray();
        gc.setVSync(true);
        gc.setMaximumLogicUpdateInterval(50);
		gc.getInput().addKeyListener((KeyListener) this);
		gc.getInput().addMouseListener((MouseListener) this);
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(drawer.drawWhat != 4 && drawer.drawWhat != 3)
			g.translate(0, -30);
		drawer.render(g);
	}
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}
	public void inputEnded() {
		
	}
	public void inputStarted() {
		
	}
	public boolean isAcceptingInput() {
		return true;
	}
	public void setInput(Input input) {
		
	}
	public void keyPressed(int key, char c) {
    	if(drawer.drawWhat == 5){
			if(drawer.usingNumber == true){
	    		if(key == Input.KEY_1 || key == Input.KEY_NUMPAD1)drawer.addToText(1);
	    		if(key == Input.KEY_2 || key == Input.KEY_NUMPAD2)drawer.addToText(2);
	    		if(key == Input.KEY_3 || key == Input.KEY_NUMPAD3)drawer.addToText(3);
	    		if(key == Input.KEY_4 || key == Input.KEY_NUMPAD4)drawer.addToText(4);
	    		if(key == Input.KEY_5 || key == Input.KEY_NUMPAD5)drawer.addToText(5);
	    		if(key == Input.KEY_6 || key == Input.KEY_NUMPAD6)drawer.addToText(6);
	    		if(key == Input.KEY_7 || key == Input.KEY_NUMPAD7)drawer.addToText(7);
	    		if(key == Input.KEY_8 || key == Input.KEY_NUMPAD8)drawer.addToText(8);
	    		if(key == Input.KEY_9 || key == Input.KEY_NUMPAD9)drawer.addToText(9);
	    		if(key == Input.KEY_0 || key == Input.KEY_NUMPAD0)drawer.addToText(0);
	    		if(key == Input.KEY_BACK)drawer.addToText(10);
	    		if(key == Input.KEY_ENTER)drawer.addToText(11);
			}
			else {
				if(key == Input.KEY_ENTER)
					drawer.confirm();
				if(key != Input.KEY_BACK && key != Input.KEY_LSHIFT && key != Input.KEY_RSHIFT)
					drawer.addToName(c);
				if(key == Input.KEY_BACK)
					drawer.undo();
			}
    	}
		if(drawer.drawWhat == 4){
			if(drawer.enableCheats == true){
	    		if(key == Input.KEY_1)drawer.addToInventory(1);
	    		if(key == Input.KEY_2)drawer.addToInventory(2);
	    		if(key == Input.KEY_3)drawer.addToInventory(3);
	    		if(key == Input.KEY_4)drawer.addToInventory(4);
	    		if(key == Input.KEY_5)drawer.addToInventory(5);
	    		if(key == Input.KEY_6)drawer.addToInventory(6);
	    		if(key == Input.KEY_7)drawer.addToInventory(7);
	    		if(key == Input.KEY_8)drawer.addToInventory(8);
	    		if(key == Input.KEY_9)drawer.addToInventory(9);
	    		if(key == Input.KEY_0)drawer.addToInventory(10);
	    		if(c == 'q' || c == 'Q')drawer.addToInventory(11);
	    		if(c == 'w' || c == 'W')drawer.addToInventory(12);
			}
		}
		if(drawer.drawWhat == 1){
			if(c == 'c' || c == 'C')drawer.getGameMode((byte) 1);
			if(c == 'r' || c == 'R')drawer.getGameMode((byte) 2);
		}
	}
	public void keyReleased(int key, char c) {
		
	}
	public void mouseClicked(int button, int x, int y, int clickCount) {
		drawer.mouseClicked(x, y, button);
	}
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		
	}
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		drawer.setPosition(newX, newY);
		drawer.mouseMoved(newX, newY);
	}
	public void mousePressed(int button, int x, int y) {
		
	}
	public void mouseReleased(int button, int x, int y) {
		
	}
	public void mouseWheelMoved(int delta) {
		
	}
}
