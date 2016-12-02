package kw.chinesechess.board;

import java.awt.*;
import java.util.*;

import kw.chinesechess.model.CopyAble;
import kw.chinesechess.model.MoveFromTo;
import kw.chinesechess.model.myList;
import kw.chinesechess.model.piece;

public final class ChessBoard<T extends CopyAble>
{
    private myList<T> blkPieces, redPieces;    
    protected piece board[][]=new piece[9][10]; 
    
    private int movingCnt;

    final static int CARRIAGE=0, HORSE=1, PAO=2, ELEPHANT=3, GUARD=4, SOLDIER=5, 
                     KING=6;
    
    public ChessBoard(myList<T> blkPieces, myList<T> redPieces)    
    {
        this.blkPieces=blkPieces;   
        this.redPieces=redPieces;
        
        piece pc=null;
        Iterator<CopyAble> it = (Iterator<CopyAble>) blkPieces.iterator();
        while(it.hasNext())
        {
            pc=(piece)it.next();
            board[pc.getCol()][pc.getRow()]=pc;
        }
        
        it = (Iterator<CopyAble>) redPieces.iterator();
        while(it.hasNext())
        {
            pc=(piece)it.next();
            board[pc.getCol()][pc.getRow()]=pc;
        }
    }
    
    public void initAgain()
    {
        piece pc=null;
        for(int c=0; c<9; c++)
            for(int r=0; r<10; r++)
                board[c][r]=null; 
        
        Iterator<CopyAble> it = (Iterator<CopyAble>) blkPieces.iterator();
        while(it.hasNext())
        {
            pc=(piece)it.next();
            board[pc.getCol()][pc.getRow()]=pc;
        }
        
        
        it = (Iterator<CopyAble>) redPieces.iterator();
        while(it.hasNext())
        {
            pc=(piece)it.next();
            board[pc.getCol()][pc.getRow()]=pc;
        }
    }
    
    public boolean isValidMove(MoveFromTo mv) {
    	int toCol = mv.getToCol();
    	int toRow = mv.getToRow();
    	piece movingFrom = board[mv.getFromCol()][mv.getFromRow()];
    	return isValidMove(movingFrom, toCol, toRow);
    }
    
    public boolean isValidMove(piece movingFrom, int toCol, int toRow)
    {   
        if(toCol>8 || toCol<0 || toRow>9 || toRow<0)
            return false;
        piece tmp=board[toCol][toRow];
        if(toCol==movingFrom.getCol() && toRow==movingFrom.getRow())
            return false;   // the piece haven't moved at all
        if(tmp!=null && tmp.isBlk()==movingFrom.isBlk())
            return false;   // you can't kill yourself
        switch(movingFrom.getName())
        {
            case CARRIAGE:
            if(toCol==movingFrom.getCol())
            {
                for(int row=Math.min(toRow, movingFrom.getRow())+1;
                        row<Math.max(toRow, movingFrom.getRow()); row++)
                    if(board[toCol][row]!=null)
                        return false;
                return true;
            }
            else if(toRow==movingFrom.getRow()) 
            {
                for(int col=Math.min(toCol, movingFrom.getCol())+1;
                        col<Math.max(toCol, movingFrom.getCol()); col++)
                    if(board[col][toRow]!=null)
                        return false;
                return true;
            }
            else
                return false;

            case HORSE:
            {
                int cols=toCol-movingFrom.getCol();
                int rows=toRow-movingFrom.getRow();
                return((Math.abs(cols)==2 && Math.abs(rows)==1 && 
                            board[movingFrom.getCol()+cols/2][movingFrom.getRow()]==null) || 
                       (Math.abs(rows)==2 && Math.abs(cols)==1 &&
                            board[movingFrom.getCol()][movingFrom.getRow()+rows/2]==null)); 
            }
            
            case PAO:
            {
                int cnt=0;
                if(toCol!=movingFrom.getCol() && toRow!=movingFrom.getRow())
                    return false;
                if(toCol==movingFrom.getCol())
                {
                    for(int row=Math.min(toRow, movingFrom.getRow())+1;
                            row<Math.max(toRow, movingFrom.getRow()); row++)
                        if(board[toCol][row]!=null)
                            cnt++;
                }
                else 
                {
                    for(int col=Math.min(toCol, movingFrom.getCol())+1;
                            col<Math.max(toCol, movingFrom.getCol()); col++)
                        if(board[col][toRow]!=null)
                            cnt++;
                }
                return((cnt==0 && tmp==null) || 
                       (cnt==1 && tmp!=null && tmp.isBlk()!=movingFrom.isBlk()));
            } 
            
            case ELEPHANT:
            {
                int cols=toCol-movingFrom.getCol();
                int rows=toRow-movingFrom.getRow();
                return(Math.abs(cols)==2 &&
                       Math.abs(rows)==2 &&
                       board[movingFrom.getCol()+cols/2][movingFrom.getRow()+rows/2]==null &&
                       ((movingFrom.isBlk() && toRow>=5) ||
                        (!movingFrom.isBlk() && toRow<=4)));
            }
            
            case GUARD:
            return(Math.abs(toCol-movingFrom.getCol())==1 &&
                   Math.abs(toRow-movingFrom.getRow())==1 &&
                   toCol>=3 &&
                   toCol<=5 &&
                   ((movingFrom.isBlk() && toRow>=7) ||
                    (!movingFrom.isBlk() && toRow<=2)));
            
            case SOLDIER:
            if(movingFrom.isBlk())
            {
                if(toCol==movingFrom.getCol() && toRow==movingFrom.getRow()-1)
                    return true;
                if(toRow<=4)
                    return(toRow==movingFrom.getRow() && 
                           Math.abs(toCol-movingFrom.getCol())==1);
                return false;
            }
            else
            {
                if(toCol==movingFrom.getCol() && toRow==movingFrom.getRow()+1)
                    return true;
                if(toRow>=5)
                    return(toRow==movingFrom.getRow() && 
                           Math.abs(toCol-movingFrom.getCol())==1);
                return false;
            }
            
            default: //KING
            return(Math.abs(toCol-movingFrom.getCol())+
                   Math.abs(toRow-movingFrom.getRow())==1 &&
                   toCol>=3 &&
                   toCol<=5 &&
                   ((movingFrom.isBlk() && toRow>=7) ||
                    (!movingFrom.isBlk() && toRow<=2)));
            //else throw a Excetion()
        }
    }       

