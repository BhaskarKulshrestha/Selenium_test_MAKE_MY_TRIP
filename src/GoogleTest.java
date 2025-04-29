import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GoogleTest {
    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "/Users/I528989/Downloads/chromedriver-mac-x64/chromedriver");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Open YouTube
            driver.get("https://www.youtube.com/");
            driver.manage().window().maximize();

            // Accept cookies (if prompted)
            try {
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Accept')]")));
                acceptButton.click();
            } catch (Exception e) {
                // No cookie prompt — continue
            }

            // Locate search bar and type query
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search_query")));
            searchBox.sendKeys("ladakh bike trip");
            searchBox.sendKeys(Keys.ENTER);

            // Wait for results and click the first video
            WebElement firstVideo = wait.until(ExpectedConditions.elementToBeClickable(By.id("video-title")));
            firstVideo.click();

            // Let the video play for a while
            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close browser
            driver.quit();
        }
    }
}
