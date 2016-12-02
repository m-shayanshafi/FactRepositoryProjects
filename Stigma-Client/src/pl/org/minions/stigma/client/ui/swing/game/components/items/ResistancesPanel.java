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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.ImagePane;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to draw resistances.
 */
public class ResistancesPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final String EXAMPLE_STRING = "1000,  90%";

    @Translated
    private static String RESISTANCES = "Resistances";

    private JLabel resistances;

    private ImagePane cuttingIcon;
    private JLabel cuttingValue;
    private ImagePane piercingIcon;
    private JLabel piercingValue;
    private ImagePane bluntIcon;
    private JLabel bluntValue;

    private ImagePane burnIcon;
    private JLabel burnValue;
    private ImagePane poisonIcon;
    private JLabel poisonValue;
    private ImagePane shockIcon;
    private JLabel shockValue;
    private ImagePane frostbiteIcon;
    private JLabel frostbiteValue;

    /**
     * Constructor.
     * @param resistances
     *            resistances to display
     */
    public ResistancesPanel(Map<DamageType, Resistance> resistances)
    {
        initialize();
        setResistances(resistances);
    }

    /**
     * Default constructor.
     */
    public ResistancesPanel()
    {
        initialize();
    }

    private void helper(JLabel label, Resistance res)
    {
        String txt;
        if (res == null || res.getThreshold() == 0 && res.getRelative() == 0)
        {
            txt = "         0% ";
            label.setEnabled(false);
        }
        else if (res.getThreshold() != 0)
            txt =
                    String.format("%5d, %3d%% ",
                                  res.getThreshold(),
                                  res.getRelative());
        else
            txt = String.format("       %3d%%", res.getRelative());
        label.setText(txt);
    }

    /**
     * Sets resistances displayed by this panel.
     * @param res
     *            resistances to display
     */
    public void setResistances(Map<DamageType, Resistance> res)
    {
        helper(cuttingValue, res.get(DamageType.CUTTING));
        helper(piercingValue, res.get(DamageType.PIERCING));
        helper(bluntValue, res.get(DamageType.BLUNT));
        helper(burnValue, res.get(DamageType.BURN));
        helper(frostbiteValue, res.get(DamageType.FROSTBITE));
        helper(poisonValue, res.get(DamageType.POISON));
        helper(shockValue, res.get(DamageType.SHOCK));
    }

    private void initialize()
    {
        //CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints14.gridy = 2;
        gridBagConstraints14.ipadx = 0;
        gridBagConstraints14.ipady = 0;
        gridBagConstraints14.anchor = GridBagConstraints.EAST;
        gridBagConstraints14.gridx = 2;
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints13.gridy = 2;
        gridBagConstraints13.ipadx = 0;
        gridBagConstraints13.anchor = GridBagConstraints.WEST;
        gridBagConstraints13.gridx = 3;
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints12.gridy = 3;
        gridBagConstraints12.ipadx = 0;
        gridBagConstraints12.ipady = 0;
        gridBagConstraints12.anchor = GridBagConstraints.EAST;
        gridBagConstraints12.gridx = 2;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints11.gridy = 3;
        gridBagConstraints11.ipadx = 0;
        gridBagConstraints11.anchor = GridBagConstraints.WEST;
        gridBagConstraints11.gridx = 3;
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints10.gridy = 1;
        gridBagConstraints10.ipadx = 0;
        gridBagConstraints10.ipady = 0;
        gridBagConstraints10.anchor = GridBagConstraints.EAST;
        gridBagConstraints10.gridx = 2;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints9.gridy = 1;
        gridBagConstraints9.ipadx = 0;
        gridBagConstraints9.anchor = GridBagConstraints.WEST;
        gridBagConstraints9.gridx = 3;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.ipadx = 0;
        gridBagConstraints8.ipady = 0;
        gridBagConstraints8.anchor = GridBagConstraints.EAST;
        gridBagConstraints8.gridx = 2;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints7.gridy = 0;
        gridBagConstraints7.ipadx = 0;
        gridBagConstraints7.anchor = GridBagConstraints.WEST;
        gridBagConstraints7.gridx = 3;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.insets = new Insets(0, 22, 0, 0);
        gridBagConstraints6.gridy = 2;
        gridBagConstraints6.ipadx = 0;
        gridBagConstraints6.ipady = 0;
        gridBagConstraints6.anchor = GridBagConstraints.EAST;
        gridBagConstraints6.gridx = 0;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.insets = new Insets(0, 22, 0, 0);
        gridBagConstraints5.gridy = 3;
        gridBagConstraints5.ipadx = 0;
        gridBagConstraints5.ipady = 0;
        gridBagConstraints5.anchor = GridBagConstraints.EAST;
        gridBagConstraints5.gridx = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.insets = new Insets(0, 22, 0, 0);
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.ipadx = 0;
        gridBagConstraints4.ipady = 0;
        gridBagConstraints4.anchor = GridBagConstraints.EAST;
        gridBagConstraints4.gridx = 0;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.insets = new Insets(0, 2, 0, 0);
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.ipadx = 0;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.insets = new Insets(0, 2, 0, 0);
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.ipadx = 0;
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        gridBagConstraints2.gridx = 1;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new Insets(0, 2, 0, 0);
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.ipadx = 0;
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 20, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());

        resistances = new JLabel();
        resistances.setText(RESISTANCES);
        cuttingIcon = new ImagePane();
        cuttingIcon.setImage(VisualizationGlobals.CUTTING_IMG);

        cuttingValue = new JLabel();
        cuttingValue.setHorizontalAlignment(SwingConstants.RIGHT);
        cuttingValue.setText(EXAMPLE_STRING);

        piercingIcon = new ImagePane();
        piercingIcon.setImage(VisualizationGlobals.PIERCING_IMG);

        piercingValue = new JLabel();
        piercingValue.setHorizontalAlignment(SwingConstants.RIGHT);
        piercingValue.setText(EXAMPLE_STRING);

        bluntIcon = new ImagePane();
        bluntIcon.setImage(VisualizationGlobals.BLUNT_IMG);

        bluntValue = new JLabel();
        bluntValue.setHorizontalAlignment(SwingConstants.RIGHT);
        bluntValue.setText(EXAMPLE_STRING);

        burnIcon = new ImagePane();
        burnIcon.setImage(VisualizationGlobals.BURN_IMG);

        burnValue = new JLabel();
        burnValue.setHorizontalAlignment(SwingConstants.RIGHT);
        burnValue.setText(EXAMPLE_STRING);

        frostbiteIcon = new ImagePane();
        frostbiteIcon.setImage(VisualizationGlobals.FROSTBITE_IMG);

        frostbiteValue = new JLabel();
        frostbiteValue.setHorizontalAlignment(SwingConstants.RIGHT);
        frostbiteValue.setText(EXAMPLE_STRING);

        poisonIcon = new ImagePane();
        poisonIcon.setImage(VisualizationGlobals.POISON_IMG);

        poisonValue = new JLabel();
        poisonValue.setHorizontalAlignment(SwingConstants.RIGHT);
        poisonValue.setText(EXAMPLE_STRING);

        shockIcon = new ImagePane();
        shockIcon.setImage(VisualizationGlobals.SHOCK_IMG);

        shockValue = new JLabel();
        shockValue.setHorizontalAlignment(SwingConstants.RIGHT);
        shockValue.setText(EXAMPLE_STRING);

        this.add(resistances, gridBagConstraints);
        this.add(cuttingIcon, gridBagConstraints1);
        this.add(piercingIcon, gridBagConstraints2);
        this.add(bluntIcon, gridBagConstraints3);
        this.add(cuttingValue, gridBagConstraints4);
        this.add(bluntValue, gridBagConstraints5);
        this.add(piercingValue, gridBagConstraints6);
        this.add(burnIcon, gridBagConstraints7);
        this.add(burnValue, gridBagConstraints8);
        this.add(frostbiteIcon, gridBagConstraints9);
        this.add(frostbiteValue, gridBagConstraints10);
        this.add(shockIcon, gridBagConstraints11);
        this.add(shockValue, gridBagConstraints12);
        this.add(poisonIcon, gridBagConstraints13);
        this.add(poisonValue, gridBagConstraints14);
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
