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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.ClientSwingUI;
import pl.org.minions.stigma.client.ui.swing.StigmaInternalFrame;
import pl.org.minions.stigma.client.ui.swing.game.components.CooldownComponent;
import pl.org.minions.stigma.client.ui.swing.game.components.ToggleFrameButton;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.i18n.Translated;

/**
 * Main game panel, placed on right border of screen.
 * Contains all important game informations.
 */
public class SidePanel extends JPanel
{
    @Translated(name = "XP")
    private static String XP = "XP";
    @Translated
    private static String LOG_OUT_TEXT = "Log out";
    @Translated
    private static String SP_MESSAGE = "SP {0}/{1}";
    @Translated
    private static String HP_MESSAGE = "HP {0}/{1}";
    @Translated
    private static String INVENTORY_TEXT = "Inventory";
    @Translated
    private static String EQUIPMENT_TEXT = "Equipment";
    @Translated
    private static String CHARACTER_SHEET_TEXT = "Character sheet";
    @Translated
    private static String SKILLS_TEXT = "Skills";
    @Translated
    private static String QUESTS_TEXT = "Quests";
    @Translated
    private static String MAP_TEXT = "Map";
    @Translated
    private static String GROUND_TEXT = "Ground";
    @Translated
    private static String UNUSED_TEXT = "Unsused";

    private static final String ROUNDED_PANEL = "RoundedPanel";

    private static final String LOGOUT_ICON_PATH = "img/client/logout.png";

    private static final ImageIcon LOGOUT_ICON =
            Resourcer.loadIcon(LOGOUT_ICON_PATH);

    private static final String PATH_LOGOUT_MOUSE_OVER =
            "img/client/logoutMO.png";

    private static final ImageIcon LOGOUT_MOUSE_OVER_ICON =
            Resourcer.loadIcon(PATH_LOGOUT_MOUSE_OVER);

    private static final long serialVersionUID = 1L;
    private JPanel longNamePanel;
    private JButton logoutButton;

    private JLabel longNameLabel;
    private JPanel menuPanel;
    private ToggleFrameButton menuInventoryButton;
    private ToggleFrameButton menuEquipmentButton;
    private ToggleFrameButton characterSheetButton;
    private ToggleFrameButton menuSkillsButton;
    private ToggleFrameButton menuQuestsButton;
    private ToggleFrameButton menuMapButton;
    private ToggleFrameButton menuXButton;
    private ToggleFrameButton menuGroundButton;
    private JPanel avatarPanel;
    private CooldownComponent cooldownComponent;
    private JProgressBar hpProgressBar;
    private JProgressBar spProgressBar;
    private JProgressBar xpProgressBar;
    private JPanel mainPanel;

