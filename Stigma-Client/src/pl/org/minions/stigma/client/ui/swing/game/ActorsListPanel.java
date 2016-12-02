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

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorChangedMapListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorDataChangedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorRemovedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorWalkListener;
import pl.org.minions.stigma.client.ui.swing.game.actions.actors.ChatAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.actors.ExamineActorAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.actors.FollowAction;
import pl.org.minions.stigma.client.ui.swing.game.actions.actors.MoveToAction;
import pl.org.minions.stigma.client.ui.swing.game.components.actors.ActorTable;
import pl.org.minions.stigma.client.ui.swing.game.models.ActorTableColumnModel;
import pl.org.minions.stigma.client.ui.swing.game.models.ActorTableModel;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorRemoved;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.globals.Position;

/**
 * This panel is used to display list of actors.
 */
public class ActorsListPanel extends JPanel
{
    private final class ActorChangedMapListenerImplementation implements
                                                             ActorChangedMapListener
    {
        @Override
        public void actorChangedMap(ActorChangedMap event, boolean playerActor)
        {
            if (playerActor)
            {
                ActorTableModel atm =
                        new ActorTableModel(ActorTableColumnModel.createDefaultColumns(),
                                            Client.globalInstance()
                                                  .getPlayerActor()
                                                  .getId());

                for (Segment s : Client.globalInstance()
                                       .getPlayerSegment()
                                       .neighborhood())
                    atm.addActors(s.getActors());
                actorTable.setModel(atm);

                Client.globalInstance()
                      .getUi()
                      .getAreaView()
                      .getActorSelectionView()
                      .setSelectedActor(null);
            }
            else
            {
                ActorTableModel atm = (ActorTableModel) actorTable.getModel();

                Actor a = Client.globalInstance().getPlayerActor();
                if (event.getMapId() == a.getMapId()
                    && event.getInstanceNo() == a.getMapInstanceNo())
                {
                    Actor actor =
                            Client.globalInstance()
                                  .getWorld()
                                  .getActor(event.getActorId());
                    if (actor == null)
                        return;
                    atm.addActor(actor);
                }
                else
                    atm.removeActor(event.getActorId());
            }
        }
    }

    private final class ActorWalkListenerImplementation implements
                                                       ActorWalkListener
    {
        @Override
        public void actorWalked(ActorWalk event,
                                Move command,
                                boolean playerActor)
        {
            if (playerActor)
            {
                ActorTableModel atm = (ActorTableModel) actorTable.getModel();
                LinkedList<Actor> list = new LinkedList<Actor>();

                for (Actor a : atm.getActors())
                    if (!isInNeighborhood(a.getPosition()))
                        list.add(a);
                for (Actor a : list)
                    atm.removeActor(a);

                for (Segment s : Client.globalInstance()
                                       .getPlayerSegment()
                                       .neighborhood())
                    atm.addActors(s.getActors());
                actorTable.repaint();
            }
            else
            {
                ActorTableModel atm = (ActorTableModel) actorTable.getModel();
                Actor actor =
                        Client.globalInstance()
                              .getWorld()
                              .getActor(event.getActorId());
                if (actor == null)
                    return;

                if (isInNeighborhood(event.getNewPosition()))
                    atm.addActor(actor);
                else
                    atm.removeActor(actor);
                actorTable.repaint();
            }
        }
    }

    private static final long serialVersionUID = 1L;

    private JScrollPane scrollPane;
    private ActorTable actorTable;

