package org.se306.visualisation.controllers.shared;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColourMapping {
  private static final String[][] colours = {
    {"#F9BDF3", "#AC64A5"}, // Pink
    {"#BDDCF9", "#6484AC"}, // Light Blue
    {"#BDF9CD", "#76AC64"}, // Light Green
    {"#B6B9DF", "#6D6F9E"}, // Light Purple
    {"#F6DA86", "#EEA44F"} // Tan
  };

  private static final Map<String, String[]> cache = new ConcurrentHashMap<>();

  public static String[] getColours(String taskId) {
    return cache.computeIfAbsent(taskId, key -> colours[Math.abs(key.hashCode()) % colours.length]);
  }
}