    public myList<CopyAble> findValidMoves(piece pc)
    {
        myList<CopyAble> validMoves=new myList<CopyAble>();
        piece tmp=null;
        switch(pc.getName())
        {
            case CARRIAGE:
            for(int col=pc.getCol()+1; col<=8; col++)
            {
                if((tmp=board[col][pc.getRow()])!=null)
                {
                    if(tmp.isBlk()!=pc.isBlk())
                        validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), col, pc.getRow()));
                    break;
                }
                validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), col, pc.getRow()));
            }
            for(int col=pc.getCol()-1; col>=0; col--)
            {
                if((tmp=board[col][pc.getRow()])!=null)
                {
                    if(tmp.isBlk()!=pc.isBlk())
                        validMoves.addElement
                                    (new MoveFromTo(pc.getCol(), pc.getRow(), col, pc.getRow()));
                    break;
                }
                validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), col, pc.getRow()));            
            }
            for(int row=pc.getRow()+1; row<=9; row++)
            {
                if((tmp=board[pc.getCol()][row])!=null)
                {
                    if(tmp.isBlk()!=pc.isBlk())
                        validMoves.addElement
                                    (new MoveFromTo(pc.getCol(), pc.getRow(), pc.getCol(), row));
                    break;
                }
                validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), pc.getCol(), row));
            }
            for(int row=pc.getRow()-1; row>=0; row--)
            {
                if((tmp=board[pc.getCol()][row])!=null)
                {
                    if(tmp.isBlk()!=pc.isBlk())
                        validMoves.addElement
                                    (new MoveFromTo(pc.getCol(), pc.getRow(), pc.getCol(), row));
                    break;
                }
                validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), pc.getCol(), row));
            }
            break;
            
            case PAO:
            for(int col=0; col<=8; col++)
            {
                if(col==pc.getCol())
                    continue;
                if(isValidMove(pc, col, pc.getRow()))
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), col, pc.getRow()));
            }
            for(int row=0; row<=9; row++)
            {
                if(row==pc.getRow())
                    continue;
                if(isValidMove(pc, pc.getCol(), row))
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), pc.getCol(), row));
            }
            break;
            
            case HORSE:
            for(int col=-2; col<=2; col+=4)
                for(int row=-1; row<=1; row+=2)
                    if(isValidMove(pc, pc.getCol()+col, pc.getRow()+row))
                        validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()+row));
            for(int col=-1; col<=1; col+=2)
                for(int row=-2; row<=2; row+=4)
                    if(isValidMove(pc, pc.getCol()+col, pc.getRow()+row))
                        validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()+row));
            break;

            case ELEPHANT:
            for(int col=-2; col<=2; col+=4)
                for(int row=-2; row<=2; row+=4)
                    if(isValidMove(pc, pc.getCol()+col, pc.getRow()+row))
                        validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()+row));
            break;
            
            case GUARD:
            for(int col=-1; col<=1; col+=2)
                for(int row=-1; row<=1; row+=2)
                    if(isValidMove(pc, pc.getCol()+col, pc.getRow()+row))
                        validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()+row));            
            break;

            case SOLDIER:
            if(pc.isBlk()) {
                if(isValidMove(pc, pc.getCol(), pc.getRow()-1)) {
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol(), pc.getRow()-1));
                }
            } else {
                if(isValidMove(pc, pc.getCol(), pc.getRow()+1)) {
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol(), pc.getRow()+1));
                }
            }
            for(int col=-1; col<=1; col+=2)
                if(isValidMove(pc, pc.getCol()+col, pc.getRow()))
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()));            
            break;
            
            default: // KING
            for(int col=-1; col<=1; col+=2)
                if(isValidMove(pc, pc.getCol()+col, pc.getRow()))
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol()+col, pc.getRow()));            
            for(int row=-1; row<=1; row+=2)
                if(isValidMove(pc, pc.getCol(), pc.getRow()+row))
                    validMoves.addElement(new MoveFromTo(pc.getCol(), pc.getRow(), 
                                                            pc.getCol(), pc.getRow()+row));            
        } 
        return validMoves;
    }

    public int evaluationNmove(MoveFromTo aMove, boolean doMove)
    {
        piece pc=board[aMove.getToCol()][aMove.getToRow()];
        piece pcO=board[aMove.getFromCol()][aMove.getFromRow()];
        int mark=0;
        int factor=pcO.isBlk()? -1: 1;

        switch(pcO.getName()) // move a own piece and earn some mark
        {
            case CARRIAGE:
            break;
            case PAO:
            if(aMove.getToCol()==4 && aMove.getFromCol()!=4)  // PAO come to the central col
            {
                pcO.setValue(pcO.getValue() + 20);
                mark+=20;
            }
            else if(aMove.getFromCol()==4 && aMove.getToCol()!=4)
            {
                pcO.setValue(pcO.getValue() - 20);
                mark-=20;
            }
            break;
            case HORSE:
            if(movingCnt<7 && aMove.getFromRow()==9 && aMove.getToRow()==7)
            {
                pcO.setValue(pcO.getValue() + 9*factor*(aMove.getToCol()-aMove.getFromCol()));
                mark+=9*factor*(aMove.getToCol()-pcO.getCol());
            }
            break;
            case GUARD:
            break;
            case SOLDIER:
            if((pcO.isBlk() && aMove.getToCol()==0) && (!pcO.isBlk() && aMove.getToCol()==9))
                {
                    pcO.setValue(pcO.getValue() - 8);
                    mark-=8;
                }
                else
                {
                    pcO.setValue(pcO.getValue() + 3*Math.abs(aMove.getToCol()-aMove.getFromCol()));
                    mark+=3*Math.abs(aMove.getToCol()-aMove.getFromCol());
                }
            break;
            case ELEPHANT:
            break;
            default:    // case KING:           
        }  
        if(pc!=null)    // kill an opposite piece and earn some mark
        {
            mark+=pc.getValue();
            if(doMove)
            {
                if(pc.isBlk())
                    blkPieces.removeElement((T) pc);
                else
                    redPieces.removeElement((T) pc);
            }
        }
        if(doMove)
        {
            board[aMove.getFromCol()][aMove.getFromRow()]=null;
            board[aMove.getToCol()][aMove.getToRow()]=pcO;
            pcO.setCol(aMove.getToCol());
            pcO.setRow(aMove.getToRow());
        }
        return mark;
    }
            
    public piece getPieceRef(int col, int row)
    {
        return board[col][row];
    }
    
    public void incMovingCnt()
    {
        movingCnt++;
    }
    

}




