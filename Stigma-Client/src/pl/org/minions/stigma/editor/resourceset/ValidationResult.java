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

/**
 * Validation result containing message, resultType and
 * priority.
 */
public class ValidationResult implements Comparable<ValidationResult>
{
    /**
     * Type of the validation result.
     */
    public enum ResultType
    {
        ERROR, WARNING, INFO;
    }

    public static final int HIGH = 1;
    public static final int MEDIUM = 10;
    public static final int LOW = 100;

    private String message;
    private ResultType resultType;
    private int priority;

    /**
     * Constructor.
     * @param message
     *            message
     * @param resultType
     *            result type
     * @param priority
     *            priority
     */
    public ValidationResult(String message, ResultType resultType, int priority)
    {
        super();
        this.message = message;
        this.resultType = resultType;
        this.priority = priority;
    }

    /**
     * Returns resultType.
     * @return resultType
     */
    public ResultType getResultType()
    {
        return resultType;
    }

    /**
     * Returns validation message.
     * @return validation message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Returns validation result priority.
     * @return validation result priority
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(ValidationResult o)
    {
        if (o.getResultType().equals(getResultType()))
        {
            return ((Integer) getPriority()).compareTo(o.getPriority());
        }
        return ((Integer) getResultType().ordinal()).compareTo(o.getResultType()
                                                                .ordinal());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "" + resultType + "(" + priority + ") - " + message;
    }
}
