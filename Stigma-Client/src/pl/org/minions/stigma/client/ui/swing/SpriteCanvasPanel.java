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
package pl.org.minions.stigma.client.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.PlayerController;
import pl.org.minions.stigma.client.PlayerController.PlayerRequest;
import pl.org.minions.stigma.client.requests.DropRequest;
import pl.org.minions.stigma.client.requests.MoveToRequest;
import pl.org.minions.stigma.client.ui.AreaView;
import pl.org.minions.stigma.client.ui.Clearable;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.sprite.ActorRenderer;
import pl.org.minions.stigma.client.ui.sprite.ForegroundLayerGroup;
import pl.org.minions.stigma.client.ui.sprite.ItemRenderer;
import pl.org.minions.stigma.client.ui.sprite.TerrainRenderer;
import pl.org.minions.stigma.client.ui.swing.handlers.ItemTransferHandler;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.ui.TickCounter;
import pl.org.minions.utils.ui.sprite.ImageSprite;
import pl.org.minions.utils.ui.sprite.Sprite;
import pl.org.minions.utils.ui.sprite.SpriteCanvas;
import pl.org.minions.utils.ui.sprite.SpriteCanvas.Viewport;

/**
 * Swing panel that encapsulates an instance of
 * {@link SpriteCanvas}, along with classes that handle
 * stigma game state rendering using sprites.
 */
public class SpriteCanvasPanel extends JPanel implements Clearable, AreaView
{
    private class InputListener implements MouseListener, MouseMotionListener
    {
        /** {@inheritDoc} */
        @Override
        public void mouseClicked(MouseEvent e)
        {

        }

        /** {@inheritDoc} */
        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        /** {@inheritDoc} */
        @Override
        public void mouseExited(MouseEvent e)
        {
            mouseLocation.setLocation(-1, -1);
        }

