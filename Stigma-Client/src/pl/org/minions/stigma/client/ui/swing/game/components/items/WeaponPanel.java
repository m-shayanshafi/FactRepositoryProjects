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

import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.AttackType;
import pl.org.minions.stigma.game.item.Weapon;
import pl.org.minions.stigma.game.item.type.WeaponAttack;

/**
 * Panel used to draw weapon details on {@link ItemPanel}.
 */
public class WeaponPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * @param item
     *            Item which detailed data will be displayed
     * @param actor
     *            actor to get primary attribute levels from
     */
    public WeaponPanel(Weapon item, Actor actor)
    {
        initialize();
        postInit(item, actor);
    }

    //NOTE: there was an ambiguous "to do: update this component"

    /**
     * 
     */
    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    private void postInit(Weapon weapon, Actor actor)
    {
        updateData(weapon, actor);
    }

    /**
     * Update displayed data.
     * @param weapon
     *            weapon to show
     * @param actor
     *            actor to get primary attribute levels from
     */
    void updateData(Weapon weapon, Actor actor)
    {
        this.removeAll();

        for (Entry<AttackType, WeaponAttack> attack : weapon.getAttackMap()
                                                            .entrySet())
        {
            WeaponAttackPanel panel = new WeaponAttackPanel();
            panel.setValue(attack.getKey(), attack.getValue(), actor);
            this.add(panel);
        }
        this.add(Box.createHorizontalGlue());
    }
}
