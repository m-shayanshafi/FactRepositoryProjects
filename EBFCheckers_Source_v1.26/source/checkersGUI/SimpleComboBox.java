package checkersGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * This is a simple ComboBox that was written to act similarly to the JComboBox,
 * but allow easy access and changing of the ComboBox's components' values,
 * styles, colors, etc.
 * 
 * @author Amos Yuen
 * @version 1.00 - 25 July 2008
 */

@SuppressWarnings("serial")
public class SimpleComboBox extends JComponent implements
		ListSelectionListener, MouseListener, MouseMotionListener, KeyListener,
		PopupMenuListener, FocusListener, ItemSelectable {

	private JButton button;
	private JTextField display;
	private Color focusBackground;
	private Color focusForeground;
	private List<ItemListener> itemListeners;
	private JList list;
	private JScrollPane listScrollPane;
	private DefaultListModel model;
	private Color normalBackground;
	private Color normalForeground;
	private JPopupMenu popup;
	private boolean popupCanceled;
	private Object selectedValue;

	public SimpleComboBox() {
		itemListeners = new LinkedList<ItemListener>();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		display = new JTextField();
		display.setEditable(false);
		display.setOpaque(true);
		display.setFocusable(true);
		display.setAutoscrolls(true);
		display.addMouseListener(this);
		display.addMouseMotionListener(this);
		display.addKeyListener(this);
		display.addFocusListener(this);
		normalForeground = display.getForeground();
		normalBackground = display.getBackground();
		display.setSelectedTextColor(normalForeground);
		display.setSelectionColor(normalBackground);
		display.setHorizontalAlignment(SwingConstants.CENTER);
		display.setBorder(null);
		display.setFont(display.getFont().deriveFont(Font.BOLD));
		add(display, c);

		c.weightx = 0;
		c.gridx++;
		button = new BasicArrowButton(SwingConstants.SOUTH);
		button.addMouseListener(this);
		button.addMouseMotionListener(this);
		button.setFocusable(false);
		add(button, c);

		model = new DefaultListModel();
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addKeyListener(this);
		list.addMouseListener(this);
		list.addMouseMotionListener(this);
		list.addListSelectionListener(this);
		list.setAutoscrolls(true);
		list.setBorder(null);
		list.setFocusTraversalKeysEnabled(false);
		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				return super.getListCellRendererComponent(list, value, index,
						true, false);
			}
		});
		listScrollPane = new JScrollPane(list);
		listScrollPane.setOpaque(false);
		listScrollPane.setBorder(null);

		popup = new JPopupMenu();
		popup.add(listScrollPane);
		popup.addPopupMenuListener(this);

		focusForeground = list.getSelectionForeground();
		focusBackground = list.getSelectionBackground();
	}

	public SimpleComboBox(Object[] elements) {
		this();
		addAll(elements);
	}

	public void add(int index, Object element) {
		model.add(index, element);
		if (model.getSize() == 1)
			setSelectedIndex(0);
	}

	public void addAll(Collection<Object> elements) {
		for (Object e : elements)
			addelement(e);
	}

	public void addAll(Object[] elements) {
		for (Object e : elements)
			addelement(e);
	}

	public void addelement(Object element) {
		model.addElement(element);
		if (model.getSize() == 1)
			setSelectedIndex(0);
	}

	@Override
	public void addItemListener(ItemListener listener) {
		itemListeners.add(listener);
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
		display.addKeyListener(l);
	}

	@Override
	public void focusGained(FocusEvent e) {
		popupCanceled = false;
		display.setForeground(focusForeground);
		display.setBackground(focusBackground);
		display.setSelectedTextColor(focusForeground);
		display.setSelectionColor(focusBackground);
	}

	@Override
	public void focusLost(FocusEvent e) {
		popupCanceled = false;
		display.setForeground(normalForeground);
		display.setBackground(normalBackground);
		display.setSelectedTextColor(normalForeground);
		display.setSelectionColor(normalBackground);
	}

	public JButton getButton() {
		return button;
	}

	public JTextField getDisplay() {
		return display;
	}

	public JList getList() {
		return list;
	}

	public JPopupMenu getPopup() {
		return popup;
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	@Override
	public Object[] getSelectedObjects() {
		return new Object[] { selectedValue };
	}

	public Object getSelectedValue() {
		return selectedValue;
	}

	public void hidePopup() {
		popup.setVisible(false);
		display.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == list) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				setSelectedValue(list.getSelectedValue());
				hidePopup();
			} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
				hidePopup();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			showPopup();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (list.isShowing()) {
			Point p = e.getLocationOnScreen();
			Point origin = list.getLocationOnScreen();
			p.translate(-origin.x, -origin.y);
			int index = list.locationToIndex(p);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = list.getMousePosition();
		if (list.isShowing() && p != null) {
			int index = list.locationToIndex(p);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() != list) {
			if (!popupCanceled) {
				if (!popup.isShowing())
					showPopup();
			} else
				popupCanceled = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (list.isShowing()) {
			if (list.getMousePosition() != null) {
				setSelectedValue(list.getSelectedValue());
				hidePopup();
			} else if (((JComponent) e.getSource()).getMousePosition() == null) {
				hidePopup();
			}
		}
	}

	private void notifyItemStateChanged(int id, int stateChange) {
		ItemEvent e = new ItemEvent(this, id, selectedValue, stateChange);
		for (ItemListener listener : itemListeners)
			listener.itemStateChanged(e);
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		popupCanceled = true;
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		if (list.getMousePosition() == null)
			list.setSelectedValue(selectedValue, true);
		display.requestFocusInWindow();
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	@Override
	public void removeItemListener(ItemListener listener) {
		itemListeners.remove(listener);
	}

	@Override
	public synchronized void removeKeyListener(KeyListener l) {
		super.removeKeyListener(l);
		display.removeKeyListener(l);
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		display.setBackground(bg);
		display.setSelectionColor(bg);
		normalBackground = bg;
		list.setBackground(bg);
	}

	public void setFocusBackground(Color bg) {
		focusBackground = bg;
		if (display.hasFocus()) {
			display.setBackground(bg);
			display.setSelectionColor(bg);
		}
	}

	public void setFocusForeground(Color fg) {
		focusForeground = fg;
		if (display.hasFocus()) {
			display.setForeground(fg);
			display.setSelectedTextColor(fg);
		}
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		display.setForeground(fg);
		display.setSelectedTextColor(fg);
		normalForeground = fg;
		list.setForeground(fg);
	}

	public void setLabelBackground(Color bg) {
		normalBackground = bg;
		if (!display.hasFocus()) {
			display.setBackground(bg);
			display.setSelectionColor(bg);
		}
	}

	public void setLabelForeground(Color fg) {
		normalForeground = fg;
		if (!display.hasFocus()) {
			display.setForeground(fg);
			display.setSelectedTextColor(fg);
		}
	}

	public void setSelectedIndex(int index) {
		list.setSelectedIndex(index);
		selectedValue = list.getSelectedValue();
		updateSelection();
	}

	public void setSelectedValue(Object e) {
		selectedValue = e;
		updateSelection();
	}

	public void showPopup() {
		popup
				.setPopupSize(getWidth(),
						listScrollPane.getPreferredSize().height);
		popup.show(this, 0, getHeight());
		list.requestFocus(true);
		list.setSelectedValue(selectedValue, true);
	}

	public void updateSelection() {
		display.setText(selectedValue.toString());
		list.setSelectedValue(selectedValue, true);
		notifyItemStateChanged(0, ItemEvent.SELECTED);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!popup.isShowing()) {
			selectedValue = list.getSelectedValue();
			updateSelection();
		}
	}

}
