package ChessGameKenai;

import java.util.ArrayList;

import ChessGameKenai.model.PieceInterface;

public class NonVisualPieceIterator implements IPieceIterator{
	ArrayList<NonVisualPiece> _pieces;
	int position;
	public NonVisualPieceIterator(ArrayList<NonVisualPiece> pieces){
		this._pieces = pieces;
		position = 0;
	}
	
	public boolean hasNext(){
		if(position >= _pieces.size())
			return false;
		
		return true;
	}
	
	public NonVisualPiece getNext(){
		NonVisualPiece nvp = _pieces.get(position);
		position++;
		return nvp;
	}
}
