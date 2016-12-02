// Written by Chris Redekop <waveform@users.sourceforge.net>
// Released under the conditions of the GPL (See below).
//
// Thanks to Travis Whitton for the Gnap Fetch code used by Napsack.
//
// Also thanks to Radovan Garabik for the pynap code used by Napsack.
//
// Napsack - a specialized client for launching cross-server Napster queries.
// Copyright (C) 2000-2002 Chris Redekop
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

package napsack.util.properties;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.StringTokenizer;

import napsack.protocol.Comparison;
import napsack.protocol.Connection;
import napsack.util.Switch;

public abstract class Property {
	private String lastParsedProperty;
	private String lastValidatedProperty;
	private volatile Object value;
	private PropertyChangeSupport propertyChangeSupport;

   protected static Integer getInteger(final String integer_) {
      Integer value_ = null;

      if (integer_ != null) {
         value_ = Integer.valueOf(integer_);
      }

      return value_;
   }

   protected static String[] getStrings(final String string_) {
      String[] strings_ = null;

      if (string_ != null) {
         final StringTokenizer stringTokenizer_ = new StringTokenizer(string_, (String) DelimiterProperty.getInstance().getValue());
         strings_ = new String[stringTokenizer_.countTokens()];

         for (int i = 0; stringTokenizer_.hasMoreTokens(); ++i) {
            strings_[i] = stringTokenizer_.nextToken();
      	}
		} 

      return strings_;
   }

   public static boolean isInt(final String candidateInt_) {
      boolean isInt_ = false;

      try {
         Integer.parseInt(candidateInt_);
         isInt_ = true;
      } catch (NumberFormatException numberFormatException_) {
      }

      return isInt_;
   }

   public static boolean isTooBig(final String candidateInt_, int maxValue_) {
		return (Integer.parseInt(candidateInt_) > maxValue_);
   }

   public static boolean isTooSmall(final String candidateInt_, int minValue_) {      return (Integer.parseInt(candidateInt_) < minValue_);
   }

	protected Property() {
		setLastParsedProperty("\u0000\u0000\u0000\u0000\u0000\u0000\u0000"); // seven for luck...
		setLastValidatedProperty("\u0000\u0000\u0000\u0000\u0000\u0000\u0000");
	}

	public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener_) {
		getPropertyChangeSupport().addPropertyChangeListener(getName(), propertyChangeListener_);
	}

	public void clearProperty() {
		NapsackProperties.getInstance().remove(getName());
	}

   protected Integer getInteger() {
		return getInteger(getProperty());
	}

	private String getLastParsedProperty() {
		return lastParsedProperty;
	}

	private String getLastValidatedProperty() {
		return lastValidatedProperty;
	}

	protected abstract String getName();

	protected String getProperty() {
		return NapsackProperties.getInstance().getProperty(getName());
	}

	private PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport == null) {
			synchronized (this) {
				if (propertyChangeSupport == null) {
					propertyChangeSupport = new PropertyChangeSupport(this);
				}
			}
		}

		return propertyChangeSupport;
	}

	protected String[] getStrings() {
		return getStrings(getProperty());
	}

	public synchronized Object getValue() {
		final String property_ = getProperty();
		final String lastParsedProperty_ = getLastParsedProperty();

		if (property_ == null && property_ != lastParsedProperty_ || property_ != null && !property_.equals(lastParsedProperty_)) {
			setValue(parseProperty());
			setLastParsedProperty(property_);
		}

		return value;
	}

	protected Boolean isSwitchOn() {
		final String switch_ = getProperty();
		Boolean on_ = null;

		if (switch_ != null) {
			on_ = new Boolean(Switch.getSwitch(switch_).isOn());
		}

		return on_;
	}

	protected abstract Object parseProperty();

	public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener_) {
		getPropertyChangeSupport().removePropertyChangeListener(getName(), propertyChangeListener_);
	}

	public void setProperty(final String property_) {
		final String oldProperty_ = getProperty();

		if (property_ != null) {
			NapsackProperties.getInstance().setProperty(getName(), property_);
		} else {
			NapsackProperties.getInstance().remove(getName());
		}

		getPropertyChangeSupport().firePropertyChange(getName(), oldProperty_, property_);
	}

	private synchronized void setLastParsedProperty(final String lastParsedProperty_) {
		lastParsedProperty = lastParsedProperty_;
	}

	private synchronized void setLastValidatedProperty(final String lastValidatedProperty_) {
		lastValidatedProperty = lastValidatedProperty_;
	}

	private void setValue(final Object value_) {
		value = value_;
	}

	public synchronized void validate() throws PropertyException {
		final String property_ = getProperty();
		final String lastValidatedProperty_ = getLastValidatedProperty();

		if (property_ == null && property_ != lastValidatedProperty_ || property_ != null && !property_.equals(lastValidatedProperty_)) {
			validateProperty();
			setLastValidatedProperty(property_);
		}
	}
					
   protected void validateComparison(final String comparison_) throws PropertyException {
      if (comparison_ != null && Comparison.getComparison(comparison_) == null) {
			throw new EnumerationException("Invalid comparison value for " + getName() + "; valid comparison values are ", Comparison.getValidComparisons());
      }
   }

	protected void validateInteger(final String property_) throws PropertyException {
		validateInteger(property_, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	protected void validateInteger(final String property_, final int minValue_) throws PropertyException {
		validateInteger(property_, minValue_, Integer.MAX_VALUE);
   }

	protected void validateInteger(final String property_, final int minValue_, final int maxValue_) throws PropertyException {
      if (property_ != null && (!isInt(property_) || isTooSmall(property_, minValue_) || isTooBig(property_, maxValue_))) {
         throw new PropertyException("Invalid value for " + getName() + "; valid values are between " +  minValue_ + " and " + maxValue_ + ".");
      }
   }

	protected void validateProperty() throws PropertyException {
		validate(getProperty());
	}

	public abstract void validate(final String property_) throws PropertyException;

   protected void validateSwitch(final String property_) throws PropertyException {
      if (property_ != null && Switch.getSwitch(property_) == null) {
         throw new EnumerationException("Invalid value for " + getName() + "; valid values are ", Switch.getValidSwitches());
      }
   }
}

