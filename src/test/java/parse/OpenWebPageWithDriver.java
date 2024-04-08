package parse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import lombok.Cleanup;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;
import util.WebDriverMaker;

@Log4j2
public class OpenWebPageWithDriver {
    public static void main(String[] args) throws IOException {
        Desktop.getDesktop().browse(URI.create("https://imgur.com/a/kBPQWWd"));
    }
}
