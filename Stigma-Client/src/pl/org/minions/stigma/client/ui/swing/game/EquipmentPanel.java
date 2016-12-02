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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.ClientSwingUI;
import pl.org.minions.stigma.client.ui.swing.game.components.ImagePane;
import pl.org.minions.stigma.client.ui.swing.game.components.PhysicalSlotButton;
import pl.org.minions.stigma.client.ui.swing.handlers.TransferableItem;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Panel representing actor figure with equipped items.
 */
public class EquipmentPanel extends JPanel
{
    private static final class EmptyTransferHandler extends TransferHandler
    {
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        @Override
        public boolean canImport(TransferSupport support)
        {
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public int getSourceActions(JComponent c)
        {
            return NONE;
        }
    }

    private final class DropTargetListenerImplementation implements
                                                        DropTargetListener
    {
        @Override
        public void dropActionChanged(DropTargetDragEvent dtde)
        {
        }

        @Override
        public void drop(DropTargetDropEvent dtde)
        {
            for (PhysicalSlotButton slot : slots)
                slot.unhighlight();
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde)
        {
        }

        @Override
        public void dragExit(DropTargetEvent dte)
        {
            for (PhysicalSlotButton slot : slots)
                slot.unhighlight();
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde)
        {
            try
            {
                Item item =
                        (Item) dtde.getTransferable()
                                   .getTransferData(TransferableItem.getItemDataFlavor());

                if (item != null
                    && Client.globalInstance().getPlayerActor().canEquip(item))
                    for (PhysicalSlotButton slot : slots)
                        slot.highlight(item);
                else
                    for (PhysicalSlotButton slot : slots)
                        slot.unhighlight();
            }
            catch (UnsupportedFlavorException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                Log.logger.error("I/O exception in drag", e);
            }
        }
    }

    @Translated
    private static String NAME = "Equipment";

    private static final String BG_MALE_IMG_PATH =
            "img/client/equipment_bg_m.png";
    private static final String BG_FEMALE_IMG_PATH =
            "img/client/equipment_bg_f.png";

    private static final String ARROW_LEFT_PATH =
            "img/client/icons/arrow_left.png"; //  @jve:decl-index=0:
    private static final String ARROW_RIGHT_PATH =
            "img/client/icons/arrow_right.png"; //  @jve:decl-index=0:
    private static final String ARROW_LEFT_ROLLOVER_PATH =
            "img/client/icons/arrow_left_rollover.png"; //  @jve:decl-index=0:
    private static final String ARROW_RIGHT_ROLLOVER_PATH =
            "img/client/icons/arrow_right_rollover.png"; //  @jve:decl-index=0:

    private static final BufferedImage BG_MALE_IMAGE =
            Resourcer.loadImage(BG_MALE_IMG_PATH);
    private static final BufferedImage BG_FEMALE_IMAGE =
            Resourcer.loadImage(BG_FEMALE_IMG_PATH);

    private static final ImageIcon ARROW_LEFT =
            Resourcer.loadIcon(ARROW_LEFT_PATH);
    private static final ImageIcon ARROW_RIGHT =
            Resourcer.loadIcon(ARROW_RIGHT_PATH);
    private static final ImageIcon ARROW_ROLLOVER_LEFT =
            Resourcer.loadIcon(ARROW_LEFT_ROLLOVER_PATH); //  @jve:decl-index=0:
    private static final ImageIcon ARROW_ROLLOVER_RIGHT =
            Resourcer.loadIcon(ARROW_RIGHT_ROLLOVER_PATH);

    private static final int PANEL_HEIGHT = 350;
    private static final int PANEL_WIDTH = 250;

    private static final long serialVersionUID = 1L;
    private ImagePane bg;

    private PhysicalSlotButton headSlot;
    private PhysicalSlotButton faceSlot;
    private PhysicalSlotButton neckSlot;
    private PhysicalSlotButton rHandSlot;
    private PhysicalSlotButton lHandSlot;
    private PhysicalSlotButton handsSlot;
    private PhysicalSlotButton torsoSlot;
    private PhysicalSlotButton legsSlot;
    private PhysicalSlotButton feetSlot;
    private PhysicalSlotButton finger1Slot;
    private PhysicalSlotButton finger2Slot;
    private PhysicalSlotButton finger3Slot;
    private PhysicalSlotButton finger4Slot;
    private PhysicalSlotButton finger5Slot;
    private PhysicalSlotButton finger6Slot;
    private PhysicalSlotButton finger7Slot;
    private PhysicalSlotButton finger8Slot;

