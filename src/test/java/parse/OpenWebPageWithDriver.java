package parse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class OpenWebPageWithDriver {
    public static void main(String[] args) throws IOException {
        Desktop.getDesktop().browse(URI.create("https://imgur.com/a/kBPQWWd"));
    }
}
