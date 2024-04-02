package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class WinAppDriver {
    
    private WinAppDriver() {
        // throw an exception to prevent instantiation
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
    public static void openApp() {
        try {
            // Set up the WebDriver to use WinAppDriver with W3C syntax
            WebDriver driver = getWebDriver("http://127.0.0.1:4723");
            
            // Interact with the application
            WebElement edit = driver.findElement(By.className("Edit")); // Example: find the edit field by class name
            edit.sendKeys("Hello, WinAppDriver!");

            // Close the application
            driver.quit();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    private static WebDriver getWebDriver(String uri) throws MalformedURLException, URISyntaxException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("ms:experimental-webdriver", true);
        capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe"); //replace with arg capability later
        
        // Create a new instance of the RemoteWebDriver using the builder pattern
        return new RemoteWebDriver(new URI(uri).toURL(), capabilities);
    }
}