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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.org.minions.stigma.editor.actions.tabbedpane.CloseAllTabsAction;
import pl.org.minions.stigma.editor.actions.tabbedpane.CloseOtherTabsAction;
import pl.org.minions.stigma.editor.actions.tabbedpane.CloseTabAction;
import pl.org.minions.stigma.editor.resourceset.ResourceEditor;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;

/**
 * Class responsible for the main layout of the application.
 */
public class MainView extends JPanel
{

    private static final long serialVersionUID = 1L;
    private JPanel treePanel;
    private JTabbedPane editorsTabbedPane;

    private JTabbedPane outlinesTabbedPane;

    private JPanel outlinesPanel;

    private Map<ResourceEditor<?>, JTabbedPane> outlinesCache =
            new HashMap<ResourceEditor<?>, JTabbedPane>();

    /**
     * Constructor.
     */
    public MainView()
    {
        super(new BorderLayout());
        init();
    }

    /**
     * Adds a new editor to the main view.
     * @param editor
     *            editor to be added
     */
    public void addEditor(ResourceEditor<?> editor)
    {
        String tabName = editor.getDocument().getName();
        int hasThisEditorAlreadyOpened = -1;
        for (int i = 0; i < editorsTabbedPane.getTabCount(); i++)
        {
            if (editorsTabbedPane.getTitleAt(i).equals(tabName))
            {
                hasThisEditorAlreadyOpened = i;
            }
        }
        if (hasThisEditorAlreadyOpened != -1)
        {
            selectEditor(editor);
        }
        else
        {
            editorsTabbedPane.addTab(tabName,
                                     new CloseTabIcon(editor.getDocument()
                                                            .getIcon()),
                                     editor);

            JTabbedPane tempTabbedPane = new JTabbedPane(SwingConstants.TOP);

            //            tempTabbedPane
            //                          .addMouseListener(new TabbedPanesMouseListener(
            //                                                                         tempTabbedPane));
            for (ResourceEditorOutline outline : editor.getOutlines())
            {
                tempTabbedPane.addTab(outline.getName(), null, outline);
            }

            tempTabbedPane.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    ResourceEditorOutline outline =
                            (ResourceEditorOutline) ((JTabbedPane) e.getSource()).getSelectedComponent();
                    ((ResourceEditor<?>) MainView.this.editorsTabbedPane.getSelectedComponent()).outlineSelected(outline);
                }
            });

            outlinesCache.put(editor, tempTabbedPane);
            outlinesPanel.add(tempTabbedPane);

            selectEditor(editor);
        }

        //        for (ResourceEditorOutline outline : editor.getOutlines())
        //        {
        //            addOutline(outline);
        //        }
    }

    private void init()
    {
        treePanel = new JPanel(new BorderLayout());
        treePanel.setMinimumSize(new Dimension(GUIConstants.MAIN_VIEW_TREE_WIDTH,
                                               GUIConstants.MAIN_VIEW_TREE_HEIGHT));

        JPanel editorsPanel = new JPanel(new BorderLayout());

        outlinesPanel = new JPanel(new BorderLayout());
        outlinesPanel.setMinimumSize(new Dimension(GUIConstants.MAIN_VIEW_OUTLINE_WIDTH,
                                                   GUIConstants.MAIN_VIEW_OUTLINE_HEIGHT));

        JSplitPane editorsOutlinesSplitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                               editorsPanel,
                               outlinesPanel);
        editorsOutlinesSplitPane.setDividerLocation(GUIConstants.MAIN_VIEW_DIVIDER_LOCATION);
        editorsOutlinesSplitPane.setDividerSize(GUIConstants.MAIN_VIEW_DIVIDER_SIZE);
        editorsOutlinesSplitPane.setResizeWeight(1d);

        JSplitPane treeRestSplitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                               treePanel,
                               editorsOutlinesSplitPane);
        treeRestSplitPane.setDividerSize(GUIConstants.MAIN_VIEW_DIVIDER_SIZE);

        UIManager.put("TabbedPane.selected", GUIConstants.VERY_LIGHT_GRAY);
        UIManager.put("TabbedPane.contentAreaColor",
                      GUIConstants.VERY_LIGHT_GRAY);

        editorsTabbedPane = new JTabbedPane(SwingConstants.TOP);
        editorsPanel.add(editorsTabbedPane);
        editorsTabbedPane.setBackground(Color.WHITE);
        editorsTabbedPane.addMouseListener(new TabbedPanesMouseListener(editorsTabbedPane));

        editorsTabbedPane.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                MainView.this.updateAfterSelectionChanged();
            }
        });

        //        outlinesTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        //        outlinesPanel.add(outlinesTabbedPane);
        //        outlinesTabbedPane
        //                          .addMouseListener(new TabbedPanesMouseListener(
        //                                                                         outlinesTabbedPane));

        this.add(treeRestSplitPane);
    }

    /**
     * Selects an editor on the main view.
     * @param editor
     *            editor to be selected
     */
    public void selectEditor(ResourceEditor<?> editor)
    {
        editorsTabbedPane.setSelectedComponent(editor);
        updateAfterSelectionChanged();
    }

    /**
     * Sets tree view.
     * @param comp
     *            component to be set in the tree view
     */
    public void setTreeView(Component comp)
    {
        treePanel.add(comp);
    }

    private void updateAfterSelectionChanged()
    {
        for (JTabbedPane outlinePane : outlinesCache.values())
        {
            outlinePane.setVisible(false);
        }

        outlinesTabbedPane =
                outlinesCache.get(editorsTabbedPane.getSelectedComponent());
        if (outlinesTabbedPane != null)
        {
            outlinesTabbedPane.setVisible(true);
        }
    }

    private class CloseTabIcon implements Icon
    {
        private int xPos;
        private int yPos;
        private Icon fileIcon;
        private Icon closeIcon = GUIConstants.CLOSE_ICON;

        public CloseTabIcon(Icon fileIcon)
        {
            this.fileIcon = fileIcon;
        }

        public Rectangle getBounds()
        {
            return new Rectangle(xPos,
                                 yPos,
                                 closeIcon.getIconWidth(),
                                 closeIcon.getIconHeight());
        }

        public int getIconHeight()
        {
            return Math.max(fileIcon.getIconHeight(), closeIcon.getIconHeight());
        }

        public int getIconWidth()
        {
            return fileIcon.getIconWidth() + closeIcon.getIconWidth() + 2;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            this.xPos = x;
            this.yPos =
                    y
                        + Math.max(0,
                                   (fileIcon.getIconHeight() - closeIcon.getIconHeight()) / 2);
            closeIcon.paintIcon(c, g, xPos, yPos);
            if (fileIcon != null)
            {
                fileIcon.paintIcon(c, g, x + closeIcon.getIconWidth() + 2, y);
            }
        }
    }

    private class TabbedPanesMouseListener extends MouseAdapter
    {

        private JTabbedPane tabbedPane;
        private JPopupMenu popup;
        private CloseTabAction closeTabAction;

        /**
         * @param tabbedPane
         */
        public TabbedPanesMouseListener(JTabbedPane tabbedPane)
        {
            super();
            this.tabbedPane = tabbedPane;
            this.closeTabAction = new CloseTabAction(tabbedPane);
            this.popup = new TabsPopupMenu(tabbedPane, closeTabAction);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            int tabNumber =
                    tabbedPane.getUI().tabForCoordinate(tabbedPane,
                                                        e.getX(),
                                                        e.getY());
            if (tabNumber < 0)
                return;

            if (e.isPopupTrigger())
            {
                popup.show((JComponent) e.getSource(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            int tabNumber =
                    tabbedPane.getUI().tabForCoordinate(tabbedPane,
                                                        e.getX(),
                                                        e.getY());
            if (tabNumber < 0)
                return;

            if (tabbedPane.getIconAt(tabNumber) != null)
            {
                Rectangle rect =
                        ((CloseTabIcon) tabbedPane.getIconAt(tabNumber)).getBounds();

                if (rect.contains(e.getX(), e.getY()))
                {
                    //the tab is being closed
                    closeTabAction.actionPerformed(null);
                }
            }

            if (e.isPopupTrigger())
            {
                popup.show((JComponent) e.getSource(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }
    }

    private class TabsPopupMenu extends JPopupMenu
    {
        private static final long serialVersionUID = 1L;

        public TabsPopupMenu(JTabbedPane tabbedPane,
                             CloseTabAction closeTabAction)
        {
            add(closeTabAction);
            addSeparator();
            add(new CloseOtherTabsAction(tabbedPane));
            add(new CloseAllTabsAction(tabbedPane));
        }
    }
}
