package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.ChineseChecker;
import model.Location;
import model.ResetNotification;
import model.SelectedNotification;
import model.ValueChangedNotification;
import model.WinNotification;
import controller.CCListener;

public class CCBoard extends Board {

	private static final long serialVersionUID = 1L;
	public static final int R = 9;
	public static final int MARGIN = 14;
	private int top_x;// x of top hole
	private int top_y;// y of top hole
	private int playerFlag;

	/**
	 * Initialize the bounds for each rows
	 * There are 17 arrays in the first array, which means the rows (from 1-17)
	 * There are 4 integers in the second array, the first 2 integers indicate 
	 * the the first and the last x-location, and the last 2 integers indicate 
	 * the x and y coordinate for the first point position.
	 */
	private int[][] bounds = { { 12, 12, 0, 0 },// y=0
			{ 11, 12, 0, 0 },// y=1
			{ 10, 12, 0, 0 },// y=2
			{ 9, 12, 0, 0 },// y=3
			{ 4, 16, 0, 0 },// y=4
			{ 4, 15, 0, 0 },// y=5
			{ 4, 14, 0, 0 },// y=6
			{ 4, 13, 0, 0 },// y=7
			{ 4, 12, 0, 0 },// y=8
			{ 3, 12, 0, 0 },// y=9
			{ 2, 12, 0, 0 },// y=10
			{ 1, 12, 0, 0 },// y=11
			{ 0, 12, 0, 0 },// y=12
			{ 4, 7, 0, 0 },// y=13
			{ 4, 6, 0, 0 },// y=14
			{ 4, 5, 0, 0 },// y=15
			{ 4, 4, 0, 0 },// y=16
	};

	public CCBoard(boolean mode) {
		model = new ChineseChecker();
		model.init(mode);
		model.addObserver(this);

		// this.ui = ui;
		// setR(ui.getR());
		// setMARGIN(ui.getMARGIN());
		top_x = (int) Math.round(6 * (2 * R + MARGIN) + 0.5 * Math.sqrt(3) * R);
		top_y = R;
		initPoints();
		this.setSize(bounds[4][2] + (2 * R + MARGIN) * 12 + R, bounds[16][3]
				+ R);
		this.setLocation(UI.SIDE, UI.TOP);
		this.addMouseListener(new CCListener());
		this.setVisible(true);
		this.initImgBuffer();
		this.paintState = State.INIT;
	}

	public void paint(Graphics g) {
		switch (paintState) {
		case INIT:
			super.paintComponent(g);
			this.initBoard(this.offScreenG);
			System.out.println("REpaint======");
			break;

		case PIECE:
			this.paintPiece(this.offScreenG);
			// System.out.println("REpaintPiece======");
			break;

		case SELECTED:
			this.paintSelected(this.offScreenG);
			break;
		default:
			System.out.println("Nothing");
			break;
		}
		g.drawImage(offScreenImg, 0, 0, this);

		// System.out.println("REpaint======");
	}

	private void paintPiece(Graphics g) {
		this.fill(g, paintPosition);
		this.drawPiece(g, paintPosition, playerFlag);
	}

	private void paintSelected(Graphics g) {
		this.fill(g, paintPosition);
		switch (playerFlag) {
		case 1:
			g.drawImage(Images.CCP_1_SELECTED, paintPosition.x,
					paintPosition.y, 2 * R, 2 * R, null);
			break;
		case 2:
			g.drawImage(Images.CCP_2_SELECTED, paintPosition.x,
					paintPosition.y, 2 * R, 2 * R, null);
			break;
		default:
			System.out.println("Invalid player");
		}
	}

	private void drawPiece(Graphics g, Point p, int flag) {

		switch (flag) {
		case 1:
			g.drawImage(Images.CCP_1, p.x, p.y, 2 * R, 2 * R, null);
			break;
		case 2:
			g.drawImage(Images.CCP_2, p.x, p.y, 2 * R, 2 * R, null);
			break;
		case 0:
			g.drawImage(Images.BALL, p.x, p.y, 2 * R, 2 * R, null);
			break;
		default:
			System.out.println("Invalid player");

		}
	}

	protected void fill(Graphics g, Point p) {
		
		g.drawImage(Images.BALL, p.x, p.y, 2 * R, 2 * R, null);
	}

	@Override
	protected void initBoard(Graphics g) {
		fillBackground(g);
		drawLines(Color.BLACK, g);
		// drawHoles(g);
		// drawPiece(1, new Location(8,8));
		for (int i = 0; i < ChineseChecker.COLOUM; i++) {
			for (int j = 0; j < ChineseChecker.ROW; j++) {
				Location l = new Location(i, j);
				// this.fill(g, getPosition(l));
				// System.out.println("col = " + i+" row = "+ j);
				this.drawPiece(g, getPosition(l), model.getValue(l));
				// System.out.print(model.getValue(l));
			}
			// System.out.println();
		}
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

	}

