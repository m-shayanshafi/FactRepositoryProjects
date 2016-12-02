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
package pl.org.minions.stigma.editor.resourceset.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.images.ImageProxy;
import pl.org.minions.stigma.client.images.TerrainTypeImageId;
import pl.org.minions.stigma.client.images.ImageProxy.LoadingState;
import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel used to choose a terrain/tile type.
 */
public class TerrainPalette extends ResourceEditorOutline implements
                                                         ActionListener
{
    @Translated
    private static String PANEL_LABEL = "Terrain";

    private class TerrainTypeButton extends JButton
    {
        private static final long serialVersionUID = 1L;

        private TerrainType terrainType;
        private TileType selectedTile;

        private ImageProxy imageProxy;

        public TerrainTypeButton(TerrainType terrainType)
        {
            super();
            this.terrainType = terrainType;
            this.selectedTile = terrainType.getTile(0);

            imageProxy =
                    ImageDB.globalInstance()
                           .getTerrainImage(TerrainTypeImageId.createTileImageId(selectedTile));

            this.setMinimumSize(new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                                              VisualizationGlobals.MAP_TILE_HEIGHT));
            this.setPreferredSize(new Dimension(VisualizationGlobals.MAP_TILE_WIDTH,
                                                VisualizationGlobals.MAP_TILE_HEIGHT));
        }

        /**
         * Returns selectedTile.
         * @return selectedTile
         */
        public TileType getSelectedTile()
        {
            return selectedTile;
        }

        /**
         * Returns terrainType.
         * @return terrainType
         */
        public TerrainType getTerrainType()
        {
            return terrainType;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            if (imageProxy.getState().equals(LoadingState.LOADED))
            {
                g.drawImage(imageProxy.getImage(), 0, 0, this);
            }
            //CHECKSTYLE:OFF
            if (terrainType.isPassable())
            {
                g.setColor(Color.green);
                g.fillOval(1, 1, 7, 7);

                g.setColor(Color.white);
                g.drawLine(3, 4, 4, 6);
                g.drawLine(4, 6, 8, 3);
            }
            else
            {
                g.setColor(Color.red);
                g.fillOval(1, 1, 7, 7);

                g.setColor(Color.white);
                g.drawLine(3, 3, 6, 6);
                g.drawLine(3, 6, 6, 3);
            }

            if (this.isSelected())
            {
                g.setColor(new Color(0xDD, 0xFF, 0xDD, 0x55));
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setColor(Color.LIGHT_GRAY);
                g.drawRoundRect(1,
                                1,
                                this.getWidth() - 3,
                                this.getHeight() - 3,
                                5,
                                5);
            }
            //CHECKSTYLE:ON
        }

    }

    private static final long serialVersionUID = 1L;

    private TerrainSet terrainSet;

    private TileType selectedTile;

    private Set<TerrainTypeButton> terrainTypeButtons =
            new HashSet<TerrainTypeButton>();

    /**
     * Constructor.
     */
    public TerrainPalette()
    {
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //System.out.println("" + e);
        if (e.getSource() instanceof TerrainTypeButton)
        {
            // deselect all buttons
            for (TerrainTypeButton terrainTypeButton : terrainTypeButtons)
            {
                terrainTypeButton.setSelected(false);
            }

            // service button click
            TerrainTypeButton terrainTypeButton =
                    (TerrainTypeButton) e.getSource();

            this.selectedTile = terrainTypeButton.getSelectedTile();

            terrainTypeButton.setSelected(true);
        }

    }

    private void addTerrainType(TerrainType terrainType)
    {
        TerrainTypeButton terrainTypeButton =
                new TerrainTypeButton(terrainType);
        terrainTypeButton.addActionListener(this);
        terrainTypeButtons.add(terrainTypeButton);
        this.add(terrainTypeButton);
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return PANEL_LABEL;
    }

    /**
     * Returns selectedTile.
     * @return selectedTile
     */
    public TileType getSelectedTile()
    {
        return selectedTile;
    }

    /**
     * Returns terrainSet.
     * @return terrainSet
     */
    public TerrainSet getTerrainSet()
    {
        return terrainSet;
    }

    /**
     * Initializes terrain palette with a given terrain
     * type.
     */
    public void init(TerrainSet terrainSet)
    {
        this.terrainSet = terrainSet;

        for (TerrainType terrainType : terrainSet.getTerrainTypes())
        {
            addTerrainType(terrainType);
        }
    }
}
