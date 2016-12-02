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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pl.org.minions.stigma.editor.actions.ActionContainer;
import pl.org.minions.stigma.editor.resourceset.ResourceSetCategory;
import pl.org.minions.stigma.editor.resourceset.ResourceSetCategoryPopup;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocumentPopup;
import pl.org.minions.stigma.editor.resourceset.ResourceSetView;
import pl.org.minions.stigma.editor.resourceset.ResourceSetViewNode;

/**
 * Tree view panel - the one on the left of the editor.
 */
public class TreeView extends JPanel
{
    private class TreeMouseListener extends MouseAdapter
    {
        private void maybeShowPopup(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                tree.setSelectionPath(selPath);
                Object selectedComponent = selPath.getLastPathComponent();

                if (selectedComponent instanceof ResourceSetCategory<?>)
                {
                    JPopupMenu popup = new ResourceSetCategoryPopup();
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());

                }
                else if (selectedComponent instanceof ResourceSetDocument<?>)
                {
                    JPopupMenu popup = new ResourceSetDocumentPopup();
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            int selRow = tree.getRowForLocation(e.getX(), e.getY());

            tree.setSelectionRow(selRow);

            if (selRow != -1)
            {
                if (e.getClickCount() == 2)
                {
                    TreePath selPath =
                            tree.getPathForLocation(e.getX(), e.getY());
                    openDocument(selPath);
                }
                else if (e.isPopupTrigger())
                {
                    maybeShowPopup(e);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            int selRow = tree.getRowForLocation(e.getX(), e.getY());

            if (selRow != -1)
            {
                maybeShowPopup(e);
            }

        }

        private void openDocument(TreePath selPath)
        {
            ActionContainer.OPEN_DOCUMENT.actionPerformed(null);
            //            if (selPath.getLastPathComponent() instanceof ResourceSetDocument<?>)
            //            {
            //                ResourceEditor<?> editor =
            //                        ((ResourceSetDocument<?>) tree.getLastSelectedPathComponent()).getInitedEditor();
            //                MainFrame.getMainFrame().getMainView().addEditor(editor);
            //            }
        }
    }

    private static final long serialVersionUID = 1L;

    private JTree tree;

    /**
     * Constructor.
     */
    public TreeView()
    {
        super(new BorderLayout());
        tree = new JTree();
        tree.getSelectionModel()
            .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroll = new JScrollPane(tree);
        this.add(treeScroll, BorderLayout.CENTER);
    }

    /**
     * Returns selected node.
     * @return selected node
     */
    public Object getSelectedNode()
    {
        return tree.getSelectionPath().getLastPathComponent();
    }

    /**
     * Selects node in this tree.
     * @param resourceSetViewNode
     *            node to be selected
     */
    public void selectNode(ResourceSetViewNode resourceSetViewNode)
    {
        Object root = tree.getModel().getRoot();
        Object[] path = null;
        if (resourceSetViewNode instanceof ResourceSetCategory<?>)
        {
            path = new Object[]
            { root, resourceSetViewNode };
        }
        else if (resourceSetViewNode instanceof ResourceSetDocument<?>)
        {
            path =
                    new Object[]
                    {
                     root,
                     ((ResourceSetDocument<?>) resourceSetViewNode).getCategory(),
                     resourceSetViewNode, };
        }
        TreePath treePath = new TreePath(path);
        tree.setSelectionPath(treePath);
    }

    /**
     * Initializes this tree view with the resource set
     * view.
     * @param resourceSetView
     *            resource set view
     */
    public void init(ResourceSetView resourceSetView)
    {
        tree.setModel(resourceSetView.getResourcesTreeModel());
        tree.setCellRenderer(resourceSetView.getResourcesTreeCellRenderer());

        tree.addMouseListener(new TreeMouseListener());
    }
}
