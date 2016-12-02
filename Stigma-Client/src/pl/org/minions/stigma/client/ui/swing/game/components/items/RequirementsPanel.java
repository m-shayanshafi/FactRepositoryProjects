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
package pl.org.minions.stigma.client.ui.swing.game.components.items;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Equipment;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to draw requirements details for item.
 */
public class RequirementsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String REQUIREMENTS = "Requirements:";

    @Translated(name = "STRENGTH_SHORT")
    private static String STRENGTH = "Str";
    @Translated(name = "WILLPOWER_SHORT")
    private static String WILLPOWER = "Wil";
    @Translated(name = "AGILITY_SHORT")
    private static String AGILITY = "Agi";
    @Translated(name = "FINESSE_SHORT")
    private static String FINESSE = "Fin";

    private JLabel strenght;
    private JLabel agility;
    private JLabel willpower;
    private JLabel finesse;

    private JLabel requirementsLabel;
    private JLabel requiredStrenght;
    private JLabel requiredAgility;
    private JLabel requiredWillpower;
    private JLabel requiredFinesse;

    /**
     * Default constructor.
     * @param item
     *            Item which detailed data will be displayed
     * @param actor
     *            actor to compare requirements to
     */
    public RequirementsPanel(Equipment item, Actor actor)
    {
        initialize();
        postInit(item, actor);
    }

    private void initialize()
    {
        //CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        strenght = new JLabel();
        strenght.setText(STRENGTH);

        agility = new JLabel();
        agility.setText(AGILITY);

        willpower = new JLabel();
        willpower.setText(WILLPOWER);

        finesse = new JLabel();
        finesse.setText(FINESSE);

        requirementsLabel = new JLabel();
        requirementsLabel.setText(REQUIREMENTS);

        requiredStrenght = new JLabel();
        requiredStrenght.setText("S");

        requiredAgility = new JLabel();
        requiredAgility.setText("A");

        requiredWillpower = new JLabel();
        requiredWillpower.setText("W");

        requiredFinesse = new JLabel();
        requiredFinesse.setText("F");

        this.add(Box.createHorizontalStrut(20));

        this.add(requirementsLabel, null);

        this.add(requiredStrenght, null);
        this.add(strenght, null);

        this.add(requiredAgility, null);
        this.add(agility, null);

        this.add(requiredWillpower, null);
        this.add(willpower, null);

        this.add(requiredFinesse, null);
        this.add(finesse, null);

        this.add(Box.createHorizontalGlue(), null);
        //CHECKSTYLE:ON
    }

    private void helper(JLabel icon, JLabel label, int value, int currentValue)
    {
        if (value > 0)
        {
            icon.setVisible(true);
            label.setVisible(true);
            label.setText(String.format("%5d ", value));
            if (value > currentValue)
                label.setForeground(Color.red);
        }
        else
        {
            icon.setVisible(false);
            label.setVisible(false);
        }
    }

    private void postInit(Equipment equipment, Actor actor)
    {
        updateData(equipment, actor);
    }

    /**
     * Updates data.
     * @param equipment
     *            equipment to show
     * @param actor
     *            actor for requirements comparison
     */
    void updateData(Equipment equipment, Actor actor)
    {
        helper(strenght,
               requiredStrenght,
               equipment.getRequiredStrength(),
               actor.getStrength());
        helper(agility,
               requiredAgility,
               equipment.getRequiredAgility(),
               actor.getAgility());
        helper(willpower,
               requiredWillpower,
               equipment.getRequiredWillpower(),
               actor.getWillpower());
        helper(finesse,
               requiredFinesse,
               equipment.getRequiredFinesse(),
               actor.getFinesse());
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
