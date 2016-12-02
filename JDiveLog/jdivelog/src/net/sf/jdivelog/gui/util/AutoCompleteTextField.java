/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AutoCompleteTextField.java
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
package net.sf.jdivelog.gui.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class AutoCompleteTextField extends JTextField implements AutoComplete {

    private static final long serialVersionUID = 1L;

    private String separatorChar;
    
    /**
     * Default Constructor for GUI Builder, do not use!
     */
    @Deprecated
    public AutoCompleteTextField() {
        super();
        setUp();
    }

    /**
     * Constructs a new AutoCompleteTextField that uses the specified dictionary
     * for autocomplete look up. A default model is created and the number of
     * columns is 0.
     * 
     * @param dict
     *            The dictionary to be used for autocomplete, or null.
     * @param separatorChar separator character if multiple entries can be supplied.
     */
    public AutoCompleteTextField(AutoCompleteDictionary dict, String separatorChar) {
        super();
        this.separatorChar = separatorChar;
        setUp();
        setDictionary(dict);
    }

    /**
     * Constructs a new AutoCompleteTextField that uses the specified dictionary
     * for autocomplete look up and is initialized with the specified text. A
     * default model is created and the number of columns is 0.
     * 
     * @param text
     *            the text to be displayed, or null
     * @param dict
     *            The dictionary to be used for autocomplete, or null.
     */
    public AutoCompleteTextField(String text, AutoCompleteDictionary dict) {
        super(text);
        setUp();
        setDictionary(dict);
    }

    /**
     * Constructs a new empty AutoCompleteTextField with the specified number of
     * columns, and uses the specified dictionary for autocomplete look up. A
     * default model is created and the initial string is set to null.
     * 
     * @param columns
     *            the number of columns to use to calculate the preferred width.
     *            If columns is set to zero, the preferred width will be
     *            whatever naturally results from the component implementation.
     * @param dict
     *            The dictionary to be used for autocomplete, or null.
     */
    public AutoCompleteTextField(int columns, AutoCompleteDictionary dict) {
        super(columns);
        setUp();
        setDictionary(dict);
    }

    /**
     * Constructs a new AutoCompleteTextField initialized with the specified
     * text and columns, and uses the specified dictionary for autocomplete look
     * up. A default model is created.
     * 
     * @param text
     *            the text to be displayed, or null
     * @param columns
     *            the number of columns to use to calculate the preferred width.
     *            If columns is set to zero, the preferred width will be
     *            whatever naturally results from the component implementation.
     * @param dict
     *            The dictionary to be used for autocomplete, or null.
     */
    public AutoCompleteTextField(String text, int columns, AutoCompleteDictionary dict) {
        super(text, columns);
        setUp();
        setDictionary(dict);
    }

    /**
     * Constructs a new AutoCompleteTextField that uses the given text storage
     * model and the given number of columns. This is the constructor through
     * which the other constructors feed. If the document is null, a default
     * model is created.
     * 
     * @param doc
     *            the text storage to use. If this is null, a default will be
     *            provided by calling the createDefaultModel method.
     * @param text
     *            the initial string to display, or null
     * @param columns
     *            the number of columns to use to calculate the preferred width >=
     *            0. If columns is set to zero, the preferred width will be
     *            whatever naturally results from the component implementation.
     * @param dict
     *            The dictionary to be used for autocomplete, or null.
     * @exception IllegalArgumentException
     *                if columns < 0
     */
    public AutoCompleteTextField(Document doc, String text, int columns, AutoCompleteDictionary dict) {
        super(doc, text, columns);
        setUp();
        setDictionary(dict);
    }

    // ----------------------------------------------------------------------------
    // Public methods
    // ----------------------------------------------------------------------------

    /**
     * Set the dictionary that autocomplete lookup should be performed by.
     * 
     * @param dict
     *            The dictionary that will be used for the autocomplete lookups.
     */
    public void setDictionary(AutoCompleteDictionary dict) {
        this.dict = dict;
    }

    /**
     * Gets the dictionary currently used for lookups.
     * 
     * @return dict The dictionary that will be used for the autocomplete
     *         lookups.
     */
    public AutoCompleteDictionary getDictionary() {
        return dict;
    }

    /**
     * Sets whether the component is currently performing autocomplete lookups
     * as keystrokes are performed.
     * 
     * @param val
     *            True or false.
     */
    public void setAutoComplete(boolean val) {
        this.autoComplete = val;
    }

    /**
     * Gets whether the component is currently performing autocomplete lookups
     * as keystrokes are performed.
     * 
     * @return True or false.
     */
    public boolean getAutoComplete() {
        return autoComplete;
    }

    // ----------------------------------------------------------------------------
    // Protected methods
    // ----------------------------------------------------------------------------
    protected void setUp() {

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                isTextSelected = getSelectionStart() != getSelectionEnd();
            }

            public void keyReleased(KeyEvent e) {
                char charPressed = e.getKeyChar();
                int charCodePressed = e.getKeyCode();

                if (charCodePressed == KeyEvent.VK_DELETE || charPressed == KeyEvent.CHAR_UNDEFINED) {
                    return;
                }
                if (getSelectionStart() != getSelectionEnd()) {
                    setText(getText().substring(0, getSelectionStart()));
                }

                String input = getText();
                String key = input;
                
                int idx = -1;
                if (separatorChar != null) {
                    idx = input.lastIndexOf(separatorChar);
                }
                if (idx > 0) {
                    key = input.substring(idx+1);
                }
                String replaceString = lookup(key.trim());
                if (replaceString != null) {
                    if (idx >= 0) {
                        setText(input.substring(0, idx+1)+replaceString);
                        setSelectionStart(idx+1+key.length());
                    } else {
                        setText(replaceString);
                        setSelectionStart(input.length());
                    }
                    setSelectionEnd(getText().length());
                    isTextSelected = true;
                } else {
                    isTextSelected = false;
                }

                if (charCodePressed == KeyEvent.VK_BACK_SPACE && isTextSelected && input.length() > 0) {
                    setText(input.substring(0, input.length()));
                }
            }
        });

    }

    protected String lookup(String s) {
        if (dict != null) { // && autoComplete) {
            return dict.lookup(s);
        }
        return null;
    }

    // ----------------------------------------------------------------------------
    // Fields
    // ----------------------------------------------------------------------------
    private boolean isTextSelected = false;

    protected AutoCompleteDictionary dict;

    protected boolean autoComplete = true;
}
