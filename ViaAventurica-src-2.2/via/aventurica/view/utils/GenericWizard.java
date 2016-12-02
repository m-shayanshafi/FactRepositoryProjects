package via.aventurica.view.utils;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;


/**
 * Wizard-Framework. Basisklasse für einen Einseiten Wizard, 
 * der ein Resultat einlesen, bearbeiten und zurückgeben kann. 
 * Wizards sollten getyped gesubclassed werden: 
 * class PoitEditWizard extends GenericWizard&lt;Point&gt;. 
 * 
 * @param <DATA> der Typ, den dieser Wizard bearbeiten soll.
 * @author Matthias Huttar 
 */
public abstract class GenericWizard<DATA> extends JDialog {

	private static final long serialVersionUID = 1L;
	/**
	 * Anzahl der Seiten, die der Wizard hat. 
	 */
	private final int pageCount; 
	private int currentPageIndex = 0; 
	
	/**
	 * Die Daten, die vom Wizard entgegengenommen udn zurückgegeben werden
	 */
	private DATA userData; 
	
	/**
	 * wurde der wizard initialisiert?
	 */
	private boolean initialized = false; 
	
	/**
	 * legen fest, ob der Wizard einen ok/cancel Button hat. 
	 */
	private boolean hasOkButton, hasCancelButton;
	
	/**
	 * Beschriftung des OK Buttons
	 */
	private String okButtonText = "OK"; 
	/**
	 * Beschriftung des Abbrechen Buttons
	 */
	private String cancelButtonText = "Abbrechen"; 
	
	/**
	 * Referenzen auf OK/Abbrechen/Weiter/Zurück Button
	 */
	private JButton okButton, cancelButton, nextPageButton, lastPageButton; 
	/**
	 * Array aus weiteren Buttons in der Buttonleiste unten
	 */
	private JButton[] userButtons; 
	
	private JPanel mainPanel; 
	private CardLayout mainPanelLayout; 
	
	/**
	 * ein {@link JPanel}, dass einen Wizard Kopf anzeigt. 
	 */
	protected class HeaderPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
		/**
		 * Überschriften Label
		 */
		private JLabel headlineLabel; 
		/**
		 * Beschreibungs Label
		 */
		private JLabel contentLabel; 
		
		/**
		 * Prad zum hintergrundbild des wizards
		 */
		private static final String BACKGROUND = "via/aventurica/view/images/wizard-bg.png"; 
		/**
		 * Das geladene Hintergrundbild. 
		 */
		private BufferedImage backgroundImage; 
		
		/**
		 * @param headline die Überschrift
		 * @param content der Beschreibungstext (wird automatisch mit &lt;html&gt; gewrappt. 
		 */
		public HeaderPanel(String headline, String content)	{
			headlineLabel = new JLabel();
			headlineLabel.setBorder(new EmptyBorder(5, 10, 2, 0)); 
			contentLabel = new JLabel();
			contentLabel.setBorder(new EmptyBorder(5, 25, 0, 0));
			contentLabel.setVerticalAlignment(SwingConstants.TOP);
			
			
			if (UIManager.getLookAndFeel() instanceof MetalLookAndFeel) {
				headlineLabel.setFont(MetalLookAndFeel.getSystemTextFont().deriveFont(Font.BOLD));
				contentLabel.setFont(MetalLookAndFeel.getSystemTextFont().deriveFont(Font.PLAIN));
				
			}
			
			
			contentLabel.setFont(contentLabel.getFont().deriveFont(Font.PLAIN));
			
			
			setHeadline(headline); 
			setContent(content);
			
			setLayout(new BorderLayout()); 
			add(headlineLabel, BorderLayout.NORTH); 
			add(contentLabel, BorderLayout.CENTER);
			
			setBackground(Color.WHITE);
			
			// "Bauen" eines Etched Border, der nur die untere Kante zeichnet. 
			setBorder(new EmptyBorder(0, 0, 2, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public void paintBorder(Component c, Graphics g, int x, int y, 
						int width, int height) {
					Color normalBackground = GenericWizard.this.getBackground();
					g.setColor(normalBackground); 
					g.draw3DRect(-1, height - 2, width + 1, 2, false); 
				}
			}); 
			
			setPreferredSize(new Dimension(70, 70)); 

			try { 
				backgroundImage = ImageIO.read(ClassLoader.getSystemResourceAsStream(BACKGROUND)); 
			} catch (Exception e) {  e.printStackTrace();  }  
		}
		
