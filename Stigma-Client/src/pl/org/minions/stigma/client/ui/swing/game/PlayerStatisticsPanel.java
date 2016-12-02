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
package pl.org.minions.stigma.client.ui.swing.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ItemDroppedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ItemPickedUpListener;
import pl.org.minions.stigma.client.ui.swing.game.components.CurrencyPanel;
import pl.org.minions.stigma.client.ui.swing.game.components.ValueIconLabel;
import pl.org.minions.stigma.client.ui.swing.game.components.items.ResistancesPanel;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.event.item.ItemDropped;
import pl.org.minions.stigma.game.event.item.ItemPickedUp;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used for displaying player's actor statistics.
 */
public class PlayerStatisticsPanel extends JPanel
{

    @Translated
    private static String CARRIED_WEIGHT_TOOLTIP = "Carried weight.";
    @Translated
    private static String NAME = "Statistics";
    @Translated(name = "STRENGTH")
    private static String STRENGTH = "Strength";
    @Translated(name = "WILLPOWER")
    private static String WILLPOWER = "Willpower";
    @Translated(name = "AGILITY")
    private static String AGILITY = "Agility";
    @Translated(name = "FINESSE")
    private static String FINESSE = "Finesse";
    @Translated(name = "STRENGTH_SHORT")
    private static String STRENGTH_SHORT = "Str";
    @Translated(name = "WILLPOWER_SHORT")
    private static String WILLPOWER_SHORT = "Wil";
    @Translated(name = "AGILITY_SHORT")
    private static String AGILITY_SHORT = "Agi";
    @Translated(name = "FINESSE_SHORT")
    private static String FINESSE_SHORT = "Fin";
    @Translated(name = "ATTACK_ABILITY")
    private static String ATTACK_ABILITY = "Attack ability";
    @Translated(name = "DEFENSE_ABILITY")
    private static String DEFENSE_ABILITY = "Defense ability";
    @Translated(name = "SPELL_POWER")
    private static String SPELL_POWER = "Spell power";
    @Translated(name = "SPELL_IMMUNITY")
    private static String SPELL_IMMUNITY = "Spell immunity";
    @Translated(name = "ATTACK_ABILITY_SHORT")
    private static String ATTACK_ABILITY_SHORT = "AA";
    @Translated(name = "DEFENSE_ABILITY_SHORT")
    private static String DEFENSE_ABILITY_SHORT = "DA";
    @Translated(name = "SPELL_POWER_SHORT")
    private static String SPELL_POWER_SHORT = "SP";
    @Translated(name = "SPELL_IMMUNITY_SHORT")
    private static String SPELL_IMMUNITY_SHORT = "SI";
    @Translated(name = "LEVEL")
    private static String LEVEL = "Level";
    @Translated(name = "XP")
    private static String XP = "XP";

    private static final long serialVersionUID = 1L;
    private static final int PANEL_WIDTH = 200;
    private JLabel nameLabel;
    private JLabel levelLabel;
    private JLabel xpLabel;
    private JLabel strLabel;
    private JLabel agiLabel;
    private JLabel wilLabel;
    private JLabel finLabel;
    private JLabel aaLabel;
    private JLabel daLabel;
    private JLabel spLabel;
    private JLabel siLabel;
    private CurrencyPanel goldPanel;
    private ValueIconLabel weightLabel;
    private JLabel strValueLabel;
    private JLabel agiValueLabel;
    private JLabel wilValueLabel;
    private JLabel finValueLabel;
    private JLabel aaValueLabel;
    private JLabel daValueLabel;
    private JLabel spValueLabel;
    private JLabel siValueLabel;
    private ResistancesPanel resistancesPanel;

    /**
     * Constructor.
     */
    public PlayerStatisticsPanel()
    {
        super();
        initialize();
        this.setSize(getDefaultWidth(), getDefaultHeight());

        final UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addItemDroppedListener(new ItemDroppedListener()
        {

            @Override
            public void itemDropped(ItemDropped event, boolean playerActor)
            {
                if (playerActor)
                    updateWeightLabel(Client.globalInstance().getPlayerActor());
            }
        });

        uiEventRegistry.addItemPickedUpListener(new ItemPickedUpListener()
        {

            @Override
            public void itemPickedUp(ItemPickedUp event, boolean playerActor)
            {
                if (playerActor)
                    updateWeightLabel(Client.globalInstance().getPlayerActor());

            }
        });

    }

    /**
     * Returns default name of window for this panel.
     * @return default name of window for this panel.
     */
    public static String getDefaultName()
    {
        return NAME;
    }

    /**
     * Returns default panel width.
     * @return default panel width.
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * Returns default panel height.
     * @return default panel height.
     */
    public static int getDefaultHeight()
    {
        return EquipmentPanel.getDefaultHeight();
    }