        /** {@inheritDoc} */
        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        /** {@inheritDoc} */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (backgroundRenderer.isTerrainSetApplied())
            {
                Position position =
                        backgroundRenderer.getMapTileAt(canvas.getViewport()
                                                              .viewToWorld(e.getPoint()));
                if (position != null && e.getButton() == MouseEvent.BUTTON1)
                {
                    Client.globalInstance()
                          .getPlayerController()
                          .playerRequest(new MoveToRequest(position));
                    e.consume();
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void mouseDragged(MouseEvent e)
        {
        }

        /** {@inheritDoc} */
        @Override
        public void mouseMoved(MouseEvent e)
        {
            mouseLocation.setLocation(e.getPoint());
        }
    }

    /**
     * The width and height of the canvas {@link Viewport}
     * in map tiles. Also defines the size of a
     * {@link SpriteCanvasPanel} when multiplied by tile
     * dimensions.
     * @see VisualizationGlobals
     */
    public static final int VIEWPORT_SIZE_IN_TILES = 18;

    private static final long serialVersionUID = 1L;

    private static final int FPS_OFFSET = 32;

    private static final float TILE_HIGHLIGHT_OPACITY = 0.1f;

    private static final String PATH_TO_TARGET = "img/client/cross.png";

    private static final BufferedImage TARGET_IMAGE =
            Resourcer.loadImage(PATH_TO_TARGET);

    private static final Point TARGET_OFFSET =
            new Point(TARGET_IMAGE.getWidth() / 2, TARGET_IMAGE.getHeight() / 2);

    private SpriteCanvas canvas = new SpriteCanvas();

    private ForegroundLayerGroup foreground;

    private TerrainRenderer backgroundRenderer;
    private ActorRenderer actorRenderer;

    private ItemRenderer itemRenderer;

    private MapInstance mapInstance;

    private boolean clean = true;

    private long lastTimestamp = System.currentTimeMillis();

    private TickCounter counter = new TickCounter();

    private Point mouseLocation = new Point();

    private Sprite tileHighlightSprite;

    private Sprite pathTargetSprite;

    /**
     * Creates a new instance of SpriteCanvasPanel.
     * <p>
     * The created panel has preferred, minimum, and maximum
     * size set to {@link #VIEWPORT_SIZE_IN_TILES}
     * multiplied by
     * {@link VisualizationGlobals#MAP_TILE_WIDTH} and
     * {@link VisualizationGlobals#MAP_TILE_HEIGHT} for
     * width and height, respectively.
     */
    public SpriteCanvasPanel()
    {
        super(null, true);
        setOpaque(true);
        setDoubleBuffered(true);

        setTransferHandler(new ItemTransferHandler(new ItemTransferHandler.DropAction()
                                                   {
                                                       @Override
                                                       public void itemDrop(Item item)
                                                       {
                                                           Client.globalInstance()
                                                                 .getPlayerController()
                                                                 .playerRequest(new DropRequest(item.getId()));
                                                       }
                                                   },
                                                   new ItemTransferHandler.ImportCheck()
                                                   {
                                                       @Override
                                                       public boolean canImport(Item item)
                                                       {
                                                           return !item.isOnGround();
                                                       }
                                                   }));

        DropTarget dropTarget = getDropTarget();
        dropTarget.setDefaultActions(DnDConstants.ACTION_MOVE);
        dropTarget.setActive(true);

        final Dimension viewportSize =
                new Dimension(VisualizationGlobals.MAP_TILE_WIDTH
                                  * VIEWPORT_SIZE_IN_TILES,
                              VisualizationGlobals.MAP_TILE_HEIGHT
                                  * VIEWPORT_SIZE_IN_TILES);
        setBackground(Color.red);

        canvas.setGraphicsConfiguration(this.getGraphicsConfiguration());
        canvas.getViewport().getBounds().setSize(viewportSize);
        canvas.setBackgroundColor(Color.black);
        backgroundRenderer = new TerrainRenderer(canvas);
        foreground = new ForegroundLayerGroup(canvas);
        actorRenderer = new ActorRenderer(foreground);
        itemRenderer = new ItemRenderer(foreground);

        tileHighlightSprite = new ImageSprite(TARGET_IMAGE, TARGET_OFFSET);

        tileHighlightSprite.setAlpha(TILE_HIGHLIGHT_OPACITY);
        tileHighlightSprite.setSize(new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                                                  VisualizationGlobals.MAP_TILE_HEIGHT));
        tileHighlightSprite.setColor(Color.yellow);
        tileHighlightSprite.setVisible(false);
        foreground.getGroundEffectLayer().addSprite(tileHighlightSprite);

        pathTargetSprite = new ImageSprite(TARGET_IMAGE, TARGET_OFFSET);
        pathTargetSprite.setColor(Color.yellow);
        pathTargetSprite.setVisible(false);
        foreground.getGroundEffectLayer().addSprite(pathTargetSprite);

        setSize(viewportSize);
        setPreferredSize(viewportSize);
        setMinimumSize(viewportSize);
        setMaximumSize(viewportSize);

        final InputListener mouseListener = new InputListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        if (clean)
            return;
        mapInstance = null;
        backgroundRenderer.clear();
        actorRenderer.clear();
        itemRenderer.clear();
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g)
    {
        final long now = System.currentTimeMillis();
        final long timeElapsed = now - lastTimestamp;
        lastTimestamp = now;

        final Actor playerActor = Client.globalInstance().getPlayerActor();

        if (canvas.getGraphicsConfiguration() == null)
        {
            GraphicsConfiguration conf = getGraphicsConfiguration();
            if (conf == null)
            {
                super.paintComponent(g);
                return;
            }
            else
                canvas.setGraphicsConfiguration(conf);
        }

        if (playerActor == null)
        {
            clear();
            return;
        }

        clean = false;

        updateBackgroundRenderer(playerActor);

        animateAll(timeElapsed);

        centerViewport(playerActor);

        tileHighlightSprite.setVisible(false);
        final Rectangle viewportBounds = canvas.getViewport().getBounds();
        if (viewportBounds.width > mouseLocation.x
            && viewportBounds.height > mouseLocation.y && mouseLocation.x >= 0
            && mouseLocation.y >= 0)
        {
            final Position highlightedMapTile =
                    backgroundRenderer.getMapTileAt(canvas.getViewport()
                                                          .viewToWorld(mouseLocation));
            if (highlightedMapTile != null
                && mapInstance.isPassable(highlightedMapTile))
            {
                tileHighlightSprite.setVisible(true);
                tileHighlightSprite.setPosition(backgroundRenderer.getMapTileCenter(highlightedMapTile));
            }
        }

        final PlayerController playerController =
                Client.globalInstance().getPlayerController();

        final PlayerRequest playerRequest =
                playerController != null ? playerController.getCurrentRequest()
                                        : null;

        final Position targetLocation =
                playerRequest != null ? playerRequest.getTargetLocation()
                                     : null;
        if (targetLocation != null)
        {
            final Point mapTileCenter =
                    backgroundRenderer.getMapTileCenter(targetLocation);
            if (mapTileCenter != null)
            {
                pathTargetSprite.setPosition(mapTileCenter);
                pathTargetSprite.setVisible(true);
            }
            else
                pathTargetSprite.setVisible(false);
        }
        else
        {
            pathTargetSprite.setVisible(false);
        }

        render((Graphics2D) g.create());

        counter.tick(timeElapsed);
        g.setColor(Color.yellow);
        g.drawString(String.format("FPS: %2.2f", counter.getTicksPerSecond()),
                     FPS_OFFSET,
                     FPS_OFFSET);
    }

