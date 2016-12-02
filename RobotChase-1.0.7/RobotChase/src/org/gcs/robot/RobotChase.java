package org.gcs.robot;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The RobotChase class controls the game. It instantiates the game model
 * ({@link org.gcs.robot.RCModel RCModel}) and three views of that model
 * ({@link org.gcs.robot.RCView RCView}, {@link org.gcs.robot.RCStatus
 * RCStatus}, and {@link org.gcs.robot.RCInfo RCInfo}). by implementing
 * ComponentListener, the controller can resize the game when the window
 * is resized. To simplify keyboard focus, the controller implements
 * KeyListener on behalf of the view.
 *      *
 * @serial exclude
 * @author John B. Matthews
 */
public class RobotChase extends JFrame
    implements ComponentListener, KeyListener {
    
    private JPanel content;
    private RCModel game;
    private RCView view;
    private RCStatus status;
    private RCInfo info;
    private int width;
    private int height;

    public static void main(String [] args) {
        EventQueue.invokeLater(new Runnable() {
            //@Override
            public void run() {
                int width = RCPrefs.getWidth();
                int height = RCPrefs.getHeight();
                new RobotChase(width, height);
            }
        });
    }
    
    /** Construct a new game. */
    public RobotChase(int width, int height) {
        this.width = width;
        this.height = height;
    
        game = new RCModel(width, height);
        view = new RCView(game);
        status = new RCStatus(game);
        info = new RCInfo(game);
    
        view.addKeyListener(this);
        view.setFocusable(true);
    
        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setOpaque(true);
        content.add(status, BorderLayout.NORTH);
        content.add(view, BorderLayout.CENTER);
        content.add(info, BorderLayout.SOUTH);
    
        this.setTitle("Robot Chase: Java Edition");
        this.addComponentListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setContentPane(content);
        this.pack();
        this.setVisible(true);

        RCPrefs.getKeys();
        game.restoreGame(
            RCPrefs.getHighLevel(),
            RCPrefs.getHighScore(),
            RCPrefs.getHighJumps());
    }
    
    /** Handle keyPressed events. */
    public void keyPressed(KeyEvent e) {
        Key k = Key.lookup(e.getKeyCode());
        if (k == null) return;
        switch (k) {
            case Animation: view.toggleAnimated(); break;
            case ChangeSet: view.nextSet(); break;
            case EditKeys: showKeyDialog(); break;
            case Help: showHelpDialog(); break;
            case NewGame: game.resetGame(); break;
            case Quit: System.exit(0); break;
            case ResetScore: info.resetHilights(); break;
            default: 
                if (game.won()) game.newLevel();
                else if (game.lost()) return;
                else game.move(k.getMove());
        }
    }
    
    /** Bring up a modal help dialog. */
    private void showHelpDialog() {
        JDialog helpDialog = new RCHelp(this);
        helpDialog.setVisible(true);
    }

    /** Bring up a modal key dialog. */
    private void showKeyDialog() {
        JDialog keyDialog = new RCKeys(this);
        keyDialog.setVisible(true);
    }

    /** Handle keyReleased events (unused). */
    public void keyReleased(KeyEvent e) {}
    
    /** Handle keyTyped events (unused). */
    public void keyTyped(KeyEvent e) {}
    
    /** Handle componentShown events (unused). */
    public void componentShown(ComponentEvent e) {}
    
    /** Handle componentHidden events (unused). */
    public void componentHidden(ComponentEvent e) {}
    
    /** Handle componentMoved events to update size display. */
    public void componentMoved(ComponentEvent e) {
        int newWidth = RCTile.tileCol(e.getComponent().getWidth());
        int newHeight = RCTile.tileRow(e.getComponent().getHeight());
        info.showSize(newWidth, newHeight);
    }
    
    /** Handle componentResized events. */
    public void componentResized(ComponentEvent e) {
        // determine how many tiles will fit in the resized view
        int newWidth = view.getWidthInTiles();
        int newHeight = view.getHeightInTiles();
        if (newWidth != this.width || newHeight != this.height) {
            width = newWidth;
            height = newHeight;
            view.resizeArray(width, height);
            info.showSize(newWidth, newHeight);
            // remember the new dimensions
            RCPrefs.putWidth(width);
            RCPrefs.putHeight(height);
        }
        // make the frame match
        newWidth = view.getWidthInPixels();
        newHeight = view.getHeightInPixels();
        newWidth += this.getWidth() - view.getWidth();
        newHeight += this.getHeight() - view.getHeight();
        this.setSize(newWidth, newHeight);
    }

}
