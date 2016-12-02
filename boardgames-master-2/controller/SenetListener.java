package controller;

import java.awt.event.MouseEvent;

import model.HumanPlayer;
import model.Location;
import model.Senet;
import view.SenetBoard;

public class SenetListener extends BGListener{
	public void mouseClicked(MouseEvent e) {
		SenetBoard gb = (SenetBoard)e.getSource();
		HumanPlayer hp = (HumanPlayer) gb.getModel().getCurrentPlayer();
		Location temp = gb.getLocationFromPonit(e.getPoint());
		System.out.println(temp.getX() + "," + temp.getY());
		if(temp.equals(Senet.ROLL))
		 hp.roll(5, 1);	
		else	
	 	 hp.select(temp);
		
		//Mancala mc = (Mancala)gp.bg;
		//mc.select(new Location(temp.getX(),temp.getY()));
	}
}
