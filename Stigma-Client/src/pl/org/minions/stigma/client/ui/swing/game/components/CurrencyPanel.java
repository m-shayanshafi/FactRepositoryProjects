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
package pl.org.minions.stigma.client.ui.swing.game.components;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.item.CurrencyRepresentation;

/**
 * Class for displaying 'in game currency'. Uses
 * {@link CurrencyRepresentation}. Value will be displayed
 * using 3 labels and 3 symbols representing each coins
 * type. Unused higher coins will be invisible. Value of
 * zero will be displayed as 'zero copper'.
 */
public class CurrencyPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final String GOLD_PATH =
            "img/client/icons/currency/gold.png";
    private static final String SILVER_PATH =
            "img/client/icons/currency/silver.png";
    private static final String COPPER_PATH =
            "img/client/icons/currency/copper.png";
    private JLabel goldLabel;
    private JLabel silverLabel;
    private JLabel copperLabel;
    private ImagePane goldImage;
    private ImagePane silverImage;
    private ImagePane copperImage;

    /**
     * Constructor.
     */
    public CurrencyPanel()
    {
        initialize();
    }

    /**
     * Constructor.
     * @param value
     *            initial value to be displayed by
     *            component/
     */
    public CurrencyPanel(int value)
    {
        this();
        setValue(value);
    }

    /**
     * Sets value displayed by this component.
     * @param value
     *            value to display
     */
    public void setValue(CurrencyRepresentation value)
    {
        boolean b;
        b = setHelper(goldLabel, goldImage, value.getGold(), false);
        b = setHelper(silverLabel, silverImage, value.getSilver(), b);
        setHelper(copperLabel, copperImage, value.getCopper(), b);
        if (value.getMoney() == 0)
        {
            copperLabel.setText("0");
            copperLabel.setVisible(true);
            copperImage.setVisible(true);
        }
    }

    private boolean setHelper(JLabel label,
                              ImagePane image,
                              int value,
                              boolean forceVisible)
    {
        if (value > 0 || forceVisible)
        {
            label.setText(String.format("%2d", value));
            label.setVisible(true);
            image.setVisible(true);
            return true;
        }
        else
        {
            label.setVisible(false);
            image.setVisible(false);
            return false;
        }
    }

    /**
     * Sets value displayed by this component.
     * @param value
     *            value to display
     * @see CurrencyRepresentation#CurrencyRepresentation(int)
     */
    public void setValue(int value)
    {
        setValue(new CurrencyRepresentation(value));
    }

    /**
     * Centers panel. By default panel is aligned to right.
     */
    public void makeCentered()
    {
        this.add(Box.createHorizontalGlue());
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalGlue());
        this.add(getGoldLabel(), null);
        this.add(Box.createRigidArea(new Dimension(1, 1)));
        this.add(getGoldImage(), null);
        this.add(Box.createRigidArea(new Dimension(3, 3)));
        this.add(getSilverLabel(), null);
        this.add(Box.createRigidArea(new Dimension(1, 1)));
        this.add(getSilverImage(), null);
        this.add(Box.createRigidArea(new Dimension(3, 3)));
        this.add(getCopperLabel(), null);
        this.add(Box.createRigidArea(new Dimension(1, 1)));
        this.add(getCopperImage(), null);
        // CHECKSTYLE:ON
    }

    private ImagePane getGoldImage()
    {
        if (goldImage == null)
        {
            goldImage = new ImagePane();
            goldImage.setImage(Resourcer.loadImage(GOLD_PATH));
            goldImage.setCentered(true);
        }
        return goldImage;
    }

    private ImagePane getSilverImage()
    {
        if (silverImage == null)
        {
            silverImage = new ImagePane();
            silverImage.setImage(Resourcer.loadImage(SILVER_PATH));
            silverImage.setCentered(true);
        }
        return silverImage;
    }

    private ImagePane getCopperImage()
    {
        if (copperImage == null)
        {
            copperImage = new ImagePane();
            copperImage.setImage(Resourcer.loadImage(COPPER_PATH));
            copperImage.setCentered(true);
        }
        return copperImage;
    }

    private JLabel getGoldLabel()
    {
        if (goldLabel == null)
        {
            goldLabel = new JLabel();
            goldLabel.setText("1G");
        }
        return goldLabel;
    }

    private JLabel getSilverLabel()
    {
        if (silverLabel == null)
        {
            silverLabel = new JLabel();
            silverLabel.setText("2S");
            silverLabel.setMaximumSize(silverLabel.getPreferredSize());
        }
        return silverLabel;
    }

    private JLabel getCopperLabel()
    {
        if (copperLabel == null)
        {
            copperLabel = new JLabel();
            copperLabel.setText("3S");
            copperLabel.setMaximumSize(copperLabel.getPreferredSize());
        }
        return copperLabel;
    }

}