		/**
		 * setzt die neue Überschrift
		 * @param headline die neue Überschrift
		 */
		void setHeadline(String headline) {
			headlineLabel.setText("<html><b>"+headline+"</b></html>"); 
		}
		
		/**
		 * setzt die neue Beschreibung
		 * @param content die neue Beschreibung
		 */
		void setContent(String content) {
			contentLabel.setText("<html>" + content + "</html>");
		} 
		
		
		/**
		 * zeichnet das Panel mit dem Hintergrund.  
		 * @param g graphics
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, getWidth() - backgroundImage.getWidth(), 0, null); 
			}
		}
	}
	
	/**
	 * Der Wizard Kopf
	 */
	private HeaderPanel headerPanel = null; 
	
	/**
	 * Action Listener für die Button Bar. Alle Buttons in dieser werden registriert. 
	 */
	private ActionListener buttonBarListener = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				JButton sourceButton = (JButton) e.getSource();
				if (sourceButton.equals(okButton)) {
					try { okButtonPressed(); } catch(FormEditException ex) { 
						ex.showMessages(GenericWizard.this);
						//ex.printStackTrace(); 
						toPage(ex.getReturnToPageNumber()); 
					}
				} else if (sourceButton.equals(cancelButton)) {
					cancelButtonPressed();
				} else if(sourceButton.equals(nextPageButton)) { 
					nextPage(); 
				} else if(sourceButton.equals(lastPageButton)) {
					lastPage();
				}
				else { 
					otherButtonPressed(sourceButton);
				}
			}
		}
	};


	/**
	 * die Buttonleiste unten
	 */
	private JComponent buttonBar;
	
	private BorderLayout rootLayout; 

		
	public GenericWizard(DATA userObject, String title, String description, int width, int height, boolean hasOkButton, boolean hasCancelButton) {
		this(userObject, JFrame.getFrames().length > 0 ? JFrame.getFrames()[0] : null, title, description, 0, width, height, hasOkButton, hasCancelButton);		
	}
	
	/**
	 * Initialisiert den Wizard
	 * @param userObject die Benutzerdaten
	 * @param title Titel des Wizards
	 * @param width Breite des Wizards
 	 * @param height Höhe des Wizards
	 * @param hasOkButton soll er einen OK Button haben?
	 * @param hasCancelButton soll er einen CANCEL Button haben? 
	 */
	public GenericWizard(DATA userObject, String title, String description, int width, int height, int pageCount, boolean hasOkButton, boolean hasCancelButton) {
		this(userObject, JFrame.getFrames().length > 0 ? JFrame.getFrames()[0] : null, title, description, width, height, pageCount, hasOkButton, hasCancelButton);		
	}
	
	/**
	 * Initialisiert den Wizard
	 * @param userObject die Benutzerdaten
	 * @param owner das Übergeordnete Fenster.
	 * @param title Titel des Wizards
	 * @param width Breite des Wizards
 	 * @param height Höhe des Wizards
	 * @param hasOkButton soll er einen OK Button haben?
	 * @param hasCancelButton soll er einen CANCEL Button haben? 
	 */
	public GenericWizard(DATA userObject, Frame owner, String title, String description,  
			int width, int height, int pageCount, boolean hasOkButton, boolean hasCancelButton) {
		super(owner);
		this.pageCount = pageCount; 
		this.userData = userObject;
		
		this.hasCancelButton = hasCancelButton; 
		this.hasOkButton = hasOkButton;
		this.okButtonText = pageCount > 1 ? "Fertig" : "Ok"; 
		
		setTitle(title); 
		if (owner == null) {
			setSize(new Dimension(width, height));
		} else {
			setBounds(owner.getBounds().x + 10, owner.getBounds().y + 10, width, height);
		}
		
		
		if(description!=null)
			setHeader(title, description); 
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
		setModal(true); 
	}
	
	/**
	 * Initialisiert den Button. Muss aus der Subclasse aufgerufen werden, 
	 * um den Wizard fertig zu stellen, und ihn zu zeigen. 
	 */
	public void initialize() {
		if(!initialized) {
		
			initialized = true; 
			 
			rootLayout = new BorderLayout(0, 0); 
			setLayout(rootLayout); 
			
			buttonBar = getButtonBar();
			if (headerPanel != null) {
				add(headerPanel, BorderLayout.NORTH);
			}
			
			mainPanel = new JPanel(mainPanelLayout = new CardLayout()); 
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			
			for(int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
				mainPanel.add(getForm(pageIndex), "page"+pageIndex); 
			}
			mainPanelLayout.first(mainPanel); 
			add(mainPanel, BorderLayout.CENTER);  
			add(buttonBar, BorderLayout.SOUTH); 
		}
	}
	
	/**
	 * Zeigt den Wizard. Diese Methode darf nicht aufgerufen werden, 
	 * wenn nicht vorher {@link #initialize()} aufgerufen wurd.e
	 * 
	 *  @param b visible
	 */
	@Override
	public final void setVisible(boolean b) {
		if (!initialized) {
			initialize();
		}		
		super.setVisible(b);
	}
	
	/**
	 * @return gibt eine Komponente zurück, die den 
	 * Inhalt des Wizards beinhält - also alle Textfelder, Labels, etc. 
	 */
	protected abstract JComponent getForm(int pageNumber); 
	
	
	/**
	 * Fügt weitere Buttons zu der Buttonleiste unten hinzu. 
	 * Auf diese Buttons wird automatisch ein Action Listener 
	 * gesetzt, der die Methode {@link #otherButtonPressed(JButton)} 
	 * aufruft. In dieser kann überprüft werden, welcher Button gedrückt wurde. 
	 * Alle Buttons, die in Fußleiste gepackt werden, 
	 * erhalten automatisch eine minimalbreite 
	 * von 65 pixeln ({@link #fitButton(JButton)}, 
	 * damit alle Buttons etwa "gleichwertig" aussehen. 
	 * 
	 * @param userButtons weitere Buttons, die in der unteren Buttonleiste platziert werden sollen. 
	 */
	public void setUserButtons(JButton... userButtons) {
		this.userButtons = userButtons;
	}
	
	/**
	 * @return die untere ButtonBar. 
	 */
	private JComponent getButtonBar() {
		JPanel newButtonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
				
		  
		if (userButtons != null) {
			for (JButton userButton : userButtons) {
				fitButton(userButton); 
				newButtonBar.add(userButton); 
				userButton.addActionListener(buttonBarListener); 
			}
		}
		
		if(pageCount > 1) {
			if(userButtons==null)
				newButtonBar.add(new JSeparator()); 
			lastPageButton = new JButton("Zurück"); 
			nextPageButton = new JButton("Weiter"); 
			fitButton(lastPageButton); 
			fitButton(nextPageButton); 
			lastPageButton.setEnabled(false); 
			nextPageButton.addActionListener(buttonBarListener); 
			lastPageButton.addActionListener(buttonBarListener); 
			newButtonBar.add(lastPageButton); 
			newButtonBar.add(nextPageButton); 
		}

		if (hasOkButton || hasCancelButton) {
			newButtonBar.add(new JSeparator()); 
		}
		
		
		if (hasOkButton) {
			okButton = new JButton(okButtonText);
			okButton.addActionListener(buttonBarListener);
			fitButton(okButton); 
			//newButtonBar.add(okButton);
			if(pageCount > 1)
				okButton.setEnabled(false); 
		}
		
		if (hasCancelButton) {
			cancelButton = new JButton(cancelButtonText);
			cancelButton.addActionListener(buttonBarListener); 
			fitButton(cancelButton);
			//newButtonBar.add(cancelButton); 
		}
		
		if(pageCount > 1) {
			if(hasCancelButton)
				newButtonBar.add(cancelButton);
			if(hasOkButton)
			newButtonBar.add(okButton); 
		} else { 
			if(hasOkButton)
				newButtonBar.add(okButton);
			if(hasCancelButton)
				newButtonBar.add(cancelButton); 
		}
		
	
		
	
		newButtonBar.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		newButtonBar.setOpaque(false); 
		return newButtonBar; 
	}
	
	/**
	 * wird aufgerufen, wenn eine neue Seite angezeigt wird. 
	 * @param pageNumber seitennummer die geöffnet wurde
	 * 
	 */
	protected void pageOpened(int pageNumber) { 
		
	}
	
	
	
	
	
	/**
	 * Passt verändert die Breite eines Buttons, 
	 * so dass auch der OK Button eine akzeptable Breite hat. 
	 * @param b ein Button
	 */
	private void fitButton(JButton b) 	{
		final int minWidth = 65; 
		Dimension size = b.getPreferredSize(); 
		if (size.width < minWidth) {
			b.setPreferredSize(new Dimension(minWidth, size.height));
		}
	}
	
	/**
	 * Setzt die UsSerData, kann von außen abgefragt werden, wenn OK Gedrückt wurde. 
	 * @param userData die UserData. 
	 */
	public void setUserData(DATA userData) {
		this.userData = userData;
	}
	
	/**
	 * @return gibt die UserData zurück, oder <code>null</code> 
	 * wenn keine UserData festgelegt wurde, oder auf Abbrechen gedrückt wurde.  
	 */
	public DATA getUserData() {
		return userData;
	}
	
	/**
	 * Callback, wenn der OK Button gedrückt wurde. 
	 * Kann überschrieben werden, um weitere Funktionalitäten einzufügen. 
	 * In diesem Fall sollte die super Methode jedoch immer aufgerufen werden
	 */
	protected void okButtonPressed() throws FormEditException {
		dispose(); 
	}
	
	/**
	 * Callback, wenn der Cancel Button gedrückt wurde. 
	 * Kann überschrieben werden, um weitere Funktionen einzufügen. 
	 * In diesem Fall sollte die super methode jedoch immer aufgerufen werden. 
	 */
	protected void cancelButtonPressed() {
		userData = null;
		dispose(); 
	}
	
	/**
	 * Callback wenn die nächste Seite aufgerufen wird.  
	 */
	protected void nextPage() { 
		currentPageIndex++; 
		if(currentPageIndex == pageCount-1) { 
			okButton.setEnabled(true); 
			nextPageButton.setEnabled(false); 
		}
		lastPageButton.setEnabled(true);
		mainPanelLayout.next(mainPanel); 
		pageOpened(currentPageIndex); 
	//	updateCurrentPage();  
	}
	
	/**
	 * Callback wenn die vorherige Seite aufgerufen wird. 
	 */
	protected void lastPage() { 
		currentPageIndex--;
		okButton.setEnabled(false); 
		nextPageButton.setEnabled(true); 
		if(currentPageIndex == 0) { 
			lastPageButton.setEnabled(false); 
		} 
		mainPanelLayout.previous(mainPanel);
		pageOpened(currentPageIndex); 
		//updateCurrentPage(); 
	}
	
	protected void toPage(int pageNumber) { 
		if(pageNumber >= 0 && pageNumber < pageCount && pageNumber!=currentPageIndex) { 
			if(currentPageIndex > pageNumber)
				for(int i=currentPageIndex; i>pageNumber; i--)
					lastPage(); 
			else 
				for(int i=currentPageIndex; i<pageNumber; i++)
					nextPage(); 
				
		}
	}
	
