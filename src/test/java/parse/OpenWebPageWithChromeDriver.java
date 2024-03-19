package parse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OpenWebPageWithChromeDriver {
    public static void main(String[] args) {
        // Set the path to the Edge WebDriver executable
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chrome/chromedriver.exe");
        
        // Create a new instance of the Edge driver
        WebDriver driver = new ChromeDriver();
        
        // Navigate to the desired web page
        driver.get("https://www.example.com");
    }
}
