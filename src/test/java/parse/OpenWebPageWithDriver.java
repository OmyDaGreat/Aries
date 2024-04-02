package parse;

import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import lombok.Cleanup;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;
import util.WebDriverMaker;

@Log4j2
public class OpenWebPageWithDriver {
    public static void main(String[] args) {
        @Cleanup var scanner = new Scanner(System.in);
        logger.info("Enter your os, arch, and browser:");
        var stuff = scanner.nextLine();
        
        WebDriver driver = new WebDriverMaker(stuff).getDriver();
        
        driver.get("https://www.example.com");
    }
}
