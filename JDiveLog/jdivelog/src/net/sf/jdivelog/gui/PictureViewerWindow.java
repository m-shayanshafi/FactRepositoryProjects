/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: PictureViewerWindow.java
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

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.ImageUtil;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Picture;

public class PictureViewerWindow extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(PictureViewerWindow.class.getName());
    
    private static final long serialVersionUID = -995268193799821470L;

    private PictureCyclingThread thread;

    private ImageCanvas imageCanvas;

    private KeyListener keyListener;

    private Picture currentPicture;

    private MouseWheelListener mousewheelListener;

    private boolean displayTitle;

    private boolean displayDescription;

    public PictureViewerWindow(Window parent, Picture p) {
        super(parent, ModalityType.MODELESS);
        requestFocus();
        thread = null;
        displayTitle = false;
        displayDescription = false;
        initialize();
        setPicture(p);
        new MnemonicFactory(this);
    }

    public PictureViewerWindow(Window parent, ArrayList<Picture> pictures, JDiveLog logbook) {
        super(parent, ModalityType.MODELESS);
        requestFocus();
        thread = new PictureCyclingThread(this, pictures, logbook);
        displayTitle = logbook.getSlideshowSettings().isDisplayTitle();
        displayDescription = logbook.getSlideshowSettings().isDisplayDescription();
        initialize();
        thread.start();
    }
    
    public PictureViewerWindow(Window parent, PictureTableModel model, JDiveLog logbook) {
        super(parent, ModalityType.MODELESS);
        requestFocus();
        thread = new PictureCyclingThread(this, model, logbook);
        displayTitle = logbook.getSlideshowSettings().isDisplayTitle();
        displayDescription = logbook.getSlideshowSettings().isDisplayDescription();
        initialize();
        thread.start();
    }

    public void setPicture(Picture p) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Image img = Toolkit.getDefaultToolkit().getImage(p.getFilename());
        currentPicture = p;
        MediaTracker mediaTracker = new MediaTracker(this);
        mediaTracker.addImage(img, 0);
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "interrupted while loading picture", e);
        }
        getImageCanvas().setImage(img, p.getRotation(), displayTitle ? p.getName() : null, displayDescription ? p.getDescription() : null);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void initialize() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/logo.gif")));
        keyListener = new KeyListener() {
            public void keyPressed(KeyEvent event) {
            }

            public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    changeState(PictureCyclingThread.STATE_ABORTED);
                } else if (event.getKeyCode() == KeyEvent.VK_PAGE_DOWN || event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER
                        || event.getKeyCode() == KeyEvent.VK_DOWN) {
                    changeState(PictureCyclingThread.STATE_NEXT);
                } else if (event.getKeyCode() == KeyEvent.VK_PAGE_UP || event.getKeyCode() == KeyEvent.VK_UP) {
                    changeState(PictureCyclingThread.STATE_PREVIOUS);
                } else if (event.getKeyCode() == KeyEvent.VK_F5) {
                    changeState(PictureCyclingThread.STATE_PAUSING);
                    editName();
                    changeState(PictureCyclingThread.STATE_RUNNING);
                } else if (event.getKeyCode() == KeyEvent.VK_F6) {
                    changeState(PictureCyclingThread.STATE_PAUSING);
                    editDescription();
                    changeState(PictureCyclingThread.STATE_RUNNING);
                } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rotateRight();
                } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                    rotateLeft();
                }
            }

            public void keyTyped(KeyEvent event) {
            }
        };
        addKeyListener(keyListener);
        mousewheelListener = new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    changeState(PictureCyclingThread.STATE_NEXT);
                } else if (e.getWheelRotation() < 0) {
                    changeState(PictureCyclingThread.STATE_PREVIOUS);
                }
            }

        };
        addMouseWheelListener(mousewheelListener);
        setUndecorated(true);
        getContentPane().add(getImageCanvas());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = ge.getDefaultScreenDevice();
        dev.setFullScreenWindow(this);
    }

    private void editName() {
        Picture p = currentPicture;
        if (p != null) {

            // JDialog doesn't contains - setState(PictureCyclingThread.STATE_PAUSING);
            EditNameDialog enw = new EditNameDialog(p);
            enw.setVisible(true);
            setPicture(p);
         // JDialog doesn't contains - setState(PictureCyclingThread.STATE_RUNNING);
        }
    }

    private void editDescription() {
        Picture p = currentPicture;
        if (p != null) {
            // JDialog doesn't contains - setState(PictureCyclingThread.STATE_PAUSING);
            EditDescriptionDialog edw = new EditDescriptionDialog(p);
            edw.setVisible(true);
            setPicture(p);
         // JDialog doesn't contains - setState(PictureCyclingThread.STATE_RUNNING);
        }
    }

    private void rotateRight() {
        int rotation = currentPicture.getRotation() + 1;
        if (rotation > 3) {
            rotation = 0;
        }
        currentPicture.setRotation(rotation);
        setPicture(currentPicture);
    }

    private void rotateLeft() {
        int rotation = currentPicture.getRotation() - 1;
        if (rotation < 0) {
            rotation = 3;
        }
        currentPicture.setRotation(rotation);
        setPicture(currentPicture);
    }

    private ImageCanvas getImageCanvas() {
        if (imageCanvas == null) {
            imageCanvas = new ImageCanvas();
            imageCanvas.addKeyListener(keyListener);
            imageCanvas.addMouseWheelListener(mousewheelListener);
        }
        return imageCanvas;
    }

    private void changeState(int runState) {
        if (thread != null) {
            thread.setRunState(runState);
        } else {
            dispose();
        }
    }

    //
    // inner classes
    //

    private static class PictureCyclingThread extends Thread {

        public static final int STATE_RUNNING = 0;

        public static final int STATE_PAUSING = 1;

        public static final int STATE_ABORTED = 2;

        public static final int STATE_NEXT = 3;

        public static final int STATE_PREVIOUS = 4;

        private long cycletime;

        private boolean repeat;
        
        private PictureTableModel pictureTableModel;

        private ArrayList<Picture> pictures;

        private int pictureIdx;

        private int runState;

        private PictureViewerWindow window;

        public PictureCyclingThread(PictureViewerWindow window, ArrayList<Picture> pictures, JDiveLog logbook) {
            this.window = window;
            this.pictures = pictures;
            pictureTableModel = null;
            this.runState = STATE_RUNNING;
            cycletime = logbook.getSlideshowSettings().getCycletime() * 1000;
            repeat = logbook.getSlideshowSettings().isRepeat();
        }

        public PictureCyclingThread(PictureViewerWindow window, PictureTableModel pictureTableModel, JDiveLog logbook) {
            this.window = window;
            this.pictures = pictureTableModel.getPictures();
            this.pictureTableModel = pictureTableModel;
            this.runState = STATE_RUNNING;
            cycletime = logbook.getSlideshowSettings().getCycletime() * 1000;
            repeat = logbook.getSlideshowSettings().isRepeat();
        }

        @Override
        public void run() {
            pictureIdx = 0;
            if (pictures.size() > 0) {
                window.setPicture(pictures.get(pictureIdx));
                while (runState != STATE_ABORTED) {
                    window.requestFocus();
                    try {
                        if (runState == STATE_RUNNING) {
                            Thread.sleep(cycletime);
                            next();
                        } else if (runState == STATE_PAUSING) {
                            Thread.sleep(cycletime);
                        } else if (runState == STATE_NEXT) {
                            runState = STATE_RUNNING;
                            next();
                        } else if (runState == STATE_PREVIOUS) {
                            runState = STATE_RUNNING;
                            previous();
                        }
                    } catch (InterruptedException e) {

                    }
                }
            }
            if (pictureTableModel != null) {
                pictureTableModel.reloadThumbs(true);
                pictureTableModel.fireTableDataChanged();
            }
            window.dispose();
        }

        public void setRunState(int runState) {
            this.runState = runState;
            interrupt();
        }

        private void next() {
            pictureIdx = (pictureIdx + 1) % pictures.size();
            if (pictureIdx == 0 && !repeat) {
                runState = STATE_ABORTED;
            } else {
                window.setPicture(pictures.get(pictureIdx));
            }
        }

        private void previous() {
            pictureIdx = (pictureIdx - 1) % pictures.size();
            if (pictureIdx < 0) {
                pictureIdx = pictures.size() - 1;
            }
            window.setPicture(pictures.get(pictureIdx));
        }
    }

    private static class ImageCanvas extends Canvas {

        private static final long serialVersionUID = -507771455667791226L;

        private int rotation;

        private Image image;

        private BufferedImage doubleBuffer;

        public ImageCanvas() {
            super();
        }

        public void setImage(Image img, int rotation) {
            setImage(img, rotation, null, null);
        }

        public void setImage(Image img, int rotation, String title, String description) {
            image = img;
            this.rotation = rotation;
            Dimension d = getPreferredSize();
            int sizeX = (int) d.getWidth();
            int sizeY = (int) d.getHeight();
            doubleBuffer = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_RGB);
            ImageUtil.transform(image, doubleBuffer, sizeX, sizeY, this.rotation, this);
            if (title != null) {
                Graphics g = doubleBuffer.getGraphics();
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(title, 5, 5 + fm.getHeight());
            }
            if (description != null) {
                Graphics g = doubleBuffer.getGraphics();
                FontMetrics fm = g.getFontMetrics();
                int spacing = fm.getHeight() + 5;
                StringTokenizer st = new StringTokenizer(description, "\n");
                int c = st.countTokens();
                for (int i = c - 1; i >= 0; i--) {
                    String line = st.nextToken();
                    g.drawString(line, 5, sizeY - 5 - (i * spacing));
                }
            }
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            if (doubleBuffer != null) {
                g.drawImage(doubleBuffer, 0, 0, this);
            }
        }

    }

    private static class EditNameDialog extends JDialog implements ActionListener {

        private static final long serialVersionUID = 636789872591258307L;

        private Picture picture;

        private JTextField titleField;

        private JPanel buttonPanel;

        private JButton closeButton;

        private JButton cancelButton;

        private JPanel fieldPanel;

        public EditNameDialog(Picture p) {
            picture = p;
            initialize();
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == closeButton) {
                picture.setName(getTitleField().getText());
                dispose();
            } else if (e.getSource() == cancelButton) {
                dispose();
            }
        }

        private void initialize() {
            setTitle(Messages.getString("edit_name"));
            setModal(true);
            setSize(300, 200);
            Container pane = getContentPane();
            pane.setLayout(new BorderLayout());
            pane.add(getFieldPanel(), BorderLayout.CENTER);
            pane.add(getButtonPanel(), BorderLayout.SOUTH);
            getTitleField().setText(picture.getName());
        }

        private JPanel getFieldPanel() {
            if (fieldPanel == null) {
                fieldPanel = new JPanel();
                fieldPanel.add(getTitleField());
            }
            return fieldPanel;
        }

        private JTextField getTitleField() {
            if (titleField == null) {
                titleField = new JTextField();
                titleField.setColumns(20);
            }
            return titleField;
        }

        private JPanel getButtonPanel() {
            if (buttonPanel == null) {
                GridBagConstraints gc = new GridBagConstraints();
                gc.fill = GridBagConstraints.HORIZONTAL;
                gc.weightx = 0.5;
                buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridBagLayout());
                buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
                gc.insets = new java.awt.Insets(5, 5, 5, 5);
                gc.gridx = 1;
                gc.gridy = 0;
                gc.insets = new java.awt.Insets(5, 5, 5, 5);
                buttonPanel.add(getCloseButton(), gc);
                gc.gridx = 2;
                gc.gridy = 0;
                buttonPanel.add(getCancelButton(), gc);
            }
            return buttonPanel;
        }

        private JButton getCloseButton() {
            if (closeButton == null) {
                closeButton = new JButton();
                closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
                closeButton.addActionListener(this);
            }
            return closeButton;
        }

        private JButton getCancelButton() {
            if (cancelButton == null) {
                cancelButton = new JButton();
                cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
                cancelButton.addActionListener(this);
            }
            return cancelButton;
        }

        public void setVisible(boolean value) {
            super.setVisible(value);
            if (value) {
                startModal();
            } else {
                stopModal();
            }
        }

        private synchronized void startModal() {
            try {
                if (SwingUtilities.isEventDispatchThread()) {
                    EventQueue theQueue = getToolkit().getSystemEventQueue();
                    while (isVisible()) {
                        AWTEvent event = theQueue.getNextEvent();
                        Object source = event.getSource();
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        } else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(event);
                        } else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(event);
                        } else {
                            LOGGER.severe("Unable to dispatch: " + event);
                        }
                    }
                } else {
                    while (isVisible()) {
                        wait();
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }

        private synchronized void stopModal() {
            notifyAll();
        }
    }

    private static class EditDescriptionDialog extends JDialog implements ActionListener {

        private static final long serialVersionUID = 636789872591258307L;

        private Picture picture;

        private JTextArea descriptionField;

        private JPanel buttonPanel;

        private JButton closeButton;

        private JButton cancelButton;

        private JPanel fieldPanel;

        public EditDescriptionDialog(Picture p) {
            picture = p;
            initialize();
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == closeButton) {
                picture.setDescription(getDescriptionField().getText());
                dispose();
            } else if (e.getSource() == cancelButton) {
                dispose();
            }
        }

        private void initialize() {
            setTitle(Messages.getString("edit_description"));
            setModal(true);
            setSize(300, 200);
            Container pane = getContentPane();
            pane.setLayout(new BorderLayout());
            pane.add(getFieldPanel(), BorderLayout.CENTER);
            pane.add(getButtonPanel(), BorderLayout.SOUTH);
            getDescriptionField().setText(picture.getDescription());
        }

        private JPanel getFieldPanel() {
            if (fieldPanel == null) {
                fieldPanel = new JPanel();
                fieldPanel.add(getDescriptionField());
            }
            return fieldPanel;
        }

        private JTextArea getDescriptionField() {
            if (descriptionField == null) {
                descriptionField = new JTextArea(6, 20);
                descriptionField.setColumns(20);
            }
            return descriptionField;
        }

        private JPanel getButtonPanel() {
            if (buttonPanel == null) {
                GridBagConstraints gc = new GridBagConstraints();
                gc.fill = GridBagConstraints.HORIZONTAL;
                gc.weightx = 0.5;
                buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridBagLayout());
                buttonPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
                gc.insets = new java.awt.Insets(5, 5, 5, 5);
                gc.gridx = 1;
                gc.gridy = 0;
                gc.insets = new java.awt.Insets(5, 5, 5, 5);
                buttonPanel.add(getCloseButton(), gc);
                gc.gridx = 2;
                gc.gridy = 0;
                buttonPanel.add(getCancelButton(), gc);
            }
            return buttonPanel;
        }

        private JButton getCloseButton() {
            if (closeButton == null) {
                closeButton = new JButton();
                closeButton.setText(Messages.getString("close")); //$NON-NLS-1$
                closeButton.addActionListener(this);
            }
            return closeButton;
        }

        private JButton getCancelButton() {
            if (cancelButton == null) {
                cancelButton = new JButton();
                cancelButton.setText(Messages.getString("cancel")); //$NON-NLS-1$
                cancelButton.addActionListener(this);
            }
            return cancelButton;
        }

        public void setVisible(boolean value) {
            super.setVisible(value);
            if (value) {
                startModal();
            } else {
                stopModal();
            }
        }

        private synchronized void startModal() {
            try {
                if (SwingUtilities.isEventDispatchThread()) {
                    EventQueue theQueue = getToolkit().getSystemEventQueue();
                    while (isVisible()) {
                        AWTEvent event = theQueue.getNextEvent();
                        Object source = event.getSource();
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        } else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(event);
                        } else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(event);
                        } else {
                            LOGGER.severe("Unable to dispatch: " + event);
                        }
                    }
                } else {
                    while (isVisible()) {
                        wait();
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }

        private synchronized void stopModal() {
            notifyAll();
        }
    }

}
