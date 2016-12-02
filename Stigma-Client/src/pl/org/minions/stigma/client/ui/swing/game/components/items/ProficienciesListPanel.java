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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Equipment;

/**
 * Class displaying list of item's required proficiencies.
 */
public class ProficienciesListPanel extends JPanel
{
    private static final String PROFICIENCY_SEPARATOR = " \u2022 ";

    private static final long serialVersionUID = 1L;
    private Map<Short, JLabel> proficiencyLabels;

    /**
     * Constructor.
     * @param item
     *            item to display
     */
    public ProficienciesListPanel(Equipment item)
    {
        super();
        initialize();
        postInit(item);
    }

    /**
     * @param item
     */
    private void postInit(Equipment item)
    {
        proficiencyLabels = new TreeMap<Short, JLabel>();

        final Actor player = Client.globalInstance().getPlayerActor();

        final List<Short> requiredProficiencies = item.getRequiredProficiency();

        boolean firstProficiency = true;
        for (short s : requiredProficiencies)
        {
            if (!firstProficiency)
                this.add(new JLabel(PROFICIENCY_SEPARATOR));

            final String proficiency =
                    Client.globalInstance()
                          .getProficiencyDB()
                          .getProficiency(s)
                          .getName();
            final JLabel label = new JLabel(proficiency);
            this.add(label);
            proficiencyLabels.put(s, label);

            if (!player.getProficiencies().contains(s))
                label.setForeground(Color.red);
            firstProficiency = false;
        }
    }

    /**
     * This method initializes this
     */
    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalStrut(20));
        // CHECKSTYLE:ON
    }
}
