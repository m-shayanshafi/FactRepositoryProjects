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
package pl.org.minions.stigma.client.ui.swing.game.components.actors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.ActorStatusPanel;
import pl.org.minions.stigma.client.ui.swing.game.components.ImagePane;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.i18n.Translated;

/**
 * Component for displaying actor's data.
 */
public class ActorStatisticPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    @Translated(name = "STRENGTH")
    private static String STRENGTH = "Strength";
    @Translated(name = "WILLPOWER")
    private static String WILLPOWER = "Willpower";
    @Translated(name = "AGILITY")
    private static String AGILITY = "Agility";
    @Translated(name = "FINESSE")
    private static String FINESSE = "Finesse";

    @Translated
    private static String GREATER = "Greater";
    @Translated
    private static String LESS = "Less";
    @Translated
    private static String SIMILIAR = "Similar";

    private static final String SUFFIX = ": ";

    private static final String[] COMPARE =
    { LESS, SIMILIAR, GREATER };
    private static final Color[] COMPARE_COLOR =
    { Color.blue, Color.white, Color.red };

    @Translated
    private static String DESCRIPTION = "Description";
    private JLabel nameLabel;
    private JPanel avatarPanel;
    private JPanel silhouettePanel;
    private JLabel strLabel;
    private JLabel agilLabel;
    private JLabel shortDescLabel;
    private JLabel wilLabel;
    private JLabel finLabel;
    private JTextArea descriptionTextField;
    private JLabel descriptionLabel;
    private ActorStatusPanel actorStatusPanel;
    private JLabel strValueLabel;
    private JLabel agiValueLabel;
    private JLabel wilValueLabel;
    private JLabel finValueLabel;

    /**
     * Constructor.
     */
    public ActorStatisticPanel()
    {
        super();
        initialize();
    }

    /**
     * Constructor.
     * @param a
     *            actor for which component should be
     *            created
     */
    public ActorStatisticPanel(Actor a)
    {
        this();
        update(a);
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 2;
        gridBagConstraints41.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints41.insets = new Insets(0, 0, 0, 45);
        gridBagConstraints41.gridy = 6;
        finValueLabel = new JLabel();
        finValueLabel.setText("Fin");
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 2;
        gridBagConstraints31.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints31.insets = new Insets(0, 0, 0, 45);
        gridBagConstraints31.gridy = 5;
        wilValueLabel = new JLabel();
        wilValueLabel.setText("Wil");
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 2;
        gridBagConstraints21.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints21.insets = new Insets(0, 0, 0, 45);
        gridBagConstraints21.gridy = 4;
        agiValueLabel = new JLabel();
        agiValueLabel.setText("Agi");
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 2;
        gridBagConstraints12.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints12.insets = new Insets(0, 0, 0, 45);
        gridBagConstraints12.gridy = 3;
        strValueLabel = new JLabel();
        strValueLabel.setText("Str");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 3;
        gridBagConstraints9.anchor = GridBagConstraints.NORTH;
        gridBagConstraints9.gridheight = 2;
        gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints9.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints9.gridy = 3;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 1;
        gridBagConstraints8.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints8.insets = new Insets(20, 5, 0, 0);
        gridBagConstraints8.gridy = 7;
        descriptionLabel = new JLabel();
        descriptionLabel.setText(DESCRIPTION);
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints7.gridy = 8;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints7.insets = new Insets(0, 15, 0, 0);
        gridBagConstraints7.gridx = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 1;
        gridBagConstraints6.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints6.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints6.gridy = 6;
        finLabel = new JLabel();
        finLabel.setText(FINESSE + SUFFIX);
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints5.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints5.gridy = 5;
        wilLabel = new JLabel();
        wilLabel.setText(WILLPOWER + SUFFIX);
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridwidth = 3;
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints4.gridy = 1;
        shortDescLabel = new JLabel();
        shortDescLabel.setText("short desc");
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints3.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints3.gridy = 4;
        agilLabel = new JLabel();
        agilLabel.setText(AGILITY + SUFFIX);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints2.gridy = 3;
        strLabel = new JLabel();
        strLabel.setText(STRENGTH + SUFFIX);
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints11.gridy = 3;
        gridBagConstraints11.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints11.gridheight = 7;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridy = 0;
        nameLabel = new JLabel();
        nameLabel.setText("Name");
        nameLabel.setName("BigLabel");
        this.setLayout(new GridBagLayout());
        this.setSize(300, 250);
        this.add(getAvatarPanel(), gridBagConstraints1);
        this.add(getSilhouettePanel(), gridBagConstraints11);
        this.add(strLabel, gridBagConstraints2);
        this.add(agilLabel, gridBagConstraints3);
        this.add(wilLabel, gridBagConstraints5);
        this.add(finLabel, gridBagConstraints6);
        this.add(nameLabel, gridBagConstraints);
        this.add(shortDescLabel, gridBagConstraints4);
        this.add(getDescriptionTextField(), gridBagConstraints7);
        this.add(descriptionLabel, gridBagConstraints8);
        this.add(getActorStatusPanel(), gridBagConstraints9);
        this.add(strValueLabel, gridBagConstraints12);
        this.add(agiValueLabel, gridBagConstraints21);
        this.add(wilValueLabel, gridBagConstraints31);
        this.add(finValueLabel, gridBagConstraints41);
    }

    /**
     * This method initializes avatarPanel
     * @return javax.swing.JPanel
     */
    private JPanel getAvatarPanel()
    {
        if (avatarPanel == null)
        {
            avatarPanel = new JPanel();
            final Dimension d = new Dimension(49, 61);
            avatarPanel.setLayout(null);
            avatarPanel.setMinimumSize(d);
            avatarPanel.setMaximumSize(d);
            avatarPanel.setPreferredSize(d);
            avatarPanel.setSize(d);
            avatarPanel.setName("RoundedPanel");
        }
        return avatarPanel;
    }

    /**
     * This method initializes silhouettePanel
     * @return javax.swing.JPanel
     */
    private JPanel getSilhouettePanel()
    {
        if (silhouettePanel == null)
        {
            silhouettePanel = new JPanel();
            silhouettePanel.setLayout(new BoxLayout(getSilhouettePanel(),
                                                    BoxLayout.Y_AXIS));
            final Dimension d = new Dimension(64, 150);
            silhouettePanel.setSize(d);
            silhouettePanel.setPreferredSize(d);
            silhouettePanel.setMinimumSize(d);

            ImagePane p = new ImagePane();
            p.setImage(VisualizationGlobals.BUNNY_IMAGE);

            silhouettePanel.add(Box.createVerticalGlue());
            silhouettePanel.add(p, null);
        }
        return silhouettePanel;
    }

    /**
     * This method initializes descriptionTextField
     * @return javax.swing.JTextField
     */
    private JTextArea getDescriptionTextField()
    {
        if (descriptionTextField == null)
        {
            descriptionTextField = new JTextArea();
            descriptionTextField.setText(DESCRIPTION);
            descriptionTextField.setEditable(false);
            descriptionTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        return descriptionTextField;
    }

    private ActorStatusPanel getActorStatusPanel()
    {
        if (actorStatusPanel == null)
        {
            // CHECKSTYLE:OFF
            actorStatusPanel = new ActorStatusPanel();
            actorStatusPanel.setPreferredSize(new Dimension(49, 20));
            // CHECKSTYLE:ON
        }
        return actorStatusPanel;
    }

    private int compare(int playerValue, int otherValue)
    {
        // CHECKSTYLE:OFF
        final int tenPercent = playerValue * 10 / 100;
        // CHECKSTYLE:ON

        if (otherValue > playerValue + tenPercent)
            return 2;
        if (otherValue < playerValue - tenPercent)
            return 0;
        return 1;
    }

    private void statsHelper(JLabel label, int value, int otherValue)
    {
        int cmp = compare(value, otherValue);
        label.setText(COMPARE[cmp]);
        label.setForeground(COMPARE_COLOR[cmp]);
    }

    /**
     * Updates component with data of given actor.
     * @param a
     *            actor to display
     */
    public void update(Actor a)
    {
        Actor playerActor = Client.globalInstance().getPlayerActor();

        nameLabel.setText(a.getName());
        if (a.getShortDescription() != null)
            shortDescLabel.setText(a.getShortDescription());
        else
            shortDescLabel.setText("");
        statsHelper(strValueLabel, playerActor.getStrength(), a.getStrength());
        statsHelper(agiValueLabel, playerActor.getAgility(), a.getAgility());
        statsHelper(wilValueLabel, playerActor.getWillpower(), a.getWillpower());
        statsHelper(finValueLabel, playerActor.getFinesse(), a.getFinesse());
        actorStatusPanel.update(a);

        if (a.getDescription() == null)
        {
            descriptionLabel.setVisible(false);
            descriptionTextField.setVisible(false);
        }
        else
        {
            descriptionTextField.setText(a.getDescription());
            descriptionLabel.setVisible(true);
            descriptionTextField.setVisible(true);
        }
    }
} //  @jve:decl-index=0:visual-constraint="261,31"