    private List<PhysicalSlotButton> slots =
            new LinkedList<PhysicalSlotButton>();

    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JButton showStatsButton;
    private JButton showItemsButton;

    /**
     * Constructor.
     */
    public EquipmentPanel()
    {
        super();

        initialize();
        postInit();
    }

    private void postInit()
    {
        this.setTransferHandler(new EmptyTransferHandler());

        slots.add(headSlot);
        slots.add(faceSlot);
        slots.add(neckSlot);
        slots.add(rHandSlot);
        slots.add(lHandSlot);
        slots.add(handsSlot);
        slots.add(torsoSlot);
        slots.add(legsSlot);
        slots.add(feetSlot);
        slots.add(finger1Slot);
        slots.add(finger2Slot);
        slots.add(finger3Slot);
        slots.add(finger4Slot);
        slots.add(finger5Slot);
        slots.add(finger6Slot);
        slots.add(finger7Slot);
        slots.add(finger8Slot);

        DropTargetListener dropTargetListener =
                new DropTargetListenerImplementation();
        try
        {
            this.getDropTarget().addDropTargetListener(dropTargetListener);
            for (PhysicalSlotButton slot : slots)
                slot.getDropTarget().addDropTargetListener(dropTargetListener);
        }
        catch (TooManyListenersException e)
        {
            Log.logger.error("Too many listeners", e);
        }
    }

    /**
     * Returns default caption.
     * @return default caption.
     */
    public static String getDefaultName()
    {
        return NAME;
    }

    /**
     * Returns default width (this panel should have fixed
     * size).
     * @return default width
     */
    public static int getDefaultWidth()
    {
        return PANEL_WIDTH;
    }

    /**
     * Returns default height(this panel should have fixed
     * size).
     * @return default height
     */
    public static int getDefaultHeight()
    {
        return PANEL_HEIGHT;
    }