    /**
     * Reloads actor data.
     */
    public void update()
    {
        if (Client.globalInstance() == null) // in Visual Editor
            return;

        Actor a = Client.globalInstance().getPlayerActor();

        nameLabel.setText(a.getName());
        levelLabel.setText(LEVEL + " " + a.getLevel());
        xpLabel.setText(String.format("%d/%d %s",
                                      a.getExperience(),
                                      a.getExperienceToNextLevel(),
                                      XP));
        strLabel.setText(printAttribute(STRENGTH, STRENGTH_SHORT));
        strValueLabel.setText(String.valueOf(a.getStrength()));
        agiLabel.setText(printAttribute(AGILITY, AGILITY_SHORT));
        agiValueLabel.setText(String.valueOf(a.getAgility()));
        wilLabel.setText(printAttribute(WILLPOWER, WILLPOWER_SHORT));
        wilValueLabel.setText(String.valueOf(a.getWillpower()));
        finLabel.setText(printAttribute(FINESSE, FINESSE_SHORT));
        finValueLabel.setText(String.valueOf(a.getFinesse()));

        aaLabel.setText(printAttribute(ATTACK_ABILITY, ATTACK_ABILITY_SHORT));
        aaValueLabel.setText(String.valueOf(a.getAttack()));
        daLabel.setText(printAttribute(DEFENSE_ABILITY, DEFENSE_ABILITY_SHORT));
        daValueLabel.setText(String.valueOf(a.getDefense()));
        spLabel.setText(printAttribute(SPELL_POWER, SPELL_POWER_SHORT));
        spValueLabel.setText(String.valueOf(a.getSpellPower()));
        siLabel.setText(printAttribute(SPELL_IMMUNITY, SPELL_IMMUNITY_SHORT));
        siValueLabel.setText(String.valueOf(a.getSpellImmunity()));
        goldPanel.setValue(a.getMoney());
        updateWeightLabel(a);

        resistancesPanel.setResistances(a.getResistanceMap());
    }

