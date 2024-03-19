package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import lombok.Getter;

public class WebDriverMaker {
    @Getter private final String type;
    @Getter private final WebDriver driver;

    public WebDriverMaker(String type) {
        this.type = type;
        switch (type) {
            case "chrome-linux":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chrome-linux/chromedriver");
                this.driver = new ChromeDriver();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chrome-win64/chromedriver.exe");
                this.driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "src/main/resources/gecko/geckodriver.exe");
                this.driver = new FirefoxDriver();
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", "src/main/resources/edge/msedgedriver.exe");
                this.driver = new EdgeDriver();
                break;
            default:
                throw new UnsupportedBrowserException("Unsupported browser type: " + type);
        }
    }
}