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
package pl.org.minions.stigma.client.ui.swing.chat;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.ChatType;
import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.chat.client.ProcessedChatListener;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.logger.Log;

/**
 * Displays chat messages. Works in two modes:
 * {@code interactive} and {@code not interactive}. In
 * {@code interactive} mode text can be selected, scrolled
 * etc. In
 * second mode all mouse events are passed 'below', to map
 * panel.
 */
public class ChatTextPanel extends JScrollPane implements ProcessedChatListener
{
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 576;
    private static final int HEIGHT = 80;
    private static final int MAX_BUFFER_SIZE = 40000;
    private JPanel mapPanel;
    private JTextPane area;
    private boolean interactiveMode;

    private class TransparentTextPane extends JTextPane
    {
        private static final long serialVersionUID = 1L;

        public TransparentTextPane()
        {
            setName(TransparentTextPane.class.getSimpleName());
            setBorder(BorderFactory.createEmptyBorder());
            setEditable(false);
            enableEvents(AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK);
            setOpaque(interactiveMode);
        }

        /** {@inheritDoc} */
        @Override
        protected void processMouseMotionEvent(MouseEvent e)
        {
            if (interactiveMode)
                super.processMouseEvent(e);
            else
                redispatchMouseEvent(this, e);
        }

        @Override
        protected void processMouseEvent(MouseEvent e)
        {
            if (interactiveMode)
                super.processMouseEvent(e);
            else
                redispatchMouseEvent(this, e);
        }
    }

    /**
     * Constructor.
     * @param processor
     *            processor from which chat messages will be
     *            received
     * @param mapPanel
     *            map panel to which mouse events should be
     *            passed when not in {@code interactive}
     *            mode
     */
    public ChatTextPanel(ChatProcessor processor, JPanel mapPanel)
    {
        setName(ChatTextPanel.class.getSimpleName());
        getViewport().setName(getName() + ".Viewport");

        this.mapPanel = mapPanel;
        this.area = new TransparentTextPane();

        setViewportView(area);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        processor.addListener(this);

        setSize(WIDTH, HEIGHT);
    }

    /**
     * Returns {@code true} when in {@code interactive}
     * mode.
     * @return mode state
     */
    public boolean isInteractiveMode()
    {
        return interactiveMode;
    }

    /**
     * Changes mode.
     * @param interactiveMode
     *            new value of mode
     */
    public void setInteractiveMode(boolean interactiveMode)
    {
        this.interactiveMode = interactiveMode;
        this.setVerticalScrollBarPolicy(interactiveMode ? VERTICAL_SCROLLBAR_ALWAYS
                                                       : VERTICAL_SCROLLBAR_NEVER);

        area.setOpaque(interactiveMode);
    }

    private void redispatchMouseEvent(JComponent source, MouseEvent e)
    {
        Point mapPoint =
                SwingUtilities.convertPoint(source, e.getPoint(), mapPanel);
        if (mapPoint.y < 0)
            return;

        mapPanel.dispatchEvent(new MouseEvent(mapPanel,
                                              e.getID(),
                                              e.getWhen(),
                                              e.getModifiers(),
                                              mapPoint.x,
                                              mapPoint.y,
                                              e.getClickCount(),
                                              e.isPopupTrigger()));

    }

    @Override
    public void chatReceived(ChatTarget target, Chat msg)
    {
        StringBuffer label = new StringBuffer();
        label.append("<");
        Actor a =
                Client.globalInstance().getWorld().getActor(msg.getSenderId());
        if (a == null || a.getName() == null || a.getName().isEmpty())
        {
            label.append("#");
            label.append(msg.getSenderId());
        }
        else
        {
            label.append(a.getName());

            if (target.getType() == ChatType.WHISPER)
            {
                label.append(":");
                final int recipientId = msg.getRecipients().iterator().next();

                Actor recipient =
                        Client.globalInstance()
                              .getWorld()
                              .getActor(recipientId);
                if (recipient == null || recipient.getName() == null
                    || recipient.getName().isEmpty())
                {
                    label.append("");
                    label.append(recipientId);
                }
                else
                    label.append(recipient.getName());
            }

        }
        label.append("> ");

        label.append(msg.getText());
        label.append("\n");

        Document doc = area.getDocument();
        try
        {
            doc.insertString(doc.getLength(), label.toString(), null);
        }
        catch (BadLocationException e)
        {
            Log.logger.fatal(e);
        }

        if (doc.getLength() > MAX_BUFFER_SIZE)
        {
            try
            {
                doc.remove(0, MAX_BUFFER_SIZE / 2); // This can leave partial lines hanging. (#270)
            }
            catch (BadLocationException e)
            {
                Log.logger.fatal(e);
            }
        }
    }
}
