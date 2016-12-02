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

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import pl.org.minions.stigma.game.actor.Actor;

/**
 * Simple component with two progress bars to display
 * actor's health and stamina.
 */
public class ActorStatusPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private JProgressBar hpProgressBar;
    private JProgressBar spProgressBar;

    /**
     * Constructor.
     */
    public ActorStatusPanel()
    {
        super();
        initialize();
    }

    /**
     * Constructor.
     * @param a
     *            actor which status should be displayed
     */
    public ActorStatusPanel(Actor a)
    {
        this();
        update(a);
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.ipadx = 0;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        gridBagConstraints1.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(49, 43));
        this.add(getHpProgressBar(), gridBagConstraints);
        this.add(getSpProgressBar(), gridBagConstraints1);
        // CHECKSTYLE:ON
    }

    private JProgressBar getHpProgressBar()
    {
        if (hpProgressBar == null)
        {
            hpProgressBar = new JProgressBar();
            hpProgressBar.setName("HpProgressBar");
            hpProgressBar.setMinimum(0);
        }
        return hpProgressBar;
    }

    private JProgressBar getSpProgressBar()
    {
        if (spProgressBar == null)
        {
            spProgressBar = new JProgressBar();
            spProgressBar.setName("SpProgressBar");
            spProgressBar.setMinimum(0);
        }
        return spProgressBar;
    }

    /**
     * Updates component with new data.
     * @param a
     *            actor which status should be displayed
     */
    public void update(Actor a)
    {
        hpProgressBar.setMaximum(a.getMaxHealth());
        hpProgressBar.setValue(a.getCurrentHealth());

        spProgressBar.setMaximum(a.getMaxStamina());
        spProgressBar.setValue(a.getCurrentStamina());
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
