/*
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import src.Constants;
import src.game.GameWorld;
import src.scenario.Images;

/**
 * Main editor window.  It shows the current level and accepts clicks
 * to place new land or objects.
 * @author Darren Watts
 * date 1/28/08
 */
public class EditorWindow extends JFrame {

	private static final long serialVersionUID = src.Constants.serialVersionUID;

	private JScrollPane scrollPane;
	private JPanel editorPanel = new JPanel();
	private Graphics g;

	GridBagLayout gridBagLayout1 = new GridBagLayout();

	private EditorControlFrame editorControl;
	
	private int xScrollLocation;
	private int yScrollLocation;
	
	private int xMouse;
	private int yMouse;
	
	private boolean canDraw;
	
	private boolean dirty;

	/**
	 * Constructor for the EditorWindow.
	 */
	public EditorWindow(EditorControlFrame editorControl){
		try {
			this.editorControl = editorControl;
			xScrollLocation = yScrollLocation = 0;
			xMouse = yMouse = 0;
			dirty = true;
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Sets up the Frame with a JPanel inside a JScrollPane.  The world
	 * gets drawn to the JPanel.
	 */
	private void jbInit(){
		canDraw = true;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(gridBagLayout1);

		editorPanel.setBackground(Color.WHITE);

		resizePanel();

		editorPanel.setLayout(null);

		scrollPane = new JScrollPane(editorPanel);
		scrollPane.getViewport().add(editorPanel);

		this.getContentPane().add(scrollPane,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
						, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		// Set editor window to a decent size.  Make sure it's not too big
		// if the person has a small screen.
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xOffset = 100;
		if(tk.getScreenSize().width < 1024){
			setSize(400, 300);
			xOffset = 50;
		}
		else if(tk.getScreenSize().width == 1024){
			setSize(600, 450);
		}
		else setSize(800, 600);

		scrollPane.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				xMouse = e.getX();
				yMouse = e.getY();
				if(!e.isControlDown())
					testPress(e.getButton(), e.getX(), e.getY());
			}
			public void mouseReleased(MouseEvent e){
				xScrollLocation = -1;
				yScrollLocation = -1;
			}
		});
		
		scrollPane.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				if(e.isControlDown())
					dragMouse(e);
			}
		});

		setTitle("Roof Notifier");
		src.Constants.centerFrame(this);
		setLocation(new Point(getLocation().x + xOffset, getLocation().y));
		setVisible(true);
	}
	
	/**
	 * Sets the text on the title bar of this window.
	 * @param str String : text
	 */
	public void setWindowTitle(String str){
		this.setTitle(str);
	}

	/**
	 * Resizes the panel to the size of the world.
	 */
	public void resizePanel(){
		editorPanel.setPreferredSize(new Dimension(src.Constants.CELL_SIZE * 
				GameWorld.getInstance().getLand()[0].length,
				src.Constants.CELL_SIZE * 
				GameWorld.getInstance().getLand().length));
		repaint();
	}
	
	private void dragMouse(MouseEvent e){
		xScrollLocation -= e.getX() - xMouse;
		yScrollLocation -= e.getY() - yMouse;
		
		int currentX = scrollPane.getHorizontalScrollBar().getValue();
		int currentY = scrollPane.getVerticalScrollBar().getValue();
		
		scrollPane.getHorizontalScrollBar().setValue(
				currentX - (e.getX() - xMouse));
		scrollPane.getVerticalScrollBar().setValue(
				currentY - (e.getY() - yMouse));
		
		xMouse = e.getX();
		yMouse = e.getY();
	}

	/**
	 * Accepts input from the mouse, finds out which cell the user
	 * clicked on, and calls the editor controller method based
	 * on which mouse button was used.
	 * @param button int : mouse button
	 * @param x int : x location
	 * @param y int : y location
	 */
	private void testPress(int button, int x, int y){
		int newX = x + (int)scrollPane.getHorizontalScrollBar().getValue();
		int newY = y + (int)scrollPane.getVerticalScrollBar().getValue();

		int row = newY / Constants.CELL_SIZE;
		int col = newX / Constants.CELL_SIZE;

		// Make sure not out of bounds.
		int rows = GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel()).length;
		int cols = GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel())[0].length;
		if(col < 0 || row < 0 || col > cols-1 || row > rows-1) return;

		if(button == MouseEvent.BUTTON1)
			editorControl.leftClick(row, col);
		else if(button == MouseEvent.BUTTON2)
			editorControl.middleClick(row, col);
		else if(button == MouseEvent.BUTTON3)
			editorControl.rightClick(row, col);
	}

	/**
	 * Updates the scrollbars to fit the current panel size.
	 */
	public void updateScrollbars(){
		scrollPane.getViewport().add(editorPanel);
	}
	
	/**
	 * Stops the editor window from drawing.
	 */
	public void haltDrawing(){
		canDraw = false;
	}
	
	/**
	 * Starts the editor window drawing.
	 */
	public void resumeDrawing(){
		canDraw = true;
	}
	
	/**
	 * Sets whether or not the screen is "dirty".  If it is, the
	 * next time the world is drawn, it will only draw the land
	 * and no objects.
	 * @param bool boolean : dirty
	 */
	public void setDirty(boolean bool){
		dirty = bool;
	}

	/**
	 * Draws the part of the current level that is in view onto the
	 * main window.
	 */
	public void draw(){
		if(!canDraw) return;
		boolean viewMoved = dirty;
		
		if(scrollPane.getHorizontalScrollBar().getValue() != xScrollLocation ||
				scrollPane.getVerticalScrollBar().getValue() != yScrollLocation){
			xScrollLocation = scrollPane.getHorizontalScrollBar().getValue();
			yScrollLocation = scrollPane.getVerticalScrollBar().getValue();
			viewMoved = true;
		}
		
		g = editorPanel.getGraphics();
		if(g == null) return;
		g.setColor(Color.BLACK);

		try{
			BufferedImage img;
			int rowsToDraw = this.getSize().height / Constants.CELL_SIZE + 1;
			int colsToDraw = this.getSize().width / Constants.CELL_SIZE + 1;

			String currentImageLocation = "";

			// For all of the rows visible based on the location of the scroll bar.
			for(int row = scrollPane.getVerticalScrollBar().getValue() / Constants.CELL_SIZE; 
			row < scrollPane.getVerticalScrollBar().getValue() / Constants.CELL_SIZE + rowsToDraw; row++){

				// For all of the columns visible based on the location of the scroll bar.
				for(int col = scrollPane.getHorizontalScrollBar().getValue() / Constants.CELL_SIZE; 
				col < scrollPane.getHorizontalScrollBar().getValue() / Constants.CELL_SIZE + colsToDraw; col++){

					// Check to see if the row and column is out of bounds in the world.
					// If it is, break out of this loop and start on the next row.
					try {
						GameWorld.getInstance().getLandAt(row, col);
					} catch(ArrayIndexOutOfBoundsException ae){ break; }

					try {
						if(viewMoved){
							
							// Draw land background
							currentImageLocation = GameWorld.getInstance().getLandAt(row, col).getLandImage();

							img = Images.getInstance().getImage(currentImageLocation);

							g.drawImage(img, col * Constants.CELL_SIZE,
									row * Constants.CELL_SIZE, 57, 57, null);
						}
						
						currentImageLocation = GameWorld.getInstance().getLandAt(row, col).getImage();

						img = Images.getInstance().getImage(currentImageLocation);

						g.drawImage(img, col * Constants.CELL_SIZE,
								row * Constants.CELL_SIZE, 57, 57, null);

					} catch(NullPointerException nil){

					} catch(Exception ex) {
						//ex.printStackTrace();
						//System.exit(-1);
						System.out.println("picture not found:\n" + 
								currentImageLocation);  
					}
				}
			}
		} catch(Exception e) { 
			//e.printStackTrace();
		}
		dirty = false;
	}

}
