package parse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class OpenWebPageWithEdgeDriver {
    public static void main(String[] args) {
        // Set the path to the Edge WebDriver executable
        System.setProperty("webdriver.edge.driver", "src/main/resources/edge/msedgedriver.exe");
        
        // Create a new instance of the Edge driver
        WebDriver driver = new EdgeDriver();
        
        // Navigate to the desired web page
        driver.get("https://www.example.com");
    }
}
