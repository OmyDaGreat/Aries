package util;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@Getter
public class WebDriverMaker {
  private final Platform platform;
  private final WebDriver driver;

  public WebDriverMaker(String stuff) {
    this.platform = Platform.detectPlatform(stuff);
    switch (platform) {
      case WINDOWS_X64_CHROME:
        System.setProperty(
            "webdriver.chrome.driver", "src/main/resources/chrome-win64/chromedriver.exe");
        this.driver = new ChromeDriver();
        break;
      case WINDOWS_X64_FIREFOX:
        System.setProperty("webdriver.gecko.driver", "src/main/resources/gecko/geckodriver.exe");
        this.driver = new FirefoxDriver();
        break;
      case WINDOWS_X64_EDGE:
        System.setProperty("webdriver.edge.driver", "src/main/resources/edge/msedgedriver.exe");
        this.driver = new EdgeDriver();
        break;
      case LINUX_X64_CHROME:
        System.setProperty(
            "webdriver.chrome.driver", "src/main/resources/chrome-linux/chromedriver");
        this.driver = new ChromeDriver();
        break;
        /*Not Yet Working:
        case LINUX_X64_FIREFOX:
            System.setProperty("webdriver.gecko.driver", "src/main/resources/gecko/geckodriver.exe");
            this.driver = new FirefoxDriver();
            break;
        case LINUX_X64_EDGE:
            System.setProperty("webdriver.edge.driver", "src/main/resources/edge/msedgedriver.exe");
            this.driver = new EdgeDriver();
            break;
        case MAC_CHROME:
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chrome-mac/chromedriver");
            this.driver = new ChromeDriver();
            break;
        case MAC_FIREFOX:
            System.setProperty("webdriver.gecko.driver", "src/main/resources/gecko/geckodriver.exe");
            this.driver = new FirefoxDriver();
            break;
        case MAC_EDGE:
            System.setProperty("webdriver.edge.driver", "src/main/resources/edge/msedgedriver.exe");
            this.driver = new EdgeDriver();
            break;*/
      case UNKNOWN:
      default:
        throw new UnsupportedBrowserException("Unsupported platform: " + platform.getDisplayName());
    }
  }
}
