package scorch.gui;

/*
  Class:  JoinGame
  Author: Mikhail Kruk

  Description: the dialog window prestented to a non-master user when (s)he
  joins the game. Allows to select tank type and shows people who are
  already waiting for the game to start
*/

import java.awt.*;
import java.awt.event.*;

import scorch.*;

public class JoinGame extends PlayersList implements ActionListener
{
    private TankSelection tankSelection;
    private Checkbox sounds;

    public JoinGame(PlayerProfile profile, ScorchApplet owner)
    {
	super("Join game", owner);
   
	Panel p0, p1, p2, p2a;
	Button bOK = new Button("OK"), bCancel = new Button("Cancel");
	sounds = new Checkbox("Sound effects");

	sounds.setState(profile.getSounds());

	tankSelection = new TankSelection(200,30);
	
	if( profile.getTankType() >= 0 ) 
	    tankSelection.setSelected(profile.getTankType());

	p0 = new Panel();
	p0.setLayout(new FlowLayout());
	p0.add(bOK);
	p0.add(bCancel);
	
	p1 = new Panel();
	p1.setLayout(new BorderLayout());
	p1.add(new Label("Select your tank:"), BorderLayout.CENTER);
	Panel p1a = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	p1a.add(sounds);
	p1.add(p1a, BorderLayout.NORTH);
	p1.add(tankSelection, BorderLayout.SOUTH);

	p2a = new Panel();
	p2a.setLayout(new FlowLayout(FlowLayout.CENTER));
	p2a.add(players);

	p2 = new Panel();
	p2.setLayout(new BorderLayout());
	p2.add(new Label("Players in game:", Label.CENTER), 
	       BorderLayout.NORTH);
	p2.add(p2a, BorderLayout.SOUTH);
	setLayout(new BorderLayout(10, 5));
	
	add(p1, BorderLayout.CENTER);
	add(p2, BorderLayout.NORTH);
	add(p0, BorderLayout.SOUTH);
	
	bOK.addActionListener(this);
	bCancel.addActionListener(this);
	
	validate();

	timeLeft = 60;
    }

    public void actionPerformed(ActionEvent evt)
    {
	String cmd = evt.getActionCommand();
	
	if( cmd.equals("OK") )
	    {
		close();
		((ScorchApplet)owner).joinGame
		    (new PlayerSettings
			(tankSelection.getSelected(), sounds.getState()));
		return;
	    }
	if( cmd.equals("Cancel") )
	    {
		((ScorchApplet)owner).Quit();
		return;
	    }
	return;
    }
}
