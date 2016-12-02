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
package pl.org.minions.utils.ui.sprite;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import pl.org.minions.utils.ui.sprite.SpriteCanvas.CanvasLayerGroup;

/**
 * Grid layer that stores images in a rectangular grid.
 * <p>
 * Every grid cell contains a single image.
 * <p>
 * Only the images in grid cells that collide with viewport
 * bounds are rendered.
 * <p>
 * Images are rendered top to bottom, left to right.
 */
public class ImageGridLayer extends SpriteCanvas.CanvasLayer
{
    private Dimension gridSize = new Dimension();
    private Dimension gridStep = new Dimension();
    private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

    /**
     * Creates a new ImageGridLayer on top of the selected
     * layer group.
     * @param group
     *            layer group to add this layer to
     */
    public ImageGridLayer(CanvasLayerGroup group)
    {
        group.getCanvas().super(group);
    }

    /**
     * Creates a new ImageGridLayer on top of the selected
     * canvas' default layer group.
     * @param canvas
     *            canvas for this layer to be a part of
     */
    public ImageGridLayer(SpriteCanvas canvas)
    {
        canvas.super();
    }

    /**
     * Centers the viewport at the given background tile.
     * <p>
     * No validation is made on the passed coordinates.
     * @param column
     *            horizontal index of tile
     * @param row
     *            vertical index of the tile
     */
    public void centerViewportAtGridLocation(int column, int row)
    {

        getCanvas().getViewport().centerAt(column * gridStep.width
                                               + gridStep.width / 2,
                                           row * gridStep.height
                                               + gridStep.height / 2);
    }

    /**
     * Removes all images from this grid layer.
     */
    public final void clear()
    {
        Collections.fill(images, null);
    }

    /**
     * Returns the size of the gird.
     * @return the number of columns and rows as width and
     *         height, respectively
     */
    public final Dimension getGridSize()
    {
        return gridSize;
    }

    /**
     * Returns grid step.
     * @return size of every single grid cell in pixels
     */
    public final Dimension getGridStep()
    {
        return gridStep;
    }

    /**
     * Returns the image at specified position within grid.
     * @param column
     *            grid horizontal coordinate
     * @param row
     *            grid vertical coordinate
     * @return image at specified position within grid
     */
    public BufferedImage getImageAt(int column, int row)
    {
        return images.get(column + row * gridSize.width);
    }

    /** {@inheritDoc} */
    @Override
    public void render(Graphics2D graphics)
    {
        if (gridSize.width == 0 || gridSize.height == 0 || gridStep.width == 0
            || gridStep.height == 0)
            return;

        final Rectangle viewportBounds = getCanvas().getViewport().getBounds();

        final int firstRow = Math.max(viewportBounds.y / gridStep.height, 0);
        final int firstColumn = Math.max(viewportBounds.x / gridStep.width, 0);

        final int lastRow =
                Math.min((viewportBounds.y + viewportBounds.height - 1)
                    / gridStep.height + 1, gridSize.height);
        final int lastColumn =
                Math.min((viewportBounds.x + viewportBounds.width - 1)
                    / gridStep.width + 1, gridSize.width);

        for (int row = firstRow; row < lastRow; ++row)
        {
            final int rowStart = row * gridSize.width;
            final int rowStarPixel = row * gridStep.height;

            for (int column = firstColumn; column < lastColumn; ++column)
            {
                final int columnStartPixel = column * gridStep.width;

                graphics.drawImage(images.get(rowStart + column),
                                   null,
                                   columnStartPixel,
                                   rowStarPixel);
            }
        }

    }

    /**
     * Sets new size of the grid.
     * @param columns
     *            the number of columns
     * @param rows
     *            the number of rows
     */
    public final void setGridSize(int columns, int rows)
    {
        this.gridSize = new Dimension(columns, rows);
        int numberOfCells = columns * rows;

        if (numberOfCells < images.size())
            images.subList(numberOfCells, images.size() - 1).clear();
        else if (numberOfCells > images.size())
            images.addAll(Collections.nCopies(numberOfCells - images.size(),
                                              (BufferedImage) null));
    }

    /**
     * Sets new grid step.
     * @param width
     *            width of every single grid cell in pixels
     * @param height
     *            height of every single grid cell in pixels
     */
    public final void setGridStep(int width, int height)
    {
        this.gridStep = new Dimension(width, height);
    }

    /**
     * Sets the image image at specified position within
     * grid.
     * @param image
     *            image to set
     * @param column
     *            grid horizontal coordinate
     * @param row
     *            grid vertical coordinate
     */
    public void setImageAt(BufferedImage image, int column, int row)
    {
        images.set(column + row * gridSize.width, image);
    }

}
