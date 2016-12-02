package kw.chinesechess.view;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  Note: Normally the ButtonPanel and DrawingArea would not be static classes.
 *  This was done for the convenience of posting the code in one class and to
 *  highlight the differences between the two approaches. All the differences
 *  are found in the DrawingArea class.
 */
public class DrawOnComponent
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI()
	{
		DrawingArea drawingArea = new DrawingArea();
		ButtonPanel buttonPanel = new ButtonPanel( drawingArea );

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Draw On Component");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(drawingArea);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	}

	static class ButtonPanel extends JPanel implements ActionListener
	{
		private DrawingArea drawingArea;

		public ButtonPanel(DrawingArea drawingArea)
		{
			this.drawingArea = drawingArea;

			add( createButton("	", Color.BLACK) );
			add( createButton("	", Color.RED) );
			add( createButton("	", Color.GREEN) );
			add( createButton("	", Color.BLUE) );
			add( createButton("	", Color.ORANGE) );
			add( createButton("	", Color.YELLOW) );
			add( createButton("Clear Drawing", null) );
		}

		private JButton createButton(String text, Color background)
		{
			JButton button = new JButton( text );
			button.setBackground( background );
			button.addActionListener( this );

			return button;
		}

		public void actionPerformed(ActionEvent e)
		{
			JButton button = (JButton)e.getSource();

			if ("Clear Drawing".equals(e.getActionCommand()))
				drawingArea.clear();
			else
				drawingArea.setForeground( button.getBackground() );
		}
	}

	static class DrawingArea extends JPanel
	{
		private ArrayList<ColoredRectangle> coloredRectangles = new ArrayList<ColoredRectangle>();
		private Point startPoint = null;
		private Point endPoint = null;

		public DrawingArea()
		{
			setBackground(Color.WHITE);

			MyMouseListener ml = new MyMouseListener();
			addMouseListener(ml);
			addMouseMotionListener(ml);
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			//  Custom code to paint all the Rectangles from the List

			Color foreground = g.getColor();

			g.setColor( Color.BLACK );
			g.drawString("Add a rectangle by doing mouse press, drag and release!", 40, 15);

			for (DrawingArea.ColoredRectangle cr : coloredRectangles)
			{
				g.setColor( cr.getForeground() );
				Rectangle r = cr.getRectangle();
				g.drawRect(r.x, r.y, r.width, r.height);
			}

			g.setColor( foreground );

			//  Paint the Rectangle as the mouse is being dragged

			if (startPoint != null && endPoint != null)
			{
				int x = Math.min(startPoint.x, endPoint.x);
				int y = Math.min(startPoint.y, endPoint.y);
				int width = Math.abs(startPoint.x - endPoint.x);
				int height = Math.abs(startPoint.y - endPoint.y);
				g.drawRect(x, y, width, height);
			}
		}

		public void clear()
		{
			coloredRectangles.clear();
			repaint();
		}

		class MyMouseListener extends MouseInputAdapter
		{
			private int xMin;
			private int xMax;
			private int yMin;
			private int yMax;

			public void mousePressed(MouseEvent e)
			{
				startPoint = e.getPoint();
				endPoint = startPoint;
				xMin = startPoint.x;
				xMax = startPoint.x;
				yMin = startPoint.y;
				yMax = startPoint.y;
			}

			public void mouseDragged(MouseEvent e)
			{
				//  Repaint only the area affected by the mouse dragging

				endPoint = e.getPoint();
				xMin = Math.min(xMin, endPoint.x);
				xMax = Math.max(xMax, endPoint.x);
				yMin = Math.min(yMin, endPoint.y);
				yMax = Math.max(yMax, endPoint.y);
				repaint(xMin, yMin, xMax - xMin + 1, yMax - yMin + 1);
			}

			public void mouseReleased(MouseEvent e)
			{
				//  Custom code to save the drawing information to the List

				int x = Math.min(startPoint.x, endPoint.x);
				int y = Math.min(startPoint.y, endPoint.y);
				int width = Math.abs(startPoint.x - endPoint.x);
				int height = Math.abs(startPoint.y - endPoint.y);
				Rectangle r = new Rectangle(x, y, width, height);

				if (r.width != 0 || r.height != 0)
				{
					ColoredRectangle cr = new ColoredRectangle(e.getComponent().getForeground(), r);
					coloredRectangles.add( cr );
				}

				startPoint = null;
				repaint();
			}
		}

		class ColoredRectangle
		{
			private Color foreground;
			private Rectangle rectangle;

			public ColoredRectangle(Color foreground, Rectangle rectangle)
			{
				this.foreground = foreground;
				this.rectangle = rectangle;
			}

			public Color getForeground()
			{
				return foreground;
			}

			public void setForeground(Color foreground)
			{
				this.foreground = foreground;
			}

			public Rectangle getRectangle()
			{
				return rectangle;
			}
		}
	}
}