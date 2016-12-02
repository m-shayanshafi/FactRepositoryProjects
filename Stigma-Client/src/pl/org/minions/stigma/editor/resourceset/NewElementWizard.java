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

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import javax.swing.JPanel;

import pl.org.minions.stigma.editor.resourceset.ValidationResult.ResultType;

/**
 * Base class for a new element wizard.
 */
public abstract class NewElementWizard extends JPanel
{

    private static final long serialVersionUID = 1L;

    private List<Validator> validators = new LinkedList<Validator>();

    private List<ValidationListener> validationListeners =
            new LinkedList<ValidationListener>();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getName();

    /**
     * Returns description.
     * @return description
     */
    public abstract String getDescription();

    /**
     * Saves new element in the resource set.
     * @return completion flag
     */
    public abstract Object saveNewElement();

    /**
     * Validates inputs in the resource set.
     * @param validationResults
     *            results of validation
     * @return true if validation passed, false if didn't
     */
    public boolean validateInputs(SortedSet<ValidationResult> validationResults)
    {
        boolean result = true;
        for (Validator validator : validators)
        {
            ValidationResult validationResult = validator.validate();

            if (validationResult != null)
            {
                if (validationResults != null)
                {
                    validationResults.add(validationResult);
                }

                if (validationResult.getResultType().equals(ResultType.ERROR))
                {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Asks each validation listener for validation.
     */
    protected void notifyValidation()
    {
        for (ValidationListener validatorListener : validationListeners)
        {
            validatorListener.notifyValidationNeed();
        }
    }

    /**
     * Adds validator.
     * @param validator
     *            validator
     */
    protected void addValidator(Validator validator)
    {
        validators.add(validator);
    }

    /**
     * Removes validator.
     * @param validator
     *            validator
     */
    protected void removeValidator(Validator validator)
    {
        validators.remove(validator);
    }

    /**
     * Registers validation listener.
     * @param validationListener
     *            validation listener
     */
    public void addValidationListener(ValidationListener validationListener)
    {
        validationListeners.add(validationListener);
    }

    /**
     * Removes validation listener.
     * @param validationListener
     *            validation listener
     */
    public void removeValidationListener(ValidationListener validationListener)
    {
        validationListeners.remove(validationListener);
    }
}
