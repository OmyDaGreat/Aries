package parse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class OpenWebPageWithGeckoDriver {
    public static void main(String[] args) {
        // Set the path to the GeckoDriver executable
        System.setProperty("webdriver.gecko.driver", "src/main/resources/gecko/geckodriver.exe");
        
        // Create a new instance of the Firefox driver
        WebDriver driver = new FirefoxDriver();
        
        // Navigate to the desired web page
        driver.get("https://www.example.com");
        
        // Close the browser
        driver.quit();
    }
}
