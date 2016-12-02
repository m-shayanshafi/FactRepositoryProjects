/*
 * GameVisualisation.java
 *
 * Created on March 11, 2007, 2:19 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package shoddybattleclient;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.color.*;
import java.io.IOException;
import shoddybattle.*;
import mechanics.PokemonType;
import java.util.*;
import netbattle.messages.UpdatePokemonStatusMessage;

/**
 *
 * @author Colin
 */
public class GameVisualisation extends JPanel {
    
    private static final Image m_background;
    private static final Image[] m_pokeball = new Image[3];
    private static final IndexColorModel m_colors;
    private Image[] m_img = new Image[2];
    
    private int[][] m_state = new int[2][6];
    private int[][] m_gender = new int[2][6];
    private String[][] m_pokemon = new String[2][6];
    private ArrayList[][] m_statuses = new ArrayList[2][6];
    private int[] m_active = new int[2];
    
    private int m_participant = 0;
    private Graphics2D m_mouseInput;
    private ModData m_data;
    
    private static Image getImageFromResources(String file) {
        return Toolkit
            .getDefaultToolkit()
            .createImage(
                GameVisualisation.class.getResource("resources/" + file));
    }
    
    static {
        m_background = getImageFromResources("grass.jpg");
        m_pokeball[0] = getImageFromResources("pokeball.png");
        m_pokeball[1] = getImageFromResources("pokeball2.png");
        m_pokeball[2] = getImageFromResources("pokeball3.png");
        
        byte[] r = new byte[13];
        byte[] g = new byte[13];
        byte[] b = new byte[13];
        for (byte i = 0; i < 2; ++i) {
            for (byte j = 0; j < 6; ++j) {
                int k = i * 6 + j;
                r[k] = i;
                g[k] = j;
            }
        }
        r[12] = g[12] = b[12] = (byte)255;
        m_colors = new IndexColorModel(4, r.length, r, g, b);
        
        ToolTipManager manager = ToolTipManager.sharedInstance();
        manager.setInitialDelay(0);
        manager.setReshowDelay(0);
    }
    
