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

package pl.org.minions.stigma.client.images.precedence.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.org.minions.stigma.client.images.TerrainTypeImageId;
import pl.org.minions.stigma.client.images.precedence.TerrainTypesPrecedenceManager;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.game.map.TileType;
import pl.org.minions.stigma.globals.Position;

//CHECKSTYLE:OFF
//TODO: should this test class be removed?
public class Test extends JPanel
{

    private static final long serialVersionUID = 1L;

    private static int GRID_SIZE = 32;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Test");
        frame.setSize(new Dimension(700, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Test panel = new Test();
        frame.getContentPane().add(new JScrollPane(panel));
        frame.setVisible(true);
    }

    private Point selBegin = new Point();
    private Point selEnd = new Point();
    private Color testColor = Color.BLACK;
    //	private Thread t = null;
    private Images images;

    private MapType mapEntity;
    private TerrainTypesPrecedenceManager terrainTypesPrecedenceManager;

    private BufferedImage image;

    public Test()
    {
        super();

        //Create terrain set
        String filename = "./misc/img_src/terrain_template.png";

        TerrainSet terrainSet = new TerrainSet((short) 0);

        Color[] colors =
        { Color.blue, Color.green, Color.white };
        for (int i = 0; i < colors.length; ++i)
        {
            TerrainType terrainType =
                    new TerrainType(i, true, colors[i], filename, i);
            terrainType.addTileType(new TileType(0));
            terrainSet.addTerrainType(terrainType);
        }

        //Create map
        List<Short> tilesList = new ArrayList<Short>(20 * 20 * 2);
        Random random = new Random();
        for (int i = 0; i < 20 * 20; ++i)
        {
            tilesList.add((short) Math.abs(random.nextInt() % colors.length));
            tilesList.add((short) 0);
        }

        EntryZone entryZone = new EntryZone((byte) 0);
        entryZone.setPositionsList(Arrays.asList(new Position((short) 0,
                                                              (short) 0)));
        ExitZone exitZone = new ExitZone((byte) 0, (short) 0, (byte) 0);
        exitZone.setPositionsList(Arrays.asList(new Position((short) 1,
                                                             (short) 1)));

        mapEntity =
                new MapType((short) 0,
                            (short) 0,
                            (short) 20,
                            (short) 20,
                            (byte) 20,
                            (byte) 20,
                            tilesList,
                            Arrays.asList(entryZone),
                            Arrays.asList(exitZone),
                            "name",
                            "description",
                            (short) 0,
                            (byte) 0);
        mapEntity.assignTerrainSet(terrainSet);

        terrainTypesPrecedenceManager =
                new TerrainTypesPrecedenceManager(terrainSet);

        initGUI();
        images = new Images(this, terrainSet.getTerrainTypes());
        image =
                new BufferedImage(mapEntity.getSizeY() * GRID_SIZE,
                                  mapEntity.getSizeX() * GRID_SIZE,
                                  BufferedImage.TYPE_INT_RGB);
        image.getGraphics().setColor(Color.white);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    private void initGUI()
    {

        this.setBackground(Color.YELLOW);
        this.setPreferredSize(new Dimension(mapEntity.getSizeY() * GRID_SIZE,
                                            mapEntity.getSizeX() * GRID_SIZE));
        this.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                //System.out.println("D_PointY:"+e.toString());
                Test.this.mouseDrag(e.getX(), e.getY());
                setTerrain(e.getModifiers() == 4 ? 0 : 1);
                Test.this.paintMe();
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                //System.out.println("PointY:"+e.toString());

                //				if (t == null) {t = new Thread(new Runnable() {
                //					public void run() {
                //						while(true) {
                //							Test.this.rep();
                //							try {
                //								Thread.sleep(100);
                //							} catch (InterruptedException e) {
                //								e.printStackTrace();
                //							}
                //						}
                //					}
                //				}); t.start();}

                Test.this.mouseAt(e.getX(), e.getY());
                Test.this.paintMe();
            }

        });
    }

    private void mouseAt(int x, int y)
    {
        selBegin.x = x / GRID_SIZE;
        selBegin.y = y / GRID_SIZE;
        selEnd.x = x / GRID_SIZE;
        selEnd.y = y / GRID_SIZE;
    }

    private void mouseDrag(int x, int y)
    {
        selBegin.x = x / GRID_SIZE;
        selBegin.y = y / GRID_SIZE;
        selEnd.x = x / GRID_SIZE;
        selEnd.y = y / GRID_SIZE;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        g.drawImage(image, 0, 0, this);
    }

    //	
    //	private void rep() {
    //		Graphics g = this.getGraphics();		
    //		
    //		paintMe();
    //		g.drawImage(image, 0, 0, this);
    //	}

    private void paintMe()
    {
        Graphics2D g = image.createGraphics();
        g.setColor(testColor);
        //	g.fillRect(0, 0, image.getWidth(), image.getHeight());

        for (short x = 0; x < mapEntity.getSizeX(); x++)
        {
            for (short y = 0; y < mapEntity.getSizeY(); y++)
            {

                List<TerrainTypeImageId> terrainTypeImageIdList =
                        terrainTypesPrecedenceManager.getTerrainTypeImageIdsQueue(mapEntity,
                                                                                  new Position(x,
                                                                                               y));

                for (TerrainTypeImageId terrainTypeImageId : terrainTypeImageIdList)
                {
                    g.drawImage(images.getImage(terrainTypeImageId), GRID_SIZE
                        * x, GRID_SIZE * y, this);
                }
            }
        }

        int selX = Math.min(selBegin.x, selEnd.x);
        int selY = Math.min(selBegin.y, selEnd.y);

        int selDX = Math.abs(selBegin.x - selEnd.x) + 1;
        int selDY = Math.abs(selBegin.y - selEnd.y) + 1;
        g.setColor(Color.black);
        g.drawRect(selX * GRID_SIZE, selY * GRID_SIZE, selDX * GRID_SIZE, selDY
            * GRID_SIZE);

        this.repaint();
    }

    private void setTerrain(int type)
    {
        //        mapEntity.setTerrainType(new Point(selEnd.x, selEnd.y),
        //                                 new TerrainType(type));
    }
}
