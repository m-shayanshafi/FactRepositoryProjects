package ChessGameKenai;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JComponent;

/**
 * The Thumbnail class is needed for the ChooseIcon class in the ChessBoardView
 * The user will be able to click on the Thumbnail for changing the icon
 * 
 * @author Dimitri Pankov
 * @see Comparable
 * @see JComponent
 * @ver 1.0
 */
public class Thumbnail extends JComponent implements Comparable {

	private String imagePath;

	/**
	 * Overloaded constructor receives the path to the image
	 * 
	 * @param imagePath
	 *            as String the path for the image
	 */
	public Thumbnail(final String imagePath) {
		this.imagePath = imagePath;
		this.setPreferredSize(new Dimension(100, 100));
	}

	/**
	 * The method painComponent of Thumbnail is used here to paint the
	 * JComponent as we want
	 * 
	 * @param graphics
	 *            Graphics object used to paint this object
	 */
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintChildren(graphics);
		final URL url = this.getClass().getResource(imagePath);
		final Toolkit toolkit = this.getToolkit();
		final Image image = toolkit.getImage(url);
		graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}

	public int compareTo(final Object o) {
		if (!(o instanceof Thumbnail)) {
			throw new ClassCastException("Thumbnail Object Expected");
		}
		return this.getImagePath().compareTo(((Thumbnail) o).getImagePath());
	}
}
