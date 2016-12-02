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
package pl.org.minions.stigma.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ClientState;
import pl.org.minions.stigma.client.ui.event.UiEventDispatcher;
import pl.org.minions.stigma.client.ui.swing.InternalFramePacker;
import pl.org.minions.stigma.client.ui.swing.SpriteCanvasPanel;
import pl.org.minions.stigma.client.ui.swing.StigmaInternalFrame;
import pl.org.minions.stigma.client.ui.swing.chat.ChatPanel;
import pl.org.minions.stigma.client.ui.swing.chat.ChatTextPanel;
import pl.org.minions.stigma.client.ui.swing.game.SidePanel;
import pl.org.minions.stigma.client.ui.swing.game.EquipmentPanel;
import pl.org.minions.stigma.client.ui.swing.game.IndicatorIconsPane;
import pl.org.minions.stigma.client.ui.swing.game.InventoryPanel;
import pl.org.minions.stigma.client.ui.swing.game.ItemsOnGroundPanel;
import pl.org.minions.stigma.client.ui.swing.game.PlayerOverloadedIndicator;
import pl.org.minions.stigma.client.ui.swing.game.PlayerStatisticsPanel;
import pl.org.minions.stigma.client.ui.swing.login.ActorChooserPanel;
import pl.org.minions.stigma.client.ui.swing.login.ConnectionPanel;
import pl.org.minions.stigma.client.ui.swing.login.ConnectionPanel.ConnectionPanelReason;
import pl.org.minions.stigma.client.ui.swing.login.LoadingPanel;
import pl.org.minions.stigma.client.ui.swing.login.LoadingPanel.LoadingPanelType;
import pl.org.minions.stigma.client.ui.swing.login.LoginPanel;
import pl.org.minions.stigma.databases.xml.client.XmlReader;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing Client interface made in SWING.
 */
public class ClientSwingUI implements ClientUI

