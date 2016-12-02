package via.aventurica.view.aboutDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import via.aventurica.ViaAventurica;
import via.aventurica.view.utils.JImagePanel;
import via.aventurica.view.utils.JImagePanel.ImageLoadingException;

public class AboutDialog extends JDialog{
	private final static long serialVersionUID = 1L;
	
	public AboutDialog() { 
		super(ViaAventurica.APPLICATION, "‹ber ViaAventurica", true); 
		setBounds(ViaAventurica.APPLICATION.getX()+30, ViaAventurica.APPLICATION.getY()+30, 400, 250);
		setResizable(false); 
		initDialog(); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
		setVisible(true);
		
	}
	
	private void initDialog() {
		setLayout(new BorderLayout(0, 5)); 
		try { 
			add(new JImagePanel("/via/aventurica/view/images/info_header.png", true), BorderLayout.NORTH);
		} catch(ImageLoadingException ex) { 
			ex.printStackTrace(); 
		}
		JLabel lbl = new JLabel("<html><big>ViaAventurica</big><br>Version: "+ViaAventurica.VERSION+"<br><br>von Matthias Huttar<p>"+ViaAventurica.PROJECT_WEBSITE+"<br><br>ViaAventurica ist freie Software und steht unter der LGPL Lizenz"); 
		
		lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		add(lbl, BorderLayout.CENTER);
		
		JButton closeButton = new JButton(new AbstractAction("Schlieﬂen"){
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.dispose(); 
			}
		});  
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
		buttonPanel.add(closeButton); 
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		 
		add(buttonPanel, BorderLayout.SOUTH); 
	}
	
}
