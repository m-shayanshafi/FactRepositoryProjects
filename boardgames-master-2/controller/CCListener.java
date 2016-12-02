package controller;

import java.awt.event.MouseEvent;

import model.HumanPlayer;
import model.Location;
import view.CCBoard;

public class CCListener extends BGListener{
	public void mouseClicked(MouseEvent e) {
		CCBoard gb = (CCBoard)e.getSource();
		HumanPlayer hp = (HumanPlayer) gb.getModel().getCurrentPlayer();
		Location temp = gb.getLocationFromPonit(e.getPoint());
		if(temp != null ){
		 System.out.println(temp.getX() + "," + temp.getY());
	 	 hp.select(temp);
		}
		
		//Mancala mc = (Mancala)gp.bg;
		//mc.select(new Location(temp.getX(),temp.getY()));
	}
}