	@Override
	public void update(Observable arg0, Object obj) {
		if (obj instanceof SelectedNotification) {
			SelectedNotification sn = (SelectedNotification) obj;
              if (sn.getOldS() != null) {
				System.out.println("unselected.....");
				this.paintPosition = this.getPosition(sn.getOldS());
				this.playerFlag = model.getValue(sn.getOldS());
				this.paintState = State.PIECE;
				paintImmediately(paintPosition.x, paintPosition.y, 2 * R, 2 * R);
				repaint();
			}
			this.paintPosition = this.getPosition(sn.getNewS());
			this.playerFlag = model.getValue(sn.getNewS());
			this.paintState = State.SELECTED;
			paintImmediately(paintPosition.x, paintPosition.y, 2 * R, 2 * R);
			repaint();
		
		}
		else if(obj instanceof ValueChangedNotification){
			System.out.println("noticed valuechange");
			ValueChangedNotification vn =(ValueChangedNotification)obj;
			  this.paintPosition = this.getPosition(vn.getLocation());
			  this.playerFlag = model.getValue(vn.getLocation());
			  this.paintState = State.PIECE;
			  paintImmediately(paintPosition.x, paintPosition.y, 2 * R, 2 * R);
			   repaint();
	       }
		else if(obj instanceof WinNotification){
			WinNotification win = (WinNotification)obj;
			JFrame message = new JFrame();
			JOptionPane.showMessageDialog(message, "Player "+ win.getPlayer().getFlag()+"" + " win");
        }
		else if(obj instanceof ResetNotification){
	    	 this.paintState = State.INIT;
	    	 repaint();
		}
	}

	private void initPoints() {
		int distance = 2 * R + MARGIN;
		bounds[0][2] = top_x;
		bounds[0][3] = top_y;
		for (int i = 1; i < 17; i++) {
			bounds[i][2] = top_x - (int) (0.5 * distance)
					* (bounds[i][1] - bounds[i][0]);
			bounds[i][3] = top_y + (int) (Math.round(0.5 * Math.sqrt(3)))
					* distance * i;
		}
	}

	/*
	 * private void drawHoles(Graphics g) { for (int i = 0; i < 17; i++) { int
	 * noOfPoints = bounds[i][1] - bounds[i][0] + 1; for (int j = 0; j <
	 * noOfPoints; j++) { g.drawImage(Images.BALL, bounds[i][2] + (2 * R +
	 * MARGIN) * j - R, bounds[i][3] - R, 2 * R, 2 * R, null); } } }
	 */

	private void drawLines(Color c, Graphics g) {
		Color old = g.getColor();
		g.setColor(c);
		int distance = 2 * R + MARGIN;
		for (int i = 1; i < 16; i++) {
			g.drawLine(bounds[i][2], bounds[i][3], bounds[i][2] + distance
					* (bounds[i][1] - bounds[i][0]), bounds[i][3]);
		}
		for (int i = 0; i < 4; i++) {
			g.drawLine(bounds[i + 4][2], bounds[i + 4][3], bounds[4][2]
					+ distance * i, bounds[4][3]);
			g.drawLine(bounds[12][2] + i * distance, bounds[12][3],
					bounds[i][2] + i * distance, bounds[i][3]);
			g.drawLine(bounds[12][2] + 4 * distance, bounds[12][3],
					bounds[4][2] + 8 * distance, bounds[4][3]);// middle line
			g.drawLine(bounds[i + 13][2], bounds[i + 13][3], bounds[4][2]
					+ (i + 9) * distance, bounds[4][3]);
			g.drawLine(bounds[12][2] + (i + 9) * distance, bounds[12][3],
					bounds[i + 9][2] + (bounds[i + 9][1] - bounds[i + 9][0])
							* distance, bounds[i + 9][3]);

			g.drawLine(bounds[12 - i][2], bounds[12 - i][3], bounds[12][2] + i
					* distance, bounds[12][3]);
			g.drawLine(bounds[4][2] + i * distance, bounds[4][3],
					bounds[16 - i][2] + (bounds[16 - i][1] - bounds[16 - i][0])
							* distance, bounds[16 - i][3]);
			g.drawLine(bounds[4][2] + 4 * distance, bounds[4][3], bounds[12][2]
					+ 8 * distance, bounds[12][3]);// middle line
			g.drawLine(bounds[3 - i][2], bounds[3 - i][3], bounds[12][2]
					+ (i + 9) * distance, bounds[12][3]);
			g.drawLine(bounds[4][2] + (i + 9) * distance, bounds[4][3],
					bounds[7 - i][2] + (bounds[7 - i][1] - bounds[7 - i][0])
							* distance, bounds[7 - i][3]);
		}
		g.setColor(old);
	}