    private void updateWeightLabel(Actor a)
    {
        weightLabel.setText(a.getCurrentLoad() + "/" + a.getMaxLoad());
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.gridx = 0;
        gridBagConstraints14.gridwidth = 3;
        gridBagConstraints14.gridheight = 1;
        gridBagConstraints14.fill = GridBagConstraints.BOTH;
        gridBagConstraints14.anchor = GridBagConstraints.CENTER;
        gridBagConstraints14.insets = new Insets(10, 0, 2, 0);
        gridBagConstraints14.gridy = 17;
        GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
        gridBagConstraints81.gridx = 2;
        gridBagConstraints81.anchor = GridBagConstraints.EAST;
        gridBagConstraints81.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints81.gridy = 10;
        siValueLabel = new JLabel();
        siValueLabel.setText(SPELL_IMMUNITY_SHORT);
        GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
        gridBagConstraints71.gridx = 2;
        gridBagConstraints71.anchor = GridBagConstraints.EAST;
        gridBagConstraints71.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints71.gridy = 9;
        spValueLabel = new JLabel();
        spValueLabel.setText(SPELL_POWER_SHORT);
        GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
        gridBagConstraints61.gridx = 2;
        gridBagConstraints61.anchor = GridBagConstraints.EAST;
        gridBagConstraints61.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints61.ipadx = 0;
        gridBagConstraints61.gridy = 8;
        daValueLabel = new JLabel();
        daValueLabel.setText(DEFENSE_ABILITY_SHORT);
        GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
        gridBagConstraints51.gridx = 2;
        gridBagConstraints51.anchor = GridBagConstraints.SOUTHEAST;
        gridBagConstraints51.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints51.gridy = 7;
        aaValueLabel = new JLabel();
        aaValueLabel.setText(ATTACK_ABILITY_SHORT);
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 2;
        gridBagConstraints41.anchor = GridBagConstraints.EAST;
        gridBagConstraints41.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints41.gridy = 6;
        finValueLabel = new JLabel();
        finValueLabel.setText(FINESSE_SHORT);
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 2;
        gridBagConstraints31.anchor = GridBagConstraints.EAST;
        gridBagConstraints31.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints31.gridy = 5;
        wilValueLabel = new JLabel();
        wilValueLabel.setText(WILLPOWER_SHORT);
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 2;
        gridBagConstraints21.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints21.anchor = GridBagConstraints.EAST;
        gridBagConstraints21.gridy = 4;
        agiValueLabel = new JLabel();
        agiValueLabel.setText(AGILITY_SHORT);
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 2;
        gridBagConstraints13.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints13.anchor = GridBagConstraints.SOUTHEAST;
        gridBagConstraints13.gridy = 3;
        strValueLabel = new JLabel();
        strValueLabel.setText(STRENGTH_SHORT);
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints12.gridy = 12;
        gridBagConstraints12.anchor = GridBagConstraints.EAST;
        gridBagConstraints12.gridwidth = 2;
        gridBagConstraints12.gridx = 1;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.anchor = GridBagConstraints.EAST;
        gridBagConstraints11.insets = new Insets(10, 0, 0, 0);
        gridBagConstraints11.gridwidth = 2;
        gridBagConstraints11.gridy = 11;
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints10.gridy = 10;
        gridBagConstraints10.anchor = GridBagConstraints.WEST;
        gridBagConstraints10.gridx = 1;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints9.gridy = 9;
        gridBagConstraints9.anchor = GridBagConstraints.WEST;
        gridBagConstraints9.gridx = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints8.gridy = 8;
        gridBagConstraints8.anchor = GridBagConstraints.WEST;
        gridBagConstraints8.gridx = 1;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.insets = new Insets(10, 0, 0, 0);
        gridBagConstraints7.gridy = 7;
        gridBagConstraints7.anchor = GridBagConstraints.SOUTHWEST;
        gridBagConstraints7.gridx = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints6.gridy = 6;
        gridBagConstraints6.anchor = GridBagConstraints.WEST;
        gridBagConstraints6.gridx = 1;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints5.gridy = 5;
        gridBagConstraints5.anchor = GridBagConstraints.WEST;
        gridBagConstraints5.gridx = 1;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints4.gridy = 4;
        gridBagConstraints4.anchor = GridBagConstraints.WEST;
        gridBagConstraints4.gridx = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.anchor = GridBagConstraints.CENTER;
        gridBagConstraints2.gridwidth = 4;
        gridBagConstraints2.gridx = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.anchor = GridBagConstraints.CENTER;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.gridx = 0;
        aaLabel = new JLabel();
        aaLabel.setText(ATTACK_ABILITY);
        daLabel = new JLabel();
        daLabel.setText(DEFENSE_ABILITY);
        spLabel = new JLabel();
        spLabel.setText(SPELL_POWER);
        siLabel = new JLabel();
        siLabel.setText(SPELL_IMMUNITY);
        finLabel = new JLabel();
        finLabel.setText(FINESSE);
        wilLabel = new JLabel();
        wilLabel.setText(WILLPOWER);
        agiLabel = new JLabel();
        agiLabel.setText(AGILITY);
        strLabel = new JLabel();
        strLabel.setText(STRENGTH);
        xpLabel = new JLabel();
        xpLabel.setText("XP/XPNext");
        levelLabel = new JLabel();
        levelLabel.setText(LEVEL);
        nameLabel = new JLabel();
        nameLabel.setText("Name");
        goldPanel = new CurrencyPanel();
        weightLabel = new ValueIconLabel();
        weightLabel.setIcon(VisualizationGlobals.WEIGHT_IMG);
        weightLabel.setToolTipText(CARRIED_WEIGHT_TOOLTIP);

        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(353, 354));
        this.add(nameLabel, gridBagConstraints);
        this.add(levelLabel, gridBagConstraints1);
        this.add(xpLabel, gridBagConstraints2);
        this.add(strLabel, gridBagConstraints3);
        this.add(agiLabel, gridBagConstraints4);
        this.add(wilLabel, gridBagConstraints5);
        this.add(finLabel, gridBagConstraints6);
        this.add(aaLabel, gridBagConstraints7);
        this.add(daLabel, gridBagConstraints8);
        this.add(spLabel, gridBagConstraints9);
        this.add(siLabel, gridBagConstraints10);
        this.add(strValueLabel, gridBagConstraints13);
        this.add(agiValueLabel, gridBagConstraints21);
        this.add(wilValueLabel, gridBagConstraints31);
        this.add(finValueLabel, gridBagConstraints41);
        this.add(aaValueLabel, gridBagConstraints51);
        this.add(daValueLabel, gridBagConstraints61);
        this.add(spValueLabel, gridBagConstraints71);
        this.add(siValueLabel, gridBagConstraints81);
        this.add(goldPanel, gridBagConstraints11);
        this.add(weightLabel, gridBagConstraints12);
        this.add(getResistancesPanel(), gridBagConstraints14);
    }

    private String printAttribute(String fullDesc, String shortDesc)
    {

        return String.format("%s (%s): ", fullDesc, shortDesc);
    }

    /**
     * This method initializes resistancesPanel
     * @return javax.swing.JPanel
     */
    private ResistancesPanel getResistancesPanel()
    {
        if (resistancesPanel == null)
        {
            resistancesPanel = new ResistancesPanel();
        }
        return resistancesPanel;
    }
} //  @jve:decl-index=0:visual-constraint="156,12"
