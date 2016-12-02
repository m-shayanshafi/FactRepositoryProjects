/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: GasField.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.Mix;
import net.sf.jdivelog.model.MixDatabase;
import net.sf.jdivelog.util.UnitConverter;

public class MixField extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String GAS_REGEX = "^([a-zA-Z]*)?[ ]*([0-9]{1,3})?([ ]*/[ ]*([0-9]{1,2}))?$";

    private JTextField textField;

    private JButton editButton;

    private String lastName;

    private double ppO2;

    int oxygen;

    int helium;

    int mod;

    int end;

    private double change;

    private String errorText;

    private Color textFieldBackground;

    private final JFrame parentFrame;

    private final Window parentWindow;

    private final MixDatabase db;

    private JPopupMenu gasPopupMenu;

    public MixField(JFrame parent, MixDatabase db) {
        this.db = db;
        parentFrame = parent;
        parentWindow = null;
        init();
    }

    public MixField(Window parent, MixDatabase db) {
        this.db = db;
        parentFrame = null;
        parentWindow = parent;
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        ppO2 = 1.6;
        gc.gridy = 0;
        gc.gridx = 0;
        add(getTextField(), gc);
        gc.gridx = 1;
        add(getEditButton(), gc);
        setToolTipText("");
        setMix(null);
    }

    private JTextField getTextField() {
        if (textField == null) {
            textField = new JTextField(15);
            Dimension d = new Dimension(130, 20);
            textField.setPreferredSize(d);
            textField.setMinimumSize(d);
            textField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent e) {
                    tfChanged();
                }

            });
            textField.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    textField.selectAll();
                }

                public void focusLost(FocusEvent e) {
                    tfChanged();
                }

            });
            textFieldBackground = textField.getBackground();

            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        int dotPosition = textField.getCaretPosition();
                        Rectangle popupLocation = textField.modelToView(dotPosition);
                        getMixPopupMenu().show(textField, popupLocation.x, popupLocation.y);
                    } catch (BadLocationException badLocationException) {
                        System.out.println("Oops");
                    }
                }
            };
            KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, Event.CTRL_MASK, false);
            textField.registerKeyboardAction(actionListener, keystroke, JComponent.WHEN_FOCUSED);
            textField.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        getMixPopupMenu().show(textField, e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        getMixPopupMenu().show(textField, e.getX(), e.getY());
                    }
                }
            });
        }
        return textField;
    }

    private void tfChanged() {
        String nt = getTextField().getText();
        if (nt != null && !nt.equals(textField)) {
            parseText();
            lastName = nt;
        }
        updateToolTipText();
    }

    private void parseText() {
        clearError();
        String s = getTextField().getText();
        Pattern p = Pattern.compile(GAS_REGEX);
        Matcher m = p.matcher(s);
        if (m.find()) {
            String a = m.group(1);
            String b = m.group(2);
            String c = m.group(4);
            if (b == null) {
                if (Messages.getString("air").equalsIgnoreCase(a)) {
                    oxygen = 21;
                    helium = 0;
                } else if (Messages.getString("oxygen").equalsIgnoreCase(a)) {
                    oxygen = 100;
                    helium = 0;
                } else {
                    setError();
                    oxygen = 0;
                    helium = 0;
                }
            } else {
                if (b != null && !"".equals(b)) {
                    oxygen = Integer.parseInt(b);
                } else {
                    oxygen = 0;
                }
                if (c != null && !"".equals(c)) {
                    helium = Integer.parseInt(c);
                } else {
                    helium = 0;
                }
            }
        } else {
            setError();
        }
        int nitrogen = 100 - oxygen - helium;
        if (nitrogen < 0) {
            setError();
        } else {
            if (ppO2 == 0) {
                ppO2 = 1.6;
            }
            mod = Mix.calcMOD(oxygen, ppO2);
            end = MixEditWindow.calcEND(mod, nitrogen);
            change = Mix.calcChangeDepth(mod, new BigDecimal("1.5"));
        }
    }

    private void setError() {
        errorText = Messages.getString("gas.invalid");
        getTextField().setBackground(new Color(255, 127, 127));
    }

    private void clearError() {
        errorText = null;
        getTextField().setBackground(textFieldBackground);
    }

    public void updateToolTipText() {
        String tt = null;
        if (errorText != null) {
            tt = errorText;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("<html>");
            sb.append("<body>");
            sb.append("<b>");
            sb.append(lastName);
            sb.append("</b><br/>");
            sb.append(Messages.getString("oxygen_percent"));
            sb.append(": ");
            sb.append(oxygen);
            sb.append("%<br/>");
            sb.append(Messages.getString("helium_percent"));
            sb.append(": ");
            sb.append(helium);
            sb.append("%<br/>");
            sb.append(Messages.getString("nitrogen_percent"));
            sb.append(": ");
            sb.append(100 - oxygen - helium);
            sb.append("%<br/>");
            sb.append("MOD: ");
            sb.append(mod);
            sb.append(UnitConverter.getDisplayAltitudeUnit());
            sb.append(" @ ppO2: ");
            sb.append(ppO2);
            if (isTrimix()) {
                sb.append("<br/>END @ ");
                sb.append(mod);
                sb.append(UnitConverter.getDisplayAltitudeUnit());
                sb.append(": ");
                sb.append(end);
                sb.append(UnitConverter.getDisplayAltitudeUnit());
            }
            sb.append("</body>");
            sb.append("</html>");
            tt = sb.toString();
        }
        setToolTipText(tt);
        getTextField().setToolTipText(tt);
    }

    protected boolean isTrimix() {
        return helium > 0;
    }

    public boolean isGasValid() {
        return errorText == null && oxygen + helium <= 100;
    }

    private JButton getEditButton() {
        if (editButton == null) {
            Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/edit.png"));
            editButton = new JButton(new ImageIcon(img));
            editButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    edit();
                }

            });
        }
        return editButton;
    }

    private JPopupMenu getMixPopupMenu() {
        if (gasPopupMenu == null) {
            gasPopupMenu = new JPopupMenu();
            gasPopupMenu.addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuCanceled(PopupMenuEvent e) {
                    // TODO Auto-generated method stub

                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    // TODO Auto-generated method stub

                }

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    rebuildGasPopupMenu();
                }
            });
        }
        return gasPopupMenu;
    }

    private void rebuildGasPopupMenu() {
        getMixPopupMenu().removeAll();
        for (Mix favorite : db.getFavorites()) {
            getMixPopupMenu().add(new MixMenuItem(favorite));
        }
    }

    private void edit() {
        if (parentFrame != null) {
            Mix m = MixEditWindow.edit(parentFrame, db, getMix());
            if (m != null) {
                setMix(m);
            }
        } else if (parentWindow != null) {
            Mix m = MixEditWindow.edit(parentWindow, db, getMix());
            if (m != null) {
                setMix(m);
            }
        }
    }

    public void setMix(Mix mix) {
        if (mix == null) {
            mix = Mix.AIR;
        }
        oxygen = mix.getOxygen();
        helium = mix.getHelium();
        ppO2 = mix.getPpO2();
        mod = mix.getMod();
        change = mix.getChange();
        getTextField().setText(mix.getName());
    }

    public Mix getMix() {
        Mix m = null;
        if (isGasValid()) {
            m = new Mix(getTextField().getText(), oxygen, helium, ppO2, mod, change);
        }
        return m;
    }

    private class MixMenuItem extends JMenuItem {
        private static final long serialVersionUID = 1L;
        private final Mix mix;

        public MixMenuItem(Mix mix) {
            super(mix.getName());
            this.mix = mix;
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setMix(MixMenuItem.this.mix);
                }
            });
        }
    }

}
