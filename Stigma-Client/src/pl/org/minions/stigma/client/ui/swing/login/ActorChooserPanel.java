/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.client.ui.swing.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.observers.AvailableActorsObserver;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.CurrencyPanel;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.i18n.StandardText;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used for choosing actors.
 */
public class ActorChooserPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final Icon BUNNY =
            new ImageIcon(VisualizationGlobals.BUNNY_IMAGE);
    private static final Icon REFRESH_ICON =
            Resourcer.loadIcon("img/client/icons/refresh.png");

    private static final int PANEL_X = 200;
    private static final int PANEL_Y = 150;
    private static final int PANEL_HEIGHT = 325;
    private static final int PANEL_WIDTH = 400;

    @Translated
    private static String NAME = "Choose Player";
    @Translated(name = "STRENGTH")
    private static String STRENGTH = "Strength";
    @Translated(name = "WILLPOWER")
    private static String WILLPOWER = "Willpower";
    @Translated(name = "AGILITY")
    private static String AGILITY = "Agility";
    @Translated(name = "FINESSE")
    private static String FINESSE = "Finesse";
    @Translated
    private static String ACTOR_UNAVAIABLE_LABEL =
            "Player is unavailable, please wait.";
    @Translated(name = "LEVEL")
    private static String LEVEL = "Level";
    @Translated(name = "XP")
    private static String XP = "XP";

    private JButton okButton;
    private JButton refreshButton;
    private JLabel refreshActorInfoLabel;

    private Actor selectedActor;

    private JPanel actorListPanel;

    private JPanel statsPanel;

    private JLabel nameLabel;
    private JLabel levelLabel;
    private JLabel strLabel;
    private JLabel strValueLabel;
    private JLabel agiLabel;
    private JLabel agiValLabel;
    private JLabel silhouetteLabel;
    private JLabel wilLabel;
    private JLabel wilValueLabel;
    private JLabel finLabel;
    private JLabel finValueLabel;
    private JLabel xpLabel;
    private CurrencyPanel goldPanel;

    /**
     * This is the default constructor.
     */
    public ActorChooserPanel()
    {
        super();
        initialize();
        Client.globalInstance()
              .addAvailableActorsObserver(new AvailableActorsObserver()
              {
                  @Override
                  public void playerAvailableActorsChange()
                  {
                      getActors();
                  }
              });
    }

    /**
     * Initializes panel.
     */
    private void initialize()
    {
        //CHECKSTYLE:OFF
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        silhouetteLabel = new JLabel();
        silhouetteLabel.setBounds(new Rectangle(128, 75, 32, 32));
        silhouetteLabel.setIcon(BUNNY);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLocation(PANEL_X, PANEL_Y);
        this.setBorder(BorderFactory.createEtchedBorder());

        okButton = new JButton();
        okButton.setText(StandardText.OK.get());
        okButton.setMargin(new Insets(0, 0, 0, 0)); // for visual editor only...
        okButton.setBounds(new Rectangle(164, 275, 36, 36));
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                okButtonPressedActionPerformed();
            }
        });
        okButton.setEnabled(false);

        refreshButton = new JButton();
        refreshButton.setBounds(new Rectangle(210, 275, 36, 36));
        refreshButton.setIcon(REFRESH_ICON);
        refreshButton.setMargin(new Insets(0, 0, 0, 0));
        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Client.globalInstance().requestDisabledActorsListUpdate();
            }
        });

        refreshActorInfoLabel = new JLabel();
        refreshActorInfoLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        refreshActorInfoLabel.setForeground(Color.red);
        refreshActorInfoLabel.setBounds(new Rectangle(105, 170, 190, 16));
        refreshActorInfoLabel.setText(ACTOR_UNAVAIABLE_LABEL);

        this.add(okButton, null);
        this.add(refreshButton, null);
        this.add(refreshActorInfoLabel, null);
        this.add(getActorListPanel(), null);
        this.add(getStatsPanel(), null);
        this.add(silhouetteLabel, null);
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        this.getActionMap().put("enter", new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (okButton.isEnabled())
                {
                    okButtonPressedActionPerformed();
                }
            }
        });
    }

    private void okButtonPressedActionPerformed()
    {
        Client.globalInstance().sendChosenActorId(selectedActor.getId());
    }

    /**
     * Gets list of actors to display on panel.
     */
    public void getActors()
    {
        Collection<Actor> localActors =
                Client.globalInstance().getPlayerAvailableActors();
        actorListPanel.removeAll();
        int selectedId = -1;
        if (selectedActor != null)
            selectedId = selectedActor.getId();
        selectedActor = null;
        ButtonGroup grp = new ButtonGroup();
        for (final Actor a : localActors)
        {
            JToggleButton btn = new JToggleButton(a.getName(), BUNNY);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            Dimension d = new Dimension(49, 61);
            btn.setMinimumSize(d);
            btn.setPreferredSize(d);

            btn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    selectActor(a);
                }
            });

            grp.add(btn);

            if (selectedActor == null || a.getId() == selectedId)
            {
                selectActor(a);
                btn.setSelected(true);
            }

            actorListPanel.add(btn);
        }
        actorListPanel.validate();
    }

    /**
     * Returns default panel name.
     * @return panel name
     */
    public static String getDefaultName()
    {
        return NAME;
    }

    /**
     * Returns default panel height.
     * @return panel height
     */
    public static int getDefaultHeight()
    {
        return PANEL_HEIGHT;
    }

    /**
     * Returns default panel width.
     * @return panel width
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * Returns default panel position X coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionX()
    {
        return PANEL_X;
    }

    /**
     * Returns default panel position Y coordinate.
     * @return X coordinate
     */
    public static int getDefaultPositionY()
    {
        return PANEL_Y;
    }

    private void selectActor(Actor a)
    {
        selectedActor = a;
        boolean b = Client.globalInstance().isPlayerActorEnabled(a.getId());
        okButton.setEnabled(b);
        refreshActorInfoLabel.setVisible(!b);
        nameLabel.setText(a.getName());
        levelLabel.setText(LEVEL + " " + a.getLevel());
        strValueLabel.setText(String.valueOf(a.getStrength()));
        agiValLabel.setText(String.valueOf(a.getAgility()));
        wilValueLabel.setText(String.valueOf(a.getWillpower()));
        finValueLabel.setText(String.valueOf(a.getFinesse()));
        xpLabel.setText(String.format("%d/%d %s",
                                      a.getExperience(),
                                      a.getExperienceToNextLevel(),
                                      XP));
        goldPanel.setValue(a.getMoney());
    }

    private JPanel getActorListPanel()
    {
        if (actorListPanel == null)
        {
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 10, 0);
            actorListPanel = new JPanel();
            actorListPanel.setBounds(new Rectangle(10, 190, 380, 75));
            actorListPanel.setLayout(flowLayout);
            actorListPanel.setName("RoundedPanel");

            JButton exampleBtn = new JButton("Example", BUNNY);
            exampleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            exampleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
            Dimension d = new Dimension(49, 61);
            exampleBtn.setMinimumSize(d);
            exampleBtn.setPreferredSize(d);
            actorListPanel.add(exampleBtn);
        }
        return actorListPanel;
    }

    /**
     * This method initializes statsPanel
     * @return javax.swing.JPanel
     */
    private JPanel getStatsPanel()
    {
        if (statsPanel == null)
        {
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.gridwidth = 2;
            gridBagConstraints16.gridy = 7;
            goldPanel = new CurrencyPanel();
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridwidth = 2;
            gridBagConstraints15.insets = new Insets(10, 0, 0, 0);
            gridBagConstraints15.gridy = 6;
            xpLabel = new JLabel();
            xpLabel.setText(XP);
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 1;
            gridBagConstraints14.anchor = GridBagConstraints.WEST;
            gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints14.gridy = 5;
            finValueLabel = new JLabel();
            finValueLabel.setText("FIN");
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.anchor = GridBagConstraints.WEST;
            gridBagConstraints13.gridy = 5;
            finLabel = new JLabel();
            finLabel.setText(FINESSE);
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.anchor = GridBagConstraints.WEST;
            gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints12.gridy = 4;
            wilValueLabel = new JLabel();
            wilValueLabel.setText("WIL");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            gridBagConstraints10.gridy = 4;
            wilLabel = new JLabel();
            wilLabel.setText(WILLPOWER);
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 1;
            gridBagConstraints9.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridy = 3;
            agiValLabel = new JLabel();
            agiValLabel.setText("AGI");
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.anchor = GridBagConstraints.WEST;
            gridBagConstraints8.gridy = 3;
            agiLabel = new JLabel();
            agiLabel.setText(AGILITY);
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints7.gridy = 2;
            strValueLabel = new JLabel();
            strValueLabel.setText("STR");
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.gridy = 2;
            strLabel = new JLabel();
            strLabel.setText(STRENGTH);
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.insets = new Insets(0, 0, 10, 0);
            gridBagConstraints5.gridy = 1;
            levelLabel = new JLabel();
            levelLabel.setText(LEVEL);
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.insets = new Insets(0, 0, 2, 0);
            gridBagConstraints4.gridy = 0;
            nameLabel = new JLabel();
            nameLabel.setText("Name");
            nameLabel.setName("BigLabel");
            statsPanel = new JPanel();
            statsPanel.setLayout(new GridBagLayout());
            statsPanel.setBounds(new Rectangle(200, 13, 114, 148));
            statsPanel.add(nameLabel, gridBagConstraints4);
            statsPanel.add(levelLabel, gridBagConstraints5);
            statsPanel.add(strLabel, gridBagConstraints6);
            statsPanel.add(strValueLabel, gridBagConstraints7);
            statsPanel.add(agiLabel, gridBagConstraints8);
            statsPanel.add(agiValLabel, gridBagConstraints9);
            statsPanel.add(wilLabel, gridBagConstraints10);
            statsPanel.add(wilValueLabel, gridBagConstraints12);
            statsPanel.add(finLabel, gridBagConstraints13);
            statsPanel.add(finValueLabel, gridBagConstraints14);
            statsPanel.add(xpLabel, gridBagConstraints15);
            statsPanel.add(goldPanel, gridBagConstraints16);
        }
        return statsPanel;
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
