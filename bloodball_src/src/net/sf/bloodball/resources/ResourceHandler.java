package net.sf.bloodball.resources;

import java.util.*;

public class ResourceHandler {
  private final static String RESOURCE_BUNDLE = "net.sf.bloodball.resources.Resources";
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

  public static ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  public static String getString(String key) {
    return resourceBundle.getString(key);
  }

  public static void setLocale(Locale locale) {
    resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
  }
}