{
    private class UIThread implements Runnable
    {
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                try
                {
                    Thread.sleep(REFRESH_INTERVAL);
                }
                catch (InterruptedException e)
                {
                    break;
                }
                spriteCanvasPanel.repaint();
            }
        }
    }

    private static final int REFRESH_INTERVAL = 20;

    private static final Dimension WINDOW_SIZE = new Dimension(800, 600);

    private static final int INTERNAL_FRAME_HEIGHT_MODIFIER = 30;
    private static final int INTERNAL_FRAME_WIDTH_MODIFIER = 10;

    private JDesktopPane desktopPane = new JDesktopPane();
    private SpriteCanvasPanel spriteCanvasPanel;
    private LoginPanel loginPanel;
    private ConnectionPanel connectionPanel;
    private ActorChooserPanel actorChooserPanel;
    private LoadingPanel gameDataLoadingPanel;
    private ItemsOnGroundPanel itemsOnGroundPanel;
    private InventoryPanel inventoryPanel;
    private EquipmentPanel equipmentPanel;
    private PlayerStatisticsPanel playerStatisticsPanel;
    private LoadingPanel mapDataLoadingPanel;
    private LoadingPanel connectingPanel;
    private LoadingPanel authenticationProcessingPanel;
    private ChatPanel chatPanel;
    private ChatTextPanel chatTextPanel;

    private SidePanel borderPanel;

    private JPanel indicatorsPane;

    private StigmaInternalFrame equipmentFrame;
    private StigmaInternalFrame itemsOnGroundFrame;
    private StigmaInternalFrame inventoryFrame;
    private StigmaInternalFrame loginFrame;
    private StigmaInternalFrame connectionFrame;
    private StigmaInternalFrame actorChooserFrame;
    private StigmaInternalFrame gameDataLoadingFrame;
    private StigmaInternalFrame mapDataLoadingFrame;
    private StigmaInternalFrame conncectingFrame;
    private StigmaInternalFrame authenticationProcessingFrame;
    private StigmaInternalFrame playerStatisticsFrame;

    private Thread uiThread;

    private final Runnable gameInProgressStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            spriteCanvasPanel.setVisible(true);
            borderPanel.setVisible(true);
            borderPanel.loadActor();
            indicatorsPane.setVisible(true);
            inventoryPanel.loadItems();
            playerStatisticsPanel.update();
            equipmentPanel.loadEquipment();
            chatPanel.setVisible(true);
            chatTextPanel.setVisible(true);
        }
    };

    private final Runnable authenticationPromptStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            loginPanel.setWelcomeStatus();
            loginFrame.setVisible(true);
            loginPanel.requestFocus();
        }
    };

    private final Runnable authenticationProcessingStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            authenticationProcessingFrame.setVisible(true);
        }
    };

    private final Runnable loggedOutStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            connectionFrame.setTitle(ConnectionPanel.getTitle(ConnectionPanelReason.LOGGED_OUT));
            connectionPanel.setReason(ConnectionPanelReason.LOGGED_OUT);
            connectionFrame.setVisible(true);
            connectionPanel.requestFocus();
        }
    };

    private final Runnable authenticatinFailedStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            loginPanel.setAuthenticationFailedStatus();
            loginFrame.setVisible(true);
            loginPanel.requestFocus();
        }
    };

    private final Runnable connectionFailedStateSetter = new Runnable()
    {
        @Override
        public void run()
        {
            setAllNotVisible();
            connectionFrame.setTitle(ConnectionPanel.getTitle(ConnectionPanelReason.CONNECTION_LOST));
            connectionPanel.setReason(ConnectionPanelReason.CONNECTION_LOST);
            connectionFrame.setVisible(true);
            connectionPanel.requestFocus();
        }
    };

    private final Runnable actorPromptStateSetter = new Runnable()
    {

        @Override
        public void run()
        {
            setAllNotVisible();
            actorChooserPanel.getActors();
            actorChooserFrame.setVisible(true);
            actorChooserPanel.requestFocus();
        }
    };

    private final Runnable gameDataLoadingStateSetter = new Runnable()
    {

        @Override
        public void run()
        {
            setAllNotVisible();
            XmlReader.globalInstance().addObserver(gameDataLoadingPanel);
            gameDataLoadingFrame.setVisible(true);
        }
    };

    private final Runnable mapDataLoadingStateSetter = new Runnable()
    {

        @Override
        public void run()
        {
            setAllNotVisible();
            XmlReader.globalInstance().addObserver(mapDataLoadingPanel);
            mapDataLoadingFrame.setVisible(true);
        }
    };

    private final Runnable connectingStateSetter = new Runnable()
    {

        @Override
        public void run()
        {
            setAllNotVisible();
            conncectingFrame.setVisible(true);
        }
    };

    private final Runnable defaultStateSetter = new Runnable()
    {

        @Override
        public void run()
        {
            setAllNotVisible();
        }
    };

    /**
     * Creates a new instance of ClientSwingUI.
     * @param container
     *            the AWT {@link Container} to place the
     *            user interface on
     */
    public ClientSwingUI(Container container)
    {
        container.add(desktopPane);
        SwingUtilities.updateComponentTreeUI(desktopPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize()
    {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        spriteCanvasPanel = new SpriteCanvasPanel();

        chatTextPanel =
                new ChatTextPanel(Client.globalInstance().getChatProcessor(),
                                  spriteCanvasPanel);
        chatTextPanel.setLocation(0, spriteCanvasPanel.getHeight()
            - chatTextPanel.getHeight());

        chatPanel = new ChatPanel(this.desktopPane, chatTextPanel);
        chatPanel.setLocation(0, spriteCanvasPanel.getHeight());

        loginPanel = new LoginPanel();
        loginFrame =
                new StigmaInternalFrame(LoginPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        loginFrame.setSize(LoginPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, LoginPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        loginFrame.setLocation(LoginPanel.getDefaultPositionX(),
                               LoginPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) loginFrame.getUI()).getNorthPane()
                                                                                 .getMouseListeners())
        {
            ((BasicInternalFrameUI) loginFrame.getUI()).getNorthPane()
                                                       .removeMouseListener(listener);
        }
        loginFrame.add(loginPanel);

        connectionPanel = new ConnectionPanel();
        connectionFrame =
                new StigmaInternalFrame(ConnectionPanel.getDefaultName(),
                // connection lost is default reason
                                        false,
                                        false,
                                        false,
                                        false);
        connectionFrame.setSize(ConnectionPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, ConnectionPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        connectionFrame.setLocation(ConnectionPanel.getDefaultPositionX(),
                                    ConnectionPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) connectionFrame.getUI()).getNorthPane()
                                                                                      .getMouseListeners())
        {
            ((BasicInternalFrameUI) connectionFrame.getUI()).getNorthPane()
                                                            .removeMouseListener(listener);
        }
        connectionFrame.add(connectionPanel);

        actorChooserPanel = new ActorChooserPanel();
        actorChooserFrame =
                new StigmaInternalFrame(ActorChooserPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        actorChooserFrame.setSize(ActorChooserPanel.getDefaultWidth()
                                      + INTERNAL_FRAME_WIDTH_MODIFIER,
                                  ActorChooserPanel.getDefaultHeight()
                                      + INTERNAL_FRAME_HEIGHT_MODIFIER);
        actorChooserFrame.setLocation(ActorChooserPanel.getDefaultPositionX(),
                                      ActorChooserPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) actorChooserFrame.getUI()).getNorthPane()
                                                                                        .getMouseListeners())
        {
            ((BasicInternalFrameUI) actorChooserFrame.getUI()).getNorthPane()
                                                              .removeMouseListener(listener);
        }
        actorChooserFrame.add(actorChooserPanel);

        gameDataLoadingPanel = new LoadingPanel();
        gameDataLoadingFrame =
                new StigmaInternalFrame(LoadingPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        gameDataLoadingFrame.setSize(LoadingPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, LoadingPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        gameDataLoadingFrame.setLocation(LoadingPanel.getDefaultPositionX(),
                                         LoadingPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) gameDataLoadingFrame.getUI()).getNorthPane()
                                                                                           .getMouseListeners())
        {
            ((BasicInternalFrameUI) gameDataLoadingFrame.getUI()).getNorthPane()
                                                                 .removeMouseListener(listener);
        }
        gameDataLoadingFrame.add(gameDataLoadingPanel);

        authenticationProcessingPanel =
                new LoadingPanel(LoadingPanelType.Authenticating);
        authenticationProcessingFrame =
                new StigmaInternalFrame(LoadingPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        authenticationProcessingFrame.setSize(LoadingPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, LoadingPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        authenticationProcessingFrame.setLocation(LoadingPanel.getDefaultPositionX(),
                                                  LoadingPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) authenticationProcessingFrame.getUI()).getNorthPane()
                                                                                                    .getMouseListeners())
        {
            ((BasicInternalFrameUI) authenticationProcessingFrame.getUI()).getNorthPane()
                                                                          .removeMouseListener(listener);
        }
        authenticationProcessingFrame.add(authenticationProcessingPanel);

        mapDataLoadingPanel = new LoadingPanel(LoadingPanelType.Loading);
        mapDataLoadingFrame =
                new StigmaInternalFrame(LoadingPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        mapDataLoadingFrame.setSize(LoadingPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, LoadingPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        mapDataLoadingFrame.setLocation(LoadingPanel.getDefaultPositionX(),
                                        LoadingPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) mapDataLoadingFrame.getUI()).getNorthPane()
                                                                                          .getMouseListeners())
        {
            ((BasicInternalFrameUI) mapDataLoadingFrame.getUI()).getNorthPane()
                                                                .removeMouseListener(listener);
        }
        mapDataLoadingFrame.add(mapDataLoadingPanel);

        connectingPanel = new LoadingPanel(LoadingPanelType.Connecting);
        conncectingFrame =
                new StigmaInternalFrame(LoadingPanel.getDefaultName(),
                                        false,
                                        false,
                                        false,
                                        false);
        conncectingFrame.setSize(LoadingPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, LoadingPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        conncectingFrame.setLocation(LoadingPanel.getDefaultPositionX(),
                                     LoadingPanel.getDefaultPositionY());
        for (MouseListener listener : ((BasicInternalFrameUI) conncectingFrame.getUI()).getNorthPane()
                                                                                       .getMouseListeners())
        {
            ((BasicInternalFrameUI) conncectingFrame.getUI()).getNorthPane()
                                                             .removeMouseListener(listener);
        }
        conncectingFrame.add(connectingPanel);

        itemsOnGroundPanel = new ItemsOnGroundPanel();

        itemsOnGroundFrame =
                new StigmaInternalFrame(ItemsOnGroundPanel.getDefaultName(),
                                        true,
                                        true,
                                        true,
                                        false);
        itemsOnGroundFrame.add(itemsOnGroundPanel);
        itemsOnGroundFrame.setSize(ItemsOnGroundPanel.getDefaultWidth()
                                       + INTERNAL_FRAME_WIDTH_MODIFIER,
                                   ItemsOnGroundPanel.getDefaultHeight()
                                       + INTERNAL_FRAME_HEIGHT_MODIFIER);

        playerStatisticsPanel = new PlayerStatisticsPanel();
        playerStatisticsFrame =
                new StigmaInternalFrame(PlayerStatisticsPanel.getDefaultName(),
                                        false,
                                        true,
                                        false,
                                        false);
        playerStatisticsFrame.add(playerStatisticsPanel);
        playerStatisticsFrame.setSize(PlayerStatisticsPanel.getDefaultWidth()
                                          + INTERNAL_FRAME_WIDTH_MODIFIER,
                                      PlayerStatisticsPanel.getDefaultHeight()
                                          + INTERNAL_FRAME_HEIGHT_MODIFIER);
        playerStatisticsFrame.setDefaultLocation(spriteCanvasPanel.getWidth()
            - playerStatisticsFrame.getWidth(), 0);

        equipmentPanel = new EquipmentPanel();
        equipmentFrame =
                new StigmaInternalFrame(EquipmentPanel.getDefaultName(),
                                        false,
                                        true,
                                        false,
                                        false);
        equipmentFrame.add(equipmentPanel);
        equipmentFrame.setSize(EquipmentPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, EquipmentPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        equipmentFrame.setDefaultLocation(spriteCanvasPanel.getWidth()
            - equipmentFrame.getWidth() - playerStatisticsFrame.getWidth(), 0);

        inventoryPanel = new InventoryPanel();
        inventoryFrame =
                new StigmaInternalFrame(InventoryPanel.getDefaultName(),
                                        true,
                                        true,
                                        true,
                                        false);
        inventoryFrame.add(inventoryPanel);

        inventoryFrame.setSize(InventoryPanel.getDefaultWidth()
            + INTERNAL_FRAME_WIDTH_MODIFIER, InventoryPanel.getDefaultHeight()
            + INTERNAL_FRAME_HEIGHT_MODIFIER);
        inventoryFrame.setDefaultLocation(0, spriteCanvasPanel.getHeight()
            - inventoryFrame.getWidth());

        borderPanel = new SidePanel();
        borderPanel.setLocation(spriteCanvasPanel.getWidth(), 0);

        desktopPane.add(spriteCanvasPanel, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(chatPanel, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(chatTextPanel, JLayeredPane.DEFAULT_LAYER, 0);
        desktopPane.add(loginFrame, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(connectionFrame, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(actorChooserFrame, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(gameDataLoadingFrame,
                        JLayeredPane.FRAME_CONTENT_LAYER,
                        0);
        desktopPane.add(authenticationProcessingFrame,
                        JLayeredPane.FRAME_CONTENT_LAYER,
                        0);
        desktopPane.add(conncectingFrame, JLayeredPane.FRAME_CONTENT_LAYER, 0);
        desktopPane.add(mapDataLoadingFrame,
                        JLayeredPane.FRAME_CONTENT_LAYER,
                        0);

        desktopPane.add(borderPanel, JLayeredPane.DEFAULT_LAYER, 0);

        desktopPane.add(itemsOnGroundFrame, JLayeredPane.PALETTE_LAYER, 0);
        desktopPane.add(inventoryFrame, JLayeredPane.PALETTE_LAYER, 0);
        desktopPane.add(equipmentFrame, JLayeredPane.PALETTE_LAYER, 0);
        desktopPane.add(playerStatisticsFrame, JLayeredPane.PALETTE_LAYER, 0);

        desktopPane.setBackground(Color.BLACK);
        desktopPane.setPreferredSize(WINDOW_SIZE);

        indicatorsPane = new IndicatorIconsPane();
        indicatorsPane.setSize(spriteCanvasPanel.getSize());

        desktopPane.add(indicatorsPane, JLayeredPane.PALETTE_LAYER);

        indicatorsPane.add(new PlayerOverloadedIndicator());
        indicatorsPane.revalidate();

        setAllNotVisible();
    }

    /** {@inheritDoc} */
    @Override
    public void start()
    {
        if (uiThread == null)
        {
            uiThread = new Thread(new UIThread(), "ClientSwingUI.UIThread");
            uiThread.start();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop()
    {
        if (uiThread != null)
        {
            uiThread.interrupt();
            uiThread = null;
        }

    }

    /** {@inheritDoc} */
    @Override
    public boolean setClientState(ClientState state)
    {
        switch (state)
        {
            case CONNECTING:
                return invokeStateSetter(connectingStateSetter);
            case AUTHENTICATION_PROMPT:
                return invokeStateSetter(authenticationPromptStateSetter);
            case AUTHENTICATION_ERROR:
                return invokeStateSetter(authenticatinFailedStateSetter);
            case AUTHENTICATION_PROCESSING:
                return invokeStateSetter(authenticationProcessingStateSetter);
            case GAME_IN_PROGRESS:
                return invokeStateSetter(gameInProgressStateSetter);
            case DISCONNECTED:
                return invokeStateSetter(connectionFailedStateSetter);
            case ACTOR_PROMPT:
                return invokeStateSetter(actorPromptStateSetter);
            case GAME_DATA_LOADING:
                return invokeStateSetter(gameDataLoadingStateSetter);
            case MAP_DATA_LOADING:
                return invokeStateSetter(mapDataLoadingStateSetter);
            case LOGGED_OUT:
                return invokeStateSetter(loggedOutStateSetter);
            default:
                invokeStateSetter(defaultStateSetter);
                Log.logger.warn("UIState not handled by "
                    + this.getClass().getName());
                return false;

        }
    }

    private boolean invokeStateSetter(Runnable setter)
    {
        if (!EventQueue.isDispatchThread())
            try
            {

                java.awt.EventQueue.invokeAndWait(setter);
            }
            catch (InterruptedException e)
            {
                Log.logger.warn("Invoking state setter interrupted.", e);
                return false;
            }
            catch (InvocationTargetException e)
            {
                Log.logger.warn("Invoking state setter caused an exception.", e);
                return false;
            }
        else
            setter.run();
        return true;
    }

    /**
     * Makes all components not visible.
     */
    private void setAllNotVisible()
    {
        if (spriteCanvasPanel.isVisible())
            spriteCanvasPanel.setVisible(false);
        if (borderPanel.isVisible())
            borderPanel.setVisible(false);
        if (loginFrame.isVisible())
            loginFrame.setVisible(false);
        if (conncectingFrame.isVisible())
            conncectingFrame.setVisible(false);
        if (mapDataLoadingFrame.isVisible())
            mapDataLoadingFrame.setVisible(false);
        if (authenticationProcessingFrame.isVisible())
            authenticationProcessingFrame.setVisible(false);
        if (connectionFrame.isVisible())
            connectionFrame.setVisible(false);
        if (actorChooserFrame.isVisible())
            actorChooserFrame.setVisible(false);
        if (itemsOnGroundFrame.isVisible())
            itemsOnGroundFrame.setVisible(false);
        if (inventoryFrame.isVisible())
            inventoryFrame.setVisible(false);
        if (equipmentFrame.isVisible())
            equipmentFrame.setVisible(false);
        if (playerStatisticsFrame.isVisible())
            playerStatisticsFrame.setVisible(false);
        if (indicatorsPane.isVisible())
            indicatorsPane.setVisible(false);
        if (gameDataLoadingFrame.isVisible())
        {
            gameDataLoadingFrame.setVisible(false);
            XmlReader.globalInstance().removeObserver(gameDataLoadingPanel);
        }
        if (chatPanel.isVisible())
            chatPanel.setVisible(false);
        if (chatTextPanel.isVisible())
            chatTextPanel.setVisible(false);
    }

    /** {@inheritDoc} */
    @Override
    public UiEventDispatcher createDispatcher()
    {
        return new UiEventDispatcher()
        {
            @Override
            protected void performDispatch(Runnable runnable, boolean wait)
            {
                if (wait)
                    try
                    {
                        java.awt.EventQueue.invokeAndWait(runnable);
                        return;
                    }
                    catch (InterruptedException e)
                    {
                        Log.logger.error("Interrupted, performing later, e:"
                            + e);
                    }
                    catch (InvocationTargetException e)
                    {
                        Log.logger.error("Runnable exception", e);
                        return;
                    }
                java.awt.EventQueue.invokeLater(runnable);
            }
        };
    }

    /**
     * Returns equipmentFrame.
     * @return equipmentFrame
     */
    public StigmaInternalFrame getEquipmentFrame()
    {
        return equipmentFrame;
    }

    /**
     * Returns inventoryFrame.
     * @return inventoryFrame
     */
    public StigmaInternalFrame getInventoryFrame()
    {
        return inventoryFrame;
    }

    /** {@inheritDoc} */
    @Override
    public AreaView getAreaView()
    {
        return spriteCanvasPanel;
    }

    /**
     * Returns itemsOnGroundFrame.
     * @return itemsOnGroundFrame
     */
    public StigmaInternalFrame getGroundFrame()
    {
        return itemsOnGroundFrame;
    }

    /**
     * Returns indicatorsPane.
     * @return indicatorsPane
     */
    public JPanel getIndicatorsPane()
    {
        return indicatorsPane;
    }

    /**
     * Returns players statistics frame.
     * @return players statistics frame.
     */
    public StigmaInternalFrame getPlayerStatisticsFrame()
    {
        return playerStatisticsFrame;
    }

    /**
     * Creates and shows an internal frame with custom title
     * and contents.
     * @param name
     *            title of the new internal frame
     * @param contents
     *            component to place inside the new internal
     *            frame
     * @param resizeable
     *            should the frame be resizeable
     * @return the new frame
     */
    public StigmaInternalFrame showCustomFrame(String name,
                                               JComponent contents,
                                               boolean resizeable)
    {
        StigmaInternalFrame frame =
                new StigmaInternalFrame(name, resizeable, true, false, false);
        frame.setLayout(new BorderLayout());
        frame.add(contents);

        frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(frame, JDesktopPane.PALETTE_LAYER);
        frame.pack();

        contents.addComponentListener(new InternalFramePacker(frame));

        frame.show();
        frame.moveToFront();

        return frame;
    }
}
