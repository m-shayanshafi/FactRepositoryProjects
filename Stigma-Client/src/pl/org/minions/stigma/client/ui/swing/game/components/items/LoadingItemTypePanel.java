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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.ui.swing.game.tooltips.ItemToolTip;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to draw information about loading item at
 * {@link ItemToolTip}.
 */
public class LoadingItemTypePanel extends JPanel
{
    /**
     * Width of this panel.
     */
    public static final int HEIGHT = 50;

    /**
     * Height of this panel.
     */
    public static final int WIDTH = 320;

    private static final long serialVersionUID = 1L;

    private static final String LOADING_IMG_PATH = "img/client/loading.gif"; //  @jve:decl-index=0:

    private static final ImageIcon LOADING_IMG =
            Resourcer.loadIcon(LOADING_IMG_PATH); //  @jve:decl-index=0:

    @Translated
    private static String LOADING_DATA = "Loading item data...";
    @Translated
    private static String REOPEN_TOOLTIP = "Reopen tooltip in few seconds"; //  @jve:decl-index=0:

    private JLabel itemIconLabel;
    private JLabel loadingItemLabel;
    private JLabel pleaseReopenLabel;

    /**
     * Default constructor.
     * @param item
     *            Item to be drawn on panel
     */
    public LoadingItemTypePanel(Item item)
    {
        initialize();
        postInit(item);
        this.setVisible(true);
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(null);
        this.setSize(WIDTH, HEIGHT);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        this.loadingItemLabel = new JLabel("Loading item data...;");
        this.loadingItemLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loadingItemLabel.setBounds(new Rectangle(101, 8, 149, 17));

        this.pleaseReopenLabel = new JLabel("Reopen tooltip in few seconds;");
        pleaseReopenLabel.setBounds(new Rectangle(94, 23, 170, 16));
        pleaseReopenLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        itemIconLabel = new JLabel();
        itemIconLabel.setIcon(LOADING_IMG);
        itemIconLabel.setBounds(new Rectangle(6, 5, 35, 35));

        this.add(itemIconLabel, null);
        // CHECKSTYLE:ON
        this.add(loadingItemLabel, null);
        this.add(pleaseReopenLabel, null);
    }

    /**
     * Function added to remove Visual Editor warnings.
     * @param item
     *            item to set values from
     */
    private void postInit(Item item)
    {
        loadingItemLabel.setText(LOADING_DATA);
        pleaseReopenLabel.setText(REOPEN_TOOLTIP);
    }
} //  @jve:decl-index=0:visual-constraint="17,13"