    /**
     * This method initializes this
     */
    private void initialize()
    {
        // CHECKSTYLE:OFF
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.anchor = GridBagConstraints.NORTH;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.weighty = 0.0D;
        gridBagConstraints3.weightx = 1.0D;
        gridBagConstraints3.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0D;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.gridx = 0;
        bg = new ImagePane();
        bg.setName("EquipmentBackground");
        bg.setBounds(new Rectangle(50, 25, 150, 300));
        bg.setBorder(BorderFactory.createEmptyBorder());

        this.setLayout(new GridBagLayout());
        this.setBounds(new Rectangle(0, 0, 250, 380));

        this.add(getMainPanel(), gridBagConstraints);
        this.add(getButtonsPanel(), gridBagConstraints3);
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getHeadSlot()
    {
        // CHECKSTYLE:OFF
        if (headSlot == null)
        {
            headSlot = new PhysicalSlotButton(PhysicalSlotType.HEAD);
            headSlot.setBounds(new Rectangle(87,
                                             16,
                                             PhysicalSlotButton.DIMENSION,
                                             PhysicalSlotButton.DIMENSION));
        }
        return headSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFaceSlot()
    {
        // CHECKSTYLE:OFF
        if (faceSlot == null)
        {
            faceSlot = new PhysicalSlotButton(PhysicalSlotType.FACE);
            faceSlot.setBounds(new Rectangle(128,
                                             29,
                                             PhysicalSlotButton.DIMENSION,
                                             PhysicalSlotButton.DIMENSION));
        }
        return faceSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getNeckSlot()
    {
        // CHECKSTYLE:OFF
        if (neckSlot == null)
        {
            neckSlot = new PhysicalSlotButton(PhysicalSlotType.NECK);
            neckSlot.setBounds(new Rectangle(106,
                                             69,
                                             PhysicalSlotButton.DIMENSION,
                                             PhysicalSlotButton.DIMENSION));
        }
        return neckSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getRHandSlot()
    {
        // CHECKSTYLE:OFF
        if (rHandSlot == null)
        {
            rHandSlot = new PhysicalSlotButton(PhysicalSlotType.RIGHT_HAND);
            rHandSlot.setBounds(new Rectangle(53,
                                              160,
                                              PhysicalSlotButton.DIMENSION,
                                              PhysicalSlotButton.DIMENSION));
        }
        return rHandSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getLHandSlot()
    {
        // CHECKSTYLE:OFF
        if (lHandSlot == null)
        {
            lHandSlot = new PhysicalSlotButton(PhysicalSlotType.LEFT_HAND);
            lHandSlot.setBounds(new Rectangle(166,
                                              160,
                                              PhysicalSlotButton.DIMENSION,
                                              PhysicalSlotButton.DIMENSION));
        }
        return lHandSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getHandsSlot()
    {
        // CHECKSTYLE:OFF
        if (handsSlot == null)
        {
            handsSlot = new PhysicalSlotButton(PhysicalSlotType.HANDS);
            handsSlot.setBounds(new Rectangle(106,
                                              155,
                                              PhysicalSlotButton.DIMENSION,
                                              PhysicalSlotButton.DIMENSION));
        }
        return handsSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getTorsoSlot()
    {
        // CHECKSTYLE:OFF
        if (torsoSlot == null)
        {
            torsoSlot = new PhysicalSlotButton(PhysicalSlotType.TORSO);
            torsoSlot.setBounds(new Rectangle(106,
                                              110,
                                              PhysicalSlotButton.DIMENSION,
                                              PhysicalSlotButton.DIMENSION));
        }
        return torsoSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getLegsSlot()
    {
        // CHECKSTYLE:OFF
        if (legsSlot == null)
        {
            legsSlot = new PhysicalSlotButton(PhysicalSlotType.LEGS);
            legsSlot.setBounds(new Rectangle(106,
                                             220,
                                             PhysicalSlotButton.DIMENSION,
                                             PhysicalSlotButton.DIMENSION));
        }
        return legsSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFeetSlot()
    {
        // CHECKSTYLE:OFF
        if (feetSlot == null)
        {
            feetSlot = new PhysicalSlotButton(PhysicalSlotType.FEET);
            feetSlot.setBounds(new Rectangle(106,
                                             285,
                                             PhysicalSlotButton.DIMENSION,
                                             PhysicalSlotButton.DIMENSION));
        }
        return feetSlot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger1Slot()
    {
        // CHECKSTYLE:OFF
        if (finger1Slot == null)
        {
            finger1Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_1);
            finger1Slot.setBounds(new Rectangle(10,
                                                110,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger1Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger2Slot()
    {
        // CHECKSTYLE:OFF
        if (finger2Slot == null)
        {
            finger2Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_2);
            finger2Slot.setBounds(new Rectangle(10,
                                                160,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger2Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger3Slot()
    {
        // CHECKSTYLE:OFF
        if (finger3Slot == null)
        {
            finger3Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_3);
            finger3Slot.setBounds(new Rectangle(10,
                                                210,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger3Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger4Slot()
    {
        // CHECKSTYLE:OFF
        if (finger4Slot == null)
        {
            finger4Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_4);
            finger4Slot.setBounds(new Rectangle(10,
                                                260,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger4Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger5Slot()
    {
        // CHECKSTYLE:OFF
        if (finger5Slot == null)
        {
            finger5Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_5);
            finger5Slot.setBounds(new Rectangle(210,
                                                110,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger5Slot;
        // CHECKSTYLE:ON        
    }

    private PhysicalSlotButton getFinger6Slot()
    {
        // CHECKSTYLE:OFF
        if (finger6Slot == null)
        {
            finger6Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_6);
            finger6Slot.setBounds(new Rectangle(210,
                                                160,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger6Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger7Slot()
    {
        // CHECKSTYLE:OFF
        if (finger7Slot == null)
        {
            finger7Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_7);
            finger7Slot.setBounds(new Rectangle(210,
                                                210,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger7Slot;
        // CHECKSTYLE:ON
    }

    private PhysicalSlotButton getFinger8Slot()
    {
        // CHECKSTYLE:OFF
        if (finger8Slot == null)
        {
            finger8Slot = new PhysicalSlotButton(PhysicalSlotType.FINGER_8);
            finger8Slot.setBounds(new Rectangle(210,
                                                260,
                                                PhysicalSlotButton.DIMENSION,
                                                PhysicalSlotButton.DIMENSION));
        }
        return finger8Slot;
        // CHECKSTYLE:ON
    }

    /**
     * Loads equipment of player.
     */
    public void loadEquipment()
    {
        Actor actor = Client.globalInstance().getPlayerActor();
        for (PhysicalSlotButton slot : slots)
            slot.load(actor);

        switch (actor.getGender())
        {
            case Male:
                bg.setImage(BG_MALE_IMAGE);
                break;
            case Female:
                bg.setImage(BG_FEMALE_IMAGE);
                break;
            case Neutral:
            default:
                assert "This should not happen".equals(null);
                bg.setImage(null);
                break;
        }
    }

    /**
     * This method initializes mainPanel
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel()
    {
        if (mainPanel == null)
        {
            mainPanel = new JPanel();
            mainPanel.setLayout(null);
            mainPanel.add(getHeadSlot(), null);
            mainPanel.add(getFaceSlot(), null);
            mainPanel.add(getNeckSlot(), null);
            mainPanel.add(getRHandSlot(), null);
            mainPanel.add(getLHandSlot(), null);
            mainPanel.add(getHandsSlot(), null);
            mainPanel.add(getTorsoSlot(), null);
            mainPanel.add(getLegsSlot(), null);
            mainPanel.add(getFeetSlot(), null);
            mainPanel.add(getFinger1Slot(), null);
            mainPanel.add(getFinger2Slot(), null);
            mainPanel.add(getFinger3Slot(), null);
            mainPanel.add(getFinger4Slot(), null);
            mainPanel.add(getFinger5Slot(), null);
            mainPanel.add(getFinger6Slot(), null);
            mainPanel.add(getFinger7Slot(), null);
            mainPanel.add(getFinger8Slot(), null);
            mainPanel.add(bg, null);
        }
        return mainPanel;
    }

    /**
     * This method initializes buttonsPanel
     * @return javax.swing.JPanel
     */
    private JPanel getButtonsPanel()
    {
        // CHECKSTYLE:OFF
        if (buttonsPanel == null)
        {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 0.5;
            gridBagConstraints2.fill = GridBagConstraints.NONE;
            gridBagConstraints2.anchor = GridBagConstraints.EAST;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 0.5;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridx = 0;
            buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new GridBagLayout());
            buttonsPanel.add(getShowItemsButton(), gridBagConstraints1);
            buttonsPanel.add(getShowStatsButton(), gridBagConstraints2);
        }
        return buttonsPanel;
        // CHECKSTYLE:ON
    }

    /**
     * This method initializes showStatsButton
     * @return javax.swing.JButton
     */
    private JButton getShowStatsButton()
    {
        if (showStatsButton == null)
        {
            showStatsButton = new JButton();
            showStatsButton.setText("Stats");
            showStatsButton.setHorizontalTextPosition(SwingConstants.LEFT);
            showStatsButton.setIcon(ARROW_RIGHT);
            showStatsButton.setRolloverIcon(ARROW_ROLLOVER_RIGHT);
            showStatsButton.setPressedIcon(ARROW_ROLLOVER_RIGHT);
            showStatsButton.setRolloverEnabled(true);
            showStatsButton.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    final ClientSwingUI ui =
                            (ClientSwingUI) Client.globalInstance().getUi();

                    JInternalFrame sta = ui.getPlayerStatisticsFrame();
                    JInternalFrame equ = ui.getEquipmentFrame();

                    sta.setBounds(equ.getX() + equ.getWidth(),
                                  equ.getY(),
                                  sta.getWidth(),
                                  equ.getHeight());
                    sta.setVisible(true);
                }
            });
        }
        return showStatsButton;
    }

    /**
     * This method initializes showItemsButton
     * @return javax.swing.JButton
     */
    private JButton getShowItemsButton()
    {
        if (showItemsButton == null)
        {
            showItemsButton = new JButton();
            showItemsButton.setText("Items");
            showItemsButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            showItemsButton.setIcon(ARROW_LEFT);
            showItemsButton.setRolloverIcon(ARROW_ROLLOVER_LEFT);
            showItemsButton.setPressedIcon(ARROW_ROLLOVER_LEFT);
            showItemsButton.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    final ClientSwingUI ui =
                            (ClientSwingUI) Client.globalInstance().getUi();

                    JInternalFrame inv = ui.getInventoryFrame();
                    JInternalFrame equ = ui.getEquipmentFrame();

                    inv.setBounds(equ.getX() - inv.getWidth(),
                                  equ.getY(),
                                  inv.getWidth(),
                                  equ.getHeight());
                    inv.setVisible(true);
                }
            });
        }
        return showItemsButton;
    }
} //  @jve:decl-index=0:visual-constraint="31,21"
