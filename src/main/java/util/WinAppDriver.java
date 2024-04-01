package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class WinAppDriver {
    public static void main(String[] args) {
        try {
            // Set up the WebDriver to use WinAppDriver with W3C syntax
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("ms:experimental-webdriver", true);
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");

            // Create a new instance of the RemoteWebDriver using the builder pattern
            WebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723"), capabilities);

            // Interact with the application
            WebElement edit = driver.findElement(By.className("Edit")); // Example: find the edit field by class name
            edit.sendKeys("Hello, WinAppDriver!");

            // Close the application
            driver.quit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}