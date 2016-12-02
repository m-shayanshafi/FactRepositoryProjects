package net.sf.bloodball.view;

import java.awt.*;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.sf.bloodball.ModelFacade;
import net.sf.bloodball.model.player.Player;

public class Icons {

  public static Image BALL_IMAGE = loadImage("icons/Ball.gif");
  public static Image GRAS_IMAGE = loadImage("icons/gras.gif");
  public static Icon BALL = new ImageIcon(BALL_IMAGE);

  private static Image loadImage(String imageString) {
    Image playerImage = null;
    try {
      playerImage = ImageIO.read(ClassLoader.getSystemResource(imageString));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return playerImage;
  }

  private static Image loadPlayerImage(Player player) {
    String iconString = player.getTeam().isHomeTeam() ? "Home" : "Guest";
    iconString += player.getType().toString();
    if (player.isProne() || player.isInjured()) {
      iconString += "Prone";
    } else if (player.inBallPossession()) {
      iconString += "WithBall";
    }
    return loadImage("icons/" + iconString + ".gif");
  }

  public static Icon getIconForSquare(Player player, Square square) {
    if (player == Player.NO_PLAYER) {
      if (square.getBackgroundImage() == null) {
        return null;
      }
      return new ImageIcon(square.getBackgroundImage());
    }
    BufferedImage combinedImage = new BufferedImage(BALL.getIconWidth(), BALL.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = combinedImage.createGraphics();
    if (square.getBackgroundImage() != null) {
      graphics.drawImage(square.getBackgroundImage(), 0, 0, null);
    }
    graphics.drawImage(loadPlayerImage(player), 0, 0, null);
    return new ImageIcon(combinedImage);
  }

  public static Icon getIconForSquareWithBall(Player player, Square square) {
    BufferedImage combinedImage = new BufferedImage(BALL.getIconWidth(), BALL.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = combinedImage.createGraphics();
    if (square.getBackgroundImage() != null) {
      graphics.drawImage(square.getBackgroundImage(), 0, 0, null);
    }
    graphics.drawImage(BALL_IMAGE, 0, 0, null);
    if (player != Player.NO_PLAYER) {
      graphics.drawImage(loadPlayerImage(player), 0, 0, null);
    }
    return new ImageIcon(combinedImage);
  }

}