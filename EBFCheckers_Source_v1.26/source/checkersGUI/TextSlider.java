package checkersGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A JComponent that combines and synchronises a JFormattedTextField and
 * JSlider.
 * 
 * @author Amos Yuen
 * @version 1.00 - 7 July 2008
 */

@SuppressWarnings("serial")
public class TextSlider extends JComponent implements ActionListener,
		ChangeListener, FocusListener {
	private JSlider slider;
	private JFormattedTextField textField;

	public TextSlider(int min, int max, int init) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 2;
		c.weighty = 1;
		init = Math.max(min, Math.min(max, init));
		textField = new JFormattedTextField(NumberFormat.getNumberInstance());
		textField.addActionListener(this);
		textField.addFocusListener(this);
		textField.setValue(init);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBorder(null);
		add(textField, c);

		c.gridx = 1;
		c.weightx = 7;
		slider = new JSlider(min, max, init);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setOpaque(false);
		slider.addChangeListener(this);
		add(slider, c);
	}

	public void actionPerformed(ActionEvent e) {
		updateTextField();
	}

	public void addChangeListener(ChangeListener listener) {
		slider.addChangeListener(listener);
	}

	@Override
	public synchronized void addKeyListener(KeyListener listener) {
		super.addKeyListener(listener);
		textField.addKeyListener(listener);
		slider.addKeyListener(listener);
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		updateTextField();
	}

	public int getMajorTickSpacing() {
		return slider.getMajorTickSpacing();
	}

	public int getMaximum() {
		return slider.getMaximum();
	}

	public int getMinimum() {
		return slider.getMinimum();
	}

	public int getMinorTickSpacing() {
		return slider.getMinorTickSpacing();
	}

	public JSlider getSlider() {
		return slider;
	}

	public JFormattedTextField getTextField() {
		return textField;
	}

	public int getValue() {
		return slider.getValue();
	}

	@Override
	public synchronized void removeKeyListener(KeyListener listener) {
		super.removeKeyListener(listener);
		textField.removeKeyListener(listener);
		slider.removeKeyListener(listener);
	}

	public void setMajorTickSpacing(int n) {
		slider.setMajorTickSpacing(n);
	}

	public void setMaximum(int max) {
		slider.setMaximum(max);
	}

	public void setMinimum(int min) {
		slider.setMinimum(min);
	}

	public void setMinorTickSpacing(int n) {
		slider.setMinorTickSpacing(n);
	}

	public void setValue(int n) {
		slider.setValue(n);
	}

	public void stateChanged(ChangeEvent e) {
		textField.setValue(slider.getValue());
	}

	public void updateTextField() {
		try {
			textField.commitEdit();
		} catch (ParseException e) {
		}
		Object o = textField.getValue();
		long value;
		if (o instanceof Integer)
			value = (Integer) o;
		else
			value = (Long) o;
		value = Math.max(slider.getMinimum(), Math.min(slider.getMaximum(),
				value));
		textField.setValue(value);
		slider.setValue((int) value);
	}
}