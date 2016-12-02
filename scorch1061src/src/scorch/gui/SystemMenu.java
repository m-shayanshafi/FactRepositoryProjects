package scorch.gui;

/*
  Class:  SystemMenu
  Author: Mikhail Kruk

  Description: this class provides system menu GUI functionality
*/

import java.awt.*;

import scorch.ScorchApplet;
import swindows.*;

public class SystemMenu extends sWindow
{
    private Button mkill, editProfile, deleteProfile, topTen;

    public SystemMenu(ScorchApplet owner)
    {
	super(-1,-1,0,0,"System Menu", owner);
	
	Panel p = new Panel();
	p.setLayout(new GridLayout(9,1,0,5));
	p.add(new Button("Statistics"));
	topTen = new Button("Top 10 players");
	topTen.setEnabled(true);
	p.add(topTen);
	mkill = new Button("Mass kill");
	mkill.setEnabled(owner.isMaster());
	p.add(mkill);
	editProfile = new Button("Edit profile");
	editProfile.setEnabled(!owner.isGuest());
	p.add(editProfile);
	deleteProfile = new Button("Delete profile");
	deleteProfile.setEnabled(false && !owner.isGuest());
	p.add(deleteProfile);
	p.add(new Button("About Scorch"));
	p.add(new Button("On-line help"));
	p.add(new Button("Leave Scorch"));
	p.add(new Button("Close this menu"));

	setLayout(new FlowLayout(FlowLayout.CENTER));
	add(p);
	
	validate();
    }

    public boolean handleEvent(Event evt)
    {
	if( evt.id == Event.KEY_PRESS )
	    {
		if ( evt.key == Event.ESCAPE )
		    {
			close();
			return true;
		    }
		else
		    return super.handleEvent(evt);
	    }
	
	if( evt.id == Event.ACTION_EVENT )
	    {
		if( evt.arg.equals("On-line help") )
		    {
			//close();
			((ScorchApplet)owner).showHelp();
			return true;
		    }
		if( evt.arg.equals("Edit profile") )
		    {
			//close();
			NewUser nu = 
			    new NewUser		
			       (((ScorchApplet)owner).getMyPlayer().getName(), 
				(ScorchApplet)owner, 
				((ScorchApplet)owner).getMyPlayer().
				getProfile());
			nu.display();
			return true;
		    }
		if( evt.arg.equals("Statistics") )
		    {
			//close();
			StatsWindow sw =
			    new StatsWindow(StatsWindow.IG,(ScorchApplet)owner);
			sw.display();
			return true;
		    }
		if( evt.arg.equals("Top 10 players") )
		    {
			//close();
			((ScorchApplet)owner).requestTopTen();
			return true;
		    }
		if( evt.arg.equals("About Scorch") )
		    {
			//close();
			AboutBox ab = new AboutBox((ScorchApplet)owner);
			ab.display();
			return true;
		    }
		if( evt.arg.equals("Leave Scorch") )
		    {
			((ScorchApplet)owner).Quit();
			return true;
		    }
		if( evt.arg.equals("Close this menu") )
		    {
			close();
			return true;
		    }
		if( evt.arg.equals("Mass kill") )
		    {
			close();

			String b[] = {"Yes", "Cancel"};
			String c[] = {"massKill", null};
			MessageBox msg = new MessageBox
			    ("Confirmation", "Are you sure you want to kill everybody and start a new round?",
			     b, c, owner, this);
			msg.display();
		    }
	    }
	return super.handleEvent(evt);
    }

    public void display()
    {
	if( ((ScorchApplet)owner).isMassKilled() )
	    mkill.setEnabled(false);
	super.display();
    }
}
