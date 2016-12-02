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
package pl.org.minions.stigma.editor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import pl.org.minions.stigma.editor.actions.ActionContainer;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.stigma.editor.resourceset.ResourceSetView;
import pl.org.minions.stigma.editor.resourceset.ResourceSetViewNode;
import pl.org.minions.stigma.editor.resourceset.archetype.ArchetypeCategory;
import pl.org.minions.stigma.editor.resourceset.map.MapTypeCategory;
import pl.org.minions.stigma.editor.resourceset.terrainset.TerrainSetCategory;
import pl.org.minions.utils.Version;

/**
 * Main frame of the editor - singleton class.
 */
public final class MainFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private static MainFrame mainFrame;

    private MainMenu mainMenu;

    private MainView mainView;

    private TreeView treeView;

    private MainToolbar mainToolbar;

    private ResourceSetView rsView;

    private MainFrame()
    {
        super("Stigma Editor " + Version.FULL_VERSION);
        init();
    }

    /**
     * Returns the instance of the main frame.
     * @return the instance of the main frame.
     */
    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    /**
     * Initializes the main editor frame.
     */
    public static void initialize()
    {
        mainFrame = new MainFrame();
        mainFrame.setIconImage(GUIConstants.STIGMA_ICON_IMAGE);
        mainFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                ActionContainer.CLOSE_EDITOR.actionPerformed(null);
            }
        });
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Returns the main view.
     * @return the main view
     */
    public MainView getMainView()
    {
        return mainView;
    }

    /**
     * Returns the tree view.
     * @return the tree view
     */
    public TreeView getTreeView()
    {
        return treeView;
    }

    private void init()
    {
        this.setSize(new Dimension(GUIConstants.MAIN_FRAME_WIDTH,
                                   GUIConstants.MAIN_FRAME_HEIGHT));

        ResourceSetModel.getInstance().loadResourceSet("./res/tests");

        rsView = new ResourceSetView();
        rsView.registerResourceSetCategory(new MapTypeCategory());
        rsView.registerResourceSetCategory(new TerrainSetCategory());
        rsView.registerResourceSetCategory(new ArchetypeCategory());

        //TODO:
        rsView.init(ResourceSetModel.getInstance().getResourceSet());

        mainMenu = new MainMenu();
        this.setJMenuBar(mainMenu);

        mainToolbar = new MainToolbar();
        this.add(mainToolbar, BorderLayout.PAGE_START);

        treeView = new TreeView();
        treeView.init(rsView);

        mainView = new MainView();
        mainView.setTreeView(treeView);

        this.add(mainView, BorderLayout.CENTER);
    }

    /**
     * Selects node in the resource set view tree.
     * @param resourceSetViewNode
     *            resource set view node
     */
    public void selectResourceSetViewNode(ResourceSetViewNode resourceSetViewNode)
    {
        rsView.selectResourceSetViewNode(resourceSetViewNode);
        treeView.selectNode(resourceSetViewNode);
    }
}
