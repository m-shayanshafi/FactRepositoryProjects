package kw.chinesechess.model;
import java.awt.*;
import java.util.*;

import kw.chinesechess.config.AppConfig;
import kw.chinesechess.engine.GraphTransformer;

public final class piece_Img extends piece
{
    private int x;
	private int y;
    private boolean visible=true;
    private Image img;

    
    public piece_Img(int n, int c, int r, Image i, boolean isB, int v, boolean f)
    {
        setCol(c);
        setRow(r);
        setName(n);
        setImg(i);
        setBlk(isB);
        setValue(v);
        focusOn=f;
        setX(colToX(getCol()));
        setY(rowToY(getRow()));
    }
    
    public piece_Img() {}

    public void setXY()
    {
        setX(colToX(getCol()));
        setY(rowToY(getRow()));
    }
    
    public void setXY(int x, int y)
    {
        this.setX(x);
        this.setY(y);
    }

    public Image getImage()
    {
        return getImg();
    }
    
    public void copyFrom(piece_Img pc)
    {
        super.copyFrom(pc);
        setImg(pc.getImg());
        setVisible(pc.isVisible());
        setX(pc.getX());
        setY(pc.getY());
    }

    public int colToX(int col)
    {
        return ((col+1)*AppConfig.xFACTOR) - (AppConfig.scaleFACTOR - col);  
    }

    public int rowToY(int row)
    {
        return (9-row+1)*AppConfig.yFACTOR + AppConfig.scaleFACTOR + (AppConfig.scaleFACTOR - row) - row;
    }

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the img
	 */
	public Image getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(Image img) {
		this.img = img;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x - AppConfig.xSPACE;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y  - AppConfig.ySPACE;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isThisPieceGivenByXAndY(int x, int y) {
		return (GraphTransformer.getReverseColFromX(x) == this.getCol() && GraphTransformer.getReverseRowFromY(y) == this.getRow());
	}
	
    
    
    public String toString() {
    	return "Piece col:"+this.getCol()+" row:"+this.getRow()+" at x:"+this.getX()+ " y:"+this.getY();
    }

}