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
package pl.org.minions.stigma.client.ui.swing.game.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.ui.event.listeners.CooldownChangedListener;
import pl.org.minions.stigma.globals.GlobalConfig;

/**
 * Component that displays the remaining cooldown as a
 * section of a rounded rectangle.
 * <p>
 * The component foreground and background colors are used
 * in painting. The radius of rounded corners is the maximum
 * of all insets of the component.
 */
public class CooldownComponent extends JPanel implements
                                             CooldownChangedListener
{
    /**
     *
     */
    private static final int HALF_TURN = 180;

    /**
     *
     */
    private static final int QUARTER_TURN = 90;

    private static final long serialVersionUID = 1L;

    private static final long REPAINT_INTERVAL = 20L;
    private static final long FLASH_DURATION = 500L;

    private long lastCooldownChangeTime = -1L;
    private int lastCooldownValue;
    private long lastCooldownDuration;
    private long turnDuration;

    private final float strokeWidth = 2;

    /**
     * Constructor.
     */
    public CooldownComponent()
    {
        setOpaque(false);
        setName("CooldownComponent");
        /*
         * setBackground(new ColorUIResource(Color.white));
         * setForeground(new ColorUIResource(Color.blue));
         */

        addAncestorListener(new AncestorListener()
        {

            @Override
            public void ancestorRemoved(AncestorEvent event)
            {
                Client.globalInstance()
                      .uiEventRegistry()
                      .removeCooldownChangedListener(CooldownComponent.this);
            }

            @Override
            public void ancestorMoved(AncestorEvent event)
            {
            }

            @Override
            public void ancestorAdded(AncestorEvent event)
            {
                Client.globalInstance()
                      .uiEventRegistry()
                      .addCooldownChangedListener(CooldownComponent.this);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g)
    {
        if (isOpaque())
            super.paintComponent(g);

        if (lastCooldownValue == 0)
            return;

        // Calculate elapsed time
        final long currentTime = System.currentTimeMillis();
        final long timeFromCooldownChange =
                currentTime - lastCooldownChangeTime;

        if (timeFromCooldownChange >= lastCooldownDuration)
        {
            lastCooldownValue = 0;
            return;
        }

        // Calculate interpolation parameters
        final double cooldownProgress =
                (double) timeFromCooldownChange / (double) lastCooldownDuration;

        final float flashIntensityCompl =
                timeFromCooldownChange < FLASH_DURATION ? (float) timeFromCooldownChange
                                                           / (float) FLASH_DURATION
                                                       : 1.f;

        final float flashIntensity = 1.f - flashIntensityCompl;

        // Calculate colors
        final int components =
                getForeground().getColorSpace().getNumComponents() + 1;
        float[] flashComponents = new float[components];
        float[] baseComponents = new float[components];
        float[] interpolatedComponents = new float[components];

        getForeground().getComponents(flashComponents);
        getBackground().getComponents(baseComponents);
        for (int i = 0; i < components; ++i)
        {
            interpolatedComponents[i] =
                    Math.min(Math.max(flashComponents[i] * flashIntensity
                                 + baseComponents[i] * flashIntensityCompl, 0.f),
                             1.f);
        }
        final Color interpolatedColor =
                new Color(getForeground().getColorSpace(),
                          interpolatedComponents,
                          interpolatedComponents[components - 1]);

        Graphics2D g2D = (Graphics2D) g.create();

        g2D.translate(strokeWidth / 2, strokeWidth / 2);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(getBackground());
        g2D.setStroke(new BasicStroke(strokeWidth,
                                      BasicStroke.CAP_ROUND,
                                      BasicStroke.JOIN_MITER));
        drawRectangularGaguge(g2D, cooldownProgress);

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        g2D.setColor(interpolatedColor);
        g2D.setStroke(new BasicStroke());
        drawRectangularGaguge(g2D, cooldownProgress);

        g2D.dispose();

        repaint(REPAINT_INTERVAL);
    }

    /** {@inheritDoc} */
    @Override
    protected void paintBorder(Graphics g)
    {
        //DO NOT WANT
    }

    /** {@inheritDoc} */
    @Override
    public void cooldownChanged(int cooldown)
    {
        lastCooldownChangeTime = System.currentTimeMillis();
        lastCooldownValue = cooldown;
        turnDuration = GlobalConfig.globalInstance().getMillisecondsPerTurn();
        lastCooldownDuration = lastCooldownValue * turnDuration;

        repaint();
    }

    @SuppressWarnings("unused")
    // Used for testing purposes.
    private void drawCircularGauge(Graphics2D g, double cooldownProgress)
    {
        //CHECKSTYLE:OFF
        final int hSize = getWidth() - (int) Math.ceil(strokeWidth) - 1;
        final int vSize = getHeight() - (int) Math.ceil(strokeWidth) - 1;

        g.drawArc(0, 0, hSize, vSize, QUARTER_TURN, -360
            + (int) (360 * cooldownProgress));
        //CHECKSTYLE:ON
    }

    private void drawRectangularGaguge(Graphics2D g, double cooldownProgress)
    {
        //CHECKSTYLE:OFF
        // math follows
        assert cooldownProgress <= 1.;
        assert cooldownProgress >= 0.;

        final double cooldownProgressCompl = 1. - cooldownProgress;

        final Insets insets = getInsets();
        final int arcSize =
                Math.max(Math.max(insets.left, insets.right),
                         Math.max(insets.top, insets.bottom));
        final int arcDiameter = 2 * arcSize;

        final int hSegmentLength =
                getWidth() - (int) Math.ceil(strokeWidth) - 1 - arcDiameter;
        final int vSegmentLength =
                getHeight() - (int) Math.ceil(strokeWidth) - 1 - arcDiameter;
        final int halfHSegmentLength = hSegmentLength / 2;

        final float arcLength = arcSize * 1.5f;
        final float length =
                2 * hSegmentLength + 2 * vSegmentLength + 4 * arcLength;

        final float lengthInv = 1 / length;

        final float hSegmentPart = hSegmentLength * lengthInv;
        final float halfHSegmentPart = halfHSegmentLength * lengthInv;
        final float arcPart = arcLength * lengthInv;
        final float vSegmentPart = vSegmentLength * lengthInv;
        //CHECKSTYLE:ON

        // Draw the rectangular shape
        float part = (float) cooldownProgressCompl;

        if (part <= halfHSegmentPart)
        {
            part *= halfHSegmentPart;
            g.drawLine(arcSize + halfHSegmentLength, 0, arcSize
                + halfHSegmentLength + (int) (part * halfHSegmentLength), 0);
            return;
        }
        g.drawLine(arcSize + halfHSegmentLength, 0, arcSize + hSegmentLength, 0);
        part -= halfHSegmentPart;

        if (part <= arcPart)
        {
            part *= arcPart;
            g.drawArc(hSegmentLength,
                      0,
                      arcDiameter,
                      arcDiameter,
                      QUARTER_TURN,
                      (int) (-QUARTER_TURN * part));
            return;
        }
        g.drawArc(hSegmentLength,
                  0,
                  arcDiameter,
                  arcDiameter,
                  QUARTER_TURN,
                  -QUARTER_TURN);
        part -= arcPart;

        if (part <= vSegmentPart)
        {
            part *= vSegmentPart;
            g.drawLine(hSegmentLength + arcDiameter, arcSize, hSegmentLength
                + arcDiameter, arcSize + (int) (part * vSegmentLength));
            return;
        }
        g.drawLine(hSegmentLength + arcDiameter, arcSize, hSegmentLength
            + arcDiameter, arcSize + vSegmentLength);
        part -= vSegmentPart;

        if (part <= arcPart)
        {
            part *= arcPart;
            g.drawArc(hSegmentLength,
                      vSegmentLength,
                      arcDiameter,
                      arcDiameter,
                      0,
                      -(int) (QUARTER_TURN * part));
            return;
        }
        g.drawArc(hSegmentLength,
                  vSegmentLength,
                  arcDiameter,
                  arcDiameter,
                  0,
                  -QUARTER_TURN);
        part -= arcPart;

        if (part <= hSegmentPart)
        {
            part *= hSegmentPart;
            g.drawLine(arcSize + hSegmentLength,
                       arcDiameter + vSegmentLength,
                       arcSize + hSegmentLength - (int) (part * hSegmentLength),
                       arcDiameter + vSegmentLength);
            return;
        }
        g.drawLine(arcSize + hSegmentLength,
                   arcDiameter + vSegmentLength,
                   arcSize,
                   arcDiameter + vSegmentLength);
        part -= hSegmentPart;

        if (part <= arcPart)
        {
            part *= arcPart;
            g.drawArc(0,
                      vSegmentLength,
                      arcDiameter,
                      arcDiameter,
                      -QUARTER_TURN,
                      -(int) (QUARTER_TURN * part));
            return;
        }
        g.drawArc(0,
                  vSegmentLength,
                  arcDiameter,
                  arcDiameter,
                  -QUARTER_TURN,
                  -QUARTER_TURN);
        part -= arcPart;

        if (part <= vSegmentPart)
        {
            part *= vSegmentPart;
            g.drawLine(0, arcSize + vSegmentLength, 0, arcSize + vSegmentLength
                - (int) (part * vSegmentLength));
            return;
        }
        g.drawLine(0, arcSize + vSegmentLength, 0, arcSize);
        part -= vSegmentPart;

        if (part <= arcPart)
        {
            part *= arcPart;
            g.drawArc(0,
                      0,
                      arcDiameter,
                      arcDiameter,
                      -HALF_TURN,
                      -(int) (QUARTER_TURN * part));
            return;
        }
        g.drawArc(0, 0, arcDiameter, arcDiameter, -HALF_TURN, -QUARTER_TURN);
        part -= arcPart;

        if (part <= halfHSegmentPart)
        {
            part *= halfHSegmentPart;
            g.drawLine(arcSize,
                       0,
                       arcSize + (int) (part * halfHSegmentLength),
                       0);
            return;
        }
        g.drawLine(arcSize, 0, halfHSegmentLength, 0);
        part -= halfHSegmentPart;
    }
}
