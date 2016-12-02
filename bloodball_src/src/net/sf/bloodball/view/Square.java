package net.sf.bloodball.view;

import java.awt.*;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import net.sf.bloodball.MessageBuilder;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.resources.ResourceHandler;
import net.sf.bloodball.resources.ResourceKeys;
import net.sf.bloodball.view.event.MouseHandler;

public class Square extends JButton {
  private Color color;
  private Image backgroundImage;

  private MouseHandler currentHandler;

  public Square(Color color, Image backgroundImage) {
    this.color = color;
    this.backgroundImage = backgroundImage;
    setBackground(color);
    setBorder(new EtchedBorder());
    setPreferredSize(new Dimension(33, 33));
    setMaximumSize(getPreferredSize());
    setMinimumSize(getPreferredSize());
    setFocusPainted(false);
  }

  public void setListener(MouseHandler mouseHandler) {
    removeMouseListener(currentHandler);
    removeActionListener(currentHandler);
    addMouseListener(mouseHandler);
    addActionListener(mouseHandler);
    currentHandler = mouseHandler;
  }

  public void update(Player player, Icon icon, boolean isActive) {
    setIcon(icon);
    adjustToolTipText(player);
    adjustSquareColor(player, isActive);
  }

  public void update(Player player, boolean isActive) {
    update(player, new Icons().getIconForSquare(player, this), isActive);
  }

  protected void adjustSquareColor(Player player, boolean isActive) {
    if (isActive) {
      setBrightColor();
    } else if (player.hasActed()) {
      setDarkColor();
    } else {
      setDefaultColor();
    }
  }

  private void adjustToolTipText(Player player) {
    String text = player.getType().getDescription();
    if (player.isProne()) {
      text += "<font color=#FF0000>";
      text += MessageBuilder.buildMessage(ResourceKeys.PRONE_TOOL_TIP, player.getProneTurns());
      text += "</font>";
    }
    if (player.isInjured()) {
      text += "<font color=#FF0000>";
      text += ResourceHandler.getString(player.getHealth().toString());
      text += "</font>";
    }
    if (text != null) {
      text = "<html>" + text + "</html>";
    }
    setToolTipText(text);
  }

  protected void setBrightColor() {
    setBackground(color.brighter());
  }

  protected void setDarkColor() {
    setBackground(color.darker());
  }

  protected void setDefaultColor() {
    setBackground(color);
  }

  public Image getBackgroundImage() {
    return backgroundImage;
  }

}