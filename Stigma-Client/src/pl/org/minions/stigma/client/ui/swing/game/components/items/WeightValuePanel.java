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

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.CurrencyPanel;
import pl.org.minions.stigma.client.ui.swing.game.components.ImagePane;
import pl.org.minions.stigma.game.item.Item;

/**
 * Panel for displaying weight and value of item.
 */
public class WeightValuePanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private JLabel weightLabel;
    private ImagePane weightIcon;
    private CurrencyPanel currencyPanel;

    /**
     * Constructor.
     * @param item
     *            item to display
     */
    public WeightValuePanel(Item item)
    {
        super();
        initialize();
        postInit(item);
    }

    /**
     * @param item
     */
    private void postInit(Item item)
    {
        weightLabel.setText(String.valueOf(item.getWeight()));
        currencyPanel.setValue(item.getValue());
    }

    /**
     * This method initializes this
     */
    private void initialize()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));

        weightPanel.add(Box.createHorizontalGlue(), null);
        weightPanel.add(getWeightLabel(), null);
        weightPanel.add(getWeightIcon(), null);

        this.add(getCurrencyPanel(), null);
        this.add(weightPanel, null);
    }

    private JLabel getWeightLabel()
    {
        if (weightLabel == null)
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            weightLabel = new JLabel();
            weightLabel.setText("000");
        }
        return weightLabel;
    }

    private ImagePane getWeightIcon()
    {
        if (weightIcon == null)
        {
            weightIcon = new ImagePane();
            weightIcon.setImage(VisualizationGlobals.WEIGHT_IMG);
        }
        return weightIcon;
    }

    private CurrencyPanel getCurrencyPanel()
    {
        if (currencyPanel == null)
        {
            currencyPanel = new CurrencyPanel();
        }
        return currencyPanel;
    }
}
