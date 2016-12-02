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
package pl.org.minions.stigma.editor.resourceset;

import java.awt.Component;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Class representing the view of the resource set.
 */
public class ResourceSetView
{

    private class ResourcesTreeCellRenderer extends DefaultTreeCellRenderer
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree,
                                               value,
                                               sel,
                                               expanded,
                                               leaf,
                                               row,
                                               hasFocus);

            if (value instanceof ResourceSetViewNode)
            {
                ResourceSetViewNode resourceSetViewNode =
                        (ResourceSetViewNode) value;
                setText(resourceSetViewNode.getName());
                setIcon(resourceSetViewNode.getIcon());
                setToolTipText(resourceSetViewNode.getDescription());
            }

            return this;
        }
    }

    private class ResourcesTreeModel implements TreeModel
    {

        private ResourceSetRoot root;

        private Set<TreeModelListener> treeModelListeners =
                new HashSet<TreeModelListener>();

        public ResourcesTreeModel(ResourceSetRoot root)
        {
            this.root = root;
        }

        public void addTreeModelListener(TreeModelListener l)
        {
            treeModelListeners.add(l);
        }

        public Object getChild(Object parent, int index)
        {
            if (parent instanceof ResourceSetRoot)
            {
                return ((ResourceSetRoot) parent).getCategories().get(index);
            }
            if (parent instanceof ResourceSetCategory<?>)
            {
                return ((ResourceSetCategory<?>) parent).getElements()
                                                        .get(index);
            }
            return null;
        }

        public int getChildCount(Object parent)
        {
            if (parent instanceof ResourceSetRoot)
            {
                return ((ResourceSetRoot) parent).getCategories().size();
            }
            if (parent instanceof ResourceSetCategory<?>)
            {
                return ((ResourceSetCategory<?>) parent).getElements().size();
            }
            return 0;
        }

        public int getIndexOfChild(Object parent, Object child)
        {
            if (parent instanceof ResourceSetRoot)
            {
                return ((ResourceSetRoot) parent).getCategories()
                                                 .indexOf(child);
            }
            if (parent instanceof ResourceSetCategory<?>)
            {
                return ((ResourceSetCategory<?>) parent).getElements()
                                                        .indexOf(child);
            }
            return 0;
        }

        public Object getRoot()
        {
            return root;
        }

        public boolean isLeaf(Object node)
        {
            if (node instanceof ResourceSetDocument<?>)
            {
                return true;
            }
            return false;
        }

        public void removeTreeModelListener(TreeModelListener l)
        {
            treeModelListeners.remove(l);
        }

        public void valueForPathChanged(TreePath path, Object newValue)
        {
            System.out.println("ValueForPathChanged:" + path + "  --- "
                + newValue);
        }

        public void firePathChanged(ResourceSetViewNode resourceSetViewNode)
        {
            Object[] path = null;
            if (resourceSetViewNode instanceof ResourceSetRoot)
            {
                path = new Object[]
                { root };
            }
            else if (resourceSetViewNode instanceof ResourceSetCategory<?>)
            {
                path = new Object[]
                { root };
            }
            else if (resourceSetViewNode instanceof ResourceSetDocument<?>)
            {
                path =
                        new Object[]
                        {
                         root,
                         ((ResourceSetDocument<?>) resourceSetViewNode).getCategory(), };
            }

            TreeModelEvent e = new TreeModelEvent(this, path);
            for (TreeModelListener l : treeModelListeners)
            {
                l.treeStructureChanged(e);
            }
        }
    }

    private ResourceSetRoot resourceSetRoot;
    private List<ResourceSetCategory<?>> categories;

    private ResourcesTreeModel resourcesTreeModel;

    private ResourcesTreeCellRenderer resourcesTreeCellRenderer;

    /**
     * Constructor.
     */
    public ResourceSetView()
    {
        categories = new LinkedList<ResourceSetCategory<?>>();
        resourceSetRoot = new ResourceSetRootImpl(categories);
        resourcesTreeModel = new ResourcesTreeModel(resourceSetRoot);
        resourcesTreeCellRenderer = new ResourcesTreeCellRenderer();
    }

    /**
     * Returns resourcesTreeCellRenderer.
     * @return resourcesTreeCellRenderer
     */
    public ResourcesTreeCellRenderer getResourcesTreeCellRenderer()
    {
        return resourcesTreeCellRenderer;
    }

    /**
     * Returns resourcesTreeModel.
     * @return resourcesTreeModel
     */
    public ResourcesTreeModel getResourcesTreeModel()
    {
        return resourcesTreeModel;
    }

    /**
     * Initializes this view with a resource set.
     * @param resourceSet
     *            resource set
     */
    public void init(ResourceSet resourceSet)
    {
        resourceSetRoot.init(resourceSet);
    }

    /**
     * Registers a new resource set category in this
     * resource set view.
     * @param resourceSetCategory
     *            resource set category
     */
    public void registerResourceSetCategory(ResourceSetCategory<?> resourceSetCategory)
    {
        categories.add(resourceSetCategory);
    }

    /**
     * Selects node in the tree model.
     * @param resourceSetViewNode
     *            resource set view node
     */
    public void selectResourceSetViewNode(ResourceSetViewNode resourceSetViewNode)
    {
        resourcesTreeModel.firePathChanged(resourceSetViewNode);
    }
}
