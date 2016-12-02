package inf101.games.gui;
import inf101.games.IGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import java.util.List;


/**
 * @author Anna Maria Eilertsen
 * @author Alexandre Vivmond
 * @author Anya Helene Bagge
 *
 */
public class GameGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = -2030937455049555857L;
	/**
	 * Her ligger cellene
	 */
	private final ImagePanel hovedPanel;
	/**
	 * Viser tid nederst
	 */
	private final JPanel statusPanel;
	/**
	 * 'Nytt games' knapp
	 */
	private final JPanel kontrollPanel;
	/**
	 *  Knapper for å starte btnNew games
	 */
	private final JButton btnNew, btnStart, btnStop, btnStep;
	/**
	 * For å finne X og Y til knappen som ble trykket
	 */
	private final Map<JButton, Point> buttonMap = new HashMap<JButton, Point>();

	/**
	 * Referanse til spillet
	 */
	private final List<IGame> games;

	private IGame selectedGame;
	/**
	 * Vekker oss hvert halve sekund
	 */
	private final javax.swing.Timer timer;
	private JComboBox<String> sizes;
	private final JComboBox<String> gameSelection;
	private JComboBox<String> gameMenu;
	private static final String[] boardSizes = new String[] {"10x10", "12x12", "15x15", "20x15", "30x20"}; 

	public GameGUI(IGame spill) {
		this(Arrays.asList(spill));
	}
	/**
	 * Oppretter en ny games-GameGUI
	 * @param games Spillet som skal kontrolleres
	 */
	public GameGUI(List<IGame> spill) {
		super();
		setLayout(new BorderLayout());
		
		this.games = new ArrayList<IGame>(spill);
		Collections.sort(games, new GameComparator());
		this.selectedGame = spill.get(0);

		String[] gameNames = new String[games.size()];
		int i = 0;
		for(IGame g : games)
			gameNames[i++] = g.getName();
		gameSelection = new JComboBox<String>(gameNames);
		gameSelection.setSelectedItem(selectedGame.getName());

		timer = new javax.swing.Timer(150, this);  // vekk oss hvert 500 millisekund

		JPanel dummyKontrollPanel = new JPanel();
		dummyKontrollPanel.setLayout(new BorderLayout());
		dummyKontrollPanel.setForeground(Style.FOREGROUND);
		dummyKontrollPanel.setBackground(Style.BACKGROUND);
		kontrollPanel = new JPanel();
		kontrollPanel.setForeground(Style.FOREGROUND);
		kontrollPanel.setBackground(Style.BACKGROUND);
		dummyKontrollPanel.add(kontrollPanel, BorderLayout.WEST);

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStop = new JButton("Stopp");
		btnStop.addActionListener(this);
		btnStep = new JButton("Neste steg");
		btnStep.addActionListener(this);
		btnNew = new JButton("Nytt brett");
		btnNew.addActionListener(this);

		hovedPanel = new ImagePanel();
		hovedPanel.setForeground(Style.BACKGROUND);
		hovedPanel.setBackground(Style.FOREGROUND);
		statusPanel = new JPanel();
		statusPanel.setForeground(Style.FOREGROUND);
		statusPanel.setBackground(Style.BACKGROUND);

		// ekstra panel for å få riktig bakgrunn på resten
		JPanel dummyPanel = new JPanel();
		dummyPanel.setForeground(Style.FOREGROUND);
		dummyPanel.setBackground(Style.BACKGROUND);

		add(dummyKontrollPanel, BorderLayout.NORTH);		
		add(hovedPanel,BorderLayout.WEST);
		add(statusPanel, BorderLayout.SOUTH);
		add(dummyPanel, BorderLayout.CENTER);
	}
	
	public void initialize() {
		initializeControl();
		initializeBoard();
		setVisible(true);
	}

	private void initializeControl() {
		kontrollPanel.removeAll();


		kontrollPanel.add(gameSelection);
		kontrollPanel.add(btnNew);

		List<String> bSizes = selectedGame.getBoardSizes();
		if(bSizes == null)
			bSizes = Arrays.asList(boardSizes);
		// sørg for at vi har vår egen lokale kopi som vi kan endre på
		bSizes = new ArrayList<String>(bSizes);
		String size = selectedGame.getWidth() + "x" + selectedGame.getHeight();
		if(!bSizes.contains(size)) {
			bSizes.add(size);
		}
		Collections.sort(bSizes);
		if(sizes != null) 
			sizes.removeActionListener(this);
		sizes = new JComboBox<String>(bSizes.toArray(new String[bSizes.size()]));
		sizes.setSelectedItem(size);
		sizes.addActionListener(this);


		//kontrollPanel.add(sizesLabel);
		kontrollPanel.add(sizes);

		if(selectedGame.hasStartStopButtons()) {
			kontrollPanel.add(btnStart);
			kontrollPanel.add(btnStop);
		}

		if(selectedGame.hasStepButton()) {
			kontrollPanel.add(btnStep);
		}
		
		if(gameMenu != null) {
			gameMenu.removeActionListener(this);
			gameMenu = null;
		}
		List<String> choices = selectedGame.getMenuChoices();
		if(choices != null) {
			gameMenu = new JComboBox<String>(choices.toArray(new String[choices.size()]));
			gameMenu.addActionListener(this);
			kontrollPanel.add(gameMenu);
		}

	}

	private void initializeBoard() {
		int width = selectedGame.getWidth();
		int height = selectedGame.getHeight();

		hovedPanel.removeAll();
		hovedPanel.setLayout(new GridLayout(height+1, width+1));
		buttonMap.clear();

		for (int y = 0; y < height; y++){
			hovedPanel.add(new CoordLabel(height-y-1));
			for (int x = 0; x < width; x++) {
				JPanel panel = new JPanel(new BorderLayout());
				JButton button = new JButton();
				button.addActionListener(this);
				button.setMargin(new Insets(0,0,0,0));
				button.setContentAreaFilled(false);

				panel.add(button);
				panel.setOpaque(false);
				hovedPanel.add(panel);
				buttonMap.put(button, new Point(x, height-y-1));
			}
		}

		hovedPanel.add(new CoordLabel(""));
		for (int x = 0; x < width; x++)
			hovedPanel.add(new CoordLabel(x));
		oppdater();
		updateFrame();
	}

	private void updateFrame() {
		Container parent = getParent();
		while(parent != null) {
		if(parent instanceof JFrame) {
			JFrame frame = (JFrame)parent;
			frame.setTitle(selectedGame.getName());
			frame.pack();
			return;
		}
		else if(parent instanceof JApplet) {
			JApplet applet = (JApplet)parent;
			applet.setName(selectedGame.getName());
			applet.resize(getPreferredSize());
			applet.validate();
			return;
		}
		parent = parent.getParent();
		}

	}
	/**
	 * Går gjennom og oppdaterer grafikken til alle brikkene, og viser oppdatert informasjon i displayet
	 */
	private void oppdater() {
		for(Entry<JButton, Point> e : buttonMap.entrySet()) {
			e.getKey().setIcon(ImageLoader.getImage(selectedGame.getIconAt(e.getValue().x, e.getValue().y)));
		}
	}

	/** 
	 * Denne blir kalt av Java hver gang brukeren trykker på en knapp, eller hver gang
	 * timer-signalet avfyres.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnNew) {
			selectedGame.newGame();
			oppdater();
		}
		else if(e.getSource() == btnStop) {
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			timer.stop();
		}
		else if(e.getSource() == btnStart) {
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			timer.start();
		}
		else if(e.getSource() == btnStep) {
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			timer.stop();
			selectedGame.timeStep();
			oppdater();
		}
		else if(e.getSource() == timer) {
			selectedGame.timeStep();
			oppdater();
			timer.restart();
		}
		else if(e.getSource() == sizes) {
			String size = (String)sizes.getSelectedItem();
			Point p = boardSize(size);
			selectedGame.setSize(p.x, p.y);
			if(!selectedGame.canChangeSize())
				selectedGame.newGame();
			initializeBoard();
			oppdater();
		}
		else if(e.getSource() == gameMenu) {
			selectedGame.setMenuChoice((String) gameMenu.getSelectedItem());
			initializeBoard();
			oppdater();
		}
		else if(e.getSource() instanceof JButton) {
			Point point = buttonMap.get(e.getSource());
			selectedGame.select(point.x, point.y);
			oppdater();
		}
	}

	private static Point boardSize(String size) {
		String[] split = size.split("x");
		if(split.length != 2)
			throw new IllegalArgumentException("Should be on WxH: " + size);
		return new Point(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
	}

	private static final class GameComparator implements Comparator<IGame>, Serializable {
		private static final long serialVersionUID = 6647481037039732094L;

		@Override
		public int compare(IGame arg0, IGame arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}

}

