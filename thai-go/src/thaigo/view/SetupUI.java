package thaigo.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import thaigo.network.client.Client;
import thaigo.network.server.ClientConnectionFactory;
import thaigo.network.server.Server;
import thaigo.utility.CenterDeterminer;
import thaigo.utility.PortChecker;
import thaigo.utility.PropertyManager;

import com.lloseng.ocsf.server.AbstractConnectionFactory;
/** SetupUI to create the connection between Server and client.
 * 
 * @author Nol & Poramate
 *
 */
public class SetupUI extends DialogFrame {
	
	private JTabbedPane tabs;
	private JPanel host,join;
	private GameUI ui;
	// HOST Components
	private JPanel hostInputPane, hostButtonPane;
	private JButton checkPort, createGame;
	private JTextField ipHost, hostName, hostPort;
	private JLabel name,ip,port;
	private JPanel hStatusPane;
	private JScrollPane hStatusScroll;
	private JTextArea hStatusDisplay;
	private Server server;
	// JOIN Component
	private JPanel joinInputPane, joinButtonPane;
	private JButton joinGame;
	private JTextField ipJoin, joinName, joinPort;
	private JLabel jname,jip,jport;
	private JPanel jStatusPane;
	private JScrollPane jStatusScroll;
	private JTextArea jStatusDisplay;
	private Client client;
	private static SetupUI instance;
	
	/** Get the instance of this object if it's already declared.
	 *  Singleton so user can't call many SetupUI window.
	 * 
	 * @param ui GameUI
	 * @return instance of this object
	 */
	public static SetupUI getInstance(GameUI ui){
		if( instance == null )
			instance = new SetupUI(ui);
		return instance;
	}
	
	/** Initializes the <code>SetupUI</code> window.
	 * 
	 * @param ui The GameUI that call this
	 */
	private SetupUI( GameUI ui ) {
		super("Setup The Game");
		setBounds(200, 200, 0, 0);
		this.ui = ui;
		initComponents();
	}
	
