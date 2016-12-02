/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: MnemonicFactory.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;

/**
 * Assigns a Mnemonic to components
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class MnemonicFactory {

    private final MnemonicFinder[] finders = { new FirstCharacterMnemonicFinder(), new UpperCaseMnemonicFinder(), new LetterOrDigitMnemonicFinder(), };

    private final Map<Integer, AbstractButton> mnemonicMap = new HashMap<Integer, AbstractButton>();

    private final ContainerChangeListener containerListener = new ContainerChangeListener();

    private final TextChangeListener textChangeListener = new TextChangeListener();

    private final HashSet<Component> components = new HashSet<Component>();
    
    private final MnemonicFactory parent;

    public MnemonicFactory(Component comp) {
        this(null, comp);
    }
    
    private MnemonicFactory(MnemonicFactory parent, Component comp) {
        this.parent = parent;
        if (comp instanceof JMenu) {
            addContainer((Container) comp);
        } else {
            addComponent(comp);
        }
    }

    private void addComponent(Component comp) {
        if (!components.contains(comp)) {
            components.add(comp);
            if (comp instanceof AbstractButton) {
                addMnemonic((AbstractButton) comp);
            }
            if (comp instanceof Container) {
                addContainer((Container) comp);
            }
        }
    }

    private void removeComponent(Component comp) {
        if (components.contains(comp)) {
            components.remove(comp);
            if (comp instanceof AbstractButton) {
                removeMnemonic((AbstractButton) comp);
            }
            if (comp instanceof Container) {
                removeContainer((Container) comp);
            }
        }
    }

    private void addMnemonic(AbstractButton comp) {
        comp.addPropertyChangeListener(textChangeListener);
        int mnemonic = findUnusedMnemonic(comp);
        if (mnemonic != 0) {
            comp.setMnemonic(mnemonic);
            mnemonicMap.put(comp.getMnemonic(), comp);
        }
    }

    private void removeMnemonic(AbstractButton comp) {
        comp.removePropertyChangeListener(textChangeListener);
        mnemonicMap.remove(comp.getMnemonic());
    }

    private void addContainer(Container cont) {
        cont.addContainerListener(containerListener);
        if (cont instanceof JTabbedPane) {
            Component[] comps = cont.getComponents();
            for (int i = 0; i < comps.length; i++) {
                new MnemonicFactory(this, comps[i]);
            }
        } else {
            Component[] comps = cont.getComponents();
            for (int i = 0; i < comps.length; i++) {
                addComponent(comps[i]);
            }
            for (int i = 0; i < comps.length; i++) {
                if (comps[i] instanceof JMenu) {
                    new MnemonicFactory(((JMenu)comps[i]).getPopupMenu());
                }
            }
        }
    }

    private void removeContainer(Container cont) {
        cont.removeContainerListener(containerListener);
        Component[] comps = cont.getComponents();
        for (int i = 0; i < comps.length; i++) {
            removeComponent(comps[i]);
        }
    }

    private int findUnusedMnemonic(AbstractButton comp) {
        for (int i = 0; i < finders.length; i++) {
            int mn = finders[i].apply(comp.getText());
            if (mn != 0) {
                return mn;
            }
        }
        return 0;
    }

    private int toKey(char c) {
        int lower = Character.toUpperCase(c);
        if (!isOccupied(lower)) {
            return lower;
        }
        return 0;
    }
    
    private boolean isOccupied(int key) {
        if (parent != null && parent.isOccupied(key)) {
            return true;
        }
        return mnemonicMap.containsKey(key);
    }

    private class ContainerChangeListener implements ContainerListener {

        public void componentAdded(ContainerEvent e) {
            addComponent(e.getChild());
        }

        public void componentRemoved(ContainerEvent e) {
            removeComponent(e.getChild());
        }

    }

    private class TextChangeListener implements PropertyChangeListener {

        public TextChangeListener() {

        }

        public void propertyChange(PropertyChangeEvent e) {
            if (e.getSource() instanceof AbstractButton && "text".equals(e.getPropertyName())) {
                removeMnemonic((AbstractButton) e.getSource());
                addMnemonic((AbstractButton) e.getSource());
            }
        }

    }

    private interface MnemonicFinder {
        int apply(String text);
    }

    private class FirstCharacterMnemonicFinder implements MnemonicFinder {
        public int apply(String text) {
            return text.length() > 0 ? toKey(text.charAt(0)) : 0;
        }
    }

    private abstract class AbstactEarliestMatchMnemonicFinder implements MnemonicFinder {
        public int apply(String text) {
            final char[] characters = text.toCharArray();

            for (int i = 0; i < characters.length; ++i) {
                if (matches(characters[i])) {
                    final int result = toKey(characters[i]);

                    if (result != 0) {
                        return result;
                    }
                }
            }

            return 0;
        }

        protected abstract boolean matches(char c);
    }

    private class UpperCaseMnemonicFinder extends AbstactEarliestMatchMnemonicFinder {
        protected boolean matches(char c) {
            return Character.isUpperCase(c);
        }
    }

    private class LetterOrDigitMnemonicFinder extends AbstactEarliestMatchMnemonicFinder {
        protected boolean matches(char c) {
            return Character.isLetterOrDigit(c);
        }
    }
}
