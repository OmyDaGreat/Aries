package util;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@UtilityClass
public class OpenPage {
    public static void open(String page) throws IOException {
        Desktop.getDesktop().browse(URI.create(page));
    }
    public static void open(URI page) throws IOException {
        Desktop.getDesktop().browse(page);
    }
}
