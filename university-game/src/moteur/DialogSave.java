package moteur;

import gui.DialogConnexion;
import gui.InterfaceJeu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import joueur.Joueur;

public class DialogSave extends JDialog {
	private static final long serialVersionUID = 1L;
	//public InterfaceJeu mainwindow;
	JTextField idField;
	JPasswordField pwdField;
	MoteurMonojoueur moteur;

	public DialogSave(/*InterfaceJeu w,*/ MoteurMonojoueur m)
	{
		
		this.setTitle("Sauvegarde");
		this.setResizable(false);
		this.setModal(true);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		
		
		
		
		JPanel panelTexte = new JPanel();
		JLabel idLabel = new JLabel("Veuillez entrer un nom de sauvegarde.");
		panelTexte.add(idLabel);
		this.add(new JLabel("     "), BorderLayout.NORTH);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		panel.add(panelTexte, BorderLayout.NORTH);
		
		
		JPanel panelField = new JPanel();
		idField = new JTextField();
		idField.setEditable(true);
		idField.setPreferredSize(new Dimension(200,20));
		panelField.add(idField, BorderLayout.CENTER);
		/*panelField.add(new JLabel("     "), BorderLayout.NORTH);
		panelField.add(new JLabel("     "), BorderLayout.SOUTH);*/
		panel.add(panelField, BorderLayout.CENTER);
		
		JPanel panelBouton = new JPanel();
		JButton boutonOk = new JButton("Ok");
		boutonOk.addActionListener(new AdaptatorOk(idField, m, this));
		JButton boutonAnnuler = new JButton("Annuler");
		boutonAnnuler.addActionListener(new AdaptatorAnnuler(this));
	
		panelBouton.add(boutonAnnuler, BorderLayout.CENTER);
		panelBouton.add(boutonOk, BorderLayout.EAST);
		
		panel.add(panelBouton, BorderLayout.SOUTH);
		panel.add(new JLabel("     "), BorderLayout.WEST);
		panel.add(new JLabel("     "), BorderLayout.EAST);
		
		this.add(panel, BorderLayout.CENTER);
		this.add(new JLabel("     "), BorderLayout.NORTH);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);

		pack();
		


		//this.setSize(300,110);
		
		setVisible(true);


	}

	class AdaptatorOk implements ActionListener{
		JTextField textFieldSauvegarde;
		MoteurMonojoueur moteur;
		private DialogSave dial;

		public AdaptatorOk(JTextField t, MoteurMonojoueur _moteur, DialogSave _dial){
			textFieldSauvegarde = t;
			moteur = _moteur;
			dial = _dial;
		}

		public void actionPerformed(ActionEvent e){
			try {
				String path = new String("save/");
				path = path.concat(textFieldSauvegarde.getText());
				PrintStream p;
				p = new PrintStream(path);
				// difficulte
				if (moteur.getDifficulte().equalsIgnoreCase("facile"))
					p.print(1 + " ");
				else if (moteur.getDifficulte().equalsIgnoreCase("normal"))
					p.print(2 + " ");
				else if (moteur.getDifficulte().equalsIgnoreCase("difficile"))
					p.print(3 + " ");
				else if (moteur.getDifficulte().equalsIgnoreCase("ultime"))
					p.print(4 + " ");
				// camp
				if (moteur.getJoueurHumain().getCamp() == Joueur.SUEDOIS){
					p.print(1 + " ");
				}
				else
					p.print(0 + " ");
				// date
				Date d = new Date();
				p.print(d.getTime() + " ");
				// plateau
				for(int i=0;i<moteur.getPlateau().getLine();i++){
					for(int j=0;j<moteur.getPlateau().getColumn();j++){
						p.print(moteur.getPlateau().getPlateauPieces(i,j).type() + " ");
					}
				}
				dial.dispose();// retour au jeu
			} catch (IOException exception) {
				System.out.println("Impossible de sauver dans le fichier selectionne");
			}
		}
	}
	
	class AdaptatorAnnuler implements ActionListener {
		private DialogSave dial;
		public AdaptatorAnnuler(DialogSave d) {
			dial = d;
		}
	    public void actionPerformed(ActionEvent e) {
	    	dial.dispose(); // retour au jeu
	    }
	}	
}