	private void fillBackground(Graphics g) {
		int distance = 4 * (2 * R + MARGIN);
		int[] x1 = { bounds[0][2], bounds[4][2], bounds[4][2] + distance,
				bounds[4][2] + distance, bounds[4][2] + 2 * distance,
				bounds[4][2] + 2 * distance, bounds[8][2], bounds[8][2],
				bounds[8][2] + distance, bounds[8][2] + distance,
				bounds[8][2] + 2 * distance, bounds[12][2] + distance };
		int[] y1 = { bounds[0][3], bounds[4][3], bounds[4][3], bounds[4][3],
				bounds[4][3], bounds[4][3], bounds[8][3], bounds[8][3],
				bounds[8][3], bounds[8][3], bounds[8][3], bounds[12][3] };
		int[] x2 = { bounds[4][2] + distance, bounds[4][2] + distance,
				bounds[8][2], bounds[4][2] + 2 * distance,
				bounds[8][2] + distance, bounds[4][2] + 3 * distance,
				bounds[12][2], bounds[8][2] + distance,
				bounds[12][2] + distance, bounds[8][2] + 2 * distance,
				bounds[12][2] + 2 * distance, bounds[12][2] + 2 * distance };
		int[] y2 = { bounds[4][3], bounds[4][3], bounds[8][3], bounds[4][3],
				bounds[8][3], bounds[4][3], bounds[12][3], bounds[8][3],
				bounds[12][3], bounds[8][3], bounds[12][3], bounds[12][3] };
		int[] x3 = { bounds[4][2] + 2 * distance, bounds[8][2],
				bounds[8][2] + distance, bounds[8][2] + distance,
				bounds[8][2] + 2 * distance, bounds[8][2] + 2 * distance,
				bounds[12][2] + distance, bounds[12][2] + distance,
				bounds[12][2] + 2 * distance, bounds[12][2] + 2 * distance,
				bounds[12][2] + 3 * distance, bounds[16][2] };
		int[] y3 = { bounds[4][3], bounds[8][3], bounds[8][3], bounds[8][3],
				bounds[8][3], bounds[8][3], bounds[12][3], bounds[12][3],
				bounds[12][3], bounds[12][3], bounds[12][3], bounds[16][3] };
		Color old = g.getColor();
		for (int i = 0; i < 12; i++) {
			switch (i) {
			case 0:
				g.setColor(new Color(70, 254, 16));
				break;
			case 1:
				g.setColor(new Color(0, 128, 255));
				break;
			case 2:
				g.setColor(new Color(97, 207, 255));
				break;
			case 3:
				g.setColor(new Color(111, 255, 72));
				break;
			case 4:
				g.setColor(new Color(237, 251, 76));
				break;
			case 5:
				g.setColor(new Color(254, 222, 0));
				break;
			case 6:
				g.setColor(new Color(90, 51, 254));
				break;
			case 7:
				g.setColor(new Color(178, 137, 255));
				break;
			case 8:
				g.setColor(new Color(255, 201, 141));
				break;
			case 9:
				g.setColor(new Color(248, 168, 79));
				break;
			case 10:
				g.setColor(new Color(255, 123, 0));
				break;
			case 11:
				g.setColor(new Color(255, 29, 13));
				break;
			}
			int[] x = { x1[i], x2[i], x3[i] };
			int[] y = { y1[i], y2[i], y3[i] };
			g.fillPolygon(x, y, 3);
		}
		g.setColor(old);
	}

	@Override
	public Location getLocationFromPonit(Point p) {
		Location result = null;
		for (int i = 0; i < 17; i++) {
			if (p.getY() >= bounds[i][3] - R && p.getY() <= bounds[i][3] + R) {

				int count = bounds[i][1] - bounds[i][0] + 1;
				for (int j = 0; j < count; j++) {
					if (p.getX() >= bounds[i][2] + j * (2 * R + MARGIN) - R
							&& p.getX() <= bounds[i][2] + j * (2 * R + MARGIN)
									+ R) {
						result = new Location(bounds[i][0] + j, i);
					}
				}

			}
		}
		return result;
	}

	@Override
	public Point getPosition(Location l) {
		int temp_x = 0;
		int temp_y = 0;
		temp_y = bounds[l.getY()][3];
		temp_x = bounds[l.getY()][2] + (l.getX() - bounds[l.getY()][0])
				* (2 * R + MARGIN);
		Point temp = new Point(temp_x - R, temp_y - R);
		return temp;
	}

}
