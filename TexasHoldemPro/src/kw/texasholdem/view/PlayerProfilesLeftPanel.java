package kw.texasholdem.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kw.texasholdem.ai.impl.Player;
import kw.texasholdem.config.AppConfig;
import kw.texasholdem.tool.SerializableHashMap;
import kw.texasholdem.util.ResourceManager;

public class PlayerProfilesLeftPanel extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1697992749327670018L;
	/** The players at the table. */
    private LinkedHashMap<String, Player> players = null;	//This will be copied from somewhere
    private List<PlayerPhotoPanel> ppp = new ArrayList<PlayerPhotoPanel>();
    
    private static final Icon PLAYER_DEFAULT_MALE_PROFILE =
            ResourceManager.getIcon("/images/male.jpg");
    private static final Icon PLAYER_DEFAULT_FEMALE_PROFILE =
            ResourceManager.getIcon("/images/female.jpg");
	
	public PlayerProfilesLeftPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setLayout(null);
		setPreferredSize(new Dimension(223, AppConfig.APP_HEIGHT_2));
	}

	public PlayerProfilesLeftPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public PlayerProfilesLeftPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public PlayerProfilesLeftPanel(LayoutManager layout,
			boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	private void addPlayer(Player player) {
		PlayerPhotoPanel p = new PlayerPhotoPanel(player);
		this.add(p);
		ppp.add(p);
	}


	public void addAllPlayers(List<Player> ps) {
		LinkedHashMap<String, Player> pps = new LinkedHashMap<String, Player>();
		for (Player p : ps) {
			pps.put(p.getName(), p);
		}
		addAllPlayers(pps);
	}
	
	public void addAllPlayers(LinkedHashMap<String, Player> ps) {
		this.players = ps;
		for (Player p : players.values()) {
			addPlayer(p);
		}
		JPanel pholder = new JPanel();
		pholder.setPreferredSize(new Dimension(10, 1000));
		this.add(pholder);
	}
	
	private static class PlayerPhotoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7107544354187583011L;
		String url;
		Player player;
		JLabel pic = new JLabel();
		private JLabel mNameLabel;
		private JPanel minfo;
		private JLabel mStatusLabel;
		PlayerPhotoPanel(Player p) {
			if(p.getProfileURL() != null) {
				 Icon profileIcon = ResourceManager.getIcon(p.getProfileURL());
				pic.setIcon(profileIcon);
			} else {
				if(p.isMale()) {
					pic.setIcon(PLAYER_DEFAULT_MALE_PROFILE);
				} else {
					pic.setIcon(PLAYER_DEFAULT_FEMALE_PROFILE);
				}
			}
			player = p;
			
			this.add(pic);
			
			minfo = new JPanel();
			mNameLabel = new JLabel(p.getName());
			minfo.add(mNameLabel);
			mStatusLabel = new JLabel();
			minfo.add(mStatusLabel);
			minfo.setPreferredSize(new Dimension(50, 50));
			this.add(minfo);
			
			this.setPreferredSize(new Dimension(100, 100));
			
			//this.setBackground(Color.BLACK);
		}
		
		public void update(Player player2) {
			this.player = player2;
			if(player.isDealer())
				mStatusLabel = new JLabel("Dealer");
			else if (player.isSmallBlind())
				mStatusLabel = new JLabel("Small Blind");
			else if (player.isBigBlind())
				mStatusLabel = new JLabel("Big Blind");
			else
				mStatusLabel = new JLabel("");
			
		}
		
		public void setSelected(boolean isInTurn, Player actor) {
			//update(actor);
			if(isInTurn) {
				this.setBackground(Color.BLACK);
				this.minfo.setBackground(Color.BLACK);
				this.mNameLabel.setForeground(Color.red);
				this.mStatusLabel.setForeground(Color.red);
			} else {
				this.setBackground(null);
				this.minfo.setBackground(null);
				this.mNameLabel.setForeground(null);
				this.mStatusLabel.setForeground(null);
			}
		}
		
		
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

	public void setInTurn(String actorName, boolean isInTurn, Player actor) {
		for (PlayerPhotoPanel p : ppp) {
			if(p.player.getName().equalsIgnoreCase(actorName)) {
				p.setSelected(isInTurn, actor);
			}
		}
	}

	public void setLeftProfilePanelVisible(String playerName, boolean b) {
		for (PlayerPhotoPanel p : ppp) {
			if(p.player.getName().equalsIgnoreCase(playerName)) {
				p.setVisible(b);
			}
		}
	}

	public void resetAllLeftProfilePanel(boolean b) {
		for (PlayerPhotoPanel p : ppp) {
			p.setVisible(b);
		}
	}

	public void updateLeftPlayerPanel(Player player) {
		for (PlayerPhotoPanel p : ppp) {
			if(p.player.getName().equalsIgnoreCase(player.getName())) {
				p.update(player);
			}
		}	
	}


}
