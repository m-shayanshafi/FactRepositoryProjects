package kw.chinesechess.model;
import java.awt.*;
import java.util.*;

public class piece implements CopyAble
{
    private int col;
	private int row;
    private int name;
    private boolean isBlk;
    private int value;
    protected boolean focusOn;
    
    public piece() {}
    
    public piece(int name, int col, int row, boolean isBlk, int value, boolean focusOn)
    {
        this.setCol(col);
        this.setRow(row);
        this.setName(name);
        this.setBlk(isBlk);
        this.setValue(value);
        this.focusOn=focusOn;
    }
        
	@Override
	public void copyFrom(CopyAble f) {
		piece pc = (piece)f;
        this.setCol(pc.getCol());
        this.setRow(pc.getRow());
        this.setName(pc.getName());
        this.setBlk(pc.isBlk());
        this.setValue(pc.getValue());
        this.focusOn=pc.focusOn;	
	}    

	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * @param col the col to set
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the isBlk
	 */
	public boolean isBlk() {
		return isBlk;
	}

	/**
	 * @param isBlk the isBlk to set
	 */
	public void setBlk(boolean isBlk) {
		this.isBlk = isBlk;
	}

	/**
	 * @return the name
	 */
	public int getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(int name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}



	
}