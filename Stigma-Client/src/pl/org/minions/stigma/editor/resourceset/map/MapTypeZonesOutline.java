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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.org.minions.stigma.editor.gui.FieldChangeListener;
import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.gui.GUIFactory;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.stigma.game.map.EntryZone;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.Zone;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.i18n.Translated;

/**
 * Zones outline of the map type editor.
 */
public class MapTypeZonesOutline extends ResourceEditorOutline implements
                                                              ListSelectionListener
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ZONES_LABEL = "Zones";

    private ResourceSetDocument<MapType> mapTypeDocument;

    private JPanel listPanel;
    private JPanel propertiesPanel;
    private JPanel buttonsPanel;

    private DefaultListModel zonesListModel;
    private JList zonesList;

    private JTextField nameField;
    private JTextField descriptionField;
    private JComboBox destMapSelectField;
    private JComboBox entryZoneSelectField;

    private JButton createEntryZoneButton;
    private JButton createExitZoneButton;
    private JButton deleteZoneButton;

    private List<ZoneSelectionListener> zoneSelectionListeners;

    private boolean updatingValues;

    /**
     * Constructor.
     */
    public MapTypeZonesOutline()
    {
        super();
        this.setLayout(new BorderLayout());

        zoneSelectionListeners = new ArrayList<ZoneSelectionListener>();

        listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                                                             ZONES_LABEL));

        zonesListModel = new DefaultListModel();
        zonesList = new JList(zonesListModel);
        zonesList.setCellRenderer(new ZoneListCellRenderer());
        zonesList.addListSelectionListener(this);

        listPanel.add(new JScrollPane(zonesList));
        this.add(listPanel, BorderLayout.CENTER);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createEntryZoneButton = new JButton(new CreateEntryZoneAction(this));
        createExitZoneButton = new JButton(new CreateExitZoneAction(this));
        deleteZoneButton = new JButton(new DeleteZoneAction(this));
        buttonsPanel.add(createEntryZoneButton);
        buttonsPanel.add(createExitZoneButton);
        buttonsPanel.add(deleteZoneButton);

        createEntryZoneButton.setMargin(new Insets(1, 1, 1, 1));
        createEntryZoneButton.setContentAreaFilled(false);
        createEntryZoneButton.setBorder(null);
        createEntryZoneButton.setIcon(GUIConstants.ENTRYZONE_ADD_ICON);
        createEntryZoneButton.setPressedIcon(GUIConstants.ENTRYZONE_ADD_ICON);
        createEntryZoneButton.setRolloverIcon(GUIConstants.ENTRYZONE_ADD_ICON);

        createExitZoneButton.setMargin(new Insets(1, 1, 1, 1));
        createExitZoneButton.setContentAreaFilled(false);
        createExitZoneButton.setBorder(null);
        createExitZoneButton.setIcon(GUIConstants.EXITZONE_ADD_ICON);
        createExitZoneButton.setPressedIcon(GUIConstants.EXITZONE_ADD_ICON);
        createExitZoneButton.setRolloverIcon(GUIConstants.EXITZONE_ADD_ICON);

        deleteZoneButton.setMargin(new Insets(1, 1, 1, 1));
        deleteZoneButton.setContentAreaFilled(false);
        deleteZoneButton.setBorder(null);
        deleteZoneButton.setIcon(GUIConstants.DELETE_ICON);
        deleteZoneButton.setPressedIcon(GUIConstants.DELETE_ICON_PRESSED);
        deleteZoneButton.setRolloverIcon(GUIConstants.DELETE_ICON_ROLLOVER);

        listPanel.add(buttonsPanel, BorderLayout.SOUTH);

        propertiesPanel = new JPanel(new BorderLayout());
        propertiesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                                                                   "Properties"));
        propertiesPanel.setMinimumSize(new Dimension(GUIConstants.ZONES_OUTLINE_WIDTH,
                                                     GUIConstants.ZONES_OUTLINE_WIDTH
                                                         + GUIFactory.ROW_HEIGHT));
        propertiesPanel.setPreferredSize(new Dimension(GUIConstants.ZONES_OUTLINE_WIDTH,
                                                       GUIConstants.ZONES_OUTLINE_WIDTH
                                                           + GUIFactory.ROW_HEIGHT));

        this.add(propertiesPanel, BorderLayout.SOUTH);

        JPanel innerPropertiesPanel = new JPanel(null);
        nameField =
                GUIFactory.createTitledTextField("Name:",
                                                 "",
                                                 0,
                                                 0,
                                                 GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                 innerPropertiesPanel);

        descriptionField =
                GUIFactory.createTitledTextField("Description:",
                                                 "",
                                                 0,
                                                 1,
                                                 GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                 innerPropertiesPanel);

        destMapSelectField =
                GUIFactory.createTitledComboBoxField("Dest map:",
                                                     0,
                                                     2,
                                                     GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                     innerPropertiesPanel);
        destMapSelectField.setRenderer(new MapTypeListCellRenderer());

        entryZoneSelectField =
                GUIFactory.createTitledComboBoxField("Dest zone:",
                                                     0,
                                                     3,
                                                     GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                     innerPropertiesPanel);
        entryZoneSelectField.setRenderer(new ZoneListCellRenderer());

        FieldChangeListener fieldChangeListener = new ZoneFieldChangeListener();
        nameField.addActionListener(fieldChangeListener);
        nameField.addCaretListener(fieldChangeListener);
        descriptionField.addActionListener(fieldChangeListener);
        descriptionField.addCaretListener(fieldChangeListener);
        destMapSelectField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateValues();
            }
        });
        destMapSelectField.addActionListener(fieldChangeListener);
        entryZoneSelectField.addActionListener(fieldChangeListener);
        propertiesPanel.add(innerPropertiesPanel);
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return ZONES_LABEL;
    }

    /**
     * Initializes zone outline with a map type document.
     * @param mapTypeDocument
     *            map type document
     */
    public void init(ResourceSetDocument<MapType> mapTypeDocument)
    {
        this.mapTypeDocument = mapTypeDocument;

        updateList();
        updateValues();
    }

    private void updateList()
    {
        zonesListModel.clear();

        MapType mapType = mapTypeDocument.getResource();
        //TODO:
        for (EntryZone entryZone : mapType.getEntryZoneMap().values())
        {
            zonesListModel.addElement(entryZone);
        }

        for (ExitZone exitZone : mapType.getExitZoneMap().values())
        {
            zonesListModel.addElement(exitZone);
        }
    }

    private void updateValues()
    {
        if (updatingValues)
        {
            return;
        }
        Zone selectedZone = (Zone) zonesList.getSelectedValue();
        if (selectedZone != null)
        {
            updatingValues = true;
            try
            {
                nameField.setText(selectedZone.getName());
                descriptionField.setText(selectedZone.getDescription());
                updateMapsList(selectedZone);
                updateEntryZoneList(selectedZone);
            }
            finally
            {
                updatingValues = false;
            }
        }
    }

    private void updateMapsList(Zone selectedZone)
    {

        destMapSelectField.removeAllItems();
        destMapSelectField.setSelectedItem(null);

        if (selectedZone != null && selectedZone instanceof ExitZone)
        {

            for (MapType mapType : ResourceSetModel.getInstance()
                                                   .getResourceSet()
                                                   .getMapTypes())
            {
                destMapSelectField.addItem(mapType);
            }
            MapType selectedMapType =
                    ResourceSetModel.getInstance()
                                    .getResourceSet()
                                    .getMapType(((ExitZone) selectedZone).getDestMap());
            destMapSelectField.setSelectedItem(selectedMapType);
        }
    }

    private void updateEntryZoneList(Zone selectedZone)
    {

        entryZoneSelectField.removeAllItems();
        entryZoneSelectField.setSelectedItem(null);

        if (selectedZone != null && selectedZone instanceof ExitZone)
        {
            MapType selectedMapType =
                    ResourceSetModel.getInstance()
                                    .getResourceSet()
                                    .getMapType(((ExitZone) selectedZone).getDestMap());
            if (selectedMapType != null)
            {
                for (EntryZone entryZone : selectedMapType.getEntryZoneMap()
                                                          .values())
                {
                    entryZoneSelectField.addItem(entryZone);
                }

                entryZoneSelectField.setSelectedItem(selectedMapType.getEntryZoneMap()
                                                                    .get(((ExitZone) selectedZone).getDestEntryZone()));
            }
        }
    }

    /**
     * Returns selected zone.
     * @return selected zone
     */
    public Zone getSelectedZone()
    {
        return (Zone) zonesList.getSelectedValue();
    }

    /**
     * Selects given zone.
     * @param zone
     *            zone
     */
    public void selectZone(Zone zone)
    {
        zonesList.setSelectedValue(zone, true);
    }

    /** {@inheritDoc} */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
        {
            return;
        }

        updateValues();

        Zone selectedZone = (Zone) zonesList.getSelectedValue();

        if (selectedZone instanceof ExitZone)
        {
            destMapSelectField.setEnabled(true);
            entryZoneSelectField.setEnabled(true);
        }
        else
        {
            destMapSelectField.setEnabled(false);
            entryZoneSelectField.setEnabled(false);
        }

        for (ZoneSelectionListener entryZoneSelectionListener : zoneSelectionListeners)
        {
            entryZoneSelectionListener.zoneSelected(selectedZone);
        }
    }

    private void updateZone()
    {
        if (updatingValues)
        {
            return;
        }

        Zone selectedZone = (Zone) zonesList.getSelectedValue();
        if (selectedZone != null)
        {
            selectedZone.setName(nameField.getText());
            selectedZone.setDescription(descriptionField.getText());

            if (selectedZone instanceof ExitZone)
            {
                short destMapId =
                        destMapSelectField.getSelectedItem() != null ? ((MapType) destMapSelectField.getSelectedItem()).getId()
                                                                    : -1;
                byte destEntryZoneId =
                        entryZoneSelectField.getSelectedItem() != null ? ((EntryZone) entryZoneSelectField.getSelectedItem()).getId()
                                                                      : -1;

                ((ExitZone) selectedZone).setDestMap(destMapId);
                ((ExitZone) selectedZone).setDestEntryZone(destEntryZoneId);
            }

            mapTypeDocument.getResource().setModified();
            zonesList.repaint();
        }
    }

    private static class CreateEntryZoneAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        @Translated
        private static String ACTION_DESC = "Create new entry zone";

        @Translated
        private static String EMPTY_NAME = "<empty name>";

        private MapTypeZonesOutline mapTypeZonesOutline;

        /**
         * Constructor.
         */
        public CreateEntryZoneAction(MapTypeZonesOutline mapTypeEntryZonesOutline)
        {
            putValue(SHORT_DESCRIPTION, ACTION_DESC);
            putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(LARGE_ICON_KEY, GUIConstants.ENTRYZONE_ICON);
            this.mapTypeZonesOutline = mapTypeEntryZonesOutline;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (mapTypeZonesOutline.mapTypeDocument == null)
            {
                return;
            }

            Map<Byte, EntryZone> entryZoneMap =
                    mapTypeZonesOutline.mapTypeDocument.getResource()
                                                       .getEntryZoneMap();
            Byte maxIndex = (byte) 0;
            if (!entryZoneMap.isEmpty())
            {
                maxIndex = Collections.max(entryZoneMap.keySet());
            }

            Byte newIndex = (byte) (maxIndex + 1);
            EntryZone newEntryZone = new EntryZone(newIndex);
            newEntryZone.setName(EMPTY_NAME);
            //TODO: give infos to the resource set about changes
            entryZoneMap.put(newIndex, newEntryZone);

            mapTypeZonesOutline.mapTypeDocument.getResource().setModified();
            mapTypeZonesOutline.updateList();
            mapTypeZonesOutline.zonesList.setSelectedValue(newEntryZone, true);
        }
    }

    private static class CreateExitZoneAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        @Translated
        private static String ACTION_DESC = "Create new exit zone";

        @Translated
        private static String EMPTY_NAME = "<empty name>";

        private MapTypeZonesOutline mapTypeZonesOutline;

        /**
         * Constructor.
         */
        public CreateExitZoneAction(MapTypeZonesOutline mapTypeEntryZonesOutline)
        {
            putValue(SHORT_DESCRIPTION, ACTION_DESC);
            putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(LARGE_ICON_KEY, GUIConstants.EXITZONE_ICON);
            this.mapTypeZonesOutline = mapTypeEntryZonesOutline;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (mapTypeZonesOutline.mapTypeDocument == null)
            {
                return;
            }

            Map<Byte, ExitZone> exitZoneMap =
                    mapTypeZonesOutline.mapTypeDocument.getResource()
                                                       .getExitZoneMap();
            Byte maxIndex = (byte) 0;
            if (!exitZoneMap.isEmpty())
            {
                maxIndex = Collections.max(exitZoneMap.keySet());
            }

            Byte newIndex = (byte) (maxIndex + 1);
            ExitZone newEntryZone =
                    new ExitZone(newIndex, (short) -1, (byte) -1);
            newEntryZone.setName(EMPTY_NAME);
            //TODO: give infos to the resource set about changes
            exitZoneMap.put(newIndex, newEntryZone);

            mapTypeZonesOutline.mapTypeDocument.getResource().setModified();
            mapTypeZonesOutline.updateList();
            mapTypeZonesOutline.zonesList.setSelectedValue(newEntryZone, true);
        }
    }

    private static class DeleteZoneAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        @Translated
        private static String ACTION_DESC = "Delete zone";

        private MapTypeZonesOutline mapTypeZonesOutline;

        /**
         * Constructor.
         */
        public DeleteZoneAction(MapTypeZonesOutline mapTypeEntryZonesOutline)
        {
            //super(ACTION_NAME);
            putValue(SHORT_DESCRIPTION, ACTION_DESC);
            putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(LARGE_ICON_KEY, GUIConstants.DELETE_ICON);
            this.mapTypeZonesOutline = mapTypeEntryZonesOutline;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (mapTypeZonesOutline.mapTypeDocument == null)
            {
                return;
            }

            Zone selectedZone =
                    (Zone) mapTypeZonesOutline.zonesList.getSelectedValue();
            if (selectedZone == null)
            {
                return;
            }
            if (selectedZone instanceof EntryZone)
            {
                Map<Byte, EntryZone> entryZoneMap =
                        mapTypeZonesOutline.mapTypeDocument.getResource()
                                                           .getEntryZoneMap();

                entryZoneMap.remove(selectedZone.getId());
            }
            else if (selectedZone instanceof ExitZone)
            {
                Map<Byte, ExitZone> exitZoneMap =
                        mapTypeZonesOutline.mapTypeDocument.getResource()
                                                           .getExitZoneMap();

                exitZoneMap.remove(selectedZone.getId());
            }
            mapTypeZonesOutline.mapTypeDocument.getResource().setModified();
            mapTypeZonesOutline.updateList();
        }
    }

    /**
     * Returns zone for a given position.
     * @param position
     *            position
     * @return zone for a given position
     */
    public Zone getZoneForPosition(Position position)
    {
        MapType mapType = mapTypeDocument.getResource();
        for (Zone zone : mapType.getEntryZoneMap().values())
        {
            //TODO: hashmap cache
            if (zone.getPositionsList().contains(position))
            {
                return zone;
            }
        }
        for (Zone zone : mapType.getExitZoneMap().values())
        {
            //TODO: hashmap cache
            if (zone.getPositionsList().contains(position))
            {
                return zone;
            }
        }
        return null;
    }

    private class ZoneFieldChangeListener extends FieldChangeListener
    {

        /** {@inheritDoc} */
        @Override
        public void fieldChanged()
        {
            MapTypeZonesOutline.this.updateZone();

        }

    }

    private static class ZoneListCellRenderer extends JLabel implements
                                                            ListCellRenderer
    {

        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            if (value instanceof Zone)
            {
                String name = ((Zone) value).getName();
                this.setText(name == null || name.isEmpty() ? "<"
                    + ((Zone) value).getId() + ">" : name);

                Icon icon = null;
                if (value instanceof EntryZone)
                {
                    icon = GUIConstants.ENTRYZONE_ICON;
                }
                else if (value instanceof ExitZone)
                {
                    icon = GUIConstants.EXITZONE_ICON;
                }
                this.setIcon(icon);

            }
            else
            {
                this.setText(null);
                this.setIcon(null);
            }
            if (isSelected)
            {
                this.setOpaque(true);
                this.setBackground(GUIConstants.SELECTED_LIST_ROW_COLOR);
            }
            else
            {
                this.setBackground(Color.WHITE);
            }
            return this;
        }
    }

    /**
     * Registers a zone selection listener.
     * @param zoneSelectionListener
     *            entry zone selection listener
     */
    public void addZoneSelectionListener(ZoneSelectionListener zoneSelectionListener)
    {
        zoneSelectionListeners.add(zoneSelectionListener);
    }

    private static class MapTypeListCellRenderer extends JLabel implements
                                                               ListCellRenderer
    {

        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            if (value instanceof MapType)
            {
                String name = ((MapType) value).getName();
                this.setText(name == null || name.isEmpty() ? "<"
                    + ((MapType) value).getId() + ">" : name);
            }
            else
            {
                this.setText(null);
            }
            if (isSelected)
            {
                this.setOpaque(true);
                this.setForeground(Color.WHITE);
                this.setBackground(Color.BLUE);
            }
            else
            {
                this.setForeground(Color.BLACK);
                this.setBackground(Color.WHITE);
            }
            return this;
        }
    }

}
