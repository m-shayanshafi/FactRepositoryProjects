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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pl.org.minions.stigma.editor.actions.ActionContainer;
import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.gui.GUIFactory;
import pl.org.minions.stigma.editor.gui.MainFrame;
import pl.org.minions.stigma.editor.resourceset.ValidationResult.ResultType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Base class for a new element wizard.
 */
public class NewElementDialog extends JDialog implements
                                             ActionListener,
                                             ValidationListener
{

    private static final long serialVersionUID = 1L;

    @Translated
    private static String FINISH_BUTTON_LABEL = "Finish";
    @Translated
    private static String CANCEL_BUTTON_LABEL = "Cancel";

    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 50;

    private JLabel infoLabel;

    private ResourceSetCategory<?> resourceSetCategory;
    private NewElementWizard newElementWizard;

    private JButton cancelButton;
    private JButton finishButton;

    private boolean finished;

    /**
     * Constructor.
     * @param resourceSetCategory
     *            category within the resource set
     */
    public NewElementDialog(ResourceSetCategory<?> resourceSetCategory)
    {
        super(MainFrame.getMainFrame(), true);
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.resourceSetCategory = resourceSetCategory;
        newElementWizard = resourceSetCategory.getNewElementWizard();

        // info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoLabel =
                new JLabel(newElementWizard.getDescription(),
                           GUIConstants.INFO_ICON,
                           SwingConstants.LEFT);
        GUIFactory.setFont(infoLabel);
        infoPanel.setBackground(Color.white);
        infoPanel.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        infoPanel.setPreferredSize(infoPanel.getMinimumSize());
        infoPanel.add(infoLabel, BorderLayout.WEST);
        this.add(infoPanel, BorderLayout.NORTH);

        // wizard panel

        this.setTitle(newElementWizard.getName());
        this.add(newElementWizard, BorderLayout.CENTER);

        // option panel
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,
                                             GUIConstants.DIALOG_FLOW_MARGIN,
                                             GUIConstants.DIALOG_FLOW_MARGIN));

        finishButton = GUIFactory.createTitledButton(FINISH_BUTTON_LABEL);
        finishButton.addActionListener(this);
        optionPanel.add(finishButton);

        cancelButton = GUIFactory.createTitledButton(CANCEL_BUTTON_LABEL);
        cancelButton.addActionListener(this);
        optionPanel.add(cancelButton);

        this.add(optionPanel, BorderLayout.SOUTH);

        newElementWizard.addValidationListener(this);

        this.pack();
    }

    /**
     * Used to obtain a new element created by the wizard.
     */
    public void getNewElement()
    {
        finished = false;
        this.setVisible(true);

        if (finished)
        {
            newElementWizard.saveNewElement();
            Collection<? extends ResourceSetDocument<?>> added =
                    resourceSetCategory.reinit(ResourceSetModel.getInstance()
                                                               .getResourceSet());
            if (!added.isEmpty())
            {
                MainFrame.getMainFrame()
                         .selectResourceSetViewNode(added.iterator().next());
                ActionContainer.OPEN_DOCUMENT.actionPerformed(null);
            }

        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == cancelButton)
        {
            this.setVisible(false);
        }
        else if (source == finishButton)
        {
            SortedSet<ValidationResult> validationResults =
                    new TreeSet<ValidationResult>();
            if (newElementWizard.validateInputs(validationResults))
            {
                finished = true;
                this.setVisible(false);
            }
            else
            {
                applyValidationResults(validationResults);
            }
        }

    }

    private void applyValidationResults(SortedSet<ValidationResult> validationResults)
    {
        if (!validationResults.isEmpty())
        {

            ValidationResult mostImportantValidationResult =
                    validationResults.first();
            if (mostImportantValidationResult != null)
            {
                if (mostImportantValidationResult.getResultType()
                                                 .equals(ResultType.ERROR))
                {
                    infoLabel.setIcon(GUIConstants.ERROR_ICON);
                    infoLabel.setForeground(Color.red);
                }
                else if (mostImportantValidationResult.getResultType()
                                                      .equals(ResultType.WARNING))
                {
                    infoLabel.setIcon(GUIConstants.WARNING_ICON);
                    infoLabel.setForeground(Color.orange);
                }
                else
                {
                    infoLabel.setIcon(GUIConstants.INFO_ICON);
                    infoLabel.setForeground(Color.black);
                }

                infoLabel.setText(GUIFactory.htmlizeText(mostImportantValidationResult.getMessage()));
            }
        }
        else
        {
            infoLabel.setForeground(Color.black);
            infoLabel.setIcon(GUIConstants.INFO_ICON);
            infoLabel.setText(GUIFactory.htmlizeText(newElementWizard.getDescription()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notifyValidationNeed()
    {
        SortedSet<ValidationResult> validationResults =
                new TreeSet<ValidationResult>();
        newElementWizard.validateInputs(validationResults);

        applyValidationResults(validationResults);
    }
}
