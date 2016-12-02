package kw.chinesechess.model;
public final class MoveFromTo implements CopyAble   
{
    private int fromCol;
    private int fromRow;
    private int toCol;
    private int toRow;
    
    public MoveFromTo(){}
    
    public MoveFromTo(int fromC, int fromR, int toC, int toR)
    {
        setFromCol(fromC);
        setFromRow(fromR);
        setToCol(toC);
        setToRow(toR);
    }
    
	@Override
	public void copyFrom(CopyAble f) {
		MoveFromTo mv = (MoveFromTo) f;
		   setFromCol(mv.getFromCol());
	        setFromRow(mv.getFromRow());
	        setToCol(mv.getToCol());
	        setToRow(mv.getToRow());
	    
	}
    

	/**
	 * @return the toCol
	 */
	public int getToCol() {
		return toCol;
	}

	/**
	 * @param toCol the toCol to set
	 */
	public void setToCol(int toCol) {
		this.toCol = toCol;
	}

	/**
	 * @return the fromCol
	 */
	public int getFromCol() {
		return fromCol;
	}

	/**
	 * @param fromCol the fromCol to set
	 */
	public void setFromCol(int fromCol) {
		this.fromCol = fromCol;
	}

	/**
	 * @return the fromRow
	 */
	public int getFromRow() {
		return fromRow;
	}

	/**
	 * @param fromRow the fromRow to set
	 */
	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}

	/**
	 * @return the toRow
	 */
	public int getToRow() {
		return toRow;
	}

	/**
	 * @param toRow the toRow to set
	 */
	public void setToRow(int toRow) {
		this.toRow = toRow;
	}
	
	public String toString () {
		String r = "fromCol: " + fromCol + ", fromRow: " + fromRow + ", toCol: " + toCol + ", toRow: " + toRow; 
		return r;
	}


}