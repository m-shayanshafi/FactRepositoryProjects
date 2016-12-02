package org.gcs.robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * RCKeys manages custom key bindings.
 * @author john B. Matthews
 */
public class RCKeys extends JDialog
    implements ActionListener, FocusListener, KeyListener {

    private EnumMap<Key, Integer> savedKeys =
        new EnumMap<Key, Integer>(Key.class);
    private HashMap<JTextField, Key> keyMap =
        new HashMap<JTextField, Key>(savedKeys.size() * 4 / 3);
    private JTextField currentText;
    private Key currentKey;

    /** Construct a key binding editor dialog. */
    public RCKeys(JFrame parent) {
        super(parent, true);
        this.setLocationByPlatform(true);
        this.setLayout(new BorderLayout(10, 10));
        this.add(genTopPanel(), BorderLayout.NORTH);
        this.add(genCenterPanel(), BorderLayout.CENTER);
        this.add(genBottomPanel(), BorderLayout.SOUTH);
        Component box = Box.createRigidArea(new Dimension(10, 0));
        this.add(box, BorderLayout.WEST);
        box = Box.createRigidArea(new Dimension(10, 0));
        this.add(box, BorderLayout.EAST);
        this.pack();
        for (Key k : Key.values()) savedKeys.put(k, k.getKeyCode());
        currentText.setBackground(Color.LIGHT_GRAY);
        highlightDuplicates();
    }
    
    private JPanel genTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon icon = RCImage.scale(RCImage.Me, 64, 64);
        JLabel label = new JLabel(
            "Click or tab to select a field, then press the desired key.",
            icon, JLabel.LEADING);
        label.setIconTextGap(10);
        panel.add(label);
        return panel;
    }
    
    private JPanel genCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        TitledBorder title;
        JPanel p1 = new JPanel(new GridLayout(0, 2, 10, 10));
        p1.setPreferredSize(new Dimension(125, 325));
        title = BorderFactory.createTitledBorder("Commands");
        p1.setBorder(title);
        for (Key k : EnumSet.range(Key.Animation, Key.ResetScore))
            p1.add(genKeyPanel(k));
        panel.add(p1);
        
        Component box = Box.createRigidArea(new Dimension(10, 0));
        panel.add(box);
        
        JPanel p2 = new JPanel(new GridLayout(0, 3, 10, 10));
        p1.setPreferredSize(new Dimension(175, 325));
        title = BorderFactory.createTitledBorder("Moves");
        p2.setBorder(title);
        for (Key k : EnumSet.range(Key.NW, Key.NE)) p2.add(genKeyPanel(k));
        for (Key k : EnumSet.range(Key.WEST, Key.EAST)) p2.add(genKeyPanel(k));
        for (Key k : EnumSet.range(Key.SW, Key.SE)) p2.add(genKeyPanel(k));
        p2.add(genKeyPanel(Key.JUMP));
        panel.add(p2);
        return panel;
    }
    
    private JPanel genKeyPanel(Key key) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(key.name(), JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(label);

        JTextField text = new JTextField(10);
        text.setHorizontalAlignment(JTextField.CENTER);
        text.setBackground(Color.WHITE);
        text.setEditable(false);
        text.setText(KeyEvent.getKeyText(key.getKeyCode()));
        text.addKeyListener(this);
        text.addFocusListener(this);
        keyMap.put(text, key);
        panel.add(text);
        if (currentKey == null) currentKey = key;
        if (currentText == null) currentText = text;
        return panel;
    }
    
    private JPanel genBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton revertButton = new JButton("Revert");
        revertButton.setActionCommand("revert");
        revertButton.setToolTipText("Revert to previous settings.");
        panel.add(revertButton);
        revertButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.setToolTipText("Close without saving changes.");
        panel.add(cancelButton);
        cancelButton.addActionListener(this);

        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.setToolTipText("Close after saving changes.");
        panel.add(saveButton);
        saveButton.addActionListener(this);

        return panel;
    }

    /** Handle buttons. */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("revert".equals(cmd)) {
            for (JTextField text : keyMap.keySet()) {
                Key k = keyMap.get(text);
                int prevCode = savedKeys.get(k);
                if (k.getKeyCode() != prevCode) {
                    k.setKeyCode(prevCode);
                    text.setText(KeyEvent.getKeyText(prevCode));
                }
            }
            highlightDuplicates();
        } else if ("cancel".equals(cmd)) {
            for (Key k : Key.values()) {
                int prevCode = savedKeys.get(k);
                if (k.getKeyCode() != prevCode) {
                    k.setKeyCode(prevCode);
                }
            }
            this.setVisible(false);
            this.dispose();
        } else if ("save".equals(cmd)) {
            boolean pristine = true;
            for (Key k : Key.values()) {
                int prevCode = savedKeys.get(k);
                pristine &= k.getKeyCode() == prevCode;
            }
            if (!pristine) RCPrefs.putKeys();
            this.setVisible(false);
            this.dispose();
        }
    }

    /** Handle keyPressed events; reserve shift for shift-tab navigation. */
    public void keyPressed(KeyEvent e) {
        if (!e.isShiftDown()) {
            int keyCode = e.getKeyCode();
            currentKey.setKeyCode(keyCode);
            currentText.setText(KeyEvent.getKeyText(keyCode));
            highlightDuplicates();
        }
    }
    
    /**
     * Highlight any duplicate keys. Duplicates are not forbidden,
     * but they should be apparent, as only the first will be used.
     * The algorithm compares each text field to every other,
     * except itself, and accumulates fields with duplicate keys.
     * The text is black by default, while duplicates are red.
     */
    private void highlightDuplicates() {
        ArrayList<JTextField> dups = new ArrayList<JTextField>();
        for (JTextField t1 : keyMap.keySet()) {
            t1.setForeground(Color.BLACK);
            Key k = keyMap.get(t1);
            for (JTextField t2 : keyMap.keySet()) {
                Key j = keyMap.get(t2);
                if (t1 != t2) {
                    if (k.getKeyCode() == j.getKeyCode()) {
                        if (!dups.contains(t1)) dups.add(t1);
                        if (!dups.contains(t2)) dups.add(t2);
                    }
                }
            }
        }
        for (JTextField tf : dups)
            tf.setForeground(Color.RED);
    }

    /** Handle keyReleased events (unused). */
    public void keyReleased(KeyEvent e) {}

    /** Handle keyTyped events (unused). */
    public void keyTyped(KeyEvent e) {}

    /** Handle focusGained events. */
    public void focusGained(FocusEvent e) {
        Object obj = e.getComponent();
        if (obj instanceof JTextField) {
            JTextField text = (JTextField) obj;
            text.setBackground(Color.LIGHT_GRAY);
            currentText = text;
            currentKey = keyMap.get(text);
        }
    }

    /** Handle focusLost events. */
    public void focusLost(FocusEvent e) {
        Object obj = e.getComponent();
        if (obj instanceof JTextField) {
            JTextField text = (JTextField) obj;
            text.setBackground(Color.WHITE);
        }
    }

}
