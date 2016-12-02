package inf101.games.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * En variant av JPanel med et bakgrunnsbilde.
 * 
 * @author Anya Helene Bagge
 *
 */
class ImagePanel extends JPanel {
	private static final long serialVersionUID = -5124282473196901178L;
	private Image img;
	private String imgNavn;
	private int w;
	private int h;

	public ImagePanel() {
		super();
		img = null;
	}

	public ImagePanel(String bilde) {
		super();
		settBilde(bilde);
		Dimension size = new Dimension(w, h);
		setSize(size);
	}
	public void settBilde(String bilde) {
		if(bilde == null) {
			img = null;
			imgNavn = null;
		}
		else if(!bilde.equals(imgNavn)) {
			img =  ImageLoader.getImage(bilde).getImage();
			w = img.getWidth(null);
			h = img.getHeight(null);
			imgNavn = bilde;
			validate();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img == null)
			return;
		int gw = this.getWidth();
		int gh = this.getHeight();
		int iw = w;
		int ih = h;
		int ix = 0, iy = 0;
		if(iw-gw > 20 || ih-gh > 20) {
			if((double)gw/gh > (double)iw/ih) {
				ih = (int)((double)iw*(double)gh/gw);
				iy = (h-ih)/2;
			}
			else {
				iw = (int)((double)ih*(double)gw/gh);
				ix = (w-iw)/2;
			}
		}
		else {
			if(iw > gw) {
				ix = (gw-iw)/2;
				iw = gw;
			}
			if(ih > gh) {
				iy = (gh-ih)/2;
				ih = gh;
			}
		}
		g.drawImage(img, 0, 0, gw-1, gh-1, ix, iy, iw-1, ih-1, null);
	}
}