    /** Creates a new instance of GameVisualisation */
    public GameVisualisation(ModData data, int participant) {
        m_data = data;
        m_participant = participant;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(m_background, 0);
        tracker.addImage(m_pokeball[0], 1);
        tracker.addImage(m_pokeball[1], 2);
        tracker.addImage(m_pokeball[2], 3);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Create a canvas the same size as the visualisation where colours
        // correspond to the action to take upon hovering over that location.
        Dimension d = getPreferredSize();
        final BufferedImage image = new BufferedImage((int)d.getWidth(),
                (int)d.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY,
                m_colors);
        m_mouseInput = image.createGraphics();
        
        for (int i = 0; i < m_statuses.length; ++i) {
            ArrayList[] lists = m_statuses[i];
            for (int j = 0; j < lists.length; ++j) {
                lists[j] = new ArrayList();
            }
        }
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if ((x > image.getWidth()) || (y > image.getHeight())) {
                    return;
                }
                int rgb = image.getRGB(x, y);
                Color c = new Color(rgb);
                if (c.getBlue() == 0) {
                    displayInformation(c.getRed(), c.getGreen());
                } else {
                    setToolTipText(null);
                }
            }
        });
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(m_background.getWidth(this), m_background.getHeight(this));
    }
    
    /**
     * Display the information the client knows about a particular pokemon.
     */
    public void displayInformation(int party, int idx) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        String name = m_pokemon[party][idx];
        if (name == null) {
            buffer.append("???");
        } else {
            buffer.append(name);
            int g = m_gender[party][idx];
            if (g != 0) {
                buffer.append(' ');
                buffer.append((g == PokemonSpecies.GENDER_MALE) ? '♂' : '♀');
            }
            PokemonSpeciesData data = m_data.getSpeciesData();
            int i = data.getPokemonByName(name);
            if (i != -1) {
                PokemonSpecies species = data.getSpecies(i);
                PokemonType[] types = species.getTypes();
                i = 0;
                buffer.append("<br>");
                do {
                    buffer.append(types[i]);
                    if (++i < types.length) {
                        buffer.append(" / ");
                    } else {
                        break;
                    }
                } while (true);
            }
        }
        ArrayList list = m_statuses[party][idx];
        if (list.size() != 0) {
            buffer.append("<br><br>");
            Iterator i = list.iterator();
            do {
                String item = (String)i.next();
                buffer.append("- ");
                buffer.append(item);
                if (i.hasNext()) {
                    buffer.append("<br>");
                } else {
                    break;
                }
            } while (true);
        }
        buffer.append("</html>");
        String tooltip = new String(buffer);
        boolean reshow = !tooltip.equals(getToolTipText());
        setToolTipText(tooltip);
        if (reshow) {
            ToolTipManager manager = ToolTipManager.sharedInstance();
            manager.setEnabled(false);
            manager.setEnabled(true);
        }
    }
    
    private void drawParty(Graphics g, int party, int x, int y) {
        int width = m_pokeball[0].getWidth(this);
        int height = m_pokeball[0].getHeight(this);
        int[] states = m_state[party];
        for (int i = 0; i < 3; ++i) {
            int xpos = x + i * (width + 5);
            m_mouseInput.setColor(new Color(party, i, 0));
            m_mouseInput.fillRect(xpos, y, width, height);
            g.drawImage(m_pokeball[states[i]], xpos, y, this);
            
            int ypos = y + height + 2;
            m_mouseInput.setColor(new Color(party, 3 + i, 0));
            m_mouseInput.fillRect(xpos, ypos, width, height);
            g.drawImage(m_pokeball[states[3 + i]], xpos, ypos, this);
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int w = getWidth();
        int h = getHeight();
        
        m_mouseInput.setColor(Color.WHITE);
        m_mouseInput.fillRect(0, 0, w, h);
        
        int opponent = (m_participant == 0) ? 1 : 0;
        
        if (m_background != null) {
            g.drawImage(m_background, 0, 0, w, h, this);
        }
        if (m_img[0] != null) {
            // First spot ends at x = 120.
            int width = m_img[0].getWidth(this);
            int height = m_img[0].getHeight(this);
            int x = (120 - width) / 2;
            int y = h - height;
            g.drawImage(m_img[0], x, y, this);
            m_mouseInput.setColor(new Color(m_participant, m_active[m_participant], 0));
            m_mouseInput.fillRect(x, y, width, height);
        }
        if (m_img[1] != null) {
            // 78
            int width = m_img[1].getWidth(this);
            int height = m_img[1].getHeight(this);
            int x = w -  width - (120 - width) / 2;
            int y = 78 - height;
            g.drawImage(m_img[1], x, y, this);
            m_mouseInput.setColor(new Color(opponent, m_active[opponent], 0));
            m_mouseInput.fillRect(x, y, width, height);
        }
        
        drawParty(g, opponent, 2, 2);
        drawParty(g, m_participant,
                w - (m_pokeball[0].getWidth(this) + 5) * 3,
                h - (m_pokeball[0].getHeight(this) + 2) * 2);
    }
    
    /**
     * Set the status of a pokemon.
     */
    public void setStatus(int party, int idx, int status) {
        if (m_state[party][idx] != UpdatePokemonStatusMessage.STATUS_FAINTED) {
            m_state[party][idx] = status;
        }
    }
    
    /**
     * Toggle whether a pokemon has a particular status.
     */
    public void toggleStatus(int party, int idx, String status) {
        ArrayList list = m_statuses[party][idx];
        if (list.contains(status)) {
            list.remove(status);
        } else {
            list.add(status);
        }
    }
    
    /**
     * Set the whole status array.
     */
    public void setStatus(int[][] status, String[][][] statuses) {
        m_state = status;
        for (int i = 0; i < m_statuses.length; ++i) {
            ArrayList[] lists = m_statuses[i];
            for (int j = 0; j < lists.length; ++j) {
                lists[j] = new ArrayList(Arrays.asList(statuses[i][j]));
            }
        }
    }
    
    /**
     * Set the pokemon to be displayed.
     */
    public void setPokemon(
            int id1, String user, int userGender, boolean userShiny,
            int id2, String opponent, int opponentGender, boolean opponentShiny) {
        
        try {
            m_img[0] = SpriteLoader.getImage(user,
                    false,
                    userGender != PokemonSpecies.GENDER_FEMALE,
                    userShiny);
            m_img[1] = SpriteLoader.getImage(opponent,
                    true,
                    opponentGender != PokemonSpecies.GENDER_FEMALE,
                    opponentShiny);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int opp = (m_participant == 0) ? 1 : 0;
        
        m_active[m_participant] = id1;
        m_active[opp] = id2;
        m_pokemon[m_participant][id1] = user;
        m_pokemon[opp][id2] = opponent;
        m_gender[m_participant][id1] = userGender;
        m_gender[opp][id2] = opponentGender;
        
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(m_img[0], 0);
        tracker.addImage(m_img[1], 1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        PokemonSpeciesData data = PokemonSpecies.getDefaultData();
        try {
            SpriteLoader.setSpriteLoader("dpsprites.PokemonSprite");
            data.loadSpeciesDatabase(WelcomeWindow.class.getResourceAsStream(
                        "resources/species.db"), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        final String[] pokemon = data.getSpeciesNames();
        
        final GameVisualisation visual = new GameVisualisation(ModData.getDefaultData(), 1);
        Dimension d = visual.getPreferredSize();
        visual.setSize(d);
        visual.toggleStatus(0, 0, "Attract");
        visual.toggleStatus(0, 0, "Confuse");
        visual.toggleStatus(1, 0, "Burn");
        visual.setPokemon(0, "Sceptile",
                PokemonSpecies.GENDER_FEMALE,
                false,
                0, "Shedinja",
                PokemonSpecies.GENDER_MALE,
                false);
        visual.setVisible(true);
        JFrame frame = new JFrame("GameVisualisation");
        frame.setSize((int)d.getWidth(), (int)d.getHeight() + 32);
        frame.getContentPane().add(visual);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        String[] arr = new String[2];
                        for (int i = 0; i < arr.length; ++i) {
                            arr[i] = pokemon[(int)Math.ceil(Math.random() * pokemon.length) - 1];
                        }
                        visual.setPokemon(0, arr[0], PokemonSpecies.GENDER_MALE, false,
                                0, arr[1], PokemonSpecies.GENDER_FEMALE, false);
                        visual.repaint();
                    } catch (Exception e) {
                        
                    }
                }
            }
        }/*.start()*/;
        
        frame.setVisible(true);
    }
    
}
