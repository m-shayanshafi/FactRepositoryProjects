package org.gcs.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


/**
 * The RCTile class handles drawing individual tiles on behalf of RCView.
 * Together, tileWidth and tileHeight determine the aspect ratio of the tiles.
 * RCTile also exports static conversion methods for tile geometry.
 *
 * @see RCView
 * @see RCImage
 * @author John B. Matthews
 */
public class RCTile {

    /** The width of a tile. */
    public static final int tileWidth = 32;

    /** The height of a tile. */
    public static final int tileHeight = 32;

    private Image currentImage = RCImage.Blank;
    private RCView panel;
    private int row, col;
    private boolean hilite = false;
    private Color hiliteColor = new Color(255, 255, 0);

    /**
     * Construct a tile for the given row & column on a panel.
     *
     * @param panel this tile's parent panel
     * @param row this tile's row
     * @param col this tile's column
     */
    public RCTile(RCView panel, int row, int col) {
        this.panel = panel;
        this.row = row;
        this.col = col;
    }

    /** Convert an x pixel coordinate into a tile column number. */
    public static int tileCol(int x) { return x / (tileWidth + 1); }

    /** Convert a y pixel coordinate into a tile row number. */
    public static int tileRow(int y) { return y / (tileHeight + 1); }

    /** Convert a tile column number into an x pixel coordinate. */
    public static int pixelX(int col) { return col * (tileWidth + 1); }

    /** Convert a tile row number into a y pixel coordinate. */
    public static int pixelY(int row) { return row * (tileHeight + 1); }

     /** Tell this tile what image it will draw. */
    public void setImg(Image image) {
        currentImage = image;
    }

    public void setHilite(boolean hilite) {
        this.hilite = hilite;
    }

    public void repaint() {
       panel.repaint(
           col * (tileWidth + 1),
           row * (tileHeight + 1),
           tileWidth,
           tileHeight);
    }

    /** Ask this tile to draw it's current image. */
    public void paint(Graphics g) {
        int dx1 = col * (tileWidth + 1);
        int dy1 = row * (tileHeight + 1);
        int dx2 = dx1 + tileWidth;
        int dy2 = dy1 + tileHeight;
        int sx1 = 0;
        int sy1 = 0;
        int sx2 = currentImage.getWidth(panel);
        int sy2 = currentImage.getHeight(panel);
        g.drawImage(
            currentImage,
            dx1, dy1, dx2, dy2,
            sx1, sy1, sx2, sy2,
            panel);
        if (hilite) {
            g.setColor(hiliteColor);
            g.drawRect(dx1, dy1, tileWidth - 1, tileHeight - 1);
        }
    }

}
