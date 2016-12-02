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

import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.chat.client.ChatTarget;
import pl.org.minions.stigma.chat.client.ChatTargetChangedListener;

/**
 * "Pop-up like" component for choosing target. Also allows
 * to enable/disable filtering of chosen targets.
 */
class ChatTargetChooser extends JPanel implements ChatTargetChangedListener
{
    private static final long serialVersionUID = 1L;
    private ChatProcessor processor;

    /**
     * Constructor.
     * @param processor
     *            chat processor from which chat target list
     *            should be taken
     */
    public ChatTargetChooser(ChatProcessor processor)
    {
        super();
        this.processor = processor;

        setName("ChatTargetChooser");
        initialize();
        refill();

        processor.addListener(this);
    }

    private void refill()
    {
        this.removeAll();
        for (ChatTarget target : processor.targets())
            if (!target.isEcho())
                this.add(new ChatTargetComponent(target, processor));
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // CHECKSTYLE:ON
    }

    /** {@inheritDoc} */
    @Override
    public void chatTargetChoosen(ChatTarget target)
    {
        setVisible(false);
    }

    /**
     * Shows chooser with bottom left corner on given
     * location.
     * @param bottomLeft
     *            location of bottom left corner of chooser
     */
    public void popup(Point bottomLeft)
    {
        refill();
        setSize(getPreferredSize());
        setVisible(true);
        setLocation(bottomLeft.x, bottomLeft.y - getSize().height);
        repaint();
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
