package controller;


import java.awt.event.MouseEvent;

import model.HumanPlayer;
import model.Location;
import view.MancalaBoard;

public class MancalaListener extends BGListener {


	public void mouseClicked(MouseEvent e) {
		MancalaBoard gb = (MancalaBoard)e.getSource();
		HumanPlayer hp = (HumanPlayer) gb.getModel().getCurrentPlayer();
		Location temp = gb.getLocationFromPonit(e.getPoint());
		System.out.println(temp.getX() + "," + temp.getY());
		hp.select(temp);
		//Mancala mc = (Mancala)gp.bg;
		//mc.select(new Location(temp.getX(),temp.getY()));
	}

}
