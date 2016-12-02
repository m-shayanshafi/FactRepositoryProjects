package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dogadikbayir on 22/12/14.
 */
public class SettingsPanel extends SubMenu {
    int genderSelect = 0;
    int music = 0;
    int ambientSelect = 0;
    int gameModeSelect = 0;
    //JPanel genderPanel;
    private ButtonGroup genderGrp;
    private JRadioButton genderMale, genderFemale;

    private ButtonGroup ambientGrp;
    private JRadioButton night, day;

    private ButtonGroup musicGrp;
    private JRadioButton musicOn, musicOff;

    private ButtonGroup modGrp;
    private JRadioButton normal, hardcore, god;

    private JLabel  gender, ambient, gameMode, musicSettings;
    public SettingsPanel(GameFrame frame)
    {
        super(frame);

        //Title
        title.setText("Settings");

        //Creating and placing labels
        gender = new JLabel("Gender");
        gender.setBounds(290,108,196,23);
        gender.setFont(new Font("Arial", 1, 15));

        ambient = new JLabel("Ambient");
        ambient.setBounds(290,168,196,23);
        ambient.setFont(new Font("Arial", 1, 15));

        musicSettings = new JLabel("Music");
        musicSettings.setBounds(290, 228,196,23);
        musicSettings.setFont(new Font("Arial", 1, 15));

        gameMode = new JLabel("Game Mode");
        gameMode.setBounds(270,288,196,23);
        gameMode.setFont(new Font("Arial", 1, 15));

        //Creating and setting buttons
        genderMale = new JRadioButton("Male");
        genderMale.setBounds(245, 128, 80, 20);
        genderMale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genderSelect = 1;
            }
        });

        genderFemale = new JRadioButton("Female");
        genderFemale.setBounds(320, 128, 100, 20);
        genderFemale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genderSelect = 0;
            }
        });

        night = new JRadioButton("Night");
        night.setBounds(245, 188, 80, 20);
        night.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ambientSelect = 1;
            }
        });

        day = new JRadioButton("Day");
        day.setBounds(320, 188, 80, 20);
        day.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ambientSelect = 0;
            }
        });

        musicOn = new JRadioButton("On");
        musicOn.setBounds(245, 248, 80, 20);
        musicOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music = 1;
            }
        });

        musicOff = new JRadioButton("Off");
        musicOff.setBounds(320, 248, 80, 20);
        musicOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music = 0;
            }
        });

        normal = new JRadioButton("Normal Mode");
        normal.setBounds(280, 308, 120, 20);
        normal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModeSelect = 0;
            }
        });

        hardcore = new JRadioButton("Hardcore Mode");
        hardcore.setBounds(280, 328, 140, 20);
        hardcore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModeSelect = 1;
            }
        });

        god = new JRadioButton("God Mode");
        god.setBounds(280, 348, 100, 20);
        god.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModeSelect = 2;
            }
        });

        genderGrp = new ButtonGroup();
        genderGrp.add(genderMale);
        genderGrp.add(genderFemale);

        ambientGrp = new ButtonGroup();
        ambientGrp.add(night);
        ambientGrp.add(day);

        musicGrp = new ButtonGroup();
        musicGrp.add(musicOn);
        musicGrp.add(musicOff);

        modGrp = new ButtonGroup();
        modGrp.add(normal);
        modGrp.add(hardcore);
        modGrp.add(god);

        add(gender);
        add(genderMale);
        add(genderFemale);
        add(ambient);
        add(day);
        add(night);
        add(musicSettings);
        add(musicOn);
        add(musicOff);
        add(gameMode);
        add(normal);
        add(hardcore);
        add(god);
    }
}
