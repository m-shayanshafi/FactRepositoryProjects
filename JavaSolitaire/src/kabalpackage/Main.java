package kabalpackage;

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import kabalpackage.*;
import kabalpackage.info.AboutWindow;
import kabalpackage.utilities.*;

/**
 * This is the main window of the application.
 */
public class Main extends JFrame{
    
    private GameArea gameArea = new GameArea();

    /**
     * Creates a new instance of Main.
     */
    public Main(){
        
        JSPSplash splash = new JSPSplash();
        Thread splashThread = new Thread(splash);
        splashThread.start();
        splash.setVisible(true); 
        
	
	// Set look and feel to GTK
	try {
	    String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	    UIManager.setLookAndFeel(GTK);
	    UIManager.installLookAndFeel("GTK", GTK);
	} catch (Exception e) {
	    System.err.println("Could not install GTK");
	}
        
        
        // If we close the window through the taskbar, we'll normally be thrown
        // an exception. This listener prevents this from happening, making
        // the application quit normally.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setTitle("Java Solitaire Project!");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(LayoutVariables.WINDOW_WIDTH, 
                LayoutVariables.WINDOW_HEIGHT));
        setResizable(false);
        
        try{
            Image iconImage = ImageIO.read(getClass()
                .getResourceAsStream("images/icon.gif") );
            setIconImage(iconImage);
        }
        catch(Exception e){
            System.err.println("Could not load icon.");
        }
        
        
	
	MenuBar newMenuBar = new MenuBar();
	add(newMenuBar);
	setJMenuBar(newMenuBar);
        
        gameArea.newGame();
	setContentPane(gameArea);
	
	pack();
        
        setLocationRelativeTo(null);
    }
    
        
    private class MenuBar extends JMenuBar{
        
        JMenuItem editMenuPause;
        JMenuItem editMenuContinue;
        JMenuItem soundMenuOn;
        JMenuItem soundMenuOff;
        
	public MenuBar(){
	    MenuListener menuListener = new MenuListener();

	    JMenu fileMenu = new JMenu("File");
	    add(fileMenu);

	    JMenuItem fileMenuNew = new JMenuItem("New Game");
	    fileMenuNew.addActionListener(menuListener);
	    fileMenuNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    fileMenu.add(fileMenuNew);
            
            JMenuItem fileMenuHighScore = new JMenuItem("Highscores");
	    fileMenuHighScore.addActionListener(menuListener);
	    fileMenuHighScore.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_H, KeyEvent.ALT_DOWN_MASK));
	    fileMenu.add(fileMenuHighScore);

	    JMenuItem fileMenuExit = new JMenuItem("Exit");
	    fileMenuExit.addActionListener(menuListener);
	    fileMenuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    fileMenu.add(fileMenuExit);

	    JMenu editMenu = new JMenu("Edit");
	    add(editMenu);

	    /* JMenuItem editMenuUndo = new JMenuItem("Undo move");
	    editMenuUndo.addActionListener(menuListener);
	    editMenuUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    editMenu.add(editMenuUndo);
            editMenuUndo.setEnabled(false); */
            
            editMenuPause = new JMenuItem("Pause");
	    editMenuPause.addActionListener(menuListener);
            editMenuPause.setEnabled(false);
	    editMenuPause.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_SPACE, 0));
	    editMenuPause.setEnabled(true);
            editMenu.add(editMenuPause);            
            
            editMenuContinue = new JMenuItem("Continue");
	    editMenuContinue.addActionListener(menuListener);
            editMenuContinue.setEnabled(false);
	    editMenu.add(editMenuContinue);
            
            
            JMenu cardDealMenu = new JMenu("Cards to deal");
            JMenuItem cardDealOne = new JMenuItem("1 card");
            cardDealOne.addActionListener(menuListener);
            cardDealMenu.add(cardDealOne);
            JMenuItem cardDealThree = new JMenuItem("3 cards");
            cardDealThree.addActionListener(menuListener);            
            cardDealMenu.add(cardDealThree);
            editMenu.add(cardDealMenu);
            
            
            JMenu backgroundMenu = new JMenu("Set background");
	    editMenu.add(backgroundMenu);            
            JMenuItem backgroundEntry;
            for(int i=0; i < LayoutVariables.fileNames.length; i++){
                backgroundEntry = new JMenuItem(LayoutVariables.bgNames[i]);
                backgroundEntry.addActionListener(menuListener);
                backgroundMenu.add(backgroundEntry);
            }
            
            
            JMenu cardStyleMenu = new JMenu("Set card style");
            editMenu.add(cardStyleMenu);
            JMenuItem cardStyleEntry;
            for(int i=0; i < LayoutVariables.cardTitles.length; i++){
                cardStyleEntry = new JMenuItem(LayoutVariables.cardTitles[i]);
                cardStyleMenu.add(cardStyleEntry);
                cardStyleEntry.addActionListener(menuListener);
            }
            
            
            JMenu soundMenu = new JMenu("Sound");
            soundMenuOn = new JMenuItem("On");
            soundMenuOn.setEnabled(false);
            soundMenuOn.addActionListener(menuListener);
            soundMenuOff = new JMenuItem("Off");
            soundMenuOff.addActionListener(menuListener);
            soundMenu.add(soundMenuOn);
            soundMenu.add(soundMenuOff);
            editMenu.add(soundMenu);
                       

	    JMenu helpMenu = new JMenu("Help");
	    add(helpMenu);

	    JMenuItem helpMenuAbout = new JMenuItem("About");
	    helpMenuAbout.addActionListener(menuListener);
	    helpMenuAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    helpMenu.add(helpMenuAbout);
            
            JMenuItem helpMenuHint = new JMenuItem("Hint");
	    helpMenuHint.addActionListener(menuListener);
	    helpMenuHint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	    helpMenu.add(helpMenuHint);

        }
	
	private class MenuListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	        String event = e.getActionCommand();
	    
                if(event.equals("New Game")){
                    gameArea.newGame();
                    System.out.println("New game");
                }
                
	        if(event.equals("Exit")) System.exit(0);
                
                if(event.equals("Highscores")) gameArea.presentHighScore();
                
                if(event.equals("Hint")) gameArea.hint();
                
                if(event.equals("Pause")){
                    System.out.println("Pause");
                    gameArea.pauseTimer();
                    editMenuContinue.setEnabled(true);
                    editMenuPause.setEnabled(false);
                    editMenuContinue.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_SPACE, 0));
                }
                
                if(event.equals("Continue")){
                    System.out.println("Continue");
                    gameArea.resumeTimer();
                    
                    editMenuPause.setEnabled(true);
                    editMenuContinue.setEnabled(false);
                }
                
                if(event.equals("About")){
                    AboutWindow about = new AboutWindow();
                }
                
                if(event.equals("On")){
                    soundMenuOn.setEnabled(false);
                    soundMenuOff.setEnabled(true);
                    gameArea.toggleSoundEffects(true);
                }
                
                if(event.equals("Off")){
                    soundMenuOn.setEnabled(true);
                    soundMenuOff.setEnabled(false);
                    gameArea.toggleSoundEffects(false);
                }
                
                if(event.equals("1 card")){
                    gameArea.setCardsToDealCount(1);
                }
                
                if(event.equals("3 cards")){
                    gameArea.setCardsToDealCount(3);
                }
                
                
                
                // Changing background image
                else{
                    for(int i=0; i<LayoutVariables.fileNames.length; i++){
                        if(event.equals(LayoutVariables.bgNames[i])){
                            gameArea.changeBackground(i);
                            break;
                        }
                    }
                    
                    for(int i=0; i<LayoutVariables.cardTitles.length; i++){
                        if(event.equals(LayoutVariables.cardTitles[i])){
                            gameArea.changeImage(LayoutVariables.cardFiles[i],
                                    LayoutVariables.cardOverFiles[i]);
                            break;
                        }
                    }
                }
                
	    }
	}
	
    }
    
    
    public static void main(String[] args){
	Main newSolitaire = new Main();
	
	newSolitaire.setVisible(true);
    }
}
