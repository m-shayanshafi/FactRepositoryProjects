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
package pl.org.minions.utils.ui.synth;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

import pl.org.minions.stigma.client.Main;
import pl.org.minions.stigma.client.ui.swing.FullReapintManager;

/**
 * A class for testing synth configuration.
 */
public class SynthTestFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane;
    private JButton button;
    private JProgressBar progressBar;
    private JCheckBox checkBox;
    private JRadioButton radioButton;
    private JTextField textField;
    private JLabel label;
    private JSlider slider;

    //CHECKSTYLE:OFF

    /**
     * This method initializes button
     * @return javax.swing.JButton
     */
    private JButton getButton()
    {
        if (button == null)
        {
            button = new JButton();
            button.setText("Button");
        }
        return button;
    }

    /**
     * This method initializes progressBar
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getProgressBar()
    {
        if (progressBar == null)
        {
            progressBar = new JProgressBar();
            progressBar.setValue(50);
            progressBar.setString("Progress bar (75/100)");
            progressBar.setStringPainted(true);
        }
        return progressBar;
    }

    /**
     * This method initializes checkBox
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCheckBox()
    {
        if (checkBox == null)
        {
            checkBox = new JCheckBox();
            checkBox.setText("Check box");
        }
        return checkBox;
    }

    /**
     * This method initializes radioButton
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getRadioButton()
    {
        if (radioButton == null)
        {
            radioButton = new JRadioButton();
            radioButton.setText("Radio button");
        }
        return radioButton;
    }

    /**
     * This method initializes textField
     * @return javax.swing.JTextField
     */
    private JTextField getTextField()
    {
        if (textField == null)
        {
            textField = new JTextField();
            textField.setEditable(false);
            textField.setText("Text field");
        }
        return textField;
    }

    /**
     * This method initializes slider
     * @return javax.swing.JSlider
     */
    private JSlider getSlider()
    {
        if (slider == null)
        {
            slider = new JSlider();
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);
            slider.addChangeListener(new javax.swing.event.ChangeListener()
            {
                @Override
                public void stateChanged(javax.swing.event.ChangeEvent e)
                {
                    getProgressBar().setValue(slider.getValue());
                }
            });
        }
        return slider;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final SynthLookAndFeel synth = new SynthLookAndFeel();

        String synthConfigFileName =
                args.length == 0 ? "synth/client/synth_cfg.xml" : args[0];

        URL synthCfgURL =
                Main.class.getClassLoader().getResource(synthConfigFileName);
        try
        {
            if (synthCfgURL == null)
            {
                final File synthCfgFile = new File(synthConfigFileName);
                synthCfgURL = synthCfgFile.toURI().toURL();
            }

            synth.load(synthCfgURL);

            RepaintManager.setCurrentManager(new FullReapintManager());
            UIManager.setLookAndFeel(synth);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return;
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
            return;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                SynthTestFrame thisClass = new SynthTestFrame();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    /**
     * This is the default constructor
     */
    public SynthTestFrame()
    {
        super();
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize()
    {
        this.setSize(427, 382);
        this.setContentPane(getJContentPane());
        this.setTitle("SynthTestFrame");
    }

    /**
     * This method initializes jContentPane
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            label = new JLabel();
            label.setText("Label");
            jContentPane = new JPanel();
            jContentPane.setLayout(new FlowLayout());
            jContentPane.add(getButton(), null);
            jContentPane.add(getProgressBar(), null);
            jContentPane.add(getCheckBox(), null);
            jContentPane.add(getRadioButton(), null);
            jContentPane.add(getTextField(), null);
            jContentPane.add(label, null);
            jContentPane.add(getSlider(), null);
        }
        return jContentPane;
    }

    //CHECKSTYLE:ON
} //  @jve:decl-index=0:visual-constraint="10,10"