	/** Initialize the components. */
	private void initComponents() {
		GridBagLayout gbl = new GridBagLayout();
		tabs = new JTabbedPane();
		host = new JPanel();
		join = new JPanel();
		BoxLayout bl = new BoxLayout(host, BoxLayout.Y_AXIS);
		host.setLayout(bl);
		bl = new BoxLayout(join, BoxLayout.Y_AXIS);
		join.setLayout(bl);
		tabs.addTab("Host", host);
		tabs.addTab("Join", join);
		this.add(tabs);
		name = new JLabel("Player Name : ");
		name.setHorizontalAlignment(4);
		ip = new JLabel("IP Adress : ");
		ip.setHorizontalAlignment(4);
		port = new JLabel("Port : ");
		port.setHorizontalAlignment(4);
		name.setPreferredSize( new Dimension(90,15));
		ip.setPreferredSize( new Dimension(90,15));
		port.setPreferredSize( new Dimension(90,15));
		hostName = new JTextField(25);
		hostName.setHorizontalAlignment( JTextField.CENTER );
		ipHost = new JTextField(15);
		try {
			ipHost.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			addServerStatus(e1.toString());
		}
		ipHost.setEditable(false);
		hostPort = new JTextField(5);
		checkPort = new JButton("Check");
		checkPort.setToolTipText("Check This Port!");
		checkPort.setPreferredSize(new Dimension(110,15));
		
		checkPort.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validPort;
				Integer port = null;
				try{
					port = Integer.parseInt( hostPort.getText() );
					validPort = true;
				}
				catch( Exception ex ){
					JOptionPane.showMessageDialog(getThis(), "Port must be in number format.", "Invalid Inut", JOptionPane.ERROR_MESSAGE);
					validPort = false;
				}
				if( validPort ){
					if( PortChecker.checkAvailable(port) ){
						JOptionPane.showMessageDialog(getThis(), String.format("Server is listening on port [%d]", port), "Port Available", JOptionPane.PLAIN_MESSAGE);
					}
					else{
						JOptionPane.showMessageDialog(getThis(), String.format("<html>Sorry!<br>Port [%d] is not available.</html>", port), "Port is Unavailable", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		
		hostInputPane = new JPanel(gbl);
		hostInputPane.setBorder(new TitledBorder("Infromation"));
		hostInputPane.add(name, setPosition(0,0,1,0,5,15,20,6));
		hostInputPane.add(ip, setPosition(0,1,1,0,5,15,10,6));
		hostInputPane.add(port, setPosition(0,2,1,0,5,15,5,6));
		hostInputPane.add(hostName , setPosition(1,0,4,2,5,5,20,10));
		hostInputPane.add(ipHost , setPosition(1,1,3,2,5,5,10,5));
		hostInputPane.add(hostPort , setPosition(1,2,1,0,5,5,5,10));
		hostInputPane.add(checkPort , setPosition(2,2,1,2,5,5,10,10));
		
		host.add( hostInputPane );
		
		hStatusPane = new JPanel();
		hStatusDisplay = new JTextArea();
		hStatusDisplay.setBackground( new Color(190,200,255) );
		hStatusDisplay.setEditable(false);
		hStatusScroll = new JScrollPane(hStatusDisplay , 20 , 30);
		hStatusScroll.setPreferredSize( new Dimension(400,100) );
		hStatusPane.setBorder(new TitledBorder("Connection Status"));
		hStatusPane.add(hStatusScroll);
		host.add(hStatusPane);
		hostButtonPane = new JPanel();
		createGame = new JButton("CREATE !");
		createGame.addActionListener(new CreateGameAction());
		hostButtonPane.add(createGame);
		host.add(hostButtonPane);
		
		
		
		joinInputPane = new JPanel(gbl);
		jname = new JLabel("Player Name : ");
		jname.setHorizontalAlignment(4);
		jip = new JLabel("IP Adress : ");
		jip.setHorizontalAlignment(4);
		jport = new JLabel("Port : ");
		jport.setHorizontalAlignment(4);
		jname.setPreferredSize( new Dimension(90,15));
		jip.setPreferredSize( new Dimension(90,15));
		jport.setPreferredSize( new Dimension(90,15));
		joinName = new JTextField(25);
		joinName.setHorizontalAlignment( JTextField.CENTER );
		ipJoin = new JTextField(15);
		joinPort = new JTextField(5);
		joinInputPane.setBorder(new TitledBorder("Infromation"));
		joinInputPane.add(jname, setPosition(0,0,1,0,5,15,20,6));
		joinInputPane.add(jip, setPosition(0,1,1,0,5,15,10,6));
		joinInputPane.add(jport, setPosition(0,2,1,0,5,15,5,6));
		joinInputPane.add(joinName , setPosition(1,0,4,2,5,5,20,10));
		joinInputPane.add(ipJoin , setPosition(1,1,3,2,5,5,10,5));
		joinInputPane.add(joinPort , setPosition(1,2,1,0,5,5,5,10));
		join.add(joinInputPane);
		/////////////////////////////////////
		joinName.setText("Client");
		ipJoin.setText("127.0.0.1");
		joinPort.setText("3311");
		/////////////////////////////////////
		hostName.setText("Server");
		//ipJoin.setText("127.0.0.1");
		hostPort.setText("3311");
		jStatusPane = new JPanel();
		jStatusDisplay = new JTextArea();
		jStatusDisplay.setBackground( new Color(190,200,255) );
		jStatusDisplay.setEditable(false);
		jStatusScroll = new JScrollPane(jStatusDisplay , 20 , 30);
		jStatusScroll.setPreferredSize( new Dimension(400,100) );
		jStatusPane.setBorder(new TitledBorder("Connection Status"));
		jStatusPane.add(jStatusScroll);
		join.add(jStatusPane);
		joinButtonPane = new JPanel();
		joinGame = new JButton("JOIN !");
		joinGame.addActionListener(new JoinGameAction());
		joinButtonPane.add(joinGame);
		join.add(joinButtonPane);
	}
	
	/** Shows the window at the middle of the GameUI. */
	public void run() {
		this.pack();
		this.setResizable(false);
		this.setLocation( CenterDeterminer.determine(ui,this) );
		this.setVisible(true);
	}
	
	/** Creates a new GridBagConstraints to set the position of components for the container that use GridBagLayout.
	 * 
	 * @param x X-Axis
	 * @param y Y-Axis
	 * @param width Width
	 * @param fill Fill the grid
	 * @param top Top insets
	 * @param left Left insets
	 * @param bottom Bottom insets
	 * @param right Right insets
	 * @return GridBagConstaints
	 */
	private GridBagConstraints setPosition(int x, int y, int width, int fill, int top, int left, int bottom, int right) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.fill = fill;
		c.insets = new Insets(top,left,bottom,right);
		return c;
	}

	/** Adds connection status to host status display area (<code>hStatusDisplay</code>).
	 * 
	 * @param text Status
	 */
	public void addServerStatus(String text) {
		hStatusDisplay.setText(hStatusDisplay.getText() + "\n" + text);
	}
	
	/** Adds connection status to client status display area (<code>jStatusDisplay</code>).
	 * 
	 * @param text Status
	 */
	public void addClientStatus(String text) {
		jStatusDisplay.setText(jStatusDisplay.getText() + "\n" + text);
	}
	
	/** Gets this user interface.
	 * 
	 * @return this object
	 */
	private SetupUI getThis() {
		return this;
	}
	
	/** Closes the setup window. */
	public void close(){
		dispose();
	}
	
	/** Enable or disable this setup ui.
	 * 
	 * @param bool True to enable, flase to disable
	 */
	private void setConnected(boolean bool){
		joinName.setEnabled(!bool);
		ipJoin.setEnabled(!bool);
		joinPort.setEnabled(!bool);
		joinGame.setEnabled(!bool);
		
		hostName.setEnabled(!bool);
		ipHost.setEnabled(!bool);
		hostPort.setEnabled(!bool);
		createGame.setEnabled(!bool);
		checkPort.setEnabled(!bool);
	}
	
	/** Listener for <code>CREATE</code> button. */
	class CreateGameAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// add properties
			PropertyManager.setProperty("mode", "server");
			PropertyManager.setProperty("thaigo.pawn.currentmodel", "Classic");
			PropertyManager.setProperty("thaigo.board.currentcolor", "Wood");
			PropertyManager.setProperty("table", "8");
			PropertyManager.setProperty("player", hostName.getText());
			
			try {
				server = Server.getInstance(Integer.parseInt(hostPort.getText()), getThis(), ui);
				server.setBacklog(1);

				AbstractConnectionFactory factory = new ClientConnectionFactory();
				server.setConnectionFactory(factory);

				try {
					server.listen();
					// change status to "Created"
					addServerStatus(String.format("Network : Created"));
					// disable create system
					hostName.setEnabled(false);
					ipJoin.setEnabled(false);
					hostPort.setEnabled(false);
					createGame.setEnabled(false);

					tabs.remove(join);
					
				} catch (IOException e) {
					// change status to "Failed"
					addServerStatus(String.format("Network : Creation failed"));
				}
			} catch (NumberFormatException e) {
				addServerStatus(e.toString());
			} catch (IllegalArgumentException e) {
				addServerStatus(e.toString());
			}
		}	
	}
	
	/** Listener for <code>JOIN</code> button. */
	class JoinGameAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// add properties
			PropertyManager.setProperty("mode", "client");
			PropertyManager.setProperty("thaigo.pawn.currentmodel", "Classic");
			PropertyManager.setProperty("thaigo.board.currentcolor", "Wood");
			PropertyManager.setProperty("table", "8");
			PropertyManager.setProperty("player", joinName.getText());
			
			try {
				client = Client.getInstance(ipJoin.getText(), Integer.parseInt(joinPort.getText()), getThis(), ui);
				addClientStatus("client is created");
				try {
					client.openConnection();
					addClientStatus("openConnection");
					// change status to "Connected"
					addClientStatus(String.format("Network : Connected to server %s:%s", ipJoin.getText(), joinPort.getText()));
					// disable join system
					setConnected(true);
					
				} catch (IOException e) {
					// change status to "Failed"
					addClientStatus(String.format("Network : Connection to %s:%s failed", ipJoin.getText(), joinPort.getText()));
				}
			} catch (NumberFormatException e) {
				addClientStatus(e.toString());
			}
		}
	}
}
