/**
 * 
 */
package kw.texasholdem.view;

/**
 * @author ken
 *
 */
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AboutDialog extends Dialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	public final static String TITLE = "v1.0";
	public final static String MESSAGE = "This program is written by Ken Wu.  Copyright 2012-2013";
	
  public AboutDialog(Frame parent) {
	  
		super(parent, TITLE, true); // true for modal
		this.add("Center", new Label(MESSAGE));
		Button ok = new Button("OK");
		ok.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	AboutDialog.this.setVisible(false);
		    }
		});
		this.add("South", ok);
		this.pack();

		// locate dialog to the center
		Dimension dd = this.getSize();
		Dimension pd = parent.getSize();
		Point pl = parent.getLocation();
		this.setLocation(
		    pl.x + ((int) (pd.getWidth() - dd.getWidth()))/2,
		    pl.y + ((int) (pd.getHeight() - dd.getHeight()))/2
		);
		this.setVisible(true);
  }
  public void actionPerformed(ActionEvent e) {
    setVisible(false); 
    dispose(); 
  }
  
}