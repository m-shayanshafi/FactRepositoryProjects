package net.sf.bloodball;

import net.sf.bloodball.model.Game;

public class SimpleMessageBuilder extends MessageBuilder {
  private String resourceKey;

  public SimpleMessageBuilder(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String buildMessage(Game game) {
    return getResourceString(resourceKey);
  }
}