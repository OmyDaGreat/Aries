package util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OpenPage {
    public static void open(String page) throws IOException {
        Desktop.getDesktop().browse(URI.create(page));
    }
    public static void open(URI page) throws IOException {
        Desktop.getDesktop().browse(page);
    }
}
