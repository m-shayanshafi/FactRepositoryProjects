package Puzzle;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
/*
 * can draw different pieces
 */

public class PieceComponent{
	private int frameX;
	private int frameY;
	final static int NORTH = 1;
	final static int EAST = 2;
	final static int SOUTH = 3;
	final static int WEST = 4;
	private int CELL_SIZE = 125;
	private Piece piece;
	public PieceComponent(int x, int y, Piece a){
		frameX = x;
		frameY = y;
		piece = a;
	}
	public void setLocation(int x, int y){
		frameX = x;
		frameY = y;
	}
	public int getFrameX(){return frameX;}
	public int getFrameY(){return frameY;}
	public Piece getPiece(){return piece;}
	public void setPiece(Piece p){piece = p;}
	/**
	 * create a Graphical representation of a Piece
	 */
	public Area drawPiece(){
		/**
		 * each switch statement makes sides using the 4 int of the private Piece piece
		 * The Area class will allow the conjunction of many shapes into one. In this case
		 * the sides as well as the interior of the piece are created as one Area object
		 * Area objects can be drawn using Graphics objects just like a Rectangle2D.Double
		 * or a Line2D.Double. 
		 * 
		 * For each case, negative signifies inward, positive is outward
		 * 
		 * 1 => triangle
		 * 2 => square
		 * 3 => rhombus
		 * 4 => trapezoid
		 */
		Area shape = new Area();
		int x = frameX;
		int y = frameY;
		//NORTH
		switch(piece.getShape(Side.top)){
		case -1:{
			shape.add(new Area(makeTriangleSide(x,y,-1)));
			break;
		}
		case 1:{
			shape.add(new Area(makeTriangleSide(x,y,1)));
			break;
		}
		case -2:{
			shape.add(new Area(makeSquareSide(x,y,-1)));
			break;
		}
		case 2:{
			shape.add(new Area(makeSquareSide(x,y,1)));
			break;
		}
		case -3:{
			shape.add(new Area(makeRhombusSide(x,y,-1)));
			break;
		}
		case 3:{
			shape.add(new Area(makeRhombusSide(x,y,1)));
			break;
		}
		case -4:{
			shape.add(new Area(makeTrapezoidSide(x,y,-1)));
			break;
		}
		case 4:{
			shape.add(new Area(makeTrapezoidSide(x,y,1)));
			break;
		}
		}
		//EAST
		switch(piece.getShape(Side.right)){
		case -1:{
			shape.add(new Area(makeTriangleSide(y,x+125,-2)));
			break;
		}
		case 1:{
			shape.add(new Area(makeTriangleSide(y,x+125,2)));
			break;
		}
		case -2:{
			shape.add(new Area(makeSquareSide(x+125,y,-2)));
			break;
		}
		case 2:{
			shape.add(new Area(makeSquareSide(x+125,y,2)));
			break;
		}
		case -3:{
			shape.add(new Area(makeRhombusSide(y,x+125,-2)));
			break;
		}
		case 3:{
			shape.add(new Area(makeRhombusSide(y,x+125,2)));
			break;
		}
		case -4:{
			shape.add(new Area(makeTrapezoidSide(x+125,y,-2)));
			break;
		}
		case 4:{
			shape.add(new Area(makeTrapezoidSide(x+125,y,2)));
			break;
		}
		}
		//SOUTH
		switch(piece.getShape(Side.bottom)){
		case -1:{
			shape.add(new Area(makeTriangleSide(x,y+125,-3)));
			break;
		}
		case 1:{
			shape.add(new Area(makeTriangleSide(x,y+125,3)));
			break;
		}
		case -2:{
			shape.add(new Area(makeSquareSide(x,y+125,-3)));
			break;
		}
		case 2:{
			shape.add(new Area(makeSquareSide(x,y+125,3)));
			break;
		}
		case -3:{
			shape.add(new Area(makeRhombusSide(x,y+125,-3)));
			break;
		}
		case 3:{
			shape.add(new Area(makeRhombusSide(x,y+125,3)));
			break;
		}
		case -4:{
			shape.add(new Area(makeTrapezoidSide(x,y+125,-3)));
			break;
		}
		case 4:{
			shape.add(new Area(makeTrapezoidSide(x,y+125,3)));
			break;
		}
		}
		//WEST
		switch(piece.getShape(Side.left)){
		case -1:{
			shape.add(new Area(makeTriangleSide(y,x,-4)));
			break;
		}
		case 1:{
			shape.add(new Area(makeTriangleSide(y,x,4)));
			break;
		}
		case -2:{
			shape.add(new Area(makeSquareSide(x,y,-4)));
			break;
		}
		case 2:{
			shape.add(new Area(makeSquareSide(x,y,4)));
			break;
		}
		case -3:{
			shape.add(new Area(makeRhombusSide(y,x,-4)));
			break;
		}
		case 3:{
			shape.add(new Area(makeRhombusSide(y,x,4)));
			break;
		}
		case -4:{
			shape.add(new Area(makeTrapezoidSide(x,y,-4)));
			break;
		}
		case 4:{
			shape.add(new Area(makeTrapezoidSide(x,y,4)));
			break;
		}
		}
		shape.exclusiveOr(new Area(new Rectangle2D.Double(x,y,125,125)));
		return shape;
	}
	/**
	 * the following accessors and mutators for the Color objects are
	 * used to change the outline and fill color of a piece
	 */
	private Color outline = Color.BLACK;
	private Color fill = Color.MAGENTA;
	public void setOutline(Color c){
		outline = c;
	}
	public void setFill(Color c){
		fill = c;
	}
	public Color getFill(){return fill;}
	public Color getOutline(){return outline;}
	/**
	 * the following methods will draw each side
	 */
	public Shape makeTrapezoidSide(int x,int y, int sideNum){
		Polygon side = new Polygon();
		int posSideNum = Math.abs(sideNum);
		side.addPoint(x, y);
		
		if(posSideNum == 1 || posSideNum == 3){//north and south side
			side.addPoint(x + CELL_SIZE / 5, y);
			if(posSideNum == 1 && sideNum > 0 || posSideNum == 3 && sideNum < 0){//north side out or south side in
				side.addPoint(x + 2 * CELL_SIZE / 5, y - CELL_SIZE / 5);
				side.addPoint(x + 3 * CELL_SIZE / 5, y - CELL_SIZE / 5);
			}
			else{//north side in or south side out
				side.addPoint(x + 2 * CELL_SIZE / 5, y + CELL_SIZE / 5);
				side.addPoint(x + 3 * CELL_SIZE / 5, y + CELL_SIZE / 5);
			}
			side.addPoint(x + 4 * CELL_SIZE / 5, y);
			side.addPoint(x + CELL_SIZE, y);
		}
		else if(posSideNum == 2 || posSideNum == 4){//west and east side
			side.addPoint(x, y + CELL_SIZE / 5);
			if(posSideNum == 2 && sideNum > 0 || posSideNum == 4 && sideNum < 0){
				side.addPoint(x + CELL_SIZE / 5, y + 2 * CELL_SIZE / 5);
				side.addPoint(x + CELL_SIZE / 5, y + 3 * CELL_SIZE / 5);
			}
			else{//east sticks in or west sticks out
				side.addPoint(x - CELL_SIZE / 5, y + 2 * CELL_SIZE / 5);
				side.addPoint(x - CELL_SIZE / 5, y + 3 * CELL_SIZE / 5);
			}
			side.addPoint(x, y + 4 * CELL_SIZE / 5);
			side.addPoint(x, y + CELL_SIZE);
		}
		return side;
	}
	public Shape makeSquareSide(int x, int y, int sideNum){
		int squareSideLength = CELL_SIZE / 4;
		Polygon side = new Polygon();
		side.addPoint(x, y);//side start
		int positiveSideNum = Math.abs(sideNum);
		
		//if the side is the north side or the south side
		if(positiveSideNum  == 1 || positiveSideNum  == 3){
			side.addPoint(x + CELL_SIZE / 3, y);//square bottom left\
			if(positiveSideNum  == 1 && sideNum > 0 || positiveSideNum == 3 && sideNum < 0){
				//north side sticking out or south side sticking in
				side.addPoint(x + CELL_SIZE / 3, y - squareSideLength);//square top left
				side.addPoint(x + 2 * CELL_SIZE / 3, y - squareSideLength);//square top right
			}
			else{//north side sticking in or south side sticking out
				side.addPoint(x + CELL_SIZE / 3, y + squareSideLength);
				side.addPoint(x + 2 * CELL_SIZE / 3, y + squareSideLength);
			}
			side.addPoint(x + 2 * CELL_SIZE / 3, y);
			side.addPoint(x + CELL_SIZE, y);
		}
		//for east or west side
		else if(positiveSideNum  == 2 || positiveSideNum  == 4){
			side.addPoint(x, y + CELL_SIZE / 3);
			//east side sticking out or west side sticking in
			if(positiveSideNum == 2 && sideNum > 0 || positiveSideNum == 4 && sideNum < 0){
				side.addPoint(x + squareSideLength, y + CELL_SIZE / 3);
				side.addPoint(x + squareSideLength, y + 2 * CELL_SIZE / 3);
			}
			//east side sticking in or west side sticking out
			else{
				side.addPoint(x - squareSideLength, y + CELL_SIZE / 3);
				side.addPoint(x - squareSideLength, y + 2 * CELL_SIZE / 3);
			}
			side.addPoint(x, y + 2 * CELL_SIZE / 3);
			side.addPoint(x, y + CELL_SIZE);
		}
		return side; 
	}
	public Shape makeTriangleSide(int x, int y, int sideNum){
		// create an empty Polygon
		Polygon side = new Polygon();
		double height = 10*10 - (11)*(11);
		int NORTH = 1,EAST = 2,SOUTH = 3, WEST = 4;
		// add all the points of the side to the Polygon
		/*
		 * if side is NORTH and faces outward
		 * of if side is SOUTH and faces inward
		 */
		if((Math.abs(sideNum) == NORTH && sideNum > 0)||
				(Math.abs(sideNum) == SOUTH && sideNum < 0)){
			side.addPoint(x,y);
			side.addPoint(x+50,y);
			side.addPoint((int)(x+50+12.5), (int)(y + height));
			side.addPoint(x+50+25,y);
			side.addPoint(x+125,y);
			return side;
		}
		/*
		 * if side is SOUTH and faces outward or
		 * if side is NOTH and faces inward
		 */
		else if((Math.abs(sideNum) == SOUTH && sideNum > 0) ||
				(Math.abs(sideNum) == NORTH && sideNum < 0)) {
			side.addPoint(x, y);
			side.addPoint(x + 50, y);
			side.addPoint((int) (x + 50 + 12.5), (int) (y - height));
			side.addPoint(x + 50 + 25, y);
			side.addPoint(x + 125, y);
			return side;
		}
		/*
		 * if side is EAST and faces outwards
		 * of if side is WEST and faces inwards
		 */
		if((Math.abs(sideNum) == EAST && sideNum > 0)||
				(Math.abs(sideNum)== WEST && sideNum < 0)){
			side.addPoint(y,x);
			side.addPoint(y,x+50);
			side.addPoint((int)(y - height),(int)(x+50+12.5));
			side.addPoint(y,x+50+25);
			side.addPoint(y,x+125);
		}
		/*
		 * if side is WEST and faces outwards or
		 * side is EAST and faces inwards
		 */
		if((Math.abs(sideNum) == WEST && sideNum > 0)||
				(Math.abs(sideNum) == EAST && sideNum < 0)){
			side.addPoint(y,x);
			side.addPoint(y,x+50);
			side.addPoint((int)(y + height),(int)(x+50+12.5));
			side.addPoint(y,x+50+25);
			side.addPoint(y,x+125);
		}
		return side;
	}
/*	public Shape makeRhombusSide(int x, int y, int sideNum){
		Arc2D.Double arc = new Arc2D.Double();
		if ((Math.abs(sideNum) == NORTH && sideNum > 0) ||
				((Math.abs(sideNum) == SOUTH && sideNum < 0))) {
			/**
			 * if side = NORTH and outwards or
			 * side = SOUTH and inwards
			 */
/*			arc = new Arc2D.Double(x+ CELL_SIZE / 5, y, CELL_SIZE/5, CELL_SIZE /5, 0, 180, Arc2D.CHORD);
		
		}
		if (((Math.abs(sideNum) == SOUTH && sideNum > 0) ||
				((Math.abs(sideNum) == NORTH && sideNum < 0)))) {
			/**
			 * if side = SOUTH and outwards or
			 * side = NORTH and inwards
			 */
	/*		arc = new Arc2D.Double(x + CELL_SIZE / 5, y, CELL_SIZE / 5, CELL_SIZE / 5, 180, 360, Arc2D.CHORD);
		}
		if (((Math.abs(sideNum) == EAST && sideNum < 0) ||
				((Math.abs(sideNum) == WEST && sideNum > 0)))) {
			/**
			 * is side = EAST and inwards or
			 * side = WEST and outwards
			 */
/*			arc = new Arc2D.Double(x, y + CELL_SIZE / 5, CELL_SIZE / 5, CELL_SIZE / 5, 270, 90, Arc2D.CHORD);
		}
		if (((Math.abs(sideNum) == EAST && sideNum > 0) || 
				((Math.abs(sideNum) == WEST && sideNum < 0)))) {
			/**
			 * if side = EAST and outwards or
			 * side = WEST and inwards
			 */
/*			arc = new Arc2D.Double(x, y + CELL_SIZE / 5, CELL_SIZE / 5, CELL_SIZE / 5, 90, 270, Arc2D.CHORD);
		}
		return arc;
	}*/
	public Shape makeRhombusSide(int x, int y, int sideNum){
		Polygon side = new Polygon();
		if ((Math.abs(sideNum) == NORTH && sideNum > 0) ||
				((Math.abs(sideNum) == SOUTH && sideNum < 0))) {
			/**
			 * if side = NORTH and outwards or
			 * side = SOUTH and inwards
			 */
			side.addPoint(x, y);
			side.addPoint((int) (x + 47.5 + 15), y);
			side.addPoint((int) (x + 47.5 + 15), (int) (y - 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 30),
					(int) (y - 2 * 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 30), (int) (y - 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 15), y);
			side.addPoint((int) (x + 47.5 + 30 + 47.5), y);
		}
		if (((Math.abs(sideNum) == SOUTH && sideNum > 0) ||
				((Math.abs(sideNum) == NORTH && sideNum < 0)))) {
			/**
			 * if side = SOUTH and outwards or
			 * side = NORTH and inwards
			 */
			side.addPoint(x, y);
			side.addPoint((int) (x + 47.5 + 15), y);
			side.addPoint((int) (x + 47.5 + 15), (int) (y + 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 30),
					(int) (y + 2 * 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 30), (int) (y + 15 * Math.sqrt(3)));
			side.addPoint((int) (x + 47.5 + 15), y);
			side.addPoint((int) (x + 47.5 + 30 + 47.5), y);
		}
		if (((Math.abs(sideNum) == EAST && sideNum < 0) ||
				((Math.abs(sideNum) == WEST && sideNum > 0)))) {
			/**
			 * is side = EAST and inwards or
			 * side = WEST and outwards
			 */
			side.addPoint(y, x);
			side.addPoint(y, (int) (x + 47.5 + 15));
			side.addPoint((int) (y - 15 * Math.sqrt(3)), (int) (x + 47.5 + 15));
			side.addPoint((int) (y - 2 * 15 * Math.sqrt(3)),
					(int) (x + 47.5 + 30));
			side.addPoint((int) (y - 15 * Math.sqrt(3)), (int) (x + 47.5 + 30));
			side.addPoint(y, (int) (x + 47.5 + 15));
			side.addPoint(y, (int) (x + 47.5 + 30 + 47.5));
		}
		if (((Math.abs(sideNum) == EAST && sideNum > 0) || 
				((Math.abs(sideNum) == WEST && sideNum < 0)))) {
			/**
			 * if side = EAST and outwards or
			 * side = WEST and inwards
			 */
			side.addPoint(y, x);
			side.addPoint(y, (int) (x + 47.5 + 15));
			side.addPoint((int) (y + 15 * Math.sqrt(3)), (int) (x + 47.5 + 15));
			side.addPoint((int) (y + 2 * 15 * Math.sqrt(3)),
					(int) (x + 47.5 + 30));
			side.addPoint((int) (y + 15 * Math.sqrt(3)), (int) (x + 47.5 + 30));
			side.addPoint(y, (int) (x + 47.5 + 15));
			side.addPoint(y, (int) (x + 47.5 + 30 + 47.5));
		}
		return side;
	}
	public boolean contains(int x, int y){
		int a = piece.getHomeColumn()*125 +100;
		int b = piece.getHomeRow()*125 + 100;
		Rectangle2D.Double test = new Rectangle2D.Double(a,b,125,125);
		return test.contains(x,y);
	}
	public boolean equals(PieceComponent c){
		return (c.getFrameX() == frameX) && (c.getFrameY() == frameY);
	}
}