package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;



public class Souris implements MouseMotionListener,MouseListener {
	
	InterfaceJeu itJeu;
	Point p;
	
	public Souris(InterfaceJeu j) {
		itJeu = j;
		p = new Point();
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		if (itJeu.getSelection()) {
			itJeu.setPositionSouris(e.getPoint());
			if (e.getPoint() != null) {
				try {
					itJeu.getAireDessin().changeCase(itJeu.getCaseSouris());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	//----- 
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * 
	 * */
	public void mousePressed(MouseEvent arg0) {
		try {
			itJeu.setPositionSouris(arg0.getPoint());
			p = (Point) itJeu.getCaseSouris().clone();
			if(arg0.getButton() == arg0.BUTTON1 )  
			{
				/*selection*/
				if (!itJeu.getSelection()) {
					itJeu.getAireDessin().changeCase(p);
					itJeu.getAireDessin().isPiece(p.x, p.y, itJeu.getTour());
					if (itJeu.getAireDessin().isPiece(p.x, p.y, itJeu.getTour())) {
						itJeu.setSelection(true);
						itJeu.getEngyne().setPoint(p);
						itJeu.setPieceSelectionne(p);
						itJeu.getAireDessin().repaint();
					} 
				/*validation*/
				} else {
					if(itJeu.getEngyne().coupValide(p, itJeu.getTour())) {
						itJeu.getEngyne().setPoint(p);
						itJeu.setSelection(false);
						itJeu.getAireDessin().setisPlayerSelect(false);
						itJeu.getAireDessin().repaint();
					} 
				}
			}
			if(arg0.getButton() == arg0.BUTTON2 || arg0.getButton() == arg0.BUTTON3 )  
			{
				itJeu.setSelection(false);
				itJeu.getAireDessin().setisPlayerSelect(false);
				itJeu.getAireDessin().repaint();
			}
			//itJeu.getAireDessin().changeCase(itJeu.getCaseSouris());
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
