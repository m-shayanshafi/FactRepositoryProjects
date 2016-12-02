package kw.chinesechess.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import kw.chinesechess.board.ChineseChess;
import kw.chinesechess.engine.GraphTransformer;
import kw.chinesechess.model.piece_Img;

public class MouseAdapterImpl extends MouseAdapter {
	 
	private ChineseChess m_c;
	Logger logger = Logger.getLogger("MouseAdapterImpl");
	
	public MouseAdapterImpl(ChineseChess c) {
		m_c = c;
	}
	
	  public void mouseClicked(MouseEvent e) {
	    	int x = e.getX();
        	int y = e.getY();
        	
        	piece_Img thisPiece = m_c.getPiece(x, y);
        	if(thisPiece != null && !m_c.isPendingToMoveTheSelectedPiece()) {
        		m_c.selectThePiece(thisPiece);
        	} else {
        		boolean hasMoved = m_c.moveThePiece(GraphTransformer.getReverseColFromX(x), GraphTransformer.getReverseRowFromY(y));
        		
        		if(hasMoved) {
        			m_c.computerMove();
        		}
        		m_c.resetCurrentPiece();
        	}
        	logger.info(thisPiece + " is selected.");
        	
        	 //repaint();
   
	  }
	}
