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
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.gui.GUIFactory;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.game.map.ExitZone;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Exit zones outline of the map type editor.
 */
public class MapTypeExitZonesOutline extends ResourceEditorOutline implements
                                                                  ListSelectionListener
{
    private static final long serialVersionUID = 1L;

    @Translated
    private static String ZONES_LABEL = "Zones";

    private JPanel listPanel;
    private JPanel propertiesPanel;

    private DefaultListModel zonesListModel;
    private JList zonesList;

    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField destinationMapField;
    private JTextField destinationEntryZoneField;

    /**
     * Constructor.
     */
    public MapTypeExitZonesOutline()
    {
        super();
        this.setLayout(new BorderLayout());

        listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                                                             ZONES_LABEL));

        zonesListModel = new DefaultListModel();
        zonesList = new JList(zonesListModel);
        zonesList.addListSelectionListener(this);

        listPanel.add(new JScrollPane(zonesList));
        this.add(listPanel, BorderLayout.CENTER);

        propertiesPanel = new JPanel(new BorderLayout());
        propertiesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                                                                   "Properties"));
        propertiesPanel.setMinimumSize(new Dimension(GUIConstants.ZONES_OUTLINE_WIDTH,
                                                     GUIConstants.ZONES_OUTLINE_WIDTH));
        propertiesPanel.setPreferredSize(new Dimension(GUIConstants.ZONES_OUTLINE_WIDTH,
                                                       GUIConstants.ZONES_OUTLINE_WIDTH));
        this.add(propertiesPanel, BorderLayout.SOUTH);

        //CHECKSTYLE:OFF
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

        destinationMapField =
                GUIFactory.createTitledTextField("Destination map:",
                                                 "",
                                                 0,
                                                 2,
                                                 GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                 innerPropertiesPanel);
        destinationEntryZoneField =
                GUIFactory.createTitledTextField("Destination zone:",
                                                 "",
                                                 0,
                                                 3,
                                                 GUIConstants.ZONES_OUTLINE_ITEM_ROW_WIDTH,
                                                 innerPropertiesPanel);
        //CHECKSTYLE:ON
        propertiesPanel.add(innerPropertiesPanel);
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return ZONES_LABEL;
    }

    /**
     * Initializes exit zone outline with a map type
     * document.
     * @param mapTypeDocument
     *            map type document
     */
    public void init(ResourceSetDocument<MapType> mapTypeDocument)
    {

        MapType mapType = mapTypeDocument.getResource();

        zonesListModel.clear();
        for (ExitZone exitZone : mapType.getExitZoneMap().values())
        {
            zonesListModel.addElement(exitZone);
        }
        updateValues();
    }

    private void updateValues()
    {
        ExitZone selectedExitZone = (ExitZone) zonesList.getSelectedValue();
        if (selectedExitZone != null)
        {
            nameField.setText(selectedExitZone.getName());
            descriptionField.setText(selectedExitZone.getDescription());
            destinationMapField.setText("" + selectedExitZone.getDestMap());
            destinationEntryZoneField.setText(""
                + selectedExitZone.getDestEntryZone());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        updateValues();

    }
}
