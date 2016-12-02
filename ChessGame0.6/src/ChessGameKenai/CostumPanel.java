package ChessGameKenai;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JPanel;

/**
 * The CostumPanel class is JPanel i want to paint i have decided to have
 * the CostumPanel that will receive path to the image and paint itself
 * accordingly
 * 
 * @author Dimitri Pankov
 * @see JPanel
 * @version 1.1
 */
public class CostumPanel extends JPanel {

	private String path;
	private LayoutManager layout;

	public CostumPanel() {
		super.setLayout(new FlowLayout());
	}

	/**
	 * The overloaded constructor of the class receives path to the mage as well
	 * as the LayoutManager to use
	 * 
	 * @param path
	 *            as a String
	 * @param layout
	 *            as a layoutManager
	 */
	public CostumPanel(String path, LayoutManager layout) {
		this.path = path;
		this.layout = layout;
		super.setLayout(layout);
	}

	/**
	 * Overloaded constructor of the class receives the path to the image
	 * 
	 * @param path
	 *            as a String
	 */
	public CostumPanel(String path) {
		this.path = path;
		super.setLayout(new FlowLayout());
	}

	/**
	 * Overloaded constructor of the class receives a LayoutManager in the
	 * constructor
	 * 
	 * @param layout
	 *            as a LayoutManager
	 */
	public CostumPanel(LayoutManager layout) {
		this.layout = layout;
		super.setLayout(layout);
	}

	/**
	 * The paintComponent method that is needed when painting costumPanel the
	 * path to the image is checked and if the path is not null paints the panel
	 * accordingly
	 * 
	 * @param graphics
	 *            as Graphics object is used to paint this panel
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		if (path != null) {
			URL url = this.getClass().getResource(path);
			Toolkit toolkit = this.getToolkit();
			Image image = toolkit.getImage(url);
			graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),
					this);
		}
	}

	/**
	 * returns the LayoutManager of the CostumPanel to the caller
	 *
	 * @return layout as a LayoutManager
	 */
	public LayoutManager getLayoutManager() {
		return layout;
	}
}
