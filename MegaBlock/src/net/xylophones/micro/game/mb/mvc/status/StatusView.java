/*
 * StatusView.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.mvc.status;

import net.xylophones.micro.game.mb.Block;

import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;

import net.xylophones.micro.ui.effects.imagestring.ImageStringDrawer;

/**
 *
 * @author william@xylophones.net
 */
public class StatusView {
    
    private int x;
    
    private int y;
    
    /**
     * The width in pixels of one cell
     */
    private int cellSize;
    
    /**
     * The block that the user is currently controlling
     */
    private TiledLayer block;
    
    private StatusModel model;
    
    private Font font = null;
    
    private int color = 0x0000FF;
    
    private ImageStringDrawer imageString;

    private String levelImageString = "/level.png";
     
    private Image levelImage = null;
    
    private String scoreImageString = "/score.png";
     
    private Image scoreImage = null;
    
    private String highScoreImageString = "/highscore.png";
    
    private Image highScoreImage = null;
    
    private String nextImageString = "/next.png";
    
    private Image linesImage = null;
    
    private String linesImageString = "/lines.png";
    
    private Image nextImage = null;
    
    private int stringImageHeight = 0;
    
    private int verticalPadding = 4;
    
    private int horizontalPadding = 2;

    /** 
     * Creates a new instance of BoardView 
     */
    public StatusView (StatusModel model, Image tiles) {
        this.model = model;

        cellSize = tiles.getHeight();

        block = new TiledLayer(4, 4, tiles, cellSize, cellSize);
        
        initImages();
    }
    
    private void initImages() {
        try {
            levelImage = Image.createImage( levelImageString );
            scoreImage = Image.createImage( scoreImageString );
            highScoreImage = Image.createImage( highScoreImageString );
            nextImage = Image.createImage( nextImageString );
            linesImage = Image.createImage( linesImageString );
            
            imageString = new ImageStringDrawer(
                "/*.png", new char[] {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
                }
            );

            stringImageHeight = imageString.getHeight("1");
            
        } catch (Exception e) {
            System.err.println("Error loading images");
        }
    }
    
    /**
     * Set the x,y pixel position of the view
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        
        this.y = y;
    }
    
    public void refresh() {
        updateBlockLayer();
    }
    
    /**
     * Get the current layer to represent the current tetrad and rotation
     *
     * @todo = move this into Block
     */
    private void updateBlockLayer() {
        boolean[][] shape = Block.getShape( model.getNextTetrad(), Block.ROTATION_0 );
        
        int tileIndex = model.getNextTetrad() + 1; //tetradImageIndexes[model.getTetrad()];
        
        for (int i=0 ; i<4 ; i++) {
            for (int j=0 ; j<4 ; j++) {
                if (shape[i][j]) {
                    block.setCell(i, j, tileIndex);
                } else {
                    block.setCell(i, j, 0);
                }
            }
        }
    }

    /**
     * @param Graphics g
     */
    public void paint(Graphics g) {
        refresh();
        
        if (font == null) {
            font = g.getFont();
        }
        g.setColor(color);

        int x = this.x + horizontalPadding;
        int y = this.y + verticalPadding;
    
        g.drawImage(scoreImage, x, y, Graphics.TOP|Graphics.LEFT);

        y += scoreImage.getHeight();
        y += verticalPadding;

        try {
            imageString.draw(g, model.getScore(), x, y, ImageStringDrawer.TOP|ImageStringDrawer.LEFT);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        
        y += stringImageHeight;
        y += verticalPadding;
        
        g.drawImage(levelImage, x, y, Graphics.TOP|Graphics.LEFT);
        
        y += levelImage.getHeight();
        y += verticalPadding;
        
        try {
            imageString.draw(g, model.getLevel(), x, y, ImageStringDrawer.TOP|ImageStringDrawer.LEFT);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        
        y += stringImageHeight;
        y += verticalPadding;
        
        g.drawImage(linesImage, x, y, Graphics.TOP|Graphics.LEFT);
        
        y += linesImage.getHeight();
        y += verticalPadding;
        
        try {
            imageString.draw(g, model.getTotalLines(), x, y, ImageStringDrawer.TOP|ImageStringDrawer.LEFT);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        
        y += stringImageHeight;
        y += verticalPadding;
                
        g.drawImage(highScoreImage, x, y, Graphics.TOP|Graphics.LEFT);
        
        y += highScoreImage.getHeight();
        y += verticalPadding;
        
        int highScore = model.getHighScore();
        
        if ( model.getScore() > model.getHighScore() ) {
            highScore = model.getScore();
        }

        try {
            imageString.draw(g, highScore, x, y, ImageStringDrawer.TOP|ImageStringDrawer.LEFT);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        
        y += stringImageHeight;
        y += verticalPadding;
        
        g.drawImage(nextImage, x, y, Graphics.TOP|Graphics.LEFT);
        
        y += nextImage.getHeight();
        y += verticalPadding;

        block.setPosition(x, y);
        block.paint(g);
    }
}