    private void animateAll(final long timeElapsed)
    {
        actorRenderer.animateAll(timeElapsed);
        itemRenderer.animateAll(timeElapsed);
    }

    private void updateBackgroundRenderer(final Actor playerActor)
    {
        if (mapInstance == null
            || playerActor.getMapInstanceNo() != mapInstance.getInstanceNo()
            || !mapInstance.needsType()
            && playerActor.getMapId() != mapInstance.getType().getId())
        {
            backgroundRenderer.applyMapType(null);

            if (playerActor.getMapId() == -1)
            {
                mapInstance = null;
            }
            else
            {
                mapInstance =
                        Client.globalInstance()
                              .getWorld()
                              .getMap(playerActor.getMapId(),
                                      playerActor.getMapInstanceNo());
            }
        }

        if (mapInstance != null)
        {
            if (mapInstance.getType() != backgroundRenderer.getMapType())
                backgroundRenderer.applyMapType(mapInstance.getType());

            if (!mapInstance.needsTerrainSet()
                && !backgroundRenderer.isTerrainSetApplied())
                backgroundRenderer.applyTerrainSet(mapInstance.getType()
                                                              .getTerrainSet());

            if (backgroundRenderer.isTerrainSetApplied()
                && backgroundRenderer.isTerrainCacheRebuildNeeded())
                backgroundRenderer.rebuildTerrainImagesCache();
        }
        else
        {
            if (backgroundRenderer.isMapTypeApplied())
                backgroundRenderer.applyMapType(null);
        }
    }

    private void centerViewport(final Actor playerActor)
    {
        final Point actorLocation =
                actorRenderer.getActorLocation(playerActor.getId());
        if (actorLocation != null)
            canvas.getViewport().centerAt(actorLocation);
        else
            backgroundRenderer.centerViewportAtPosition(playerActor.getPosition());
    }

    private void render(final Graphics2D g2D)
    {
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                             RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        canvas.render(g2D);
    }

    /** {@inheritDoc} */
    @Override
    public ActorSelectionView getActorSelectionView()
    {
        return actorRenderer;
    }
}
