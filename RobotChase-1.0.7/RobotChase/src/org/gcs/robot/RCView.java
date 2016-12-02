package org.gcs.robot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import static java.lang.Math.*;
import static org.gcs.robot.RCModel.*;
import static org.gcs.robot.RCTile.*;

/**
 * The RCView class is the main view of the game board.
 * It observes the game model and determines what to draw on each square,
 * delegating the actual drawing to {@link org.gcs.robot.RCTile RCTile}.
 * The view also implements MouseListener and MouseMotionListener, as
 * an alternative to keyboard input. If animation is enabled, a timer
 * moves the player as far as possible at a rate determined by DELAY.
 *
 * @see RCModel
 * @see RCTile
 * @serial exclude
 * @author John B. Matthews
 */
public class RCView extends JPanel
    implements ActionListener, MouseListener, MouseMotionListener, Observer {

    private static final int DELAY = 1000 / 5; // 5 Hz.
    private RCModel game;
    private int [][] board;
    private int width;
    private int height;
    private RCTile [][] tiles;
    private int mouseRow;
    private int mouseCol;
    private Cursor gameCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor sysCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private RCImage imageSet = RCImage.getSet();
    private Color c = RCImage.borderColor;
    private Timer timer;
    private boolean isAnimated = RCPrefs.getAnimated();

    /**
     * Construct a view of the specified game board.
     *
     * @param game a game board
     */
    public RCView(RCModel game) {
        this.game = game;
        newView(this.game);
        this.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, c));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setCursor(gameCursor);
        this.game.addObserver(this);
        timer = new Timer(DELAY, this);
    }

    /**
     * Resize the game and view. Resize the game board,
     * create a conformal view and initialize it.
     * 
     * @param width board width measured in tiles
     * @param height board height measured in tiles
     */
    public void resizeArray(int width, int height) {
        game.resizeArray(width, height);
        newView(this.game);
        game.initLevel();
    }

    private void newView(RCModel game) {
        this.board = game.getBoard();
        this.width = this.board[0].length;
        this.height = this.board.length;
        tiles = new RCTile [height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new RCTile(this, i, j);
            }
        }
    }

    /** Return this panel's preferred size based on tile geometry. */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(pixelX(width), pixelY(height));
    }

    /** Return the view width in tiles. */
    public int getWidthInTiles() { return max(MINTILE, tileCol(getWidth())); }

    /** Return the view height in tiles. */
    public int getHeightInTiles() { return max(MINTILE, tileRow(getHeight())); }

    /** Return the view width in pixels. */
    public int getWidthInPixels() { return pixelX(width); }

    /** Return the view height in pixels. */
    public int getHeightInPixels() { return pixelY(height); }

   /** Determine what to draw on each tile, then repaint. */
    public void update(Observable model, Object arg) {
        for (int i = 0; i < height; i++) 
            for (int j = 0; j < width; j++) {
                tiles[i][j].setImg(select(board[i][j]));
            }
        Point p = game.getPosition();
        if (game.lost()) tiles[p.y][p.x].setImg(RCImage.Dead);
        else tiles[p.y][p.x].setImg(RCImage.Me);
        this.repaint();
    }

    // Return the image correspoding to this board position.
    private Image select(int token) {
        switch(token) {
            case POST:  return RCImage.Post;
            case BOMB:  return RCImage.Bomb;
            case EMPTY: return RCImage.Blank;
            case ROBOT: return RCImage.Robot;
            default:    return RCImage.Wreck;
        }
    }

    /** Display another set of game tiles. */
    public void nextSet() {
        imageSet = RCImage.getNextSet();
        update(game, null);
    }

    /**  Draw a grid and tell each tile to repaint itself. */
    @Override
    public void paintComponent(Graphics g) {
        drawGrid(g);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles [i][j].paint(g);
            }
        }
    }

    // draw a dark gray grid between tiles
    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        int x = tileWidth;
        int y = getHeight();
        for (int i = 1; i < width ; i++) {
            g.drawLine(x, 0, x, y);
            x += tileWidth + 1;
        }
        x = getWidth();
        y = tileHeight;
        for (int i = 1; i < height ; i++) {
            g.drawLine(0, y, x, y);
            y += tileHeight + 1;
        }
    }

    /** Handle mouseClicked events (unused). */
    public void mouseClicked(MouseEvent e) {}

    /** Handle mouseEntered events to set the cursor. */
    public void mouseEntered(MouseEvent e) { setCursor(gameCursor); }

    /** Handle mouseExited events to restore the cursor. */
    public void mouseExited(MouseEvent e) { setCursor(sysCursor); }

    /** Handle mousePressed events. */
    public void mousePressed(MouseEvent e) {
        mouseRow = pinRow(e);
        mouseCol = pinCol(e);
        setHilite(mouseRow, mouseCol, true);
    }

    /** Handle mouseReleased events. */
    public void mouseReleased(MouseEvent e) {
        setHilite(mouseRow, mouseCol, false);
        if (game.lost()) game.resetGame();
        else if (game.won()) game.newLevel();
        else if (isJump(e)) game.move();
        else if (isAnimated) timer.start();
        else game.move(mouseRow, mouseCol);
    }

    /** Handle mouseMoved events (unused). */
    public void mouseMoved(MouseEvent e) {}

    /** Handle mouseDragged events. */
    public void mouseDragged(MouseEvent e) {
        setHilite(mouseRow, mouseCol, false);
        mouseRow = pinRow(e);
        mouseCol = pinCol(e);
        setHilite(mouseRow, mouseCol, true);
    }

    // Set or clear the hilite state of a tile and repaint it
    private void setHilite(int row, int col, boolean setting) {
        tiles[row][col].setHilite(setting);
        tiles[row][col].repaint();
    }

    // Return a valid tile row from MouseEvent
    private int pinRow(MouseEvent e) {
        int row = tileRow(e.getY());
        if (row < 0) return 0;
        else if (row >= height) return height - 1;
        else return row;
    }

    // Return a valid tile column from MouseEvent
    private int pinCol(MouseEvent e) {
        int col = tileCol(e.getX());
        if (col < 0) return 0;
        else if (col >= width) return width - 1;
        else return col;
    }

    // Return true if jumping
    private boolean isJump(MouseEvent e) {
        return e.isControlDown() ||
            SwingUtilities.isRightMouseButton(e);
    }

    /** Animate one move; stop when no more moves or game won. */
    public void actionPerformed(ActionEvent e) {
        if (!game.move(mouseRow, mouseCol) || game.won()) timer.stop();
    }

    /** Toggle the animation feature. */
    public void toggleAnimated() {
        this.isAnimated = !isAnimated;
        RCPrefs.putAnimated(this.isAnimated);
    }

}
