package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class WinAppDriver {
    private WinAppDriver() {}
    public static void openApp(String executablePath, String remoteURI) throws InterruptedException {
        // Open cmd prompt
        try {
            // Command to run WinAppDriver.exe as an administrator
            // We use cmd.exe /c to execute the command and start cmd.exe /c to run the command in a new command prompt window
            String[] command = {"cmd.exe", "/c", "start", "cmd.exe", "/c", "\"" + executablePath + "\""};
            
            // Execute the command
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open application

        try {
            // Set up the WebDriver to use WinAppDriver with W3C syntax
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("ms:experimental-webdriver", true);
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");

            // Create a new instance of the RemoteWebDriver using the builder pattern
            WebDriver driver = new RemoteWebDriver(new URI(remoteURI).toURL(), capabilities);

            // Interact with the application
            WebElement edit = driver.findElement(By.className("Edit")); // Example: find the edit field by class name
            edit.sendKeys("Hello, WinAppDriver!");

            // Click the "Close" button to trigger the "Save As" dialog
            WebElement closeButton = driver.findElement(By.xpath("//Button[@Name='Close']"));
            closeButton.click();

            // Wait for the "Save As" dialog to appear
            WebElement saveAsDialog = waitForElement(driver, By.xpath("//Window[@Name='Notepad']"), 10);

            // Interact with the "Save As" dialog
            // For example, find the "Save" button and click it
            WebElement saveButton = saveAsDialog.findElement(By.xpath(".//Button[@Name='Save']"));
            saveButton.click();

            WebElement saveDialog= waitForElement(driver, By.xpath("//Window[@Name='Save As']"), 10);


            WebElement fileNameEditElement = driver.findElement(By.xpath("//Edit[@Name='File name:']"));

            fileNameEditElement.sendKeys("WinAppDriverTxtFile" + ".txt");

            WebElement taskManSaveButton = saveDialog.findElement(By.xpath(".//Button[@Name='Save']"));
            taskManSaveButton.click();

            // Close the application
            driver.quit();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException("Error while waiting for element.");
        }
    }

    public static WebElement waitForElement(WebDriver driver, By locator, int timeoutInSeconds) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeoutInSeconds * 1000L;
        while (System.currentTimeMillis() < endTime) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Element not found, continue waiting
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500); // Wait for 500 milliseconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new InterruptedException("Error while waiting for element.");
            }
        }
        throw new InterruptedException("Element not found after " + timeoutInSeconds + " seconds (or smth like that).");}
}