    /**
     * Constructor.
     */
    public ActorsListPanel()
    {
        super();

        initialize();

        UiEventRegistry uiEventRegistry =
                Client.globalInstance().uiEventRegistry();

        uiEventRegistry.addActorWalkListener(new ActorWalkListenerImplementation());
        uiEventRegistry.addActorChangedMapListener(new ActorChangedMapListenerImplementation());
        uiEventRegistry.addActorAddedListener(new ActorAddedListener()
        {
            @Override
            public void actorAdded(ActorAdded event, boolean playerActor)
            {
                if (playerActor)
                {
                    ActorTableModel atm =
                            new ActorTableModel(ActorTableColumnModel.createDefaultColumns(),
                                                Client.globalInstance()
                                                      .getPlayerActor()
                                                      .getId());

                    for (Segment s : Client.globalInstance()
                                           .getPlayerSegment()
                                           .neighborhood())
                        atm.addActors(s.getActors());
                    actorTable.setModel(atm);
                }
                else
                {
                    ActorTableModel atm =
                            (ActorTableModel) actorTable.getModel();
                    Actor actor = event.getActor();
                    if (actor == null)
                        return;

                    if (isInNeighborhood(actor.getPosition()))
                        atm.addActor(actor);
                    actorTable.repaint();
                }
            }
        });

        uiEventRegistry.addActorRemovedListener(new ActorRemovedListener()
        {

            @Override
            public void actorRemoved(ActorRemoved event)
            {
                ActorTableModel atm = (ActorTableModel) actorTable.getModel();
                atm.removeActor(event.getActorId());
            }
        });

        //refreshing when data changed
        uiEventRegistry.addActorDataChangedListener(new ActorDataChangedListener()
        {
            @Override
            public void actorDataChanged(int id)
            {
                ActorTableModel atm = (ActorTableModel) actorTable.getModel();
                if (atm.containsActor(id))
                    actorTable.repaint();
            }
        });
    }

    /**
     * Determines whether position is in vicinity.
     * @param pos
     *            position to check
     * @return {@code true} when position is in neighborhood
     */
    private boolean isInNeighborhood(Position pos)
    {
        assert pos != null;

        Segment segment = Client.globalInstance().getPlayerSegment();

        assert segment != null;

        for (Segment s : segment.neighborhood())
        {
            if (s.contains(pos))
            {
                return true;
            }
        }

        return false;
    }

    private void initialize()
    {
        // CHECKSTYLE:OFF
        this.setLayout(null);
        ActorTableModel atm =
                new ActorTableModel(ActorTableColumnModel.createDefaultColumns(),
                                    0); // zero - placeholder, will be changed when changing map

        ActorTableColumnModel atcm = new ActorTableColumnModel();

        this.actorTable = new ActorTable(atm, atcm);
        this.actorTable.setPopupActions(new MoveToAction(),
                                        new FollowAction(),
                                        new ChatAction(),
                                        null,
                                        new ExamineActorAction()); // TODO examine action
        this.actorTable.setOpaque(true);
        this.actorTable.setFillsViewportHeight(true);
        //
        this.actorTable.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                int row = actorTable.rowAtPoint(e.getPoint());

                if (row != -1)
                {
                    int actorId =
                            ((Actor) actorTable.getModel().getValueAt(row, 0)).getId();
                    Client.globalInstance()
                          .getUi()
                          .getAreaView()
                          .getActorSelectionView()
                          .setSelectedActor(actorId);
                }
                else if (actorTable.getSelectedRow() >= 0)
                {
                    int actorId =
                            ((Actor) actorTable.getModel()
                                               .getValueAt(actorTable.getSelectedRow(),
                                                           0)).getId();
                    Client.globalInstance()
                          .getUi()
                          .getAreaView()
                          .getActorSelectionView()
                          .setSelectedActor(actorId);
                }
            }
        });
        this.actorTable.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (actorTable.getSelectedRow() >= 0
                    && actorTable.getSelectedRow() < actorTable.getModel()
                                                               .getRowCount())
                {
                    int actorId =
                            ((Actor) actorTable.getModel()
                                               .getValueAt(actorTable.getSelectedRow(),
                                                           0)).getId();
                    Client.globalInstance()
                          .getUi()
                          .getAreaView()
                          .getActorSelectionView()
                          .setSelectedActor(actorId);
                }
            }

        });

        this.scrollPane = new JScrollPane(actorTable);
        this.scrollPane.setBounds(new Rectangle(4, 4, 181, 441));
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane);
        // CHECKSTYLE:ON
    }
}