    /**
     * Constructor.
     */
    public SidePanel()
    {
        super();
        initialize();
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 224, 600));
        this.setName("BackgroundPanel");

        this.add(getLongNamePanel(), null);
        this.add(getMenuPanel(), null);
        // CHECKSTYLE:ON
        this.add(getAvatarPanel(), null);
        this.add(getHpProgressBar(), null);
        this.add(getSpProgressBar(), null);
        this.add(getXpProgressBar(), null);
        this.add(getMainPanel(), null);
    }

    /**
     * This method initializes longNamePanel
     * @return javax.swing.JPanel
     */
    private JPanel getLongNamePanel()
    {
        if (longNamePanel == null)
        {
            // CHECKSTYLE:OFF
            longNameLabel = new JLabel();
            longNameLabel.setBounds(new Rectangle(9, 4, 178, 12));
            longNameLabel.setText("Long and Elaborate name");
            longNamePanel = new JPanel();
            longNamePanel.setLayout(null);
            longNamePanel.setBounds(new Rectangle(3, 4, 218, 21));
            longNamePanel.setName(ROUNDED_PANEL);
            longNamePanel.add(getLogoutButton(), null);
            longNamePanel.add(longNameLabel, null);
            // CHECKSTYLE:ON
        }
        return longNamePanel;
    }

    /**
     * This method initializes logoutButton
     * @return javax.swing.JButton
     */
    private JButton getLogoutButton()
    {
        if (logoutButton == null)
        {
            // CHECKSTYLE:OFF
            logoutButton = new JButton();
            logoutButton.setIconTextGap(0);
            logoutButton.setHorizontalTextPosition(SwingConstants.CENTER);
            logoutButton.setBounds(new Rectangle(195, 2, 21, 17));
            logoutButton.setToolTipText(LOG_OUT_TEXT);
            logoutButton.setIcon(LOGOUT_ICON);
            logoutButton.setRolloverIcon(LOGOUT_MOUSE_OVER_ICON);
            logoutButton.setPressedIcon(LOGOUT_MOUSE_OVER_ICON);
            logoutButton.setName("LogoutButton");
            logoutButton.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    Client.globalInstance().logout();
                }
            });
            //CHECKSTYLE:ON
        }
        return logoutButton;
    }

    /**
     * Loads player actors data into panel.
     */
    public void loadActor()
    {
        final Actor a = Client.globalInstance().getPlayerActor();
        longNameLabel.setText(a.getName());

        hpProgressBar.setMaximum(a.getMaxHealth());
        hpProgressBar.setMinimum(0);
        hpProgressBar.setValue(a.getCurrentHealth());

        spProgressBar.setMaximum(a.getMaxStamina());
        spProgressBar.setMinimum(0);
        spProgressBar.setValue(a.getCurrentStamina());

        xpProgressBar.setMaximum(a.getExperienceToNextLevel());
        xpProgressBar.setMinimum(0);
        xpProgressBar.setValue(a.getExperience());
    }

    /**
     * This method initializes menuPanel
     * @return javax.swing.JPanel
     */
    private JPanel getMenuPanel()
    {
        if (menuPanel == null)
        {
            // CHECKSTYLE:OFF
            menuPanel = new JPanel();
            menuPanel.setLayout(null);
            menuPanel.setBounds(new Rectangle(21, 107, 200, 33));
            menuPanel.setName(ROUNDED_PANEL);
            menuPanel.add(getMenuInventoryButton(), null);
            menuPanel.add(getMenuEquipmentButton(), null);
            menuPanel.add(getCharacterSheetButton(), null);
            menuPanel.add(getMenuSkillsButton(), null);
            menuPanel.add(getMenuQuestsButton(), null);
            menuPanel.add(getMenuMapButton(), null);
            menuPanel.add(getMenuXButton(), null);
            menuPanel.add(getMenuGroundButton(), null);
            // CHECKSTYLE:ON
        }
        return menuPanel;
    }

    /**
     * This method initializes menuInventoryButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuInventoryButton()
    {
        if (menuInventoryButton == null)
        {
            // CHECKSTYLE:OFF
            menuInventoryButton = new ToggleFrameButton();
            menuInventoryButton.setText("I");
            menuInventoryButton.setBounds(new Rectangle(4, 4, 24, 25));
            menuInventoryButton.setName("MenuLeftButton");
            menuInventoryButton.setToolTipText(INVENTORY_TEXT);
            final StigmaInternalFrame invFrame =
                    ((ClientSwingUI) Client.globalInstance().getUi()).getInventoryFrame();
            menuInventoryButton.setToggledFrame(invFrame);
            // CHECKSTYLE:ON
        }
        return menuInventoryButton;
    }

    /**
     * This method initializes menuEquipmentButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuEquipmentButton()
    {
        if (menuEquipmentButton == null)
        {
            // CHECKSTYLE:OFF
            menuEquipmentButton = new ToggleFrameButton();
            menuEquipmentButton.setText("E");
            menuEquipmentButton.setBounds(new Rectangle(28, 4, 24, 25));
            menuEquipmentButton.setName("MenuInnerButton");
            menuEquipmentButton.setToolTipText(EQUIPMENT_TEXT);
            final StigmaInternalFrame eqFrame =
                    ((ClientSwingUI) Client.globalInstance().getUi()).getEquipmentFrame();
            menuEquipmentButton.setToggledFrame(eqFrame);
            // CHECKSTYLE:ON
        }
        return menuEquipmentButton;
    }

    /**
     * This method initializes characterSheetButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getCharacterSheetButton()
    {
        if (characterSheetButton == null)
        {
            // CHECKSTYLE:OFF
            characterSheetButton = new ToggleFrameButton();
            characterSheetButton.setText("C");
            characterSheetButton.setBounds(new Rectangle(52, 4, 24, 25));
            characterSheetButton.setName("MenuInnerButton");
            characterSheetButton.setToolTipText(CHARACTER_SHEET_TEXT);
            final StigmaInternalFrame stFrame =
                    ((ClientSwingUI) Client.globalInstance().getUi()).getPlayerStatisticsFrame();
            characterSheetButton.setToggledFrame(stFrame);
            // CHECKSTYLE:ON
        }
        return characterSheetButton;
    }

    /**
     * This method initializes menuSkillsButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuSkillsButton()
    {
        if (menuSkillsButton == null)
        {
            // CHECKSTYLE:OFF
            menuSkillsButton = new ToggleFrameButton();
            menuSkillsButton.setText("S");
            menuSkillsButton.setBounds(new Rectangle(76, 4, 24, 25));
            menuSkillsButton.setName("MenuInnerButton");
            menuSkillsButton.setToolTipText(SKILLS_TEXT);
            menuSkillsButton.setEnabled(false);
            // CHECKSTYLE:ON
        }
        return menuSkillsButton;
    }

    /**
     * This method initializes menuQuestsButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuQuestsButton()
    {
        if (menuQuestsButton == null)
        {
            // CHECKSTYLE:OFF
            menuQuestsButton = new ToggleFrameButton();
            menuQuestsButton.setText("Q");
            menuQuestsButton.setBounds(new Rectangle(100, 4, 24, 25));
            menuQuestsButton.setName("MenuInnerButton");
            menuQuestsButton.setToolTipText(QUESTS_TEXT);
            menuQuestsButton.setEnabled(false);
            // CHECKSTYLE:ON
        }
        return menuQuestsButton;
    }

    /**
     * This method initializes menuMapButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuMapButton()
    {
        if (menuMapButton == null)
        {
            // CHECKSTYLE:OFF
            menuMapButton = new ToggleFrameButton();
            menuMapButton.setBounds(new Rectangle(124, 4, 24, 25));
            menuMapButton.setText("M");
            menuMapButton.setName("MenuInnerButton");
            menuMapButton.setEnabled(false);
            menuMapButton.setToolTipText(MAP_TEXT);
            // CHECKSTYLE:ON
        }
        return menuMapButton;
    }

    /**
     * This method initializes menuXButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuXButton()
    {
        if (menuXButton == null)
        {
            // CHECKSTYLE:OFF
            menuXButton = new ToggleFrameButton();
            menuXButton.setBounds(new Rectangle(148, 4, 24, 25));
            menuXButton.setName("MenuInnerButton");
            menuXButton.setToolTipText(UNUSED_TEXT);
            menuXButton.setEnabled(false);
            // CHECKSTYLE:ON
        }
        return menuXButton;
    }

    /**
     * This method initializes menuGroundButton
     * @return javax.swing.JButton
     */
    private ToggleFrameButton getMenuGroundButton()
    {
        if (menuGroundButton == null)
        {
            // CHECKSTYLE:OFF
            menuGroundButton = new ToggleFrameButton();
            menuGroundButton.setBounds(new Rectangle(172, 4, 24, 25));
            menuGroundButton.setName("MenuRightButton");
            menuGroundButton.setText("G");
            menuGroundButton.setToolTipText(GROUND_TEXT);
            menuGroundButton.setEnabled(true);
            final StigmaInternalFrame grFrame =
                    ((ClientSwingUI) Client.globalInstance().getUi()).getGroundFrame();
            menuGroundButton.setToggledFrame(grFrame);
            // CHECKSTYLE:ON
        }
        return menuGroundButton;
    }

    /**
     * This method initializes avatarPanel
     * @return javax.swing.JPanel
     */
    private JPanel getAvatarPanel()
    {
        if (avatarPanel == null)
        {
            // CHECKSTYLE:OFF
            avatarPanel = new JPanel();
            avatarPanel.setLayout(new BorderLayout());
            avatarPanel.setBounds(new Rectangle(32, 31, 49, 61));
            avatarPanel.setBackground(Color.black);
            avatarPanel.setName(ROUNDED_PANEL);
            avatarPanel.add(getCooldownComponent(), null);
            // CHECKSTYLE:ON
        }
        return avatarPanel;
    }

    /**
     * This method initializes cooldownComponent
     * @return pl.org.minions.stigma.client.ui.swing.game.
     *         components.CooldownComponent
     */
    private CooldownComponent getCooldownComponent()
    {
        if (cooldownComponent == null)
        {
            cooldownComponent = new CooldownComponent();
            //            cooldownComponent.setBounds(new Rectangle(4, 4, 41, 53));
            // CHECKSTYLE:OFF  
            // CHECKSTYLE:ON
        }
        return cooldownComponent;
    }

    /**
     * This method initializes hpProgressBar
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getHpProgressBar()
    {
        if (hpProgressBar == null)
        {
            // CHECKSTYLE:OFF
            hpProgressBar = new JProgressBar();
            hpProgressBar.setBounds(new Rectangle(84, 31, 137, 33));
            hpProgressBar.setStringPainted(true);
            hpProgressBar.setName("HpProgressBar");
            hpProgressBar.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    hpProgressBar.setString(MessageFormat.format(HP_MESSAGE,
                                                                 hpProgressBar.getValue(),
                                                                 hpProgressBar.getMaximum()));
                }
            });
            // CHECKSTYLE:ON
        }
        return hpProgressBar;
    }

    /**
     * This method initializes spProgressBar
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getSpProgressBar()
    {
        if (spProgressBar == null)
        {
            // CHECKSTYLE:OFF
            spProgressBar = new JProgressBar();
            spProgressBar.setBounds(new Rectangle(84, 67, 137, 33));
            spProgressBar.setStringPainted(true);
            spProgressBar.setName("SpProgressBar");
            spProgressBar.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    spProgressBar.setString(MessageFormat.format(SP_MESSAGE,
                                                                 spProgressBar.getValue(),
                                                                 spProgressBar.getMaximum()));
                }
            });
            // CHECKSTYLE:ON
        }
        return spProgressBar;
    }

    /**
     * This method initializes xpProgressBar
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getXpProgressBar()
    {
        if (xpProgressBar == null)
        {
            // CHECKSTYLE:OFF
            xpProgressBar = new JProgressBar();
            xpProgressBar.setBounds(new Rectangle(32, 95, 49, 5));
            xpProgressBar.setName("XpProgressBar");
            xpProgressBar.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    xpProgressBar.setToolTipText(String.format("%d/%d %s",
                                                               xpProgressBar.getValue(),
                                                               xpProgressBar.getMaximum(),
                                                               XP));
                }
            });
            // CHECKSTYLE:ON
        }
        return xpProgressBar;
    }

    /**
     * This method initializes mainPanel
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel()
    {
        if (mainPanel == null)
        {
            // CHECKSTYLE:OFF
            mainPanel = new ActorsListPanel();
            mainPanel.setBounds(new Rectangle(32, 148, 189, 449));
            mainPanel.setName(ROUNDED_PANEL);
            // CHECKSTYLE:ON
        }
        return mainPanel;
    }
}