/*	private void updateCurrentPage() { 
		mainPanelLayout.show(mainPanel, "_"+currentPageIndex);
		mainPanelLayout.
/*		System.out.println("Old Component: "+currentPageComponent);
		remove(currentPageComponent); 
		currentPageComponent = getForm(currentPageIndex); 
		add(currentPageComponent, BorderLayout.CENTER);
		System.out.println("New Component: "+currentPageComponent);
		//getLayout().layoutContainer(getContentPane()); 
		repaint();  
	} */
	
	/**
	 * Callback Methode, die aufgerufen wird, wenn ein Button der 
	 * button Bar gedrückt wird, der nicht der OK und nicht der Cancel Button ist. 
	 * @param button der gedrückte Button
	 */
	protected void otherButtonPressed(JButton button) {
	}
	
	/**
	 * Erstellt einen Header für diesen Wizard bzw. ändert den vorhandenen 
	 * @param headline die überschrift
	 * @param text die Beschreibung. 
	 */
	protected void setHeader(String headline, String text) {
		if (headerPanel == null) {
			headerPanel = new HeaderPanel(headline, text);
			if (initialized) {
				add(headerPanel, BorderLayout.NORTH);
			} 
		} else {
			headerPanel.setHeadline(headline); 
			headerPanel.setContent(text); 
		}
		 
	}
	
	/**
	 * @param okButtonText der neue Text des OK Buttons
	 */
	protected void setOkButtonText(String okButtonText) {
		this.okButtonText = okButtonText;
		if (okButton != null) { 
			okButton.setText(okButtonText); 
		}
	}
	
	/**
	 * @param cancelButtonText der neue Text des Cancel Buttons
	 */
	protected void setCancelButtonText(String cancelButtonText) {
		this.cancelButtonText = cancelButtonText;
		if (cancelButton != null) { 
			cancelButton.setText(cancelButtonText); 
		}
	}
	

	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		revalidateLayout(); 
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		revalidateLayout(); 
	}
	
	private final void revalidateLayout() {
		
		dispatchEvent(new ComponentEvent(this, WindowEvent.COMPONENT_RESIZED)); 
		
	}
	
	

	
	
}
