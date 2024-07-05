package util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import lombok.experimental.UtilityClass;

/**
 * Utility class for opening web pages in the system's default browser.
 * This class provides methods to open a web page from a URL string or a URI object.
 */
@UtilityClass
public class OpenPage {

  /**
   * Opens a web page in the system's default browser using a URL string.
   *
   * @param page The URL of the web page to open as a String.
   * @throws IOException If the default browser is not found, or it fails to be launched.
   */
  public static void open(String page) throws IOException {
    Desktop.getDesktop().browse(URI.create(page));
  }

  /**
   * Opens a web page in the system's default browser using a URI object.
   *
   * @param page The URI of the web page to open.
   * @throws IOException If the default browser is not found, or it fails to be launched.
   */
  public static void open(URI page) throws IOException {
    Desktop.getDesktop().browse(page);
  }
}