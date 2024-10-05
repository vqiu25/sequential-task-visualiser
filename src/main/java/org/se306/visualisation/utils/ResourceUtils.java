package org.se306.visualisation.utils;

import org.se306.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class ResourceUtils {
  private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

  /**
   * Load a resource (e.g. image, css) and retrive its contents as a string. Urls
   * are relative to
   * the resources directory (e.g. "css/globals.css").
   *
   * @param url the url of the resource relative to the resources directory
   * @return the resource as a string
   */
  private static String loadAsString(String url) {
    return App.class.getResource(url).toExternalForm();
  }

  /**
   * Load a CSS file and return it. Urls are relative to the css directory (e.g.
   * globals.css).
   *
   * @param url the url of the css file relative to the css directory
   * @return the css file as a string
   */
  public static String loadCss(String url) {
    return loadAsString("css/" + url);
  }

  /**
   * Load an image and return it. Urls are relative to the images directory (e.g.
   * logo.png).
   *
   * @param url the url of the image file relative to the images directory
   * @return the image as an Image object
   */
  public static Image loadImage(String url) {
    return new Image(loadAsString("images/" + url));
  }

  /**
   * Load a font and register it with JavaFX. Urls are relative to the fonts
   * directory (e.g.
   * Montserrat-Medium.ttf).
   *
   * @param url the url of the font file relative to the fonts directory
   */
  public static void loadFont(String url) {
    Font.loadFont(loadAsString("fonts/" + url), 10);
  }
}
