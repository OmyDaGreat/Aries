package parse;

import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import lombok.Cleanup;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import util.WebDriverMaker;

@Log4j2
public class OpenWebPageWithDriver {
    public static void main(String[] args) {
        @Cleanup var scanner = new Scanner(System.in);
        logger.info("Enter your browser:");
        var browser = scanner.nextLine();
        logger.debug("User entered browser: {}", browser);

        WebDriver driver = new WebDriverMaker(browser).getDriver();
        
        driver.get("https://www.example.com");
    }
}
