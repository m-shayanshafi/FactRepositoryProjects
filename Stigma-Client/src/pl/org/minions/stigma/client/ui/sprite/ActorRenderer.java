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
package pl.org.minions.stigma.client.ui.sprite;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.observers.CurrentSegmentChangeObserver;
import pl.org.minions.stigma.client.ui.AreaView;
import pl.org.minions.stigma.client.ui.Clearable;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ActorAddedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorChangedMapListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorRemovedListener;
import pl.org.minions.stigma.client.ui.event.listeners.ActorWalkListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorChangedMap;
import pl.org.minions.stigma.game.event.actor.ActorRemoved;
import pl.org.minions.stigma.game.event.actor.ActorWalk;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.utils.logger.Log;

/**
 * Manages {@link ActorAnimator actor animators} in order to
 * render all {@link Actor actors} in the field of view.
 * Interprets {@link Event events} and issues animator
 * orders.
 */
public class ActorRenderer implements
                          CurrentSegmentChangeObserver,
                          Clearable,
                          AreaView.ActorSelectionView
{
    /**
     * Segment that contains the current player actor.
     */
    private MapInstance.Segment currentSegment;

    private Map<Integer, ActorAnimator> visibleAnimators =
            new TreeMap<Integer, ActorAnimator>();

    /**
     * Animators for actors that left field of view.
     * <p>
     * They will finish up any animations and be removed
     * during {@link #animateAll(long)}.
     */
    private Map<Integer, ActorAnimator> remoteAnimators =
            new TreeMap<Integer, ActorAnimator>();

    private ForegroundLayerGroup foreground;

    private Integer selectedActorId;

    /**
     * Create new actor renderer.
     * @param foreground
     *            foreground layer group
     */
    public ActorRenderer(ForegroundLayerGroup foreground)
    {
        this.foreground = foreground;

        final Client client = Client.globalInstance();
        client.addCurrentSegmentChangeObserver(this);

        UiEventRegistry uiEventRegistry = client.uiEventRegistry();

        uiEventRegistry.addActorChangedMapListener(new ActorChangedMapListener()
        {
            @Override
            public void actorChangedMap(ActorChangedMap event,
                                        boolean playerActor)
            {
                if (!playerActor)
                    handleOtherActorChangedMap(event);
                else if (currentSegment != null)
                    fetchActorAnimator(event.getActorId()).setPosition(event.getPosition());
            }
        });

        uiEventRegistry.addActorWalkListener(new ActorWalkListener()
        {
            @Override
            public void actorWalked(ActorWalk event,
                                    Move command,
                                    boolean playerActor)
            {
                if (!playerActor)
                    handleOtherActorWalk(event, command);
                else
                {
                    final Actor playerActorA =
                            Client.globalInstance().getPlayerActor();

                    final ActorAnimator animator =
                            fetchActorAnimator(playerActorA.getId());

                    animator.setPosition(event.getNewPosition()
                                              .newPosition(command.getDirection()
                                                                  .getOpposite()));
                    animator.walk(command.getDirection());
                }
            }
        });

        uiEventRegistry.addActorAddedListener(new ActorAddedListener()
        {
            @Override
            public void actorAdded(ActorAdded event, boolean playerActor)
            {
                if (!playerActor)
                    handleActorAdded(event);
            }
        });

        uiEventRegistry.addActorRemovedListener(new ActorRemovedListener()
        {
            @Override
            public void actorRemoved(ActorRemoved event)
            {
                handleActorRemoved(event);
            }
        });
    }

    /**
     * Notifies all animators about the passage of time.
     * @param milliseconds
     *            the time passed
     */
    public void animateAll(long milliseconds)
    {
        for (ActorAnimator animator : visibleAnimators.values())
        {
            animator.animate(milliseconds);
        }
        for (Iterator<ActorAnimator> it = remoteAnimators.values().iterator(); it.hasNext();)
        {
            ActorAnimator animator = it.next();
            animator.animate(milliseconds);
            if (animator.isAnimationFinished())
            {
                animator.remove();
                it.remove();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void currentSegmentChanged(Segment currentSegment,
                                      Segment previousSegment,
                                      Collection<Segment> appearingSegments,
                                      Collection<Segment> disappearingSegments)
    {
        this.currentSegment = currentSegment;
        if (currentSegment == null)
        {
            //            pathTargetSprite.setVisible(false);
            removeActorAnimators();
            return;
        }

        if (previousSegment != null
            && currentSegment.getParentMap() != previousSegment.getParentMap())
        {
            //            pathTargetSprite.setVisible(false);
            removeActorAnimators();
        }
        else
        {
            for (Segment segment : disappearingSegments)
                for (Actor actor : segment.getActors())
                {
                    ActorAnimator animator;
                    animator = visibleAnimators.remove(actor.getId());
                    if (animator != null)
                    //                        animator.remove();
                    {
                        remoteAnimators.put(actor.getId(), animator);
                    }
                }
        }
        for (Segment segment : appearingSegments)
            for (Actor actor : segment.getActors())
            {
                fetchActorAnimator(actor.getId()).setPosition(actor.getPosition());
            }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        currentSegment = null;
        //        pathTargetSprite.setVisible(false);
        removeActorAnimators();
    }

    private ActorAnimator fetchActorAnimator(int actorId)
    {
        ActorAnimator animator;
        animator = visibleAnimators.get(actorId);
        if (animator != null)
            return animator;

        animator = remoteAnimators.remove(actorId);
        if (animator != null)
        {

            visibleAnimators.put(actorId, animator);
            return animator;
        }

        if (Log.isDebugEnabled())
            Log.logger.debug("Creating ActorAnimator for actor: " + actorId);

        animator =
                new ActorAnimator(actorId,
                                  Client.globalInstance().getWorld(),
                                  foreground);
        visibleAnimators.put(actorId, animator);

        animator.setHighlighted(selectedActorId != null
            && actorId == selectedActorId);

        final boolean isPlayerActor =
                Client.globalInstance().getPlayerActor().getId() == actorId;
        animator.setActorColor(isPlayerActor ? VisualizationGlobals.COLOR_SELF
                                            : VisualizationGlobals.COLOR_FOE);
        animator.setBarsVisible(!isPlayerActor);

        return animator;
    }

    /**
     * Returns the location of the representation of a
     * chosen actor on canvas, if the actor is currently
     * represented and within field of view.
     * @param actorId
     *            chosen actor id
     * @return location on screen in pixels
     */
    public Point getActorLocation(int actorId)
    {
        final ActorAnimator animator;
        animator = visibleAnimators.get(actorId);
        if (animator != null)
            return animator.getActorLocation();
        else
            return null;
    }

    private void handleActorRemoved(ActorRemoved event)
    {
        final ActorAnimator animator;
        animator = visibleAnimators.remove(event.getActorId());
        if (animator != null)
        {
            remoteAnimators.put(event.getActorId(), animator);
        }
    }

    private void handleActorAdded(ActorAdded event)
    {
        final ActorAnimator animator =
                fetchActorAnimator(event.getActor().getId());
        animator.setPosition(event.getActor().getPosition());
    }

    private void handleOtherActorChangedMap(ActorChangedMap event)
    {
        final ActorAnimator animator = fetchActorAnimator(event.getActorId());
        if (currentSegment == null)
            return;
        if (event.getMapId() == currentSegment.getParentMap().getType().getId()
            && event.getInstanceNo() == currentSegment.getParentMap()
                                                      .getInstanceNo())
        {
            animator.setPosition(event.getPosition());
            //TODO: appearing animation
        }
        else
        {
            visibleAnimators.remove(event.getActorId());
            remoteAnimators.put(event.getActorId(), animator);
        }
    }

    private void handleOtherActorWalk(ActorWalk event, Move command)
    {
        final ActorAnimator animator = fetchActorAnimator(event.getActorId());
        animator.setPosition(event.getNewPosition()
                                  .newPosition(command.getDirection()
                                                      .getOpposite()));
        animator.walk(command.getDirection());
        if (currentSegment == null)
        {
            animator.remove();
            visibleAnimators.remove(event.getActorId());
        }
        else
        {
            boolean stillVisible =
                    currentSegment.contains(event.getNewPosition());
            if (!stillVisible)
                for (Segment segment : currentSegment.neighborhood())
                {
                    if (segment.contains(event.getNewPosition()))
                    {
                        stillVisible = true;
                        break;
                    }
                }
            if (!stillVisible)
            {
                visibleAnimators.remove(event.getActorId());
                remoteAnimators.put(event.getActorId(), animator);
            }
        }

    }

    private void removeActorAnimators()
    {
        for (ActorAnimator animator : visibleAnimators.values())
            animator.remove();
        visibleAnimators.clear();
        for (ActorAnimator animator : remoteAnimators.values())
            animator.remove();
        remoteAnimators.clear();
    }

    /**
     * 
     */
    private ActorAnimator getActorAnimator(int actorId)
    {
        ActorAnimator animator = visibleAnimators.get(actorId);
        if (animator != null)
            return animator;
        animator = remoteAnimators.remove(actorId);
        return animator;
    }

    /** {@inheritDoc} */
    @Override
    public Integer getSelectedActor()
    {
        return selectedActorId;
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectedActor(Integer actorId)
    {
        if (selectedActorId != null)
        {
            if (selectedActorId.equals(actorId))
                return;
            ActorAnimator animator = getActorAnimator(selectedActorId);
            if (animator != null)
                animator.setHighlighted(false);
        }

        selectedActorId = actorId;

        if (selectedActorId != null)
        {
            ActorAnimator animator = getActorAnimator(actorId);
            if (animator != null)
                animator.setHighlighted(true);
        }

    }
}
